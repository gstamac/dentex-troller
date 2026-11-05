package com.dentex.troller.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dentex.troller.chart.BoatPosition
import com.dentex.troller.chart.ChartStyle
import com.dentex.troller.navigation.Pin
import com.dentex.troller.navigation.SavedRoute
import com.dentex.troller.navigation.Waypoint
import com.dentex.troller.navigation.nearestPointOnRoute
import java.util.Locale

/**
 * What a tap on the chart found.
 *
 * @param title the one line shown while the sheet is pulled down.
 * @param details the lines shown once it is pulled up, above the distance.
 * @param at the spot the info is about; the distance is measured to it.
 * @param marked true when the chart draws a ring at [at], for spots that have no symbol of their own.
 */
internal data class MapInfo(
    val title: String,
    val details: List<String> = emptyList(),
    val at: Waypoint? = null,
    val marked: Boolean = false,
    val pin: Pin? = null,
    val routeId: String? = null,
)

/**
 * A pull-up sheet for [info] along the bottom edge of the chart, full width and square at the
 * bottom so it looks like it continues below the edge: the title alone when it is down,
 * everything when it is up. Drag it or tap its title to switch. Dragging it down from the small
 * state calls [onReset], which goes back to the boat's own info.
 */
@Composable
internal fun InfoSheet(
    info: MapInfo,
    route: SavedRoute?,
    boat: BoatPosition?,
    onReset: () -> Unit,
    onDeletePin: (Pin) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val threshold = with(LocalDensity.current) { DRAG_THRESHOLD_DP.dp.toPx() }
    var dragged by remember { mutableFloatStateOf(0f) }
    val dragState = rememberDraggableState { delta -> dragged += delta }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .draggable(
                state = dragState,
                orientation = Orientation.Vertical,
                onDragStopped = {
                    when {
                        dragged < -threshold -> expanded = true
                        dragged > threshold && expanded -> expanded = false
                        dragged > threshold -> onReset()
                    }
                    dragged = 0f
                },
            ),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f),
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        shadowElevation = 8.dp,
    ) {
        Column(modifier = Modifier.padding(bottom = 10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .semantics {
                        contentDescription = if (expanded) "Collapse the details" else "Show the details"
                        stateDescription = if (expanded) "Expanded" else "Collapsed"
                    }
                    .padding(top = 6.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(2.dp)),
                )
            }
            Text(
                text = info.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = if (expanded) 3 else 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 6.dp),
            )
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 12.dp, bottom = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    (info.details + distanceLines(info, route, boat)).forEach { line ->
                        Text(line, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    info.pin?.let { pin ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { onDeletePin(pin) }) {
                                Text("Delete pin", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

/** How far the boat is from what was tapped; for a route, from its start and its nearest point. */
private fun distanceLines(info: MapInfo, route: SavedRoute?, boat: BoatPosition?): List<String> {
    val from = boat?.let { Waypoint(it.latitude, it.longitude) }
    if (route != null) {
        if (route.waypoints.isEmpty()) return emptyList()
        if (from == null) return listOf("Boat position unknown")
        val start = route.waypoints.first()
        return listOfNotNull(
            "Start: ${distanceAndBearing(from, start)}",
            nearestPointOnRoute(route.waypoints, from)
                ?.takeIf { route.waypoints.size > 1 }
                ?.let { "Nearest point: ${distanceAndBearing(from, it)}" },
        )
    }
    val at = info.at ?: return emptyList()
    return listOf(if (from == null) "Boat position unknown" else "From the boat: ${distanceAndBearing(from, at)}")
}

/** The lines a route shows: its size and length. */
internal fun routeDetails(route: SavedRoute): List<String> {
    val count = route.waypoints.size
    val points = if (count == 1) "1 waypoint" else "$count waypoints"
    return listOf(
        if (count < 2) points else String.format(Locale.US, "%s · %s", points, formatDistance(route.lengthMeters)),
    )
}

/** The position line every spot shows. */
internal fun positionLine(at: Waypoint): String = ChartStyle.formatPosition(at.latitude, at.longitude)

private const val DRAG_THRESHOLD_DP = 24
