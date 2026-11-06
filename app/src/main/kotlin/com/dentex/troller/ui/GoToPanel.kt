package com.dentex.troller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dentex.troller.chart.BoatPosition
import com.dentex.troller.chart.ChartStyle
import com.dentex.troller.haswing.MotorTelemetry
import com.dentex.troller.haswing.ReportedMode
import com.dentex.troller.navigation.GoToState
import com.dentex.troller.navigation.Waypoint
import com.dentex.troller.navigation.bearingDegrees
import com.dentex.troller.navigation.distanceMeters
import java.util.Locale

/**
 * What "Move boat here" or "Follow route" asks for: one point, or a route called [name]. A route
 * started from a later waypoint leaves the first [skipped] out and [waypoints] is the rest.
 */
internal data class GoToRequest(val waypoints: List<Waypoint>, val name: String? = null, val skipped: Int = 0) {
    val target: Waypoint get() = waypoints.first()
    val followsRoute: Boolean get() = name != null
}

/** The go-to and what the chart may do with it. */
internal class GoToActions(
    val state: GoToState,
    /** Why a go-to cannot start now, or null when it can. */
    val blockedBecause: String?,
    val start: (GoToRequest) -> Unit,
    val cancel: () -> Unit,
    val anchor: () -> Unit,
    val dismiss: () -> Unit,
)

/** The panel for a go-to or a followed route while it starts, runs and ends. */
@Composable
internal fun GoToPanel(
    actions: GoToActions,
    boat: BoatPosition?,
    telemetry: MotorTelemetry,
) {
    val state = actions.state
    // A followed route shows the waypoint the motor heads for.
    val target = state.heading(telemetry.targetPoint)
    val route = state.followsRoute && state.phase != GoToState.Phase.IDLE
    val motorNavigating = telemetry.reportedMode == ReportedMode.NAVIGATION

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 3.dp,
    ) {
        Column(modifier = Modifier.padding(start = 14.dp, end = 6.dp, top = 8.dp, bottom = 4.dp)) {
            Text(
                text = when (state.phase) {
                    GoToState.Phase.IDLE -> "Target"
                    GoToState.Phase.UPLOADING -> if (route) "Sending ${state.name} to the motor…" else "Sending the target to the motor…"
                    GoToState.Phase.STARTING -> "Switching the motor to navigation…"
                    GoToState.Phase.UNDER_WAY -> if (route) "Following ${state.name}" else "Navigating to the target"
                    GoToState.Phase.ENDED -> if (route) "Route ended" else "Go-to ended"
                    GoToState.Phase.FAILED -> if (route) "Route did not start" else "Go-to did not start"
                },
                style = MaterialTheme.typography.titleSmall,
                color = if (state.phase == GoToState.Phase.FAILED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            )
            val lines = buildList {
                if (route && state.phase == GoToState.Phase.UNDER_WAY) {
                    // Numbered as on the phone's route, counting the waypoints a join left out.
                    val index = (telemetry.targetPoint ?: 0).coerceIn(0, state.waypoints.lastIndex)
                    add("Heading for waypoint ${state.skipped + index + 1} of ${state.skipped + state.waypoints.size}")
                }
                target?.let { add(targetLine(it, boat)) }
                state.message?.let(::add)
                if (state.phase == GoToState.Phase.UNDER_WAY && telemetry.motorOn == false) {
                    add("The motor reports its propeller is off.")
                }
                if (state.phase == GoToState.Phase.ENDED || state.phase == GoToState.Phase.FAILED) {
                    telemetry.reportedMode?.let { add("The motor reports: ${it.displayName}.") }
                }
            }
            Text(
                text = lines.joinToString("\n"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (state.phase) {
                    GoToState.Phase.IDLE -> Unit
                    GoToState.Phase.UPLOADING, GoToState.Phase.STARTING, GoToState.Phase.UNDER_WAY -> {
                        // Anchoring only makes sense once navigation mode was sent.
                        if (state.phase != GoToState.Phase.UPLOADING) {
                            TextButton(onClick = actions.anchor) { Text("Anchor here") }
                        }
                        Button(
                            onClick = actions.cancel,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            ),
                        ) { Text("Stop go-to") }
                    }
                    GoToState.Phase.ENDED, GoToState.Phase.FAILED -> {
                        // The motor may still be navigating, for example after a lost link.
                        if (motorNavigating) {
                            TextButton(onClick = actions.anchor) { Text("Anchor here") }
                            TextButton(onClick = actions.cancel) { Text("End navigation and stop") }
                        }
                        TextButton(onClick = actions.dismiss) { Text("OK") }
                    }
                }
            }
        }
    }
}

/** Asks before the boat is moved: says what will happen and how far it is. */
@Composable
internal fun ConfirmGoToDialog(request: GoToRequest, boat: BoatPosition?, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    val count = request.waypoints.size
    val length = request.waypoints.zipWithNext { a, b -> distanceMeters(a, b) }.sum()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (request.followsRoute) "Follow ${request.name}?" else "Move the boat here?") },
        text = {
            Text(
                if (request.followsRoute) {
                    val join = if (request.skipped > 0) {
                        "Starts at waypoint ${request.skipped + 1} of ${request.skipped + count}.\n"
                    } else {
                        ""
                    }
                    join +
                        "${if (count == 1) "1 waypoint" else "$count waypoints"} to go · ${formatDistance(length)}\n" +
                        "First: " + targetLine(request.target, boat) + "\n\n" +
                        "The route stored on the motor is replaced by this route and the motor switches to " +
                        "navigation mode. The boat steers to waypoint ${request.skipped + 1} and on along the route by itself, at the " +
                        "last speed or at speed 1 if it was 0.\n\n" +
                        "Stop go-to switches the motor to manual and stops it. STOP stays at the top right."
                } else {
                    targetLine(request.target, boat) + "\n\n" +
                        "The route stored on the motor is replaced by this point and the motor switches to " +
                        "navigation mode. The boat starts moving by itself, at the last speed or at speed 1 if it was 0. " +
                        "Your routes on the phone are not changed.\n\n" +
                        "Stop go-to switches the motor to manual and stops it. STOP stays at the top right."
                },
            )
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text(if (request.followsRoute) "Follow route" else "Move boat") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

/** The target position, and its distance and bearing from the boat when the boat position is known. */
private fun targetLine(target: Waypoint, boat: BoatPosition?): String {
    val position = ChartStyle.formatPosition(target.latitude, target.longitude)
    if (boat == null) return "$position · boat position unknown"
    return "$position\n${distanceAndBearing(Waypoint(boat.latitude, boat.longitude), target)}"
}

/** "0.42 NM · bearing 093°" from [from] to [to]. */
internal fun distanceAndBearing(from: Waypoint, to: Waypoint): String {
    val bearing = Math.round(bearingDegrees(from, to)).toInt() % 360
    return String.format(Locale.US, "%s · bearing %03d°", formatDistance(distanceMeters(from, to)), bearing)
}

/** Metres below one nautical mile, nautical miles from there. */
internal fun formatDistance(meters: Double): String =
    if (meters < METERS_PER_NM) String.format(Locale.US, "%.0f m", meters) else String.format(Locale.US, "%.2f NM", meters / METERS_PER_NM)

private const val METERS_PER_NM = 1852.0
