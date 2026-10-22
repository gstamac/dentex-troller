package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.request.UploadResponseWrapper;
import com.zendesk.sdk.network.UploadService;
import com.zendesk.service.RetrofitZendeskCallbackAdapter;
import com.zendesk.service.ZendeskCallback;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskUploadService {
    private static final String LOG_TAG = "ZendeskUploadService";
    private final UploadService uploadService;

    public ZendeskUploadService(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    void uploadAttachment(String str, String str2, File file, String str3, ZendeskCallback<UploadResponseWrapper> zendeskCallback) {
        this.uploadService.uploadAttachment(str, str2, RequestBody.create(MediaType.parse(str3), file)).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    void deleteAttachment(String str, String str2, ZendeskCallback<Void> zendeskCallback) {
        this.uploadService.deleteAttachment(str, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }
}
