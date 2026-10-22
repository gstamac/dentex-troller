package com.amap.api.mapcore.util;

import android.content.Context;

/* JADX INFO: compiled from: OfflineLocEntity.java */
/* JADX INFO: loaded from: classes.dex */
public class hg {
    private Context a;
    private ff b;
    private String c;

    public hg(Context context, ff ffVar, String str) {
        this.a = context.getApplicationContext();
        this.b = ffVar;
        this.c = str;
    }

    byte[] a() {
        return fg.a(a(this.a, this.b, this.c));
    }

    private static String a(Context context, ff ffVar, String str) {
        return ez.b(context, fg.b(fg.a(b(context, ffVar, str))));
    }

    private static String b(Context context, ff ffVar, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("\"sdkversion\":\"").append(ffVar.c()).append("\",\"product\":\"").append(ffVar.a()).append("\",\"nt\":\"").append(fa.c(context)).append("\",\"details\":").append(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }
}
