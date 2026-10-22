package com.zendesk.sdk.model.settings;

/* JADX INFO: loaded from: classes2.dex */
public class MobileSettings {
    private AccountSettings account;
    private SdkSettings sdk;

    public SdkSettings getSdkSettings() {
        SdkSettings sdkSettings = this.sdk;
        return sdkSettings == null ? new SdkSettings() : sdkSettings;
    }

    public AccountSettings getAccountSettings() {
        AccountSettings accountSettings = this.account;
        return accountSettings == null ? new AccountSettings() : accountSettings;
    }
}
