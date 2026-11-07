package com.dentex.troller.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.MainViewModel
import com.dentex.troller.chart.ChartStyle
import kotlin.math.roundToInt

/** Metres per second at one knot. */
private const val KNOT_MPS = 1852.0 / 3600.0

/** The trolling speed the radius choices are explained with. */
private const val TROLLING_KNOTS = 3

/** Settings that are kept across restarts. */
@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val radius by viewModel.nearSeamarkRadius.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        SectionHeader("Seamarks near the boat")
        Text(
            "With Seamarks off in the chart's layers menu, rocks, wrecks, obstructions, buoys and " +
                "beacons still show within this distance of the boat.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Column(modifier = Modifier.selectableGroup()) {
            ChartStyle.NEAR_BOAT_RADII.forEach { meters ->
                val seconds = (meters / (TROLLING_KNOTS * KNOT_MPS)).roundToInt()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = meters == radius, role = Role.RadioButton) {
                            viewModel.setNearSeamarkRadius(meters)
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = meters == radius, onClick = null, modifier = Modifier.padding(10.dp))
                    Column {
                        Text("$meters m", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "About $seconds s at $TROLLING_KNOTS knots",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
