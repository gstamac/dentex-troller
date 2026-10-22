package com.dentex.troller.ble

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WriteChunkerTest {

    @Test
    fun anEmptyPayloadProducesNoChunk() {
        assertTrue(WriteChunker.split(byteArrayOf()).isEmpty())
    }

    @Test
    fun aShortPayloadIsOneChunk() {
        val chunks = WriteChunker.split(ByteArray(9) { it.toByte() })
        assertEquals(1, chunks.size)
        assertEquals(9, chunks[0].size)
    }

    @Test
    fun aFullChunkIsNotSplit() {
        val chunks = WriteChunker.split(ByteArray(20))
        assertEquals(1, chunks.size)
        assertEquals(20, chunks[0].size)
    }

    @Test
    fun aLongerPayloadIsSplitAndTheRemainderIsKept() {
        val payload = ByteArray(45) { it.toByte() }
        val chunks = WriteChunker.split(payload)

        assertEquals(3, chunks.size)
        assertEquals(20, chunks[0].size)
        assertEquals(20, chunks[1].size)
        assertEquals(5, chunks[2].size)
        assertEquals(payload.toList(), chunks.flatMap { it.toList() })
    }

    @Test
    fun theChunkSizeMatchesTheHelmsmanApplication() {
        assertEquals(20, WriteChunker.CHUNK_SIZE)
    }
}
