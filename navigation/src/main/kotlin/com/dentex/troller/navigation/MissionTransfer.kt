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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Route transfer state machine.
 *
 * The timings and the handshake follow the `HMMissionManager` of the Helmsman application:
 *
 * **Upload**
 *  1. The phone sends `MISSION_COUNT` with the number of waypoints. It repeats this up to
 *     [UPLOAD_COUNT_ATTEMPTS] times with [UPLOAD_RETRY_DELAY_MILLIS] in between.
 *  2. The motor answers with `MISSION_REQUEST` for the waypoint it wants. The phone answers with
 *     `MISSION_ITEM` and repeats that answer every [ITEM_RETRY_DELAY_MILLIS] until the motor
 *     moves on.
 *  3. The motor finishes with `MISSION_ACK`.
 *
 * **Download**
 *  1. The phone sends `MISSION_REQUEST_LIST` every [DOWNLOAD_LIST_POLL_MILLIS] until the motor
 *     answers with `MISSION_COUNT`. The whole download must finish within
 *     [DOWNLOAD_TIMEOUT_MILLIS].
 *  2. The phone then requests the waypoints one by one with `MISSION_REQUEST`, polling every
 *     [DOWNLOAD_ITEM_POLL_MILLIS]. Each individual waypoint must arrive within
 *     [DOWNLOAD_ITEM_TIMEOUT_MILLIS].
 */
class MissionTransfer(
    private val scope: CoroutineScope,
    private val sendMessage: suspend (MavlinkMessage) -> Unit,
    private val requestRouteList: suspend () -> Unit,
) {

    private val progressState = MutableStateFlow(MissionProgress(MissionProgress.Phase.IDLE))
    private val downloaded = mutableListOf<Waypoint>()

    private var uploadJob: Job? = null
    private var downloadJob: Job? = null
    private var itemRetryJob: Job? = null
    private var downloadWatchdogJob: Job? = null
    private var pendingUploadSequence: Int = -1
    private var pendingRoute: Route? = null
    private var expectedCount: Int = -1
    private var requestedSequence: Int = -1

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
        uploadJob?.cancel()
        itemRetryJob?.cancel()
        pendingUploadSequence = -1
        pendingRoute = route
        progressState.value = MissionProgress(MissionProgress.Phase.UPLOADING, total = route.size)
        uploadJob = scope.launch {
            var attempt = 0
            while (attempt < UPLOAD_COUNT_ATTEMPTS && isActive) {
                sendMessage(MissionCount(count = route.size))
                attempt++
                delay(UPLOAD_RETRY_DELAY_MILLIS)
            }
        }
        return true
    }

    /** Starts a download of the route that is stored on the motor. */
    fun startDownload(): Boolean {
        downloadJob?.cancel()
        downloadWatchdogJob?.cancel()
        downloaded.clear()
        expectedCount = -1
        requestedSequence = -1
        progressState.value = MissionProgress(MissionProgress.Phase.DOWNLOADING)
        downloadJob = scope.launch {
            val deadline = System.currentTimeMillis() + DOWNLOAD_TIMEOUT_MILLIS
            while (isActive && expectedCount < 0) {
                if (System.currentTimeMillis() > deadline) {
                    fail("The motor did not answer the route request within ${DOWNLOAD_TIMEOUT_MILLIS} ms")
                    return@launch
                }
                requestRouteList()
                delay(DOWNLOAD_LIST_POLL_MILLIS)
            }
        }
        return true
    }

    /** Cancels any running transfer. */
    fun cancel() {
        uploadJob?.cancel()
        uploadJob = null
        downloadJob?.cancel()
        downloadJob = null
        itemRetryJob?.cancel()
        itemRetryJob = null
        downloadWatchdogJob?.cancel()
        downloadWatchdogJob = null
        pendingUploadSequence = -1
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
                handleAcknowledgement()
                return true
            }

            else -> return false
        }
    }

    private fun handleRequest(sequence: Int) {
        val route = pendingRoute ?: return
        if (sequence >= route.size) return
        pendingUploadSequence = sequence
        val waypoint = route.waypoints[sequence]
        sendWaypoint(route, sequence, waypoint)
        scheduleItemRetry(route, sequence, waypoint)
    }

    private fun sendWaypoint(route: Route, sequence: Int, waypoint: Waypoint) {
        scope.launch {
            sendMessage(
                MissionItem(
                    x = waypoint.latitude.toFloat(),
                    y = waypoint.longitude.toFloat(),
                    sequence = sequence,
                    targetSystem = MISSION_TARGET_SYSTEM,
                ),
            )
            progressState.value = progressState.value.copy(
                total = route.size,
                completed = sequence,
            )
        }
    }

    /**
     * Repeats the answer for the requested waypoint until the motor asks for the next one.
     */
    private fun scheduleItemRetry(route: Route, sequence: Int, waypoint: Waypoint) {
        itemRetryJob?.cancel()
        itemRetryJob = scope.launch {
            while (isActive && pendingUploadSequence == sequence) {
                delay(ITEM_RETRY_DELAY_MILLIS)
                if (pendingUploadSequence != sequence) break
                sendWaypoint(route, sequence, waypoint)
            }
        }
    }

    private fun handleCount(count: Int) {
        if (progressState.value.phase != MissionProgress.Phase.DOWNLOADING) return
        if (count <= 0) {
            progressState.value = MissionProgress(
                MissionProgress.Phase.COMPLETE,
                total = 0,
                completed = 0,
                message = "The motor has no stored route",
            )
            return
        }
        expectedCount = count
        scope.launch {
            var sequence = 0
            while (isActive && sequence < expectedCount) {
                sendMessage(MissionRequest(sequence = sequence))
                delay(DOWNLOAD_ITEM_POLL_MILLIS)
                sequence = requestedSequence + 1
            }
        }
    }

    private fun handleItem(item: MissionItem) {
        if (progressState.value.phase != MissionProgress.Phase.DOWNLOADING) return
        if (item.sequence != downloaded.size) return
        downloaded.add(Waypoint(item.x.toDouble(), item.y.toDouble()))
        requestedSequence = item.sequence
        progressState.value = progressState.value.copy(
            total = expectedCount,
            completed = downloaded.size,
        )
        scheduleDownloadWatchdog()
        if (expectedCount > 0 && downloaded.size >= expectedCount) {
            downloadWatchdogJob?.cancel()
            progressState.value = progressState.value.copy(
                phase = MissionProgress.Phase.COMPLETE,
                message = "Route downloaded",
            )
        }
    }

    /**
     * Re-requests the current waypoint when the motor does not answer in time.
     */
    private fun scheduleDownloadWatchdog() {
        downloadWatchdogJob?.cancel()
        downloadWatchdogJob = scope.launch {
            delay(DOWNLOAD_ITEM_TIMEOUT_MILLIS)
            if (progressState.value.phase != MissionProgress.Phase.DOWNLOADING) return@launch
            val sequence = downloaded.size
            if (sequence >= expectedCount) return@launch
            sendMessage(MissionRequest(sequence = sequence))
        }
    }

    private fun handleAcknowledgement() {
        if (progressState.value.phase != MissionProgress.Phase.UPLOADING) return
        itemRetryJob?.cancel()
        itemRetryJob = null
        progressState.value = progressState.value.copy(
            phase = MissionProgress.Phase.COMPLETE,
            message = "Route uploaded",
        )
    }

    private fun fail(message: String) {
        progressState.value = progressState.value.copy(
            phase = MissionProgress.Phase.FAILED,
            message = message,
        )
    }

    companion object {
        /** Number of times the `MISSION_COUNT` handshake is repeated. */
        const val UPLOAD_COUNT_ATTEMPTS: Int = 3

        /** Delay between two `MISSION_COUNT` attempts. */
        const val UPLOAD_RETRY_DELAY_MILLIS: Long = 3000L

        /** Delay after which an unanswered waypoint is sent again. */
        const val ITEM_RETRY_DELAY_MILLIS: Long = 3000L

        /** Interval of the `MISSION_REQUEST_LIST` poll during a download. */
        const val DOWNLOAD_LIST_POLL_MILLIS: Long = 150L

        /** Interval of the `MISSION_REQUEST` poll during a download. */
        const val DOWNLOAD_ITEM_POLL_MILLIS: Long = 500L

        /** Overall timeout of a route download. */
        const val DOWNLOAD_TIMEOUT_MILLIS: Long = 25000L

        /** Timeout for a single waypoint during a download. */
        const val DOWNLOAD_ITEM_TIMEOUT_MILLIS: Long = 10000L

        /** Target system id the type 110 motor expects in `MISSION_ITEM`. */
        const val MISSION_TARGET_SYSTEM: Int = 106
    }
}
