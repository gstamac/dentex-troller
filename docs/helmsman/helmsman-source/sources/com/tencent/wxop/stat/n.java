package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class n implements Runnable {
    final /* synthetic */ Context a;

    n(Context context) {
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Context context = this.a;
        if (context == null) {
            StatServiceImpl.q.error("The Context of StatService.onStop() can not be null!");
            return;
        }
        StatServiceImpl.flushDataToDB(context);
        if (StatServiceImpl.a()) {
            return;
        }
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (com.tencent.wxop.stat.common.k.B(this.a)) {
            if (StatConfig.isDebugEnable()) {
                StatServiceImpl.q.i("onStop isBackgroundRunning flushDataToDB");
            }
            StatServiceImpl.commitEvents(this.a, -1);
        }
    }
}
