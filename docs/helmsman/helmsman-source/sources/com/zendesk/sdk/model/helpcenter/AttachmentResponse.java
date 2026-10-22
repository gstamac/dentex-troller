package com.zendesk.sdk.model.helpcenter;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AttachmentResponse {
    private List<Attachment> articleAttachments;

    public List<Attachment> getArticleAttachments() {
        return CollectionUtils.copyOf(this.articleAttachments);
    }
}
