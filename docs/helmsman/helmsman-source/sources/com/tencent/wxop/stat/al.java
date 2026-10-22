package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class al implements Runnable {
    final /* synthetic */ StatAccount a;
    final /* synthetic */ Context b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    al(StatAccount statAccount, Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = statAccount;
        this.b = context;
        this.c = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        StatAccount statAccount = this.a;
        if (statAccount == null || statAccount.getAccount().trim().length() == 0) {
            StatServiceImpl.q.w("account is null or empty.");
        } else {
            StatConfig.setQQ(this.b, this.a.getAccount());
            StatServiceImpl.b(this.b, this.a, this.c);
        }
    }
}
