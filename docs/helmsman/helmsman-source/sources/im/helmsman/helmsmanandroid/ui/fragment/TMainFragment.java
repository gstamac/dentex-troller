package im.helmsman.helmsmanandroid.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.controller.BaseIoController;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.presenter.MainPresenter;
import im.helmsman.helmsmanandroid.ui.common.BaseFragment;
import im.helmsman.helmsmanandroid.ui.view.MainView;

/* JADX INFO: loaded from: classes2.dex */
public class TMainFragment extends BaseFragment<MainView, MainPresenter> implements MainView, View.OnTouchListener {

    @BindView(R.id.btn_add)
    Button btnAdd;

    @BindView(R.id.btn_jian)
    Button btnJian;

    @BindView(R.id.btn_left)
    Button btnLeft;

    @BindView(R.id.btn_right)
    Button btnRight;

    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    private View view;

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void batteryState(int i) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void boatDirectionState(float f) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void boatModelState(int i) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void boatTarget(int i) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void clearMotorBtnAnimation() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void flickHeartBeat() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void flickerRunnable(boolean z) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void gpsState(int i, int i2) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void offline() {
    }

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.btn_add, R.id.btn_jian, R.id.tv_main_title})
    public void onClick(View view) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void onRemoteControlBatteryLow() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void onServerCreate(BaseIoController baseIoController) {
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void online() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void positionState(MLatLng mLatLng) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void recoveryViewState() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void setMotorSpeedImageView(int i) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void speedState(float f) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void startmotorBtnAnimation(Animation animation) {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void stopFlickHeartBeat() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.view.MainView
    public void wifiState(boolean z) {
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.tfragment_main, viewGroup, false);
        this.view = viewInflate;
        ButterKnife.bind(this, viewInflate);
        this.btnLeft.setOnTouchListener(this);
        this.btnRight.setOnTouchListener(this);
        return this.view;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.BaseFragment
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }
}
