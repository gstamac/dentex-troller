package im.helmsman.helmsmanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.presenter.AutoConnectBlePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.view.AutoConnectBleView;
import im.helmsman.helmsmanandroid.view.TitleBar;
import im.helmsman.lib.HelmsmanSDK;
import im.helmsman.lib.component.TeleControlComponent;
import javy.lib.jsave.JSave;

/* JADX INFO: loaded from: classes2.dex */
public class AutoConnectBleActivity extends Mvp2BaseActivity<AutoConnectBleView, AutoConnectBlePresenter> implements AutoConnectBleView, TeleControlComponent.OnTeleControlComponentCreateListener, CompoundButton.OnCheckedChangeListener {
    private String address;
    private String name;

    @BindView(R.id.rel_alias_bluetooth)
    RelativeLayout relAliasBluetooth;

    @BindView(R.id.rel_search_wifi)
    RelativeLayout relSearchWifi;

    @BindView(R.id.switch_check_auto_connect)
    Switch switchCheckAutoConnect;

    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_auto_connect_ble);
        ButterKnife.bind(this);
        initEvent();
        Intent intent = getIntent();
        this.address = intent.getStringExtra("address");
        String stringExtra = intent.getStringExtra("name");
        this.name = stringExtra;
        this.titleBar.setCenterViewText(stringExtra);
        String str = (String) JSave.get("auto_connect_address");
        String str2 = this.address;
        if (str2 != null && str2.equals(str)) {
            this.switchCheckAutoConnect.setChecked(true);
        } else {
            this.switchCheckAutoConnect.setChecked(false);
        }
        TeleControlComponent.setOnTeleControlComponentCreateListener(this);
        loadTeleControlInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadTeleControlInfo() {
        if (HelmsmanSDK.getTeleControlComponent() != null) {
            this.relSearchWifi.setVisibility(0);
        } else {
            this.relSearchWifi.setVisibility(8);
        }
    }

    private void initEvent() {
        this.switchCheckAutoConnect.setOnCheckedChangeListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public AutoConnectBlePresenter initPresenter() {
        return new AutoConnectBlePresenter();
    }

    @OnClick({R.id.rel_alias_bluetooth, R.id.rel_search_wifi})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rel_alias_bluetooth) {
            Intent intent = new Intent(this, (Class<?>) BleBlueToothAliasActivity.class);
            intent.putExtra("address", this.address);
            intent.putExtra("name", this.name);
            startActivity(intent);
            return;
        }
        if (id == R.id.rel_search_wifi && HelmsmanSDK.getTeleControlComponent() != null) {
            startActivity(new Intent(this, (Class<?>) ConnectWifiActivity.class));
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            JSave.put("auto_connect_address", this.address);
        } else {
            JSave.put("auto_connect_address", "");
        }
    }

    @Override // im.helmsman.lib.component.TeleControlComponent.OnTeleControlComponentCreateListener
    public void onTeleControlComponentCreate(TeleControlComponent teleControlComponent) {
        runOnUiThread(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.AutoConnectBleActivity.1
            @Override // java.lang.Runnable
            public void run() {
                AutoConnectBleActivity.this.loadTeleControlInfo();
            }
        });
    }
}
