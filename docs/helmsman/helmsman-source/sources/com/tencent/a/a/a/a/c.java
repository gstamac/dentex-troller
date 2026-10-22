package com.tencent.a.a.a.a;

import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public final class c {
    String a = null;
    String b = null;
    String c = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    long T = 0;

    static c e(String str) {
        c cVar = new c();
        if (h.b(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.isNull("ui")) {
                    cVar.a = jSONObject.getString("ui");
                }
                if (!jSONObject.isNull("mc")) {
                    cVar.b = jSONObject.getString("mc");
                }
                if (!jSONObject.isNull("mid")) {
                    cVar.c = jSONObject.getString("mid");
                }
                if (!jSONObject.isNull("ts")) {
                    cVar.T = jSONObject.getLong("ts");
                }
            } catch (JSONException e) {
                Log.w("MID", e);
            }
        }
        return cVar;
    }

    private JSONObject n() {
        JSONObject jSONObject = new JSONObject();
        try {
            h.a(jSONObject, "ui", this.a);
            h.a(jSONObject, "mc", this.b);
            h.a(jSONObject, "mid", this.c);
            jSONObject.put("ts", this.T);
        } catch (JSONException e) {
            Log.w("MID", e);
        }
        return jSONObject;
    }

    public final String a() {
        return this.c;
    }

    public final String toString() {
        return n().toString();
    }
}
