package com.MAVLink.common;

import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.helmsman.msg_control_command;
import com.MAVLink.helmsman.msg_firmware_update;
import com.MAVLink.helmsman.msg_gps_aid_data;
import com.MAVLink.helmsman.msg_record_route_data;
import com.MAVLink.helmsman.msg_serial_number_request;

/* JADX INFO: loaded from: classes.dex */
public class CRC {
    private static final int CRC_INIT_VALUE = 65535;
    private static final int[] MAVLINK_MESSAGE_CRCS = {50, 124, msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2, 0, 237, 217, 104, 119, 0, 0, 0, 89, 0, 0, 0, 0, 0, 0, 0, 0, 214, 159, 220, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, 24, 23, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 144, 67, 115, 39, 246, 185, 104, 237, msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL, MAV_CMD.MAV_CMD_DO_GUIDED_LIMITS, 212, 9, 254, 230, 28, 28, msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR, 221, msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT, 11, 153, 41, 39, 78, 196, 0, 0, 15, 3, 0, 0, 0, 0, 0, 167, 183, 119, MAV_CMD.MAV_CMD_DO_GO_AROUND, 118, 148, 21, 0, 243, 124, 0, 0, 38, 20, MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL, 152, 143, 0, 0, 0, 106, 49, 22, 143, 140, 5, MAV_COMPONENT.MAV_COMP_ID_SERVO11, 0, msg_wind_cov.MAVLINK_MSG_ID_WIND_COV, 183, 63, 54, 47, 0, 0, 0, 0, 0, 0, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, 102, MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL, 208, 56, 93, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, 108, 32, 185, 84, 34, msg_firmware_update.MAVLINK_MSG_ID_FIRMWARE_UPDATE, 124, 237, 4, 76, 128, 56, 116, 134, 237, 203, 250, 87, 203, 220, 25, 226, 46, 29, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, 85, 6, 229, 203, 1, MAV_COMPONENT.MAV_COMP_ID_PATHPLANNER, 109, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, 181, 47, 72, msg_encapsulated_data.MAVLINK_MSG_ID_ENCAPSULATED_DATA, 127, 0, 103, 154, 178, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, msg_gps_aid_data.MAVLINK_MSG_ID_GPS_AID_DATA, 105, 151, 35, MAV_COMPONENT.MAV_COMP_ID_SERVO11, 0, 0, 0, 0, 0, 0, 90, 104, 85, 95, msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE, 184, 81, 8, 204, 49, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 44, 83, 46, 0};
    private int crcValue;

    public void update_checksum(int i) {
        int i2 = this.crcValue;
        int i3 = (i & 255) ^ (i2 & 255);
        int i4 = i3 ^ ((i3 << 4) & 255);
        this.crcValue = ((i4 >> 4) & 15) ^ ((((i2 >> 8) & 255) ^ (i4 << 8)) ^ (i4 << 3));
    }

    public void finish_checksum(int i) {
        update_checksum(MAVLINK_MESSAGE_CRCS[i]);
    }

    public void start_checksum() {
        this.crcValue = 65535;
    }

    public int getMSB() {
        return (this.crcValue >> 8) & 255;
    }

    public int getLSB() {
        return this.crcValue & 255;
    }

    public CRC() {
        start_checksum();
    }
}
