package com.zendesk.sdk.rating.ui;

import android.content.Context;
import com.zendesk.sdk.R;
import com.zendesk.sdk.feedback.BaseZendeskFeedbackConfiguration;

/* JADX INFO: compiled from: RateMyAppDialog.java */
/* JADX INFO: loaded from: classes2.dex */
class DefaultContactConfiguration extends BaseZendeskFeedbackConfiguration {
    private String mSubject;

    public DefaultContactConfiguration(Context context) {
        this.mSubject = context == null ? "" : context.getString(R.string.rate_my_app_dialog_feedback_request_subject);
    }

    @Override // com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration
    public String getRequestSubject() {
        return this.mSubject;
    }
}
