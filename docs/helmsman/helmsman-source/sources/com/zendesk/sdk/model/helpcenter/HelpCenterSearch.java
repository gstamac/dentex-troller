package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class HelpCenterSearch implements Serializable, Cloneable {
    private String categoryIds;
    private String include;
    private String labelNames;
    private Locale locale;
    private Integer page;
    private Integer perPage;
    private String query;
    private String sectionIds;

    private HelpCenterSearch() {
    }

    public String getQuery() {
        return this.query;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getInclude() {
        return this.include;
    }

    public String getLabelNames() {
        return this.labelNames;
    }

    public String getCategoryIds() {
        return this.categoryIds;
    }

    public String getSectionIds() {
        return this.sectionIds;
    }

    public Integer getPage() {
        return this.page;
    }

    public Integer getPerPage() {
        return this.perPage;
    }

    public HelpCenterSearch withQuery(String str) {
        HelpCenterSearch helpCenterSearch = new HelpCenterSearch();
        try {
            HelpCenterSearch helpCenterSearch2 = (HelpCenterSearch) clone();
            try {
                helpCenterSearch2.query = str;
                return helpCenterSearch2;
            } catch (CloneNotSupportedException e) {
                e = e;
                helpCenterSearch = helpCenterSearch2;
                e.printStackTrace();
                return helpCenterSearch;
            }
        } catch (CloneNotSupportedException e2) {
            e = e2;
        }
    }

    public static class Builder {
        private String categoryIds;
        private String[] include;
        private String[] labelNames;
        private Locale locale;
        private Integer page;
        private Integer perPage;
        private String query;
        private String sectionIds;

        public Builder withQuery(String str) {
            this.query = str;
            return this;
        }

        public Builder forLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder withIncludes(String... strArr) {
            this.include = strArr;
            return this;
        }

        public Builder withLabelNames(String... strArr) {
            this.labelNames = strArr;
            return this;
        }

        public Builder withCategoryId(Long l) {
            if (l != null) {
                this.categoryIds = Long.toString(l.longValue());
            }
            return this;
        }

        public Builder withCategoryIds(List<Long> list) {
            this.categoryIds = StringUtils.toCsvStringNumber((List<? extends Number>) CollectionUtils.copyOf(list));
            return this;
        }

        public Builder withSectionId(Long l) {
            if (l != null) {
                this.sectionIds = Long.toString(l.longValue());
            }
            return this;
        }

        public Builder withSectionIds(List<Long> list) {
            this.sectionIds = StringUtils.toCsvStringNumber((List<? extends Number>) CollectionUtils.copyOf(list));
            return this;
        }

        public Builder page(Integer num) {
            this.page = num;
            return this;
        }

        public Builder perPage(Integer num) {
            this.perPage = num;
            return this;
        }

        public HelpCenterSearch build() {
            HelpCenterSearch helpCenterSearch = new HelpCenterSearch();
            helpCenterSearch.query = this.query;
            helpCenterSearch.locale = this.locale;
            helpCenterSearch.include = StringUtils.toCsvString(this.include);
            helpCenterSearch.labelNames = StringUtils.toCsvString(this.labelNames);
            helpCenterSearch.categoryIds = this.categoryIds;
            helpCenterSearch.sectionIds = this.sectionIds;
            helpCenterSearch.page = this.page;
            helpCenterSearch.perPage = this.perPage;
            return helpCenterSearch;
        }
    }
}
