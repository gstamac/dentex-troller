package cn.jiguang.ai;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    private byte[] a;
    private int b;
    private int c;

    public d() {
        this(32);
    }

    private d(int i) {
        this.a = new byte[32];
        this.b = 0;
        this.c = -1;
    }

    private static void a(long j, int i) {
        long j2 = 1 << i;
        if (j < 0 || j > j2) {
            throw new IllegalArgumentException(j + " out of range for " + i + " bit value");
        }
    }

    private void d(int i) {
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
        if (i > this.b) {
            throw new IllegalArgumentException("cannot jump past end of data");
        }
        this.b = i;
    }

    public final void a(int i, int i2) {
        a(i, 16);
        if (i2 > this.b - 2) {
            throw new IllegalArgumentException("cannot write past end of data");
        }
        byte[] bArr = this.a;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        bArr[i2 + 1] = (byte) (i & 255);
    }

    public final void a(long j) {
        a(j, 32);
        d(4);
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

    public final void a(byte[] bArr) {
        a(bArr, 0, bArr.length);
    }

    public final void a(byte[] bArr, int i, int i2) {
        d(i2);
        System.arraycopy(bArr, i, this.a, this.b, i2);
        this.b += i2;
    }

    public final void b(int i) {
        a(0L, 8);
        d(1);
        byte[] bArr = this.a;
        int i2 = this.b;
        this.b = i2 + 1;
        bArr[i2] = 0;
    }

    public final byte[] b() {
        int i = this.b;
        byte[] bArr = new byte[i];
        System.arraycopy(this.a, 0, bArr, 0, i);
        return bArr;
    }

    public final void c(int i) {
        a(i, 16);
        d(2);
        byte[] bArr = this.a;
        int i2 = this.b;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 8) & 255);
        this.b = i3 + 1;
        bArr[i3] = (byte) (i & 255);
    }
}
