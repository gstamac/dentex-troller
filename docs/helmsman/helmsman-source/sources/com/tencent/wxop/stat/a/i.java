package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatConfig;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.common.q;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class i extends e {
    public static final StatSpecifyReportedInfo a;

    static {
        StatSpecifyReportedInfo statSpecifyReportedInfo = new StatSpecifyReportedInfo();
        a = statSpecifyReportedInfo;
        statSpecifyReportedInfo.setAppKey("A9VH9B8L4GX4");
    }

    public i(Context context) {
        super(context, 0, a);
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.NETWORK_DETECTOR;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) {
        q.a(jSONObject, "actky", StatConfig.getAppKey(this.l));
        return true;
    }
}
