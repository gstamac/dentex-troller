package im.helmsman.helmsmanandroid.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.StringUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.mission.HMMissionManager;

/* JADX INFO: loaded from: classes2.dex */
public class SettingActivity extends Base2Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private LinearLayout mLinOverScroll;
    private RelativeLayout mRelCalibration;
    private RelativeLayout mRelDiagnostics;
    private RelativeLayout mRelModifyWifiPassword;
    private RelativeLayout mRelNaviMode;
    private Switch mSwIsChinaOptimization;
    private Switch mSwIsVibrator;
    private RelativeLayout mUnitConvert;
    private HMMissionManager manager;
    private SeekBar seekBar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting);
        initView();
        initEvent();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        HMMissionManager hMMissionManager = this.manager;
        if (hMMissionManager != null) {
            hMMissionManager.releaseMissionManager();
        }
    }

    protected void initView() {
        this.mUnitConvert = (RelativeLayout) findViewById(R.id.rel_unit_convert);
        this.mSwIsChinaOptimization = (Switch) findViewById(R.id.sv_ischina_opimization);
        this.mRelDiagnostics = (RelativeLayout) findViewById(R.id.rel_navi_log);
        this.mRelModifyWifiPassword = (RelativeLayout) findViewById(R.id.rel_wifi_password);
        this.mSwIsVibrator = (Switch) findViewById(R.id.sv_isvibrator);
        this.mRelCalibration = (RelativeLayout) findViewById(R.id.rel_setting_calibration);
        this.mRelNaviMode = (RelativeLayout) findViewById(R.id.rel_navi_mode);
        this.mLinOverScroll = (LinearLayout) findViewById(R.id.lin_setting_overscroll);
    }

    protected void initEvent() {
        this.mRelDiagnostics.setOnClickListener(this);
        this.mRelModifyWifiPassword.setOnClickListener(this);
        this.mSwIsVibrator.setOnCheckedChangeListener(this);
        this.mSwIsChinaOptimization.setOnCheckedChangeListener(this);
        this.mSwIsChinaOptimization.setChecked(Config.isChineseCoordinateCorrection);
        this.mSwIsVibrator.setChecked(Config.isVibration);
        this.mRelCalibration.setOnClickListener(this);
        this.mRelNaviMode.setOnClickListener(this);
        this.mLinOverScroll.setOnClickListener(this);
        this.mUnitConvert.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.mUnitConvert)) {
            startActivity(new Intent(this, (Class<?>) UnitSelectActivity.class));
            return;
        }
        if (view.equals(this.mRelModifyWifiPassword)) {
            View viewInflate = LayoutInflater.from(this).inflate(R.layout.item_modify_password, (ViewGroup) null);
            final EditText editText = (EditText) viewInflate.findViewById(R.id.et_login_pwd);
            new AlertDialog.Builder(this).setTitle(getString(R.string.setting_modify_wifi_password)).setView(viewInflate).setPositiveButton(R.string.setting_sure, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.SettingActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    String string = editText.getText().toString();
                    if (!TextUtils.isEmpty(string) && string.length() == 10 && StringUtils.isHaveSpecialCharacters(string)) {
                        if (HelmsmanSDK.getProduct() != null) {
                            HelmsmanSDK.getProduct().boatControlComponent.wifiProperty.changeWifiPassword(string);
                            return;
                        }
                        return;
                    }
                    ViewUtils.ShowToast(SettingActivity.this.getString(R.string.setting_warning));
                }
            }).setNegativeButton(R.string.setting_cancel, (DialogInterface.OnClickListener) null).show();
        } else if (view.equals(this.mRelCalibration)) {
            startActivity(new Intent(this, (Class<?>) HeadingCalibrationActivity.class));
        } else if (view.equals(this.mRelNaviMode)) {
            startActivity(new Intent(this, (Class<?>) NaviModeActivity.class));
        } else if (view.equals(this.mRelDiagnostics)) {
            startActivity(new Intent(this, (Class<?>) DiagnosticsActivity.class));
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (compoundButton.equals(this.mSwIsVibrator)) {
            Config.isVibration = z;
        } else if (compoundButton.equals(this.mSwIsChinaOptimization)) {
            Config.isChineseCoordinateCorrection = z;
        }
        Config.saveConfig();
    }
}
