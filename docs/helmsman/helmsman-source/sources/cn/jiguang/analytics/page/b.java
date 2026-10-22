package cn.jiguang.analytics.page;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
final class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ PushSA b;

    b(PushSA pushSA, Context context) {
        this.b = pushSA;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.b.saveLogRoutine(this.a);
        } catch (Throwable unused) {
        }
    }
}
