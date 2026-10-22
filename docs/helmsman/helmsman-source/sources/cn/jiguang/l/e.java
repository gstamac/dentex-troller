package cn.jiguang.l;

import android.content.Context;
import android.content.SharedPreferences;
import cn.jiguang.internal.JConstants;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public final class e {
    private static SharedPreferences a;
    private static HashMap<String, Long> b;

    private static String a(String str, String str2) {
        return str + str2;
    }

    public static void a(Context context, String str, int i) {
        e(context).edit().putInt(a("JType", "ktsv_" + str), i).apply();
    }

    public static void a(Context context, String str, long j) {
        if (j < 0) {
            return;
        }
        if (!str.contains("JLocation") || j <= g(context, str)) {
            String strA = a(str, "_bi");
            cn.jiguang.w.a.c("JCommonConfig", "update " + str + " businessInterval:" + j);
            e(context).edit().putLong(strA, j).apply();
        }
    }

    public static void a(Context context, String str, String str2) {
        e(context).edit().putString(a("JType", str), str2).apply();
    }

    public static void a(Context context, String str, boolean z) {
        e(context).edit().putBoolean(a(str, "_ace"), z).apply();
    }

    public static void a(Context context, boolean z) {
        e(context).edit().putBoolean("JArponceEnable", z).apply();
    }

    public static boolean a(Context context) {
        return e(context).getBoolean("JArponceEnable", false);
    }

    public static boolean a(Context context, String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jD = d(context, str);
        long j = e(context).getLong(a(str, "_bi"), b(str, "_bi"));
        boolean z = jCurrentTimeMillis - jD > j;
        cn.jiguang.w.a.c("JCommonConfig", "is " + str + " businessTime:" + z + ",curTime:" + jCurrentTimeMillis + ",lastBusinessTime:" + jD + ",businessInterval:" + j);
        return z;
    }

    private static long b(String str, String str2) {
        try {
            return b.get(a(str, str2)).longValue();
        } catch (NullPointerException unused) {
            return 0L;
        }
    }

    public static String b(Context context) {
        return e(context).getString(a("JLocation", "info"), "");
    }

    public static void b(Context context, String str, long j) {
        if (j < 0) {
            return;
        }
        String strA = a(str, "_ri");
        cn.jiguang.w.a.c("JCommonConfig", "update " + str + " reportInterval:" + j);
        e(context).edit().putLong(strA, j).apply();
    }

    public static void b(Context context, String str, boolean z) {
        e(context).edit().putBoolean(a(str, "_aue"), z).apply();
    }

    public static boolean b(Context context, String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jF = f(context, str);
        long jG = g(context, str);
        boolean z = jCurrentTimeMillis - jF > jG;
        cn.jiguang.w.a.c("JCommonConfig", "is " + str + " reportTime:" + z + ",curTime:" + jCurrentTimeMillis + ",lastReportTime:" + jF + ",reportInterval:" + jG);
        return z;
    }

    public static String c(Context context) {
        return e(context).getString("JNotificationState", "");
    }

    public static void c(Context context, String str) {
        if (!str.contains("JApp") || str.equals("JAppMovement")) {
            String strA = a(str, "_blt");
            long jCurrentTimeMillis = System.currentTimeMillis();
            cn.jiguang.w.a.c("JCommonConfig", "update " + str + " lastBusinessTime");
            e(context).edit().putLong(strA, jCurrentTimeMillis).apply();
        }
    }

    public static long d(Context context, String str) {
        return e(context).getLong(a(str, "_blt"), 0L);
    }

    public static String d(Context context) {
        return e(context).getString("JDevicesession", "");
    }

    private static SharedPreferences e(Context context) {
        if (a == null) {
            a = context.getSharedPreferences("cn.jiguang.common", 0);
            HashMap<String, Long> map = new HashMap<>();
            b = map;
            map.put(a("JLocation", "_bi"), 900000L);
            b.put(a("JWake", "_bi"), Long.valueOf(JConstants.HOUR));
            b.put(a("JWakeConfigHelper", "_bi"), Long.valueOf(JConstants.HOUR));
            b.put(a("JArp", "_ri"), Long.valueOf(JConstants.HOUR));
            b.put(a("JDeviceBattery", "_ri"), Long.valueOf(JConstants.HOUR));
            b.put(a("JDevice", "_ri"), Long.valueOf(JConstants.DAY));
            b.put(a("JLocation", "_ri"), Long.valueOf(JConstants.HOUR));
            b.put(a("JWake", "_ri"), Long.valueOf(JConstants.HOUR));
        }
        return a;
    }

    public static void e(Context context, String str) {
        cn.jiguang.w.a.c("JCommonConfig", "update " + str + " lastReportTime");
        e(context).edit().putLong(a(str, "_rlt"), System.currentTimeMillis()).apply();
    }

    public static long f(Context context, String str) {
        return e(context).getLong(a(str, "_rlt"), 0L);
    }

    public static long g(Context context, String str) {
        return e(context).getLong(a(str, "_ri"), b(str, "_ri"));
    }

    public static boolean h(Context context, String str) {
        return e(context).getBoolean(a(str, "_ace"), !str.equals("JArp"));
    }

    public static boolean i(Context context, String str) {
        return e(context).getBoolean(a(str, "_aue"), true);
    }

    public static String j(Context context, String str) {
        return e(context).getString(a("JType", str), "-1,-1");
    }

    public static int k(Context context, String str) {
        return e(context).getInt(a("JType", "ktsv_" + str), 0);
    }

    public static void l(Context context, String str) {
        e(context).edit().putBoolean(a("JArp", str), true).apply();
    }

    public static boolean m(Context context, String str) {
        return e(context).getBoolean(a("JArp", str), false);
    }

    public static void n(Context context, String str) {
        e(context).edit().putString(a("JLocation", "info"), str).apply();
    }

    public static void o(Context context, String str) {
        e(context).edit().putString("JNotificationState", str).apply();
    }

    public static void p(Context context, String str) {
        cn.jiguang.w.a.c("JCommonConfig", "update deviceSession");
        e(context).edit().putString("JDevicesession", str).apply();
    }
}
