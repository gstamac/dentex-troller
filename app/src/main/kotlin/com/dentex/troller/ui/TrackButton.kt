package com.dentex.troller.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Starts and stops recording the track. The icon is a winding trail, unlike the route button's
 * straight legs between waypoints. Idle: a dashed trail that ends in a red record dot. Recording:
 * a red button with a solid trail and a stop square where the dot was.
 */
@Composable
internal fun TrackButton(recording: Boolean, onClick: () -> Unit) {
    val red = MaterialTheme.colorScheme.error
    val onRed = MaterialTheme.colorScheme.onError
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (recording) red else MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        shadowElevation = 2.dp,
        modifier = Modifier
            .size(48.dp)
            .semantics { contentDescription = if (recording) "Stop recording the track" else "Record the track" },
    ) {
        val ink = if (recording) onRed else MaterialTheme.colorScheme.onSurface
        Canvas(modifier = Modifier.padding(11.dp)) {
            val w = size.width
            val h = size.height
            val end = Offset(w * 0.8f, h * 0.2f)
            val trail = Path().apply {
                moveTo(w * 0.1f, h * 0.9f)
                cubicTo(w * 0.75f, h * 0.95f, w * 0.05f, h * 0.45f, w * 0.5f, h * 0.42f)
                quadraticTo(w * 0.72f, h * 0.4f, end.x - w * 0.12f, end.y + h * 0.1f)
            }
            val stroke = w * 0.09f
            drawPath(
                trail,
                ink,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round,
                    pathEffect = if (recording) null else PathEffect.dashPathEffect(floatArrayOf(stroke * 1.4f, stroke * 1.6f)),
                ),
            )
            if (recording) {
                val side = w * 0.3f
                drawRect(onRed, topLeft = Offset(end.x - side / 2, end.y - side / 2), size = Size(side, side))
            } else {
                drawCircle(red, radius = w * 0.17f, center = end)
            }
        }
    }
}
