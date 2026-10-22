package com.dentex.troller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
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
import com.dentex.troller.logging.HexDump
import com.dentex.troller.logging.LogEntry

/**
 * Diagnostics screen.
 *
 * Shows the raw traffic in both directions. The export is meant to be compared with a capture
 * from the Helmsman application, so the raw bytes are kept in full.
 */
@Composable
fun DiagnosticsScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showExport by remember { mutableStateOf(false) }
    val entries = state.logEntries

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Diagnostics", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "${entries.size} retained records. Export the log to compare the wire format " +
                "with a Helmsman capture.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { showExport = true }) { Text("Export") }
            TextButton(onClick = { viewModel.clearLog() }) { Text("Clear") }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        if (entries.isEmpty()) {
            Text(
                text = "No traffic captured yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            val visible = remember(entries) { entries.takeLast(300).reversed() }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items(visible, key = { "${it.timestampMillis}-${it.direction}-${it.label}" }) { entry ->
                    LogRow(entry)
                }
            }
        }
    }

    if (showExport) {
        ExportDialog(text = viewModel.exportLog(), onDismiss = { showExport = false })
    }
}

@Composable
private fun LogRow(entry: LogEntry) {
    val color = when (entry.direction) {
        LogEntry.Direction.RX -> MaterialTheme.colorScheme.primary
        LogEntry.Direction.TX -> MaterialTheme.colorScheme.tertiary
        LogEntry.Direction.EVENT -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.direction.name,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                modifier = Modifier.padding(end = 6.dp),
            )
            Text(
                text = entry.message ?: entry.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        entry.payload?.let { payload ->
            MonospaceText(text = HexDump.toSpacedHex(payload), modifier = Modifier.padding(start = 42.dp))
        }
    }
}

@Composable
private fun ExportDialog(text: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Diagnostic export") },
        text = {
            SelectionContainer {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.heightIn(max = 400.dp).verticalScroll(rememberScrollState()),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
    )
}
