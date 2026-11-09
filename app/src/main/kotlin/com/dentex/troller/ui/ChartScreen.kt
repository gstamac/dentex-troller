package com.dentex.troller.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.location.LocationListenerCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.location.LocationRequestCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.BuildConfig
import com.dentex.troller.MainViewModel
import com.dentex.troller.ble.ConnectionState
import com.dentex.troller.chart.BoatPosition
import com.dentex.troller.chart.ChartGeoJson
import com.dentex.troller.chart.ChartKind
import com.dentex.troller.chart.ChartLayer
import com.dentex.troller.chart.ChartStore
import com.dentex.troller.chart.ChartStyle
import com.dentex.troller.chart.MapScale
import com.dentex.troller.chart.PhoneFix
import com.dentex.troller.chart.PositionSource
import com.dentex.troller.chart.RouteHit
import com.dentex.troller.haswing.CommandGateState
import com.dentex.troller.haswing.MotorMode
import com.dentex.troller.haswing.MotorTelemetry
import com.dentex.troller.haswing.ReportedMode
import com.dentex.troller.navigation.GoToState
import com.dentex.troller.navigation.MissionProgress
import com.dentex.troller.navigation.Pin
import com.dentex.troller.navigation.PinLibrary
import com.dentex.troller.navigation.RouteLibrary
import com.dentex.troller.navigation.Track
import com.dentex.troller.navigation.Waypoint
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Point
import kotlinx.coroutines.delay
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.hypot

/**
 * Offline depth chart with the boat position and the route being edited.
 *
 * The chart displays data and edits routes. The only thing on it that talks to the motor, apart
 * from STOP, is "Move boat here", which asks for a confirmation first.
 */
@Composable
fun ChartScreen(viewModel: MainViewModel, onOpenConnect: () -> Unit, onOpenMenu: () -> Unit) {
    val chart by viewModel.chartFile.collectAsStateWithLifecycle()
    val rainbowFile by viewModel.rainbowFile.collectAsStateWithLifecycle()
    val layer by viewModel.chartLayer.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val routes by viewModel.routes.collectAsStateWithLifecycle()
    val pins by viewModel.pins.collectAsStateWithLifecycle()
    val showPins by viewModel.showPins.collectAsStateWithLifecycle()
    val showSeamarks by viewModel.showSeamarks.collectAsStateWithLifecycle()
    val nearSeamarkRadius by viewModel.nearSeamarkRadius.collectAsStateWithLifecycle()
    val track by viewModel.track.collectAsStateWithLifecycle()
    val recordingTrack by viewModel.recordingTrack.collectAsStateWithLifecycle()
    val trackTail by viewModel.trackTail.collectAsStateWithLifecycle()
    val goToState by viewModel.goToState.collectAsStateWithLifecycle()
    val resume by viewModel.resumeAfterAnchor.collectAsStateWithLifecycle()
    val gpxImporter = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let(viewModel::importGpx)
    }
    val importer = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let(viewModel::importChart)
    }
    val import = { importer.launch(arrayOf("*/*")) }

    // Picks up chart files that were copied with adb while the app was running.
    LaunchedEffect(Unit) { viewModel.refreshChart() }

    val installed = chart
    if (installed == null) {
        // The chart screen has no bottom menu, so the way to the other screens is kept here too.
        Box(modifier = Modifier.fillMaxSize()) {
            NoChart(path = viewModel.chartPath(), onImport = import, onStop = viewModel::requestStop)
            MoreButton(onClick = onOpenMenu, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp))
        }
        return
    }

    val editable = state.missionProgress.phase != MissionProgress.Phase.UPLOADING
    val installedKinds = setOfNotNull(ChartKind.DEPTHS, rainbowFile?.let { ChartKind.RAINBOW })
    ChartView(
        chart = installed,
        rainbow = rainbowFile?.takeIf { layer == ChartLayer.RAINBOW },
        layer = layer,
        installedKinds = installedKinds,
        // A layer whose file is missing opens the file picker; importing it switches to it.
        onPickLayer = { picked ->
            if (picked.needs == null || picked.needs in installedKinds) viewModel.setChartLayer(picked) else import()
        },
        status = when {
            state.connectionState is ConnectionState.Ready ->
                if (state.commandGate == CommandGateState.READY) MotorStatus.motor(state.telemetry) else MotorStatus.NoMotor
            else -> MotorStatus.Disconnected(
                connecting = state.reconnecting ||
                    state.connectionState is ConnectionState.Connecting ||
                    state.connectionState is ConnectionState.Connected,
            )
        },
        onOpenConnect = onOpenConnect,
        onOpenMenu = onOpenMenu,
        savedZoom = viewModel.chartZoom,
        onZoomSettled = viewModel::saveChartZoom,
        telemetry = state.telemetry,
        routes = routes,
        routeActions = RouteActions(
            editable = editable,
            add = viewModel::addWaypoint,
            removeAt = viewModel::removeWaypoint,
            insert = viewModel::insertWaypoint,
            move = viewModel::moveWaypoint,
            newRoute = viewModel::newRoute,
            select = viewModel::selectRoute,
            rename = viewModel::renameRoute,
            delete = viewModel::deleteRoute,
        ),
        pins = PinOverlay(
            library = pins,
            show = showPins,
            onShow = viewModel::setShowPins,
            onImport = { gpxImporter.launch(arrayOf("*/*")) },
            onDelete = viewModel::deletePin,
        ),
        seamarks = SeamarkOverlay(show = showSeamarks, nearRadius = nearSeamarkRadius, onShow = viewModel::setShowSeamarks),
        track = TrackOverlay(
            track = track,
            recording = recordingTrack,
            tail = trackTail,
            onStart = viewModel::startTrack,
            onStop = viewModel::stopTrack,
            onPosition = viewModel::recordTrackPosition,
        ),
        goTo = GoToActions(
            state = goToState,
            blockedBecause = when {
                state.connectionState !is ConnectionState.Ready -> "Connect to the remote to move the boat."
                state.commandGate != CommandGateState.READY -> "Waiting for the motor to answer before it can move the boat."
                state.telemetry.reportedMode == ReportedMode.NAVIGATION -> "The motor is already navigating."
                else -> null
            },
            start = { request ->
                val name = request.name
                if (name == null) viewModel.goTo(request.target) else viewModel.followRoute(request.waypoints, name, request.skipped)
            },
        ),
        // The motor controls show whenever the link can carry a command: the transport only takes
        // writes once it is ready. Until the motor answers they offer only Stop, which is the
        // chart's emergency stop, as the status badge at the top right does not stop the motor.
        motor = ControlSet.of(
            mode = state.telemetry.reportedMode,
            goToActive = goToState.active,
            ready = state.commandGate == CommandGateState.READY,
        )
            .takeIf { state.connectionState is ConnectionState.Ready }
            ?.let { set ->
                MotorControls(
                    set = set,
                    modeName = state.telemetry.reportedMode?.displayName,
                    speed = state.telemetry.motorSpeed,
                    propellerOn = state.telemetry.motorOn,
                    canAnchorNavigation = goToState.phase != GoToState.Phase.UPLOADING ||
                        state.telemetry.reportedMode == ReportedMode.NAVIGATION,
                    resume = resume?.let { GoToRequest(it.waypoints, it.name, it.skipped) },
                    speedDown = viewModel::requestSpeedDown,
                    speedUp = viewModel::requestSpeedUp,
                    turnLeft = viewModel::requestTurnLeft,
                    turnRight = viewModel::requestTurnRight,
                    start = viewModel::requestMotorOn,
                    stop = viewModel::requestStop,
                    anchor = { viewModel.requestMode(MotorMode.ANCHOR) },
                    stopNavigation = viewModel::cancelGoTo,
                    anchorNavigation = viewModel::anchorGoTo,
                    stopInManual = viewModel::stopInManual,
                )
            },
    )
}

/** What route mode may do. Waypoints can't change while a route is being uploaded. */
internal class RouteActions(
    val editable: Boolean,
    val add: (Waypoint) -> Unit,
    val removeAt: (Int) -> Unit,
    val insert: (Int, Waypoint) -> Unit,
    val move: (Int, Waypoint) -> Unit,
    val newRoute: () -> Unit,
    val select: (String?) -> Unit,
    val rename: (String, String) -> Unit,
    val delete: (String) -> Unit,
) {
    /** The same actions, with [select] also calling [then] for the route picked. */
    fun onSelect(then: (String) -> Unit) = RouteActions(
        editable, add, removeAt, insert, move, newRoute,
        select = { id -> select(id); id?.let(then) },
        rename, delete,
    )
}

/**
 * The recorded track and its recording. [onStart] takes whether to keep the old track;
 * [onPosition] takes each fresh boat position, with its time in milliseconds.
 */
internal class TrackOverlay(
    val track: Track,
    val recording: Boolean,
    /** Where the line runs on to from its last point: the boat's last position that fitted. */
    val tail: Waypoint?,
    val onStart: (Boolean) -> Unit,
    val onStop: () -> Unit,
    val onPosition: (Double, Double, PositionSource, Long) -> Unit,
)

/** The pins and what the chart may do with them. */
internal class PinOverlay(
    val library: PinLibrary,
    val show: Boolean,
    val onShow: (Boolean) -> Unit,
    val onImport: () -> Unit,
    val onDelete: (String) -> Unit,
)

/**
 * Whether the chart draws every seamark of the chart file, and how to change it. When not, only
 * hazards and buoys within [nearRadius] metres of the boat are drawn.
 */
internal class SeamarkOverlay(
    val show: Boolean,
    val nearRadius: Int,
    val onShow: (Boolean) -> Unit,
)

@Composable
private fun ChartView(
    chart: ChartStore.InstalledChart,
    rainbow: ChartStore.InstalledChart?,
    layer: ChartLayer,
    installedKinds: Set<ChartKind>,
    onPickLayer: (ChartLayer) -> Unit,
    status: MotorStatus,
    onOpenConnect: () -> Unit,
    /** Leaves the chart for the other screens, which have the bottom menu. */
    onOpenMenu: () -> Unit,
    /** The zoom to open at, from the last time; null the first time. */
    savedZoom: Double?,
    onZoomSettled: (Double) -> Unit,
    telemetry: MotorTelemetry,
    routes: RouteLibrary,
    pins: PinOverlay,
    seamarks: SeamarkOverlay,
    track: TrackOverlay,
    routeActions: RouteActions,
    goTo: GoToActions,
    /** The motor controls, or null while commands can't go out. */
    motor: MotorControls?,
) {
    val context = LocalContext.current
    val night = isSystemInDarkTheme()
    val mapView = remember { MapView(context).apply { onCreate(null) } }
    MapLifecycle(mapView)

    var map by remember { mutableStateOf<MapLibreMap?>(null) }
    var style by remember { mutableStateOf<Style?>(null) }
    var follow by rememberSaveable { mutableStateOf(true) }
    var tapped by remember { mutableStateOf<MapInfo?>(null) }
    // Metres per screen pixel at the centre of the map and the zoom, for the scale line.
    var metersPerPixel by remember { mutableStateOf<Double?>(null) }
    var mapZoom by remember { mutableStateOf<Double?>(null) }
    // Counts camera moves, so what sits on a chart position (the waypoint's trash button) follows.
    var cameraMoves by remember { mutableStateOf(0) }
    val currentRoutes by rememberUpdatedState(routes)
    val currentPins by rememberUpdatedState(pins)
    var confirmDeletePin by remember { mutableStateOf<Pin?>(null) }
    var confirmDeleteWaypoint by remember { mutableStateOf<Int?>(null) }
    // Starting to record with a track already there asks whether to keep it.
    var askNewTrack by remember { mutableStateOf(false) }

    // The selection the last go-to started from. It stays selected; while it is, a go-to that
    // anchoring interrupted is continued rather than started again from the beginning.
    var navigatedFrom by remember { mutableStateOf<MapInfo?>(null) }
    // The navigate button starts a go-to at once, without asking; checked again here, as the link
    // may have dropped since the button was drawn.
    val startGoTo: (GoToRequest) -> Unit = { request ->
        if (goTo.blockedBecause == null) {
            goTo.start(request)
            navigatedFrom = tapped
        }
    }
    // While a go-to runs, the line runs to the target, or to the waypoint of a followed route the
    // motor reports it heads for.
    val goToTarget = if (goTo.state.active) goTo.state.heading(telemetry.targetPoint) else null

    // Route mode: taps edit routes instead of showing the depth.
    var routeMode by rememberSaveable { mutableStateOf(false) }
    // The route to zoom to, with when it was asked for, so picking the same route again zooms again.
    var fitRoute by remember { mutableStateOf<Pair<String, Long>?>(null) }
    // The Routes list, from the menu button under the route button.
    var showRoutes by remember { mutableStateOf(false) }
    if (showRoutes) {
        // A route picked from the list is zoomed to; one tapped on the chart is on screen already.
        RoutesDialog(
            routes = routes,
            actions = routeActions.onSelect { id -> fitRoute = id to System.nanoTime() },
            onDismiss = { showRoutes = false },
        )
    }
    var selectedWaypoint by remember { mutableStateOf<Int?>(null) }
    val waypoints = routes.selected?.waypoints ?: emptyList()
    LaunchedEffect(routes.selectedId) { selectedWaypoint = null }
    val shownWaypoint = selectedWaypoint?.takeIf { routeMode && it < waypoints.size }
    val currentRouteMode by rememberUpdatedState(routeMode)
    val currentActions by rememberUpdatedState(routeActions)
    val currentWaypoints by rememberUpdatedState(waypoints)
    // Where the selected waypoint is while it is being dragged; saved when the finger lifts.
    var dragged by remember { mutableStateOf<Waypoint?>(null) }
    val shownWaypoints = dragged?.let { moving ->
        shownWaypoint?.let { index -> waypoints.mapIndexed { i, old -> if (i == index) moving else old } }
    } ?: waypoints
    val currentShownWaypoint by rememberUpdatedState(shownWaypoint)

    var locationAllowed by remember { mutableStateOf(hasLocationPermission(context)) }
    val locationRequest = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        locationAllowed = hasLocationPermission(context)
    }
    // The app may ask for location at startup, alongside the Bluetooth permissions.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) locationAllowed = hasLocationPermission(context)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    val phoneLocation = phoneLocation(context, locationAllowed)
    // Ticks once a second so a fix that stops updating turns stale on screen.
    val now by produceState(SystemClock.elapsedRealtime()) {
        while (true) {
            delay(1_000)
            value = SystemClock.elapsedRealtime()
        }
    }
    val phoneFresh = phoneLocation != null &&
        now - phoneLocation.elapsedRealtimeNanos / 1_000_000 <= PHONE_STALE_AFTER_MILLIS
    val motorFresh = telemetry.connected && !telemetry.isStale(STALE_AFTER_MILLIS)
    val position = BoatPosition.choose(
        motorLatitude = telemetry.latitude,
        motorLongitude = telemetry.longitude,
        motorHeading = telemetry.headingDegrees,
        motorFresh = motorFresh,
        phone = phoneLocation?.let { fix ->
            // The phone's bearing is its course, which is meaningless while drifting slowly.
            val moving = fix.hasBearing() && fix.hasSpeed() && fix.speed >= MIN_COURSE_SPEED_MPS
            PhoneFix(fix.latitude, fix.longitude, if (moving) fix.bearing else null)
        },
        phoneFresh = phoneFresh,
    )
    val latitude = position?.latitude
    val longitude = position?.longitude
    val hasPosition = position != null

    DisposableEffect(mapView) {
        mapView.getMapAsync { m ->
            m.uiSettings.apply {
                isRotateGesturesEnabled = false
                isTiltGesturesEnabled = false
                isCompassEnabled = false
                isLogoEnabled = false
                isAttributionEnabled = false
            }
            m.setMinZoomPreference(MIN_ZOOM)
            m.setMaxZoomPreference(MAX_ZOOM)
            val (west, south, east, north) = ChartStyle.BOUNDS.toList()
            m.setLatLngBoundsForCameraTarget(LatLngBounds.from(north, east, south, west))
            val startZoom = savedZoom ?: if (hasPosition) FOLLOW_ZOOM else START_ZOOM
            m.cameraPosition = CameraPosition.Builder()
                .target(LatLng(latitude ?: START_LATITUDE, longitude ?: START_LONGITUDE))
                .zoom(startZoom)
                .build()
            // The map can change the zoom while it is first laid out, so the start zoom is applied
            // again once the camera first settles. Only a zoom the user chose with a gesture is
            // kept for the next start; following and the start itself never overwrite it.
            var started = false
            var gesture = false
            val updateScale = {
                m.cameraPosition.target?.let { metersPerPixel = m.projection.getMetersPerPixelAtLatitude(it.latitude) }
                mapZoom = m.cameraPosition.zoom
                cameraMoves++
            }
            m.addOnCameraMoveListener { updateScale() }
            m.addOnCameraIdleListener {
                updateScale()
                val zoom = m.cameraPosition.zoom
                when {
                    !started -> {
                        started = true
                        if (abs(zoom - startZoom) > 0.01) m.moveCamera(CameraUpdateFactory.zoomTo(startZoom))
                    }
                    gesture -> {
                        gesture = false
                        onZoomSettled(zoom)
                    }
                }
            }
            m.addOnCameraMoveStartedListener { reason ->
                if (reason == MapLibreMap.OnCameraMoveStartedListener.REASON_API_GESTURE) {
                    gesture = true
                    follow = false
                }
            }
            m.addOnMapClickListener { point ->
                val screen = m.projection.toScreenLocation(point)
                // Seamarks and waypoints are small, so they get a finger-sized box rather than a single point.
                val reach = SEAMARK_TAP_DP * context.resources.displayMetrics.density
                val box = RectF(screen.x - reach, screen.y - reach, screen.x + reach, screen.y + reach)
                if (currentRouteMode) {
                    // A waypoint of the selected route toggles its selection, a tap on its line
                    // inserts a waypoint there, another route's line selects that route, and
                    // anywhere else adds a waypoint at the end.
                    val waypoint = m.queryRenderedFeatures(box, ChartStyle.LAYER_ROUTE_POINTS).firstOrNull()
                    val onScreen = currentWaypoints.map { m.projection.toScreenLocation(LatLng(it.latitude, it.longitude)) }
                    val segment = RouteHit.segment(onScreen.map { it.x to it.y }, screen.x, screen.y, reach)
                    val other = m.queryRenderedFeatures(box, ChartStyle.LAYER_OTHER_ROUTE_POINT, ChartStyle.LAYER_OTHER_ROUTE_LINE)
                        .firstOrNull()
                    when {
                        waypoint != null -> {
                            val index = waypoint.getNumberProperty("index").toInt()
                            selectedWaypoint = if (selectedWaypoint == index) null else index
                        }
                        segment != null && currentActions.editable -> {
                            val at = m.projection.fromScreenLocation(PointF(segment.x, segment.y))
                            currentActions.insert(segment.segment + 1, Waypoint(at.latitude, at.longitude))
                            selectedWaypoint = segment.segment + 1
                        }
                        other != null -> currentActions.select(other.getStringProperty("id"))
                        selectedWaypoint != null -> selectedWaypoint = null
                        currentActions.editable -> currentActions.add(Waypoint(point.latitude, point.longitude))
                    }
                    return@addOnMapClickListener true
                }
                // The pin's tip is the spot and the icon rises above it, so the box reaches higher.
                val pinBox = RectF(screen.x - reach, screen.y - reach, screen.x + reach, screen.y + 2.5f * reach)
                val pinId = m.queryRenderedFeatures(pinBox, ChartStyle.LAYER_PIN).firstOrNull()?.getStringProperty("id")
                val pin = currentPins.library.pins.firstOrNull { it.id == pinId }
                if (pin != null) {
                    val at = Waypoint(pin.latitude, pin.longitude)
                    val date = pin.timeMillis?.let { DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(it)) }
                    tapped = MapInfo("Pin: ${pin.name}", listOfNotNull(date, positionLine(at)), at = at, marked = true, pin = pin)
                    return@addOnMapClickListener true
                }
                // Outside route mode only the selected route is drawn. A tapped waypoint, or the
                // first waypoint of a tapped section, is marked: Follow route starts there.
                val route = currentRoutes.selected
                val tappedPoint = m.queryRenderedFeatures(box, ChartStyle.LAYER_ROUTE_POINTS).firstOrNull()
                    ?.getNumberProperty("index")?.toInt()
                val tappedSection = if (tappedPoint == null && route != null) {
                    val onScreen = route.waypoints.map { m.projection.toScreenLocation(LatLng(it.latitude, it.longitude)) }
                    RouteHit.segment(onScreen.map { it.x to it.y }, screen.x, screen.y, reach)?.segment
                } else {
                    null
                }
                val onLine = tappedSection != null ||
                    m.queryRenderedFeatures(box, ChartStyle.LAYER_ROUTE_LINE).isNotEmpty()
                if (route != null && route.waypoints.isNotEmpty() && (tappedPoint != null || onLine)) {
                    val start = (tappedPoint ?: tappedSection ?: 0).coerceIn(0, route.waypoints.lastIndex)
                    tapped = routeInfo(route, start)
                    return@addOnMapClickListener true
                }
                val mark = m.queryRenderedFeatures(box, ChartStyle.LAYER_SEAMARK_ICON, ChartStyle.LAYER_SEAMARK_DOT).firstOrNull()
                if (mark != null) {
                    val at = (mark.geometry() as? Point)?.let { LatLng(it.latitude(), it.longitude()) } ?: point
                    val text = ChartStyle.describeSeamark(
                        kind = mark.getStringProperty("kind"),
                        name = mark.getStringProperty("name"),
                        light = mark.getStringProperty("light"),
                        colour = mark.getStringProperty("colour"),
                    )
                    val spot = Waypoint(at.latitude, at.longitude)
                    val lines = text.split("\n")
                    tapped = MapInfo(lines.first(), lines.drop(1) + positionLine(spot), at = spot, marked = true)
                    return@addOnMapClickListener true
                }
                tapped = spotInfo(m, point)
                true
            }
            // A long press always takes the spot itself, ignoring pins, routes and seamarks
            // close to it, so the depth there can be read.
            m.addOnMapLongClickListener { point ->
                if (currentRouteMode) return@addOnMapLongClickListener false
                tapped = spotInfo(m, point)
                true
            }
            // In route mode, a press that starts on the selected waypoint drags it instead of
            // panning the chart. Everything else goes to the map as usual.
            val dragReach = DRAG_REACH_DP * context.resources.displayMetrics.density
            val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
            var downX = 0f
            var downY = 0f
            var moving = false
            mapView.setOnTouchListener { _, event ->
                val index = currentShownWaypoint
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        val start = index?.let { currentWaypoints.getOrNull(it) }
                        if (!currentRouteMode || start == null || !currentActions.editable) return@setOnTouchListener false
                        val at = m.projection.toScreenLocation(LatLng(start.latitude, start.longitude))
                        if (hypot(event.x - at.x, event.y - at.y) > dragReach) return@setOnTouchListener false
                        dragged = start
                        downX = event.x
                        downY = event.y
                        moving = false
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (dragged == null) return@setOnTouchListener false
                        if (!moving && hypot(event.x - downX, event.y - downY) < touchSlop) return@setOnTouchListener true
                        moving = true
                        val at = m.projection.fromScreenLocation(PointF(event.x, event.y))
                        dragged = Waypoint(at.latitude, at.longitude)
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val moved = dragged ?: return@setOnTouchListener false
                        dragged = null
                        if (event.actionMasked == MotionEvent.ACTION_UP && index != null) {
                            // A press that never moved is a tap: it deselects the waypoint, as elsewhere.
                            if (moving) currentActions.move(index, moved) else selectedWaypoint = null
                        }
                        true
                    }
                    else -> dragged != null
                }
            }
            map = m
        }
        onDispose { map = null }
    }

    val shaded = layer != ChartLayer.LINES
    LaunchedEffect(map, chart, rainbow, night, shaded) {
        val m = map ?: return@LaunchedEffect
        style = null
        val json = ChartStyle.json(chart.file.absolutePath, night, rainbow?.file?.absolutePath, shaded)
        m.setStyle(Style.Builder().fromJson(json)) { loaded ->
            val palette = if (night) ChartStyle.NIGHT else ChartStyle.DAY
            loaded.addImage(ChartStyle.boatImage(PositionSource.MOTOR), boatArrow(context, palette.boatMotor))
            loaded.addImage(ChartStyle.boatImage(PositionSource.PHONE), boatArrow(context, palette.boatPhone))
            loaded.addImages(HashMap(SeamarkIcons.all(context)))
            loaded.addImage(ChartStyle.IMAGE_PIN, pinIcon(context, palette.pin))
            style = loaded
        }
    }

    LaunchedEffect(style, position) {
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_BOAT)?.setGeoJson(ChartGeoJson.boat(position))
    }
    LaunchedEffect(style, shownWaypoints, shownWaypoint) {
        val loaded = style ?: return@LaunchedEffect
        loaded.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_ROUTE_LINE)?.setGeoJson(ChartGeoJson.routeLine(shownWaypoints))
        loaded.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_ROUTE_POINTS)
            ?.setGeoJson(ChartGeoJson.routePoints(shownWaypoints, shownWaypoint))
    }
    // The other routes only show in route mode, where they can be picked.
    LaunchedEffect(style, routes, routeMode) {
        val others = if (routeMode) routes.routes.filter { it.id != routes.selectedId } else emptyList()
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_OTHER_ROUTES)?.setGeoJson(ChartGeoJson.otherRoutes(others))
    }
    // The pin and the route whose info is shown are drawn highlighted.
    val selectedPinId = tapped?.pin?.id?.takeIf { !routeMode }
    // Fresh positions go to the track; a stale one is where the boat was, not where it is.
    val freshPosition = position?.takeIf { !it.stale }
    val freshBoat = freshPosition?.let { Waypoint(it.latitude, it.longitude) }
    val freshSource = freshPosition?.source
    val currentTrack by rememberUpdatedState(track)
    // Also run when recording starts, so the track begins where the boat is then rather than
    // after its first move.
    LaunchedEffect(freshBoat, freshSource, track.recording) {
        if (freshBoat != null && freshSource != null) {
            currentTrack.onPosition(freshBoat.latitude, freshBoat.longitude, freshSource, System.currentTimeMillis())
        }
    }
    // Never joined to a position the recorder left out as a bad fix, nor to a stale one.
    val trackTail = track.tail?.takeIf { track.recording && freshBoat != null }
    LaunchedEffect(style, track.track, trackTail) {
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_TRACK)?.setGeoJson(ChartGeoJson.track(track.track, trackTail))
    }
    LaunchedEffect(style, pins.library, pins.show, selectedPinId) {
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_PINS)
            ?.setGeoJson(ChartGeoJson.pins(if (pins.show) pins.library.pins else emptyList(), selectedPinId))
    }
    // With the seamarks hidden, hazards and buoys within the set radius of the boat are still
    // drawn, by the last known position when the fix is stale. The circle only follows the boat
    // once it has moved a bit (ChartStyle.nearBoatCentre), as each new filter re-lays the layers.
    // Filtered-out seamarks can't be tapped either. Applied again whenever the style is rebuilt,
    // for example on a layer or day/night change.
    val nearRadius = seamarks.nearRadius
    val boatAt = position?.let { Waypoint(it.latitude, it.longitude) }
    val nearCentre = remember(nearRadius) { mutableStateOf<Waypoint?>(null) }
    LaunchedEffect(boatAt, nearRadius) {
        nearCentre.value = ChartStyle.nearBoatCentre(nearCentre.value, boatAt, nearRadius)
    }
    val centre = nearCentre.value
    LaunchedEffect(style, seamarks.show, centre, nearRadius) {
        val near = when {
            seamarks.show -> null
            centre == null || nearRadius <= ChartStyle.NEAR_BOAT_OFF -> ""
            else -> ChartGeoJson.circle(centre.latitude, centre.longitude, nearRadius.toDouble())
        }
        ChartStyle.SEAMARK_LAYERS.forEach { id ->
            val filter = Expression.Converter.convert(ChartStyle.seamarkFilter(id, near))
            when (val layer = style?.getLayer(id)) {
                is SymbolLayer -> layer.setFilter(filter)
                is CircleLayer -> layer.setFilter(filter)
            }
        }
    }
    val highlightedRoute = tapped?.routeId?.takeIf { !routeMode }?.let { id -> routes.routes.firstOrNull { it.id == id } }
    LaunchedEffect(style, highlightedRoute) {
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_ROUTE_HIGHLIGHT)
            ?.setGeoJson(ChartGeoJson.routeLine(highlightedRoute?.waypoints ?: emptyList()))
    }
    val canMove = goTo.blockedBecause == null
    // A route that was deleted takes its info with it; then the boat's own info shows.
    val infoRoute = tapped?.routeId?.let { id -> routes.routes.firstOrNull { it.id == id } }
    // Where the navigate button goes: the selection (a route from its marked waypoint, anything
    // else as one point), or what is left of a go-to that anchoring interrupted, while nothing
    // else was selected since that go-to started.
    val selected = tapped?.let { picked ->
        val route = infoRoute
        when {
            route != null && route.waypoints.isNotEmpty() -> {
                val start = (picked.routeStart ?: 0).coerceIn(0, route.waypoints.lastIndex)
                GoToRequest(route.waypoints.drop(start), route.name, skipped = start)
            }
            picked.routeId == null && picked.at != null -> GoToRequest(listOf(picked.at))
            else -> null
        }
    }
    val resume = motor?.resume
    val navigateTo = when {
        !canMove -> null
        resume != null && (tapped == null || tapped == navigatedFrom) -> resume
        else -> selected ?: resume
    }
    // While the boat can be moved, a tapped spot gets the dashed line from the boat that a go-to
    // has. It has its own ring already, so the go-to ring is left out. For a route, [MapInfo.at]
    // is the marked waypoint the navigate button starts from.
    val infoSpot = tapped?.at?.takeIf { canMove && !routeMode && goToTarget == null }
    LaunchedEffect(style, goToTarget, infoSpot, position, goTo.state.active) {
        val json = if (goToTarget != null) {
            ChartGeoJson.goTo(goToTarget, position, goTo.state.active)
        } else {
            ChartGeoJson.goTo(infoSpot, position, active = false, ring = false)
        }
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_GO_TO)?.setGeoJson(json)
    }
    // The spot whose depth or seamark is shown gets a ring, so it is clear what the info is for.
    val markedSpot = tapped?.takeIf { it.marked && !routeMode }?.at
    LaunchedEffect(style, markedSpot) {
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_INFO_MARK)?.setGeoJson(ChartGeoJson.mark(markedSpot))
    }
    // Zooms to a route picked from the Routes list, clear of the buttons and the route panel, and
    // stops following the boat so the chart stays on the route. Very short routes stop at
    // ROUTE_FIT_MAX_ZOOM rather than filling the screen.
    LaunchedEffect(map, fitRoute) {
        val m = map ?: return@LaunchedEffect
        val id = fitRoute?.first ?: return@LaunchedEffect
        val points = currentRoutes.routes.firstOrNull { it.id == id }?.waypoints ?: return@LaunchedEffect
        if (points.isEmpty()) return@LaunchedEffect
        follow = false
        val target = if (points.size == 1) {
            CameraPosition.Builder()
                .target(LatLng(points[0].latitude, points[0].longitude))
                .zoom(maxOf(m.cameraPosition.zoom, FOLLOW_ZOOM))
                .build()
        } else {
            val bounds = LatLngBounds.Builder().apply { points.forEach { include(LatLng(it.latitude, it.longitude)) } }.build()
            val density = context.resources.displayMetrics.density
            val padding = intArrayOf(32, 80, 80, 240).map { (it * density).toInt() }.toIntArray()
            val fitted = m.getCameraForLatLngBounds(bounds, padding) ?: return@LaunchedEffect
            CameraPosition.Builder()
                .target(fitted.target)
                .zoom(minOf(fitted.zoom, ROUTE_FIT_MAX_ZOOM))
                .build()
        }
        m.animateCamera(CameraUpdateFactory.newCameraPosition(target), FOLLOW_ANIMATION_MILLIS)
    }
    LaunchedEffect(map, follow, latitude, longitude) {
        val m = map ?: return@LaunchedEffect
        if (!follow || latitude == null || longitude == null) return@LaunchedEffect
        // Only the centre moves: the zoom stays where the user left it. A boat off the chart is
        // followed to the chart's edge, and once the centre is there nothing more is sent.
        val (lat, lon) = ChartStyle.clampToBounds(latitude, longitude)
        val current = m.cameraPosition
        val at = current.target
        if (at != null && abs(at.latitude - lat) < 1e-7 && abs(at.longitude - lon) < 1e-7) return@LaunchedEffect
        val target = CameraPosition.Builder().target(LatLng(lat, lon)).zoom(current.zoom).build()
        m.animateCamera(CameraUpdateFactory.newCameraPosition(target), FOLLOW_ANIMATION_MILLIS)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        // A trash button beside the selected waypoint deletes it; tapping elsewhere deselects it.
        // Hidden while the waypoint is dragged.
        val deletable = shownWaypoint?.takeIf { routeMode && dragged == null }
        val deleteAt = remember(map, deletable, waypoints, cameraMoves) {
            val m = map
            val point = deletable?.let { waypoints.getOrNull(it) }
            if (m == null || point == null) null else m.projection.toScreenLocation(LatLng(point.latitude, point.longitude))
        }
        if (deletable != null && deleteAt != null) {
            val density = LocalDensity.current
            val gap = with(density) { WAYPOINT_DELETE_GAP.toPx() }
            val side = with(density) { WAYPOINT_DELETE_SIZE.toPx() }
            Surface(
                onClick = { confirmDeleteWaypoint = deletable },
                enabled = routeActions.editable,
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shadowElevation = 3.dp,
                modifier = Modifier
                    .offset { IntOffset((deleteAt.x + gap).roundToInt(), (deleteAt.y - gap - side).roundToInt()) }
                    .size(WAYPOINT_DELETE_SIZE)
                    .semantics { contentDescription = "Delete waypoint ${deletable + 1}" },
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.padding(8.dp))
            }
        }

        Column(
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp).widthIn(max = 230.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "EMODnet Bathymetry · © OpenStreetMap, OpenSeaMap",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
            // The sim build's motor is simulated; this must never be mistaken for a real session.
            if (BuildConfig.SIMULATOR) {
                Surface(color = MaterialTheme.colorScheme.tertiary, shape = MaterialTheme.shapes.small) {
                    Text(
                        text = "SIMULATOR",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            MotorStatusBadge(status = status, onClick = onOpenConnect)
            LayersMenu(
                layer = layer,
                installed = { it.needs == null || it.needs in installedKinds },
                onPick = onPickLayer,
                pins = pins,
                seamarks = seamarks,
            )
            RouteModeButton(
                active = routeMode,
                onClick = {
                    routeMode = !routeMode
                    selectedWaypoint = null
                },
            )
            if (routeMode) {
                RouteToolButton(Icons.Default.Menu, "Routes: pick, rename or delete") { showRoutes = true }
                RouteToolButton(Icons.Default.Add, "New route", enabled = routeActions.editable) {
                    routeActions.newRoute()
                    selectedWaypoint = null
                }
            }
            TrackButton(
                recording = track.recording,
                onClick = {
                    when {
                        track.recording -> track.onStop()
                        track.track.isEmpty -> track.onStart(true)
                        else -> askNewTrack = true
                    }
                },
            )
            MoreButton(onClick = onOpenMenu)
            if (rainbow != null) RainbowLegend()
            if (!locationAllowed) {
                FilterChip(
                    selected = false,
                    onClick = { locationRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                    label = { Text("Use phone GPS") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    ),
                )
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (hasPosition && !follow) {
                FollowBoatButton(
                    onClick = { follow = true },
                    modifier = Modifier.align(Alignment.End),
                )
            }
            metersPerPixel?.let { ScaleLine(it, zoom = mapZoom, modifier = Modifier.align(Alignment.End)) }
            // The motor controls sit just above the info sheet.
            if (!routeMode && motor != null) {
                MotorControlBar(controls = motor, navigate = navigateTo, onNavigate = startGoTo)
            }
            if (routeMode) {
                RoutePanel(
                    routes = routes,
                    selectedWaypoint = shownWaypoint,
                    actions = routeActions,
                )
            }
        }
        // The info sheet runs along the bottom edge, outside the padding of the panels above.
        if (!routeMode) {
            val info = tapped?.takeIf { it.routeId == null || infoRoute != null } ?: boatInfo(position, telemetry)
            InfoSheet(
                info = info,
                route = infoRoute,
                boat = position,
                onReset = { tapped = null },
                onDeletePin = { confirmDeletePin = it },
            )
        }
        }
    }

    if (askNewTrack) {
        AlertDialog(
            onDismissRequest = { askNewTrack = false },
            title = { Text("Record track") },
            text = {
                Text(
                    "The last track is ${formatDistance(track.track.lengthMeters)}. Carry on adding to it, " +
                        "or delete it and start a new one?",
                )
            },
            confirmButton = {
                TextButton(onClick = { track.onStart(true); askNewTrack = false }) { Text("Continue") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { askNewTrack = false }) { Text("Cancel") }
                    TextButton(onClick = { track.onStart(false); askNewTrack = false }) {
                        Text("New track", color = MaterialTheme.colorScheme.error)
                    }
                }
            },
        )
    }

    confirmDeletePin?.let { pin ->
        AlertDialog(
            onDismissRequest = { confirmDeletePin = null },
            title = { Text("Delete pin ${pin.name}?") },
            text = { Text(ChartStyle.formatPosition(pin.latitude, pin.longitude)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        pins.onDelete(pin.id)
                        tapped = null
                        confirmDeletePin = null
                    },
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { confirmDeletePin = null }) { Text("Cancel") } },
        )
    }
    // The trash button by a selected waypoint asks first.
    confirmDeleteWaypoint?.let { index ->
        val point = waypoints.getOrNull(index)
        AlertDialog(
            onDismissRequest = { confirmDeleteWaypoint = null },
            title = { Text("Delete waypoint ${index + 1}?") },
            text = { point?.let { Text(ChartStyle.formatPosition(it.latitude, it.longitude)) } },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (point != null) routeActions.removeAt(index)
                        selectedWaypoint = null
                        confirmDeleteWaypoint = null
                    },
                    enabled = routeActions.editable,
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { confirmDeleteWaypoint = null }) { Text("Cancel") } },
        )
    }
}


private fun hasLocationPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED

/** The phone's latest GPS fix while the chart is on screen, or null without one. */
@Composable
private fun phoneLocation(context: Context, allowed: Boolean): Location? {
    var location by remember { mutableStateOf<Location?>(null) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(allowed, lifecycle) {
        val manager = context.getSystemService(LocationManager::class.java)
        if (!allowed || manager == null || LocationManager.GPS_PROVIDER !in manager.allProviders) {
            return@DisposableEffect onDispose { }
        }
        val listener = LocationListenerCompat { location = it }
        val request = LocationRequestCompat.Builder(PHONE_UPDATE_MILLIS)
            .setQuality(LocationRequestCompat.QUALITY_HIGH_ACCURACY)
            .build()
        var listening = false
        fun start() {
            if (listening || !hasLocationPermission(context)) return
            try {
                LocationManagerCompat.requestLocationUpdates(
                    manager, LocationManager.GPS_PROVIDER, request, ContextCompat.getMainExecutor(context), listener,
                )
                listening = true
                if (location == null) location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } catch (_: SecurityException) {
                // Permission revoked while the app was running; the chart falls back to the motor only.
            }
        }
        fun stop() {
            if (listening) LocationManagerCompat.removeUpdates(manager, listener)
            listening = false
        }
        // GPS runs only while the chart is visible, to spare the battery.
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> start()
                Lifecycle.Event.ON_STOP -> stop()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            stop()
        }
    }
    return location
}

/** The layers button; tapping it opens the list of chart layers below it. */
@Composable
private fun LayersMenu(
    layer: ChartLayer,
    installed: (ChartLayer) -> Boolean,
    onPick: (ChartLayer) -> Unit,
    pins: PinOverlay,
    seamarks: SeamarkOverlay,
) {
    var open by rememberSaveable { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(
            onClick = { open = !open },
            shape = CircleShape,
            color = if (open) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            shadowElevation = 2.dp,
            modifier = Modifier.size(48.dp).semantics { contentDescription = "Map layers" },
        ) {
            val ink = MaterialTheme.colorScheme.onSurface
            Canvas(modifier = Modifier.padding(12.dp)) { drawLayersIcon(ink) }
        }
        if (open) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                shape = MaterialTheme.shapes.small,
                shadowElevation = 2.dp,
            ) {
                Column(modifier = Modifier.width(IntrinsicSize.Max).padding(vertical = 4.dp)) {
                    ChartLayer.entries.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(selected = option == layer, role = Role.RadioButton) {
                                    open = false
                                    onPick(option)
                                }
                                .padding(start = 4.dp, end = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(selected = option == layer, onClick = null, modifier = Modifier.padding(10.dp))
                            Column {
                                Text(option.title, style = MaterialTheme.typography.bodyMedium)
                                if (!installed(option)) {
                                    Text(
                                        "Tap to add the file",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(value = seamarks.show, role = Role.Checkbox, onValueChange = seamarks.onShow)
                            .padding(start = 4.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(checked = seamarks.show, onCheckedChange = null, modifier = Modifier.padding(10.dp))
                        Column {
                            Text("Seamarks", style = MaterialTheme.typography.bodyMedium)
                            if (!seamarks.show) {
                                Text(
                                    if (seamarks.nearRadius <= ChartStyle.NEAR_BOAT_OFF) {
                                        "None near the boat either"
                                    } else {
                                        "Hazards and buoys within ${seamarks.nearRadius} m"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(value = pins.show, role = Role.Checkbox, onValueChange = pins.onShow)
                            .padding(start = 4.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(checked = pins.show, onCheckedChange = null, modifier = Modifier.padding(10.dp))
                        Text("Pins (${pins.library.pins.size})", style = MaterialTheme.typography.bodyMedium)
                    }
                    TextButton(
                        onClick = {
                            open = false
                            pins.onImport()
                        },
                        modifier = Modifier.padding(start = 8.dp),
                    ) { Text("Import GPX…") }
                }
            }
        }
    }
}

/** Two stacked map sheets, the usual "layers" symbol. */
private fun DrawScope.drawLayersIcon(ink: Color) {
    val w = size.width
    val h = size.height
    fun sheet(top: Float) = androidx.compose.ui.graphics.Path().apply {
        moveTo(w * 0.5f, top)
        lineTo(w, top + h * 0.25f)
        lineTo(w * 0.5f, top + h * 0.5f)
        lineTo(0f, top + h * 0.25f)
        close()
    }
    val stroke = Stroke(width = w * 0.09f, join = StrokeJoin.Round)
    drawPath(sheet(h * 0.32f), ink, style = stroke)
    drawPath(sheet(h * 0.05f), ink)
}

/** Colour stops of the rainbow layer that occur in the Adriatic (it is nowhere deeper than about 1250 m). */
@Composable
private fun RainbowLegend() {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shape = MaterialTheme.shapes.small,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            ChartStyle.RAINBOW.filter { (depth, _) -> depth <= LEGEND_MAX_DEPTH }.forEach { (depth, color) ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(width = 18.dp, height = 10.dp)
                            .background(Color(android.graphics.Color.parseColor(color)), MaterialTheme.shapes.extraSmall),
                    )
                    Text("$depth m", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun NoChart(path: String, onImport: () -> Unit, onStop: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        EmergencyStopButton(onStop = onStop)
        Text("No chart installed", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "The depth chart is a separate file, adriatic.pmtiles, built with the tools in " +
                "tools/charts. Pick it from the phone's storage, or copy it over USB.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Button(onClick = onImport) { Text("Import chart file") }
        SectionHeader("Copy with adb")
        MonospaceText("adb push adriatic.pmtiles $path")
        Text(
            text = "The optional rainbow depth layer, adriatic-rainbow.pmtiles, is imported or copied " +
                "the same way, into the same folder.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "The chart appears when you open this tab again after the file is in place.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Forwards the lifecycle to the MapView, which needs every callback in order. */
@Composable
private fun MapLifecycle(mapView: MapView) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            // Leaving the tab while the activity runs: wind the view down in order.
            val current = lifecycle.currentState
            if (current.isAtLeast(Lifecycle.State.RESUMED)) mapView.onPause()
            if (current.isAtLeast(Lifecycle.State.STARTED)) mapView.onStop()
            mapView.onDestroy()
        }
    }
}

/** A chart-plotter style arrow pointing north; the layer rotates it to the heading. */
private fun boatArrow(context: Context, color: String): Bitmap {
    val metrics = context.resources.displayMetrics
    val size = (BOAT_ICON_DP * metrics.density).toInt()
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    bitmap.density = metrics.densityDpi
    val s = size.toFloat()
    val path = Path().apply {
        moveTo(s * 0.5f, s * 0.06f)
        lineTo(s * 0.88f, s * 0.92f)
        lineTo(s * 0.5f, s * 0.72f)
        lineTo(s * 0.12f, s * 0.92f)
        close()
    }
    val canvas = Canvas(bitmap)
    canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = android.graphics.Color.parseColor(color)
        style = Paint.Style.FILL
    })
    canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = android.graphics.Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = s * 0.06f
        strokeJoin = Paint.Join.ROUND
    })
    return bitmap
}

private const val MIN_ZOOM = 5.0
private const val MAX_ZOOM = 20.0
private const val START_ZOOM = 6.5
/** A round button with a target sign that centres the chart on the boat again and keeps it there. */
/** Three dots: opens Settings, from where the bottom menu leads to every screen. */
/** The longest the scale line is drawn. */
private val SCALE_MAX_WIDTH = 100.dp

/**
 * A line of a round length in m or km, with end ticks, as on a paper chart, and the map's [zoom]
 * level beside the length.
 */
@Composable
private fun ScaleLine(metersPerPixel: Double, zoom: Double?, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val maxPixels = with(density) { SCALE_MAX_WIDTH.toPx() }.toDouble()
    val scale = MapScale.fit(metersPerPixel, maxPixels) ?: return
    val width = with(density) { scale.length.toFloat().toDp() }
    val ink = MaterialTheme.colorScheme.onSurface
    val zoomText = zoom?.let { "z" + String.format(Locale.US, "%.1f", it) }
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
        shape = MaterialTheme.shapes.small,
        modifier = modifier.semantics {
            contentDescription = "Scale: ${scale.label}" + (zoom?.let { ", zoom ${String.format(Locale.US, "%.1f", it)}" } ?: "")
        },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = listOfNotNull(scale.label, zoomText).joinToString("  ·  "),
                style = MaterialTheme.typography.labelSmall,
                color = ink,
            )
            Canvas(modifier = Modifier.width(width).height(6.dp)) {
                val stroke = 2.dp.toPx()
                val y = size.height - stroke / 2
                drawLine(ink, Offset(0f, y), Offset(size.width, y), stroke)
                drawLine(ink, Offset(stroke / 2, 0f), Offset(stroke / 2, y), stroke)
                drawLine(ink, Offset(size.width - stroke / 2, 0f), Offset(size.width - stroke / 2, y), stroke)
            }
        }
    }
}

@Composable
private fun MoreButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = 2.dp,
        modifier = modifier
            .size(48.dp)
            .semantics { contentDescription = "More: open Settings and the other screens" },
    ) {
        val ink = MaterialTheme.colorScheme.onSurface
        Canvas(modifier = Modifier.padding(12.dp)) {
            val w = size.width
            listOf(0.18f, 0.5f, 0.82f).forEach { y -> drawCircle(ink, radius = w * 0.1f, center = Offset(w / 2, size.height * y)) }
        }
    }
}

@Composable
private fun FollowBoatButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = 2.dp,
        modifier = modifier
            .size(48.dp)
            .semantics { contentDescription = "Follow the boat" },
    ) {
        val ink = MaterialTheme.colorScheme.onSurface
        Canvas(modifier = Modifier.padding(11.dp)) {
            val w = size.width
            val c = center
            val stroke = w * 0.09f
            drawCircle(ink, radius = w * 0.32f, center = c, style = Stroke(width = stroke))
            drawCircle(ink, radius = w * 0.12f, center = c)
            val inner = w * 0.32f
            val outer = w * 0.5f
            listOf(Offset(1f, 0f), Offset(-1f, 0f), Offset(0f, 1f), Offset(0f, -1f)).forEach { d ->
                drawLine(ink, c + d * inner, c + d * outer, strokeWidth = stroke, cap = StrokeCap.Round)
            }
        }
    }
}

private const val FOLLOW_ZOOM = 13.0
private const val ROUTE_FIT_MAX_ZOOM = 17.0
/** The trash button by a selected waypoint, up and to the right of it, clear of the finger. */
private val WAYPOINT_DELETE_SIZE = 40.dp
private val WAYPOINT_DELETE_GAP = 10.dp
private const val START_LATITUDE = 43.6
private const val START_LONGITUDE = 15.6
private const val FOLLOW_ANIMATION_MILLIS = 500
private const val STALE_AFTER_MILLIS = 5_000L
private const val PHONE_STALE_AFTER_MILLIS = 10_000L
private const val PHONE_UPDATE_MILLIS = 1_000L
private const val MIN_COURSE_SPEED_MPS = 0.5f
private const val BOAT_ICON_DP = 30f
private const val LEGEND_MAX_DEPTH = 500
private const val SEAMARK_TAP_DP = 16f
private const val DRAG_REACH_DP = 28f

/** What the corner badge of the chart offers, following the state of the link to the motor. */

/** The depth, or land, at a spot on the chart. */
private fun spotInfo(m: MapLibreMap, point: LatLng): MapInfo {
    val screen = m.projection.toScreenLocation(point)
    val onLand = m.queryRenderedFeatures(screen, ChartStyle.LAYER_LAND).isNotEmpty()
    val depth = m.queryRenderedFeatures(screen, ChartStyle.LAYER_DEPTH_AREA)
        .firstOrNull()?.getNumberProperty("depth")?.toInt()
    val what = when {
        onLand -> "Land"
        depth != null -> "Depth ${ChartStyle.bandLabel(depth)}"
        else -> "No depth data here"
    }
    val spot = Waypoint(point.latitude, point.longitude)
    return MapInfo(what, listOf(positionLine(spot)), at = spot, marked = true)
}

/** What the sheet shows while nothing on the chart was tapped: the boat itself. */
private fun boatInfo(position: BoatPosition?, telemetry: MotorTelemetry): MapInfo {
    val hint = "Tap the chart for the depth, a seamark, a pin or the route."
    if (position == null) return MapInfo("No boat position", listOf(hint))
    val source = if (position.source == PositionSource.PHONE) "phone GPS" else "motor GPS"
    val title = if (position.stale) "Boat · $source · no recent fix" else "Boat · $source"
    val details = listOfNotNull(
        positionLine(Waypoint(position.latitude, position.longitude)),
        position.headingDegrees?.let { String.format(Locale.US, "Heading %03d°", Math.round(it) % 360) },
        telemetry.speedKmh?.takeIf { position.source == PositionSource.MOTOR }?.let { String.format(Locale.US, "Speed %.1f km/h", it) },
        hint,
    )
    return MapInfo(title, details)
}
