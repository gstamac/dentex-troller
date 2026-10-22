package cn.jiguang.analytics.page;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
final class d implements Runnable {
    boolean a;
    Context b;
    PushSA c;

    public d(boolean z, Context context, PushSA pushSA) {
        this.a = z;
        this.b = context;
        this.c = pushSA;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            if (this.a) {
                this.c.sendLogRoutine(this.b);
            } else {
                this.c.saveLogRoutine(this.b);
            }
        } catch (Throwable unused) {
        }
    }
}
