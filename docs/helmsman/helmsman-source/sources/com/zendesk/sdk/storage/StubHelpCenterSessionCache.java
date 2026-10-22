package com.zendesk.sdk.storage;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.helpcenter.LastSearch;

/* JADX INFO: loaded from: classes2.dex */
class StubHelpCenterSessionCache implements HelpCenterSessionCache {
    private static final String LOG_TAG = "StubZendeskHelpCenterSessionCache";

    StubHelpCenterSessionCache() {
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public LastSearch getLastSearch() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return new LastSearch("", -1);
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public void setLastSearch(String str, int i) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public void unsetUniqueSearchResultClick() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public boolean isUniqueSearchResultClick() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return false;
    }
}
