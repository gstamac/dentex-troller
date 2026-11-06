package com.dentex.troller.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResumeAfterAnchorTest {

    private val route = listOf(Waypoint(44.0, 14.0), Waypoint(44.0, 14.01), Waypoint(44.01, 14.01))
    private val underWay = GoToState(GoToState.Phase.UNDER_WAY, route.first(), waypoints = route, name = "Bay", skipped = 2)
    private val anchoring = underWay.copy(phase = GoToState.Phase.ENDED)

    @Test
    fun anchoringDuringAGoToKeepsTheRestFromTheReportedWaypoint() {
        val resume = ResumeAfterAnchor()
        resume.update(navigating = true, anchored = false, goTo = underWay, targetIndex = 1)
        // Anchor here ends the go-to while the motor still reports navigation.
        assertNull(resume.update(navigating = true, anchored = false, goTo = anchoring, targetIndex = 1))

        val rest = resume.update(navigating = false, anchored = true, goTo = anchoring, targetIndex = 1)

        assertEquals(ResumeAfterAnchor.Rest(route.drop(1), "Bay", 3), rest)
    }

    @Test
    fun aLostLinkKeepsItAndManualForgetsIt() {
        val resume = ResumeAfterAnchor()
        resume.update(navigating = true, anchored = false, goTo = underWay, targetIndex = 0)
        resume.update(navigating = false, anchored = true, goTo = anchoring, targetIndex = 0)

        assertEquals(route, resume.update(navigating = null, anchored = false, goTo = anchoring, targetIndex = null)?.waypoints)
        assertNull(resume.update(navigating = false, anchored = false, goTo = anchoring, targetIndex = 0))
        assertNull(resume.update(navigating = false, anchored = true, goTo = anchoring, targetIndex = 0))
    }

    @Test
    fun navigatingAgainForgetsIt() {
        val resume = ResumeAfterAnchor()
        resume.update(navigating = true, anchored = false, goTo = underWay, targetIndex = 2)
        resume.update(navigating = false, anchored = true, goTo = anchoring, targetIndex = 2)

        assertNull(resume.update(navigating = true, anchored = false, goTo = anchoring, targetIndex = 2))
    }

    @Test
    fun navigationTheAppDidNotStartLeavesNothingToContinue() {
        val resume = ResumeAfterAnchor()
        val idle = GoToState(GoToState.Phase.IDLE)
        resume.update(navigating = true, anchored = false, goTo = idle, targetIndex = 1)

        assertNull(resume.update(navigating = false, anchored = true, goTo = idle, targetIndex = 1))
    }

    @Test
    fun anOldGoToDoesNotCountForNavigationFromTheRemote() {
        val resume = ResumeAfterAnchor()
        resume.update(navigating = true, anchored = false, goTo = underWay, targetIndex = 0)
        resume.update(navigating = false, anchored = false, goTo = anchoring, targetIndex = 0)
        // The go-to ended long ago; the remote starts navigation and then anchors.
        resume.update(navigating = true, anchored = false, goTo = anchoring, targetIndex = 0)

        assertNull(resume.update(navigating = false, anchored = true, goTo = anchoring, targetIndex = 0))
    }
}
