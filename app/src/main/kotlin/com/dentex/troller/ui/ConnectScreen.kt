package com.dentex.troller.ui

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.MainViewModel
import com.dentex.troller.ble.BleScanResult
import com.dentex.troller.ble.ConnectionState

/**
 * Scan and connect screen.
 *
 * The BLE peer is the remote control of the motor, not the motor itself, so the remote has to be
 * switched on before it answers.
 *
 * The scan deliberately runs without a service filter. The remote does not necessarily advertise
 * the GATT service, and a filter would hide it. The filter is only an optional convenience for
 * reducing the list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var serviceFilter by remember { mutableStateOf(false) }

    val locationPermission = BluetoothPermissions.location
    var hasBluetoothPermission by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    fun refreshPermissionState() {
        hasBluetoothPermission = BluetoothPermissions.hasBluetooth(context)
        hasLocationPermission = BluetoothPermissions.hasLocation(context)
    }

    LaunchedEffect(Unit) { refreshPermissionState() }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        refreshPermissionState()
        if (hasBluetoothPermission) {
            viewModel.startScan(serviceFilter)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Switch on the remote control of the motor first. The phone talks to the " +
                "remote, and the remote talks to the motor.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    if (hasBluetoothPermission) {
                        viewModel.startScan(serviceFilter)
                        // The location permission is not needed for the scan itself, but it is
                        // requested as well because some phones deliver no scan results without
                        // it. Granting it restarts the scan.
                        if (!hasLocationPermission && locationPermission != null) {
                            permissionLauncher.launch(arrayOf(locationPermission))
                        }
                    } else {
                        permissionLauncher.launch(BluetoothPermissions.all)
                    }
                },
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Text("  Scan")
            }
            IconButton(onClick = { viewModel.startScan(serviceFilter) }) {
                Icon(Icons.Default.Refresh, contentDescription = "Restart the scan")
            }
            TextButton(
                onClick = {
                    serviceFilter = !serviceFilter
                    viewModel.startScan(serviceFilter)
                },
            ) {
                Text(if (serviceFilter) "Filter: service" else "Filter: off")
            }
        }

        if (!state.bluetoothEnabled) {
            Text(
                text = "Bluetooth is switched off on the phone.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        if (!hasLocationPermission && locationPermission != null) {
            Text(
                text = "The location permission is missing. Some phones refuse to deliver " +
                    "Bluetooth scan results without it.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        if (state.scanning) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                Text(
                    state.autoConnectTo?.let { "Looking for $it to reconnect… ${state.devices.size} device(s) found" }
                        ?: "Scanning… ${state.devices.size} device(s) found",
                )
            }
        } else if (state.reconnecting) {
            Text(
                text = "Reconnecting to the last remote. The next scan starts in a few seconds.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )
        } else if (state.devices.isEmpty()) {
            Text(
                text = "No devices found yet. Make sure the remote is switched on and close to " +
                    "the phone, then scan again.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        ConnectionStateRow(state.connectionState)

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        if (state.devices.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items(state.devices, key = { it.deviceId }) { device ->
                    DeviceRow(
                        device = device,
                        onConnect = { viewModel.connect(device.deviceId) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceRow(device: BleScanResult, onConnect: () -> Unit) {
    ListItem(
        headlineContent = { Text(device.deviceName ?: "(no name)") },
        supportingContent = {
            Text(
                text = buildString {
                    append(device.deviceId)
                    append("  ·  ")
                    append(device.rssi)
                    append(" dBm")
                    if (device.serviceUuids.isNotEmpty()) {
                        append("  ·  ")
                        append(device.serviceUuids.joinToString(", ") { uuid -> uuid.substring(0, 8) })
                    }
                },
            )
        },
        trailingContent = {
            TextButton(onClick = onConnect) { Text("Connect") }
        },
    )
}

@Composable
private fun ConnectionStateRow(connectionState: ConnectionState) {
    val (label, detail) = when (connectionState) {
        is ConnectionState.Disconnected -> "Disconnected" to "No motor connected"
        is ConnectionState.Connecting -> "Connecting" to "Opening the GATT connection…"
        is ConnectionState.Connected -> "Connected" to "Discovering services…"
        is ConnectionState.Ready ->
            "Ready" to "${connectionState.deviceName ?: connectionState.deviceId}"

        is ConnectionState.Failed -> "Failed" to connectionState.reason
    }
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
