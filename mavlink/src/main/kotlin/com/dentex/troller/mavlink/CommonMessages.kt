package com.dentex.troller.mavlink

/** `HEARTBEAT` (message id 0). */
data class Heartbeat(
    val customMode: Long = 0,
    val type: Int = 0,
    val autopilot: Int = 0,
    val baseMode: Int = 0,
    val systemStatus: Int = 0,
    val mavlinkVersion: Int = 3,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeInt(customMode.toInt())
        writeByte(type)
        writeByte(autopilot)
        writeByte(baseMode)
        writeByte(systemStatus)
        writeByte(mavlinkVersion)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 0
        const val PAYLOAD_LENGTH: Int = 9

        /** `type` value the Helmsman application sends in its own heartbeats. */
        const val GCS_TYPE: Int = 106

        fun decode(reader: PayloadReader) = Heartbeat(
            customMode = reader.readInt().toLong() and 0xFFFFFFFFL,
            type = reader.readByte(),
            autopilot = reader.readByte(),
            baseMode = reader.readByte(),
            systemStatus = reader.readByte(),
            mavlinkVersion = reader.readByte(),
        )
    }
}

/** `SYS_STATUS` (message id 1). */
data class SysStatus(
    val onboardControlSensorsPresent: Long = 0,
    val onboardControlSensorsEnabled: Long = 0,
    val onboardControlSensorsHealth: Long = 0,
    val load: Int = 0,
    val voltageBattery: Int = 0,
    val currentBattery: Int = 0,
    val dropRateComm: Int = 0,
    val errorsComm: Int = 0,
    val errorsCount1: Int = 0,
    val errorsCount2: Int = 0,
    val errorsCount3: Int = 0,
    val errorsCount4: Int = 0,
    val batteryRemaining: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeInt(onboardControlSensorsPresent.toInt())
        writeInt(onboardControlSensorsEnabled.toInt())
        writeInt(onboardControlSensorsHealth.toInt())
        writeShort(load)
        writeShort(voltageBattery)
        writeShort(currentBattery)
        writeShort(dropRateComm)
        writeShort(errorsComm)
        writeShort(errorsCount1)
        writeShort(errorsCount2)
        writeShort(errorsCount3)
        writeShort(errorsCount4)
        writeByte(batteryRemaining)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 1
        const val PAYLOAD_LENGTH: Int = 31

        fun decode(reader: PayloadReader) = SysStatus(
            onboardControlSensorsPresent = reader.readInt().toLong() and 0xFFFFFFFFL,
            onboardControlSensorsEnabled = reader.readInt().toLong() and 0xFFFFFFFFL,
            onboardControlSensorsHealth = reader.readInt().toLong() and 0xFFFFFFFFL,
            load = reader.readShort(),
            voltageBattery = reader.readShort(),
            currentBattery = reader.readShort(),
            dropRateComm = reader.readShort(),
            errorsComm = reader.readShort(),
            errorsCount1 = reader.readShort(),
            errorsCount2 = reader.readShort(),
            errorsCount3 = reader.readShort(),
            errorsCount4 = reader.readShort(),
            batteryRemaining = reader.readByte(),
        )
    }
}

/**
 * `RAW_IMU` (message id 27).
 *
 * The motor reuses this message for non-IMU purposes. The Helmsman application reads
 * [ygyro] as the GPS signal strength, [zacc] as the target point, [zgyro] as the motor speed
 * and treats [xgyro] equal to [MOTOR_ON_MARKER] as "motor running".
 */
data class RawImu(
    val timeUsec: Long = 0,
    val xacc: Int = 0,
    val yacc: Int = 0,
    val zacc: Int = 0,
    val xgyro: Int = 0,
    val ygyro: Int = 0,
    val zgyro: Int = 0,
    val xmag: Int = 0,
    val ymag: Int = 0,
    val zmag: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeLong(timeUsec)
        writeShort(xacc)
        writeShort(yacc)
        writeShort(zacc)
        writeShort(xgyro)
        writeShort(ygyro)
        writeShort(zgyro)
        writeShort(xmag)
        writeShort(ymag)
        writeShort(zmag)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 27
        const val PAYLOAD_LENGTH: Int = 26

        /** Value of `xgyro` that the motor uses to signal that the propeller is running. */
        const val MOTOR_ON_MARKER: Int = 11

        fun decode(reader: PayloadReader) = RawImu(
            timeUsec = reader.readLong(),
            xacc = reader.readShort(),
            yacc = reader.readShort(),
            zacc = reader.readShort(),
            xgyro = reader.readShort(),
            ygyro = reader.readShort(),
            zgyro = reader.readShort(),
            xmag = reader.readShort(),
            ymag = reader.readShort(),
            zmag = reader.readShort(),
        )
    }
}

/**
 * `ATTITUDE` (message id 30).
 *
 * The motor reuses this message as well: [yaw] carries the boat heading in radians, [pitch]
 * carries the battery level and [pitchspeed] carries the boat model identifier.
 */
data class Attitude(
    val timeBootMs: Long = 0,
    val roll: Float = 0f,
    val pitch: Float = 0f,
    val yaw: Float = 0f,
    val rollSpeed: Float = 0f,
    val pitchSpeed: Float = 0f,
    val yawSpeed: Float = 0f,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeInt(timeBootMs.toInt())
        writeFloat(roll)
        writeFloat(pitch)
        writeFloat(yaw)
        writeFloat(rollSpeed)
        writeFloat(pitchSpeed)
        writeFloat(yawSpeed)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 30
        const val PAYLOAD_LENGTH: Int = 28

        fun decode(reader: PayloadReader) = Attitude(
            timeBootMs = reader.readInt().toLong() and 0xFFFFFFFFL,
            roll = reader.readFloat(),
            pitch = reader.readFloat(),
            yaw = reader.readFloat(),
            rollSpeed = reader.readFloat(),
            pitchSpeed = reader.readFloat(),
            yawSpeed = reader.readFloat(),
        )
    }
}

/**
 * `GLOBAL_POSITION_INT` (message id 33).
 *
 * Latitude and longitude are scaled by 1e7, velocity by 1 cm/s.
 */
data class GlobalPositionInt(
    val timeBootMs: Long = 0,
    val latitude: Int = 0,
    val longitude: Int = 0,
    val altitude: Int = 0,
    val relativeAltitude: Int = 0,
    val vx: Int = 0,
    val vy: Int = 0,
    val vz: Int = 0,
    val hdg: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeInt(timeBootMs.toInt())
        writeInt(latitude)
        writeInt(longitude)
        writeInt(altitude)
        writeInt(relativeAltitude)
        writeShort(vx)
        writeShort(vy)
        writeShort(vz)
        writeShort(hdg)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 33
        const val PAYLOAD_LENGTH: Int = 28

        /** Scale factor of the latitude and longitude fields. */
        const val COORDINATE_SCALE: Double = 1e7

        /** Scale factor of the velocity fields, in cm/s. */
        const val VELOCITY_SCALE: Double = 100.0

        fun decode(reader: PayloadReader) = GlobalPositionInt(
            timeBootMs = reader.readInt().toLong() and 0xFFFFFFFFL,
            latitude = reader.readInt(),
            longitude = reader.readInt(),
            altitude = reader.readInt(),
            relativeAltitude = reader.readInt(),
            vx = reader.readShort(),
            vy = reader.readShort(),
            vz = reader.readShort(),
            hdg = reader.readShort(),
        )
    }
}
