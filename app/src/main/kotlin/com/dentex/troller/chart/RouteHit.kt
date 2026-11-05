package com.dentex.troller.chart

import kotlin.math.hypot

/** Where a tap lands on a route drawn on screen. Coordinates are screen pixels. */
object RouteHit {

    /** A tap on the segment from waypoint [segment] to the next one, at [x], [y] on that segment. */
    data class OnSegment(val segment: Int, val x: Float, val y: Float)

    /**
     * The segment closest to ([x], [y]) within [reach] pixels, with the tapped spot moved onto it,
     * or null when the tap is farther from every segment.
     */
    fun segment(points: List<Pair<Float, Float>>, x: Float, y: Float, reach: Float): OnSegment? {
        var best: OnSegment? = null
        var bestDistance = reach
        for (i in 0 until points.size - 1) {
            val (ax, ay) = points[i]
            val (bx, by) = points[i + 1]
            val dx = bx - ax
            val dy = by - ay
            val lengthSquared = dx * dx + dy * dy
            val t = if (lengthSquared == 0f) 0f else (((x - ax) * dx + (y - ay) * dy) / lengthSquared).coerceIn(0f, 1f)
            val px = ax + t * dx
            val py = ay + t * dy
            val distance = hypot(x - px, y - py)
            if (distance <= bestDistance) {
                bestDistance = distance
                best = OnSegment(i, px, py)
            }
        }
        return best
    }
}
