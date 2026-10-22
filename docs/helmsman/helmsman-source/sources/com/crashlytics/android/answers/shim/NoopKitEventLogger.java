package com.crashlytics.android.answers.shim;

/* JADX INFO: loaded from: classes.dex */
class NoopKitEventLogger implements KitEventLogger {
    @Override // com.crashlytics.android.answers.shim.KitEventLogger
    public void logKitEvent(KitEvent kitEvent) {
    }

    NoopKitEventLogger() {
    }

    public static KitEventLogger create() {
        return new NoopKitEventLogger();
    }
}
