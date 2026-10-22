package cn.jiguang.z;

import android.content.Context;
import cn.jiguang.internal.JConstants;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class d implements Runnable {
    final /* synthetic */ a a;
    private Context b;
    private JSONObject c;

    d(a aVar, Context context, JSONObject jSONObject) {
        this.a = aVar;
        this.b = context;
        this.c = jSONObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            long jD = cn.jiguang.l.e.d(this.b, "JWakecmd");
            long jCurrentTimeMillis = System.currentTimeMillis();
            JSONObject jSONObject = this.c;
            if (jSONObject == null && jCurrentTimeMillis - jD < JConstants.HOUR) {
                cn.jiguang.w.a.d("JWake", "is not cmd wake time");
                return;
            }
            a.a(this.a, this.b, jSONObject);
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWake", "WakeAction failed:" + th.getMessage());
        }
    }
}
