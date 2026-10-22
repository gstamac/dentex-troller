package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.SdkConfiguration;
import com.zendesk.sdk.model.request.UploadResponse;
import com.zendesk.sdk.model.request.UploadResponseWrapper;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.UploadProvider;
import com.zendesk.service.ZendeskCallback;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskUploadProvider implements UploadProvider {
    private static final String LOG_TAG = "ZendeskUploadProvider";
    private final BaseProvider baseProvider;
    private final ZendeskUploadService uploadService;

    ZendeskUploadProvider(BaseProvider baseProvider, ZendeskUploadService zendeskUploadService) {
        this.baseProvider = baseProvider;
        this.uploadService = zendeskUploadService;
    }

    @Override // com.zendesk.sdk.network.UploadProvider
    public void uploadAttachment(final String str, final File file, final String str2, final ZendeskCallback<UploadResponse> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUploadProvider.1
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskUploadProvider.this.uploadService.uploadAttachment(sdkConfiguration.getBearerAuthorizationHeader(), str, file, str2, new PassThroughErrorZendeskCallback<UploadResponseWrapper>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUploadProvider.1.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(UploadResponseWrapper uploadResponseWrapper) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(uploadResponseWrapper.getUpload());
                        }
                    }
                });
            }
        });
    }

    @Override // com.zendesk.sdk.network.UploadProvider
    public void deleteAttachment(final String str, final ZendeskCallback<Void> zendeskCallback) {
        this.baseProvider.configureSdk(new PassThroughErrorZendeskCallback<SdkConfiguration>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUploadProvider.2
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(SdkConfiguration sdkConfiguration) {
                ZendeskUploadProvider.this.uploadService.deleteAttachment(sdkConfiguration.getBearerAuthorizationHeader(), str, new PassThroughErrorZendeskCallback<Void>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskUploadProvider.2.1
                    @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                    public void onSuccess(Void r2) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onSuccess(r2);
                        }
                    }
                });
            }
        });
    }
}
