package com.google.zxing.datamatrix.encoder;

import com.MAVLink.common.msg_att_pos_mocap;
import com.MAVLink.common.msg_collision;
import com.MAVLink.common.msg_data_transmission_handshake;
import com.MAVLink.common.msg_distance_sensor;
import com.MAVLink.common.msg_encapsulated_data;
import com.MAVLink.common.msg_gps_input;
import com.MAVLink.common.msg_gps_rtcm_data;
import com.MAVLink.common.msg_high_latency;
import com.MAVLink.common.msg_local_position_ned_cov;
import com.MAVLink.common.msg_memory_vect;
import com.MAVLink.common.msg_message_interval;
import com.MAVLink.common.msg_scaled_pressure2;
import com.MAVLink.common.msg_set_actuator_control_target;
import com.MAVLink.common.msg_statustext;
import com.MAVLink.common.msg_terrain_report;
import com.MAVLink.common.msg_terrain_request;
import com.MAVLink.common.msg_v2_extension;
import com.MAVLink.common.msg_wind_cov;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.helmsman.msg_anchor_data;
import com.MAVLink.helmsman.msg_control_command;
import com.MAVLink.helmsman.msg_cruise_data;
import com.MAVLink.helmsman.msg_firmware_info_request;
import com.MAVLink.helmsman.msg_firmware_update;
import com.MAVLink.helmsman.msg_gps_aid_ack;
import com.MAVLink.helmsman.msg_gps_aid_data;
import com.MAVLink.helmsman.msg_gps_aid_data_request;
import com.MAVLink.helmsman.msg_gps_aid_info;
import com.MAVLink.helmsman.msg_new_command_long_ack;
import com.MAVLink.helmsman.msg_protocol_att_ekf_atti;
import com.MAVLink.helmsman.msg_record_route_data;
import com.MAVLink.helmsman.msg_serial_number_request;

/* JADX INFO: loaded from: classes2.dex */
public final class ErrorCorrection {
    private static final int MODULO_VALUE = 301;
    private static final int[] FACTOR_SETS = {5, 7, 10, 11, 12, 14, 18, 20, 24, 28, 36, 42, 48, 56, 62, 68};
    private static final int[][] FACTORS = {new int[]{228, 48, 15, 111, 62}, new int[]{23, 68, 144, 134, 240, 92, 254}, new int[]{28, 24, 185, msg_protocol_att_ekf_atti.MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, msg_v2_extension.MAVLINK_MSG_ID_V2_EXTENSION, 116, 255, 110, 61}, new int[]{msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, 205, 12, 194, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, 39, 245, 60, 97, 120}, new int[]{41, 153, MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL, 91, 61, 42, 142, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, 97, 178, 100, 242}, new int[]{MAV_COMPONENT.MAV_COMP_ID_ADSB, 97, 192, 252, 95, 9, MAV_COMPONENT.MAV_COMP_ID_OSD, 119, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, 45, 18, 186, 83, 185}, new int[]{83, MAV_COMPONENT.MAV_COMP_ID_PATHPLANNER, 100, 39, 188, 75, 66, 61, 241, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, 109, 129, 94, 254, msg_local_position_ned_cov.MAVLINK_MSG_LENGTH, 48, 90, 188}, new int[]{15, MAV_COMPONENT.MAV_COMP_ID_PATHPLANNER, msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL, 9, msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA, 71, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, 2, 188, 160, 153, MAV_COMPONENT.MAV_COMP_ID_SERVO6, msg_statustext.MAVLINK_MSG_ID_STATUSTEXT, 79, 108, 82, 27, msg_firmware_update.MAVLINK_MSG_ID_FIRMWARE_UPDATE, 186, msg_cruise_data.MAVLINK_MSG_ID_CRUISE_DATA}, new int[]{52, 190, 88, 205, 109, 39, 176, 21, MAV_COMPONENT.MAV_COMP_ID_LOG, 197, 251, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, MAV_COMPONENT.MAV_COMP_ID_LOG, 21, 5, msg_cruise_data.MAVLINK_MSG_ID_CRUISE_DATA, 254, 124, 12, 181, 184, 96, 50, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE}, new int[]{211, msg_wind_cov.MAVLINK_MSG_ID_WIND_COV, 43, 97, 71, 96, 103, msg_firmware_update.MAVLINK_MSG_ID_FIRMWARE_UPDATE, 37, 151, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 53, 75, 34, msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT, 121, 17, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, 110, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, 141, msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT, 120, 151, msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, 93, 255}, new int[]{245, 127, 242, 218, msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE, 250, msg_gps_aid_data_request.MAVLINK_MSG_ID_GPS_AID_DATA_REQUEST, 181, 102, 120, 84, 179, 220, 251, 80, 182, 229, 18, 2, 4, 68, 33, 101, msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2, 95, 119, 115, 44, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, 184, 59, 25, msg_local_position_ned_cov.MAVLINK_MSG_LENGTH, 98, 81, 112}, new int[]{77, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2, 31, 19, 38, 22, 153, msg_collision.MAVLINK_MSG_ID_COLLISION, 105, 122, 2, 245, msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST, 242, 8, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, 95, 100, 9, 167, 105, 214, 111, 57, 121, 21, 1, msg_statustext.MAVLINK_MSG_ID_STATUSTEXT, 57, 54, 101, msg_v2_extension.MAVLINK_MSG_ID_V2_EXTENSION, 202, 69, 50, MAV_COMPONENT.MAV_COMP_ID_SERVO11, 177, 226, 5, 9, 5}, new int[]{245, msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR, msg_cruise_data.MAVLINK_MSG_ID_CRUISE_DATA, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, 96, 32, 117, 22, 238, msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST, 238, msg_wind_cov.MAVLINK_MSG_ID_WIND_COV, 205, 188, 237, 87, MAV_CMD.MAV_CMD_DO_GO_AROUND, 106, 16, 147, 118, 23, 37, 90, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 205, msg_encapsulated_data.MAVLINK_MSG_ID_ENCAPSULATED_DATA, 88, 120, 100, 66, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, 186, 240, 82, 44, 176, 87, msg_new_command_long_ack.MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK, 147, 160, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, 69, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, 92, msg_statustext.MAVLINK_MSG_ID_STATUSTEXT, msg_local_position_ned_cov.MAVLINK_MSG_LENGTH, 19}, new int[]{msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, 9, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, 238, 12, 17, 220, 208, 100, 29, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 230, 192, 215, 235, MAV_COMPONENT.MAV_COMP_ID_SERVO11, 159, 36, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, 38, 200, msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR, 54, 228, 146, 218, msg_high_latency.MAVLINK_MSG_ID_HIGH_LATENCY, 117, 203, 29, msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT, 144, 238, 22, MAV_COMPONENT.MAV_COMP_ID_SERVO11, 201, 117, 62, MAV_CMD.MAV_CMD_DO_FENCE_ENABLE, msg_gps_aid_ack.MAVLINK_MSG_ID_GPS_AID_ACK, 13, msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2, 245, 127, 67, msg_collision.MAVLINK_MSG_ID_COLLISION, 28, MAV_COMPONENT.MAV_COMP_ID_LOG, 43, 203, 107, msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA, 53, 143, 46}, new int[]{242, 93, msg_anchor_data.MAVLINK_MSG_ID_ANCHOR_DATA, 50, 144, MAV_CMD.MAV_CMD_DO_INVERTED_FLIGHT, 39, 118, 202, 188, 201, MAV_CMD.MAV_CMD_DO_LAND_START, 143, 108, 196, 37, 185, 112, 134, 230, 245, 63, 197, 190, 250, 106, 185, 221, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, 64, 114, 71, msg_gps_aid_info.MAVLINK_MSG_ID_GPS_AID_INFO, 44, 147, 6, 27, 218, 51, 63, 87, 10, 40, msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE, 188, 17, msg_gps_aid_data.MAVLINK_MSG_ID_GPS_AID_DATA, 31, 176, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 4, 107, msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT, 7, 94, msg_protocol_att_ekf_atti.MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI, 224, 124, 86, 47, 11, 204}, new int[]{220, 228, msg_firmware_info_request.MAVLINK_MSG_ID_FIRMWARE_INFO_REQUEST, 89, 251, 149, 159, 56, 89, 33, 147, msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL, 154, 36, 73, 127, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT, msg_v2_extension.MAVLINK_MSG_ID_V2_EXTENSION, 180, msg_high_latency.MAVLINK_MSG_ID_HIGH_LATENCY, 197, MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL, 177, 68, 122, 93, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, 15, 160, 227, 236, 66, msg_set_actuator_control_target.MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET, 153, 185, 202, 167, 179, 25, 220, msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT, 96, MAV_CMD.MAV_CMD_DO_INVERTED_FLIGHT, msg_wind_cov.MAVLINK_MSG_ID_WIND_COV, msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, 239, 181, 241, 59, 52, msg_cruise_data.MAVLINK_MSG_ID_CRUISE_DATA, 25, 49, msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT, 211, MAV_CMD.MAV_CMD_DO_LAND_START, 64, 54, 108, 153, msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR, 63, 96, 103, 82, 186}};
    private static final int[] LOG = new int[256];
    private static final int[] ALOG = new int[255];

    static {
        int i = 1;
        for (int i2 = 0; i2 < 255; i2++) {
            ALOG[i2] = i;
            LOG[i] = i2;
            i <<= 1;
            if (i >= 256) {
                i ^= 301;
            }
        }
    }

    private ErrorCorrection() {
    }

    public static String encodeECC200(String str, SymbolInfo symbolInfo) {
        if (str.length() != symbolInfo.getDataCapacity()) {
            throw new IllegalArgumentException("The number of codewords does not match the selected symbol");
        }
        StringBuilder sb = new StringBuilder(symbolInfo.getDataCapacity() + symbolInfo.getErrorCodewords());
        sb.append(str);
        int interleavedBlockCount = symbolInfo.getInterleavedBlockCount();
        if (interleavedBlockCount == 1) {
            sb.append(createECCBlock(str, symbolInfo.getErrorCodewords()));
        } else {
            sb.setLength(sb.capacity());
            int[] iArr = new int[interleavedBlockCount];
            int[] iArr2 = new int[interleavedBlockCount];
            int[] iArr3 = new int[interleavedBlockCount];
            int i = 0;
            while (i < interleavedBlockCount) {
                int i2 = i + 1;
                iArr[i] = symbolInfo.getDataLengthForInterleavedBlock(i2);
                iArr2[i] = symbolInfo.getErrorLengthForInterleavedBlock(i2);
                iArr3[i] = 0;
                if (i > 0) {
                    iArr3[i] = iArr3[i - 1] + iArr[i];
                }
                i = i2;
            }
            for (int i3 = 0; i3 < interleavedBlockCount; i3++) {
                StringBuilder sb2 = new StringBuilder(iArr[i3]);
                for (int i4 = i3; i4 < symbolInfo.getDataCapacity(); i4 += interleavedBlockCount) {
                    sb2.append(str.charAt(i4));
                }
                String strCreateECCBlock = createECCBlock(sb2.toString(), iArr2[i3]);
                int i5 = i3;
                int i6 = 0;
                while (i5 < iArr2[i3] * interleavedBlockCount) {
                    sb.setCharAt(symbolInfo.getDataCapacity() + i5, strCreateECCBlock.charAt(i6));
                    i5 += interleavedBlockCount;
                    i6++;
                }
            }
        }
        return sb.toString();
    }

    private static String createECCBlock(CharSequence charSequence, int i) {
        return createECCBlock(charSequence, 0, charSequence.length(), i);
    }

    private static String createECCBlock(CharSequence charSequence, int i, int i2, int i3) {
        int i4;
        int i5;
        int i6 = 0;
        while (true) {
            int[] iArr = FACTOR_SETS;
            if (i6 >= iArr.length) {
                i6 = -1;
                break;
            }
            if (iArr[i6] == i3) {
                break;
            }
            i6++;
        }
        if (i6 < 0) {
            throw new IllegalArgumentException("Illegal number of error correction codewords specified: " + i3);
        }
        int[] iArr2 = FACTORS[i6];
        char[] cArr = new char[i3];
        for (int i7 = 0; i7 < i3; i7++) {
            cArr[i7] = 0;
        }
        for (int i8 = i; i8 < i + i2; i8++) {
            int i9 = i3 - 1;
            int iCharAt = cArr[i9] ^ charSequence.charAt(i8);
            while (i9 > 0) {
                if (iCharAt != 0 && (i5 = iArr2[i9]) != 0) {
                    char c = cArr[i9 - 1];
                    int[] iArr3 = ALOG;
                    int[] iArr4 = LOG;
                    cArr[i9] = (char) (iArr3[(iArr4[iCharAt] + iArr4[i5]) % 255] ^ c);
                } else {
                    cArr[i9] = cArr[i9 - 1];
                }
                i9--;
            }
            if (iCharAt != 0 && (i4 = iArr2[0]) != 0) {
                int[] iArr5 = ALOG;
                int[] iArr6 = LOG;
                cArr[0] = (char) iArr5[(iArr6[iCharAt] + iArr6[i4]) % 255];
            } else {
                cArr[0] = 0;
            }
        }
        char[] cArr2 = new char[i3];
        for (int i10 = 0; i10 < i3; i10++) {
            cArr2[i10] = cArr[(i3 - i10) - 1];
        }
        return String.valueOf(cArr2);
    }
}
