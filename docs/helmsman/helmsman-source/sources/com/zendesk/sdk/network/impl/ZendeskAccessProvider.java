package com.zendesk.sdk.network.impl;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.JwtIdentity;
import com.zendesk.sdk.network.AccessProvider;
import com.zendesk.sdk.storage.IdentityStorage;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskAccessProvider implements AccessProvider {
    private static final String LOG_TAG = "ZendeskAccessProvider";
    private final ZendeskAccessService accessService;
    private final IdentityStorage identityStorage;

    public ZendeskAccessProvider(IdentityStorage identityStorage, ZendeskAccessService zendeskAccessService) {
        this.identityStorage = identityStorage;
        this.accessService = zendeskAccessService;
    }

    @Override // com.zendesk.sdk.network.AccessProvider
    public void getAndStoreAuthTokenViaJwt(JwtIdentity jwtIdentity, final ZendeskCallback<AccessToken> zendeskCallback) {
        Logger.d(LOG_TAG, "Requesting an access token for jwt identity.", new Object[0]);
        if (StringUtils.isEmpty(jwtIdentity.getJwtUserIdentifier())) {
            Logger.e(LOG_TAG, "The jwt user identifier is null or empty. We cannot proceed to get an access token", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onError(new ErrorResponseAdapter("The jwt user identifier is null or empty. We cannot proceed to get an access token"));
                return;
            }
            return;
        }
        this.accessService.getAuthTokenViaJWT(jwtIdentity, new PassThroughErrorZendeskCallback<AccessToken>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskAccessProvider.1
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(AccessToken accessToken) {
                ZendeskAccessProvider.this.identityStorage.storeAccessToken(accessToken);
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onSuccess(accessToken);
                }
            }
        });
    }

    @Override // com.zendesk.sdk.network.AccessProvider
    public void getAndStoreAuthTokenViaAnonymous(AnonymousIdentity anonymousIdentity, final ZendeskCallback<AccessToken> zendeskCallback) {
        Logger.d(LOG_TAG, "Requesting an access token for anonymous identity.", new Object[0]);
        this.accessService.getAuthTokenViaMobileSDK(anonymousIdentity, new PassThroughErrorZendeskCallback<AccessToken>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskAccessProvider.2
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(AccessToken accessToken) {
                ZendeskAccessProvider.this.identityStorage.storeAccessToken(accessToken);
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onSuccess(accessToken);
                }
            }
        });
    }
}
