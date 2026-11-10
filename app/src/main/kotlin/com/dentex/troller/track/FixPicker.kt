package com.dentex.troller.track

import com.dentex.troller.chart.PositionSource
import com.dentex.troller.navigation.TrackPoint

/**
 * Picks which GPS the track follows, the way the chart picks the boat it draws: the motor's while
 * its position is fresh, otherwise the phone's. The motor's GPS is on the boat, so it wins.
 */
class FixPicker {

    private var motor: TrackPoint? = null
    private var phone: TrackPoint? = null

    /** A position from the motor. It always goes into the track. */
    fun motor(point: TrackPoint) {
        motor = point
    }

    /** A position from the phone. Whether it goes into the track: only while the motor has none. */
    fun phone(point: TrackPoint): Boolean {
        phone = point
        return !motorFresh(point.timeMillis)
    }

    /** The position to start a track with at [nowMillis], or null when no position is fresh. */
    fun freshest(nowMillis: Long): Pair<TrackPoint, PositionSource>? {
        val fromMotor = motor?.takeIf { motorFresh(nowMillis) }
        val fromPhone = phone?.takeIf { nowMillis - it.timeMillis <= PHONE_FRESH_MILLIS }
        return when {
            fromMotor != null -> fromMotor to PositionSource.MOTOR
            fromPhone != null -> fromPhone to PositionSource.PHONE
            else -> null
        }
    }

    private fun motorFresh(nowMillis: Long): Boolean =
        motor?.let { nowMillis - it.timeMillis <= MOTOR_FRESH_MILLIS } == true

    companion object {
        /** The chart shows the motor's position as stale after this long. */
        const val MOTOR_FRESH_MILLIS = 5_000L

        /** The chart shows the phone's position as stale after this long. */
        const val PHONE_FRESH_MILLIS = 10_000L
    }
}
