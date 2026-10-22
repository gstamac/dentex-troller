package com.facebook.places.internal;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface WifiScanner {
    WifiScanResult getConnectedWifi() throws ScannerException;

    List<WifiScanResult> getWifiScans() throws ScannerException;

    void initAndCheckEligibility() throws ScannerException;

    boolean isWifiScanningEnabled();
}
