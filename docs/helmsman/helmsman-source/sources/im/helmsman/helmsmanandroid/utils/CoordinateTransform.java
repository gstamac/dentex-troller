package im.helmsman.helmsmanandroid.utils;

import im.helmsman.helmsmanandroid.config.Config;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import im.helmsman.helmsmanandroid.dao.WayPoint;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CoordinateTransform {
    private static final double PI = 3.141592653589793d;
    private static final double a = 6378245.0d;
    private static final double ee = 0.006693421622965943d;
    private static final double x_PI = 52.35987755982988d;

    public static double[] transformBD09ToGCJ02(double d, double d2) {
        double d3 = d - 0.0065d;
        double d4 = d2 - 0.006d;
        double dSqrt = Math.sqrt((d3 * d3) + (d4 * d4)) - (Math.sin(d4 * x_PI) * 2.0E-5d);
        double dAtan2 = Math.atan2(d4, d3) - (Math.cos(d3 * x_PI) * 3.0E-6d);
        return new double[]{Math.cos(dAtan2) * dSqrt, dSqrt * Math.sin(dAtan2)};
    }

    public static double[] transformGCJ02ToBD09(double d, double d2) {
        double dSqrt = Math.sqrt((d * d) + (d2 * d2)) + (Math.sin(d2 * x_PI) * 2.0E-5d);
        double dAtan2 = Math.atan2(d2, d) + (Math.cos(d * x_PI) * 3.0E-6d);
        return new double[]{(Math.cos(dAtan2) * dSqrt) + 0.0065d, (dSqrt * Math.sin(dAtan2)) + 0.006d};
    }

    public static double[] transformGCJ02ToWGS84(double d, double d2) {
        if (!Config.isChineseCoordinateCorrection) {
            return new double[]{d, d2};
        }
        double d3 = d2 - 105.0d;
        double d4 = d - 35.0d;
        double dTransformLat = transformLat(d3, d4);
        double dTransformLng = transformLng(d3, d4);
        double d5 = (d / 180.0d) * 3.141592653589793d;
        double dSin = Math.sin(d5);
        double d6 = 1.0d - ((ee * dSin) * dSin);
        double dSqrt = Math.sqrt(d6);
        return new double[]{(d * 2.0d) - (d + ((dTransformLat * 180.0d) / ((6335552.717000426d / (d6 * dSqrt)) * 3.141592653589793d))), (d2 * 2.0d) - (d2 + ((dTransformLng * 180.0d) / (((a / dSqrt) * Math.cos(d5)) * 3.141592653589793d)))};
    }

    public static double[] transformWGS84ToGCJ02(double d, double d2) {
        if (!Config.isChineseCoordinateCorrection) {
            return new double[]{d, d2};
        }
        double d3 = d2 - 105.0d;
        double d4 = d - 35.0d;
        double dTransformLat = transformLat(d3, d4);
        double dTransformLng = transformLng(d3, d4);
        double d5 = (d / 180.0d) * 3.141592653589793d;
        double dSin = Math.sin(d5);
        double d6 = 1.0d - ((ee * dSin) * dSin);
        double dSqrt = Math.sqrt(d6);
        return new double[]{d + ((dTransformLat * 180.0d) / ((6335552.717000426d / (d6 * dSqrt)) * 3.141592653589793d)), d2 + ((dTransformLng * 180.0d) / (((a / dSqrt) * Math.cos(d5)) * 3.141592653589793d))};
    }

    public static double[] transformBD09ToWGS84(double d, double d2) {
        double[] dArrTransformBD09ToGCJ02 = transformBD09ToGCJ02(d, d2);
        return transformGCJ02ToWGS84(dArrTransformBD09ToGCJ02[0], dArrTransformBD09ToGCJ02[1]);
    }

    public static double[] transformWGS84ToBD09(double d, double d2) {
        double[] dArrTransformWGS84ToGCJ02 = transformWGS84ToGCJ02(d, d2);
        return transformGCJ02ToBD09(dArrTransformWGS84ToGCJ02[0], dArrTransformWGS84ToGCJ02[1]);
    }

    private static double transformLat(double d, double d2) {
        double d3 = d * 2.0d;
        double dSqrt = (-100.0d) + d3 + (d2 * 3.0d) + (d2 * 0.2d * d2) + (0.1d * d * d2) + (Math.sqrt(Math.abs(d)) * 0.2d) + ((((Math.sin((6.0d * d) * 3.141592653589793d) * 20.0d) + (Math.sin(d3 * 3.141592653589793d) * 20.0d)) * 2.0d) / 3.0d);
        double d4 = d2 * 3.141592653589793d;
        return dSqrt + ((((Math.sin(d4) * 20.0d) + (Math.sin((d2 / 3.0d) * 3.141592653589793d) * 40.0d)) * 2.0d) / 3.0d) + ((((Math.sin((d2 / 12.0d) * 3.141592653589793d) * 160.0d) + (Math.sin(d4 / 30.0d) * 320.0d)) * 2.0d) / 3.0d);
    }

    private static double transformLng(double d, double d2) {
        double d3 = d * 0.1d;
        return d + 300.0d + (d2 * 2.0d) + (d3 * d) + (d3 * d2) + (Math.sqrt(Math.abs(d)) * 0.1d) + ((((Math.sin((6.0d * d) * 3.141592653589793d) * 20.0d) + (Math.sin((d * 2.0d) * 3.141592653589793d) * 20.0d)) * 2.0d) / 3.0d) + ((((Math.sin(d * 3.141592653589793d) * 20.0d) + (Math.sin((d / 3.0d) * 3.141592653589793d) * 40.0d)) * 2.0d) / 3.0d) + ((((Math.sin((d / 12.0d) * 3.141592653589793d) * 150.0d) + (Math.sin((d / 30.0d) * 3.141592653589793d) * 300.0d)) * 2.0d) / 3.0d);
    }

    public static List<MLatLng> latLngWGS84ToGCJ02(List<MLatLng> list) {
        int size = list.size();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            MLatLng mLatLng = list.get(i);
            double[] dArrTransformWGS84ToGCJ02 = transformWGS84ToGCJ02(mLatLng.latitude, mLatLng.longitude);
            arrayList.add(new MLatLng((float) dArrTransformWGS84ToGCJ02[0], (float) dArrTransformWGS84ToGCJ02[1]));
        }
        return arrayList;
    }

    public static List<MLatLng> latLngGCJ024ToWGS84(List<MLatLng> list) {
        int size = list.size();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            MLatLng mLatLng = list.get(i);
            double[] dArrTransformGCJ02ToWGS84 = transformGCJ02ToWGS84(mLatLng.latitude, mLatLng.longitude);
            arrayList.add(new MLatLng((float) dArrTransformGCJ02ToWGS84[0], (float) dArrTransformGCJ02ToWGS84[1]));
        }
        return arrayList;
    }

    public static List<WayPoint> wayPointWGS84ToGCJ02(List<WayPoint> list) {
        int size = list.size();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            WayPoint wayPoint = list.get(i);
            double[] dArrTransformWGS84ToGCJ02 = transformWGS84ToGCJ02(wayPoint.getLatitude(), wayPoint.getLongitude());
            arrayList.add(new WayPoint(dArrTransformWGS84ToGCJ02[0], dArrTransformWGS84ToGCJ02[1]));
        }
        return arrayList;
    }

    public static MLatLng gcj02ToWgs84(MLatLng mLatLng) {
        double[] dArrTransformGCJ02ToWGS84 = transformGCJ02ToWGS84(mLatLng.latitude, mLatLng.longitude);
        return new MLatLng((float) dArrTransformGCJ02ToWGS84[0], (float) dArrTransformGCJ02ToWGS84[1]);
    }

    public static MLatLng Wgs84ToGcj02(MLatLng mLatLng) {
        double[] dArrTransformWGS84ToGCJ02 = transformWGS84ToGCJ02(mLatLng.latitude, mLatLng.longitude);
        return new MLatLng((float) dArrTransformWGS84ToGCJ02[0], (float) dArrTransformWGS84ToGCJ02[1]);
    }
}
