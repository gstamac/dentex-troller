package com.dentex.troller.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dentex.troller.navigation.RouteLibrary
import com.dentex.troller.navigation.SavedRoute
import java.text.DateFormat
import java.util.Date

/** The selected route and the route tools, shown at the bottom of the chart in route mode. */
@Composable
internal fun RoutePanel(
    routes: RouteLibrary,
    selectedWaypoint: Int?,
    actions: RouteActions,
) {
    val route = routes.selected
    val count = route?.waypoints?.size ?: 0

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 3.dp,
    ) {
        Column(modifier = Modifier.padding(start = 14.dp, end = 6.dp, top = 8.dp, bottom = 4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = route?.name ?: "No route selected",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = when {
                            !actions.editable -> "A route is being uploaded; editing is paused."
                            route == null -> "Tap the chart to start a new route."
                            selectedWaypoint != null ->
                                "Waypoint ${selectedWaypoint + 1} of $count selected. Drag it to move it, or delete it with the trash button."
                            count == 0 -> "Tap the chart to add the first waypoint."
                            else -> "${routeSummary(route)} · tap to add, tap the line to insert, tap a point to select it"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

/**
 * A route-mode tool under the route button: smaller than the main buttons, so it reads as part of
 * route mode. The Routes list and New route use it.
 */
@Composable
internal fun RouteToolButton(icon: ImageVector, description: String, enabled: Boolean = true, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = 2.dp,
        // Centred under the 48 dp buttons above, which the column aligns to the right edge.
        modifier = Modifier
            .padding(end = 5.dp)
            .size(38.dp)
            .semantics { contentDescription = description },
    ) {
        val ink = MaterialTheme.colorScheme.onSurface
        Icon(
            icon,
            contentDescription = null,
            tint = if (enabled) ink else ink.copy(alpha = 0.38f),
            modifier = Modifier.padding(8.dp),
        )
    }
}

/** Every saved route: pick one, rename or delete it, or start a new one. */
@Composable
internal fun RoutesDialog(routes: RouteLibrary, actions: RouteActions, onDismiss: () -> Unit) {
    var renaming by remember { mutableStateOf<SavedRoute?>(null) }
    var deleting by remember { mutableStateOf<SavedRoute?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Routes") },
        text = {
            if (routes.routes.isEmpty()) {
                Text("No routes yet. Start one with New route, then tap the chart to add waypoints.")
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 360.dp)) {
                    // Newest first.
                    items(routes.routes.reversed(), key = { it.id }) { route ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    actions.select(route.id)
                                    onDismiss()
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(selected = route.id == routes.selectedId, onClick = null, modifier = Modifier.padding(end = 8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(route.name, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(
                                    text = routeSummary(route) + " · " +
                                        DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(route.createdMillis)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            IconButton(onClick = { renaming = route }) {
                                Icon(Icons.Default.Edit, contentDescription = "Rename ${route.name}")
                            }
                            IconButton(onClick = { deleting = route }, enabled = actions.editable) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete ${route.name}")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    actions.newRoute()
                    onDismiss()
                },
                enabled = actions.editable,
            ) { Text("New route") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
    )

    renaming?.let { route ->
        var name by remember(route.id) { mutableStateOf(route.name) }
        AlertDialog(
            onDismissRequest = { renaming = null },
            title = { Text("Rename route") },
            text = {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        actions.rename(route.id, name)
                        renaming = null
                    },
                    enabled = RouteLibrary.cleanName(name).isNotEmpty(),
                ) { Text("Rename") }
            },
            dismissButton = { TextButton(onClick = { renaming = null }) { Text("Cancel") } },
        )
    }

    deleting?.let { route ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Delete ${route.name}?") },
            text = { Text("Its ${route.waypoints.size} waypoint(s) are removed from the phone. A route already on the motor stays there.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        actions.delete(route.id)
                        deleting = null
                    },
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("Cancel") } },
        )
    }
}

/** "3 waypoints · 1.25 NM", or metres below one nautical mile. */
private fun routeSummary(route: SavedRoute): String {
    val count = route.waypoints.size
    val points = if (count == 1) "1 waypoint" else "$count waypoints"
    return if (count < 2) points else "$points · ${formatDistance(route.lengthMeters)}"
}

/** Switches route mode on the chart; a route line with three waypoints. */
@Composable
internal fun RouteModeButton(active: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = 2.dp,
        modifier = Modifier
            .size(48.dp)
            .semantics { contentDescription = if (active) "Leave route mode" else "Route mode: add and edit routes" },
    ) {
        val ink = if (active) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        Canvas(modifier = Modifier.padding(12.dp)) {
            val w = size.width
            val h = size.height
            val points = listOf(Offset(w * 0.12f, h * 0.82f), Offset(w * 0.45f, h * 0.3f), Offset(w * 0.88f, h * 0.55f))
            points.zipWithNext { a, b -> drawLine(ink, a, b, strokeWidth = w * 0.08f, cap = StrokeCap.Round) }
            points.forEach {
                drawCircle(ink, radius = w * 0.13f, center = it)
                drawCircle(Color.White, radius = w * 0.06f, center = it)
            }
        }
    }
}

