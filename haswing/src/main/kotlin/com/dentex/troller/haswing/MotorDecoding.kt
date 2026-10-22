package com.dentex.troller.haswing

import com.dentex.troller.mavlink.Attitude
import com.dentex.troller.mavlink.CommandLong
import com.dentex.troller.mavlink.GlobalPositionInt
import com.dentex.troller.mavlink.Heartbeat
import com.dentex.troller.mavlink.MavlinkMessage
import com.dentex.troller.mavlink.MissionAck
import com.dentex.troller.mavlink.MissionCount
import com.dentex.troller.mavlink.MissionItem
import com.dentex.troller.mavlink.MissionRequest
import com.dentex.troller.mavlink.RawImu
import com.dentex.troller.mavlink.SysStatus
import com.dentex.troller.mavlink.TrollingMotorStatus
import kotlin.math.sqrt

/**
 * Decodes the messages that the type 110 motor sends into telemetry.
 *
 * The field mapping is the one used by `HMMotor50736.onReceiverData` in the Helmsman application:
 *
 * - `RAW_IMU` carries the GPS signal, the motor speed and the motor on/off marker.
 * - `ATTITUDE` carries the heading in radians, the battery level and the boat model.
 * - `GLOBAL_POSITION_INT` carries the position and the speed.
 *
 * The motor reuses standard MAVLink messages for non standard purposes, so a value that has not
 * been verified against real hardware is reported as observed rather than as a named quantity.
 */
class TelemetryDecoder {

    private var lastSpeedKmh: Float = 0f

    /** Resets the smoothing state; call this when the link is re-established. */
    fun reset() {
        lastSpeedKmh = 0f
    }

    /**
     * Folds [message] into [current] and returns the updated telemetry.
     */
    fun apply(message: MavlinkMessage, current: MotorTelemetry, nowMillis: Long): MotorTelemetry =
        when (message) {
            is Heartbeat -> current.copy(
                lastHeartbeatMillis = nowMillis,
                lastUpdateMillis = nowMillis,
            )

            is SysStatus -> current.copy(
                batteryMilliVolts = message.voltageBattery,
                lastUpdateMillis = nowMillis,
            )

            is RawImu -> current.copy(
                gpsSignal = message.ygyro,
                targetPoint = message.zacc,
                motorSpeed = message.zgyro,
                motorOn = message.xgyro == RawImu.MOTOR_ON_MARKER,
                lastUpdateMillis = nowMillis,
            )

            is Attitude -> current.copy(
                headingDegrees = Math.toDegrees(message.yaw.toDouble()).toFloat(),
                batteryLevel = message.pitch.toInt(),
                boatModel = message.pitchSpeed.toInt(),
                lastUpdateMillis = nowMillis,
            )

            is GlobalPositionInt -> {
                val speedKmh = smoothSpeed(message.vx, message.vy)
                current.copy(
                    latitude = message.latitude / GlobalPositionInt.COORDINATE_SCALE,
                    longitude = message.longitude / GlobalPositionInt.COORDINATE_SCALE,
                    altitude = message.altitude / 1000.0,
                    speedKmh = speedKmh,
                    lastUpdateMillis = nowMillis,
                )
            }

            is TrollingMotorStatus -> current.copy(
                batteryMilliVolts = message.voltageBattery,
                motorOn = message.isMotorOn != 0,
                motorSpeed = message.motorSpeed,
                lastUpdateMillis = nowMillis,
            )

            else -> current
        }

    /**
     * Converts the horizontal velocity into km/h.
     *
     * The Helmsman application converts centimetres per second into km/h and then smooths the
     * result with an exponential filter, clamping values below 0.5 km/h to zero.
     */
    private fun smoothSpeed(vx: Int, vy: Int): Float {
        val speedX = (vx * 3.6f) / 100.0f
        val speedY = (vy * 3.6f) / 100.0f
        val magnitude = sqrt(speedX * speedX + speedY * speedY)
        val smoothed = (lastSpeedKmh * 0.45f) + (magnitude * 0.55f)
        lastSpeedKmh = if (smoothed < 0.5f) 0f else smoothed
        return lastSpeedKmh
    }
}

/**
 * Extracts the mission traffic from the message stream.
 *
 * The Helmsman application uses `MISSION_COUNT`, `MISSION_REQUEST`, `MISSION_ITEM` and
 * `MISSION_ACK` for both the route download and the route upload.
 */
object MissionTraffic {

    /** Number of waypoints the motor reported, or `null` when the message is not a count. */
    fun waypointCount(message: MavlinkMessage): Int? =
        (message as? MissionCount)?.count

    /** Sequence number the motor asked for, or `null` when the message is not a request. */
    fun requestedSequence(message: MavlinkMessage): Int? =
        (message as? MissionRequest)?.sequence

    /** Waypoint the motor sent, or `null` when the message is not an item. */
    fun waypoint(message: MavlinkMessage): MissionItem? = message as? MissionItem

    /** True when the message acknowledges a completed route transfer. */
    fun isAcknowledgement(message: MavlinkMessage): Boolean =
        message is MissionAck && message.type == MissionAck.MAV_MISSION_ACCEPTED
}

/**
 * Builds the `COMMAND_LONG` message for a type 110 command.
 *
 * The Helmsman application leaves `target_system`, `target_component` and `confirmation` at zero
 * and only sets `param1` to the PWM value for the steering commands when the firmware version is
 * [PWM_FIRMWARE_VERSION].
 */
object MotorCommandFactory {

    /** Firmware version that expects a PWM value in `param1` for steering commands. */
    const val PWM_FIRMWARE_VERSION: Int = 102

    /** Default PWM value used by the Helmsman application. */
    const val DEFAULT_PWM: Float = 90f

    /** Builds a motor command for the given firmware version. */
    fun command(command: MotorCommand, firmwareVersion: Int, pwm: Float = DEFAULT_PWM): CommandLong {
        val needsPwm = command == MotorCommand.TURN_LEFT || command == MotorCommand.TURN_RIGHT
        return CommandLong(
            command = command.value,
            param1 = if (needsPwm && firmwareVersion == PWM_FIRMWARE_VERSION) pwm else 0f,
        )
    }

    /** Builds a mode change command. */
    fun mode(mode: MotorMode): CommandLong = CommandLong(command = mode.command)
}
