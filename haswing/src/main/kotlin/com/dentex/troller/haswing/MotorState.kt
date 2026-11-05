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

    /** Quality of the motor's GPS, or `null` before the first `RAW_IMU`. */
    val gpsQuality: GpsQuality? get() = gpsSignal?.let(GpsQuality::fromSignal)

    /** The mode the motor reports it is in, or `null` before the first `ATTITUDE`. */
    val reportedMode: ReportedMode? get() = boatModel?.let(ReportedMode::fromCode)
}

/**
 * The mode the motor reports in `ATTITUDE.pitchspeed`. The codes are the ones that
 * `PMainFragment.boatModelState` of the Helmsman application tells apart; they differ from the
 * command values of [MotorMode].
 */
enum class ReportedMode(val code: Int, val displayName: String) {
    MANUAL(0, "Manual"),
    ANCHOR(1, "Anchor"),
    NAVIGATION(2, "Navigation"),
    CRUISE(3, "Cruise"),
    RECORD(4, "Recording a route"),
    PEDAL(7, "Foot pedal"),
    REMOTE(32, "Remote control"),
    OTHER(-1, "Unknown"),
    ;

    companion object {
        fun fromCode(code: Int): ReportedMode = entries.firstOrNull { it.code == code } ?: OTHER
    }
}

/**
 * Quality of the motor's GPS, from the "GPS signal" value in `RAW_IMU.ygyro`.
 *
 * Lower is better and 250 means no fix. The bands are the ones `MainPresenter.gpsSignal` in the
 * Helmsman application uses: it shows a green icon below 171 and a red one from there on.
 */
enum class GpsQuality(val label: String, val usable: Boolean) {
    GOOD("good", true),
    FAIR("fair", true),
    POOR("poor", false),
    NO_FIX("no fix", false);

    companion object {
        /** Value the motor reports while it has no fix. */
        const val NO_FIX_SIGNAL = 250

        fun fromSignal(signal: Int): GpsQuality = when {
            signal >= NO_FIX_SIGNAL -> NO_FIX
            signal > 170 -> POOR
            signal >= 100 -> FAIR
            else -> GOOD
        }
    }
}

/** Outcome of a command that was sent to the motor. */
sealed interface CommandResult {
    data object Sent : CommandResult
    data class Rejected(val reason: String) : CommandResult
    data class Failed(val reason: String) : CommandResult
}
