package com.dentex.troller.mavlink

/**
 * A MAVLink message that this application can encode and decode.
 *
 * Every message knows its id and its fixed payload length, and can serialise itself into the
 * payload of a v1 frame. Payload field layouts follow the Helmsman dialect exactly.
 */
sealed interface MavlinkMessage {

    /** MAVLink message id. */
    val messageId: Int

    /** Fixed payload length in bytes. */
    val payloadLength: Int

    /** Serialises the payload of this message. */
    fun pack(): ByteArray

    companion object {

        /**
         * Decodes [frame] into a typed message.
         *
         * Returns `null` for message ids that this application does not model, so that unknown
         * traffic can still be counted and logged.
         */
        fun decode(frame: MavlinkFrame): MavlinkMessage? {
            val reader = PayloadReader(frame.payload)
            return when (frame.messageId) {
                Heartbeat.MESSAGE_ID -> Heartbeat.decode(reader)
                SysStatus.MESSAGE_ID -> SysStatus.decode(reader)
                RawImu.MESSAGE_ID -> RawImu.decode(reader)
                Attitude.MESSAGE_ID -> Attitude.decode(reader)
                GlobalPositionInt.MESSAGE_ID -> GlobalPositionInt.decode(reader)
                MissionItem.MESSAGE_ID -> MissionItem.decode(reader)
                MissionRequest.MESSAGE_ID -> MissionRequest.decode(reader)
                MissionRequestList.MESSAGE_ID -> MissionRequestList.decode(reader)
                MissionCount.MESSAGE_ID -> MissionCount.decode(reader)
                MissionClearAll.MESSAGE_ID -> MissionClearAll.decode(reader)
                MissionAck.MESSAGE_ID -> MissionAck.decode(reader)
                CommandLong.MESSAGE_ID -> CommandLong.decode(reader)
                ControlCommand.MESSAGE_ID -> ControlCommand.decode(reader)
                ModeEnable.MESSAGE_ID -> ModeEnable.decode(reader)
                CommandShort.MESSAGE_ID -> CommandShort.decode(reader)
                RealtimeData.MESSAGE_ID -> RealtimeData.decode(reader)
                TrollingMotorStatus.MESSAGE_ID -> TrollingMotorStatus.decode(reader)
                NewCommandLong.MESSAGE_ID -> NewCommandLong.decode(reader)
                NewCommandLongAck.MESSAGE_ID -> NewCommandLongAck.decode(reader)
                else -> null
            }
        }
    }
}

/**
 * Header of a MAVLink v1 frame.
 *
 * The Helmsman application hardcodes `sysid = 255` and `compid = 190` in every generated message,
 * with a single exception: the mission download request uses `sysid = 106`, `compid = 200`.
 */
data class MavlinkHeader(
    val sequence: Int,
    val systemId: Int = DEFAULT_SYSTEM_ID,
    val componentId: Int = DEFAULT_COMPONENT_ID,
) {
    companion object {
        /** System id used by the Helmsman application for all generated messages. */
        const val DEFAULT_SYSTEM_ID: Int = 255

        /** Component id used by the Helmsman application for all generated messages. */
        const val DEFAULT_COMPONENT_ID: Int = 190

        /** System id the motor expects for mission traffic. */
        const val MISSION_SYSTEM_ID: Int = 106

        /** Component id the motor expects for mission traffic. */
        const val MISSION_COMPONENT_ID: Int = 200
    }
}
