package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.List;

/* JADX INFO: compiled from: LogDBOperation.java */
/* JADX INFO: loaded from: classes.dex */
public class gb {
    private ft a;

    public gb(Context context) {
        try {
            this.a = new ft(context, ft.a((Class<? extends fs>) ga.class));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void a(String str, Class<? extends gc> cls) {
        try {
            c(str, cls);
        } catch (Throwable th) {
            fk.a(th, "LogDB", "delLog");
        }
    }

    public void b(String str, Class<? extends gc> cls) {
        try {
            c(str, cls);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void c(String str, Class<? extends gc> cls) {
        this.a.a(gc.c(str), (Class) cls);
    }

    public List<? extends gc> a(int i, Class<? extends gc> cls) {
        try {
            return this.a.b(gc.c(i), cls);
        } catch (Throwable th) {
            fk.a(th, "LogDB", "ByState");
            return null;
        }
    }

    public void a(gc gcVar) {
        if (gcVar == null) {
            return;
        }
        String strC = gc.c(gcVar.b());
        List listA = this.a.a(strC, (Class) gcVar.getClass(), true);
        if (listA == null || listA.size() == 0) {
            this.a.a(gcVar, true);
            return;
        }
        gc gcVar2 = (gc) listA.get(0);
        if (gcVar.a() == 0) {
            gcVar2.b(gcVar2.c() + 1);
        } else {
            gcVar2.b(0);
        }
        this.a.a(strC, (Object) gcVar2, true);
    }

    public void b(gc gcVar) {
        try {
            this.a.a(gc.c(gcVar.b()), gcVar);
        } catch (Throwable th) {
            fk.a(th, "LogDB", "updateLogInfo");
        }
    }
}
