package com.zendesk.sdk.network;

import com.zendesk.service.ErrorResponse;
import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public interface SubmissionListener extends Serializable {
    void onSubmissionCancel();

    void onSubmissionCompleted();

    void onSubmissionError(ErrorResponse errorResponse);

    void onSubmissionStarted();
}
