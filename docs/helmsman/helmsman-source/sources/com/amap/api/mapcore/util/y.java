package com.amap.api.mapcore.util;

import android.graphics.Point;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.CameraUpdateMessage;

/* JADX INFO: compiled from: CameraScrollMessage.java */
/* JADX INFO: loaded from: classes.dex */
public class y extends CameraUpdateMessage {
    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void mergeCameraUpdateDelegate(CameraUpdateMessage cameraUpdateMessage) {
    }

    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        float f = this.xPixel;
        float f2 = this.yPixel;
        float f3 = (this.width / 2.0f) + f;
        float f4 = (this.height / 2.0f) + f2;
        Point point = new Point();
        a(gLMapState, (int) f3, (int) f4, point);
        gLMapState.setMapGeoCenter(point.x, point.y);
    }

    public void a(GLMapState gLMapState, int i, int i2, Point point) {
        gLMapState.screenToP20Point(i, i2, point);
    }
}
