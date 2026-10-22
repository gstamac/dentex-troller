package cn.jiguang.analytics.page;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
final class a implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ PushSA b;

    a(PushSA pushSA, Context context) {
        this.b = pushSA;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.b.sendLogRoutine(this.a);
        } catch (Throwable unused) {
        }
    }
}
