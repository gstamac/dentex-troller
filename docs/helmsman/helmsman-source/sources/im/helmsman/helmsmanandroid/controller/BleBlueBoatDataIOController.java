package im.helmsman.helmsmanandroid.controller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.MAVLink.Messages.MAVLinkMessage;
import com.bugsnag.android.Bugsnag;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import im.helmsman.helmsmanandroid.utils.LogUtil;
import im.helmsman.helmsmanandroid.utils.MavlinkUtil;
import im.helmsman.helmsmanandroid.utils.ViewUtils;
import im.helmsman.lib.DataIOManager;
import im.helmsman.lib.HelmsmanSDK;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import javy.lib.jsave.JSave;

/* JADX INFO: loaded from: classes2.dex */
public class BleBlueBoatDataIOController extends BaseIoController implements DataIOManager.OnSendDatasListener, MavlinkUtil.OncreateMavlinkMessageListener {
    public static BluetoothGatt gatt;
    private static BleBlueBoatDataIOController instance;
    private BluetoothAdapter adapter;
    private BluetoothGattCallback bluetoothGattCallback;
    private BluetoothAdapter.LeScanCallback callback;
    private String connectAddress;
    private BluetoothGattCharacteristic readBluetoothGattCharacteristic;
    private BluetoothGattCharacteristic writeBluetoothGattCharacteristic;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS", "android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_PRIVILEGED"};
    private static String[] PERMISSIONS_LOCATION = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS", "android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_PRIVILEGED"};
    private UUID uuid = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private UUID uuid2 = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private UUID uuid3 = UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb");
    LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<>(20);

    @Override // im.helmsman.helmsmanandroid.utils.MavlinkUtil.OncreateMavlinkMessageListener
    public void generateMavlink(MAVLinkMessage mAVLinkMessage) {
    }

    @Override // im.helmsman.helmsmanandroid.view.Rudder.RudderListener
    public void onClickStop() {
    }

    @Override // im.helmsman.helmsmanandroid.controller.BaseIoController
    public void readtBotaMessage() {
    }

    public static BleBlueBoatDataIOController getInstance() {
        if (instance == null) {
            instance = new BleBlueBoatDataIOController();
        }
        return instance;
    }

    public BleBlueBoatDataIOController() {
        HelmsmanSDK.getDataIOManager().setSendDatasListener(this);
        new Thread(new sendCommandByQueue(), "messageQuery").start();
        MavlinkUtil.addOnCreateMavlinkMessageListener(this);
    }

    @Override // im.helmsman.helmsmanandroid.controller.BaseIoController
    protected void sendCommand(byte[] bArr) {
        if (this.writeBluetoothGattCharacteristic == null) {
            return;
        }
        try {
            this.queue.put(bArr);
            LogUtil.instance().saveSendCommand(bArr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override // im.helmsman.lib.DataIOManager.OnSendDatasListener
    public void sendDatas(byte[] bArr) {
        sendCommand(bArr);
    }

    class sendCommandByQueue implements Runnable {
        private final int length = 20;

        sendCommandByQueue() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    byte[] bArrTake = BleBlueBoatDataIOController.this.queue.take();
                    Thread.sleep(15L);
                    int length = bArrTake.length % 20;
                    for (int i = 0; i < bArrTake.length / 20 && BleBlueBoatDataIOController.gatt != null; i++) {
                        int i2 = i * 20;
                        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArrTake, i2, i2 + 20);
                        if (bArrCopyOfRange == null) {
                            throw new NullPointerException("诡异~tempValue1,command:" + Arrays.toString(bArrTake) + " index:" + i);
                        }
                        BleBlueBoatDataIOController.this.writeBluetoothGattCharacteristic.setValue(bArrCopyOfRange);
                        BleBlueBoatDataIOController.gatt.writeCharacteristic(BleBlueBoatDataIOController.this.writeBluetoothGattCharacteristic);
                        Thread.sleep(15L);
                    }
                    if (length > 0 && BleBlueBoatDataIOController.gatt != null) {
                        byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArrTake, bArrTake.length - length, bArrTake.length);
                        if (bArrCopyOfRange2 == null) {
                            throw new NullPointerException("诡异~tempValue2,command:" + Arrays.toString(bArrTake) + " y:" + length);
                        }
                        BleBlueBoatDataIOController.this.writeBluetoothGattCharacteristic.setValue(bArrCopyOfRange2);
                        BleBlueBoatDataIOController.gatt.writeCharacteristic(BleBlueBoatDataIOController.this.writeBluetoothGattCharacteristic);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void connectBlueTooth(Context context, String str, BluetoothDevice bluetoothDevice, BluetoothGattCallback bluetoothGattCallback) {
        this.bluetoothGattCallback = bluetoothGattCallback;
        if (!BluetoothAdapter.checkBluetoothAddress(str)) {
            ViewUtils.ShowToast(R.string.valid_bluetooth_device);
            return;
        }
        if (bluetoothDevice == null) {
            bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(str);
        }
        BluetoothGatt bluetoothGatt = gatt;
        if (bluetoothGatt == null) {
            gatt = bluetoothDevice.connectGatt(context, false, new MyBluetoothGattCallback());
        } else {
            if (!bluetoothGatt.getDevice().equals(bluetoothDevice)) {
                gatt.close();
                gatt = null;
                gatt = bluetoothDevice.connectGatt(context, false, new MyBluetoothGattCallback());
                return;
            }
            gatt.connect();
        }
    }

    public synchronized void closeConnect() {
        try {
            BluetoothGatt bluetoothGatt = gatt;
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect();
            }
        } catch (NullPointerException e) {
            Bugsnag.notify(e);
        }
    }

    public void autoScanBle(Activity activity) {
        BluetoothAdapter adapter = ((BluetoothManager) MyApplication.getInstance().getSystemService("bluetooth")).getAdapter();
        this.adapter = adapter;
        if (adapter == null) {
            ViewUtils.ShowToast(R.string.none_bluetooth);
            return;
        }
        this.callback = new BluetoothAdapter.LeScanCallback() { // from class: im.helmsman.helmsmanandroid.controller.BleBlueBoatDataIOController.1
            @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
            public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
                String name = bluetoothDevice.getName();
                Log.e("onLeScan:" + Thread.currentThread().getName(), name == null ? bluetoothDevice.getAddress() : name + bluetoothDevice.getAddress());
                final String str = (String) JSave.get("auto_connect_address");
                String address = bluetoothDevice.getAddress();
                if (str == null || str.equals("")) {
                    BleBlueBoatDataIOController.this.stopScanBle();
                    return;
                }
                if (!str.equals(address) || address.equals(BleBlueBoatDataIOController.this.connectAddress)) {
                    return;
                }
                BleBlueBoatDataIOController.this.connectAddress = address;
                if (MyApplication.getInstance().bluetoothDevice != null) {
                    BleBlueBoatDataIOController.this.closeConnect();
                }
                BleBlueBoatDataIOController.this.adapter.stopLeScan(this);
                if (MyApplication.getInstance().bluetoothDevice != null) {
                    BleBlueBoatDataIOController.this.closeConnect();
                    new Handler().postDelayed(new Runnable() { // from class: im.helmsman.helmsmanandroid.controller.BleBlueBoatDataIOController.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BleBlueBoatDataIOController.this.connectBlueTooth(MyApplication.getInstance(), str, bluetoothDevice, null);
                        }
                    }, 5000L);
                } else {
                    BleBlueBoatDataIOController.this.connectBlueTooth(MyApplication.getInstance(), str, bluetoothDevice, null);
                }
            }
        };
        BluetoothAdapter bluetoothAdapter = this.adapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.enable()) {
            return;
        }
        if (this.adapter.isEnabled()) {
            this.adapter.startLeScan(this.callback);
        } else {
            Log.e("auto scan error", "bluetooth adapter off", new Throwable("bluetooth adapter off"));
        }
    }

    private void checkPermissions(Activity activity) {
        int iCheckSelfPermission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
        int iCheckSelfPermission2 = ActivityCompat.checkSelfPermission(activity, "android.permission.BLUETOOTH_SCAN");
        if (iCheckSelfPermission != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        } else if (iCheckSelfPermission2 != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_LOCATION, 1);
        }
    }

    public void stopScanBle() {
        BluetoothAdapter bluetoothAdapter = this.adapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return;
        }
        this.adapter.stopLeScan(this.callback);
    }

    class MyBluetoothGattCallback extends BluetoothGattCallback {
        MyBluetoothGattCallback() {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 == 2) {
                bluetoothGatt.discoverServices();
            }
            if (BleBlueBoatDataIOController.this.bluetoothGattCallback != null) {
                BleBlueBoatDataIOController.this.bluetoothGattCallback.onConnectionStateChange(bluetoothGatt, i, i2);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            BluetoothGattService service = bluetoothGatt.getService(BleBlueBoatDataIOController.this.uuid);
            BleBlueBoatDataIOController bleBlueBoatDataIOController = BleBlueBoatDataIOController.this;
            bleBlueBoatDataIOController.writeBluetoothGattCharacteristic = service.getCharacteristic(bleBlueBoatDataIOController.uuid2);
            BleBlueBoatDataIOController bleBlueBoatDataIOController2 = BleBlueBoatDataIOController.this;
            bleBlueBoatDataIOController2.readBluetoothGattCharacteristic = service.getCharacteristic(bleBlueBoatDataIOController2.uuid3);
            BleBlueBoatDataIOController.this.writeBluetoothGattCharacteristic.setWriteType(1);
            BleBlueBoatDataIOController.gatt = bluetoothGatt;
            if (i == 0) {
                BleBlueBoatDataIOController.gatt.setCharacteristicNotification(BleBlueBoatDataIOController.this.readBluetoothGattCharacteristic, true);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            byte[] bArr = {-2, 2, 0, -1, -56, 43, 0, 0, -12, 22};
            byte[] value = bluetoothGattCharacteristic.getValue();
            int length = 10 > value.length ? value.length : 10;
            for (int i2 = 0; i2 < length && bArr[i2] == value[i2]; i2++) {
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
            byte[] value = bluetoothGattCharacteristic.getValue();
            HelmsmanSDK.getDataIOManager().decode(value);
            MavlinkUtil.dataToMAVLinkMessage(value);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
        }
    }
}
