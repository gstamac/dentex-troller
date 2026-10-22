package cn.jiguang.d;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.ad.d;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    private static volatile Long a;
    private static volatile Long b;

    public static long a(Context context, long j) {
        return (j + c(context)) / 1000;
    }

    public static boolean a(Context context) {
        String str;
        if (((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.c())).longValue() <= 0) {
            str = "isValidRegistered uid <= 0";
        } else {
            if (!TextUtils.isEmpty((String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.d()))) {
                return true;
            }
            str = "isValidRegistered regId is empty";
        }
        d.a("SpHelper", str);
        return false;
    }

    public static long b(Context context) {
        return a(context, System.currentTimeMillis());
    }

    public static void b(Context context, long j) {
        if (j > 0) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            a = Long.valueOf(j);
            b = Long.valueOf(jCurrentTimeMillis);
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.h().a(Long.valueOf(j)), cn.jiguang.g.a.g().a(Long.valueOf(jCurrentTimeMillis))});
        }
    }

    public static long c(Context context) {
        if (a != null && b != null) {
            return a.longValue() - b.longValue();
        }
        long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.g())).longValue();
        long jLongValue2 = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.h())).longValue();
        if (jLongValue == 0 || jLongValue2 == 0) {
            return 0L;
        }
        a = Long.valueOf(jLongValue2);
        b = Long.valueOf(jLongValue);
        return jLongValue2 - jLongValue;
    }
}
