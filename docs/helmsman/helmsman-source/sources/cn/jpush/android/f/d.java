package cn.jpush.android.f;

import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class d implements Runnable {
    private String a;
    private long b;
    private JSONObject c;

    public d(String str, long j, JSONObject jSONObject) {
        this.a = str;
        this.b = j;
        this.c = jSONObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        c.a(this.a, this.b, this.c);
    }
}
