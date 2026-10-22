package com.zendesk.sdk.storage;

import com.zendesk.sdk.network.impl.ApplicationScope;
import com.zendesk.sdk.util.BaseInjector;
import com.zendesk.sdk.util.DependencyProvider;
import com.zendesk.sdk.util.LibraryInjector;
import com.zendesk.sdk.util.ScopeCache;

/* JADX INFO: loaded from: classes2.dex */
public class StorageInjector {
    static SdkStorage injectSdkStorage(ApplicationScope applicationScope) {
        return new ZendeskSdkStorage();
    }

    public static SdkStorage injectCachedSdkStorage(ApplicationScope applicationScope) {
        return injectCachedStorageModule(applicationScope).getSdkStorage();
    }

    static RequestStorage injectRequestStorage(ApplicationScope applicationScope) {
        return new ZendeskRequestStorage(BaseInjector.injectApplicationContext(applicationScope));
    }

    public static RequestStorage injectCachedRequestStorage(ApplicationScope applicationScope) {
        return injectCachedStorageModule(applicationScope).getRequestStorage();
    }

    static SdkSettingsStorage injectSdkSettingsStorage(ApplicationScope applicationScope) {
        return new ZendeskSdkSettingsStorage(BaseInjector.injectApplicationContext(applicationScope), LibraryInjector.injectGson(applicationScope));
    }

    public static SdkSettingsStorage injectCachedSdkSettingsStorage(ApplicationScope applicationScope) {
        return injectCachedStorageModule(applicationScope).getSdkSettingsStorage();
    }

    static IdentityStorage injectIdentityStorage(ApplicationScope applicationScope) {
        return new ZendeskIdentityStorage(BaseInjector.injectApplicationContext(applicationScope), LibraryInjector.injectGson(applicationScope));
    }

    public static IdentityStorage injectCachedIdentityStorage(ApplicationScope applicationScope) {
        return injectCachedStorageModule(applicationScope).getIdentityStorage();
    }

    static HelpCenterSessionCache injectHelpCenterSessionCache(ApplicationScope applicationScope) {
        return new ZendeskHelpCenterSessionCache();
    }

    public static HelpCenterSessionCache injectCachedHelpCenterSessionCache(ApplicationScope applicationScope) {
        return injectCachedStorageModule(applicationScope).getHelpCenterSessionCache();
    }

    static StorageModule injectStorageModule(ApplicationScope applicationScope) {
        return new StorageModule(injectSdkStorage(applicationScope), injectIdentityStorage(applicationScope), injectRequestStorage(applicationScope), injectSdkSettingsStorage(applicationScope), injectHelpCenterSessionCache(applicationScope), injectRequestSessionCache(applicationScope));
    }

    private static ScopeCache<StorageModule> injectStorageModuleCache(ApplicationScope applicationScope) {
        return applicationScope.getStorageModuleCache();
    }

    static StorageModule injectCachedStorageModule(final ApplicationScope applicationScope) {
        return injectStorageModuleCache(applicationScope).get(new DependencyProvider<StorageModule>() { // from class: com.zendesk.sdk.storage.StorageInjector.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.zendesk.sdk.util.DependencyProvider
            public StorageModule provideDependency() {
                return StorageInjector.injectStorageModule(applicationScope);
            }
        });
    }

    public static StorageStore injectStorageStore(ApplicationScope applicationScope) {
        return new ZendeskStorageStore(injectCachedSdkStorage(applicationScope), injectCachedIdentityStorage(applicationScope), injectCachedRequestStorage(applicationScope), injectCachedSdkSettingsStorage(applicationScope), injectCachedHelpCenterSessionCache(applicationScope));
    }

    public static StubStorageStore injectStubStorageStore(ApplicationScope applicationScope) {
        return new StubStorageStore();
    }

    static RequestSessionCache injectRequestSessionCache(ApplicationScope applicationScope) {
        return new ZendeskRequestSessionCache();
    }

    public static RequestSessionCache injectCachedRequestSessionCache(ApplicationScope applicationScope) {
        return injectCachedStorageModule(applicationScope).getRequestSessionCache();
    }
}
