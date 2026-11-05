package com.dentex.troller.chart

/** Where the position shown on the chart came from. */
enum class PositionSource(val property: String) {
    MOTOR("motor"),
    PHONE("phone"),
}

/** A position from the phone's own GPS. [bearingDegrees] is the course, only known while moving. */
data class PhoneFix(val latitude: Double, val longitude: Double, val bearingDegrees: Float?)

/** The position drawn on the chart. It is only displayed and never sent to the motor. */
data class BoatPosition(
    val latitude: Double,
    val longitude: Double,
    val headingDegrees: Float?,
    val source: PositionSource,
    val stale: Boolean,
) {
    companion object {
        /**
         * Prefers a fresh motor fix, then a fresh phone fix. When neither is fresh, the last known
         * one is shown as stale, the motor first.
         */
        fun choose(
            motorLatitude: Double?,
            motorLongitude: Double?,
            motorHeading: Float?,
            motorFresh: Boolean,
            phone: PhoneFix?,
            phoneFresh: Boolean,
        ): BoatPosition? {
            val motor = if (motorLatitude != null && motorLongitude != null) {
                BoatPosition(motorLatitude, motorLongitude, motorHeading, PositionSource.MOTOR, stale = !motorFresh)
            } else {
                null
            }
            val fromPhone = phone?.let {
                BoatPosition(it.latitude, it.longitude, it.bearingDegrees, PositionSource.PHONE, stale = !phoneFresh)
            }
            return when {
                motor != null && motorFresh -> motor
                fromPhone != null && phoneFresh -> fromPhone
                else -> motor ?: fromPhone
            }
        }
    }
}
