package cn.jpush.android.a;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class b extends a {
    public static void a(String str) {
        a("key_token", str);
    }

    public static String b(Context context) {
        a(context);
        return b("key_token", "");
    }

    public static boolean c(Context context) {
        a(context);
        return b("first_request", true).booleanValue();
    }

    public static void d(Context context) {
        a(context);
        a("first_request", false);
    }
}
