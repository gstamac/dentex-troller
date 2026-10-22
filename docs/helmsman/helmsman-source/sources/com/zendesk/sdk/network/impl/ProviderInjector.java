package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.network.AccessProvider;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.HelpCenterProvider;
import com.zendesk.sdk.network.NetworkInfoProvider;
import com.zendesk.sdk.network.PushRegistrationProvider;
import com.zendesk.sdk.network.RequestProvider;
import com.zendesk.sdk.network.SdkSettingsProvider;
import com.zendesk.sdk.network.SettingsHelper;
import com.zendesk.sdk.network.UploadProvider;
import com.zendesk.sdk.network.UserProvider;
import com.zendesk.sdk.storage.StorageInjector;
import com.zendesk.sdk.util.BaseInjector;

/* JADX INFO: loaded from: classes2.dex */
class ProviderInjector {
    ProviderInjector() {
    }

    static AccessProvider injectAccessProvider(ApplicationScope applicationScope) {
        return new ZendeskAccessProvider(StorageInjector.injectCachedIdentityStorage(applicationScope), ServiceInjector.injectZendeskAccessService(applicationScope));
    }

    static BaseProvider injectZendeskBaseProvider(ApplicationScope applicationScope) {
        return new ZendeskBaseProvider(injectAccessProvider(applicationScope), StorageInjector.injectCachedSdkSettingsStorage(applicationScope), StorageInjector.injectCachedIdentityStorage(applicationScope), injectSdkSettingsProvider(applicationScope));
    }

    static SdkSettingsProvider injectSdkSettingsProvider(ApplicationScope applicationScope) {
        return new ZendeskSdkSettingsProvider(ServiceInjector.injectZendeskSdkSettingsService(applicationScope), BaseInjector.injectLocale(applicationScope), StorageInjector.injectCachedSdkSettingsStorage(applicationScope), BaseInjector.injectAppId(applicationScope), new HelpCenterLocaleConverter());
    }

    static HelpCenterProvider injectHelpCenterProvider(ApplicationScope applicationScope) {
        return new ZendeskHelpCenterProvider(injectZendeskBaseProvider(applicationScope), ServiceInjector.injectZendeskHelpCenterService(applicationScope), StorageInjector.injectCachedHelpCenterSessionCache(applicationScope));
    }

    static PushRegistrationProvider injectPushRegistrationProvider(ApplicationScope applicationScope) {
        return new ZendeskPushRegistrationProvider(injectZendeskBaseProvider(applicationScope), ServiceInjector.injectZendeskPushRegistrationService(applicationScope), StorageInjector.injectCachedIdentityStorage(applicationScope).getIdentity());
    }

    static UploadProvider injectUploadProvider(ApplicationScope applicationScope) {
        return new ZendeskUploadProvider(injectZendeskBaseProvider(applicationScope), ServiceInjector.injectZendeskUploadService(applicationScope));
    }

    static UserProvider injectUserProvider(ApplicationScope applicationScope) {
        return new ZendeskUserProvider(injectZendeskBaseProvider(applicationScope), ServiceInjector.injectZendeskUserService(applicationScope));
    }

    static RequestProvider injectRequestProvider(ApplicationScope applicationScope) {
        return new ZendeskRequestProvider(injectZendeskBaseProvider(applicationScope), ServiceInjector.injectZendeskRequestService(applicationScope), StorageInjector.injectCachedIdentityStorage(applicationScope).getIdentity(), StorageInjector.injectCachedRequestStorage(applicationScope), StorageInjector.injectCachedRequestSessionCache(applicationScope), BaseInjector.injectLocale(applicationScope));
    }

    static SettingsHelper injectZendeskSettingsHelper(ApplicationScope applicationScope) {
        return new ZendeskSettingsHelper(injectZendeskBaseProvider(applicationScope));
    }

    public static NetworkInfoProvider injectNetworkInfoProvider(ApplicationScope applicationScope) {
        return new ZendeskNetworkInfoProvider(applicationScope.getApplicationContext());
    }

    public static ProviderStore injectProviderStore(ApplicationScope applicationScope) {
        return new ZendeskProviderStore(injectUserProvider(applicationScope), injectHelpCenterProvider(applicationScope), injectPushRegistrationProvider(applicationScope), injectRequestProvider(applicationScope), injectUploadProvider(applicationScope), injectSdkSettingsProvider(applicationScope), injectNetworkInfoProvider(applicationScope), injectZendeskSettingsHelper(applicationScope));
    }

    static ProviderStore injectStubProviderStore(ApplicationScope applicationScope) {
        return new StubProviderStore();
    }
}
