package com.zendesk.sdk.storage;

import com.zendesk.sdk.model.request.fields.TicketForm;
import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskRequestSessionCache implements RequestSessionCache {
    private final Map<Long, TicketForm> cachedTicketForms = new HashMap();

    ZendeskRequestSessionCache() {
    }

    @Override // com.zendesk.sdk.storage.RequestSessionCache
    public void updateTicketFormCache(List<TicketForm> list) {
        List<TicketForm> listEnsureEmpty = CollectionUtils.ensureEmpty(list);
        HashMap map = new HashMap();
        for (TicketForm ticketForm : listEnsureEmpty) {
            map.put(Long.valueOf(ticketForm.getId()), ticketForm);
        }
        synchronized (this.cachedTicketForms) {
            this.cachedTicketForms.putAll(map);
        }
    }

    @Override // com.zendesk.sdk.storage.RequestSessionCache
    public synchronized List<TicketForm> getTicketFormsById(List<Long> list) {
        ArrayList arrayList;
        arrayList = new ArrayList();
        List listEnsureEmpty = CollectionUtils.ensureEmpty(list);
        synchronized (this.cachedTicketForms) {
            Iterator it = listEnsureEmpty.iterator();
            while (it.hasNext()) {
                arrayList.add(this.cachedTicketForms.get((Long) it.next()));
            }
        }
        return arrayList;
    }

    @Override // com.zendesk.sdk.storage.RequestSessionCache
    public boolean containsAllTicketForms(List<Long> list) {
        boolean z;
        List listEnsureEmpty = CollectionUtils.ensureEmpty(list);
        synchronized (this.cachedTicketForms) {
            Iterator it = listEnsureEmpty.iterator();
            while (it.hasNext()) {
                if (!this.cachedTicketForms.containsKey((Long) it.next())) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }
}
