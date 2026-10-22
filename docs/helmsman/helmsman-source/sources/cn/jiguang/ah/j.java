package cn.jiguang.ah;

import cn.jiguang.internal.JConstants;
import com.MAVLink.enums.MAV_CMD;
import com.zhy.http.okhttp.OkHttpUtils;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
public final class j implements Callable<n> {
    private LinkedHashSet<h> a = new LinkedHashSet<>();
    private l b;

    public j(l lVar) {
        this.b = lVar;
    }

    private void a(r rVar, g gVar) {
        LinkedHashSet<h> linkedHashSetB = h.b((String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.O()));
        if (linkedHashSetB.isEmpty()) {
            linkedHashSetB = h.a(cn.jiguang.ag.a.b());
        }
        linkedHashSetB.removeAll(this.a);
        LinkedList linkedListA = cn.jiguang.f.k.a(linkedHashSetB);
        cn.jiguang.ad.d.b("Sis", "main sis: default sis" + linkedListA);
        Iterator it = linkedListA.iterator();
        while (it.hasNext()) {
            if (a((h) it.next(), rVar, gVar)) {
                return;
            }
        }
        LinkedHashSet<h> linkedHashSetA = cn.jiguang.ai.m.a().a(cn.jiguang.ag.a.c(), OkHttpUtils.DEFAULT_MILLISECONDS);
        linkedListA.clear();
        if (linkedHashSetA != null) {
            linkedHashSetA.removeAll(this.a);
            linkedListA = cn.jiguang.f.k.a(linkedHashSetA);
        }
        cn.jiguang.ad.d.b("Sis", "main sis: sis srv" + linkedListA);
        Iterator it2 = linkedListA.iterator();
        while (it2.hasNext() && !a((h) it2.next(), rVar, gVar)) {
        }
    }

    private boolean a(h hVar, r rVar, g gVar) {
        InetAddress[] inetAddressArrA;
        if (rVar.a()) {
            return true;
        }
        if (hVar != null && hVar.a() && !this.a.contains(hVar) && (inetAddressArrA = cn.jiguang.ak.a.a().a(this.b.a, hVar.a, 3000L, false)) != null && inetAddressArrA.length != 0) {
            for (InetAddress inetAddress : cn.jiguang.f.k.a(Arrays.asList(inetAddressArrA))) {
                if (rVar.a()) {
                    return true;
                }
                h hVar2 = new h(inetAddress, hVar.b);
                if (!this.a.contains(hVar2) && gVar.a(hVar2)) {
                    this.a.add(hVar2);
                    rVar.a((Callable<?>) new q(this.b, rVar, gVar));
                    break;
                }
            }
            this.a.add(hVar);
        }
        return false;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ n call() {
        g gVar = new g();
        r rVar = new r(5, MAV_CMD.MAV_CMD_IMAGE_START_CAPTURE, null);
        LinkedHashSet<h> linkedHashSetA = h.a(cn.jiguang.ag.a.a());
        linkedHashSetA.removeAll(this.a);
        LinkedList linkedListA = cn.jiguang.f.k.a(linkedHashSetA);
        cn.jiguang.ad.d.b("Sis", "main sis: sis host=" + linkedListA);
        Iterator it = linkedListA.iterator();
        do {
            if (!it.hasNext()) {
                h hVarA = h.a((String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.b(true)));
                cn.jiguang.ad.d.b("Sis", "main sis: last good sis v4 address=" + hVarA);
                if (!a(hVarA, rVar, gVar)) {
                    h hVarA2 = h.a((String) cn.jiguang.g.b.a(this.b.a, cn.jiguang.g.a.b(false)));
                    cn.jiguang.ad.d.b("Sis", "main sis: last good sis v6 address=" + hVarA2);
                    a(hVarA2, rVar, gVar);
                    break;
                }
                break;
            }
        } while (!a((h) it.next(), rVar, gVar));
        cn.jiguang.ad.d.b("Sis", "main sis: after host and last good, wait Result");
        Object objA = rVar.a(JConstants.MIN);
        if (objA instanceof n) {
            return (n) objA;
        }
        a(rVar, gVar);
        cn.jiguang.ad.d.b("Sis", "main sis: after default and srv, wait Result");
        Object objA2 = rVar.a(JConstants.MIN);
        if (objA2 instanceof n) {
            return (n) objA2;
        }
        return null;
    }
}
