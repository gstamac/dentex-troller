package com.zendesk.service;

import com.zendesk.logger.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class SafeZendeskCallback<T> extends ZendeskCallback<T> {
    private static final String LOG_TAG = "SafeZendeskCallback";
    private final ZendeskCallback<T> callback;
    private boolean cancelled = false;

    public SafeZendeskCallback(ZendeskCallback<T> zendeskCallback) {
        this.callback = zendeskCallback;
    }

    @Override // com.zendesk.service.ZendeskCallback
    public void onSuccess(T t) {
        ZendeskCallback<T> zendeskCallback;
        if (!this.cancelled && (zendeskCallback = this.callback) != null) {
            zendeskCallback.onSuccess(t);
        } else {
            Logger.w(LOG_TAG, "Operation was a success but callback is null or was cancelled", new Object[0]);
        }
    }

    @Override // com.zendesk.service.ZendeskCallback
    public void onError(ErrorResponse errorResponse) {
        ZendeskCallback<T> zendeskCallback;
        if (!this.cancelled && (zendeskCallback = this.callback) != null) {
            zendeskCallback.onError(errorResponse);
        } else {
            Logger.e(LOG_TAG, errorResponse);
        }
    }

    public void cancel() {
        this.cancelled = true;
    }

    public static <T> SafeZendeskCallback<T> from(ZendeskCallback<T> zendeskCallback) {
        return new SafeZendeskCallback<>(zendeskCallback);
    }
}
