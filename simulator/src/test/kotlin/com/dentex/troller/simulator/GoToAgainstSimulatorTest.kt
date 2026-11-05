package com.dentex.troller.simulator

import com.dentex.troller.haswing.HaswingMotorController
import com.dentex.troller.haswing.MotorMode
import com.dentex.troller.haswing.MotorTransport
import com.dentex.troller.haswing.ReportedMode
import com.dentex.troller.haswing.CommandResult
import com.dentex.troller.mavlink.HelmsmanMavlinkCodec
import com.dentex.troller.mavlink.MavlinkMessage
import com.dentex.troller.navigation.GoToState
import com.dentex.troller.navigation.GoToTarget
import com.dentex.troller.navigation.MissionTransfer
import com.dentex.troller.navigation.Waypoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The app's own motor controller, route transfer and go-to, talking MAVLink bytes to the
 * simulator, as the sim build wires them. Runs on virtual time.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GoToAgainstSimulatorTest {

    private class Rig(scope: TestScope) {
        val decoder = HelmsmanMavlinkCodec()
        lateinit var simulator: MotorSimulator
        val controller = HaswingMotorController(
            transport = MotorTransport { bytes -> simulator.receive(bytes) },
            scope = scope.backgroundScope,
            clock = { scope.currentTime },
            autoHeartbeat = false,
        )
        val mission = MissionTransfer(
            scope = scope.backgroundScope,
            sendMessage = { controller.send(it) == CommandResult.Sent },
            requestRouteList = { true },
        )
        val goTo: GoToTarget

        init {
            simulator = MotorSimulator(
                scope = scope.backgroundScope,
                send = { bytes ->
                    decoder.consume(bytes).forEach { frame ->
                        val message = MavlinkMessage.decode(frame) ?: return@forEach
                        controller.onMessage(message, frame.systemId, frame.componentId)
                        mission.onMessage(message)
                    }
                },
                clock = { scope.currentTime },
            )
            fun sent(result: CommandResult) = result == CommandResult.Sent
            goTo = GoToTarget(
                scope = scope.backgroundScope,
                startUpload = { mission.startUpload(it) },
                uploadProgress = mission.progress,
                cancelUpload = { mission.cancel() },
                sendNavigation = { sent(controller.requestMode(MotorMode.NAVIGATION)) },
                sendManual = { sent(controller.requestMode(MotorMode.MANUAL)) },
                sendStop = { sent(controller.requestStop()) },
                sendAnchor = { sent(controller.requestMode(MotorMode.ANCHOR)) },
                sendSpeedUp = { sent(controller.requestSpeedUp()) },
                navigating = controller.telemetry
                    .map { t -> t.reportedMode?.let { it == ReportedMode.NAVIGATION } }
                    .stateIn(scope.backgroundScope, SharingStarted.Eagerly, null),
                motorSpeed = controller.telemetry.map { it.motorSpeed }.stateIn(scope.backgroundScope, SharingStarted.Eagerly, null),
                anchored = { controller.telemetry.value.reportedMode == ReportedMode.ANCHOR },
            )
        }
    }

    private val target = Waypoint(SimulatedMotor.START_LATITUDE + 0.0013, SimulatedMotor.START_LONGITUDE + 0.0018)

    @Test
    fun theBoatIsMovedToTheTargetAndAnchorsThere() = runTest {
        val rig = Rig(this)
        rig.simulator.start()
        advanceTimeBy(3_000)
        runCurrent()

        assertEquals(null, rig.goTo.start(target))
        advanceTimeBy(5_000)
        runCurrent()
        assertEquals(GoToState.Phase.UNDER_WAY, rig.goTo.state.value.phase)
        assertEquals("speed 0 was raised to 1", 1, rig.simulator.motor.speedStep)

        advanceTimeBy(20 * 60_000)
        runCurrent()
        assertEquals(GoToState.Phase.ENDED, rig.goTo.state.value.phase)
        assertEquals(SimulatedMotor.Mode.ANCHOR, rig.simulator.motor.mode)
        val left = SimulatedMotor.distanceMeters(
            rig.simulator.motor.latitude, rig.simulator.motor.longitude, target.latitude, target.longitude,
        )
        assertTrue("stopped $left m from the target", left < 15)
    }

    @Test
    fun aRouteIsFollowedThroughEveryWaypoint() = runTest {
        val rig = Rig(this)
        rig.simulator.start()
        advanceTimeBy(3_000)
        runCurrent()
        val start = SimulatedMotor.START_LATITUDE to SimulatedMotor.START_LONGITUDE
        val route = listOf(
            Waypoint(start.first + 0.0009, start.second),
            Waypoint(start.first + 0.0009, start.second + 0.0012),
            Waypoint(start.first, start.second + 0.0012),
        )

        assertEquals(null, rig.goTo.start(route, "Square"))
        advanceTimeBy(5_000)
        runCurrent()
        assertEquals(GoToState.Phase.UNDER_WAY, rig.goTo.state.value.phase)
        assertEquals(3, rig.simulator.motor.route.size)

        advanceTimeBy(30 * 60_000)
        runCurrent()
        assertEquals(2, rig.controller.telemetry.value.targetPoint)
        assertEquals(SimulatedMotor.Mode.ANCHOR, rig.simulator.motor.mode)
        val left = SimulatedMotor.distanceMeters(
            rig.simulator.motor.latitude, rig.simulator.motor.longitude, route.last().latitude, route.last().longitude,
        )
        assertTrue("stopped $left m from the last waypoint", left < 15)
    }

    @Test
    fun aBoatAlreadyOnTheRouteJoinsItInsteadOfGoingBackToTheStart() = runTest {
        val rig = Rig(this)
        val start = SimulatedMotor.START_LATITUDE to SimulatedMotor.START_LONGITUDE
        // The boat sits halfway along the first section.
        val route = listOf(
            Waypoint(start.first, start.second - 0.0012),
            Waypoint(start.first, start.second + 0.0012),
            Waypoint(start.first + 0.0009, start.second + 0.0012),
        )
        val join = com.dentex.troller.navigation.joinIndex(route, Waypoint(start.first, start.second))
        assertEquals(1, join)

        rig.simulator.start()
        advanceTimeBy(3_000)
        assertEquals(null, rig.goTo.start(route.drop(join), "Line", skipped = join))
        advanceTimeBy(5_000)
        runCurrent()
        assertEquals(route.drop(1).map { it.latitude.toFloat().toDouble() }, rig.simulator.motor.route.map { it.first })
        assertEquals(1, rig.goTo.state.value.skipped)

        advanceTimeBy(30 * 60_000)
        runCurrent()
        val left = SimulatedMotor.distanceMeters(
            rig.simulator.motor.latitude, rig.simulator.motor.longitude, route.last().latitude, route.last().longitude,
        )
        assertTrue("stopped $left m from the last waypoint", left < 15)
    }

    @Test
    fun stopGoToLeavesTheMotorInManualWithThePropellerOff() = runTest {
        val rig = Rig(this)
        rig.simulator.start()
        advanceTimeBy(3_000)
        rig.goTo.start(target)
        advanceTimeBy(5_000)
        runCurrent()

        rig.goTo.cancel()
        advanceTimeBy(2_000)
        runCurrent()

        assertEquals(SimulatedMotor.Mode.MANUAL, rig.simulator.motor.mode)
        assertTrue(!rig.simulator.motor.propellerOn)
    }

    @Test
    fun aMotorWithoutAFixMakesTheGoToFailWithoutASecondCommand() = runTest {
        val rig = Rig(this)
        rig.simulator.control("gps", "nofix")
        rig.simulator.start()
        advanceTimeBy(3_000)
        rig.goTo.start(target)
        advanceTimeBy(20_000)
        runCurrent()

        assertEquals(GoToState.Phase.FAILED, rig.goTo.state.value.phase)
        assertEquals(SimulatedMotor.Mode.MANUAL, rig.simulator.motor.mode)
    }

    @Test
    fun silenceClosesTheCommandGate() = runTest {
        val rig = Rig(this)
        rig.simulator.start()
        advanceTimeBy(3_000)
        runCurrent()
        assertEquals(com.dentex.troller.haswing.CommandGateState.READY, rig.controller.commandGate.value)

        rig.simulator.silence(10.0)
        advanceTimeBy(7_000)
        runCurrent()
        assertEquals(com.dentex.troller.haswing.CommandGateState.LINK_LOST, rig.controller.commandGate.value)
    }
}
