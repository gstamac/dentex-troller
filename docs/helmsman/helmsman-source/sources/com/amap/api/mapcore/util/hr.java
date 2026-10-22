package com.amap.api.mapcore.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.facebook.places.model.PlaceFields;
import com.zhy.http.okhttp.OkHttpUtils;

/* JADX INFO: compiled from: MapGPSLocation.java */
/* JADX INFO: loaded from: classes.dex */
public final class hr {
    Context a;
    LocationManager b;
    Object g;
    volatile long c = 0;
    volatile boolean d = false;
    boolean e = false;
    volatile Inner_3dMap_location f = null;
    boolean h = false;
    LocationListener i = new LocationListener() { // from class: com.amap.api.mapcore.util.hr.1
        @Override // android.location.LocationListener
        public final void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            try {
                Inner_3dMap_location inner_3dMap_location = new Inner_3dMap_location(location);
                inner_3dMap_location.setLocationType(1);
                Bundle extras = location.getExtras();
                inner_3dMap_location.setSatellites(extras != null ? extras.getInt("satellites") : 0);
                hr.this.f = inner_3dMap_location;
                hr.this.c = im.b();
                hr.this.d = true;
            } catch (Throwable th) {
                ij.a(th, "MAPGPSLocation", "onLocationChanged");
            }
        }

        @Override // android.location.LocationListener
        public final void onProviderDisabled(String str) {
            try {
                if ("gps".equals(str)) {
                    hr.this.d = false;
                }
            } catch (Throwable th) {
                ij.a(th, "MAPGPSLocation", "onProviderDisabled");
            }
        }

        @Override // android.location.LocationListener
        public final void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public final void onStatusChanged(String str, int i, Bundle bundle) {
        }
    };

    public hr(Context context) {
        this.g = null;
        if (context == null) {
            return;
        }
        this.a = context;
        e();
        try {
            if (this.g == null) {
                this.g = this.h ? Class.forName("com.amap.api.maps.CoordinateConverter").getConstructor(Context.class).newInstance(context) : Class.forName("com.amap.api.maps2d.CoordinateConverter").getConstructor(new Class[0]).newInstance(new Object[0]);
            }
        } catch (Throwable unused) {
        }
        if (this.b == null) {
            this.b = (LocationManager) this.a.getSystemService(PlaceFields.LOCATION);
        }
    }

    private void e() {
        try {
            if (Class.forName("com.amap.api.maps.CoordinateConverter") != null) {
                this.h = true;
            }
        } catch (Throwable unused) {
        }
    }

    private void f() {
        try {
            Looper looperMyLooper = Looper.myLooper();
            if (looperMyLooper == null) {
                looperMyLooper = this.a.getMainLooper();
            }
            Looper looper = looperMyLooper;
            try {
                this.b.sendExtraCommand("gps", "force_xtra_injection", new Bundle());
            } catch (Throwable unused) {
            }
            this.b.requestLocationUpdates("gps", 800L, 0.0f, this.i, looper);
        } catch (SecurityException unused2) {
        } catch (Throwable th) {
            ij.a(th, "MAPGPSLocation", "requestLocationUpdates");
        }
    }

    private void g() {
        this.d = false;
        this.c = 0L;
        this.f = null;
    }

    public final void a() {
        if (this.e) {
            return;
        }
        f();
        this.e = true;
    }

    public final void b() {
        LocationListener locationListener;
        this.e = false;
        g();
        LocationManager locationManager = this.b;
        if (locationManager == null || (locationListener = this.i) == null) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    public final boolean c() {
        if (!this.d) {
            return false;
        }
        if (im.b() - this.c <= OkHttpUtils.DEFAULT_MILLISECONDS) {
            return true;
        }
        this.f = null;
        return false;
    }

    public final Inner_3dMap_location d() {
        Object objA;
        Object objNewInstance;
        if (this.f == null) {
            return null;
        }
        Inner_3dMap_location inner_3dMap_locationM44clone = this.f.m44clone();
        if (inner_3dMap_locationM44clone != null && inner_3dMap_locationM44clone.getErrorCode() == 0) {
            try {
                if (this.g != null && ij.a(inner_3dMap_locationM44clone.getLatitude(), inner_3dMap_locationM44clone.getLongitude())) {
                    Long.valueOf(System.currentTimeMillis());
                    Object[] objArr = {"GPS"};
                    Class[] clsArr = {String.class};
                    if (this.h) {
                        objA = ik.a("com.amap.api.maps.CoordinateConverter$CoordType", "valueOf", objArr, (Class<?>[]) clsArr);
                        objNewInstance = Class.forName("com.amap.api.maps.model.LatLng").getConstructor(Double.TYPE, Double.TYPE).newInstance(Double.valueOf(inner_3dMap_locationM44clone.getLatitude()), Double.valueOf(inner_3dMap_locationM44clone.getLongitude()));
                    } else {
                        objA = ik.a("com.amap.api.maps2d.CoordinateConverter$CoordType", "valueOf", objArr, (Class<?>[]) clsArr);
                        objNewInstance = Class.forName("com.amap.api.maps2d.model.LatLng").getConstructor(Double.TYPE, Double.TYPE).newInstance(Double.valueOf(inner_3dMap_locationM44clone.getLatitude()), Double.valueOf(inner_3dMap_locationM44clone.getLongitude()));
                    }
                    ik.a(this.g, "coord", objNewInstance);
                    ik.a(this.g, TypedValues.TransitionType.S_FROM, objA);
                    Object objA2 = ik.a(this.g, "convert", new Object[0]);
                    double dDoubleValue = ((Double) objA2.getClass().getDeclaredField("latitude").get(objA2)).doubleValue();
                    double dDoubleValue2 = ((Double) objA2.getClass().getDeclaredField("longitude").get(objA2)).doubleValue();
                    inner_3dMap_locationM44clone.setLatitude(dDoubleValue);
                    inner_3dMap_locationM44clone.setLongitude(dDoubleValue2);
                }
            } catch (Throwable unused) {
            }
        }
        return inner_3dMap_locationM44clone;
    }
}
