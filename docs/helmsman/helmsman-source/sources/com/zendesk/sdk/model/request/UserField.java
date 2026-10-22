package com.zendesk.sdk.model.request;

import com.google.gson.annotations.SerializedName;
import com.zendesk.util.CollectionUtils;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class UserField {
    private Boolean active;
    private Date createdAt;
    private List<UserFieldOption> customFieldOptions;
    private String description;
    private Long id;
    private String key;
    private Long position;
    private String rawDescription;
    private String rawTitle;
    private String regexpForValidation;
    private Boolean system;
    private String title;
    private Date updatedAt;
    private String url;

    @SerializedName("type")
    private UserFieldType userFieldType;

    enum UserFieldType {
        Integer,
        Decimal,
        Checkbox,
        Date,
        Text,
        Textarea,
        Dropdown,
        Regexp
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public UserFieldType getUserFieldType() {
        return this.userFieldType;
    }

    public String getKey() {
        return this.key;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getRawDescription() {
        return this.rawDescription;
    }

    public String getRawTitle() {
        return this.rawTitle;
    }

    public Long getPosition() {
        return this.position;
    }

    public boolean isActive() {
        Boolean bool = this.active;
        return bool != null && bool.booleanValue();
    }

    public boolean isSystem() {
        Boolean bool = this.system;
        return bool != null && bool.booleanValue();
    }

    public String getRegexpForValidation() {
        return this.regexpForValidation;
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

    public List<UserFieldOption> getUserFieldOptions() {
        return CollectionUtils.copyOf(this.customFieldOptions);
    }
}
