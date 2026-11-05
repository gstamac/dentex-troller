package com.dentex.troller.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/** A titled card with a label and a value, used across the telemetry screens. */
@Composable
fun TelemetryCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Color? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = accent?.copy(alpha = 0.12f) ?: MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

/** A row of telemetry cards. */
@Composable
fun TelemetryRow(
    modifier: Modifier = Modifier,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}

/** A section header. */
@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(top = 8.dp, bottom = 4.dp),
    )
}

/** Monospaced text used for the raw traffic log. */
@Composable
fun MonospaceText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
        modifier = modifier,
    )
}

/** The full-width emergency stop, the same red STOP button as on the controls screen. */
@Composable
fun EmergencyStopButton(onStop: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onStop,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
        modifier = modifier.fillMaxWidth(),
    ) { Text("STOP") }
}

/** The emergency stop drawn as a STOP traffic sign, for screens where a full-width button would cover the content. */
@Composable
fun StopSignButton(onStop: () -> Unit, modifier: Modifier = Modifier) {
    val measurer = rememberTextMeasurer()
    val label = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
    Canvas(
        modifier = modifier
            .size(STOP_SIGN_SIZE)
            .clickable(role = Role.Button, onClickLabel = "Stop the motor", onClick = onStop)
            .semantics { contentDescription = "Stop the motor" },
    ) {
        val outer = octagon(size.minDimension / 2)
        drawPath(outer, Color.White)
        drawPath(octagon(size.minDimension / 2 * 0.88f), STOP_RED)
        val text = measurer.measure("STOP", label)
        drawText(text, topLeft = Offset((size.width - text.size.width) / 2, (size.height - text.size.height) / 2))
    }
}

private fun DrawScope.octagon(radius: Float): Path = Path().apply {
    // Flat top and bottom, like the road sign: corners at 22.5° + n·45°.
    for (i in 0 until 8) {
        val angle = Math.toRadians(22.5 + i * 45.0)
        val x = center.x + radius * cos(angle).toFloat()
        val y = center.y + radius * sin(angle).toFloat()
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

private val STOP_SIGN_SIZE = 60.dp
private val STOP_RED = Color(0xFFC8102E)

/** Shown instead of the STOP sign while there is no link to the motor; a crossed-out Bluetooth badge. */
@Composable
fun NoConnectionButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .size(STOP_SIGN_SIZE)
            .clickable(role = Role.Button, onClickLabel = "Open the connect screen", onClick = onClick)
            .semantics { contentDescription = "No connection to the motor. Open the connect screen" },
    ) {
        val s = size.minDimension
        drawCircle(Color.White, radius = s / 2)
        drawCircle(NO_LINK_GREY, radius = s / 2 * 0.88f)
        drawPath(bluetoothRune(s), Color.White, style = Stroke(width = s * 0.06f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawLine(NO_LINK_GREY, Offset(s * 0.24f, s * 0.24f), Offset(s * 0.76f, s * 0.76f), strokeWidth = s * 0.14f)
        drawLine(Color.White, Offset(s * 0.24f, s * 0.24f), Offset(s * 0.76f, s * 0.76f), strokeWidth = s * 0.06f, cap = StrokeCap.Round)
    }
}

private val NO_LINK_GREY = Color(0xFF5F6B73)

/** Shown instead of the STOP sign while the app reconnects; a Bluetooth badge with a turning arc. */
@Composable
fun ReconnectingButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val turn by rememberInfiniteTransition(label = "reconnecting").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1_200, easing = LinearEasing)),
        label = "arc",
    )
    Canvas(
        modifier = modifier
            .size(STOP_SIGN_SIZE)
            .clickable(role = Role.Button, onClickLabel = "Open the connect screen", onClick = onClick)
            .semantics { contentDescription = "Reconnecting to the motor. Open the connect screen" },
    ) {
        val s = size.minDimension
        drawCircle(Color.White, radius = s / 2)
        drawCircle(RECONNECT_BLUE, radius = s / 2 * 0.88f)
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

private fun bluetoothRune(s: Float): Path = Path().apply {
    moveTo(s * 0.34f, s * 0.35f)
    lineTo(s * 0.66f, s * 0.63f)
    lineTo(s * 0.5f, s * 0.79f)
    lineTo(s * 0.5f, s * 0.21f)
    lineTo(s * 0.66f, s * 0.37f)
    lineTo(s * 0.34f, s * 0.65f)
}

private val RECONNECT_BLUE = Color(0xFF1F6FB2)
