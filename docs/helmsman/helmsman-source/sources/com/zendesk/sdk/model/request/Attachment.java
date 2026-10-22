package com.zendesk.sdk.model.request;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Attachment {
    private String contentType;
    private String contentUrl;
    private String fileName;
    private Long id;
    private String mappedContentUrl;
    private Long size;
    private List<Attachment> thumbnails;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public Long getId() {
        return this.id;
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

    public List<Attachment> getThumbnails() {
        return CollectionUtils.copyOf(this.thumbnails);
    }
}
