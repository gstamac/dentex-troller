package im.helmsman.helmsmanandroid.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.Boat;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.inet.RequestInetUtils;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.view.MainView;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.utils.LogUtil;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.DataChangeSubscribe;
import im.helmsman.lib.DataManager;
import im.helmsman.lib.HelmsmanSDK;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public class MainPresenter extends BasePresenter<MainView> implements DataManager.DataChangeObserver {
    public static final int BAD = 3;
    public static final int EXCELLENT = 1;
    public static final int GOOD = 2;
    public static final int LOADING = 4;
    public static final int NONE = 0;
    public static final int TRUN_RIGHT = 1;
    public static final int TURN_LEFT = 0;
    public static Handler handler;
    private Timer keepSendHeartbeatTimer;
    private KeepSendHeartbeatTimerTask keepSendHeartbeatTimerTask;
    private RotateAnimation rotateAnimation;
    private String ssid;
    private Timer turnTimer;
    private int flickerTime = 6;
    private boolean isBatteryLow = false;
    private final long REV = 1200;
    private boolean isConnectAck = false;
    private int direction = 2;

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void aidAck() {
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void connectWifiComplete() {
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void firmwareAck() {
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void requestAidData(long j, short s) {
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void requestFirmwareData(long j, int i) {
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
    public void scanWifiCount(int i) {
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void scanWifiSsidResult(String str, int i) {
    }

    public MainPresenter() {
        handler = new MyHandler(Looper.getMainLooper());
    }

    @Override // im.helmsman.helmsmanandroid.presenter.common.BasePresenter
    public void attachView(MainView mainView) {
        super.attachView(mainView);
        DataChangeSubscribe.subscribe(this);
        this.keepSendHeartbeatTimer = new Timer();
        KeepSendHeartbeatTimerTask keepSendHeartbeatTimerTask = new KeepSendHeartbeatTimerTask();
        this.keepSendHeartbeatTimerTask = keepSendHeartbeatTimerTask;
        this.keepSendHeartbeatTimer.schedule(keepSendHeartbeatTimerTask, 0L, 2000L);
    }

    @Override // im.helmsman.helmsmanandroid.presenter.common.BasePresenter
    public void detachView() {
        super.detachView();
        DataChangeSubscribe.unSubscrible(this);
        Timer timer = this.keepSendHeartbeatTimer;
        if (timer != null) {
            timer.cancel();
            this.keepSendHeartbeatTimer = null;
        }
        KeepSendHeartbeatTimerTask keepSendHeartbeatTimerTask = this.keepSendHeartbeatTimerTask;
        if (keepSendHeartbeatTimerTask != null) {
            keepSendHeartbeatTimerTask.cancel();
            this.keepSendHeartbeatTimerTask = null;
        }
    }

    public void finishRecordWayPoint() {
        if (HelmsmanSDK.getProduct() != null) {
            HelmsmanSDK.getProduct().boatControlComponent.changeModel(13);
        }
    }

    public void sendSwitchModeCommand(int i) {
        if (HelmsmanSDK.getProduct() != null) {
            HelmsmanSDK.getProduct().boatControlComponent.changeModel(i);
        }
    }

    public void sendClearCacheCommand() {
        if (HelmsmanSDK.getProduct() != null) {
            HelmsmanSDK.getProduct().boatControlComponent.sendClearCacheCommand();
        }
    }

    public void closeMotor() {
        if (HelmsmanSDK.getProduct() != null) {
            HelmsmanSDK.getProduct().motorComponent.motorOff();
        }
        flickeMotor();
    }

    public void startMotor() {
        if (HelmsmanSDK.getProduct() != null) {
            HelmsmanSDK.getProduct().motorComponent.motorOn();
        }
        flickeMotor();
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void targeterPoint(int i) {
        if (this.view != 0) {
            ((MainView) this.view).boatTarget(i);
        }
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void boatDegress(float f) {
        float degrees = (float) Math.toDegrees(f);
        if (this.view != 0) {
            ((MainView) this.view).boatDirectionState(degrees);
        }
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void boatSpeed(float f) {
        ((MainView) this.view).speedState(new BigDecimal(f).setScale(1, 4).floatValue());
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void motorState(boolean z, int i) {
        changeMotorState(z, i);
        changeMotorSpeedImage(i);
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void boatPosition(float f, float f2) {
        if (this.view != 0) {
            if (f == 0.0f && f2 == 0.0f) {
                return;
            }
            ((MainView) this.view).positionState(new MLatLng(f, f2));
        }
    }

    class KeepSendHeartbeatTimerTask extends TimerTask {
        KeepSendHeartbeatTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ABaseProduct aBaseProduct = ABaseProduct.getInstance();
            if (!MyApplication.getInstance().isAppIsForeground() || aBaseProduct == null || aBaseProduct.boatControlComponent == null) {
                return;
            }
            aBaseProduct.boatControlComponent.sendHeartBeat();
        }
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void gpsSignal(int i) {
        int i2;
        if (i >= 250) {
            i2 = 4;
        } else if (i > 170) {
            i2 = 3;
        } else if (i >= 100) {
            i2 = 2;
        } else {
            i2 = i < 100 ? 1 : 0;
        }
        MyApplication.getInstance().gpsState = i2;
        ((MainView) this.view).gpsState(i2, (int) (((250.0d - ((double) i)) / 250.0d) * 100.0d));
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void boatControlModel(int i) {
        ((MainView) this.view).boatModelState(i);
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void batteryElectricityLevel(int i) {
        ((MainView) this.view).batteryState(i);
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void teleControlBatteryElectricityLevel(int i) {
        if (i > 3400) {
            this.isBatteryLow = false;
            return;
        }
        if (!this.isBatteryLow && this.view != 0) {
            ((MainView) this.view).onRemoteControlBatteryLow();
        }
        this.isBatteryLow = true;
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void onBoatControlConnectStateChange(int i) {
        if (i == 0) {
            ((MainView) this.view).online();
            MyApplication.getInstance().wifiState = true;
            ((MainView) this.view).wifiState(true);
            LogUtil.instance().saveIsConnectedStatelog(true);
            ((MainView) this.view).flickHeartBeat();
            return;
        }
        if (i != 1) {
            return;
        }
        ((MainView) this.view).wifiState(false);
        MyApplication.getInstance().wifiState = false;
        ((MainView) this.view).gpsState(0, 0);
        ((MainView) this.view).offline();
        LogUtil.instance().saveIsConnectedStatelog(false);
        ((MainView) this.view).recoveryViewState();
        ((MainView) this.view).stopFlickHeartBeat();
        MyApplication.getInstance().ssid = null;
    }

    @Override // im.helmsman.lib.DataManager.DataChangeObserver
    public void deviceInfo(int i, int i2, int i3, String str) {
        if (TextUtils.isEmpty(str) || str.equals(this.ssid)) {
            return;
        }
        this.ssid = str;
        Boat boat = new Boat();
        MyApplication.getInstance().ssid = str;
        boat.setSsid(str);
        boat.setDisconnectProtection(i2);
        boat.setMode(i3);
        boat.setFirmwareVersion(i);
        DaoUtil.getInstance().saveBoatToDataBase(boat);
        if (this.isConnectAck) {
            return;
        }
        RequestInetUtils.instance().activateRequest(boat.getSsid(), boat.getFirmwareVersion() + "", boat.getMode() + "");
        this.isConnectAck = true;
    }

    public void turn(int i) {
        if (this.turnTimer == null) {
            Timer timer = new Timer();
            this.turnTimer = timer;
            timer.schedule(new TurnTimerTask(), 0L, 66L);
        }
        this.direction = i;
    }

    class TurnTimerTask extends TimerTask {
        TurnTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (HelmsmanSDK.getProduct() == null) {
                cancel();
                MainPresenter.this.turnTimer = null;
            } else if (MainPresenter.this.direction == 1) {
                HelmsmanSDK.getProduct().motorComponent.turnRight();
                Log.e("right", "right");
            } else if (MainPresenter.this.direction == 0) {
                HelmsmanSDK.getProduct().motorComponent.turnLeft();
                Log.e("left", "left");
            }
        }
    }

    public void turnEnd() {
        Timer timer = this.turnTimer;
        if (timer != null) {
            timer.cancel();
        }
        this.turnTimer = null;
    }

    class flickerRunnable implements Runnable {
        flickerRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MainPresenter.this.flickerTime--;
            if (MainPresenter.this.flickerTime > 0) {
                ((MainView) MainPresenter.this.view).flickerRunnable(false);
                MainPresenter.handler.postDelayed(this, 200L);
            } else {
                MainPresenter.this.flickerTime = 6;
                ((MainView) MainPresenter.this.view).flickerRunnable(true);
            }
        }
    }

    public void flickeMotor() {
        handler.post(new flickerRunnable());
    }

    public void stopMotorAnimation() {
        RotateAnimation rotateAnimation = this.rotateAnimation;
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
        ((MainView) this.view).clearMotorBtnAnimation();
    }

    private void changeMotorState(boolean z, int i) {
        if (i == 0 || !z) {
            RotateAnimation rotateAnimation = this.rotateAnimation;
            if (rotateAnimation != null) {
                rotateAnimation.cancel();
                ((MainView) this.view).clearMotorBtnAnimation();
                this.rotateAnimation = null;
            }
            MyApplication.getInstance().motorIsRun = false;
            return;
        }
        if (this.rotateAnimation == null) {
            RotateAnimation rotateAnimation2 = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
            this.rotateAnimation = rotateAnimation2;
            rotateAnimation2.setRepeatCount(-1);
            this.rotateAnimation.setFillAfter(true);
            this.rotateAnimation.setInterpolator(new LinearInterpolator());
            this.rotateAnimation.setDuration(1200 - ((long) (i * 55)));
            ((MainView) this.view).startmotorBtnAnimation(this.rotateAnimation);
        }
        MyApplication.getInstance().motorIsRun = true;
    }

    public void changeMotorSpeedImage(int i) {
        RotateAnimation rotateAnimation = this.rotateAnimation;
        if (rotateAnimation != null) {
            long j = 1200 - ((long) (i * 55));
            if (rotateAnimation.getDuration() != j) {
                ((MainView) this.view).clearMotorBtnAnimation();
                this.rotateAnimation.cancel();
                RotateAnimation rotateAnimation2 = new RotateAnimation(0.0f, 359.0f, 1, 0.5f, 1, 0.5f);
                this.rotateAnimation = rotateAnimation2;
                rotateAnimation2.setRepeatCount(-1);
                this.rotateAnimation.setInterpolator(new LinearInterpolator());
                this.rotateAnimation.setDuration(j);
                ((MainView) this.view).startmotorBtnAnimation(this.rotateAnimation);
            }
        }
        switch (i) {
            case 0:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed0);
                break;
            case 1:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed1);
                break;
            case 2:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed2);
                break;
            case 3:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed3);
                break;
            case 4:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed4);
                break;
            case 5:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed5);
                break;
            case 6:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed6);
                break;
            case 7:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed7);
                break;
            case 8:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed8);
                break;
            case 9:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed9);
                break;
            case 10:
                ((MainView) this.view).setMotorSpeedImageView(R.drawable.icon_motorspeed10);
                break;
        }
    }

    class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (MainPresenter.this.view == null) {
                return;
            }
            super.handleMessage(message);
            if (message.what != 3) {
                return;
            }
            MainPresenter.handler.post(MainPresenter.this.new flickerRunnable());
        }
    }
}
