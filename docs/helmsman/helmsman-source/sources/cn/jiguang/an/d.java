package cn.jiguang.an;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
final class d extends Handler {
    final /* synthetic */ b a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public d(b bVar, Looper looper) {
        super(looper);
        this.a = bVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        try {
            a aVar = (a) this.a.d.get(Integer.valueOf(message.what));
            if (aVar == null) {
                cn.jiguang.ad.d.f("TaskHandlerManager_xxx", "miss task:" + message.what);
                return;
            }
            if (aVar.c == 1) {
                sendEmptyMessageDelayed(message.what, aVar.b);
            } else {
                this.a.d.remove(Integer.valueOf(message.what));
            }
            aVar.a(message);
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("TaskHandlerManager_xxx", "handleMessage,e:" + th);
        }
    }
}
