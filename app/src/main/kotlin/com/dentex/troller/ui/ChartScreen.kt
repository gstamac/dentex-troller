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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.dentex.troller.chart.PhoneFix
import com.dentex.troller.chart.PositionSource
import com.dentex.troller.chart.RouteHit
import com.dentex.troller.haswing.MotorTelemetry
import com.dentex.troller.navigation.MissionProgress
import com.dentex.troller.navigation.RouteLibrary
import com.dentex.troller.navigation.Waypoint
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Point
import kotlinx.coroutines.delay
import kotlin.math.hypot

/**
 * Offline depth chart with the boat position and the route being edited.
 *
 * The chart only displays data and edits the route draft. It never sends anything to the motor;
 * uploading the route stays an explicit action on the route screen.
 */
@Composable
fun ChartScreen(viewModel: MainViewModel, onOpenConnect: () -> Unit) {
    val chart by viewModel.chartFile.collectAsStateWithLifecycle()
    val rainbowFile by viewModel.rainbowFile.collectAsStateWithLifecycle()
    val layer by viewModel.chartLayer.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val routes by viewModel.routes.collectAsStateWithLifecycle()
    val importer = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let(viewModel::importChart)
    }
    val import = { importer.launch(arrayOf("*/*")) }

    // Picks up chart files that were copied with adb while the app was running.
    LaunchedEffect(Unit) { viewModel.refreshChart() }

    val installed = chart
    if (installed == null) {
        NoChart(path = viewModel.chartPath(), onImport = import, onStop = viewModel::requestStop)
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
        onStop = viewModel::requestStop,
        link = when {
            // The transport only accepts writes once the link is ready, so STOP is offered only then.
            state.connectionState is ConnectionState.Ready -> LinkBadge.STOP
            state.reconnecting ||
                state.connectionState is ConnectionState.Connecting ||
                state.connectionState is ConnectionState.Connected -> LinkBadge.RECONNECTING
            else -> LinkBadge.NO_CONNECTION
        },
        onOpenConnect = onOpenConnect,
        telemetry = state.telemetry,
        routes = routes,
        routeActions = RouteActions(
            editable = editable,
            add = viewModel::addWaypoint,
            removeLast = viewModel::removeLastWaypoint,
            removeAt = viewModel::removeWaypoint,
            insert = viewModel::insertWaypoint,
            move = viewModel::moveWaypoint,
            newRoute = viewModel::newRoute,
            select = viewModel::selectRoute,
            rename = viewModel::renameRoute,
            delete = viewModel::deleteRoute,
        ),
    )
}

/** What route mode may do. Waypoints can't change while a route is being uploaded. */
internal class RouteActions(
    val editable: Boolean,
    val add: (Waypoint) -> Unit,
    val removeLast: () -> Unit,
    val removeAt: (Int) -> Unit,
    val insert: (Int, Waypoint) -> Unit,
    val move: (Int, Waypoint) -> Unit,
    val newRoute: () -> Unit,
    val select: (String?) -> Unit,
    val rename: (String, String) -> Unit,
    val delete: (String) -> Unit,
)

@Composable
private fun ChartView(
    chart: ChartStore.InstalledChart,
    rainbow: ChartStore.InstalledChart?,
    layer: ChartLayer,
    installedKinds: Set<ChartKind>,
    onPickLayer: (ChartLayer) -> Unit,
    onStop: () -> Unit,
    link: LinkBadge,
    onOpenConnect: () -> Unit,
    telemetry: MotorTelemetry,
    routes: RouteLibrary,
    routeActions: RouteActions,
) {
    val context = LocalContext.current
    val night = isSystemInDarkTheme()
    val mapView = remember { MapView(context).apply { onCreate(null) } }
    MapLifecycle(mapView)

    var map by remember { mutableStateOf<MapLibreMap?>(null) }
    var style by remember { mutableStateOf<Style?>(null) }
    var follow by rememberSaveable { mutableStateOf(true) }
    var tapped by remember { mutableStateOf<String?>(null) }

    // Route mode: taps edit routes instead of showing the depth.
    var routeMode by rememberSaveable { mutableStateOf(false) }
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
            m.cameraPosition = CameraPosition.Builder()
                .target(LatLng(latitude ?: START_LATITUDE, longitude ?: START_LONGITUDE))
                .zoom(if (hasPosition) FOLLOW_ZOOM else START_ZOOM)
                .build()
            m.addOnCameraMoveStartedListener { reason ->
                if (reason == MapLibreMap.OnCameraMoveStartedListener.REASON_API_GESTURE) follow = false
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
                val mark = m.queryRenderedFeatures(box, ChartStyle.LAYER_SEAMARK_ICON, ChartStyle.LAYER_SEAMARK_DOT).firstOrNull()
                if (mark != null) {
                    val at = (mark.geometry() as? Point)?.let { LatLng(it.latitude(), it.longitude()) } ?: point
                    val text = ChartStyle.describeSeamark(
                        kind = mark.getStringProperty("kind"),
                        name = mark.getStringProperty("name"),
                        light = mark.getStringProperty("light"),
                        colour = mark.getStringProperty("colour"),
                    )
                    tapped = "$text\n${ChartStyle.formatPosition(at.latitude, at.longitude)}"
                    return@addOnMapClickListener true
                }
                val onLand = m.queryRenderedFeatures(screen, ChartStyle.LAYER_LAND).isNotEmpty()
                val depth = m.queryRenderedFeatures(screen, ChartStyle.LAYER_DEPTH_AREA)
                    .firstOrNull()?.getNumberProperty("depth")?.toInt()
                val what = when {
                    onLand -> "Land"
                    depth != null -> "Depth ${ChartStyle.bandLabel(depth)}"
                    else -> "No depth data here"
                }
                tapped = "$what\n${ChartStyle.formatPosition(point.latitude, point.longitude)}"
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

    LaunchedEffect(map, chart, rainbow, night) {
        val m = map ?: return@LaunchedEffect
        style = null
        val json = ChartStyle.json(chart.file.absolutePath, night, rainbow?.file?.absolutePath)
        m.setStyle(Style.Builder().fromJson(json)) { loaded ->
            val palette = if (night) ChartStyle.NIGHT else ChartStyle.DAY
            loaded.addImage(ChartStyle.boatImage(PositionSource.MOTOR), boatArrow(context, palette.boatMotor))
            loaded.addImage(ChartStyle.boatImage(PositionSource.PHONE), boatArrow(context, palette.boatPhone))
            loaded.addImages(HashMap(SeamarkIcons.all(context)))
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
    LaunchedEffect(map, follow, latitude, longitude) {
        val m = map ?: return@LaunchedEffect
        if (!follow || latitude == null || longitude == null) return@LaunchedEffect
        val target = LatLng(latitude, longitude)
        val update = if (m.cameraPosition.zoom < FOLLOW_ZOOM - 2) {
            CameraUpdateFactory.newLatLngZoom(target, FOLLOW_ZOOM)
        } else {
            CameraUpdateFactory.newLatLng(target)
        }
        m.animateCamera(update, FOLLOW_ANIMATION_MILLIS)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        Surface(
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp).widthIn(max = 230.dp),
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

        Column(
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            when (link) {
                LinkBadge.STOP -> StopSignButton(onStop = onStop)
                LinkBadge.RECONNECTING -> ReconnectingButton(onClick = onOpenConnect)
                LinkBadge.NO_CONNECTION -> NoConnectionButton(onClick = onOpenConnect)
            }
            LayersMenu(
                layer = layer,
                installed = { it.needs == null || it.needs in installedKinds },
                onPick = onPickLayer,
            )
            RouteModeButton(
                active = routeMode,
                onClick = {
                    routeMode = !routeMode
                    selectedWaypoint = null
                },
            )
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

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (hasPosition && !follow) {
                ExtendedFloatingActionButton(
                    onClick = { follow = true },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    text = { Text("Follow boat") },
                    modifier = Modifier.align(Alignment.End),
                )
            }
            if (routeMode) {
                RoutePanel(
                    routes = routes,
                    selectedWaypoint = shownWaypoint,
                    actions = routeActions,
                    onDeselectWaypoint = { selectedWaypoint = null },
                )
            } else {
                Surface(
                    modifier = Modifier.widthIn(max = 260.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    shape = MaterialTheme.shapes.small,
                    tonalElevation = 2.dp,
                ) {
                    Text(
                        text = tapped ?: "Tap for the depth or a seamark.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }
            }
        }
    }
}

private fun hasLocationPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED

/** The fixed position of the fakeGps build, renewed every second so it never turns stale. */
@Composable
private fun fakePhoneLocation(position: String): Location {
    val (latitude, longitude) = position.split(",").map { it.trim().toDouble() }
    val location by produceState(fakeFix(latitude, longitude)) {
        while (true) {
            delay(PHONE_UPDATE_MILLIS)
            value = fakeFix(latitude, longitude)
        }
    }
    return location
}

private fun fakeFix(latitude: Double, longitude: Double) = Location(LocationManager.GPS_PROVIDER).apply {
    this.latitude = latitude
    this.longitude = longitude
    accuracy = 5f
    time = System.currentTimeMillis()
    elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
}

/** The phone's latest GPS fix while the chart is on screen, or null without one. */
@Composable
private fun phoneLocation(context: Context, allowed: Boolean): Location? {
    BuildConfig.FAKE_PHONE_LOCATION?.let { return fakePhoneLocation(it) }
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
private fun LayersMenu(layer: ChartLayer, installed: (ChartLayer) -> Boolean, onPick: (ChartLayer) -> Unit) {
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
private const val MAX_ZOOM = 16.0
private const val START_ZOOM = 6.5
private const val FOLLOW_ZOOM = 13.0
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
private enum class LinkBadge { STOP, RECONNECTING, NO_CONNECTION }
