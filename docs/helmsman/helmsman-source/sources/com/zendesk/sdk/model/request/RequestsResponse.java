package com.zendesk.sdk.model.request;

import com.zendesk.sdk.model.network.ResponseWrapper;
import com.zendesk.util.CollectionUtils;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RequestsResponse extends ResponseWrapper {
    private List<Request> requests;

    public List<Request> getRequests() {
        return CollectionUtils.copyOf(this.requests);
    }
}
