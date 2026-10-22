package com.zendesk.sdk.model.request;

import com.google.gson.annotations.SerializedName;
import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class EndUserComment {

    @SerializedName("uploads")
    private List<String> attachments;
    private String value;

    public void setValue(String str) {
        this.value = str;
    }

    public List<String> getAttachments() {
        return CollectionUtils.copyOf(this.attachments);
    }

    public void setAttachments(List<String> list) {
        this.attachments = list;
    }
}
