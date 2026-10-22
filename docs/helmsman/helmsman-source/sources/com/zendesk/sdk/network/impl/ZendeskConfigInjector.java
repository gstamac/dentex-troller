package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.storage.StorageInjector;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskConfigInjector {
    ZendeskConfigInjector() {
    }

    static ZendeskConfigHelper injectZendeskConfigHelper(ApplicationScope applicationScope) {
        return new ZendeskConfigHelper(ProviderInjector.injectProviderStore(applicationScope), StorageInjector.injectStorageStore(applicationScope));
    }

    static ZendeskConfigHelper injectStubZendeskConfigHelper(ApplicationScope applicationScope) {
        return new ZendeskConfigHelper(ProviderInjector.injectStubProviderStore(applicationScope), StorageInjector.injectStubStorageStore(applicationScope));
    }
}
