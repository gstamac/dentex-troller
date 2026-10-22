package com.amap.api.maps;

import com.autonavi.amap.mapcore.CameraUpdateMessage;

/* JADX INFO: loaded from: classes.dex */
public final class CameraUpdate {
    CameraUpdateMessage a;

    CameraUpdate(CameraUpdateMessage cameraUpdateMessage) {
        this.a = cameraUpdateMessage;
    }

    public CameraUpdateMessage getCameraUpdateFactoryDelegate() {
        return this.a;
    }
}
