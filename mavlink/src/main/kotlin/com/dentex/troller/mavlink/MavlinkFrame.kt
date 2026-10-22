package com.dentex.troller.mavlink

/**
 * A decoded MAVLink v1 frame.
 *
 * @param sequence frame sequence number
 * @param systemId sender system id
 * @param componentId sender component id
 * @param messageId message id
 * @param payload raw payload bytes, exactly [payloadLength] long
 * @param checksum the 16 bit checksum that was accepted for this frame
 */
data class MavlinkFrame(
    val sequence: Int,
    val systemId: Int,
    val componentId: Int,
    val messageId: Int,
    val payload: ByteArray,
    val checksum: Int,
) {
    /** Name of the message, taken from the dialect table. */
    val messageName: String get() = MavlinkDialect.name(messageId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MavlinkFrame) return false
        return sequence == other.sequence &&
            systemId == other.systemId &&
            componentId == other.componentId &&
            messageId == other.messageId &&
            payload.contentEquals(other.payload) &&
            checksum == other.checksum
    }

    override fun hashCode(): Int {
        var result = sequence
        result = 31 * result + systemId
        result = 31 * result + componentId
        result = 31 * result + messageId
        result = 31 * result + payload.contentHashCode()
        result = 31 * result + checksum
        return result
    }

    override fun toString(): String =
        "MavlinkFrame($messageName seq=$sequence sys=$systemId comp=$componentId len=${payload.size})"
}
