package com.zendesk.sdk.storage;

/* JADX INFO: loaded from: classes2.dex */
public class StorageModule {
    private final HelpCenterSessionCache helpCenterSessionCache;
    private final IdentityStorage identityStorage;
    private final RequestSessionCache requestSessionCache;
    private final RequestStorage requestStorage;
    private final SdkSettingsStorage sdkSettingsStorage;
    private final SdkStorage sdkStorage;

    StorageModule(SdkStorage sdkStorage, IdentityStorage identityStorage, RequestStorage requestStorage, SdkSettingsStorage sdkSettingsStorage, HelpCenterSessionCache helpCenterSessionCache, RequestSessionCache requestSessionCache) {
        this.sdkStorage = sdkStorage;
        this.identityStorage = identityStorage;
        this.requestStorage = requestStorage;
        this.sdkSettingsStorage = sdkSettingsStorage;
        this.helpCenterSessionCache = helpCenterSessionCache;
        this.requestSessionCache = requestSessionCache;
    }

    public SdkStorage getSdkStorage() {
        return this.sdkStorage;
    }

    public IdentityStorage getIdentityStorage() {
        return this.identityStorage;
    }

    public RequestStorage getRequestStorage() {
        return this.requestStorage;
    }

    public SdkSettingsStorage getSdkSettingsStorage() {
        return this.sdkSettingsStorage;
    }

    public HelpCenterSessionCache getHelpCenterSessionCache() {
        return this.helpCenterSessionCache;
    }

    public RequestSessionCache getRequestSessionCache() {
        return this.requestSessionCache;
    }
}
