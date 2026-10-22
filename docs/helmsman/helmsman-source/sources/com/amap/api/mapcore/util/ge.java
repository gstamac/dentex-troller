package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.List;

/* JADX INFO: compiled from: UpdateLogDBOperation.java */
/* JADX INFO: loaded from: classes.dex */
public class ge {
    private ft a;
    private Context b;

    public ge(Context context) {
        this.b = context;
        this.a = a(context);
    }

    private ft a(Context context) {
        try {
            return new ft(context, ft.a((Class<? extends fs>) ga.class));
        } catch (Throwable th) {
            fk.a(th, "UpdateLogDB", "getDB");
            return null;
        }
    }

    public gf a() {
        try {
            if (this.a == null) {
                this.a = a(this.b);
            }
            List listB = this.a.b("1=1", gf.class);
            if (listB.size() > 0) {
                return (gf) listB.get(0);
            }
            return null;
        } catch (Throwable th) {
            fk.a(th, "UpdateLogDB", "getUpdateLog");
            return null;
        }
    }

    public void a(gf gfVar) {
        if (gfVar == null) {
            return;
        }
        try {
            if (this.a == null) {
                this.a = a(this.b);
            }
            List listB = this.a.b("1=1", gf.class);
            if (listB != null && listB.size() != 0) {
                this.a.a("1=1", gfVar);
                return;
            }
            this.a.a(gfVar);
        } catch (Throwable th) {
            fk.a(th, "UpdateLogDB", "updateLog");
        }
    }
}
