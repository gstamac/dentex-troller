package com.dentex.troller.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    CONTROLS("Controls", Icons.Default.Settings),
    ROUTE("Route", Icons.Default.LocationOn),
    DIAGNOSTICS("Log", Icons.AutoMirrored.Filled.List),
}

/**
 * Root composable.
 *
 * A single activity with a bottom navigation bar. The emergency stop is always reachable from the
 * top bar, no matter which screen is open.
 */
@OptIn(ExperimentalMaterial3Api::class)
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (destination) {
                            Destination.CONNECT -> "Dentex Troller"
                            Destination.DASHBOARD -> "Dashboard"
                            Destination.CONTROLS -> "Controls"
                            Destination.ROUTE -> "Route"
                            Destination.DIAGNOSTICS -> "Diagnostics"
                        },
                    )
                },
                actions = {
                    if (destination != Destination.CONTROLS) {
                        IconButton(onClick = { viewModel.requestStop() }) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Stop the motor",
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                },
            )
        },
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (destination) {
                Destination.CONNECT -> ConnectScreen(viewModel)
                Destination.DASHBOARD -> DashboardScreen(viewModel)
                Destination.CONTROLS -> ControlsScreen(viewModel)
                Destination.ROUTE -> RouteScreen(viewModel)
                Destination.DIAGNOSTICS -> DiagnosticsScreen(viewModel)
            }
        }
    }
}
