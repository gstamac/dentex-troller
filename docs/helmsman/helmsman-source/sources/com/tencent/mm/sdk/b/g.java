package com.tencent.mm.sdk.b;

import android.os.Debug;
import android.os.Handler;
import android.os.Process;

/* JADX INFO: loaded from: classes2.dex */
public final class g implements Runnable {
    private static final String bf = "taskName = %s|token = %s|handler = %s|threadName = %s|threadId = %d|priority = %d|addTime = %d|delayTime = %d|usedTime = %d|cpuTime = %d|started = %b";
    private static final String bg = "taskName = %s | addTime = %s | endTime = %s | usedTime = %d | cpuTime = %d | threadCpuTime = %d | totalCpuTime = %d | threadCpuRate = %.1f";
    final Runnable aQ;
    final String aR;
    final Object aS;
    final Thread aT;
    String aU;
    long aV;
    final a aW;
    long aX;
    long aY;
    long aZ;
    long ba;
    long bb;
    long bc;
    long bd;
    final Handler handler;
    int priority;
    boolean started = false;
    float be = -1.0f;

    public interface a {
        void c(Runnable runnable, g gVar);
    }

    g(Thread thread, Handler handler, Runnable runnable, Object obj, a aVar) {
        int iIndexOf;
        this.aT = thread;
        if (thread != null) {
            this.aU = thread.getName();
            this.aV = thread.getId();
            this.priority = thread.getPriority();
        }
        this.handler = handler;
        this.aQ = runnable;
        String name = runnable.getClass().getName();
        String string = runnable.toString();
        if (!h.h(string) && (iIndexOf = string.indexOf(124)) > 0) {
            name = name + "_" + string.substring(iIndexOf + 1);
        }
        this.aR = name;
        this.aS = obj;
        this.aW = aVar;
        this.aX = System.currentTimeMillis();
    }

    @Override // java.lang.Runnable
    public final void run() {
        Process.myTid();
        this.ba = System.currentTimeMillis();
        this.bb = Debug.threadCpuTimeNanos();
        this.bc = -1L;
        this.bd = -1L;
        this.started = true;
        this.aQ.run();
        this.bc = (-1) - this.bc;
        this.bd = (-1) - this.bd;
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.aZ = jCurrentTimeMillis;
        this.ba = jCurrentTimeMillis - this.ba;
        this.bb = (Debug.threadCpuTimeNanos() - this.bb) / 1000000;
        long j = this.bd;
        if (j != 0) {
            this.be = (this.bc * 100) / j;
        }
        a aVar = this.aW;
        if (aVar != null) {
            aVar.c(this.aQ, this);
        }
    }
}
