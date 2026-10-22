package com.zendesk.sdk.network;

import com.zendesk.service.ErrorResponse;

/* JADX INFO: loaded from: classes2.dex */
public interface RequestLoadingListener {
    void onLoadError(ErrorResponse errorResponse);

    void onLoadFinished(int i);

    void onLoadStarted();
}
