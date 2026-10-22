package com.facebook.places.internal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.internal.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BleScannerImpl implements BleScanner {
    private static final String TAG = "BleScannerImpl";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private Context context;
    private int errorCode;
    private boolean isScanInProgress;
    private LocationPackageRequestParams params;
    private ScanCallBackImpl scanCallBack;
    private final List<BluetoothScanResult> scanResults = new ArrayList();
    private static final byte[] IBEACON_PREFIX = fromHexString("ff4c000215");
    private static final byte[] EDDYSTONE_PREFIX = fromHexString("16aafe");
    private static final byte[] GRAVITY_PREFIX = fromHexString("17ffab01");

    BleScannerImpl(Context context, LocationPackageRequestParams locationPackageRequestParams) {
        this.context = context;
        this.params = locationPackageRequestParams;
    }

    @Override // com.facebook.places.internal.BleScanner
    public synchronized void initAndCheckEligibility() throws ScannerException {
        if (!Validate.hasBluetoothPermission(this.context)) {
            throw new ScannerException(ScannerException.Type.PERMISSION_DENIED);
        }
        if (!Validate.hasLocationPermission(this.context)) {
            throw new ScannerException(ScannerException.Type.PERMISSION_DENIED);
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.bluetoothAdapter = defaultAdapter;
        if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
            throw new ScannerException(ScannerException.Type.DISABLED);
        }
        BluetoothLeScanner bluetoothLeScanner = this.bluetoothAdapter.getBluetoothLeScanner();
        this.bluetoothLeScanner = bluetoothLeScanner;
        if (bluetoothLeScanner == null) {
            throw new ScannerException(ScannerException.Type.UNKNOWN_ERROR);
        }
    }

    @Override // com.facebook.places.internal.BleScanner
    public synchronized void startScanning() throws ScannerException {
        if (this.isScanInProgress) {
            throw new ScannerException(ScannerException.Type.SCAN_ALREADY_IN_PROGRESS);
        }
        this.scanCallBack = new ScanCallBackImpl();
        this.isScanInProgress = true;
        this.errorCode = 0;
        synchronized (this.scanResults) {
            this.scanResults.clear();
        }
        if (this.bluetoothLeScanner == null) {
            throw new ScannerException(ScannerException.Type.UNKNOWN_ERROR);
        }
        try {
            ScanSettings.Builder builder = new ScanSettings.Builder();
            builder.setScanMode(2);
            builder.setReportDelay(0L);
            this.bluetoothLeScanner.startScan((List<ScanFilter>) null, builder.build(), this.scanCallBack);
            this.isScanInProgress = true;
        } catch (Exception unused) {
            throw new ScannerException(ScannerException.Type.UNKNOWN_ERROR);
        }
    }

    @Override // com.facebook.places.internal.BleScanner
    public synchronized void stopScanning() {
        this.bluetoothLeScanner.flushPendingScanResults(this.scanCallBack);
        this.bluetoothLeScanner.stopScan(this.scanCallBack);
        waitForMainLooper(this.params.getBluetoothFlushResultsTimeoutMs());
        this.isScanInProgress = false;
    }

    private void waitForMainLooper(long j) {
        try {
            final Object obj = new Object();
            synchronized (obj) {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.facebook.places.internal.BleScannerImpl.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            synchronized (obj) {
                                obj.notify();
                            }
                        } catch (Exception e) {
                            BleScannerImpl.logException("Exception waiting for main looper", e);
                        }
                    }
                });
                obj.wait(j);
            }
        } catch (Exception e) {
            logException("Exception waiting for main looper", e);
        }
    }

    @Override // com.facebook.places.internal.BleScanner
    public synchronized int getErrorCode() {
        return this.errorCode;
    }

    @Override // com.facebook.places.internal.BleScanner
    public synchronized List<BluetoothScanResult> getScanResults() {
        ArrayList arrayList;
        synchronized (this.scanResults) {
            int bluetoothMaxScanResults = this.params.getBluetoothMaxScanResults();
            if (this.scanResults.size() > bluetoothMaxScanResults) {
                arrayList = new ArrayList(bluetoothMaxScanResults);
                Collections.sort(this.scanResults, new Comparator<BluetoothScanResult>() { // from class: com.facebook.places.internal.BleScannerImpl.2
                    @Override // java.util.Comparator
                    public int compare(BluetoothScanResult bluetoothScanResult, BluetoothScanResult bluetoothScanResult2) {
                        return bluetoothScanResult2.rssi - bluetoothScanResult.rssi;
                    }
                });
                arrayList.addAll(this.scanResults.subList(0, bluetoothMaxScanResults));
            } else {
                arrayList = new ArrayList(this.scanResults.size());
                arrayList.addAll(this.scanResults);
            }
        }
        return arrayList;
    }

    private class ScanCallBackImpl extends ScanCallback {
        private ScanCallBackImpl() {
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int i) {
            super.onScanFailed(i);
            BleScannerImpl.this.errorCode = i;
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onBatchScanResults(List<ScanResult> list) {
            super.onBatchScanResults(list);
            try {
                synchronized (BleScannerImpl.this.scanResults) {
                    Iterator<ScanResult> it = list.iterator();
                    while (it.hasNext()) {
                        BluetoothScanResult bluetoothScanResultNewBluetoothScanResult = BleScannerImpl.newBluetoothScanResult(it.next());
                        if (bluetoothScanResultNewBluetoothScanResult != null) {
                            BleScannerImpl.this.scanResults.add(bluetoothScanResultNewBluetoothScanResult);
                        }
                    }
                }
            } catch (Exception e) {
                BleScannerImpl.logException("Exception in ble scan callback", e);
            }
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            try {
                synchronized (BleScannerImpl.this.scanResults) {
                    BluetoothScanResult bluetoothScanResultNewBluetoothScanResult = BleScannerImpl.newBluetoothScanResult(scanResult);
                    if (bluetoothScanResultNewBluetoothScanResult != null) {
                        BleScannerImpl.this.scanResults.add(bluetoothScanResultNewBluetoothScanResult);
                    }
                }
            } catch (Exception e) {
                BleScannerImpl.logException("Exception in ble scan callback", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BluetoothScanResult newBluetoothScanResult(ScanResult scanResult) {
        ScanRecord scanRecord = scanResult.getScanRecord();
        if (isBeacon(scanRecord.getBytes())) {
            return new BluetoothScanResult(formatPayload(scanRecord.getBytes()), scanResult.getRssi(), scanResult.getTimestampNanos());
        }
        return null;
    }

    private static String formatPayload(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        return toHexString(bArr, getPayloadLength(bArr));
    }

    private static int getPayloadLength(byte[] bArr) {
        int i = 0;
        while (i < bArr.length) {
            byte b = bArr[i];
            if (b == 0) {
                return i;
            }
            if (b < 0) {
                return bArr.length;
            }
            i += b + 1;
        }
        return bArr.length;
    }

    private static String toHexString(byte[] bArr, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        if (i < 0 || i > bArr.length) {
            i = bArr.length;
        }
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(String.format("%02x", Byte.valueOf(bArr[i2])));
        }
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logException(String str, Exception exc) {
        if (FacebookSdk.isDebugEnabled()) {
            Log.e(TAG, str, exc);
        }
    }

    private static boolean isBeacon(byte[] bArr) {
        int i;
        if (bArr == null) {
            return false;
        }
        int length = bArr.length;
        int i2 = 0;
        while (i2 < length) {
            byte b = bArr[i2];
            if (b <= 0 || (i = b + 1 + i2) > length) {
                return false;
            }
            if (isAdvPacketBeacon(bArr, i2)) {
                return true;
            }
            i2 = i;
        }
        return false;
    }

    private static boolean isAdvPacketBeacon(byte[] bArr, int i) {
        int i2 = i + 1;
        return isArrayContained(bArr, i2, IBEACON_PREFIX) || isArrayContained(bArr, i2, EDDYSTONE_PREFIX) || isArrayContained(bArr, i, GRAVITY_PREFIX) || isAltBeacon(bArr, i);
    }

    private static boolean isAltBeacon(byte[] bArr, int i) {
        int i2 = i + 5;
        if (i2 < bArr.length) {
            return bArr[i] == 27 && bArr[i + 1] == -1 && bArr[i + 4] == -66 && bArr[i2] == -84;
        }
        return false;
    }

    private static boolean isArrayContained(byte[] bArr, int i, byte[] bArr2) {
        int length = bArr2.length;
        if (i + length > bArr.length) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (bArr[i + i2] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private static byte[] fromHexString(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }
}
