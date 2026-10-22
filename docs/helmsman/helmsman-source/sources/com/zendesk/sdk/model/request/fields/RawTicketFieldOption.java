package com.zendesk.sdk.model.request.fields;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes2.dex */
public class RawTicketFieldOption {
    private long id;

    @SerializedName("default")
    private boolean isDefault;
    private String name;
    private String rawName;
    private String value;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isDefault() {
        return this.isDefault;
    }
}
