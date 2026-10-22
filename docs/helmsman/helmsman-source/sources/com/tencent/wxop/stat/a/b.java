package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatServiceImpl;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import java.util.Map;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class b extends e {
    protected c a;
    private long m;

    public b(Context context, int i, String str, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        c cVar = new c();
        this.a = cVar;
        this.m = -1L;
        cVar.a = str;
    }

    private void h() {
        Properties commonKeyValueForKVEvent;
        if (this.a.a == null || (commonKeyValueForKVEvent = StatServiceImpl.getCommonKeyValueForKVEvent(this.a.a)) == null || commonKeyValueForKVEvent.size() <= 0) {
            return;
        }
        if (this.a.c == null || this.a.c.length() == 0) {
            this.a.c = new JSONObject(commonKeyValueForKVEvent);
            return;
        }
        for (Map.Entry entry : commonKeyValueForKVEvent.entrySet()) {
            try {
                this.a.c.put(entry.getKey().toString(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.CUSTOM;
    }

    public void a(long j) {
        this.m = j;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) throws JSONException {
        Object obj;
        String str;
        jSONObject.put("ei", this.a.a);
        long j = this.m;
        if (j > 0) {
            jSONObject.put("du", j);
        }
        if (this.a.b == null) {
            h();
            obj = this.a.c;
            str = "kv";
        } else {
            obj = this.a.b;
            str = "ar";
        }
        jSONObject.put(str, obj);
        return true;
    }

    public c b() {
        return this.a;
    }
}
