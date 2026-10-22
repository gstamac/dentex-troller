package im.helmsman.helmsmanandroid.utils;

import com.google.android.gms.maps.model.LatLng;
import im.helmsman.helmsmanandroid.dao.MLatLng;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SphericalUtil {
    private SphericalUtil() {
    }

    public static double computeHeading(LatLng latLng, LatLng latLng2) {
        double radians = Math.toRadians(latLng.latitude);
        double radians2 = Math.toRadians(latLng.longitude);
        double radians3 = Math.toRadians(latLng2.latitude);
        double radians4 = Math.toRadians(latLng2.longitude) - radians2;
        return MathUtils.wrap(Math.toDegrees(Math.atan2(Math.sin(radians4) * Math.cos(radians3), (Math.cos(radians) * Math.sin(radians3)) - ((Math.sin(radians) * Math.cos(radians3)) * Math.cos(radians4)))), -180.0d, 180.0d);
    }

    public static LatLng computeOffset(LatLng latLng, double d, double d2) {
        double d3 = d / 6371009.0d;
        double radians = Math.toRadians(d2);
        double radians2 = Math.toRadians(latLng.latitude);
        double radians3 = Math.toRadians(latLng.longitude);
        double dCos = Math.cos(d3);
        double dSin = Math.sin(d3);
        double dSin2 = Math.sin(radians2);
        double dCos2 = dSin * Math.cos(radians2);
        double dCos3 = (dCos * dSin2) + (Math.cos(radians) * dCos2);
        return new LatLng(Math.toDegrees(Math.asin(dCos3)), Math.toDegrees(radians3 + Math.atan2(dCos2 * Math.sin(radians), dCos - (dSin2 * dCos3))));
    }

    public static LatLng computeOffsetOrigin(LatLng latLng, double d, double d2) {
        double radians = Math.toRadians(d2);
        double d3 = d / 6371009.0d;
        double dCos = Math.cos(d3);
        double dSin = Math.sin(d3) * Math.cos(radians);
        double dSin2 = Math.sin(d3) * Math.sin(radians);
        double dSin3 = Math.sin(Math.toRadians(latLng.latitude));
        double d4 = dCos * dCos;
        double d5 = dSin * dSin;
        double d6 = ((d5 * d4) + (d4 * d4)) - ((d4 * dSin3) * dSin3);
        if (d6 < 0.0d) {
            return null;
        }
        double d7 = dSin * dSin3;
        double d8 = d4 + d5;
        double dSqrt = (d7 + Math.sqrt(d6)) / d8;
        double d9 = (dSin3 - (dSin * dSqrt)) / dCos;
        double dAtan2 = Math.atan2(d9, dSqrt);
        if (dAtan2 < -1.5707963267948966d || dAtan2 > 1.5707963267948966d) {
            dAtan2 = Math.atan2(d9, (d7 - Math.sqrt(d6)) / d8);
        }
        if (dAtan2 < -1.5707963267948966d || dAtan2 > 1.5707963267948966d) {
            return null;
        }
        return new LatLng(Math.toDegrees(dAtan2), Math.toDegrees(Math.toRadians(latLng.longitude) - Math.atan2(dSin2, (dCos * Math.cos(dAtan2)) - (dSin * Math.sin(dAtan2)))));
    }

    public static MLatLng interpolate(MLatLng mLatLng, MLatLng mLatLng2, double d) {
        double radians = Math.toRadians(mLatLng.latitude);
        double radians2 = Math.toRadians(mLatLng.longitude);
        double radians3 = Math.toRadians(mLatLng2.latitude);
        double radians4 = Math.toRadians(mLatLng2.longitude);
        double dCos = Math.cos(radians);
        double dCos2 = Math.cos(radians3);
        double dComputeAngleBetween = computeAngleBetween(mLatLng, mLatLng2);
        double dSin = Math.sin(dComputeAngleBetween);
        if (dSin < 1.0E-6d) {
            return mLatLng;
        }
        double dSin2 = Math.sin((1.0d - d) * dComputeAngleBetween) / dSin;
        double dSin3 = Math.sin(dComputeAngleBetween * d) / dSin;
        double d2 = dCos * dSin2;
        double d3 = dCos2 * dSin3;
        double dCos3 = (Math.cos(radians2) * d2) + (Math.cos(radians4) * d3);
        double dSin4 = (d2 * Math.sin(radians2)) + (d3 * Math.sin(radians4));
        return new MLatLng((float) Math.toDegrees(Math.atan2((dSin2 * Math.sin(radians)) + (Math.sin(radians3) * dSin3), Math.sqrt((dCos3 * dCos3) + (dSin4 * dSin4)))), (float) Math.toDegrees(Math.atan2(dSin4, dCos3)));
    }

    private static double distanceRadians(double d, double d2, double d3, double d4) {
        return MathUtils.arcHav(MathUtils.havDistance(d, d3, d2 - d4));
    }

    static double computeAngleBetween(MLatLng mLatLng, MLatLng mLatLng2) {
        return distanceRadians(Math.toRadians(mLatLng.latitude), Math.toRadians(mLatLng.longitude), Math.toRadians(mLatLng2.latitude), Math.toRadians(mLatLng2.longitude));
    }

    public static double computeDistanceBetween(MLatLng mLatLng, MLatLng mLatLng2) {
        return computeAngleBetween(mLatLng, mLatLng2) * 6371009.0d;
    }

    public static double computeLength(List<LatLng> list) {
        double dDistanceRadians = 0.0d;
        if (list.size() < 2) {
            return 0.0d;
        }
        LatLng latLng = list.get(0);
        double radians = Math.toRadians(latLng.latitude);
        double radians2 = Math.toRadians(latLng.longitude);
        Iterator<LatLng> it = list.iterator();
        while (true) {
            double d = radians2;
            double d2 = radians;
            if (!it.hasNext()) {
                return dDistanceRadians * 6371009.0d;
            }
            LatLng next = it.next();
            radians = Math.toRadians(next.latitude);
            radians2 = Math.toRadians(next.longitude);
            dDistanceRadians += distanceRadians(d2, d, radians, radians2);
        }
    }

    public static double computeArea(List<LatLng> list) {
        return Math.abs(computeSignedArea(list));
    }

    public static double computeSignedArea(List<LatLng> list) {
        return computeSignedArea(list, 6371009.0d);
    }

    static double computeSignedArea(List<LatLng> list, double d) {
        int size = list.size();
        double dPolarTriangleArea = 0.0d;
        if (size < 3) {
            return 0.0d;
        }
        LatLng latLng = list.get(size - 1);
        double dTan = Math.tan((1.5707963267948966d - Math.toRadians(latLng.latitude)) / 2.0d);
        double radians = Math.toRadians(latLng.longitude);
        double d2 = dTan;
        double d3 = radians;
        for (LatLng latLng2 : list) {
            double dTan2 = Math.tan((1.5707963267948966d - Math.toRadians(latLng2.latitude)) / 2.0d);
            double radians2 = Math.toRadians(latLng2.longitude);
            dPolarTriangleArea += polarTriangleArea(dTan2, radians2, d2, d3);
            d2 = dTan2;
            d3 = radians2;
        }
        return dPolarTriangleArea * d * d;
    }

    private static double polarTriangleArea(double d, double d2, double d3, double d4) {
        double d5 = d2 - d4;
        double d6 = d * d3;
        return Math.atan2(Math.sin(d5) * d6, (d6 * Math.cos(d5)) + 1.0d) * 2.0d;
    }
}
