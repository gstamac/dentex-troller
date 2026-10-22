package com.zendesk.sdk.model.request.fields;

import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RawTicketFormResponse {
    private List<RawTicketField> ticketFields;
    private List<RawTicketForm> ticketForms;

    public List<RawTicketForm> getTicketForms() {
        return CollectionUtils.copyOf(this.ticketForms);
    }

    public List<RawTicketField> getTicketFields() {
        return CollectionUtils.copyOf(this.ticketFields);
    }
}
