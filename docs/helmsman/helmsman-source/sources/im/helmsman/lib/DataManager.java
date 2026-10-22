package im.helmsman.lib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import im.helmsman.lib.component.BoatControlComponent;
import im.helmsman.lib.component.TeleControlComponent;
import im.helmsman.lib.product.HMMotor50736;
import im.helmsman.lib.product.HMMotorH1;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DataManager implements TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener, BoatControlComponent.OnUpLoadRouteListener, BoatControlComponent.OnDownLoadRouteListener, HMMotorH1.AidDataListener, HMMotorH1.BoatControlComponentH1.FirmwareUpdateListener {
    public static DataManager manager;
    private int batteryElectricityLevel;
    private int boatControlcomponentConnectState;
    private float boatDegress;
    private int boatModel;
    private float boatSpeed;
    private int disconnectProtect;
    private int firware;
    private int gpsSignal;
    private boolean isOn;
    private float lat;
    private float lon;
    private int motorSpeed;
    private String ssid;
    private int target;
    private int type;
    private boolean teleControlIsConnectWifi = false;
    private int ssidCount = 0;
    private int tempBoatControlcomponentConnectState = -2;
    private List<DataChangeObserver> observerList = DataChangeSubscribe.getSubList();
    private Handler handler = new Handler(Looper.getMainLooper());

    public interface DataChangeObserver {
        void aidAck();

        void batteryElectricityLevel(int i);

        void boatControlModel(int i);

        void boatDegress(float f);

        void boatPosition(float f, float f2);

        void boatSpeed(float f);

        void connectWifiComplete();

        void deviceInfo(int i, int i2, int i3, String str);

        void firmwareAck();

        void gpsSignal(int i);

        void motorState(boolean z, int i);

        void onBoatControlConnectStateChange(int i);

        void requestAidData(long j, short s);

        void requestFirmwareData(long j, int i);

        void routeDownloadWaypoint(float f, float f2, int i);

        void routeDownloadWaypointCount(int i);

        void routeUploadComplete();

        void routeUploadRequestWaypoint(int i);

        void scanWifiCount(int i);

        void scanWifiSsidResult(String str, int i);

        void targeterPoint(int i);

        void teleControlBatteryElectricityLevel(int i);
    }

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (manager == null) {
            synchronized (DataManager.class) {
                if (manager == null) {
                    manager = new DataManager();
                }
            }
        }
        return manager;
    }

    public void dataChange(ABaseProduct aBaseProduct) {
        int type = aBaseProduct.getType();
        if (type == 11) {
            dataChangeH1((HMMotorH1) aBaseProduct);
        } else {
            if (type != 110) {
                return;
            }
            dataChange50736((HMMotor50736) aBaseProduct);
        }
    }

    private void dataChange50736(HMMotor50736 hMMotor50736) {
        this.batteryElectricityLevel = hMMotor50736.boatControlComponent.getElectricityLevel();
        this.boatDegress = hMMotor50736.boatControlComponent.getBoatDegress();
        this.boatSpeed = hMMotor50736.boatControlComponent.getBoatSpeed();
        this.gpsSignal = hMMotor50736.boatControlComponent.gpsProperty.getSignal();
        this.boatModel = hMMotor50736.boatControlComponent.getBoatModel();
        this.lat = hMMotor50736.boatControlComponent.gpsProperty.getLatitude();
        this.lon = hMMotor50736.boatControlComponent.gpsProperty.getLongitude();
        this.motorSpeed = hMMotor50736.motorComponent.getSpeed();
        this.target = hMMotor50736.boatControlComponent.getTargetPoint();
        this.isOn = hMMotor50736.motorComponent.isOn();
        this.boatControlcomponentConnectState = hMMotor50736.boatControlComponent.getConnectState();
        this.ssid = hMMotor50736.boatControlComponent.wifiProperty.getSsid();
        this.firware = hMMotor50736.getFirwareVersion();
        this.type = hMMotor50736.getType();
        notifiAllObserver();
    }

    private void dataChangeH1(HMMotorH1 hMMotorH1) {
        this.batteryElectricityLevel = hMMotorH1.boatControlComponent.getElectricityLevel();
        this.boatDegress = hMMotorH1.boatControlComponent.getBoatDegress();
        this.boatSpeed = hMMotorH1.boatControlComponent.getBoatSpeed();
        this.gpsSignal = hMMotorH1.boatControlComponent.gpsProperty.getSignal();
        this.boatModel = hMMotorH1.boatControlComponent.getBoatModel();
        this.lat = hMMotorH1.boatControlComponent.gpsProperty.getLatitude();
        this.lon = hMMotorH1.boatControlComponent.gpsProperty.getLongitude();
        this.motorSpeed = hMMotorH1.motorComponent.getSpeed();
        this.target = hMMotorH1.boatControlComponent.getTargetPoint();
        this.isOn = hMMotorH1.motorComponent.isOn();
        this.boatControlcomponentConnectState = hMMotorH1.boatControlComponent.getConnectState();
        notifiAllObserver();
    }

    private void notifiAllObserver() {
        this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (DataManager.this.observerList) {
                    for (DataChangeObserver dataChangeObserver : DataManager.this.observerList) {
                        dataChangeObserver.targeterPoint(DataManager.this.target);
                        dataChangeObserver.motorState(DataManager.this.isOn, DataManager.this.motorSpeed);
                        dataChangeObserver.batteryElectricityLevel(DataManager.this.batteryElectricityLevel);
                        dataChangeObserver.boatDegress(DataManager.this.boatDegress);
                        dataChangeObserver.boatControlModel(DataManager.this.boatModel);
                        dataChangeObserver.boatPosition(DataManager.this.lat, DataManager.this.lon);
                        dataChangeObserver.boatSpeed(DataManager.this.boatSpeed);
                        dataChangeObserver.gpsSignal(DataManager.this.gpsSignal);
                        if (DataManager.this.tempBoatControlcomponentConnectState != DataManager.this.boatControlcomponentConnectState) {
                            dataChangeObserver.onBoatControlConnectStateChange(DataManager.this.boatControlcomponentConnectState);
                            DataManager dataManager = DataManager.this;
                            dataManager.tempBoatControlcomponentConnectState = dataManager.boatControlcomponentConnectState;
                        }
                        dataChangeObserver.deviceInfo(DataManager.this.firware, 0, DataManager.this.type, DataManager.this.ssid);
                    }
                }
            }
        });
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener
    public void scanResult(final String str, final int i) {
        synchronized (this.observerList) {
            this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.2
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = DataManager.this.observerList.iterator();
                    while (it.hasNext()) {
                        ((DataChangeObserver) it.next()).scanWifiSsidResult(str, i);
                    }
                }
            });
        }
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener
    public void ssidCount(final int i) {
        synchronized (this.observerList) {
            this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.3
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = DataManager.this.observerList.iterator();
                    while (it.hasNext()) {
                        ((DataChangeObserver) it.next()).scanWifiCount(i);
                    }
                }
            });
        }
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener
    public void connectWifiSuccess() {
        synchronized (this.observerList) {
            this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.4
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = DataManager.this.observerList.iterator();
                    while (it.hasNext()) {
                        ((DataChangeObserver) it.next()).connectWifiComplete();
                    }
                }
            });
        }
    }

    @Override // im.helmsman.lib.component.BoatControlComponent.OnUpLoadRouteListener
    public void onUploadComplete() {
        synchronized (this.observerList) {
            this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.5
                @Override // java.lang.Runnable
                public void run() {
                    for (int i = 0; i < DataManager.this.observerList.size(); i++) {
                        try {
                            ((DataChangeObserver) DataManager.this.observerList.get(i)).routeUploadComplete();
                        } catch (ConcurrentModificationException e) {
                            e.printStackTrace();
                            Log.e("Thread", Thread.currentThread().getName());
                            return;
                        }
                    }
                }
            });
        }
    }

    @Override // im.helmsman.lib.component.BoatControlComponent.OnUpLoadRouteListener
    public void onUploadProgress(final int i) {
        synchronized (this.observerList) {
            this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.6
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = DataManager.this.observerList.iterator();
                    while (it.hasNext()) {
                        ((DataChangeObserver) it.next()).routeUploadRequestWaypoint(i);
                    }
                }
            });
        }
    }

    @Override // im.helmsman.lib.component.BoatControlComponent.OnDownLoadRouteListener
    public void downloadWaypoint(final int i, final float f, final float f2) {
        synchronized (this.observerList) {
            this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.7
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = DataManager.this.observerList.iterator();
                    while (it.hasNext()) {
                        ((DataChangeObserver) it.next()).routeDownloadWaypoint(f, f2, i);
                    }
                }
            });
        }
    }

    @Override // im.helmsman.lib.component.BoatControlComponent.OnDownLoadRouteListener
    public void downloadWaypointCount(final int i) {
        synchronized (this.observerList) {
            this.handler.post(new Runnable() { // from class: im.helmsman.lib.DataManager.8
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = DataManager.this.observerList.iterator();
                    while (it.hasNext()) {
                        ((DataChangeObserver) it.next()).routeDownloadWaypointCount(i);
                    }
                }
            });
        }
    }

    @Override // im.helmsman.lib.product.HMMotorH1.AidDataListener
    public void onRequestAidData(long j, short s) {
        synchronized (this.observerList) {
            Iterator<DataChangeObserver> it = this.observerList.iterator();
            while (it.hasNext()) {
                it.next().requestAidData(j, s);
            }
        }
    }

    @Override // im.helmsman.lib.product.HMMotorH1.AidDataListener
    public void onAck() {
        synchronized (this.observerList) {
            Iterator<DataChangeObserver> it = this.observerList.iterator();
            while (it.hasNext()) {
                it.next().aidAck();
            }
        }
    }

    @Override // im.helmsman.lib.product.HMMotorH1.BoatControlComponentH1.FirmwareUpdateListener
    public void onRequestFirmwareData(long j, int i) {
        synchronized (this.observerList) {
            Iterator<DataChangeObserver> it = this.observerList.iterator();
            while (it.hasNext()) {
                it.next().requestFirmwareData(j, i);
            }
        }
    }

    @Override // im.helmsman.lib.product.HMMotorH1.BoatControlComponentH1.FirmwareUpdateListener
    public void onFirmwareAck() {
        synchronized (this.observerList) {
            Iterator<DataChangeObserver> it = this.observerList.iterator();
            while (it.hasNext()) {
                it.next().firmwareAck();
            }
        }
    }
}
