package com.zendesk.sdk.storage;

import com.zendesk.logger.Logger;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class StubRequestStorage implements RequestStorage {
    private static final String LOG_TAG = "StubRequestStorage";

    StubRequestStorage() {
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public List<String> getStoredRequestIds() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return new ArrayList();
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public void storeRequestId(String str) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public void setCommentCount(String str, int i) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.RequestStorage
    public Integer getCommentCount(String str) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return 0;
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
