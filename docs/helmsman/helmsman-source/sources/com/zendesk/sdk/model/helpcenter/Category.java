package com.zendesk.sdk.model.helpcenter;

import java.io.Serializable;
import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class Category implements Serializable {
    private Date createdAt;
    private String description;
    private String htmlUrl;
    private Long id;
    private String locale;
    private String name;
    private boolean outdated;
    private int position;
    private String sourceLocale;
    private Date updatedAt;
    private String url;

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public int getPosition() {
        return this.position;
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

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getSourceLocale() {
        return this.sourceLocale;
    }

    public boolean isOutdated() {
        return this.outdated;
    }
}
