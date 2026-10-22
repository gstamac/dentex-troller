package cn.jiguang.ai;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public abstract class j implements Serializable, Cloneable, Comparable {
    private static final DecimalFormat e;
    protected h a;
    protected int b;
    protected int c;
    protected long d;

    static {
        DecimalFormat decimalFormat = new DecimalFormat();
        e = decimalFormat;
        decimalFormat.setMinimumIntegerDigits(3);
    }

    protected j() {
    }

    static j a(c cVar, int i) throws IOException {
        h hVar = new h(cVar);
        int iG = cVar.g();
        int iG2 = cVar.g();
        if (i == 0) {
            return a(hVar, iG, iG2);
        }
        long jH = cVar.h();
        int iG3 = cVar.g();
        j jVarA = a(hVar, iG, iG2, jH);
        if (cVar != null) {
            if (cVar.b() < iG3) {
                throw new IOException("truncated record");
            }
            cVar.a(iG3);
            jVarA.a(cVar);
            if (cVar.b() > 0) {
                throw new IOException("invalid record length");
            }
            cVar.c();
        }
        return jVarA;
    }

    public static j a(h hVar, int i, int i2) {
        if (hVar.a()) {
            return a(hVar, i, i2, 0L);
        }
        throw new k(hVar);
    }

    private static final j a(h hVar, int i, int i2, long j) {
        o oVar = new o();
        oVar.a = hVar;
        oVar.b = i;
        oVar.c = i2;
        oVar.d = j;
        return oVar;
    }

    private byte[] h() {
        d dVar = new d();
        a(dVar, true);
        return dVar.b();
    }

    abstract String a();

    final void a(long j) {
        this.d = j;
    }

    abstract void a(c cVar);

    final void a(d dVar, a aVar) {
        this.a.a(dVar, aVar);
        dVar.c(this.b);
        dVar.c(this.c);
    }

    abstract void a(d dVar, boolean z);

    public final boolean a(j jVar) {
        return this.b == jVar.b && this.c == jVar.c && this.a.equals(jVar.a);
    }

    public final h b() {
        return this.a;
    }

    public final int c() {
        return this.b;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        j jVar = (j) obj;
        if (this == jVar) {
            return 0;
        }
        int iCompareTo = this.a.compareTo(jVar.a);
        if (iCompareTo != 0) {
            return iCompareTo;
        }
        int i = this.c - jVar.c;
        if (i != 0) {
            return i;
        }
        int i2 = this.b - jVar.b;
        if (i2 != 0) {
            return i2;
        }
        byte[] bArrH = h();
        byte[] bArrH2 = jVar.h();
        for (int i3 = 0; i3 < bArrH.length && i3 < bArrH2.length; i3++) {
            int i4 = (bArrH[i3] & UByte.MAX_VALUE) - (bArrH2[i3] & UByte.MAX_VALUE);
            if (i4 != 0) {
                return i4;
            }
        }
        return bArrH.length - bArrH2.length;
    }

    public final int d() {
        return this.b;
    }

    public final int e() {
        return this.c;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof j)) {
            j jVar = (j) obj;
            if (this.b == jVar.b && this.c == jVar.c && this.a.equals(jVar.a)) {
                return Arrays.equals(h(), jVar.h());
            }
        }
        return false;
    }

    public final long f() {
        return this.d;
    }

    final j g() {
        try {
            return (j) clone();
        } catch (CloneNotSupportedException unused) {
            throw new IllegalStateException();
        }
    }

    public int hashCode() {
        d dVar = new d();
        this.a.a(dVar);
        dVar.c(this.b);
        dVar.c(this.c);
        dVar.a(0L);
        int iA = dVar.a();
        dVar.c(0);
        a(dVar, true);
        dVar.a((dVar.a() - iA) - 2, iA);
        int i = 0;
        for (byte b : dVar.b()) {
            i += (i << 3) + (b & UByte.MAX_VALUE);
        }
        return i;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.a);
        if (stringBuffer.length() < 8) {
            stringBuffer.append("\t");
        }
        if (stringBuffer.length() < 16) {
            stringBuffer.append("\t");
        }
        stringBuffer.append("\t");
        String strA = a();
        if (!strA.equals("")) {
            stringBuffer.append("\t");
            stringBuffer.append(strA);
        }
        return stringBuffer.toString();
    }
}
