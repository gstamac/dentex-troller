package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: DownloadManager.java */
/* JADX INFO: loaded from: classes.dex */
public class gy {
    private gz a;
    private hc b;

    /* JADX INFO: compiled from: DownloadManager.java */
    public interface a {
        void a(Throwable th);

        void a(byte[] bArr, long j);

        void d();

        void e();
    }

    public gy(hc hcVar) {
        this(hcVar, 0L, -1L);
    }

    public gy(hc hcVar, long j, long j2) {
        this.b = hcVar;
        gz gzVar = new gz(this.b.f, this.b.g, hcVar.h == null ? null : hcVar.h);
        this.a = gzVar;
        gzVar.b(j2);
        this.a.a(j);
    }

    public void a(a aVar) {
        this.a.a(this.b.c(), this.b.a(), this.b.b(), aVar);
    }

    public void a() {
        this.a.a();
    }
}
