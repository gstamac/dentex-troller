package com.dentex.troller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.MainViewModel
import com.dentex.troller.haswing.MotorTelemetry
import java.util.Locale

/**
 * Telemetry dashboard.
 *
 * Values that the motor has not sent are shown as "—". Nothing is invented, and the screen shows
 * when the last update was received so that stale data is obvious.
 */
@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val telemetry = state.telemetry
    val identity = state.identity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text("Telemetry", style = MaterialTheme.typography.headlineSmall)

        SectionHeader("Motor")
        TelemetryRow {
            TelemetryCard(
                label = "Profile",
                value = identity?.profile?.displayName ?: "Not identified",
                modifier = Modifier.weight(1f),
            )
        }
        TelemetryRow {
            TelemetryCard(
                label = "Heartbeat type",
                value = identity?.heartbeatType?.toString() ?: "—",
                modifier = Modifier.weight(1f),
            )
            TelemetryCard(
                label = "Firmware",
                value = identity?.firmwareVersion?.toString() ?: "—",
                modifier = Modifier.weight(1f),
            )
        }
        TelemetryRow {
            TelemetryCard(
                label = "Motor",
                value = motorStateText(telemetry),
                modifier = Modifier.weight(1f),
            )
            TelemetryCard(
                label = "Battery",
                value = batteryText(telemetry),
                modifier = Modifier.weight(1f),
            )
        }

        SectionHeader("Position")
        TelemetryRow {
            TelemetryCard(
                label = "Latitude",
                value = telemetry.latitude?.let { String.format(Locale.US, "%.7f", it) } ?: "—",
                modifier = Modifier.weight(1f),
            )
            TelemetryCard(
                label = "Longitude",
                value = telemetry.longitude?.let { String.format(Locale.US, "%.7f", it) } ?: "—",
                modifier = Modifier.weight(1f),
            )
        }
        TelemetryRow {
            TelemetryCard(
                label = "Speed",
                value = telemetry.speedKmh?.let { String.format(Locale.US, "%.2f km/h", it) } ?: "—",
                modifier = Modifier.weight(1f),
            )
            TelemetryCard(
                label = "Heading",
                value = telemetry.headingDegrees?.let { String.format(Locale.US, "%.1f°", it) } ?: "—",
                modifier = Modifier.weight(1f),
            )
        }
        TelemetryRow {
            TelemetryCard(
                label = "GPS signal",
                value = telemetry.gpsSignal?.toString() ?: "—",
                modifier = Modifier.weight(1f),
            )
            TelemetryCard(
                label = "Motor speed",
                value = telemetry.motorSpeed?.toString() ?: "—",
                modifier = Modifier.weight(1f),
            )
        }

        SectionHeader("Freshness")
        Text(
            text = freshnessText(telemetry),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "The motor reuses standard MAVLink messages for non standard data. " +
                "The values above follow the mapping of the Helmsman application and still need to " +
                "be confirmed against the real unit.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

private fun motorStateText(telemetry: MotorTelemetry): String = when (telemetry.motorOn) {
    true -> "Running"
    false -> "Stopped"
    null -> "—"
}

private fun batteryText(telemetry: MotorTelemetry): String {
    val level = telemetry.batteryLevel
    val millivolts = telemetry.batteryMilliVolts
    return when {
        level != null && millivolts != null -> "$level %  ($millivolts mV)"
        level != null -> "$level %"
        millivolts != null -> "$millivolts mV"
        else -> "—"
    }
}

private fun freshnessText(telemetry: MotorTelemetry): String {
    if (telemetry.lastUpdateMillis == 0L) return "No telemetry received yet."
    val age = System.currentTimeMillis() - telemetry.lastUpdateMillis
    val heartbeatAge = if (telemetry.lastHeartbeatMillis > 0L) {
        " · last heartbeat ${System.currentTimeMillis() - telemetry.lastHeartbeatMillis} ms ago"
    } else {
        ""
    }
    return "Last update $age ms ago$heartbeatAge"
}
