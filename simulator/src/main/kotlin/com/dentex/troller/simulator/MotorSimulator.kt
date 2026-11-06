package com.dentex.troller.simulator

import com.dentex.troller.mavlink.HelmsmanMavlinkCodec
import com.dentex.troller.mavlink.MavlinkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Runs a [SimulatedMotor] behind a byte stream, the way the remote sits behind the BLE link.
 *
 * Bytes from the app go to [receive]; the motor's answers and its telemetry come out through
 * [send] as MAVLink v1 frames. [control] takes the commands that adb or the simulator buttons send.
 *
 * @param send delivers frames to the app; false when the link is down and they were dropped.
 * @param log tells what the simulator did, for the diagnostic log.
 */
class MotorSimulator(
    private val scope: CoroutineScope,
    private val send: suspend (ByteArray) -> Unit,
    private val log: (String) -> Unit = {},
    private val clock: () -> Long = System::currentTimeMillis,
) {

    val motor = SimulatedMotor()

    /** Simulated seconds per real second. */
    var timeScale: Double = 1.0
        private set

    private val parser = HelmsmanMavlinkCodec()
    private val encoder = HelmsmanMavlinkCodec()
    private var job: Job? = null
    private var silentUntilMillis = 0L
    private val started = clock()

    /** Starts the motor clock and the telemetry; does nothing when it already runs. */
    fun start() {
        if (job?.isActive == true) return
        job = scope.launch {
            var last = clock()
            var nextTelemetry = last
            while (isActive) {
                val now = clock()
                synchronized(motor) { motor.advance((now - last) / 1000.0 * timeScale) }
                last = now
                if (now >= nextTelemetry) {
                    nextTelemetry = now + TELEMETRY_PERIOD_MILLIS
                    if (now >= silentUntilMillis) {
                        val messages = synchronized(motor) { motor.telemetry(now - started) }
                        messages.forEach { emit(it) }
                    }
                }
                delay(STEP_MILLIS)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    /** Bytes the app wrote to the link. */
    fun receive(bytes: ByteArray) {
        parser.consume(bytes).forEach { frame ->
            val message = MavlinkMessage.decode(frame) ?: return@forEach
            val replies = synchronized(motor) { motor.receive(message) }
            if (replies.isNotEmpty() && clock() >= silentUntilMillis) {
                scope.launch { replies.forEach { emit(it) } }
            }
        }
    }

    private suspend fun emit(message: MavlinkMessage) {
        send(encoder.encodeNext(message, systemId = MOTOR_SYSTEM_ID, componentId = MOTOR_COMPONENT_ID))
    }

    /** Stops answering for [seconds] while the link stays up, so the app's watchdog fires. */
    fun silence(seconds: Double) {
        silentUntilMillis = clock() + (seconds * 1000).toLong()
    }

    /**
     * Runs one control command and says what it did.
     *
     * Commands: `status`, `position lat,lon`, `heading deg`, `gps fix|nofix`,
     * `arrival anchor|manual|circle`, `current knots,toward`, `timescale factor`,
     * `fail none|upload|navigation|anchor`, `battery percent`, `silence seconds`, `reset`.
     * The link commands (`link drop|lose`) belong to the transport, which handles them itself.
     */
    fun control(command: String, value: String?): String {
        val result = try {
            synchronized(motor) { apply(command.trim().lowercase(Locale.US), value?.trim()) }
        } catch (error: IllegalArgumentException) {
            "Error: ${error.message}"
        }
        log(result)
        return result
    }

    private fun apply(command: String, value: String?): String {
        fun need(): String = requireNotNull(value?.takeIf { it.isNotEmpty() }) { "$command needs a value" }
        fun numbers(count: Int): List<Double> {
            val parts = need().split(',').map { it.trim().toDoubleOrNull() ?: throw IllegalArgumentException("not a number: $it") }
            require(parts.size == count) { "$command needs $count number(s)" }
            return parts
        }
        fun <T : Enum<T>> choice(options: Array<T>): T =
            options.firstOrNull { it.name.equals(need(), ignoreCase = true) }
                ?: throw IllegalArgumentException("$command is one of ${options.joinToString("|") { it.name.lowercase(Locale.US) }}")

        when (command) {
            "status" -> Unit
            "position" -> numbers(2).let { (lat, lon) -> motor.place(lat, lon) }
            "heading" -> motor.turnTo(numbers(1).first())
            "gps" -> motor.settings = motor.settings.copy(
                gpsFix = when (need().lowercase(Locale.US)) {
                    "fix", "on" -> true
                    "nofix", "off" -> false
                    else -> throw IllegalArgumentException("gps is fix|nofix")
                },
            )
            "arrival" -> motor.settings = motor.settings.copy(arrival = choice(SimulatedMotor.Arrival.entries.toTypedArray()))
            "mode" -> motor.pressMode(choice(SimulatedMotor.Mode.entries.toTypedArray()))
            "fail" ->motor.settings = motor.settings.copy(failure = choice(SimulatedMotor.Failure.entries.toTypedArray()))
            "current" -> numbers(2).let { (knots, toward) ->
                motor.settings = motor.settings.copy(currentKnots = knots, currentTowardDegrees = toward)
            }
            "battery" -> motor.settings = motor.settings.copy(batteryPercent = numbers(1).first().toInt().coerceIn(0, 100))
            "timescale" -> timeScale = numbers(1).first().coerceIn(0.1, 100.0)
            "silence" -> silence(numbers(1).first())
            "reset" -> {
                motor.settings = SimulatedMotor.Settings()
                timeScale = 1.0
                motor.place(SimulatedMotor.START_LATITUDE, SimulatedMotor.START_LONGITUDE)
            }
            else -> throw IllegalArgumentException("unknown command $command")
        }
        return "Simulator: ${motor.describe()}, time x${String.format(Locale.US, "%.1f", timeScale)}"
    }

    companion object {
        const val TELEMETRY_PERIOD_MILLIS = 1_000L
        private const val STEP_MILLIS = 100L
        private const val MOTOR_SYSTEM_ID = 1
        private const val MOTOR_COMPONENT_ID = 1
    }
}
