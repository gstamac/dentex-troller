package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.maps.model.LatLng;
import com.autonavi.amap.mapcore.CoordUtil;
import com.autonavi.amap.mapcore.DPoint;
import java.io.File;
import java.math.BigDecimal;

/* JADX INFO: compiled from: OffsetUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class ab {
    static double a = 3.141592653589793d;
    private static boolean b = false;

    public static LatLng a(Context context, LatLng latLng) {
        if (context == null) {
            return null;
        }
        String strA = fe.a(context, "libwgs2gcj.so");
        if (!TextUtils.isEmpty(strA) && new File(strA).exists() && !b) {
            try {
                System.load(strA);
                b = true;
            } catch (Throwable unused) {
            }
        }
        DPoint dPointA = a(DPoint.obtain(latLng.longitude, latLng.latitude), b);
        LatLng latLng2 = new LatLng(dPointA.y, dPointA.x, false);
        dPointA.recycle();
        return latLng2;
    }

    private static DPoint a(DPoint dPoint, boolean z) {
        try {
            if (!dr.a(dPoint.y, dPoint.x)) {
                return dPoint;
            }
            double[] dArrA = new double[2];
            if (z) {
                try {
                    if (CoordUtil.convertToGcj(new double[]{dPoint.x, dPoint.y}, dArrA) != 0) {
                        dArrA = hn.a(dPoint.x, dPoint.y);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                    dArrA = hn.a(dPoint.x, dPoint.y);
                }
            } else {
                dArrA = hn.a(dPoint.x, dPoint.y);
            }
            dPoint.recycle();
            return DPoint.obtain(dArrA[0], dArrA[1]);
        } catch (Throwable unused) {
            return dPoint;
        }
    }

    public static LatLng b(Context context, LatLng latLng) {
        try {
            DPoint dPointC = c(latLng.longitude, latLng.latitude);
            LatLng latLngA = a(context, new LatLng(dPointC.y, dPointC.x, false));
            dPointC.recycle();
            return latLngA;
        } catch (Throwable th) {
            th.printStackTrace();
            return latLng;
        }
    }

    public static double a(double d, double d2) {
        return (Math.cos(d2 / 100000.0d) * (d / 18000.0d)) + (Math.sin(d / 100000.0d) * (d2 / 9000.0d));
    }

    public static double b(double d, double d2) {
        return (Math.sin(d2 / 100000.0d) * (d / 18000.0d)) + (Math.cos(d / 100000.0d) * (d2 / 9000.0d));
    }

    private static DPoint c(double d, double d2) {
        double d3 = ((long) (d * 100000.0d)) % 36000000;
        double d4 = ((long) (d2 * 100000.0d)) % 36000000;
        double d5 = (int) ((-a(d3, d4)) + d3);
        double d6 = (int) ((-b(d3, d4)) + d4);
        double d7 = (int) ((-a(d5, d6)) + d3 + ((double) (d3 > 0.0d ? 1 : -1)));
        return DPoint.obtain(d7 / 100000.0d, ((double) ((int) (((-b(d7, d6)) + d4) + ((double) (d4 <= 0.0d ? -1 : 1))))) / 100000.0d);
    }

    public static LatLng a(LatLng latLng) {
        if (latLng != null) {
            try {
                DPoint dPointA = a(latLng.longitude, latLng.latitude, 2);
                LatLng latLng2 = new LatLng(dPointA.y, dPointA.x, false);
                dPointA.recycle();
                return latLng2;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return latLng;
    }

    private static double a(double d) {
        return Math.sin(d * 3000.0d * (a / 180.0d)) * 2.0E-5d;
    }

    private static double b(double d) {
        return Math.cos(d * 3000.0d * (a / 180.0d)) * 3.0E-6d;
    }

    private static DPoint d(double d, double d2) {
        DPoint dPointObtain = DPoint.obtain();
        double d3 = (d * d) + (d2 * d2);
        double dCos = (Math.cos(b(d) + Math.atan2(d2, d)) * (a(d2) + Math.sqrt(d3))) + 0.0065d;
        double dSin = (Math.sin(b(d) + Math.atan2(d2, d)) * (a(d2) + Math.sqrt(d3))) + 0.006d;
        dPointObtain.x = a(dCos, 8);
        dPointObtain.y = a(dSin, 8);
        return dPointObtain;
    }

    private static double a(double d, int i) {
        return new BigDecimal(d).setScale(i, 4).doubleValue();
    }

    private static DPoint a(double d, double d2, int i) {
        DPoint dPointA = null;
        double d3 = 0.006401062d;
        double d4 = 0.0060424805d;
        for (int i2 = 0; i2 < i; i2++) {
            dPointA = a(d, d2, d3, d4);
            d3 = d - dPointA.x;
            d4 = d2 - dPointA.y;
        }
        return dPointA;
    }

    private static DPoint a(double d, double d2, double d3, double d4) {
        DPoint dPointObtain = DPoint.obtain();
        double d5 = d - d3;
        double d6 = d2 - d4;
        DPoint dPointD = d(d5, d6);
        dPointObtain.x = a((d + d5) - dPointD.x, 8);
        dPointObtain.y = a((d2 + d6) - dPointD.y, 8);
        return dPointObtain;
    }
}
