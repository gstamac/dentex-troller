package com.dentex.troller.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Where a go-to stands.
 *
 * @param target the first waypoint: the one point of a go-to, or the start of a followed route.
 * @param waypoints every waypoint sent to the motor; one for a go-to.
 * @param name the name of a followed route, or null for a go-to.
 * @param skipped how many waypoints at the start of a followed route were left out, because the
 *   boat was already on the route past them; [waypoints] holds the rest.
 */
data class GoToState(
    val phase: Phase,
    val target: Waypoint? = null,
    val message: String? = null,
    val waypoints: List<Waypoint> = listOfNotNull(target),
    val name: String? = null,
    val skipped: Int = 0,
) {
    enum class Phase {
        /** Nothing is happening. */
        IDLE,

        /** The target (a one-waypoint route) or the followed route is being uploaded. */
        UPLOADING,

        /** Navigation mode was sent; waiting for the motor to report it. */
        STARTING,

        /** The motor reports navigation mode towards the target. */
        UNDER_WAY,

        /** The motor left navigation mode, or the go-to was cancelled. */
        ENDED,

        /** The go-to did not start; [message] says why. */
        FAILED,
    }

    /** True while the go-to is starting or the motor is navigating to the target. */
    val active: Boolean get() = phase == Phase.UPLOADING || phase == Phase.STARTING || phase == Phase.UNDER_WAY

    /** True when a whole route is followed rather than one point. */
    val followsRoute: Boolean get() = name != null

    /** The waypoint the motor heads for, from the index it reports; the first one when unknown. */
    fun heading(reportedIndex: Int?): Waypoint? =
        waypoints.getOrNull(reportedIndex ?: 0) ?: waypoints.firstOrNull()
}

/**
 * Moves the boat to one position, or along a route, the way the Helmsman application starts a route.
 *
 * The motor has no "go to" command. Helmsman uploads the waypoints and, one second after the
 * motor acknowledges them, sends navigation mode. This class does the same, with a single
 * waypoint for a go-to or all of a route's waypoints to follow it, and adds the checks that the
 * motor never moves on a guess:
 *
 *  - Navigation is sent once, and only after the upload was acknowledged. It is never repeated:
 *    when the motor does not report navigation mode within [CONFIRM_TIMEOUT_MILLIS], the go-to
 *    fails and the user decides what to do.
 *  - A go-to does not start while the motor already reports navigation mode, so that the
 *    confirmation cannot come from a route that was running before.
 *  - [onLinkLost] stops whatever is still pending. Nothing is sent again after a reconnect.
 *
 * Tried on the motor (2026-11-05): navigation steers to the target at the speed the motor had
 * last, so at speed 0 it only turns. Manual mode leaves the propeller running, and only the stop
 * command stops it. So once navigation is confirmed, one speed step is sent when the motor
 * reports speed 0, and ending the go-to sends manual mode and then the stop command.
 *
 * @param startUpload starts the upload of a route; false when it could not start.
 * @param uploadProgress progress of that upload.
 * @param cancelUpload stops the upload.
 * @param sendNavigation sends navigation mode once; false when it could not be sent.
 * @param sendManual sends manual mode once, which ends the navigation; false when it could not be sent.
 * @param sendStop sends the stop command once; false when it could not be sent.
 * @param sendAnchor sends anchor mode once, which holds the boat where it is; false when it could not be sent.
 * @param sendSpeedUp sends one speed step up; false when it could not be sent.
 * @param navigating whether the motor reports navigation mode, or null while it reports no mode.
 * @param motorSpeed the speed step the motor reports, or null before it reported one.
 * @param anchored whether the motor reports anchor mode, which holds the boat with the propeller.
 */
class GoToTarget(
    private val scope: CoroutineScope,
    private val startUpload: (Route) -> Boolean,
    private val uploadProgress: StateFlow<MissionProgress>,
    private val cancelUpload: () -> Unit,
    private val sendNavigation: suspend () -> Boolean,
    private val sendManual: suspend () -> Boolean,
    private val sendStop: suspend () -> Boolean,
    private val sendAnchor: suspend () -> Boolean,
    private val sendSpeedUp: suspend () -> Boolean,
    private val navigating: StateFlow<Boolean?>,
    private val motorSpeed: StateFlow<Int?>,
    private val anchored: () -> Boolean,
) {

    private val stateFlow = MutableStateFlow(GoToState(GoToState.Phase.IDLE))
    private var job: Job? = null

    val state: StateFlow<GoToState> = stateFlow.asStateFlow()

    /**
     * Starts moving the boat to [target].
     *
     * @return null when the go-to started, otherwise why it was refused.
     */
    fun start(target: Waypoint): String? = start(listOf(target), name = null)

    /**
     * Starts moving the boat along [waypoints], from the first: a route called [name], or one
     * point when [name] is null. [skipped] says how many waypoints of the route came before
     * [waypoints], for the numbers on screen.
     *
     * @return null when it started, otherwise why it was refused.
     */
    fun start(waypoints: List<Waypoint>, name: String?, skipped: Int = 0): String? {
        if (waypoints.isEmpty()) return "The route has no waypoints."
        if (stateFlow.value.active) return "A go-to is already running. Cancel it first."
        if (navigating.value == true) return "The motor is already navigating. Cancel that first."
        if (!startUpload(Route(waypoints))) return "The route could not be uploaded."
        val base = GoToState(GoToState.Phase.IDLE, waypoints.first(), waypoints = waypoints, name = name, skipped = skipped)
        stateFlow.value = base.copy(phase = GoToState.Phase.UPLOADING)
        job = scope.launch {
            val uploaded = withTimeoutOrNull(UPLOAD_TIMEOUT_MILLIS) {
                uploadProgress.first {
                    it.phase == MissionProgress.Phase.COMPLETE ||
                        it.phase == MissionProgress.Phase.FAILED ||
                        it.phase == MissionProgress.Phase.IDLE
                }
            }
            when (uploaded?.phase) {
                MissionProgress.Phase.COMPLETE -> Unit
                MissionProgress.Phase.FAILED -> return@launch fail(uploaded.message ?: "The upload failed.")
                null -> {
                    cancelUpload()
                    return@launch fail("The motor did not take the route in time.")
                }
                else -> return@launch fail("The upload was stopped.")
            }
            // Helmsman waits a second between the acknowledgement and the mode change.
            delay(START_DELAY_MILLIS)
            stateFlow.value = base.copy(phase = GoToState.Phase.STARTING, message = null)
            if (!sendNavigation()) return@launch fail("Navigation mode could not be sent.")
            val confirmed = withTimeoutOrNull(CONFIRM_TIMEOUT_MILLIS) { navigating.first { it == true } }
            if (confirmed == null) {
                return@launch fail(
                    "The motor did not report navigation mode. Check the motor; nothing was sent again.",
                )
            }
            // Navigation keeps the last speed; at 0 the motor only turns towards the target.
            val note = when (motorSpeed.value) {
                0 -> if (sendSpeedUp()) "The speed was 0, so it was set to 1." else "The speed is 0 and could not be raised. Use +."
                null -> "The motor reports no speed. Use + if the boat does not move."
                else -> null
            }
            stateFlow.value = base.copy(phase = GoToState.Phase.UNDER_WAY, message = note)
            navigating.first { it != true }
            // Left by itself, for example on arrival. Anchor mode holds the spot with the
            // propeller, so it is left alone; anything else gets the stop command.
            stateFlow.value = if (anchored()) {
                base.copy(phase = GoToState.Phase.ENDED, message = "The motor left navigation mode and is anchoring.")
            } else if (sendStop()) {
                base.copy(phase = GoToState.Phase.ENDED, message = "The motor left navigation mode, so it was stopped.")
            } else {
                base.copy(phase = GoToState.Phase.FAILED, message = "The motor left navigation mode and the stop could not be sent. Use STOP.")
            }
        }
        return null
    }

    /**
     * Cancels the go-to. A pending upload is stopped; once navigation mode was sent, or whenever
     * the motor reports navigation mode, manual mode is sent once to end it and then the stop
     * command, because manual mode leaves the propeller running.
     */
    fun cancel() {
        val current = stateFlow.value
        job?.cancel()
        job = null
        val navigationSent = current.phase == GoToState.Phase.STARTING || current.phase == GoToState.Phase.UNDER_WAY
        if (current.phase == GoToState.Phase.UPLOADING) cancelUpload()
        if (!navigationSent && navigating.value != true) {
            stateFlow.value = current.copy(phase = GoToState.Phase.ENDED, message = "Cancelled. The motor was not started.")
            return
        }
        stateFlow.value = current.copy(phase = GoToState.Phase.ENDED, message = "Cancelling…")
        job = scope.launch {
            val manual = sendManual()
            delay(STOP_AFTER_MANUAL_MILLIS)
            // The stop goes out even when manual mode could not be sent.
            val stopped = sendStop()
            stateFlow.value = when {
                manual && stopped -> current.copy(phase = GoToState.Phase.ENDED, message = "Navigation cancelled and the motor stopped.")
                stopped -> current.copy(phase = GoToState.Phase.FAILED, message = "The motor was stopped, but manual mode could not be sent.")
                else -> current.copy(phase = GoToState.Phase.FAILED, message = "The stop could not be sent. Use STOP.")
            }
        }
    }

    /**
     * Switches from navigation to anchor mode, so the motor holds the boat where it is now.
     * Anchor mode is sent once; the stop command is not sent, because anchoring needs the propeller.
     * Does nothing unless navigation mode was sent or the motor reports it.
     */
    fun anchor() {
        val current = stateFlow.value
        val navigationSent = current.phase == GoToState.Phase.STARTING || current.phase == GoToState.Phase.UNDER_WAY
        if (!navigationSent && navigating.value != true) return
        // Stops the watcher too, so leaving navigation for anchor does not send the stop command.
        job?.cancel()
        stateFlow.value = current.copy(phase = GoToState.Phase.ENDED, message = "Switching to anchor mode…")
        job = scope.launch {
            if (!sendAnchor()) {
                stateFlow.value = current.copy(phase = GoToState.Phase.FAILED, message = "Anchor mode could not be sent. The motor may still be navigating.")
                return@launch
            }
            val left = withTimeoutOrNull(CONFIRM_TIMEOUT_MILLIS) { navigating.first { it != true } }
            stateFlow.value = if (left != null && anchored()) {
                current.copy(phase = GoToState.Phase.ENDED, message = "The motor is anchoring where the boat is.")
            } else {
                current.copy(phase = GoToState.Phase.FAILED, message = "The motor did not report anchor mode. Check the motor; nothing was sent again.")
            }
        }
    }

    /** Clears an ended or failed go-to from the screen. */
    fun dismiss() {
        if (stateFlow.value.active) return
        job?.cancel()
        job = null
        stateFlow.value = GoToState(GoToState.Phase.IDLE)
    }

    /**
     * The link to the motor is lost. A go-to that has not been confirmed fails, and nothing is
     * sent again on its behalf. One that is under way stays on screen, because the motor carries
     * on by itself; the motor's own mode report decides when it ends.
     */
    fun onLinkLost() {
        val current = stateFlow.value
        when (current.phase) {
            GoToState.Phase.UPLOADING -> {
                job?.cancel()
                job = null
                cancelUpload()
                stateFlow.value = current.copy(phase = GoToState.Phase.FAILED, message = "The link was lost during the upload. The motor was not started.")
            }
            GoToState.Phase.STARTING -> {
                job?.cancel()
                job = null
                stateFlow.value = current.copy(phase = GoToState.Phase.FAILED, message = "The link was lost just after navigation mode was sent. The motor may be moving.")
            }
            else -> Unit
        }
    }

    private fun fail(message: String) {
        stateFlow.value = stateFlow.value.copy(phase = GoToState.Phase.FAILED, message = message)
    }

    companion object {
        /** Longest wait for the motor to take the one-waypoint route. */
        const val UPLOAD_TIMEOUT_MILLIS: Long = 30_000L

        /** Pause between the upload acknowledgement and navigation mode, as in Helmsman. */
        const val START_DELAY_MILLIS: Long = 1_000L

        /** Pause between manual mode and the stop command when a go-to is cancelled. */
        const val STOP_AFTER_MANUAL_MILLIS: Long = 300L

        /** Longest wait for the motor to report navigation mode. */
        const val CONFIRM_TIMEOUT_MILLIS: Long = 6_000L
    }
}

/** Initial bearing from [from] to [to], in degrees clockwise from true north. */
fun bearingDegrees(from: Waypoint, to: Waypoint): Double {
    val lat1 = Math.toRadians(from.latitude)
    val lat2 = Math.toRadians(to.latitude)
    val dLon = Math.toRadians(to.longitude - from.longitude)
    val y = sin(dLon) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
    return (Math.toDegrees(atan2(y, x)) + 360.0) % 360.0
}
