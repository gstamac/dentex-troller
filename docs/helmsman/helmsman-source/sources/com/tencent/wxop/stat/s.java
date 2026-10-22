package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class s implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;
    final /* synthetic */ com.tencent.wxop.stat.a.c c;

    s(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo, com.tencent.wxop.stat.a.c cVar) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
        this.c = cVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Context context = this.a;
            com.tencent.wxop.stat.a.b bVar = new com.tencent.wxop.stat.a.b(context, StatServiceImpl.a(context, false, this.b), this.c.a, this.b);
            bVar.b().b = this.c.b;
            new aq(bVar).a();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
