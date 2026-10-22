package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class r implements Runnable {
    final /* synthetic */ Throwable a;
    final /* synthetic */ Context b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    r(Throwable th, Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = th;
        this.b = context;
        this.c = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.a == null) {
            StatServiceImpl.q.error("The Throwable error message of StatService.reportException() can not be null!");
        } else {
            Context context = this.b;
            new aq(new com.tencent.wxop.stat.a.d(context, StatServiceImpl.a(context, false, this.c), 1, this.a, this.c)).a();
        }
    }
}
