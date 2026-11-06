package com.dentex.troller.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dentex.troller.haswing.ReportedMode

/** Which controls the bar shows, from the mode the motor reports. */
internal enum class ControlSet {
    /** Manual, or stopped in manual: speed, steering, anchor and start. */
    MANUAL,

    /** Anchor: stop, and continue navigation when anchoring ended a go-to. */
    ANCHOR,

    /** Navigation, or a go-to that is starting: stop, anchor and speed. */
    NAVIGATION,

    /** Any other mode (cruise, recording, the foot pedal): stop and speed. */
    OTHER,

    /**
     * The remote is connected but the motor is not ready for commands, or reports no mode: only
     * the stop command, which goes out even then.
     */
    STOP_ONLY,
    ;

    companion object {
        /**
         * The set for [mode] while the motor takes commands ([ready]); a go-to that is starting
         * counts as navigation.
         */
        fun of(mode: ReportedMode?, goToActive: Boolean, ready: Boolean): ControlSet = when {
            !ready -> STOP_ONLY
            goToActive || mode == ReportedMode.NAVIGATION -> NAVIGATION
            mode == null -> STOP_ONLY
            mode == ReportedMode.MANUAL -> MANUAL
            mode == ReportedMode.ANCHOR -> ANCHOR
            else -> OTHER
        }
    }
}

/**
 * The motor controls on the chart and what they do. Each one sends its command once; the command
 * gate of the motor controller still decides whether it goes out.
 */
internal class MotorControls(
    val set: ControlSet,
    val modeName: String?,
    /** The speed step the motor reports. */
    val speed: Int?,
    /** Whether the motor reports its propeller running. */
    val propellerOn: Boolean?,
    /** False while a go-to is still uploading, before navigation mode was sent. */
    val canAnchorNavigation: Boolean,
    /** Where Continue navigation goes; null hides it. */
    val resume: GoToRequest?,
    val speedDown: () -> Unit,
    val speedUp: () -> Unit,
    val turnLeft: () -> Unit,
    val turnRight: () -> Unit,
    /** Manual: turns the propeller on. */
    val start: () -> Unit,
    /** Manual: the stop command. */
    val stop: () -> Unit,
    /** Manual: switches to anchor mode. */
    val anchor: () -> Unit,
    /** Navigation: ends it with manual mode and the stop command (Stop go-to). */
    val stopNavigation: () -> Unit,
    /** Navigation: switches to anchor mode where the boat is (Anchor here). */
    val anchorNavigation: () -> Unit,
    /** Anchor and other modes: manual mode, then the stop command. */
    val stopInManual: () -> Unit,
)

/**
 * The motor controls, floating over the bottom of the chart as round icon buttons: on the left
 * the speed (rabbit faster, turtle slower) between turn left and right, on the right what the
 * mode allows. The action buttons use the symbols of the status badge: the anchor, the
 * propeller for Start, the navigation arrow for Continue navigation and the square for Stop.
 */
@Composable
internal fun MotorControlBar(controls: MotorControls, onContinue: (GoToRequest) -> Unit) {
    val set = controls.set
    val speed = set == ControlSet.MANUAL || set == ControlSet.NAVIGATION || set == ControlSet.OTHER
    val steering = set == ControlSet.MANUAL
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Always shown, so they stay where the hand expects them; disabled where the mode has no use
            // for them: steering outside manual (navigation and anchor steer by themselves), and both
            // in anchor mode or before the motor answers.
            RoundButton("Turn left", CONTROL_FILL, SMALL, enabled = steering, onClick = controls.turnLeft) { r ->
                turnGlyph(r, CONTROL_INK, right = false)
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // The animals are drawn a little larger: they are wide but not tall.
                RoundButton("Speed up", CONTROL_FILL, SMALL, enabled = speed, onClick = controls.speedUp) { r ->
                    rabbitGlyph(r * 1.2f, CONTROL_INK)
                }
                RoundButton("Speed down", CONTROL_FILL, SMALL, enabled = speed, onClick = controls.speedDown) { r ->
                    turtleGlyph(r * 1.15f, CONTROL_INK, shell = CONTROL_FILL)
                }
            }
            RoundButton("Turn right", CONTROL_FILL, SMALL, enabled = steering, onClick = controls.turnRight) { r ->
                turnGlyph(r, CONTROL_INK, right = true)
            }
        }
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            when (set) {
                ControlSet.MANUAL -> {
                    RoundButton("Anchor here", MotorRunState.ANCHOR.color, LARGE, onClick = controls.anchor) { r -> anchorGlyph(r) }
                    // Start only while the motor reports its propeller off, so Stop is there otherwise.
                    if (controls.propellerOn == false) {
                        RoundButton("Start the motor", MotorRunState.MANUAL.color, LARGE, onClick = controls.start) { r ->
                            propellerGlyph(r, hub = MotorRunState.MANUAL.color)
                        }
                    } else {
                        StopButton(controls.stop)
                    }
                }
                ControlSet.NAVIGATION -> {
                    RoundButton(
                        "Anchor here",
                        MotorRunState.ANCHOR.color,
                        LARGE,
                        enabled = controls.canAnchorNavigation,
                        onClick = controls.anchorNavigation,
                    ) { r -> anchorGlyph(r) }
                    StopButton(controls.stopNavigation)
                }
                ControlSet.ANCHOR -> {
                    controls.resume?.let { rest ->
                        RoundButton("Continue navigation", MotorRunState.NAVIGATION.color, LARGE, onClick = { onContinue(rest) }) { r ->
                            navigationGlyph(r)
                        }
                    }
                    StopButton(controls.stopInManual)
                }
                ControlSet.OTHER -> StopButton(controls.stopInManual)
                ControlSet.STOP_ONLY -> StopButton(controls.stop)
            }
        }
    }
}

/** Stop: a white square on red, the colour of the STOP button. */
@Composable
private fun StopButton(onClick: () -> Unit) {
    RoundButton("Stop the motor", STOP_RED, LARGE, onClick = onClick) { r -> stopGlyph(r) }
}

/** A round button over the chart with a drawn symbol; [glyph] gets the radius to draw within. */
@Composable
private fun RoundButton(
    description: String,
    fill: Color,
    size: Dp,
    enabled: Boolean = true,
    onClick: () -> Unit,
    glyph: DrawScope.(Float) -> Unit,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        color = fill,
        shadowElevation = 4.dp,
        modifier = Modifier
            .size(size)
            .alpha(if (enabled) 1f else 0.45f)
            .semantics { contentDescription = description },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) { glyph(this.size.minDimension / 2 * 0.72f) }
    }
}

private val SMALL = 50.dp
private val LARGE = 58.dp
private val CONTROL_FILL = Color.White
private val CONTROL_INK = Color(0xFF1B2A38)
private val STOP_RED = Color(0xFFC8102E)
