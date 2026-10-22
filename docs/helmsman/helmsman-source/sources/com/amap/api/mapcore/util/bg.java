package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMapException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/* JADX INFO: compiled from: NetFileFetch.java */
/* JADX INFO: loaded from: classes.dex */
public class bg implements gy.a {
    bh a;
    long d;
    bb f;
    a h;
    private Context i;
    private bl j;
    private String k;
    private gy l;
    private bc m;
    long b = 0;
    long c = 0;
    boolean e = true;
    long g = 0;

    /* JADX INFO: compiled from: NetFileFetch.java */
    public interface a {
        void d();
    }

    public bg(bh bhVar, String str, Context context, bl blVar) throws IOException {
        this.a = null;
        this.f = bb.a(context.getApplicationContext());
        this.a = bhVar;
        this.i = context;
        this.k = str;
        this.j = blVar;
        g();
    }

    private void f() throws IOException {
        bm bmVar = new bm(this.k);
        bmVar.a(1800000);
        bmVar.b(1800000);
        this.l = new gy(bmVar, this.b, this.c);
        this.m = new bc(this.a.b() + File.separator + this.a.c(), this.b);
    }

    private void g() {
        File file = new File(this.a.b() + this.a.c());
        if (file.exists()) {
            this.e = false;
            this.b = file.length();
            try {
                long jB = b();
                this.d = jB;
                this.c = jB;
                return;
            } catch (IOException unused) {
                bl blVar = this.j;
                if (blVar != null) {
                    blVar.a(bl.a.file_io_exception);
                    return;
                }
                return;
            }
        }
        this.b = 0L;
        this.c = 0L;
    }

    public void a() {
        try {
            if (dw.c(this.i)) {
                i();
                if (ey.a != 1) {
                    bl blVar = this.j;
                    if (blVar != null) {
                        blVar.a(bl.a.amap_exception);
                        return;
                    }
                    return;
                }
                if (!h()) {
                    this.e = true;
                }
                if (this.e) {
                    long jB = b();
                    this.d = jB;
                    if (jB == -1) {
                        bj.a("File Length is not known!");
                    } else if (jB == -2) {
                        bj.a("File is not access!");
                    } else {
                        this.c = jB;
                    }
                    this.b = 0L;
                }
                bl blVar2 = this.j;
                if (blVar2 != null) {
                    blVar2.n();
                }
                if (this.b >= this.c) {
                    e();
                    return;
                } else {
                    f();
                    this.l.a(this);
                    return;
                }
            }
            bl blVar3 = this.j;
            if (blVar3 != null) {
                blVar3.a(bl.a.network_exception);
            }
        } catch (AMapException e) {
            fn.b(e, "SiteFileFetch", "download");
            bl blVar4 = this.j;
            if (blVar4 != null) {
                blVar4.a(bl.a.amap_exception);
            }
        } catch (IOException unused) {
            bl blVar5 = this.j;
            if (blVar5 != null) {
                blVar5.a(bl.a.file_io_exception);
            }
        }
    }

    private boolean h() {
        return new File(new StringBuilder().append(this.a.b()).append(File.separator).append(this.a.c()).toString()).length() >= 10;
    }

    private void i() throws AMapException {
        if (ey.a != 1) {
            for (int i = 0; i < 3; i++) {
                try {
                    if (ey.a(this.i, dw.e())) {
                        return;
                    }
                } catch (Throwable th) {
                    fn.b(th, "SiteFileFetch", "authOffLineDownLoad");
                    th.printStackTrace();
                }
            }
        }
    }

    public long b() throws IOException {
        int i;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.a.a()).openConnection();
        httpURLConnection.setRequestProperty("User-Agent", g.d);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode >= 400) {
            a(responseCode);
            return -2L;
        }
        int i2 = 1;
        while (true) {
            String headerFieldKey = httpURLConnection.getHeaderFieldKey(i2);
            if (headerFieldKey == null) {
                i = -1;
                break;
            }
            if (headerFieldKey.equalsIgnoreCase("Content-Length")) {
                i = Integer.parseInt(httpURLConnection.getHeaderField(headerFieldKey));
                break;
            }
            i2++;
        }
        return i;
    }

    private void j() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.a == null || jCurrentTimeMillis - this.g <= 500) {
            return;
        }
        k();
        this.g = jCurrentTimeMillis;
        a(this.b);
    }

    private void k() {
        this.f.a(this.a.e(), this.a.d(), this.d, this.b, this.c);
    }

    private void a(long j) {
        bl blVar;
        long j2 = this.d;
        if (j2 <= 0 || (blVar = this.j) == null) {
            return;
        }
        blVar.a(j2, j);
        this.g = System.currentTimeMillis();
    }

    private void a(int i) {
        System.err.println("Error Code : " + i);
    }

    public void c() {
        gy gyVar = this.l;
        if (gyVar != null) {
            gyVar.a();
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void d() {
        bl blVar = this.j;
        if (blVar != null) {
            blVar.p();
        }
        k();
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void e() {
        j();
        bl blVar = this.j;
        if (blVar != null) {
            blVar.o();
        }
        bc bcVar = this.m;
        if (bcVar != null) {
            bcVar.a();
        }
        a aVar = this.h;
        if (aVar != null) {
            aVar.d();
        }
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void a(Throwable th) {
        bc bcVar;
        bl blVar = this.j;
        if (blVar != null) {
            blVar.a(bl.a.network_exception);
        }
        if ((th instanceof IOException) || (bcVar = this.m) == null) {
            return;
        }
        bcVar.a();
    }

    @Override // com.amap.api.mapcore.util.gy.a
    public void a(byte[] bArr, long j) {
        try {
            this.m.a(bArr);
            this.b = j;
            j();
        } catch (IOException e) {
            e.printStackTrace();
            fn.b(e, "fileAccessI", "fileAccessI.write(byte[] data)");
            bl blVar = this.j;
            if (blVar != null) {
                blVar.a(bl.a.file_io_exception);
            }
            gy gyVar = this.l;
            if (gyVar != null) {
                gyVar.a();
            }
        }
    }

    public void a(a aVar) {
        this.h = aVar;
    }
}
