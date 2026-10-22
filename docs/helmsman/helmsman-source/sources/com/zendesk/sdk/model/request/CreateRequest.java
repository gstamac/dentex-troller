package com.zendesk.sdk.model.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class CreateRequest {
    private static final transient String METADATA_SDK_KEY = "sdk";
    private Comment comment;
    private List<CustomField> customFields;
    private String email;
    private String id;
    private Map<String, Map<String, String>> metadata;
    private String subject;
    private List<String> tags;
    private Long ticketFormId;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> list) {
        this.tags = list;
    }

    public String getDescription() {
        Comment comment = this.comment;
        if (comment == null) {
            return null;
        }
        return comment.getBody();
    }

    public void setDescription(String str) {
        if (this.comment == null) {
            this.comment = new Comment();
        }
        this.comment.setBody(str);
    }

    public void setAttachments(List<String> list) {
        if (this.comment == null) {
            this.comment = new Comment();
        }
        this.comment.setAttachments(list);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String str) {
        this.subject = str;
    }

    public void setCustomFields(List<CustomField> list) {
        this.customFields = list;
    }

    public void setMetadata(Map<String, String> map) {
        HashMap map2 = new HashMap();
        this.metadata = map2;
        map2.put("sdk", map);
    }

    public void setTicketFormId(Long l) {
        this.ticketFormId = l;
    }
}
