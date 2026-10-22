package cn.jiguang.ao;

import cn.jiguang.f.i;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    private static final BigInteger d = BigInteger.ONE.shiftLeft(64);
    private byte[] a;
    private int b;
    private int c;

    public b() {
        this(32);
    }

    public b(int i) {
        this.a = new byte[i];
        this.b = 0;
        this.c = -1;
    }

    private static void a(long j, int i) {
        long j2 = 1 << i;
        if (j < 0 || j > j2) {
            cn.jiguang.ad.d.f("Outputer", j + " out of range for " + i + " bit value max:" + j2);
        }
    }

    private void a(byte[] bArr, int i, int i2) {
        c(i2);
        System.arraycopy(bArr, 0, this.a, this.b, i2);
        this.b += i2;
    }

    private void c(int i) {
        byte[] bArr = this.a;
        int length = bArr.length;
        int i2 = this.b;
        if (length - i2 >= i) {
            return;
        }
        int length2 = bArr.length * 2;
        if (length2 < i2 + i) {
            length2 = i2 + i;
        }
        byte[] bArr2 = new byte[length2];
        System.arraycopy(bArr, 0, bArr2, 0, i2);
        this.a = bArr2;
    }

    public final int a() {
        return this.b;
    }

    public final void a(int i) {
        a(i, 8);
        c(1);
        byte[] bArr = this.a;
        int i2 = this.b;
        this.b = i2 + 1;
        bArr[i2] = (byte) (i & 255);
    }

    public final void a(int i, int i2) {
        a(i, 8);
        if (4 > this.b - 1) {
            throw new IllegalArgumentException("cannot write past end of data");
        }
        this.a[4] = (byte) (i & 255);
    }

    public final void a(long j) {
        a(j, 32);
        c(4);
        byte[] bArr = this.a;
        int i = this.b;
        int i2 = i + 1;
        bArr[i] = (byte) ((j >>> 24) & 255);
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((j >>> 16) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((j >>> 8) & 255);
        this.b = i4 + 1;
        bArr[i4] = (byte) (j & 255);
    }

    public final void a(String str) {
        byte[] bArrI = i.i(str);
        b(bArrI.length);
        a(bArrI, 0, bArrI.length);
    }

    public final void a(byte[] bArr) {
        a(bArr, 0, bArr.length);
    }

    public final void b(int i) {
        a(i, 16);
        c(2);
        byte[] bArr = this.a;
        int i2 = this.b;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        this.b = i3 + 1;
        bArr[i3] = (byte) (i & 255);
    }

    public final void b(int i, int i2) {
        a(i, 16);
        if (this.b - 2 < 0) {
            throw new IllegalArgumentException("cannot write past end of data");
        }
        byte[] bArr = this.a;
        bArr[0] = (byte) ((i >>> 8) & 255);
        bArr[1] = (byte) (i & 255);
    }

    public final void b(long j) {
        c(8);
        byte[] bArr = this.a;
        int i = this.b;
        int i2 = i + 1;
        bArr[i] = (byte) ((j >>> 56) & 255);
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((j >>> 48) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((j >>> 40) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((j >>> 32) & 255);
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((j >>> 24) & 255);
        int i7 = i6 + 1;
        bArr[i6] = (byte) ((j >>> 16) & 255);
        int i8 = i7 + 1;
        bArr[i7] = (byte) ((j >>> 8) & 255);
        this.b = i8 + 1;
        bArr[i8] = (byte) (j & 255);
    }

    public final byte[] b() {
        int i = this.b;
        byte[] bArr = new byte[i];
        System.arraycopy(this.a, 0, bArr, 0, i);
        return bArr;
    }
}
