package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: ThreadTask.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class hm implements Runnable {
    a d;

    /* JADX INFO: compiled from: ThreadTask.java */
    interface a {
        void a(hm hmVar);

        void b(hm hmVar);

        void c(hm hmVar);
    }

    public abstract void a();

    @Override // java.lang.Runnable
    public final void run() {
        a aVar;
        try {
            a aVar2 = this.d;
            if (aVar2 != null) {
                aVar2.a(this);
            }
            if (Thread.interrupted()) {
                return;
            }
            a();
            if (Thread.interrupted() || (aVar = this.d) == null) {
                return;
            }
            aVar.b(this);
        } catch (Throwable th) {
            fn.b(th, "ThreadTask", "run");
            th.printStackTrace();
        }
    }

    public final void e() {
        try {
            a aVar = this.d;
            if (aVar != null) {
                aVar.c(this);
            }
        } catch (Throwable th) {
            fn.b(th, "ThreadTask", "cancelTask");
            th.printStackTrace();
        }
    }
}
