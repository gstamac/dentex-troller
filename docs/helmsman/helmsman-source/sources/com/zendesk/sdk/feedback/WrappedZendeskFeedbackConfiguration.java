package com.zendesk.sdk.feedback;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class WrappedZendeskFeedbackConfiguration implements ZendeskFeedbackConfiguration {
    private String mAdditionalInfo;
    private String mRequestSubject;
    private List<String> mTags;

    public WrappedZendeskFeedbackConfiguration(ZendeskFeedbackConfiguration zendeskFeedbackConfiguration) {
        if (zendeskFeedbackConfiguration != null) {
            this.mTags = zendeskFeedbackConfiguration.getTags();
            this.mAdditionalInfo = zendeskFeedbackConfiguration.getAdditionalInfo();
            this.mRequestSubject = zendeskFeedbackConfiguration.getRequestSubject();
        }
    }

    @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
    public List<String> getTags() {
        return this.mTags;
    }

    @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
    public String getAdditionalInfo() {
        return this.mAdditionalInfo;
    }

    @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
    public String getRequestSubject() {
        return this.mRequestSubject;
    }
}
