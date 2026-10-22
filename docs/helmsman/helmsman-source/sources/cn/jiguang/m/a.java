package cn.jiguang.m;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.api.ReportCallBack;
import cn.jiguang.l.e;

/* JADX INFO: loaded from: classes.dex */
public final class a implements ReportCallBack {
    private Context a;
    private String b;
    private String c;

    public a(Context context, String str, String str2) {
        this.a = context;
        this.b = str;
        this.c = str2;
    }

    @Override // cn.jiguang.api.ReportCallBack
    public final void onFinish(int i) {
        cn.jiguang.w.a.a("DeviceReport", "report finish code:" + i);
        if (i != 0) {
            return;
        }
        e.e(this.a, this.c);
        if (TextUtils.isEmpty(this.b)) {
            return;
        }
        e.p(this.a, this.b);
    }
}
