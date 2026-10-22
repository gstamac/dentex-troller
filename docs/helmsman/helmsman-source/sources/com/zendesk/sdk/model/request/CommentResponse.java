package com.zendesk.sdk.model.request;

import com.google.gson.annotations.SerializedName;
import com.zendesk.util.CollectionUtils;
import im.helmsman.helmsmanandroid.factory.RetrofitFactory;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class CommentResponse {
    private List<Attachment> attachments;
    private Long authorId;
    private String body;
    private Date createdAt;
    private String htmlBody;
    private Long id;

    @SerializedName(RetrofitFactory.PUBLIC)
    private boolean isPublic = true;
    private String requestId;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String str) {
        this.body = str;
    }

    public String getHtmlBody() {
        return this.htmlBody;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId(Long l) {
        this.authorId = l;
    }

    public List<Attachment> getAttachments() {
        return CollectionUtils.copyOf(this.attachments);
    }

    public void setAttachments(List<Attachment> list) {
        this.attachments = list;
    }

    public Date getCreatedAt() {
        if (this.createdAt == null) {
            return null;
        }
        return new Date(this.createdAt.getTime());
    }

    public void setCreatedAt(Date date) {
        this.createdAt = date;
    }
}
