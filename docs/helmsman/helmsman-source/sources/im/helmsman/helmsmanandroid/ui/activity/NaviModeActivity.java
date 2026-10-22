package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow;
import im.helmsman.helmsmanandroid.view.TitleBar;
import im.helmsman.lib.HelmsmanSDK;

/* JADX INFO: loaded from: classes2.dex */
public class NaviModeActivity extends Base2Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener {
    private CheckGpsAndWifiPopuWindow checkGpsAndWifiPopuWindow;
    private RadioButton mRBAccurate;
    private RadioButton mRBStandard;
    private RadioGroup mRGModeSelection;
    private RelativeLayout mRelSave;
    private TextView mTvDescription;
    private TitleBar titleBar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_navi_mode);
        initView();
        initEvent();
    }

    private void initView() {
        this.mRBStandard = (RadioButton) findViewById(R.id.rb_standard_mode);
        this.mRGModeSelection = (RadioGroup) findViewById(R.id.rg_mode_selection);
        this.mTvDescription = (TextView) findViewById(R.id.tv_navi_mode_description);
        this.mRBAccurate = (RadioButton) findViewById(R.id.rb_accurate_mode);
        this.mRelSave = (RelativeLayout) findViewById(R.id.rel_navi_mode_save);
        this.titleBar = (TitleBar) findViewById(R.id.titleBar);
        this.checkGpsAndWifiPopuWindow = new CheckGpsAndWifiPopuWindow(this);
    }

    private void initEvent() {
        this.mRGModeSelection.setOnCheckedChangeListener(this);
        this.mRelSave.setOnClickListener(this);
        this.mRBStandard.setChecked(true);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.equals(this.mRelSave)) {
            this.checkGpsAndWifiPopuWindow.setOnCheckGpsAndWifi(this);
            this.checkGpsAndWifiPopuWindow.checkWifi(this.titleBar);
        }
    }

    @Override // android.widget.RadioGroup.OnCheckedChangeListener
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == this.mRBStandard.getId()) {
            this.mTvDescription.setText(getString(R.string.navimode_standard_mode_spec));
        } else if (i == this.mRBAccurate.getId()) {
            this.mTvDescription.setText(getString(R.string.navimode_accurate_mode_spec));
        }
    }

    @Override // im.helmsman.helmsmanandroid.view.CheckGpsAndWifiPopuWindow.OnCheckGpsAndWifiListener
    public void GpsAndWifiSuccess() {
        if (this.mRBStandard.isChecked() && !HelmsmanSDK.productIsNull()) {
            HelmsmanSDK.getProduct().boatControlComponent.setNaviMode(0);
        } else {
            HelmsmanSDK.getProduct().boatControlComponent.setNaviMode(1);
        }
    }
}
