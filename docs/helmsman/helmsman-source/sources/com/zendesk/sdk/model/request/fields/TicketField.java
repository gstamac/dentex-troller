package com.zendesk.sdk.model.request.fields;

import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class TicketField {
    private String description;
    private long id;
    private String regexpForValidation;
    private List<TicketFieldOption> ticketFieldOptions;
    private String title;
    private TicketFieldType type;

    public static TicketField create(RawTicketField rawTicketField) {
        ArrayList arrayList = new ArrayList();
        Iterator<RawTicketFieldOption> it = rawTicketField.getCustomFieldOptions().iterator();
        while (it.hasNext()) {
            arrayList.add(TicketFieldOption.create(it.next()));
        }
        return new TicketField(rawTicketField.getId(), rawTicketField.getType(), rawTicketField.getTitle(), rawTicketField.getDescription(), rawTicketField.getRegexpForValidation(), arrayList);
    }

    public TicketField(long j, TicketFieldType ticketFieldType, String str, String str2, String str3, List<TicketFieldOption> list) {
        this.id = j;
        this.type = ticketFieldType;
        this.title = str;
        this.description = str2;
        this.regexpForValidation = str3;
        this.ticketFieldOptions = list;
    }

    public long getId() {
        return this.id;
    }

    public TicketFieldType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getRegexpForValidation() {
        return this.regexpForValidation;
    }

    public List<TicketFieldOption> getTicketFieldOptions() {
        return CollectionUtils.copyOf(this.ticketFieldOptions);
    }
}
