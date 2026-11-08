package com.dentex.troller.chart

import com.dentex.troller.navigation.Track
import com.dentex.troller.navigation.TrackPoint
import com.dentex.troller.navigation.Waypoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TrackGeoJsonTest {

    private fun p(lat: Double, lon: Double) = TrackPoint(lat, lon, 0)

    @Test
    fun eachSegmentIsItsOwnLineAndLonePointsAreLeftOut() {
        val track = Track(listOf(listOf(p(44.0, 14.0), p(44.1, 14.1)), listOf(p(45.0, 15.0))))
        val json = ChartGeoJson.track(track)
        assertTrue(json.contains("\"MultiLineString\""))
        assertEquals(1, Regex("""\[\[""").findAll(json).count())
        assertTrue(!json.contains("15.0000000,45.0000000"))
    }

    @Test
    fun whileRecordingTheBoatIsJoinedToTheLastSegment() {
        val track = Track(listOf(listOf(p(44.0, 14.0), p(44.1, 14.1)), listOf(p(45.0, 15.0))))
        val json = ChartGeoJson.track(track, tail = Waypoint(45.1, 15.1))
        // The lone point now makes a line with the boat.
        assertEquals(2, Regex("""\[\[""").findAll(json).count())
    }

    @Test
    fun anEmptyTrackDrawsNothing() {
        assertTrue(ChartGeoJson.track(Track()).contains("\"features\":[]"))
    }

    @Test
    fun theTrackIsDrawnYellowInBothStyles() {
        for ((night, palette) in listOf(false to ChartStyle.DAY, true to ChartStyle.NIGHT)) {
            val style = ChartStyle.json("/chart.pmtiles", night)
            assertTrue(style.contains("\"id\": \"${ChartStyle.LAYER_TRACK}\""))
            assertTrue(style.contains("\"line-color\": \"${palette.track}\""))
        }
    }
}
