package com.zendesk.sdk.model.helpcenter.help;

import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ArticleItem implements HelpItem {
    private Long id;
    private String name;

    @SerializedName("section_id")
    private Long sectionId;

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public int getViewType() {
        return 3;
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
    public Long getParentId() {
        return this.sectionId;
    }

    @Override // com.zendesk.sdk.model.helpcenter.help.HelpItem
    public List<HelpItem> getChildren() {
        return Collections.emptyList();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ArticleItem articleItem = (ArticleItem) obj;
        Long l = this.id;
        if (l == null ? articleItem.id != null : !l.equals(articleItem.id)) {
            return false;
        }
        Long l2 = this.sectionId;
        Long l3 = articleItem.sectionId;
        if (l2 != null) {
            return l2.equals(l3);
        }
        return l3 == null;
    }

    public int hashCode() {
        Long l = this.id;
        int iHashCode = (l != null ? l.hashCode() : 0) * 31;
        Long l2 = this.sectionId;
        return iHashCode + (l2 != null ? l2.hashCode() : 0);
    }
}
