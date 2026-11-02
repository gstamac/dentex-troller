package com.dentex.troller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dentex.troller.ble.AndroidBleTransport
import com.dentex.troller.ble.BleEvent
import com.dentex.troller.ble.BleScanResult
import com.dentex.troller.ble.BleScanner
import com.dentex.troller.ble.ConnectionState
import com.dentex.troller.haswing.CommandGateState
import com.dentex.troller.haswing.CommandResult
import com.dentex.troller.haswing.HaswingMotorController
import com.dentex.troller.haswing.MotorIdentity
import com.dentex.troller.haswing.MotorMode
import com.dentex.troller.haswing.MotorTelemetry
import com.dentex.troller.haswing.MotorTransport
import com.dentex.troller.logging.DiagnosticLog
import com.dentex.troller.logging.LogEntry
import com.dentex.troller.logging.Redaction
import com.dentex.troller.mavlink.HelmsmanMavlinkCodec
import com.dentex.troller.mavlink.MavlinkMessage
import com.dentex.troller.navigation.MissionProgress
import com.dentex.troller.navigation.MissionTransfer
import com.dentex.troller.navigation.Route
import com.dentex.troller.navigation.Waypoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** State rendered by the UI. */
data class MainUiState(
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val devices: List<BleScanResult> = emptyList(),
    val scanning: Boolean = false,
    val bluetoothEnabled: Boolean = true,
    val identity: MotorIdentity? = null,
    val telemetry: MotorTelemetry = MotorTelemetry(),
    val commandGate: CommandGateState = CommandGateState.UNVERIFIED,
    val missionProgress: MissionProgress = MissionProgress(MissionProgress.Phase.IDLE),
    val downloadedRoute: Route? = null,
    val notice: String? = null,
)

/**
 * Wires the BLE transport, the MAVLink decoder, the motor controller and the mission state
 * machine together.
 *
 * The view model is the only place that knows about the whole stack, so the UI stays a pure
 * rendering of [uiState].
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    /** Diagnostic log; also used to build the export that can be compared with Helmsman. */
    val log: DiagnosticLog = DiagnosticLog()

    private val transport = AndroidBleTransport(application, log, viewModelScope)
    private val scanner = BleScanner(application)
    private val decoder = HelmsmanMavlinkCodec()

    /** Motor controller; exposes the identity, the telemetry and the command gate. */
    val motor = HaswingMotorController(
        transport = MotorTransport { bytes -> transport.write(bytes) },
        scope = viewModelScope,
    )

    /** Route transfer state machine. */
    val mission = MissionTransfer(
        scope = viewModelScope,
        sendMessage = { message -> reportFailure(motor.send(message)) },
        requestRouteList = { reportFailure(motor.requestRouteDownload()) },
    )

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Retained log records for the diagnostics screen.
     *
     * Kept out of [uiState] and sampled, so that BLE traffic does not redraw every screen for
     * every notification, and only copied while the diagnostics screen is visible.
     */
    @OptIn(FlowPreview::class)
    val logEntries: StateFlow<List<LogEntry>> = log.changes
        .sample(LOG_REFRESH_MILLIS)
        .onStart { emit(log.changes.value) }
        .map { log.entries }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private var scanJob: Job? = null

    init {
        viewModelScope.launch {
            transport.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state) }
                when (state) {
                    is ConnectionState.Ready -> motor.onConnected()
                    is ConnectionState.Disconnected, is ConnectionState.Failed -> {
                        motor.onDisconnected()
                        // Stop a running transfer; keep the outcome of a finished one visible.
                        val phase = mission.progress.value.phase
                        if (phase == MissionProgress.Phase.UPLOADING ||
                            phase == MissionProgress.Phase.DOWNLOADING
                        ) {
                            mission.cancel()
                        }
                    }
                    else -> Unit
                }
            }
        }
        viewModelScope.launch {
            transport.events.collect { event ->
                when (event) {
                    is BleEvent.Error -> notifyUser(event.reason)
                    is BleEvent.Disconnected -> notifyUser("Disconnected: ${event.reason}")
                    else -> Unit
                }
            }
        }
        viewModelScope.launch {
            transport.incomingBytes.collect { bytes ->
                decoder.consume(bytes).forEach { frame ->
                    val message = MavlinkMessage.decode(frame) ?: return@forEach
                    motor.onMessage(message, frame.systemId, frame.componentId)
                    mission.onMessage(message)
                }
            }
        }
        viewModelScope.launch {
            motor.identity.collect { identity ->
                _uiState.update { it.copy(identity = identity) }
            }
        }
        viewModelScope.launch {
            motor.telemetry.collect { telemetry ->
                _uiState.update { it.copy(telemetry = telemetry) }
            }
        }
        viewModelScope.launch {
            motor.commandGate.collect { gate ->
                _uiState.update { it.copy(commandGate = gate) }
            }
        }
        viewModelScope.launch {
            mission.progress.collect { progress ->
                _uiState.update {
                    it.copy(
                        missionProgress = progress,
                        downloadedRoute = mission.downloadedRoute,
                    )
                }
            }
        }
    }

    /**
     * Starts a scan for nearby devices.
     *
     * A running scan is always replaced, so that the operator can switch the filter or retry
     * without having to leave the screen first.
     */
    fun startScan(filterByService: Boolean = false) {
        if (!scanner.isBluetoothEnabled) {
            notifyUser("Bluetooth is switched off on the phone")
            _uiState.update { it.copy(bluetoothEnabled = false) }
            return
        }
        _uiState.update { it.copy(bluetoothEnabled = true) }
        scanJob?.cancel()
        scanJob = null
        _uiState.update { it.copy(scanning = true, devices = emptyList()) }
        scanJob = viewModelScope.launch {
            try {
                scanner.scan(filterByService).collect { result ->
                    log.event(
                        "SCAN",
                        "${result.deviceName ?: "(no name)"} ${result.deviceId} " +
                            "${result.rssi} dBm " +
                            "services=${result.serviceUuids.joinToString(",").ifEmpty { "none" }}",
                    )
                    _uiState.update { state ->
                        val existing = state.devices.indexOfFirst { it.deviceId == result.deviceId }
                        val enriched = result.copy(deviceName = result.deviceName ?: result.deviceId)
                        if (existing < 0) {
                            state.copy(devices = state.devices + enriched)
                        } else {
                            // The modern and the legacy scanner both report the same device.
                            // Keep the richest information that either of them provided.
                            val known = state.devices[existing]
                            val merged = known.copy(
                                deviceName = known.deviceName ?: enriched.deviceName,
                                rssi = maxOf(known.rssi, enriched.rssi),
                                serviceUuids = known.serviceUuids.ifEmpty { enriched.serviceUuids },
                            )
                            state.copy(devices = state.devices.toMutableList().also { it[existing] = merged })
                        }
                    }
                }
            } catch (error: Exception) {
                notifyUser(error.message ?: "Scan failed")
            } finally {
                _uiState.update { it.copy(scanning = false) }
            }
        }
    }

    /** Stops the scan. */
    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        _uiState.update { it.copy(scanning = false) }
    }

    /** Connects to the motor with the given Bluetooth address. */
    fun connect(deviceId: String) {
        stopScan()
        viewModelScope.launch {
            transport.connect(deviceId)
        }
    }

    /** Disconnects from the motor. */
    fun disconnect() {
        viewModelScope.launch {
            transport.disconnect()
        }
    }

    /** Disables propulsion. This is the emergency stop. */
    fun requestStop() {
        runCommand { motor.requestStop() }
    }

    /** Enables propulsion. */
    fun requestMotorOn() {
        runCommand { motor.requestMotorOn() }
    }

    /** Increases the speed by one step. */
    fun requestSpeedUp() {
        runCommand { motor.requestSpeedUp() }
    }

    /** Decreases the speed by one step. */
    fun requestSpeedDown() {
        runCommand { motor.requestSpeedDown() }
    }

    /** Sets the motor to full speed. */
    fun requestFullSpeed() {
        runCommand { motor.requestFullSpeed() }
    }

    /** Steers to the left. */
    fun requestTurnLeft() {
        runCommand { motor.requestTurnLeft() }
    }

    /** Steers to the right. */
    fun requestTurnRight() {
        runCommand { motor.requestTurnRight() }
    }

    /** Switches the motor to a different operating mode. */
    fun requestMode(mode: MotorMode) {
        runCommand { motor.requestMode(mode) }
    }

    /** Uploads a route to the motor. Uploading never starts propulsion. */
    fun uploadRoute(waypoints: List<Waypoint>) {
        val route = Route(waypoints)
        if (!mission.startUpload(route)) {
            notifyUser("The route is empty")
        }
    }

    /** Downloads the route that is stored on the motor. */
    fun downloadRoute() {
        mission.startDownload()
    }

    /** Cancels a running route transfer. */
    fun cancelMission() {
        mission.cancel()
    }

    /** Returns the diagnostic log as a text report, with device addresses masked. */
    fun exportLog(): String = log.exportText(Redaction::redactEntry)

    /** Clears the diagnostic log. */
    fun clearLog() {
        log.clear()
    }

    /** Dismisses the current notice. */
    fun clearNotice() {
        _uiState.update { it.copy(notice = null) }
    }

    private fun runCommand(block: suspend () -> CommandResult) {
        viewModelScope.launch {
            when (val result = block()) {
                is CommandResult.Sent -> Unit
                is CommandResult.Rejected -> notifyUser(result.reason)
                is CommandResult.Failed -> notifyUser(result.reason)
            }
        }
    }

    private fun notifyUser(message: String) {
        _uiState.update { it.copy(notice = message) }
    }

    /** Converts a send result for the route transfer, telling the operator about failures. */
    private fun reportFailure(result: CommandResult): Boolean = when (result) {
        is CommandResult.Sent -> true
        is CommandResult.Rejected -> false.also { notifyUser(result.reason) }
        is CommandResult.Failed -> false.also { notifyUser(result.reason) }
    }

    override fun onCleared() {
        scanJob?.cancel()
        // viewModelScope is already cancelled here, so the link is released synchronously.
        motor.onDisconnected()
        transport.release()
        super.onCleared()
    }

    private companion object {
        /** How often the diagnostics screen picks up new log records. */
        const val LOG_REFRESH_MILLIS = 250L
    }
}
