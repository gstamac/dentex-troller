package com.amap.api.mapcore.util;

import com.amap.api.maps.model.LatLng;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.interfaces.IMarker;

/* JADX INFO: compiled from: IMarkerDelegate.java */
/* JADX INFO: loaded from: classes.dex */
public interface cf extends ci, IMarker {
    FPoint a();

    void a(boolean z);

    LatLng b();

    int c();

    int d();

    int e();

    int f();

    boolean g();

    boolean isInfoWindowEnable();
}
