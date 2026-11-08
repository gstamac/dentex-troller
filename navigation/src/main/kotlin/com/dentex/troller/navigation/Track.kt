package com.dentex.troller.navigation

import java.util.Locale

/** A recorded position of the boat. */
data class TrackPoint(val latitude: Double, val longitude: Double, val timeMillis: Long)

/**
 * The boat's recorded track, in segments. A segment starts each time recording starts, and
 * after a long gap in the positions, so the line never jumps across a stretch that wasn't
 * recorded.
 */
data class Track(val segments: List<List<TrackPoint>> = emptyList()) {

    val isEmpty: Boolean get() = segments.all { it.isEmpty() }

    val last: TrackPoint? get() = segments.lastOrNull()?.lastOrNull()

    /** The distance travelled along the track; gaps between segments don't count. */
    val lengthMeters: Double
        get() = segments.sumOf { segment ->
            segment.zipWithNext { a, b -> distanceMeters(a.waypoint, b.waypoint) }.sum()
        }

    /** The track with [point] added, at the end of the last segment or in a new one. */
    fun add(point: TrackPoint, newSegment: Boolean): Track =
        if (newSegment || segments.isEmpty()) {
            Track(segments + listOf(listOf(point)))
        } else {
            Track(segments.dropLast(1) + listOf(segments.last() + point))
        }

    fun encode(): String = buildString {
        appendLine(HEADER)
        segments.forEach { segment ->
            appendLine(SEGMENT)
            segment.forEach { appendLine(pointLine(it)) }
        }
    }

    companion object {
        const val HEADER = "dentex-track 1"
        const val SEGMENT = "segment"

        /** The line [encode] writes for [point]; also appended on its own while recording. */
        fun pointLine(point: TrackPoint): String =
            String.format(Locale.US, "p %.7f %.7f %d", point.latitude, point.longitude, point.timeMillis)

        /**
         * Reads [encode]'s text, or the same text appended to line by line. A line that can't be
         * read, such as the last one when the phone died while it was written, is skipped.
         * Throws [IllegalArgumentException] when the text is not a track file.
         */
        fun decode(text: String): Track {
            val lines = text.lines().map { it.trimEnd() }.filter { it.isNotEmpty() }
            require(lines.firstOrNull() == HEADER) { "Not a track file" }
            val segments = mutableListOf<MutableList<TrackPoint>>()
            for (line in lines.drop(1)) {
                when (line.substringBefore(' ')) {
                    SEGMENT -> segments += mutableListOf<TrackPoint>()
                    "p" -> {
                        val point = parsePoint(line) ?: continue
                        if (segments.isEmpty()) segments += mutableListOf<TrackPoint>()
                        segments.last() += point
                    }
                    else -> Unit // Written by a newer version; skipped.
                }
            }
            return Track(segments.filter { it.isNotEmpty() })
        }

        private fun parsePoint(line: String): TrackPoint? {
            val parts = line.split(' ')
            if (parts.size != 4) return null
            val latitude = parts[1].toDoubleOrNull() ?: return null
            val longitude = parts[2].toDoubleOrNull() ?: return null
            val time = parts[3].toLongOrNull() ?: return null
            if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) return null
            return TrackPoint(latitude, longitude, time)
        }
    }
}

private val TrackPoint.waypoint: Waypoint get() = Waypoint(latitude, longitude)

/**
 * Decides which positions go into the track. It remembers the last good position and how long
 * positions have been bad, so make one per recording session and feed it every fresh position.
 */
class TrackRecorder {

    /** The last position that fitted, recorded or not. Bad fixes are judged against it. */
    private var lastGood: TrackPoint? = null

    /** When the last position of any kind came in. */
    private var lastSeenMillis: Long? = null

    /** Since when every position has been a bad fix; null while they fit. */
    private var badSinceMillis: Long? = null

    sealed interface Decision {
        /** Fits, but too close to the last point to add. */
        data object TooClose : Decision

        /** A bad fix, left out; nothing is drawn to it. */
        data object Ignored : Decision

        data class Add(val newSegment: Boolean) : Decision
    }

    /**
     * What to do with [point]. [startSegment] is true for the first position after recording
     * started, after the app was opened again while recording, and when the position comes from
     * another GPS than the last one.
     */
    fun decide(track: Track, point: TrackPoint, startSegment: Boolean): Decision {
        val last = track.last
        val good = lastGood
        val seen = lastSeenMillis
        lastSeenMillis = point.timeMillis
        if (startSegment || last == null || good == null) return accept(point, newSegment = true)

        // No positions for a while: the screen was off or the app in the background. The boat
        // may have gone anywhere meanwhile, so no line is drawn across, unless it hasn't moved.
        if (seen != null && point.timeMillis - seen > NEW_SEGMENT_AFTER_MILLIS) {
            if (distanceMeters(last.waypoint, point.waypoint) < MIN_STEP_METERS) {
                lastGood = point
                badSinceMillis = null
                return Decision.TooClose
            }
            return accept(point, newSegment = true)
        }

        if (!fits(good, point)) {
            val since = badSinceMillis ?: point.timeMillis.also { badSinceMillis = it }
            // Bad for this long, the boat really is there; it is recorded from there on.
            return if (point.timeMillis - since >= NEW_SEGMENT_AFTER_MILLIS) accept(point, newSegment = true) else Decision.Ignored
        }
        badSinceMillis = null
        lastGood = point
        return if (distanceMeters(last.waypoint, point.waypoint) < MIN_STEP_METERS) {
            Decision.TooClose
        } else {
            Decision.Add(newSegment = false)
        }
    }

    private fun accept(point: TrackPoint, newSegment: Boolean): Decision {
        lastGood = point
        badSinceMillis = null
        return Decision.Add(newSegment)
    }

    companion object {
        /** Closer than this to the last point, a position is GPS jitter or drift and is left out. */
        const val MIN_STEP_METERS = 5.0

        /**
         * No positions for longer than this starts a new segment. Positions that are all bad fixes
         * for this long are taken as where the boat really is.
         */
        const val NEW_SEGMENT_AFTER_MILLIS = 2 * 60_000L

        /** A move up to this far is always taken, however quick; the simulator jumps this way. */
        const val MAX_JUMP_METERS = 1_000.0

        /** Further than [MAX_JUMP_METERS] and faster than this (about 50 knots), a fix is wrong. */
        const val MAX_SPEED_MPS = 25.0

        /**
         * Whether a boat at [last] could be at [point]: within [MAX_JUMP_METERS], or no faster
         * than [MAX_SPEED_MPS].
         */
        fun fits(last: TrackPoint, point: TrackPoint): Boolean {
            val meters = distanceMeters(last.waypoint, point.waypoint)
            val seconds = maxOf(point.timeMillis - last.timeMillis, 1_000L) / 1000.0
            return meters <= MAX_JUMP_METERS || meters <= MAX_SPEED_MPS * seconds
        }
    }
}
