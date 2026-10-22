package com.amap.api.mapcore.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/* JADX INFO: compiled from: ThreadPool.java */
/* JADX INFO: loaded from: classes.dex */
public final class hl {
    private static hl a;
    private ExecutorService b;
    private ConcurrentHashMap<hm, Future<?>> c = new ConcurrentHashMap<>();
    private hm.a d = new hm.a() { // from class: com.amap.api.mapcore.util.hl.1
        @Override // com.amap.api.mapcore.util.hm.a
        public void a(hm hmVar) {
        }

        @Override // com.amap.api.mapcore.util.hm.a
        public void b(hm hmVar) {
            hl.this.a(hmVar, false);
        }

        @Override // com.amap.api.mapcore.util.hm.a
        public void c(hm hmVar) {
            hl.this.a(hmVar, true);
        }
    };

    public static synchronized hl a(int i) {
        if (a == null) {
            a = new hl(i);
        }
        return a;
    }

    private hl(int i) {
        try {
            this.b = Executors.newFixedThreadPool(i);
        } catch (Throwable th) {
            fn.b(th, "TPool", "ThreadPool");
            th.printStackTrace();
        }
    }

    public void a(hm hmVar) throws ev {
        ExecutorService executorService;
        try {
            if (!b(hmVar) && (executorService = this.b) != null && !executorService.isShutdown()) {
                hmVar.d = this.d;
                try {
                    Future<?> futureSubmit = this.b.submit(hmVar);
                    if (futureSubmit == null) {
                        return;
                    }
                    a(hmVar, futureSubmit);
                } catch (RejectedExecutionException unused) {
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
            fn.b(th, "TPool", "addTask");
            throw new ev("thread pool has exception");
        }
    }

    public static synchronized void a() {
        try {
            hl hlVar = a;
            if (hlVar != null) {
                hlVar.b();
                a = null;
            }
        } catch (Throwable th) {
            fn.b(th, "TPool", "onDestroy");
            th.printStackTrace();
        }
    }

    private void b() {
        try {
            Iterator<Map.Entry<hm, Future<?>>> it = this.c.entrySet().iterator();
            while (it.hasNext()) {
                Future<?> future = this.c.get(it.next().getKey());
                if (future != null) {
                    try {
                        future.cancel(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.c.clear();
            this.b.shutdown();
        } catch (Throwable th) {
            fn.b(th, "TPool", "destroy");
            th.printStackTrace();
        }
    }

    private synchronized boolean b(hm hmVar) {
        boolean zContainsKey;
        try {
            zContainsKey = this.c.containsKey(hmVar);
        } catch (Throwable th) {
            fn.b(th, "TPool", "contain");
            th.printStackTrace();
            zContainsKey = false;
        }
        return zContainsKey;
    }

    private synchronized void a(hm hmVar, Future<?> future) {
        try {
            this.c.put(hmVar, future);
        } catch (Throwable th) {
            fn.b(th, "TPool", "addQueue");
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(hm hmVar, boolean z) {
        try {
            Future<?> futureRemove = this.c.remove(hmVar);
            if (z && futureRemove != null) {
                futureRemove.cancel(true);
            }
        } catch (Throwable th) {
            fn.b(th, "TPool", "removeQueue");
            th.printStackTrace();
        }
    }
}
