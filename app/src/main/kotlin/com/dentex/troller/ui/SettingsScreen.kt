package com.dentex.troller.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dentex.troller.BuildConfig
import com.dentex.troller.MainViewModel
import com.dentex.troller.ble.ConnectionState
import com.dentex.troller.chart.ChartKind
import com.dentex.troller.chart.ChartStore
import com.dentex.troller.chart.ChartStyle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/** Metres per second at one knot. */
private const val KNOT_MPS = 1852.0 / 3600.0

/**
 * The boat speed each radius choice is explained with: trolling for the small ones, faster
 * running for the large ones. A radius not listed uses trolling speed.
 */
private val KNOTS_FOR_RADIUS = mapOf(25 to 3, 50 to 3, 100 to 6, 200 to 20)
private const val TROLLING_KNOTS = 3

/** Settings that are kept across restarts. */
@Composable
fun SettingsScreen(viewModel: MainViewModel, onChangeRemote: () -> Unit) {
    val radius by viewModel.nearSeamarkRadius.collectAsStateWithLifecycle()
    val remote by viewModel.savedRemote.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val depths by viewModel.chartFile.collectAsStateWithLifecycle()
    val rainbow by viewModel.rainbowFile.collectAsStateWithLifecycle()
    val chartImporter = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let(viewModel::importChart)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        SectionHeader("Remote")
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                val saved = remote
                Text(
                    saved?.let { it.name ?: "(no name)" } ?: "No remote yet",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    when {
                        saved == null -> "Connect to the motor's remote once; the app reconnects to it after that."
                        (state.connectionState as? ConnectionState.Ready)?.deviceId == saved.id -> "${saved.id}  ·  connected"
                        else -> saved.id
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            OutlinedButton(onClick = onChangeRemote, modifier = Modifier.padding(start = 8.dp)) {
                Text(if (remote == null) "Connect" else "Change")
            }
        }
        SectionHeader("Charts")
        ChartRow(ChartKind.DEPTHS, depths)
        ChartRow(ChartKind.RAINBOW, rainbow)
        Text(
            "Pick a .pmtiles file, for example from Download. The app reads which chart it is and " +
                "replaces the installed one; a file that is not a chart leaves it as it was.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
        )
        OutlinedButton(
            onClick = { chartImporter.launch(arrayOf("*/*")) },
            modifier = Modifier.padding(bottom = 16.dp),
        ) { Text("Import chart file…") }
        SectionHeader("Seamarks near the boat")
        Text(
            "With Seamarks off in the chart's layers menu, rocks, wrecks, obstructions, buoys and " +
                "beacons still show within this distance of the boat, unless this is Off.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Column(modifier = Modifier.selectableGroup()) {
            (listOf(ChartStyle.NEAR_BOAT_OFF) + ChartStyle.NEAR_BOAT_RADII).forEach { meters ->
                val off = meters == ChartStyle.NEAR_BOAT_OFF
                val knots = KNOTS_FOR_RADIUS[meters] ?: TROLLING_KNOTS
                val seconds = (meters / (knots * KNOT_MPS)).roundToInt()
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
                        Text(if (off) "Off" else "$meters m", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            if (off) "No seamarks near the boat while they are hidden" else "About $seconds s at $knots knots",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        SectionHeader("About")
        Text(
            "Dentex Troller ${BuildConfig.VERSION_NAME}" + if (BuildConfig.SIMULATOR) " (simulator)" else "",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        CREDITS.forEach { (title, text) ->
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Text(
            "The chart is not for navigation. Its depths come from a grid of about 115 m, so rocks " +
                "and shallows near the shore can be missing; keep a lookout.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

/** What the chart is made from, and the credit and licence each source asks for. */
private val CREDITS = listOf(
    "Depths" to "EMODnet Bathymetry Consortium, EMODnet Digital Bathymetry (DTM 2024). CC BY 4.0.",
    "Land and shoreline" to "© OpenStreetMap contributors. ODbL.",
    "Seamarks" to "OpenSeaMap, © OpenStreetMap contributors. ODbL.",
)

/** One chart file: its name, and its size and date or that it is missing. */
@Composable
private fun ChartRow(kind: ChartKind, installed: ChartStore.InstalledChart?) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(kind.title, style = MaterialTheme.typography.bodyLarge)
        Text(
            installed?.let {
                val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(it.modifiedMillis))
                String.format(Locale.US, "%.1f MB  ·  %s", it.bytes / 1_048_576.0, date)
            } ?: "Not installed",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
