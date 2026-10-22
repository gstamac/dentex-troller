package com.qiniu.android.utils;

import android.os.Handler;
import android.os.Looper;

/* JADX INFO: loaded from: classes2.dex */
public final class AsyncRun {
    public static void runInBack(Runnable runnable) {
    }

    public static void runInMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
