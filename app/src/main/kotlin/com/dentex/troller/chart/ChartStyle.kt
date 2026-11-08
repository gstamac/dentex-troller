package com.dentex.troller.chart

import com.dentex.troller.navigation.Waypoint
import com.dentex.troller.navigation.distanceMeters
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

    /**
     * Depths in metres where a band starts: every metre down to 150 m, then coarser. Matches
     * `DEPTHS` in the build script.
     */
    val DEPTHS: List<Int> = (0..150).toList() + listOf(200, 300, 500, 750, 1000, 1250)

    /** Contours drawn heavier and labelled from a lower zoom. */
    val MAJOR_CONTOURS: List<Int> = listOf(10, 20, 50, 100, 200, 500, 1000)

    /**
     * Depths where each water shade of a [Palette] is reached, shallow first; between them the
     * colour fades from one shade to the next, down to [Palette.deep] at [DEEP_FROM]. The shades
     * follow the depth, not the number of bands, so a chart built with other levels keeps the
     * same colours.
     */
    val SHADE_DEPTHS: List<Int> = listOf(0, 2, 5, 10, 15, 20, 25, 30)

    /** Depth past which water takes [Palette.deep]. */
    const val DEEP_FROM = 40

    /** Contours between the 5 m ones; drawn lighter and only from [FINE_CONTOUR_ZOOM]. */
    fun isFineContour(depth: Int): Boolean = depth % 5 != 0

    /** Zoom from which the 1 m contours are in the tiles; matches the build script. */
    const val FINE_CONTOUR_ZOOM = 13

    /** Zoom from which the 1 m contours are labelled. */
    const val FINE_LABEL_ZOOM = 15

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
    const val SOURCE_TRACK = "track"

    const val LAYER_DEPTH_AREA = "depth-area"
    const val LAYER_RAINBOW = "depth-rainbow"
    const val LAYER_SEAMARK_DOT = "seamark-dot"
    const val LAYER_SEAMARK_ICON = "seamark-icon"
    const val LAYER_SEAMARK_NAME = "seamark-name"
    const val LAYER_SEAMARK_LIGHT = "seamark-light"

    /**
     * Every layer that draws seamarks, with the filter that picks its features. With all
     * seamarks shown each layer uses this filter; near the boat it is narrowed by [seamarkFilter].
     */
    private val SEAMARK_BASE_FILTERS: Map<String, String> = linkedMapOf(
        LAYER_SEAMARK_DOT to """["match", ["get", "kind"], ["buoy", "beacon", "mooring"], true, false]""",
        LAYER_SEAMARK_ICON to """["match", ["get", "kind"], [${SEAMARK_ICONS.joinToString(", ") { "\"$it\"" }}], true, false]""",
        LAYER_SEAMARK_NAME to """["all", ["has", "name"], ["match", ["get", "kind"], ["harbour", "marina"], true, false]]""",
        LAYER_SEAMARK_LIGHT to """["all", ["has", "light"], ["!", ["match", ["get", "kind"], ["harbour", "marina"], true, false]]]""",
    )

    /** Every layer that draws seamarks. */
    val SEAMARK_LAYERS: List<String> = SEAMARK_BASE_FILTERS.keys.toList()

    /** The seamarks still drawn near the boat when the others are hidden: hazards and buoys. */
    val NEAR_BOAT_KINDS: List<String> = listOf("rock", "wreck", "obstruction", "buoy", "beacon")

    /** Choices for the radius around the boat on the Settings screen, in metres. */
    val NEAR_BOAT_RADII: List<Int> = listOf(25, 50, 100, 200)

    const val DEFAULT_NEAR_BOAT_RADIUS = 50

    /**
     * The filter of seamark layer [layerId]. [near] is null to show every seamark; otherwise only
     * [NEAR_BOAT_KINDS] inside that GeoJSON polygon are drawn, and an empty string draws none,
     * for when the boat position is unknown.
     */
    /**
     * The centre of the near-the-boat circle: [previous] until the boat has moved more than
     * [NEAR_BOAT_MOVE_FRACTION] of [radiusMeters] from it. Every new centre makes MapLibre
     * re-lay the seamark layers, so it is not moved for every position update.
     */
    fun nearBoatCentre(previous: Waypoint?, boat: Waypoint?, radiusMeters: Int): Waypoint? = when {
        boat == null -> previous
        previous == null -> boat
        distanceMeters(previous, boat) > radiusMeters * NEAR_BOAT_MOVE_FRACTION -> boat
        else -> previous
    }

    /** How far the boat moves, as a fraction of the radius, before the circle follows it. */
    const val NEAR_BOAT_MOVE_FRACTION = 0.15

    fun seamarkFilter(layerId: String, near: String?): String {
        val base = requireNotNull(SEAMARK_BASE_FILTERS[layerId]) { "not a seamark layer: $layerId" }
        if (near == null) return base
        if (near.isEmpty()) return """["literal", false]"""
        val kinds = NEAR_BOAT_KINDS.joinToString(", ") { "\"$it\"" }
        return """["all", $base, ["match", ["get", "kind"], [$kinds], true, false], ["within", $near]]"""
    }
    const val LAYER_LAND = "land"
    const val LAYER_BOAT_ARROW = "boat-arrow"
    const val LAYER_ROUTE_POINTS = "route-points"
    const val LAYER_ROUTE_LINE = "route-line"
    const val LAYER_OTHER_ROUTE_LINE = "other-route-line"
    const val LAYER_OTHER_ROUTE_POINT = "other-route-point"
    const val LAYER_PIN = "pin"
    const val LAYER_TRACK = "track"
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
        /** The recorded track, yellow. */
        val track: String,
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
        track = "#ffd000",
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
        track = "#ffd83d",
    )

    /**
     * Depth and colour stops the depth bands are shaded along: each water shade at its
     * [SHADE_DEPTHS] depth, then [Palette.deep] at [DEEP_FROM]. A band takes the colour
     * interpolated at the depth it starts, so the shade fades evenly instead of in steps.
     */
    fun depthColorStops(palette: Palette): List<Pair<Int, String>> =
        SHADE_DEPTHS.zip(palette.water) + (DEEP_FROM to palette.deep)

    /** Human readable range of the band that starts at [depth], for example "2–5 m". */
    fun bandLabel(depth: Int): String {
        val index = DEPTHS.indexOf(depth)
        val next = DEPTHS.getOrNull(index + 1)
        return if (index < 0 || next == null) "more than $depth m" else "$depth–$next m"
    }

    /**
     * Style JSON for the chart stored at [chartPath]. With a [rainbowPath] the rainbow raster
     * is drawn over the depth bands. Without [shaded] all water is [Palette.deep], so only the
     * contour lines show the depth; the bands are still drawn, so a tap still finds the depth.
     */
    fun json(chartPath: String, night: Boolean, rainbowPath: String? = null, shaded: Boolean = true): String {
        val p = if (night) NIGHT else DAY
        val bandStops = depthColorStops(p).joinToString(", ") { (depth, color) -> "$depth, \"$color\"" }
        val bandColor = if (shaded) {
            """["interpolate", ["linear"], ["to-number", ["get", "depth"], $DEEP_FROM], $bandStops]"""
        } else {
            "\"${p.deep}\""
        }
        val major = MAJOR_CONTOURS.joinToString(", ")
        // Not a multiple of 5 m; matches isFineContour.
        val fine = """["!=", ["%", ["get", "depth"], 5], 0]"""
        val buoyColors = BUOY_COLORS.entries.joinToString(", ") { (name, color) -> "\"$name\", \"$color\"" }
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
            "$SOURCE_ROUTE_POINTS": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } },
            "$SOURCE_TRACK": { "type": "geojson", "data": { "type": "FeatureCollection", "features": [] } }
          },
          "layers": [
            { "id": "water", "type": "background", "paint": { "background-color": "${p.water[0]}" } },
            { "id": "$LAYER_DEPTH_AREA", "type": "fill", "source": "$SOURCE_CHART", "source-layer": "depth_area",
              "paint": { "fill-color": $bandColor, "fill-antialias": false } },
            $rainbowLayer
            { "id": "contour-fine", "type": "line", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "minzoom": $FINE_CONTOUR_ZOOM, "filter": $fine,
              "paint": { "line-color": "${p.contour}", "line-opacity": 0.3,
                "line-width": ["interpolate", ["linear"], ["zoom"], $FINE_CONTOUR_ZOOM, 0.3, 17, 0.7] } },
            { "id": "contour-minor", "type": "line", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "filter": ["all", ["!", $fine], ["match", ["get", "depth"], [$major], false, true]],
              "paint": { "line-color": "${p.contour}", "line-opacity": 0.45,
                "line-width": ["interpolate", ["linear"], ["zoom"], 11, 0.4, 15, 0.9] } },
            { "id": "contour-major", "type": "line", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "filter": ["match", ["get", "depth"], [$major], true, false],
              "paint": { "line-color": "${p.contour}", "line-opacity": 0.85,
                "line-width": ["interpolate", ["linear"], ["zoom"], 6, 0.4, 15, 1.5] } },
            { "id": "contour-label-fine", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "minzoom": $FINE_LABEL_ZOOM, "filter": $fine,
              "layout": { "symbol-placement": "line", "symbol-spacing": 350, "text-field": ["to-string", ["get", "depth"]],
                "text-font": ["$FONT"], "text-size": 10, "text-keep-upright": true },
              "paint": { "text-color": "${p.label}", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "contour-label", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "depth_contour",
              "minzoom": 11, "filter": ["!", $fine],
              "layout": { "symbol-placement": "line", "symbol-spacing": 350, "text-field": ["to-string", ["get", "depth"]],
                "text-font": ["$FONT"], "text-size": 11, "text-keep-upright": true },
              "paint": { "text-color": "${p.label}", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "$LAYER_LAND", "type": "fill", "source": "$SOURCE_CHART", "source-layer": "land",
              "paint": { "fill-color": "${p.land}" } },
            { "id": "shore", "type": "line", "source": "$SOURCE_CHART", "source-layer": "land",
              "paint": { "line-color": "${p.shore}", "line-width": ["interpolate", ["linear"], ["zoom"], 6, 0.3, 15, 1.4] } },
            { "id": "$LAYER_SEAMARK_DOT", "type": "circle", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "filter": ${seamarkFilter(LAYER_SEAMARK_DOT, null)},
              "paint": { "circle-radius": ["interpolate", ["linear"], ["zoom"], 12, 3, 16, 6],
                "circle-color": ["match", ["get", "colour"], $buoyColors, "#9a9a9a"],
                "circle-stroke-color": "#1d2b36", "circle-stroke-width": 1 } },
            { "id": "$LAYER_SEAMARK_ICON", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "filter": ${seamarkFilter(LAYER_SEAMARK_ICON, null)},
              "layout": { "icon-image": ["concat", "seamark-", ["get", "kind"]],
                "icon-size": ["interpolate", ["linear"], ["zoom"], 9, 0.7, 14, 1.0],
                "icon-allow-overlap": true, "icon-ignore-placement": true } },
            { "id": "$LAYER_SEAMARK_NAME", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "minzoom": 11, "filter": ${seamarkFilter(LAYER_SEAMARK_NAME, null)},
              "layout": { "text-field": ["get", "name"], "text-font": ["$FONT"], "text-size": 12,
                "text-anchor": "top", "text-offset": [0, 1.0], "text-optional": true },
              "paint": { "text-color": "${p.label}", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "$LAYER_SEAMARK_LIGHT", "type": "symbol", "source": "$SOURCE_CHART", "source-layer": "seamark",
              "minzoom": 13, "filter": ${seamarkFilter(LAYER_SEAMARK_LIGHT, null)},
              "layout": { "text-field": ["get", "light"], "text-font": ["$FONT"], "text-size": 10,
                "text-anchor": "left", "text-offset": [0.9, 0], "text-optional": true },
              "paint": { "text-color": "$LIGHT_LABEL", "text-halo-color": "${p.labelHalo}", "text-halo-width": 1.5 } },
            { "id": "$LAYER_TRACK", "type": "line", "source": "$SOURCE_TRACK",
              "layout": { "line-join": "round", "line-cap": "round" },
              "paint": { "line-color": "${p.track}", "line-width": 2.5 } },
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
