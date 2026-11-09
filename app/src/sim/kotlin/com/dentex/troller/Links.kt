package com.dentex.troller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.dentex.troller.ble.BleEvent
import com.dentex.troller.ble.BleScanResult
import com.dentex.troller.ble.BleTransport
import com.dentex.troller.ble.ConnectionState
import com.dentex.troller.ble.DeviceScanner
import com.dentex.troller.logging.DiagnosticLog
import com.dentex.troller.simulator.MotorSimulator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.util.Locale

/**
 * The sim build's link: a simulated remote and motor instead of Bluetooth. Nothing here can
 * reach a real remote, so no command can move a real boat.
 *
 * It is also controlled with adb, for example:
 * `adb shell am broadcast -a com.dentex.troller.SIM --es cmd gps --es value nofix`
 */
internal object Links {
    fun create(context: Context, log: DiagnosticLog, scope: CoroutineScope): Link {
        val transport = SimulatedLink(context.applicationContext, log, scope)
        return Link(transport, SimulatedScanner(transport), transport)
    }
}

/** The simulated remote: a [BleTransport] whose peer is a [MotorSimulator]. */
private class SimulatedLink(
    private val context: Context,
    private val log: DiagnosticLog,
    scope: CoroutineScope,
) : BleTransport, SimulatorControls {

    private val state = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    private val incoming = MutableSharedFlow<ByteArray>(extraBufferCapacity = 256)
    private val eventFlow = MutableSharedFlow<BleEvent>(extraBufferCapacity = 64)

    /** Until when the remote is out of range after `link lose`. */
    @Volatile
    var hiddenUntilMillis = 0L
        private set

    // The motor keeps running while the link is down, like the real one; only delivery stops.
    private val simulator = MotorSimulator(
        scope = scope,
        send = { bytes ->
            if (state.value is ConnectionState.Ready) {
                log.rx(TAG, bytes)
                incoming.emit(bytes)
            }
        },
        log = { log.event(TAG, it) },
    ).also { it.start() }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // "--es cmd gps --es value nofix", or all in one: "--es cmd 'gps nofix'".
            val raw = intent.getStringExtra("cmd")?.trim().orEmpty()
            val name = raw.substringBefore(' ')
            val value = intent.getStringExtra("value") ?: raw.substringAfter(' ', "").ifEmpty { null }
            resultData = this@SimulatedLink.command(name, value)
        }
    }

    init {
        ContextCompat.registerReceiver(context, receiver, IntentFilter(ACTION), ContextCompat.RECEIVER_EXPORTED)
        log.event(TAG, "Simulated remote and motor; control with adb broadcasts to $ACTION")
    }

    override val incomingBytes: Flow<ByteArray> = incoming.asSharedFlow()
    override val events: Flow<BleEvent> = eventFlow.asSharedFlow()
    override val connectionState: StateFlow<ConnectionState> = state.asStateFlow()

    override suspend fun connect(deviceId: String) {
        if (System.currentTimeMillis() < hiddenUntilMillis) {
            state.value = ConnectionState.Failed("The simulated remote is out of range")
            return
        }
        log.event(TAG, "Connecting to the simulated remote")
        state.value = ConnectionState.Connecting
        delay(400)
        state.value = ConnectionState.Connected
        delay(300)
        state.value = ConnectionState.Ready(deviceId, NAME)
        eventFlow.emit(BleEvent.Connected(deviceId))
    }

    override suspend fun write(bytes: ByteArray) {
        check(state.value is ConnectionState.Ready) { "Not connected" }
        log.tx(TAG, bytes)
        simulator.receive(bytes)
    }

    override suspend fun disconnect() {
        state.value = ConnectionState.Disconnected
    }

    override fun release() {
        state.value = ConnectionState.Disconnected
        simulator.stop()
        runCatching { context.unregisterReceiver(receiver) }
    }

    override fun command(command: String, value: String?): String {
        if (command.lowercase(Locale.US) != "link") return simulator.control(command, value)
        val parts = value?.split(',', ' ')?.filter { it.isNotBlank() }.orEmpty()
        val seconds = parts.getOrNull(1)?.toDoubleOrNull()
        val result = when (parts.firstOrNull()?.lowercase(Locale.US)) {
            // The remote stays connected but the motor goes quiet: the app's watchdog fires.
            "drop" -> {
                val quiet = seconds ?: 8.0
                simulator.silence(quiet)
                "Simulator: the motor is silent for ${quiet.toInt()} s"
            }
            // The remote disconnects and stays out of range: the app reconnects when it is back.
            "lose" -> {
                val away = seconds ?: 15.0
                hiddenUntilMillis = System.currentTimeMillis() + (away * 1000).toLong()
                state.value = ConnectionState.Disconnected
                eventFlow.tryEmit(BleEvent.Disconnected("Simulated link loss"))
                "Simulator: the remote is out of range for ${away.toInt()} s"
            }
            else -> "Error: link is drop|lose[,seconds]"
        }
        log.event(TAG, result)
        return result
    }

    companion object {
        const val ACTION = "com.dentex.troller.SIM"
        const val DEVICE_ID = "SIM:00:00:00:00:01"
        // Named like the real remote's module, so the connect list groups it the same way.
        const val NAME = "HM_SIM simulated remote"
        private const val TAG = "SIM"
    }
}

/** Finds the simulated remote, except while it is out of range. */
private class SimulatedScanner(private val link: SimulatedLink) : DeviceScanner {
    override val isBluetoothEnabled: Boolean = true

    override fun scan(filterByService: Boolean): Flow<BleScanResult> = flow {
        delay(600)
        while (true) {
            if (System.currentTimeMillis() >= link.hiddenUntilMillis) {
                emit(BleScanResult(SimulatedLink.DEVICE_ID, SimulatedLink.NAME, rssi = -45))
            }
            delay(1_500)
        }
    }
}
