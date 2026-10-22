package com.zendesk.sdk.network;

import com.zendesk.sdk.model.push.PushRegistrationResponse;
import com.zendesk.service.ZendeskCallback;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public interface PushRegistrationProvider {
    void registerDeviceWithIdentifier(String str, Locale locale, ZendeskCallback<PushRegistrationResponse> zendeskCallback);

    void registerDeviceWithUAChannelId(String str, Locale locale, ZendeskCallback<PushRegistrationResponse> zendeskCallback);

    void unregisterDevice(String str, ZendeskCallback<Void> zendeskCallback);
}
