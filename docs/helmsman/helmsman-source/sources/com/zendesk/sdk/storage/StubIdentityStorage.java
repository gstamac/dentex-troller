package com.zendesk.sdk.storage;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.access.Identity;

/* JADX INFO: loaded from: classes2.dex */
class StubIdentityStorage implements IdentityStorage {
    private static final String LOG_TAG = "StubIdentityStorage";

    StubIdentityStorage() {
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public void storeAccessToken(AccessToken accessToken) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public AccessToken getStoredAccessToken() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return new AccessToken();
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public String getStoredAccessTokenAsBearerToken() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return "";
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public String getUUID() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return "";
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public void storeIdentity(Identity identity) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public Identity getIdentity() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return new StubIdentity();
    }

    @Override // com.zendesk.sdk.storage.IdentityStorage
    public Identity anonymiseIdentity() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return new StubIdentity();
    }

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public void clearUserData() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public String getCacheKey() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return "";
    }
}
