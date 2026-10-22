package com.tencent.wxop.stat;

/* JADX INFO: loaded from: classes2.dex */
class ao implements Thread.UncaughtExceptionHandler {
    ao() {
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (!StatConfig.isEnableStatService() || StatServiceImpl.t == null) {
            return;
        }
        if (StatConfig.isAutoExceptionCaught()) {
            au.a(StatServiceImpl.t).a((com.tencent.wxop.stat.a.e) new com.tencent.wxop.stat.a.d(StatServiceImpl.t, StatServiceImpl.a(StatServiceImpl.t, false, (StatSpecifyReportedInfo) null), 2, th, thread, null), (h) null, false, true);
            StatServiceImpl.q.debug("MTA has caught the following uncaught exception:");
            StatServiceImpl.q.error(th);
        }
        StatServiceImpl.flushDataToDB(StatServiceImpl.t);
        if (StatServiceImpl.r != null) {
            StatServiceImpl.q.d("Call the original uncaught exception handler.");
            if (StatServiceImpl.r instanceof ao) {
                return;
            }
            StatServiceImpl.r.uncaughtException(thread, th);
        }
    }
}
