package com.amap.api.mapcore.util;

import okhttp3.HttpUrl;

/* JADX INFO: compiled from: IntArray.java */
/* JADX INFO: loaded from: classes.dex */
public class dm {
    public int[] a;
    public int b;
    public boolean c;

    public int hashCode() {
        return 42;
    }

    public dm() {
        this(true, 16);
    }

    public dm(boolean z, int i) {
        this.c = z;
        this.a = new int[i];
    }

    public void a(int i) {
        int[] iArrD = this.a;
        int i2 = this.b;
        if (i2 == iArrD.length) {
            iArrD = d(Math.max(8, (int) (i2 * 1.75f)));
        }
        int i3 = this.b;
        this.b = i3 + 1;
        iArrD[i3] = i;
    }

    public int b(int i) {
        int i2 = this.b;
        if (i >= i2) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + this.b);
        }
        int[] iArr = this.a;
        int i3 = iArr[i];
        int i4 = i2 - 1;
        this.b = i4;
        if (this.c) {
            System.arraycopy(iArr, i + 1, iArr, i, i4 - i);
        } else {
            iArr[i] = iArr[i4];
        }
        return i3;
    }

    public void a() {
        this.b = 0;
    }

    public int[] c(int i) {
        int i2 = this.b + i;
        if (i2 > this.a.length) {
            d(Math.max(8, i2));
        }
        return this.a;
    }

    protected int[] d(int i) {
        int[] iArr = new int[i];
        System.arraycopy(this.a, 0, iArr, 0, Math.min(this.b, i));
        this.a = iArr;
        return iArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof dm)) {
            return false;
        }
        dm dmVar = (dm) obj;
        int i = this.b;
        if (i != dmVar.b) {
            return false;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (this.a[i2] != dmVar.a[i2]) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        if (this.b == 0) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        int[] iArr = this.a;
        StringBuilder sb = new StringBuilder(32);
        sb.append('[');
        sb.append(iArr[0]);
        for (int i = 1; i < this.b; i++) {
            sb.append(", ");
            sb.append(iArr[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
