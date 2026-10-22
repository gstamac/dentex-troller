package com.zendesk.sdk.storage;

import com.zendesk.sdk.model.request.fields.TicketForm;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface RequestSessionCache {
    boolean containsAllTicketForms(List<Long> list);

    List<TicketForm> getTicketFormsById(List<Long> list);

    void updateTicketFormCache(List<TicketForm> list);
}
