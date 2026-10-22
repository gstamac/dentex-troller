package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatAccount;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class a extends e {
    private StatAccount a;

    public a(Context context, int i, StatAccount statAccount, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.a = statAccount;
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.ADDITION;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) throws JSONException {
        q.a(jSONObject, "qq", this.a.getAccount());
        jSONObject.put("acc", this.a.toJsonString());
        return true;
    }
}
