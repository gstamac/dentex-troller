package com.dentex.troller.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dentex.troller.MainViewModel

private enum class Destination(
    val label: String,
    val icon: ImageVector,
) {
    CONNECT("Connect", Icons.Default.Home),
    DASHBOARD("Dashboard", Icons.Default.Info),
    CHART("Chart", Icons.Default.Place),
    CONTROLS("Controls", Icons.Default.Settings),
    ROUTE("Route", Icons.Default.LocationOn),
    DIAGNOSTICS("Log", Icons.AutoMirrored.Filled.List),
}

/**
 * Root composable.
 *
 * A single activity with a bottom navigation bar. The emergency stop is on every screen: a STOP
 * button at the top, or a STOP sign on the chart.
 */
@Composable
fun DentexApp(viewModel: MainViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var destination by remember { mutableStateOf(Destination.CONNECT) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.notice) {
        val notice = state.notice ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(notice)
        viewModel.clearNotice()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
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
            // Controls has its own STOP next to "Motor on", and the chart draws a STOP sign.
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
                    Destination.CHART -> ChartScreen(viewModel)
                    Destination.CONTROLS -> ControlsScreen(viewModel)
                    Destination.ROUTE -> RouteScreen(viewModel)
                    Destination.DIAGNOSTICS -> DiagnosticsScreen(viewModel)
                }
            }
        }
    }
}
