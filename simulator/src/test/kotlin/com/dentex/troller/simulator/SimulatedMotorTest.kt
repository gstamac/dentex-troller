package com.dentex.troller.simulator

import com.dentex.troller.mavlink.Attitude
import com.dentex.troller.mavlink.CommandLong
import com.dentex.troller.mavlink.GlobalPositionInt
import com.dentex.troller.mavlink.MissionAck
import com.dentex.troller.mavlink.MissionCount
import com.dentex.troller.mavlink.MissionItem
import com.dentex.troller.mavlink.MissionRequest
import com.dentex.troller.mavlink.RawImu
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SimulatedMotorTest {

    private val start = SimulatedMotor.START_LATITUDE to SimulatedMotor.START_LONGITUDE
    // About 200 m north-east of the start.
    private val target = (start.first + 0.0013) to (start.second + 0.0018)

    private fun SimulatedMotor.upload(vararg points: Pair<Double, Double>) {
        assertEquals(listOf(MissionRequest(sequence = 0)), receive(MissionCount(count = points.size)))
        points.forEachIndexed { index, (lat, lon) ->
            val reply = receive(MissionItem(x = lat.toFloat(), y = lon.toFloat(), sequence = index))
            if (index < points.lastIndex) assertEquals(listOf(MissionRequest(sequence = index + 1)), reply)
            else assertEquals(MissionAck.MAV_MISSION_ACCEPTED, (reply.single() as MissionAck).type)
        }
    }

    private fun SimulatedMotor.command(value: Int) = receive(CommandLong(command = value))

    private fun SimulatedMotor.run(seconds: Int) = repeat(seconds * 10) { advance(0.1) }

    private fun SimulatedMotor.distanceTo(point: Pair<Double, Double>) =
        SimulatedMotor.distanceMeters(latitude, longitude, point.first, point.second)

    @Test
    fun aRouteIsUploadedWithTheHandshakeTheAppExpects() {
        val motor = SimulatedMotor()
        motor.upload(target, start)
        assertEquals(2, motor.route.size)
    }

    @Test
    fun navigationAtSpeedZeroOnlyTurnsTowardsTheTarget() {
        val motor = SimulatedMotor()
        motor.upload(target)
        motor.command(15)
        motor.run(20)

        assertEquals(SimulatedMotor.Mode.NAVIGATION, motor.mode)
        assertTrue(motor.propellerOn)
        val bearing = SimulatedMotor.bearingDegrees(start.first, start.second, target.first, target.second)
        assertEquals(bearing, motor.headingDegrees, 1.0)
        assertTrue("it must not move at speed 0", motor.distanceTo(start) < 0.5)
    }

    @Test
    fun withASpeedStepItReachesTheTargetAndAnchors() {
        val motor = SimulatedMotor()
        motor.upload(target)
        motor.command(15)
        motor.command(8)
        motor.run(20 * 60)

        assertEquals(SimulatedMotor.Mode.ANCHOR, motor.mode)
        assertTrue("ended ${motor.distanceTo(target)} m away", motor.distanceTo(target) < 15)
    }

    @Test
    fun arrivalCanAlsoSwitchToManualWithThePropellerRunning() {
        val motor = SimulatedMotor()
        motor.settings = motor.settings.copy(arrival = SimulatedMotor.Arrival.MANUAL)
        motor.upload(target)
        motor.command(15)
        motor.command(8)
        motor.run(20 * 60)

        assertEquals(SimulatedMotor.Mode.MANUAL, motor.mode)
        assertTrue(motor.propellerOn)
    }

    @Test
    fun manualKeepsThePropellerRunningAndOnlyStopStopsIt() {
        val motor = SimulatedMotor()
        motor.upload(target)
        motor.command(15)
        motor.command(8)
        motor.command(13)
        assertTrue(motor.propellerOn)

        motor.command(11)
        assertTrue(!motor.propellerOn)
        assertEquals("the speed step is kept for the next start", 1, motor.speedStep)
        motor.command(15)
        assertTrue(motor.propellerOn)
    }

    @Test
    fun anchorHoldsTheSpotAgainstACurrent() {
        val motor = SimulatedMotor()
        motor.settings = motor.settings.copy(currentKnots = 0.5, currentTowardDegrees = 90.0)
        motor.command(12)
        motor.run(5 * 60)

        assertEquals(SimulatedMotor.Mode.ANCHOR, motor.mode)
        assertTrue("drifted ${motor.distanceTo(start)} m", motor.distanceTo(start) < 10)
    }

    @Test
    fun withoutAFixNavigationIsRefusedAndThePositionIsZero() {
        val motor = SimulatedMotor()
        motor.settings = motor.settings.copy(gpsFix = false)
        motor.upload(target)
        motor.command(15)

        assertEquals(SimulatedMotor.Mode.MANUAL, motor.mode)
        val telemetry = motor.telemetry(0)
        assertEquals(SimulatedMotor.NO_FIX_SIGNAL, telemetry.filterIsInstance<RawImu>().single().ygyro)
        val position = telemetry.filterIsInstance<GlobalPositionInt>().single()
        assertEquals(0, position.latitude)
        assertEquals(0, position.longitude)
    }

    @Test
    fun telemetryUsesTheFieldsTheAppReads() {
        val motor = SimulatedMotor()
        motor.upload(target)
        motor.command(15)
        val telemetry = motor.telemetry(0)

        assertEquals(2f, telemetry.filterIsInstance<Attitude>().single().pitchSpeed)
        assertEquals(RawImu.MOTOR_ON_MARKER, telemetry.filterIsInstance<RawImu>().single().xgyro)
        val position = telemetry.filterIsInstance<GlobalPositionInt>().single()
        assertEquals(start.first, position.latitude / 1e7, 1e-6)
    }

    @Test
    fun aFailureSettingMakesTheMotorIgnoreThatStep() {
        val motor = SimulatedMotor()
        motor.settings = motor.settings.copy(failure = SimulatedMotor.Failure.UPLOAD)
        assertTrue(motor.receive(MissionCount(count = 1)).isEmpty())
    }
}
