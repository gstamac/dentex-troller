package com.dentex.troller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
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

/** The motor controls that float above the bottom panel of the chart; they change with the mode. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MotorControlBar(controls: MotorControls, onContinue: (GoToRequest) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shape = MaterialTheme.shapes.large,
        shadowElevation = 4.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                when (controls.set) {
                    ControlSet.MANUAL -> {
                        SpeedControl(controls)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            RoundButton(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Turn left", controls.turnLeft)
                            RoundButton(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Turn right", controls.turnRight)
                        }
                        BarButton("Anchor", onClick = controls.anchor)
                        // Start only while the motor reports its propeller off, so Stop is there otherwise.
                        if (controls.propellerOn != false) {
                            StopButton(controls.stop)
                        } else {
                            Button(onClick = controls.start, contentPadding = BAR_PADDING, modifier = Modifier.height(BAR_HEIGHT)) {
                                Text("Start")
                            }
                        }
                    }
                    ControlSet.ANCHOR -> {
                        ModeLabel("Anchored")
                        controls.resume?.let { rest ->
                            Button(onClick = { onContinue(rest) }, contentPadding = BAR_PADDING, modifier = Modifier.height(BAR_HEIGHT)) {
                                Text("Continue navigation")
                            }
                        }
                        StopButton(controls.stopInManual)
                    }
                    ControlSet.NAVIGATION -> {
                        SpeedControl(controls)
                        BarButton("Anchor", enabled = controls.canAnchorNavigation, onClick = controls.anchorNavigation)
                        StopButton(controls.stopNavigation)
                    }
                    ControlSet.OTHER -> {
                        val name = controls.modeName
                        if (name != null) ModeLabel(name)
                        SpeedControl(controls)
                        StopButton(controls.stopInManual)
                    }
                    ControlSet.STOP_ONLY -> {
                        ModeLabel("Waiting for the motor")
                        StopButton(controls.stop)
                    }
                }
            }
        }
    }
}

/** − speed + */
@Composable
private fun SpeedControl(controls: MotorControls) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        SignButton("−", "Speed down", controls.speedDown)
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(min = 40.dp)) {
            Text("Speed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = controls.speed?.toString() ?: "–",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
        SignButton("+", "Speed up", controls.speedUp)
    }
}

@Composable
private fun RoundButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    FilledTonalIconButton(onClick = onClick, modifier = Modifier.size(BAR_HEIGHT)) {
        Icon(icon, contentDescription = description)
    }
}

/** A round button with a sign for a label, for − and +. */
@Composable
private fun SignButton(sign: String, description: String, onClick: () -> Unit) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = Modifier.size(BAR_HEIGHT).semantics { contentDescription = description },
    ) {
        Text(sign, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun BarButton(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    FilledTonalButton(onClick = onClick, enabled = enabled, contentPadding = BAR_PADDING, modifier = Modifier.height(BAR_HEIGHT)) {
        Text(label)
    }
}

/** Stop in the colour of the STOP button; what it sends depends on the mode. */
@Composable
private fun StopButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
        contentPadding = BAR_PADDING,
        modifier = Modifier.height(BAR_HEIGHT),
    ) { Text("Stop") }
}

@Composable
private fun ModeLabel(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(horizontal = 6.dp))
}

private val BAR_HEIGHT = 44.dp
private val BAR_PADDING = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
