package com.qiniu.android.collect;

import com.qiniu.android.utils.ContextGetter;

/* JADX INFO: loaded from: classes2.dex */
public final class Config {
    public static int interval = 10;
    public static boolean isRecord = true;
    public static boolean isUpload = true;
    public static int maxRecordFileSize = 2097152;
    public static String recordDir = null;
    public static final String serverURL = "https://uplog.qbox.me/log/3";
    public static int uploadThreshold = 4096;

    static {
        try {
            recordDir = ContextGetter.applicationContext().getCacheDir().getAbsolutePath();
        } catch (Throwable th) {
            th.fillInStackTrace();
        }
    }

    public static void quick() {
        uploadThreshold = 1024;
        interval = 2;
    }

    public static void normal() {
        uploadThreshold = 4096;
        interval = 10;
    }

    public static void slow() {
        uploadThreshold = 153600;
        interval = 300;
    }
}
