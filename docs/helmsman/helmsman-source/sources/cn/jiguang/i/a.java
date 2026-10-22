package cn.jiguang.i;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.api.ReportCallBack;
import cn.jiguang.l.e;

/* JADX INFO: loaded from: classes.dex */
public final class a implements ReportCallBack {
    private String a;
    private final Context b;
    private final String c;
    private final String d;

    public a(String str, Context context, String str2, String str3) {
        this.a = str;
        this.b = context;
        this.c = str2;
        this.d = str3;
    }

    @Override // cn.jiguang.api.ReportCallBack
    public final void onFinish(int i) {
        if (TextUtils.isEmpty(this.a)) {
            this.a = "";
        }
        e.e(this.b, this.c + this.a);
        if (e.g(this.b, this.c) == 0) {
            e.l(this.b, this.a);
        }
        e.e(this.b, this.d);
    }
}
