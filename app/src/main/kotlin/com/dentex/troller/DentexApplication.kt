package com.dentex.troller

import android.app.Application
import android.content.Context
import com.dentex.troller.track.TrackRecording
import org.maplibre.android.MapLibre

class DentexApplication : Application() {

    /** The boat's track, shared by the screens and the service that records in the background. */
    lateinit var trackRecording: TrackRecording
        private set

    override fun onCreate() {
        super.onCreate()
        // The chart is fully offline; MapLibre only needs to be initialised once per process.
        MapLibre.getInstance(this)
        trackRecording = TrackRecording(
            TrackStore(this),
            getSharedPreferences(TrackRecording.SETTINGS, Context.MODE_PRIVATE),
        )
    }
}
