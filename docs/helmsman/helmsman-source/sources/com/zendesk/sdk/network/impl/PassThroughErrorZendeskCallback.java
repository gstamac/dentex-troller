package com.zendesk.sdk.network.impl;

import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
abstract class PassThroughErrorZendeskCallback<E> extends ZendeskCallback<E> {
    private final ZendeskCallback callback;

    @Override // com.zendesk.service.ZendeskCallback
    public abstract void onSuccess(E e);

    PassThroughErrorZendeskCallback(ZendeskCallback zendeskCallback) {
        this.callback = zendeskCallback;
    }

    @Override // com.zendesk.service.ZendeskCallback
    public void onError(ErrorResponse errorResponse) {
        ZendeskCallback zendeskCallback = this.callback;
        if (zendeskCallback != null) {
            zendeskCallback.onError(errorResponse);
        }
    }
}
