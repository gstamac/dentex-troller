package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class am implements Runnable {
    final /* synthetic */ StatGameUser a;
    final /* synthetic */ Context b;
    final /* synthetic */ StatSpecifyReportedInfo c;

    am(StatGameUser statGameUser, Context context, StatSpecifyReportedInfo statSpecifyReportedInfo) {
        this.a = statGameUser;
        this.b = context;
        this.c = statSpecifyReportedInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        StatGameUser statGameUser = this.a;
        if (statGameUser == null) {
            StatServiceImpl.q.error("The gameUser of StatService.reportGameUser() can not be null!");
            return;
        }
        if (statGameUser.getAccount() == null || this.a.getAccount().length() == 0) {
            StatServiceImpl.q.error("The account of gameUser on StatService.reportGameUser() can not be null or empty!");
            return;
        }
        try {
            Context context = this.b;
            new aq(new com.tencent.wxop.stat.a.g(context, StatServiceImpl.a(context, false, this.c), this.a, this.c)).a();
        } catch (Throwable th) {
            StatServiceImpl.q.e(th);
            StatServiceImpl.a(this.b, th);
        }
    }
}
