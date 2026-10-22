package com.dentex.troller.navigation

import com.dentex.troller.mavlink.MavlinkMessage
import com.dentex.troller.mavlink.MissionAck
import com.dentex.troller.mavlink.MissionCount
import com.dentex.troller.mavlink.MissionItem
import com.dentex.troller.mavlink.MissionRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MissionTransferTest {

    /**
     * The transfer runs in the background scope so that the polling loops do not keep the test
     * alive. `runCurrent` then executes the coroutines that are ready without advancing the
     * virtual clock.
     */
    private fun TestScope.newTransfer(sent: MutableList<MavlinkMessage>): MissionTransfer =
        MissionTransfer(
            scope = backgroundScope,
            sendMessage = { sent += it },
            requestRouteList = { },
        )

    @Test
    fun uploadSendsTheWaypointCount() = runTest {
        val sent = mutableListOf<MavlinkMessage>()
        val transfer = newTransfer(sent)

        transfer.startUpload(Route(listOf(Waypoint(45.0, 13.0), Waypoint(45.1, 13.1))))
        runCurrent()

        val count = sent.filterIsInstance<MissionCount>().first()
        assertEquals(2, count.count)
    }

    @Test
    fun uploadAnswersTheMotorRequestWithTheWaypoint() = runTest {
        val sent = mutableListOf<MavlinkMessage>()
        val transfer = newTransfer(sent)
        transfer.startUpload(Route(listOf(Waypoint(45.0, 13.0), Waypoint(45.1, 13.1))))
        runCurrent()
        sent.clear()

        transfer.onMessage(MissionRequest(sequence = 1))
        runCurrent()

        val item = sent.filterIsInstance<MissionItem>().first()
        assertEquals(1, item.sequence)
        assertEquals(45.1f, item.x, 1e-4f)
        assertEquals(13.1f, item.y, 1e-4f)
        assertEquals(106, item.targetSystem)
    }

    @Test
    fun uploadRejectsAnEmptyRoute() = runTest {
        val transfer = newTransfer(mutableListOf())

        assertEquals(false, transfer.startUpload(Route.EMPTY))
        assertEquals(MissionProgress.Phase.FAILED, transfer.progress.value.phase)
    }

    @Test
    fun downloadCollectsTheWaypointsInOrder() = runTest {
        val sent = mutableListOf<MavlinkMessage>()
        val transfer = newTransfer(sent)

        transfer.startDownload()
        transfer.onMessage(MissionCount(count = 2))
        transfer.onMessage(MissionItem(sequence = 0, x = 45.0f, y = 13.0f))
        transfer.onMessage(MissionItem(sequence = 1, x = 45.1f, y = 13.1f))

        val route = transfer.downloadedRoute
        assertEquals(2, route?.size)
        assertEquals(45.0, route?.waypoints?.get(0)?.latitude ?: 0.0, 1e-6)
        assertEquals(13.1, route?.waypoints?.get(1)?.longitude ?: 0.0, 1e-6)
        assertEquals(MissionProgress.Phase.COMPLETE, transfer.progress.value.phase)
    }

    @Test
    fun downloadIgnoresOutOfOrderWaypoints() = runTest {
        val transfer = newTransfer(mutableListOf())

        transfer.startDownload()
        transfer.onMessage(MissionCount(count = 2))
        transfer.onMessage(MissionItem(sequence = 1, x = 45.1f, y = 13.1f))

        assertNull(transfer.downloadedRoute)
    }

    @Test
    fun downloadReportsAnEmptyRoute() = runTest {
        val transfer = newTransfer(mutableListOf())

        transfer.startDownload()
        transfer.onMessage(MissionCount(count = 0))

        assertEquals(MissionProgress.Phase.COMPLETE, transfer.progress.value.phase)
        assertTrue(transfer.progress.value.message!!.contains("no stored route"))
    }

    @Test
    fun uploadCompletesOnTheMissionAcknowledgement() = runTest {
        val transfer = newTransfer(mutableListOf())
        transfer.startUpload(Route(listOf(Waypoint(45.0, 13.0))))
        runCurrent()

        transfer.onMessage(MissionAck(type = MissionAck.MAV_MISSION_ACCEPTED))

        assertEquals(MissionProgress.Phase.COMPLETE, transfer.progress.value.phase)
    }

    @Test
    fun anUnansweredWaypointIsSentAgain() = runTest {
        val sent = mutableListOf<MavlinkMessage>()
        val transfer = newTransfer(sent)
        transfer.startUpload(Route(listOf(Waypoint(45.0, 13.0))))
        runCurrent()
        sent.clear()

        transfer.onMessage(MissionRequest(sequence = 0))
        runCurrent()
        assertEquals(1, sent.filterIsInstance<MissionItem>().size)

        // The motor did not ask for the next waypoint, so the answer is repeated.
        advanceTimeBy(MissionTransfer.ITEM_RETRY_DELAY_MILLIS + 1)
        runCurrent()

        assertEquals(2, sent.filterIsInstance<MissionItem>().size)
    }
}
