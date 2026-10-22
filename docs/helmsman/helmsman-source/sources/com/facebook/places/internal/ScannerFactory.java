package com.facebook.places.internal;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class ScannerFactory {
    public static final int OS_VERSION_JELLY_BEAN_MR1 = 17;
    public static final int OS_VERSION_JELLY_BEAN_MR2 = 18;
    public static final int OS_VERSION_LOLLIPOP = 21;

    public static BleScanner newBleScanner(Context context, LocationPackageRequestParams locationPackageRequestParams) {
        return new BleScannerImpl(context, locationPackageRequestParams);
    }

    public static WifiScanner newWifiScanner(Context context, LocationPackageRequestParams locationPackageRequestParams) {
        return new WifiScannerImpl(context, locationPackageRequestParams);
    }

    public static LocationScanner newLocationScanner(Context context, LocationPackageRequestParams locationPackageRequestParams) {
        return new LocationScannerImpl(context, locationPackageRequestParams);
    }
}
