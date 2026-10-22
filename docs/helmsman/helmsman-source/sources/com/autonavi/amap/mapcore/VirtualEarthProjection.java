package com.autonavi.amap.mapcore;

import android.graphics.Point;

/* JADX INFO: loaded from: classes.dex */
public class VirtualEarthProjection {
    public static final double EarthCircumferenceInMeters = 4.007501668557849E7d;
    public static final int EarthRadiusInMeters = 6378137;
    public static final int MAXZOOMLEVEL = 20;
    public static final double MaxLatitude = 85.0511287798d;
    public static final double MaxLongitude = 180.0d;
    public static final double MinLatitude = -85.0511287798d;
    public static final double MinLongitude = -180.0d;
    public static final int PixelsPerTile = 256;
    public static final int TileSplitLevel = 0;

    public static double Clip(double d, double d2, double d3) {
        return Math.min(Math.max(d, d2), d3);
    }

    public static Point LatLongToPixels(int i, int i2, int i3) {
        return LatLongToPixels(((double) i2) / 3600000.0d, ((double) i) / 3600000.0d, i3);
    }

    public static Point LatLongToPixels(double d, double d2, int i) {
        Point point = new Point();
        double dClip = (Clip(d, -85.0511287798d, 85.0511287798d) * 3.141592653589793d) / 180.0d;
        double dClip2 = (Clip(d2, -180.0d, 180.0d) * 3.141592653589793d) / 180.0d;
        double dSin = Math.sin(dClip);
        double dLog = Math.log((dSin + 1.0d) / (1.0d - dSin)) * 3189068.0d;
        long j = 256 << i;
        double d3 = 4.007501668557849E7d / j;
        double d4 = j - 1;
        point.x = (int) Clip((((dClip2 * 6378137.0d) + 2.0037508342789244E7d) / d3) + 0.5d, 0.0d, d4);
        point.y = (int) Clip((((long) (2.0037508342789244E7d - dLog)) / d3) + 0.5d, 0.0d, d4);
        return point;
    }

    public static DPoint PixelsToLatLong(long j, long j2, int i) {
        DPoint dPointObtain = DPoint.obtain();
        double d = 4.007501668557849E7d / ((double) ((1 << i) * 256));
        dPointObtain.y = 1.5707963267948966d - (Math.atan(Math.exp((-(2.0037508342789244E7d - (j2 * d))) / 6378137.0d)) * 2.0d);
        dPointObtain.y *= 57.29577951308232d;
        dPointObtain.x = ((j * d) - 2.0037508342789244E7d) / 6378137.0d;
        dPointObtain.x *= 57.29577951308232d;
        return dPointObtain;
    }
}
