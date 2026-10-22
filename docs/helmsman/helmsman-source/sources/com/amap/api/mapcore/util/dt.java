package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: compiled from: SPUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class dt {
    public static void a(Context context, String str, String str2, Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
        if ("String".equals(simpleName)) {
            editorEdit.putString(str2, (String) obj);
        } else if ("Integer".equals(simpleName)) {
            editorEdit.putInt(str2, ((Integer) obj).intValue());
        } else if ("Boolean".equals(simpleName)) {
            editorEdit.putBoolean(str2, ((Boolean) obj).booleanValue());
        } else if ("Float".equals(simpleName)) {
            editorEdit.putFloat(str2, ((Float) obj).floatValue());
        } else if ("Long".equals(simpleName)) {
            editorEdit.putLong(str2, ((Long) obj).longValue());
        }
        editorEdit.commit();
    }
}
