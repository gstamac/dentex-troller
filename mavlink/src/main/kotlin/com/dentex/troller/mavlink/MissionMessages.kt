package com.dentex.troller.mavlink

/** `MISSION_ITEM` (message id 39). */
data class MissionItem(
    val param1: Float = 0f,
    val param2: Float = 0f,
    val param3: Float = 0f,
    val param4: Float = 0f,
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val sequence: Int = 0,
    val command: Int = 0,
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
    val frame: Int = 0,
    val current: Int = 0,
    val autocontinue: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeFloat(param1)
        writeFloat(param2)
        writeFloat(param3)
        writeFloat(param4)
        writeFloat(x)
        writeFloat(y)
        writeFloat(z)
        writeShort(sequence)
        writeShort(command)
        writeByte(targetSystem)
        writeByte(targetComponent)
        writeByte(frame)
        writeByte(current)
        writeByte(autocontinue)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 39
        const val PAYLOAD_LENGTH: Int = 37

        fun decode(reader: PayloadReader) = MissionItem(
            param1 = reader.readFloat(),
            param2 = reader.readFloat(),
            param3 = reader.readFloat(),
            param4 = reader.readFloat(),
            x = reader.readFloat(),
            y = reader.readFloat(),
            z = reader.readFloat(),
            sequence = reader.readShort(),
            command = reader.readShort(),
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
            frame = reader.readByte(),
            current = reader.readByte(),
            autocontinue = reader.readByte(),
        )
    }
}

/** `MISSION_REQUEST` (message id 40). */
data class MissionRequest(
    val sequence: Int = 0,
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeShort(sequence)
        writeByte(targetSystem)
        writeByte(targetComponent)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 40
        const val PAYLOAD_LENGTH: Int = 4

        fun decode(reader: PayloadReader) = MissionRequest(
            sequence = reader.readShort(),
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
        )
    }
}

/** `MISSION_REQUEST_LIST` (message id 43). */
data class MissionRequestList(
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeByte(targetSystem)
        writeByte(targetComponent)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 43
        const val PAYLOAD_LENGTH: Int = 2

        fun decode(reader: PayloadReader) = MissionRequestList(
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
        )
    }
}

/** `MISSION_COUNT` (message id 44). */
data class MissionCount(
    val count: Int = 0,
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeShort(count)
        writeByte(targetSystem)
        writeByte(targetComponent)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 44
        const val PAYLOAD_LENGTH: Int = 4

        fun decode(reader: PayloadReader) = MissionCount(
            count = reader.readShort(),
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
        )
    }
}

/** `MISSION_CLEAR_ALL` (message id 45). */
data class MissionClearAll(
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeByte(targetSystem)
        writeByte(targetComponent)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 45
        const val PAYLOAD_LENGTH: Int = 2

        fun decode(reader: PayloadReader) = MissionClearAll(
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
        )
    }
}

/** `MISSION_ACK` (message id 47). */
data class MissionAck(
    val targetSystem: Int = 0,
    val targetComponent: Int = 0,
    val type: Int = 0,
) : MavlinkMessage {

    override val messageId: Int get() = MESSAGE_ID
    override val payloadLength: Int get() = PAYLOAD_LENGTH

    override fun pack(): ByteArray = PayloadWriter(PAYLOAD_LENGTH).apply {
        writeByte(targetSystem)
        writeByte(targetComponent)
        writeByte(type)
    }.toByteArray()

    companion object {
        const val MESSAGE_ID: Int = 47
        const val PAYLOAD_LENGTH: Int = 3

        /** `type` value reported when the motor accepted the route. */
        const val MAV_MISSION_ACCEPTED: Int = 0

        fun decode(reader: PayloadReader) = MissionAck(
            targetSystem = reader.readByte(),
            targetComponent = reader.readByte(),
            type = reader.readByte(),
        )
    }
}
