package cn.jiguang.z;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class b implements Runnable {
    final /* synthetic */ a a;
    private Context b;

    b(a aVar, Context context) {
        this.a = aVar;
        this.b = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.a.a(this.b);
            if (cn.jiguang.l.e.h(this.b, "JWake")) {
                this.a.b(this.b, "JWake");
            } else {
                cn.jiguang.w.a.d("JWake", "can't wake because wakeConfig not allow");
            }
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWake", "dealAction throwable:" + th.getMessage());
        }
    }
}
