package com.zendesk.sdk.storage;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes2.dex */
public class StubStorageStore implements StorageStore {
    private final SdkStorage sdkStorage = new StubSdkStorage();
    private final IdentityStorage identityStorage = new StubIdentityStorage();
    private final RequestStorage requestStorage = new StubRequestStorage();
    private final SdkSettingsStorage sdkSettingsStorage = new StubSdkSettingsStorage();
    private final HelpCenterSessionCache helpCenterSessionCache = new StubHelpCenterSessionCache();

    StubStorageStore() {
    }

    @Override // com.zendesk.sdk.storage.StorageStore
    public SdkStorage sdkStorage() {
        return this.sdkStorage;
    }

    @Override // com.zendesk.sdk.storage.StorageStore
    public IdentityStorage identityStorage() {
        return this.identityStorage;
    }

    @Override // com.zendesk.sdk.storage.StorageStore
    public RequestStorage requestStorage() {
        return this.requestStorage;
    }

    @Override // com.zendesk.sdk.storage.StorageStore
    public SdkSettingsStorage sdkSettingsStorage() {
        return this.sdkSettingsStorage;
    }

    @Override // com.zendesk.sdk.storage.StorageStore
    public HelpCenterSessionCache helpCenterSessionCache() {
        return this.helpCenterSessionCache;
    }
}
