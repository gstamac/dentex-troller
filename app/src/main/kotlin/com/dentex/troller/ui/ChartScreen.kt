package com.dentex.troller.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.dentex.troller.MainViewModel
import com.dentex.troller.ble.ConnectionState
import com.dentex.troller.chart.BoatPosition
import com.dentex.troller.chart.ChartGeoJson
import com.dentex.troller.chart.ChartStore
import com.dentex.troller.chart.ChartStyle
import com.dentex.troller.chart.PhoneFix
import com.dentex.troller.chart.PositionSource
import com.dentex.troller.haswing.MotorTelemetry
import com.dentex.troller.navigation.MissionProgress
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
    val showRainbow by viewModel.showRainbow.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val route by viewModel.routeDraft.collectAsStateWithLifecycle()
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
    ChartView(
        chart = installed,
        rainbow = rainbowFile?.takeIf { showRainbow },
        rainbowInstalled = rainbowFile != null,
        onRainbowClick = { if (rainbowFile == null) import() else viewModel.setShowRainbow(!showRainbow) },
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
        route = route,
        onLongPress = { if (editable) viewModel.addWaypoint(it) },
    )
}

@Composable
private fun ChartView(
    chart: ChartStore.InstalledChart,
    rainbow: ChartStore.InstalledChart?,
    rainbowInstalled: Boolean,
    onRainbowClick: () -> Unit,
    onStop: () -> Unit,
    link: LinkBadge,
    onOpenConnect: () -> Unit,
    telemetry: MotorTelemetry,
    route: List<Waypoint>,
    onLongPress: (Waypoint) -> Unit,
) {
    val context = LocalContext.current
    val night = isSystemInDarkTheme()
    val mapView = remember { MapView(context).apply { onCreate(null) } }
    MapLifecycle(mapView)

    var map by remember { mutableStateOf<MapLibreMap?>(null) }
    var style by remember { mutableStateOf<Style?>(null) }
    var follow by rememberSaveable { mutableStateOf(true) }
    var tapped by remember { mutableStateOf<String?>(null) }
    val longPress by rememberUpdatedState(onLongPress)

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
                // Seamarks are small, so they get a finger-sized box rather than a single point.
                val reach = SEAMARK_TAP_DP * context.resources.displayMetrics.density
                val mark = m.queryRenderedFeatures(
                    RectF(screen.x - reach, screen.y - reach, screen.x + reach, screen.y + reach),
                    ChartStyle.LAYER_SEAMARK_ICON, ChartStyle.LAYER_SEAMARK_DOT,
                ).firstOrNull()
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
            m.addOnMapLongClickListener { point ->
                longPress(Waypoint(point.latitude, point.longitude))
                true
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
    LaunchedEffect(style, route) {
        val loaded = style ?: return@LaunchedEffect
        loaded.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_ROUTE_LINE)?.setGeoJson(ChartGeoJson.routeLine(route))
        loaded.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_ROUTE_POINTS)?.setGeoJson(ChartGeoJson.routePoints(route))
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
            FilterChip(
                selected = rainbow != null,
                onClick = onRainbowClick,
                label = { Text(if (rainbowInstalled) "Rainbow" else "Add rainbow layer") },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                ),
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

        Surface(
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp).widthIn(max = 260.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            shape = MaterialTheme.shapes.small,
            tonalElevation = 2.dp,
        ) {
            Text(
                text = tapped ?: "Tap for the depth or a seamark. Long-press to add a waypoint.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }

        if (hasPosition && !follow) {
            ExtendedFloatingActionButton(
                onClick = { follow = true },
                icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                text = { Text("Follow boat") },
                modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
            )
        }
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

/** What the corner badge of the chart offers, following the state of the link to the motor. */
private enum class LinkBadge { STOP, RECONNECTING, NO_CONNECTION }
