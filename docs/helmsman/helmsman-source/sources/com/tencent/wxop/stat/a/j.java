package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class j extends e {
    private static String a;
    private String m;
    private String n;

    public j(Context context, int i, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.m = null;
        this.n = null;
        this.m = com.tencent.wxop.stat.a.a(context).b();
        if (a == null) {
            a = com.tencent.wxop.stat.common.k.i(context);
        }
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.NETWORK_MONITOR;
    }

    public void a(String str) {
        this.n = str;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) throws JSONException {
        q.a(jSONObject, "op", a);
        q.a(jSONObject, "cn", this.m);
        jSONObject.put("sp", this.n);
        return true;
    }
}
