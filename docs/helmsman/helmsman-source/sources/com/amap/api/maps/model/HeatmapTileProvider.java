package com.amap.api.maps.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import androidx.collection.LongSparseArray;
import com.MAVLink.common.msg_local_position_ned_cov;
import com.amap.api.mapcore.util.df;
import com.amap.api.maps.AMapException;
import com.autonavi.amap.mapcore.DPoint;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class HeatmapTileProvider implements TileProvider {
    public static final Gradient DEFAULT_GRADIENT;
    public static final double DEFAULT_OPACITY = 0.6d;
    public static final int DEFAULT_RADIUS = 12;
    private static final int[] a;
    private static final float[] b;
    private a c;
    private Collection<WeightedLatLng> d;
    private df e;
    private int f;
    private Gradient g;
    private int[] h;
    private double[] i;
    private double j;
    private double[] k;

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileHeight() {
        return 256;
    }

    @Override // com.amap.api.maps.model.TileProvider
    public int getTileWidth() {
        return 256;
    }

    static {
        int[] iArr = {Color.rgb(102, msg_local_position_ned_cov.MAVLINK_MSG_LENGTH, 0), Color.rgb(255, 0, 0)};
        a = iArr;
        float[] fArr = {0.2f, 1.0f};
        b = fArr;
        DEFAULT_GRADIENT = new Gradient(iArr, fArr);
    }

    public static class Builder {
        private Collection<WeightedLatLng> a;
        private int b = 12;
        private Gradient c = HeatmapTileProvider.DEFAULT_GRADIENT;
        private double d = 0.6d;

        public Builder data(Collection<LatLng> collection) {
            return weightedData(HeatmapTileProvider.d(collection));
        }

        public Builder weightedData(Collection<WeightedLatLng> collection) {
            this.a = collection;
            return this;
        }

        public Builder radius(int i) {
            this.b = Math.max(10, Math.min(i, 50));
            return this;
        }

        public Builder gradient(Gradient gradient) {
            this.c = gradient;
            return this;
        }

        public Builder transparency(double d) {
            this.d = Math.max(0.0d, Math.min(d, 1.0d));
            return this;
        }

        public HeatmapTileProvider build() {
            Collection<WeightedLatLng> collection = this.a;
            if (collection == null || collection.size() == 0) {
                try {
                    throw new AMapException("No input points.");
                } catch (AMapException e) {
                    Log.e("amap", e.getErrorMessage());
                    e.printStackTrace();
                    return null;
                }
            }
            try {
                return new HeatmapTileProvider(this);
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }
    }

    private HeatmapTileProvider(Builder builder) {
        this.d = builder.a;
        this.f = builder.b;
        Gradient gradient = builder.c;
        this.g = gradient;
        if (gradient == null || !gradient.isAvailable()) {
            this.g = DEFAULT_GRADIENT;
        }
        this.j = builder.d;
        int i = this.f;
        this.i = a(i, ((double) i) / 3.0d);
        a(this.g);
        c(this.d);
    }

    private void c(Collection<WeightedLatLng> collection) {
        try {
            ArrayList arrayList = new ArrayList();
            for (WeightedLatLng weightedLatLng : collection) {
                if (weightedLatLng.latLng.latitude < 85.0d && weightedLatLng.latLng.latitude > -85.0d) {
                    arrayList.add(weightedLatLng);
                }
            }
            this.d = arrayList;
            this.e = a(arrayList);
            this.c = new a(this.e);
            Iterator<WeightedLatLng> it = this.d.iterator();
            while (it.hasNext()) {
                this.c.a(it.next());
            }
            this.k = a(this.f);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Collection<WeightedLatLng> d(Collection<LatLng> collection) {
        ArrayList arrayList = new ArrayList();
        Iterator<LatLng> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(new WeightedLatLng(it.next()));
        }
        return arrayList;
    }

    /* JADX WARN: Code duplicated, block: B:12:0x00a9  */
    /* JADX WARN: Code duplicated, block: B:14:0x00ac  */
    /* JADX WARN: Code duplicated, block: B:16:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:18:0x00bb  */
    /* JADX WARN: Code duplicated, block: B:21:0x00db A[LOOP:0: B:19:0x00d5->B:21:0x00db, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:25:0x0109 A[LOOP:1: B:23:0x0103->B:25:0x0109, LOOP_END] */
    @Override // com.amap.api.maps.model.TileProvider
    public Tile getTile(int i, int i2, int i3) {
        double d;
        double d2;
        df dfVar;
        Collection<WeightedLatLng> collectionA;
        double[][] dArr;
        Iterator<WeightedLatLng> it;
        Iterator<WeightedLatLng> it2;
        double dPow = 1.0d / Math.pow(2.0d, i3);
        int i4 = this.f;
        double d3 = (((double) i4) * dPow) / 256.0d;
        double d4 = ((2.0d * d3) + dPow) / ((double) ((i4 * 2) + 256));
        double d5 = (((double) i) * dPow) - d3;
        double d6 = (((double) (i + 1)) * dPow) + d3;
        double d7 = (((double) i2) * dPow) - d3;
        double d8 = (((double) (i2 + 1)) * dPow) + d3;
        Collection<WeightedLatLng> arrayList = new ArrayList<>();
        if (d5 < 0.0d) {
            arrayList = this.c.a(new df(d5 + 1.0d, 1.0d, d7, d8));
            d = -1.0d;
        } else {
            d = 1.0d;
            if (d6 > 1.0d) {
                arrayList = this.c.a(new df(0.0d, d6 - 1.0d, d7, d8));
            } else {
                d2 = 0.0d;
            }
            dfVar = new df(d5, d6, d7, d8);
            if (!dfVar.a(new df(this.e.a - d3, this.e.c + d3, this.e.b - d3, this.e.d + d3))) {
                return TileProvider.NO_TILE;
            }
            collectionA = this.c.a(dfVar);
            if (collectionA.isEmpty()) {
                return TileProvider.NO_TILE;
            }
            int i5 = this.f;
            dArr = (double[][]) Array.newInstance((Class<?>) Double.TYPE, (i5 * 2) + 256, (i5 * 2) + 256);
            for (it = collectionA.iterator(); it.hasNext(); it = it) {
                WeightedLatLng next = it.next();
                DPoint point = next.getPoint();
                int i6 = (int) ((point.x - d5) / d4);
                int i7 = (int) ((point.y - d7) / d4);
                double[] dArr2 = dArr[i6];
                dArr2[i7] = dArr2[i7] + next.intensity;
            }
            for (it2 = arrayList.iterator(); it2.hasNext(); it2 = it2) {
                WeightedLatLng next2 = it2.next();
                DPoint point2 = next2.getPoint();
                int i8 = (int) (((point2.x + d2) - d5) / d4);
                int i9 = (int) ((point2.y - d7) / d4);
                double[] dArr3 = dArr[i8];
                dArr3[i9] = dArr3[i9] + next2.intensity;
            }
            return a(a(a(dArr, this.i), this.h, this.k[i3]));
        }
        d2 = d;
        dfVar = new df(d5, d6, d7, d8);
        if (!dfVar.a(new df(this.e.a - d3, this.e.c + d3, this.e.b - d3, this.e.d + d3))) {
            return TileProvider.NO_TILE;
        }
        collectionA = this.c.a(dfVar);
        if (collectionA.isEmpty()) {
            return TileProvider.NO_TILE;
        }
        int i10 = this.f;
        dArr = (double[][]) Array.newInstance((Class<?>) Double.TYPE, (i10 * 2) + 256, (i10 * 2) + 256);
        while (it.hasNext()) {
            WeightedLatLng next3 = it.next();
            DPoint point3 = next3.getPoint();
            int i11 = (int) ((point3.x - d5) / d4);
            int i12 = (int) ((point3.y - d7) / d4);
            double[] dArr4 = dArr[i11];
            dArr4[i12] = dArr4[i12] + next3.intensity;
        }
        while (it2.hasNext()) {
            WeightedLatLng next4 = it2.next();
            DPoint point4 = next4.getPoint();
            int i13 = (int) (((point4.x + d2) - d5) / d4);
            int i14 = (int) ((point4.y - d7) / d4);
            double[] dArr5 = dArr[i13];
            dArr5[i14] = dArr5[i14] + next4.intensity;
        }
        return a(a(a(dArr, this.i), this.h, this.k[i3]));
    }

    private void a(Gradient gradient) {
        this.g = gradient;
        this.h = gradient.generateColorMap(this.j);
    }

    private double[] a(int i) {
        int i2;
        double[] dArr = new double[21];
        int i3 = 5;
        while (true) {
            if (i3 >= 11) {
                break;
            }
            dArr[i3] = a(this.d, this.e, i, (int) (Math.pow(2.0d, i3) * 1280.0d));
            if (i3 == 5) {
                for (int i4 = 0; i4 < i3; i4++) {
                    dArr[i4] = dArr[i3];
                }
            }
            i3++;
        }
        for (i2 = 11; i2 < 21; i2++) {
            dArr[i2] = dArr[10];
        }
        return dArr;
    }

    private static Tile a(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return Tile.obtain(256, 256, byteArrayOutputStream.toByteArray());
    }

    static df a(Collection<WeightedLatLng> collection) {
        Iterator<WeightedLatLng> it = collection.iterator();
        WeightedLatLng next = it.next();
        double d = next.getPoint().x;
        double d2 = next.getPoint().x;
        double d3 = d;
        double d4 = d2;
        double d5 = next.getPoint().y;
        double d6 = next.getPoint().y;
        while (it.hasNext()) {
            WeightedLatLng next2 = it.next();
            double d7 = next2.getPoint().x;
            double d8 = next2.getPoint().y;
            if (d7 < d3) {
                d3 = d7;
            }
            if (d7 > d4) {
                d4 = d7;
            }
            if (d8 < d5) {
                d5 = d8;
            }
            if (d8 > d6) {
                d6 = d8;
            }
        }
        return new df(d3, d4, d5, d6);
    }

    static double[] a(int i, double d) {
        double[] dArr = new double[(i * 2) + 1];
        for (int i2 = -i; i2 <= i; i2++) {
            dArr[i2 + i] = Math.exp(((double) ((-i2) * i2)) / ((2.0d * d) * d));
        }
        return dArr;
    }

    static double[][] a(double[][] dArr, double[] dArr2) {
        int iFloor = (int) Math.floor(((double) dArr2.length) / 2.0d);
        int length = dArr.length;
        int i = length - (iFloor * 2);
        int i2 = (iFloor + i) - 1;
        double[][] dArr3 = (double[][]) Array.newInstance((Class<?>) Double.TYPE, length, length);
        for (int i3 = 0; i3 < length; i3++) {
            for (int i4 = 0; i4 < length; i4++) {
                double d = dArr[i3][i4];
                if (d != 0.0d) {
                    int i5 = i3 + iFloor;
                    if (i2 < i5) {
                        i5 = i2;
                    }
                    int i6 = i5 + 1;
                    int i7 = i3 - iFloor;
                    for (int i8 = iFloor > i7 ? iFloor : i7; i8 < i6; i8++) {
                        double[] dArr4 = dArr3[i8];
                        dArr4[i4] = dArr4[i4] + (dArr2[i8 - i7] * d);
                    }
                }
            }
        }
        double[][] dArr5 = (double[][]) Array.newInstance((Class<?>) Double.TYPE, i, i);
        for (int i9 = iFloor; i9 < i2 + 1; i9++) {
            for (int i10 = 0; i10 < length; i10++) {
                double d2 = dArr3[i9][i10];
                if (d2 != 0.0d) {
                    int i11 = i10 + iFloor;
                    if (i2 < i11) {
                        i11 = i2;
                    }
                    int i12 = i11 + 1;
                    int i13 = i10 - iFloor;
                    for (int i14 = iFloor > i13 ? iFloor : i13; i14 < i12; i14++) {
                        double[] dArr6 = dArr5[i9 - iFloor];
                        int i15 = i14 - iFloor;
                        dArr6[i15] = dArr6[i15] + (dArr2[i14 - i13] * d2);
                    }
                }
            }
        }
        return dArr5;
    }

    static Bitmap a(double[][] dArr, int[] iArr, double d) {
        int i = iArr[iArr.length - 1];
        double length = ((double) (iArr.length - 1)) / d;
        int length2 = dArr.length;
        int[] iArr2 = new int[length2 * length2];
        for (int i2 = 0; i2 < length2; i2++) {
            for (int i3 = 0; i3 < length2; i3++) {
                double d2 = dArr[i3][i2];
                int i4 = (i2 * length2) + i3;
                int i5 = (int) (d2 * length);
                if (d2 != 0.0d) {
                    if (i5 < iArr.length) {
                        iArr2[i4] = iArr[i5];
                    } else {
                        iArr2[i4] = i;
                    }
                } else {
                    iArr2[i4] = 0;
                }
            }
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(length2, length2, Bitmap.Config.ARGB_8888);
        bitmapCreateBitmap.setPixels(iArr2, 0, length2, 0, 0, length2, length2);
        return bitmapCreateBitmap;
    }

    static double a(Collection<WeightedLatLng> collection, df dfVar, int i, int i2) {
        double d = dfVar.a;
        double d2 = dfVar.c;
        double d3 = dfVar.b;
        double d4 = d2 - d;
        double d5 = dfVar.d - d3;
        if (d4 <= d5) {
            d4 = d5;
        }
        double d6 = ((double) ((int) (((double) (i2 / (i * 2))) + 0.5d))) / d4;
        LongSparseArray longSparseArray = new LongSparseArray();
        double dDoubleValue = 0.0d;
        for (WeightedLatLng weightedLatLng : collection) {
            double d7 = weightedLatLng.getPoint().x;
            int i3 = (int) ((weightedLatLng.getPoint().y - d3) * d6);
            long j = (int) ((d7 - d) * d6);
            LongSparseArray longSparseArray2 = (LongSparseArray) longSparseArray.get(j);
            if (longSparseArray2 == null) {
                longSparseArray2 = new LongSparseArray();
                longSparseArray.put(j, longSparseArray2);
            }
            long j2 = i3;
            Double dValueOf = (Double) longSparseArray2.get(j2);
            if (dValueOf == null) {
                dValueOf = Double.valueOf(0.0d);
            }
            LongSparseArray longSparseArray3 = longSparseArray;
            double d8 = d;
            Double dValueOf2 = Double.valueOf(dValueOf.doubleValue() + weightedLatLng.intensity);
            longSparseArray2.put(j2, dValueOf2);
            if (dValueOf2.doubleValue() > dDoubleValue) {
                dDoubleValue = dValueOf2.doubleValue();
            }
            longSparseArray = longSparseArray3;
            d = d8;
        }
        return dDoubleValue;
    }
}
