package cn.jiguang.ag;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.g.b;
import java.util.LinkedHashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static String a;
    public static int b;
    public static String c;
    public static int d;
    private static final LinkedHashMap<String, Integer> e;
    private static final LinkedHashMap<String, Integer> f;
    private static final LinkedHashMap<String, Integer> g;
    private static final LinkedHashMap<String, Integer> h;
    private static String i;
    private static String j;
    private static String k;
    private static String l;

    static {
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();
        e = linkedHashMap;
        linkedHashMap.put("s.jpush.cn", 19000);
        linkedHashMap.put("sis.jpush.io", 19000);
        linkedHashMap.put("easytomessage.com", 19000);
        LinkedHashMap<String, Integer> linkedHashMap2 = new LinkedHashMap<>();
        f = linkedHashMap2;
        linkedHashMap2.put("123.196.118.23", 19000);
        linkedHashMap2.put("103.229.215.60", 19000);
        linkedHashMap2.put("117.121.49.100", 19000);
        g = new LinkedHashMap<>();
        h = new LinkedHashMap<>();
        i = "";
        j = "";
        k = "";
        l = "";
    }

    public static String a(Context context) {
        if (JCoreManager.isTestEnv() && !TextUtils.isEmpty(i)) {
            return i;
        }
        String str = (String) b.b(context, cn.jiguang.g.a.P());
        return !TextUtils.isEmpty(str) ? str : "im64.jpush.cn";
    }

    public static LinkedHashMap<String, Integer> a() {
        if (JCoreManager.isTestEnv()) {
            LinkedHashMap<String, Integer> linkedHashMap = g;
            if (!linkedHashMap.isEmpty()) {
                return linkedHashMap;
            }
        }
        return e;
    }

    public static void a(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            d.f("HostConfig", "conn info was empty");
            return;
        }
        d.b("HostConfig", "get conn info=" + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("srv");
            d.b("HostConfig", "save srvHost:" + strOptString);
            if (!TextUtils.isEmpty(strOptString)) {
                b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.Q().a(strOptString)});
            }
            String strOptString2 = jSONObject.optString("conn");
            d.b("HostConfig", "save connHost:" + strOptString2);
            if (TextUtils.isEmpty(strOptString2)) {
                return;
            }
            b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.P().a(strOptString2)});
        } catch (Throwable unused) {
        }
    }

    public static String b(Context context) {
        if (JCoreManager.isTestEnv() && !TextUtils.isEmpty(j)) {
            return j;
        }
        String str = (String) b.b(context, cn.jiguang.g.a.Q());
        return !TextUtils.isEmpty(str) ? str : "_im64._tcp.jpush.cn";
    }

    public static LinkedHashMap<String, Integer> b() {
        if (JCoreManager.isTestEnv()) {
            LinkedHashMap<String, Integer> linkedHashMap = h;
            if (!linkedHashMap.isEmpty()) {
                return linkedHashMap;
            }
        }
        return f;
    }

    public static String c() {
        return (!JCoreManager.isTestEnv() || TextUtils.isEmpty(k)) ? "_psis._udp.jpush.cn" : k;
    }

    public static String d() {
        return (!JCoreManager.isTestEnv() || TextUtils.isEmpty(l)) ? "" : l;
    }
}
