package cn.jiguang.d;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.analytics.page.ActivityLifecycle;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.f.e;
import cn.jiguang.f.i;
import cn.jiguang.internal.JConstants;
import com.facebook.appevents.UserDataStore;
import com.facebook.internal.ServerProtocol;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static JSONObject a(Context context, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                d.b("JBridgeHelper", "file_name is null , give up read ");
                return null;
            }
            String strB = e.b(e.a(context, str));
            if (!i.a(strB)) {
                return new JSONObject(strB.trim());
            }
            d.b("JBridgeHelper", "read String is empty");
            return null;
        } catch (Throwable th) {
            d.b("JBridgeHelper", "can't build " + str + " into JsonObject, give up read :" + th);
            return null;
        }
    }

    public static JSONObject a(Context context, JSONObject jSONObject, String str) {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        try {
            jSONObject.put("itime", b.a(context, System.currentTimeMillis()));
            jSONObject.put("type", str);
            jSONObject.put("account_id", cn.jiguang.g.b.a(context, cn.jiguang.g.a.m()));
        } catch (JSONException e) {
            d.f("JBridgeHelper", "fillBase exception:" + e);
        }
        return jSONObject;
    }

    public static synchronized void a(Context context) {
        try {
            if (JConstants.isInstrumentationHookFailed && (context instanceof Application)) {
                String strA = cn.jiguang.f.a.a(context);
                String packageName = context.getPackageName();
                if (strA != null && packageName != null && context.getPackageName().equals(strA)) {
                    JConstants.isInstrumentationHookFailed = false;
                    ((Application) context).registerActivityLifecycleCallbacks(new ActivityLifecycle());
                    d.b("JBridgeHelper", "registerActivityLifecycleCallbacks in main process,packageName:" + packageName + ",currentProcessName:" + strA);
                    return;
                }
                d.b("JBridgeHelper", "need not registerActivityLifecycleCallbacks in other process :" + strA);
            }
        } catch (Throwable th) {
            d.g("JBridgeHelper", "registerActivityLifecycleCallbacks failed:" + th);
            JConstants.isInstrumentationHookFailed = true;
        }
    }

    public static void a(Context context, Intent intent) {
        if (intent != null) {
            try {
                if ("asm".equals(intent.getAction())) {
                    JCoreManager.onEvent(context, JConstants.SDK_TYPE, 3, "asm", intent.getExtras(), new Object[0]);
                    return;
                }
            } catch (Throwable th) {
                d.f("JBridgeHelper", "shareActionRun error:" + th.getMessage());
                return;
            }
        }
        d.b("JBridgeHelper", "shareActionRun intent error:" + (intent == null ? "null" : intent.getAction()));
    }

    public static String b(Context context) {
        return (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.d());
    }

    public static String c(Context context) {
        return (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.m());
    }

    public static long d(Context context) {
        return ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.c())).longValue();
    }

    public static String e(Context context) {
        return (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.a());
    }

    public static String f(Context context) {
        return (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.e());
    }

    public static Object g(Context context) {
        HashMap map = new HashMap();
        String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.i());
        long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.j())).longValue();
        int iIntValue = ((Integer) cn.jiguang.g.b.a(context, cn.jiguang.g.a.k())).intValue();
        map.put("uuid", str);
        map.put(UserDataStore.CITY, Long.valueOf(jLongValue));
        map.put(ServerProtocol.DIALOG_PARAM_STATE, Integer.valueOf(iIntValue));
        return map;
    }

    public static int h(Context context) {
        return ((Integer) cn.jiguang.g.b.a(context, cn.jiguang.g.a.f())).intValue();
    }

    public static boolean i(Context context) {
        d.b("JBridgeHelper", "canShowLbsPermissionDialog");
        Boolean bool = (Boolean) cn.jiguang.g.b.a(context, cn.jiguang.g.a.z());
        if (bool != null && !bool.booleanValue()) {
            d.c("JBridgeHelper", "lbs disable");
            return false;
        }
        long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.y())).longValue();
        long jLongValue2 = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.x())).longValue();
        d.b("JBridgeHelper", "lbs permission dialog shield, firstInit=" + jLongValue2 + ", delay=" + jLongValue);
        if (jLongValue2 > 0) {
            return jLongValue <= 0 || System.currentTimeMillis() > jLongValue2 + jLongValue;
        }
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.x().a(Long.valueOf(System.currentTimeMillis()))});
        return jLongValue <= 0;
    }

    public static int j(Context context) {
        return ((Integer) cn.jiguang.g.b.a(context, cn.jiguang.g.a.k())).intValue();
    }
}
