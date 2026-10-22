package cn.jpush.android.a;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {
    private static SharedPreferences a;

    public static void a(Context context) {
        if (a == null) {
            a = context.getSharedPreferences("asus_push_sdk_preferences", 0);
        }
    }

    protected static void a(String str, String str2) {
        SharedPreferences sharedPreferences = a;
        if (sharedPreferences != null) {
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putString(str, str2);
            editorEdit.apply();
        }
    }

    protected static void a(String str, boolean z) {
        SharedPreferences sharedPreferences = a;
        if (sharedPreferences != null) {
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putBoolean(str, false);
            editorEdit.apply();
        }
    }

    protected static Boolean b(String str, boolean z) {
        SharedPreferences sharedPreferences = a;
        if (sharedPreferences != null) {
            return Boolean.valueOf(sharedPreferences.getBoolean(str, true));
        }
        return true;
    }

    protected static String b(String str, String str2) {
        SharedPreferences sharedPreferences = a;
        return sharedPreferences != null ? sharedPreferences.getString(str, str2) : str2;
    }
}
