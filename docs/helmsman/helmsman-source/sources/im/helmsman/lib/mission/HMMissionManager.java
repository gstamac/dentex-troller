package im.helmsman.lib.mission;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.zhy.http.okhttp.OkHttpUtils;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.DataChangeSubscribe;
import im.helmsman.lib.DataManager;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.component.TeleControlComponent;
import im.helmsman.lib.error.HMError;
import im.helmsman.lib.mission.common.HMMission;
import im.helmsman.lib.net.NetUtils;
import im.helmsman.lib.product.HMMotorH1;
import im.helmsman.lib.util.HMCommonCallback;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public class HMMissionManager implements TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener {
    private static HMMissionManager hMMissionManager;
    private HMCommonCallback.HMCompletionCallback callback;
    private Timer downloadCountTimeOutTimer;
    private HMMission.HMMissionDownloadProgress downloadProgress;
    private DownloadRoutTimeOutRunnable downloadRoutTimeOutRunnable;
    private Timer downloadWaypointTimeOutTimer;
    private Handler handler;
    private HMMission mission;
    private BoatControlObserver observer;
    private HMMission.HMMissionProgressHandler progressHandler;
    private Runnable retryUploadRunnable;
    private int ssidCount;
    private TimeOutRunnable timeOutRunnable;
    protected Runnable uploadtimeoutRunnable;
    private int requestWaypointSeq = 0;
    private int retryuploadTime = 0;

    public void pauseMissionExecution(HMCommonCallback.HMCompletionCallback hMCompletionCallback) {
    }

    static /* synthetic */ int access$408(HMMissionManager hMMissionManager2) {
        int i = hMMissionManager2.retryuploadTime;
        hMMissionManager2.retryuploadTime = i + 1;
        return i;
    }

    public static HMMissionManager getInstance() {
        if (hMMissionManager == null) {
            synchronized (HMMissionManager.class) {
                if (hMMissionManager == null) {
                    hMMissionManager = new HMMissionManager();
                }
            }
        }
        return hMMissionManager;
    }

    private HMMissionManager() {
        Log.d("HMMissionManager", "new HMMissionManager");
    }

    /* JADX WARN: Type inference failed for: r2v5, types: [im.helmsman.lib.mission.HMMissionManager$1] */
    public void prepareMission(HMMission hMMission, HMMission.HMMissionProgressHandler hMMissionProgressHandler, HMCommonCallback.HMCompletionCallback hMCompletionCallback) {
        Log.e("prepareMission", "prepareMission");
        this.progressHandler = hMMissionProgressHandler;
        this.mission = hMMission;
        this.callback = hMCompletionCallback;
        if (this.handler == null) {
            this.handler = new Handler(Looper.getMainLooper());
        }
        switch (AnonymousClass6.$SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[hMMission.getMissionType().ordinal()]) {
            case 1:
                List<HMWayPoint> hmWayPoints = ((HMWayPointMission) hMMission).getHmWayPoints();
                this.retryuploadTime = 0;
                uploadWayPointToBoat(hmWayPoints);
                break;
            case 2:
                anchor();
                break;
            case 3:
                record();
                break;
            case 4:
                navigation();
                break;
            case 5:
                cruise();
                break;
            case 6:
                sendRequestFirmwareInfo();
                break;
            case 7:
                new Thread() { // from class: im.helmsman.lib.mission.HMMissionManager.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        HMMissionManager.this.updateEphemerisData();
                    }
                }.start();
                break;
            case 8:
                this.handler = new Handler(Looper.getMainLooper());
                sendFirmwareUpdate();
                break;
        }
        BoatControlObserver boatControlObserver = this.observer;
        if (boatControlObserver != null) {
            DataChangeSubscribe.unSubscrible(boatControlObserver);
        }
        BoatControlObserver boatControlObserver2 = new BoatControlObserver();
        this.observer = boatControlObserver2;
        DataChangeSubscribe.subscribe(boatControlObserver2);
    }

    /* JADX INFO: renamed from: im.helmsman.lib.mission.HMMissionManager$6, reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType;

        static {
            int[] iArr = new int[HMMission.MissionType.values().length];
            $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType = iArr;
            try {
                iArr[HMMission.MissionType.WAYPOINT_TYPE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.ANCHOR_TYPE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.RECORD_TYPE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.NAVIGATION_TYPE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.CRUISE_TYPE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.FIRMWAREINFO_TYPE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.EPHEMERIS_DATA_TYPE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.UPDATE_BOATCONTROL_FIRMWARE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[HMMission.MissionType.TELECONTROL_SCANWIFI_TYPE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    class TimeOutRunnable implements Runnable {
        TimeOutRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (HMMissionManager.this.callback != null) {
                HMMissionManager.this.callback.onResult(new HMError(-1));
            }
            if (HMMissionManager.this.uploadtimeoutRunnable != null) {
                HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.uploadtimeoutRunnable);
            }
        }
    }

    private void sendFirmwareUpdate() {
        HMUpdateBoatControlFirmwareMission hMUpdateBoatControlFirmwareMission = (HMUpdateBoatControlFirmwareMission) this.mission;
        HMMotorH1.BoatControlComponentH1.sendFrimwareUpdate(hMUpdateBoatControlFirmwareMission.getBuild(), hMUpdateBoatControlFirmwareMission.getVersion(), hMUpdateBoatControlFirmwareMission.getFirmwaredatas().length);
    }

    public void downloadMissionExecution(HMMission hMMission, HMMission.HMMissionDownloadProgress hMMissionDownloadProgress) {
        this.downloadProgress = hMMissionDownloadProgress;
        this.handler = new Handler(Looper.getMainLooper());
        BoatControlObserver boatControlObserver = this.observer;
        if (boatControlObserver != null) {
            DataChangeSubscribe.unSubscrible(boatControlObserver);
        }
        BoatControlObserver boatControlObserver2 = new BoatControlObserver();
        this.observer = boatControlObserver2;
        DataChangeSubscribe.subscribe(boatControlObserver2);
        this.mission = hMMission;
        int i = AnonymousClass6.$SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[hMMission.getMissionType().ordinal()];
        if (i == 1) {
            downLoadWayPoint();
        } else {
            if (i != 9) {
                return;
            }
            TeleControlComponent.WifiLinker.onTeleControlScanwifiListener = this;
            scanWifi();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEphemerisData() {
        if (HelmsmanSDK.productIsNull()) {
            return;
        }
        ABaseProduct product = HelmsmanSDK.getProduct();
        if (product.getType() == 11) {
            final HMEphemerisdataMission hMEphemerisdataMission = (HMEphemerisdataMission) this.mission;
            final HMMotorH1.BoatControlComponentH1.GpsPropertyH1 gpsPropertyH1 = (HMMotorH1.BoatControlComponentH1.GpsPropertyH1) ((HMMotorH1.BoatControlComponentH1) ((HMMotorH1) product).boatControlComponent).gpsProperty;
            gpsPropertyH1.getGpsModuleInfo(new HMMotorH1.BoatControlComponentH1.GpsPropertyH1.GPSModuleInfoCallback() { // from class: im.helmsman.lib.mission.HMMissionManager.2
                @Override // im.helmsman.lib.product.HMMotorH1.BoatControlComponentH1.GpsPropertyH1.GPSModuleInfoCallback
                public void gpsInfo(int i) {
                    String str;
                    if (i == 1) {
                        str = "aid";
                    } else {
                        if (i != 2) {
                            HMMissionManager.this.callback.onResult(new HMError(-2));
                            return;
                        }
                        str = "mga";
                    }
                    new NetUtils().downloadEphemerisFile(str, new NetUtils.OnDownLoadEphemerisFileListener() { // from class: im.helmsman.lib.mission.HMMissionManager.2.1
                        @Override // im.helmsman.lib.net.NetUtils.OnDownLoadEphemerisFileListener
                        public void onComplete(String str2, short[] sArr) {
                            hMEphemerisdataMission.setEphemerisdata(sArr);
                            gpsPropertyH1.sendGPSInfoToComponent(hMEphemerisdataMission.getEphemerisdata().length);
                        }

                        @Override // im.helmsman.lib.net.NetUtils.OnDownLoadEphemerisFileListener
                        public void onFailure(int i2) {
                            HMMissionManager.this.callback.onResult(new HMError(i2));
                        }
                    });
                }
            });
        }
    }

    private void sendRequestFirmwareInfo() {
        if (HelmsmanSDK.productIsNull()) {
            return;
        }
        HelmsmanSDK.getProduct().boatControlComponent.sendRequestFirmwareInfo();
    }

    private void cruise() {
        if (HelmsmanSDK.productIsNull()) {
            return;
        }
        HelmsmanSDK.getProduct().boatControlComponent.changeModel(14);
    }

    public void startMissionExecution(final HMCommonCallback.HMCompletionCallback hMCompletionCallback) {
        if (this.mission != null && AnonymousClass6.$SwitchMap$im$helmsman$lib$mission$common$HMMission$MissionType[this.mission.getMissionType().ordinal()] == 1) {
            new Handler().postDelayed(new Runnable() { // from class: im.helmsman.lib.mission.HMMissionManager.3
                @Override // java.lang.Runnable
                public void run() {
                    HelmsmanSDK.getProduct().boatControlComponent.changeModel(15);
                    HMCommonCallback.HMCompletionCallback hMCompletionCallback2 = hMCompletionCallback;
                    if (hMCompletionCallback2 != null) {
                        hMCompletionCallback2.onResult(null);
                    }
                    HMMissionManager.this.stopMissionExecution(hMCompletionCallback);
                }
            }, 1000L);
        }
    }

    public void stopMissionExecution(HMCommonCallback.HMCompletionCallback hMCompletionCallback) {
        DataChangeSubscribe.unSubscrible(this.observer);
    }

    public void releaseMissionManager() {
        DataChangeSubscribe.unSubscrible(this.observer);
        this.mission = null;
    }

    private void scanWifi() {
        if (HelmsmanSDK.getTeleControlComponent() == null || HelmsmanSDK.getTeleControlComponent().wifiLinker == null) {
            return;
        }
        HelmsmanSDK.getTeleControlComponent().wifiLinker.scanWifiSsid();
    }

    private void record() {
        if (HelmsmanSDK.productIsNull()) {
            return;
        }
        HelmsmanSDK.getProduct().boatControlComponent.changeModel(181);
    }

    private void navigation() {
        if (HelmsmanSDK.productIsNull()) {
            return;
        }
        HelmsmanSDK.getProduct().boatControlComponent.changeModel(15);
    }

    private void anchor() {
        if (HelmsmanSDK.productIsNull()) {
            return;
        }
        HelmsmanSDK.getProduct().boatControlComponent.changeModel(12);
    }

    private void downLoadWayPoint() {
        this.requestWaypointSeq = 0;
        if (HelmsmanSDK.productIsNull()) {
            return;
        }
        Timer timer = this.downloadCountTimeOutTimer;
        if (timer != null) {
            timer.cancel();
            this.downloadCountTimeOutTimer = null;
        }
        Timer timer2 = this.downloadWaypointTimeOutTimer;
        if (timer2 != null) {
            timer2.cancel();
        }
        DownloadRoutTimeOutRunnable downloadRoutTimeOutRunnable = this.downloadRoutTimeOutRunnable;
        if (downloadRoutTimeOutRunnable != null) {
            this.handler.removeCallbacks(downloadRoutTimeOutRunnable);
            this.downloadRoutTimeOutRunnable = null;
        }
        Timer timer3 = new Timer();
        this.downloadCountTimeOutTimer = timer3;
        timer3.schedule(new TimerTask() { // from class: im.helmsman.lib.mission.HMMissionManager.4
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                HelmsmanSDK.getProduct().boatControlComponent.requestDownLoadRoute();
            }
        }, 0L, 150L);
        this.handler.removeCallbacksAndMessages(null);
        DownloadRoutTimeOutRunnable downloadRoutTimeOutRunnable2 = new DownloadRoutTimeOutRunnable();
        this.downloadRoutTimeOutRunnable = downloadRoutTimeOutRunnable2;
        this.handler.postDelayed(downloadRoutTimeOutRunnable2, 25000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadWayPointToBoat(final List<HMWayPoint> list) {
        Runnable runnable = this.retryUploadRunnable;
        if (runnable != null) {
            this.handler.removeCallbacks(runnable);
            this.retryUploadRunnable = null;
        }
        if (HelmsmanSDK.productIsNull() || list == null) {
            return;
        }
        HelmsmanSDK.getProduct().boatControlComponent.uploadWaypointRequest(list.size());
        if (this.retryuploadTime < 3) {
            Runnable runnable2 = new Runnable() { // from class: im.helmsman.lib.mission.HMMissionManager.5
                @Override // java.lang.Runnable
                public void run() {
                    HMMissionManager.this.uploadWayPointToBoat(list);
                    HMMissionManager.access$408(HMMissionManager.this);
                }
            };
            this.retryUploadRunnable = runnable2;
            this.handler.postDelayed(runnable2, 3000L);
        } else {
            HMCommonCallback.HMCompletionCallback hMCompletionCallback = this.callback;
            if (hMCompletionCallback != null) {
                hMCompletionCallback.onResult(new HMError(-1));
            }
        }
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener
    public void scanResult(String str, int i) {
        if (this.downloadProgress != null) {
            if (i <= this.ssidCount) {
                if (HelmsmanSDK.getTeleControlComponent() != null) {
                    HelmsmanSDK.getTeleControlComponent().wifiLinker.requestSsid(i + 1);
                } else {
                    this.downloadProgress.downloadFinish(this.mission);
                }
                ((HMTeleControlScanWifiMission) this.mission).addSsid(str);
                this.downloadProgress.onProgress(this.mission, this.ssidCount, i);
            }
            if (i == this.ssidCount) {
                this.downloadProgress.downloadFinish(this.mission);
            }
        }
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener
    public void ssidCount(int i) {
        Log.e("scanwifiCount", this.ssidCount + "");
        this.ssidCount = i;
        if (HelmsmanSDK.getTeleControlComponent() != null) {
            HelmsmanSDK.getTeleControlComponent().wifiLinker.requestSsid(1);
        }
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.WifiLinker.OnTeleControlScanWifiListener
    public void connectWifiSuccess() {
        HMMission.HMMissionDownloadProgress hMMissionDownloadProgress = this.downloadProgress;
        if (hMMissionDownloadProgress != null) {
            hMMissionDownloadProgress.downloadFinish(this.mission);
        }
    }

    class BoatControlObserver implements DataManager.DataChangeObserver {
        private FirmwareDataRunnable firmwareDataRunnable;
        private int waypointCount = 0;

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
        public void connectWifiComplete() {
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
        public void scanWifiCount(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void scanWifiSsidResult(String str, int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void targeterPoint(int i) {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void teleControlBatteryElectricityLevel(int i) {
        }

        BoatControlObserver() {
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeUploadRequestWaypoint(final int i) {
            HMWayPointMission hMWayPointMission = (HMWayPointMission) HMMissionManager.this.mission;
            if (hMWayPointMission == null) {
                return;
            }
            if (HMMissionManager.this.retryUploadRunnable != null) {
                HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.retryUploadRunnable);
            }
            if (HMMissionManager.this.uploadtimeoutRunnable != null) {
                HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.uploadtimeoutRunnable);
            }
            HMMissionManager.this.uploadtimeoutRunnable = new Runnable() { // from class: im.helmsman.lib.mission.HMMissionManager.BoatControlObserver.1
                @Override // java.lang.Runnable
                public void run() {
                    BoatControlObserver.this.routeUploadRequestWaypoint(i);
                }
            };
            HMMissionManager.this.handler.postDelayed(HMMissionManager.this.uploadtimeoutRunnable, 3000L);
            if (!HelmsmanSDK.productIsNull() && hMWayPointMission != null) {
                if (i >= hMWayPointMission.getWayPointCount()) {
                    return;
                }
                HMWayPoint hMWayPoint = hMWayPointMission.getHmWayPoints().get(i);
                HelmsmanSDK.getProduct().boatControlComponent.upLoadWayPount((float) hMWayPoint.latitude, (float) hMWayPoint.longitude, i);
            }
            if (HMMissionManager.this.progressHandler != null) {
                HMMissionManager.this.progressHandler.onProgress(HMMissionManager.this.mission, hMWayPointMission.getWayPointCount(), i);
            }
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeDownloadWaypointCount(int i) {
            if (i == 0) {
                if (HMMissionManager.this.downloadProgress != null) {
                    if (HMMissionManager.this.downloadCountTimeOutTimer != null) {
                        HMMissionManager.this.downloadCountTimeOutTimer.cancel();
                        HMMissionManager.this.downloadCountTimeOutTimer = null;
                    }
                    HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.downloadRoutTimeOutRunnable);
                    HMMissionManager.this.downloadProgress.onProgress(null, 0.0f, 0.0f);
                    return;
                }
                return;
            }
            if (HMMissionManager.this.downloadCountTimeOutTimer != null) {
                HMMissionManager.this.downloadCountTimeOutTimer.cancel();
                HMMissionManager.this.downloadCountTimeOutTimer = null;
            }
            this.waypointCount = i;
            if (HMMissionManager.this.downloadWaypointTimeOutTimer == null) {
                HMMissionManager.this.downloadWaypointTimeOutTimer = new Timer();
                HMMissionManager.this.downloadWaypointTimeOutTimer.schedule(HMMissionManager.this.new DownloadRouteTimer(), 0L, 500L);
            }
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeDownloadWaypoint(float f, float f2, int i) {
            HMWayPointMission hMWayPointMission = (HMWayPointMission) HMMissionManager.this.mission;
            HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.downloadRoutTimeOutRunnable);
            HMMissionManager.this.handler.postDelayed(HMMissionManager.this.downloadRoutTimeOutRunnable, OkHttpUtils.DEFAULT_MILLISECONDS);
            if (hMWayPointMission.getWayPointCount() == i) {
                hMWayPointMission.addWayPoint(new HMWayPoint(f, f2));
                if (HMMissionManager.this.downloadProgress != null) {
                    HMMissionManager.this.downloadProgress.onProgress(HMMissionManager.this.mission, this.waypointCount, i + 1);
                }
                if (i >= this.waypointCount - 1) {
                    HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.downloadRoutTimeOutRunnable);
                    if (HMMissionManager.this.downloadProgress != null) {
                        HMMissionManager.this.downloadProgress.downloadFinish(HMMissionManager.this.mission);
                    }
                    if (HMMissionManager.this.downloadWaypointTimeOutTimer != null) {
                        HMMissionManager.this.downloadWaypointTimeOutTimer.cancel();
                        HMMissionManager.this.downloadWaypointTimeOutTimer = null;
                        return;
                    }
                    return;
                }
            }
            if (HMMissionManager.this.requestWaypointSeq <= i) {
                HMMissionManager.this.requestWaypointSeq = i + 1;
            }
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void routeUploadComplete() {
            if (HMMissionManager.this.progressHandler != null) {
                HMMissionManager.this.progressHandler.onPrepareFinish(HMMissionManager.this.mission);
            }
            if (HMMissionManager.this.timeOutRunnable != null) {
                HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.timeOutRunnable);
            }
            if (HMMissionManager.this.uploadtimeoutRunnable != null) {
                HMMissionManager.this.handler.removeCallbacks(HMMissionManager.this.uploadtimeoutRunnable);
            }
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void requestAidData(long j, short s) {
            Log.e("请求的AidData", "address" + j + "  length" + ((int) s));
            HMEphemerisdataMission hMEphemerisdataMission = (HMEphemerisdataMission) HMMissionManager.this.mission;
            if (HelmsmanSDK.productIsNull()) {
                return;
            }
            ((HMMotorH1.BoatControlComponentH1.GpsPropertyH1) ((HMMotorH1.BoatControlComponentH1) ((HMMotorH1) HelmsmanSDK.getProduct()).boatControlComponent).gpsProperty).sendGPSAidData(Arrays.copyOfRange(hMEphemerisdataMission.getEphemerisdata(), (int) j, (int) (((long) s) + j)), j);
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void aidAck() {
            HMMissionManager.this.callback.onResult(null);
        }

        class FirmwareDataRunnable implements Runnable {
            private long address;
            private int length;

            public FirmwareDataRunnable(long j, int i) {
                this.address = j;
                this.length = i;
            }

            @Override // java.lang.Runnable
            public void run() {
                Log.e("~~~~", this.address + "," + this.length);
                Log.d("重发", "丫的又不行了~");
                BoatControlObserver.this.requestFirmwareData(this.address, this.length);
            }
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void requestFirmwareData(long j, int i) {
            short[] sArrCopyOfRange;
            if (this.firmwareDataRunnable != null) {
                HMMissionManager.this.handler.removeCallbacks(this.firmwareDataRunnable);
            }
            this.firmwareDataRunnable = new FirmwareDataRunnable(j, i);
            HMMissionManager.this.handler.postDelayed(this.firmwareDataRunnable, 1000L);
            Log.d("requestFirmwareData", "address:" + j + "  length:" + i);
            short[] firmwaredatas = ((HMUpdateBoatControlFirmwareMission) HMMissionManager.this.mission).getFirmwaredatas();
            long j2 = ((long) i) + j;
            if (j2 < firmwaredatas.length) {
                sArrCopyOfRange = Arrays.copyOfRange(firmwaredatas, (int) j, (int) j2);
            } else {
                sArrCopyOfRange = Arrays.copyOfRange(firmwaredatas, (int) j, firmwaredatas.length);
            }
            if (sArrCopyOfRange != null) {
                HMMotorH1.BoatControlComponentH1.sendFrimwareData(j, sArrCopyOfRange);
            }
        }

        @Override // im.helmsman.lib.DataManager.DataChangeObserver
        public void firmwareAck() {
            if (this.firmwareDataRunnable != null) {
                HMMissionManager.this.handler.removeCallbacks(this.firmwareDataRunnable);
            }
            Log.d("complete", "传输完成");
        }
    }

    class DownloadRoutTimeOutRunnable implements Runnable {
        DownloadRoutTimeOutRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (HMMissionManager.this.downloadCountTimeOutTimer != null) {
                HMMissionManager.this.downloadCountTimeOutTimer.cancel();
                HMMissionManager.this.downloadCountTimeOutTimer = null;
            }
            if (HMMissionManager.this.downloadProgress != null) {
                HMMissionManager.this.downloadProgress.downloadFinish(null);
            }
        }
    }

    class DownloadRouteTimer extends TimerTask {
        DownloadRouteTimer() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (HelmsmanSDK.productIsNull()) {
                return;
            }
            HelmsmanSDK.getProduct().boatControlComponent.downloadWaypointBySeq(HMMissionManager.this.requestWaypointSeq);
        }
    }
}
