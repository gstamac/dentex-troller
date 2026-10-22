package cn.jiguang.z;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Runnable {
    final /* synthetic */ a a;
    private Context b;

    private c(a aVar, Context context) {
        this.a = aVar;
        this.b = context;
    }

    /* synthetic */ c(a aVar, Context context, byte b) {
        this(aVar, context);
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Context context = this.b;
            cn.jiguang.n.a.a(context, cn.jiguang.n.a.d(context));
            this.a.h(this.b);
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWake", "RegisterAction failed:" + th.getMessage());
        }
    }
}
