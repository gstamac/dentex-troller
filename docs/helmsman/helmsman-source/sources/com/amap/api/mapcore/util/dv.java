package com.amap.api.mapcore.util;

import android.content.Context;

/* JADX INFO: compiled from: StatisticsUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class dv {
    public static void a(Context context, boolean z) {
        try {
            String strA = a(z);
            hi hiVar = new hi(context, "3dmap", "5.4.0", "O001");
            hiVar.a(strA);
            hj.a(hiVar, context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static String a(boolean z) {
        try {
            StringBuilder sb = new StringBuilder("{\"Quest\":");
            sb.append(z).append("}");
            return sb.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }
}
