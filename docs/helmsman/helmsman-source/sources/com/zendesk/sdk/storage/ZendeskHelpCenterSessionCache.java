package com.zendesk.sdk.storage;

import com.zendesk.sdk.model.helpcenter.LastSearch;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskHelpCenterSessionCache implements HelpCenterSessionCache {
    public static final LastSearch DEFAULT_SEARCH = new LastSearch("", 0);
    private LastSearch lastSearch;
    private boolean uniqueSearchResultClick = false;

    ZendeskHelpCenterSessionCache() {
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public LastSearch getLastSearch() {
        LastSearch lastSearch = this.lastSearch;
        return lastSearch != null ? lastSearch : DEFAULT_SEARCH;
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public void setLastSearch(String str, int i) {
        this.lastSearch = new LastSearch(str, i);
        this.uniqueSearchResultClick = true;
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public void unsetUniqueSearchResultClick() {
        this.uniqueSearchResultClick = false;
    }

    @Override // com.zendesk.sdk.storage.HelpCenterSessionCache
    public boolean isUniqueSearchResultClick() {
        return this.uniqueSearchResultClick;
    }

    void reset() {
        this.lastSearch = null;
        this.uniqueSearchResultClick = false;
    }
}
