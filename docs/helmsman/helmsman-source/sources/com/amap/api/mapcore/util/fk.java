package com.amap.api.mapcore.util;

import android.content.Context;

/* JADX INFO: compiled from: BasicLogHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class fk {
    protected static fk a;
    protected Thread.UncaughtExceptionHandler b;
    protected boolean c = true;

    protected void a(Context context, ff ffVar, boolean z) {
    }

    protected void a(ff ffVar, String str, String str2) {
    }

    protected void a(Throwable th, int i, String str, String str2) {
    }

    public static void a(Throwable th, String str, String str2) {
        th.printStackTrace();
        fk fkVar = a;
        if (fkVar != null) {
            fkVar.a(th, 1, str, str2);
        }
    }
}
