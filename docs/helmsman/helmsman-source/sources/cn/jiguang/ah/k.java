package cn.jiguang.ah;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import com.MAVLink.enums.MAV_CMD;
import com.zhy.http.okhttp.OkHttpUtils;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class k {
    private l b;
    private i c;
    private q f;
    private LinkedHashSet<h> a = new LinkedHashSet<>();
    private r d = new r(5, MAV_CMD.MAV_CMD_IMAGE_START_CAPTURE, null);
    private g e = new g();

    public k(l lVar) {
        this.b = lVar;
    }

    private void a() {
        h hVarA = h.a((String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.c(true)));
        cn.jiguang.ad.d.b("SisConn", "connect: use last good v4 address=" + hVarA);
        if (a(hVarA)) {
            return;
        }
        h hVarA2 = h.a((String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.c(false)));
        cn.jiguang.ad.d.b("SisConn", "connect: use last good v6 address=" + hVarA2);
        if (a(hVarA2)) {
            return;
        }
        LinkedHashSet<h> linkedHashSetB = b();
        if (linkedHashSetB != null) {
            linkedHashSetB.removeAll(this.a);
        }
        LinkedList linkedListA = cn.jiguang.f.k.a(linkedHashSetB);
        cn.jiguang.ad.d.b("SisConn", "connect: use defaultConn=" + linkedListA);
        Iterator it = linkedListA.iterator();
        while (it.hasNext()) {
            if (a((h) it.next())) {
                return;
            }
        }
        LinkedHashSet<h> linkedHashSetA = cn.jiguang.ai.m.a().a(cn.jiguang.ag.a.b(this.b.a), OkHttpUtils.DEFAULT_MILLISECONDS);
        linkedListA.clear();
        if (linkedHashSetA != null) {
            linkedHashSetA.removeAll(this.a);
            linkedListA = cn.jiguang.f.k.a(linkedHashSetA);
        }
        cn.jiguang.ad.d.b("SisConn", "connect: use srv address" + linkedListA);
        Iterator it2 = linkedListA.iterator();
        while (it2.hasNext() && !a((h) it2.next())) {
        }
    }

    private void a(LinkedHashSet<h> linkedHashSet) {
        linkedHashSet.removeAll(this.a);
        if (linkedHashSet.isEmpty()) {
            return;
        }
        LinkedHashSet<h> linkedHashSetA = e.a(this.b.a, linkedHashSet, 0L);
        cn.jiguang.ad.d.b("SisConn", "connect: last good sis info" + linkedHashSetA);
        Iterator<h> it = linkedHashSetA.iterator();
        while (it.hasNext() && !a(it.next())) {
        }
    }

    private void a(LinkedHashSet<h> linkedHashSet, long j) {
        InetAddress[] inetAddressArrA;
        this.e.a();
        this.d.b();
        linkedHashSet.removeAll(this.a);
        LinkedHashSet<h> linkedHashSetA = e.a(this.b.a, linkedHashSet, System.currentTimeMillis());
        cn.jiguang.ad.d.b("SisConn", "connect: new sis info=" + linkedHashSetA);
        if (linkedHashSetA.isEmpty()) {
            return;
        }
        for (h hVar : linkedHashSetA) {
            if (SystemClock.uptimeMillis() >= j || a(hVar)) {
                return;
            }
        }
        cn.jiguang.ad.d.b("SisConn", "after connect use new sis, wait connect Result");
        long jUptimeMillis = j - SystemClock.uptimeMillis();
        if (jUptimeMillis > 0 && this.d.a(jUptimeMillis) == null && this.f == null) {
            HashSet hashSet = new HashSet();
            for (h hVar2 : linkedHashSetA) {
                if (this.a.contains(hVar2)) {
                    hashSet.add(hVar2.a);
                }
            }
            int iA = this.b.a();
            boolean z = true;
            if (iA != 1 && iA != 0) {
                z = false;
            }
            h hVarA = h.a((String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.b(z)));
            if (hVarA == null || (inetAddressArrA = cn.jiguang.ak.a.a().a(this.b.a, hVarA.a, 3000L, false)) == null || inetAddressArrA.length <= 0) {
                return;
            }
            hVarA.c = inetAddressArrA[0];
            long jUptimeMillis2 = j - SystemClock.uptimeMillis();
            if (jUptimeMillis2 < 10) {
                return;
            }
            this.f = new q(this.b, hVarA, hashSet);
            cn.jiguang.ad.d.b("SisConn", "second sis, addr=" + hVarA + ", failIps=" + hashSet);
            FutureTask futureTask = new FutureTask(this.f);
            try {
                JCoreManager.onEvent(null, null, 11, "ASYNC", null, futureTask);
                n nVar = (n) futureTask.get(jUptimeMillis2, TimeUnit.MILLISECONDS);
                LinkedHashSet<h> linkedHashSet2 = nVar != null ? nVar.a : null;
                if (linkedHashSet2 == null || linkedHashSet2.isEmpty()) {
                    return;
                }
                this.b.a(nVar.g);
                a(linkedHashSet2, j);
            } catch (Throwable th) {
                cn.jiguang.ad.d.f("SisConn", "second sis e:" + th);
            }
        }
    }

    private boolean a(h hVar) {
        InetAddress[] inetAddressArrA;
        cn.jiguang.aj.f fVar;
        if (!this.c.a) {
            if (this.d.a()) {
                return true;
            }
            if (hVar != null && hVar.a() && !this.a.contains(hVar) && (inetAddressArrA = cn.jiguang.ak.a.a().a(this.b.a, hVar.a, 3000L, false)) != null && inetAddressArrA.length != 0) {
                for (InetAddress inetAddress : cn.jiguang.f.k.a(Arrays.asList(inetAddressArrA))) {
                    if (this.c.a) {
                        fVar = new cn.jiguang.aj.f(-991, null);
                    } else {
                        if (this.d.a()) {
                            return true;
                        }
                        h hVar2 = new h(inetAddress, hVar.b);
                        if (!this.a.contains(hVar2) && this.e.a(hVar2)) {
                            this.a.add(hVar2);
                            this.d.a((Callable<?>) new b(this.b, this.d, this.e, this.c));
                            break;
                        }
                    }
                }
                this.a.add(hVar);
            }
            return false;
        }
        fVar = new cn.jiguang.aj.f(-991, null);
        this.d.a(fVar);
        return true;
    }

    private LinkedHashSet<h> b() {
        InetAddress[] inetAddressArrA;
        try {
            String strA = cn.jiguang.ag.a.a(this.b.a);
            cn.jiguang.ad.d.b("SisConn", "load Default Conn, from host=" + strA);
            if (!TextUtils.isEmpty(strA) && (inetAddressArrA = cn.jiguang.ak.a.a().a(this.b.a, strA, 3000L, false)) != null && inetAddressArrA.length != 0) {
                LinkedList linkedListA = cn.jiguang.f.k.a(Arrays.asList(inetAddressArrA));
                if (linkedListA.isEmpty()) {
                    return null;
                }
                String hostAddress = ((InetAddress) linkedListA.get(0)).getHostAddress();
                LinkedHashSet<h> linkedHashSet = new LinkedHashSet<>();
                linkedHashSet.add(new h(hostAddress, 7000));
                linkedHashSet.add(new h(hostAddress, 7002));
                linkedHashSet.add(new h(hostAddress, 7003));
                linkedHashSet.add(new h(hostAddress, 7004));
                linkedHashSet.add(new h(hostAddress, 7005));
                linkedHashSet.add(new h(hostAddress, 7006));
                linkedHashSet.add(new h(hostAddress, 7007));
                linkedHashSet.add(new h(hostAddress, 7008));
                linkedHashSet.add(new h(hostAddress, 7009));
                return linkedHashSet;
            }
        } catch (Throwable unused) {
        }
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:4:0x0042  */
    public final cn.jiguang.am.a a(i iVar) throws Exception {
        boolean z;
        this.c = iVar;
        cn.jiguang.ad.d.b("SisConn", "start sisAndConnect...");
        this.e = new g();
        long jUptimeMillis = SystemClock.uptimeMillis() + 12000;
        LinkedHashSet<h> linkedHashSetB = h.b((String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.J()));
        boolean z2 = !linkedHashSetB.isEmpty();
        String strC = cn.jiguang.f.j.c(this.b.a);
        Context context = this.b.a;
        boolean z3 = false;
        if (TextUtils.isEmpty(strC)) {
            z = true;
        } else {
            String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.F());
            cn.jiguang.ad.d.b("SisConn", "newType=" + strC + " last=" + str);
            if (strC.equalsIgnoreCase(str)) {
                z = false;
            } else {
                z = true;
            }
        }
        long jLongValue = ((Long) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.M())).longValue();
        if (!z && !cn.jiguang.f.k.a(jLongValue, 180000L)) {
            z3 = true;
        }
        if (z2 && z3) {
            a(linkedHashSetB);
        }
        n nVarA = this.b.a(12000L);
        LinkedHashSet<h> linkedHashSet = nVarA != null ? nVarA.a : null;
        if (linkedHashSet != null && !linkedHashSet.isEmpty()) {
            this.b.a(nVarA.g);
            a(linkedHashSet, jUptimeMillis);
        } else if (z2 && !z3) {
            a(linkedHashSetB);
        }
        a();
        cn.jiguang.ad.d.b("SisConn", "wait final result...");
        Object objA = this.d.a(JConstants.MIN);
        if (objA instanceof cn.jiguang.am.a) {
            cn.jiguang.ad.d.b("SisConn", "connect succeed");
            return (cn.jiguang.am.a) objA;
        }
        if (objA instanceof Exception) {
            cn.jiguang.ad.d.b("SisConn", "all sis and connect failed, e:" + objA);
            throw ((Exception) objA);
        }
        cn.jiguang.ad.d.f("SisConn", "all sis and connect failed:" + objA);
        throw new cn.jiguang.aj.f(1, null);
    }
}
