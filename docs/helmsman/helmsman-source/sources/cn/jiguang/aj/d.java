package cn.jiguang.aj;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    int a = 0;
    public long b;
    public String c;
    int d;
    int e;
    long f;
    byte[] g;
    long h;
    long i;
    boolean j;

    public d(long j, String str, int i, int i2, long j2, long j3, byte[] bArr) {
        this.b = j;
        this.c = str;
        this.d = i;
        this.e = i2;
        this.f = j2;
        this.i = j3;
        this.g = bArr;
        if (j3 > 0) {
            this.j = true;
        }
    }

    public final String toString() {
        return "InnerRequest{times=" + this.a + ", requestId=" + this.b + ", sdkType='" + this.c + "', command=" + this.d + ", ver=" + this.e + ", rid=" + this.f + ", reqeustTime=" + this.h + ", timeout=" + this.i + '}';
    }
}
