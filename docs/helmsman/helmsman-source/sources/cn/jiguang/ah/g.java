package cn.jiguang.ah;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class g {
    private LinkedHashSet<h> a = new LinkedHashSet<>();
    private LinkedHashSet<h> b = new LinkedHashSet<>();
    private List<Boolean> c = new ArrayList();

    /* JADX WARN: Code duplicated, block: B:33:0x005c  */
    private h a(boolean z, boolean z2) {
        LinkedHashSet<h> linkedHashSet;
        Object objA;
        LinkedHashSet<h> linkedHashSet2 = z ? this.b : this.a;
        if (z2) {
            linkedHashSet = null;
        } else {
            linkedHashSet = z ? this.a : this.b;
        }
        if (linkedHashSet == null || linkedHashSet.isEmpty()) {
            objA = a(linkedHashSet2);
        } else {
            if (linkedHashSet2 != null && !linkedHashSet2.isEmpty()) {
                boolean z3 = false;
                if (this.c.size() >= 2) {
                    int size = this.c.size() - 1;
                    while (true) {
                        if (size < this.c.size() - 2) {
                            z3 = true;
                            break;
                        }
                        if (this.c.get(size).booleanValue() != z) {
                            break;
                        }
                        size--;
                    }
                }
                if (!z3) {
                    objA = a(linkedHashSet2);
                }
            }
            objA = a(linkedHashSet);
        }
        return (h) objA;
    }

    private static <T> T a(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        Iterator<T> it = collection.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        collection.remove(next);
        return next;
    }

    public final h a(int i) {
        h hVarA;
        cn.jiguang.ad.d.b("IpPool", "current ipv4List=" + this.a + " ipv6List=" + this.b);
        if (i == 0) {
            hVarA = a(false, false);
        } else if (i == 1) {
            hVarA = a(false, true);
        } else if (i != 2) {
            hVarA = i != 3 ? null : a(true, false);
        } else {
            hVarA = a(true, true);
        }
        cn.jiguang.ad.d.b("IpPool", "get ipPort=" + hVarA);
        if (hVarA != null) {
            if (hVarA.c instanceof Inet4Address) {
                this.c.add(false);
            } else if (hVarA.c instanceof Inet6Address) {
                this.c.add(true);
            }
        }
        return hVarA;
    }

    public final void a() {
        try {
            this.a.clear();
            this.b.clear();
        } catch (Throwable unused) {
        }
    }

    public final boolean a(h hVar) {
        LinkedHashSet<h> linkedHashSet;
        if (!hVar.a()) {
            return false;
        }
        if (hVar.c instanceof Inet4Address) {
            linkedHashSet = this.a;
        } else {
            if (!(hVar.c instanceof Inet6Address)) {
                return false;
            }
            linkedHashSet = this.b;
        }
        return linkedHashSet.add(hVar);
    }
}
