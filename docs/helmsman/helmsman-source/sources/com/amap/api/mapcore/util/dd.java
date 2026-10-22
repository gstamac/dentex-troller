package com.amap.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: AsyncTask.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class dd<Params, Progress, Result> {
    public static final Executor a;
    public static final Executor b;
    public static final Executor c;
    private static final ThreadFactory d;
    private static final BlockingQueue<Runnable> e;
    private static final b f;
    private static volatile Executor g;
    private final e<Params, Result> h;
    private final FutureTask<Result> i;
    private volatile d j = d.PENDING;
    private final AtomicBoolean k = new AtomicBoolean();
    private final AtomicBoolean l = new AtomicBoolean();

    /* JADX INFO: compiled from: AsyncTask.java */
    public enum d {
        PENDING,
        RUNNING,
        FINISHED
    }

    protected abstract Result a(Params... paramsArr);

    protected void a(Result result) {
    }

    protected void b() {
    }

    protected void b(Progress... progressArr) {
    }

    protected void c() {
    }

    static {
        ThreadFactory threadFactory = new ThreadFactory() { // from class: com.amap.api.mapcore.util.dd.1
            private final AtomicInteger a = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "AsyncTask #" + this.a.getAndIncrement());
            }
        };
        d = threadFactory;
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(10);
        e = linkedBlockingQueue;
        a = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, linkedBlockingQueue, threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
        Executor cVar = dw.c() ? new c() : Executors.newSingleThreadExecutor(threadFactory);
        b = cVar;
        c = Executors.newFixedThreadPool(2, threadFactory);
        f = new b(Looper.getMainLooper());
        g = cVar;
    }

    /* JADX INFO: compiled from: AsyncTask.java */
    private static class c implements Executor {
        final ArrayDeque<Runnable> a;
        Runnable b;

        private c() {
            this.a = new ArrayDeque<>();
        }

        @Override // java.util.concurrent.Executor
        public synchronized void execute(final Runnable runnable) {
            this.a.offer(new Runnable() { // from class: com.amap.api.mapcore.util.dd.c.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        runnable.run();
                    } finally {
                        c.this.a();
                    }
                }
            });
            if (this.b == null) {
                a();
            }
        }

        protected synchronized void a() {
            Runnable runnablePoll = this.a.poll();
            this.b = runnablePoll;
            if (runnablePoll != null) {
                dd.a.execute(this.b);
            }
        }
    }

    public dd() {
        e<Params, Result> eVar = new e<Params, Result>() { // from class: com.amap.api.mapcore.util.dd.2
            @Override // java.util.concurrent.Callable
            public Result call() throws Exception {
                dd.this.l.set(true);
                Process.setThreadPriority(10);
                dd ddVar = dd.this;
                return (Result) ddVar.d(ddVar.a((Object[]) this.b));
            }
        };
        this.h = eVar;
        this.i = new FutureTask<Result>(eVar) { // from class: com.amap.api.mapcore.util.dd.3
            @Override // java.util.concurrent.FutureTask
            protected void done() {
                try {
                    dd ddVar = dd.this;
                    ddVar.c(ddVar.i.get());
                } catch (InterruptedException e2) {
                    Log.w("AsyncTask", e2);
                } catch (CancellationException unused) {
                    dd.this.c((Object) null);
                } catch (ExecutionException e3) {
                    throw new RuntimeException("An error occured while executing doInBackground()", e3.getCause());
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Result result) {
        if (this.l.get()) {
            return;
        }
        d(result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Result d(Result result) {
        f.obtainMessage(1, new a(this, result)).sendToTarget();
        return result;
    }

    public final d a() {
        return this.j;
    }

    protected void b(Result result) {
        c();
    }

    public final boolean d() {
        return this.k.get();
    }

    public final boolean a(boolean z) {
        this.k.set(true);
        return this.i.cancel(z);
    }

    public final dd<Params, Progress, Result> c(Params... paramsArr) {
        return a(g, paramsArr);
    }

    /* JADX INFO: renamed from: com.amap.api.mapcore.util.dd$4, reason: invalid class name */
    /* JADX INFO: compiled from: AsyncTask.java */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[d.values().length];
            a = iArr;
            try {
                iArr[d.RUNNING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[d.FINISHED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public final dd<Params, Progress, Result> a(Executor executor, Params... paramsArr) {
        if (this.j != d.PENDING) {
            int i = AnonymousClass4.a[this.j.ordinal()];
            if (i == 1) {
                throw new IllegalStateException("Cannot execute task: the task is already running.");
            }
            if (i == 2) {
                throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }
        this.j = d.RUNNING;
        b();
        this.h.b = paramsArr;
        executor.execute(this.i);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(Result result) {
        if (d()) {
            b(result);
        } else {
            a(result);
        }
        this.j = d.FINISHED;
    }

    /* JADX INFO: compiled from: AsyncTask.java */
    private static class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.obj == null || !(message.obj instanceof a)) {
                return;
            }
            a aVar = (a) message.obj;
            int i = message.what;
            if (i == 1) {
                aVar.a.e(aVar.b[0]);
            } else {
                if (i != 2) {
                    return;
                }
                aVar.a.b((Object[]) aVar.b);
            }
        }
    }

    /* JADX INFO: compiled from: AsyncTask.java */
    private static abstract class e<Params, Result> implements Callable<Result> {
        Params[] b;

        private e() {
        }
    }

    /* JADX INFO: compiled from: AsyncTask.java */
    private static class a<Data> {
        final dd a;
        final Data[] b;

        a(dd ddVar, Data... dataArr) {
            this.a = ddVar;
            this.b = dataArr;
        }
    }
}
