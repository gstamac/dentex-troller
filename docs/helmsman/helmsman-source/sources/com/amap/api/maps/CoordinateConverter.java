package com.amap.api.maps;

import android.content.Context;
import com.amap.api.mapcore.util.ab;
import com.amap.api.mapcore.util.dr;
import com.amap.api.mapcore.util.fn;
import com.amap.api.maps.model.LatLng;

/* JADX INFO: loaded from: classes.dex */
public class CoordinateConverter {
    private Context a;
    private CoordType b = null;
    private LatLng c = null;

    public enum CoordType {
        BAIDU,
        MAPBAR,
        GPS,
        MAPABC,
        SOSOMAP,
        ALIYUN,
        GOOGLE
    }

    public CoordinateConverter(Context context) {
        this.a = context;
    }

    public CoordinateConverter from(CoordType coordType) {
        this.b = coordType;
        return this;
    }

    public CoordinateConverter coord(LatLng latLng) {
        this.c = latLng;
        return this;
    }

    public LatLng convert() {
        LatLng latLngA = null;
        if (this.b == null || this.c == null) {
            return null;
        }
        try {
            switch (AnonymousClass1.a[this.b.ordinal()]) {
                case 1:
                    latLngA = ab.a(this.c);
                    break;
                case 2:
                    latLngA = ab.b(this.a, this.c);
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    latLngA = this.c;
                    break;
                case 7:
                    latLngA = ab.a(this.a, this.c);
                    break;
            }
            return latLngA;
        } catch (Throwable th) {
            th.printStackTrace();
            fn.b(th, "CoordinateConverter", "convert");
            return this.c;
        }
    }

    /* JADX INFO: renamed from: com.amap.api.maps.CoordinateConverter$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[CoordType.values().length];
            a = iArr;
            try {
                iArr[CoordType.BAIDU.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[CoordType.MAPBAR.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[CoordType.MAPABC.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[CoordType.SOSOMAP.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[CoordType.ALIYUN.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[CoordType.GOOGLE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[CoordType.GPS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public static boolean isAMapDataAvailable(double d, double d2) {
        return dr.a(d, d2);
    }
}
