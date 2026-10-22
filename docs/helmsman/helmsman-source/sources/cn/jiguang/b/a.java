package cn.jiguang.b;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.f.e;
import cn.jiguang.internal.JConstants;
import com.facebook.share.internal.ShareConstants;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.PatternSyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Thread.UncaughtExceptionHandler {
    private static a b = new a();
    private static int c = 1048576;
    public boolean a;
    private Thread.UncaughtExceptionHandler d = null;

    private a() {
        this.a = true;
        this.a = ((Boolean) cn.jiguang.g.b.a(JConstants.getAppContext(null), cn.jiguang.g.a.b())).booleanValue();
    }

    public static a a() {
        return b;
    }

    private static String a(Throwable th) {
        String string = th.toString();
        try {
            String[] strArrSplit = string.split(":");
            if (strArrSplit.length <= 1) {
                return string;
            }
            for (int length = strArrSplit.length - 1; length >= 0; length--) {
                if (!strArrSplit[length].endsWith("Exception") && !strArrSplit[length].endsWith("Error")) {
                }
                return strArrSplit[length];
            }
            return string;
        } catch (NullPointerException | PatternSyntaxException unused) {
            return string;
        }
    }

    public static JSONArray a(Context context) {
        String strB = e.b(e.a(context, "jpush_uncaughtexception_file"));
        if (TextUtils.isEmpty(strB)) {
            return null;
        }
        try {
            return new JSONArray(strB);
        } catch (JSONException unused) {
            return null;
        }
    }

    private JSONArray a(Context context, Throwable th) {
        String strB = e.b(e.a(context, "jpush_uncaughtexception_file"));
        JSONArray jSONArray = null;
        int length = 0;
        if (!TextUtils.isEmpty(strB)) {
            try {
                JSONArray jSONArray2 = new JSONArray(strB);
                try {
                    length = strB.length();
                } catch (JSONException unused) {
                }
                jSONArray = jSONArray2;
            } catch (JSONException unused2) {
            }
        }
        return a(context, jSONArray, length, th);
    }

    private JSONArray a(Context context, JSONArray jSONArray, int i, Throwable th) {
        JSONObject jSONObjectOptJSONObject;
        long jCurrentTimeMillis = System.currentTimeMillis() + cn.jiguang.d.b.c(context);
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        String string = stringWriter.toString();
        if (jSONArray == null) {
            jSONArray = new JSONArray();
        }
        int i2 = 0;
        while (true) {
            try {
                if (i2 >= jSONArray.length()) {
                    jSONObjectOptJSONObject = null;
                    break;
                }
                jSONObjectOptJSONObject = jSONArray.optJSONObject(i2);
                if (jSONObjectOptJSONObject != null && string.equals(jSONObjectOptJSONObject.getString("stacktrace"))) {
                    jSONObjectOptJSONObject.put("count", jSONObjectOptJSONObject.getInt("count") + 1);
                    jSONObjectOptJSONObject.put("crashtime", jCurrentTimeMillis);
                    break;
                }
                i2++;
            } catch (Throwable unused) {
            }
        }
        if (jSONObjectOptJSONObject == null) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("crashtime", jCurrentTimeMillis);
            jSONObject.put("stacktrace", string);
            jSONObject.put(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, a(th));
            jSONObject.put("count", 1);
            jSONObject.put("networktype", cn.jiguang.f.a.j(context));
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (packageInfo != null) {
                String str = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String string2 = new StringBuilder().append(packageInfo.versionCode).toString();
                jSONObject.put("versionname", str);
                jSONObject.put("versioncode", string2);
            }
            if (i + jSONObject.toString().length() < c) {
                jSONArray.put(jSONObject);
            } else {
                long j = -1;
                int i3 = 0;
                for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                    JSONObject jSONObjectOptJSONObject2 = jSONArray.optJSONObject(i4);
                    if (jSONObjectOptJSONObject2 != null) {
                        long jOptLong = jSONObjectOptJSONObject2.optLong("crashtime");
                        if (j == -1 || jOptLong < j) {
                            i3 = i4;
                            j = jOptLong;
                        }
                    }
                }
                jSONArray.put(i3, jSONObject);
            }
        }
        return jSONArray;
    }

    public static void b(Context context) {
        if (context == null) {
            d.f("JPushCrashHandler", "Action - deleteCrashLog context is null");
        } else {
            e.a(e.a(context, "jpush_uncaughtexception_file"));
        }
    }

    public static void c(Context context) {
        if (context == null) {
            d.f("JPushCrashHandler", "Action - reportCrashLog context is null");
        } else if (cn.jiguang.d.b.a(context)) {
            try {
                new b().start();
            } catch (Throwable th) {
                d.h("JPushCrashHandler", "report crash e:" + th);
            }
        }
    }

    public final void b() {
        if (this.d == null) {
            this.d = Thread.getDefaultUncaughtExceptionHandler();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final void uncaughtException(Thread thread, Throwable th) {
        File fileA;
        if (this.a) {
            d.a("JPushCrashHandler", "enable crash report");
            if (this.a) {
                Context appContext = JConstants.getAppContext(null);
                if (appContext != null) {
                    JSONArray jSONArrayA = a(appContext, th);
                    b(appContext);
                    String string = jSONArrayA != null ? jSONArrayA.toString() : null;
                    if (!TextUtils.isEmpty(string) && (fileA = e.a(appContext, "jpush_uncaughtexception_file")) != null) {
                        e.a(fileA, string);
                    }
                } else {
                    d.h("JPushCrashHandler", "handleException failed: context is null");
                }
            }
            try {
                b bVar = new b();
                bVar.start();
                bVar.join(2000L);
            } catch (InterruptedException unused) {
            } catch (Throwable th2) {
                d.h("JPushCrashHandler", "report crash e:" + th2);
            }
        } else {
            d.a("JPushCrashHandler", "disable crash report");
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.d;
        if (uncaughtExceptionHandler == this || uncaughtExceptionHandler == null) {
            return;
        }
        uncaughtExceptionHandler.uncaughtException(thread, th);
    }
}
