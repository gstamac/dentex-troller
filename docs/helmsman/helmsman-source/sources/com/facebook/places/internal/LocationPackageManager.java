package com.facebook.places.internal;

import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.internal.Utility;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes.dex */
public class LocationPackageManager {
    private static final String TAG = "LocationPackageManager";

    public interface Listener {
        void onLocationPackage(LocationPackage locationPackage);
    }

    public static void requestLocationPackage(final LocationPackageRequestParams locationPackageRequestParams, final Listener listener) {
        FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.places.internal.LocationPackageManager.1
            @Override // java.lang.Runnable
            public void run() {
                FutureTask futureTaskNewLocationScanFuture;
                FutureTask futureTaskNewWifiScanFuture;
                LocationPackage locationPackage = new LocationPackage();
                try {
                    try {
                        FutureTask futureTaskNewBluetoothScanFuture = null;
                        if (locationPackageRequestParams.isLocationScanEnabled()) {
                            LocationScanner locationScannerNewLocationScanner = ScannerFactory.newLocationScanner(FacebookSdk.getApplicationContext(), locationPackageRequestParams);
                            locationScannerNewLocationScanner.initAndCheckEligibility();
                            futureTaskNewLocationScanFuture = LocationPackageManager.newLocationScanFuture(locationScannerNewLocationScanner, locationPackageRequestParams);
                            FacebookSdk.getExecutor().execute(futureTaskNewLocationScanFuture);
                        } else {
                            futureTaskNewLocationScanFuture = null;
                        }
                        if (locationPackageRequestParams.isWifiScanEnabled()) {
                            futureTaskNewWifiScanFuture = LocationPackageManager.newWifiScanFuture(locationPackageRequestParams);
                            FacebookSdk.getExecutor().execute(futureTaskNewWifiScanFuture);
                        } else {
                            futureTaskNewWifiScanFuture = null;
                        }
                        if (locationPackageRequestParams.isBluetoothScanEnabled()) {
                            futureTaskNewBluetoothScanFuture = LocationPackageManager.newBluetoothScanFuture(locationPackageRequestParams);
                            FacebookSdk.getExecutor().execute(futureTaskNewBluetoothScanFuture);
                        }
                        if (futureTaskNewBluetoothScanFuture != null) {
                            try {
                                LocationPackage locationPackage2 = (LocationPackage) futureTaskNewBluetoothScanFuture.get();
                                locationPackage.ambientBluetoothLe = locationPackage2.ambientBluetoothLe;
                                locationPackage.isBluetoothScanningEnabled = locationPackage2.isBluetoothScanningEnabled;
                            } catch (Exception e) {
                                LocationPackageManager.logException("Exception scanning for bluetooth beacons", e);
                            }
                        }
                        if (futureTaskNewWifiScanFuture != null) {
                            try {
                                LocationPackage locationPackage3 = (LocationPackage) futureTaskNewWifiScanFuture.get();
                                locationPackage.isWifiScanningEnabled = locationPackage3.isWifiScanningEnabled;
                                locationPackage.connectedWifi = locationPackage3.connectedWifi;
                                locationPackage.ambientWifi = locationPackage3.ambientWifi;
                            } catch (Exception e2) {
                                LocationPackageManager.logException("Exception scanning for wifi access points", e2);
                            }
                        }
                        if (futureTaskNewLocationScanFuture != null) {
                            try {
                                LocationPackage locationPackage4 = (LocationPackage) futureTaskNewLocationScanFuture.get();
                                locationPackage.locationError = locationPackage4.locationError;
                                locationPackage.location = locationPackage4.location;
                            } catch (Exception e3) {
                                LocationPackageManager.logException("Exception getting location", e3);
                            }
                        }
                    } catch (Exception e4) {
                        LocationPackageManager.logException("Exception requesting a location package", e4);
                    }
                } catch (ScannerException e5) {
                    LocationPackageManager.logException("Exception scanning for locations", e5);
                    locationPackage.locationError = e5.type;
                }
                listener.onLocationPackage(locationPackage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FutureTask<LocationPackage> newLocationScanFuture(final LocationScanner locationScanner, LocationPackageRequestParams locationPackageRequestParams) {
        return new FutureTask<>(new Callable<LocationPackage>() { // from class: com.facebook.places.internal.LocationPackageManager.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public LocationPackage call() throws Exception {
                LocationPackage locationPackage = new LocationPackage();
                try {
                    locationPackage.location = locationScanner.getLocation();
                } catch (ScannerException e) {
                    locationPackage.locationError = e.type;
                    LocationPackageManager.logException("Exception while getting location", e);
                } catch (Exception unused) {
                    locationPackage.locationError = ScannerException.Type.UNKNOWN_ERROR;
                }
                return locationPackage;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FutureTask<LocationPackage> newBluetoothScanFuture(final LocationPackageRequestParams locationPackageRequestParams) {
        return new FutureTask<>(new Callable<LocationPackage>() { // from class: com.facebook.places.internal.LocationPackageManager.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public LocationPackage call() throws Exception {
                LocationPackage locationPackage = new LocationPackage();
                try {
                    BleScanner bleScannerNewBleScanner = ScannerFactory.newBleScanner(FacebookSdk.getApplicationContext(), locationPackageRequestParams);
                    bleScannerNewBleScanner.initAndCheckEligibility();
                    try {
                        bleScannerNewBleScanner.startScanning();
                        try {
                            Thread.sleep(locationPackageRequestParams.getBluetoothScanDurationMs());
                        } catch (Exception unused) {
                        }
                        bleScannerNewBleScanner.stopScanning();
                        int errorCode = bleScannerNewBleScanner.getErrorCode();
                        if (errorCode == 0) {
                            locationPackage.ambientBluetoothLe = bleScannerNewBleScanner.getScanResults();
                            locationPackage.isBluetoothScanningEnabled = true;
                        } else {
                            if (FacebookSdk.isDebugEnabled()) {
                                Utility.logd(LocationPackageManager.TAG, String.format(Locale.getDefault(), "Bluetooth LE scan failed with error: %d", Integer.valueOf(errorCode)));
                            }
                            locationPackage.isBluetoothScanningEnabled = false;
                        }
                    } catch (Throwable th) {
                        bleScannerNewBleScanner.stopScanning();
                        throw th;
                    }
                } catch (Exception e) {
                    LocationPackageManager.logException("Exception scanning for bluetooth beacons", e);
                    locationPackage.isBluetoothScanningEnabled = false;
                }
                return locationPackage;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FutureTask<LocationPackage> newWifiScanFuture(final LocationPackageRequestParams locationPackageRequestParams) {
        return new FutureTask<>(new Callable<LocationPackage>() { // from class: com.facebook.places.internal.LocationPackageManager.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public LocationPackage call() throws Exception {
                LocationPackage locationPackage = new LocationPackage();
                try {
                    WifiScanner wifiScannerNewWifiScanner = ScannerFactory.newWifiScanner(FacebookSdk.getApplicationContext(), locationPackageRequestParams);
                    wifiScannerNewWifiScanner.initAndCheckEligibility();
                    locationPackage.connectedWifi = wifiScannerNewWifiScanner.getConnectedWifi();
                    locationPackage.isWifiScanningEnabled = wifiScannerNewWifiScanner.isWifiScanningEnabled();
                    if (locationPackage.isWifiScanningEnabled) {
                        locationPackage.ambientWifi = wifiScannerNewWifiScanner.getWifiScans();
                    }
                } catch (Exception e) {
                    LocationPackageManager.logException("Exception scanning for wifi access points", e);
                    locationPackage.isWifiScanningEnabled = false;
                }
                return locationPackage;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logException(String str, Throwable th) {
        if (FacebookSdk.isDebugEnabled()) {
            Log.e(TAG, str, th);
        }
    }
}
