package com.dentex.troller.mavlink

/**
 * X.25 checksum used by MAVLink v1.
 *
 * This is a byte-for-byte port of `com.MAVLink.helmsman.CRC` from the Helmsman APK. There is no
 * final XOR and the accumulator is never masked to 16 bits explicitly; the arithmetic keeps it
 * inside 16 bits on its own.
 */
class MavlinkCrc {

    private var crcValue: Int = CRC_INIT

    /** Resets the accumulator to its initial value. */
    fun start() {
        crcValue = CRC_INIT
    }

    /** Folds a single byte into the accumulator. */
    fun update(value: Int) {
        val current = crcValue
        val mixed = (value and 0xFF) xor (current and 0xFF)
        val shifted = mixed xor ((mixed shl 4) and 0xFF)
        crcValue = ((shifted shr 4) and 0x0F) xor
            (((current shr 8) and 0xFF) xor (shifted shl 8) xor (shifted shl 3))
    }

    /** Low byte of the checksum; this is the byte written first on the wire. */
    val lsb: Int get() = crcValue and 0xFF

    /** High byte of the checksum; this is the byte written second on the wire. */
    val msb: Int get() = (crcValue shr 8) and 0xFF

    companion object {
        /** Initial accumulator value. */
        const val CRC_INIT: Int = 0xFFFF
    }
}
