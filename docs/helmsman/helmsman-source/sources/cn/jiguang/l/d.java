package cn.jiguang.l;

import android.content.Context;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class d implements Runnable {
    final /* synthetic */ a a;
    private Context b;
    private String c;
    private JSONObject d;

    d(a aVar, Context context, String str, JSONObject jSONObject) {
        this.a = aVar;
        this.b = context;
        this.c = str;
        this.d = jSONObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            a.a(this.a, this.b, this.c, this.d);
        } catch (Throwable unused) {
        }
    }
}
