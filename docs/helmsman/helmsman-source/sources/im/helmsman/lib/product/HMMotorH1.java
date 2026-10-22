package im.helmsman.lib.product;

import android.util.Log;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_auth_key;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_clear_all;
import com.MAVLink.common.msg_mission_count;
import com.MAVLink.common.msg_mission_item_int;
import com.MAVLink.common.msg_mission_request_int;
import com.MAVLink.common.msg_mission_request_list;
import com.MAVLink.common.msg_param_request_list;
import com.MAVLink.common.msg_param_request_read;
import com.MAVLink.common.msg_param_set;
import com.MAVLink.common.msg_param_value;
import com.MAVLink.common.msg_raw_imu;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.helmsman.msg_command_short;
import com.MAVLink.helmsman.msg_device_activation;
import com.MAVLink.helmsman.msg_device_activation_request;
import com.MAVLink.helmsman.msg_firmware_data;
import com.MAVLink.helmsman.msg_firmware_data_request;
import com.MAVLink.helmsman.msg_firmware_info;
import com.MAVLink.helmsman.msg_firmware_info_request;
import com.MAVLink.helmsman.msg_firmware_update;
import com.MAVLink.helmsman.msg_firmware_update_ack;
import com.MAVLink.helmsman.msg_gps_aid_data;
import com.MAVLink.helmsman.msg_gps_aid_data_request;
import com.MAVLink.helmsman.msg_gps_aid_info;
import com.MAVLink.helmsman.msg_gps_module_info;
import com.MAVLink.helmsman.msg_gps_module_info_request;
import com.MAVLink.helmsman.msg_serial_number;
import com.MAVLink.helmsman.msg_serial_number_request;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.component.BoatControlComponent;
import im.helmsman.lib.component.MotorDriverComponent;
import im.helmsman.lib.component.TeleControlComponent;
import im.helmsman.lib.net.NetUtils;
import im.helmsman.lib.property.ComapassProperty;
import im.helmsman.lib.property.GpsProperty;
import im.helmsman.lib.property.WifiProperty;
import im.helmsman.lib.util.PositionUtils;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public class HMMotorH1 extends ABaseProduct {
    private AidDataListener aidDataListener;
    private long recevieBoatControlDataTime;
    private Timer timer;

    public interface AidDataListener {
        void onAck();

        void onRequestAidData(long j, short s);
    }

    public HMMotorH1(int i, int i2) {
        super(i, i2);
        this.recevieBoatControlDataTime = 0L;
        BoatControlComponent.onUpLoadRouteListener = dataManager;
        BoatControlComponent.onDownLoadRouteListener = dataManager;
        this.aidDataListener = dataManager;
        BoatControlComponentH1.firmwareUpdateListener = dataManager;
    }

    @Override // im.helmsman.lib.ABaseProduct
    public void onReceiverData(MAVLinkMessage mAVLinkMessage) {
        if (this.timer == null) {
            Timer timer = new Timer();
            this.timer = timer;
            timer.schedule(new IsConnectedTimer(), 0L, 5000L);
        }
        Log.e("test", mAVLinkMessage.toString());
        this.recevieBoatControlDataTime = System.currentTimeMillis();
        int i = mAVLinkMessage.msgid;
        if (i == 0) {
            msg_heartbeat msg_heartbeatVar = (msg_heartbeat) mAVLinkMessage;
            setType(msg_heartbeatVar.type);
            setFirwareVersion((int) msg_heartbeatVar.custom_mode);
            setDisconnectProtection(msg_heartbeatVar.base_mode);
        } else if (i == 1) {
            this.teleControlComponent.setBatteryElectricityLevel(((msg_sys_status) mAVLinkMessage).voltage_battery);
        } else if (i == 151) {
            msg_firmware_data_request msg_firmware_data_requestVar = (msg_firmware_data_request) mAVLinkMessage;
            BoatControlComponentH1.firmwareUpdateListener.onRequestFirmwareData(msg_firmware_data_requestVar.address, (int) msg_firmware_data_requestVar.length);
        } else if (i == 152) {
            msg_firmware_info msg_firmware_infoVar = (msg_firmware_info) mAVLinkMessage;
            Log.e("固件版本", msg_firmware_infoVar.version + "");
            Log.e("编译号", msg_firmware_infoVar.build + "");
        } else if (i == 178) {
            msg_gps_module_info msg_gps_module_infoVar = (msg_gps_module_info) mAVLinkMessage;
            Log.e("gpsType", ((int) msg_gps_module_infoVar.type) + "");
            BoatControlComponentH1.GpsPropertyH1.callback.gpsInfo(msg_gps_module_infoVar.type);
        } else if (i != 179) {
            switch (i) {
                case 22:
                    Log.e("msg_pparamidaram_value", ((msg_param_value) mAVLinkMessage).toString());
                    break;
                case 27:
                    msg_raw_imu msg_raw_imuVar = (msg_raw_imu) mAVLinkMessage;
                    this.boatControlComponent.gpsProperty.setSignal(msg_raw_imuVar.ygyro);
                    this.boatControlComponent.setTargetPoint(msg_raw_imuVar.zacc);
                    this.motorComponent.setSpeed(msg_raw_imuVar.zgyro);
                    this.motorComponent.setOn(msg_raw_imuVar.xgyro == 11);
                    break;
                case 30:
                    msg_attitude msg_attitudeVar = (msg_attitude) mAVLinkMessage;
                    float f = msg_attitudeVar.yaw;
                    Log.e("MAVLINK_MSG_ID_ATTITUDE", msg_attitudeVar.yaw + "");
                    this.boatControlComponent.setBoatDegress(f);
                    this.boatControlComponent.setElectricityLevel((int) msg_attitudeVar.pitch);
                    this.boatControlComponent.setBoatModel((int) msg_attitudeVar.pitchspeed);
                    break;
                case 44:
                    msg_mission_count msg_mission_countVar = (msg_mission_count) mAVLinkMessage;
                    if (BoatControlComponent.onDownLoadRouteListener != null) {
                        BoatControlComponent.onDownLoadRouteListener.downloadWaypointCount(msg_mission_countVar.count);
                    }
                    break;
                case 47:
                    if (BoatControlComponent.onUpLoadRouteListener != null) {
                        BoatControlComponent.onUpLoadRouteListener.onUploadComplete();
                    }
                    break;
                case 51:
                    msg_mission_request_int msg_mission_request_intVar = (msg_mission_request_int) mAVLinkMessage;
                    Log.e("mission_request_int", msg_mission_request_intVar.seq + "");
                    if (BoatControlComponent.onUpLoadRouteListener != null) {
                        BoatControlComponent.onUpLoadRouteListener.onUploadProgress(msg_mission_request_intVar.seq);
                    }
                    break;
                case 73:
                    msg_mission_item_int msg_mission_item_intVar = (msg_mission_item_int) mAVLinkMessage;
                    if (BoatControlComponent.onDownLoadRouteListener != null) {
                        BoatControlComponent.onDownLoadRouteListener.downloadWaypoint(msg_mission_item_intVar.seq, msg_mission_item_intVar.x, msg_mission_item_intVar.y);
                    }
                    break;
                case 154:
                    Log.e("firmwareack", ((int) ((msg_firmware_update_ack) mAVLinkMessage).result) + "");
                    break;
                case msg_gps_aid_data_request.MAVLINK_MSG_ID_GPS_AID_DATA_REQUEST /* 162 */:
                    msg_gps_aid_data_request msg_gps_aid_data_requestVar = (msg_gps_aid_data_request) mAVLinkMessage;
                    AidDataListener aidDataListener = this.aidDataListener;
                    if (aidDataListener != null) {
                        aidDataListener.onRequestAidData(msg_gps_aid_data_requestVar.address, msg_gps_aid_data_requestVar.length);
                    }
                    Log.e("firstreadaidDataRequest", msg_gps_aid_data_requestVar.toString());
                    break;
                case 176:
                    Log.e("serial", ((msg_serial_number) mAVLinkMessage).getSerial_Number());
                    break;
                case 181:
                    Log.d("device_activated_ack", "activition");
                    new NetUtils().deviceActivated(getSerialNum());
                    break;
            }
        } else {
            String str = new String(((msg_device_activation_request) mAVLinkMessage).serial_number);
            Log.e("msg_device_activation", str);
            new NetUtils();
            NetUtils.activitionRequest(str, new NetUtils.ActivtionCallback() { // from class: im.helmsman.lib.product.HMMotorH1.1
                @Override // im.helmsman.lib.net.NetUtils.ActivtionCallback
                public void onSuccess(long j, String str2) {
                    HMMotorH1.this.boatControlComponent.activationDevice(j);
                    HMMotorH1.this.setSerialNum(str2);
                }

                @Override // im.helmsman.lib.net.NetUtils.ActivtionCallback
                public void onFailure(String str2) {
                    Log.e("activitionRequest_error", str2);
                }
            });
        }
        dataManager.dataChange(this);
    }

    @Override // im.helmsman.lib.ABaseProduct
    public void initComponent() {
        this.motorComponent = new MotorDriverComponentH1(0, 0);
        this.boatControlComponent = new BoatControlComponentH1(0, 0);
        this.teleControlComponent = TeleControlComponent.getTeleControlComponentInstance(11);
    }

    public static class MotorDriverComponentH1 extends MotorDriverComponent {
        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void maxSpeed() {
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void motorOff() {
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void motorOn() {
        }

        public MotorDriverComponentH1(int i, int i2) {
            super(i, i2);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void speedUp() {
            msg_command_short msg_command_shortVar = new msg_command_short();
            msg_command_shortVar.compid = 0;
            msg_command_shortVar.command = 2;
            HMMotorH1.manager.sendData(msg_command_shortVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void speedDown() {
            msg_command_short msg_command_shortVar = new msg_command_short();
            msg_command_shortVar.compid = 0;
            msg_command_shortVar.command = 3;
            HMMotorH1.manager.sendData(msg_command_shortVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void turnRight() {
            msg_command_short msg_command_shortVar = new msg_command_short();
            msg_command_shortVar.compid = 0;
            msg_command_shortVar.command = 1;
            HMMotorH1.manager.sendData(msg_command_shortVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void turnLeft() {
            msg_command_short msg_command_shortVar = new msg_command_short();
            msg_command_shortVar.compid = 0;
            msg_command_shortVar.command = 0;
            HMMotorH1.manager.sendData(msg_command_shortVar);
        }
    }

    public static class BoatControlComponentH1 extends BoatControlComponent {
        public static FirmwareUpdateListener firmwareUpdateListener;

        public interface FirmwareUpdateListener {
            void onFirmwareAck();

            void onRequestFirmwareData(long j, int i);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void setNaviMode(int i) {
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void activationDevice(long j) {
            msg_device_activation msg_device_activationVar = new msg_device_activation();
            msg_device_activationVar.timestamp = j;
            msg_device_activationVar.compid = 0;
            HMMotorH1.manager.sendData(msg_device_activationVar);
        }

        public static void sendFrimwareUpdate(int i, int i2, int i3) {
            msg_firmware_update msg_firmware_updateVar = new msg_firmware_update();
            msg_firmware_updateVar.build = i;
            msg_firmware_updateVar.version = i2;
            msg_firmware_updateVar.size = i3;
            HMMotorH1.manager.sendData(msg_firmware_updateVar);
        }

        public static void sendFrimwareData(long j, short[] sArr) {
            msg_firmware_data msg_firmware_dataVar = new msg_firmware_data();
            msg_firmware_dataVar.data = Arrays.copyOf(sArr, 127);
            msg_firmware_dataVar.address = j;
            msg_firmware_dataVar.length = (short) sArr.length;
            HMMotorH1.manager.sendData(msg_firmware_dataVar);
        }

        public static class WifiPropertyH1 extends WifiProperty {
            @Override // im.helmsman.lib.property.WifiProperty
            public void changeWifiPassword(String str) {
                msg_auth_key msg_auth_keyVar = new msg_auth_key();
                char[] charArray = str.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                    msg_auth_keyVar.key[i] = (byte) charArray[i];
                }
                HMMotorH1.manager.sendData(msg_auth_keyVar);
            }
        }

        public static class GpsPropertyH1 extends GpsProperty {
            public static GPSModuleInfoCallback callback;

            public interface GPSModuleInfoCallback {
                void gpsInfo(int i);
            }

            public void getGpsModuleInfo(GPSModuleInfoCallback gPSModuleInfoCallback) {
                msg_gps_module_info_request msg_gps_module_info_requestVar = new msg_gps_module_info_request();
                msg_gps_module_info_requestVar.target_component = (short) 0;
                HMMotorH1.manager.sendData(msg_gps_module_info_requestVar);
                callback = gPSModuleInfoCallback;
            }

            public void sendGPSInfoToComponent(long j) {
                msg_gps_aid_info msg_gps_aid_infoVar = new msg_gps_aid_info();
                msg_gps_aid_infoVar.target_component = (short) 0;
                msg_gps_aid_infoVar.type = (short) 1;
                msg_gps_aid_infoVar.aid_data_size = j;
                HMMotorH1.manager.sendData(msg_gps_aid_infoVar);
            }

            public void sendGPSAidData(short[] sArr, long j) {
                msg_gps_aid_data msg_gps_aid_dataVar = new msg_gps_aid_data();
                msg_gps_aid_dataVar.length = (short) sArr.length;
                msg_gps_aid_dataVar.data = Arrays.copyOf(sArr, 127);
                msg_gps_aid_dataVar.address = j;
                Log.e("发出的星历数据：", Arrays.toString(msg_gps_aid_dataVar.pack().encodePacket()));
                Log.e("发出的星历数据：" + j + "  " + ((int) msg_gps_aid_dataVar.length), Arrays.toString(msg_gps_aid_dataVar.data));
                HMMotorH1.manager.sendData(msg_gps_aid_dataVar);
            }
        }

        public BoatControlComponentH1(int i, int i2) {
            super(i, i2, new GpsPropertyH1(), new ComapassProperty(), new WifiPropertyH1());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void readSerial() {
            msg_serial_number_request msg_serial_number_requestVar = new msg_serial_number_request();
            msg_serial_number_requestVar.target_component = (short) 0;
            HMMotorH1.manager.sendData(msg_serial_number_requestVar);
        }

        public void setParam(short s, float f) {
            msg_param_set msg_param_setVar = new msg_param_set();
            msg_param_setVar.param_value = f;
            msg_param_setVar.param_type = s;
            HMMotorH1.manager.sendData(msg_param_setVar);
        }

        public void setParam(short s, float f, byte[] bArr) {
            msg_param_set msg_param_setVar = new msg_param_set();
            msg_param_setVar.param_value = f;
            msg_param_setVar.param_type = s;
            msg_param_setVar.param_id = Arrays.copyOf(bArr, 16);
            HMMotorH1.manager.sendData(msg_param_setVar);
        }

        public void readParamByIndex(int i) {
            new msg_param_request_read().param_index = (short) i;
        }

        public void getParams() {
            msg_param_request_list msg_param_request_listVar = new msg_param_request_list();
            msg_param_request_listVar.target_component = (short) 0;
            HMMotorH1.manager.sendData(msg_param_request_listVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void changeModel(int i) {
            HMMotorH1.manager.sendData(new msg_command_long());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendHeartBeat() {
            HMMotorH1.manager.sendData(new msg_heartbeat());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendClearCacheCommand() {
            HMMotorH1.manager.sendData(new msg_mission_clear_all());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendAckToBoatControl() {
            HMMotorH1.manager.sendData(new msg_mission_ack());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendRequestFirmwareInfo() {
            msg_firmware_info_request msg_firmware_info_requestVar = new msg_firmware_info_request();
            msg_firmware_info_requestVar.target_component = (short) 0;
            HMMotorH1.manager.sendData(msg_firmware_info_requestVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void uploadWaypointRequest(int i) {
            msg_mission_count msg_mission_countVar = new msg_mission_count();
            msg_mission_countVar.count = i;
            HMMotorH1.manager.sendData(msg_mission_countVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void upLoadWayPount(float f, float f2, int i) {
            msg_mission_item_int msg_mission_item_intVar = new msg_mission_item_int();
            msg_mission_item_intVar.seq = i;
            msg_mission_item_intVar.x = PositionUtils.floatToInt(f);
            msg_mission_item_intVar.y = PositionUtils.floatToInt(f2);
            HMMotorH1.manager.sendData(msg_mission_item_intVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void requestDownLoadRoute() {
            HMMotorH1.manager.sendData(new msg_mission_request_list().pack());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void downloadWaypointBySeq(int i) {
            msg_mission_request_int msg_mission_request_intVar = new msg_mission_request_int();
            msg_mission_request_intVar.seq = i;
            HMMotorH1.manager.sendData(msg_mission_request_intVar);
        }
    }

    class IsConnectedTimer extends TimerTask {
        IsConnectedTimer() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (System.currentTimeMillis() - HMMotorH1.this.recevieBoatControlDataTime >= 5000) {
                cancel();
                HMMotorH1.this.timer = null;
                HMMotorH1.this.boatControlComponent.setConnectState(1);
                return;
            }
            HMMotorH1.this.boatControlComponent.setConnectState(0);
        }
    }
}
