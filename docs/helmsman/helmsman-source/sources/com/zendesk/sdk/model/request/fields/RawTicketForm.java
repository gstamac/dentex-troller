package com.zendesk.sdk.model.request.fields;

import com.google.gson.annotations.SerializedName;
import com.zendesk.util.CollectionUtils;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RawTicketForm {
    private Date createdAt;
    private boolean endUserVisible;
    private long id;
    private boolean inAllBrands;
    private boolean inAllOrganizations;

    @SerializedName("active")
    private boolean isActive;

    @SerializedName("default")
    private boolean isDefault;
    private String name;
    private int position;
    private String rawDisplayName;
    private String rawName;
    private List<Long> restrictedBrandIds;
    private List<Long> restrictedOrganizationIds;
    private List<Long> ticketFieldIds;
    private Date updatedAt;
    private String url;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Long> getTicketFieldIds() {
        return CollectionUtils.copyOf(this.ticketFieldIds);
    }
}
