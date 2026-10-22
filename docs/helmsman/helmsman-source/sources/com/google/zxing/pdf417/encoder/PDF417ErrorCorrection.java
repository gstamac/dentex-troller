package com.google.zxing.pdf417.encoder;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.view.InputDeviceCompat;
import com.MAVLink.common.msg_att_pos_mocap;
import com.MAVLink.common.msg_collision;
import com.MAVLink.common.msg_data_transmission_handshake;
import com.MAVLink.common.msg_distance_sensor;
import com.MAVLink.common.msg_gps_input;
import com.MAVLink.common.msg_high_latency;
import com.MAVLink.common.msg_local_position_ned_cov;
import com.MAVLink.common.msg_memory_vect;
import com.MAVLink.common.msg_message_interval;
import com.MAVLink.common.msg_scaled_pressure2;
import com.MAVLink.common.msg_terrain_report;
import com.MAVLink.common.msg_terrain_request;
import com.MAVLink.common.msg_v2_extension;
import com.MAVLink.common.msg_wind_cov;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_COMPONENT;
import com.MAVLink.enums.MAV_MODE;
import com.MAVLink.helmsman.msg_control_command;
import com.MAVLink.helmsman.msg_cruise_data;
import com.MAVLink.helmsman.msg_firmware_info_request;
import com.MAVLink.helmsman.msg_gps_aid_ack;
import com.MAVLink.helmsman.msg_gps_aid_data_request;
import com.MAVLink.helmsman.msg_navigation_data;
import com.MAVLink.helmsman.msg_new_command_long_ack;
import com.MAVLink.helmsman.msg_protocol_att_ekf_states;
import com.MAVLink.helmsman.msg_serial_number_request;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import com.facebook.internal.FacebookRequestErrorClassification;
import com.google.zxing.WriterException;
import com.google.zxing.pdf417.PDF417Common;
import com.zendesk.service.HttpConstants;

/* JADX INFO: loaded from: classes2.dex */
final class PDF417ErrorCorrection {
    private static final int[][] EC_COEFFICIENTS = {new int[]{27, 917}, new int[]{MAV_CMD.MAV_CMD_REQUEST_CAMERA_SETTINGS, 568, 723, 809}, new int[]{237, 308, 436, 284, 646, 653, 428, 379}, new int[]{274, 562, msg_gps_input.MAVLINK_MSG_ID_GPS_INPUT, 755, 599, MAV_CMD.MAV_CMD_SET_CAMERA_SETTINGS_2, 801, msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR, 295, 116, 442, 428, 295, 42, 176, 65}, new int[]{361, 575, 922, MAV_CMD.MAV_CMD_REQUEST_STORAGE_INFORMATION, 176, 586, 640, 321, 536, 742, 677, 742, 687, 284, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, 517, 273, 494, 263, 147, 593, GLMapStaticValue.ANIMATION_FLUENT_TIME, 571, 320, 803, msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST, msg_wind_cov.MAVLINK_MSG_ID_WIND_COV, 390, 685, 330, 63, 410}, new int[]{539, TypedValues.CycleType.TYPE_CUSTOM_WAVE_SHAPE, 6, 93, 862, 771, 453, 106, TypedValues.MotionType.TYPE_QUANTIZE_MOTIONSTEPS, 287, 107, 505, 733, 877, 381, TypedValues.MotionType.TYPE_QUANTIZE_INTERPOLATOR_ID, 723, 476, 462, msg_cruise_data.MAVLINK_MSG_ID_CRUISE_DATA, 430, TypedValues.MotionType.TYPE_POLAR_RELATIVETO, 858, 822, 543, 376, 511, 400, 672, 762, 283, 184, 440, 35, 519, 31, 460, 594, msg_local_position_ned_cov.MAVLINK_MSG_LENGTH, 535, 517, 352, TypedValues.MotionType.TYPE_ANIMATE_RELATIVE_TO, MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL, 651, 201, 488, 502, 648, 733, 717, 83, HttpConstants.HTTP_NOT_FOUND, 97, 280, 771, 840, 629, 4, 381, 843, 623, 264, 543}, new int[]{MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION, 310, 864, 547, 858, 580, 296, 379, 53, 779, 897, 444, 400, 925, 749, HttpConstants.HTTP_UNSUPPORTED_TYPE, 822, 93, 217, 208, PDF417Common.MAX_CODEWORDS_IN_BARCODE, msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL, 583, 620, 246, 148, 447, 631, 292, 908, 490, TypedValues.TransitionType.TYPE_AUTO_TRANSITION, 516, 258, 457, 907, 594, 723, 674, 292, 272, 96, 684, 432, 686, TypedValues.MotionType.TYPE_ANIMATE_CIRCLEANGLE_TO, 860, 569, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, 219, 129, 186, 236, 287, 192, 775, 278, msg_firmware_info_request.MAVLINK_MSG_ID_FIRMWARE_INFO_REQUEST, 40, 379, 712, 463, 646, 776, msg_navigation_data.MAVLINK_MSG_ID_NAVIGATION_DATA, 491, 297, 763, MAV_COMPONENT.MAV_COMP_ID_ADSB, 732, 95, 270, 447, 90, TypedValues.PositionType.TYPE_PERCENT_Y, 48, 228, 821, 808, 898, 784, 663, 627, 378, 382, 262, 380, TypedValues.MotionType.TYPE_QUANTIZE_MOTION_PHASE, 754, 336, 89, 614, 87, 432, 670, 616, MAV_COMPONENT.MAV_COMP_ID_OSD, 374, 242, 726, 600, 269, 375, 898, 845, 454, 354, msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE, 814, 587, 804, 34, 211, 330, 539, 297, 827, 865, 37, 517, 834, 315, 550, 86, 801, 4, 108, 539}, new int[]{MAV_CMD.MAV_CMD_SET_CAMERA_SETTINGS_2, 894, 75, 766, 882, 857, 74, 204, 82, 586, 708, 250, TypedValues.Custom.TYPE_DIMENSION, 786, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, 720, 858, 194, 311, 913, 275, 190, 375, 850, 438, 733, 194, 280, 201, 280, 828, 757, 710, 814, 919, 89, 68, 569, 11, 204, 796, TypedValues.MotionType.TYPE_ANIMATE_RELATIVE_TO, 540, 913, 801, TypedValues.TransitionType.TYPE_DURATION, 799, msg_scaled_pressure2.MAVLINK_MSG_ID_SCALED_PRESSURE2, 439, 418, 592, 668, 353, 859, 370, 694, 325, 240, MAV_MODE.MAV_MODE_GUIDED_ARMED, 257, 284, 549, MAV_CMD.MAV_CMD_DO_MOTOR_TEST, 884, 315, 70, 329, 793, 490, 274, 877, msg_gps_aid_data_request.MAVLINK_MSG_ID_GPS_AID_DATA_REQUEST, 749, 812, 684, 461, 334, 376, 849, MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION, 307, 291, 803, 712, 19, 358, 399, 908, 103, 511, 51, 8, 517, msg_local_position_ned_cov.MAVLINK_MSG_LENGTH, 289, 470, 637, 731, 66, 255, 917, 269, 463, 830, 730, 433, 848, 585, msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT, 538, TypedValues.Custom.TYPE_REFERENCE, 90, 2, 290, 743, 199, 655, TypedValues.Custom.TYPE_STRING, 329, 49, 802, 580, 355, 588, 188, 462, 10, 134, 628, 320, 479, msg_data_transmission_handshake.MAVLINK_MSG_ID_DATA_TRANSMISSION_HANDSHAKE, 739, 71, 263, TypedValues.AttributesType.TYPE_PIVOT_TARGET, 374, 601, 192, TypedValues.MotionType.TYPE_ANIMATE_RELATIVE_TO, 142, 673, 687, msg_high_latency.MAVLINK_MSG_ID_HIGH_LATENCY, 722, 384, 177, 752, TypedValues.MotionType.TYPE_PATHMOTION_ARC, 640, 455, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, 689, TypedValues.TransitionType.TYPE_TRANSITION_FLAGS, 805, 641, 48, 60, 732, 621, 895, 544, 261, 852, 655, 309, 697, 755, 756, 60, msg_wind_cov.MAVLINK_MSG_ID_WIND_COV, 773, 434, 421, 726, MAV_CMD.MAV_CMD_REQUEST_FLIGHT_INFORMATION, 503, 118, 49, 795, 32, 144, 500, 238, 836, 394, 280, 566, 319, 9, 647, 550, 73, 914, 342, 126, 32, 681, 331, 792, 620, 60, TypedValues.MotionType.TYPE_POLAR_RELATIVETO, 441, 180, 791, 893, 754, TypedValues.MotionType.TYPE_ANIMATE_RELATIVE_TO, 383, 228, 749, 760, MAV_CMD.MAV_CMD_NAV_SET_YAW_SPEED, 54, 297, 134, 54, 834, 299, 922, MAV_CMD.MAV_CMD_DO_GO_AROUND, 910, 532, TypedValues.MotionType.TYPE_POLAR_RELATIVETO, 829, MAV_CMD.MAV_CMD_DO_LAND_START, 20, 167, 29, 872, 449, 83, 402, 41, 656, 505, 579, 481, msg_firmware_info_request.MAVLINK_MSG_ID_FIRMWARE_INFO_REQUEST, HttpConstants.HTTP_NOT_FOUND, 251, 688, 95, 497, 555, 642, 543, 307, 159, 924, 558, 648, 55, 497, 10}, new int[]{352, 77, 373, 504, 35, 599, 428, MAV_CMD.MAV_CMD_DO_FENCE_ENABLE, HttpConstants.HTTP_CONFLICT, 574, 118, 498, 285, 380, 350, 492, 197, 265, 920, MAV_COMPONENT.MAV_COMP_ID_LOG, 914, 299, 229, 643, 294, 871, 306, 88, 87, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, 352, 781, 846, 75, 327, MAV_CMD.MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES, 435, 543, 203, 666, msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT, 346, 781, 621, 640, 268, 794, 534, 539, 781, HttpConstants.HTTP_CLIENT_TIMEOUT, 390, 644, 102, 476, 499, 290, 632, 545, 37, 858, 916, 552, 41, 542, 289, 122, 272, 383, GLMapStaticValue.ANIMATION_FLUENT_TIME, 485, 98, 752, 472, 761, 107, 784, 860, 658, 741, 290, 204, 681, HttpConstants.HTTP_PROXY_AUTH, 855, 85, 99, 62, 482, 180, 20, 297, 451, 593, 913, 142, 808, 684, 287, 536, 561, 76, 653, 899, 729, 567, 744, 390, InputDeviceCompat.SOURCE_DPAD, 192, 516, 258, 240, 518, 794, 395, 768, 848, 51, TypedValues.MotionType.TYPE_QUANTIZE_MOTIONSTEPS, 384, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, 190, 826, 328, 596, 786, 303, 570, 381, HttpConstants.HTTP_UNSUPPORTED_TYPE, 641, MAV_COMPONENT.MAV_COMP_ID_ADSB, 237, 151, 429, 531, MAV_CMD.MAV_CMD_DO_FENCE_ENABLE, 676, 710, 89, msg_control_command.MAVLINK_MSG_ID_CONTROL_COMMAND, 304, 402, 40, 708, 575, msg_gps_aid_data_request.MAVLINK_MSG_ID_GPS_AID_DATA_REQUEST, 864, 229, 65, 861, 841, 512, msg_gps_aid_ack.MAVLINK_MSG_ID_GPS_AID_ACK, 477, 221, 92, 358, 785, 288, 357, 850, 836, 827, 736, TypedValues.TransitionType.TYPE_TRANSITION_FLAGS, 94, 8, 494, 114, MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION, 2, 499, 851, 543, 152, 729, 771, 95, msg_v2_extension.MAVLINK_MSG_ID_V2_EXTENSION, 361, 578, 323, 856, 797, 289, 51, 684, 466, 533, 820, 669, 45, TypedValues.Custom.TYPE_COLOR, 452, 167, 342, msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL, msg_firmware_info_request.MAVLINK_MSG_ID_FIRMWARE_INFO_REQUEST, 35, 463, 651, 51, 699, 591, 452, 578, 37, 124, 298, 332, 552, 43, 427, 119, 662, 777, 475, 850, 764, 364, 578, 911, 283, 711, 472, TypedValues.CycleType.TYPE_EASING, 245, 288, 594, 394, 511, 327, 589, 777, 699, 688, 43, HttpConstants.HTTP_CLIENT_TIMEOUT, 842, 383, 721, MAV_CMD.MAV_CMD_REQUEST_CAMERA_INFORMATION, 560, 644, 714, 559, 62, MAV_COMPONENT.MAV_COMP_ID_SERVO6, 873, 663, 713, 159, 672, 729, 624, 59, MAV_CMD.MAV_CMD_DO_PAUSE_CONTINUE, 417, MAV_COMPONENT.MAV_COMP_ID_PERIPHERAL, MAV_CMD.MAV_CMD_DO_MOTOR_TEST, 563, 564, 343, 693, 109, TypedValues.MotionType.TYPE_DRAW_PATH, 563, 365, 181, 772, 677, 310, msg_v2_extension.MAVLINK_MSG_ID_V2_EXTENSION, 353, 708, 410, 579, 870, 617, 841, 632, 860, 289, 536, 35, 777, 618, 586, TypedValues.CycleType.TYPE_WAVE_OFFSET, 833, 77, 597, 346, 269, 757, 632, 695, 751, 331, msg_collision.MAVLINK_MSG_ID_COLLISION, 184, 45, 787, 680, 18, 66, HttpConstants.HTTP_PROXY_AUTH, 369, 54, 492, 228, 613, 830, 922, 437, 519, 644, TypedValues.Custom.TYPE_DIMENSION, 789, TypedValues.CycleType.TYPE_EASING, 305, 441, MAV_CMD.MAV_CMD_DO_FENCE_ENABLE, 300, 892, 827, 141, 537, 381, 662, InputDeviceCompat.SOURCE_DPAD, 56, 252, FacebookRequestErrorClassification.EC_TOO_MANY_USER_ACTION_CALLS, 242, 797, 838, 837, 720, 224, 307, 631, 61, 87, 560, 310, 756, 665, 397, 808, 851, 309, 473, 795, 378, 31, 647, 915, 459, 806, 590, 731, TypedValues.CycleType.TYPE_WAVE_PHASE, MAV_MODE.MAV_MODE_GUIDED_ARMED, 548, msg_memory_vect.MAVLINK_MSG_ID_MEMORY_VECT, 321, 881, 699, 535, 673, 782, MAV_CMD.MAV_CMD_DO_INVERTED_FLIGHT, 815, TypedValues.Custom.TYPE_DIMENSION, 303, 843, 922, 281, 73, 469, 791, 660, msg_gps_aid_data_request.MAVLINK_MSG_ID_GPS_AID_DATA_REQUEST, 498, 308, MAV_COMPONENT.MAV_COMP_ID_LOG, TypedValues.CycleType.TYPE_CUSTOM_WAVE_SHAPE, 907, 817, msg_new_command_long_ack.MAVLINK_MSG_ID_NEW_COMMAND_LONG_ACK, 62, 16, TypedValues.CycleType.TYPE_WAVE_PHASE, 535, 336, 286, 437, 375, 273, TypedValues.MotionType.TYPE_QUANTIZE_MOTIONSTEPS, 296, 183, 923, 116, 667, 751, 353, 62, 366, 691, 379, 687, 842, 37, 357, 720, 742, 330, 5, 39, 923, 311, TypedValues.CycleType.TYPE_WAVE_OFFSET, 242, 749, 321, 54, 669, TypedValues.AttributesType.TYPE_PATH_ROTATE, 342, 299, 534, 105, 667, 488, 640, 672, 576, 540, TypedValues.AttributesType.TYPE_PATH_ROTATE, 486, 721, TypedValues.MotionType.TYPE_QUANTIZE_MOTIONSTEPS, 46, 656, 447, msg_navigation_data.MAVLINK_MSG_ID_NAVIGATION_DATA, 616, 464, 190, 531, 297, 321, 762, 752, 533, msg_serial_number_request.MAVLINK_MSG_ID_SERIAL_NUMBER_REQUEST, 134, 14, 381, 433, 717, 45, 111, 20, 596, 284, 736, msg_att_pos_mocap.MAVLINK_MSG_ID_ATT_POS_MOCAP, 646, HttpConstants.HTTP_LENGTH_REQUIRED, 877, 669, 141, 919, 45, 780, HttpConstants.HTTP_PROXY_AUTH, msg_gps_aid_ack.MAVLINK_MSG_ID_GPS_AID_ACK, 332, 899, msg_protocol_att_ekf_states.MAVLINK_MSG_ID_PROTOCOL_ATT_EKF_STATES, 726, 600, 325, 498, 655, 357, 752, 768, MAV_CMD.MAV_CMD_DO_ENGINE_CONTROL, 849, 647, 63, 310, 863, 251, 366, 304, 282, 738, 675, 410, 389, msg_message_interval.MAVLINK_MSG_ID_MESSAGE_INTERVAL, 31, 121, 303, 263}};

    private PDF417ErrorCorrection() {
    }

    static int getErrorCorrectionCodewordCount(int i) {
        if (i < 0 || i > 8) {
            throw new IllegalArgumentException("Error correction level must be between 0 and 8!");
        }
        return 1 << (i + 1);
    }

    static int getRecommendedMinimumErrorCorrectionLevel(int i) throws WriterException {
        if (i <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }
        if (i <= 40) {
            return 2;
        }
        if (i <= 160) {
            return 3;
        }
        if (i <= 320) {
            return 4;
        }
        if (i <= 863) {
            return 5;
        }
        throw new WriterException("No recommendation possible");
    }

    static String generateErrorCorrection(CharSequence charSequence, int i) {
        int errorCorrectionCodewordCount = getErrorCorrectionCodewordCount(i);
        char[] cArr = new char[errorCorrectionCodewordCount];
        int length = charSequence.length();
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = errorCorrectionCodewordCount - 1;
            int iCharAt = (charSequence.charAt(i2) + cArr[i3]) % PDF417Common.NUMBER_OF_CODEWORDS;
            while (i3 > 0) {
                cArr[i3] = (char) ((cArr[i3 - 1] + (929 - ((EC_COEFFICIENTS[i][i3] * iCharAt) % PDF417Common.NUMBER_OF_CODEWORDS))) % PDF417Common.NUMBER_OF_CODEWORDS);
                i3--;
            }
            cArr[0] = (char) ((929 - ((iCharAt * EC_COEFFICIENTS[i][0]) % PDF417Common.NUMBER_OF_CODEWORDS)) % PDF417Common.NUMBER_OF_CODEWORDS);
        }
        StringBuilder sb = new StringBuilder(errorCorrectionCodewordCount);
        for (int i4 = errorCorrectionCodewordCount - 1; i4 >= 0; i4--) {
            char c = cArr[i4];
            if (c != 0) {
                cArr[i4] = (char) (929 - c);
            }
            sb.append(cArr[i4]);
        }
        return sb.toString();
    }
}
