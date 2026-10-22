package com.amap.api.mapcore.util;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.CameraUpdateMessage;

/* JADX INFO: compiled from: CameraZoomMessage.java */
/* JADX INFO: loaded from: classes.dex */
public class aa extends CameraUpdateMessage {
    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void mergeCameraUpdateDelegate(CameraUpdateMessage cameraUpdateMessage) {
    }

    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        this.zoom = gLMapState.getMapZoomer() + this.amount;
        this.zoom = dw.a(this.mapConfig, this.zoom);
        normalChange(gLMapState);
    }
}
