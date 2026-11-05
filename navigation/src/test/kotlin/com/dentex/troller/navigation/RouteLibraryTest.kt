package com.dentex.troller.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteLibraryTest {

    private val a = Waypoint(44.2167, 14.7934)
    private val b = Waypoint(44.2200, 14.8000)

    @Test
    fun aNewRouteIsNumberedAndSelected() {
        val library = RouteLibrary().create("r1", 1_000).create("r2", 2_000)
        assertEquals(listOf("Route 1", "Route 2"), library.routes.map { it.name })
        assertEquals("r2", library.selectedId)
    }

    @Test
    fun numberingContinuesAfterTheHighestNumber() {
        val library = RouteLibrary().create("r1", 1).create("r2", 2).delete("r1").rename("r2", "Kremik")
            .create("r3", 3, name = "Route 7")
        assertEquals("Route 8", library.nextName())
    }

    @Test
    fun onlyTheSelectedRouteIsEdited() {
        val library = RouteLibrary().create("r1", 1).editSelected { it + a }
            .create("r2", 2).editSelected { it + b + a }
        assertEquals(listOf(a), library.routes[0].waypoints)
        assertEquals(listOf(b, a), library.routes[1].waypoints)
        assertEquals(library, library.select(null).editSelected { emptyList() }.select("r2"))
    }

    @Test
    fun deletingTheSelectedRouteClearsTheSelection() {
        val library = RouteLibrary().create("r1", 1).create("r2", 2).delete("r2")
        assertNull(library.selected)
        assertEquals("r1", library.select("r1").delete("r2").selectedId)
    }

    @Test
    fun selectingAnUnknownRouteSelectsNothing() {
        assertNull(RouteLibrary().create("r1", 1).select("gone").selectedId)
    }

    @Test
    fun namesStayOnOneLineAndBlankNamesAreIgnored() {
        val library = RouteLibrary().create("r1", 1).rename("r1", "  Rt\nKremik  ").rename("r1", "   ")
        assertEquals("Rt Kremik", library.routes[0].name)
    }

    @Test
    fun theLibrarySurvivesTheRoundTrip() {
        val library = RouteLibrary().create("r1", 1_700_000_000_000).editSelected { it + a + b }
            .rename("r1", "Kremik – Zečevo").create("r2", 1_700_000_100_000).select("r1")
        assertEquals(library, RouteLibrary.decode(library.encode()))
    }

    @Test
    fun unknownLinesAreSkippedAndOtherFilesRejected() {
        val text = RouteLibrary().create("r1", 5).encode() + "colour red\n"
        assertEquals("r1", RouteLibrary.decode(text).routes.single().id)
        val error = runCatching { RouteLibrary.decode("hello") }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun theLengthFollowsTheWaypoints() {
        // About 640 m between a and b.
        val route = SavedRoute("r", "r", listOf(a, b, a), 0)
        assertEquals(2 * distanceMeters(a, b), route.lengthMeters, 1e-6)
        assertEquals(640.0, distanceMeters(a, b), 15.0)
    }

    @Test
    fun theNearestPointCanLieBetweenTwoWaypoints() {
        val route = listOf(Waypoint(44.0, 14.0), Waypoint(44.0, 14.2), Waypoint(44.2, 14.2))
        val nearest = nearestPointOnRoute(route, Waypoint(44.05, 14.1))!!
        assertEquals(44.0, nearest.latitude, 1e-9)
        assertEquals(14.1, nearest.longitude, 1e-9)

        val pastTheEnd = nearestPointOnRoute(route, Waypoint(44.3, 14.25))!!
        assertEquals(Waypoint(44.2, 14.2), pastTheEnd)
        assertEquals(null, nearestPointOnRoute(emptyList(), Waypoint(44.0, 14.0)))
    }
}
