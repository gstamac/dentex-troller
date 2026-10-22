package com.zendesk.sdk.model.helpcenter.help;

import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SeeAllArticlesItem implements HelpItem {
    private boolean isLoading;
    private SectionItem section;

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public String getName() {
        return "";
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public int getViewType() {
        return 4;
    }

    public SeeAllArticlesItem(SectionItem sectionItem) {
        this.section = sectionItem;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public Long getId() {
        return this.section.getId();
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public Long getParentId() {
        return this.section.getParentId();
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public List<? extends HelpItem> getChildren() {
        return Collections.emptyList();
    }

    public SectionItem getSection() {
        return this.section;
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setLoading(boolean z) {
        this.isLoading = z;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SectionItem sectionItem = this.section;
        SectionItem sectionItem2 = ((SeeAllArticlesItem) obj).section;
        if (sectionItem != null) {
            return sectionItem.equals(sectionItem2);
        }
        return sectionItem2 == null;
    }

    public int hashCode() {
        SectionItem sectionItem = this.section;
        if (sectionItem != null) {
            return sectionItem.hashCode();
        }
        return 0;
    }
}
