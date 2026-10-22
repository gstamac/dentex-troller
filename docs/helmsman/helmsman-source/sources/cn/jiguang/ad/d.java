package cn.jiguang.ad;

import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    public static void a(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, true, 2, str2);
    }

    public static void a(String str, String str2, Throwable th) {
        a.a(JConstants.SDK_TYPE, str, true, 5, str2);
    }

    public static void b(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, true, 3, str2);
    }

    public static void b(String str, String str2, Throwable th) {
        a.a(JConstants.SDK_TYPE, str, true, 6, str2);
    }

    public static void c(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, false, 3, str2);
    }

    public static void c(String str, String str2, Throwable th) {
        a.a(JConstants.SDK_TYPE, str, false, 6, str2);
    }

    public static void d(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, true, 4, str2);
    }

    public static void d(String str, String str2, Throwable th) {
        c(str, str2, th);
        if (JConstants.INTERNAL_USE) {
            throw new RuntimeException(th);
        }
    }

    public static void e(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, false, 4, str2);
    }

    public static void f(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, true, 5, str2);
    }

    public static void g(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, false, 5, str2);
    }

    public static void h(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, true, 6, str2);
    }

    public static void i(String str, String str2) {
        a.a(JConstants.SDK_TYPE, str, false, 6, str2);
    }
}
