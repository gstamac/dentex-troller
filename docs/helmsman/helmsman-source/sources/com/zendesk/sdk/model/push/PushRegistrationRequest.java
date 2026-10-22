package com.zendesk.sdk.model.push;

import com.facebook.appevents.codeless.internal.Constants;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PushRegistrationRequest {
    private final String deviceType = Constants.PLATFORM;
    private String identifier;
    private String locale;
    private String tokenType;

    public void setLocale(String str) {
        this.locale = str;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public void setTokenType(String str) {
        this.tokenType = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getTokenType() {
        return this.tokenType;
    }
}
