package cn.jiguang.ai;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public final class h implements Serializable, Comparable {
    public static final h a;
    public static final h b;
    private static final byte[] f = {0};
    private static final byte[] g = {1, 42};
    private static final DecimalFormat h;
    private static final byte[] i;
    private static final h j;
    private byte[] c;
    private long d;
    private int e;

    static {
        DecimalFormat decimalFormat = new DecimalFormat();
        h = decimalFormat;
        i = new byte[256];
        decimalFormat.setMinimumIntegerDigits(3);
        int i2 = 0;
        while (true) {
            byte[] bArr = i;
            if (i2 >= bArr.length) {
                h hVar = new h();
                a = hVar;
                hVar.b(f, 0, 1);
                h hVar2 = new h();
                b = hVar2;
                hVar2.c = new byte[0];
                h hVar3 = new h();
                j = hVar3;
                hVar3.b(g, 0, 1);
                return;
            }
            if (i2 < 65 || i2 > 90) {
                bArr[i2] = (byte) i2;
            } else {
                bArr[i2] = (byte) ((i2 - 65) + 97);
            }
            i2++;
        }
    }

    private h() {
    }

    public h(c cVar) throws IOException {
        byte[] bArr = new byte[64];
        boolean z = false;
        boolean z2 = false;
        while (!z) {
            int iF = cVar.f();
            int i2 = iF & 192;
            if (i2 != 0) {
                if (i2 != 192) {
                    throw new IOException("bad label type");
                }
                int iF2 = cVar.f() + ((iF & (-193)) << 8);
                if (iF2 >= cVar.a() - 2) {
                    throw new IOException("bad compression");
                }
                if (!z2) {
                    cVar.d();
                    z2 = true;
                }
                cVar.b(iF2);
            } else {
                if (b() >= 128) {
                    throw new IOException("too many labels");
                }
                if (iF == 0) {
                    a(f, 0, 1);
                    z = true;
                } else {
                    bArr[0] = (byte) iF;
                    cVar.a(bArr, 1, iF);
                    a(bArr, 0, 1);
                }
            }
        }
        if (z2) {
            cVar.e();
        }
    }

    private h(h hVar, int i2) {
        int iB = hVar.b();
        if (i2 > iB) {
            throw new IllegalArgumentException("attempted to remove too many labels");
        }
        this.c = hVar.c;
        int i3 = iB - i2;
        b(i3);
        for (int i4 = 0; i4 < 7 && i4 < i3; i4++) {
            a(i4, hVar.a(i4 + i2));
        }
    }

    private h(String str, h hVar) throws IOException {
        int i2;
        boolean z;
        int i3;
        h hVar2;
        if (str.equals("")) {
            throw a(str, "empty name");
        }
        if (str.equals("@")) {
            if (hVar != null) {
                b(hVar, this);
                return;
            }
            hVar2 = b;
        } else {
            if (!str.equals(".")) {
                byte[] bArr = new byte[64];
                int i4 = 0;
                boolean z2 = false;
                int i5 = -1;
                int i6 = 1;
                int i7 = 0;
                for (int i8 = 0; i8 < str.length(); i8++) {
                    byte bCharAt = (byte) str.charAt(i8);
                    if (z2) {
                        if (bCharAt >= 48 && bCharAt <= 57 && i4 < 3) {
                            i4++;
                            i7 = (i7 * 10) + (bCharAt - 48);
                            if (i7 > 255) {
                                throw a(str, "bad escape");
                            }
                            if (i4 >= 3) {
                                bCharAt = (byte) i7;
                            } else {
                                continue;
                            }
                        } else if (i4 > 0 && i4 < 3) {
                            throw a(str, "bad escape");
                        }
                        if (i6 > 63) {
                            throw a(str, "label too long");
                        }
                        i3 = i6 + 1;
                        bArr[i6] = bCharAt;
                        i5 = i6;
                        z2 = false;
                        i6 = i3;
                    } else if (bCharAt == 92) {
                        i4 = 0;
                        z2 = true;
                        i7 = 0;
                    } else if (bCharAt != 46) {
                        i5 = i5 == -1 ? i8 : i5;
                        if (i6 > 63) {
                            throw a(str, "label too long");
                        }
                        i3 = i6 + 1;
                        bArr[i6] = bCharAt;
                        i6 = i3;
                    } else {
                        if (i5 == -1) {
                            throw a(str, "invalid empty label");
                        }
                        bArr[0] = (byte) (i6 - 1);
                        a(str, bArr, 0, 1);
                        i5 = -1;
                        i6 = 1;
                    }
                }
                if (i4 > 0 && i4 < 3) {
                    throw a(str, "bad escape");
                }
                if (z2) {
                    throw a(str, "bad escape");
                }
                if (i5 == -1) {
                    z = true;
                    i2 = 0;
                    a(str, f, 0, 1);
                } else {
                    i2 = 0;
                    bArr[0] = (byte) (i6 - 1);
                    a(str, bArr, 0, 1);
                    z = false;
                }
                if (hVar == null || z) {
                    return;
                }
                a(str, hVar.c, hVar.a(i2), hVar.b());
                return;
            }
            hVar2 = a;
        }
        b(hVar2, this);
    }

    private final int a(int i2) {
        if (i2 == 0 && b() == 0) {
            return 0;
        }
        if (i2 < 0 || i2 >= b()) {
            throw new IllegalArgumentException("label out of range");
        }
        if (i2 < 7) {
            return ((int) (this.d >>> ((7 - i2) * 8))) & 255;
        }
        int iA = a(6);
        for (int i3 = 6; i3 < i2; i3++) {
            iA += this.c[iA] + 1;
        }
        return iA;
    }

    public static h a(h hVar, h hVar2) throws IOException {
        if (hVar.a()) {
            return hVar;
        }
        h hVar3 = new h();
        b(hVar, hVar3);
        hVar3.a(hVar2.c, hVar2.a(0), hVar2.b());
        return hVar3;
    }

    public static h a(String str) {
        return a(str, (h) null);
    }

    public static h a(String str, h hVar) {
        if (!str.equals("@") || hVar == null) {
            return str.equals(".") ? a : new h(str, hVar);
        }
        return hVar;
    }

    private static IOException a(String str, String str2) {
        return new IOException("'" + str + "': " + str2);
    }

    private static String a(byte[] bArr, int i2) {
        StringBuffer stringBuffer = new StringBuffer();
        int i3 = i2 + 1;
        int i4 = bArr[i2];
        for (int i5 = i3; i5 < i3 + i4; i5++) {
            int i6 = bArr[i5] & UByte.MAX_VALUE;
            if (i6 <= 32 || i6 >= 127) {
                stringBuffer.append('\\');
                stringBuffer.append(h.format(i6));
            } else {
                if (i6 == 34 || i6 == 40 || i6 == 41 || i6 == 46 || i6 == 59 || i6 == 92 || i6 == 64 || i6 == 36) {
                    stringBuffer.append('\\');
                }
                stringBuffer.append((char) i6);
            }
        }
        return stringBuffer.toString();
    }

    private final void a(int i2, int i3) {
        if (i2 >= 7) {
            return;
        }
        int i4 = (7 - i2) * 8;
        this.d = (((long) i3) << i4) | (this.d & (~(255 << i4)));
    }

    private final void a(String str, byte[] bArr, int i2, int i3) throws IOException {
        try {
            a(bArr, i2, i3);
        } catch (Exception unused) {
            throw a(str, "Name too long");
        }
    }

    private final void a(byte[] bArr, int i2, int i3) throws IOException {
        byte[] bArr2 = this.c;
        int length = bArr2 == null ? 0 : bArr2.length - a(0);
        int i4 = i2;
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = bArr[i4];
            if (i7 > 63) {
                throw new IllegalStateException("invalid label");
            }
            int i8 = i7 + 1;
            i4 += i8;
            i5 += i8;
        }
        int i9 = length + i5;
        if (i9 > 255) {
            throw new IOException();
        }
        int iB = b();
        int i10 = iB + i3;
        if (i10 > 128) {
            throw new IllegalStateException("too many labels");
        }
        byte[] bArr3 = new byte[i9];
        if (length != 0) {
            System.arraycopy(this.c, a(0), bArr3, 0, length);
        }
        System.arraycopy(bArr, i2, bArr3, length, i5);
        this.c = bArr3;
        for (int i11 = 0; i11 < i3; i11++) {
            a(iB + i11, length);
            length += bArr3[length] + 1;
        }
        b(i10);
    }

    private final int b() {
        return (int) (this.d & 255);
    }

    private final void b(int i2) {
        this.d = (this.d & (-256)) | ((long) i2);
    }

    private static final void b(h hVar, h hVar2) {
        if (hVar.a(0) == 0) {
            hVar2.c = hVar.c;
            hVar2.d = hVar.d;
            return;
        }
        int iA = hVar.a(0);
        int length = hVar.c.length - iA;
        int iB = hVar.b();
        byte[] bArr = new byte[length];
        hVar2.c = bArr;
        System.arraycopy(hVar.c, iA, bArr, 0, length);
        for (int i2 = 0; i2 < iB && i2 < 7; i2++) {
            hVar2.a(i2, hVar.a(i2) - iA);
        }
        hVar2.b(iB);
    }

    private final void b(byte[] bArr, int i2, int i3) {
        try {
            a(bArr, 0, 1);
        } catch (Exception unused) {
        }
    }

    public final void a(d dVar) {
        byte[] bArr;
        int iB = b();
        if (iB == 0) {
            bArr = new byte[0];
        } else {
            byte[] bArr2 = new byte[this.c.length - a(0)];
            int iA = a(0);
            int i2 = 0;
            for (int i3 = 0; i3 < iB; i3++) {
                byte b2 = this.c[iA];
                if (b2 > 63) {
                    throw new IllegalStateException("invalid label");
                }
                iA++;
                bArr2[i2] = b2;
                i2++;
                int i4 = 0;
                while (i4 < b2) {
                    bArr2[i2] = i[this.c[iA] & UByte.MAX_VALUE];
                    i4++;
                    i2++;
                    iA++;
                }
            }
            bArr = bArr2;
        }
        dVar.a(bArr);
    }

    public final void a(d dVar, a aVar) {
        int iB = b();
        int i2 = 0;
        while (i2 < iB - 1) {
            h hVar = i2 == 0 ? this : new h(this, i2);
            int iA = aVar != null ? aVar.a(hVar) : -1;
            if (iA >= 0) {
                dVar.c(49152 | iA);
                return;
            }
            if (aVar != null) {
                aVar.a(dVar.a(), hVar);
            }
            int iA2 = a(i2);
            byte[] bArr = this.c;
            dVar.a(bArr, iA2, bArr[iA2] + 1);
            i2++;
        }
        dVar.b(0);
    }

    public final void a(d dVar, a aVar, boolean z) {
        if (z) {
            a(dVar);
        } else {
            a(dVar, (a) null);
        }
    }

    public final boolean a() {
        int iB = b();
        return iB != 0 && this.c[a(iB - 1)] == 0;
    }

    @Override // java.lang.Comparable
    public final int compareTo(Object obj) {
        h hVar = (h) obj;
        if (this == hVar) {
            return 0;
        }
        int iB = b();
        int iB2 = hVar.b();
        int i2 = iB > iB2 ? iB2 : iB;
        for (int i3 = 1; i3 <= i2; i3++) {
            int iA = a(iB - i3);
            int iA2 = hVar.a(iB2 - i3);
            byte b2 = this.c[iA];
            byte b3 = hVar.c[iA2];
            for (int i4 = 0; i4 < b2 && i4 < b3; i4++) {
                byte[] bArr = i;
                int i5 = bArr[this.c[(i4 + iA) + 1] & UByte.MAX_VALUE] - bArr[hVar.c[(i4 + iA2) + 1] & UByte.MAX_VALUE];
                if (i5 != 0) {
                    return i5;
                }
            }
            if (b2 != b3) {
                return b2 - b3;
            }
        }
        return iB - iB2;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof h)) {
            return false;
        }
        h hVar = (h) obj;
        if (hVar.e == 0) {
            hVar.hashCode();
        }
        if (this.e == 0) {
            hashCode();
        }
        if (hVar.e != this.e || hVar.b() != b()) {
            return false;
        }
        byte[] bArr = hVar.c;
        int iA = hVar.a(0);
        int iB = b();
        int iA2 = a(0);
        for (int i2 = 0; i2 < iB; i2++) {
            byte b2 = this.c[iA2];
            if (b2 != bArr[iA]) {
                return false;
            }
            iA2++;
            iA++;
            if (b2 > 63) {
                throw new IllegalStateException("invalid label");
            }
            int i3 = 0;
            while (i3 < b2) {
                byte[] bArr2 = i;
                int i4 = iA2 + 1;
                int i5 = iA + 1;
                if (bArr2[this.c[iA2] & UByte.MAX_VALUE] != bArr2[bArr[iA] & UByte.MAX_VALUE]) {
                    return false;
                }
                i3++;
                iA = i5;
                iA2 = i4;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i2 = this.e;
        if (i2 != 0) {
            return i2;
        }
        int i3 = 0;
        int iA = a(0);
        while (true) {
            byte[] bArr = this.c;
            if (iA >= bArr.length) {
                this.e = i3;
                return i3;
            }
            i3 += (i3 << 3) + i[bArr[iA] & UByte.MAX_VALUE];
            iA++;
        }
    }

    public final String toString() {
        int iB = b();
        if (iB == 0) {
            return "@";
        }
        if (iB == 1 && this.c[a(0)] == 0) {
            return ".";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int iA = a(0);
        for (int i2 = 0; i2 < iB; i2++) {
            byte b2 = this.c[iA];
            if (b2 > 63) {
                throw new IllegalStateException("invalid label");
            }
            if (b2 == 0) {
                stringBuffer.append('.');
                break;
            }
            if (i2 > 0) {
                stringBuffer.append('.');
            }
            stringBuffer.append(a(this.c, iA));
            iA += b2 + 1;
        }
        return stringBuffer.toString();
    }
}
