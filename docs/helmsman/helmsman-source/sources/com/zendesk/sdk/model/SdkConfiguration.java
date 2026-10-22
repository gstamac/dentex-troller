package com.zendesk.sdk.model;

import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class SdkConfiguration {
    private static final String BEARER_FORMAT = "Bearer %s";
    private final AccessToken mAccessToken;
    private final SafeMobileSettings mMobileSettings;

    public SdkConfiguration(AccessToken accessToken, SafeMobileSettings safeMobileSettings) {
        this.mAccessToken = accessToken;
        this.mMobileSettings = safeMobileSettings;
    }

    public AccessToken getAccessToken() {
        return this.mAccessToken;
    }

    public SafeMobileSettings getMobileSettings() {
        return this.mMobileSettings;
    }

    public String getBearerAuthorizationHeader() {
        Locale locale = Locale.US;
        Object[] objArr = new Object[1];
        AccessToken accessToken = this.mAccessToken;
        objArr[0] = accessToken == null ? null : accessToken.getAccessToken();
        return String.format(locale, BEARER_FORMAT, objArr);
    }
}
