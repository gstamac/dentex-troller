package im.helmsman.helmsmanandroid.utils;

import android.graphics.Point;

/* JADX INFO: loaded from: classes2.dex */
public class MathUtils {
    static final double EARTH_RADIUS = 6371009.0d;

    static double clamp(double d, double d2, double d3) {
        if (d < d2) {
            return d2;
        }
        return d > d3 ? d3 : d;
    }

    static double mod(double d, double d2) {
        return ((d % d2) + d2) % d2;
    }

    public static int getLength(float f, float f2, float f3, float f4) {
        return (int) Math.sqrt(Math.pow(f - f3, 2.0d) + Math.pow(f2 - f4, 2.0d));
    }

    public static Point getBorderPoint(Point point, Point point2, int i) {
        double d = i;
        double radian = getRadian(point, point2);
        return new Point(point.x + ((int) (Math.cos(radian) * d)), point.x + ((int) (d * Math.sin(radian))));
    }

    public static float getRadian(Point point, Point point2) {
        float f = point2.x - point.x;
        float f2 = point2.y - point.y;
        return ((float) Math.acos(f / ((float) Math.sqrt((f * f) + (f2 * f2))))) * (point2.y < point.y ? -1 : 1);
    }

    static double wrap(double d, double d2, double d3) {
        return (d < d2 || d >= d3) ? mod(d - d2, d3 - d2) + d2 : d;
    }

    static double mercator(double d) {
        return Math.log(Math.tan((d * 0.5d) + 0.7853981633974483d));
    }

    static double inverseMercator(double d) {
        return (Math.atan(Math.exp(d)) * 2.0d) - 1.5707963267948966d;
    }

    static double hav(double d) {
        double dSin = Math.sin(d * 0.5d);
        return dSin * dSin;
    }

    static double arcHav(double d) {
        return Math.asin(Math.sqrt(d)) * 2.0d;
    }

    static double sinFromHav(double d) {
        return Math.sqrt(d * (1.0d - d)) * 2.0d;
    }

    static double havFromSin(double d) {
        double d2 = d * d;
        return (d2 / (Math.sqrt(1.0d - d2) + 1.0d)) * 0.5d;
    }

    static double sinSumFromHav(double d, double d2) {
        double dSqrt = Math.sqrt((1.0d - d) * d);
        double dSqrt2 = Math.sqrt((1.0d - d2) * d2);
        return ((dSqrt + dSqrt2) - (((dSqrt * d2) + (dSqrt2 * d)) * 2.0d)) * 2.0d;
    }

    static double havDistance(double d, double d2, double d3) {
        return hav(d - d2) + (hav(d3) * Math.cos(d) * Math.cos(d2));
    }
}
