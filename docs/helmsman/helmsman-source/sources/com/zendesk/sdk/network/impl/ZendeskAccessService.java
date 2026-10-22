package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.access.AuthenticationRequestWrapper;
import com.zendesk.sdk.model.access.AuthenticationResponse;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.AccessService;
import com.zendesk.service.RetrofitZendeskCallbackAdapter;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskAccessService {
    private static final RetrofitZendeskCallbackAdapter.RequestExtractor<AuthenticationResponse, AccessToken> AUTH_EXTRACTOR = new RetrofitZendeskCallbackAdapter.RequestExtractor<AuthenticationResponse, AccessToken>() { // from class: com.zendesk.sdk.network.impl.ZendeskAccessService.1
        @Override // com.zendesk.service.RetrofitZendeskCallbackAdapter.RequestExtractor
        public AccessToken extract(AuthenticationResponse authenticationResponse) {
            return authenticationResponse.getAuthentication();
        }
    };
    private static final String LOG_TAG = "ZendeskAccessService";
    private final AccessService accessService;

    public ZendeskAccessService(AccessService accessService) {
        this.accessService = accessService;
    }

    void getAuthTokenViaJWT(Identity identity, ZendeskCallback<AccessToken> zendeskCallback) {
        AuthenticationRequestWrapper authenticationRequestWrapper = new AuthenticationRequestWrapper();
        authenticationRequestWrapper.setUser(identity);
        this.accessService.getAuthToken(authenticationRequestWrapper).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, AUTH_EXTRACTOR));
    }

    void getAuthTokenViaMobileSDK(Identity identity, ZendeskCallback<AccessToken> zendeskCallback) {
        AuthenticationRequestWrapper authenticationRequestWrapper = new AuthenticationRequestWrapper();
        authenticationRequestWrapper.setUser(identity);
        this.accessService.getAuthTokenForAnonymous(authenticationRequestWrapper).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback, AUTH_EXTRACTOR));
    }
}
