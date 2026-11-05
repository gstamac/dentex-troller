package com.dentex.troller.navigation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GoToTest {

    private val target = Waypoint(44.2167, 14.7934)

    /** A fake motor: records what was sent and lets the test drive the upload and the mode report. */
    private class Motor {
        val progress = MutableStateFlow(MissionProgress(MissionProgress.Phase.IDLE))
        val navigating = MutableStateFlow<Boolean?>(false)
        val speed = MutableStateFlow<Int?>(2)
        var anchored = false
        val uploaded = mutableListOf<Route>()
        val sent = mutableListOf<String>()
        var uploadCancelled = 0
    }

    private fun TestScope.newGoTo(motor: Motor) = GoToTarget(
        scope = backgroundScope,
        startUpload = { route ->
            motor.uploaded += route
            motor.progress.value = MissionProgress(MissionProgress.Phase.UPLOADING, total = route.size)
            true
        },
        uploadProgress = motor.progress,
        cancelUpload = {
            motor.uploadCancelled++
            motor.progress.value = MissionProgress(MissionProgress.Phase.IDLE)
        },
        sendNavigation = { motor.sent += "navigation"; true },
        sendManual = { motor.sent += "manual"; true },
        sendStop = { motor.sent += "stop"; true },
        sendAnchor = { motor.sent += "anchor"; true },
        sendSpeedUp = { motor.sent += "speed up"; true },
        navigating = motor.navigating,
        motorSpeed = motor.speed,
        anchored = { motor.anchored },
    )

    private fun Motor.acknowledge() {
        progress.value = MissionProgress(MissionProgress.Phase.COMPLETE, total = 1, completed = 1)
    }

    @Test
    fun uploadsTheTargetThenSendsNavigationOnceAfterTheAcknowledgement() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)

        assertNull(goTo.start(target))
        runCurrent()
        assertEquals(listOf(Route(listOf(target))), motor.uploaded)
        assertEquals(GoToState.Phase.UPLOADING, goTo.state.value.phase)
        assertTrue(motor.sent.isEmpty())

        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS - 1)
        runCurrent()
        assertTrue("navigation waits a second after the acknowledgement", motor.sent.isEmpty())

        advanceTimeBy(2)
        runCurrent()
        assertEquals(listOf("navigation"), motor.sent)
        assertEquals(GoToState.Phase.STARTING, goTo.state.value.phase)

        motor.navigating.value = true
        runCurrent()
        assertEquals(GoToState.Phase.UNDER_WAY, goTo.state.value.phase)
        assertEquals(target, goTo.state.value.target)
    }

    @Test
    fun aFollowedRouteUploadsEveryWaypointAndKeepsItsName() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        val route = listOf(target, Waypoint(44.22, 14.80), Waypoint(44.23, 14.81))

        assertNull(goTo.start(route, "Evening run"))
        runCurrent()
        assertEquals(listOf(Route(route)), motor.uploaded)
        assertEquals("Evening run", goTo.state.value.name)
        assertEquals(route[1], goTo.state.value.heading(1))
        assertEquals(target, goTo.state.value.heading(null))

        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS + 1)
        runCurrent()
        motor.navigating.value = true
        runCurrent()
        assertEquals(GoToState.Phase.UNDER_WAY, goTo.state.value.phase)
        assertEquals(route, goTo.state.value.waypoints)
    }

    @Test
    fun anEmptyRouteIsRefused() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        assertNotNull(goTo.start(emptyList(), "Empty"))
        assertTrue(motor.uploaded.isEmpty())
    }

    @Test
    fun failedUploadNeverSendsNavigation() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()

        motor.progress.value = MissionProgress(MissionProgress.Phase.FAILED, message = "The motor did not answer the route upload")
        runCurrent()
        advanceTimeBy(60_000)
        runCurrent()

        assertTrue(motor.sent.isEmpty())
        assertEquals(GoToState.Phase.FAILED, goTo.state.value.phase)
        assertEquals("The motor did not answer the route upload", goTo.state.value.message)
    }

    @Test
    fun unconfirmedNavigationFailsWithoutSendingItAgain() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()
        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS + GoToTarget.CONFIRM_TIMEOUT_MILLIS + 1_000)
        runCurrent()

        assertEquals(listOf("navigation"), motor.sent)
        assertEquals(GoToState.Phase.FAILED, goTo.state.value.phase)
    }

    @Test
    fun refusesToStartWhileTheMotorIsAlreadyNavigating() = runTest {
        val motor = Motor()
        motor.navigating.value = true
        val goTo = newGoTo(motor)

        assertNotNull(goTo.start(target))
        runCurrent()
        assertTrue(motor.uploaded.isEmpty())
        assertEquals(GoToState.Phase.IDLE, goTo.state.value.phase)
    }

    @Test
    fun refusesASecondGoToWhileOneIsRunning() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()

        assertNotNull(goTo.start(Waypoint(44.3, 14.8)))
        assertEquals(1, motor.uploaded.size)
    }

    @Test
    fun cancellingDuringTheUploadStopsItAndSendsNothing() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()

        goTo.cancel()
        runCurrent()
        motor.acknowledge()
        advanceTimeBy(10_000)
        runCurrent()

        assertEquals(1, motor.uploadCancelled)
        assertTrue(motor.sent.isEmpty())
        assertEquals(GoToState.Phase.ENDED, goTo.state.value.phase)
    }

    /** Runs a go-to until the motor reports navigation mode. */
    private fun TestScope.underWay(motor: Motor): GoToTarget {
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()
        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS + 1)
        runCurrent()
        motor.navigating.value = true
        runCurrent()
        return goTo
    }

    @Test
    fun atSpeedZeroOneSpeedStepIsSentOnceNavigationIsConfirmed() = runTest {
        val motor = Motor()
        motor.speed.value = 0
        val goTo = underWay(motor)

        assertEquals(listOf("navigation", "speed up"), motor.sent)
        assertEquals(GoToState.Phase.UNDER_WAY, goTo.state.value.phase)
    }

    @Test
    fun aRunningSpeedIsLeftAlone() = runTest {
        val motor = Motor()
        motor.speed.value = 3
        underWay(motor)

        assertEquals(listOf("navigation"), motor.sent)
    }

    @Test
    fun cancellingUnderWaySendsManualThenStop() = runTest {
        val motor = Motor()
        val goTo = underWay(motor)

        goTo.cancel()
        runCurrent()
        assertEquals(listOf("navigation", "manual"), motor.sent)
        advanceTimeBy(GoToTarget.STOP_AFTER_MANUAL_MILLIS + 1)
        runCurrent()

        assertEquals(listOf("navigation", "manual", "stop"), motor.sent)
        assertEquals(GoToState.Phase.ENDED, goTo.state.value.phase)
    }

    @Test
    fun leavingNavigationByItselfStopsTheMotor() = runTest {
        val motor = Motor()
        val goTo = underWay(motor)

        motor.navigating.value = false
        runCurrent()

        assertEquals(listOf("navigation", "stop"), motor.sent)
        assertEquals(GoToState.Phase.ENDED, goTo.state.value.phase)
    }

    @Test
    fun anchorSwitchesFromNavigationWithoutStopping() = runTest {
        val motor = Motor()
        val goTo = underWay(motor)

        goTo.anchor()
        runCurrent()
        motor.anchored = true
        motor.navigating.value = false
        runCurrent()

        assertEquals(listOf("navigation", "anchor"), motor.sent)
        assertEquals(GoToState.Phase.ENDED, goTo.state.value.phase)
    }

    @Test
    fun anchorThatIsNotReportedFailsWithoutResending() = runTest {
        val motor = Motor()
        val goTo = underWay(motor)

        goTo.anchor()
        advanceTimeBy(GoToTarget.CONFIRM_TIMEOUT_MILLIS + 1_000)
        runCurrent()

        assertEquals(listOf("navigation", "anchor"), motor.sent)
        assertEquals(GoToState.Phase.FAILED, goTo.state.value.phase)
    }

    @Test
    fun anchorDoesNothingBeforeNavigationWasSent() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()

        goTo.anchor()
        runCurrent()

        assertTrue(motor.sent.isEmpty())
        assertEquals(GoToState.Phase.UPLOADING, goTo.state.value.phase)
    }

    @Test
    fun anchoringOnArrivalIsLeftAlone() = runTest {
        val motor = Motor()
        val goTo = underWay(motor)

        motor.anchored = true
        motor.navigating.value = false
        runCurrent()

        assertEquals(listOf("navigation"), motor.sent)
        assertEquals(GoToState.Phase.ENDED, goTo.state.value.phase)
    }

    @Test
    fun cancellingUnderWaySendsManualOnce() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()
        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS + 1)
        runCurrent()
        motor.navigating.value = true
        runCurrent()

        goTo.cancel()
        advanceTimeBy(GoToTarget.STOP_AFTER_MANUAL_MILLIS + 1)
        runCurrent()

        assertEquals(listOf("navigation", "manual", "stop"), motor.sent)
        assertEquals(GoToState.Phase.ENDED, goTo.state.value.phase)
    }

    @Test
    fun endsWhenTheMotorLeavesNavigationMode() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()
        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS + 1)
        runCurrent()
        motor.navigating.value = true
        runCurrent()

        motor.navigating.value = false
        runCurrent()

        assertEquals(GoToState.Phase.ENDED, goTo.state.value.phase)
        assertEquals(listOf("navigation", "stop"), motor.sent)
    }

    @Test
    fun aLostLinkDuringTheUploadFailsAndNothingIsSentAfterwards() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()

        goTo.onLinkLost()
        runCurrent()
        // The link comes back and the motor answers late: still nothing may be sent.
        motor.acknowledge()
        advanceTimeBy(10_000)
        runCurrent()

        assertEquals(GoToState.Phase.FAILED, goTo.state.value.phase)
        assertTrue(motor.sent.isEmpty())
    }

    @Test
    fun aLostLinkWhileStartingFailsWithoutResending() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()
        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS + 1)
        runCurrent()

        goTo.onLinkLost()
        advanceTimeBy(30_000)
        runCurrent()

        assertEquals(GoToState.Phase.FAILED, goTo.state.value.phase)
        assertEquals(listOf("navigation"), motor.sent)
    }

    @Test
    fun aLostLinkUnderWayKeepsTheGoToOnScreen() = runTest {
        val motor = Motor()
        val goTo = newGoTo(motor)
        goTo.start(target)
        runCurrent()
        motor.acknowledge()
        runCurrent()
        advanceTimeBy(GoToTarget.START_DELAY_MILLIS + 1)
        runCurrent()
        motor.navigating.value = true
        runCurrent()

        goTo.onLinkLost()
        runCurrent()

        assertEquals(GoToState.Phase.UNDER_WAY, goTo.state.value.phase)
        assertEquals(listOf("navigation"), motor.sent)
    }

    @Test
    fun bearingPointsTheRightWay() {
        val from = Waypoint(44.0, 14.0)
        assertEquals(0.0, bearingDegrees(from, Waypoint(44.1, 14.0)), 0.01)
        assertEquals(90.0, bearingDegrees(from, Waypoint(44.0, 14.1)), 0.1)
        assertEquals(180.0, bearingDegrees(from, Waypoint(43.9, 14.0)), 0.01)
        assertEquals(270.0, bearingDegrees(from, Waypoint(44.0, 13.9)), 0.1)
    }
}
