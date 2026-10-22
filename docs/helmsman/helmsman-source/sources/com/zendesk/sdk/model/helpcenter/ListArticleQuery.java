package com.zendesk.sdk.model.helpcenter;

import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class ListArticleQuery {
    private String mInclude;
    private String mLabelNames;
    private Locale mLocale;
    private Integer mPage;
    private Integer mResultsPerPage;
    private SortBy mSortBy;
    private SortOrder mSortOrder;

    public String getLabelNames() {
        return this.mLabelNames;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public String getInclude() {
        return this.mInclude;
    }

    public SortBy getSortBy() {
        return this.mSortBy;
    }

    public SortOrder getSortOrder() {
        return this.mSortOrder;
    }

    public Integer getPage() {
        return this.mPage;
    }

    public Integer getResultsPerPage() {
        return this.mResultsPerPage;
    }

    public void setLabelNames(String str) {
        this.mLabelNames = str;
    }

    public void setLocale(Locale locale) {
        this.mLocale = locale;
    }

    public void setInclude(String str) {
        this.mInclude = str;
    }

    public void setSortBy(SortBy sortBy) {
        this.mSortBy = sortBy;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.mSortOrder = sortOrder;
    }

    public void setPage(Integer num) {
        this.mPage = num;
    }

    public void setResultsPerPage(Integer num) {
        this.mResultsPerPage = num;
    }
}
