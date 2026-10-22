package com.dentex.troller.navigation

/**
 * A single waypoint of a route.
 *
 * Coordinates are decimal degrees. The type 110 motor receives them as plain floats in
 * `MISSION_ITEM.x` and `MISSION_ITEM.y`, without the 1e7 scaling that `GLOBAL_POSITION_INT` uses.
 */
data class Waypoint(
    val latitude: Double,
    val longitude: Double,
) {
    init {
        require(latitude in -90.0..90.0) { "Latitude $latitude is out of range" }
        require(longitude in -180.0..180.0) { "Longitude $longitude is out of range" }
    }
}

/** An ordered list of waypoints. */
data class Route(
    val waypoints: List<Waypoint>,
) {
    val size: Int get() = waypoints.size

    val isEmpty: Boolean get() = waypoints.isEmpty()

    companion object {
        val EMPTY = Route(emptyList())
    }
}

/** Progress of a route transfer. */
data class MissionProgress(
    val phase: Phase,
    val total: Int = 0,
    val completed: Int = 0,
    val message: String? = null,
) {
    enum class Phase {
        IDLE,
        UPLOADING,
        DOWNLOADING,
        COMPLETE,
        FAILED,
    }

    /** Progress in percent, or `null` when the total is unknown. */
    val percent: Int? get() = if (total > 0) ((completed * 100) / total) else null
}
