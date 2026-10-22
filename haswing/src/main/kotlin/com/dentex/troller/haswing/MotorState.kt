package com.dentex.troller.haswing

/**
 * Identity of the connected motor, taken from its `HEARTBEAT` message.
 *
 * @param heartbeatType the `type` field of the heartbeat, used to select the motor profile
 * @param firmwareCustomMode the `custom_mode` field, which the motor uses as its firmware version
 * @param systemId the system id the motor sent the heartbeat from
 * @param componentId the component id the motor sent the heartbeat from
 * @param baseMode the raw `base_mode` field, which the motor uses as its disconnect protection flag
 */
data class MotorIdentity(
    val heartbeatType: Int,
    val firmwareCustomMode: Long,
    val systemId: Int,
    val componentId: Int,
    val baseMode: Int = 0,
) {
    /** The profile this identity maps to. */
    val profile: MotorProfile get() = MotorProfile.fromHeartbeatType(heartbeatType)

    /** Firmware version as reported by the motor, if it is a plausible version number. */
    val firmwareVersion: Int get() = firmwareCustomMode.toInt()
}

/**
 * Latest telemetry received from the motor.
 *
 * Every value is nullable: the motor only sends some of these messages, and a missing value must
 * be shown as "unknown" rather than as a zero. [lastUpdateMillis] and [lastHeartbeatMillis] allow
 * the UI to flag stale data.
 */
data class MotorTelemetry(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val altitude: Double? = null,
    val speedKmh: Float? = null,
    val headingDegrees: Float? = null,
    val batteryLevel: Int? = null,
    val batteryMilliVolts: Int? = null,
    val gpsSignal: Int? = null,
    val motorOn: Boolean? = null,
    val motorSpeed: Int? = null,
    val boatModel: Int? = null,
    val targetPoint: Int? = null,
    val mode: MotorMode? = null,
    val lastUpdateMillis: Long = 0L,
    val lastHeartbeatMillis: Long = 0L,
    val connected: Boolean = false,
) {
    /** True when no telemetry has been received for [staleAfterMillis]. */
    fun isStale(staleAfterMillis: Long, nowMillis: Long = System.currentTimeMillis()): Boolean =
        lastUpdateMillis > 0L && nowMillis - lastUpdateMillis > staleAfterMillis
}

/** Outcome of a command that was sent to the motor. */
sealed interface CommandResult {
    data object Sent : CommandResult
    data class Rejected(val reason: String) : CommandResult
    data class Failed(val reason: String) : CommandResult
}
