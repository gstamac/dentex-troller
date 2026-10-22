package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class q implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ Throwable b;

    q(Context context, Throwable th) {
        this.a = context;
        this.b = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            if (StatConfig.isEnableStatService()) {
                Context context = this.a;
                new aq(new com.tencent.wxop.stat.a.d(context, StatServiceImpl.a(context, false, (StatSpecifyReportedInfo) null), 99, this.b, com.tencent.wxop.stat.a.i.a)).a();
            }
        } catch (Throwable th) {
            StatServiceImpl.q.e("reportSdkSelfException error: " + th);
        }
    }
}
