package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.StringUtils;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class SuggestedArticleSearch {
    private Long mCategoryId;
    private String mLabelNames;
    private Locale mLocale;
    private String mQuery;
    private Long mSectionId;

    private SuggestedArticleSearch() {
    }

    public String getQuery() {
        return this.mQuery;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public String getLabelNames() {
        return this.mLabelNames;
    }

    public Long getCategoryId() {
        return this.mCategoryId;
    }

    public Long getSectionId() {
        return this.mSectionId;
    }

    public static class Builder {
        private Long mCategoryId;
        private String[] mLabelNames;
        private Locale mLocale;
        private String mQuery;
        private Long mSectionId;

        public Builder withQuery(String str) {
            this.mQuery = str;
            return this;
        }

        public Builder forLocale(Locale locale) {
            this.mLocale = locale;
            return this;
        }

        public Builder withLabelNames(String... strArr) {
            this.mLabelNames = strArr;
            return this;
        }

        public Builder withCategoryId(Long l) {
            this.mCategoryId = l;
            return this;
        }

        public Builder withSectionId(Long l) {
            this.mSectionId = l;
            return this;
        }

        public SuggestedArticleSearch build() {
            SuggestedArticleSearch suggestedArticleSearch = new SuggestedArticleSearch();
            suggestedArticleSearch.mQuery = this.mQuery;
            suggestedArticleSearch.mLocale = this.mLocale;
            suggestedArticleSearch.mLabelNames = StringUtils.toCsvString(this.mLabelNames);
            suggestedArticleSearch.mCategoryId = this.mCategoryId;
            suggestedArticleSearch.mSectionId = this.mSectionId;
            return suggestedArticleSearch;
        }
    }
}
