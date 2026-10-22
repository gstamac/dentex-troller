package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.location.Location;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.ae.gmap.GLMapEngine;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.gesture.EAMapPlatformGestureInfo;
import com.autonavi.ae.gmap.listener.MapWidgetListener;
import com.autonavi.amap.mapcore.CameraUpdateMessage;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.interfaces.IAMap;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.message.GestureMapMessage;

/* JADX INFO: compiled from: IAMapDelegate.java */
/* JADX INFO: loaded from: classes.dex */
public interface k extends IAMap {
    float a(int i);

    int a(EAMapPlatformGestureInfo eAMapPlatformGestureInfo);

    int a(IMarkerAction iMarkerAction, Rect rect);

    r a(BitmapDescriptor bitmapDescriptor);

    LatLngBounds a(LatLng latLng, float f, float f2, float f3);

    GLMapEngine a();

    void a(double d, double d2, FPoint fPoint);

    void a(double d, double d2, IPoint iPoint);

    void a(float f, float f2, IPoint iPoint);

    void a(int i, float f);

    void a(int i, int i2);

    void a(int i, int i2, DPoint dPoint);

    void a(int i, int i2, FPoint fPoint);

    void a(int i, int i2, IPoint iPoint);

    void a(int i, MotionEvent motionEvent);

    void a(int i, GestureMapMessage gestureMapMessage);

    void a(Location location) throws RemoteException;

    void a(cf cfVar) throws RemoteException;

    void a(r rVar);

    void a(MapWidgetListener mapWidgetListener);

    void a(CameraUpdateMessage cameraUpdateMessage) throws RemoteException;

    void a(boolean z);

    boolean a(String str) throws RemoteException;

    GLMapState b();

    void b(double d, double d2, IPoint iPoint);

    void b(int i, int i2);

    void b(int i, int i2, PointF pointF);

    void b(int i, int i2, DPoint dPoint);

    void b(CameraUpdateMessage cameraUpdateMessage) throws RemoteException;

    void b(String str);

    void b(boolean z);

    boolean b(int i, MotionEvent motionEvent);

    int c();

    String c(String str);

    void c(int i);

    void c(boolean z);

    boolean c(int i, MotionEvent motionEvent);

    int d();

    void d(boolean z);

    boolean d(int i);

    int e();

    void e(boolean z);

    boolean e(int i);

    void f();

    void f(int i);

    float g();

    float g(int i);

    n h();

    void h(int i);

    void h(boolean z);

    void i();

    void i(int i);

    void j();

    void j(int i);

    float k(int i);

    Point k();

    View l();

    float m(int i);

    boolean m();

    int n();

    Point n(int i);

    float o(int i);

    float s();

    cv s(int i);

    Context u();

    float[] v();

    cw w();
}
