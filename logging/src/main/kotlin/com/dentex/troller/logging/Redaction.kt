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

    /** Reduces a coordinate to a coarse grid cell so that exact position is not retained. */
    fun coarsenLatitude(latitude: Double): Double = roundTo(latitude, 2)

    fun coarsenLongitude(longitude: Double): Double = roundTo(longitude, 2)

    private fun roundTo(value: Double, decimals: Int): Double {
        var factor = 1.0
        repeat(decimals) { factor *= 10.0 }
        return Math.round(value * factor) / factor
    }
}
