package com.zendesk.sdk.feedback;

import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface ZendeskFeedbackConfiguration extends Serializable {
    String getAdditionalInfo();

    String getRequestSubject();

    List<String> getTags();
}
