package com.dentex.troller.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import com.dentex.troller.logging.DiagnosticLog
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * GATT client for the Haswing motor link.
 *
 * All GATT operations are serialized: a single writer coroutine drains the
 * outgoing queue and waits for the GATT write callback before issuing the next
 * chunk. Payload bytes are sliced into [WRITE_CHUNK_SIZE] chunks with a short
 * pause between them, matching the pacing observed in the Helmsman application.
 */
class AndroidBleTransport(
    private val context: Context,
    private val log: DiagnosticLog,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) : BleTransport {

    private val incoming = MutableSharedFlow<ByteArray>(extraBufferCapacity = 256)
    private val eventFlow = MutableSharedFlow<BleEvent>(extraBufferCapacity = 64)
    private val state = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)

    private val writeQueue = Channel<ByteArray>(capacity = WRITE_QUEUE_CAPACITY)
    private var writerJob: Job? = null

    private var gatt: BluetoothGatt? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null
    private var rxCharacteristic: BluetoothGattCharacteristic? = null
    private var pendingWrite: CompletableDeferred<Boolean>? = null

    override val incomingBytes: Flow<ByteArray> = incoming.asSharedFlow()
    override val events: SharedFlow<BleEvent> = eventFlow.asSharedFlow()
    override val connectionState: StateFlow<ConnectionState> = state.asStateFlow()

    @SuppressLint("MissingPermission")
    override suspend fun connect(deviceId: String) {
        disconnect()
        val device = bluetoothAdapter()?.getRemoteDevice(deviceId)
        if (device == null) {
            val reason = "Unknown Bluetooth device $deviceId"
            fail(reason)
            return
        }
        state.value = ConnectionState.Connecting
        log.event(TAG, "Connecting to $deviceId")
        gatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
    }

    override suspend fun write(bytes: ByteArray) {
        if (bytes.isEmpty()) return
        val result = writeQueue.trySend(bytes.copyOf())
        if (result.isFailure) {
            log.event(TAG, "Write queue is full, dropped ${bytes.size} bytes")
            eventFlow.tryEmit(BleEvent.WriteFailed("Write queue is full"))
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun disconnect() {
        writerJob?.cancel()
        writerJob = null
        // Drop everything that is still queued so that a command is never replayed after a
        // reconnect.
        while (writeQueue.tryReceive().isSuccess) {
            // Keep draining.
        }
        gatt?.let { client ->
            runCatching { client.disconnect() }
            runCatching { client.close() }
        }
        gatt = null
        txCharacteristic = null
        rxCharacteristic = null
        pendingWrite?.complete(false)
        pendingWrite = null
        if (state.value != ConnectionState.Disconnected) {
            state.value = ConnectionState.Disconnected
        }
    }

    private fun startWriter() {
        if (writerJob?.isActive == true) return
        writerJob = scope.launch {
            for (payload in writeQueue) {
                writeChunked(payload)
            }
        }
    }

    private suspend fun writeChunked(payload: ByteArray) {
        val characteristic = txCharacteristic
        val client = gatt
        if (characteristic == null || client == null) {
            log.event(TAG, "Dropping ${payload.size} byte payload: not connected")
            return
        }
        for (chunk in WriteChunker.split(payload)) {
            val ok = writeChunk(client, characteristic, chunk)
            log.tx(TAG, chunk)
            if (!ok) {
                eventFlow.tryEmit(BleEvent.WriteFailed("Write of ${chunk.size} bytes failed"))
                return
            }
        }
    }

    /**
     * Writes a single chunk.
     *
     * The motor's characteristic is written without a response, exactly like the Helmsman
     * application does, so no write callback is expected and the pacing delay is the only flow
     * control. Characteristics that do support a response are awaited instead.
     */
    @SuppressLint("MissingPermission")
    private suspend fun writeChunk(
        client: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        chunk: ByteArray,
    ): Boolean = withContext(Dispatchers.IO) {
        val writeType = characteristic.writeType
        if (writeType == BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE) {
            val accepted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                client.writeCharacteristic(characteristic, chunk, writeType) ==
                    BluetoothGatt.GATT_SUCCESS
            } else {
                @Suppress("DEPRECATION")
                characteristic.value = chunk
                @Suppress("DEPRECATION")
                client.writeCharacteristic(characteristic)
            }
            if (accepted) delay(WRITE_CHUNK_DELAY_MILLIS)
            return@withContext accepted
        }

        val deferred = CompletableDeferred<Boolean>()
        pendingWrite = deferred
        try {
            val accepted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                client.writeCharacteristic(characteristic, chunk, writeType) ==
                    BluetoothGatt.GATT_SUCCESS
            } else {
                @Suppress("DEPRECATION")
                characteristic.value = chunk
                @Suppress("DEPRECATION")
                client.writeCharacteristic(characteristic)
            }
            if (!accepted) {
                pendingWrite = null
                return@withContext false
            }
            deferred.await()
        } catch (_: Exception) {
            pendingWrite = null
            false
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    log.event(TAG, "GATT connected (status=$status)")
                    state.value = ConnectionState.Connected
                    eventFlow.tryEmit(BleEvent.Connected(gatt.device.address))
                    discoverServices(gatt)
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    log.event(TAG, "GATT disconnected (status=$status)")
                    txCharacteristic = null
                    rxCharacteristic = null
                    pendingWrite?.complete(false)
                    pendingWrite = null
                    state.value = ConnectionState.Disconnected
                    eventFlow.tryEmit(BleEvent.Disconnected("status=$status"))
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                fail("Service discovery failed with status $status")
                return
            }
            val service = gatt.getService(BleUuids.SERVICE)
            val tx = service?.getCharacteristic(BleUuids.TX_CHARACTERISTIC)
            val rx = service?.getCharacteristic(BleUuids.RX_CHARACTERISTIC)
            txCharacteristic = tx
            rxCharacteristic = rx
            // The Helmsman application writes without a response and paces the writes itself.
            tx?.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            log.event(
                TAG,
                "Services discovered: service=${service != null} tx=${tx != null} rx=${rx != null} " +
                    "services=${gatt.services.joinToString(",") { it.uuid.toString() }}",
            )
            eventFlow.tryEmit(
                BleEvent.ServiceDiscovered(
                    gatt.services.map { it.uuid.toString() },
                ),
            )
            if (tx == null || rx == null) {
                val found = gatt.services.joinToString(", ") { serviceInfo ->
                    val characteristics = serviceInfo.characteristics.joinToString("/") {
                        it.uuid.toString()
                    }
                    "${serviceInfo.uuid} [$characteristics]"
                }
                fail("The expected characteristics were not found (tx=${tx != null}, rx=${rx != null}). " +
                    "The remote exposes: $found")
                return
            }
            log.event(
                TAG,
                "tx properties=${tx.properties} rx properties=${rx.properties}",
            )
            state.value = ConnectionState.Ready(gatt.device.address, safeDeviceName(gatt.device))
            startWriter()
            enableNotifications(gatt, rx)
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int,
        ) {
            log.event(TAG, "CCCD write status=$status")
        }

        @SuppressLint("MissingPermission")
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int,
        ) {
            val ok = status == BluetoothGatt.GATT_SUCCESS
            if (!ok) {
                log.event(TAG, "Characteristic write failed with status $status")
            }
            pendingWrite?.complete(ok)
            pendingWrite = null
        }

        @SuppressLint("MissingPermission")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
        ) {
            log.rx(TAG, value)
            incoming.tryEmit(value)
        }

        @Deprecated("Deprecated in Java")
        @Suppress("DEPRECATION")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            @Suppress("DEPRECATION")
            val value = characteristic.value ?: return
            log.rx(TAG, value)
            incoming.tryEmit(value)
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        gatt.setCharacteristicNotification(characteristic, true)
        val cccd = characteristic.getDescriptor(BleUuids.CLIENT_CHARACTERISTIC_CONFIG)
        if (cccd == null) {
            log.event(TAG, "CCCD descriptor missing; notifications rely on setCharacteristicNotification")
            return
        }
        val value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt.writeDescriptor(cccd, value)
        } else {
            @Suppress("DEPRECATION")
            cccd.value = value
            @Suppress("DEPRECATION")
            gatt.writeDescriptor(cccd)
        }
    }

    @SuppressLint("MissingPermission")
    private fun discoverServices(gatt: BluetoothGatt) {
        if (!gatt.discoverServices()) {
            fail("Unable to start service discovery")
        }
    }

    @SuppressLint("MissingPermission")
    private fun safeDeviceName(device: BluetoothDevice): String? = try {
        device.name
    } catch (_: SecurityException) {
        null
    }

    @SuppressLint("MissingPermission")
    private fun bluetoothAdapter(): BluetoothAdapter? =
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter

    private fun fail(reason: String) {
        log.event(TAG, reason)
        state.value = ConnectionState.Failed(reason)
        eventFlow.tryEmit(BleEvent.Error(reason))
    }

    companion object {
        private const val TAG = "BLE"

        /** Maximum number of queued outbound messages, mirroring the Helmsman queue. */
        const val WRITE_QUEUE_CAPACITY = 20

        /** Bytes per GATT write. */
        const val WRITE_CHUNK_SIZE: Int = WriteChunker.CHUNK_SIZE

        /** Pause between consecutive chunks, matching the Helmsman pacing. */
        const val WRITE_CHUNK_DELAY_MILLIS = 15L
    }
}
