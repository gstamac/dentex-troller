package cn.jiguang.p;

import android.support.v4.media.session.PlaybackStateCompat;
import java.math.BigInteger;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static final BigInteger d = BigInteger.ONE.shiftLeft(64);
    private byte[] a;
    private int b;
    private int c;

    public a() {
        this(32);
    }

    public a(int i) {
        this.a = new byte[i];
        this.b = 0;
        this.c = -1;
    }

    private void b(int i) {
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

    public final void a(int i) {
        long j = i;
        if (j < 0 || j > PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH) {
            cn.jiguang.w.a.d("JCommonPackager", j + " out of range for 16 bit value max:65536");
        }
        b(2);
        byte[] bArr = this.a;
        int i2 = this.b;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        this.b = i3 + 1;
        bArr[i3] = (byte) (i & 255);
    }

    public final void a(long j) {
        b(8);
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

    public final void a(byte[] bArr, int i, int i2) {
        b(i2);
        System.arraycopy(bArr, 0, this.a, this.b, i2);
        this.b += i2;
    }

    public final byte[] a() {
        int i = this.b;
        byte[] bArr = new byte[i];
        System.arraycopy(this.a, 0, bArr, 0, i);
        return bArr;
    }
}
