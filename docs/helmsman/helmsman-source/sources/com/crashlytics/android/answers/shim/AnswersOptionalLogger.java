package com.crashlytics.android.answers.shim;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
public class AnswersOptionalLogger {
    private static final String TAG = "AnswersOptionalLogger";
    private static final KitEventLogger logger;

    static {
        KitEventLogger kitEventLoggerCreate;
        try {
            kitEventLoggerCreate = AnswersKitEventLogger.create();
        } catch (IllegalStateException | NoClassDefFoundError unused) {
            kitEventLoggerCreate = null;
        } catch (Throwable th) {
            Log.w(TAG, "Unexpected error creating AnswersKitEventLogger", th);
            kitEventLoggerCreate = null;
        }
        if (kitEventLoggerCreate == null) {
            kitEventLoggerCreate = NoopKitEventLogger.create();
        }
        logger = kitEventLoggerCreate;
    }

    public static KitEventLogger get() {
        return logger;
    }
}
