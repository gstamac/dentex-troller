package cn.jiguang.ae;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.api.JThirdPlatFormInterface;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class i {
    private static j a;
    private static volatile FutureTask<?> b;
    private static final Object c = new Object();
    private static final LinkedHashSet<String> d;
    private static final LinkedHashSet<String> e;

    static {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        d = linkedHashSet;
        linkedHashSet.add("https://tsis.jpush.cn");
        e = new LinkedHashSet<>();
    }

    public static LinkedHashSet<String> a() {
        if (JConstants.isTestEnv()) {
            LinkedHashSet<String> linkedHashSet = e;
            if (!linkedHashSet.isEmpty()) {
                return linkedHashSet;
            }
        }
        return d;
    }

    public static void a(Context context, boolean z) {
        String str;
        StringBuilder sb;
        if (a == null) {
            a = new j(context, (byte) 0);
        }
        if (b == null || b.isCancelled() || b.isDone()) {
            synchronized (c) {
                if (b == null || b.isCancelled() || b.isDone()) {
                    try {
                        b = new FutureTask<>(a, null);
                        cn.jiguang.aq.a.a("ASYNC", b);
                    } catch (Throwable th) {
                        cn.jiguang.ad.d.f("ReportSis", "new sis task e:" + th);
                    }
                }
            }
        }
        if (z) {
            try {
                b.get(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e2) {
                e = e2;
                str = "ReportSis";
                sb = new StringBuilder("sis task e:");
                cn.jiguang.ad.d.f(str, sb.append(e).toString());
            } catch (ExecutionException e3) {
                e = e3;
                str = "ReportSis";
                sb = new StringBuilder("sis task e:");
                cn.jiguang.ad.d.f(str, sb.append(e).toString());
            } catch (TimeoutException e4) {
                e = e4;
                str = "ReportSis";
                sb = new StringBuilder("sis task e:");
                cn.jiguang.ad.d.f(str, sb.append(e).toString());
            } catch (Throwable th2) {
                cn.jiguang.ad.d.f("ReportSis", "sis task e:" + th2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean b(Context context, String str, String str2) {
        n nVarA = b.a(str, str2, context, true, 3, 2);
        cn.jiguang.ad.d.b("ReportSis", "report sis code[" + nVarA.a() + "] from url=" + str + "\n body=" + nVarA.b());
        if (nVarA.a() != 0) {
            return false;
        }
        String strB = nVarA.b();
        if (TextUtils.isEmpty(strB)) {
            return false;
        }
        try {
            a.a().a(context, new JSONObject(strB).getJSONObject("ret"));
            return true;
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("ReportSis", "getUrls e:" + th);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String c(Context context) {
        double d2;
        JSONObject jSONObject = new JSONObject();
        try {
            String strA = cn.jiguang.ac.a.a(context);
            long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.c())).longValue();
            int iA = cn.jiguang.f.j.a(context);
            String strB = cn.jiguang.f.j.b(context);
            jSONObject.put("type", iA);
            jSONObject.put("appkey", strA);
            jSONObject.put("sdkver", cn.jiguang.a.a.b);
            jSONObject.put(JThirdPlatFormInterface.KEY_PLATFORM, 0);
            long j = 0;
            if (jLongValue != 0) {
                jSONObject.put("uid", jLongValue);
            }
            if (strB != null) {
                jSONObject.put("opera", strB);
            }
            Object objA = cn.jiguang.aj.e.a(context, "get_loc_info", null);
            double d3 = 200.0d;
            if (objA instanceof Bundle) {
                try {
                    Bundle bundle = (Bundle) objA;
                    double d4 = bundle.getDouble("lat");
                    try {
                        d3 = bundle.getDouble("lot");
                        j = bundle.getLong("time");
                    } catch (Throwable unused) {
                    }
                    double d5 = d3;
                    d3 = d4;
                    d2 = d5;
                } catch (Throwable unused2) {
                    d2 = 200.0d;
                }
            } else {
                d2 = 200.0d;
            }
            if (d3 > -90.0d && d3 < 90.0d && d2 > -180.0d && d2 < 180.0d) {
                jSONObject.put("lat", d3);
                jSONObject.put("lng", d2);
                jSONObject.put("time", j);
            }
        } catch (Throwable unused3) {
        }
        return jSONObject.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static LinkedHashSet<String> d(Context context) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.t());
        if (TextUtils.isEmpty(str)) {
            return linkedHashSet;
        }
        try {
            JSONArray jSONArray = new JSONArray(str);
            for (int i = 0; i < jSONArray.length(); i++) {
                linkedHashSet.add(jSONArray.optString(i));
            }
        } catch (JSONException unused) {
        }
        return linkedHashSet;
    }
}
