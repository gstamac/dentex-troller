package cn.jiguang.aj;

import android.os.Bundle;
import android.os.Message;
import cn.jiguang.api.JCoreManager;

/* JADX INFO: loaded from: classes.dex */
final class p extends cn.jiguang.an.a {
    final /* synthetic */ o a;

    p(o oVar) {
        this.a = oVar;
    }

    @Override // cn.jiguang.an.a
    public final void a(Message message) {
        long j = message.what - 100000;
        Bundle bundle = new Bundle();
        bundle.putLong("rid", j);
        i.a().a(JCoreManager.getAppContext(null), "tcp_a6", bundle);
    }
}
