package com.dentex.troller.ble

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Transport used to exchange raw bytes with the motor.
 *
 * Implementations are responsible for the BLE specifics; the MAVLink layer above
 * only sees an ordered byte stream.
 */
interface BleTransport {

    /** Raw bytes received from the peer, in arrival order. */
    val incomingBytes: Flow<ByteArray>

    /** Lifecycle and error events for the UI. */
    val events: Flow<BleEvent>

    /** Current connection state. */
    val connectionState: StateFlow<ConnectionState>

    /** Connects to the device identified by [deviceId] (a Bluetooth address). */
    suspend fun connect(deviceId: String)

    /** Queues [bytes] for delivery. Returns once the bytes are queued, not once sent. */
    suspend fun write(bytes: ByteArray)

    /** Disconnects and releases the GATT resources. */
    suspend fun disconnect()
}

/** State of the BLE link. */
sealed interface ConnectionState {
    data object Disconnected : ConnectionState
    data object Connecting : ConnectionState
    data object Connected : ConnectionState
    data class Ready(val deviceId: String, val deviceName: String?) : ConnectionState
    data class Failed(val reason: String) : ConnectionState
}

/** Events emitted by a [BleTransport]. */
sealed interface BleEvent {
    data class DeviceFound(val device: BleScanResult) : BleEvent
    data class Connected(val deviceId: String) : BleEvent
    data class Disconnected(val reason: String) : BleEvent
    data class ServiceDiscovered(val services: List<String>) : BleEvent
    data class WriteFailed(val reason: String) : BleEvent
    data class Error(val reason: String) : BleEvent
}

/** A device seen during a scan. */
data class BleScanResult(
    val deviceId: String,
    val deviceName: String?,
    val rssi: Int,
    val serviceUuids: List<String> = emptyList(),
)
