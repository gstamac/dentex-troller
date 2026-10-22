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
class StubProviderStore implements ProviderStore {
    final UserProvider userProvider = (UserProvider) StubProviderFactory.getStubProvider(UserProvider.class);
    final HelpCenterProvider helpCenterProvider = (HelpCenterProvider) StubProviderFactory.getStubProvider(HelpCenterProvider.class);
    final PushRegistrationProvider pushRegistrationProvider = (PushRegistrationProvider) StubProviderFactory.getStubProvider(PushRegistrationProvider.class);
    final RequestProvider requestProvider = (RequestProvider) StubProviderFactory.getStubProvider(RequestProvider.class);
    final UploadProvider uploadProvider = (UploadProvider) StubProviderFactory.getStubProvider(UploadProvider.class);
    final SdkSettingsProvider sdkSettingsProvider = (SdkSettingsProvider) StubProviderFactory.getStubProvider(SdkSettingsProvider.class);
    final NetworkInfoProvider networkInfoProvider = (NetworkInfoProvider) StubProviderFactory.getStubProvider(NetworkInfoProvider.class);
    final SettingsHelper settingsHelper = (SettingsHelper) StubProviderFactory.getStubProvider(SettingsHelper.class);

    StubProviderStore() {
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
    public NetworkInfoProvider networkInfoProvider() {
        return this.networkInfoProvider;
    }

    @Override // com.zendesk.sdk.network.impl.ProviderStore
    public SettingsHelper uiSettingsHelper() {
        return this.settingsHelper;
    }
}
