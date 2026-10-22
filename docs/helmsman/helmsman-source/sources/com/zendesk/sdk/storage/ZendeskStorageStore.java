package com.zendesk.sdk.storage;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskStorageStore implements StorageStore {
    private final HelpCenterSessionCache helpCenterSessionCache;
    private final IdentityStorage identityStorage;
    private final RequestStorage requestStorage;
    private final SdkSettingsStorage sdkSettingsStorage;
    private final SdkStorage sdkStorage;

    ZendeskStorageStore(SdkStorage sdkStorage, IdentityStorage identityStorage, RequestStorage requestStorage, SdkSettingsStorage sdkSettingsStorage, HelpCenterSessionCache helpCenterSessionCache) {
        this.sdkStorage = sdkStorage;
        this.identityStorage = identityStorage;
        this.requestStorage = requestStorage;
        this.sdkSettingsStorage = sdkSettingsStorage;
        this.helpCenterSessionCache = helpCenterSessionCache;
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
