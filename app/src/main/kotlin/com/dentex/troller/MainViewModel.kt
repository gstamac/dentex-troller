package com.dentex.troller

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dentex.troller.ble.BleEvent
import com.dentex.troller.ble.BleScanResult
import com.dentex.troller.ble.ConnectionState
import com.dentex.troller.chart.ChartKind
import com.dentex.troller.chart.ChartLayer
import com.dentex.troller.chart.ChartStore
import com.dentex.troller.haswing.CommandGateState
import com.dentex.troller.haswing.CommandResult
import com.dentex.troller.haswing.HaswingMotorController
import com.dentex.troller.haswing.MotorIdentity
import com.dentex.troller.haswing.MotorMode
import com.dentex.troller.haswing.MotorTelemetry
import com.dentex.troller.haswing.MotorTransport
import com.dentex.troller.haswing.ReportedMode
import com.dentex.troller.logging.DiagnosticLog
import com.dentex.troller.logging.LogEntry
import com.dentex.troller.logging.Redaction
import com.dentex.troller.mavlink.HelmsmanMavlinkCodec
import com.dentex.troller.mavlink.MavlinkMessage
import com.dentex.troller.navigation.GoToState
import com.dentex.troller.navigation.GoToTarget
import com.dentex.troller.navigation.MissionProgress
import com.dentex.troller.navigation.MissionTransfer
import com.dentex.troller.navigation.Route
import com.dentex.troller.navigation.Gpx
import com.dentex.troller.navigation.PinLibrary
import com.dentex.troller.navigation.RouteLibrary
import com.dentex.troller.navigation.SavedRoute
import com.dentex.troller.navigation.Waypoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale
import java.util.UUID

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
    /** Name of the remote that the running scan reconnects to when it shows up. */
    val autoConnectTo: String? = null,
    /** True from a lost link (or the app start) until the last remote is connected again. */
    val reconnecting: Boolean = false,
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

    // Bluetooth, or in the sim build a simulated remote and motor.
    private val link = Links.create(application, log, viewModelScope)
    private val transport = link.transport
    private val scanner = link.scanner

    /** The simulated motor in the sim build, null otherwise. */
    val simulator: SimulatorControls? = link.simulator
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
     * Moves the boat to a position picked on the chart: a one-waypoint route, then navigation
     * mode once. Each step goes through the command gate of [motor].
     */
    private val goTo = GoToTarget(
        scope = viewModelScope,
        startUpload = { route -> mission.startUpload(route) },
        uploadProgress = mission.progress,
        cancelUpload = { mission.cancel() },
        sendNavigation = { reportFailure(motor.requestMode(MotorMode.NAVIGATION)) },
        sendManual = { reportFailure(motor.requestMode(MotorMode.MANUAL)) },
        sendStop = { reportFailure(motor.requestStop()) },
        sendAnchor = { reportFailure(motor.requestMode(MotorMode.ANCHOR)) },
        sendSpeedUp = { reportFailure(motor.requestSpeedUp()) },
        navigating = motor.telemetry
            .map { telemetry -> telemetry.reportedMode?.let { it == ReportedMode.NAVIGATION } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null),
        motorSpeed = motor.telemetry
            .map { it.motorSpeed }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null),
        anchored = { motor.telemetry.value.reportedMode == ReportedMode.ANCHOR },
    )

    /** The go-to that is starting or running, or how the last one ended. */
    val goToState: StateFlow<GoToState> = goTo.state

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

    private val chartStore = ChartStore(application)
    private val _chartFile = MutableStateFlow(chartStore.current(ChartKind.DEPTHS))

    /** The installed offline chart, or `null` when none is installed yet. */
    val chartFile: StateFlow<ChartStore.InstalledChart?> = _chartFile.asStateFlow()

    private val _rainbowFile = MutableStateFlow(chartStore.current(ChartKind.RAINBOW))

    /** The installed rainbow depth layer, or `null` when none is installed. */
    val rainbowFile: StateFlow<ChartStore.InstalledChart?> = _rainbowFile.asStateFlow()

    private val chartSettings = application.getSharedPreferences(CHART_SETTINGS, android.content.Context.MODE_PRIVATE)
    private val _chartLayer = MutableStateFlow(
        chartSettings.getString(KEY_CHART_LAYER, null)
            ?.let { saved -> ChartLayer.entries.firstOrNull { it.name == saved } }
            // Before the layers menu, the rainbow layer was a switch that defaulted to on.
            ?: if (chartSettings.getBoolean(KEY_SHOW_RAINBOW, true)) ChartLayer.RAINBOW else ChartLayer.DEFAULT,
    )

    /** The layer picked in the chart's layers menu. Kept across restarts. */
    val chartLayer: StateFlow<ChartLayer> = _chartLayer.asStateFlow()

    private val routeStore = TextStore(application, "routes.txt", RouteLibrary(), RouteLibrary::decode, RouteLibrary::encode)

    // One writer at a time, in order, so an older library never overwrites a newer one.
    private val routeSaving = Dispatchers.IO.limitedParallelism(1)
    private val _routes = MutableStateFlow(routeStore.load())

    /** Every route kept on the phone and the selected one. Saved after every change. */
    val routes: StateFlow<RouteLibrary> = _routes.asStateFlow()

    private val pinStore = TextStore(application, "pins.txt", PinLibrary(), PinLibrary::decode, PinLibrary::encode)
    private val _pins = MutableStateFlow(pinStore.load())

    /** Marked spots, imported from GPX. Saved after every change. */
    val pins: StateFlow<PinLibrary> = _pins.asStateFlow()

    private val _showPins = MutableStateFlow(chartSettings.getBoolean(KEY_SHOW_PINS, true))

    /** Whether the chart draws the pins. Kept across restarts. */
    val showPins: StateFlow<Boolean> = _showPins.asStateFlow()

    /**
     * The waypoints of the selected route, shared by the route screen and the chart.
     *
     * Editing it never talks to the motor; only [uploadRoute] does.
     */
    val routeDraft: StateFlow<List<Waypoint>> = _routes
        .map { it.selected?.waypoints ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, _routes.value.selected?.waypoints ?: emptyList())

    private var scanJob: Job? = null

    private val connectionSettings =
        // The sim build remembers its simulated remote separately, so the real one is kept.
        application.getSharedPreferences(
            if (BuildConfig.SIMULATOR) "$CONNECTION_SETTINGS-sim" else CONNECTION_SETTINGS,
            android.content.Context.MODE_PRIVATE,
        )
    private var autoConnectTarget: String? = null
    private var autoConnectFound = false
    private var reconnectJob: Job? = null

    // After Disconnect the operator chose to stay offline; only a manual connect re-arms auto-connect.
    private var userDisconnected = false

    init {
        viewModelScope.launch {
            var linkWasUp = false
            transport.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state) }
                when (state) {
                    is ConnectionState.Ready -> {
                        connectionSettings.edit()
                            .putString(KEY_LAST_DEVICE, state.deviceId)
                            .putString(KEY_LAST_DEVICE_NAME, state.deviceName)
                            .apply()
                        _uiState.update { it.copy(reconnecting = false) }
                        motor.onConnected()
                    }
                    is ConnectionState.Disconnected, is ConnectionState.Failed -> {
                        motor.onDisconnected()
                        // Before the transfer is cancelled, so the go-to reports the lost link.
                        goTo.onLinkLost()
                        // Stop a running transfer; keep the outcome of a finished one visible.
                        val phase = mission.progress.value.phase
                        if (phase == MissionProgress.Phase.UPLOADING ||
                            phase == MissionProgress.Phase.DOWNLOADING
                        ) {
                            mission.cancel()
                        }
                        // A lost or failed link (also a failed reconnect attempt) starts over,
                        // unless the operator disconnected.
                        if (linkWasUp) autoConnect()
                    }
                    else -> Unit
                }
                linkWasUp = state !is ConnectionState.Disconnected && state !is ConnectionState.Failed
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
                if (gate == CommandGateState.LINK_LOST) goTo.onLinkLost()
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
     * without having to leave the screen first. A manual scan never connects on its own.
     */
    fun startScan(filterByService: Boolean = false) {
        cancelReconnect()
        runScan(filterByService, target = null, targetName = null, durationMillis = null)
    }

    /**
     * Reconnects to the remote that was connected last: scans for it, connects when it shows up,
     * and keeps retrying every [RECONNECT_PAUSE_MILLIS] until a link is open. Without a known
     * remote it runs one plain scan instead. Does nothing while a link exists or is being opened,
     * or after the operator disconnected. Connecting sends no motor command, and the transport
     * drops queued writes with the old link, so nothing is replayed.
     */
    fun autoConnect() {
        if (userDisconnected || reconnectJob?.isActive == true || !linkDown()) return
        val last = connectionSettings.getString(KEY_LAST_DEVICE, null)
        if (last == null) {
            if (!_uiState.value.scanning) runScan(false, target = null, targetName = null, durationMillis = AUTO_SCAN_MILLIS)
            return
        }
        val name = connectionSettings.getString(KEY_LAST_DEVICE_NAME, null) ?: last
        _uiState.update { it.copy(reconnecting = true) }
        reconnectJob = viewModelScope.launch {
            while (linkDown()) {
                autoConnectFound = false
                if (scanner.isBluetoothEnabled) {
                    runScan(filterByService = false, target = last, targetName = name, durationMillis = AUTO_SCAN_MILLIS)
                    scanJob?.join()
                } else {
                    _uiState.update { it.copy(bluetoothEnabled = false) }
                }
                if (autoConnectFound) break
                delay(RECONNECT_PAUSE_MILLIS)
            }
        }
    }

    private fun linkDown(): Boolean {
        val link = _uiState.value.connectionState
        return link is ConnectionState.Disconnected || link is ConnectionState.Failed
    }

    /** The operator took over: stops retrying, and stops a scan that would connect on its own. */
    private fun cancelReconnect() {
        reconnectJob?.cancel()
        reconnectJob = null
        if (autoConnectTarget != null) stopScan()
        _uiState.update { it.copy(reconnecting = false) }
    }

    private fun runScan(filterByService: Boolean, target: String?, targetName: String?, durationMillis: Long?) {
        if (!scanner.isBluetoothEnabled) {
            notifyUser("Bluetooth is switched off on the phone")
            _uiState.update { it.copy(bluetoothEnabled = false) }
            return
        }
        _uiState.update { it.copy(bluetoothEnabled = true) }
        scanJob?.cancel()
        autoConnectTarget = target
        _uiState.update { it.copy(scanning = true, devices = emptyList(), autoConnectTo = target?.let { targetName }) }
        lateinit var job: Job
        job = viewModelScope.launch {
            try {
                val scan = scanner.scan(filterByService)
                if (durationMillis == null) {
                    scan.collect(::onScanResult)
                } else {
                    withTimeoutOrNull(durationMillis) { scan.collect(::onScanResult) }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                notifyUser(error.message ?: "Scan failed")
            } finally {
                // A replaced scan finishes after its successor started; leave the new one's state alone.
                if (scanJob === job) {
                    scanJob = null
                    autoConnectTarget = null
                    _uiState.update { it.copy(scanning = false, autoConnectTo = null) }
                }
            }
        }
        scanJob = job
    }

    private fun onScanResult(result: BleScanResult) {
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
        if (result.deviceId == autoConnectTarget) {
            autoConnectTarget = null
            log.event("SCAN", "found the last remote ${result.deviceId}, connecting")
            autoConnectFound = true
            openLink(result.deviceId)
        }
    }

    /** Stops the scan. */
    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        autoConnectTarget = null
        _uiState.update { it.copy(scanning = false, autoConnectTo = null) }
    }

    /** Connects to the motor with the given Bluetooth address. */
    fun connect(deviceId: String) {
        userDisconnected = false
        cancelReconnect()
        openLink(deviceId)
    }

    private fun openLink(deviceId: String) {
        stopScan()
        viewModelScope.launch {
            transport.connect(deviceId)
        }
    }

    /** Disconnects from the motor. Auto-connect stays off until the next manual connect. */
    fun disconnect() {
        userDisconnected = true
        cancelReconnect()
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

    /**
     * Moves the boat to [target]: uploads it as the motor's route and switches the motor to
     * navigation mode. The chart asks for a confirmation first.
     */
    fun goTo(target: Waypoint) {
        goTo.start(target)?.let(::notifyUser)
    }

    /** Ends the go-to; sends manual mode when navigation mode was sent. */
    fun cancelGoTo() {
        goTo.cancel()
    }

    /** Switches the motor from navigation to anchor mode, holding the boat where it is. */
    fun anchorGoTo() {
        goTo.anchor()
    }

    /** Clears an ended or failed go-to from the chart. */
    fun dismissGoTo() {
        goTo.dismiss()
    }

    /** Uploads a route to the motor. Uploading never starts propulsion. */
    fun uploadRoute(waypoints: List<Waypoint>) {
        if (goTo.state.value.active) {
            notifyUser("A go-to is running. Cancel it before uploading a route.")
            return
        }
        val route = Route(waypoints)
        if (!mission.startUpload(route)) {
            notifyUser("The route is empty")
        }
    }

    /** Appends a waypoint to the selected route, starting a new route when none is selected. */
    fun addWaypoint(waypoint: Waypoint) {
        editRoutes { library ->
            val target = if (library.selected == null) library.create(newRouteId(), System.currentTimeMillis()) else library
            target.editSelected { it + waypoint }
        }
    }

    /** Removes the last waypoint of the selected route. */
    fun removeLastWaypoint() {
        editRoutes { library -> library.editSelected { it.dropLast(1) } }
    }

    /** Inserts a waypoint into the selected route so that it becomes number [index] + 1. */
    fun insertWaypoint(index: Int, waypoint: Waypoint) {
        editRoutes { library ->
            library.editSelected { points -> points.toMutableList().apply { add(index.coerceIn(0, size), waypoint) } }
        }
    }

    /** Moves one waypoint of the selected route. */
    fun moveWaypoint(index: Int, waypoint: Waypoint) {
        editRoutes { library ->
            library.editSelected { points -> points.mapIndexed { i, old -> if (i == index) waypoint else old } }
        }
    }

    /** Removes one waypoint of the selected route. */
    fun removeWaypoint(index: Int) {
        editRoutes { library -> library.editSelected { points -> points.filterIndexed { i, _ -> i != index } } }
    }

    /** Clears the selected route's waypoints. */
    fun clearRoute() {
        editRoutes { library -> library.editSelected { emptyList() } }
    }

    /** Starts a new, empty route and selects it. */
    fun newRoute() {
        editRoutes { library -> library.create(newRouteId(), System.currentTimeMillis()) }
    }

    fun selectRoute(id: String?) {
        editRoutes { library -> library.select(id) }
    }

    fun renameRoute(id: String, name: String) {
        editRoutes { library -> library.rename(id, name) }
    }

    fun deleteRoute(id: String) {
        editRoutes { library -> library.delete(id) }
    }

    /** Keeps the route last read from the motor as a new route on the phone. */
    fun saveDownloadedRoute() {
        val route = _uiState.value.downloadedRoute ?: return
        val now = System.currentTimeMillis()
        val name = "From motor " + java.text.SimpleDateFormat("d MMM HH:mm", Locale.getDefault()).format(java.util.Date(now))
        editRoutes { library -> library.add(SavedRoute(newRouteId(), name, route.waypoints, now)) }
        notifyUser("Saved as \"$name\"")
    }

    private fun editRoutes(change: (RouteLibrary) -> RouteLibrary) {
        val updated = _routes.updateAndGet(change)
        viewModelScope.launch(routeSaving) {
            try {
                routeStore.save(updated)
            } catch (error: Exception) {
                notifyUser("The routes could not be saved: ${error.message}")
            }
        }
    }

    private fun newRouteId(): String = UUID.randomUUID().toString()

    /**
     * Imports a GPX file, such as the markers exported from the Navionics Boating app. Its
     * waypoints become pins, skipping ones already there, and its routes become saved routes.
     */
    fun importGpx(uri: Uri) {
        viewModelScope.launch {
            try {
                val bytes = withContext(Dispatchers.IO) {
                    getApplication<Application>().contentResolver.openInputStream(uri)?.use { input ->
                        val data = input.readNBytesCompat(MAX_GPX_BYTES + 1)
                        require(data.size <= MAX_GPX_BYTES) { "The file is larger than ${MAX_GPX_BYTES / 1_048_576} MB" }
                        data
                    } ?: throw IllegalArgumentException("The file could not be opened")
                }
                val content = withContext(Dispatchers.Default) { Gpx.parse(bytes, ::newRouteId) }
                val (merged, added) = _pins.value.merge(content.pins)
                editPins { merged }
                val now = System.currentTimeMillis()
                content.routes.forEach { (name, points) ->
                    editRoutes { library -> library.add(SavedRoute(newRouteId(), name, points, now)) }
                }
                setShowPins(true)
                val skipped = content.pins.size - added
                notifyUser(
                    buildString {
                        append("Imported $added pin(s)")
                        if (skipped > 0) append(", $skipped already there")
                        if (content.routes.isNotEmpty()) append(" and ${content.routes.size} route(s)")
                    },
                )
            } catch (error: Exception) {
                notifyUser("GPX import failed: ${error.message ?: "unknown error"}")
            }
        }
    }

    fun deletePin(id: String) {
        editPins { it.delete(id) }
    }

    fun setShowPins(show: Boolean) {
        _showPins.value = show
        chartSettings.edit().putBoolean(KEY_SHOW_PINS, show).apply()
    }

    private fun editPins(change: (PinLibrary) -> PinLibrary) {
        val updated = _pins.updateAndGet(change)
        viewModelScope.launch(routeSaving) {
            try {
                pinStore.save(updated)
            } catch (error: Exception) {
                notifyUser("The pins could not be saved: ${error.message}")
            }
        }
    }

    private fun java.io.InputStream.readNBytesCompat(limit: Int): ByteArray {
        val out = java.io.ByteArrayOutputStream()
        val buffer = ByteArray(64 * 1024)
        while (out.size() < limit) {
            val read = read(buffer, 0, minOf(buffer.size, limit - out.size()))
            if (read < 0) break
            out.write(buffer, 0, read)
        }
        return out.toByteArray()
    }

    /** Looks for the chart files again, for example after they were copied with adb. */
    fun refreshChart() {
        _chartFile.value = chartStore.current(ChartKind.DEPTHS)
        _rainbowFile.value = chartStore.current(ChartKind.RAINBOW)
    }

    /** Where the chart file of [kind] is expected, for the instructions on the chart screen. */
    fun chartPath(kind: ChartKind = ChartKind.DEPTHS): String = chartStore.file(kind).absolutePath

    /** Picks the chart layer. */
    fun setChartLayer(layer: ChartLayer) {
        _chartLayer.value = layer
        chartSettings.edit().putString(KEY_CHART_LAYER, layer.name).apply()
    }

    /** Imports a chart or rainbow file picked from the phone's storage. */
    fun importChart(uri: Uri) {
        viewModelScope.launch {
            when (val result = chartStore.import(uri)) {
                is ChartStore.ImportResult.Imported -> {
                    refreshChart()
                    if (result.kind == ChartKind.RAINBOW) setChartLayer(ChartLayer.RAINBOW)
                    notifyUser(String.format(Locale.US, "%s imported (%.1f MB)", result.kind.title, result.bytes / 1_048_576.0))
                }
                is ChartStore.ImportResult.Failed -> notifyUser(result.reason)
            }
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

        const val CHART_SETTINGS = "chart"
        const val KEY_SHOW_RAINBOW = "show_rainbow"
        const val KEY_CHART_LAYER = "layer"
        const val KEY_SHOW_PINS = "show_pins"

        /** A GPX file of pins is a few kilobytes; this only guards against picking the wrong file. */
        const val MAX_GPX_BYTES = 20 * 1_048_576

        const val CONNECTION_SETTINGS = "connection"
        const val KEY_LAST_DEVICE = "last_device"
        const val KEY_LAST_DEVICE_NAME = "last_device_name"

        /** How long the automatic scan looks for the last remote before giving up. */
        const val AUTO_SCAN_MILLIS = 30_000L

        /** Pause between two reconnect scans. */
        const val RECONNECT_PAUSE_MILLIS = 5_000L
    }
}
