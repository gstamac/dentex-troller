package im.helmsman.helmsmanandroid.ui.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.controller.BleBlueBoatDataIOController;
import im.helmsman.helmsmanandroid.presenter.common.BasePresenter;
import im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity;
import im.helmsman.helmsmanandroid.ui.fragment.PMainFragment;
import im.helmsman.helmsmanandroid.ui.fragment.TMainFragment;
import im.helmsman.helmsmanandroid.utils.AppVersionUtils;
import im.helmsman.helmsmanandroid.utils.UnitConvertUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.lib.HelmsmanSDK;
import java.math.BigDecimal;
import java.util.ArrayList;
import javy.lib.jsave.JSave;

/* JADX INFO: loaded from: classes2.dex */
public class MainActivity extends Mvp2BaseActivity {
    public static final int BATTERY_STATE = 5;
    public static final int BOAT_DIRECTION_STATE = 9;
    public static final int BOAT_MODEL_STATE = 4;
    public static final int BOAT_TARGET = 11;
    public static final int FLICKER_SPEED_METER = 3;
    public static final int GPSSIGNAL_STATE = 6;
    public static final int HAVE_WIFI_STATE = 2;
    public static final int HEART_BEAT = 10;
    public static final int NO_WIFI_STATE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final int PERMISSION_REQUEST_READ_PHONE_STATE_CODE = 2;
    public static final int PERMISSION_REQUEST_WRITE_CODE = 1;
    public static final int POSITION_STATE = 8;
    public static final int REMOTE_CONTROL_BATTERY = 12;
    public static final int SERVICE_CREATE = 0;
    public static final int SPEED_STATE = 7;
    private Fragment mUiFragment;
    private long firstClick = 0;
    private BroadcastReceiver BluetoothBroadcastReceiver = new BroadcastReceiver() { // from class: im.helmsman.helmsmanandroid.ui.activity.MainActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                MyApplication.getInstance().bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                BleBlueBoatDataIOController.getInstance().stopScanBle();
                return;
            }
            if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                MyApplication.getInstance().bluetoothDevice = null;
                if (MainActivity.this.activityIsTop(BleBlueToothActivity.class)) {
                    return;
                }
                MyApplication.getInstance().ssid = null;
                new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.MainActivity.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BleBlueBoatDataIOController.getInstance().autoScanBle(MainActivity.this);
                    }
                }, 5000L);
                return;
            }
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                if (intExtra != 10) {
                    if (intExtra == 12) {
                        if (MainActivity.this.activityIsTop(BleBlueToothActivity.class)) {
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= 31) {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.BLUETOOTH_SCAN") == 0 || ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.BLUETOOTH_ADVERTISE") == 0 || ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.BLUETOOTH_CONNECT") == 0) {
                                BleBlueBoatDataIOController.getInstance().autoScanBle(MainActivity.this);
                                return;
                            }
                            return;
                        }
                        BleBlueBoatDataIOController.getInstance().autoScanBle(MainActivity.this);
                        return;
                    }
                    if (intExtra != 13) {
                        return;
                    }
                }
                BleBlueBoatDataIOController.getInstance().stopScanBle();
            }
        }
    };

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public BasePresenter initPresenter() {
        return null;
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        setStatusBar(R.color.black);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED");
        intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        try {
            registerReceiver(this.BluetoothBroadcastReceiver, intentFilter);
        } catch (Exception unused) {
        }
        checkPermission();
        showPrivatePolicyDialog();
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity, im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.BluetoothBroadcastReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Mvp2BaseActivity
    public void initFragment() {
        if (isPad) {
            this.mUiFragment = new TMainFragment();
        } else {
            this.mUiFragment = new PMainFragment();
        }
        FragmentTransaction fragmentTransactionBeginTransaction = getFragmentManager().beginTransaction();
        fragmentTransactionBeginTransaction.replace(R.id.fram_main_root, this.mUiFragment);
        fragmentTransactionBeginTransaction.commit();
    }

    private void checkPermission() {
        ArrayList arrayList = new ArrayList();
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
            arrayList.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (Build.VERSION.SDK_INT >= 31 && (ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_SCAN") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_CONNECT") != 0)) {
            arrayList.add("android.permission.BLUETOOTH_SCAN");
            arrayList.add("android.permission.BLUETOOTH_CONNECT");
        }
        if (arrayList.size() > 0) {
            ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[arrayList.size()]), 1);
            return;
        }
        if (BleBlueBoatDataIOController.getInstance() != null) {
            BleBlueBoatDataIOController.getInstance().autoScanBle(this);
        }
        new AppVersionUtils().checkAppVersion();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"}, 2);
        } else {
            new AppVersionUtils().checkAppVersion();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        if (System.currentTimeMillis() - this.firstClick > 2000) {
            this.firstClick = System.currentTimeMillis();
            Toast.makeText(this, getString(R.string.press_again) + " " + getString(R.string.app_name), 1).show();
        } else {
            if (!HelmsmanSDK.productIsNull() && HelmsmanSDK.getProduct().motorComponent != null && HelmsmanSDK.getProduct().motorComponent.isOn()) {
                HelmsmanSDK.getProduct().motorComponent.motorOff();
            }
            finishAllActivity();
            BleBlueBoatDataIOController.getInstance().closeConnect();
            System.exit(0);
        }
        return true;
    }

    public CharSequence intToStringSpeed(float f) {
        SpannableString spannableString;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.writeInt(25);
        parcelObtain.setDataPosition(0);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(parcelObtain);
        if (Config.SPEED_UNIT == 1) {
            spannableStringBuilder.append((CharSequence) (new BigDecimal((float) UnitConvertUtil.kphConvertmph(f)).setScale(1, 4).doubleValue() + "\n"));
            spannableString = new SpannableString("MPH");
        } else {
            spannableStringBuilder.append((CharSequence) (f + "\n"));
            spannableString = new SpannableString("KPH");
        }
        spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), 33);
        spannableStringBuilder.append((CharSequence) spannableString);
        return spannableStringBuilder;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (iArr == null || iArr.length <= 0 || iArr[0] != 0) {
            ViewUtils.ShowToast(R.string.main_connection_established_readwirte_permission_denied);
        }
        if (Build.VERSION.SDK_INT >= 31) {
            if (iArr != null && iArr.length > 4 && iArr[2] == 0 && iArr[3] == 0 && iArr[4] == 0) {
                if (BleBlueBoatDataIOController.getInstance() != null) {
                    BleBlueBoatDataIOController.getInstance().autoScanBle(this);
                }
            } else {
                ViewUtils.ShowToast(R.string.Please_turn_on_bluetooth);
            }
        } else if (iArr != null && iArr.length > 2 && iArr[2] == 0) {
            if (BleBlueBoatDataIOController.getInstance() != null) {
                BleBlueBoatDataIOController.getInstance().autoScanBle(this);
            }
        } else {
            ViewUtils.ShowToast(R.string.Please_turn_on_bluetooth);
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    private void showPrivatePolicyDialog() {
        long jLongValue;
        final long jLongValue2;
        final String str = (String) JSave.get("private_policy_url");
        try {
            jLongValue = ((Long) JSave.get("read_private_policy_time")).longValue();
            try {
                jLongValue2 = ((Long) JSave.get("private_policy_time")).longValue();
            } catch (NullPointerException unused) {
                jLongValue2 = 1;
            }
        } catch (NullPointerException unused2) {
            jLongValue = 0;
        }
        if (jLongValue >= jLongValue2) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(jLongValue > 0 ? R.string.privacy_policy_update : R.string.private_policy);
        builder.setMessage(jLongValue > 0 ? R.string.private_policy_message_update : R.string.private_policy_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Accect, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.MainActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                JSave.put("read_private_policy_time", Long.valueOf(jLongValue2));
            }
        });
        builder.setNegativeButton(R.string.details, (DialogInterface.OnClickListener) null);
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.show();
        alertDialogCreate.getButton(-2).setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.MainActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, (Class<?>) WebViewActivity.class);
                intent.putExtra("weburl", TextUtils.isEmpty(str) ? "http://qnimg.helmsman.im/agreement/helmsman-pp.html" : str);
                intent.putExtra("title", MainActivity.this.getString(R.string.private_policy));
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
