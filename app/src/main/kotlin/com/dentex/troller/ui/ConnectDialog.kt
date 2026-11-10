package com.dentex.troller.ui

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.MainViewModel
import com.dentex.troller.ble.BleScanResult
import com.dentex.troller.ble.BleUuids
import com.dentex.troller.ble.ConnectionState

/**
 * The connect popup, opened from the status badge on the chart and from the remote on the
 * Settings screen. It closes by itself once a link it did not start with is ready. A connected
 * remote is shown at the top with a Disconnect button.
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
fun ConnectDialog(viewModel: MainViewModel, onDismiss: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val readyAtOpen = remember { state.connectionState is ConnectionState.Ready }
    LaunchedEffect(state.connectionState) {
        if (state.connectionState is ConnectionState.Ready && !readyAtOpen) onDismiss()
    }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            // A fixed size: a popup that grew with each scan result made the window manager
            // re-lay and animate its window every time, and the chart behind it flickered.
            modifier = Modifier.fillMaxWidth(0.92f).fillMaxHeight(0.8f),
        ) {
            ConnectContent(viewModel, onDismiss)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConnectContent(viewModel: MainViewModel, onDismiss: () -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val locationPermission = BluetoothPermissions.location
    var hasBluetoothPermission by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    fun refreshPermissionState() {
        hasBluetoothPermission = BluetoothPermissions.hasBluetooth(context)
        hasLocationPermission = BluetoothPermissions.hasLocation(context)
    }

    LaunchedEffect(Unit) { refreshPermissionState() }

    var confirmDisconnect by remember { mutableStateOf(false) }
    if (confirmDisconnect) {
        AlertDialog(
            onDismissRequest = { confirmDisconnect = false },
            title = { Text("Disconnect the remote?") },
            text = {
                Text(
                    "Disconnecting does not stop the motor, and the app cannot stop it until you " +
                        "connect again. Press STOP first if the motor is running. The app does not " +
                        "reconnect by itself.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDisconnect = false
                        viewModel.disconnect()
                    },
                ) { Text("Disconnect", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { confirmDisconnect = false }) { Text("Cancel") } },
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        refreshPermissionState()
        if (hasBluetoothPermission) {
            viewModel.startScan()
        }
    }

    Column(modifier = Modifier.padding(start = 20.dp, end = 8.dp, top = 8.dp, bottom = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Connect", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
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
                        viewModel.startScan()
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

        ConnectionStateRow(state.connectionState, onDisconnect = { confirmDisconnect = true })

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        if (state.devices.isNotEmpty()) {
            // The remote's Bluetooth module is named "HM_…"; those come first, apart from the rest.
            val linked = (state.connectionState as? ConnectionState.Ready)?.deviceId
            val (remotes, others) = state.devices
                .filter { it.deviceId != linked }
                .partition { BleUuids.isRemoteName(it.deviceName) }
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items(remotes, key = { it.deviceId }) { device ->
                    DeviceRow(device = device, onConnect = { viewModel.connect(device.deviceId) })
                }
                if (remotes.isNotEmpty() && others.isNotEmpty()) {
                    item(key = "divider") {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            HorizontalDivider(thickness = 2.dp)
                            Text(
                                "Other devices",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                            )
                        }
                    }
                }
                items(others, key = { it.deviceId }) { device ->
                    DeviceRow(device = device, onConnect = { viewModel.connect(device.deviceId) })
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

/**
 * The link at the top of the popup. A connected remote is shown here with a Disconnect button: it
 * stops advertising while it is connected, so a scan never lists it.
 */
@Composable
private fun ConnectionStateRow(connectionState: ConnectionState, onDisconnect: () -> Unit) {
    if (connectionState is ConnectionState.Ready) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, end = 12.dp),
        ) {
            ListItem(
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                overlineContent = { Text("Connected") },
                headlineContent = { Text(connectionState.deviceName ?: "(no name)") },
                supportingContent = { Text(connectionState.deviceId) },
                trailingContent = {
                    OutlinedButton(onClick = onDisconnect) { Text("Disconnect") }
                },
            )
        }
        return
    }
    val (label, detail) = when (connectionState) {
        is ConnectionState.Disconnected -> "Disconnected" to "No remote connected"
        is ConnectionState.Connecting -> "Connecting" to "Opening the GATT connection…"
        is ConnectionState.Connected -> "Connected" to "Discovering services…"
        is ConnectionState.Failed -> "Failed" to connectionState.reason
        is ConnectionState.Ready -> return
    }
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
