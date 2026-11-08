package com.dentex.troller.chart

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/** The scale line on the chart: a round distance and how long it is drawn. */
data class MapScale(
    /** The distance the line stands for. */
    val meters: Double,
    /** How long the line is, in the same units as the length it was fitted into. */
    val length: Double,
) {
    /** "50 m", "200 m", "1 km", "20 km". */
    val label: String
        get() = if (meters < 1000) "${meters.toInt()} m" else "${(meters / 1000).toInt()} km"

    companion object {
        /** The steps within each power of ten. */
        private val STEPS = listOf(1.0, 2.0, 5.0)

        /**
         * The longest round distance (1, 2 or 5 times a power of ten, at least 1 m) that fits in
         * [maxLength], with [metersPerUnit] metres to one unit of length. Null when even 1 m is
         * longer than [maxLength] or the scale is unknown.
         */
        fun fit(metersPerUnit: Double, maxLength: Double): MapScale? {
            if (!(metersPerUnit > 0) || !(maxLength > 0) || metersPerUnit.isInfinite()) return null
            val most = metersPerUnit * maxLength
            if (most < 1.0) return null
            val power = 10.0.pow(floor(log10(most)))
            val meters = STEPS.map { it * power }.last { it <= most * (1 + 1e-9) }
            return MapScale(meters, meters / metersPerUnit)
        }    }
}
