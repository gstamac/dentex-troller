package com.amap.api.mapcore.util;

import android.util.Pair;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.CameraUpdateMessage;
import com.autonavi.amap.mapcore.IPoint;

/* JADX INFO: compiled from: CameraBoundsMessage.java */
/* JADX INFO: loaded from: classes.dex */
public class w extends CameraUpdateMessage {
    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void mergeCameraUpdateDelegate(CameraUpdateMessage cameraUpdateMessage) {
    }

    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        Pair<Float, IPoint> pairA = dw.a(this, gLMapState, this.mapConfig);
        gLMapState.setMapZoomer(((Float) pairA.first).floatValue());
        gLMapState.setMapGeoCenter(((IPoint) pairA.second).x, ((IPoint) pairA.second).y);
        gLMapState.setCameraDegree(0.0f);
        gLMapState.setMapAngle(0.0f);
    }
}
