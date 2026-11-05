package com.dentex.troller.chart

import com.dentex.troller.navigation.Pin
import com.dentex.troller.navigation.SavedRoute
import com.dentex.troller.navigation.Waypoint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PmtilesHeaderTest {

    private fun header(magic: String, version: Int, tileType: Int = PmtilesHeader.TILE_TYPE_MVT): ByteArray =
        ByteArray(PmtilesHeader.LENGTH).also { bytes ->
            magic.toByteArray(Charsets.US_ASCII).copyInto(bytes)
            bytes[magic.length] = version.toByte()
            bytes[PmtilesHeader.TILE_TYPE_OFFSET] = tileType.toByte()
        }

    @Test
    fun acceptsAVersion3Archive() {
        assertTrue(PmtilesHeader.isVersion3(header("PMTiles", 3)))
    }

    @Test
    fun rejectsOtherVersionsAndOtherFiles() {
        assertFalse(PmtilesHeader.isVersion3(header("PMTiles", 2)))
        assertFalse(PmtilesHeader.isVersion3(header("SQLite ", 3)))
        assertFalse(PmtilesHeader.isVersion3(ByteArray(4)))
    }

    @Test
    fun vectorTilesAreTheChartAndImagesAreTheRainbowLayer() {
        assertEquals(ChartKind.DEPTHS, PmtilesHeader.kind(header("PMTiles", 3, PmtilesHeader.TILE_TYPE_MVT)))
        assertEquals(ChartKind.RAINBOW, PmtilesHeader.kind(header("PMTiles", 3, PmtilesHeader.TILE_TYPE_WEBP)))
        assertEquals(ChartKind.RAINBOW, PmtilesHeader.kind(header("PMTiles", 3, PmtilesHeader.TILE_TYPE_PNG)))
    }

    @Test
    fun unknownTileTypesAndOtherFilesAreRejected() {
        assertNull(PmtilesHeader.kind(header("PMTiles", 3, tileType = 0)))
        assertNull(PmtilesHeader.kind(header("PMTiles", 3, tileType = 5)))
        assertNull(PmtilesHeader.kind(header("PMTiles", 2)))
        assertNull(PmtilesHeader.kind(ByteArray(8)))
    }
}

class ChartStyleTest {

    /** A style that is not strict JSON (a comment, a trailing comma) leaves the whole chart blank. */
    @Test
    fun bothStylesAreStrictJson() {
        for (night in listOf(false, true)) {
            for (rainbow in listOf(null, "/rainbow.pmtiles")) {
                val reader = com.google.gson.stream.JsonReader(java.io.StringReader(ChartStyle.json("/chart.pmtiles", night, rainbow)))
                reader.isLenient = false
                com.google.gson.internal.Streams.parse(reader)
                assertEquals(com.google.gson.stream.JsonToken.END_DOCUMENT, reader.peek())
            }
        }
        // The check itself catches a comment.
        val withComment = com.google.gson.stream.JsonReader(java.io.StringReader("{ // no\n \"a\": 1 }"))
        withComment.isLenient = false
        val refused = runCatching { com.google.gson.internal.Streams.parse(withComment) }.isFailure
        assertTrue(refused)
    }

    /**
     * MapLibre drops a layer whose zoom expression is not the input of the outermost step or
     * interpolate, without an error on screen. That once hid every pin.
     */
    @Test
    fun zoomIsOnlyUsedAsTheInputOfATopLevelStepOrInterpolate() {
        val json = ChartStyle.json("/chart.pmtiles", night = false, rainbowPath = "/rainbow.pmtiles")
        Regex("""("[a-z-]+"): (\[[^\n]*?\["zoom"\])""").findAll(json).forEach { match ->
            val expression = match.groupValues[2]
            assertTrue(
                "${match.groupValues[1]} uses zoom inside another expression: $expression",
                expression.startsWith("[\"step\", [\"zoom\"]") ||
                    expression.matches(Regex("""\["interpolate", \["[a-z]+"\], \["zoom"\]""")),
            )
        }
    }

    @Test
    fun theStyleReadsTheLocalChartFile() {
        val json = ChartStyle.json("/storage/emulated/0/Android/data/com.dentex.troller/files/charts/adriatic.pmtiles", night = false)
        assertTrue(json.contains("\"pmtiles://file:///storage/emulated/0/Android/data/com.dentex.troller/files/charts/adriatic.pmtiles\""))
        for (layer in listOf("depth_area", "depth_contour", "land")) {
            assertTrue("missing source layer $layer", json.contains("\"source-layer\": \"$layer\""))
        }
    }

    @Test
    fun theRainbowLayerIsOnlyAddedWhenItsFileIsGiven() {
        val without = ChartStyle.json("/charts/adriatic.pmtiles", night = false)
        assertFalse(without.contains(ChartStyle.LAYER_RAINBOW))

        val with = ChartStyle.json("/charts/adriatic.pmtiles", night = false, rainbowPath = "/charts/adriatic-rainbow.pmtiles")
        assertTrue(with.contains("\"type\": \"raster\", \"url\": \"pmtiles://file:///charts/adriatic-rainbow.pmtiles\""))
        // Drawn over the depth bands and under the contours and the land.
        val bands = with.indexOf("\"id\": \"${ChartStyle.LAYER_DEPTH_AREA}\"")
        val rainbow = with.indexOf("\"id\": \"${ChartStyle.LAYER_RAINBOW}\"")
        val contours = with.indexOf("\"id\": \"contour-minor\"")
        assertTrue(bands in 0 until rainbow && rainbow < contours)
    }

    @Test
    fun seamarksAreDrawnAboveTheLandAndBelowTheRoute() {
        val json = ChartStyle.json("/charts/adriatic.pmtiles", night = false)
        assertTrue(json.contains("\"source-layer\": \"seamark\""))
        val land = json.indexOf("\"id\": \"${ChartStyle.LAYER_LAND}\"")
        val icons = json.indexOf("\"id\": \"${ChartStyle.LAYER_SEAMARK_ICON}\"")
        val route = json.indexOf("\"id\": \"route-line\"")
        assertTrue(land in 0 until icons && icons < route)
        // Every icon kind has an image, and every image kind is a known seamark kind.
        assertTrue(ChartStyle.SEAMARK_KINDS.keys.containsAll(ChartStyle.SEAMARK_ICONS))
    }

    @Test
    fun aTappedSeamarkIsDescribedWithItsLightAndName() {
        assertEquals("Light · Fl(2) WR 5s\nRt Kremik", ChartStyle.describeSeamark("light", "Rt Kremik", "Fl(2) WR 5s", null))
        assertEquals("Buoy · red", ChartStyle.describeSeamark("buoy", null, null, "red"))
        assertEquals("Marina\nACI Šibenik", ChartStyle.describeSeamark("marina", "ACI Šibenik", null, "blue"))
        assertEquals("Seamark", ChartStyle.describeSeamark("something_new", "", null, null))
    }

    @Test
    fun theRainbowStopsGoFromShallowToDeep() {
        val depths = ChartStyle.RAINBOW.map { it.first }
        assertEquals(depths.sorted(), depths)
        assertEquals(0, depths.first())
    }

    @Test
    fun aQuoteInThePathCannotBreakTheStyle() {
        val json = ChartStyle.json("/a\"b", night = true)
        assertTrue(json.contains("\"pmtiles://file:///a\\\"b\""))
    }

    @Test
    fun everyBandHasItsOwnRange() {
        assertEquals("0–2 m", ChartStyle.bandLabel(0))
        assertEquals("30–40 m", ChartStyle.bandLabel(30))
        assertEquals("more than 1250 m", ChartStyle.bandLabel(1250))
    }

    @Test
    fun shallowBandsAreTintedAndDeepWaterIsPlain() {
        assertEquals(ChartStyle.DAY.water[0], ChartStyle.bandColor(ChartStyle.DAY, 0))
        assertEquals(ChartStyle.DAY.water[7], ChartStyle.bandColor(ChartStyle.DAY, 30))
        assertEquals(ChartStyle.DAY.deep, ChartStyle.bandColor(ChartStyle.DAY, 40))
        assertEquals(ChartStyle.DAY.deep, ChartStyle.bandColor(ChartStyle.DAY, 500))
    }

    @Test
    fun positionsAreWrittenInDegreesAndMinutes() {
        assertEquals("44°13.214′N  14°47.731′E", ChartStyle.formatPosition(44.22023319537834, 14.795524186248247))
    }
}

class ChartGeoJsonTest {

    @Test
    fun theGoToTargetHasALineFromTheBoatOnlyWhenTheBoatIsKnown() {
        val target = Waypoint(44.3, 14.9)
        assertTrue(ChartGeoJson.goTo(null, null, active = false).contains("\"features\":[]"))

        val alone = ChartGeoJson.goTo(target, null, active = false)
        assertTrue(alone.contains("\"type\":\"Point\",\"coordinates\":[14.9000000,44.3000000]"))
        assertTrue(!alone.contains("LineString"))
        assertTrue(alone.contains("\"active\":false"))

        val withBoat = ChartGeoJson.goTo(target, BoatPosition(44.2, 14.8, null, PositionSource.MOTOR, stale = false), active = true)
        assertTrue(withBoat.contains("\"coordinates\":[[14.8000000,44.2000000],[14.9000000,44.3000000]]"))
        assertTrue(withBoat.contains("\"active\":true"))

        val lineOnly = ChartGeoJson.goTo(target, BoatPosition(44.2, 14.8, null, PositionSource.MOTOR, stale = false), active = false, ring = false)
        assertTrue(lineOnly.contains("LineString"))
        assertTrue(!lineOnly.contains("\"type\":\"Point\""))
    }

    @Test
    fun onlyTheSelectedPinIsMarkedSelected() {
        val pins = listOf(Pin("p1", "a", 44.0, 14.0, null), Pin("p2", "b", 44.1, 14.1, null))
        val json = ChartGeoJson.pins(pins, selectedId = "p2")
        assertTrue(json.contains("\"id\":\"p1\",\"name\":\"a\",\"selected\":false"))
        assertTrue(json.contains("\"id\":\"p2\",\"name\":\"b\",\"selected\":true"))
    }

    @Test
    fun theInfoMarkIsOnePointOrNothing() {
        assertTrue(ChartGeoJson.mark(null).contains("\"features\":[]"))
        assertTrue(ChartGeoJson.mark(Waypoint(44.3, 14.9)).contains("\"coordinates\":[14.9000000,44.3000000]"))
    }

    @Test
    fun anUnknownPositionDrawsNoBoat() {
        assertTrue(ChartGeoJson.boat(null).contains("\"features\":[]"))
    }

    @Test
    fun theBoatIsLongitudeFirstAndOnlyCarriesAReportedHeading() {
        val withHeading = ChartGeoJson.boat(BoatPosition(44.2, 14.8, 87.25f, PositionSource.MOTOR, stale = false))
        assertTrue(withHeading.contains("[14.8000000,44.2000000]"))
        assertTrue(withHeading.contains("\"heading\":87.3") || withHeading.contains("\"heading\":87.2"))
        assertTrue(withHeading.contains("\"source\":\"motor\""))

        val withoutHeading = ChartGeoJson.boat(BoatPosition(44.2, 14.8, null, PositionSource.PHONE, stale = true))
        assertFalse(withoutHeading.contains("heading"))
        assertTrue(withoutHeading.contains("\"stale\":true"))
        assertTrue(withoutHeading.contains("\"source\":\"phone\""))
    }

    @Test
    fun everySourceHasAnArrowImage() {
        val json = ChartStyle.json("/charts/adriatic.pmtiles", night = false)
        assertTrue(json.contains("[\"concat\", \"boat-arrow-\", [\"get\", \"source\"]]"))
        assertEquals("boat-arrow-motor", ChartStyle.boatImage(PositionSource.MOTOR))
        assertEquals("boat-arrow-phone", ChartStyle.boatImage(PositionSource.PHONE))
    }

    @Test
    fun theRouteIsNumberedFromOne() {
        val route = listOf(Waypoint(44.0, 14.0), Waypoint(44.1, 14.1))
        val points = ChartGeoJson.routePoints(route)
        assertTrue(points.contains("\"number\":1"))
        assertTrue(points.contains("\"number\":2"))
        assertTrue(ChartGeoJson.routeLine(route).contains("[14.0000000,44.0000000],[14.1000000,44.1000000]"))
    }

    @Test
    fun everyWaypointSaysWhetherItIsSelected() {
        val points = ChartGeoJson.routePoints(listOf(Waypoint(44.0, 14.0), Waypoint(44.1, 14.1)), selectedIndex = 1)
        assertTrue(points.contains("\"index\":0,\"number\":1,\"selected\":false"))
        assertTrue(points.contains("\"index\":1,\"number\":2,\"selected\":true"))
    }

    @Test
    fun otherRoutesHaveALineAndAPointPerWaypoint() {
        val json = ChartGeoJson.otherRoutes(
            listOf(
                SavedRoute("a", "Rt \"Kremik\"", listOf(Waypoint(44.0, 14.0), Waypoint(44.1, 14.1)), 0),
                SavedRoute("b", "One", listOf(Waypoint(44.2, 14.2)), 0),
                SavedRoute("c", "Empty", emptyList(), 0),
            ),
        )
        assertTrue(json.contains("\"id\":\"a\",\"name\":\"Rt \\\"Kremik\\\"\""))
        assertEquals(1, Regex("\"LineString\"").findAll(json).count())
        // Two points for route a, one for route b.
        assertEquals(3, Regex("\"Point\"").findAll(json).count())
        assertTrue(json.contains("\"id\":\"b\""))
        assertTrue(json.contains("\"type\":\"Point\",\"coordinates\":[14.2000000,44.2000000]"))
        assertFalse(json.contains("\"id\":\"c\""))
    }

    @Test
    fun pinsCarryTheirIdAndName() {
        val json = ChartGeoJson.pins(listOf(Pin("p1", "fish", 44.067495, 14.859677, null)))
        assertTrue(json.contains("\"id\":\"p1\",\"name\":\"fish\""))
        assertTrue(json.contains("[14.8596770,44.0674950]"))
        val style = ChartStyle.json("/charts/adriatic.pmtiles", night = false)
        // Above the seamarks, below the routes.
        val seamarks = style.indexOf("\"id\": \"${ChartStyle.LAYER_SEAMARK_ICON}\"")
        val pins = style.indexOf("\"id\": \"${ChartStyle.LAYER_PIN}\"")
        val route = style.indexOf("\"id\": \"route-line\"")
        assertTrue(seamarks in 0 until pins && pins < route)
    }

    @Test
    fun aSingleWaypointDrawsNoLine() {
        assertTrue(ChartGeoJson.routeLine(listOf(Waypoint(44.0, 14.0))).contains("\"features\":[]"))
    }
}

class RouteHitTest {

    private val zigzag = listOf(0f to 0f, 100f to 0f, 100f to 100f)

    @Test
    fun aTapNearASegmentLandsOnIt() {
        val hit = RouteHit.segment(zigzag, 40f, 8f, reach = 20f)!!
        assertEquals(0, hit.segment)
        assertEquals(40f, hit.x, 0.01f)
        assertEquals(0f, hit.y, 0.01f)
        assertEquals(1, RouteHit.segment(zigzag, 92f, 60f, reach = 20f)!!.segment)
    }

    @Test
    fun theClosestSegmentWinsAndFarTapsMiss() {
        // Near the corner, 3 px from the second segment and 5 px from the first.
        assertEquals(1, RouteHit.segment(zigzag, 97f, 5f, reach = 20f)!!.segment)
        assertNull(RouteHit.segment(zigzag, 50f, 50f, reach = 20f))
        assertNull(RouteHit.segment(listOf(0f to 0f), 0f, 0f, reach = 20f))
    }
}

class BoatPositionTest {

    private val phone = PhoneFix(44.3, 14.9, bearingDegrees = 120f)

    private fun choose(motorFresh: Boolean, phone: PhoneFix?, phoneFresh: Boolean, motor: Boolean = true) =
        BoatPosition.choose(
            motorLatitude = if (motor) 44.2 else null,
            motorLongitude = if (motor) 14.8 else null,
            motorHeading = 90f,
            motorFresh = motorFresh,
            phone = phone,
            phoneFresh = phoneFresh,
        )

    @Test
    fun aFreshMotorFixWinsOverThePhone() {
        val position = choose(motorFresh = true, phone = phone, phoneFresh = true)!!
        assertEquals(PositionSource.MOTOR, position.source)
        assertEquals(90f, position.headingDegrees)
        assertFalse(position.stale)
    }

    @Test
    fun thePhoneFillsInWhenTheMotorHasNoFix() {
        val position = choose(motorFresh = true, phone = phone, phoneFresh = true, motor = false)!!
        assertEquals(PositionSource.PHONE, position.source)
        assertEquals(44.3, position.latitude, 0.0)
        assertEquals(120f, position.headingDegrees)
    }

    @Test
    fun thePhoneFillsInWhenTheMotorFixIsStale() {
        assertEquals(PositionSource.PHONE, choose(motorFresh = false, phone = phone, phoneFresh = true)!!.source)
    }

    @Test
    fun withoutAnyFreshFixTheLastMotorFixIsShownStale() {
        val position = choose(motorFresh = false, phone = phone, phoneFresh = false)!!
        assertEquals(PositionSource.MOTOR, position.source)
        assertTrue(position.stale)
        val phoneOnly = choose(motorFresh = false, phone = phone, phoneFresh = false, motor = false)!!
        assertEquals(PositionSource.PHONE, phoneOnly.source)
        assertTrue(phoneOnly.stale)
    }

    @Test
    fun noFixAtAllDrawsNothing() {
        assertNull(choose(motorFresh = true, phone = null, phoneFresh = false, motor = false))
    }
}
