package com.dentex.troller.chart

import java.util.Locale

/**
 * The MapLibre style of the offline chart.
 *
 * The layers follow paper chart conventions: buff land, blue shallows that fade to white with
 * depth, and contour lines with their depth in metres. The night palette keeps the same order of
 * tints on a dark background, like the night mode of a chart plotter.
 *
 * The PMTiles file is built by `tools/charts/build-chart.sh` and carries three layers:
 * `land`, `depth_area` (each band carries the depth where it starts) and `depth_contour`.
 *
 * The optional rainbow file is a raster of the same depths in EMODnet's rainbow colour ramp. When
 * it is shown it covers the depth bands, which stay underneath for the depth readout and fill the
 * gaps where the raster has no water.
 */
object ChartStyle {

    /** Depths in metres where a band starts; matches `DEPTHS` in the build script. */
    val DEPTHS: List<Int> = listOf(0, 2, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100, 125, 150, 200, 300, 500, 750, 1000, 1250)

    /** Contours drawn heavier and labelled from a lower zoom. */
    val MAJOR_CONTOURS: List<Int> = listOf(10, 20, 50, 100, 200, 500, 1000)

    /**
     * Stops of the rainbow ramp: depth in metres and colour, shallow first. They match the colour
     * file in the build script, which copies EMODnet's `rainbowcolour` WMS style.
     */
    val RAINBOW: List<Pair<Int, String>> = listOf(
        0 to "#ce0010", 5 to "#ff081e", 40 to "#e5eb02", 200 to "#02e411", 500 to "#00c7dc",
        3000 to "#0057d9", 4800 to "#120091", 5500 to "#7818cf", 6000 to "#b600d0",
    )

    const val SOURCE_CHART = "chart"
    const val SOURCE_RAINBOW = "rainbow"
    const val SOURCE_BOAT = "boat"
    const val SOURCE_ROUTE_LINE = "route-line"
    const val SOURCE_ROUTE_POINTS = "route-points"

    const val LAYER_DEPTH_AREA = "depth-area"
    const val LAYER_RAINBOW = "depth-rainbow"
    const val LAYER_LAND = "land"
    const val LAYER_BOAT_ARROW = "boat-arrow"

    const val IMAGE_BOAT = "boat-arrow"
    const val FONT = "NotoSansRegular"

    /** Adriatic bounds of the chart file: west, south, east, north. */
    val BOUNDS: DoubleArray = doubleArrayOf(12.0, 39.75, 20.05, 45.85)

    data class Palette(
        val water: List<String>,
        val deep: String,
        val land: String,
        val shore: String,
        val contour: String,
        val label: String,
        val labelHalo: String,
        val route: String,
        val boat: String,
        val boatStale: String,
    )

    val DAY = Palette(
        water = listOf("#7fb6dc", "#98c5e4", "#b0d3ea", "#c6e0f0", "#d6e9f4", "#e3f0f7", "#ecf5fa", "#f3f9fc"),
        deep = "#f8fbfd",
        land = "#e9dcae",
        shore = "#9a8350",
        contour = "#3f6f8f",
        label = "#2b5573",
        labelHalo = "#f8fbfd",
        route = "#b0412e",
        boat = "#d24a1f",
        boatStale = "#8a8a8a",
    )

    val NIGHT = Palette(
        water = listOf("#1f5578", "#1c4b6a", "#19425d", "#163a52", "#143349", "#122d41", "#10283a", "#0e2333"),
        deep = "#0c1f2d",
        land = "#3b3526",
        shore = "#7d6b44",
        contour = "#6f9fbf",
        label = "#9cc3dc",
        labelHalo = "#0c1f2d",
        route = "#e0745f",
        boat = "#ff8a4c",
        boatStale = "#6b6b6b",
    )

    /** Fill colour of the band that starts at [depth]. */
    fun bandColor(palette: Palette, depth: Int): String {
        val index = DEPTHS.indexOf(depth)
        return if (index in palette.water.indices) palette.water[index] else palette.deep
    }

    /** Human readable range of the band that starts at [depth], for example "2–5 m". */
    fun bandLabel(depth: Int): String {
        val index = DEPTHS.indexOf(depth)
        val next = DEPTHS.getOrNull(index + 1)
        return if (index < 0 || next == null) "more than $depth m" else "$depth–$next m"
    }

    /**
     * Style JSON for the chart stored at [chartPath]. With a [rainbowPath] the rainbow raster
     * is drawn over the depth bands.
     */
    fun json(chartPath: String, night: Boolean, rainbowPath: String? = null): String {
        val p = if (night) NIGHT else DAY
        val bandColors = DEPTHS.joinToString(", ") { "$it, \"${bandColor(p, it)}\"" }
        val major = MAJOR_CONTOURS.joinToString(", ")
        val rainbowSource = rainbowPath?.let {
            """"$SOURCE_RAINBOW": { "type": "raster", "url": ${quote("pmtiles://file://$it")}, "tileSize": 256 },"""
        } ?: ""
        // Dimmed at night so the bright ramp does not spoil night vision.
        val rainbowLayer = rainbowPath?.let {
            """{ "id": "$LAYER_RAINBOW", "type": "raster", "source": "$SOURCE_RAINBOW",
              "paint": { "raster-resampling": "linear", "raster-fade-duration": 0,
                "raster-brightness-max": ${if (night) 0.45 else 1.0}, "raster-saturation": ${if (night) -0.3 else 0.0} } },"""
        } ?: ""
        return """
        {
          "version": 8,
          "name": "Adriatic chart",
          "glyphs": "asset://glyphs/{fontstack}/{range}.pbf",
          "sources": {
            "$SOURCE_CHART": { "type": "vector", "url": ${quote("pmtiles://file://$chartPath")} },
            $rainbowSource
            "$SOURCE_BOAT": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
            "$SOURCE_ROUTE_LINE": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
            "$SOURCE_ROUTE_POINTS": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } }
          },
          "layers": [
            { "id": "water", "type": "background", "paint": { "background-color": "${p.water[0]}" } },
            { "id": "$LAYER_DEPTH_AREA", "type": "fill", "source": "$SOURCE_CHART", "source-layer": "depth_area",
              "paint": { "fill-color": ["match", ["get", "depth"], $bandColors, "${p.deep}"], "fill-antialias": false } },
            $rainbowLayer
            { "id": "contour-minor", "type": "line", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "filter": ["match", ["get", "depth"], [$major], false, true],
              "paint": { "line-color": "${p.contour}", "line-opacity": 0.45,
                "line-width": ["interpolate", ["linear"], ["zoom"], 11, 0.4, 15, 0.9] } },
            { "id": "contour-major", "type": "line", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "filter": ["match", ["get", "depth"], [$major], true, false],
              "paint": { "line-color": "${p.contour}", "line-opacity": 0.85,
                "line-width": ["interpolate", ["linear"], ["zoom"], 6, 0.4, 15, 1.5] } },
            { "id": "contour-label", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "minzoom": 11,
              "layout": { "symbol-placement": "line", "symbol-spacing": 350, "text-field": ["to-string", ["get", "depth"]],
                "text-font": ["$FONT"], "text-size": 11, "text-keep-upright": true },
              "paint": { "text-color": "${p.label}", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "$LAYER_LAND", "type": "fill", "source": "$SOURCE_CHART", "source-layer": "land",
              "paint": { "fill-color": "${p.land}" } },
            { "id": "shore", "type": "line", "source": "$SOURCE_CHART", "source-layer": "land",
              "paint": { "line-color": "${p.shore}", "line-width": ["interpolate", ["linear"], ["zoom"], 6, 0.3, 15, 1.4] } },
            { "id": "route-line", "type": "line", "source": "$SOURCE_ROUTE_LINE",
              "layout": { "line-join": "round", "line-cap": "round" },
              "paint": { "line-color": "${p.route}", "line-width": 2.5, "line-dasharray": [2, 1.5] } },
            { "id": "route-points", "type": "circle", "source": "$SOURCE_ROUTE_POINTS",
              "paint": { "circle-radius": 9, "circle-color": "${p.route}", "circle-stroke-color": "${p.labelHalo}", "circle-stroke-width": 1.5 } },
            { "id": "route-numbers", "type": "symbol", "source": "$SOURCE_ROUTE_POINTS",
              "layout": { "text-field": ["to-string", ["get", "number"]], "text-font": ["$FONT"], "text-size": 11,
                "text-allow-overlap": true, "text-ignore-placement": true },
              "paint": { "text-color": "#ffffff" } },
            { "id": "boat-dot", "type": "circle", "source": "$SOURCE_BOAT",
              "filter": ["!", ["has", "heading"]],
              "paint": { "circle-radius": 7, "circle-color": ["case", ["get", "stale"], "${p.boatStale}", "${p.boat}"],
                "circle-stroke-color": "#ffffff", "circle-stroke-width": 2 } },
            { "id": "$LAYER_BOAT_ARROW", "type": "symbol", "source": "$SOURCE_BOAT",
              "filter": ["has", "heading"],
              "layout": { "icon-image": "$IMAGE_BOAT", "icon-rotate": ["get", "heading"], "icon-rotation-alignment": "map",
                "icon-allow-overlap": true, "icon-ignore-placement": true },
              "paint": { "icon-opacity": ["case", ["get", "stale"], 0.45, 1.0] } }
          ]
        }
        """.trimIndent()
    }

    /** Formats a position as degrees and decimal minutes, the way charts write it. */
    fun formatPosition(latitude: Double, longitude: Double): String =
        "${degreesMinutes(latitude, 'N', 'S')}  ${degreesMinutes(longitude, 'E', 'W')}"

    private fun degreesMinutes(value: Double, positive: Char, negative: Char): String {
        val absolute = kotlin.math.abs(value)
        val degrees = absolute.toInt()
        val minutes = (absolute - degrees) * 60.0
        return String.format(Locale.US, "%d°%06.3f′%c", degrees, minutes, if (value >= 0) positive else negative)
    }

    private fun quote(value: String): String =
        "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
}
