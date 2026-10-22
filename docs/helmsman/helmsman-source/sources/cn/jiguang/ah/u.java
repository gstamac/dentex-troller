package cn.jiguang.ah;

import android.os.Handler;
import android.os.Message;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes.dex */
final class u extends Handler {
    private final ExecutorService a;

    u(ExecutorService executorService) {
        super(r.c());
        this.a = executorService;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        try {
            if (this.a.isShutdown()) {
                cn.jiguang.ad.d.f("Step", "executor is shutdown");
            } else {
                this.a.submit((Callable) message.obj);
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.h("Step", "handleMessage e:" + th);
        }
    }
}
