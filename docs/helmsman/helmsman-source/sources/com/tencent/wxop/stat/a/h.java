package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatAppMonitor;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.common.q;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class h extends e {
    private static String m;
    private static String n;
    private StatAppMonitor a;

    public h(Context context, int i, StatAppMonitor statAppMonitor, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.a = null;
        this.a = statAppMonitor.m53clone();
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.MONITOR_STAT;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) throws JSONException {
        StatAppMonitor statAppMonitor = this.a;
        if (statAppMonitor == null) {
            return false;
        }
        jSONObject.put("na", statAppMonitor.getInterfaceName());
        jSONObject.put("rq", this.a.getReqSize());
        jSONObject.put("rp", this.a.getRespSize());
        jSONObject.put("rt", this.a.getResultType());
        jSONObject.put("tm", this.a.getMillisecondsConsume());
        jSONObject.put("rc", this.a.getReturnCode());
        jSONObject.put("sp", this.a.getSampling());
        if (n == null) {
            n = com.tencent.wxop.stat.common.k.n(this.l);
        }
        q.a(jSONObject, "av", n);
        if (m == null) {
            m = com.tencent.wxop.stat.common.k.i(this.l);
        }
        q.a(jSONObject, "op", m);
        jSONObject.put("cn", com.tencent.wxop.stat.a.a(this.l).b());
        return true;
    }
}
