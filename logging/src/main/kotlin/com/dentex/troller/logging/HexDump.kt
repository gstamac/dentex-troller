package com.dentex.troller.logging

/** Hex helpers used by the diagnostic log and the raw traffic viewer. */
object HexDump {

    private const val HEX_DIGITS = "0123456789ABCDEF"

    /** Renders [bytes] as contiguous uppercase hex without separators. */
    fun toHex(bytes: ByteArray): String {
        val out = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            val v = b.toInt() and 0xFF
            out.append(HEX_DIGITS[v ushr 4])
            out.append(HEX_DIGITS[v and 0x0F])
        }
        return out.toString()
    }

    /** Renders [bytes] as space separated hex pairs. */
    fun toSpacedHex(bytes: ByteArray): String {
        if (bytes.isEmpty()) return ""
        val out = StringBuilder(bytes.size * 3)
        for ((index, b) in bytes.withIndex()) {
            if (index > 0) out.append(' ')
            val v = b.toInt() and 0xFF
            out.append(HEX_DIGITS[v ushr 4])
            out.append(HEX_DIGITS[v and 0x0F])
        }
        return out.toString()
    }

    /** Classic 16 byte per line hex dump with an ASCII gutter. */
    fun toAsciiDump(bytes: ByteArray, bytesPerLine: Int = 16): String {
        if (bytes.isEmpty()) return ""
        val out = StringBuilder()
        var offset = 0
        while (offset < bytes.size) {
            val end = minOf(offset + bytesPerLine, bytes.size)
            out.append(String.format("%04X", offset)).append("  ")
            for (i in offset until end) {
                val v = bytes[i].toInt() and 0xFF
                out.append(HEX_DIGITS[v ushr 4]).append(HEX_DIGITS[v and 0x0F]).append(' ')
                if (i == offset + bytesPerLine / 2 - 1) out.append(' ')
            }
            repeat(bytesPerLine - (end - offset)) { out.append("   ") }
            out.append(' ')
            for (i in offset until end) {
                val c = bytes[i].toInt() and 0xFF
                out.append(if (c in 0x20..0x7E) c.toChar() else '.')
            }
            out.append('\n')
            offset = end
        }
        return out.toString()
    }

    /** Parses a hex string (whitespace and `:` separators tolerated) into bytes. */
    fun parseHex(text: String): ByteArray {
        val cleaned = text.filterNot { it.isWhitespace() || it == ':' || it == '-' }
        require(cleaned.length % 2 == 0) { "Hex string must contain an even number of digits" }
        val out = ByteArray(cleaned.length / 2)
        for (i in out.indices) {
            val hi = Character.digit(cleaned[i * 2], 16)
            val lo = Character.digit(cleaned[i * 2 + 1], 16)
            require(hi >= 0 && lo >= 0) { "Invalid hex digit at index ${i * 2}" }
            out[i] = ((hi shl 4) or lo).toByte()
        }
        return out
    }
}
