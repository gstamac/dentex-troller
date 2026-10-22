package com.dentex.troller.mavlink

/**
 * `COMMAND_LONG` (message id 76).
 *
 * This is the command channel of the type 110 motor profile. The Helmsman application only ever
 * sets the [command] field, and sets `param1` to the PWM value for the steering commands when the
 * firmware version is 102. `targetSystem`, `targetComponent` and `confirmation` stay at zero.
 */
data class CommandLong(
    val command: Int = 0,
    val param1: Float = 0f,
    val param2: Float = 0f,
    val param3: Float = 0f,
    val param4: Float = 0f,
    val param5: Float = 0f,
    val param6: Float = 0f,
    val param7: Float = 0f,
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
    val confirmation: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeFloat(param1)
        writeFloat(param2)
        writeFloat(param3)
        writeFloat(param4)
        writeFloat(param5)
        writeFloat(param6)
        writeFloat(param7)
        writeShort(command)
        writeByte(targetSystem)
        writeByte(targetComponent)
        writeByte(confirmation)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 76
        const val PAYLOAD_LENGTH: Int = 33

        /** Full speed. */
        const val COMMAND_FULL_SPEED: Int = 3

        /** Turn right. */
        const val COMMAND_TURN_RIGHT: Int = 6

        /** Turn left. */
        const val COMMAND_TURN_LEFT: Int = 7

        /** Increase speed by one step. */
        const val COMMAND_SPEED_UP: Int = 8

        /** Decrease speed by one step. */
        const val COMMAND_SPEED_DOWN: Int = 9

        /** Enable propulsion. */
        const val COMMAND_MOTOR_ON: Int = 10

        /** Disable propulsion. */
        const val COMMAND_MOTOR_OFF: Int = 11

        fun decode(reader: PayloadReader) = CommandLong(
            param1 = reader.readFloat(),
            param2 = reader.readFloat(),
            param3 = reader.readFloat(),
            param4 = reader.readFloat(),
            param5 = reader.readFloat(),
            param6 = reader.readFloat(),
            param7 = reader.readFloat(),
            command = reader.readShort(),
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
            confirmation = reader.readByte(),
        )
    }
}

/**
 * `COMMAND_SHORT` (message id 183), part of the Helmsman dialect.
 *
 * Used by the alternative type 11 motor profile.
 */
data class CommandShort(
    val command: Int = 0,
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeShort(command)
        writeByte(targetSystem)
        writeByte(targetComponent)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 183
        const val PAYLOAD_LENGTH: Int = 4

        /** Turn left. */
        const val COMMAND_TURN_LEFT: Int = 0

        /** Turn right. */
        const val COMMAND_TURN_RIGHT: Int = 1

        /** Increase speed by one step. */
        const val COMMAND_SPEED_UP: Int = 2

        /** Decrease speed by one step. */
        const val COMMAND_SPEED_DOWN: Int = 3

        fun decode(reader: PayloadReader) = CommandShort(
            command = reader.readShort(),
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
        )
    }
}

/** `CONTROL_COMMAND` (message id 168), part of the Helmsman dialect. */
data class ControlCommand(
    val command: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeByte(command)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 168
        const val PAYLOAD_LENGTH: Int = 1

        fun decode(reader: PayloadReader) = ControlCommand(command = reader.readByte())
    }
}

/** `MODE_ENABLE` (message id 182), part of the Helmsman dialect. */
data class ModeEnable(
    val controlModeEnableFlag: Int = 0,
    val functionEnableFlag: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeShort(controlModeEnableFlag)
        writeShort(functionEnableFlag)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 182
        const val PAYLOAD_LENGTH: Int = 4

        fun decode(reader: PayloadReader) = ModeEnable(
            controlModeEnableFlag = reader.readShort(),
            functionEnableFlag = reader.readShort(),
        )
    }
}

/** `REALTIME_DATA` (message id 184), part of the Helmsman dialect. */
data class RealtimeData(
    val param1: Int = 0,
    val param2: Int = 0,
    val param3: Float = 0f,
    val param4: Float = 0f,
    val param5: Long = 0,
    val param6: Long = 0,
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeFloat(param3)
        writeFloat(param4)
        writeInt(param5.toInt())
        writeInt(param6.toInt())
        writeShort(param1)
        writeShort(param2)
        writeByte(targetSystem)
        writeByte(targetComponent)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 184
        const val PAYLOAD_LENGTH: Int = 22

        fun decode(reader: PayloadReader) = RealtimeData(
            param3 = reader.readFloat(),
            param4 = reader.readFloat(),
            param5 = reader.readInt().toLong() and 0xFFFFFFFFL,
            param6 = reader.readInt().toLong() and 0xFFFFFFFFL,
            param1 = reader.readShort(),
            param2 = reader.readShort(),
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
        )
    }
}

/** `TROLLING_MOTOR_STATUS` (message id 185), part of the Helmsman dialect. */
data class TrollingMotorStatus(
    val motorOutput: Int = 0,
    val steeringMotorOutput: Int = 0,
    val voltageBattery: Int = 0,
    val currentBattery: Int = 0,
    val componentPresent: Int = 0,
    val componentHealth: Int = 0,
    val modeFlag1: Int = 0,
    val modeFlag2: Int = 0,
    val error: Int = 0,
    val isMotorOn: Int = 0,
    val motorSpeed: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeShort(motorOutput)
        writeShort(steeringMotorOutput)
        writeShort(voltageBattery)
        writeShort(currentBattery)
        writeShort(componentPresent)
        writeShort(componentHealth)
        writeShort(modeFlag1)
        writeShort(modeFlag2)
        writeShort(error)
        writeByte(isMotorOn)
        writeByte(motorSpeed)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 185
        const val PAYLOAD_LENGTH: Int = 20

        fun decode(reader: PayloadReader) = TrollingMotorStatus(
            motorOutput = reader.readShort(),
            steeringMotorOutput = reader.readShort(),
            voltageBattery = reader.readShort(),
            currentBattery = reader.readShort(),
            componentPresent = reader.readShort(),
            componentHealth = reader.readShort(),
            modeFlag1 = reader.readShort(),
            modeFlag2 = reader.readShort(),
            error = reader.readShort(),
            isMotorOn = reader.readByte(),
            motorSpeed = reader.readByte(),
        )
    }
}

/** `NEW_COMMAND_LONG` (message id 186), part of the Helmsman dialect. */
data class NewCommandLong(
    val command: Int = 0,
    val param1: Float = 0f,
    val param2: Float = 0f,
    val param3: Float = 0f,
    val param4: Float = 0f,
    val param5: Long = 0,
    val param6: Long = 0,
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
    val confirmation: Int = 0,
    val sequence: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeFloat(param1)
        writeFloat(param2)
        writeFloat(param3)
        writeFloat(param4)
        writeInt(param5.toInt())
        writeInt(param6.toInt())
        writeShort(command)
        writeByte(targetSystem)
        writeByte(targetComponent)
        writeByte(confirmation)
        writeByte(sequence)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 186
        const val PAYLOAD_LENGTH: Int = 30

        fun decode(reader: PayloadReader) = NewCommandLong(
            param1 = reader.readFloat(),
            param2 = reader.readFloat(),
            param3 = reader.readFloat(),
            param4 = reader.readFloat(),
            param5 = reader.readInt().toLong() and 0xFFFFFFFFL,
            param6 = reader.readInt().toLong() and 0xFFFFFFFFL,
            command = reader.readShort(),
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
            confirmation = reader.readByte(),
            sequence = reader.readByte(),
        )
    }
}

/** `NEW_COMMAND_LONG_ACK` (message id 187), part of the Helmsman dialect. */
data class NewCommandLongAck(
    val command: Int = 0,
    val result: Int = 0,
    val sequence: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeShort(command)
        writeByte(result)
        writeByte(sequence)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 187
        const val PAYLOAD_LENGTH: Int = 4

        fun decode(reader: PayloadReader) = NewCommandLongAck(
            command = reader.readShort(),
            result = reader.readByte(),
            sequence = reader.readByte(),
        )
    }
}
