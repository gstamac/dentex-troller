package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.storage.StorageStore;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskConfigHelper {
    private ProviderStore providerStore;
    private StorageStore storageStore;

    ZendeskConfigHelper(ProviderStore providerStore, StorageStore storageStore) {
        this.providerStore = providerStore;
        this.storageStore = storageStore;
    }

    public ProviderStore getProviderStore() {
        return this.providerStore;
    }

    public StorageStore getStorageStore() {
        return this.storageStore;
    }
}
