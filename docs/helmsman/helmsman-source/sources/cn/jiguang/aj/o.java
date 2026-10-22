package cn.jiguang.aj;

import android.content.Context;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class o {
    private static volatile o a = null;
    private static final Object b = new Object();
    private static long e = 1;
    private cn.jiguang.an.a d = new p(this);
    private Map<Long, d> c = new HashMap();

    private o() {
    }

    public static o a() {
        if (a == null) {
            synchronized (b) {
                if (a == null) {
                    a = new o();
                }
            }
        }
        return a;
    }

    private static byte[] a(Context context, d dVar) {
        return cn.jiguang.al.b.a(context, dVar.d, dVar.e, dVar.f, dVar.g, 0L);
    }

    public static long b() {
        long j = e + 1;
        e = j;
        if (j >= 2147483647L) {
            e = 1L;
        }
        return e;
    }

    public final void a(long j) {
        d dVarRemove = this.c.remove(Long.valueOf(j));
        if (dVarRemove != null) {
            if (dVarRemove.j) {
                cn.jiguang.an.b.a().b((int) (j + 100000));
            }
            cn.jiguang.ad.d.b("TcpRequestManager", "handle reponse :" + dVarRemove);
        }
    }

    public final void a(Context context) {
        if (this.c.isEmpty()) {
            cn.jiguang.ad.d.b("TcpRequestManager", "no cache request");
            return;
        }
        for (Map.Entry<Long, d> entry : this.c.entrySet()) {
            if (entry.getValue().j) {
                long jNanoTime = System.nanoTime() - entry.getValue().h;
                if (entry.getValue().i - jNanoTime >= OkHttpUtils.DEFAULT_MILLISECONDS) {
                    entry.getValue().a++;
                    cn.jiguang.ad.d.b("TcpRequestManager", "send again:" + entry.getValue());
                    i.a().c().c().a(a(context, entry.getValue()));
                } else {
                    cn.jiguang.ad.d.b("TcpRequestManager", "shoud not send again by 10000ms,hasRequestTime:" + jNanoTime + ",timeout:" + entry.getValue().i);
                }
            }
        }
    }

    public final void a(Context context, long j) {
        d dVarRemove = this.c.remove(Long.valueOf(j));
        if (dVarRemove == null) {
            cn.jiguang.ad.d.f("TcpRequestManager", "not found requst by rid:" + j);
            return;
        }
        cn.jiguang.ad.d.b("TcpRequestManager", "request time out:" + dVarRemove);
        b.a();
        b.a(context, dVarRemove.c, dVarRemove.b, dVarRemove.d);
    }

    public final void a(Context context, long j, int i, int i2, byte[] bArr, String str) {
        long jB = cn.jiguang.ah.c.b(context);
        if (this.c.containsKey(Long.valueOf(jB))) {
            cn.jiguang.ad.d.g("TcpRequestManager", "Generator same rid,not do this msg");
            return;
        }
        d dVar = new d(j, str, i, i2, jB, 0L, bArr);
        if (i.a().d()) {
            i.a().c().c().a(a(context, dVar));
        }
        this.c.put(Long.valueOf(jB), dVar);
    }

    public final void a(Context context, long j, int i, int i2, byte[] bArr, String str, long j2) {
        long j3;
        if (i == 10) {
            j3 = j;
        } else {
            long jB = cn.jiguang.ah.c.b(context);
            cn.jiguang.ad.d.b("TcpRequestManager", "Generator new rid:" + jB);
            if (this.c.containsKey(Long.valueOf(jB))) {
                cn.jiguang.ad.d.g("TcpRequestManager", "Generator same rid,not do this msg");
                return;
            }
            j3 = jB;
        }
        long j4 = j2 <= 0 ? 10000L : j2;
        d dVar = new d(j, str, i, i2, j3, j4, bArr);
        if (i.a().d()) {
            i.a().c().c().a(a(context, dVar));
        }
        dVar.h = System.nanoTime();
        this.c.put(Long.valueOf(j3), dVar);
        cn.jiguang.an.b.a().b((int) (j3 + 100000), j4, this.d);
    }

    public final d b(long j) {
        return this.c.get(Long.valueOf(j));
    }
}
