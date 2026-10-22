package cn.jiguang.ah;

import android.text.TextUtils;
import cn.jpush.android.api.JThirdPlatFormInterface;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class o {
    public String a;
    public int b;
    public h c;
    public long d;
    public long e;
    public long f;
    public int g;
    public double h;
    public double i;
    public long j;
    public int k;

    private static o a(JSONObject jSONObject) {
        if (jSONObject != null && jSONObject.length() != 0) {
            try {
                o oVar = new o();
                oVar.a = jSONObject.optString("appkey");
                oVar.b = jSONObject.getInt("type");
                oVar.c = h.a(jSONObject.getString("addr"));
                oVar.e = jSONObject.getLong("rtime");
                oVar.f = jSONObject.getLong("interval");
                oVar.g = jSONObject.getInt("net");
                oVar.k = jSONObject.getInt(JThirdPlatFormInterface.KEY_CODE);
                oVar.d = jSONObject.optLong("uid");
                oVar.h = jSONObject.optDouble("lat");
                oVar.i = jSONObject.optDouble("lng");
                oVar.j = jSONObject.optLong("ltime");
                return oVar;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static LinkedList<o> a(String str) {
        LinkedList<o> linkedList = new LinkedList<>();
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONArray(str);
                for (int i = 0; i < jSONArray.length(); i++) {
                    linkedList.add(a(jSONArray.getJSONObject(i)));
                }
            } catch (JSONException unused) {
            }
        }
        return linkedList;
    }

    public final JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(this.a)) {
                jSONObject.put("appkey", this.a);
            }
            jSONObject.put("type", this.b);
            jSONObject.put("addr", this.c.toString());
            jSONObject.put("rtime", this.e);
            jSONObject.put("interval", this.f);
            jSONObject.put("net", this.g);
            jSONObject.put(JThirdPlatFormInterface.KEY_CODE, this.k);
            long j = this.d;
            if (j != 0) {
                jSONObject.put("uid", j);
            }
            double d = this.h;
            double d2 = this.i;
            if (d > -90.0d && d < 90.0d && d2 > -180.0d && d2 < 180.0d) {
                jSONObject.put("lat", d);
                jSONObject.put("lng", this.i);
                jSONObject.put("ltime", this.j);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
