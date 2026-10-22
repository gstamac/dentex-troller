package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.network.Constants;
import com.zendesk.util.StringUtils;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/* JADX INFO: loaded from: classes2.dex */
public class ZendeskRequestInterceptor implements Interceptor {
    private final String oauthId;
    private final String userAgent;

    public ZendeskRequestInterceptor(String str, String str2) {
        this.oauthId = str;
        this.userAgent = str2;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builderAddHeader = chain.request().newBuilder().addHeader("User-Agent", this.userAgent).addHeader("Accept", "application/json");
        if (StringUtils.hasLength(this.oauthId)) {
            builderAddHeader.addHeader(Constants.CLIENT_IDENTIFIER_HEADER, this.oauthId);
        }
        return chain.proceed(builderAddHeader.build());
    }
}
