package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.LogUtil;
import im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow;
import im.helmsman.lib.ABaseProduct;
import im.helmsman.lib.HelmsmanSDK;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public class HeadingCalibrationActivity extends Base2Activity implements View.OnClickListener, CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener {
    private CheckGpsAndWifiPopuWindow checkGpsAndWifiPopuWindow;
    private Handler handler;
    private ImageView imageView;
    private Button mBtnStart;
    private FrameLayout mFrameTop;
    private PopupWindow mPWCalibration;
    private PopupWindow mPopupWindow;
    private long startTime = 0;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_heading_calibration);
        setStatusBar(R.color.red_state);
        initView();
        initEvent();
    }

    private void initView() {
        this.mBtnStart = (Button) findViewById(R.id.btn_calibration_start);
        this.mFrameTop = (FrameLayout) findViewById(R.id.frame_heading_calibration_top);
        this.checkGpsAndWifiPopuWindow = new CheckGpsAndWifiPopuWindow(this);
        this.imageView = (ImageView) findViewById(R.id.iv_gif);
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.anim_headingcallbration_calibration)).into(this.imageView);
    }

    private void initEvent() {
        this.mBtnStart.setOnClickListener(this);
        this.mFrameTop.setOnClickListener(null);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.mBtnStart)) {
            this.checkGpsAndWifiPopuWindow.setOnCheckGpsAndWifi(this);
            this.mPopupWindow = this.checkGpsAndWifiPopuWindow.checkWifi(findViewById(R.id.titleBar));
        }
    }

    @Override // im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener
    public void GpsAndWifiSuccess() {
        View viewInflate = getLayoutInflater().inflate(R.layout.item_custom_progress_dialog, (ViewGroup) null);
        ((TextView) viewInflate.findViewById(R.id.tv_custom_progress_content)).setText(getString(R.string.headingcallbration_calibration));
        PopupWindow popupWindow = new PopupWindow(viewInflate, 600, 400, false);
        this.mPWCalibration = popupWindow;
        popupWindow.showAtLocation(findViewById(R.id.titleBar), 17, 0, 0);
        this.mFrameTop.setVisibility(0);
        Handler handler = new Handler();
        this.handler = handler;
        handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.HeadingCalibrationActivity.1
            @Override // java.lang.Runnable
            public void run() {
                HeadingCalibrationActivity.this.startMagnetometerCheck();
            }
        }, 150L);
        ABaseProduct aBaseProduct = ABaseProduct.getInstance();
        if (aBaseProduct != null) {
            aBaseProduct.motorComponent.motorOff();
        }
    }

    public void startMagnetometerCheck() {
        LogUtil.instance().magnetometerCheck();
        this.startTime = System.currentTimeMillis();
        final ABaseProduct product = HelmsmanSDK.getProduct();
        if (product == null) {
            return;
        }
        new Timer().schedule(new TimerTask() { // from class: im.helmsman.helmsmanandroid.ui.activity.HeadingCalibrationActivity.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (System.currentTimeMillis() - HeadingCalibrationActivity.this.startTime < 12000) {
                    product.motorComponent.turnRight();
                } else {
                    if (System.currentTimeMillis() - HeadingCalibrationActivity.this.startTime < 24000) {
                        product.motorComponent.turnLeft();
                        return;
                    }
                    product.motorComponent.motorOff();
                    cancel();
                    HeadingCalibrationActivity.this.runOnUiThread(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.HeadingCalibrationActivity.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (HeadingCalibrationActivity.this.mPWCalibration != null && HeadingCalibrationActivity.this.mPWCalibration.isShowing()) {
                                HeadingCalibrationActivity.this.mPWCalibration.dismiss();
                            }
                            HeadingCalibrationActivity.this.mFrameTop.setVisibility(8);
                            Toast.makeText(HeadingCalibrationActivity.this, HeadingCalibrationActivity.this.getString(R.string.main_success), 0).show();
                            HeadingCalibrationActivity.this.finish();
                        }
                    });
                }
            }
        }, 0L, 62L);
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        PopupWindow popupWindow;
        if (i == 4 && (popupWindow = this.mPWCalibration) != null && popupWindow.isShowing()) {
            this.mPWCalibration.dismiss();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        PopupWindow popupWindow = this.mPopupWindow;
        if (popupWindow == null || !popupWindow.isShowing()) {
            return;
        }
        this.mPopupWindow.dismiss();
    }
}
