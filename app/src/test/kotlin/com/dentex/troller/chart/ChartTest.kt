package com.dentex.troller.chart

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
    fun anUnknownPositionDrawsNoBoat() {
        assertTrue(ChartGeoJson.boat(null, 14.8, 90f, stale = false).contains("\"features\":[]"))
    }

    @Test
    fun theBoatIsLongitudeFirstAndOnlyCarriesAReportedHeading() {
        val withHeading = ChartGeoJson.boat(44.2, 14.8, 87.25f, stale = false)
        assertTrue(withHeading.contains("[14.8000000,44.2000000]"))
        assertTrue(withHeading.contains("\"heading\":87.3") || withHeading.contains("\"heading\":87.2"))

        val withoutHeading = ChartGeoJson.boat(44.2, 14.8, null, stale = true)
        assertFalse(withoutHeading.contains("heading"))
        assertTrue(withoutHeading.contains("\"stale\":true"))
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
    fun aSingleWaypointDrawsNoLine() {
        assertTrue(ChartGeoJson.routeLine(listOf(Waypoint(44.0, 14.0))).contains("\"features\":[]"))
    }
}
