package com.zendesk.sdk.model.push;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes2.dex */
public class PushRegistrationRequestWrapper {

    @SerializedName("push_notification_device")
    private PushRegistrationRequest pushRegistrationRequest;

    public void setPushRegistrationRequest(PushRegistrationRequest pushRegistrationRequest) {
        this.pushRegistrationRequest = pushRegistrationRequest;
    }
}
