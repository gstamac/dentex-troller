package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class ad implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ int b;

    ad(Context context, int i) {
        this.a = context;
        this.b = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            StatServiceImpl.flushDataToDB(this.a);
            au.a(this.a).a(this.b);
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
