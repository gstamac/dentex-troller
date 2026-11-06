package com.dentex.troller.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
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
    CONNECT("Connect", Icons.Default.Home),
    DASHBOARD("Dashboard", Icons.Default.Info),
    CONTROLS("Controls", Icons.Default.Settings),
    DIAGNOSTICS("Log", Icons.AutoMirrored.Filled.List),
}

/**
 * Root composable.
 *
 * A single activity with a bottom navigation bar on every screen but the chart, which has the whole
 * screen and a three-dot button that opens the dashboard. Back on another screen returns to the
 * chart. The emergency stop is on every screen: a STOP
 * button at the top, or Stop on the motor controls of the chart.
 */
@Composable
fun DentexApp(viewModel: MainViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var destination by remember { mutableStateOf(Destination.CHART) }
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

    // The chart has no bottom menu, so Back from any other screen goes back to it.
    BackHandler(enabled = destination != Destination.CHART) { destination = Destination.CHART }

    Scaffold(
        bottomBar = {
            if (destination != Destination.CHART) NavigationBar {
                Destination.entries.forEach { entry ->
                    NavigationBarItem(
                        selected = destination == entry,
                        onClick = { destination = entry },
                        icon = { Icon(entry.icon, contentDescription = entry.label) },
                        label = { Text(entry.label) },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Controls has its own STOP next to "Motor on", and the chart has Stop on its motor controls.
            if (destination != Destination.CONTROLS && destination != Destination.CHART) {
                EmergencyStopButton(
                    onStop = viewModel::requestStop,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
                )
            }
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                when (destination) {
                    Destination.CONNECT -> ConnectScreen(viewModel)
                    Destination.DASHBOARD -> DashboardScreen(viewModel)
                    Destination.CHART -> ChartScreen(
                        viewModel,
                        onOpenConnect = { destination = Destination.CONNECT },
                        onOpenMenu = { destination = Destination.DASHBOARD },
                    )
                    Destination.CONTROLS -> ControlsScreen(viewModel)
                    Destination.DIAGNOSTICS -> DiagnosticsScreen(viewModel)
                }
            }
        }
    }
}
