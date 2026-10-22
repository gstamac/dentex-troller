package com.zendesk.sdk.storage;

import com.zendesk.sdk.model.helpcenter.LastSearch;

/* JADX INFO: loaded from: classes2.dex */
public interface HelpCenterSessionCache {
    LastSearch getLastSearch();

    boolean isUniqueSearchResultClick();

    void setLastSearch(String str, int i);

    void unsetUniqueSearchResultClick();
}
