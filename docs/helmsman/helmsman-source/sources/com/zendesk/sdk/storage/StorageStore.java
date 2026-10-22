package com.zendesk.sdk.storage;

/* JADX INFO: loaded from: classes2.dex */
public interface StorageStore {
    HelpCenterSessionCache helpCenterSessionCache();

    IdentityStorage identityStorage();

    RequestStorage requestStorage();

    SdkSettingsStorage sdkSettingsStorage();

    SdkStorage sdkStorage();
}
