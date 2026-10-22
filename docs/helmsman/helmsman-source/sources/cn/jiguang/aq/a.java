package cn.jiguang.aq;

import cn.jiguang.ad.d;
import com.facebook.share.internal.ShareConstants;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static final HashMap<String, ExecutorService> a = new HashMap<>();

    /* JADX WARN: Code duplicated, block: B:42:0x0086  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private static ExecutorService a(String str) {
        ExecutorService executorServiceNewSingleThreadExecutor;
        byte b;
        HashMap<String, ExecutorService> map = a;
        ExecutorService executorService = map.get(str);
        if (executorService != null && !executorService.isShutdown()) {
            return executorService;
        }
        synchronized (a.class) {
            executorServiceNewSingleThreadExecutor = map.get(str);
            if (executorServiceNewSingleThreadExecutor == null || executorServiceNewSingleThreadExecutor.isShutdown()) {
                switch (str.hashCode()) {
                    case -1649445006:
                        if (!str.equals("TCP_REPORT")) {
                            b = -1;
                        } else {
                            b = 1;
                        }
                        break;
                    case -1642909390:
                        if (!str.equals("UPLOAD_REPORT")) {
                            b = -1;
                        } else {
                            b = 7;
                        }
                        break;
                    case -788009879:
                        if (!str.equals("REPORT_HISTORY")) {
                            b = -1;
                        } else {
                            b = 8;
                        }
                        break;
                    case -501390267:
                        if (!str.equals("BUILD_REPORT")) {
                            b = -1;
                        } else {
                            b = 6;
                        }
                        break;
                    case 62589532:
                        if (!str.equals("ASYNC")) {
                            b = -1;
                        } else {
                            b = 0;
                        }
                        break;
                    case 1082250773:
                        if (!str.equals("SDK_INIT")) {
                            b = -1;
                        } else {
                            b = 2;
                        }
                        break;
                    case 1082357438:
                        if (!str.equals("SDK_MAIN")) {
                            b = -1;
                        } else {
                            b = 4;
                        }
                        break;
                    case 1350548607:
                        if (!str.equals("SDK_SERVICE_INIT")) {
                            b = -1;
                        } else {
                            b = 3;
                        }
                        break;
                    case 1925345846:
                        if (!str.equals(ShareConstants.ACTION)) {
                            b = -1;
                        } else {
                            b = 5;
                        }
                        break;
                    default:
                        b = -1;
                        break;
                }
                executorServiceNewSingleThreadExecutor = b != 0 ? b != 1 ? Executors.newSingleThreadExecutor() : Executors.newFixedThreadPool(3) : Executors.newFixedThreadPool(15);
                map.put(str, executorServiceNewSingleThreadExecutor);
            }
        }
        return executorServiceNewSingleThreadExecutor;
    }

    public static void a(String str, Runnable runnable) {
        ExecutorService executorServiceA;
        try {
            executorServiceA = a(str);
            try {
                executorServiceA.execute(runnable);
            } catch (Throwable th) {
                th = th;
                d.h("SDKWorker_XExecutor", "execute failed, try again e:" + th);
                if (executorServiceA != null) {
                    try {
                        executorServiceA.shutdown();
                        if (!executorServiceA.awaitTermination(100L, TimeUnit.MILLISECONDS)) {
                            executorServiceA.shutdownNow();
                            if (!executorServiceA.awaitTermination(100L, TimeUnit.MILLISECONDS)) {
                                d.a("SDKWorker_XExecutor", "executor did not terminate");
                            }
                        }
                    } catch (InterruptedException unused) {
                        executorServiceA.shutdownNow();
                        d.a("SDKWorker_XExecutor", "current thread is interrupted by self");
                        Thread.currentThread().interrupt();
                    } catch (Throwable th2) {
                        d.f("SDKWorker_XExecutor", "shutDown e:" + th2);
                    }
                }
                try {
                    a(str).execute(runnable);
                } catch (Throwable unused2) {
                    d.h("SDKWorker_XExecutor", "execute e:" + th);
                    if (runnable != null) {
                        try {
                            if (str.equals("BUILD_REPORT")) {
                                new Thread(runnable).start();
                            }
                        } catch (Throwable th3) {
                            d.c("SDKWorker_XExecutor", "execute BUILD_REPORT last error", th3);
                        }
                    }
                }
            }
        } catch (Throwable th4) {
            th = th4;
            executorServiceA = null;
        }
    }
}
