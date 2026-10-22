package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.push.PushRegistrationRequestWrapper;
import com.zendesk.sdk.model.push.PushRegistrationResponseWrapper;
import com.zendesk.sdk.network.PushRegistrationService;
import com.zendesk.service.RetrofitZendeskCallbackAdapter;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskPushRegistrationService {
    private static final String LOG_TAG = "ZendeskPushRegistrationService";
    private final PushRegistrationService pushService;

    ZendeskPushRegistrationService(PushRegistrationService pushRegistrationService) {
        this.pushService = pushRegistrationService;
    }

    public void registerDevice(String str, PushRegistrationRequestWrapper pushRegistrationRequestWrapper, ZendeskCallback<PushRegistrationResponseWrapper> zendeskCallback) {
        this.pushService.registerDevice(str, pushRegistrationRequestWrapper).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }

    public void unregisterDevice(String str, String str2, ZendeskCallback<Void> zendeskCallback) {
        this.pushService.unregisterDevice(str, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }
}
