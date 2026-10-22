package com.zendesk.sdk.storage;

import com.zendesk.logger.Logger;
import java.util.Set;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes2.dex */
class StubSdkStorage implements SdkStorage {
    private static final String LOG_TAG = "StubSdkStorage";

    StubSdkStorage() {
    }

    @Override // com.zendesk.sdk.storage.SdkStorage
    public void registerUserStorage(SdkStorage.UserStorage userStorage) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.SdkStorage
    public void clearUserData() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.SdkStorage
    public Set<SdkStorage.UserStorage> getUserStorage() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return new TreeSet();
    }
}
