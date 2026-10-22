package com.zendesk.sdk.model.request;

import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class User {
    private Long id = -1L;
    private String name = "";
    private Attachment photo = null;
    private boolean agent = false;
    private Long organizationId = -1L;
    private List<String> tags = new ArrayList();
    private Map<String, String> userFields = new HashMap();

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Attachment getPhoto() {
        return this.photo;
    }

    public boolean isAgent() {
        return this.agent;
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public List<String> getTags() {
        return CollectionUtils.copyOf(this.tags);
    }

    public Map<String, String> getUserFields() {
        return CollectionUtils.copyOf(this.userFields);
    }
}
