package com.google.zxing.maxicode.decoder;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.view.InputDeviceCompat;
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
import com.MAVLink.common.msg_power_status;
import com.MAVLink.common.msg_scaled_pressure2;
import com.MAVLink.common.msg_set_actuator_control_target;
import com.MAVLink.common.msg_statustext;
import com.MAVLink.common.msg_terrain_check;
import com.MAVLink.common.msg_terrain_report;
import com.MAVLink.common.msg_terrain_request;
import com.MAVLink.common.msg_v2_extension;
import com.MAVLink.common.msg_wind_cov;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.enums.MAV_MODE;
import com.MAVLink.helmsman.msg_anchor_data;
import com.MAVLink.helmsman.msg_control_command;
import com.MAVLink.helmsman.msg_cruise_data;
import com.MAVLink.helmsman.msg_firmware_info_request;
import com.MAVLink.helmsman.msg_firmware_update;
import com.MAVLink.helmsman.msg_gps_aid_ack;
import com.MAVLink.helmsman.msg_gps_aid_data;
import com.MAVLink.helmsman.msg_gps_aid_data_request;
import com.MAVLink.helmsman.msg_gps_aid_info;
import com.MAVLink.helmsman.msg_navigation_data;
import com.MAVLink.helmsman.msg_new_command_long_ack;
import com.MAVLink.helmsman.msg_protocol_att_ekf_atti;
import com.MAVLink.helmsman.msg_protocol_att_ekf_states;
import com.MAVLink.helmsman.msg_record_route_data;
import com.MAVLink.helmsman.msg_serial_number_request;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.facebook.internal.FacebookRequestErrorClassification;
import com.google.zxing.common.BitMatrix;
import com.zendesk.service.HttpConstants;

/* JADX INFO: loaded from: classes2.dex */
final class BitMatrixParser {
    private static final int[][] BITNR = {new int[]{121, 120, 127, 126, msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST, msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR, msg_set_actuator_control_target.MAVLINK_MSG_ID_SET_ACTUATOR_CONTROL_TARGET, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, MAV_COMPONENT.MAV_COMP_ID_SERVO6, 144, 151, MAV_COMPONENT.MAV_COMP_ID_SERVO11, MAV_COMPONENT.MAV_COMP_ID_OSD, MAV_COMPONENT.MAV_COMP_ID_ADSB, msg_gps_aid_data.MAVLINK_MSG_ID_GPS_AID_DATA, msg_gps_aid_data_request.MAVLINK_MSG_ID_GPS_AID_DATA_REQUEST, msg_anchor_data.MAVLINK_MSG_ID_ANCHOR_DATA, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, msg_firmware_update.MAVLINK_MSG_ID_FIRMWARE_UPDATE, 181, 180, msg_new_command_long_ack.MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK, 186, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, 192, 199, 198, -2, -2}, new int[]{123, 122, 129, 128, msg_terrain_check.MAVLINK_MSG_ID_TERRAIN_CHECK, 134, 141, 140, 147, 146, 153, 152, 159, MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL, msg_protocol_att_ekf_states.MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES, msg_gps_aid_ack.MAVLINK_MSG_ID_GPS_AID_ACK, msg_navigation_data.MAVLINK_MSG_ID_NAVIGATION_DATA, msg_record_route_data.MAVLINK_MSG_ID_RECORD_ROUTE_DATA, 177, 176, 183, 182, MAV_CMD.MAV_CMD_DO_LAND_START, 188, MAV_COMPONENT.MAV_COMP_ID_PATHPLANNER, 194, 201, 200, 816, -3}, new int[]{msg_power_status.MAVLINK_MSG_ID_POWER_STATUS, 124, msg_encapsulated_data.MAVLINK_MSG_ID_ENCAPSULATED_DATA, msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE, msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2, msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT, 143, 142, 149, 148, MAV_COMPONENT.MAV_COMP_ID_LOG, 154, msg_gps_aid_info.MAVLINK_MSG_ID_GPS_AID_INFO, 160, 167, msg_protocol_att_ekf_atti.MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_ATTI, msg_firmware_info_request.MAVLINK_MSG_ID_FIRMWARE_INFO_REQUEST, msg_cruise_data.MAVLINK_MSG_ID_CRUISE_DATA, 179, 178, 185, 184, MAV_CMD.MAV_CMD_DO_GO_AROUND, 190, 197, 196, 203, 202, 818, 817}, new int[]{283, 282, 277, 276, 271, 270, 265, 264, 259, 258, msg_statustext.MAVLINK_MSG_ID_STATUSTEXT, 252, msg_collision.MAVLINK_MSG_ID_COLLISION, 246, 241, 240, 235, msg_high_latency.MAVLINK_MSG_ID_HIGH_LATENCY, 229, 228, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, MAV_CMD.MAV_CMD_DO_GUIDED_LIMITS, 217, MAV_MODE.MAV_MODE_GUIDED_ARMED, 211, MAV_CMD.MAV_CMD_DO_INVERTED_FLIGHT, 205, 204, 819, -3}, new int[]{285, 284, 279, 278, 273, 272, 267, 266, 261, 260, 255, 254, msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT, msg_v2_extension.MAVLINK_MSG_ID_V2_EXTENSION, 243, 242, 237, 236, msg_wind_cov.MAVLINK_MSG_ID_WIND_COV, 230, msg_local_position_ned_cov.MAVLINK_MSG_LENGTH, 224, 219, 218, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, 212, MAV_CMD.MAV_CMD_DO_FENCE_ENABLE, 206, 821, 820}, new int[]{287, 286, 281, 280, 275, 274, 269, 268, 263, 262, 257, 256, 251, 250, 245, msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL, 239, 238, msg_gps_rtcm_data.MAVLINK_MSG_ID_GPS_RTCM_DATA, msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT, 227, 226, 221, 220, 215, 214, MAV_CMD.MAV_CMD_DO_MOTOR_TEST, 208, 822, -3}, new int[]{289, 288, 295, 294, 301, 300, 307, 306, 313, 312, 319, TypedValues.AttributesType.TYPE_PIVOT_TARGET, 325, 324, 331, 330, 337, 336, 343, 342, 349, 348, 355, 354, 361, 360, 367, 366, 824, 823}, new int[]{291, 290, 297, 296, 303, 302, 309, 308, 315, 314, 321, 320, 327, 326, 333, 332, 339, 338, 345, 344, 351, 350, 357, 356, 363, 362, 369, 368, 825, -3}, new int[]{293, 292, 299, 298, 305, 304, 311, 310, TypedValues.AttributesType.TYPE_EASING, TypedValues.AttributesType.TYPE_PATH_ROTATE, 323, 322, 329, 328, 335, 334, FacebookRequestErrorClassification.EC_TOO_MANY_USER_ACTION_CALLS, 340, 347, 346, 353, 352, 359, 358, 365, 364, 371, 370, 827, 826}, new int[]{HttpConstants.HTTP_CONFLICT, HttpConstants.HTTP_CLIENT_TIMEOUT, 403, 402, 397, 396, 391, 390, 79, 78, -2, -2, 13, 12, 37, 36, 2, -1, 44, 43, 109, 108, 385, 384, 379, 378, 373, 372, 828, -3}, new int[]{HttpConstants.HTTP_LENGTH_REQUIRED, 410, HttpConstants.HTTP_BAD_METHOD, HttpConstants.HTTP_NOT_FOUND, 399, 398, 393, 392, 81, 80, 40, -2, 15, 14, 39, 38, 3, -1, -1, 45, 111, 110, 387, 386, 381, 380, 375, 374, 830, 829}, new int[]{HttpConstants.HTTP_ENTITY_TOO_LARGE, 412, HttpConstants.HTTP_PROXY_AUTH, HttpConstants.HTTP_NOT_ACCEPTABLE, 401, 400, 395, 394, 83, 82, 41, -3, -3, -3, -3, -3, 5, 4, 47, 46, 113, 112, 389, 388, 383, 382, 377, 376, 831, -3}, new int[]{HttpConstants.HTTP_UNSUPPORTED_TYPE, HttpConstants.HTTP_REQ_TOO_LONG, 421, TypedValues.CycleType.TYPE_EASING, 427, 426, 103, 102, 55, 54, 16, -3, -3, -3, -3, -3, -3, -3, 20, 19, 85, 84, 433, 432, 439, 438, 445, 444, 833, 832}, new int[]{417, TypedValues.CycleType.TYPE_PATH_ROTATE, TypedValues.CycleType.TYPE_WAVE_PERIOD, TypedValues.CycleType.TYPE_CUSTOM_WAVE_SHAPE, 429, 428, 105, 104, 57, 56, -3, -3, -3, -3, -3, -3, -3, -3, 22, 21, 87, 86, 435, 434, 441, 440, 447, 446, 834, -3}, new int[]{419, 418, TypedValues.CycleType.TYPE_WAVE_PHASE, TypedValues.CycleType.TYPE_WAVE_OFFSET, 431, 430, 107, 106, 59, 58, -3, -3, -3, -3, -3, -3, -3, -3, -3, 23, 89, 88, 437, 436, 443, 442, 449, 448, 836, 835}, new int[]{481, 480, 475, 474, 469, 468, 48, -2, 30, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, 0, 53, 52, 463, 462, 457, 456, 451, HttpConstants.HTTP_BLOCKED, 837, -3}, new int[]{483, 482, 477, 476, 471, 470, 49, -1, -2, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -2, -1, 465, 464, 459, FacebookRequestErrorClassification.ESC_APP_NOT_INSTALLED, 453, 452, 839, 838}, new int[]{485, 484, 479, 478, 473, 472, 51, 50, 31, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, 1, -2, 42, 467, 466, 461, 460, 455, 454, 840, -3}, new int[]{487, 486, FacebookRequestErrorClassification.ESC_APP_INACTIVE, 492, 499, 498, 97, 96, 61, 60, -3, -3, -3, -3, -3, -3, -3, -3, -3, 26, 91, 90, 505, 504, 511, 510, 517, 516, 842, 841}, new int[]{489, 488, 495, 494, 501, 500, 99, 98, 63, 62, -3, -3, -3, -3, -3, -3, -3, -3, 28, 27, 93, 92, TypedValues.PositionType.TYPE_PERCENT_Y, TypedValues.PositionType.TYPE_PERCENT_X, InputDeviceCompat.SOURCE_DPAD, 512, 519, 518, 843, -3}, new int[]{491, 490, 497, 496, 503, 502, 101, 100, 65, 64, 17, -3, -3, -3, -3, -3, -3, -3, 18, 29, 95, 94, 509, TypedValues.PositionType.TYPE_CURVE_FIT, 515, 514, MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION, MAV_CMD.MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES, 845, 844}, new int[]{559, 558, 553, 552, 547, 546, 541, 540, 73, 72, 32, -3, -3, -3, -3, -3, -3, 10, 67, 66, 115, 114, 535, 534, 529, MAV_CMD.MAV_CMD_REQUEST_FLIGHT_INFORMATION, MAV_CMD.MAV_CMD_SET_CAMERA_SETTINGS_1, MAV_CMD.MAV_CMD_REQUEST_CAMERA_SETTINGS, 846, -3}, new int[]{561, 560, 555, 554, 549, 548, 543, 542, 75, 74, -2, -1, 7, 6, 35, 34, 11, -2, 69, 68, 117, 116, 537, 536, 531, 530, MAV_CMD.MAV_CMD_REQUEST_STORAGE_INFORMATION, MAV_CMD.MAV_CMD_SET_CAMERA_SETTINGS_2, 848, 847}, new int[]{563, 562, 557, 556, 551, 550, 545, 544, 77, 76, -2, 33, 9, 8, 25, 24, -1, -2, 71, 70, 119, 118, 539, 538, 533, 532, MAV_CMD.MAV_CMD_REQUEST_CAMERA_CAPTURE_STATUS, MAV_CMD.MAV_CMD_STORAGE_FORMAT, 849, -3}, new int[]{565, 564, 571, 570, 577, 576, 583, 582, 589, 588, 595, 594, 601, 600, TypedValues.MotionType.TYPE_PATHMOTION_ARC, TypedValues.MotionType.TYPE_ANIMATE_CIRCLEANGLE_TO, 613, TypedValues.MotionType.TYPE_QUANTIZE_INTERPOLATOR_ID, 619, 618, 625, 624, 631, 630, 637, 636, 643, 642, 851, 850}, new int[]{567, 566, 573, 572, 579, 578, 585, 584, 591, 590, 597, 596, TypedValues.MotionType.TYPE_EASING, TypedValues.MotionType.TYPE_QUANTIZE_MOTION_PHASE, TypedValues.MotionType.TYPE_POLAR_RELATIVETO, TypedValues.MotionType.TYPE_DRAW_PATH, 615, 614, 621, 620, 627, 626, 633, 632, 639, 638, 645, 644, 852, -3}, new int[]{569, 568, 575, 574, 581, 580, 587, 586, 593, 592, 599, 598, TypedValues.MotionType.TYPE_ANIMATE_RELATIVE_TO, TypedValues.MotionType.TYPE_QUANTIZE_INTERPOLATOR, TypedValues.MotionType.TYPE_QUANTIZE_INTERPOLATOR_TYPE, TypedValues.MotionType.TYPE_QUANTIZE_MOTIONSTEPS, 617, 616, 623, 622, 629, 628, 635, 634, 641, 640, 647, 646, 854, 853}, new int[]{727, 726, 721, 720, 715, 714, 709, 708, 703, TypedValues.TransitionType.TYPE_TO, 697, 696, 691, 690, 685, 684, 679, 678, 673, 672, 667, 666, 661, 660, 655, 654, 649, 648, 855, -3}, new int[]{729, 728, 723, 722, 717, 716, 711, 710, TypedValues.TransitionType.TYPE_INTERPOLATOR, TypedValues.TransitionType.TYPE_AUTO_TRANSITION, 699, 698, 693, 692, 687, 686, 681, 680, 675, 674, 669, 668, 663, 662, 657, 656, 651, 650, 857, 856}, new int[]{731, 730, 725, 724, 719, 718, 713, 712, TypedValues.TransitionType.TYPE_TRANSITION_FLAGS, TypedValues.TransitionType.TYPE_STAGGERED, TypedValues.TransitionType.TYPE_FROM, TypedValues.TransitionType.TYPE_DURATION, 695, 694, 689, 688, 683, 682, 677, 676, 671, 670, 665, 664, 659, 658, 653, 652, 858, -3}, new int[]{733, 732, 739, 738, 745, 744, 751, 750, 757, 756, 763, 762, 769, 768, 775, 774, 781, 780, 787, 786, 793, 792, 799, 798, 805, 804, 811, 810, 860, 859}, new int[]{735, 734, 741, 740, 747, 746, 753, 752, 759, 758, 765, 764, 771, 770, 777, 776, 783, 782, 789, 788, 795, 794, 801, GLMapStaticValue.ANIMATION_FLUENT_TIME, 807, 806, 813, 812, 861, -3}, new int[]{737, 736, 743, 742, 749, 748, 755, 754, 761, 760, 767, 766, 773, 772, 779, 778, 785, 784, 791, 790, 797, 796, 803, 802, 809, 808, 815, 814, 863, 862}};
    private final BitMatrix bitMatrix;

    BitMatrixParser(BitMatrix bitMatrix) {
        this.bitMatrix = bitMatrix;
    }

    byte[] readCodewords() {
        byte[] bArr = new byte[144];
        int height = this.bitMatrix.getHeight();
        int width = this.bitMatrix.getWidth();
        for (int i = 0; i < height; i++) {
            int[] iArr = BITNR[i];
            for (int i2 = 0; i2 < width; i2++) {
                int i3 = iArr[i2];
                if (i3 >= 0 && this.bitMatrix.get(i2, i)) {
                    int i4 = i3 / 6;
                    bArr[i4] = (byte) (((byte) (1 << (5 - (i3 % 6)))) | bArr[i4]);
                }
            }
        }
        return bArr;
    }
}
