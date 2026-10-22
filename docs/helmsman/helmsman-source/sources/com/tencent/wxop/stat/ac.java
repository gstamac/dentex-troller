package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class ac implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;
    final /* synthetic */ com.tencent.wxop.stat.a.c c;
    final /* synthetic */ int d;

    ac(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo, com.tencent.wxop.stat.a.c cVar, int i) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
        this.c = cVar;
        this.d = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Context context = this.a;
            com.tencent.wxop.stat.a.b bVar = new com.tencent.wxop.stat.a.b(context, StatServiceImpl.a(context, false, this.b), this.c.a, this.b);
            bVar.b().c = this.c.c;
            Long lValueOf = Long.valueOf(this.d);
            bVar.a(Long.valueOf(lValueOf.longValue() <= 0 ? 1L : lValueOf.longValue()).longValue());
            new aq(bVar).a();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
