package com.autonavi.amap.mapcore;

import android.graphics.Point;

/* JADX INFO: loaded from: classes.dex */
public class MapProjection {
    public static void lonlat2Geo(double d, double d2, IPoint iPoint) {
        Point pointLatLongToPixels = VirtualEarthProjection.LatLongToPixels(d2, d, 20);
        iPoint.x = pointLatLongToPixels.x;
        iPoint.y = pointLatLongToPixels.y;
    }

    public static void geo2LonLat(int i, int i2, DPoint dPoint) {
        DPoint dPointPixelsToLatLong = VirtualEarthProjection.PixelsToLatLong(i, i2, 20);
        dPoint.x = dPointPixelsToLatLong.x;
        dPoint.y = dPointPixelsToLatLong.y;
        dPointPixelsToLatLong.recycle();
    }
}
