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

    /** Colours of buoy and beacon dots, by their OpenSeaMap colour. */
    val BUOY_COLORS: Map<String, String> = linkedMapOf(
        "red" to "#d23a2f", "green" to "#2f9a4c", "yellow" to "#f2c40f", "black" to "#222222",
        "white" to "#ffffff", "orange" to "#f28c28",
    )

    /** Magenta of light characteristics, as on paper charts. */
    const val LIGHT_LABEL = "#a8166e"

    /** Seamark kinds written by `tools/charts/seamarks.py`, with the name shown when one is tapped. */
    val SEAMARK_KINDS: Map<String, String> = linkedMapOf(
        "harbour" to "Harbour", "marina" to "Marina", "slipway" to "Slipway", "fuel" to "Fuel",
        "light" to "Light", "buoy" to "Buoy", "beacon" to "Beacon", "rock" to "Rock",
        "wreck" to "Wreck", "obstruction" to "Obstruction", "anchorage" to "Anchorage", "mooring" to "Mooring",
    )

    /** Kinds drawn as an icon named `seamark-<kind>`; buoys, beacons and moorings are coloured dots. */
    val SEAMARK_ICONS: List<String> = listOf("harbour", "marina", "slipway", "fuel", "light", "rock", "wreck", "obstruction", "anchorage")

    /** Image name of a seamark icon. */
    fun seamarkImage(kind: String): String = "seamark-$kind"

    /** Text for a tapped seamark, for example "Light · Fl(2) WR 5s\nRt Kremik". */
    fun describeSeamark(kind: String?, name: String?, light: String?, colour: String?): String {
        val title = SEAMARK_KINDS[kind] ?: "Seamark"
        val first = listOfNotNull(title, colour?.takeIf { kind == "buoy" || kind == "beacon" }, light)
            .joinToString(" · ")
        return if (name.isNullOrBlank()) first else "$first\n$name"
    }

    const val SOURCE_CHART = "chart"
    const val SOURCE_RAINBOW = "rainbow"
    const val SOURCE_BOAT = "boat"
    const val SOURCE_ROUTE_LINE = "route-line"
    const val SOURCE_ROUTE_POINTS = "route-points"
    const val SOURCE_OTHER_ROUTES = "other-routes"
    const val SOURCE_PINS = "pins"
    const val SOURCE_GO_TO = "go-to"
    const val SOURCE_INFO_MARK = "info-mark"
    const val SOURCE_ROUTE_HIGHLIGHT = "route-highlight"

    const val LAYER_DEPTH_AREA = "depth-area"
    const val LAYER_RAINBOW = "depth-rainbow"
    const val LAYER_SEAMARK_DOT = "seamark-dot"
    const val LAYER_SEAMARK_ICON = "seamark-icon"
    const val LAYER_LAND = "land"
    const val LAYER_BOAT_ARROW = "boat-arrow"
    const val LAYER_ROUTE_POINTS = "route-points"
    const val LAYER_ROUTE_LINE = "route-line"
    const val LAYER_OTHER_ROUTE_LINE = "other-route-line"
    const val LAYER_OTHER_ROUTE_POINT = "other-route-point"
    const val LAYER_PIN = "pin"
    const val IMAGE_PIN = "pin"

    /** Arrow image of each position source: "boat-arrow-motor" and "boat-arrow-phone". */
    fun boatImage(source: PositionSource): String = "boat-arrow-${source.property}"
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
        val routeSelected: String,
        val otherRoute: String,
        val pin: String,
        val goTo: String,
        val boatMotor: String,
        val boatPhone: String,
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
        routeSelected = "#f2b705",
        otherRoute = "#7b6a92",
        pin = "#d9480f",
        goTo = "#c2187a",
        boatMotor = "#2e9e44",
        boatPhone = "#5bb8f0",
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
        routeSelected = "#ffd34d",
        otherRoute = "#a99cc2",
        pin = "#ff8a3d",
        goTo = "#ff6fb5",
        boatMotor = "#5fd17a",
        boatPhone = "#8dd0f7",
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
        val buoyColors = BUOY_COLORS.entries.joinToString(", ") { (name, color) -> "\"$name\", \"$color\"" }
        val iconKinds = SEAMARK_ICONS.joinToString(", ") { "\"$it\"" }
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
            "$SOURCE_PINS": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
            "$SOURCE_GO_TO": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
            "$SOURCE_INFO_MARK": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
            "$SOURCE_ROUTE_HIGHLIGHT": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
            "$SOURCE_OTHER_ROUTES": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
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
            { "id": "$LAYER_SEAMARK_DOT", "type": "circle", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "filter": ["match", ["get", "kind"], ["buoy", "beacon", "mooring"], true, false],
              "paint": { "circle-radius": ["interpolate", ["linear"], ["zoom"], 12, 3, 16, 6],
                "circle-color": ["match", ["get", "colour"], $buoyColors, "#9a9a9a"],
                "circle-stroke-color": "#1d2b36", "circle-stroke-width": 1 } },
            { "id": "$LAYER_SEAMARK_ICON", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "filter": ["match", ["get", "kind"], [$iconKinds], true, false],
              "layout": { "icon-image": ["concat", "seamark-", ["get", "kind"]],
                "icon-size": ["interpolate", ["linear"], ["zoom"], 9, 0.7, 14, 1.0],
                "icon-allow-overlap": true, "icon-ignore-placement": true } },
            { "id": "seamark-name", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "minzoom": 11, "filter": ["all", ["has", "name"], ["match", ["get", "kind"], ["harbour", "marina"], true, false]],
              "layout": { "text-field": ["get", "name"], "text-font": ["$FONT"], "text-size": 12,
                "text-anchor": "top", "text-offset": [0, 1.0], "text-optional": true },
              "paint": { "text-color": "${p.label}", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "seamark-light", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "minzoom": 13, "filter": ["all", ["has", "light"], ["!", ["match", ["get", "kind"], ["harbour", "marina"], true, false]]],
              "layout": { "text-field": ["get", "light"], "text-font": ["$FONT"], "text-size": 10,
                "text-anchor": "left", "text-offset": [0.9, 0], "text-optional": true },
              "paint": { "text-color": "$LIGHT_LABEL", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "$LAYER_PIN", "type": "symbol", "source": "$SOURCE_PINS",
              "layout": { "icon-image": "$IMAGE_PIN", "icon-anchor": "bottom", "icon-allow-overlap": true,
                "icon-size": ["case", ["get", "selected"], 1.5, 1.0],
                "symbol-sort-key": ["case", ["get", "selected"], 1, 0] } },
            { "id": "$LAYER_OTHER_ROUTE_LINE", "type": "line", "source": "$SOURCE_OTHER_ROUTES",
              "filter": ["==", ["geometry-type"], "LineString"],
              "layout": { "line-join": "round", "line-cap": "round" },
              "paint": { "line-color": "${p.otherRoute}", "line-width": 2, "line-dasharray": [1, 1.5] } },
            { "id": "$LAYER_OTHER_ROUTE_POINT", "type": "circle", "source": "$SOURCE_OTHER_ROUTES",
              "filter": ["==", ["geometry-type"], "Point"],
              "paint": { "circle-radius": 5, "circle-color": "${p.otherRoute}", "circle-stroke-color": "${p.labelHalo}", "circle-stroke-width": 1 } },
            { "id": "other-route-name", "type": "symbol", "source": "$SOURCE_OTHER_ROUTES",
              "filter": ["==", ["geometry-type"], "LineString"],
              "layout": { "symbol-placement": "line", "text-field": ["get", "name"], "text-font": ["$FONT"], "text-size": 11 },
              "paint": { "text-color": "${p.otherRoute}", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "route-highlight", "type": "line", "source": "$SOURCE_ROUTE_HIGHLIGHT",
              "layout": { "line-join": "round", "line-cap": "round" },
              "paint": { "line-color": "${p.routeSelected}", "line-width": 10, "line-opacity": 0.55 } },
            { "id": "$LAYER_ROUTE_LINE", "type": "line", "source": "$SOURCE_ROUTE_LINE",
              "layout": { "line-join": "round", "line-cap": "round" },
              "paint": { "line-color": "${p.route}", "line-width": 2.5, "line-dasharray": [2, 1.5] } },
            { "id": "$LAYER_ROUTE_POINTS", "type": "circle", "source": "$SOURCE_ROUTE_POINTS",
              "paint": { "circle-radius": ["case", ["get", "selected"], 12, 9], "circle-color": "${p.route}",
                "circle-stroke-color": ["case", ["get", "selected"], "${p.routeSelected}", "${p.labelHalo}"],
                "circle-stroke-width": ["case", ["get", "selected"], 3.5, 1.5] } },
            { "id": "route-numbers", "type": "symbol", "source": "$SOURCE_ROUTE_POINTS",
              "layout": { "text-field": ["to-string", ["get", "number"]], "text-font": ["$FONT"], "text-size": 11,
                "text-allow-overlap": true, "text-ignore-placement": true },
              "paint": { "text-color": "#ffffff" } },
            { "id": "info-mark-halo", "type": "circle", "source": "$SOURCE_INFO_MARK",
              "paint": { "circle-radius": 10, "circle-color": "rgba(0,0,0,0)",
                "circle-stroke-color": "${p.labelHalo}", "circle-stroke-width": 5 } },
            { "id": "info-mark", "type": "circle", "source": "$SOURCE_INFO_MARK",
              "paint": { "circle-radius": 10, "circle-color": "rgba(0,0,0,0)",
                "circle-stroke-color": "${p.label}", "circle-stroke-width": 2.5 } },
            { "id": "info-mark-centre", "type": "circle", "source": "$SOURCE_INFO_MARK",
              "paint": { "circle-radius": 2.5, "circle-color": "${p.label}" } },
            { "id": "go-to-line", "type": "line", "source": "$SOURCE_GO_TO",
              "filter": ["==", ["geometry-type"], "LineString"],
              "layout": { "line-cap": "round" },
              "paint": { "line-color": "${p.goTo}", "line-width": 2.5, "line-dasharray": [1, 1.5],
                "line-opacity": ["case", ["get", "active"], 1.0, 0.5] } },
            { "id": "go-to-target", "type": "circle", "source": "$SOURCE_GO_TO",
              "filter": ["==", ["geometry-type"], "Point"],
              "paint": { "circle-radius": 11, "circle-color": "rgba(0,0,0,0)",
                "circle-stroke-color": "${p.goTo}", "circle-stroke-width": 3,
                "circle-stroke-opacity": ["case", ["get", "active"], 1.0, 0.6] } },
            { "id": "go-to-centre", "type": "circle", "source": "$SOURCE_GO_TO",
              "filter": ["==", ["geometry-type"], "Point"],
              "paint": { "circle-radius": 3.5, "circle-color": "${p.goTo}" } },
            { "id": "boat-dot", "type": "circle", "source": "$SOURCE_BOAT",
              "filter": ["!", ["has", "heading"]],
              "paint": { "circle-radius": 7, "circle-color": ["case", ["get", "stale"], "${p.boatStale}",
                ["==", ["get", "source"], "${PositionSource.PHONE.property}"], "${p.boatPhone}", "${p.boatMotor}"],
                "circle-stroke-color": "#ffffff", "circle-stroke-width": 2 } },
            { "id": "$LAYER_BOAT_ARROW", "type": "symbol", "source": "$SOURCE_BOAT",
              "filter": ["has", "heading"],
              "layout": { "icon-image": ["concat", "boat-arrow-", ["get", "source"]], "icon-rotate": ["get", "heading"], "icon-rotation-alignment": "map",
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
