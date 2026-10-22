package com.zendesk.sdk.network;

import com.zendesk.sdk.model.SdkConfiguration;
import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
public interface BaseProvider {
    void configureSdk(ZendeskCallback<SdkConfiguration> zendeskCallback);

    void getAccessToken(SafeMobileSettings safeMobileSettings, ZendeskCallback<AccessToken> zendeskCallback);

    void getSdkSettings(ZendeskCallback<SafeMobileSettings> zendeskCallback);
}
