package com.zendesk.sdk.model.push;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes2.dex */
public class PushRegistrationResponseWrapper {

    @SerializedName("push_notification_device")
    private PushRegistrationResponse registrationResponse;

    public PushRegistrationResponse getRegistrationResponse() {
        return this.registrationResponse;
    }
}
