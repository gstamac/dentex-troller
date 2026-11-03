package com.dentex.troller.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.MainViewModel
import com.dentex.troller.chart.ChartGeoJson
import com.dentex.troller.chart.ChartStore
import com.dentex.troller.chart.ChartStyle
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

/**
 * Offline depth chart with the boat position and the route being edited.
 *
 * The chart only displays data and edits the route draft. It never sends anything to the motor;
 * uploading the route stays an explicit action on the route screen.
 */
@Composable
fun ChartScreen(viewModel: MainViewModel) {
    val chart by viewModel.chartFile.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val route by viewModel.routeDraft.collectAsStateWithLifecycle()
    val importer = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let(viewModel::importChart)
    }

    // Picks up a chart that was copied with adb while the app was running.
    LaunchedEffect(Unit) { viewModel.refreshChart() }

    val installed = chart
    if (installed == null) {
        NoChart(path = viewModel.chartPath, onImport = { importer.launch(arrayOf("*/*")) })
        return
    }

    val editable = state.missionProgress.phase != MissionProgress.Phase.UPLOADING
    ChartView(
        chart = installed,
        telemetry = state.telemetry,
        route = route,
        onLongPress = { if (editable) viewModel.addWaypoint(it) },
    )
}

@Composable
private fun ChartView(
    chart: ChartStore.InstalledChart,
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

    val latitude = telemetry.latitude
    val longitude = telemetry.longitude
    val hasPosition = latitude != null && longitude != null

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

    LaunchedEffect(map, chart, night) {
        val m = map ?: return@LaunchedEffect
        style = null
        val json = ChartStyle.json(chart.file.absolutePath, night)
        m.setStyle(Style.Builder().fromJson(json)) { loaded ->
            loaded.addImage(ChartStyle.IMAGE_BOAT, boatArrow(context, if (night) ChartStyle.NIGHT.boat else ChartStyle.DAY.boat))
            style = loaded
        }
    }

    val stale = !telemetry.connected || telemetry.isStale(STALE_AFTER_MILLIS)
    LaunchedEffect(style, latitude, longitude, telemetry.headingDegrees, stale) {
        style?.getSourceAs<GeoJsonSource>(ChartStyle.SOURCE_BOAT)
            ?.setGeoJson(ChartGeoJson.boat(latitude, longitude, telemetry.headingDegrees, stale))
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
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(
                text = "NOT FOR NAVIGATION · EMODnet Bathymetry · © OpenStreetMap",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }

        Surface(
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp).widthIn(max = 260.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            shape = MaterialTheme.shapes.small,
            tonalElevation = 2.dp,
        ) {
            Text(
                text = tapped ?: "Tap for the depth. Long-press to add a waypoint.",
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

@Composable
private fun NoChart(path: String, onImport: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
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
private const val BOAT_ICON_DP = 30f
