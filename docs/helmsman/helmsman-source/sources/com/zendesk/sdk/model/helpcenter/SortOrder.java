package com.zendesk.sdk.model.helpcenter;

/* JADX INFO: loaded from: classes2.dex */
public enum SortOrder {
    ASCENDING("asc"),
    DESCENDING("desc");

    private final String apiValue;

    SortOrder(String str) {
        this.apiValue = str;
    }

    public String getApiValue() {
        return this.apiValue;
    }
}
