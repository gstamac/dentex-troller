package com.zendesk.sdk.model.network;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ResponseWrapper {
    private Integer count;
    private String nextPage;
    private String previousPage;

    public Integer getCount() {
        return this.count;
    }

    public String getNextPage() {
        return this.nextPage;
    }

    public String getPreviousPage() {
        return this.previousPage;
    }
}
