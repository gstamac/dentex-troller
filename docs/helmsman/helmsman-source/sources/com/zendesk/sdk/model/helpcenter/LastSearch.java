package com.zendesk.sdk.model.helpcenter;

/* JADX INFO: loaded from: classes2.dex */
public class LastSearch {
    private final String origin = "mobile_sdk";
    private final String query;
    private final int resultsCount;

    public LastSearch(String str, int i) {
        this.query = str;
        this.resultsCount = i;
    }

    public String getQuery() {
        return this.query;
    }

    public int getResultsCount() {
        return this.resultsCount;
    }
}
