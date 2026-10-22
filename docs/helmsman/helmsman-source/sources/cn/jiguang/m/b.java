package cn.jiguang.m;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import cn.jiguang.f.i;
import cn.jiguang.internal.JConstants;
import cn.jiguang.l.e;
import cn.jiguang.l.h;
import com.facebook.devicerequests.internal.DeviceRequestsHelper;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b extends cn.jiguang.l.a {
    private static volatile b c;
    private Context a;
    private JSONObject b;
    private String d;

    public static b a() {
        if (c == null) {
            synchronized (b.class) {
                c = new b();
            }
        }
        return c;
    }

    private static JSONObject f(Context context) {
        String str;
        String str2;
        String str3;
        StringBuilder sbAppend;
        if (context == null) {
            cn.jiguang.w.a.d("JDevice", "when getDeviceInfo, context can't be null");
            return null;
        }
        try {
            try {
                String strA = cn.jiguang.n.a.a();
                try {
                    String strA2 = cn.jiguang.n.a.a(context);
                    String str4 = String.format(Locale.ENGLISH, "%.1f", Double.valueOf(cn.jiguang.n.a.b(context)));
                    String str5 = String.format(Locale.ENGLISH, Build.VERSION.RELEASE, new Object[0]);
                    String str6 = String.format(Locale.ENGLISH, Build.MODEL, new Object[0]);
                    String str7 = String.format(Locale.ENGLISH, Build.BRAND, new Object[0]);
                    String str8 = String.format(Locale.ENGLISH, Build.PRODUCT, new Object[0]);
                    String str9 = String.format(Locale.ENGLISH, Build.FINGERPRINT, new Object[0]);
                    String string = context.getResources().getConfiguration().locale.toString();
                    String str10 = String.format(Locale.ENGLISH, Build.MANUFACTURER, new Object[0]);
                    long rawOffset = ((long) TimeZone.getDefault().getRawOffset()) / JConstants.HOUR;
                    String strC = cn.jiguang.f.a.c("");
                    try {
                        if (rawOffset > 0) {
                            str2 = "JDevice";
                            sbAppend = new StringBuilder("+").append(rawOffset);
                        } else {
                            str2 = "JDevice";
                            sbAppend = rawOffset < 0 ? new StringBuilder("-").append(rawOffset) : new StringBuilder().append(rawOffset);
                        }
                        String string2 = sbAppend.toString();
                        long jC = cn.jiguang.n.a.c(context);
                        long jC2 = cn.jiguang.n.a.c();
                        int iD = cn.jiguang.n.a.d();
                        String strB = cn.jiguang.n.a.b();
                        int iE = cn.jiguang.n.a.e();
                        String strC2 = cn.jiguang.f.a.c();
                        JSONArray jSONArrayF = cn.jiguang.n.a.f();
                        JSONObject jSONObject = new JSONObject();
                        if (TextUtils.isEmpty(strA)) {
                            strA = "";
                        }
                        jSONObject.put("cpu_info", strA);
                        jSONObject.put("cpu_count", iD);
                        jSONObject.put("cpu_max_freq", iE);
                        jSONObject.put("cpu_hardware", strB);
                        jSONObject.put("ram", jC);
                        jSONObject.put("rom", jC2);
                        if (TextUtils.isEmpty(strA2)) {
                            strA2 = "";
                        }
                        jSONObject.put("resolution", strA2);
                        if (TextUtils.isEmpty(str4)) {
                            str4 = "";
                        }
                        jSONObject.put("screensize", str4);
                        if (TextUtils.isEmpty(str5)) {
                            str5 = "";
                        }
                        jSONObject.put("os_version", str5);
                        if (TextUtils.isEmpty(str6)) {
                            str6 = "";
                        }
                        jSONObject.put("model", str6);
                        if (TextUtils.isEmpty(str7)) {
                            str7 = "";
                        }
                        jSONObject.put("brand", str7);
                        if (TextUtils.isEmpty(str8)) {
                            str8 = "";
                        }
                        jSONObject.put("product", str8);
                        TextUtils.isEmpty("");
                        String str11 = "";
                        jSONObject.put("serial", str11);
                        jSONObject.put("fingerprint", TextUtils.isEmpty(str9) ? str11 : str9);
                        jSONObject.put("language", TextUtils.isEmpty(string) ? str11 : string);
                        jSONObject.put("manufacturer", TextUtils.isEmpty(str10) ? str11 : str10);
                        jSONObject.put("timezone", TextUtils.isEmpty(string2) ? str11 : string2);
                        String strA3 = cn.jiguang.n.c.a();
                        if (TextUtils.isEmpty(strA3)) {
                            strA3 = str11;
                        }
                        jSONObject.put("romversion", strA3);
                        jSONObject.put("mac", TextUtils.isEmpty(strC) ? str11 : strC);
                        TextUtils.isEmpty(str11);
                        jSONObject.put("meid", str11);
                        jSONObject.put("sim_slots", jSONArrayF);
                        if (!TextUtils.isEmpty(strC2)) {
                            str11 = strC2;
                        }
                        jSONObject.put("android_id", str11);
                        JSONObject jSONObjectA = cn.jiguang.q.a.a(context);
                        if (jSONObjectA != null) {
                            jSONObject.put("ids", jSONObjectA);
                        }
                        return jSONObject;
                    } catch (JSONException e) {
                        e = e;
                        str = str2;
                        str3 = "package json exception: " + e.getMessage();
                        cn.jiguang.w.a.d(str, str3);
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        str3 = "getDeviceInfo exception: " + th.getMessage();
                        str = str2;
                        cn.jiguang.w.a.d(str, str3);
                        return null;
                    }
                } catch (JSONException e2) {
                    e = e2;
                    str2 = "JDevice";
                }
            } catch (Throwable th2) {
                th = th2;
                str2 = "JDevice";
            }
        } catch (JSONException e3) {
            e = e3;
            str = "JDevice";
        }
    }

    private static String g(Context context) {
        String packageName;
        PackageInfo packageInfo;
        String str = "";
        try {
            String strC = cn.jiguang.ac.a.c(context);
            String strA = cn.jiguang.ac.a.a(context);
            try {
                packageName = context.getPackageName();
                try {
                    packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                } catch (Throwable th) {
                    th = th;
                    cn.jiguang.w.a.d("JDevice", "getPackageManager failed:" + th.getMessage());
                    packageInfo = null;
                }
            } catch (Throwable th2) {
                th = th2;
                packageName = "";
            }
            String str2 = packageInfo == null ? "" : packageInfo.versionName;
            String strValueOf = packageInfo == null ? "" : String.valueOf(packageInfo.versionCode);
            String str3 = cn.jiguang.a.a.b;
            String strValueOf2 = String.valueOf(cn.jiguang.a.a.c);
            StringBuilder sb = new StringBuilder();
            sb.append(strC);
            sb.append(",");
            if (TextUtils.isEmpty(strA)) {
                strA = "";
            }
            sb.append(strA);
            sb.append(",");
            if (TextUtils.isEmpty(str2)) {
                str2 = "";
            }
            sb.append(str2);
            sb.append(",");
            if (TextUtils.isEmpty(strValueOf)) {
                strValueOf = "";
            }
            sb.append(strValueOf);
            sb.append(",");
            if (TextUtils.isEmpty(str3)) {
                str3 = "";
            }
            sb.append(str3);
            sb.append(",");
            if (TextUtils.isEmpty(strValueOf2)) {
                strValueOf2 = "";
            }
            sb.append(strValueOf2);
            sb.append(",");
            if (!TextUtils.isEmpty(packageName)) {
                str = packageName;
            }
            sb.append(str);
            return sb.toString();
        } catch (Throwable th3) {
            cn.jiguang.w.a.d("JDevice", "getCurrentCondition throwable: " + th3.getMessage());
            return null;
        }
    }

    @Override // cn.jiguang.l.a
    protected final String a(Context context) {
        this.a = context;
        return "JDevice";
    }

    @Override // cn.jiguang.l.a
    protected final void b(Context context, String str) {
        JSONObject jSONObjectF = f(context);
        this.b = jSONObjectF;
        if (jSONObjectF != null) {
            cn.jiguang.w.a.a("JDevice", "collect success:" + this.b);
        } else {
            cn.jiguang.w.a.d("JDevice", "collect failed");
        }
    }

    @Override // cn.jiguang.l.a
    protected final boolean c(Context context, String str) {
        if (!e.b(context, str)) {
            return false;
        }
        JSONObject jSONObject = this.b;
        if (jSONObject == null) {
            cn.jiguang.w.a.d("JDevice", "there are no data to report");
            return false;
        }
        String string = jSONObject.toString();
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        this.d = i.j(string + g(context));
        String strD = e.d(context);
        if (TextUtils.isEmpty(this.d) || TextUtils.equals(this.d, strD)) {
            cn.jiguang.w.a.a("JDevice", "device detail is not change");
            return false;
        }
        cn.jiguang.w.a.a("JDevice", "device detail is change");
        return super.c(context, str);
    }

    @Override // cn.jiguang.l.a
    protected final void d(Context context, String str) {
        JSONObject jSONObject = this.b;
        if (jSONObject == null) {
            cn.jiguang.w.a.a("JDevice", "there are no data to report");
            return;
        }
        cn.jiguang.d.a.a(context, jSONObject, DeviceRequestsHelper.DEVICE_INFO_PARAM);
        h.a(context, this.b, new a(context, this.d, str));
        this.b = null;
    }

    @Override // cn.jiguang.l.a
    public final Object e(Context context) {
        return f(context);
    }
}
