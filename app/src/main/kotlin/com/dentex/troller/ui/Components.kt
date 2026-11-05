package com.dentex.troller.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
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
