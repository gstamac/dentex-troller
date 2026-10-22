package com.zendesk.sdk.network;

import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.JwtIdentity;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
public interface AccessProvider {
    void getAndStoreAuthTokenViaAnonymous(AnonymousIdentity anonymousIdentity, ZendeskCallback<AccessToken> zendeskCallback);

    void getAndStoreAuthTokenViaJwt(JwtIdentity jwtIdentity, ZendeskCallback<AccessToken> zendeskCallback);
}
