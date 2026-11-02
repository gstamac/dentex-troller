package com.dentex.troller.logging

import java.util.Locale

/**
 * Redaction helpers for exported diagnostic logs.
 *
 * The exported log is intended for comparison against a Helmsman capture, so the
 * raw MAVLink bytes are kept. Device identifiers and coordinates are the two
 * categories that are scrubbed by default, because the log is shared over email
 * or chat and location history is not needed to compare wire formats.
 */
object Redaction {

    /** Masks the tail of a Bluetooth MAC address, keeping the OUI for correlation. */
    fun maskMac(mac: String): String {
        val parts = mac.split(':', '-', '.').filter { it.isNotBlank() }
        if (parts.isEmpty()) return "??:??:??:??:??:??"
        if (parts.size < 3) return parts.joinToString(":") { "??" }
        val head = parts.take(3).joinToString(":") { it.uppercase(Locale.US) }
        val tail = parts.drop(3).joinToString(":") { "??".take(it.length.coerceIn(2, 2)) }
        return "$head:$tail"
    }

    private val macPattern = Regex("\\b[0-9A-Fa-f]{2}(?::[0-9A-Fa-f]{2}){5}\\b")

    /** Masks every Bluetooth MAC address that appears in [text]. */
    fun maskMacs(text: String): String = macPattern.replace(text) { maskMac(it.value) }

    /**
     * Redacts a log entry for export.
     *
     * Device addresses in event messages are masked. Raw payloads are kept unchanged, because
     * they are what the export is compared against; they can still contain the motor position.
     */
    fun redactEntry(entry: LogEntry): LogEntry {
        val message = entry.message ?: return entry
        val masked = maskMacs(message)
        return if (masked == message) entry else entry.copy(message = masked)
    }

    /** Reduces a coordinate to a coarse grid cell so that exact position is not retained. */
    fun coarsenLatitude(latitude: Double): Double = roundTo(latitude, 2)

    fun coarsenLongitude(longitude: Double): Double = roundTo(longitude, 2)

    private fun roundTo(value: Double, decimals: Int): Double {
        var factor = 1.0
        repeat(decimals) { factor *= 10.0 }
        return Math.round(value * factor) / factor
    }
}
