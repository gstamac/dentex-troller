package com.dentex.troller.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dentex.troller.MainViewModel

private enum class Destination(
    val label: String,
    val icon: ImageVector,
) {
    CHART("Chart", Icons.Default.Place),
    SETTINGS("Settings", Icons.Default.Settings),
    DASHBOARD("Dashboard", Icons.Default.Info),
    CONTROLS("Controls", Icons.Default.PlayArrow),
    DIAGNOSTICS("Log", Icons.AutoMirrored.Filled.List),
}

/**
 * Root composable.
 *
 * A single activity. The chart has the whole screen and a three-dot button that opens the
 * Settings; the other screens slide in over it from the right, with a bottom menu between them,
 * and return to the chart with the X at the top or with Back. Connecting is a popup, opened from
 * the status badge on the chart or from the remote on the Settings screen. STOP is on the
 * Controls screen and on the motor controls of the chart only.
 */
@Composable
fun DentexApp(viewModel: MainViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var destination by remember { mutableStateOf(Destination.CHART) }
    // The connect popup, from the status badge on the chart or Change on the Settings screen.
    var showConnect by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Looks for the last remote whenever the app comes to the front without a link. The first
    // time, the Bluetooth permissions are asked for; the scan starts once they are granted.
    val context = LocalContext.current
    var askedForPermissions by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (BluetoothPermissions.hasBluetooth(context)) viewModel.autoConnect()
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event != Lifecycle.Event.ON_START) return@LifecycleEventObserver
            // A track that was recording when the app was closed records in the background again.
            viewModel.resumeTrackRecording()
            if (BluetoothPermissions.hasBluetooth(context)) {
                viewModel.autoConnect()
            } else if (!askedForPermissions) {
                askedForPermissions = true
                permissionLauncher.launch(BluetoothPermissions.all)
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(state.notice) {
        val notice = state.notice ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(notice)
        viewModel.clearNotice()
    }

    // The chart stays underneath, so it keeps its view and the track keeps recording. It looks
    // for a newly copied chart file each time it comes back to the front.
    LaunchedEffect(destination == Destination.CHART) {
        if (destination == Destination.CHART) viewModel.refreshChart()
    }
    // The last screen shown over the chart, kept while the panel slides out.
    var shown by remember { mutableStateOf(Destination.SETTINGS) }
    if (destination != Destination.CHART) shown = destination

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            ChartScreen(
                viewModel,
                onOpenConnect = { showConnect = true },
                onOpenMenu = { destination = Destination.SETTINGS },
            )
            AnimatedVisibility(
                visible = destination != Destination.CHART,
                enter = slideInHorizontally(tween(SLIDE_MILLIS)) { it },
                exit = slideOutHorizontally(tween(SLIDE_MILLIS)) { it },
            ) {
                // Registered after the chart's own handlers, so Back closes the panel first.
                BackHandler { destination = Destination.CHART }
                ServicePanel(
                    viewModel = viewModel,
                    destination = shown,
                    onPick = { destination = it },
                    onClose = { destination = Destination.CHART },
                    onConnect = { showConnect = true },
                )
            }
        }
    }
    if (showConnect) ConnectDialog(viewModel, onDismiss = { showConnect = false })
}

private const val SLIDE_MILLIS = 250

/**
 * The screens other than the chart, sliding in over it from the right, with an X at the top that
 * closes back to the chart and a bottom menu between them. By the owner's choice (2026-11-09) only
 * Controls (next to "Motor on") and the chart's motor controls have STOP.
 */
@Composable
private fun ServicePanel(
    viewModel: MainViewModel,
    destination: Destination,
    onPick: (Destination) -> Unit,
    onClose: () -> Unit,
    onConnect: () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 8.dp),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close and return to the chart")
                }
            }
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                when (destination) {
                    Destination.DASHBOARD -> DashboardScreen(viewModel)
                    Destination.CONTROLS -> ControlsScreen(viewModel)
                    Destination.DIAGNOSTICS -> DiagnosticsScreen(viewModel)
                    Destination.SETTINGS -> SettingsScreen(viewModel, onChangeRemote = onConnect)
                    Destination.CHART -> Unit
                }
            }
            // The outer Scaffold already keeps clear of the system bars.
            NavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
                Destination.entries.filter { it != Destination.CHART }.forEach { entry ->
                    NavigationBarItem(
                        selected = destination == entry,
                        onClick = { onPick(entry) },
                        icon = { Icon(entry.icon, contentDescription = entry.label) },
                        label = { Text(entry.label) },
                    )
                }
            }
        }
    }
}
