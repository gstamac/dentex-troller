package com.amap.api.mapcore.util;

import android.content.Context;
import android.util.Log;
import com.qiniu.android.http.Client;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: AuthManager.java */
/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class ey {
    public static int a = -1;
    public static String b = "";
    private static ff c = null;
    private static String d = "http://apiinit.amap.com/v3/log/init";
    private static String e;

    private static boolean a(Context context, ff ffVar, boolean z) {
        c = ffVar;
        try {
            String strA = a();
            HashMap map = new HashMap();
            map.put(Client.ContentTypeHeader, Client.FormMime);
            map.put("Accept-Encoding", "gzip");
            map.put("Connection", "Keep-Alive");
            map.put("User-Agent", c.d());
            map.put("X-INFO", ez.a(context, c, null, z));
            map.put("logversion", "2.1");
            map.put("platinfo", String.format("platform=Android&sdkversion=%s&product=%s", c.b(), c.a()));
            gw gwVarA = gw.a();
            fh fhVar = new fh();
            fhVar.a(fd.a(context));
            fhVar.a(map);
            fhVar.b(a(context));
            fhVar.a(strA);
            return a(gwVarA.b(fhVar));
        } catch (Throwable th) {
            fk.a(th, "Auth", "getAuth");
            return true;
        }
    }

    @Deprecated
    public static synchronized boolean a(Context context, ff ffVar) {
        return a(context, ffVar, false);
    }

    public static void a(String str) {
        ew.a(str);
    }

    private static String a() {
        return d;
    }

    private static boolean a(byte[] bArr) {
        if (bArr == null) {
            return true;
        }
        try {
            JSONObject jSONObject = new JSONObject(fg.a(bArr));
            if (jSONObject.has("status")) {
                int i = jSONObject.getInt("status");
                if (i == 1) {
                    a = 1;
                } else if (i == 0) {
                    a = 0;
                }
            }
            if (jSONObject.has("info")) {
                b = jSONObject.getString("info");
            }
            if (a == 0) {
                Log.i("AuthFailure", b);
            }
            return a == 1;
        } catch (JSONException e2) {
            fk.a(e2, "Auth", "lData");
            return false;
        } catch (Throwable th) {
            fk.a(th, "Auth", "lData");
            return false;
        }
    }

    private static Map<String, String> a(Context context) {
        HashMap map = new HashMap();
        try {
            map.put("resType", "json");
            map.put("encode", "UTF-8");
            String strA = ez.a();
            map.put("ts", strA);
            map.put("key", ew.f(context));
            map.put("scode", ez.a(context, strA, fg.d("resType=json&encode=UTF-8&key=" + ew.f(context))));
        } catch (Throwable th) {
            fk.a(th, "Auth", "gParams");
        }
        return map;
    }
}
