package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;

/* JADX INFO: compiled from: DynamicExceptionHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class gm implements Thread.UncaughtExceptionHandler {
    private static gm a;
    private Thread.UncaughtExceptionHandler b = Thread.getDefaultUncaughtExceptionHandler();
    private Context c;
    private ff d;

    private gm(Context context, ff ffVar) {
        this.c = context.getApplicationContext();
        this.d = ffVar;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    static synchronized gm a(Context context, ff ffVar) {
        if (a == null) {
            a = new gm(context, ffVar);
        }
        return a;
    }

    void a(Throwable th) {
        String strA = fg.a(th);
        try {
            if (TextUtils.isEmpty(strA)) {
                return;
            }
            if ((strA.contains("amapdynamic") || strA.contains("admic")) && strA.contains("com.amap.api")) {
                ft ftVar = new ft(this.c, gn.a());
                if (strA.contains("loc")) {
                    gl.a(ftVar, this.c, "loc");
                }
                if (strA.contains("navi")) {
                    gl.a(ftVar, this.c, "navi");
                }
                if (strA.contains("sea")) {
                    gl.a(ftVar, this.c, "sea");
                }
                if (strA.contains("2dmap")) {
                    gl.a(ftVar, this.c, "2dmap");
                }
                if (strA.contains("3dmap")) {
                    gl.a(ftVar, this.c, "3dmap");
                    return;
                }
                return;
            }
            if (strA.contains("com.autonavi.aps.amapapi.offline")) {
                gl.a(new ft(this.c, gn.a()), this.c, "OfflineLocation");
                return;
            }
            if (strA.contains("com.data.carrier_v4")) {
                gl.a(new ft(this.c, gn.a()), this.c, "Collection");
            } else if (strA.contains("com.autonavi.aps.amapapi.httpdns") || strA.contains("com.autonavi.httpdns")) {
                gl.a(new ft(this.c, gn.a()), this.c, "HttpDNS");
            }
        } catch (Throwable th2) {
            fk.a(th2, "DynamicExceptionHandler", "uncaughtException");
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        a(th);
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.b;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        }
    }
}
