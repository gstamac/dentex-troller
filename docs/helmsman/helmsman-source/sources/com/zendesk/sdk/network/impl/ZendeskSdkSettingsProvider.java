package com.zendesk.sdk.network.impl;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.settings.MobileSettings;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.SdkSettingsProvider;
import com.zendesk.sdk.storage.SdkSettingsStorage;
import com.zendesk.service.ZendeskCallback;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskSdkSettingsProvider implements SdkSettingsProvider {
    private static final String LOG_TAG = "ZendeskSdkSettingsProvider";
    private final String applicationId;
    private final Locale deviceLocale;
    private final HelpCenterLocaleConverter localeConverter;
    private final SdkSettingsStorage sdkSettingsStorage;
    private final ZendeskSdkSettingsService settingsService;

    ZendeskSdkSettingsProvider(ZendeskSdkSettingsService zendeskSdkSettingsService, Locale locale, SdkSettingsStorage sdkSettingsStorage, String str, HelpCenterLocaleConverter helpCenterLocaleConverter) {
        this.settingsService = zendeskSdkSettingsService;
        this.deviceLocale = locale;
        this.sdkSettingsStorage = sdkSettingsStorage;
        this.applicationId = str;
        this.localeConverter = helpCenterLocaleConverter;
    }

    @Override // com.zendesk.sdk.network.SdkSettingsProvider
    public void getSettings(final ZendeskCallback<SafeMobileSettings> zendeskCallback) {
        final String helpCenterLocaleString = this.localeConverter.toHelpCenterLocaleString(this.deviceLocale);
        this.settingsService.getSettings(helpCenterLocaleString, this.applicationId, new PassThroughErrorZendeskCallback<MobileSettings>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskSdkSettingsProvider.1
            @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
            public void onSuccess(MobileSettings mobileSettings) {
                Logger.d(ZendeskSdkSettingsProvider.LOG_TAG, "Successfully retrieved SDK Settings", new Object[0]);
                SafeMobileSettings safeMobileSettings = new SafeMobileSettings(mobileSettings);
                if (!helpCenterLocaleString.equals(safeMobileSettings.getHelpCenterLocale())) {
                    Logger.w(ZendeskSdkSettingsProvider.LOG_TAG, "No support for %s, Help Center is %s in your application settings", helpCenterLocaleString, Boolean.valueOf(safeMobileSettings.isHelpCenterEnabled()));
                }
                ZendeskSdkSettingsProvider.this.sdkSettingsStorage.setStoredSettings(safeMobileSettings);
                if (zendeskCallback != null) {
                    Logger.d(ZendeskSdkSettingsProvider.LOG_TAG, "Calling back with successful result", new Object[0]);
                    zendeskCallback.onSuccess(safeMobileSettings);
                }
            }
        });
    }
}
