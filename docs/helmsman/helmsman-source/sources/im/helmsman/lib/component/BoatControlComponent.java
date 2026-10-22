package im.helmsman.lib.component;

import com.MAVLink.Messages.MAVLinkMessage;
import im.helmsman.lib.AComponentModel;
import im.helmsman.lib.DataManager;
import im.helmsman.lib.property.ComapassProperty;
import im.helmsman.lib.property.GpsProperty;
import im.helmsman.lib.property.WifiProperty;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BoatControlComponent extends AComponentModel {
    public static OnDownLoadRouteListener onDownLoadRouteListener;
    public static OnUpLoadRouteListener onUpLoadRouteListener;
    private int ElectricityLevel;
    private float boatDegress;
    private int boatModel;
    private float boatSpeed;
    public ComapassProperty comapassProperty;
    public GpsProperty gpsProperty;
    private int targetPoint;
    public WifiProperty wifiProperty;

    public static abstract class BoatControlComponentDataChangeAdapter implements DataManager.DataChangeObserver {
        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void connectWifiComplete() {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void motorState(boolean z, int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void requestAidData(long j, short s) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void scanWifiCount(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void scanWifiSsidResult(String str, int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void teleControlBatteryElectricityLevel(int i) {
        }
    }

    public interface OnDownLoadRouteListener {
        void downloadWaypoint(int i, float f, float f2);

        void downloadWaypointCount(int i);
    }

    public interface OnUpLoadRouteListener {
        void onUploadComplete();

        void onUploadProgress(int i);
    }

    public void activationDevice(long j) {
    }

    public abstract void changeModel(int i);

    @Override // im.helmsman.lib.AComponentModel
    public void decode(MAVLinkMessage mAVLinkMessage) {
    }

    public abstract void downloadWaypointBySeq(int i);

    public void readSerial() {
    }

    public abstract void requestDownLoadRoute();

    public abstract void sendAckToBoatControl();

    public abstract void sendClearCacheCommand();

    public abstract void sendHeartBeat();

    public abstract void sendRequestFirmwareInfo();

    public abstract void setNaviMode(int i);

    public abstract void upLoadWayPount(float f, float f2, int i);

    public abstract void uploadWaypointRequest(int i);

    public BoatControlComponent(int i, int i2, GpsProperty gpsProperty, ComapassProperty comapassProperty, WifiProperty wifiProperty) {
        super(i, i2);
        this.gpsProperty = gpsProperty;
        this.wifiProperty = wifiProperty;
        this.comapassProperty = comapassProperty;
    }

    public int getBoatModel() {
        return this.boatModel;
    }

    public void setBoatModel(int i) {
        this.boatModel = i;
    }

    public float getBoatSpeed() {
        return this.boatSpeed;
    }

    public void setBoatSpeed(float f) {
        this.boatSpeed = f;
    }

    public int getTargetPoint() {
        return this.targetPoint;
    }

    public void setTargetPoint(int i) {
        this.targetPoint = i;
    }

    public int getElectricityLevel() {
        return this.ElectricityLevel;
    }

    public void setElectricityLevel(int i) {
        this.ElectricityLevel = i;
    }

    public float getBoatDegress() {
        return this.boatDegress;
    }

    public void setBoatDegress(float f) {
        this.boatDegress = f;
    }
}
