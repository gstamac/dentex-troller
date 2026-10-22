package com.zendesk.sdk.model.helpcenter.help;

import com.google.gson.annotations.SerializedName;
import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SectionItem implements HelpItem {

    @SerializedName("category_id")
    private Long categoryId;
    private List<HelpItem> children;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private Long sectionId;

    @SerializedName("article_count")
    private int totalArticlesCount;

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public int getViewType() {
        return 2;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public String getName() {
        String str = this.name;
        return str == null ? "" : str;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public Long getId() {
        return this.sectionId;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public Long getParentId() {
        return this.categoryId;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public List<HelpItem> getChildren() {
        return CollectionUtils.copyOf(this.children);
    }

    public void addChild(HelpItem helpItem) {
        if (this.children == null) {
            this.children = new ArrayList(1);
        }
        if (helpItem != null) {
            this.children.add(helpItem);
        }
    }

    public void addChildren(List<HelpItem> list) {
        List<HelpItem> list2 = this.children;
        if (list2 == null) {
            this.children = CollectionUtils.copyOf(list);
        } else {
            list2.addAll(list);
        }
    }

    public void removeChild(HelpItem helpItem) {
        List<HelpItem> list = this.children;
        if (list != null) {
            list.remove(helpItem);
        }
    }

    public int getTotalArticlesCount() {
        return this.totalArticlesCount;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SectionItem sectionItem = (SectionItem) obj;
        Long l = this.sectionId;
        if (l == null ? sectionItem.sectionId != null : !l.equals(sectionItem.sectionId)) {
            return false;
        }
        Long l2 = this.categoryId;
        Long l3 = sectionItem.categoryId;
        if (l2 != null) {
            return l2.equals(l3);
        }
        return l3 == null;
    }

    public int hashCode() {
        Long l = this.sectionId;
        int iHashCode = (l != null ? l.hashCode() : 0) * 31;
        Long l2 = this.categoryId;
        return iHashCode + (l2 != null ? l2.hashCode() : 0);
    }
}
