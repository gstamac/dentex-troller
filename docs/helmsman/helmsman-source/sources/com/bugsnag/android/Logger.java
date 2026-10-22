package com.bugsnag.android;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
class Logger {
    private static final String LOG_TAG = "Bugsnag";

    Logger() {
    }

    static void info(String str) {
        Log.i(LOG_TAG, str);
    }

    static void warn(String str) {
        Log.w(LOG_TAG, str);
    }

    static void warn(String str, Throwable th) {
        Log.w(LOG_TAG, str, th);
    }
}
