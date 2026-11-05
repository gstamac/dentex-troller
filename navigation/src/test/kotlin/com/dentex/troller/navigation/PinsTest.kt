package com.dentex.troller.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PinsTest {

    private var next = 0
    private val newId = { "p${++next}" }

    // The shape of the Navionics Boating app's export.
    private val navionics = """<?xml version="1.0" encoding="UTF-8" ?><gpx xmlns="http://www.topografix.com/GPX/1/1" version="1.1" creator="Navionics Boating App"><metadata><link href="http://www.navionics.com" /></metadata><wpt lat="44.067495" lon="14.859677"><name>fish</name><time>2024-07-12T21:41:40Z</time></wpt><wpt lat="44.209614" lon="14.863584"><name>Marker 001</name><time>2022-07-29T15:58:26Z</time></wpt></gpx>"""

    @Test
    fun navionicsMarkersBecomePins() {
        val content = Gpx.parse(navionics.toByteArray(), newId)
        assertEquals(listOf("fish", "Marker 001"), content.pins.map { it.name })
        assertEquals(44.067495, content.pins[0].latitude, 0.0)
        assertEquals(14.859677, content.pins[0].longitude, 0.0)
        assertEquals(1_720_820_500_000L, content.pins[0].timeMillis)
        assertTrue(content.routes.isEmpty())
    }

    @Test
    fun routesWithoutANamespaceAreReadToo() {
        val gpx = """<gpx version="1.0"><wpt lat="1" lon="2"/><rte><name>Out</name><rtept lat="44.1" lon="14.1"/><rtept lat="44.2" lon="14.2"/></rte><rte/></gpx>"""
        val content = Gpx.parse(gpx.toByteArray(), newId)
        assertEquals("Pin", content.pins.single().name)
        assertNull(content.pins.single().timeMillis)
        assertEquals("Out", content.routes.single().first)
        assertEquals(2, content.routes.single().second.size)
    }

    @Test
    fun otherFilesAndBadPointsAreRejected() {
        assertTrue(runCatching { Gpx.parse("PMTiles".toByteArray(), newId) }.exceptionOrNull() is IllegalArgumentException)
        assertTrue(runCatching { Gpx.parse("<kml/>".toByteArray(), newId) }.exceptionOrNull() is IllegalArgumentException)
        val bad = """<gpx><wpt lat="95" lon="14"/><wpt lat="x" lon="14"/></gpx>"""
        assertTrue(Gpx.parse(bad.toByteArray(), newId).pins.isEmpty())
    }

    @Test
    fun aDoctypeIsRefused() {
        val gpx = """<?xml version="1.0"?><!DOCTYPE gpx [<!ENTITY x SYSTEM "file:///etc/passwd">]><gpx><wpt lat="1" lon="2"><name>&x;</name></wpt></gpx>"""
        assertTrue(runCatching { Gpx.parse(gpx.toByteArray(), newId) }.isFailure)
    }

    @Test
    fun importingTheSameFileTwiceAddsNothing() {
        val pins = Gpx.parse(navionics.toByteArray(), newId).pins
        val (once, added) = PinLibrary().merge(pins)
        assertEquals(2, added)
        val (twice, again) = once.merge(Gpx.parse(navionics.toByteArray(), newId).pins)
        assertEquals(0, again)
        assertEquals(once, twice)
    }

    @Test
    fun thePinsSurviveTheRoundTrip() {
        val library = PinLibrary(
            listOf(
                Pin("a", "Rt Kremik fish", 44.2, 14.8, 1_700_000_000_000),
                Pin("b", "No time", 44.3, 14.9, null),
            ),
        )
        assertEquals(library, PinLibrary.decode(library.encode()))
        assertEquals(listOf("b"), library.delete("a").pins.map { it.id })
    }
}
