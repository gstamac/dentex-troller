package com.amap.api.mapcore.util;

import android.content.Context;

/* JADX INFO: compiled from: SPUtil.java */
/* JADX INFO: loaded from: classes.dex */
public final class il {
    public static int a(Context context, String str, String str2) {
        try {
            return context.getSharedPreferences(str, 0).getInt(str2, 200);
        } catch (Throwable th) {
            ij.a(th, "SPUtil", "getPrefsInt");
            return 200;
        }
    }

    public static boolean b(Context context, String str, String str2) {
        try {
            return context.getSharedPreferences(str, 0).getBoolean(str2, true);
        } catch (Throwable th) {
            ij.a(th, "SPUtil", "getPrefsBoolean");
            return true;
        }
    }
}
