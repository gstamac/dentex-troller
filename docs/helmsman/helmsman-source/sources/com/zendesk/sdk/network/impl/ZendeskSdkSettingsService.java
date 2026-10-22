package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.settings.MobileSettings;
import com.zendesk.sdk.network.SdkSettingsService;
import com.zendesk.service.RetrofitZendeskCallbackAdapter;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskSdkSettingsService {
    private static final String LOG_TAG = "ZendeskSdkSettingsService";
    private final SdkSettingsService sdkSettingsService;

    ZendeskSdkSettingsService(SdkSettingsService sdkSettingsService) {
        this.sdkSettingsService = sdkSettingsService;
    }

    public void getSettings(String str, String str2, ZendeskCallback<MobileSettings> zendeskCallback) {
        this.sdkSettingsService.getSettings(str, str2).enqueue(new RetrofitZendeskCallbackAdapter(zendeskCallback));
    }
}
