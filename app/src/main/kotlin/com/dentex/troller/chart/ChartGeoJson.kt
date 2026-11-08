package com.dentex.troller.chart

import com.dentex.troller.navigation.Pin
import com.dentex.troller.navigation.SavedRoute
import com.dentex.troller.navigation.Track
import com.dentex.troller.navigation.Waypoint
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** GeoJSON for the live chart overlays: the boat and the route being edited. */
object ChartGeoJson {

    private const val EMPTY = """{"type":"FeatureCollection","features":[]}"""

    /**
     * The boat position. The heading is left out when the motor has not reported one, so the
     * chart shows a dot instead of an arrow pointing in an invented direction.
     */
    fun boat(position: BoatPosition?): String {
        if (position == null) return EMPTY
        val heading = position.headingDegrees?.let { String.format(Locale.US, ""","heading":%.1f""", it) } ?: ""
        return """{"type":"FeatureCollection","features":[{"type":"Feature",""" +
            """"properties":{"source":"${position.source.property}","stale":${position.stale}$heading},""" +
            """"geometry":${point(position.latitude, position.longitude)}}]}"""
    }

    /**
     * The recorded track, one line per segment. While recording, [tail] (the boat) is joined to
     * the last point, so the line keeps up with the boat between recorded points.
     */
    fun track(track: Track, tail: Waypoint? = null): String {
        val lines = track.segments.mapIndexed { index, segment ->
            segment.map { it.latitude to it.longitude } +
                listOfNotNull(tail?.takeIf { index == track.segments.lastIndex }?.let { it.latitude to it.longitude })
        }.filter { it.size >= 2 }
        if (lines.isEmpty()) return EMPTY
        val coordinates = lines.joinToString(",") { line ->
            line.joinToString(",", "[", "]") { (latitude, longitude) -> coordinate(latitude, longitude) }
        }
        return """{"type":"FeatureCollection","features":[{"type":"Feature","properties":{},""" +
            """"geometry":{"type":"MultiLineString","coordinates":[$coordinates]}}]}"""
    }

    /** The route as one line; empty below two waypoints. */
    fun routeLine(waypoints: List<Waypoint>): String {
        if (waypoints.size < 2) return EMPTY
        val coordinates = waypoints.joinToString(",") { coordinate(it.latitude, it.longitude) }
        return """{"type":"FeatureCollection","features":[{"type":"Feature","properties":{},""" +
            """"geometry":{"type":"LineString","coordinates":[$coordinates]}}]}"""
    }

    /** The waypoints, numbered from 1 like the route screen. [selectedIndex] is drawn highlighted. */
    fun routePoints(waypoints: List<Waypoint>, selectedIndex: Int? = null): String {
        val features = waypoints.mapIndexed { index, waypoint ->
            """{"type":"Feature","properties":{"index":$index,"number":${index + 1},"selected":${index == selectedIndex}},""" +
                """"geometry":${point(waypoint.latitude, waypoint.longitude)}}"""
        }
        return """{"type":"FeatureCollection","features":[${features.joinToString(",")}]}"""
    }

    /**
     * The routes that are not selected: a line each (from two waypoints) and a point for every
     * waypoint. Each feature carries the route id, so a tap can select it, and the line carries
     * the name for the label along it.
     */
    fun otherRoutes(routes: List<SavedRoute>): String {
        val features = routes.flatMap { route ->
            val properties = """{"id":${quote(route.id)},"name":${quote(route.name)}}"""
            val line = if (route.waypoints.size < 2) {
                emptyList()
            } else {
                val coordinates = route.waypoints.joinToString(",") { coordinate(it.latitude, it.longitude) }
                listOf("""{"type":"Feature","properties":$properties,"geometry":{"type":"LineString","coordinates":[$coordinates]}}""")
            }
            line + route.waypoints.map {
                """{"type":"Feature","properties":{"id":${quote(route.id)}},"geometry":${point(it.latitude, it.longitude)}}"""
            }
        }
        return """{"type":"FeatureCollection","features":[${features.joinToString(",")}]}"""
    }

    /** The pins, each with its id for a tap and its name for the label. [selectedId] is drawn larger. */
    fun pins(pins: List<Pin>, selectedId: String? = null): String {
        val features = pins.map { pin ->
            """{"type":"Feature","properties":{"id":${quote(pin.id)},"name":${quote(pin.name)},"selected":${pin.id == selectedId}},""" +
                """"geometry":${point(pin.latitude, pin.longitude)}}"""
        }
        return """{"type":"FeatureCollection","features":[${features.joinToString(",")}]}"""
    }

    /**
     * The go-to target as a ring, and a line to it from the boat when the boat position is
     * known. [active] is true once the go-to started; a target that is only picked is drawn fainter.
     */
    fun goTo(target: Waypoint?, boat: BoatPosition?, active: Boolean, ring: Boolean = true): String {
        if (target == null) return EMPTY
        val properties = """{"active":$active}"""
        val features = buildList {
            if (boat != null) {
                add(
                    """{"type":"Feature","properties":$properties,"geometry":{"type":"LineString","coordinates":""" +
                        """[${coordinate(boat.latitude, boat.longitude)},${coordinate(target.latitude, target.longitude)}]}}""",
                )
            }
            if (ring) add("""{"type":"Feature","properties":$properties,"geometry":${point(target.latitude, target.longitude)}}""")
        }
        return """{"type":"FeatureCollection","features":[${features.joinToString(",")}]}"""
    }

    /** A ring at the spot whose info is shown, or nothing. */
    fun mark(at: Waypoint?): String {
        if (at == null) return EMPTY
        return """{"type":"FeatureCollection","features":[{"type":"Feature","properties":{},""" +
            """"geometry":${point(at.latitude, at.longitude)}}]}"""
    }

    private fun quote(text: String): String = buildString {
        append('"')
        text.forEach { c ->
            when {
                c == '"' -> append("\\\"")
                c == '\\' -> append("\\\\")
                c < ' ' -> append(String.format(Locale.US, "\\u%04x", c.code))
                else -> append(c)
            }
        }
        append('"')
    }

    /**
     * A circle of [radiusMeters] around a position, as a GeoJSON Polygon geometry of [sides]
     * corners, for a `within` filter. Longitude degrees shrink towards the poles, so they are
     * scaled by the latitude; that is exact enough for a few hundred metres.
     */
    fun circle(latitude: Double, longitude: Double, radiusMeters: Double, sides: Int = 24): String {
        val dLat = Math.toDegrees(radiusMeters / EARTH_RADIUS_METERS)
        val dLon = dLat / cos(Math.toRadians(latitude))
        // The ring is closed: the last coordinate repeats the first.
        val ring = (0..sides).joinToString(",") { i ->
            val angle = 2 * PI * (i % sides) / sides
            coordinate(latitude + dLat * sin(angle), longitude + dLon * cos(angle))
        }
        return """{"type":"Polygon","coordinates":[[$ring]]}"""
    }

    private const val EARTH_RADIUS_METERS = 6_371_000.0

    private fun point(latitude: Double, longitude: Double): String =
        """{"type":"Point","coordinates":${coordinate(latitude, longitude)}}"""

    // GeoJSON order is longitude first.
    private fun coordinate(latitude: Double, longitude: Double): String =
        String.format(Locale.US, "[%.7f,%.7f]", longitude, latitude)
}
