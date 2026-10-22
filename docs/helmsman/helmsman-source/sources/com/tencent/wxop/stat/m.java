package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class m implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ StatSpecifyReportedInfo b;

    m(Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = context;
        this.b = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Context context = this.a;
        if (context == null) {
            StatServiceImpl.q.error("The Context of StatService.onPause() can not be null!");
        } else {
            StatServiceImpl.trackEndPage(context, com.tencent.wxop.stat.common.k.h(context), this.b);
        }
    }
}
