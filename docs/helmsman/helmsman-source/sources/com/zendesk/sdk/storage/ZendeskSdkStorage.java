package com.zendesk.sdk.storage;

import com.zendesk.logger.Logger;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskSdkStorage implements SdkStorage {
    private static final String LOG_TAG = "ZendeskSdkStorage";
    private final Set<SdkStorage.UserStorage> mUserStorage = Collections.synchronizedSet(new LinkedHashSet());

    ZendeskSdkStorage() {
    }

    @Override // com.zendesk.sdk.storage.SdkStorage
    public void registerUserStorage(SdkStorage.UserStorage userStorage) {
        Set<SdkStorage.UserStorage> set = this.mUserStorage;
        if (set == null) {
            Logger.e(LOG_TAG, "Additional storage set is null, returning...", new Object[0]);
            return;
        }
        if (userStorage == null) {
            Logger.e(LOG_TAG, "Supplied UserStorage is null, returning...", new Object[0]);
            return;
        }
        synchronized (set) {
            Iterator<SdkStorage.UserStorage> it = this.mUserStorage.iterator();
            boolean z = false;
            while (it.hasNext() && !z) {
                SdkStorage.UserStorage next = it.next();
                if (next != null && next.getClass().equals(userStorage.getClass())) {
                    Logger.d(LOG_TAG, "There is already an instance of %s registered, will not add a duplicate", userStorage.getClass().getSimpleName());
                    if (!next.getCacheKey().equals(userStorage.getCacheKey())) {
                        next.clearUserData();
                    }
                    it.remove();
                    z = true;
                }
            }
        }
        Logger.d(LOG_TAG, "Additional user storage size %s, modified %s", Integer.valueOf(this.mUserStorage.size()), Boolean.valueOf(this.mUserStorage.add(userStorage)));
    }

    @Override // com.zendesk.sdk.storage.SdkStorage
    public void clearUserData() {
        Set<SdkStorage.UserStorage> set = this.mUserStorage;
        if (set == null) {
            Logger.w(LOG_TAG, "Additional user storage is null, returning...", new Object[0]);
            return;
        }
        synchronized (set) {
            for (SdkStorage.UserStorage userStorage : this.mUserStorage) {
                if (userStorage != null) {
                    userStorage.clearUserData();
                }
            }
        }
        this.mUserStorage.clear();
    }

    @Override // com.zendesk.sdk.storage.SdkStorage
    public Set<SdkStorage.UserStorage> getUserStorage() {
        return Collections.unmodifiableSet(this.mUserStorage);
    }
}
