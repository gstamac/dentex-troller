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

/** The go-to and what the chart may do with it. */
internal class GoToActions(
    val state: GoToState,
    /** Why a go-to cannot start now, or null when it can. */
    val blockedBecause: String?,
    val start: (Waypoint) -> Unit,
    val cancel: () -> Unit,
    val anchor: () -> Unit,
    val dismiss: () -> Unit,
)

/**
 * The panel for a target picked with a long press, and for the go-to while it starts, runs and
 * ends. [picked] is the target that has not been started yet.
 */
@Composable
internal fun GoToPanel(
    actions: GoToActions,
    picked: Waypoint?,
    boat: BoatPosition?,
    telemetry: MotorTelemetry,
    onMoveHere: () -> Unit,
) {
    val state = actions.state
    val target = if (state.phase == GoToState.Phase.IDLE) picked else state.target
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
                    GoToState.Phase.UPLOADING -> "Sending the target to the motor…"
                    GoToState.Phase.STARTING -> "Switching the motor to navigation…"
                    GoToState.Phase.UNDER_WAY -> "Navigating to the target"
                    GoToState.Phase.ENDED -> "Go-to ended"
                    GoToState.Phase.FAILED -> "Go-to did not start"
                },
                style = MaterialTheme.typography.titleSmall,
                color = if (state.phase == GoToState.Phase.FAILED) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            )
            val lines = buildList {
                target?.let { add(targetLine(it, boat)) }
                state.message?.let(::add)
                if (state.phase == GoToState.Phase.UNDER_WAY && telemetry.motorOn == false) {
                    add("The motor reports its propeller is off.")
                }
                if (state.phase == GoToState.Phase.ENDED || state.phase == GoToState.Phase.FAILED) {
                    telemetry.reportedMode?.let { add("The motor reports: ${it.displayName}.") }
                }
                if (state.phase == GoToState.Phase.IDLE) {
                    add(actions.blockedBecause ?: "The motor will steer there by itself.")
                    add("Tap the chart to cancel.")
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
                    GoToState.Phase.IDLE -> {
                        Button(onClick = onMoveHere, enabled = actions.blockedBecause == null && picked != null) {
                            Text("Move boat here")
                        }
                    }
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
internal fun ConfirmGoToDialog(target: Waypoint, boat: BoatPosition?, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Move the boat here?") },
        text = {
            Text(
                targetLine(target, boat) + "\n\n" +
                    "The route stored on the motor is replaced by this point and the motor switches to " +
                    "navigation mode. The boat starts moving by itself, at the last speed or at speed 1 if it was 0. " +
                    "Your routes on the phone are not changed.\n\n" +
                    "Stop go-to switches the motor to manual and stops it. STOP stays at the top right.",
            )
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Move boat") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

/** The target position, and its distance and bearing from the boat when the boat position is known. */
private fun targetLine(target: Waypoint, boat: BoatPosition?): String {
    val position = ChartStyle.formatPosition(target.latitude, target.longitude)
    if (boat == null) return "$position · boat position unknown"
    val from = Waypoint(boat.latitude, boat.longitude)
    val meters = distanceMeters(from, target)
    val distance = if (meters < 185.2) {
        String.format(Locale.US, "%.0f m", meters)
    } else {
        String.format(Locale.US, "%.2f NM", meters / 1852.0)
    }
    val bearing = Math.round(bearingDegrees(from, target)).toInt() % 360
    return String.format(Locale.US, "%s\n%s · bearing %03d°", position, distance, bearing)
}
