package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.storage.SdkStorage;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultZendeskUnauthorizedInterceptor implements Interceptor {
    private final SdkStorage sdkStorage;

    public DefaultZendeskUnauthorizedInterceptor(SdkStorage sdkStorage) {
        this.sdkStorage = sdkStorage;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response responseProceed = chain.proceed(chain.request());
        if (!responseProceed.isSuccessful() && 401 == responseProceed.code()) {
            onHttpUnauthorized();
        }
        return responseProceed;
    }

    public void onHttpUnauthorized() {
        this.sdkStorage.clearUserData();
    }
}
