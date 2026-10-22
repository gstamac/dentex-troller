package com.zendesk.belvedere;

import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
class DefaultLogger implements BelvedereLogger {
    private boolean loggable = false;

    DefaultLogger() {
    }

    @Override // com.zendesk.belvedere.BelvedereLogger
    public void d(String str, String str2) {
        if (this.loggable) {
            Log.d(str, str2);
        }
    }

    @Override // com.zendesk.belvedere.BelvedereLogger
    public void w(String str, String str2) {
        if (this.loggable) {
            Log.w(str, str2);
        }
    }

    @Override // com.zendesk.belvedere.BelvedereLogger
    public void e(String str, String str2) {
        if (this.loggable) {
            Log.e(str, str2);
        }
    }

    @Override // com.zendesk.belvedere.BelvedereLogger
    public void e(String str, String str2, Throwable th) {
        if (this.loggable) {
            Log.e(str, str2, th);
        }
    }

    @Override // com.zendesk.belvedere.BelvedereLogger
    public void setLoggable(boolean z) {
        this.loggable = z;
    }
}
