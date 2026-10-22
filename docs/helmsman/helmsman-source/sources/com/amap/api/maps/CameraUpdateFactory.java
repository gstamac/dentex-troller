package com.amap.api.maps;

import android.graphics.Point;
import android.util.Log;
import com.amap.api.mapcore.util.z;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.VirtualEarthProjection;

/* JADX INFO: loaded from: classes.dex */
public final class CameraUpdateFactory {
    public static CameraUpdate zoomIn() {
        return new CameraUpdate(z.a());
    }

    public static CameraUpdate zoomOut() {
        return new CameraUpdate(z.b());
    }

    public static CameraUpdate scrollBy(float f, float f2) {
        return new CameraUpdate(z.a(f, f2));
    }

    public static CameraUpdate zoomTo(float f) {
        return new CameraUpdate(z.a(f));
    }

    public static CameraUpdate zoomBy(float f) {
        return new CameraUpdate(z.b(f));
    }

    public static CameraUpdate zoomBy(float f, Point point) {
        return new CameraUpdate(z.a(f, point));
    }

    public static CameraUpdate newCameraPosition(CameraPosition cameraPosition) {
        if (cameraPosition == null) {
            Log.w("CameraUpdateFactory", "cameraPosition is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.a(cameraPosition));
    }

    public static CameraUpdate newLatLng(LatLng latLng) {
        if (latLng == null) {
            Log.w("CameraUpdateFactory", "latLng is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.a(latLng));
    }

    public static CameraUpdate newLatLngZoom(LatLng latLng, float f) {
        if (latLng == null) {
            Log.w("CameraUpdateFactory", "target is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.a(latLng, f));
    }

    public static CameraUpdate newLatLngBounds(LatLngBounds latLngBounds, int i) {
        if (latLngBounds == null) {
            Log.w("CameraUpdateFactory", "bounds is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.a(latLngBounds, i));
    }

    public static CameraUpdate changeLatLng(LatLng latLng) {
        if (latLng == null) {
            Log.w("CameraUpdateFactory", "target is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.a(VirtualEarthProjection.LatLongToPixels(latLng.latitude, latLng.longitude, 20)));
    }

    public static CameraUpdate changeBearing(float f) {
        return new CameraUpdate(z.d(f % 360.0f));
    }

    public static CameraUpdate changeBearingGeoCenter(float f, IPoint iPoint) {
        if (iPoint == null) {
            Log.w("CameraUpdateFactory", "geoPoint is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.b(f % 360.0f, new Point(iPoint.x, iPoint.y)));
    }

    public static CameraUpdate changeTilt(float f) {
        return new CameraUpdate(z.c(f));
    }

    public static CameraUpdate newLatLngBounds(LatLngBounds latLngBounds, int i, int i2, int i3) {
        if (latLngBounds == null) {
            Log.w("CameraUpdateFactory", "bounds is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.a(latLngBounds, i, i2, i3));
    }

    public static CameraUpdate newLatLngBoundsRect(LatLngBounds latLngBounds, int i, int i2, int i3, int i4) {
        if (latLngBounds == null) {
            Log.w("CameraUpdateFactory", "bounds is null");
            return new CameraUpdate(z.c());
        }
        return new CameraUpdate(z.a(latLngBounds, i, i2, i3, i4));
    }
}
