package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class aa implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;
    final /* synthetic */ StatAppMonitor c;

    aa(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo, StatAppMonitor statAppMonitor) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
        this.c = statAppMonitor;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Context context = this.a;
            new aq(new com.tencent.wxop.stat.a.h(context, StatServiceImpl.a(context, false, this.b), this.c, this.b)).a();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
