package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class z implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ com.tencent.wxop.stat.a.c b;
    final /* synthetic */ Context c;
    final /* synthetic */ StatSpecifyReportedInfo d;

    z(String str, com.tencent.wxop.stat.a.c cVar, Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = str;
        this.b = cVar;
        this.c = context;
        this.d = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            if (StatServiceImpl.a(this.a)) {
                StatServiceImpl.q.error("The event_id of StatService.trackCustomEndEvent() can not be null or empty.");
                return;
            }
            Long l = (Long) StatServiceImpl.e.remove(this.b);
            if (l == null) {
                StatServiceImpl.q.warn("No start time found for custom event: " + this.b.toString() + ", lost trackCustomBeginKVEvent()?");
                return;
            }
            Context context = this.c;
            com.tencent.wxop.stat.a.b bVar = new com.tencent.wxop.stat.a.b(context, StatServiceImpl.a(context, false, this.d), this.b.a, this.d);
            bVar.b().c = this.b.c;
            Long lValueOf = Long.valueOf((System.currentTimeMillis() - l.longValue()) / 1000);
            bVar.a(Long.valueOf(lValueOf.longValue() <= 0 ? 1L : lValueOf.longValue()).longValue());
            new aq(bVar).a();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.c, th);
        }
    }
}
