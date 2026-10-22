package com.amap.api.mapcore.util;

import okhttp3.HttpUrl;

/* JADX INFO: compiled from: ShortArray.java */
/* JADX INFO: loaded from: classes.dex */
public class du {
    public short[] a;
    public int b;
    public boolean c;

    public int hashCode() {
        return 42;
    }

    public du() {
        this(true, 16);
    }

    public du(boolean z, int i) {
        this.c = z;
        this.a = new short[i];
    }

    public void a(short s) {
        short[] sArrD = this.a;
        int i = this.b;
        if (i == sArrD.length) {
            sArrD = d(Math.max(8, (int) (i * 1.75f)));
        }
        int i2 = this.b;
        this.b = i2 + 1;
        sArrD[i2] = s;
    }

    public short a(int i) {
        if (i >= this.b) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + this.b);
        }
        return this.a[i];
    }

    public short b(int i) {
        int i2 = this.b;
        if (i >= i2) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + this.b);
        }
        short[] sArr = this.a;
        short s = sArr[i];
        int i3 = i2 - 1;
        this.b = i3;
        if (this.c) {
            System.arraycopy(sArr, i + 1, sArr, i, i3 - i);
        } else {
            sArr[i] = sArr[i3];
        }
        return s;
    }

    public void a() {
        this.b = 0;
    }

    public short[] c(int i) {
        int i2 = this.b + i;
        if (i2 > this.a.length) {
            d(Math.max(8, i2));
        }
        return this.a;
    }

    protected short[] d(int i) {
        short[] sArr = new short[i];
        System.arraycopy(this.a, 0, sArr, 0, Math.min(this.b, i));
        this.a = sArr;
        return sArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof du)) {
            return false;
        }
        du duVar = (du) obj;
        int i = this.b;
        if (i != duVar.b) {
            return false;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (this.a[i2] != duVar.a[i2]) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        if (this.b == 0) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        short[] sArr = this.a;
        StringBuilder sb = new StringBuilder(32);
        sb.append('[');
        sb.append((int) sArr[0]);
        for (int i = 1; i < this.b; i++) {
            sb.append(", ");
            sb.append((int) sArr[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
