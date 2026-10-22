package com.dentex.troller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.MainViewModel
import com.dentex.troller.navigation.MissionProgress
import com.dentex.troller.navigation.Waypoint
import java.util.Locale

/**
 * Route screen.
 *
 * Uploading a route never starts the motor. Switching to navigation is a separate, explicitly
 * confirmed action on the controls screen.
 */
@Composable
fun RouteScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val progress = state.missionProgress
    var showAddDialog by remember { mutableStateOf(false) }
    var waypoints by remember { mutableStateOf(listOf<Waypoint>()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Route", style = MaterialTheme.typography.headlineSmall)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
            Button(
                onClick = { showAddDialog = true },
                enabled = progress.phase != MissionProgress.Phase.UPLOADING,
            ) { Text("Add waypoint") }
            OutlinedButton(
                onClick = { viewModel.uploadRoute(waypoints) },
                enabled = waypoints.isNotEmpty() &&
                    progress.phase != MissionProgress.Phase.UPLOADING &&
                    progress.phase != MissionProgress.Phase.DOWNLOADING,
            ) { Text("Upload") }
            OutlinedButton(
                onClick = { viewModel.downloadRoute() },
                enabled = progress.phase != MissionProgress.Phase.DOWNLOADING,
            ) { Text("Download") }
        }

        if (progress.phase != MissionProgress.Phase.IDLE) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(
                    text = "${progress.phase.name.lowercase(Locale.US)} " +
                        "${progress.completed}/${progress.total}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                progress.percent?.let {
                    LinearProgressIndicator(
                        progress = { it / 100f },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    )
                }
                progress.message?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (progress.phase == MissionProgress.Phase.UPLOADING ||
                    progress.phase == MissionProgress.Phase.DOWNLOADING
                ) {
                    TextButton(onClick = { viewModel.cancelMission() }) { Text("Cancel") }
                }
            }
        }

        SectionHeader("Waypoints")
        if (waypoints.isEmpty()) {
            Text(
                text = "No waypoint yet. Add one with a latitude and a longitude in decimal degrees.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items(waypoints) { waypoint ->
                    Text(
                        text = String.format(
                            Locale.US,
                            "%d.  %.7f, %.7f",
                            waypoints.indexOf(waypoint) + 1,
                            waypoint.latitude,
                            waypoint.longitude,
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        state.downloadedRoute?.let { route ->
            SectionHeader("Route stored on the motor")
            Text(
                text = "${route.size} waypoint(s)",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }

    if (showAddDialog) {
        AddWaypointDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { latitude, longitude ->
                waypoints = waypoints + Waypoint(latitude, longitude)
                showAddDialog = false
            },
        )
    }
}

@Composable
private fun AddWaypointDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, Double) -> Unit,
) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    val latitudeValue = latitude.toDoubleOrNull()
    val longitudeValue = longitude.toDoubleOrNull()
    val valid = latitudeValue != null && longitudeValue != null &&
        latitudeValue in -90.0..90.0 && longitudeValue in -180.0..180.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add waypoint") },
        text = {
            Column {
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(latitudeValue!!, longitudeValue!!) },
                enabled = valid,
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
