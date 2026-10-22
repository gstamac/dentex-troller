package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class aj implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;

    aj(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Context context = this.a;
        if (context == null) {
            StatServiceImpl.q.error("The Context of StatService.onResume() can not be null!");
        } else {
            StatServiceImpl.trackBeginPage(context, com.tencent.wxop.stat.common.k.h(context), this.b);
        }
    }
}
