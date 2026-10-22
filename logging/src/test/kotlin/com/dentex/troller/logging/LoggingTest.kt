package com.dentex.troller.logging

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HexDumpTest {

    @Test
    fun rendersContiguousHex() {
        assertEquals("FE02FF", HexDump.toHex(byteArrayOf(0xFE.toByte(), 0x02, 0xFF.toByte())))
    }

    @Test
    fun rendersSpacedHex() {
        assertEquals("FE 02 FF", HexDump.toSpacedHex(byteArrayOf(0xFE.toByte(), 0x02, 0xFF.toByte())))
    }

    @Test
    fun parsesHexWithSeparators() {
        val parsed = HexDump.parseHex("FE:02 ff")
        assertEquals(3, parsed.size)
        assertEquals(0xFE.toByte(), parsed[0])
        assertEquals(0x02.toByte(), parsed[1])
        assertEquals(0xFF.toByte(), parsed[2])
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsOddLengthHex() {
        HexDump.parseHex("FE0")
    }

    @Test
    fun asciiDumpContainsTheAsciiGutter() {
        val dump = HexDump.toAsciiDump("MAVLink".toByteArray())
        assertTrue(dump.contains("MAVLink"))
    }
}

class DiagnosticLogTest {

    @Test
    fun keepsTheMostRecentEntries() {
        val log = DiagnosticLog(maxEntries = 3)
        repeat(5) { index -> log.event("test", "entry $index") }

        assertEquals(3, log.entries.size)
        assertEquals("entry 4", log.entries.last().message)
        assertEquals(5L, log.appendedCount)
    }

    @Test
    fun exportContainsEveryDirection() {
        val log = DiagnosticLog()
        log.event("link", "connected")
        log.tx("ble", byteArrayOf(0x01, 0x02))
        log.rx("ble", byteArrayOf(0x03))

        val export = log.exportText()
        assertTrue(export.contains("EVENT"))
        assertTrue(export.contains("TX"))
        assertTrue(export.contains("RX"))
        assertTrue(export.contains("01 02"))
    }

    @Test
    fun clearRemovesTheEntries() {
        val log = DiagnosticLog()
        log.event("link", "connected")
        log.clear()
        assertEquals(0, log.entries.size)
    }
}

class RedactionTest {

    @Test
    fun masksTheMacTail() {
        assertEquals("AA:BB:CC:??:??:??", Redaction.maskMac("AA:BB:CC:DD:EE:FF"))
    }

    @Test
    fun coarsensCoordinates() {
        assertEquals(45.12, Redaction.coarsenLatitude(45.123456), 1e-9)
        assertEquals(13.87, Redaction.coarsenLongitude(13.874321), 1e-9)
    }
}
