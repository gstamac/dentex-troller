package im.helmsman.lib.product;

import android.util.Log;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_auth_key;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_global_position_int;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_memory_vect;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_clear_all;
import com.MAVLink.common.msg_mission_count;
import com.MAVLink.common.msg_mission_item;
import com.MAVLink.common.msg_mission_request;
import com.MAVLink.common.msg_mission_request_list;
import com.MAVLink.common.msg_raw_imu;
import com.MAVLink.common.msg_sys_status;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.AComponentModel;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.component.BoatControlComponent;
import im.helmsman.lib.component.MotorDriverComponent;
import im.helmsman.lib.component.TeleControlComponent;
import im.helmsman.lib.property.ComapassProperty;
import im.helmsman.lib.property.GpsProperty;
import im.helmsman.lib.property.WifiProperty;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public class HMMotor50736 extends ABaseProduct implements AComponentModel.OnConnectStateChangeListener {
    public static final int BATTERY_STATE_FULL = 4;
    public static final int BATTERY_STATE_LOW = 1;
    public static final int BATTERY_STATE_NULL = 0;
    public static final int BATTERY_STATE_OK = 2;
    public static final int BOAT_MODE_ANCHOR = 12;
    public static final int BOAT_MODE_CRUISE = 14;
    public static final int BOAT_MODE_MANAUL = 13;
    public static final int BOAT_MODE_NAVIGATION = 15;
    public static final int BOAT_MODE_RECORD_WAYPOINT = 181;
    public static final int BOAT_MODE_REMOTECONTROL = 6;
    public static final int BOAT_MODE_UNKNOW = 5;
    public static final int BOAT_PEDAL_MODEL = 7;
    public static final int NAVI_MODE_ACCURATE = 1;
    public static final int NAVI_MODE_STANDARD = 0;
    private float lastSpeed;
    private long recevieBoatControlDataTime;
    private Timer timer;

    public HMMotor50736(int i, int i2) {
        super(i, i2);
        this.lastSpeed = 0.0f;
        this.recevieBoatControlDataTime = 0L;
        initComponent();
        TeleControlComponent.WifiLinker.onTeleControlScanwifiListener = dataManager;
        BoatControlComponent.onUpLoadRouteListener = dataManager;
        BoatControlComponent.onDownLoadRouteListener = dataManager;
        this.boatControlComponent.setOnConnectStateChange(this);
        Log.e("conscruct", "123");
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // im.helmsman.lib.ABaseProduct
    public void onReceiverData(MAVLinkMessage mAVLinkMessage) {
        char c;
        int i = mAVLinkMessage.msgid;
        if (i == 0) {
            msg_heartbeat msg_heartbeatVar = (msg_heartbeat) mAVLinkMessage;
            setType(msg_heartbeatVar.type);
            setFirwareVersion((int) msg_heartbeatVar.custom_mode);
            setDisconnectProtection(msg_heartbeatVar.base_mode);
        } else if (i != 1) {
            String str = "";
            if (i == 7) {
                byte[] bArr = ((msg_auth_key) mAVLinkMessage).key;
                for (int i2 = 0; i2 < 9 && (c = (char) bArr[i2]) != 0; i2++) {
                    str = str + c;
                }
                this.boatControlComponent.wifiProperty.setSsid(str);
            } else if (i == 27) {
                msg_raw_imu msg_raw_imuVar = (msg_raw_imu) mAVLinkMessage;
                this.boatControlComponent.gpsProperty.setSignal(msg_raw_imuVar.ygyro);
                this.boatControlComponent.setTargetPoint(msg_raw_imuVar.zacc);
                this.motorComponent.setSpeed(msg_raw_imuVar.zgyro);
                this.motorComponent.setOn(msg_raw_imuVar.xgyro == 11);
            } else if (i == 30) {
                msg_attitude msg_attitudeVar = (msg_attitude) mAVLinkMessage;
                float f = msg_attitudeVar.yaw;
                Log.e("MAVLINK_MSG_ID_ATTITUDE", msg_attitudeVar.yaw + "");
                this.boatControlComponent.setBoatDegress(f);
                this.boatControlComponent.setElectricityLevel((int) msg_attitudeVar.pitch);
                this.boatControlComponent.setBoatModel((int) msg_attitudeVar.pitchspeed);
            } else if (i == 33) {
                msg_global_position_int msg_global_position_intVar = (msg_global_position_int) mAVLinkMessage;
                this.boatControlComponent.gpsProperty.setLatitude(longToFloat(msg_global_position_intVar.lat));
                this.boatControlComponent.gpsProperty.setLongitude(longToFloat(msg_global_position_intVar.lon));
                float f2 = (msg_global_position_intVar.vx * 3.6f) / 100.0f;
                float f3 = (msg_global_position_intVar.vy * 3.6f) / 100.0f;
                float fSqrt = (float) ((((double) this.lastSpeed) * 0.45d) + (((double) ((float) Math.sqrt((f2 * f2) + (f3 * f3)))) * 0.55d));
                if (fSqrt < 0.5d) {
                    fSqrt = 0.0f;
                }
                this.lastSpeed = fSqrt;
                this.boatControlComponent.setBoatSpeed(fSqrt);
            } else if (i == 44) {
                msg_mission_count msg_mission_countVar = (msg_mission_count) mAVLinkMessage;
                if (msg_mission_countVar.compid == 99) {
                    Log.e("download_ssid_count", msg_mission_countVar.count + "");
                    TeleControlComponent.WifiLinker.onTeleControlScanwifiListener.ssidCount(msg_mission_countVar.count);
                } else if (BoatControlComponent.onDownLoadRouteListener != null) {
                    BoatControlComponent.onDownLoadRouteListener.downloadWaypointCount(msg_mission_countVar.count);
                }
            } else if (i == 47) {
                msg_mission_ack msg_mission_ackVar = (msg_mission_ack) mAVLinkMessage;
                if (msg_mission_ackVar.compid == 99 && TeleControlComponent.WifiLinker.onTeleControlScanwifiListener != null) {
                    TeleControlComponent.WifiLinker wifiLinker = this.teleControlComponent.wifiLinker;
                    TeleControlComponent.WifiLinker.onTeleControlScanwifiListener.connectWifiSuccess();
                    Log.e("connect_success", msg_mission_ackVar + "");
                } else if (BoatControlComponent.onUpLoadRouteListener != null) {
                    BoatControlComponent.onUpLoadRouteListener.onUploadComplete();
                }
            } else if (i == 249) {
                msg_memory_vect msg_memory_vectVar = (msg_memory_vect) mAVLinkMessage;
                StringBuilder sb = new StringBuilder();
                for (int i3 = 0; i3 < msg_memory_vectVar.type; i3++) {
                    sb.append((char) msg_memory_vectVar.value[i3]);
                }
                TeleControlComponent.WifiLinker wifiLinker2 = this.teleControlComponent.wifiLinker;
                if (TeleControlComponent.WifiLinker.onTeleControlScanwifiListener != null) {
                    TeleControlComponent.WifiLinker wifiLinker3 = this.teleControlComponent.wifiLinker;
                    TeleControlComponent.WifiLinker.onTeleControlScanwifiListener.scanResult(sb.toString(), msg_memory_vectVar.ver);
                }
            } else if (i == 39) {
                msg_mission_item msg_mission_itemVar = (msg_mission_item) mAVLinkMessage;
                if (BoatControlComponent.onDownLoadRouteListener != null) {
                    BoatControlComponent.onDownLoadRouteListener.downloadWaypoint(msg_mission_itemVar.seq, msg_mission_itemVar.x, msg_mission_itemVar.y);
                }
            } else if (i == 40) {
                msg_mission_request msg_mission_requestVar = (msg_mission_request) mAVLinkMessage;
                if (BoatControlComponent.onUpLoadRouteListener != null) {
                    BoatControlComponent.onUpLoadRouteListener.onUploadProgress(msg_mission_requestVar.seq);
                }
            }
        } else {
            this.teleControlComponent.setBatteryElectricityLevel(((msg_sys_status) mAVLinkMessage).voltage_battery);
        }
        if (this.timer == null) {
            Timer timer = new Timer();
            this.timer = timer;
            timer.schedule(new IsConnectedTimer(), 0L, 5000L);
        }
        if (mAVLinkMessage.msgid != 1) {
            this.recevieBoatControlDataTime = System.currentTimeMillis();
        }
        dataManager.dataChange(this);
    }

    @Override // im.helmsman.lib.ABaseProduct
    public void initComponent() {
        this.motorComponent = new MotorDriverComponent50736(0, 0);
        this.boatControlComponent = new BoatControlComponent50736(0, 0);
        this.teleControlComponent = HelmsmanSDK.getTeleControlComponent();
    }

    @Override // im.helmsman.lib.AComponentModel.OnConnectStateChangeListener
    public void onStateChange(int i) {
        dataManager.dataChange(this);
    }

    public static class MotorDriverComponent50736 extends MotorDriverComponent {
        public float pwm;

        public void setPwm(float f) {
            this.pwm = f;
        }

        public MotorDriverComponent50736(int i, int i2) {
            super(i, i2);
            this.pwm = 90.0f;
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void speedUp() {
            msg_command_long msg_command_longVar = new msg_command_long();
            msg_command_longVar.command = 8;
            HMMotor50736.manager.sendData(msg_command_longVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void speedDown() {
            msg_command_long msg_command_longVar = new msg_command_long();
            msg_command_longVar.command = 9;
            HMMotor50736.manager.sendData(msg_command_longVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void motorOn() {
            msg_command_long msg_command_longVar = new msg_command_long();
            msg_command_longVar.command = 10;
            HMMotor50736.manager.sendData(msg_command_longVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void motorOff() {
            msg_command_long msg_command_longVar = new msg_command_long();
            msg_command_longVar.command = 11;
            HMMotor50736.manager.sendData(msg_command_longVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void maxSpeed() {
            msg_command_long msg_command_longVar = new msg_command_long();
            msg_command_longVar.command = 3;
            HMMotor50736.manager.sendData(msg_command_longVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void turnRight() {
            msg_command_long msg_command_longVar = new msg_command_long();
            if (HelmsmanSDK.getProduct().getFirwareVersion() == 102) {
                msg_command_longVar.param1 = this.pwm;
            }
            msg_command_longVar.compid = HelmsmanSDK.getProduct().getFirwareVersion();
            msg_command_longVar.command = 6;
            Log.e("turnRight", msg_command_longVar.toString());
            HMMotor50736.manager.sendData(msg_command_longVar);
        }

        @Override // im.helmsman.lib.component.MotorDriverComponent
        public void turnLeft() {
            msg_command_long msg_command_longVar = new msg_command_long();
            if (HelmsmanSDK.getProduct().getFirwareVersion() == 102) {
                msg_command_longVar.param1 = this.pwm;
            }
            msg_command_longVar.command = 7;
            Log.e("turnLeft", msg_command_longVar.toString());
            HMMotor50736.manager.sendData(msg_command_longVar);
        }
    }

    public static class BoatControlComponent50736 extends BoatControlComponent {
        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendAckToBoatControl() {
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendRequestFirmwareInfo() {
        }

        public static class WifiProperty50736 extends WifiProperty {
            @Override // im.helmsman.lib.property.WifiProperty
            public void changeWifiPassword(String str) {
                msg_auth_key msg_auth_keyVar = new msg_auth_key();
                char[] charArray = str.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                    msg_auth_keyVar.key[i] = (byte) charArray[i];
                }
                HMMotor50736.manager.sendData(msg_auth_keyVar);
            }
        }

        public BoatControlComponent50736(int i, int i2) {
            super(i, i2, new GpsProperty(), new ComapassProperty(), new WifiProperty50736());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void changeModel(int i) {
            msg_command_long msg_command_longVar = new msg_command_long();
            msg_command_longVar.command = i;
            HMMotor50736.manager.sendData(msg_command_longVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendHeartBeat() {
            msg_heartbeat msg_heartbeatVar = new msg_heartbeat();
            msg_heartbeatVar.mavlink_version = (short) 3;
            msg_heartbeatVar.type = (short) 106;
            HMMotor50736.manager.sendData(msg_heartbeatVar);
            Log.d("sendHeartBeat", "sendHeartBeat");
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void sendClearCacheCommand() {
            HMMotor50736.manager.sendData(new msg_mission_clear_all());
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void uploadWaypointRequest(int i) {
            msg_mission_count msg_mission_countVar = new msg_mission_count();
            msg_mission_countVar.count = i;
            HMMotor50736.manager.sendData(msg_mission_countVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void upLoadWayPount(float f, float f2, int i) {
            msg_mission_item msg_mission_itemVar = new msg_mission_item();
            msg_mission_itemVar.x = f;
            msg_mission_itemVar.y = f2;
            msg_mission_itemVar.target_system = (short) 106;
            msg_mission_itemVar.seq = i;
            HMMotor50736.manager.sendData(msg_mission_itemVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void requestDownLoadRoute() {
            MAVLinkPacket mAVLinkPacketPack = new msg_mission_request_list().pack();
            mAVLinkPacketPack.compid = 200;
            mAVLinkPacketPack.sysid = 106;
            HMMotor50736.manager.sendData(mAVLinkPacketPack);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void downloadWaypointBySeq(int i) {
            msg_mission_request msg_mission_requestVar = new msg_mission_request();
            msg_mission_requestVar.seq = i;
            HMMotor50736.manager.sendData(msg_mission_requestVar);
        }

        @Override // im.helmsman.lib.component.BoatControlComponent
        public void setNaviMode(int i) {
            if (i == 0) {
                HMMotor50736.manager.sendData("STO,1,12,30,4.00,0.007,0.80,12,15,1.50,2.00,20,".getBytes());
            } else if (i == 1) {
                HMMotor50736.manager.sendData("STO,1,20,30,4.00,0.007,0.80,12,15,1.50,2.00,10,".getBytes());
            }
        }
    }

    public float longToFloat(long j) {
        if (j == 0) {
            return 0.0f;
        }
        StringBuffer stringBuffer = new StringBuffer(Long.toString(j));
        stringBuffer.insert(stringBuffer.length() - 7, ".");
        return Float.valueOf(stringBuffer.toString()).floatValue();
    }

    class IsConnectedTimer extends TimerTask {
        IsConnectedTimer() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (System.currentTimeMillis() - HMMotor50736.this.recevieBoatControlDataTime >= 5000) {
                cancel();
                HMMotor50736.this.timer = null;
                HMMotor50736.this.boatControlComponent.setConnectState(1);
                return;
            }
            HMMotor50736.this.boatControlComponent.setConnectState(0);
        }
    }
}
