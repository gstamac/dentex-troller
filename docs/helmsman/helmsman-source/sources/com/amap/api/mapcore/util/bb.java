package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: OfflineDBOperation.java */
/* JADX INFO: loaded from: classes.dex */
public class bb {
    private static volatile bb a;
    private static ft b;
    private Context c;

    public static bb a(Context context) {
        if (a == null) {
            synchronized (bb.class) {
                if (a == null) {
                    a = new bb(context);
                }
            }
        }
        return a;
    }

    private bb(Context context) {
        this.c = context;
        b = b(context);
    }

    private ft b(Context context) {
        try {
            return new ft(context, ba.a());
        } catch (Throwable th) {
            fn.b(th, "OfflineDB", "getDB");
            th.printStackTrace();
            return null;
        }
    }

    private boolean b() {
        if (b == null) {
            b = b(this.c);
        }
        return b != null;
    }

    public ArrayList<aw> a() {
        ArrayList<aw> arrayList = new ArrayList<>();
        if (!b()) {
            return arrayList;
        }
        Iterator it = b.b("", aw.class).iterator();
        while (it.hasNext()) {
            arrayList.add((aw) it.next());
        }
        return arrayList;
    }

    public synchronized aw a(String str) {
        if (!b()) {
            return null;
        }
        List listB = b.b(aw.e(str), aw.class);
        if (listB.size() <= 0) {
            return null;
        }
        return (aw) listB.get(0);
    }

    public synchronized void a(aw awVar) {
        if (b()) {
            b.a(awVar, aw.f(awVar.h()));
            a(awVar.f(), awVar.b());
        }
    }

    private void a(String str, String str2) {
        if (str2 == null || str2.length() <= 0) {
            return;
        }
        String strA = ay.a(str);
        if (b.b(strA, ay.class).size() > 0) {
            b.a(strA, ay.class);
        }
        String[] strArrSplit = str2.split(";");
        ArrayList arrayList = new ArrayList();
        for (String str3 : strArrSplit) {
            arrayList.add(new ay(str, str3));
        }
        b.a((List) arrayList);
    }

    public synchronized List<String> b(String str) {
        ArrayList arrayList = new ArrayList();
        if (!b()) {
            return arrayList;
        }
        arrayList.addAll(a(b.b(ay.a(str), ay.class)));
        return arrayList;
    }

    private List<String> a(List<ay> list) {
        ArrayList arrayList = new ArrayList();
        if (list.size() > 0) {
            Iterator<ay> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().a());
            }
        }
        return arrayList;
    }

    public synchronized void c(String str) {
        if (b()) {
            b.a(az.e(str), az.class);
            b.a(ay.a(str), ay.class);
            b.a(ax.a(str), ax.class);
        }
    }

    public synchronized void b(aw awVar) {
        if (b()) {
            b.a(az.f(awVar.h()), az.class);
            b.a(ay.a(awVar.f()), ay.class);
            b.a(ax.a(awVar.f()), ax.class);
        }
    }

    public void a(String str, int i, long j, long j2, long j3) {
        if (b()) {
            a(str, i, j, new long[]{j2, 0, 0, 0, 0}, new long[]{j3, 0, 0, 0, 0});
        }
    }

    public synchronized void a(String str, int i, long j, long[] jArr, long[] jArr2) {
        if (b()) {
            b.a(new ax(str, j, i, jArr[0], jArr2[0]), ax.a(str));
        }
    }

    public synchronized String d(String str) {
        if (!b()) {
            return null;
        }
        List listB = b.b(az.f(str), az.class);
        return listB.size() > 0 ? ((az) listB.get(0)).e() : null;
    }
}
