package com.zendesk.sdk.feedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BaseZendeskFeedbackConfiguration implements ZendeskFeedbackConfiguration, Serializable {
    @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
    public String getAdditionalInfo() {
        return "";
    }

    @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
    public List<String> getTags() {
        return new ArrayList();
    }
}
