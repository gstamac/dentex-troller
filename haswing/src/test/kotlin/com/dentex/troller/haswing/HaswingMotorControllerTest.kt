package com.dentex.troller.haswing

import com.dentex.troller.mavlink.Attitude
import com.dentex.troller.mavlink.CommandLong
import com.dentex.troller.mavlink.GlobalPositionInt
import com.dentex.troller.mavlink.Heartbeat
import com.dentex.troller.mavlink.RawImu
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HaswingMotorControllerTest {

    private class RecordingTransport : MotorTransport {
        val sent = mutableListOf<ByteArray>()
        override suspend fun send(bytes: ByteArray) {
            sent += bytes
        }
    }

    @Test
    fun commandsAreRefusedBeforeTheProfileIsConfirmed() = runTest {
        val transport = RecordingTransport()
        val motor = HaswingMotorController(transport, this, clock = { 0L }, autoHeartbeat = false)

        assertEquals(CommandGateState.UNVERIFIED, motor.commandGate.value)
        val result = motor.requestMotorOn()

        assertTrue(result is CommandResult.Rejected)
        assertTrue(transport.sent.isEmpty())
    }

    @Test
    fun commandsAreAllowedAfterAType110Heartbeat() = runTest {
        val transport = RecordingTransport()
        val motor = HaswingMotorController(transport, this, clock = { 0L }, autoHeartbeat = false)

        motor.onMessage(Heartbeat(type = 110, customMode = 102))

        assertEquals(CommandGateState.READY, motor.commandGate.value)
        assertEquals(MotorProfile.TYPE_110, motor.profile)
        assertEquals(102, motor.identity.value?.firmwareVersion)

        assertTrue(motor.requestMotorOn() is CommandResult.Sent)
        assertEquals(1, transport.sent.size)
    }

    @Test
    fun stopIsAlwaysAllowed() = runTest {
        val transport = RecordingTransport()
        val motor = HaswingMotorController(transport, this, clock = { 0L }, autoHeartbeat = false)

        assertTrue(motor.requestStop() is CommandResult.Sent)
        assertEquals(1, transport.sent.size)
    }

    @Test
    fun anUnknownProfileKeepsTheGateClosed() = runTest {
        val transport = RecordingTransport()
        val motor = HaswingMotorController(transport, this, clock = { 0L }, autoHeartbeat = false)

        motor.onMessage(Heartbeat(type = 42))

        assertEquals(CommandGateState.UNVERIFIED, motor.commandGate.value)
        assertTrue(motor.requestSpeedUp() is CommandResult.Rejected)
    }

    @Test
    fun disconnectDropsTheIdentity() = runTest {
        val transport = RecordingTransport()
        val motor = HaswingMotorController(transport, this, clock = { 0L }, autoHeartbeat = false)

        motor.onMessage(Heartbeat(type = 110))
        motor.onDisconnected()

        assertNull(motor.identity.value)
        assertEquals(CommandGateState.UNVERIFIED, motor.commandGate.value)
        assertTrue(motor.requestMotorOn() is CommandResult.Rejected)
    }
}

class TelemetryDecoderTest {

    @Test
    fun rawImuCarriesTheMotorState() {
        val decoder = TelemetryDecoder()
        val telemetry = decoder.apply(
            RawImu(xgyro = RawImu.MOTOR_ON_MARKER, ygyro = 12, zacc = 7, zgyro = 3),
            MotorTelemetry(),
            nowMillis = 100L,
        )

        assertEquals(true, telemetry.motorOn)
        assertEquals(12, telemetry.gpsSignal)
        assertEquals(7, telemetry.targetPoint)
        assertEquals(3, telemetry.motorSpeed)
        assertEquals(100L, telemetry.lastUpdateMillis)
    }

    @Test
    fun attitudeCarriesHeadingBatteryAndModel() {
        val decoder = TelemetryDecoder()
        val telemetry = decoder.apply(
            Attitude(yaw = Math.toRadians(90.0).toFloat(), pitch = 87.6f, pitchSpeed = 3f),
            MotorTelemetry(),
            nowMillis = 50L,
        )

        assertEquals(90f, telemetry.headingDegrees!!, 0.01f)
        assertEquals(87, telemetry.batteryLevel)
        assertEquals(3, telemetry.boatModel)
    }

    @Test
    fun globalPositionCarriesCoordinatesAndSpeed() {
        val decoder = TelemetryDecoder()
        val telemetry = decoder.apply(
            GlobalPositionInt(latitude = 1130000000, longitude = 2000000000, vx = 100, vy = 0),
            MotorTelemetry(),
            nowMillis = 10L,
        )

        assertEquals(113.0, telemetry.latitude!!, 1e-9)
        assertEquals(200.0, telemetry.longitude!!, 1e-9)
        // 100 cm/s is 3.6 km/h. The exponential filter starts at 55 % of the first sample.
        assertEquals(1.98f, telemetry.speedKmh!!, 0.01f)
    }

    @Test
    fun speedBelowHalfIsClampedToZero() {
        val decoder = TelemetryDecoder()
        val telemetry = decoder.apply(
            GlobalPositionInt(vx = 10, vy = 0),
            MotorTelemetry(),
            nowMillis = 10L,
        )

        assertEquals(0f, telemetry.speedKmh!!, 0.001f)
    }
}

class MotorCommandFactoryTest {

    @Test
    fun pwmIsOnlySentForTheExpectedFirmwareVersion() {
        val withPwm = MotorCommandFactory.command(MotorCommand.TURN_LEFT, firmwareVersion = 102)
        assertEquals(90f, withPwm.param1, 0.001f)
        assertEquals(CommandLong.COMMAND_TURN_LEFT, withPwm.command)

        val withoutPwm = MotorCommandFactory.command(MotorCommand.TURN_LEFT, firmwareVersion = 101)
        assertEquals(0f, withoutPwm.param1, 0.001f)
    }

    @Test
    fun nonSteeringCommandsNeverCarryPwm() {
        val command = MotorCommandFactory.command(MotorCommand.MOTOR_ON, firmwareVersion = 102)
        assertEquals(0f, command.param1, 0.001f)
        assertEquals(CommandLong.COMMAND_MOTOR_ON, command.command)
    }

    @Test
    fun modeCommandsUseTheModeValue() {
        assertEquals(12, MotorCommandFactory.mode(MotorMode.ANCHOR).command)
        assertEquals(13, MotorCommandFactory.mode(MotorMode.MANUAL).command)
        assertEquals(14, MotorCommandFactory.mode(MotorMode.CRUISE).command)
        assertEquals(15, MotorCommandFactory.mode(MotorMode.NAVIGATION).command)
        assertEquals(181, MotorCommandFactory.mode(MotorMode.RECORD_WAYPOINT).command)
    }
}
