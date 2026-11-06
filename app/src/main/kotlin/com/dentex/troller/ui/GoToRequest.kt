package com.dentex.troller.ui

import com.dentex.troller.navigation.GoToState
import com.dentex.troller.navigation.Waypoint
import com.dentex.troller.navigation.bearingDegrees
import com.dentex.troller.navigation.distanceMeters
import java.util.Locale

/**
 * What "Move boat here" or "Follow route" asks for: one point, or a route called [name]. A route
 * started from a later waypoint leaves the first [skipped] out and [waypoints] is the rest.
 */
internal data class GoToRequest(val waypoints: List<Waypoint>, val name: String? = null, val skipped: Int = 0) {
    val target: Waypoint get() = waypoints.first()
    val followsRoute: Boolean get() = name != null
}

/** The go-to and what the chart may do with it. */
internal class GoToActions(
    val state: GoToState,
    /** Why a go-to cannot start now, or null when it can. */
    val blockedBecause: String?,
    /** Starts the go-to at once; Stop on the motor controls ends it. */
    val start: (GoToRequest) -> Unit,
)

/** "0.42 NM · bearing 093°" from [from] to [to]. */
internal fun distanceAndBearing(from: Waypoint, to: Waypoint): String {
    val bearing = Math.round(bearingDegrees(from, to)).toInt() % 360
    return String.format(Locale.US, "%s · bearing %03d°", formatDistance(distanceMeters(from, to)), bearing)
}

/** Metres below one nautical mile, nautical miles from there. */
internal fun formatDistance(meters: Double): String =
    if (meters < METERS_PER_NM) String.format(Locale.US, "%.0f m", meters) else String.format(Locale.US, "%.2f NM", meters / METERS_PER_NM)

private const val METERS_PER_NM = 1852.0
