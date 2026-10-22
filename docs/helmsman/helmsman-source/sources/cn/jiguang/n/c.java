package cn.jiguang.n;

import android.os.Build;
import android.text.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    private static String a = "";
    private static String b = "";
    private static String c = "";
    private static String d = "";
    private static String e = "";
    private static String f = "";

    public static String a() {
        try {
            String lowerCase = Build.MANUFACTURER.toLowerCase();
            if (lowerCase.contains("huawei")) {
                if (!TextUtils.isEmpty(a)) {
                    return a;
                }
                String strA = a("ro.build.version.emui");
                a = strA;
                return strA;
            }
            if (lowerCase.contains("xiaomi")) {
                if (!TextUtils.isEmpty(e)) {
                    return e;
                }
                String strA2 = a("ro.miui.ui.version.name");
                e = strA2;
                return strA2;
            }
            if (lowerCase.contains("meizu")) {
                if (!TextUtils.isEmpty(f)) {
                    return f;
                }
                String strA3 = a("ro.build.display.id");
                f = strA3;
                return strA3;
            }
            if (!lowerCase.contains("oppo") && !lowerCase.contains("realme")) {
                if (lowerCase.contains("vivo")) {
                    if (!TextUtils.isEmpty(c)) {
                        return c;
                    }
                    String strA4 = a("ro.vivo.os.build.display.id");
                    c = strA4;
                    return strA4;
                }
                if (!lowerCase.contains("oneplus")) {
                    return "";
                }
                if (!TextUtils.isEmpty(d)) {
                    return d;
                }
                String strA5 = a("ro.rom.version");
                d = strA5;
                return strA5;
            }
            if (!TextUtils.isEmpty(b)) {
                return b;
            }
            String strA6 = a("ro.build.version.opporom");
            b = strA6;
            return strA6;
        } catch (Throwable unused) {
            return "";
        }
    }

    private static String a(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str2 = (String) cls.getDeclaredMethod("get", String.class).invoke(cls, str);
            cn.jiguang.w.a.c("JRomVersionHelper", "get " + str + " version is:" + str2);
            return str2;
        } catch (Throwable th) {
            cn.jiguang.w.a.f("JRomVersionHelper", " get " + str + "wrong error:" + th.getMessage());
            return "";
        }
    }
}
