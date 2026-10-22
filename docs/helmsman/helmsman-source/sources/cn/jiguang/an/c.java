package cn.jiguang.an;

import android.os.HandlerThread;

/* JADX INFO: loaded from: classes.dex */
final class c extends HandlerThread {
    final /* synthetic */ b a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    c(b bVar, String str) {
        super(str);
        this.a = bVar;
    }

    @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
    public final void run() {
        try {
            super.run();
        } catch (RuntimeException e) {
            cn.jiguang.ad.d.h("TaskHandlerManager_xxx", "handler thread run e:" + e + "  t=" + Thread.currentThread().getName() + "_" + Thread.currentThread().getId());
        }
    }
}
