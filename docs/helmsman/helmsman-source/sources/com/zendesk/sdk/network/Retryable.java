package com.zendesk.sdk.network;

import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public interface Retryable {
    void onRetryAvailable(String str, View.OnClickListener onClickListener);

    void onRetryUnavailable();
}
