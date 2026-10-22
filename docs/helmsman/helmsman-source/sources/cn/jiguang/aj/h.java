package cn.jiguang.aj;

import android.os.Message;
import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
final class h extends cn.jiguang.an.a {
    final /* synthetic */ g a;

    h(g gVar) {
        this.a = gVar;
    }

    @Override // cn.jiguang.an.a
    public final void a(Message message) {
        cn.jiguang.ad.d.b("PeriodWorker", "time is up, next period=" + (JConstants.DEFAULT_HEARTBEAT_INTERVAL * 1000));
        g gVar = this.a;
        gVar.c(gVar.c);
    }
}
