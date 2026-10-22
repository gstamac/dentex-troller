package com.amap.api.maps.utils;

import android.util.Pair;
import com.amap.api.maps.model.LatLng;
import com.autonavi.amap.mapcore.DPoint;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class SpatialRelationUtil {
    public static Pair<Integer, LatLng> calShortestDistancePoint(List<LatLng> list, LatLng latLng) {
        if (list != null && latLng != null) {
            try {
                if (list.size() != 0) {
                    ArrayList arrayList = new ArrayList();
                    int i = 0;
                    for (LatLng latLng2 : list) {
                        arrayList.add(DPoint.obtain(latLng2.latitude, latLng2.longitude));
                        if (latLng2.equals(latLng)) {
                            return new Pair<>(Integer.valueOf(i), latLng);
                        }
                        i++;
                    }
                    Pair<Integer, DPoint> pairCalShortestDistancePoint = calShortestDistancePoint(arrayList, DPoint.obtain(latLng.latitude, latLng.longitude));
                    if (pairCalShortestDistancePoint != null) {
                        return new Pair<>(pairCalShortestDistancePoint.first, new LatLng(((DPoint) pairCalShortestDistancePoint.second).x, ((DPoint) pairCalShortestDistancePoint.second).y));
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return null;
    }

    public static Pair<Integer, DPoint> calShortestDistancePoint(List<DPoint> list, DPoint dPoint) {
        List<DPoint> list2 = list;
        DPoint dPoint2 = dPoint;
        Pair<Integer, DPoint> pair = null;
        if (list2 != null && dPoint2 != null && list.size() != 0) {
            if (list.size() < 2) {
                return null;
            }
            int i = 0;
            double d = 0.0d;
            DPoint dPoint3 = null;
            while (i < list.size() - 1) {
                if (i == 0) {
                    dPoint3 = list2.get(i);
                    if (dPoint3.equals(dPoint2)) {
                        return new Pair<>(Integer.valueOf(i), dPoint2);
                    }
                }
                int i2 = i + 1;
                DPoint dPoint4 = list2.get(i2);
                if (dPoint4.equals(dPoint2)) {
                    return new Pair<>(Integer.valueOf(i2), dPoint2);
                }
                double d2 = d;
                int i3 = i;
                Pair<Double, DPoint> pairPointToSegDist = pointToSegDist(dPoint2.x, dPoint2.y, dPoint3.x, dPoint3.y, dPoint4.x, dPoint4.y);
                if (pair != null && d2 <= ((Double) pairPointToSegDist.first).doubleValue()) {
                    d = d2;
                } else {
                    double dDoubleValue = ((Double) pairPointToSegDist.first).doubleValue();
                    Pair<Integer, DPoint> pair2 = new Pair<>(Integer.valueOf(i3), pairPointToSegDist.second);
                    d = dDoubleValue;
                    pair = pair2;
                }
                list2 = list;
                dPoint2 = dPoint;
                i = i2;
                dPoint3 = dPoint4;
            }
        }
        return pair;
    }

    private static Pair<Double, DPoint> pointToSegDist(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d5 - d3;
        double d8 = d - d3;
        double d9 = d6 - d4;
        double d10 = d2 - d4;
        double d11 = (d7 * d8) + (d9 * d10);
        if (d11 <= 0.0d) {
            return new Pair<>(Double.valueOf(Math.sqrt((d8 * d8) + (d10 * d10))), new DPoint(d3, d4));
        }
        double d12 = (d7 * d7) + (d9 * d9);
        if (d11 >= d12) {
            double d13 = d - d5;
            double d14 = d2 - d6;
            return new Pair<>(Double.valueOf(Math.sqrt((d13 * d13) + (d14 * d14))), new DPoint(d5, d6));
        }
        double d15 = d11 / d12;
        double d16 = d3 + (d7 * d15);
        double d17 = d4 + (d9 * d15);
        double d18 = d - d16;
        double d19 = d17 - d2;
        return new Pair<>(Double.valueOf(Math.sqrt((d18 * d18) + (d19 * d19))), new DPoint(d16, d17));
    }
}
