package com.amap.api.maps.model;

import android.graphics.Color;
import android.util.Log;
import com.amap.api.maps.AMapException;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class Gradient {
    private int a;
    private int[] b;
    private float[] c;
    private boolean d;

    private class a {
        private final int b;
        private final int c;
        private final float d;

        private a(int i, int i2, float f) {
            this.b = i;
            this.c = i2;
            this.d = f;
        }
    }

    public Gradient(int[] iArr, float[] fArr) {
        this(iArr, fArr, 1000);
    }

    private Gradient(int[] iArr, float[] fArr, int i) {
        this.d = true;
        try {
            if (iArr == null || fArr == null) {
                throw new AMapException("colors and startPoints should not be null");
            }
            if (iArr.length != fArr.length) {
                throw new AMapException("colors and startPoints should be same length");
            }
            if (iArr.length == 0) {
                throw new AMapException("No colors have been defined");
            }
            for (int i2 = 1; i2 < fArr.length; i2++) {
                if (fArr[i2] <= fArr[i2 - 1]) {
                    throw new AMapException("startPoints should be in increasing order");
                }
            }
            this.a = i;
            int[] iArr2 = new int[iArr.length];
            this.b = iArr2;
            this.c = new float[fArr.length];
            System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
            System.arraycopy(fArr, 0, this.c, 0, fArr.length);
            this.d = true;
        } catch (AMapException e) {
            this.d = false;
            Log.e("amap", e.getErrorMessage());
            e.printStackTrace();
        }
    }

    private HashMap<Integer, a> a() {
        HashMap<Integer, a> map = new HashMap<>();
        if (this.c[0] != 0.0f) {
            map.put(0, new a(Color.argb(0, Color.red(this.b[0]), Color.green(this.b[0]), Color.blue(this.b[0])), this.b[0], this.a * this.c[0]));
        }
        for (int i = 1; i < this.b.length; i++) {
            int i2 = i - 1;
            Integer numValueOf = Integer.valueOf((int) (this.a * this.c[i2]));
            int[] iArr = this.b;
            int i3 = iArr[i2];
            int i4 = iArr[i];
            float f = this.a;
            float[] fArr = this.c;
            map.put(numValueOf, new a(i3, i4, (fArr[i] - fArr[i2]) * f));
        }
        float[] fArr2 = this.c;
        if (fArr2[fArr2.length - 1] != 1.0f) {
            int length = fArr2.length - 1;
            Integer numValueOf2 = Integer.valueOf((int) (this.a * fArr2[length]));
            int i5 = this.b[length];
            map.put(numValueOf2, new a(i5, i5, this.a * (1.0f - this.c[length])));
        }
        return map;
    }

    protected int[] generateColorMap(double d) {
        HashMap<Integer, a> mapA = a();
        int[] iArr = new int[this.a];
        a aVar = mapA.get(0);
        int i = 0;
        for (int i2 = 0; i2 < this.a; i2++) {
            if (mapA.containsKey(Integer.valueOf(i2))) {
                aVar = mapA.get(Integer.valueOf(i2));
                i = i2;
            }
            iArr[i2] = a(aVar.b, aVar.c, (i2 - i) / aVar.d);
        }
        if (d != 1.0d) {
            for (int i3 = 0; i3 < this.a; i3++) {
                int i4 = iArr[i3];
                iArr[i3] = Color.argb((int) (((double) Color.alpha(i4)) * d), Color.red(i4), Color.green(i4), Color.blue(i4));
            }
        }
        return iArr;
    }

    static int a(int i, int i2, float f) {
        int iAlpha = (int) (((Color.alpha(i2) - Color.alpha(i)) * f) + Color.alpha(i));
        float[] fArr = new float[3];
        Color.RGBToHSV(Color.red(i), Color.green(i), Color.blue(i), fArr);
        float[] fArr2 = new float[3];
        Color.RGBToHSV(Color.red(i2), Color.green(i2), Color.blue(i2), fArr2);
        float f2 = fArr[0];
        float f3 = fArr2[0];
        if (f2 - f3 > 180.0f) {
            fArr2[0] = f3 + 360.0f;
        } else if (f3 - f2 > 180.0f) {
            fArr[0] = f2 + 360.0f;
        }
        float[] fArr3 = new float[3];
        for (int i3 = 0; i3 < 3; i3++) {
            float f4 = fArr2[i3];
            float f5 = fArr[i3];
            fArr3[i3] = ((f4 - f5) * f) + f5;
        }
        return Color.HSVToColor(iAlpha, fArr3);
    }

    protected boolean isAvailable() {
        return this.d;
    }
}
