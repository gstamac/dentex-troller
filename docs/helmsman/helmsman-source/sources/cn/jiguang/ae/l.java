package cn.jiguang.ae;

import android.content.Context;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
final class l implements Runnable {
    final /* synthetic */ Object a;
    final /* synthetic */ Context b;

    l(Object obj, Context context) {
        this.a = obj;
        this.b = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            JSONArray jSONArrayC = k.c(this.a);
            if (jSONArrayC != null) {
                k.a(this.b, jSONArrayC, k.c(jSONArrayC));
            } else {
                cn.jiguang.ad.d.b("ReportUtils", "data" + this.a + " is empty");
            }
        } catch (Throwable unused) {
        }
    }
}
