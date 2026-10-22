package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Looper;
import java.util.Date;
import java.util.List;

/* JADX INFO: compiled from: CrashLogProcessor.java */
/* JADX INFO: loaded from: classes.dex */
public class fp extends fr {
    private static boolean b = true;

    @Override // com.amap.api.mapcore.util.fr
    protected String a(List<ff> list) {
        return null;
    }

    protected fp(int i) {
        super(i);
    }

    @Override // com.amap.api.mapcore.util.fr
    protected boolean a(Context context) {
        if (!b) {
            return false;
        }
        b = false;
        synchronized (Looper.getMainLooper()) {
            ge geVar = new ge(context);
            gf gfVarA = geVar.a();
            if (gfVarA == null) {
                return true;
            }
            if (!gfVarA.a()) {
                return false;
            }
            gfVarA.a(false);
            geVar.a(gfVarA);
            return true;
        }
    }

    @Override // com.amap.api.mapcore.util.fr
    protected String a(String str) {
        return fc.c(str + fg.a(new Date().getTime()));
    }
}
