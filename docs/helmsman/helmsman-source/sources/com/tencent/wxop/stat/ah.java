package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class ah implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    ah(Context context, String str, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = str;
        this.c = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Long l;
        try {
            StatServiceImpl.flushDataToDB(this.a);
            synchronized (StatServiceImpl.o) {
                l = (Long) StatServiceImpl.o.remove(this.b);
            }
            if (l == null) {
                StatServiceImpl.q.e("Starttime for PageID:" + this.b + " not found, lost onResume()?");
                return;
            }
            Long lValueOf = Long.valueOf((System.currentTimeMillis() - l.longValue()) / 1000);
            if (lValueOf.longValue() <= 0) {
                lValueOf = 1L;
            }
            Long l2 = lValueOf;
            String str = StatServiceImpl.n;
            if (str != null && str.equals(this.b)) {
                str = "-";
            }
            Context context = this.a;
            com.tencent.wxop.stat.a.k kVar = new com.tencent.wxop.stat.a.k(context, str, this.b, StatServiceImpl.a(context, false, this.c), l2, this.c);
            if (!this.b.equals(StatServiceImpl.m)) {
                StatServiceImpl.q.warn("Invalid invocation since previous onResume on diff page.");
            }
            new aq(kVar).a();
            String unused = StatServiceImpl.n = this.b;
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.a, th);
        }
    }
}
