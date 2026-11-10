package com.dentex.troller.track

import android.content.SharedPreferences
import com.dentex.troller.TrackStore
import com.dentex.troller.chart.PositionSource
import com.dentex.troller.navigation.Track
import com.dentex.troller.navigation.TrackPoint
import com.dentex.troller.navigation.TrackRecorder
import com.dentex.troller.navigation.Waypoint
import com.dentex.troller.navigation.distanceMeters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * The boat's track and its recording, one for the whole app. The motor's positions come from the
 * link and the phone's from [TrackService], which keeps recording while the app is in the
 * background or the screen is off. Call it on the main thread.
 */
class TrackRecording(
    private val store: TrackStore,
    private val settings: SharedPreferences,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    // Its own writer, in order, so the appended points keep their order.
    private val saving = Dispatchers.IO.limitedParallelism(1)

    private val _track = MutableStateFlow(store.load())

    /** The recorded track. Kept across restarts. */
    val track: StateFlow<Track> = _track.asStateFlow()

    private val _lengthMeters = MutableStateFlow(_track.value.lengthMeters)

    /** The length of [track], kept up to date without measuring the whole track each time. */
    val lengthMeters: StateFlow<Double> = _lengthMeters.asStateFlow()

    private val _recording = MutableStateFlow(settings.getBoolean(KEY_RECORDING, false))

    /** Whether the boat's positions are being added to [track]. Kept across restarts. */
    val recording: StateFlow<Boolean> = _recording.asStateFlow()

    /** When recording last started, in milliseconds; the notification counts from it. */
    val startedMillis: Long get() = settings.getLong(KEY_STARTED, System.currentTimeMillis())

    private val _tail = MutableStateFlow<Waypoint?>(null)

    /**
     * While recording, the boat's last position that fitted the track, which the chart joins to
     * the end of the line; never a bad fix that was left out.
     */
    val tail: StateFlow<Waypoint?> = _tail.asStateFlow()

    private val _errors = MutableSharedFlow<String>(extraBufferCapacity = 4)

    /** Saving failures, for the screen to show. */
    val errors: SharedFlow<String> = _errors.asSharedFlow()

    private val picker = FixPicker()
    private val recorder = TrackRecorder()

    // The next recorded position starts a new segment: after recording starts, and after the app
    // was started again while recording, so the line doesn't jump across the time it was closed.
    private var segmentPending = true

    // Which GPS the last recorded position came from. The motor's and the phone's never agree
    // exactly, so a switch between them starts a new segment rather than a line between the two.
    private var lastSource: PositionSource? = null

    /**
     * Starts recording. With [keepOld] the new positions are added to the track as a new
     * segment; otherwise the old track is deleted first. The track starts where the boat is now.
     */
    fun start(keepOld: Boolean) {
        if (!keepOld) {
            _track.value = Track()
            _lengthMeters.value = 0.0
            save { store.replace(Track()) }
        }
        segmentPending = true
        _tail.value = null
        val now = System.currentTimeMillis()
        settings.edit().putBoolean(KEY_RECORDING, true).putLong(KEY_STARTED, now).apply()
        _recording.value = true
        picker.freshest(now)?.let { (point, source) -> record(point.copy(timeMillis = now), source) }
    }

    fun stop() {
        settings.edit().putBoolean(KEY_RECORDING, false).apply()
        _recording.value = false
        _tail.value = null
    }

    /** A position from the motor's GPS; [timeMillis] is when the motor last reported. */
    fun onMotorFix(latitude: Double, longitude: Double, timeMillis: Long) {
        val point = TrackPoint(latitude, longitude, timeMillis)
        picker.motor(point)
        record(point, PositionSource.MOTOR)
    }

    /** A position from the phone's GPS, used while the motor has no fresh one. */
    fun onPhoneFix(latitude: Double, longitude: Double, timeMillis: Long) {
        val point = TrackPoint(latitude, longitude, timeMillis)
        if (picker.phone(point)) record(point, PositionSource.PHONE)
    }

    private fun record(point: TrackPoint, source: PositionSource) {
        if (!_recording.value) return
        val startSegment = segmentPending || source != lastSource
        val decision = recorder.decide(_track.value, point, startSegment)
        if (decision == TrackRecorder.Decision.Ignored) return
        _tail.value = Waypoint(point.latitude, point.longitude)
        if (decision !is TrackRecorder.Decision.Add) return
        segmentPending = false
        lastSource = source
        val last = _track.value.last
        if (!decision.newSegment && last != null) {
            _lengthMeters.value += distanceMeters(
                Waypoint(last.latitude, last.longitude),
                Waypoint(point.latitude, point.longitude),
            )
        }
        _track.value = _track.value.add(point, decision.newSegment)
        val lines = if (decision.newSegment) listOf(Track.SEGMENT, Track.pointLine(point)) else listOf(Track.pointLine(point))
        save { store.append(lines) }
    }

    private fun save(write: () -> Unit) {
        scope.launch(saving) {
            try {
                write()
            } catch (error: Exception) {
                _errors.tryEmit("The track could not be saved: ${error.message}")
            }
        }
    }

    companion object {
        /** The settings file the recording flag has always been kept in. */
        const val SETTINGS = "chart"
        private const val KEY_RECORDING = "recording_track"
        private const val KEY_STARTED = "recording_track_started"
    }
}
