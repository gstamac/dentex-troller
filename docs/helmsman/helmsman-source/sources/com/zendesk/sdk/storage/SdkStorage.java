package com.zendesk.sdk.storage;

import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
public interface SdkStorage {

    public interface UserStorage {
        void clearUserData();

        String getCacheKey();
    }

    void clearUserData();

    Set<UserStorage> getUserStorage();

    void registerUserStorage(UserStorage userStorage);
}
