package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Looper;
import java.util.List;

/* JADX INFO: compiled from: ExceptionLogProcessor.java */
/* JADX INFO: loaded from: classes.dex */
public class fq extends fr {
    private static boolean b = true;

    @Override // com.amap.api.mapcore.util.fr
    protected String a(List<ff> list) {
        return null;
    }

    protected fq(int i) {
        super(i);
    }

    @Override // com.amap.api.mapcore.util.fr
    protected boolean a(Context context) {
        if (fa.m(context) != 1 || !b) {
            return false;
        }
        b = false;
        synchronized (Looper.getMainLooper()) {
            ge geVar = new ge(context);
            gf gfVarA = geVar.a();
            if (gfVarA == null) {
                return true;
            }
            if (!gfVarA.b()) {
                return false;
            }
            gfVarA.b(false);
            geVar.a(gfVarA);
            return true;
        }
    }
}
