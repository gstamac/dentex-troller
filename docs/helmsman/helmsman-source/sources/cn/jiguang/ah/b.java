package cn.jiguang.ah;

import android.os.SystemClock;
import android.text.TextUtils;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public final class b implements Callable<cn.jiguang.am.a> {
    private final l a;
    private final r b;
    private final g c;
    private final i d;

    public b(l lVar, r rVar, g gVar, i iVar) {
        this.b = rVar;
        this.a = lVar;
        this.c = gVar;
        this.d = iVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public cn.jiguang.am.a call() {
        try {
            if (this.c != null) {
                h hVarA = this.c.a(this.a.a());
                if (this.b.a()) {
                    return null;
                }
                i iVar = this.d;
                if (iVar != null && !iVar.a) {
                    if (hVarA == null) {
                        return null;
                    }
                    if (!TextUtils.isEmpty(cn.jiguang.ag.a.c)) {
                        hVarA.a = cn.jiguang.ag.a.c;
                        hVarA.c = InetAddress.getByName(cn.jiguang.ag.a.c);
                    }
                    if (cn.jiguang.ag.a.d > 0) {
                        hVarA.b = cn.jiguang.ag.a.d;
                    }
                    cn.jiguang.ad.d.c("ConnTask", "Open connection with ip=" + hVarA.c + ", port:" + hVarA.b);
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    cn.jiguang.am.b bVar = new cn.jiguang.am.b(8128, 20);
                    int iA = bVar.a(hVarA.a, hVarA.b);
                    if (this.b.a()) {
                        cn.jiguang.f.k.a(bVar);
                        return null;
                    }
                    if (this.d.a) {
                        this.b.a(new cn.jiguang.aj.f(-991, null));
                        cn.jiguang.f.k.a(bVar);
                        return null;
                    }
                    if (iA == 0) {
                        cn.jiguang.g.b.a(this.a.a, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.c((hVarA.c instanceof Inet4Address) || cn.jiguang.f.i.f(hVarA.a)).a(hVarA.toString())});
                        cn.jiguang.ad.d.e("ConnTask", "Succeed to open connection - ip:" + hVarA.c + ", port:" + hVarA.b);
                        this.b.a(bVar);
                        e.a(this.a.a, hVarA, 1, 0L);
                        return bVar;
                    }
                    long jUptimeMillis2 = SystemClock.uptimeMillis() - jUptimeMillis;
                    this.a.a(2, hVarA.a, hVarA.b, cn.jiguang.d.b.b(this.a.a), jUptimeMillis2, iA);
                    cn.jiguang.ad.d.d("ConnTask", "Failed(" + iA + ") to open connection - ip:" + hVarA.c + ", port:" + hVarA.b + ", cost:" + jUptimeMillis2);
                    e.a(this.a.a, hVarA, -1, jUptimeMillis2);
                    cn.jiguang.f.k.a(bVar);
                    return null;
                }
                this.b.a(new cn.jiguang.aj.f(-991, null));
                return null;
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("ConnTask", "run e:" + th);
        }
        return null;
    }
}
