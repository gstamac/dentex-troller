package com.zendesk.sdk.storage;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.settings.MobileSettings;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
class StubSdkSettingsStorage implements SdkSettingsStorage {
    private static final String LOG_TAG = "StubSdkStorage";

    StubSdkSettingsStorage() {
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public boolean hasStoredSdkSettings() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return false;
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public boolean areSettingsUpToDate(long j, TimeUnit timeUnit) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return false;
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public SafeMobileSettings getStoredSettings() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
        return new SafeMobileSettings(new MobileSettings());
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public void setStoredSettings(SafeMobileSettings safeMobileSettings) {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public void deleteStoredSettings() {
        Logger.w(LOG_TAG, "Zendesk not initialised", new Object[0]);
    }
}
