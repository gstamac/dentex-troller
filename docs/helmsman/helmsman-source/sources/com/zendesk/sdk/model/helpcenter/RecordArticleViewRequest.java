package com.zendesk.sdk.model.helpcenter;

/* JADX INFO: loaded from: classes2.dex */
public class RecordArticleViewRequest {
    private LastSearch lastSearch;
    private boolean uniqueSearchResultClick;

    public RecordArticleViewRequest(LastSearch lastSearch, boolean z) {
        this.lastSearch = lastSearch;
        this.uniqueSearchResultClick = z;
    }
}
