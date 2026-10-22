package com.zendesk.sdk.model.request;

import com.zendesk.util.CollectionUtils;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Request {
    private List<Long> collaboratorIds;
    private EndUserComment comment;
    private Integer commentCount;
    private Date createdAt;
    private String description;
    private Date dueAt;
    private String id;
    private Long organizationId;
    private String priority;
    private Date publicUpdatedAt;
    private Long requesterId;
    private String status;
    private String subject;
    private String type;
    private Date updatedAt;
    private String url;

    public String getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return this.status;
    }

    public String getPriority() {
        return this.priority;
    }

    public String getType() {
        return this.type;
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public Long getRequesterId() {
        return this.requesterId;
    }

    public List<Long> getCollaboratorIds() {
        return CollectionUtils.copyOf(this.collaboratorIds);
    }

    public Date getDueAt() {
        if (this.dueAt == null) {
            return null;
        }
        return new Date(this.dueAt.getTime());
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

    public Date getPublicUpdatedAt() {
        if (this.publicUpdatedAt == null) {
            return null;
        }
        return new Date(this.publicUpdatedAt.getTime());
    }

    public void setComment(EndUserComment endUserComment) {
        this.comment = endUserComment;
    }

    public Integer getCommentCount() {
        return this.commentCount;
    }
}
