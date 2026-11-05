package com.dentex.troller.simulator

import com.dentex.troller.mavlink.Attitude
import com.dentex.troller.mavlink.CommandLong
import com.dentex.troller.mavlink.GlobalPositionInt
import com.dentex.troller.mavlink.Heartbeat
import com.dentex.troller.mavlink.MavlinkMessage
import com.dentex.troller.mavlink.MissionAck
import com.dentex.troller.mavlink.MissionCount
import com.dentex.troller.mavlink.MissionItem
import com.dentex.troller.mavlink.MissionRequest
import com.dentex.troller.mavlink.MissionRequestList
import com.dentex.troller.mavlink.RawImu
import com.dentex.troller.mavlink.SysStatus
import java.util.Locale
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A model of the Haswing type 110 motor and its remote, for testing the app on land.
 *
 * It takes the MAVLink messages the app sends and answers with the messages the motor sends,
 * using the same fields the Helmsman application reads. The behaviour follows what was seen on
 * the real motor (2026-11-05):
 *
 *  - Navigation steers to the stored route at the speed the motor had last; at speed 0 it only
 *    turns. It switches the propeller on.
 *  - Manual mode leaves the propeller running. Only the stop command (11) stops it, and the
 *    speed step is kept for the next start.
 *  - Speed up switches the propeller on.
 *
 * What is not known yet is a setting: [Settings.arrival] decides what happens at the last
 * waypoint. Without a GPS fix the motor refuses navigation and anchor mode.
 *
 * The model is deterministic: [advance] moves it by a given time, so tests need no clock.
 */
class SimulatedMotor(latitude: Double = START_LATITUDE, longitude: Double = START_LONGITUDE) {

    /** What the motor does on reaching the last waypoint. */
    enum class Arrival { ANCHOR, MANUAL, CIRCLE }

    /** Modes as the motor reports them in `ATTITUDE.pitchspeed`. */
    enum class Mode(val code: Int) { MANUAL(0), ANCHOR(1), NAVIGATION(2), CRUISE(3), RECORD(4) }

    /** A step that the motor ignores, to test how the app copes. */
    enum class Failure { NONE, UPLOAD, NAVIGATION, ANCHOR }

    /** Settings that can be changed while the simulation runs. */
    data class Settings(
        val arrival: Arrival = Arrival.ANCHOR,
        val failure: Failure = Failure.NONE,
        val gpsFix: Boolean = true,
        /** Water current: speed in knots and the direction it flows towards, in degrees. */
        val currentKnots: Double = 0.0,
        val currentTowardDegrees: Double = 0.0,
        val knotsPerStep: Double = 0.6,
        val maxStep: Int = 5,
        val turnDegreesPerSecond: Double = 20.0,
        val batteryPercent: Int = 80,
    )

    var settings: Settings = Settings()

    var latitude: Double = latitude
        private set
    var longitude: Double = longitude
        private set
    var headingDegrees: Double = 0.0
        private set
    /** Speed through the water in metres per second. */
    var speedMetersPerSecond: Double = 0.0
        private set
    var speedStep: Int = 0
        private set
    var propellerOn: Boolean = false
        private set
    var mode: Mode = Mode.MANUAL
        private set
    var route: List<Pair<Double, Double>> = emptyList()
        private set
    var targetIndex: Int = 0
        private set

    private var cruiseHeading = 0.0
    private var anchorPoint: Pair<Double, Double>? = null
    private var uploadCount = -1
    private var uploadItems = arrayOfNulls<Pair<Double, Double>>(0)

    /** Moves the boat to a position, as if it had been carried there. */
    fun place(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        if (mode == Mode.ANCHOR) anchorPoint = latitude to longitude
    }

    /** Points the boat (and the motor) in a direction. */
    fun turnTo(degrees: Double) {
        headingDegrees = normalize(degrees)
        cruiseHeading = headingDegrees
    }

    /**
     * Takes a message from the app.
     *
     * @return the messages the motor answers with.
     */
    fun receive(message: MavlinkMessage): List<MavlinkMessage> = when (message) {
        is CommandLong -> command(message.command).let { emptyList() }
        is MissionCount -> startUpload(message.count)
        is MissionItem -> uploadItem(message)
        is MissionRequestList -> listOf(MissionCount(count = route.size))
        is MissionRequest -> route.getOrNull(message.sequence)?.let { (lat, lon) ->
            listOf(MissionItem(x = lat.toFloat(), y = lon.toFloat(), sequence = message.sequence))
        } ?: emptyList()
        else -> emptyList()
    }

    private fun command(command: Int) {
        when (command) {
            FULL_SPEED -> { speedStep = settings.maxStep; propellerOn = true }
            TURN_RIGHT -> steerBy(STEER_STEP_DEGREES)
            TURN_LEFT -> steerBy(-STEER_STEP_DEGREES)
            SPEED_UP -> { speedStep = min(settings.maxStep, speedStep + 1); propellerOn = true }
            SPEED_DOWN -> speedStep = maxOf(0, speedStep - 1)
            MOTOR_ON -> propellerOn = true
            MOTOR_OFF -> propellerOn = false
            MODE_ANCHOR -> if (settings.gpsFix && settings.failure != Failure.ANCHOR) {
                mode = Mode.ANCHOR
                anchorPoint = latitude to longitude
                propellerOn = true
            }
            MODE_MANUAL -> mode = Mode.MANUAL
            MODE_CRUISE -> { mode = Mode.CRUISE; cruiseHeading = headingDegrees }
            MODE_NAVIGATION -> if (settings.gpsFix && route.isNotEmpty() && settings.failure != Failure.NAVIGATION) {
                mode = Mode.NAVIGATION
                targetIndex = 0
                propellerOn = true
            }
            MODE_RECORD -> mode = Mode.RECORD
        }
    }

    private fun steerBy(degrees: Double) {
        // In navigation and anchor mode the motor steers itself.
        if (mode == Mode.NAVIGATION || mode == Mode.ANCHOR) return
        headingDegrees = normalize(headingDegrees + degrees)
        cruiseHeading = headingDegrees
    }

    private fun startUpload(count: Int): List<MavlinkMessage> {
        if (settings.failure == Failure.UPLOAD) return emptyList()
        uploadCount = count
        uploadItems = arrayOfNulls(count.coerceAtLeast(0))
        if (count <= 0) return finishUpload()
        return listOf(MissionRequest(sequence = 0))
    }

    private fun uploadItem(item: MissionItem): List<MavlinkMessage> {
        if (uploadCount < 0 || item.sequence !in 0 until uploadCount) return emptyList()
        uploadItems[item.sequence] = item.x.toDouble() to item.y.toDouble()
        val missing = uploadItems.indexOfFirst { it == null }
        return if (missing < 0) finishUpload() else listOf(MissionRequest(sequence = missing))
    }

    private fun finishUpload(): List<MavlinkMessage> {
        route = uploadItems.filterNotNull()
        uploadCount = -1
        // A new route replaces the one being followed; navigation starts it from the beginning.
        targetIndex = 0
        if (mode == Mode.NAVIGATION && route.isEmpty()) mode = Mode.MANUAL
        return listOf(MissionAck(type = MissionAck.MAV_MISSION_ACCEPTED))
    }

    /** Moves the simulation on by [seconds]. */
    fun advance(seconds: Double) {
        if (seconds <= 0) return
        var wantedSpeed = if (propellerOn) speedStep * settings.knotsPerStep * METERS_PER_SECOND_PER_KNOT else 0.0
        var anchorPull = 0.0 to 0.0

        when (mode) {
            Mode.NAVIGATION -> {
                val target = route.getOrNull(targetIndex)
                if (target != null) {
                    if (distanceMeters(latitude, longitude, target.first, target.second) < ARRIVAL_METERS) {
                        arrive()
                    } else {
                        steerTowards(bearingDegrees(latitude, longitude, target.first, target.second), seconds)
                    }
                }
            }
            Mode.ANCHOR -> {
                // The motor pushes back towards the spot, harder the further it drifted.
                val (lat, lon) = anchorPoint ?: (latitude to longitude)
                val distance = distanceMeters(latitude, longitude, lat, lon)
                wantedSpeed = 0.0
                if (distance > ANCHOR_DEADBAND_METERS) {
                    val bearing = bearingDegrees(latitude, longitude, lat, lon)
                    steerTowards(bearing, seconds)
                    val push = min(ANCHOR_MAX_METERS_PER_SECOND, distance * ANCHOR_GAIN)
                    anchorPull = push * cos(Math.toRadians(bearing)) to push * sin(Math.toRadians(bearing))
                }
            }
            Mode.CRUISE -> steerTowards(cruiseHeading, seconds)
            Mode.MANUAL, Mode.RECORD -> Unit
        }

        // The boat takes a few seconds to reach the speed the motor pushes it to.
        speedMetersPerSecond += (wantedSpeed - speedMetersPerSecond) * min(1.0, seconds / SPEED_TIME_CONSTANT_SECONDS)
        val heading = Math.toRadians(headingDegrees)
        val current = settings.currentKnots * METERS_PER_SECOND_PER_KNOT
        val toward = Math.toRadians(settings.currentTowardDegrees)
        val north = speedMetersPerSecond * cos(heading) + current * cos(toward) + anchorPull.first
        val east = speedMetersPerSecond * sin(heading) + current * sin(toward) + anchorPull.second
        latitude += Math.toDegrees(north * seconds / EARTH_RADIUS_METERS)
        longitude += Math.toDegrees(east * seconds / (EARTH_RADIUS_METERS * cos(Math.toRadians(latitude))))
        lastNorth = north
        lastEast = east
    }

    private var lastNorth = 0.0
    private var lastEast = 0.0

    private fun arrive() {
        if (targetIndex < route.lastIndex) {
            targetIndex++
            return
        }
        when (settings.arrival) {
            Arrival.ANCHOR -> {
                mode = Mode.ANCHOR
                anchorPoint = latitude to longitude
            }
            Arrival.MANUAL -> mode = Mode.MANUAL
            // Stays in navigation: it overshoots and comes back round, again and again.
            Arrival.CIRCLE -> Unit
        }
    }

    private fun steerTowards(bearing: Double, seconds: Double) {
        val difference = normalize(bearing - headingDegrees + 180.0) - 180.0
        val step = settings.turnDegreesPerSecond * seconds
        headingDegrees = normalize(headingDegrees + difference.coerceIn(-step, step))
    }

    /** The messages the motor sends about once a second. */
    fun telemetry(timeMillis: Long): List<MavlinkMessage> {
        val fix = settings.gpsFix
        return listOf(
            Heartbeat(type = MOTOR_TYPE, customMode = FIRMWARE_VERSION),
            SysStatus(voltageBattery = 11_000 + settings.batteryPercent * 20),
            RawImu(
                xgyro = if (propellerOn) RawImu.MOTOR_ON_MARKER else 0,
                ygyro = if (fix) GOOD_GPS_SIGNAL else NO_FIX_SIGNAL,
                zgyro = speedStep,
                zacc = targetIndex,
            ),
            Attitude(
                timeBootMs = timeMillis,
                yaw = (Math.toRadians(headingDegrees).let { if (it > PI) it - 2 * PI else it }).toFloat(),
                pitch = settings.batteryPercent.toFloat(),
                pitchSpeed = mode.code.toFloat(),
            ),
            GlobalPositionInt(
                timeBootMs = timeMillis,
                latitude = if (fix) (latitude * 1e7).roundToInt() else 0,
                longitude = if (fix) (longitude * 1e7).roundToInt() else 0,
                vx = if (fix) (lastNorth * 100).roundToInt() else 0,
                vy = if (fix) (lastEast * 100).roundToInt() else 0,
                hdg = (headingDegrees * 100).roundToInt(),
            ),
        )
    }

    /** One line about the state, for the log and for adb. */
    fun describe(): String = String.format(
        Locale.US,
        "%s, propeller %s, speed step %d (%.1f kn), heading %03.0f°, %.6f,%.6f, route %d waypoint(s), target %d, " +
            "GPS %s, arrival %s, failure %s, current %.1f kn toward %03.0f°",
        mode.name.lowercase(Locale.US),
        if (propellerOn) "on" else "off",
        speedStep,
        speedMetersPerSecond / METERS_PER_SECOND_PER_KNOT,
        headingDegrees,
        latitude,
        longitude,
        route.size,
        targetIndex,
        if (settings.gpsFix) "fix" else "no fix",
        settings.arrival.name.lowercase(Locale.US),
        settings.failure.name.lowercase(Locale.US),
        settings.currentKnots,
        settings.currentTowardDegrees,
    )

    companion object {
        /** Where the simulated boat starts. */
        const val START_LATITUDE = 44.216686739950376
        const val START_LONGITUDE = 14.793421749585923

        const val MOTOR_TYPE = 110
        const val FIRMWARE_VERSION = 101L
        const val GOOD_GPS_SIGNAL = 80
        const val NO_FIX_SIGNAL = 250

        // COMMAND_LONG commands of the type 110 profile, as the app sends them.
        private const val FULL_SPEED = 3
        private const val TURN_RIGHT = 6
        private const val TURN_LEFT = 7
        private const val SPEED_UP = 8
        private const val SPEED_DOWN = 9
        private const val MOTOR_ON = 10
        private const val MOTOR_OFF = 11
        private const val MODE_ANCHOR = 12
        private const val MODE_MANUAL = 13
        private const val MODE_CRUISE = 14
        private const val MODE_NAVIGATION = 15
        private const val MODE_RECORD = 181

        /** How far one steering command turns the motor. */
        private const val STEER_STEP_DEGREES = 2.0
        const val ARRIVAL_METERS = 8.0
        private const val ANCHOR_DEADBAND_METERS = 2.0
        private const val ANCHOR_GAIN = 0.15
        private const val ANCHOR_MAX_METERS_PER_SECOND = 0.8
        private const val SPEED_TIME_CONSTANT_SECONDS = 3.0
        private const val METERS_PER_SECOND_PER_KNOT = 1852.0 / 3600.0
        private const val EARTH_RADIUS_METERS = 6_371_008.8

        private fun normalize(degrees: Double): Double = ((degrees % 360.0) + 360.0) % 360.0

        fun distanceMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val p1 = Math.toRadians(lat1)
            val p2 = Math.toRadians(lat2)
            val h = sin((p2 - p1) / 2).pow(2) + cos(p1) * cos(p2) * sin(Math.toRadians(lon2 - lon1) / 2).pow(2)
            return 2 * EARTH_RADIUS_METERS * asin(sqrt(h))
        }

        fun bearingDegrees(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val p1 = Math.toRadians(lat1)
            val p2 = Math.toRadians(lat2)
            val d = Math.toRadians(lon2 - lon1)
            val y = sin(d) * cos(p2)
            val x = cos(p1) * sin(p2) - sin(p1) * cos(p2) * cos(d)
            return normalize(Math.toDegrees(atan2(y, x)))
        }
    }
}
