package cn.jiguang.ap;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public final class e {
    private static SharedPreferences a;

    private static long a(long j) {
        return (j + (j % 2 == 0 ? 1L : 2L)) % 32767;
    }

    public static synchronized long a(Context context) {
        long jA;
        jA = b(context).getLong("next_rid", -1L);
        if (jA != -1) {
            jA = a(jA);
            b(context).edit().putLong("next_rid", jA).apply();
        }
        if (jA == -1) {
            jA = a(Math.abs(new Random().nextInt(32767)));
            b(context).edit().putLong("next_rid", jA).apply();
        }
        return jA;
    }

    private static SharedPreferences b(Context context) {
        if (a == null) {
            a = context.getSharedPreferences("cn.jpush.preferences.support.rid", 0);
        }
        return a;
    }
}
