package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.network.HelpCenterProvider;
import com.zendesk.sdk.network.NetworkInfoProvider;
import com.zendesk.sdk.network.PushRegistrationProvider;
import com.zendesk.sdk.network.RequestProvider;
import com.zendesk.sdk.network.SdkSettingsProvider;
import com.zendesk.sdk.network.SettingsHelper;
import com.zendesk.sdk.network.UploadProvider;
import com.zendesk.sdk.network.UserProvider;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskProviderStore implements ProviderStore {
    private final HelpCenterProvider helpCenterProvider;
    private final NetworkInfoProvider networkInfoProvider;
    private final PushRegistrationProvider pushRegistrationProvider;
    private final RequestProvider requestProvider;
    private final SdkSettingsProvider sdkSettingsProvider;
    private final SettingsHelper settingsHelper;
    private final UploadProvider uploadProvider;
    private final UserProvider userProvider;

    ZendeskProviderStore(UserProvider userProvider, HelpCenterProvider helpCenterProvider, PushRegistrationProvider pushRegistrationProvider, RequestProvider requestProvider, UploadProvider uploadProvider, SdkSettingsProvider sdkSettingsProvider, NetworkInfoProvider networkInfoProvider, SettingsHelper settingsHelper) {
        this.userProvider = userProvider;
        this.helpCenterProvider = helpCenterProvider;
        this.pushRegistrationProvider = pushRegistrationProvider;
        this.requestProvider = requestProvider;
        this.uploadProvider = uploadProvider;
        this.sdkSettingsProvider = sdkSettingsProvider;
        this.networkInfoProvider = networkInfoProvider;
        this.settingsHelper = settingsHelper;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public UserProvider userProvider() {
        return this.userProvider;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public HelpCenterProvider helpCenterProvider() {
        return this.helpCenterProvider;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public PushRegistrationProvider pushRegistrationProvider() {
        return this.pushRegistrationProvider;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public RequestProvider requestProvider() {
        return this.requestProvider;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public UploadProvider uploadProvider() {
        return this.uploadProvider;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public SdkSettingsProvider sdkSettingsProvider() {
        return this.sdkSettingsProvider;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public SettingsHelper uiSettingsHelper() {
        return this.settingsHelper;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public NetworkInfoProvider networkInfoProvider() {
        return this.networkInfoProvider;
    }
}
