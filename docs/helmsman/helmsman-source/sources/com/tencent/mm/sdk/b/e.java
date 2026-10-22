package com.tencent.mm.sdk.b;

import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import junit.framework.Assert;

/* JADX INFO: loaded from: classes2.dex */
final class e extends Handler implements g.a {
    private Looper aN;
    private Handler.Callback aO;
    a aP;

    public interface a {
        void a(Runnable runnable, g gVar);

        void b(Runnable runnable, g gVar);
    }

    e(Looper looper, a aVar) {
        super(looper);
        this.aN = getLooper();
        this.aP = aVar;
    }

    e(a aVar) {
        this.aN = getLooper();
        this.aP = aVar;
    }

    @Override // com.tencent.mm.sdk.b.g.a
    public final void c(Runnable runnable, g gVar) {
        a aVar = this.aP;
        if (aVar != null) {
            aVar.b(runnable, gVar);
        }
    }

    @Override // android.os.Handler
    public final void dispatchMessage(Message message) {
        if (message.getCallback() != null || this.aO != null) {
            super.dispatchMessage(message);
            return;
        }
        System.currentTimeMillis();
        Debug.threadCpuTimeNanos();
        handleMessage(message);
        if (this.aP != null) {
            this.aN.getThread();
            System.currentTimeMillis();
            Debug.threadCpuTimeNanos();
        }
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
    }

    @Override // android.os.Handler
    public final boolean sendMessageAtTime(Message message, long j) {
        a aVar;
        Assert.assertTrue("msg is null", message != null);
        Runnable callback = message.getCallback();
        if (callback == null) {
            return super.sendMessageAtTime(message, j);
        }
        long jUptimeMillis = j - SystemClock.uptimeMillis();
        g gVar = new g(this.aN.getThread(), message.getTarget() == null ? this : message.getTarget(), callback, message.obj, this);
        if (jUptimeMillis > 0) {
            gVar.aY = jUptimeMillis;
        }
        Message messageObtain = Message.obtain(message.getTarget(), gVar);
        messageObtain.what = message.what;
        messageObtain.arg1 = message.arg1;
        messageObtain.arg2 = message.arg2;
        messageObtain.obj = message.obj;
        messageObtain.replyTo = message.replyTo;
        messageObtain.setData(message.getData());
        message.recycle();
        a aVar2 = this.aP;
        if (aVar2 != null) {
            aVar2.a(callback, gVar);
        }
        boolean zSendMessageAtTime = super.sendMessageAtTime(messageObtain, j);
        if (!zSendMessageAtTime && (aVar = this.aP) != null) {
            aVar.b(callback, gVar);
        }
        return zSendMessageAtTime;
    }

    @Override // android.os.Handler
    public final String toString() {
        return "MMInnerHandler{listener = " + this.aP + "}";
    }
}
