package im.helmsman.helmsmanandroid.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.places.model.PlaceFields;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.adapter.BleBlueToothListAdapter;
import im.helmsman.helmsmanandroid.controller.BleBlueBoatDataIOController;
import im.helmsman.helmsmanandroid.dao.BleBlueToothConnected;
import im.helmsman.helmsmanandroid.ui.common.Base2Activity;
import im.helmsman.helmsmanandroid.utils.PermissionUtils;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.litepal.crud.DataSupport;

/* JADX INFO: loaded from: classes2.dex */
public class BleBlueToothActivity extends Base2Activity implements AdapterView.OnItemLongClickListener, BluetoothAdapter.LeScanCallback, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION_CODE = 1;
    private static final String REMOVE_STOP_BLE_SCAN_TAG = "Stop_BLE_SCAN";
    private static final int REQUEST_ENABLE_BT = 1;
    private BleBlueToothListAdapter adapter;
    private Runnable connectTimeOutRunnable;
    private Handler handler;
    private ImageButton ibBack;

    @BindView(R.id.iv_ble_bluetooth_swipeing)
    ImageView ivBleBluetoothSwipeing;

    @BindView(R.id.iv_ble_connect_delete)
    ImageView ivBleConnectDelete;

    @BindView(R.id.lv_ble_bluetooth)
    ListView lvBleBluetooth;
    private BluetoothAdapter mBlueToothAdapter;

    @BindView(R.id.rel_blebluetooth_permission)
    RelativeLayout mRelPermissionSetting;

    @BindView(R.id.tv_blebluetooth_permission_miss_hint)
    TextView mTvPermissionHint;

    @BindView(R.id.swipe_ble_bluetooth)
    SwipeRefreshLayout swipeBleBluetooth;

    @BindView(R.id.tv_ble_bluetooth_count)
    TextView tvBleBluetoothCount;
    private List<BluetoothDevice> devices = new ArrayList();
    private int findCount = 0;
    private List<BluetoothDevice> checkDevics = new ArrayList();
    List<String> mPermissionList = new ArrayList();
    private Runnable stopBleScanRunnable = new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.1
        @Override // java.lang.Runnable
        public void run() {
            BleBlueToothActivity.this.mBlueToothAdapter.stopLeScan(BleBlueToothActivity.this);
            BleBlueToothActivity.this.ivBleBluetoothSwipeing.clearAnimation();
        }
    };
    private BroadcastReceiver BluetoothBroadcastReceiver = new BroadcastReceiver() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.7
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                MyApplication.getInstance().bluetoothDevice = null;
                Toast.makeText(Base2Activity.getTopActivity(), R.string.ble_disconnected, 0).show();
                BleBlueToothActivity.this.adapter.notifyDataSetChanged();
                return;
            }
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                if (intExtra == 10) {
                    MyApplication.getInstance().bluetoothDevice = null;
                    BleBlueToothActivity.this.adapter.notifyDataSetChanged();
                } else {
                    if (intExtra != 12) {
                        return;
                    }
                    BleBlueToothActivity.this.onRefresh();
                }
            }
        }
    };

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ble_blue_booth);
        setStatusBar(R.color.red_state);
        BleBlueBoatDataIOController bleBlueBoatDataIOController = BleBlueBoatDataIOController.getInstance();
        if (bleBlueBoatDataIOController != null) {
            bleBlueBoatDataIOController.stopScanBle();
        }
        this.handler = new Handler();
        this.mBlueToothAdapter = getBlueToothAdapter();
        initView();
        initEvent();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        try {
            registerReceiver(this.BluetoothBroadcastReceiver, intentFilter);
        } catch (Exception unused) {
        }
        if (getIntent().getBooleanExtra("isFinish", false)) {
            finish();
        }
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        if (checkPermissions()) {
            this.mRelPermissionSetting.setVisibility(8);
            if (openBlueTooth()) {
                showOpenGpsDialog();
            }
        } else {
            List<String> list = this.mPermissionList;
            requestPermission((String[]) list.toArray(new String[list.size()]));
        }
        this.adapter.notifyDataSetChanged();
    }

    private BluetoothAdapter getBlueToothAdapter() {
        return ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
    }

    private void scanBleBlueToothDevice() {
        boolean zStartLeScan;
        BluetoothAdapter bluetoothAdapter = this.mBlueToothAdapter;
        if (bluetoothAdapter != null && (zStartLeScan = bluetoothAdapter.startLeScan(this))) {
            startRoateAnimation();
            this.devices.clear();
            this.findCount = 0;
            Log.e("scan", zStartLeScan + "");
            this.handler.postDelayed(this.stopBleScanRunnable, 30000L);
        }
    }

    private void initView() {
        ImageButton imageButton = (ImageButton) findViewById(R.id.ib_back);
        this.ibBack = imageButton;
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BleBlueToothActivity.this.finish();
            }
        });
        if (this.findCount == 0 && MyApplication.getInstance().bluetoothDevice != null) {
            this.findCount = 1;
        }
        this.tvBleBluetoothCount.setText(this.findCount + "");
        BleBlueToothListAdapter bleBlueToothListAdapter = new BleBlueToothListAdapter(this, this.devices, this.checkDevics);
        this.adapter = bleBlueToothListAdapter;
        this.lvBleBluetooth.setAdapter((ListAdapter) bleBlueToothListAdapter);
    }

    private void initEvent() {
        this.lvBleBluetooth.setOnItemLongClickListener(this);
        this.lvBleBluetooth.setOnItemClickListener(this);
        this.swipeBleBluetooth.setOnRefreshListener(this);
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        this.findCount = 0;
        this.devices.clear();
        BleBlueBoatDataIOController.getInstance().closeConnect();
        if (checkPermissions()) {
            showOpenGpsDialog();
        }
        this.swipeBleBluetooth.setRefreshing(false);
        this.adapter.notifyDataSetChanged();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.adapter.getIsCheck()) {
            this.adapter.setIsCheck(false);
            this.checkDevics.clear();
            this.ivBleConnectDelete.setVisibility(8);
            this.adapter.notifyDataSetChanged();
            return;
        }
        super.onBackPressed();
    }

    private void startRoateAnimation() {
        this.ivBleBluetoothSwipeing.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_motor_roate));
    }

    @OnClick({R.id.rel_blebluetooth_permission, R.id.iv_ble_connect_delete})
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.iv_ble_connect_delete) {
            if (id != R.id.rel_blebluetooth_permission) {
                return;
            }
            PermissionUtils.startAppSettingActivity();
            return;
        }
        Iterator<BluetoothDevice> it = this.checkDevics.iterator();
        while (it.hasNext()) {
            List listFind = DataSupport.where("address = ?", it.next().getAddress()).find(BleBlueToothConnected.class);
            if (listFind != null && listFind.size() > 0) {
                ((BleBlueToothConnected) listFind.get(0)).delete();
            }
        }
        this.checkDevics.clear();
        this.adapter.setIsCheck(false);
        this.ivBleConnectDelete.setVisibility(8);
        if (MyApplication.getInstance().bluetoothDevice != null) {
            BleBlueBoatDataIOController.getInstance().closeConnect();
        }
        this.devices.clear();
        MyApplication.getInstance().bluetoothDevice = null;
        this.adapter.notifyDataSetChanged();
        scanBleBlueToothDevice();
    }

    @Override // android.widget.AdapterView.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        List listFindAll = DataSupport.findAll(BleBlueToothConnected.class, new long[0]);
        if (listFindAll != null && listFindAll.size() >= i) {
            this.adapter.setIsCheck(true);
            this.adapter.notifyDataSetChanged();
        }
        Log.e("onItemLongClick", "LongClick");
        return true;
    }

    class BlueToothEventOnUIListener extends BluetoothGattCallback {
        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
        }

        BlueToothEventOnUIListener() {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(final BluetoothGatt bluetoothGatt, final int i, final int i2) {
            BleBlueToothActivity.this.handler.post(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.BlueToothEventOnUIListener.1
                @Override // java.lang.Runnable
                public void run() {
                    int i3 = i;
                    if (i3 == 0 && i2 == 2) {
                        ViewUtils.cancelProgressDialog();
                        BleBlueToothActivity.this.handler.removeCallbacks(BleBlueToothActivity.this.connectTimeOutRunnable);
                        BluetoothDevice device = bluetoothGatt.getDevice();
                        MyApplication.getInstance().bluetoothDevice = device;
                        String address = device.getAddress();
                        Log.e("address", address);
                        if (DataSupport.where("address = ?", address).find(BleBlueToothConnected.class).size() <= 0) {
                            BleBlueToothConnected bleBlueToothConnected = new BleBlueToothConnected(address);
                            bleBlueToothConnected.setName(device.getName());
                            bleBlueToothConnected.save();
                        }
                        BleBlueToothActivity.this.adapter.notifyDataSetChanged();
                        return;
                    }
                    if (i3 == 0 && i2 == 0) {
                        MyApplication.getInstance().bluetoothDevice = null;
                        Toast.makeText(Base2Activity.getTopActivity(), R.string.ble_disconnected, 0).show();
                        BleBlueToothActivity.this.adapter.notifyDataSetChanged();
                    } else if (i3 == 133) {
                        ViewUtils.cancelProgressDialog();
                        ViewUtils.ShowToast(R.string.bluetooth_connection_fail);
                    }
                }
            });
        }
    }

    private boolean openBlueTooth() {
        BluetoothAdapter bluetoothAdapter = this.mBlueToothAdapter;
        if (bluetoothAdapter == null) {
            ViewUtils.ShowToast(getString(R.string.transferstation_not_bluetooth));
            return false;
        }
        if (bluetoothAdapter.isEnabled()) {
            return true;
        }
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        return false;
    }

    @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        if (this.devices.contains(bluetoothDevice)) {
            return;
        }
        this.devices.add(bluetoothDevice);
        Log.d("onLeScan", bluetoothDevice.getAddress() + "~~~~~~~~~~~~~~~~~~`" + this.devices.size());
        this.adapter.notifyDataSetChanged();
        this.findCount++;
        this.tvBleBluetoothCount.setText(this.findCount + "");
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.adapter.getIsCheck()) {
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_ble_connect_delete);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(!checkBox.isChecked());
            if (checkBox.isChecked() && checkBox.getVisibility() == 0) {
                if (!this.checkDevics.contains(this.devices.get(i))) {
                    this.checkDevics.add(this.devices.get(i));
                }
            } else if (checkBox.getVisibility() == 0) {
                this.checkDevics.remove(this.devices.get(i));
            }
            if (this.checkDevics.size() > 0) {
                this.ivBleConnectDelete.setVisibility(0);
            } else {
                this.ivBleConnectDelete.setVisibility(8);
            }
            this.adapter.notifyDataSetChanged();
            return;
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_item_bluebooth_address);
        TextView textView2 = (TextView) view.findViewById(R.id.tv_item_bluebooth_name);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_bluetooth_correct);
        final String string = textView.getText().toString();
        String string2 = textView2.getText().toString();
        if (imageView.getVisibility() == 0) {
            Intent intent = new Intent(this, (Class<?>) AutoConnectBleActivity.class);
            intent.putExtra("address", string);
            intent.putExtra("name", string2);
            startActivity(intent);
            ViewUtils.ShowToast(getString(R.string.bluetooth_connected));
            return;
        }
        ViewUtils.showProgressDialog(getString(R.string.bluetooth_connecting_bluetooth)).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.3
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                BleBlueBoatDataIOController.getInstance().closeConnect();
            }
        });
        if (MyApplication.getInstance().bluetoothDevice != null) {
            BleBlueBoatDataIOController.getInstance().closeConnect();
        }
        this.mBlueToothAdapter.stopLeScan(this);
        this.handler.postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.4
            @Override // java.lang.Runnable
            public void run() {
                BleBlueBoatDataIOController.getInstance().connectBlueTooth(Base2Activity.getTopActivity().getApplicationContext(), string, null, BleBlueToothActivity.this.new BlueToothEventOnUIListener());
            }
        }, 200L);
        Runnable runnable = new Runnable() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.5
            @Override // java.lang.Runnable
            public void run() {
                ViewUtils.cancelProgressDialog();
                ViewUtils.ShowToast(R.string.bluetooth_connection_fail);
            }
        };
        this.connectTimeOutRunnable = runnable;
        this.handler.postDelayed(runnable, 15000L);
    }

    private boolean checkPermissions() {
        this.mPermissionList.clear();
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            this.mPermissionList.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (Build.VERSION.SDK_INT >= 31 && (ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_SCAN") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_ADVERTISE") != 0 || ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_CONNECT") != 0 || this.mPermissionList.size() > 0)) {
            this.mPermissionList.add("android.permission.BLUETOOTH_SCAN");
            this.mPermissionList.add("android.permission.BLUETOOTH_ADVERTISE");
            this.mPermissionList.add("android.permission.BLUETOOTH_CONNECT");
        }
        return this.mPermissionList.size() <= 0;
    }

    private void requestPermission(String[] strArr) {
        ActivityCompat.requestPermissions(this, strArr, 1);
    }

    private void scanBlueToothDevice() {
        if (MyApplication.getInstance().bluetoothDevice == null) {
            scanBleBlueToothDevice();
            this.findCount = 0;
            this.devices.clear();
            this.adapter.notifyDataSetChanged();
        }
    }

    private void showOpenGpsDialog() {
        if (gpsIsOn()) {
            scanBlueToothDevice();
        } else {
            ViewUtils.showAlertDialog(R.string.location_is_not_open, R.string.location_is_not_open_message, R.string.setting, R.string.ignore, new DialogInterface.OnClickListener() { // from class: im.helmsman.helmsmanandroid.ui.activity.BleBlueToothActivity.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    BleBlueToothActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                }
            });
        }
    }

    /* JADX WARN: Code duplicated, block: B:24:0x0048  */
    /* JADX WARN: Code duplicated, block: B:26:0x004c  */
    /* JADX WARN: Code duplicated, block: B:41:0x007c  */
    /* JADX WARN: Code duplicated, block: B:43:0x0082  */
    /* JADX WARN: Code duplicated, block: B:45:0x008b  */
    /* JADX WARN: Code duplicated, block: B:47:0x008f  */
    /* JADX WARN: Code duplicated, block: B:48:0x0098  */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 1) {
            int length = iArr.length;
            if (length == 1) {
                if (iArr[0] == 0) {
                    if (Build.VERSION.SDK_INT < 31 && iArr.length > 3) {
                        if (iArr[1] == 0 && iArr[2] == 0 && iArr[3] == 0) {
                            if (openBlueTooth()) {
                                showOpenGpsDialog();
                            }
                            this.mRelPermissionSetting.setVisibility(8);
                        } else {
                            this.mRelPermissionSetting.setVisibility(0);
                            this.mTvPermissionHint.setText(R.string.location_permission_authorized_after_scan_ble_os13);
                            ViewUtils.ShowToast(R.string.blebluetoothactivity_bluetooth_permission_denied_os13);
                        }
                    } else {
                        if (openBlueTooth()) {
                            showOpenGpsDialog();
                        }
                        this.mRelPermissionSetting.setVisibility(8);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 31) {
                        this.mTvPermissionHint.setText(R.string.location_permission_authorized_after_scan_ble_os13);
                        ViewUtils.ShowToast(R.string.blebluetoothactivity_bluetooth_permission_denied_os13);
                    } else {
                        this.mTvPermissionHint.setText(R.string.location_permission_authorized_after_scan_ble);
                        ViewUtils.ShowToast(R.string.blebluetoothactivity_bluetooth_permission_denied);
                    }
                    this.mRelPermissionSetting.setVisibility(0);
                }
            } else if (length != 3) {
                if (length == 4) {
                    if (iArr[0] == 0) {
                        if (Build.VERSION.SDK_INT < 31) {
                            if (openBlueTooth()) {
                                showOpenGpsDialog();
                            }
                            this.mRelPermissionSetting.setVisibility(8);
                        } else {
                            if (openBlueTooth()) {
                                showOpenGpsDialog();
                            }
                            this.mRelPermissionSetting.setVisibility(8);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 31) {
                            this.mTvPermissionHint.setText(R.string.location_permission_authorized_after_scan_ble_os13);
                            ViewUtils.ShowToast(R.string.blebluetoothactivity_bluetooth_permission_denied_os13);
                        } else {
                            this.mTvPermissionHint.setText(R.string.location_permission_authorized_after_scan_ble);
                            ViewUtils.ShowToast(R.string.blebluetoothactivity_bluetooth_permission_denied);
                        }
                        this.mRelPermissionSetting.setVisibility(0);
                    }
                }
            } else if (iArr[0] == 0 && iArr[1] == 0 && iArr[2] == 0) {
                if (openBlueTooth()) {
                    showOpenGpsDialog();
                }
                this.mRelPermissionSetting.setVisibility(8);
            } else {
                if (Build.VERSION.SDK_INT >= 31) {
                    this.mTvPermissionHint.setText(R.string.location_permission_authorized_after_scan_ble_os13);
                    ViewUtils.ShowToast(R.string.blebluetoothactivity_bluetooth_permission_denied_os13);
                }
                this.mRelPermissionSetting.setVisibility(0);
            }
        }
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    @Override // im.helmsman.helmsmanandroid.ui.common.Base2Activity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.BluetoothBroadcastReceiver);
        this.handler.removeCallbacks(this.stopBleScanRunnable);
        BluetoothAdapter bluetoothAdapter = this.mBlueToothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(this);
        }
        BluetoothDevice bluetoothDevice = MyApplication.getInstance().bluetoothDevice;
    }

    private boolean gpsIsOn() {
        return ((LocationManager) getSystemService(PlaceFields.LOCATION)).isProviderEnabled("gps");
    }
}
