package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Looper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: SDKLogHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class fn extends fk implements Thread.UncaughtExceptionHandler {
    private static ExecutorService e;
    private static Set<Integer> f = Collections.synchronizedSet(new HashSet());
    private static final ThreadFactory g = new ThreadFactory() { // from class: com.amap.api.mapcore.util.fn.2
        private final AtomicInteger a = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "pama#" + this.a.getAndIncrement());
        }
    };
    private Context d;

    public static synchronized fn a(Context context, ff ffVar) throws ev {
        try {
            if (ffVar == null) {
                throw new ev("sdk info is null");
            }
            if (ffVar.a() == null || "".equals(ffVar.a())) {
                throw new ev("sdk name is invalid");
            }
            try {
                if (!f.add(Integer.valueOf(ffVar.hashCode()))) {
                    return (fn) fk.a;
                }
                if (fk.a == null) {
                    fk.a = new fn(context, ffVar);
                } else {
                    fk.a.c = false;
                }
                fk.a.a(context, ffVar, fk.a.c);
                return (fn) fk.a;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    public static synchronized fn a() {
        return (fn) fk.a;
    }

    public static void b(Throwable th, String str, String str2) {
        if (fk.a != null) {
            fk.a.a(th, 1, str, str2);
        }
    }

    public static void a(ff ffVar, String str, String str2, String str3, String str4) {
        if (fk.a != null) {
            StringBuilder sb = new StringBuilder("path:");
            sb.append(str).append(",type:").append(str2).append(",gsid:").append(str3).append(",code:").append(str4);
            fk.a.a(ffVar, sb.toString(), "networkError");
        }
    }

    public static void a(ff ffVar, String str, ev evVar) {
        if (evVar != null) {
            a(ffVar, str, evVar.c(), evVar.d(), evVar.b());
        }
    }

    public static void b(ff ffVar, String str, String str2) {
        if (fk.a != null) {
            fk.a.a(ffVar, str, str2);
        }
    }

    public static synchronized void b() {
        try {
            ExecutorService executorService = e;
            if (executorService != null) {
                executorService.shutdown();
            }
            gs.a();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            if (fk.a != null && Thread.getDefaultUncaughtExceptionHandler() == fk.a && fk.a.b != null) {
                Thread.setDefaultUncaughtExceptionHandler(fk.a.b);
            }
            fk.a = null;
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (th == null) {
            return;
        }
        a(th, 0, null, null);
        if (this.b != null) {
            try {
                Thread.setDefaultUncaughtExceptionHandler(this.b);
            } catch (Throwable unused) {
            }
            this.b.uncaughtException(thread, th);
        }
    }

    @Override // com.amap.api.mapcore.util.fk
    protected void a(ff ffVar, String str, String str2) {
        fl.a(this.d, ffVar, str, str2);
    }

    @Override // com.amap.api.mapcore.util.fk
    protected void a(Throwable th, int i, String str, String str2) {
        fl.a(this.d, th, i, str, str2);
    }

    @Override // com.amap.api.mapcore.util.fk
    protected void a(final Context context, final ff ffVar, final boolean z) {
        try {
            ExecutorService executorServiceC = c();
            if (executorServiceC != null && !executorServiceC.isShutdown()) {
                executorServiceC.submit(new Runnable() { // from class: com.amap.api.mapcore.util.fn.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            synchronized (Looper.getMainLooper()) {
                                new gd(context, true).a(ffVar);
                            }
                            if (z) {
                                synchronized (Looper.getMainLooper()) {
                                    ge geVar = new ge(context);
                                    gf gfVar = new gf();
                                    gfVar.c(true);
                                    gfVar.a(true);
                                    gfVar.b(true);
                                    geVar.a(gfVar);
                                }
                                fl.a(fn.this.d);
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }
        } catch (RejectedExecutionException unused) {
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private fn(Context context, ff ffVar) {
        this.d = context;
        gz.a(new a(context));
        d();
    }

    private void d() {
        try {
            this.b = Thread.getDefaultUncaughtExceptionHandler();
            if (this.b == null) {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
            } else {
                String string = this.b.toString();
                if (string.indexOf("com.amap.api") != -1 || string.indexOf("com.loc") != -1) {
                    this.c = false;
                } else {
                    Thread.setDefaultUncaughtExceptionHandler(this);
                    this.c = true;
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void c(Throwable th, String str, String str2) {
        if (th == null) {
            return;
        }
        try {
            a(th, 1, str, str2);
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    public static synchronized ExecutorService c() {
        try {
            ExecutorService executorService = e;
            if (executorService == null || executorService.isShutdown()) {
                e = Executors.newSingleThreadExecutor(g);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return e;
    }

    /* JADX INFO: compiled from: SDKLogHandler.java */
    private static class a implements ha {
        private Context a;

        a(Context context) {
            this.a = context;
        }

        @Override // com.amap.api.mapcore.util.ha
        public void a() {
            try {
                fl.b(this.a);
            } catch (Throwable th) {
                fk.a(th, "LogNetListener", "onNetCompleted");
            }
        }
    }
}
