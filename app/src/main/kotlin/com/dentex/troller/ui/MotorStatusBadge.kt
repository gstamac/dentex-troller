package com.dentex.troller.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.dentex.troller.haswing.MotorTelemetry
import com.dentex.troller.haswing.ReportedMode

/** What the status badge at the top right of the chart shows. */
internal sealed interface MotorStatus {
    /** No Bluetooth link to the remote; [connecting] while one is being made. */
    data class Disconnected(val connecting: Boolean) : MotorStatus

    /** The remote is connected, but no type 110 motor has answered (or it stopped answering). */
    data object NoMotor : MotorStatus

    /** The motor answers: its [state] and the speed step it reports. */
    data class Motor(val state: MotorRunState, val speed: Int?, val modeName: String?) : MotorStatus

    companion object {
        /** The motor's state from its telemetry. */
        fun motor(telemetry: MotorTelemetry): Motor {
            val state = when (telemetry.reportedMode) {
                ReportedMode.NAVIGATION -> MotorRunState.NAVIGATION
                ReportedMode.ANCHOR -> MotorRunState.ANCHOR
                ReportedMode.MANUAL, null -> if (telemetry.motorOn == true) MotorRunState.MANUAL else MotorRunState.STOPPED
                else -> MotorRunState.OTHER
            }
            return Motor(state, telemetry.motorSpeed, telemetry.reportedMode?.displayName)
        }
    }
}

/** The states the badge tells apart, each with its colour and label. */
internal enum class MotorRunState(val color: Color, val label: String) {
    STOPPED(Color(0xFF455A64), "Stopped"),
    MANUAL(Color(0xFF2E7D32), "Manual"),
    ANCHOR(Color(0xFF00897B), "Anchored"),
    NAVIGATION(Color(0xFF1F6FB2), "Navigation"),
    OTHER(Color(0xFF6A4C93), "Motor"),
}

/**
 * The motor status at the top right of the chart: the link, and once the motor answers its state
 * and its speed as a ring of [SPEED_STEPS] dashes. It only shows the status; tapping it opens the
 * connect screen. Stop is on the motor control bar.
 */
@Composable
internal fun MotorStatusBadge(status: MotorStatus, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val label = when (status) {
        is MotorStatus.Disconnected -> if (status.connecting) "Connecting…" else "No connection"
        MotorStatus.NoMotor -> "No motor"
        is MotorStatus.Motor -> {
            val name = if (status.state == MotorRunState.OTHER) status.modeName ?: status.state.label else status.state.label
            if (status.state == MotorRunState.STOPPED || status.speed == null) name else "$name · ${status.speed}"
        }
    }
    // The label is only read out by screen readers; on screen the icon says it all.
    Box(
        modifier = modifier
            .clickable(role = Role.Button, onClickLabel = "Open the connect screen", onClick = onClick)
            .semantics { contentDescription = "Motor status: $label. Open the connect screen" },
    ) {
        when (status) {
            is MotorStatus.Disconnected -> if (status.connecting) ConnectingIcon() else DisconnectedIcon()
            MotorStatus.NoMotor -> NoMotorIcon()
            is MotorStatus.Motor -> MotorIcon(status)
        }
    }
}

@Composable
private fun DisconnectedIcon() {
    Canvas(modifier = Modifier.size(BADGE_SIZE)) {
        val s = size.minDimension
        drawCircle(Color.White, radius = s / 2)
        drawCircle(NO_LINK_GREY, radius = s / 2 * 0.88f)
        drawPath(bluetoothRune(s), Color.White, style = Stroke(width = s * 0.06f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawLine(NO_LINK_GREY, Offset(s * 0.24f, s * 0.24f), Offset(s * 0.76f, s * 0.76f), strokeWidth = s * 0.14f)
        drawLine(Color.White, Offset(s * 0.24f, s * 0.24f), Offset(s * 0.76f, s * 0.76f), strokeWidth = s * 0.06f, cap = StrokeCap.Round)
    }
}

@Composable
private fun ConnectingIcon() {
    val turn by rememberInfiniteTransition(label = "connecting").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1_200, easing = LinearEasing)),
        label = "arc",
    )
    Canvas(modifier = Modifier.size(BADGE_SIZE)) {
        val s = size.minDimension
        drawCircle(Color.White, radius = s / 2)
        drawCircle(CONNECTING_BLUE, radius = s / 2 * 0.88f)
        drawPath(bluetoothRune(s), Color.White, style = Stroke(width = s * 0.06f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        val inset = s * 0.1f
        drawArc(
            color = Color.White,
            startAngle = turn,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = Size(s - 2 * inset, s - 2 * inset),
            style = Stroke(width = s * 0.05f, cap = StrokeCap.Round),
        )
    }
}

/** The remote is there but the motor is not: the Bluetooth rune with a question mark. */
@Composable
private fun NoMotorIcon() {
    Canvas(modifier = Modifier.size(BADGE_SIZE)) {
        val s = size.minDimension
        drawCircle(Color.White, radius = s / 2)
        drawCircle(NO_MOTOR_AMBER, radius = s / 2 * 0.88f)
        val stroke = Stroke(width = s * 0.06f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        translate(left = -s * 0.1f) { drawPath(bluetoothRune(s), Color.White, style = stroke) }
        // A question mark to the right of the rune.
        val mark = Path().apply {
            moveTo(s * 0.6f, s * 0.42f)
            cubicTo(s * 0.6f, s * 0.3f, s * 0.78f, s * 0.3f, s * 0.78f, s * 0.42f)
            cubicTo(s * 0.78f, s * 0.5f, s * 0.69f, s * 0.5f, s * 0.69f, s * 0.58f)
        }
        drawPath(mark, Color.White, style = stroke)
        drawCircle(Color.White, radius = s * 0.04f, center = Offset(s * 0.69f, s * 0.7f))
    }
}

/** The motor's state in the middle and its speed as dashes around it, filled up to the speed step. */
@Composable
private fun MotorIcon(status: MotorStatus.Motor) {
    Canvas(modifier = Modifier.size(BADGE_SIZE)) {
        val s = size.minDimension
        val color = status.state.color
        drawCircle(Color.White, radius = s / 2)
        // The speed ring: SPEED_STEPS dashes clockwise from the top, the first [speed] filled.
        val ring = s * 0.09f
        val radius = s / 2 - ring / 2 - s * 0.02f
        val step = 360f / SPEED_STEPS
        val speed = (status.speed ?: 0).coerceIn(0, SPEED_STEPS)
        for (i in 0 until SPEED_STEPS) {
            drawArc(
                color = if (i < speed) color else DASH_EMPTY,
                startAngle = -90f + i * step + DASH_GAP_DEGREES / 2,
                sweepAngle = step - DASH_GAP_DEGREES,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = ring),
            )
        }
        drawCircle(color, radius = s * 0.32f)
        val r = s * 0.32f
        when (status.state) {
            MotorRunState.STOPPED -> stopGlyph(r)
            MotorRunState.MANUAL -> propellerGlyph(r, hub = color)
            MotorRunState.ANCHOR -> anchorGlyph(r)
            MotorRunState.NAVIGATION -> navigationGlyph(r)
            MotorRunState.OTHER -> drawCircle(Color.White, radius = r * 0.25f)
        }
    }
}

private fun bluetoothRune(s: Float): Path = Path().apply {
    moveTo(s * 0.34f, s * 0.35f)
    lineTo(s * 0.66f, s * 0.63f)
    lineTo(s * 0.5f, s * 0.79f)
    lineTo(s * 0.5f, s * 0.21f)
    lineTo(s * 0.66f, s * 0.37f)
    lineTo(s * 0.34f, s * 0.65f)
}

/** The motor's speed steps, 0 to 10, as Helmsman draws them (icon_motorspeed0 … 10). */
internal const val SPEED_STEPS = 10

private val BADGE_SIZE = 60.dp
private const val DASH_GAP_DEGREES = 9f
private val DASH_EMPTY = Color(0xFFCFD8DC)
private val NO_LINK_GREY = Color(0xFF5F6B73)
private val CONNECTING_BLUE = Color(0xFF1F6FB2)
private val NO_MOTOR_AMBER = Color(0xFFC77700)
