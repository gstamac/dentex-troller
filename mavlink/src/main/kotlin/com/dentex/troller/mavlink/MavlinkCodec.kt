package com.dentex.troller.mavlink

/**
 * Encodes and decodes MAVLink frames.
 *
 * The contract is deliberately narrow: the transport hands over raw bytes and receives raw bytes
 * back, and the MAVLink layer above deals only in frames.
 */
interface MavlinkCodec {

    /** Parses every complete frame contained in [bytes]. */
    fun consume(bytes: ByteArray): List<MavlinkFrame>

    /** Serialises [message] into a complete v1 frame. */
    fun encode(message: MavlinkMessage): ByteArray
}

/**
 * Default [MavlinkCodec] for the Helmsman dialect.
 *
 * Outgoing frames use `sysid = 255` and `compid = 190`, matching the Helmsman application. The
 * mission download request is the one exception and is sent with `sysid = 106`, `compid = 200`.
 */
class HelmsmanMavlinkCodec(
    private val defaultHeader: MavlinkHeader = MavlinkHeader(sequence = 0),
) : MavlinkCodec {

    private val parser = MavlinkParser()
    private var sequence: Int = defaultHeader.sequence

    /** Number of frames rejected because of a checksum mismatch. */
    val checksumErrorCount: Int get() = parser.checksumErrorCount

    /** Number of frames accepted. */
    val frameCount: Int get() = parser.frameCount

    override fun consume(bytes: ByteArray): List<MavlinkFrame> = parser.parse(bytes)

    override fun encode(message: MavlinkMessage): ByteArray = encode(message, defaultHeader)

    /** Serialises [message] with an explicit [header]. */
    fun encode(message: MavlinkMessage, header: MavlinkHeader): ByteArray {
        val payload = message.pack()
        require(payload.size <= MavlinkDialect.MAX_PAYLOAD_LENGTH) {
            "Payload of ${MavlinkDialect.name(message.messageId)} exceeds the v1 limit"
        }
        val frame = ByteArray(payload.size + 8)
        frame[0] = MavlinkDialect.STX.toByte()
        frame[1] = payload.size.toByte()
        frame[2] = header.sequence.toByte()
        frame[3] = header.systemId.toByte()
        frame[4] = header.componentId.toByte()
        frame[5] = message.messageId.toByte()
        payload.copyInto(frame, 6)

        val accumulator = MavlinkCrc()
        accumulator.update(payload.size)
        accumulator.update(header.sequence)
        accumulator.update(header.systemId)
        accumulator.update(header.componentId)
        accumulator.update(message.messageId)
        for (byte in payload) {
            accumulator.update(byte.toInt() and 0xFF)
        }
        accumulator.update(MavlinkDialect.crcExtra(message.messageId))
        frame[6 + payload.size] = accumulator.lsb.toByte()
        frame[7 + payload.size] = accumulator.msb.toByte()
        return frame
    }

    /**
     * Serialises [message] using the next sequence number.
     *
     * The sequence number is incremented for every frame that is produced, matching the
     * behaviour of a MAVLink v1 sender.
     */
    fun encodeNext(message: MavlinkMessage, systemId: Int = MavlinkHeader.DEFAULT_SYSTEM_ID, componentId: Int = MavlinkHeader.DEFAULT_COMPONENT_ID): ByteArray {
        val header = MavlinkHeader(
            sequence = sequence and 0xFF,
            systemId = systemId,
            componentId = componentId,
        )
        sequence = (sequence + 1) and 0xFF
        return encode(message, header)
    }
}
