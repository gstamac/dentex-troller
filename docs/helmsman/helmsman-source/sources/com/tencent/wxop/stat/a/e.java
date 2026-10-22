package com.tencent.wxop.stat.a;

import android.content.Context;
import com.facebook.appevents.AppEventsConstants;
import com.tencent.wxop.stat.StatConfig;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.au;
import com.tencent.wxop.stat.common.q;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public abstract class e {
    protected static String j;
    private StatSpecifyReportedInfo a;
    protected String b;
    protected long c = System.currentTimeMillis() / 1000;
    protected int d;
    protected com.tencent.wxop.stat.common.a e;
    protected int f;
    protected String g;
    protected String h;
    protected String i;
    protected boolean k;
    protected Context l;

    e(Context context, int i, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.b = null;
        this.e = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.k = false;
        this.a = null;
        this.l = context;
        this.d = i;
        this.h = StatConfig.getInstallChannel(context);
        this.i = com.tencent.wxop.stat.common.k.j(context);
        this.b = StatConfig.getAppKey(context);
        if (statSpecifyReportedInfo != null) {
            this.a = statSpecifyReportedInfo;
            if (com.tencent.wxop.stat.common.k.c(statSpecifyReportedInfo.getAppKey())) {
                this.b = statSpecifyReportedInfo.getAppKey();
            }
            if (com.tencent.wxop.stat.common.k.c(statSpecifyReportedInfo.getInstallChannel())) {
                this.h = statSpecifyReportedInfo.getInstallChannel();
            }
            if (com.tencent.wxop.stat.common.k.c(statSpecifyReportedInfo.getVersion())) {
                this.i = statSpecifyReportedInfo.getVersion();
            }
            this.k = statSpecifyReportedInfo.isImportant();
        }
        this.g = StatConfig.getCustomUserId(context);
        this.e = au.a(context).b(context);
        this.f = a() != f.NETWORK_DETECTOR ? com.tencent.wxop.stat.common.k.s(context).intValue() : -f.NETWORK_DETECTOR.a();
        if (com.tencent.a.a.a.a.h.c(j)) {
            return;
        }
        String localMidOnly = StatConfig.getLocalMidOnly(context);
        j = localMidOnly;
        if (com.tencent.wxop.stat.common.k.c(localMidOnly)) {
            return;
        }
        j = AppEventsConstants.EVENT_PARAM_VALUE_NO;
    }

    public abstract f a();

    public abstract boolean a(JSONObject jSONObject);

    public boolean b(JSONObject jSONObject) {
        try {
            q.a(jSONObject, "ky", this.b);
            jSONObject.put("et", a().a());
            com.tencent.wxop.stat.common.a aVar = this.e;
            if (aVar != null) {
                jSONObject.put("ui", aVar.b());
                q.a(jSONObject, "mc", this.e.c());
                int iD = this.e.d();
                jSONObject.put("ut", iD);
                if (iD == 0 && com.tencent.wxop.stat.common.k.w(this.l) == 1) {
                    jSONObject.put("ia", 1);
                }
            }
            q.a(jSONObject, "cui", this.g);
            if (a() != f.SESSION_ENV) {
                q.a(jSONObject, "av", this.i);
                q.a(jSONObject, "ch", this.h);
            }
            if (this.k) {
                jSONObject.put("impt", 1);
            }
            q.a(jSONObject, "mid", j);
            jSONObject.put("idx", this.f);
            jSONObject.put("si", this.d);
            jSONObject.put("ts", this.c);
            jSONObject.put("dts", com.tencent.wxop.stat.common.k.a(this.l, false));
            return a(jSONObject);
        } catch (Throwable unused) {
            return false;
        }
    }

    public long c() {
        return this.c;
    }

    public StatSpecifyReportedInfo d() {
        return this.a;
    }

    public Context e() {
        return this.l;
    }

    public boolean f() {
        return this.k;
    }

    public String g() {
        try {
            JSONObject jSONObject = new JSONObject();
            b(jSONObject);
            return jSONObject.toString();
        } catch (Throwable unused) {
            return "";
        }
    }
}
