package com.dentex.troller.track

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationListenerCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.location.LocationRequestCompat
import com.dentex.troller.DentexApplication
import com.dentex.troller.MainActivity
import com.dentex.troller.R
import com.dentex.troller.ui.formatDistance
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Keeps the track recording while the app is in the background or the screen is off: a
 * foreground service with a notification, which listens to the phone's GPS and keeps the app
 * alive, so the motor's positions keep coming in over the link as well.
 *
 * It only records the track. It never sends anything to the motor.
 */
class TrackService : Service() {

    private val scope = MainScope()
    private lateinit var recording: TrackRecording
    private var locationManager: LocationManager? = null
    private val listener = LocationListenerCompat { fix ->
        recording.onPhoneFix(fix.latitude, fix.longitude, System.currentTimeMillis())
    }
    private var running = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        recording = (application as DentexApplication).trackRecording
        val channel = NotificationChannel(CHANNEL, "Track recording", NotificationManager.IMPORTANCE_LOW).apply {
            description = "Shown while the boat's track is being recorded"
            setShowBadge(false)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            recording.stop()
            stopSelf()
            return START_NOT_STICKY
        }
        // Started with startForegroundService, so it has to go to the foreground in any case.
        try {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification(recording.lengthMeters.value),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION else 0,
            )
        } catch (_: Exception) {
            // Not allowed now, for example without the location permission; the track still
            // records while the app is open.
            stopSelf()
            return START_NOT_STICKY
        }
        if (!recording.recording.value) {
            stopSelf()
            return START_NOT_STICKY
        }
        if (!running) {
            running = true
            listenToGps()
            scope.launch {
                recording.recording.first { !it }
                stopSelf()
            }
            scope.launch {
                recording.lengthMeters
                    .map(::formatDistance)
                    .distinctUntilChanged()
                    .collect { update(recording.lengthMeters.value) }
            }
        }
        return START_NOT_STICKY
    }

    private fun listenToGps() {
        val manager = getSystemService(LocationManager::class.java) ?: return
        if (LocationManager.GPS_PROVIDER !in manager.allProviders) return
        val request = LocationRequestCompat.Builder(GPS_INTERVAL_MILLIS)
            .setQuality(LocationRequestCompat.QUALITY_HIGH_ACCURACY)
            .build()
        try {
            LocationManagerCompat.requestLocationUpdates(
                manager, LocationManager.GPS_PROVIDER, request, ContextCompat.getMainExecutor(this), listener,
            )
            locationManager = manager
        } catch (_: SecurityException) {
            // The permission was taken away; the motor's positions are still recorded.
        }
    }

    private fun notification(lengthMeters: Double) = NotificationCompat.Builder(this, CHANNEL)
        .setSmallIcon(R.drawable.ic_stat_track)
        .setContentTitle("Recording the track")
        .setContentText("${formatDistance(lengthMeters)} so far")
        .setWhen(recording.startedMillis)
        .setShowWhen(true)
        .setUsesChronometer(true)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setSilent(true)
        .setCategory(NotificationCompat.CATEGORY_SERVICE)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        .setContentIntent(
            PendingIntent.getActivity(
                this, 0,
                Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_IMMUTABLE,
            ),
        )
        .addAction(
            0, "Stop recording",
            PendingIntent.getService(
                this, 1,
                Intent(this, TrackService::class.java).setAction(ACTION_STOP),
                PendingIntent.FLAG_IMMUTABLE,
            ),
        )
        .build()

    private fun update(lengthMeters: Double) {
        val allowed = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        if (allowed) NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification(lengthMeters))
    }

    override fun onDestroy() {
        locationManager?.let { LocationManagerCompat.removeUpdates(it, listener) }
        locationManager = null
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL = "track"
        private const val NOTIFICATION_ID = 1
        private const val ACTION_STOP = "com.dentex.troller.STOP_TRACK"
        private const val GPS_INTERVAL_MILLIS = 1_000L

        /**
         * Starts the service while recording. Call it while the app is on screen: Android only
         * lets a location service start from the foreground. Returns false when it can't run
         * because the location permission is missing.
         */
        fun start(context: Context): Boolean {
            val app = context.applicationContext
            if (ContextCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return try {
                ContextCompat.startForegroundService(app, Intent(app, TrackService::class.java))
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
