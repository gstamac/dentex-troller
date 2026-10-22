package com.tencent.wxop.stat;

import android.content.Context;
import android.os.Build;
import com.facebook.appevents.AppEventsConstants;
import com.tencent.wxop.stat.common.StatConstants;
import com.tencent.wxop.stat.common.StatLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import java.net.URI;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class StatConfig {
    private static String B;
    private static String C;
    private static StatLogger p = com.tencent.wxop.stat.common.k.b();
    static f a = new f(2);
    static f b = new f(1);
    private static StatReportStrategy q = StatReportStrategy.APP_LAUNCH;
    private static boolean r = false;
    private static boolean s = true;
    private static int t = 30000;
    private static int u = 100000;
    private static int v = 30;
    private static int w = 10;
    private static int x = 100;
    private static int y = 30;
    private static int z = 1;
    static String c = "__HIBERNATE__";
    static String d = "__HIBERNATE__TIME";
    static String e = "__MTA_KILL__";
    private static String A = null;
    private static String D = "mta_channel";
    static String f = "";
    private static int E = 180;
    static boolean g = false;
    static int h = 100;
    static long i = OkHttpUtils.DEFAULT_MILLISECONDS;
    private static int F = 1024;
    static boolean j = true;
    private static long G = 0;
    private static long H = 300000;
    public static boolean isAutoExceptionCaught = true;
    static volatile String k = StatConstants.MTA_SERVER;
    private static volatile String I = StatConstants.MTA_REPORT_FULL_URL;
    private static int J = 0;
    private static volatile int K = 0;
    private static int L = 20;
    private static int M = 0;
    private static boolean N = false;
    private static int O = 4096;
    private static boolean P = false;
    private static String Q = null;
    private static boolean R = false;
    private static g S = null;
    static boolean l = true;
    static int m = 0;
    static long n = OkHttpUtils.DEFAULT_MILLISECONDS;
    static int o = 512;

    static int a() {
        return v;
    }

    static String a(Context context) {
        return com.tencent.wxop.stat.common.q.a(com.tencent.wxop.stat.common.p.a(context, "_mta_ky_tag_", (String) null));
    }

    static String a(String str, String str2) {
        try {
            String string = b.b.getString(str);
            return string != null ? string : str2;
        } catch (Throwable unused) {
            p.w("can't find custom key:" + str);
        }
    }

    static synchronized void a(int i2) {
        K = i2;
    }

    static void a(long j2) {
        com.tencent.wxop.stat.common.p.b(i.a(), c, j2);
        setEnableStatService(false);
        p.warn("MTA is disable for current SDK version");
    }

    static void a(Context context, f fVar) {
        if (fVar.a != b.a) {
            if (fVar.a == a.a) {
                a = fVar;
            }
        } else {
            b = fVar;
            a(fVar.b);
            if (b.b.isNull("iplist")) {
                return;
            }
            a.a(context).a(b.b.getString("iplist"));
        }
    }

    static void a(Context context, f fVar, JSONObject jSONObject) {
        try {
            Iterator<String> itKeys = jSONObject.keys();
            boolean z2 = false;
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                if (next.equalsIgnoreCase("v")) {
                    int i2 = jSONObject.getInt(next);
                    if (fVar.d != i2) {
                        z2 = true;
                    }
                    fVar.d = i2;
                } else if (next.equalsIgnoreCase("c")) {
                    String string = jSONObject.getString("c");
                    if (string.length() > 0) {
                        fVar.b = new JSONObject(string);
                    }
                } else if (next.equalsIgnoreCase("m")) {
                    fVar.c = jSONObject.getString("m");
                }
            }
            if (z2) {
                au auVarA = au.a(i.a());
                if (auVarA != null) {
                    auVarA.a(fVar);
                }
                if (fVar.a == b.a) {
                    a(fVar.b);
                    b(fVar.b);
                }
            }
            a(context, fVar);
        } catch (JSONException e2) {
            p.e((Throwable) e2);
        }
    }

    static void a(Context context, String str) {
        if (str != null) {
            com.tencent.wxop.stat.common.p.b(context, "_mta_ky_tag_", com.tencent.wxop.stat.common.q.b(str));
        }
    }

    static void a(Context context, JSONObject jSONObject) {
        JSONObject jSONObject2;
        f fVar;
        try {
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                if (next.equalsIgnoreCase(Integer.toString(b.a))) {
                    jSONObject2 = jSONObject.getJSONObject(next);
                    fVar = b;
                } else if (next.equalsIgnoreCase(Integer.toString(a.a))) {
                    jSONObject2 = jSONObject.getJSONObject(next);
                    fVar = a;
                } else {
                    if (!next.equalsIgnoreCase("rs")) {
                        return;
                    }
                    StatReportStrategy statReportStrategy = StatReportStrategy.getStatReportStrategy(jSONObject.getInt(next));
                    if (statReportStrategy != null) {
                        q = statReportStrategy;
                        if (isDebugEnable()) {
                            p.d("Change to ReportStrategy:" + statReportStrategy.name());
                        }
                    }
                }
                a(context, fVar, jSONObject2);
            }
        } catch (JSONException e2) {
            p.e((Throwable) e2);
        }
    }

    static void a(JSONObject jSONObject) {
        try {
            StatReportStrategy statReportStrategy = StatReportStrategy.getStatReportStrategy(jSONObject.getInt("rs"));
            if (statReportStrategy != null) {
                setStatSendStrategy(statReportStrategy);
            }
        } catch (JSONException unused) {
            if (isDebugEnable()) {
                p.i("rs not found.");
            }
        }
    }

    static boolean a(int i2, int i3, int i4) {
        return i2 >= i3 && i2 <= i4;
    }

    private static boolean a(String str) {
        if (str == null) {
            return false;
        }
        String str2 = B;
        if (str2 == null) {
            B = str;
            return true;
        }
        if (str2.contains(str)) {
            return false;
        }
        B += "|" + str;
        return true;
    }

    static boolean a(JSONObject jSONObject, String str, String str2) {
        if (jSONObject.isNull(str)) {
            return false;
        }
        String strOptString = jSONObject.optString(str);
        return com.tencent.wxop.stat.common.k.c(str2) && com.tencent.wxop.stat.common.k.c(strOptString) && str2.equalsIgnoreCase(strOptString);
    }

    static void b() {
        M++;
    }

    static void b(int i2) {
        if (i2 < 0) {
            return;
        }
        M = i2;
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0040 A[Catch: Exception -> 0x01bd, TryCatch #0 {Exception -> 0x01bd, blocks: (B:3:0x0004, B:5:0x0010, B:8:0x001c, B:10:0x0023, B:12:0x002b, B:13:0x002d, B:19:0x0040, B:21:0x0046, B:22:0x0060, B:14:0x0032, B:16:0x0036, B:23:0x0079, B:25:0x0082, B:26:0x008a, B:28:0x0094, B:29:0x00ab, B:31:0x00b7, B:32:0x00d0, B:34:0x00e7, B:35:0x00fe, B:38:0x0117, B:39:0x012c, B:41:0x0143, B:42:0x0158, B:44:0x016c, B:45:0x018d, B:47:0x0199, B:50:0x01b5), top: B:55:0x0004 }] */
    /* JADX WARN: Code duplicated, block: B:21:0x0046 A[Catch: Exception -> 0x01bd, TryCatch #0 {Exception -> 0x01bd, blocks: (B:3:0x0004, B:5:0x0010, B:8:0x001c, B:10:0x0023, B:12:0x002b, B:13:0x002d, B:19:0x0040, B:21:0x0046, B:22:0x0060, B:14:0x0032, B:16:0x0036, B:23:0x0079, B:25:0x0082, B:26:0x008a, B:28:0x0094, B:29:0x00ab, B:31:0x00b7, B:32:0x00d0, B:34:0x00e7, B:35:0x00fe, B:38:0x0117, B:39:0x012c, B:41:0x0143, B:42:0x0158, B:44:0x016c, B:45:0x018d, B:47:0x0199, B:50:0x01b5), top: B:55:0x0004 }] */
    static void b(Context context, JSONObject jSONObject) {
        int iIntValue;
        Integer numValueOf;
        try {
            String strOptString = jSONObject.optString(e);
            if (com.tencent.wxop.stat.common.k.c(strOptString)) {
                JSONObject jSONObject2 = new JSONObject(strOptString);
                if (jSONObject2.length() == 0) {
                    return;
                }
                boolean z2 = false;
                if (!jSONObject2.isNull("sm")) {
                    Object obj = jSONObject2.get("sm");
                    if (obj instanceof Integer) {
                        numValueOf = (Integer) obj;
                    } else {
                        if (obj instanceof String) {
                            numValueOf = Integer.valueOf((String) obj);
                        } else {
                            iIntValue = 0;
                        }
                        if (iIntValue > 0) {
                            if (isDebugEnable()) {
                                p.i("match sleepTime:" + iIntValue + " minutes");
                            }
                            com.tencent.wxop.stat.common.p.b(context, d, System.currentTimeMillis() + ((long) (iIntValue * 60 * 1000)));
                            setEnableStatService(false);
                            p.warn("MTA is disable for current SDK version");
                        }
                    }
                    iIntValue = numValueOf.intValue();
                    if (iIntValue > 0) {
                        if (isDebugEnable()) {
                            p.i("match sleepTime:" + iIntValue + " minutes");
                        }
                        com.tencent.wxop.stat.common.p.b(context, d, System.currentTimeMillis() + ((long) (iIntValue * 60 * 1000)));
                        setEnableStatService(false);
                        p.warn("MTA is disable for current SDK version");
                    }
                }
                boolean z3 = true;
                if (a(jSONObject2, "sv", "2.0.3")) {
                    p.i("match sdk version:2.0.3");
                    z2 = true;
                }
                if (a(jSONObject2, "md", Build.MODEL)) {
                    p.i("match MODEL:" + Build.MODEL);
                    z2 = true;
                }
                if (a(jSONObject2, "av", com.tencent.wxop.stat.common.k.j(context))) {
                    p.i("match app version:" + com.tencent.wxop.stat.common.k.j(context));
                    z2 = true;
                }
                if (a(jSONObject2, "mf", Build.MANUFACTURER)) {
                    p.i("match MANUFACTURER:" + Build.MANUFACTURER);
                    z2 = true;
                }
                if (a(jSONObject2, "osv", new StringBuilder().append(Build.VERSION.SDK_INT).toString())) {
                    p.i("match android SDK version:" + Build.VERSION.SDK_INT);
                    z2 = true;
                }
                if (a(jSONObject2, "ov", new StringBuilder().append(Build.VERSION.SDK_INT).toString())) {
                    p.i("match android SDK version:" + Build.VERSION.SDK_INT);
                    z2 = true;
                }
                if (a(jSONObject2, "ui", au.a(context).b(context).b())) {
                    p.i("match imei:" + au.a(context).b(context).b());
                    z2 = true;
                }
                if (a(jSONObject2, "mid", getLocalMidOnly(context))) {
                    p.i("match mid:" + getLocalMidOnly(context));
                } else {
                    z3 = z2;
                }
                if (z3) {
                    a(com.tencent.wxop.stat.common.k.b("2.0.3"));
                }
            }
        } catch (Exception e2) {
            p.e((Throwable) e2);
        }
    }

    static void b(JSONObject jSONObject) {
        if (jSONObject == null || jSONObject.length() == 0) {
            return;
        }
        try {
            b(i.a(), jSONObject);
            String string = jSONObject.getString(c);
            if (isDebugEnable()) {
                p.d("hibernateVer:" + string + ", current version:2.0.3");
            }
            long jB = com.tencent.wxop.stat.common.k.b(string);
            if (com.tencent.wxop.stat.common.k.b("2.0.3") <= jB) {
                a(jB);
            }
        } catch (JSONException unused) {
            p.d("__HIBERNATE__ not found.");
        }
    }

    static int c() {
        return M;
    }

    public static synchronized String getAppKey(Context context) {
        String str = B;
        if (str != null) {
            return str;
        }
        if (context != null && str == null) {
            B = com.tencent.wxop.stat.common.k.f(context);
        }
        String str2 = B;
        if (str2 == null || str2.trim().length() == 0) {
            p.error("AppKey can not be null or empty, please read Developer's Guide first!");
        }
        return B;
    }

    public static int getCurSessionStatReportCount() {
        return K;
    }

    public static g getCustomLogger() {
        return S;
    }

    public static String getCustomProperty(String str) {
        try {
            return a.b.getString(str);
        } catch (Throwable th) {
            p.e(th);
            return null;
        }
    }

    public static String getCustomProperty(String str, String str2) {
        try {
            String string = a.b.getString(str);
            return string != null ? string : str2;
        } catch (Throwable th) {
            p.e(th);
        }
    }

    public static String getCustomUserId(Context context) {
        if (context == null) {
            p.error("Context for getCustomUid is null.");
            return null;
        }
        if (Q == null) {
            Q = com.tencent.wxop.stat.common.p.a(context, "MTA_CUSTOM_UID", "");
        }
        return Q;
    }

    public static long getFlushDBSpaceMS() {
        return n;
    }

    public static synchronized String getInstallChannel(Context context) {
        String str = C;
        if (str != null) {
            return str;
        }
        String strA = com.tencent.wxop.stat.common.p.a(context, D, "");
        C = strA;
        if (strA == null || strA.trim().length() == 0) {
            C = com.tencent.wxop.stat.common.k.g(context);
        }
        String str2 = C;
        if (str2 == null || str2.trim().length() == 0) {
            p.w("installChannel can not be null or empty, please read Developer's Guide first!");
        }
        return C;
    }

    public static String getLocalMidOnly(Context context) {
        return context != null ? com.tencent.a.a.a.a.g.E(context).p().a() : AppEventsConstants.EVENT_PARAM_VALUE_NO;
    }

    public static int getMaxBatchReportCount() {
        return y;
    }

    public static int getMaxDaySessionNumbers() {
        return L;
    }

    public static int getMaxImportantDataSendRetryCount() {
        return x;
    }

    public static int getMaxParallelTimmingEvents() {
        return F;
    }

    public static int getMaxReportEventLength() {
        return O;
    }

    public static int getMaxSendRetryCount() {
        return w;
    }

    public static int getMaxSessionStatReportCount() {
        return J;
    }

    public static int getMaxStoreEventCount() {
        return u;
    }

    public static String getMid(Context context) {
        return getLocalMidOnly(context);
    }

    public static long getMsPeriodForMethodsCalledLimitClear() {
        return i;
    }

    public static int getNumEventsCachedInMemory() {
        return m;
    }

    public static int getNumEventsCommitPerSec() {
        return z;
    }

    public static int getNumOfMethodsCalledLimit() {
        return h;
    }

    public static String getQQ(Context context) {
        return com.tencent.wxop.stat.common.p.a(context, "mta.acc.qq", f);
    }

    public static int getReportCompressedSize() {
        return o;
    }

    public static int getSendPeriodMinutes() {
        return E;
    }

    public static int getSessionTimoutMillis() {
        return t;
    }

    public static String getStatReportHost() {
        return k;
    }

    public static String getStatReportUrl() {
        return I;
    }

    public static StatReportStrategy getStatSendStrategy() {
        return q;
    }

    public static boolean isAutoExceptionCaught() {
        return isAutoExceptionCaught;
    }

    public static boolean isDebugEnable() {
        return r;
    }

    public static boolean isEnableConcurrentProcess() {
        return P;
    }

    public static boolean isEnableSmartReporting() {
        return j;
    }

    public static boolean isEnableStatService() {
        return s;
    }

    public static boolean isReportEventsByOrder() {
        return l;
    }

    public static boolean isXGProMode() {
        return R;
    }

    public static void setAppKey(Context context, String str) {
        StatLogger statLogger;
        String str2;
        if (context == null) {
            statLogger = p;
            str2 = "ctx in StatConfig.setAppKey() is null";
        } else {
            if (str != null && str.length() <= 256) {
                if (B == null) {
                    B = a(context);
                }
                if (a(str) || a(com.tencent.wxop.stat.common.k.f(context))) {
                    a(context, B);
                    return;
                }
                return;
            }
            statLogger = p;
            str2 = "appkey in StatConfig.setAppKey() is null or exceed 256 bytes";
        }
        statLogger.error(str2);
    }

    public static void setAppKey(String str) {
        StatLogger statLogger;
        String str2;
        if (str == null) {
            statLogger = p;
            str2 = "appkey in StatConfig.setAppKey() is null";
        } else if (str.length() <= 256) {
            B = str;
            return;
        } else {
            statLogger = p;
            str2 = "The length of appkey cann't exceed 256 bytes.";
        }
        statLogger.error(str2);
    }

    public static void setAutoExceptionCaught(boolean z2) {
        isAutoExceptionCaught = z2;
    }

    public static void setCustomLogger(g gVar) {
        S = gVar;
    }

    public static void setCustomUserId(Context context, String str) {
        if (context == null) {
            p.error("Context for setCustomUid is null.");
        } else {
            com.tencent.wxop.stat.common.p.b(context, "MTA_CUSTOM_UID", str);
            Q = str;
        }
    }

    public static void setDebugEnable(boolean z2) {
        r = z2;
        com.tencent.wxop.stat.common.k.b().setDebugEnable(z2);
    }

    public static void setEnableConcurrentProcess(boolean z2) {
        P = z2;
    }

    public static void setEnableSmartReporting(boolean z2) {
        j = z2;
    }

    public static void setEnableStatService(boolean z2) {
        s = z2;
        if (z2) {
            return;
        }
        p.warn("!!!!!!MTA StatService has been disabled!!!!!!");
    }

    public static void setFlushDBSpaceMS(long j2) {
        if (j2 > 0) {
            n = j2;
        }
    }

    public static void setInstallChannel(Context context, String str) {
        if (str.length() > 128) {
            p.error("the length of installChannel can not exceed the range of 128 bytes.");
        } else {
            C = str;
            com.tencent.wxop.stat.common.p.b(context, D, str);
        }
    }

    public static void setInstallChannel(String str) {
        if (str.length() > 128) {
            p.error("the length of installChannel can not exceed the range of 128 bytes.");
        } else {
            C = str;
        }
    }

    public static void setMaxBatchReportCount(int i2) {
        if (a(i2, 2, 1000)) {
            y = i2;
        } else {
            p.error("setMaxBatchReportCount can not exceed the range of [2,1000].");
        }
    }

    public static void setMaxDaySessionNumbers(int i2) {
        if (i2 <= 0) {
            p.e("maxDaySessionNumbers must be greater than 0.");
        } else {
            L = i2;
        }
    }

    public static void setMaxImportantDataSendRetryCount(int i2) {
        if (i2 > 100) {
            x = i2;
        }
    }

    public static void setMaxParallelTimmingEvents(int i2) {
        if (a(i2, 1, 4096)) {
            F = i2;
        } else {
            p.error("setMaxParallelTimmingEvents can not exceed the range of [1, 4096].");
        }
    }

    public static void setMaxReportEventLength(int i2) {
        if (i2 <= 0) {
            p.error("maxReportEventLength on setMaxReportEventLength() must greater than 0.");
        } else {
            O = i2;
        }
    }

    public static void setMaxSendRetryCount(int i2) {
        if (a(i2, 1, 1000)) {
            w = i2;
        } else {
            p.error("setMaxSendRetryCount can not exceed the range of [1,1000].");
        }
    }

    public static void setMaxSessionStatReportCount(int i2) {
        if (i2 < 0) {
            p.error("maxSessionStatReportCount cannot be less than 0.");
        } else {
            J = i2;
        }
    }

    public static void setMaxStoreEventCount(int i2) {
        if (a(i2, 0, 500000)) {
            u = i2;
        } else {
            p.error("setMaxStoreEventCount can not exceed the range of [0, 500000].");
        }
    }

    public static void setNumEventsCachedInMemory(int i2) {
        if (i2 >= 0) {
            m = i2;
        }
    }

    public static void setNumEventsCommitPerSec(int i2) {
        if (i2 > 0) {
            z = i2;
        }
    }

    public static void setNumOfMethodsCalledLimit(int i2, long j2) {
        h = i2;
        if (j2 >= 1000) {
            i = j2;
        }
    }

    public static void setQQ(Context context, String str) {
        com.tencent.wxop.stat.common.p.b(context, "mta.acc.qq", str);
        f = str;
    }

    public static void setReportCompressedSize(int i2) {
        if (i2 > 0) {
            o = i2;
        }
    }

    public static void setReportEventsByOrder(boolean z2) {
        l = z2;
    }

    public static void setSendPeriodMinutes(int i2) {
        if (a(i2, 1, 10080)) {
            E = i2;
        } else {
            p.error("setSendPeriodMinutes can not exceed the range of [1, 7*24*60] minutes.");
        }
    }

    public static void setSessionTimoutMillis(int i2) {
        if (a(i2, 1000, 86400000)) {
            t = i2;
        } else {
            p.error("setSessionTimoutMillis can not exceed the range of [1000, 24 * 60 * 60 * 1000].");
        }
    }

    public static void setStatReportUrl(String str) {
        if (str == null || str.length() == 0) {
            p.error("statReportUrl cannot be null or empty.");
            return;
        }
        I = str;
        try {
            k = new URI(I).getHost();
        } catch (Exception e2) {
            p.w(e2);
        }
        if (isDebugEnable()) {
            p.i("url:" + I + ", domain:" + k);
        }
    }

    public static void setStatSendStrategy(StatReportStrategy statReportStrategy) {
        q = statReportStrategy;
        if (statReportStrategy != StatReportStrategy.PERIOD) {
            StatServiceImpl.c = 0L;
        }
        if (isDebugEnable()) {
            p.d("Change to statSendStrategy: " + statReportStrategy);
        }
    }

    public static void setXGProMode(boolean z2) {
        R = z2;
    }
}
