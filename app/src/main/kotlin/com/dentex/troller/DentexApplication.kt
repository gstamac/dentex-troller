package com.dentex.troller

import android.app.Application
import org.maplibre.android.MapLibre

class DentexApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // The chart is fully offline; MapLibre only needs to be initialised once per process.
        MapLibre.getInstance(this)
    }
}
