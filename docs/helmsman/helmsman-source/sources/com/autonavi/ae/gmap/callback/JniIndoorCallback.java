package com.autonavi.ae.gmap.callback;

/* JADX INFO: loaded from: classes.dex */
public interface JniIndoorCallback {
    void onIndoorBuildingActivity(int i, byte[] bArr);

    void onIndoorDataRequired(int i, int i2, String[] strArr, int[] iArr, int[] iArr2);
}
