package com.dentex.troller.ui

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

/*
 * The symbols of the motor status badge and the motor controls, drawn around the centre of the
 * draw scope within a radius r, in [ink]. The badge and the buttons share them, so a button shows
 * the same symbol as the state it switches to.
 */

/** A square: the propeller is off, or Stop. */
internal fun DrawScope.stopGlyph(r: Float, ink: Color = Color.White) {
    val side = r * 0.9f
    drawRoundRect(
        ink,
        topLeft = Offset(center.x - side / 2, center.y - side / 2),
        size = Size(side, side),
        cornerRadius = CornerRadius(r * 0.12f),
    )
}

/** A three-blade propeller: the motor runs under manual control, or Start. [hub] fills the hub's middle. */
internal fun DrawScope.propellerGlyph(r: Float, ink: Color = Color.White, hub: Color) {
    val length = r * 0.68f
    val width = r * 0.36f
    for (blade in 0 until 3) {
        // Each blade leans a little off the radius, as a propeller blade is pitched.
        rotate(degrees = blade * 120f + 15f, pivot = center) {
            drawOval(ink, topLeft = Offset(center.x - width / 2, center.y - length), size = Size(width, length))
        }
    }
    drawCircle(ink, radius = r * 0.2f)
    drawCircle(hub, radius = r * 0.08f)
}

/** An anchor: a ring, a shank with a stock, and curved arms. */
internal fun DrawScope.anchorGlyph(r: Float, ink: Color = Color.White) {
    val w = r * 0.14f
    val stroke = Stroke(width = w, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val top = center.y - r * 0.62f
    val bottom = center.y + r * 0.6f
    drawCircle(ink, radius = r * 0.13f, center = Offset(center.x, top + r * 0.13f), style = Stroke(width = w * 0.8f))
    drawLine(ink, Offset(center.x, top + r * 0.26f), Offset(center.x, bottom), strokeWidth = w, cap = StrokeCap.Round)
    drawLine(ink, Offset(center.x - r * 0.3f, top + r * 0.42f), Offset(center.x + r * 0.3f, top + r * 0.42f), strokeWidth = w, cap = StrokeCap.Round)
    val arms = Path().apply {
        moveTo(center.x - r * 0.6f, center.y + r * 0.05f)
        quadraticTo(center.x - r * 0.5f, bottom, center.x, bottom)
        quadraticTo(center.x + r * 0.5f, bottom, center.x + r * 0.6f, center.y + r * 0.05f)
    }
    drawPath(arms, ink, style = stroke)
}

/** A navigation arrow pointing up and to the right. */
internal fun DrawScope.navigationGlyph(r: Float, ink: Color = Color.White) {
    val arrow = Path().apply {
        moveTo(center.x + r * 0.55f, center.y - r * 0.55f)
        lineTo(center.x - r * 0.62f, center.y - r * 0.02f)
        lineTo(center.x - r * 0.05f, center.y + r * 0.02f)
        lineTo(center.x - r * 0.02f, center.y + r * 0.62f)
        close()
    }
    drawPath(arrow, ink)
}

/** A rabbit facing right, sitting: faster. */
internal fun DrawScope.rabbitGlyph(r: Float, ink: Color) {
    val c = center
    // Body, haunch and the head.
    drawOval(ink, topLeft = Offset(c.x - r * 0.62f, c.y - r * 0.05f), size = Size(r * 0.95f, r * 0.62f))
    drawCircle(ink, radius = r * 0.3f, center = Offset(c.x - r * 0.3f, c.y + r * 0.22f))
    drawCircle(ink, radius = r * 0.23f, center = Offset(c.x + r * 0.38f, c.y - r * 0.08f))
    // Two long ears leaning back.
    rotate(degrees = -28f, pivot = Offset(c.x + r * 0.3f, c.y - r * 0.2f)) {
        drawOval(ink, topLeft = Offset(c.x + r * 0.22f, c.y - r * 0.85f), size = Size(r * 0.17f, r * 0.68f))
    }
    rotate(degrees = -10f, pivot = Offset(c.x + r * 0.42f, c.y - r * 0.2f)) {
        drawOval(ink, topLeft = Offset(c.x + r * 0.36f, c.y - r * 0.88f), size = Size(r * 0.17f, r * 0.7f))
    }
    // Front paw and the tail.
    drawOval(ink, topLeft = Offset(c.x + r * 0.12f, c.y + r * 0.4f), size = Size(r * 0.32f, r * 0.16f))
    drawCircle(ink, radius = r * 0.12f, center = Offset(c.x - r * 0.68f, c.y + r * 0.08f))
}

/** A turtle facing right: slower. [shell] draws the lines on its shell. */
internal fun DrawScope.turtleGlyph(r: Float, ink: Color, shell: Color) {
    val c = center
    val base = c.y + r * 0.22f
    // The domed shell.
    val dome = Path().apply {
        moveTo(c.x - r * 0.72f, base)
        cubicTo(c.x - r * 0.68f, c.y - r * 0.62f, c.x + r * 0.5f, c.y - r * 0.62f, c.x + r * 0.54f, base)
        close()
    }
    drawPath(dome, ink)
    // Head on a short neck, and the legs.
    drawCircle(ink, radius = r * 0.17f, center = Offset(c.x + r * 0.76f, c.y + r * 0.02f))
    drawRect(ink, topLeft = Offset(c.x + r * 0.45f, c.y + r * 0.0f), size = Size(r * 0.3f, r * 0.2f))
    drawRoundRect(ink, topLeft = Offset(c.x - r * 0.52f, base - r * 0.05f), size = Size(r * 0.22f, r * 0.36f), cornerRadius = CornerRadius(r * 0.08f))
    drawRoundRect(ink, topLeft = Offset(c.x + r * 0.16f, base - r * 0.05f), size = Size(r * 0.22f, r * 0.36f), cornerRadius = CornerRadius(r * 0.08f))
    // The shell's rim and a plate.
    val line = Stroke(width = r * 0.07f, cap = StrokeCap.Round)
    drawLine(shell, Offset(c.x - r * 0.66f, base - r * 0.1f), Offset(c.x + r * 0.5f, base - r * 0.1f), strokeWidth = line.width, cap = StrokeCap.Round)
    drawCircle(shell, radius = r * 0.17f, center = Offset(c.x - r * 0.09f, c.y - r * 0.14f), style = line)
}

/** A curved arrow turning to the left, or to the right when [right]. */
internal fun DrawScope.turnGlyph(r: Float, ink: Color, right: Boolean) {
    val w = r * 0.16f
    // An arc over the top, from the bottom of one side to the other, with the head at its end.
    val sign = if (right) 1f else -1f
    val arc = Path().apply {
        moveTo(center.x - sign * r * 0.5f, center.y + r * 0.5f)
        cubicTo(
            center.x - sign * r * 0.55f, center.y - r * 0.35f,
            center.x + sign * r * 0.1f, center.y - r * 0.55f,
            center.x + sign * r * 0.35f, center.y - r * 0.2f,
        )
    }
    drawPath(arc, ink, style = Stroke(width = w, cap = StrokeCap.Round, join = StrokeJoin.Round))
    val tipX = center.x + sign * r * 0.62f
    val tipY = center.y + r * 0.05f
    val head = Path().apply {
        moveTo(tipX, tipY)
        lineTo(center.x + sign * r * 0.12f, center.y - r * 0.05f)
        lineTo(center.x + sign * r * 0.52f, center.y - r * 0.5f)
        close()
    }
    drawPath(head, ink)
}
