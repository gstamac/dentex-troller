package com.zendesk.sdk.model.request.fields;

import com.google.gson.annotations.SerializedName;
import com.zendesk.util.CollectionUtils;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RawTicketField {
    private long accountId;

    @SerializedName("isActive")
    private boolean active;

    @SerializedName("isCollapsedForAgents")
    private boolean collapsedForAgents;
    private Date createdAt;
    private List<RawTicketFieldOption> customFieldOptions;
    private String defaultContentKey;
    private String description;

    @SerializedName("isEditableInPortal")
    private boolean editableInPortal;

    @SerializedName("isExportable")
    private boolean exportable;
    private long id;
    private int position;
    private String regexpForValidation;

    @SerializedName("isRequired")
    private boolean required;

    @SerializedName("isRequiredInPortal")
    private boolean requiredInPortal;
    private long subTypeId;
    private String tag;
    private String title;
    private String titleInPortal;
    private TicketFieldType type;
    private Date updatedAt;

    @SerializedName("isVisibleInPortal")
    private boolean visibleInPortal;

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public TicketFieldType getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public String getRegexpForValidation() {
        return this.regexpForValidation;
    }

    public List<RawTicketFieldOption> getCustomFieldOptions() {
        return CollectionUtils.copyOf(this.customFieldOptions);
    }
}
