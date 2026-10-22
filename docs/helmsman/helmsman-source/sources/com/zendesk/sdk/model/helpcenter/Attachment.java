package com.zendesk.sdk.model.helpcenter;

import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class Attachment {
    private Long articleId;
    private String contentType;
    private String contentUrl;
    private Date createdAt;
    private String fileName;
    private Long id;
    private Long size;
    private Date updatedAt;
    private String url;

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public Long getArticleId() {
        return this.articleId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getContentUrl() {
        return this.contentUrl;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Long getSize() {
        return this.size;
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
