package com.zendesk.sdk.model.helpcenter;

import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class ArticleVote {
    private Date createdAt;
    private Long id;
    private Long itemId;
    private String itemType;
    private Date updatedAt;
    private String url;
    private Long userId;
    private Integer value;

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Integer getValue() {
        return this.value;
    }

    public Long getItemId() {
        return this.itemId;
    }

    public String getItemType() {
        return this.itemType;
    }

    public Date getCreatedAt() {
        if (this.createdAt == null) {
            return null;
        }
        return new Date(this.createdAt.getTime());
    }

    public Date getUpdatedAt() {
        if (this.updatedAt == null) {
            return null;
        }
        return new Date(this.updatedAt.getTime());
    }
}
