package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: LoaderFactory.java */
/* JADX INFO: loaded from: classes.dex */
public class gq {
    private static final gq a = new gq();
    private final Map<String, gj> b = new HashMap();

    private gq() {
    }

    public static gq a() {
        return a;
    }

    synchronized gj a(Context context, ff ffVar) throws Exception {
        if (a(ffVar) && context != null) {
            String strA = ffVar.a();
            gj gjVar = this.b.get(strA);
            if (gjVar == null) {
                try {
                    go goVar = new go(context.getApplicationContext(), ffVar, true);
                    try {
                        this.b.put(strA, goVar);
                        gm.a(context, ffVar);
                    } catch (Throwable unused) {
                    }
                    gjVar = goVar;
                } catch (Throwable unused2) {
                }
            }
            return gjVar;
        }
        return null;
    }

    private boolean a(ff ffVar) {
        return (ffVar == null || TextUtils.isEmpty(ffVar.b()) || TextUtils.isEmpty(ffVar.a())) ? false : true;
    }
}
