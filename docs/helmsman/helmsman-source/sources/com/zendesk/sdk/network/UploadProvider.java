package com.zendesk.sdk.network;

import com.zendesk.sdk.model.request.UploadResponse;
import com.zendesk.service.ZendeskCallback;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
public interface UploadProvider {
    void deleteAttachment(String str, ZendeskCallback<Void> zendeskCallback);

    void uploadAttachment(String str, File file, String str2, ZendeskCallback<UploadResponse> zendeskCallback);
}
