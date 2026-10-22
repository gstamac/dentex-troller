package cn.jiguang.ah;

import android.text.TextUtils;
import java.util.LinkedHashSet;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class n {
    LinkedHashSet<h> a;
    LinkedHashSet<h> b;
    LinkedHashSet<h> c;
    LinkedHashSet<h> d;
    LinkedHashSet<h> e;
    JSONObject f;
    transient h g;
    boolean h;

    n(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(str);
            this.a = a(jSONObject, "ips");
            this.b = a(jSONObject, "ssl_ips");
            this.c = a(jSONObject, "http_report");
            this.d = a(jSONObject, "https_report");
            this.e = a(jSONObject, "sis_ips");
            this.h = jSONObject.optBoolean("data_report");
            this.f = jSONObject.optJSONObject("tcp_report");
            cn.jiguang.ad.d.b("sis", "get sis=" + jSONObject.toString(2));
        } catch (Throwable unused) {
        }
    }

    private static LinkedHashSet<h> a(JSONObject jSONObject, String str) {
        LinkedHashSet<h> linkedHashSet = new LinkedHashSet<>();
        try {
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(str);
            if (jSONArrayOptJSONArray != null) {
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    h hVarA = h.a(jSONArrayOptJSONArray.optString(i, null));
                    if (hVarA != null && hVarA.a()) {
                        linkedHashSet.add(hVarA);
                    }
                }
            }
        } catch (Throwable unused) {
        }
        return linkedHashSet;
    }

    public final boolean a() {
        LinkedHashSet<h> linkedHashSet = this.a;
        if (linkedHashSet != null && !linkedHashSet.isEmpty()) {
            return false;
        }
        LinkedHashSet<h> linkedHashSet2 = this.b;
        return linkedHashSet2 == null || linkedHashSet2.isEmpty();
    }
}
