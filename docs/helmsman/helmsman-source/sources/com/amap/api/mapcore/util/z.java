package com.amap.api.mapcore.util;

import android.graphics.Point;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.amap.mapcore.CameraUpdateMessage;
import com.autonavi.amap.mapcore.VirtualEarthProjection;

/* JADX INFO: compiled from: CameraUpdateFactoryDelegate.java */
/* JADX INFO: loaded from: classes.dex */
public class z {
    public static CameraUpdateMessage a() {
        aa aaVar = new aa();
        aaVar.nowType = CameraUpdateMessage.Type.zoomBy;
        aaVar.amount = 1.0f;
        return aaVar;
    }

    public static CameraUpdateMessage b() {
        aa aaVar = new aa();
        aaVar.nowType = CameraUpdateMessage.Type.zoomBy;
        aaVar.amount = -1.0f;
        return aaVar;
    }

    public static CameraUpdateMessage a(float f, float f2) {
        y yVar = new y();
        yVar.nowType = CameraUpdateMessage.Type.scrollBy;
        yVar.xPixel = f;
        yVar.yPixel = f2;
        return yVar;
    }

    public static CameraUpdateMessage a(float f) {
        x xVar = new x();
        xVar.nowType = CameraUpdateMessage.Type.newCameraPosition;
        xVar.zoom = f;
        return xVar;
    }

    public static CameraUpdateMessage b(float f) {
        return a(f, (Point) null);
    }

    public static CameraUpdateMessage a(float f, Point point) {
        aa aaVar = new aa();
        aaVar.nowType = CameraUpdateMessage.Type.zoomBy;
        aaVar.amount = f;
        aaVar.focus = point;
        return aaVar;
    }

    public static CameraUpdateMessage a(CameraPosition cameraPosition) {
        x xVar = new x();
        xVar.nowType = CameraUpdateMessage.Type.newCameraPosition;
        if (cameraPosition != null && cameraPosition.target != null) {
            xVar.geoPoint = VirtualEarthProjection.LatLongToPixels(cameraPosition.target.latitude, cameraPosition.target.longitude, 20);
            xVar.zoom = cameraPosition.zoom;
            xVar.bearing = cameraPosition.bearing;
            xVar.tilt = cameraPosition.tilt;
            xVar.cameraPosition = cameraPosition;
        }
        return xVar;
    }

    public static CameraUpdateMessage a(Point point) {
        x xVar = new x();
        xVar.nowType = CameraUpdateMessage.Type.newCameraPosition;
        xVar.geoPoint = point;
        return xVar;
    }

    public static CameraUpdateMessage c(float f) {
        x xVar = new x();
        xVar.nowType = CameraUpdateMessage.Type.newCameraPosition;
        xVar.tilt = f;
        return xVar;
    }

    public static CameraUpdateMessage d(float f) {
        x xVar = new x();
        xVar.nowType = CameraUpdateMessage.Type.newCameraPosition;
        xVar.bearing = f;
        return xVar;
    }

    public static CameraUpdateMessage b(float f, Point point) {
        x xVar = new x();
        xVar.nowType = CameraUpdateMessage.Type.newCameraPosition;
        xVar.geoPoint = point;
        xVar.bearing = f;
        return xVar;
    }

    public static CameraUpdateMessage a(LatLng latLng) {
        return a(CameraPosition.builder().target(latLng).zoom(Float.NaN).bearing(Float.NaN).tilt(Float.NaN).build());
    }

    public static CameraUpdateMessage a(LatLng latLng, float f) {
        return a(CameraPosition.builder().target(latLng).zoom(f).bearing(Float.NaN).tilt(Float.NaN).build());
    }

    public static CameraUpdateMessage a(LatLngBounds latLngBounds, int i) {
        w wVar = new w();
        wVar.nowType = CameraUpdateMessage.Type.newLatLngBounds;
        wVar.bounds = latLngBounds;
        wVar.paddingLeft = i;
        wVar.paddingRight = i;
        wVar.paddingTop = i;
        wVar.paddingBottom = i;
        return wVar;
    }

    public static CameraUpdateMessage a(LatLngBounds latLngBounds, int i, int i2, int i3) {
        w wVar = new w();
        wVar.nowType = CameraUpdateMessage.Type.newLatLngBoundsWithSize;
        wVar.bounds = latLngBounds;
        wVar.paddingLeft = i3;
        wVar.paddingRight = i3;
        wVar.paddingTop = i3;
        wVar.paddingBottom = i3;
        wVar.width = i;
        wVar.height = i2;
        return wVar;
    }

    public static CameraUpdateMessage a(LatLngBounds latLngBounds, int i, int i2, int i3, int i4) {
        w wVar = new w();
        wVar.nowType = CameraUpdateMessage.Type.newLatLngBounds;
        wVar.bounds = latLngBounds;
        wVar.paddingLeft = i;
        wVar.paddingRight = i2;
        wVar.paddingTop = i3;
        wVar.paddingBottom = i4;
        return wVar;
    }

    public static CameraUpdateMessage c() {
        return new x();
    }
}
