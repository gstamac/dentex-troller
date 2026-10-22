package com.zendesk.sdk.storage;

import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.access.Identity;

/* JADX INFO: loaded from: classes2.dex */
public interface IdentityStorage extends SdkStorage.UserStorage {
    Identity anonymiseIdentity();

    Identity getIdentity();

    AccessToken getStoredAccessToken();

    String getStoredAccessTokenAsBearerToken();

    String getUUID();

    void storeAccessToken(AccessToken accessToken);

    void storeIdentity(Identity identity);
}
