package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: SDKDBOperation.java */
/* JADX INFO: loaded from: classes.dex */
public class gd {
    private ft a;
    private Context b;

    public gd(Context context, boolean z) {
        this.b = context;
        this.a = a(context, z);
    }

    private ft a(Context context, boolean z) {
        try {
            return new ft(context, ft.a((Class<? extends fs>) ga.class));
        } catch (Throwable th) {
            if (!z) {
                fk.a(th, "SDKDB", "getDB");
            } else {
                th.printStackTrace();
            }
            return null;
        }
    }

    public void a(ff ffVar) {
        if (ffVar == null) {
            return;
        }
        try {
            if (this.a == null) {
                this.a = a(this.b, false);
            }
            String strA = ff.a(ffVar.a());
            List<ff> listB = this.a.b(strA, ff.class);
            if (listB != null && listB.size() != 0) {
                if (a(listB, ffVar)) {
                    this.a.a(strA, ffVar);
                    return;
                }
                return;
            }
            this.a.a(ffVar);
        } catch (Throwable th) {
            fk.a(th, "SDKDB", "insert");
            th.printStackTrace();
        }
    }

    private boolean a(List<ff> list, ff ffVar) {
        Iterator<ff> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals(ffVar)) {
                return false;
            }
        }
        return true;
    }

    public List<ff> a() {
        try {
            return this.a.a(ff.h(), ff.class, true);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }
}
