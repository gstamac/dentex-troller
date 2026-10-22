package com.zendesk.sdk.storage;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface RequestStorage extends SdkStorage.UserStorage {
    Integer getCommentCount(String str);

    List<String> getStoredRequestIds();

    void setCommentCount(String str, int i);

    void storeRequestId(String str);
}
