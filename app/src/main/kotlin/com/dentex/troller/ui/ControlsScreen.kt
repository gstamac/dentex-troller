package com.dentex.troller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.dentex.troller.haswing.CommandGateState
import com.dentex.troller.haswing.MotorMode

/**
 * Motor controls.
 *
 * Every propulsion control stays disabled until a type 110 motor has been confirmed from a real
 * heartbeat. The stop control is always enabled.
 */
@Composable
fun ControlsScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val gate = state.commandGate
    val enabled = gate == CommandGateState.READY
    var pendingMode by remember { mutableStateOf<MotorMode?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text("Controls", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = when (gate) {
                CommandGateState.READY ->
                    "Type 110 motor confirmed. Commands are sent as COMMAND_LONG."

                CommandGateState.ALTERNATE_PROFILE ->
                    "A different motor profile answered. The type 110 commands are blocked."

                CommandGateState.UNVERIFIED ->
                    "Waiting for a heartbeat. Propulsion commands stay blocked until the motor " +
                    "profile is confirmed."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )

        SectionHeader("Propulsion")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { viewModel.requestMotorOn() },
                enabled = enabled,
                modifier = Modifier.weight(1f),
            ) { Text("Motor on") }
            Button(
                onClick = { viewModel.requestStop() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
                modifier = Modifier.weight(1f),
            ) { Text("STOP") }
        }

        SectionHeader("Speed")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilledTonalButton(
                onClick = { viewModel.requestSpeedDown() },
                enabled = enabled,
                modifier = Modifier.weight(1f),
            ) { Text("Speed −") }
            FilledTonalButton(
                onClick = { viewModel.requestSpeedUp() },
                enabled = enabled,
                modifier = Modifier.weight(1f),
            ) { Text("Speed +") }
            OutlinedButton(
                onClick = { viewModel.requestFullSpeed() },
                enabled = enabled,
                modifier = Modifier.weight(1f),
            ) { Text("Full") }
        }

        SectionHeader("Steering")
        Text(
            text = "Steering is sent once per press. The Helmsman application repeats the command " +
                "every 66 ms while the control is held; this application sends a single command " +
                "so that a steering command cannot get stuck.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilledTonalButton(
                onClick = { viewModel.requestTurnLeft() },
                enabled = enabled,
                modifier = Modifier.weight(1f),
            ) { Text("◀ Left") }
            FilledTonalButton(
                onClick = { viewModel.requestTurnRight() },
                enabled = enabled,
                modifier = Modifier.weight(1f),
            ) { Text("Right ▶") }
        }

        SectionHeader("Mode")
        MotorMode.entries.filter { it != MotorMode.UNKNOWN && it != MotorMode.REMOTE_CONTROL }
            .forEach { mode ->
                OutlinedButton(
                    onClick = { pendingMode = mode },
                    enabled = enabled,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                ) {
                    Text(mode.displayName)
                }
            }
        Text(
            text = "Switching to a mode can move the boat. The motor performs the GPS work itself.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }

    pendingMode?.let { mode ->
        AlertDialog(
            onDismissRequest = { pendingMode = null },
            title = { Text("Switch to ${mode.displayName}?") },
            text = {
                Text(
                    "The motor starts moving as soon as it accepts the mode. Make sure the " +
                        "propeller is clear and that you can cut the power.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.requestMode(mode)
                    pendingMode = null
                }) { Text("Switch") }
            },
            dismissButton = {
                TextButton(onClick = { pendingMode = null }) { Text("Cancel") }
            },
        )
    }
}
