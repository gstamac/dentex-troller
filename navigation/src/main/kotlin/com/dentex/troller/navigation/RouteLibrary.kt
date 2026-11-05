package com.dentex.troller.navigation

import java.util.Locale
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/** A route kept on the phone. */
data class SavedRoute(
    val id: String,
    val name: String,
    val waypoints: List<Waypoint>,
    val createdMillis: Long,
) {
    /** Length along the waypoints in metres. */
    val lengthMeters: Double
        get() = waypoints.zipWithNext { a, b -> distanceMeters(a, b) }.sum()
}

/**
 * All routes kept on the phone and the one being edited. Every change returns a new library, so
 * the view model can save it and publish it in one step.
 */
data class RouteLibrary(
    val routes: List<SavedRoute> = emptyList(),
    val selectedId: String? = null,
) {
    val selected: SavedRoute? get() = routes.firstOrNull { it.id == selectedId }

    fun select(id: String?): RouteLibrary =
        copy(selectedId = id?.takeIf { wanted -> routes.any { it.id == wanted } })

    /** Adds an empty route named "Route N" and selects it. */
    fun create(id: String, nowMillis: Long, name: String = nextName()): RouteLibrary =
        copy(routes = routes + SavedRoute(id, name, emptyList(), nowMillis), selectedId = id)

    /** Adds a route with waypoints, for example one downloaded from the motor, and selects it. */
    fun add(route: SavedRoute): RouteLibrary = copy(routes = routes + route, selectedId = route.id)

    /** Changes the waypoints of the selected route; does nothing without a selection. */
    fun editSelected(change: (List<Waypoint>) -> List<Waypoint>): RouteLibrary {
        val id = selectedId ?: return this
        return copy(routes = routes.map { if (it.id == id) it.copy(waypoints = change(it.waypoints)) else it })
    }

    fun rename(id: String, name: String): RouteLibrary {
        val clean = cleanName(name)
        if (clean.isEmpty()) return this
        return copy(routes = routes.map { if (it.id == id) it.copy(name = clean) else it })
    }

    /** Deletes a route. Deleting the selected one leaves nothing selected. */
    fun delete(id: String): RouteLibrary =
        copy(routes = routes.filterNot { it.id == id }, selectedId = selectedId.takeIf { it != id })

    /** "Route N" with the first N above every number already used that way. */
    fun nextName(): String {
        val used = routes.mapNotNull { NAME_NUMBER.matchEntire(it.name)?.groupValues?.get(1)?.toIntOrNull() }
        return "Route ${(used.maxOrNull() ?: 0) + 1}"
    }

    /** The text form stored on the phone. */
    fun encode(): String = buildString {
        appendLine(HEADER)
        appendLine("selected ${selectedId ?: NONE}")
        routes.forEach { route ->
            appendLine("route ${route.id} ${route.createdMillis}")
            appendLine("name ${route.name}")
            route.waypoints.forEach { appendLine(String.format(Locale.US, "wp %.7f %.7f", it.latitude, it.longitude)) }
        }
    }

    companion object {
        private const val HEADER = "dentex-routes 1"
        private const val NONE = "-"
        private val NAME_NUMBER = Regex("""Route (\d+)""")

        /** Names are stored one per line, so line breaks are folded into spaces. */
        fun cleanName(name: String): String = name.replace(Regex("""\s+"""), " ").trim()

        /** Reads [encode]'s text. Throws [IllegalArgumentException] when it is not a route file. */
        fun decode(text: String): RouteLibrary {
            val lines = text.lines().map { it.trimEnd() }.filter { it.isNotEmpty() }
            require(lines.firstOrNull() == HEADER) { "Not a route file" }
            var selected: String? = null
            val routes = mutableListOf<SavedRoute>()
            for (line in lines.drop(1)) {
                val key = line.substringBefore(' ')
                val rest = line.substringAfter(' ', "")
                when (key) {
                    "selected" -> selected = rest.takeIf { it != NONE }
                    "route" -> {
                        val parts = rest.split(' ')
                        require(parts.size == 2) { "Bad route line: $line" }
                        routes += SavedRoute(parts[0], "", emptyList(), parts[1].toLong())
                    }
                    "name" -> routes[routes.lastIndex] = routes.last().copy(name = rest)
                    "wp" -> {
                        val (latitude, longitude) = rest.split(' ').map { it.toDouble() }
                        val last = routes.last()
                        routes[routes.lastIndex] = last.copy(waypoints = last.waypoints + Waypoint(latitude, longitude))
                    }
                    else -> Unit // Written by a newer version; skipped.
                }
            }
            return RouteLibrary(routes).select(selected)
        }
    }
}

private const val EARTH_RADIUS_METERS = 6_371_008.8

/** Great-circle distance in metres. */
fun distanceMeters(a: Waypoint, b: Waypoint): Double {
    val lat1 = Math.toRadians(a.latitude)
    val lat2 = Math.toRadians(b.latitude)
    val h = sin((lat2 - lat1) / 2).pow(2) +
        cos(lat1) * cos(lat2) * sin(Math.toRadians(b.longitude - a.longitude) / 2).pow(2)
    return 2 * EARTH_RADIUS_METERS * asin(sqrt(h))
}

/**
 * The point of the route line closest to [from], or null for a route without waypoints.
 *
 * Works on a flat projection around [from], which is exact enough at the scale of a trolling route.
 */
fun nearestPointOnRoute(route: List<Waypoint>, from: Waypoint): Waypoint? {
    if (route.isEmpty()) return null
    if (route.size == 1) return route.first()
    val scale = cos(Math.toRadians(from.latitude))
    fun x(p: Waypoint) = (p.longitude - from.longitude) * scale
    fun y(p: Waypoint) = p.latitude - from.latitude
    var best = route.first()
    var bestSquared = Double.MAX_VALUE
    route.zipWithNext { a, b ->
        val ax = x(a)
        val ay = y(a)
        val dx = x(b) - ax
        val dy = y(b) - ay
        val lengthSquared = dx * dx + dy * dy
        // The fraction along a→b of the foot of the perpendicular from the origin, kept on the segment.
        val t = if (lengthSquared == 0.0) 0.0 else (-(ax * dx + ay * dy) / lengthSquared).coerceIn(0.0, 1.0)
        val px = ax + t * dx
        val py = ay + t * dy
        val squared = px * px + py * py
        if (squared < bestSquared) {
            bestSquared = squared
            best = Waypoint(
                latitude = a.latitude + t * (b.latitude - a.latitude),
                longitude = a.longitude + t * (b.longitude - a.longitude),
            )
        }
    }
    return best
}
