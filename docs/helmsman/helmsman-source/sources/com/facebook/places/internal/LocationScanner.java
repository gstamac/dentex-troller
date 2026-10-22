package com.facebook.places.internal;

import android.location.Location;

/* JADX INFO: loaded from: classes.dex */
public interface LocationScanner {
    Location getLocation() throws ScannerException;

    void initAndCheckEligibility() throws ScannerException;
}
