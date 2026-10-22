package com.zendesk.sdk.attachment;

import android.content.Context;
import com.zendesk.belvedere.Belvedere;
import com.zendesk.belvedere.BelvedereLogger;
import com.zendesk.logger.Logger;

/* JADX INFO: loaded from: classes2.dex */
public enum ZendeskBelvedereProvider {
    INSTANCE;

    private Belvedere mBelvedere;

    public Belvedere getBelvedere(Context context) {
        if (this.mBelvedere == null) {
            synchronized (ZendeskBelvedereProvider.class) {
                this.mBelvedere = Belvedere.from(context).withAllowMultiple(true).withContentType("image/*").withCustomLogger(new BelvedereZendeskLogger()).withDebug(Logger.isLoggable()).build();
            }
        }
        return this.mBelvedere;
    }

    static class BelvedereZendeskLogger implements BelvedereLogger {
        boolean logging = false;

        BelvedereZendeskLogger() {
        }

        @Override // com.zendesk.belvedere.BelvedereLogger
        public void d(String str, String str2) {
            if (this.logging) {
                Logger.d(str, str2, new Object[0]);
            }
        }

        @Override // com.zendesk.belvedere.BelvedereLogger
        public void w(String str, String str2) {
            if (this.logging) {
                Logger.d(str, str2, new Object[0]);
            }
        }

        @Override // com.zendesk.belvedere.BelvedereLogger
        public void e(String str, String str2) {
            if (this.logging) {
                Logger.d(str, str2, new Object[0]);
            }
        }

        @Override // com.zendesk.belvedere.BelvedereLogger
        public void e(String str, String str2, Throwable th) {
            if (this.logging) {
                Logger.d(str, str2, th, new Object[0]);
            }
        }

        @Override // com.zendesk.belvedere.BelvedereLogger
        public void setLoggable(boolean z) {
            this.logging = z;
        }
    }
}
