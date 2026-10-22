package com.bugsnag.android;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
class Async {
    private static final int CORE_POOL_SIZE;
    private static final int CPU_COUNT;
    private static final Executor EXECUTOR;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final int MAXIMUM_POOL_SIZE;
    private static final BlockingQueue<Runnable> POOL_WORK_QUEUE;
    private static final ThreadFactory THREAD_FACTORY;

    Async() {
    }

    static {
        int iAvailableProcessors = Runtime.getRuntime().availableProcessors();
        CPU_COUNT = iAvailableProcessors;
        int iMax = Math.max(1, Math.min(iAvailableProcessors - 1, 4));
        CORE_POOL_SIZE = iMax;
        int i = (iAvailableProcessors * 2) + 1;
        MAXIMUM_POOL_SIZE = i;
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(128);
        POOL_WORK_QUEUE = linkedBlockingQueue;
        ThreadFactory threadFactory = new ThreadFactory() { // from class: com.bugsnag.android.Async.1
            private final AtomicInteger count = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "Bugsnag Thread #" + this.count.getAndIncrement());
            }
        };
        THREAD_FACTORY = threadFactory;
        EXECUTOR = new ThreadPoolExecutor(iMax, i, 30L, TimeUnit.SECONDS, linkedBlockingQueue, threadFactory);
    }

    static void run(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }
}
