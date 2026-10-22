package com.dentex.troller.mavlink

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Guards the CRC-extra table.
 *
 * The values were recovered from the Helmsman APK and are not the same as the stock MAVLink
 * common dialect in every position, so they must not be regenerated from an upstream generator
 * without re-verifying against the motor firmware.
 */
class MavlinkDialectTest {

    @Test
    fun crcExtrasMatchTheHelmsmanDialect() {
        val expected = mapOf(
            0 to 50,      // HEARTBEAT
            1 to 124,     // SYS_STATUS
            7 to 119,     // AUTH_KEY
            27 to 144,    // RAW_IMU
            30 to 39,     // ATTITUDE
            33 to 104,    // GLOBAL_POSITION_INT
            39 to 254,    // MISSION_ITEM
            40 to 230,    // MISSION_REQUEST
            43 to 132,    // MISSION_REQUEST_LIST
            44 to 221,    // MISSION_COUNT
            45 to 232,    // MISSION_CLEAR_ALL
            47 to 153,    // MISSION_ACK
            73 to 38,     // MISSION_ITEM_INT
            76 to 152,    // COMMAND_LONG
            168 to 0,     // CONTROL_COMMAND
            182 to 159,   // MODE_ENABLE
            183 to 169,   // COMMAND_SHORT
            184 to 117,   // REALTIME_DATA
            185 to 98,    // TROLLING_MOTOR_STATUS
            186 to 157,   // NEW_COMMAND_LONG
            187 to 98,    // NEW_COMMAND_LONG_ACK
            249 to 204,   // MEMORY_VECT
        )
        for ((messageId, crcExtra) in expected) {
            assertEquals(
                "CRC extra of message $messageId",
                crcExtra,
                MavlinkDialect.crcExtra(messageId),
            )
        }
    }

    @Test
    fun unknownMessageIdsContributeZero() {
        assertEquals(0, MavlinkDialect.crcExtra(200))
        assertEquals(0, MavlinkDialect.crcExtra(1000))
    }

    @Test
    fun theTableCoversEveryMessageId() {
        assertEquals(256, MavlinkDialect.crcExtraTableSize)
    }

    @Test
    fun messageNamesAreResolved() {
        assertEquals("HEARTBEAT", MavlinkDialect.name(0))
        assertEquals("COMMAND_LONG", MavlinkDialect.name(76))
        assertEquals("MSG_199", MavlinkDialect.name(199))
    }
}
