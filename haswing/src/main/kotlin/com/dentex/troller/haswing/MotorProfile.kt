package com.dentex.troller.haswing

/**
 * Motor profiles that the Helmsman application can talk to.
 *
 * The profile is selected from the `type` field of the motor's `HEARTBEAT` message, exactly as
 * `im.helmsman.lib.factory.ProductFactory` does in the original application.
 */
enum class MotorProfile(
    /** Value of the heartbeat `type` field that identifies this profile. */
    val type: Int,
    val displayName: String,
) {
    /** Type 110, the `HMMotor50736` profile used by the Cayman-B and similar units. */
    TYPE_110(110, "Haswing 50736 (type 110)"),

    /** Type 11, the `HMMotorH1` profile, kept as a diagnostic fallback. */
    TYPE_11(11, "Haswing H1 (type 11)"),

    /** Any other heartbeat type; the Helmsman application creates no profile for it. */
    UNKNOWN(-1, "Unknown motor profile");

    companion object {
        /** Maps a heartbeat `type` to a profile, mirroring `ProductFactory.createProduct`. */
        fun fromHeartbeatType(type: Int): MotorProfile = when (type) {
            TYPE_110.type -> TYPE_110
            TYPE_11.type -> TYPE_11
            else -> UNKNOWN
        }
    }
}

/**
 * Operating modes of the type 110 motor.
 *
 * The values are the `COMMAND_LONG.command` identifiers that the Helmsman application sends in
 * `changeModel(int)`.
 */
enum class MotorMode(val command: Int, val displayName: String) {
    ANCHOR(12, "Anchor / spot lock"),
    MANUAL(13, "Manual"),
    CRUISE(14, "Cruise"),
    NAVIGATION(15, "Navigation"),
    RECORD_WAYPOINT(181, "Record waypoint"),
    REMOTE_CONTROL(6, "Remote control"),
    UNKNOWN(5, "Unknown"),
    ;

    companion object {
        /** Resolves a mode command value back to a mode. */
        fun fromCommand(command: Int): MotorMode = entries.firstOrNull { it.command == command } ?: UNKNOWN
    }
}

/**
 * Direct motor commands of the type 110 profile.
 *
 * These are the `COMMAND_LONG.command` values used by `HMMotor50736.MotorDriverComponent50736`.
 */
enum class MotorCommand(val value: Int) {
    FULL_SPEED(3),
    TURN_RIGHT(6),
    TURN_LEFT(7),
    SPEED_UP(8),
    SPEED_DOWN(9),
    MOTOR_ON(10),
    MOTOR_OFF(11),
}

/**
 * Commands of the alternative type 11 profile.
 *
 * These are the `COMMAND_SHORT.command` values used by `HMMotorH1.MotorDriverComponentH1`.
 */
enum class ShortMotorCommand(val value: Int) {
    TURN_LEFT(0),
    TURN_RIGHT(1),
    SPEED_UP(2),
    SPEED_DOWN(3),
}
