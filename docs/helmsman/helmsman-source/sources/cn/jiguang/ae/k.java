package cn.jiguang.ae;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.api.ReportCallBack;
import cn.jiguang.internal.ActionManager;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.api.JThirdPlatFormInterface;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class k {
    public static JSONObject a = null;
    public static boolean b = true;
    public static boolean c = true;
    private static String d = "";
    private static String e = "/v3/report";
    private static String f = "";

    public static int a(Context context, Set<String> set, JSONObject jSONObject, File file, ReportCallBack reportCallBack) {
        if (jSONObject != null) {
            try {
                if (jSONObject.length() != 0) {
                    if (!cn.jiguang.f.a.d(context)) {
                        cn.jiguang.ad.d.f("ReportUtils", "no network, give up upload");
                        d.a(file);
                        if (reportCallBack != null) {
                            reportCallBack.onFinish(-2);
                        }
                        return -2;
                    }
                    Pair<byte[], Integer> pairA = a(jSONObject.toString(), true, 2);
                    if (pairA != null && pairA.first != null && ((byte[]) pairA.first).length != 0) {
                        byte[] bArr = (byte[]) pairA.first;
                        int iIntValue = ((Integer) pairA.second).intValue();
                        cn.jiguang.ad.d.b("ReportUtils", "will upload length=" + bArr.length);
                        if (b && bArr.length < 30680 && set != null && !set.contains("crash_log")) {
                            cn.jiguang.aj.l.a();
                            cn.jiguang.aj.l.a().a(context, cn.jiguang.aj.l.a(context, jSONObject, bArr, iIntValue, file, set, reportCallBack));
                            return 1;
                        }
                        int iA = a(context, bArr, iIntValue, set);
                        if (iA == 0) {
                            cn.jiguang.ad.d.b("ReportUtils", "http upload success json=" + cn.jiguang.f.g.a(jSONObject));
                            cn.jiguang.f.e.a(file);
                        }
                        if (iA != 1) {
                            d.a(file);
                            if (reportCallBack != null) {
                                reportCallBack.onFinish(iA);
                            }
                        }
                        return iA;
                    }
                    cn.jiguang.ad.d.f("ReportUtils", "package body failed, give up upload");
                    d.a(file);
                    if (reportCallBack != null) {
                        reportCallBack.onFinish(-1);
                    }
                    return -1;
                }
            } catch (Throwable th) {
                try {
                    cn.jiguang.ad.d.f("ReportUtils", "upload failed, error:" + th);
                    d.a(file);
                    if (reportCallBack != null) {
                        reportCallBack.onFinish(-1);
                    }
                    return -1;
                } catch (Throwable th2) {
                    if (0 != 1) {
                        d.a(file);
                        if (reportCallBack != null) {
                            reportCallBack.onFinish(0);
                        }
                    }
                    throw th2;
                }
            }
        }
        cn.jiguang.ad.d.f("ReportUtils", "upload content is empty, do nothing");
        d.a(file);
        if (reportCallBack != null) {
            reportCallBack.onFinish(-1);
        }
        return -1;
    }

    private static int a(Context context, byte[] bArr, int i, Set<String> set) {
        StringBuilder sb;
        StringBuilder sbAppend;
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
        String str = (!JCoreManager.isTestEnv() || TextUtils.isEmpty(f)) ? "stats.jpush.cn" : f;
        if ("stats.jpush.cn".equals(str)) {
            Set<String> setB = a.a().b(context).b(set);
            if (setB != null) {
                for (String str2 : setB) {
                    if (!TextUtils.isEmpty(str2)) {
                        linkedHashSet.add(str2);
                    }
                }
            }
            if (!TextUtils.isEmpty(str)) {
                linkedHashSet.add("https://" + str);
            }
            String str3 = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.a(true));
            if (!TextUtils.isEmpty(str3)) {
                for (String str4 : str3.split(",")) {
                    if (cn.jiguang.f.i.f(str4)) {
                        if (c) {
                            sb = new StringBuilder("https://");
                            linkedHashSet.add(sb.append(str4).toString());
                        }
                    } else if (!cn.jiguang.f.i.g(str4) || !c) {
                        sb = new StringBuilder("https://");
                        linkedHashSet.add(sb.append(str4).toString());
                    }
                }
            }
            cn.jiguang.ad.d.b("ReportUtils", "types=" + set + " find urls=" + linkedHashSet);
        } else {
            linkedHashSet.add(str);
        }
        if (linkedHashSet.isEmpty()) {
            cn.jiguang.ad.d.f("ReportUtils", "can't get url, give up upload");
            return -2;
        }
        String str5 = " type=" + set;
        for (String str6 : linkedHashSet) {
            if (TextUtils.isEmpty(str6)) {
                cn.jiguang.ad.d.f("ReportUtils", "can't get url, give up upload");
            } else if (Build.VERSION.SDK_INT < 28 || str6.startsWith("https://")) {
                if (!str6.endsWith(e)) {
                    str6 = str6 + e;
                }
                String str7 = str6;
                cn.jiguang.ad.d.b("ReportUtils", "upload" + str5 + " to url:" + str7);
                n nVarA = b.a(context, str7, bArr, i, 3, 1);
                int iA = nVarA.a();
                if (iA == -3) {
                    d.a(context, cn.jiguang.ac.a.a(context));
                    return -2;
                }
                if (iA == -1) {
                    sbAppend = new StringBuilder("upload").append(str5).append(" error:").append(nVarA.b());
                } else {
                    if (iA == 0) {
                        return 0;
                    }
                    sbAppend = new StringBuilder("upload").append(str5).append(" failed");
                }
                cn.jiguang.ad.d.b("ReportUtils", sbAppend.toString());
            } else {
                cn.jiguang.ad.d.f("ReportUtils", "won't use http at device since 28");
            }
        }
        return -1;
    }

    private static Pair<byte[], Integer> a(String str, boolean z, int i) throws Throwable {
        try {
            byte[] bArrA = cn.jiguang.f.k.a(str.getBytes("UTF-8"));
            int iA = cn.jiguang.ar.d.a();
            return new Pair<>(cn.jiguang.ar.d.a(bArrA, cn.jiguang.ar.d.a(iA), "iop203040506aPk!", true), Integer.valueOf(iA));
        } catch (UnsupportedEncodingException | IOException | Exception unused) {
            return null;
        }
    }

    public static String a(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.c())).longValue();
        if (jLongValue == 0) {
            cn.jiguang.ad.d.b("ReportUtils", " miss uid,generate report token failed");
            return null;
        }
        String strK = cn.jiguang.f.i.k(jLongValue + cn.jiguang.f.i.e((String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.e())) + str);
        if (cn.jiguang.f.i.a(strK)) {
            return null;
        }
        try {
            return Base64.encodeToString((jLongValue + ":" + strK + ":" + str2).getBytes(), 10);
        } catch (Exception unused) {
            cn.jiguang.ad.d.h("getBasicAuthorization", "basic authorization encode failed");
            return null;
        }
    }

    public static String a(String str) {
        try {
            return cn.jiguang.ar.d.a(str, cn.jiguang.ar.a.a());
        } catch (Throwable unused) {
            cn.jiguang.ad.d.h("getBasicAuthorization", "basic authorization encode failed");
            return null;
        }
    }

    private static ArrayList<JSONArray> a(JSONArray jSONArray, int i, int i2) {
        ArrayList<JSONArray> arrayList = new ArrayList<>();
        if (jSONArray != null && jSONArray.length() != 0) {
            if (jSONArray.length() == 1) {
                arrayList.add(jSONArray);
                return arrayList;
            }
            JSONArray jSONArray2 = new JSONArray();
            int i3 = 0;
            int i4 = 0;
            for (int length = jSONArray.length() - 1; length >= 0; length--) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(length);
                try {
                    int iB = cn.jiguang.f.g.b(jSONObjectOptJSONObject);
                    if (iB != 0) {
                        int i5 = i3 + iB;
                        if (i5 > 204800) {
                            break;
                        }
                        int i6 = i4 + iB;
                        if (i6 > 40960) {
                            if (jSONArray2.length() > 0) {
                                arrayList.add(jSONArray2);
                            }
                            JSONArray jSONArray3 = new JSONArray();
                            try {
                                jSONArray3.put(jSONObjectOptJSONObject);
                                jSONArray2 = jSONArray3;
                            } catch (Throwable th) {
                                th = th;
                                jSONArray2 = jSONArray3;
                                cn.jiguang.ad.d.f("ReportUtils", "partition exception:" + th);
                            }
                        } else {
                            jSONArray2.put(jSONObjectOptJSONObject);
                            iB = i6;
                        }
                        i4 = iB;
                        i3 = i5;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (jSONArray2.length() > 0) {
                arrayList.add(jSONArray2);
            }
        }
        return arrayList;
    }

    public static Set<String> a(JSONObject jSONObject) {
        return jSONObject == null ? new HashSet() : c(jSONObject.optJSONArray("content"));
    }

    public static JSONObject a(Context context) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(JThirdPlatFormInterface.KEY_PLATFORM, "a");
            long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.c())).longValue();
            if (jLongValue == 0) {
                cn.jiguang.ad.d.f("ReportUtils", "miss uid when wrap container info");
                return null;
            }
            jSONObject.put("uid", jLongValue);
            String strA = cn.jiguang.ac.a.a(context);
            if (cn.jiguang.f.i.a(strA)) {
                cn.jiguang.ad.d.h("ReportUtils", "miss app_key when wrap container info");
                return null;
            }
            jSONObject.put("app_key", strA);
            ActionManager.getInstance().wrapSdkVersionInfo(jSONObject);
            jSONObject.put("core_sdk_ver", cn.jiguang.a.a.b);
            String strC = cn.jiguang.ac.a.c(context);
            if (cn.jiguang.f.i.a(strC)) {
                cn.jiguang.ad.d.g("ReportUtils", "miss channel when wrap container info,but continue report...");
            } else {
                jSONObject.put("channel", strC);
            }
            Pair<String, Integer> pairB = cn.jiguang.c.b.b(context);
            if (pairB == null || cn.jiguang.f.i.a((String) pairB.first)) {
                cn.jiguang.ad.d.g("ReportUtils", "miss app version when wrap container info,but continue report...");
            } else {
                jSONObject.put("app_version", pairB.first);
            }
            return jSONObject;
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("ReportUtils", "wrapContainerInfo exception:" + th);
            return null;
        }
    }

    private static JSONObject a(JSONArray jSONArray, JSONObject jSONObject) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("content", jSONArray);
        } catch (JSONException unused) {
        }
        cn.jiguang.f.g.a(jSONObject2, jSONObject);
        return jSONObject2;
    }

    public static void a(Context context, int i, JSONObject jSONObject, byte[] bArr, int i2, File file, Set<String> set, ReportCallBack reportCallBack) {
        String str;
        try {
            cn.jiguang.ad.d.b("ReportUtils", "onTcpReportResult, types=" + set + " code=" + i);
            if (i == -3) {
                d.a(context, cn.jiguang.ac.a.a(context));
            } else if (i != 0) {
                i = a(context, bArr, i2, set);
                if (i == 0) {
                    if (JConstants.DEBUG_MODE && JConstants.INTERNAL_USE) {
                        str = "http upload success, json=" + cn.jiguang.f.g.a(jSONObject);
                        cn.jiguang.ad.d.b("ReportUtils", str);
                    }
                    cn.jiguang.f.e.a(file);
                }
            } else {
                if (JConstants.DEBUG_MODE && JConstants.INTERNAL_USE) {
                    str = "tcp upload success, json=" + cn.jiguang.f.g.a(jSONObject);
                    cn.jiguang.ad.d.b("ReportUtils", str);
                }
                cn.jiguang.f.e.a(file);
            }
            if (reportCallBack != null) {
                reportCallBack.onFinish(i);
            }
            d.a(file);
        } catch (Throwable unused) {
            if (reportCallBack != null) {
                reportCallBack.onFinish(i);
            }
            d.a(file);
        }
    }

    public static void a(Context context, Object obj) {
        try {
            if (b(obj)) {
                cn.jiguang.aq.a.a("BUILD_REPORT", new l(obj, context));
            } else {
                cn.jiguang.ad.d.b("ReportUtils", "data is invalid or empty");
            }
            d.b(context);
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("ReportUtils", "report e:" + th);
        }
    }

    public static void a(Context context, String str, Object obj) {
        try {
            cn.jiguang.ad.d.b("ReportUtils", "going to report data at push service");
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("sdk_type", str);
            }
            if (b(obj)) {
                bundle.putString("report_data", obj.toString());
            }
            cn.jiguang.a.a.a(context, "a1", bundle);
        } catch (Throwable th) {
            try {
                cn.jiguang.ad.d.b("ReportUtils", "reportAtPushService", th);
            } catch (Throwable th2) {
                cn.jiguang.ad.d.b("ReportUtils", "reportAtPushService", th2);
            }
        }
    }

    static /* synthetic */ void a(Context context, JSONArray jSONArray, Set set) {
        try {
            String strA = a.a().a(context).a((Set<String>) set);
            JSONObject jSONObjectA = a(context);
            boolean z = jSONObjectA != null;
            String str = File.separator + strA + File.separator + (z ? "tmp" : "nowrap");
            Iterator<JSONArray> it = a(jSONArray, 40960, 204800).iterator();
            while (it.hasNext()) {
                try {
                    JSONObject jSONObjectA2 = a(it.next(), jSONObjectA);
                    File fileA = d.a(context, str, jSONObjectA2, z);
                    cn.jiguang.ad.d.b("ReportUtils", "save report types=" + set + " at " + str + File.separator + fileA.getName());
                    if (z) {
                        cn.jiguang.aq.a.a("UPLOAD_REPORT", new m(context, set, jSONObjectA2, fileA));
                    }
                } catch (Throwable unused) {
                    cn.jiguang.ad.a.a(JConstants.SDK_TYPE, "ReportUtils", false, 5, "buildReport [for item]");
                }
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.h("ReportUtils", "report exception:" + th);
        }
    }

    public static void a(Context context, JSONObject jSONObject, ReportCallBack reportCallBack) {
        if (jSONObject != null) {
            try {
                if (jSONObject.length() > 0) {
                    JSONObject jSONObjectA = a(context);
                    if (jSONObjectA == null) {
                        cn.jiguang.ad.d.h("ReportUtils", "wrap data failed");
                        if (reportCallBack != null) {
                            reportCallBack.onFinish(-1);
                        }
                    } else {
                        String strB = b(jSONObject);
                        JSONObject jSONObjectA2 = a(new JSONArray().put(jSONObject), jSONObjectA);
                        HashSet hashSet = new HashSet();
                        hashSet.add(strB);
                        cn.jiguang.ad.d.b("ReportUtils", "reportWithoutStore type=" + strB);
                        a(context, hashSet, jSONObjectA2, null, reportCallBack);
                    }
                }
            } catch (Throwable th) {
                cn.jiguang.ad.d.h("ReportUtils", "reportWithoutStore exception:" + th);
            }
        }
        d.b(context);
    }

    public static boolean a(Context context, String str, JSONObject jSONObject) {
        try {
            if (cn.jiguang.f.i.a(str)) {
                cn.jiguang.ad.d.b("ReportUtils", "file_name is null , give up save ");
                return false;
            }
            if (context != null) {
                return cn.jiguang.f.e.a(cn.jiguang.f.e.a(context, str), jSONObject != null ? jSONObject.toString() : "");
            }
            cn.jiguang.ad.d.b("ReportUtils", "context is null , give up save " + str);
            return false;
        } catch (Throwable th) {
            cn.jiguang.ad.d.b("ReportUtils", "writeLogFile e:" + th);
            return false;
        }
    }

    private static String b(JSONObject jSONObject) {
        if (jSONObject != null) {
            return jSONObject.optString("type");
        }
        return null;
    }

    private static JSONArray b(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() == 0) {
            return jSONArray;
        }
        JSONArray jSONArray2 = new JSONArray();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
            if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.length() > 0) {
                jSONArray2.put(jSONObjectOptJSONObject);
            }
        }
        return jSONArray2;
    }

    private static boolean b(Object obj) {
        if (obj instanceof String) {
            return ((String) obj).length() > 2;
        }
        if (obj instanceof JSONObject) {
            return ((JSONObject) obj).length() > 0;
        }
        return (obj instanceof JSONArray) && b((JSONArray) obj).length() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<String> c(JSONArray jSONArray) {
        HashSet hashSet = new HashSet();
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                String strB = b(jSONArray.optJSONObject(i));
                if (strB == null) {
                    strB = "";
                }
                hashSet.add(strB);
            }
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JSONArray c(Object obj) {
        JSONArray jSONArrayPut;
        try {
            if (obj instanceof String) {
                try {
                    try {
                        jSONArrayPut = new JSONArray((String) obj);
                    } catch (Throwable unused) {
                    }
                } catch (Throwable unused2) {
                    jSONArrayPut = new JSONArray().put(new JSONObject((String) obj));
                }
            } else if (obj instanceof JSONObject) {
                jSONArrayPut = new JSONArray().put(obj);
            } else {
                jSONArrayPut = obj instanceof JSONArray ? (JSONArray) obj : null;
            }
            JSONArray jSONArrayB = b(jSONArrayPut);
            if (jSONArrayB == null || jSONArrayB.length() <= 0) {
                return null;
            }
            return jSONArrayB;
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("ReportUtils", "adapt JSONArray e:" + th);
        }
    }
}
