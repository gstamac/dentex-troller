package im.helmsman.helmsmanandroid.ui.activity;

import android.os.Bundle;
import android.widget.TextView;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.dao.Boat;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.DaoUtil;
import im.helmsman.helmsmanandroid.view.TitleBar;

/* JADX INFO: loaded from: classes2.dex */
public class MyDevicesActivity extends Base2Activity {
    public static final int MYDEVICES_HEARTBEAT_INFO = -2;
    public static final int MYDEVICES_SSID_INFO = -1;
    private TextView mTvMode;
    private TextView mTvName;
    private TextView mTvVersion;
    private TitleBar titleBar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_devices);
        initView();
    }

    private void initView() {
        this.titleBar = (TitleBar) findViewById(R.id.titleBar);
        this.mTvName = (TextView) findViewById(R.id.tv_mydevices_name);
        this.mTvVersion = (TextView) findViewById(R.id.tv_mydevices_version);
        this.mTvMode = (TextView) findViewById(R.id.tv_mydevices_mode);
        String stringExtra = getIntent().getStringExtra("ssid");
        Boat boatQueryBoatBySSID = DaoUtil.getInstance().queryBoatBySSID(stringExtra);
        long mode = boatQueryBoatBySSID.getMode();
        if (mode == 110) {
            mode = 50736;
        }
        this.mTvMode.setText(mode + "");
        this.mTvVersion.setText(boatQueryBoatBySSID.getFirmwareVersion() + "");
        this.titleBar.setCenterViewText(stringExtra);
        this.mTvName.setText(boatQueryBoatBySSID.getSsid());
    }
}
