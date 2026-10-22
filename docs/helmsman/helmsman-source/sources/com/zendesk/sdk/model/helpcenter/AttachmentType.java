package com.zendesk.sdk.model.helpcenter;

/* JADX INFO: loaded from: classes2.dex */
public enum AttachmentType {
    INLINE("inline"),
    BLOCK("block");

    private String attachmentType;

    AttachmentType(String str) {
        this.attachmentType = str;
    }

    public String getAttachmentType() {
        return this.attachmentType;
    }
}
