package com.zendesk.sdk.model.push;

/* JADX INFO: loaded from: classes2.dex */
public class AnonymousPushRegistrationRequest extends PushRegistrationRequest {
    private String sdkGuid;

    public void setSdkGuid(String str) {
        this.sdkGuid = str;
    }

    public String getSdkGuid() {
        return this.sdkGuid;
    }
}
