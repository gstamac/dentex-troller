package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class an implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;

    an(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            StatServiceImpl.a(this.a, false, this.b);
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
        }
    }
}
