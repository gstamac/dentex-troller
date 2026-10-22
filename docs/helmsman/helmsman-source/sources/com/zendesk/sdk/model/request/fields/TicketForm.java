package com.zendesk.sdk.model.request.fields;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class TicketForm {
    private long id;
    private String name;
    private List<TicketField> ticketFields;

    public static TicketForm create(RawTicketForm rawTicketForm, List<TicketField> list) {
        return new TicketForm(rawTicketForm.getId(), rawTicketForm.getName(), list);
    }

    public TicketForm(long j, String str, List<TicketField> list) {
        this.id = j;
        this.name = str;
        this.ticketFields = CollectionUtils.copyOf(list);
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<TicketField> getTicketFields() {
        return CollectionUtils.copyOf(this.ticketFields);
    }
}
