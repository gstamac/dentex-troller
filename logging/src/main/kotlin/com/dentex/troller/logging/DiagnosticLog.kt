package com.dentex.troller.logging

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicLong

/**
 * Thread safe, bounded diagnostic log.
 *
 * The log keeps the most recent [maxEntries] records so that a long session cannot
 * exhaust memory. Every mutation is observable through [entries] so the UI can
 * render a live tail of the traffic.
 */
class DiagnosticLog(private val maxEntries: Int = DEFAULT_MAX_ENTRIES) {

    private val lock = Any()
    private val buffer = ArrayDeque<LogEntry>()
    private val sequence = AtomicLong(0)
    private val revision = MutableStateFlow(0L)

    /** Monotonic counter of records ever appended, including evicted ones. */
    val appendedCount: Long get() = sequence.get()

    /**
     * Counter that is increased on every mutation.
     *
     * Collecting this flow gives the UI a cheap way to know that it has to re-read [entries].
     */
    val changes: StateFlow<Long> = revision.asStateFlow()

    /** Snapshot of the retained entries, oldest first. */
    val entries: List<LogEntry>
        get() = synchronized(lock) { buffer.toList() }

    fun append(entry: LogEntry) {
        synchronized(lock) {
            buffer.addLast(entry)
            while (buffer.size > maxEntries) {
                buffer.removeFirst()
            }
        }
        sequence.incrementAndGet()
        revision.value = sequence.get()
    }

    fun event(label: String, message: String, timestampMillis: Long = System.currentTimeMillis()) {
        append(LogEntry(timestampMillis, LogEntry.Direction.EVENT, label, message = message))
    }

    fun rx(label: String, payload: ByteArray, timestampMillis: Long = System.currentTimeMillis()) {
        append(LogEntry(timestampMillis, LogEntry.Direction.RX, label, payload = payload.copyOf()))
    }

    fun tx(label: String, payload: ByteArray, timestampMillis: Long = System.currentTimeMillis()) {
        append(LogEntry(timestampMillis, LogEntry.Direction.TX, label, payload = payload.copyOf()))
    }

    fun clear() {
        synchronized(lock) { buffer.clear() }
        revision.value = sequence.get()
    }

    /** Renders the retained entries as a plain text report. */
    fun exportText(redactor: (LogEntry) -> LogEntry = { it }): String = buildString {
        append("# Dentex Troller diagnostic log\n")
        append("# entries: ").append(entries.size).append(" of ").append(appendedCount).append('\n')
        append("# generated: ").append(java.time.Instant.now()).append('\n')
        append('\n')
        for (entry in entries) {
            val redacted = redactor(entry)
            append(java.time.Instant.ofEpochMilli(redacted.timestampMillis)).append(' ')
            append(redacted.direction.name).append(' ')
            append(redacted.label)
            if (redacted.message != null) append(" | ").append(redacted.message)
            if (redacted.payload != null) {
                append(" | ")
                append(HexDump.toSpacedHex(redacted.payload))
            }
            append('\n')
        }
    }

    companion object {
        const val DEFAULT_MAX_ENTRIES = 2000
    }
}
