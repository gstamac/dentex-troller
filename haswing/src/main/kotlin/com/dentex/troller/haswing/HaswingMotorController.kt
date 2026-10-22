package com.dentex.troller.haswing

import com.dentex.troller.mavlink.CommandLong
import com.dentex.troller.mavlink.CommandShort
import com.dentex.troller.mavlink.Heartbeat
import com.dentex.troller.mavlink.HelmsmanMavlinkCodec
import com.dentex.troller.mavlink.MavlinkHeader
import com.dentex.troller.mavlink.MavlinkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/** Sink for encoded MAVLink frames. */
fun interface MotorTransport {
    suspend fun send(bytes: ByteArray)
}

/**
 * Controls a Haswing motor over a MAVLink link.
 *
 * The controller is deliberately conservative:
 *
 * - Propulsion commands are refused until the motor has been identified from a real heartbeat,
 *   and until the heartbeat type matches the profile that the command belongs to.
 * - The emergency stop is always allowed, even before the profile is known.
 * - Nothing is replayed automatically. A reconnect starts from a clean state and never repeats a
 *   previously sent motor-on command.
 */
class HaswingMotorController(
    private val transport: MotorTransport,
    private val scope: CoroutineScope,
    private val clock: () -> Long = System::currentTimeMillis,
    private val autoHeartbeat: Boolean = true,
) : HaswingMotor {

    private val codec = HelmsmanMavlinkCodec()
    private val decoder = TelemetryDecoder()

    private val identityState = MutableStateFlow<MotorIdentity?>(null)
    private val telemetryState = MutableStateFlow(MotorTelemetry())
    private val commandGateState = MutableStateFlow(CommandGateState.UNVERIFIED)

    private var heartbeatJob: Job? = null

    override val identity: StateFlow<MotorIdentity?> = identityState.asStateFlow()
    override val telemetry: StateFlow<MotorTelemetry> = telemetryState.asStateFlow()

    /** Whether commands may be sent, and why not when they may not. */
    val commandGate: StateFlow<CommandGateState> = commandGateState.asStateFlow()

    override val profile: MotorProfile
        get() = identityState.value?.profile ?: MotorProfile.UNKNOWN

    /**
     * Handles a decoded message from the motor.
     *
     * @return true when the message was consumed by this controller.
     */
    fun onMessage(message: MavlinkMessage, systemId: Int = 0, componentId: Int = 0): Boolean {
        val now = clock()
        if (message is Heartbeat) {
            val identity = MotorIdentity(
                heartbeatType = message.type,
                firmwareCustomMode = message.customMode,
                systemId = systemId,
                componentId = componentId,
                baseMode = message.baseMode,
            )
            val profileChanged = identityState.value?.profile != identity.profile
            identityState.value = identity
            telemetryState.value = decoder.apply(message, telemetryState.value, now)
                .copy(connected = true)
            commandGateState.value = when (identity.profile) {
                MotorProfile.TYPE_110 -> CommandGateState.READY
                MotorProfile.TYPE_11 -> CommandGateState.ALTERNATE_PROFILE
                MotorProfile.UNKNOWN -> CommandGateState.UNVERIFIED
            }
            if (profileChanged) {
                decoder.reset()
                if (autoHeartbeat) {
                    startHeartbeat()
                }
            }
            return true
        }
        telemetryState.value = decoder.apply(message, telemetryState.value, now)
        return true
    }

    /**
     * Marks the link as down.
     *
     * The identity is dropped so that a reconnect has to re-verify the profile before any
     * propulsion command is accepted again.
     */
    fun onDisconnected() {
        stopHeartbeat()
        decoder.reset()
        identityState.value = null
        telemetryState.value = MotorTelemetry()
        commandGateState.value = CommandGateState.UNVERIFIED
    }

    /**
     * Disables propulsion. Always allowed.
     *
     * The type 110 profile has a real stop command (`COMMAND_LONG` command 11). The alternative
     * type 11 profile does not implement a stop in the Helmsman application, so the closest
     * available action is sent there: a speed step down.
     */
    override suspend fun requestStop(): CommandResult {
        val identity = identityState.value
        val command = when (identity?.profile) {
            MotorProfile.TYPE_11 -> CommandShort(command = ShortMotorCommand.SPEED_DOWN.value)
            else -> CommandLong(command = MotorCommand.MOTOR_OFF.value)
        }
        return send(command)
    }

    /** Enables propulsion. Only allowed for a confirmed type 110 motor. */
    suspend fun requestMotorOn(): CommandResult {
        val identity = requireType110() ?: return CommandResult.Rejected(GATE_REASON)
        return send(MotorCommandFactory.command(MotorCommand.MOTOR_ON, identity.firmwareVersion))
    }

    /** Increases the speed by one step. */
    suspend fun requestSpeedUp(): CommandResult {
        val identity = requireType110() ?: return CommandResult.Rejected(GATE_REASON)
        return send(MotorCommandFactory.command(MotorCommand.SPEED_UP, identity.firmwareVersion))
    }

    /** Decreases the speed by one step. */
    suspend fun requestSpeedDown(): CommandResult {
        val identity = requireType110() ?: return CommandResult.Rejected(GATE_REASON)
        return send(MotorCommandFactory.command(MotorCommand.SPEED_DOWN, identity.firmwareVersion))
    }

    /** Sets the motor to full speed. */
    suspend fun requestFullSpeed(): CommandResult {
        val identity = requireType110() ?: return CommandResult.Rejected(GATE_REASON)
        return send(MotorCommandFactory.command(MotorCommand.FULL_SPEED, identity.firmwareVersion))
    }

    /**
     * Steers to the left.
     *
     * The Helmsman application repeats the steering command every 66 ms while the control is
     * held. Repeating is left to the caller so that a stuck steering command cannot happen
     * because of a lifecycle bug in this application.
     */
    suspend fun requestTurnLeft(): CommandResult {
        val identity = requireType110() ?: return CommandResult.Rejected(GATE_REASON)
        return send(MotorCommandFactory.command(MotorCommand.TURN_LEFT, identity.firmwareVersion))
    }

    /** Steers to the right. See [requestTurnLeft] for the repetition semantics. */
    suspend fun requestTurnRight(): CommandResult {
        val identity = requireType110() ?: return CommandResult.Rejected(GATE_REASON)
        return send(MotorCommandFactory.command(MotorCommand.TURN_RIGHT, identity.firmwareVersion))
    }

    /** Switches the motor to a different operating mode. */
    suspend fun requestMode(mode: MotorMode): CommandResult {
        val identity = requireType110() ?: return CommandResult.Rejected(GATE_REASON)
        return send(MotorCommandFactory.mode(mode))
    }

    /** Sends an arbitrary MAVLink message over the motor link. */
    suspend fun send(message: MavlinkMessage): CommandResult = try {
        transport.send(codec.encodeNext(message))
        CommandResult.Sent
    } catch (error: Exception) {
        CommandResult.Failed(error.message ?: "Unable to send message")
    }

    /**
     * Starts the periodic heartbeat.
     *
     * The Helmsman application sends a heartbeat with `type = 106` and `mavlink_version = 3`
     * every two seconds while the app is in the foreground.
     */
    fun startHeartbeat(periodMillis: Long = HEARTBEAT_PERIOD_MILLIS) {
        if (heartbeatJob?.isActive == true) return
        heartbeatJob = scope.launch {
            while (isActive) {
                try {
                    transport.send(
                        codec.encodeNext(
                            Heartbeat(type = Heartbeat.GCS_TYPE, mavlinkVersion = 3),
                        ),
                    )
                } catch (_: Exception) {
                    // A failed heartbeat must not crash the link; the motor watchdog handles it.
                }
                delay(periodMillis)
            }
        }
    }

    /** Stops the periodic heartbeat. */
    fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    /** Sends the mission download request with the identifiers the motor expects. */
    suspend fun requestRouteDownload(): CommandResult = try {
        transport.send(
            codec.encode(
                com.dentex.troller.mavlink.MissionRequestList(),
                MavlinkHeader(
                    sequence = nextSequence(),
                    systemId = MavlinkHeader.MISSION_SYSTEM_ID,
                    componentId = MavlinkHeader.MISSION_COMPONENT_ID,
                ),
            ),
        )
        CommandResult.Sent
    } catch (error: Exception) {
        CommandResult.Failed(error.message ?: "Unable to request route")
    }

    private var sequenceCounter: Int = 0

    private fun nextSequence(): Int {
        sequenceCounter = (sequenceCounter + 1) and 0xFF
        return sequenceCounter
    }

    private fun requireType110(): MotorIdentity? =
        identityState.value?.takeIf { it.profile == MotorProfile.TYPE_110 }

    companion object {
        /** Heartbeat period used by the Helmsman application. */
        const val HEARTBEAT_PERIOD_MILLIS: Long = 2000L

        /** Reason reported when a command is refused because the motor is not identified. */
        const val GATE_REASON: String =
            "Motor profile not confirmed; waiting for a heartbeat from a type 110 motor"
    }
}

/** Whether the application is allowed to send propulsion commands. */
enum class CommandGateState {
    /** No heartbeat has been received yet. */
    UNVERIFIED,

    /** A type 110 motor was confirmed; propulsion commands are allowed. */
    READY,

    /** A different motor profile was detected; only the type 11 commands are available. */
    ALTERNATE_PROFILE,
}
