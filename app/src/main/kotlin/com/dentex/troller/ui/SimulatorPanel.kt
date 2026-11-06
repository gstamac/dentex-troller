package com.dentex.troller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dentex.troller.SimulatorControls

/** The sim build's buttons for the simulated motor; the same commands work over adb. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SimulatorPanel(simulator: SimulatorControls) {
    var result by rememberSaveable { mutableStateOf("Simulated remote and motor.") }
    var gpsFix by rememberSaveable { mutableStateOf(true) }
    var arrival by rememberSaveable { mutableStateOf(0) }
    var fast by rememberSaveable { mutableStateOf(false) }
    fun run(command: String, value: String? = null) {
        result = simulator.command(command, value)
    }

    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Simulator", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                FilledTonalButton(onClick = { run("status") }) { Text("Status") }
                FilledTonalButton(onClick = {
                    gpsFix = !gpsFix
                    run("gps", if (gpsFix) "fix" else "nofix")
                }) { Text(if (gpsFix) "GPS: fix" else "GPS: no fix") }
                FilledTonalButton(onClick = {
                    arrival = (arrival + 1) % ARRIVALS.size
                    run("arrival", ARRIVALS[arrival])
                }) { Text("Arrival: ${ARRIVALS[arrival]}") }
                FilledTonalButton(onClick = {
                    fast = !fast
                    run("timescale", if (fast) "10" else "1")
                }) { Text(if (fast) "Time ×10" else "Time ×1") }
                FilledTonalButton(onClick = { run("link", "drop") }) { Text("Silence 8 s") }
                FilledTonalButton(onClick = { run("link", "lose") }) { Text("Lose link 15 s") }
            }
            // As if the mode button on the remote was pressed, without the app asking.
            Text("Remote button", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onTertiaryContainer)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                FilledTonalButton(onClick = { run("mode", "manual") }) { Text("Manual") }
                FilledTonalButton(onClick = { run("mode", "anchor") }) { Text("Anchor") }
                FilledTonalButton(onClick = { run("mode", "navigation") }) { Text("Navigation") }
            }
            Text(result, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
    }
}

private val ARRIVALS = listOf("anchor", "manual", "circle")
