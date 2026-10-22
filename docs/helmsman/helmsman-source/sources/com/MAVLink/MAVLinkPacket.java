package com.MAVLink;

import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
import com.MAVLink.common.msg_actuator_control_target;
import com.MAVLink.common.msg_adsb_vehicle;
import com.MAVLink.common.msg_altitude;
import com.MAVLink.common.msg_att_pos_mocap;
import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_attitude_quaternion;
import com.MAVLink.common.msg_attitude_quaternion_cov;
import com.MAVLink.common.msg_attitude_target;
import com.MAVLink.common.msg_auth_key;
import com.MAVLink.common.msg_autopilot_version;
import com.MAVLink.common.msg_battery_status;
import com.MAVLink.common.msg_camera_trigger;
import com.MAVLink.common.msg_change_operator_control;
import com.MAVLink.common.msg_change_operator_control_ack;
import com.MAVLink.common.msg_collision;
import com.MAVLink.common.msg_command_ack;
import com.MAVLink.common.msg_command_int;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_control_system_state;
import com.MAVLink.common.msg_data_stream;
import com.MAVLink.common.msg_data_transmission_handshake;
import com.MAVLink.common.msg_debug;
import com.MAVLink.common.msg_debug_vect;
import com.MAVLink.common.msg_distance_sensor;
import com.MAVLink.common.msg_encapsulated_data;
import com.MAVLink.common.msg_estimator_status;
import com.MAVLink.common.msg_extended_sys_state;
import com.MAVLink.common.msg_file_transfer_protocol;
import com.MAVLink.common.msg_follow_target;
import com.MAVLink.common.msg_global_position_int;
import com.MAVLink.common.msg_global_position_int_cov;
import com.MAVLink.common.msg_global_vision_position_estimate;
import com.MAVLink.common.msg_gps2_raw;
import com.MAVLink.common.msg_gps2_rtk;
import com.MAVLink.common.msg_gps_global_origin;
import com.MAVLink.common.msg_gps_inject_data;
import com.MAVLink.common.msg_gps_input;
import com.MAVLink.common.msg_gps_raw_int;
import com.MAVLink.common.msg_gps_rtcm_data;
import com.MAVLink.common.msg_gps_rtk;
import com.MAVLink.common.msg_gps_status;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_high_latency;
import com.MAVLink.common.msg_highres_imu;
import com.MAVLink.common.msg_hil_actuator_controls;
import com.MAVLink.common.msg_hil_controls;
import com.MAVLink.common.msg_hil_gps;
import com.MAVLink.common.msg_hil_optical_flow;
import com.MAVLink.common.msg_hil_rc_inputs_raw;
import com.MAVLink.common.msg_hil_sensor;
import com.MAVLink.common.msg_hil_state;
import com.MAVLink.common.msg_hil_state_quaternion;
import com.MAVLink.common.msg_home_position;
import com.MAVLink.common.msg_landing_target;
import com.MAVLink.common.msg_local_position_ned;
import com.MAVLink.common.msg_local_position_ned_cov;
import com.MAVLink.common.msg_local_position_ned_system_global_offset;
import com.MAVLink.common.msg_log_data;
import com.MAVLink.common.msg_log_entry;
import com.MAVLink.common.msg_log_erase;
import com.MAVLink.common.msg_log_request_data;
import com.MAVLink.common.msg_log_request_end;
import com.MAVLink.common.msg_log_request_list;
import com.MAVLink.common.msg_manual_control;
import com.MAVLink.common.msg_manual_setpoint;
import com.MAVLink.common.msg_memory_vect;
import com.MAVLink.common.msg_message_interval;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_clear_all;
import com.MAVLink.common.msg_mission_count;
import com.MAVLink.common.msg_mission_current;
import com.MAVLink.common.msg_mission_item;
import com.MAVLink.common.msg_mission_item_int;
import com.MAVLink.common.msg_mission_item_reached;
import com.MAVLink.common.msg_mission_request;
import com.MAVLink.common.msg_mission_request_int;
import com.MAVLink.common.msg_mission_request_list;
import com.MAVLink.common.msg_mission_request_partial_list;
import com.MAVLink.common.msg_mission_set_current;
import com.MAVLink.common.msg_mission_write_partial_list;
import com.MAVLink.common.msg_named_value_float;
import com.MAVLink.common.msg_named_value_int;
import com.MAVLink.common.msg_nav_controller_output;
import com.MAVLink.common.msg_optical_flow;
import com.MAVLink.common.msg_optical_flow_rad;
import com.MAVLink.common.msg_param_map_rc;
import com.MAVLink.common.msg_param_request_list;
import com.MAVLink.common.msg_param_request_read;
import com.MAVLink.common.msg_param_set;
import com.MAVLink.common.msg_param_value;
import com.MAVLink.common.msg_ping;
import com.MAVLink.common.msg_position_target_global_int;
import com.MAVLink.common.msg_position_target_local_ned;
import com.MAVLink.common.msg_power_status;
import com.MAVLink.common.msg_radio_status;
import com.MAVLink.common.msg_raw_imu;
import com.MAVLink.common.msg_raw_pressure;
import com.MAVLink.common.msg_rc_channels;
import com.MAVLink.common.msg_rc_channels_override;
import com.MAVLink.common.msg_rc_channels_raw;
import com.MAVLink.common.msg_rc_channels_scaled;
import com.MAVLink.common.msg_request_data_stream;
import com.MAVLink.common.msg_resource_request;
import com.MAVLink.common.msg_safety_allowed_area;
import com.MAVLink.common.msg_safety_set_allowed_area;
import com.MAVLink.common.msg_scaled_imu;
import com.MAVLink.common.msg_scaled_imu2;
import com.MAVLink.common.msg_scaled_imu3;
import com.MAVLink.common.msg_scaled_pressure;
import com.MAVLink.common.msg_scaled_pressure2;
import com.MAVLink.common.msg_scaled_pressure3;
import com.MAVLink.common.msg_serial_control;
import com.MAVLink.common.msg_servo_output_raw;
import com.MAVLink.common.msg_set_actuator_control_target;
import com.MAVLink.common.msg_set_attitude_target;
import com.MAVLink.common.msg_set_gps_global_origin;
import com.MAVLink.common.msg_set_home_position;
import com.MAVLink.common.msg_set_mode;
import com.MAVLink.common.msg_set_position_target_global_int;
import com.MAVLink.common.msg_set_position_target_local_ned;
import com.MAVLink.common.msg_sim_state;
import com.MAVLink.common.msg_statustext;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.common.msg_system_time;
import com.MAVLink.common.msg_terrain_check;
import com.MAVLink.common.msg_terrain_data;
import com.MAVLink.common.msg_terrain_report;
import com.MAVLink.common.msg_terrain_request;
import com.MAVLink.common.msg_timesync;
import com.MAVLink.common.msg_v2_extension;
import com.MAVLink.common.msg_vfr_hud;
import com.MAVLink.common.msg_vibration;
import com.MAVLink.common.msg_vicon_position_estimate;
import com.MAVLink.common.msg_vision_position_estimate;
import com.MAVLink.common.msg_vision_speed_estimate;
import com.MAVLink.common.msg_wind_cov;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.enums.MAV_MODE;
import com.MAVLink.helmsman.CRC;
import com.MAVLink.helmsman.msg_anchor_data;
import com.MAVLink.helmsman.msg_command_short;
import com.MAVLink.helmsman.msg_control_command;
import com.MAVLink.helmsman.msg_cruise_data;
import com.MAVLink.helmsman.msg_device_activated_ack;
import com.MAVLink.helmsman.msg_device_activation;
import com.MAVLink.helmsman.msg_device_activation_request;
import com.MAVLink.helmsman.msg_firmware_data;
import com.MAVLink.helmsman.msg_firmware_data_request;
import com.MAVLink.helmsman.msg_firmware_info;
import com.MAVLink.helmsman.msg_firmware_info_request;
import com.MAVLink.helmsman.msg_firmware_update;
import com.MAVLink.helmsman.msg_firmware_update_ack;
import com.MAVLink.helmsman.msg_gps_aid_ack;
import com.MAVLink.helmsman.msg_gps_aid_data;
import com.MAVLink.helmsman.msg_gps_aid_data_request;
import com.MAVLink.helmsman.msg_gps_aid_info;
import com.MAVLink.helmsman.msg_gps_module_info;
import com.MAVLink.helmsman.msg_gps_module_info_request;
import com.MAVLink.helmsman.msg_mode_enable;
import com.MAVLink.helmsman.msg_navigation_data;
import com.MAVLink.helmsman.msg_new_command_long;
import com.MAVLink.helmsman.msg_new_command_long_ack;
import com.MAVLink.helmsman.msg_protocol_att_ekf_atti;
import com.MAVLink.helmsman.msg_protocol_att_ekf_states;
import com.MAVLink.helmsman.msg_realtime_data;
import com.MAVLink.helmsman.msg_record_route_data;
import com.MAVLink.helmsman.msg_serial_number;
import com.MAVLink.helmsman.msg_serial_number_request;
import com.MAVLink.helmsman.msg_trolling_motor_status;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class MAVLinkPacket implements Serializable {
    public static final int MAVLINK_STX = 254;
    private static final long serialVersionUID = 2095947771227815314L;
    public int compid;
    public CRC crc;
    public final int len;
    public int msgid;
    public MAVLinkPayload payload;
    public int seq;
    public int sysid;

    public MAVLinkPacket(int i) {
        this.len = i;
        this.payload = new MAVLinkPayload(i);
    }

    public boolean payloadIsFilled() {
        return this.payload.size() >= this.len;
    }

    public void generateCRC() {
        CRC crc = this.crc;
        if (crc == null) {
            this.crc = new CRC();
        } else {
            crc.start_checksum();
        }
        this.crc.update_checksum(this.len);
        this.crc.update_checksum(this.seq);
        this.crc.update_checksum(this.sysid);
        this.crc.update_checksum(this.compid);
        this.crc.update_checksum(this.msgid);
        this.payload.resetIndex();
        int size = this.payload.size();
        for (int i = 0; i < size; i++) {
            this.crc.update_checksum(this.payload.getByte());
        }
        this.crc.finish_checksum(this.msgid);
    }

    public byte[] encodePacket() {
        int i = this.len;
        byte[] bArr = new byte[i + 6 + 2];
        int i2 = 0;
        bArr[0] = -2;
        bArr[1] = (byte) i;
        bArr[2] = (byte) this.seq;
        bArr[3] = (byte) this.sysid;
        bArr[4] = (byte) this.compid;
        bArr[5] = (byte) this.msgid;
        int size = this.payload.size();
        int i3 = 6;
        while (i2 < size) {
            bArr[i3] = this.payload.payload.get(i2);
            i2++;
            i3++;
        }
        generateCRC();
        bArr[i3] = (byte) this.crc.getLSB();
        bArr[i3 + 1] = (byte) this.crc.getMSB();
        return bArr;
    }

    public MAVLinkMessage unpack() {
        switch (this.msgid) {
            case 0:
                return new msg_heartbeat(this);
            case 1:
                return new msg_sys_status(this);
            case 2:
                return new msg_system_time(this);
            case 3:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 52:
            case 53:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 68:
            case 71:
            case 72:
            case 78:
            case 79:
            case 80:
            case 88:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case MAV_COMPONENT.MAV_COMP_ID_SERVO6 /* 145 */:
            case MAV_COMPONENT.MAV_COMP_ID_SERVO11 /* 150 */:
            case MAV_COMPONENT.MAV_COMP_ID_LOG /* 155 */:
            case MAV_COMPONENT.MAV_COMP_ID_ADSB /* 156 */:
            case MAV_COMPONENT.MAV_COMP_ID_OSD /* 157 */:
            case MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL /* 158 */:
            case 159:
            case 160:
            case 167:
            case msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND /* 168 */:
            case msg_anchor_data.MAVLINK_MSG_ID_ANCHOR_DATA /* 169 */:
            case msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA /* 170 */:
            case msg_navigation_data.MAVLINK_MSG_ID_NAVIGATION_DATA /* 171 */:
            case msg_cruise_data.MAVLINK_MSG_ID_CRUISE_DATA /* 172 */:
            case 188:
            case MAV_CMD.MAV_CMD_DO_LAND_START /* 189 */:
            case 190:
            case MAV_CMD.MAV_CMD_DO_GO_AROUND /* 191 */:
            case 192:
            case MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE /* 193 */:
            case 194:
            case MAV_COMPONENT.MAV_COMP_ID_PATHPLANNER /* 195 */:
            case 196:
            case 197:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case MAV_CMD.MAV_CMD_DO_FENCE_ENABLE /* 207 */:
            case 208:
            case MAV_CMD.MAV_CMD_DO_MOTOR_TEST /* 209 */:
            case MAV_CMD.MAV_CMD_DO_INVERTED_FLIGHT /* 210 */:
            case 211:
            case 212:
            case MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED /* 213 */:
            case 214:
            case 215:
            case MAV_MODE.MAV_MODE_GUIDED_ARMED /* 216 */:
            case 217:
            case 218:
            case 219:
            case 220:
            case 221:
            case MAV_CMD.MAV_CMD_DO_GUIDED_LIMITS /* 222 */:
            case MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL /* 223 */:
            case 224:
            case msg_local_position_ned_cov.MAVLINK_MSG_LENGTH /* 225 */:
            case 226:
            case 227:
            case 228:
            case 229:
            case 235:
            case 236:
            case 237:
            case 238:
            case 239:
            case 240:
            default:
                return null;
            case 4:
                return new msg_ping(this);
            case 5:
                return new msg_change_operator_control(this);
            case 6:
                return new msg_change_operator_control_ack(this);
            case 7:
                return new msg_auth_key(this);
            case 11:
                return new msg_set_mode(this);
            case 20:
                return new msg_param_request_read(this);
            case 21:
                return new msg_param_request_list(this);
            case 22:
                return new msg_param_value(this);
            case 23:
                return new msg_param_set(this);
            case 24:
                return new msg_gps_raw_int(this);
            case 25:
                return new msg_gps_status(this);
            case 26:
                return new msg_scaled_imu(this);
            case 27:
                return new msg_raw_imu(this);
            case 28:
                return new msg_raw_pressure(this);
            case 29:
                return new msg_scaled_pressure(this);
            case 30:
                return new msg_attitude(this);
            case 31:
                return new msg_attitude_quaternion(this);
            case 32:
                return new msg_local_position_ned(this);
            case 33:
                return new msg_global_position_int(this);
            case 34:
                return new msg_rc_channels_scaled(this);
            case 35:
                return new msg_rc_channels_raw(this);
            case 36:
                return new msg_servo_output_raw(this);
            case 37:
                return new msg_mission_request_partial_list(this);
            case 38:
                return new msg_mission_write_partial_list(this);
            case 39:
                return new msg_mission_item(this);
            case 40:
                return new msg_mission_request(this);
            case 41:
                return new msg_mission_set_current(this);
            case 42:
                return new msg_mission_current(this);
            case 43:
                return new msg_mission_request_list(this);
            case 44:
                return new msg_mission_count(this);
            case 45:
                return new msg_mission_clear_all(this);
            case 46:
                return new msg_mission_item_reached(this);
            case 47:
                return new msg_mission_ack(this);
            case 48:
                return new msg_set_gps_global_origin(this);
            case 49:
                return new msg_gps_global_origin(this);
            case 50:
                return new msg_param_map_rc(this);
            case 51:
                return new msg_mission_request_int(this);
            case 54:
                return new msg_safety_set_allowed_area(this);
            case 55:
                return new msg_safety_allowed_area(this);
            case 61:
                return new msg_attitude_quaternion_cov(this);
            case 62:
                return new msg_nav_controller_output(this);
            case 63:
                return new msg_global_position_int_cov(this);
            case 64:
                return new msg_local_position_ned_cov(this);
            case 65:
                return new msg_rc_channels(this);
            case 66:
                return new msg_request_data_stream(this);
            case 67:
                return new msg_data_stream(this);
            case 69:
                return new msg_manual_control(this);
            case 70:
                return new msg_rc_channels_override(this);
            case 73:
                return new msg_mission_item_int(this);
            case 74:
                return new msg_vfr_hud(this);
            case 75:
                return new msg_command_int(this);
            case 76:
                return new msg_command_long(this);
            case 77:
                return new msg_command_ack(this);
            case 81:
                return new msg_manual_setpoint(this);
            case 82:
                return new msg_set_attitude_target(this);
            case 83:
                return new msg_attitude_target(this);
            case 84:
                return new msg_set_position_target_local_ned(this);
            case 85:
                return new msg_position_target_local_ned(this);
            case 86:
                return new msg_set_position_target_global_int(this);
            case 87:
                return new msg_position_target_global_int(this);
            case 89:
                return new msg_local_position_ned_system_global_offset(this);
            case 90:
                return new msg_hil_state(this);
            case 91:
                return new msg_hil_controls(this);
            case 92:
                return new msg_hil_rc_inputs_raw(this);
            case 93:
                return new msg_hil_actuator_controls(this);
            case 100:
                return new msg_optical_flow(this);
            case 101:
                return new msg_global_vision_position_estimate(this);
            case 102:
                return new msg_vision_position_estimate(this);
            case 103:
                return new msg_vision_speed_estimate(this);
            case 104:
                return new msg_vicon_position_estimate(this);
            case 105:
                return new msg_highres_imu(this);
            case 106:
                return new msg_optical_flow_rad(this);
            case 107:
                return new msg_hil_sensor(this);
            case 108:
                return new msg_sim_state(this);
            case 109:
                return new msg_radio_status(this);
            case 110:
                return new msg_file_transfer_protocol(this);
            case 111:
                return new msg_timesync(this);
            case 112:
                return new msg_camera_trigger(this);
            case 113:
                return new msg_hil_gps(this);
            case 114:
                return new msg_hil_optical_flow(this);
            case 115:
                return new msg_hil_state_quaternion(this);
            case 116:
                return new msg_scaled_imu2(this);
            case 117:
                return new msg_log_request_list(this);
            case 118:
                return new msg_log_entry(this);
            case 119:
                return new msg_log_request_data(this);
            case 120:
                return new msg_log_data(this);
            case 121:
                return new msg_log_erase(this);
            case 122:
                return new msg_log_request_end(this);
            case 123:
                return new msg_gps_inject_data(this);
            case 124:
                return new msg_gps2_raw(this);
            case msg_power_status.MAVLINK_MSG_ID_POWER_STATUS /* 125 */:
                return new msg_power_status(this);
            case 126:
                return new msg_serial_control(this);
            case 127:
                return new msg_gps_rtk(this);
            case 128:
                return new msg_gps2_rtk(this);
            case 129:
                return new msg_scaled_imu3(this);
            case msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE /* 130 */:
                return new msg_data_transmission_handshake(this);
            case msg_encapsulated_data.MAVLINK_MSG_ID_ENCAPSULATED_DATA /* 131 */:
                return new msg_encapsulated_data(this);
            case msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR /* 132 */:
                return new msg_distance_sensor(this);
            case msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST /* 133 */:
                return new msg_terrain_request(this);
            case 134:
                return new msg_terrain_data(this);
            case msg_terrain_check.MAVLINK_MSG_ID_TERRAIN_CHECK /* 135 */:
                return new msg_terrain_check(this);
            case msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT /* 136 */:
                return new msg_terrain_report(this);
            case msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2 /* 137 */:
                return new msg_scaled_pressure2(this);
            case msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP /* 138 */:
                return new msg_att_pos_mocap(this);
            case msg_set_actuator_control_target.MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET /* 139 */:
                return new msg_set_actuator_control_target(this);
            case 140:
                return new msg_actuator_control_target(this);
            case 141:
                return new msg_altitude(this);
            case 142:
                return new msg_resource_request(this);
            case 143:
                return new msg_scaled_pressure3(this);
            case 144:
                return new msg_follow_target(this);
            case 146:
                return new msg_control_system_state(this);
            case 147:
                return new msg_battery_status(this);
            case 148:
                return new msg_autopilot_version(this);
            case 149:
                return new msg_landing_target(this);
            case 151:
                return new msg_firmware_data_request(this);
            case 152:
                return new msg_firmware_info(this);
            case 153:
                return new msg_firmware_data(this);
            case 154:
                return new msg_firmware_update_ack(this);
            case msg_gps_aid_info.MAVLINK_MSG_ID_GPS_AID_INFO /* 161 */:
                return new msg_gps_aid_info(this);
            case msg_gps_aid_data_request.MAVLINK_MSG_ID_GPS_AID_DATA_REQUEST /* 162 */:
                return new msg_gps_aid_data_request(this);
            case msg_gps_aid_data.MAVLINK_MSG_ID_GPS_AID_DATA /* 163 */:
                return new msg_gps_aid_data(this);
            case msg_gps_aid_ack.MAVLINK_MSG_ID_GPS_AID_ACK /* 164 */:
                return new msg_gps_aid_ack(this);
            case msg_protocol_att_ekf_states.MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES /* 165 */:
                return new msg_protocol_att_ekf_states(this);
            case msg_protocol_att_ekf_atti.MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI /* 166 */:
                return new msg_protocol_att_ekf_atti(this);
            case msg_firmware_info_request.MAVLINK_MSG_ID_FIRMWARE_INFO_REQUEST /* 173 */:
                return new msg_firmware_info_request(this);
            case msg_firmware_update.MAVLINK_MSG_ID_FIRMWARE_UPDATE /* 174 */:
                return new msg_firmware_update(this);
            case msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST /* 175 */:
                return new msg_serial_number_request(this);
            case 176:
                return new msg_serial_number(this);
            case 177:
                return new msg_gps_module_info_request(this);
            case 178:
                return new msg_gps_module_info(this);
            case 179:
                return new msg_device_activation_request(this);
            case 180:
                return new msg_device_activation(this);
            case 181:
                return new msg_device_activated_ack(this);
            case 182:
                return new msg_mode_enable(this);
            case 183:
                return new msg_command_short(this);
            case 184:
                return new msg_realtime_data(this);
            case 185:
                return new msg_trolling_motor_status(this);
            case 186:
                return new msg_new_command_long(this);
            case msg_new_command_long_ack.MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK /* 187 */:
                return new msg_new_command_long_ack(this);
            case 230:
                return new msg_estimator_status(this);
            case msg_wind_cov.MAVLINK_MSG_ID_WIND_COV /* 231 */:
                return new msg_wind_cov(this);
            case msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT /* 232 */:
                return new msg_gps_input(this);
            case msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA /* 233 */:
                return new msg_gps_rtcm_data(this);
            case msg_high_latency.MAVLINK_MSG_ID_HIGH_LATENCY /* 234 */:
                return new msg_high_latency(this);
            case 241:
                return new msg_vibration(this);
            case 242:
                return new msg_home_position(this);
            case 243:
                return new msg_set_home_position(this);
            case msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL /* 244 */:
                return new msg_message_interval(this);
            case 245:
                return new msg_extended_sys_state(this);
            case 246:
                return new msg_adsb_vehicle(this);
            case msg_collision.MAVLINK_MSG_ID_COLLISION /* 247 */:
                return new msg_collision(this);
            case msg_v2_extension.MAVLINK_MSG_ID_V2_EXTENSION /* 248 */:
                return new msg_v2_extension(this);
            case msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT /* 249 */:
                return new msg_memory_vect(this);
            case 250:
                return new msg_debug_vect(this);
            case 251:
                return new msg_named_value_float(this);
            case 252:
                return new msg_named_value_int(this);
            case msg_statustext.MAVLINK_MSG_ID_STATUSTEXT /* 253 */:
                return new msg_statustext(this);
            case 254:
                return new msg_debug(this);
        }
    }
}
