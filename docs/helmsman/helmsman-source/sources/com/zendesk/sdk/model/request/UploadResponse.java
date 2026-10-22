package com.zendesk.sdk.model.request;

import java.util.Date;

/* JADX INFO: loaded from: classes2.dex */
public class UploadResponse {
    private Attachment attachment;
    private Date expiresAt;
    private String token;

    public String getToken() {
        return this.token;
    }

    public Date getExpiresAt() {
        if (this.expiresAt == null) {
            return null;
        }
        return new Date(this.expiresAt.getTime());
    }

    public Attachment getAttachment() {
        return this.attachment;
    }
}
