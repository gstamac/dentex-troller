package cn.jiguang.ah;

import android.content.Context;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public final class e {
    private static final Comparator<d> a = new f();

    public static LinkedHashSet<h> a(Context context, LinkedHashSet<h> linkedHashSet, long j) {
        if ((linkedHashSet != null ? linkedHashSet.size() : 0) == 0) {
            return new LinkedHashSet<>();
        }
        LinkedList linkedList = new LinkedList();
        boolean z = true;
        for (h hVar : linkedHashSet) {
            if (hVar.a()) {
                cn.jiguang.g.a<String> aVarF = cn.jiguang.g.a.f(hVar.toString());
                d dVarA = d.a((String) cn.jiguang.g.b.a(context, aVarF));
                if (dVarA == null) {
                    dVarA = new d(hVar);
                }
                if (z) {
                    dVarA.e = 1;
                    z = false;
                }
                if (j > 0) {
                    dVarA.c = j;
                    cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{aVarF.a(dVarA.a())});
                }
                linkedList.add(dVarA);
            }
        }
        LinkedList linkedListA = a(linkedList, a);
        LinkedHashSet<h> linkedHashSet2 = new LinkedHashSet<>();
        Iterator it = linkedListA.iterator();
        while (it.hasNext()) {
            Iterator it2 = ((LinkedList) it.next()).iterator();
            while (it2.hasNext()) {
                linkedHashSet2.add(((d) it2.next()).a);
            }
        }
        return linkedHashSet2;
    }

    private static <T> LinkedList<LinkedList<T>> a(Collection<T> collection, Comparator<T> comparator) {
        int size = collection.size();
        if (size == 0) {
            return new LinkedList<>();
        }
        LinkedList<LinkedList<T>> linkedList = new LinkedList<>();
        if (size == 1) {
            linkedList.add(new LinkedList<>(collection));
            return linkedList;
        }
        SecureRandom secureRandom = new SecureRandom();
        for (T t : collection) {
            boolean z = false;
            for (int i = 0; i < linkedList.size(); i++) {
                LinkedList<T> linkedList2 = linkedList.get(i);
                int iCompare = comparator.compare(t, linkedList2.getFirst());
                if (iCompare == 0) {
                    linkedList2.add(secureRandom.nextInt(linkedList2.size() + 1), t);
                } else if (iCompare < 0) {
                    LinkedList<T> linkedList3 = new LinkedList<>();
                    linkedList3.add(t);
                    linkedList.add(i, linkedList3);
                }
                z = true;
            }
            if (!z) {
                LinkedList<T> linkedList4 = new LinkedList<>();
                linkedList4.add(t);
                linkedList.add(linkedList4);
            }
        }
        return linkedList;
    }

    public static void a(Context context, h hVar, int i, long j) {
        if (hVar != null) {
            cn.jiguang.g.a<String> aVarF = cn.jiguang.g.a.f(hVar.toString());
            d dVarA = d.a((String) cn.jiguang.g.b.a(context, aVarF));
            if (dVarA == null) {
                dVarA = new d(hVar);
            }
            dVarA.d = j;
            dVarA.b = i;
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{aVarF.a(dVarA.a())});
        }
    }
}
