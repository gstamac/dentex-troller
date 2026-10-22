package cn.jiguang.af;

import android.content.Context;
import android.os.Bundle;
import cn.jiguang.ad.d;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    public static boolean a = false;

    public static void a(Context context) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 38, null, null, new Object[0]);
    }

    public static void a(Context context, int i) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 57, null, null, Integer.valueOf(i));
    }

    public static void a(Context context, long j) {
        if (j > 0) {
            JCoreManager.onEvent(context, JConstants.SDK_TYPE, 37, null, null, Long.valueOf(j));
        }
    }

    public static void a(Context context, long j, String str, String str2) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 35, null, null, Long.valueOf(j), str, str2);
    }

    public static void a(Context context, Bundle bundle) {
        if (bundle != null) {
            a = bundle.getBoolean("foreground");
            d.b("JCoreManagerInternal", "changeForeGroundStat:" + a);
            if (a) {
                cn.jiguang.e.a.a().b(context);
            }
        }
    }

    public static void a(Context context, Object obj) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 39, null, null, obj);
    }

    public static void a(Context context, String str) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 36, null, null, str);
    }

    public static void a(Context context, String str, int i, int i2, long j, long j2, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("cmd", i);
        bundle.putInt("ver", 0);
        bundle.putLong("rid", j);
        bundle.putLong("timeout", j2);
        bundle.putByteArray("body", bArr);
        JCoreManager.onEvent(context, str, 17, null, bundle, new Object[0]);
    }

    public static void a(Context context, String str, long j) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 44, null, null, str, Long.valueOf(j));
    }

    public static void a(Runnable runnable) {
        JCoreManager.onEvent(null, null, 12, null, null, runnable);
    }

    public static void a(String str) {
        JCoreManager.onEvent(null, null, 13, str, null, new Object[0]);
    }

    public static void a(String str, Runnable runnable) {
        JCoreManager.onEvent(null, null, 11, str, null, runnable);
    }

    public static Map b(Context context) {
        Object objOnEvent = JCoreManager.onEvent(context, JConstants.SDK_TYPE, 45, null, null, new Object[0]);
        if (objOnEvent instanceof Map) {
            return (Map) objOnEvent;
        }
        return null;
    }

    public static void b(Context context, int i) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 51, "", null, Integer.valueOf(i));
    }

    public static void b(Context context, String str, int i, int i2, long j, long j2, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putInt("cmd", 4);
        bundle.putInt("ver", 2);
        bundle.putLong("rid", j);
        bundle.putLong("timeout", 0L);
        bundle.putByteArray("body", bArr);
        bundle.putLong("uid", j2);
        JCoreManager.onEvent(context, str, 59, null, bundle, new Object[0]);
    }

    public static int c(Context context) {
        Object objOnEvent = JCoreManager.onEvent(context, JConstants.SDK_TYPE, 47, null, null, new Object[0]);
        if (objOnEvent instanceof Integer) {
            return ((Integer) objOnEvent).intValue();
        }
        return 0;
    }

    public static void d(Context context) {
        JCoreManager.onEvent(context, JConstants.SDK_TYPE, 48, null, null, new Object[0]);
    }
}
