package cn.jiguang.b;

import android.content.Context;
import cn.jiguang.ad.d;
import cn.jiguang.ae.k;
import cn.jiguang.api.ReportCallBack;
import cn.jiguang.internal.JConstants;
import com.facebook.devicerequests.internal.DeviceRequestsHelper;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class b extends Thread implements ReportCallBack {
    private static JSONObject a(Context context) {
        JSONArray jSONArrayA = a.a(context);
        if (jSONArrayA == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("crashlogs", jSONArrayA);
            jSONObject.put("network_type", cn.jiguang.f.a.j(context));
            cn.jiguang.d.a.a(context, jSONObject, "crash_log");
            Object objA = cn.jiguang.c.b.a(context);
            JSONObject jSONObject2 = objA instanceof JSONObject ? (JSONObject) objA : null;
            if (jSONObject2 != null && jSONObject2.length() > 0) {
                jSONObject.put(DeviceRequestsHelper.DEVICE_INFO_PARAM, jSONObject2);
            }
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    @Override // cn.jiguang.api.ReportCallBack
    public final void onFinish(int i) {
        d.f("ReportCrashLogDirect", "ReportDirect finish : " + i);
        if (i == 0) {
            a.b(JConstants.getAppContext(null));
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        try {
            Context appContext = JConstants.getAppContext(null);
            if (appContext == null) {
                d.f("ReportCrashLogDirect", "ReportDirect context is null");
                return;
            }
            JSONObject jSONObjectA = a(appContext);
            if (jSONObjectA != null) {
                k.a(appContext, jSONObjectA, this);
            }
        } catch (Throwable th) {
            d.h("ReportCrashLogDirect", "run report crash e:" + th);
        }
    }
}
