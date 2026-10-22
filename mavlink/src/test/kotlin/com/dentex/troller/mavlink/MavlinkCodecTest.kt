package com.dentex.troller.mavlink

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MavlinkCodecTest {

    /**
     * The Helmsman application contains a hard coded frame in its GATT write callback:
     * `{-2, 2, 0, -1, -56, 43, 0, 0, -12, 22}`.
     *
     * That is `FE 02 00 FF C8 2B 00 00 F4 16`: a `MISSION_REQUEST_LIST` (message id 43) with
     * `sysid = 255`, `compid = 200`, an empty payload and the checksum `F4 16`. It is a wire
     * level reference for the framing, the checksum algorithm, the CRC-extra table and the
     * low byte first checksum ordering.
     */
    @Test
    fun encodesMissionRequestListLikeTheReferenceFrame() {
        val codec = HelmsmanMavlinkCodec()
        val frame = codec.encode(
            MissionRequestList(),
            MavlinkHeader(sequence = 0, systemId = 255, componentId = 200),
        )

        assertArrayEquals(
            byteArrayOf(
                0xFE.toByte(), 0x02, 0x00, 0xFF.toByte(), 0xC8.toByte(), 0x2B,
                0x00, 0x00, 0xF4.toByte(), 0x16,
            ),
            frame,
        )
    }

    @Test
    fun parsesTheReferenceFrame() {
        val codec = HelmsmanMavlinkCodec()
        val frames = codec.consume(
            byteArrayOf(
                0xFE.toByte(), 0x02, 0x00, 0xFF.toByte(), 0xC8.toByte(), 0x2B,
                0x00, 0x00, 0xF4.toByte(), 0x16,
            ),
        )

        assertEquals(1, frames.size)
        val frame = frames.first()
        assertEquals(0, frame.sequence)
        assertEquals(255, frame.systemId)
        assertEquals(200, frame.componentId)
        assertEquals(43, frame.messageId)
        assertEquals("MISSION_REQUEST_LIST", frame.messageName)
        assertEquals(0xF4 or (0x16 shl 8), frame.checksum)
    }

    @Test
    fun heartBeatRoundTrips() {
        val codec = HelmsmanMavlinkCodec()
        val heartbeat = Heartbeat(
            customMode = 102,
            type = 110,
            autopilot = 3,
            baseMode = 81,
            systemStatus = 4,
            mavlinkVersion = 3,
        )
        val frame = codec.encode(heartbeat, MavlinkHeader(sequence = 7))

        assertEquals(9 + 8, frame.size)
        assertEquals(0xFE.toByte(), frame[0])
        assertEquals(9.toByte(), frame[1])
        assertEquals(7.toByte(), frame[2])
        assertEquals(255.toByte(), frame[3])
        assertEquals(190.toByte(), frame[4])
        assertEquals(0.toByte(), frame[5])

        val decoded = MavlinkMessage.decode(codec.consume(frame).single())
        assertEquals(heartbeat, decoded)
    }

    @Test
    fun commandLongKeepsParameterOrder() {
        val codec = HelmsmanMavlinkCodec()
        val command = CommandLong(command = CommandLong.COMMAND_TURN_LEFT, param1 = 90f)
        val frame = codec.encode(command)

        val decoded = MavlinkMessage.decode(codec.consume(frame).single())
        assertEquals(command, decoded)
    }

    @Test
    fun sequenceNumberAdvances() {
        val codec = HelmsmanMavlinkCodec()
        val first = codec.encodeNext(Heartbeat())
        val second = codec.encodeNext(Heartbeat())
        assertEquals(0, first[2].toInt())
        assertEquals(1, second[2].toInt())
    }

    @Test
    fun parserReassemblesFrameSplitAcrossChunks() {
        val codec = HelmsmanMavlinkCodec()
        val frame = codec.encode(Heartbeat(type = 110))

        assertTrue(codec.consume(frame.copyOfRange(0, 4)).isEmpty())
        assertTrue(codec.consume(frame.copyOfRange(4, 11)).isEmpty())
        val frames = codec.consume(frame.copyOfRange(11, frame.size))

        assertEquals(1, frames.size)
        assertEquals(0, frames.first().messageId)
    }

    @Test
    fun parserRejectsCorruptedChecksumAndResynchronises() {
        val codec = HelmsmanMavlinkCodec()
        val corrupt = codec.encode(Heartbeat(type = 110)).copyOf()
        corrupt[corrupt.size - 1] = (corrupt[corrupt.size - 1] + 1).toByte()

        val good = codec.encode(Heartbeat(type = 110), MavlinkHeader(sequence = 1))
        val frames = codec.consume(corrupt + good)

        assertEquals(1, frames.size)
        assertEquals(1, frames.first().sequence)
        assertEquals(1, codec.checksumErrorCount)
    }

    @Test
    fun parserCountsSeveralFramesInOneChunk() {
        val codec = HelmsmanMavlinkCodec()
        val frames = codec.consume(
            codec.encode(Heartbeat(), MavlinkHeader(sequence = 1)) +
                codec.encode(
                    GlobalPositionInt(latitude = 1130000000, longitude = 2000000000),
                    MavlinkHeader(sequence = 2),
                ) +
                codec.encode(CommandLong(command = 10), MavlinkHeader(sequence = 3)),
        )

        assertEquals(3, frames.size)
        assertEquals(0, frames[0].messageId)
        assertEquals(33, frames[1].messageId)
        assertEquals(76, frames[2].messageId)
    }

    @Test
    fun unknownMessageIdsDecodeToNull() {
        val codec = HelmsmanMavlinkCodec()
        val frame = codec.encode(
            ControlCommand(command = 3),
            MavlinkHeader(sequence = 0),
        )
        assertNotNull(MavlinkMessage.decode(codec.consume(frame).single()))

        val unknown = MavlinkFrame(
            sequence = 0,
            systemId = 0,
            componentId = 0,
            messageId = 200,
            payload = byteArrayOf(0, 0),
            checksum = 0,
        )
        assertNull(MavlinkMessage.decode(unknown))
    }
}
