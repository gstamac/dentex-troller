package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class l extends e {
    private com.tencent.wxop.stat.common.b a;
    private JSONObject m;

    public l(Context context, int i, JSONObject jSONObject, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.m = null;
        this.a = new com.tencent.wxop.stat.common.b(context);
        this.m = jSONObject;
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.SESSION_ENV;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) throws JSONException {
        if (this.e != null) {
            jSONObject.put("ut", this.e.d());
        }
        JSONObject jSONObject2 = this.m;
        if (jSONObject2 != null) {
            jSONObject.put("cfg", jSONObject2);
        }
        if (com.tencent.wxop.stat.common.k.y(this.l)) {
            jSONObject.put("ncts", 1);
        }
        this.a.a(jSONObject, (Thread) null);
        return true;
    }
}
