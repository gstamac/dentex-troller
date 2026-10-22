package im.helmsman.lib.component;

import android.util.Log;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.common.msg_memory_vect;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_count;
import com.MAVLink.common.msg_mission_request;
import com.MAVLink.common.msg_mission_request_list;
import im.helmsman.lib.AComponentModel;
import im.helmsman.lib.DataManager;
import im.helmsman.lib.HelmsmanSDK;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class TeleControlComponent extends AComponentModel {
    private static OnTeleControlComponentCreateListener onteleControlComponentCreateListener;
    private static TeleControlComponent teleControlComponent;
    private int batteryElectricityLevel;
    public WifiLinker wifiLinker;

    public interface OnTeleControlComponentCreateListener {
        void onTeleControlComponentCreate(TeleControlComponent teleControlComponent);
    }

    private TeleControlComponent(int i) {
        super(0, 0);
        this.compId = i;
        if (i == 99) {
            this.wifiLinker = new WifiLinker(true);
        }
        teleControlComponent = this;
        OnTeleControlComponentCreateListener onTeleControlComponentCreateListener = onteleControlComponentCreateListener;
        if (onTeleControlComponentCreateListener != null) {
            onTeleControlComponentCreateListener.onTeleControlComponentCreate(this);
            onteleControlComponentCreateListener = null;
        }
    }

    public static TeleControlComponent getTeleControlComponentInstance() {
        return teleControlComponent;
    }

    public static TeleControlComponent getTeleControlComponentInstance(int i) {
        TeleControlComponent teleControlComponent2 = teleControlComponent;
        if (teleControlComponent2 == null || teleControlComponent2.compId != i) {
            teleControlComponent = new TeleControlComponent(i);
        }
        return teleControlComponent;
    }

    public int getBatteryElectricityLevel() {
        return this.batteryElectricityLevel;
    }

    public void setBatteryElectricityLevel(int i) {
        this.batteryElectricityLevel = i;
    }

    @Override // im.helmsman.lib.AComponentModel
    public void decode(MAVLinkMessage mAVLinkMessage) {
        int i = mAVLinkMessage.msgid;
        if (i == 44) {
            WifiLinker.onTeleControlScanwifiListener.ssidCount(((msg_mission_count) mAVLinkMessage).count);
            return;
        }
        if (i == 47) {
            Log.e("connect_success", ((msg_mission_ack) mAVLinkMessage) + "");
            if (WifiLinker.onTeleControlScanwifiListener != null) {
                WifiLinker.onTeleControlScanwifiListener.connectWifiSuccess();
            }
            if (this.wifiLinker.onConnectWifiCompleteListener != null) {
                this.wifiLinker.onConnectWifiCompleteListener.onconnectSuccess();
                return;
            }
            return;
        }
        if (i != 249) {
            return;
        }
        msg_memory_vect msg_memory_vectVar = (msg_memory_vect) mAVLinkMessage;
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < msg_memory_vectVar.type; i2++) {
            sb.append((char) msg_memory_vectVar.value[i2]);
        }
        WifiLinker wifiLinker = teleControlComponent.wifiLinker;
        if (WifiLinker.onTeleControlScanwifiListener != null) {
            WifiLinker wifiLinker2 = teleControlComponent.wifiLinker;
            WifiLinker.onTeleControlScanwifiListener.scanResult(sb.toString(), msg_memory_vectVar.ver);
        }
    }

    public static void setOnTeleControlComponentCreateListener(OnTeleControlComponentCreateListener onTeleControlComponentCreateListener) {
        onteleControlComponentCreateListener = onTeleControlComponentCreateListener;
    }

    public static class WifiLinker {
        public static OnTeleControlScanWifiListener onTeleControlScanwifiListener;
        private boolean isConnectWifi;
        private boolean isSupportLinkWifi;
        private OnConnectWifiCompleteListener onConnectWifiCompleteListener;
        private List<String> ssid;
        private int ssidCount;

        public interface OnConnectWifiCompleteListener {
            void onconnectSuccess();
        }

        public interface OnTeleControlScanWifiListener {
            void connectWifiSuccess();

            void scanResult(String str, int i);

            void ssidCount(int i);
        }

        public static abstract class TeleControlComponentDataChangeAdapter implements DataManager.DataChangeObserver {
            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void batteryElectricityLevel(int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void boatControlModel(int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void boatDegress(float f) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void boatPosition(float f, float f2) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void boatSpeed(float f) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void deviceInfo(int i, int i2, int i3, String str) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void gpsSignal(int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void motorState(boolean z, int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void onBoatControlConnectStateChange(int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void routeDownloadWaypoint(float f, float f2, int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void routeDownloadWaypointCount(int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void routeUploadComplete() {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void routeUploadRequestWaypoint(int i) {
            }

            @Override // im.helmsman.lib.DataManager.DataChangeObserver
            public void targeterPoint(int i) {
            }
        }

        public WifiLinker(boolean z) {
            this.isConnectWifi = false;
            this.ssidCount = 0;
            this.isSupportLinkWifi = z;
        }

        public WifiLinker() {
            this.isSupportLinkWifi = false;
            this.isConnectWifi = false;
            this.ssidCount = 0;
        }

        public boolean isSupportScanWifi() {
            return this.isSupportLinkWifi;
        }

        public void setSupportLinkWifi(boolean z) {
            this.isSupportLinkWifi = z;
        }

        public void setOnConnectWifiCompleteListener(OnConnectWifiCompleteListener onConnectWifiCompleteListener) {
            this.onConnectWifiCompleteListener = onConnectWifiCompleteListener;
        }

        public boolean isConnectWifi() {
            return this.isConnectWifi;
        }

        public void setConnectWifi(boolean z) {
            this.isConnectWifi = z;
        }

        public List<String> getSsid() {
            return this.ssid;
        }

        public void setSsid(List<String> list) {
            this.ssid = list;
        }

        public void scanWifiSsid() {
            MAVLinkPacket mAVLinkPacketPack = new msg_mission_request_list().pack();
            mAVLinkPacketPack.compid = 99;
            HelmsmanSDK.getDataIOManager().sendData(mAVLinkPacketPack);
        }

        public void requestSsid(int i) {
            msg_mission_request msg_mission_requestVar = new msg_mission_request();
            msg_mission_requestVar.seq = i;
            MAVLinkPacket mAVLinkPacketPack = msg_mission_requestVar.pack();
            mAVLinkPacketPack.compid = 99;
            HelmsmanSDK.getDataIOManager().sendData(mAVLinkPacketPack);
        }

        public void ConnectWifi(String str, String str2) {
            String str3 = str + "," + str2 + ",";
            if (str3.length() > 32) {
                throw new RuntimeException("密码+ssid的长度超过31，pwd" + str2 + "ssid," + str);
            }
            msg_memory_vect msg_memory_vectVar = new msg_memory_vect();
            char[] charArray = str3.toCharArray();
            int[] iArr = new int[32];
            for (int i = 0; i < charArray.length; i++) {
                iArr[i] = charArray[i];
            }
            byte[] bArr = new byte[charArray.length];
            for (int i2 = 0; i2 < charArray.length; i2++) {
                bArr[i2] = (byte) charArray[i2];
            }
            msg_memory_vectVar.value = bArr;
            MAVLinkPacket mAVLinkPacketPack = msg_memory_vectVar.pack();
            mAVLinkPacketPack.compid = 99;
            HelmsmanSDK.getDataIOManager().sendData(mAVLinkPacketPack);
        }

        public int getSsidCount() {
            return this.ssidCount;
        }

        public void setSsidCount(int i) {
            this.ssidCount = i;
        }
    }
}
