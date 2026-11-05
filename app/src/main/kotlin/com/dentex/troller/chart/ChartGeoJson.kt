package com.dentex.troller.chart

import com.dentex.troller.navigation.Waypoint
import java.util.Locale

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

    /** The route as one line; empty below two waypoints. */
    fun routeLine(waypoints: List<Waypoint>): String {
        if (waypoints.size < 2) return EMPTY
        val coordinates = waypoints.joinToString(",") { coordinate(it.latitude, it.longitude) }
        return """{"type":"FeatureCollection","features":[{"type":"Feature","properties":{},""" +
            """"geometry":{"type":"LineString","coordinates":[$coordinates]}}]}"""
    }

    /** The waypoints, numbered from 1 like the route screen. */
    fun routePoints(waypoints: List<Waypoint>): String {
        val features = waypoints.mapIndexed { index, waypoint ->
            """{"type":"Feature","properties":{"number":${index + 1}},""" +
                """"geometry":${point(waypoint.latitude, waypoint.longitude)}}"""
        }
        return """{"type":"FeatureCollection","features":[${features.joinToString(",")}]}"""
    }

    private fun point(latitude: Double, longitude: Double): String =
        """{"type":"Point","coordinates":${coordinate(latitude, longitude)}}"""

    // GeoJSON order is longitude first.
    private fun coordinate(latitude: Double, longitude: Double): String =
        String.format(Locale.US, "[%.7f,%.7f]", longitude, latitude)
}
