package im.helmsman.helmsmanandroid.ui.view;

import android.view.animation.Animation;
import im.helmsman.helmsmanandroid.controller.BaseIoController;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.ui.view.commom.MvpView;

/* JADX INFO: loaded from: classes2.dex */
public interface MainView extends MvpView {
    void batteryState(int i);

    void boatDirectionState(float f);

    void boatModelState(int i);

    void boatTarget(int i);

    void clearMotorBtnAnimation();

    void flickHeartBeat();

    void flickerRunnable(boolean z);

    void gpsState(int i, int i2);

    void offline();

    void onRemoteControlBatteryLow();

    void onServerCreate(BaseIoController baseIoController);

    void online();

    void positionState(MLatLng mLatLng);

    void recoveryViewState();

    void setMotorSpeedImageView(int i);

    void speedState(float f);

    void startmotorBtnAnimation(Animation animation);

    void stopFlickHeartBeat();

    void wifiState(boolean z);
}
