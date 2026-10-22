package com.zendesk.sdk.model.request.fields;

/* JADX INFO: loaded from: classes2.dex */
public class TicketFieldOption {
    private long id;
    private boolean isDefault;
    private String name;
    private String value;

    public static TicketFieldOption create(RawTicketFieldOption rawTicketFieldOption) {
        return new TicketFieldOption(rawTicketFieldOption.getId(), rawTicketFieldOption.getName(), rawTicketFieldOption.getValue(), rawTicketFieldOption.isDefault());
    }

    public TicketFieldOption(long j, String str, String str2, boolean z) {
        this.id = j;
        this.name = str;
        this.value = str2;
        this.isDefault = z;
    }

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
