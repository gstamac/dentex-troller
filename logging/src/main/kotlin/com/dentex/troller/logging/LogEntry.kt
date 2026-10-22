package com.dentex.troller.logging

/**
 * A single timestamped record in the diagnostic log.
 *
 * [payload] carries raw bytes for BLE traffic; [message] carries human readable
 * text for connection lifecycle and decoding events.
 */
data class LogEntry(
    val timestampMillis: Long,
    val direction: Direction,
    val label: String,
    val payload: ByteArray? = null,
    val message: String? = null,
) {
    enum class Direction { RX, TX, EVENT }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogEntry) return false
        return timestampMillis == other.timestampMillis &&
            direction == other.direction &&
            label == other.label &&
            payload.contentEqualsOrNull(other.payload) &&
            message == other.message
    }

    override fun hashCode(): Int {
        var result = timestampMillis.hashCode()
        result = 31 * result + direction.hashCode()
        result = 31 * result + label.hashCode()
        result = 31 * result + (payload?.contentHashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = buildString {
        append('[').append(direction.name).append("] ")
        append(label)
        if (message != null) append(": ").append(message)
        if (payload != null) {
            append(' ')
            append(HexDump.toHex(payload))
        }
    }

    private fun ByteArray?.contentEqualsOrNull(other: ByteArray?): Boolean = when {
        this == null && other == null -> true
        this == null || other == null -> false
        else -> contentEquals(other)
    }
}
