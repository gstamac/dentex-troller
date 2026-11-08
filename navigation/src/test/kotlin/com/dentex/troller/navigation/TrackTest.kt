package com.dentex.troller.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Assert.assertThrows

class TrackTest {

    /** [northMeters] north of a fixed spot, rounded to the 7 decimals the file keeps (about 1 cm). */
    private fun point(northMeters: Double, seconds: Long) =
        TrackPoint(Math.round((44.2 + northMeters / 111_195.0) * 1e7) / 1e7, 14.8, 1_700_000_000_000 + seconds * 1000)

    private fun record(points: List<TrackPoint>, startSegmentAt: Set<Int> = setOf(0)): Track {
        val recorder = TrackRecorder()
        return points.foldIndexed(Track()) { index, track, p ->
            when (val d = recorder.decide(track, p, startSegment = index in startSegmentAt)) {
                TrackRecorder.Decision.TooClose, TrackRecorder.Decision.Ignored -> track
                is TrackRecorder.Decision.Add -> track.add(p, d.newSegment)
            }
        }
    }

    /** Metres north of the fixed spot, per point of the single segment. */
    private fun Track.northMeters(): List<Double> =
        segments.single().map { Math.round((it.latitude - 44.2) * 111_195.0).toDouble() }

    @Test
    fun positionsCloserThanTheStepAreLeftOut() {
        val track = record(listOf(point(0.0, 0), point(2.0, 1), point(4.0, 2), point(6.0, 3), point(13.0, 5)))
        assertEquals(listOf(0.0, 6.0, 13.0), track.northMeters())
        assertEquals(13.0, track.lengthMeters, 0.1)
    }

    @Test
    fun startingAgainOrALongGapBeginsANewSegment() {
        val gap = TrackRecorder.NEW_SEGMENT_AFTER_MILLIS / 1000 + 1
        val track = record(
            listOf(point(0.0, 0), point(10.0, 5), point(20.0, 5 + gap), point(30.0, 10 + gap), point(31.0, 20 + gap)),
            startSegmentAt = setOf(0, 4),
        )
        assertEquals(listOf(2, 2, 1), track.segments.map { it.size })
        // Only the distance within segments counts.
        assertEquals(20.0, track.lengthMeters, 0.1)
    }

    @Test
    fun aJumpOfAFewHundredMetresIsJoined() {
        // The simulator moves the boat in jumps like this one; each is drawn.
        val track = record(listOf(point(0.0, 0), point(10.0, 3), point(250.0, 6), point(260.0, 9)))
        assertEquals(listOf(4), track.segments.map { it.size })
    }

    @Test
    fun aFixNoBoatCouldReachIsIgnoredAndTheTrackCarriesOn() {
        // 445 km south in 3 s, then small moves there, then back near the boat.
        val track = record(
            listOf(point(0.0, 0), point(10.0, 3), point(-445_000.0, 6), point(-444_990.0, 9), point(250.0, 12)),
        )
        assertEquals(listOf(0.0, 10.0, 250.0), track.northMeters())
    }

    @Test
    fun aBoatThatReallyIsWhereOnlyBadFixesPutItIsRecordedThereAfterTwoMinutes() {
        val track = record(
            listOf(
                point(0.0, 0), point(10.0, 3),
                point(50_000.0, 6), point(50_005.0, 66), point(50_010.0, 127), point(50_020.0, 130),
            ),
        )
        assertEquals(listOf(2, 2), track.segments.map { it.size })
    }

    @Test
    fun aBoatThatSitsStillKeepsOneLine() {
        // On anchor for 30 minutes, then on its way.
        val still = (1..60).map { point(10.5, 3 + it * 30L) }
        val track = record(listOf(point(0.0, 0), point(10.0, 3)) + still + point(20.0, 1_805))
        assertEquals(listOf(0.0, 10.0, 20.0), track.northMeters())
    }

    @Test
    fun aBadFixAfterSittingStillIsStillIgnored() {
        // Judged against the last good position, not the last recorded one half an hour before.
        val still = (1..60).map { point(10.5, 3 + it * 30L) }
        val track = record(listOf(point(0.0, 0), point(10.0, 3)) + still + point(2_000.0, 1_804) + point(20.0, 1_806))
        assertEquals(listOf(0.0, 10.0, 20.0), track.northMeters())
    }

    @Test
    fun aLongRunAtBoatSpeedFits() {
        // 1.5 km in 2 minutes is 12.5 m/s, about 24 knots.
        assertTrue(TrackRecorder.fits(point(0.0, 0), point(1_500.0, 120)))
        assertTrue(!TrackRecorder.fits(point(0.0, 0), point(1_500.0, 10)))
    }

    @Test
    fun theFileReadsBackTheSameTrack() {
        val track = record(listOf(point(0.0, 0), point(10.0, 5), point(20.0, 9)), startSegmentAt = setOf(0, 2))
        assertEquals(track, Track.decode(track.encode()))
    }

    @Test
    fun appendedLinesReadLikeAWrittenFile() {
        val a = point(0.0, 0)
        val b = point(10.0, 5)
        val text = Track.HEADER + "\n" + Track.SEGMENT + "\n" + Track.pointLine(a) + "\n" + Track.pointLine(b) + "\n"
        assertEquals(Track(listOf(listOf(a, b))), Track.decode(text))
    }

    @Test
    fun aHalfWrittenLastLineIsSkipped() {
        val a = point(0.0, 0)
        val text = Track.HEADER + "\n" + Track.SEGMENT + "\n" + Track.pointLine(a) + "\np 44.20001"
        assertEquals(Track(listOf(listOf(a))), Track.decode(text))
    }

    @Test
    fun emptySegmentsAreDropped() {
        val text = Track.HEADER + "\n" + Track.SEGMENT + "\n" + Track.SEGMENT + "\n"
        assertTrue(Track.decode(text).isEmpty)
        assertTrue(Track.decode(text).segments.isEmpty())
    }

    @Test
    fun otherFilesAreRefused() {
        assertThrows(IllegalArgumentException::class.java) { Track.decode("dentex-pins 1\n") }
    }
}
