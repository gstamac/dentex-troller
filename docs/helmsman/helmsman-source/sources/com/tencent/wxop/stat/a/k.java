package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class k extends e {
    Long a;
    String m;
    String n;

    public k(Context context, String str, String str2, int i, Long l, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.n = str;
        this.m = str2;
        this.a = l;
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.PAGE_VIEW;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) throws JSONException {
        q.a(jSONObject, "pi", this.m);
        q.a(jSONObject, "rf", this.n);
        Long l = this.a;
        if (l == null) {
            return true;
        }
        jSONObject.put("du", l);
        return true;
    }
}
