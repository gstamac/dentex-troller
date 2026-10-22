package com.dentex.troller.mavlink

/**
 * Incremental MAVLink v1 parser.
 *
 * This is a port of `com.MAVLink.Parser` from the Helmsman APK. The parser is fed one byte at a
 * time and keeps its state between calls, so a frame that is split across several BLE
 * notifications is reassembled correctly.
 *
 * Recovery behaviour matches the original: after a checksum mismatch the parser returns to the
 * idle state, and if the offending byte was itself a start-of-frame marker it is reused as the
 * beginning of the next frame.
 */
class MavlinkParser {

    private enum class State {
        IDLE,
        GOT_STX,
        GOT_LENGTH,
        GOT_SEQ,
        GOT_SYSID,
        GOT_COMPID,
        GOT_MSGID,
        GOT_PAYLOAD,
        GOT_CRC1,
    }

    private var state: State = State.IDLE
    private var payloadLength: Int = 0
    private var sequence: Int = 0
    private var systemId: Int = 0
    private var componentId: Int = 0
    private var messageId: Int = 0
    private val payload = ByteArray(MavlinkDialect.MAX_PAYLOAD_LENGTH)
    private var payloadSize: Int = 0

    /** Number of frames rejected because of a checksum mismatch since the parser was created. */
    var checksumErrorCount: Int = 0
        private set

    /** Number of frames accepted since the parser was created. */
    var frameCount: Int = 0
        private set

    /** Discards any partially received frame and resets the parser. */
    fun reset() {
        state = State.IDLE
        payloadSize = 0
    }

    /**
     * Feeds a single byte into the parser.
     *
     * @return the completed frame, or `null` when the byte did not finish a frame.
     */
    fun parseByte(value: Int): MavlinkFrame? {
        val byte = value and 0xFF
        return when (state) {
            State.IDLE -> {
                if (byte == MavlinkDialect.STX) {
                    state = State.GOT_STX
                }
                null
            }

            State.GOT_STX -> {
                payloadLength = byte
                state = State.GOT_LENGTH
                null
            }

            State.GOT_LENGTH -> {
                sequence = byte
                state = State.GOT_SEQ
                null
            }

            State.GOT_SEQ -> {
                systemId = byte
                state = State.GOT_SYSID
                null
            }

            State.GOT_SYSID -> {
                componentId = byte
                state = State.GOT_COMPID
                null
            }

            State.GOT_COMPID -> {
                messageId = byte
                payloadSize = 0
                state = if (payloadLength == 0) State.GOT_PAYLOAD else State.GOT_MSGID
                null
            }

            State.GOT_MSGID -> {
                payload[payloadSize++] = byte.toByte()
                if (payloadSize >= payloadLength) state = State.GOT_PAYLOAD
                null
            }

            State.GOT_PAYLOAD -> {
                val expected = computeChecksum()
                if (byte != expected.lsb) {
                    checksumErrorCount++
                    resync(byte)
                    return null
                }
                state = State.GOT_CRC1
                null
            }

            State.GOT_CRC1 -> {
                val expected = computeChecksum()
                if (byte != expected.msb) {
                    checksumErrorCount++
                    resync(byte)
                    return null
                }
                state = State.IDLE
                frameCount++
                return MavlinkFrame(
                    sequence = sequence,
                    systemId = systemId,
                    componentId = componentId,
                    messageId = messageId,
                    payload = payload.copyOf(payloadSize),
                    checksum = expected.lsb or (expected.msb shl 8),
                )
            }
        }
    }

    /**
     * Feeds a chunk of bytes into the parser and returns every frame that was completed.
     */
    fun parse(bytes: ByteArray): List<MavlinkFrame> {
        if (bytes.isEmpty()) return emptyList()
        val frames = ArrayList<MavlinkFrame>()
        for (byte in bytes) {
            parseByte(byte.toInt() and 0xFF)?.let { frames.add(it) }
        }
        return frames
    }

    private fun computeChecksum(): MavlinkCrc {
        val accumulator = MavlinkCrc()
        accumulator.update(payloadLength)
        accumulator.update(sequence)
        accumulator.update(systemId)
        accumulator.update(componentId)
        accumulator.update(messageId)
        for (index in 0 until payloadSize) {
            accumulator.update(payload[index].toInt() and 0xFF)
        }
        accumulator.update(MavlinkDialect.crcExtra(messageId))
        return accumulator
    }

    private fun resync(byte: Int) {
        state = State.IDLE
        payloadSize = 0
        if (byte == MavlinkDialect.STX) {
            state = State.GOT_STX
        }
    }
}
