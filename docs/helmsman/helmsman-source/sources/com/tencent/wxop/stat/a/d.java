package com.tencent.wxop.stat.a;

import android.content.Context;
import com.tencent.wxop.stat.StatConfig;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import com.tencent.wxop.stat.common.q;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class d extends e {
    private String a;
    private int m;
    private int n;
    private Thread o;

    public d(Context context, int i, int i2, Throwable th, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.n = 100;
        this.o = null;
        a(i2, th);
    }

    public d(Context context, int i, int i2, Throwable th, Thread thread, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.n = 100;
        this.o = null;
        a(i2, th);
        this.o = thread;
    }

    public d(Context context, int i, String str, int i2, int i3, Thread thread, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        super(context, i, statSpecifyReportedInfo);
        this.n = 100;
        this.o = null;
        if (str != null) {
            i3 = i3 <= 0 ? StatConfig.getMaxReportEventLength() : i3;
            if (str.length() <= i3) {
                this.a = str;
            } else {
                this.a = str.substring(0, i3);
            }
        }
        this.o = thread;
        this.m = i2;
    }

    private void a(int i, Throwable th) {
        if (th != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            this.a = stringWriter.toString();
            this.m = i;
            printWriter.close();
        }
    }

    @Override // com.tencent.wxop.stat.a.e
    public f a() {
        return f.ERROR;
    }

    @Override // com.tencent.wxop.stat.a.e
    public boolean a(JSONObject jSONObject) throws JSONException {
        q.a(jSONObject, "er", this.a);
        jSONObject.put("ea", this.m);
        int i = this.m;
        if (i != 2 && i != 3) {
            return true;
        }
        new com.tencent.wxop.stat.common.b(this.l).a(jSONObject, this.o);
        return true;
    }
}
