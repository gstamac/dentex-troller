package com.bugsnag.android;

import java.util.Iterator;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    final WeakHashMap<Client, Boolean> clientMap = new WeakHashMap<>();
    private final Thread.UncaughtExceptionHandler originalHandler;

    static void enable(Client client) {
        ExceptionHandler exceptionHandler;
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler instanceof ExceptionHandler) {
            exceptionHandler = (ExceptionHandler) defaultUncaughtExceptionHandler;
        } else {
            ExceptionHandler exceptionHandler2 = new ExceptionHandler(defaultUncaughtExceptionHandler);
            Thread.setDefaultUncaughtExceptionHandler(exceptionHandler2);
            exceptionHandler = exceptionHandler2;
        }
        exceptionHandler.clientMap.put(client, true);
    }

    static void disable(Client client) {
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler instanceof ExceptionHandler) {
            ExceptionHandler exceptionHandler = (ExceptionHandler) defaultUncaughtExceptionHandler;
            exceptionHandler.clientMap.remove(client);
            if (exceptionHandler.clientMap.size() == 0) {
                Thread.setDefaultUncaughtExceptionHandler(exceptionHandler.originalHandler);
            }
        }
    }

    public ExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.originalHandler = uncaughtExceptionHandler;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) throws Throwable {
        Iterator<Client> it = this.clientMap.keySet().iterator();
        while (it.hasNext()) {
            it.next().cacheAndNotify(th, Severity.ERROR);
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.originalHandler;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        } else {
            System.err.printf("Exception in thread \"%s\" ", thread.getName());
            th.printStackTrace(System.err);
        }
    }
}
