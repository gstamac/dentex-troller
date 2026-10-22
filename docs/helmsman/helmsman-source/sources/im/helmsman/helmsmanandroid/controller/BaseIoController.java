package im.helmsman.helmsmanandroid.controller;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_heartbeat;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.presenter.MainPresenter;
import im.helmsman.helmsmanandroid.view.Rudder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseIoController extends Service implements Rudder.RudderListener {
    protected static BaseIoController instance;
    protected int baseMode;
    private volatile byte[] buffer;
    protected OnDownLoadWayPointListener downLoadWayPointListener;
    private Handler handler;
    private Timer heartBeat;
    private OnHeadingCalibrationCompleteListener listener;
    private Thread readThread;
    private Timer timer;
    protected OnUpLoadWayPointListener upLoadWayPointListener;
    public boolean mMotorIsRun = false;
    protected MyApplication app = MyApplication.getInstance();

    public interface OnDownLoadWayPointListener {
        void Finish(List<MLatLng> list);

        void onFailed();

        void onProgress(int i, int i2);
    }

    public interface OnHeadingCalibrationCompleteListener {
        void onFailed();

        void onSuccess();
    }

    public interface OnUpLoadWayPointListener {
        void onFailed(String str);

        void onFinish();

        void onProgress(int i, int i2);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onLongPressDown() {
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onLongPressUp() {
    }

    public abstract void readtBotaMessage();

    protected abstract void sendCommand(byte[] bArr);

    public static BaseIoController getInstance() {
        return instance;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (this.handler == null) {
            this.handler = new Handler(Looper.getMainLooper());
        }
        keepSendCommandPicker();
        Thread thread = new Thread() { // from class: im.helmsman.helmsmanandroid.controller.BaseIoController.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                super.run();
                BaseIoController.this.readtBotaMessage();
            }
        };
        this.readThread = thread;
        thread.start();
        if (MainPresenter.handler != null) {
            MainPresenter.handler.sendEmptyMessage(0);
        }
    }

    public void closeMotor() {
        msg_command_long msg_command_longVar = new msg_command_long();
        msg_command_longVar.command = 11;
        sendCommand(msg_command_longVar.pack().encodePacket());
        this.mMotorIsRun = false;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.readThread.interrupt();
        this.timer.cancel();
        this.heartBeat.cancel();
        this.heartBeat = null;
        new Thread() { // from class: im.helmsman.helmsmanandroid.controller.BaseIoController.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                super.run();
                BaseIoController.this.closeMotor();
            }
        };
        instance = null;
    }

    public void setOnupLoadWayPointListener(OnUpLoadWayPointListener onUpLoadWayPointListener) {
        this.upLoadWayPointListener = onUpLoadWayPointListener;
    }

    public void setOnDownLoadListener(OnDownLoadWayPointListener onDownLoadWayPointListener) {
        this.downLoadWayPointListener = onDownLoadWayPointListener;
    }

    public void setOnHeadingCalibrationCompleteListener(OnHeadingCalibrationCompleteListener onHeadingCalibrationCompleteListener) {
        this.listener = onHeadingCalibrationCompleteListener;
    }

    protected void keepSendCommandPicker() {
        Timer timer = new Timer();
        this.timer = timer;
        timer.schedule(new TimerTask() { // from class: im.helmsman.helmsmanandroid.controller.BaseIoController.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (BaseIoController.this.buffer != null) {
                    BaseIoController baseIoController = BaseIoController.this;
                    baseIoController.sendCommand(baseIoController.buffer);
                }
            }
        }, 0L, 66L);
    }

    public void keepSendHeartBeat() {
        if (this.heartBeat != null) {
            return;
        }
        Timer timer = new Timer();
        this.heartBeat = timer;
        timer.schedule(new TimerTask() { // from class: im.helmsman.helmsmanandroid.controller.BaseIoController.4
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                msg_heartbeat msg_heartbeatVar = new msg_heartbeat();
                msg_heartbeatVar.mavlink_version = (short) 3;
                msg_heartbeatVar.base_mode = (short) BaseIoController.this.baseMode;
                msg_heartbeatVar.type = (short) 106;
                BaseIoController.this.sendCommand(msg_heartbeatVar.pack().encodePacket());
            }
        }, 0L, 1000L);
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelUp() {
        msg_command_long msg_command_longVar = new msg_command_long();
        msg_command_longVar.command = 8;
        sendCommand(msg_command_longVar.pack().encodePacket());
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelBottom() {
        msg_command_long msg_command_longVar = new msg_command_long();
        msg_command_longVar.command = 9;
        sendCommand(msg_command_longVar.pack().encodePacket());
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelLeft() {
        msg_command_long msg_command_longVar = new msg_command_long();
        msg_command_longVar.command = 7;
        this.buffer = msg_command_longVar.pack().encodePacket();
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onSteeringWheelRight() {
        msg_command_long msg_command_longVar = new msg_command_long();
        msg_command_longVar.command = 6;
        this.buffer = msg_command_longVar.pack().encodePacket();
    }
}
