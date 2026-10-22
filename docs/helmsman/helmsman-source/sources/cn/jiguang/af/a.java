package cn.jiguang.af;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.aj.c;
import cn.jiguang.aj.g;
import cn.jiguang.aj.i;
import cn.jiguang.android.BuildConfig;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static boolean a;

    private static synchronized void a(Context context) {
        if (a) {
            return;
        }
        if (context == null) {
            return;
        }
        d.b("JCoreActionImpl", "init jcore impl ,version:" + cn.jiguang.a.a.b + ",local version:" + JConstants.SDK_VERSION_INT);
        a = true;
        try {
            d.b("JCoreActionImpl", "hb:" + JConstants.DEFAULT_HEARTBEAT_INTERVAL + ",google:true,internal:" + JConstants.INTERNAL_USE);
            int i = cn.jiguang.a.a.c != JConstants.SDK_VERSION_INT ? 2 : 0;
            d.b("JCoreActionImpl", "custom:1,dynamic:" + i);
            JCoreManager.onEvent(context, JConstants.SDK_TYPE, 72, true, null, null, "core", 1, Integer.valueOf(i), Integer.valueOf(cn.jiguang.a.a.c));
        } catch (Throwable th) {
            d.g("JCoreActionImpl", "sdk type call failed:" + th.getMessage());
        }
        String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.w());
        if (TextUtils.isEmpty(str) || str.startsWith("1.")) {
            b.d(context);
        }
        if (TextUtils.isEmpty(str) || !BuildConfig.VERSION_NAME.equals(str)) {
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.w().a(BuildConfig.VERSION_NAME)});
        }
        c.a(context);
    }

    public static void a(Context context, String str, Bundle bundle) {
        String string;
        a(context);
        if (TextUtils.isEmpty(str)) {
            d.g("JCoreActionImpl", "handleAction Failed,action is empty");
            return;
        }
        d.b("JCoreActionImpl", "handleAction action:" + str);
        String string2 = bundle != null ? bundle.getString("sdk_type") : "";
        if (str.equals("a1")) {
            if (bundle != null) {
                try {
                    string = bundle.getString("report_data");
                } catch (Throwable th) {
                    d.f("JCoreActionImpl", "report failed:" + th.getMessage());
                    return;
                }
            } else {
                string = null;
            }
            b.a(context, (Object) string);
            return;
        }
        if (str.startsWith("tcp_")) {
            i.a().a(context, str, bundle);
            return;
        }
        if (str.equals("a2")) {
            g.a().a(context, true);
            return;
        }
        if (str.equals("a3")) {
            cn.jiguang.aj.b.a();
            cn.jiguang.aj.b.a(context, string2, bundle);
        } else if (str.equals("a4")) {
            b.a(context, bundle);
        }
    }
}
