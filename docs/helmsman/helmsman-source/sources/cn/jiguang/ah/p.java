package cn.jiguang.ah;

import cn.jpush.android.api.JThirdPlatFormInterface;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class p {
    public int a;
    public String b;
    public String c;
    public long d;
    public String e;
    public double f;
    public double g;
    public long h;
    private int i = 0;
    private int j = 0;

    public p(int i, String str, String str2, long j, String str3, double d, double d2, long j2) {
        this.a = i;
        this.b = str;
        this.c = str2;
        this.d = j;
        this.e = str3;
        this.f = d;
        this.g = d2;
        this.h = j2;
    }

    public final JSONObject a(Set<String> set) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", this.a);
            jSONObject.put("appkey", this.b);
            jSONObject.put("sdkver", this.c);
            boolean z = false;
            jSONObject.put(JThirdPlatFormInterface.KEY_PLATFORM, 0);
            long j = this.d;
            if (j != 0) {
                jSONObject.put("uid", j);
            }
            String str = this.e;
            if (str != null) {
                jSONObject.put("opera", str);
            }
            double d = this.f;
            double d2 = this.g;
            if (d > -90.0d && d < 90.0d && d2 > -180.0d && d2 < 180.0d) {
                z = true;
            }
            if (z) {
                jSONObject.put("lat", d);
                jSONObject.put("lng", this.g);
                jSONObject.put("time", this.h);
            }
            if (set != null && !set.isEmpty()) {
                JSONArray jSONArray = new JSONArray();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    jSONArray.put(it.next());
                }
                jSONObject.put("fail_ips", jSONArray);
            }
            int i = this.i;
            if (i != 0) {
                jSONObject.put("ips_flag", i);
            }
            int i2 = this.j;
            if (i2 != 0) {
                jSONObject.put("report_flag", i2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
