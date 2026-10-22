package com.dentex.troller.mavlink

/**
 * Message metadata for the Helmsman MAVLink dialect.
 *
 * The CRC-extra table was recovered verbatim from the `CRC.MAVLINK_MESSAGE_CRCS` array inside the
 * Helmsman APK (`com.MAVLink.helmsman.CRC`). It is a superset of the stock MAVLink common dialect
 * and differs from it in a few places, so the table must not be swapped for the generic upstream
 * values without re-verifying against the motor firmware.
 */
object MavlinkDialect {

    /** Start of frame marker for MAVLink v1. */
    const val STX: Int = 0xFE

    /** Largest payload a v1 frame can carry. */
    const val MAX_PAYLOAD_LENGTH: Int = 255

    /** Per message id CRC-extra byte, indexed by message id. */
    private val CRC_EXTRAS: IntArray = intArrayOf(
        50, 124, 137, 0, 237, 217, 104, 119, 0, 0, 0, 89, 0, 0, 0, 0, // ids 0..15
        0, 0, 0, 0, 214, 159, 220, 168, 24, 23, 170, 144, 67, 115, 39, 246, // ids 16..31
        185, 104, 237, 244, 222, 212, 9, 254, 230, 28, 28, 132, 221, 232, 11, 153, // ids 32..47
        41, 39, 78, 196, 0, 0, 15, 3, 0, 0, 0, 0, 0, 167, 183, 119, // ids 48..63
        191, 118, 148, 21, 0, 243, 124, 0, 0, 38, 20, 158, 152, 143, 0, 0, // ids 64..79
        0, 106, 49, 22, 143, 140, 5, 150, 0, 231, 183, 63, 54, 47, 0, 0, // ids 80..95
        0, 0, 0, 0, 175, 102, 158, 208, 56, 93, 138, 108, 32, 185, 84, 34, // ids 96..111
        174, 124, 237, 4, 76, 128, 56, 116, 134, 237, 203, 250, 87, 203, 220, 25, // ids 112..127
        226, 46, 29, 223, 85, 6, 229, 203, 1, 195, 109, 168, 181, 47, 72, 131, // ids 128..143
        127, 0, 103, 154, 178, 200, 0, 118, 46, 7, 115, 0, 0, 0, 0, 0, // ids 144..159
        0, 123, 119, 54, 45, 64, 64, 203, 0, 0, 0, 0, 0, 121, 40, 40, // ids 160..175
        104, 139, 238, 213, 59, 168, 159, 169, 117, 98, 157, 98, 0, 0, 0, 0, // ids 176..191
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // ids 192..207
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // ids 208..223
        0, 0, 0, 0, 0, 0, 163, 105, 151, 35, 150, 0, 0, 0, 0, 0, // ids 224..239
        0, 90, 104, 85, 95, 130, 184, 81, 8, 204, 49, 170, 44, 83, 46, 0 // ids 240..255
    )

    /** CRC-extra byte for [messageId]; unknown ids contribute zero. */
    fun crcExtra(messageId: Int): Int =
        if (messageId in CRC_EXTRAS.indices) CRC_EXTRAS[messageId] else 0

    /**
     * Number of entries in the CRC-extra table.
     *
     * The table has to cover every message id, so a truncated table is a defect that would
     * silently disable the checksum of the affected messages.
     */
    val crcExtraTableSize: Int get() = CRC_EXTRAS.size

    /** Human readable name for [messageId], or `MSG_<id>` when the id is unknown. */
    fun name(messageId: Int): String = MESSAGE_NAMES[messageId] ?: "MSG_$messageId"

    /** Known message names, indexed by message id. */
    private val MESSAGE_NAMES: Map<Int, String> = mapOf(
        0 to "HEARTBEAT",
        1 to "SYS_STATUS",
        7 to "AUTH_KEY",
        27 to "RAW_IMU",
        30 to "ATTITUDE",
        33 to "GLOBAL_POSITION_INT",
        39 to "MISSION_ITEM",
        40 to "MISSION_REQUEST",
        43 to "MISSION_REQUEST_LIST",
        44 to "MISSION_COUNT",
        45 to "MISSION_CLEAR_ALL",
        47 to "MISSION_ACK",
        73 to "MISSION_ITEM_INT",
        76 to "COMMAND_LONG",
        168 to "CONTROL_COMMAND",
        169 to "ANCHOR_DATA",
        170 to "RECORD_ROUTE_DATA",
        171 to "NAVIGATION_DATA",
        172 to "CRUISE_DATA",
        182 to "MODE_ENABLE",
        183 to "COMMAND_SHORT",
        184 to "REALTIME_DATA",
        185 to "TROLLING_MOTOR_STATUS",
        186 to "NEW_COMMAND_LONG",
        187 to "NEW_COMMAND_LONG_ACK",
        249 to "MEMORY_VECT",
    )
}
