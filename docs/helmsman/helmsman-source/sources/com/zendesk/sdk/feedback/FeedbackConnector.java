package com.zendesk.sdk.feedback;

import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.service.ZendeskCallback;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface FeedbackConnector extends Serializable {
    boolean isValid();

    void sendFeedback(String str, List<String> list, ZendeskCallback<CreateRequest> zendeskCallback);
}
