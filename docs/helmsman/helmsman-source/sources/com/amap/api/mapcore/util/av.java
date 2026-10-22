package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.maps.AMap;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: compiled from: TaskManager.java */
/* JADX INFO: loaded from: classes.dex */
public class av {
    private static av a;
    private hl b;
    private LinkedHashMap<String, hm> c = new LinkedHashMap<>();
    private boolean d = true;

    public static av a(int i) {
        return a(true, i);
    }

    private static synchronized av a(boolean z, int i) {
        try {
            av avVar = a;
            if (avVar == null) {
                a = new av(z, i);
            } else if (z && avVar.b == null) {
                avVar.b = hl.a(i);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return a;
    }

    private av(boolean z, int i) {
        if (z) {
            try {
                this.b = hl.a(i);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void a() {
        synchronized (this.c) {
            if (this.c.size() < 1) {
                return;
            }
            for (Map.Entry<String, hm> entry : this.c.entrySet()) {
                entry.getKey();
                ((ar) entry.getValue()).b();
            }
            this.c.clear();
        }
    }

    public void a(au auVar) {
        synchronized (this.c) {
            ar arVar = (ar) this.c.get(auVar.b());
            if (arVar == null) {
                return;
            }
            arVar.b();
            this.c.remove(auVar.b());
        }
    }

    public void a(au auVar, Context context, AMap aMap) throws ev {
        if (!this.c.containsKey(auVar.b())) {
            ar arVar = new ar((bk) auVar, context.getApplicationContext(), aMap);
            synchronized (this.c) {
                this.c.put(auVar.b(), arVar);
            }
        }
        this.b.a(this.c.get(auVar.b()));
    }

    public void b() {
        a();
        hl.a();
        this.b = null;
        c();
    }

    public static void c() {
        a = null;
    }

    public void b(au auVar) {
        ar arVar = (ar) this.c.get(auVar.b());
        if (arVar != null) {
            synchronized (this.c) {
                arVar.c();
                this.c.remove(auVar.b());
            }
        }
    }
}
