package com.zendesk.sdk.model.helpcenter.help;

import com.google.gson.annotations.SerializedName;
import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CategoryItem implements HelpItem {
    private List<SectionItem> children;
    private boolean expanded = true;

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public Long getParentId() {
        return null;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public int getViewType() {
        return 1;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public String getName() {
        String str = this.name;
        return str == null ? "" : str;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public Long getId() {
        return this.id;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public List<? extends HelpItem> getChildren() {
        return CollectionUtils.copyOf(this.children);
    }

    public void setSections(List<SectionItem> list) {
        this.children = CollectionUtils.copyOf(list);
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public boolean setExpanded(boolean z) {
        this.expanded = z;
        return z;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Long l = this.id;
        Long l2 = ((CategoryItem) obj).id;
        if (l != null) {
            return l.equals(l2);
        }
        return l2 == null;
    }

    public int hashCode() {
        Long l = this.id;
        if (l != null) {
            return l.hashCode();
        }
        return 0;
    }
}
