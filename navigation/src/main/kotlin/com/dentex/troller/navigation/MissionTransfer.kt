package com.dentex.troller.navigation

import com.dentex.troller.mavlink.MavlinkMessage
import com.dentex.troller.mavlink.MissionAck
import com.dentex.troller.mavlink.MissionCount
import com.dentex.troller.mavlink.MissionItem
import com.dentex.troller.mavlink.MissionRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Route transfer state machine.
 *
 * The timings and the handshake follow the `HMMissionManager` of the Helmsman application:
 *
 * **Upload**
 *  1. The phone sends `MISSION_COUNT` with the number of waypoints. It repeats this up to
 *     [UPLOAD_COUNT_ATTEMPTS] times with [UPLOAD_RETRY_DELAY_MILLIS] in between, and stops as
 *     soon as the motor asks for the first waypoint. Without an answer the upload fails.
 *  2. The motor answers with `MISSION_REQUEST` for the waypoint it wants. The phone answers with
 *     `MISSION_ITEM` and repeats that answer every [ITEM_RETRY_DELAY_MILLIS] until the motor
 *     moves on, at most [ITEM_RETRY_ATTEMPTS] times.
 *  3. The motor finishes with `MISSION_ACK`.
 *
 * **Download**
 *  1. The phone sends `MISSION_REQUEST_LIST` every [DOWNLOAD_LIST_POLL_MILLIS] until the motor
 *     answers with `MISSION_COUNT`, for at most [DOWNLOAD_TIMEOUT_MILLIS].
 *  2. The phone then requests the waypoints one by one with `MISSION_REQUEST`, polling every
 *     [DOWNLOAD_ITEM_POLL_MILLIS]. Each individual waypoint must arrive within
 *     [DOWNLOAD_ITEM_TIMEOUT_MILLIS].
 *
 * Every background loop belongs to the transfer that started it: cancelling, failing or
 * completing a transfer stops all of them, so nothing keeps talking to the motor afterwards.
 */
class MissionTransfer(
    private val scope: CoroutineScope,
    /** Sends a message; returns false when the message could not be handed to the link. */
    private val sendMessage: suspend (MavlinkMessage) -> Boolean,
    /** Sends `MISSION_REQUEST_LIST`; returns false when it could not be handed to the link. */
    private val requestRouteList: suspend () -> Boolean,
) {

    private val progressState = MutableStateFlow(MissionProgress(MissionProgress.Phase.IDLE))
    private val downloaded = mutableListOf<Waypoint>()

    /** Upload `MISSION_COUNT` loop, or download list/item loop. */
    private var transferJob: Job? = null

    /** Repeats the answer for the waypoint the motor asked for during an upload. */
    private var itemJob: Job? = null

    private var pendingRoute: Route? = null
    private var uploadRequestReceived: Boolean = false
    private var expectedCount: Int = -1

    /** Progress of the current transfer. */
    val progress: StateFlow<MissionProgress> = progressState.asStateFlow()

    /** The route that was downloaded, or `null` while no download completed. */
    val downloadedRoute: Route?
        get() = if (downloaded.isEmpty()) null else Route(downloaded.toList())

    /**
     * Starts an upload of [route].
     *
     * Uploading a route never starts propulsion. Switching to navigation mode is a separate,
     * explicitly confirmed action.
     */
    fun startUpload(route: Route): Boolean {
        if (route.isEmpty) {
            progressState.value = MissionProgress(
                MissionProgress.Phase.FAILED,
                message = "The route does not contain any waypoint",
            )
            return false
        }
        stopJobs()
        pendingRoute = route
        uploadRequestReceived = false
        progressState.value = MissionProgress(MissionProgress.Phase.UPLOADING, total = route.size)
        transferJob = scope.launch {
            repeat(UPLOAD_COUNT_ATTEMPTS) {
                if (uploadRequestReceived) return@launch
                if (!sendMessage(MissionCount(count = route.size))) {
                    fail("Unable to send the route size to the motor")
                    return@launch
                }
                delay(UPLOAD_RETRY_DELAY_MILLIS)
            }
            if (!uploadRequestReceived) {
                fail("The motor did not answer the route upload")
            }
        }
        return true
    }

    /** Starts a download of the route that is stored on the motor. */
    fun startDownload(): Boolean {
        stopJobs()
        pendingRoute = null
        downloaded.clear()
        expectedCount = -1
        progressState.value = MissionProgress(MissionProgress.Phase.DOWNLOADING)
        transferJob = scope.launch {
            val answered = withTimeoutOrNull(DOWNLOAD_TIMEOUT_MILLIS) {
                while (expectedCount < 0) {
                    if (!requestRouteList()) return@withTimeoutOrNull false
                    delay(DOWNLOAD_LIST_POLL_MILLIS)
                }
                true
            }
            when (answered) {
                null -> fail("The motor did not answer the route request within $DOWNLOAD_TIMEOUT_MILLIS ms")
                false -> fail("Unable to send the route request to the motor")
                true -> Unit
            }
        }
        return true
    }

    /** Cancels any running transfer. */
    fun cancel() {
        stopJobs()
        pendingRoute = null
        progressState.value = MissionProgress(MissionProgress.Phase.IDLE)
    }

    /**
     * Feeds a message from the motor into the state machine.
     *
     * @return true when the message was part of a transfer.
     */
    fun onMessage(message: MavlinkMessage): Boolean {
        when (message) {
            is MissionRequest -> {
                handleRequest(message.sequence)
                return true
            }

            is MissionCount -> {
                handleCount(message.count)
                return true
            }

            is MissionItem -> {
                handleItem(message)
                return true
            }

            is MissionAck -> {
                handleAcknowledgement(message)
                return true
            }

            else -> return false
        }
    }

    private fun handleRequest(sequence: Int) {
        if (progressState.value.phase != MissionProgress.Phase.UPLOADING) return
        val route = pendingRoute ?: return
        if (sequence !in 0 until route.size) return

        // The motor has accepted the size; repeating MISSION_COUNT now could restart its transfer.
        uploadRequestReceived = true
        transferJob?.cancel()
        transferJob = null

        val waypoint = route.waypoints[sequence]
        val item = MissionItem(
            x = waypoint.latitude.toFloat(),
            y = waypoint.longitude.toFloat(),
            sequence = sequence,
            targetSystem = MISSION_TARGET_SYSTEM,
        )
        itemJob?.cancel()
        itemJob = scope.launch {
            repeat(ITEM_RETRY_ATTEMPTS) {
                if (!sendMessage(item)) {
                    fail("Unable to send waypoint ${sequence + 1} to the motor")
                    return@launch
                }
                progressState.value = progressState.value.copy(total = route.size, completed = sequence)
                delay(ITEM_RETRY_DELAY_MILLIS)
            }
            fail("The motor stopped answering after waypoint ${sequence + 1}")
        }
    }

    private fun handleCount(count: Int) {
        if (progressState.value.phase != MissionProgress.Phase.DOWNLOADING) return
        // The list request is polled, so the motor may answer it more than once.
        if (expectedCount >= 0) return
        transferJob?.cancel()
        transferJob = null
        if (count <= 0) {
            expectedCount = 0
            progressState.value = MissionProgress(
                MissionProgress.Phase.COMPLETE,
                total = 0,
                completed = 0,
                message = "The motor has no stored route",
            )
            return
        }
        expectedCount = count
        progressState.value = progressState.value.copy(total = count, completed = 0)
        transferJob = scope.launch {
            while (downloaded.size < expectedCount) {
                val sequence = downloaded.size
                val result = withTimeoutOrNull(DOWNLOAD_ITEM_TIMEOUT_MILLIS) {
                    while (downloaded.size == sequence) {
                        if (!sendMessage(MissionRequest(sequence = sequence))) return@withTimeoutOrNull false
                        delay(DOWNLOAD_ITEM_POLL_MILLIS)
                    }
                    true
                }
                when (result) {
                    null -> {
                        fail("Waypoint ${sequence + 1} did not arrive within $DOWNLOAD_ITEM_TIMEOUT_MILLIS ms")
                        return@launch
                    }
                    false -> {
                        fail("Unable to request waypoint ${sequence + 1} from the motor")
                        return@launch
                    }
                    true -> Unit
                }
            }
        }
    }

    private fun handleItem(item: MissionItem) {
        if (progressState.value.phase != MissionProgress.Phase.DOWNLOADING) return
        if (expectedCount <= 0) return
        if (item.sequence != downloaded.size) return
        downloaded.add(Waypoint(item.x.toDouble(), item.y.toDouble()))
        progressState.value = progressState.value.copy(
            total = expectedCount,
            completed = downloaded.size,
        )
        if (downloaded.size >= expectedCount) {
            stopJobs()
            progressState.value = progressState.value.copy(
                phase = MissionProgress.Phase.COMPLETE,
                message = "Route downloaded",
            )
        }
    }

    private fun handleAcknowledgement(ack: MissionAck) {
        if (progressState.value.phase != MissionProgress.Phase.UPLOADING) return
        stopJobs()
        val total = pendingRoute?.size ?: progressState.value.total
        pendingRoute = null
        progressState.value = progressState.value.copy(
            phase = MissionProgress.Phase.COMPLETE,
            total = total,
            completed = total,
            message = if (ack.type == MissionAck.MAV_MISSION_ACCEPTED) {
                "Route uploaded"
            } else {
                "Route uploaded (acknowledgement type ${ack.type})"
            },
        )
    }

    private fun fail(message: String) {
        progressState.value = progressState.value.copy(
            phase = MissionProgress.Phase.FAILED,
            message = message,
        )
        pendingRoute = null
        stopJobs()
    }

    private fun stopJobs() {
        transferJob?.cancel()
        transferJob = null
        itemJob?.cancel()
        itemJob = null
    }

    companion object {
        /** Number of times the `MISSION_COUNT` handshake is repeated. */
        const val UPLOAD_COUNT_ATTEMPTS: Int = 3

        /** Delay between two `MISSION_COUNT` attempts. */
        const val UPLOAD_RETRY_DELAY_MILLIS: Long = 3000L

        /** Delay after which an unanswered waypoint is sent again. */
        const val ITEM_RETRY_DELAY_MILLIS: Long = 3000L

        /** How often an unanswered waypoint is sent before the upload fails. */
        const val ITEM_RETRY_ATTEMPTS: Int = 5

        /** Interval of the `MISSION_REQUEST_LIST` poll during a download. */
        const val DOWNLOAD_LIST_POLL_MILLIS: Long = 150L

        /** Interval of the `MISSION_REQUEST` poll during a download. */
        const val DOWNLOAD_ITEM_POLL_MILLIS: Long = 500L

        /** Time the motor has to answer the route request with `MISSION_COUNT`. */
        const val DOWNLOAD_TIMEOUT_MILLIS: Long = 25000L

        /** Timeout for a single waypoint during a download. */
        const val DOWNLOAD_ITEM_TIMEOUT_MILLIS: Long = 10000L

        /** Target system id the type 110 motor expects in `MISSION_ITEM`. */
        const val MISSION_TARGET_SYSTEM: Int = 106
    }
}
