package com.amap.api.mapcore.util;

import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.CameraUpdateMessage;

/* JADX INFO: compiled from: CameraPositionMessage.java */
/* JADX INFO: loaded from: classes.dex */
public class x extends CameraUpdateMessage {
    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void runCameraUpdate(GLMapState gLMapState) {
        normalChange(gLMapState);
    }

    @Override // com.autonavi.amap.mapcore.CameraUpdateMessage
    public void mergeCameraUpdateDelegate(CameraUpdateMessage cameraUpdateMessage) {
        cameraUpdateMessage.geoPoint = this.geoPoint == null ? cameraUpdateMessage.geoPoint : this.geoPoint;
        cameraUpdateMessage.zoom = Float.isNaN(this.zoom) ? cameraUpdateMessage.zoom : this.zoom;
        cameraUpdateMessage.bearing = Float.isNaN(this.bearing) ? cameraUpdateMessage.bearing : this.bearing;
        cameraUpdateMessage.tilt = Float.isNaN(this.tilt) ? cameraUpdateMessage.tilt : this.tilt;
    }
}
