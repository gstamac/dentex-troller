package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: EarClippingTriangulator.java */
/* JADX INFO: loaded from: classes.dex */
public class dh {
    private short[] b;
    private double[] c;
    private int d;
    private final du a = new du();
    private final dm e = new dm();
    private final du f = new du();

    public du a(double[] dArr) {
        return a(dArr, 0, dArr.length);
    }

    public du a(double[] dArr, int i, int i2) {
        this.c = dArr;
        int i3 = i2 / 2;
        this.d = i3;
        int i4 = i / 2;
        du duVar = this.a;
        duVar.a();
        duVar.c(i3);
        duVar.b = i3;
        short[] sArr = duVar.a;
        this.b = sArr;
        int i5 = i3 - 1;
        for (int i6 = 0; i6 < i3; i6++) {
            sArr[i6] = (short) ((i4 + i5) - i6);
        }
        dm dmVar = this.e;
        dmVar.a();
        dmVar.c(i3);
        for (int i7 = 0; i7 < i3; i7++) {
            dmVar.a(a(i7));
        }
        du duVar2 = this.f;
        duVar2.a();
        duVar2.c(Math.max(0, i3 - 2) * 3);
        a();
        return duVar2;
    }

    private void a() {
        int i;
        int[] iArr = this.e.a;
        while (true) {
            i = this.d;
            int i2 = 0;
            if (i <= 3) {
                break;
            }
            int iB = b();
            c(iB);
            int iD = d(iB);
            if (iB != this.d) {
                i2 = iB;
            }
            iArr[iD] = a(iD);
            iArr[i2] = a(i2);
        }
        if (i == 3) {
            du duVar = this.f;
            short[] sArr = this.b;
            duVar.a(sArr[0]);
            duVar.a(sArr[1]);
            duVar.a(sArr[2]);
        }
    }

    private int a(int i) {
        short[] sArr = this.b;
        int i2 = sArr[d(i)] * 2;
        int i3 = sArr[i] * 2;
        int i4 = sArr[e(i)] * 2;
        double[] dArr = this.c;
        return a(dArr[i2], dArr[i2 + 1], dArr[i3], dArr[i3 + 1], dArr[i4], dArr[i4 + 1]);
    }

    private int b() {
        int i = this.d;
        for (int i2 = 0; i2 < i; i2++) {
            if (b(i2)) {
                return i2;
            }
        }
        int[] iArr = this.e.a;
        for (int i3 = 0; i3 < i; i3++) {
            if (iArr[i3] != -1) {
                return i3;
            }
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean b(int i) {
        int i2;
        int[] iArr = this.e.a;
        if (iArr[i] == -1) {
            return false;
        }
        int iD = d(i);
        int iE = e(i);
        short[] sArr = this.b;
        int i3 = sArr[iD] * 2;
        int i4 = sArr[i] * 2;
        int i5 = sArr[iE] * 2;
        double[] dArr = this.c;
        double d = dArr[i3];
        int i6 = 1;
        double d2 = dArr[i3 + 1];
        double d3 = dArr[i4];
        double d4 = dArr[i4 + 1];
        double d5 = dArr[i5];
        double d6 = dArr[i5 + 1];
        int iE2 = e(iE);
        while (iE2 != iD) {
            if (iArr[iE2] != i6) {
                int i7 = sArr[iE2] * 2;
                double d7 = dArr[i7];
                double d8 = dArr[i7 + i6];
                i2 = i6;
                if (a(d5, d6, d, d2, d7, d8) >= 0 && a(d, d2, d3, d4, d7, d8) >= 0 && a(d3, d4, d5, d6, d7, d8) >= 0) {
                    return false;
                }
            } else {
                i2 = i6;
            }
            iE2 = e(iE2);
            i6 = i2;
        }
        return i6;
    }

    private void c(int i) {
        short[] sArr = this.b;
        du duVar = this.f;
        duVar.a(sArr[d(i)]);
        duVar.a(sArr[i]);
        duVar.a(sArr[e(i)]);
        this.a.b(i);
        this.e.b(i);
        this.d--;
    }

    private int d(int i) {
        if (i == 0) {
            i = this.d;
        }
        return i - 1;
    }

    private int e(int i) {
        return (i + 1) % this.d;
    }

    private static int a(double d, double d2, double d3, double d4, double d5, double d6) {
        return (int) Math.signum((d * (d6 - d4)) + (d3 * (d2 - d6)) + (d5 * (d4 - d2)));
    }
}
