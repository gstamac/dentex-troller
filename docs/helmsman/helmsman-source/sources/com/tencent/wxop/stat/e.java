package com.tencent.wxop.stat;

import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
class e extends TimerTask {
    final /* synthetic */ d a;

    e(d dVar) {
        this.a = dVar;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        if (StatConfig.isDebugEnable()) {
            com.tencent.wxop.stat.common.k.b().i("TimerTask run");
        }
        StatServiceImpl.e(this.a.c);
        cancel();
        this.a.a();
    }
}
