package com.zendesk.sdk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.settings.MobileSettings;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskSdkSettingsStorage implements SdkSettingsStorage {
    private static final String LOG_TAG = "SdkSettingsStorage";
    private static final String MOBILE_SETTINGS_KEY = "mobileconfiguration";
    private static final String MOBILE_SETTINGS_TIMESTAMP_KEY = "mobileconfigurationtimestamp";
    static final String PREFERENCES_FILE = "ZendeskConfigStorage";
    private final Gson gson;
    private final SharedPreferences sharedPreferences;

    ZendeskSdkSettingsStorage(Context context, Gson gson) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, 0);
        this.gson = gson;
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public boolean hasStoredSdkSettings() {
        return this.sharedPreferences.contains(MOBILE_SETTINGS_KEY);
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public boolean areSettingsUpToDate(long j, TimeUnit timeUnit) {
        Long lValueOf = Long.valueOf(this.sharedPreferences.getLong(MOBILE_SETTINGS_TIMESTAMP_KEY, -1L));
        return lValueOf.longValue() != -1 && System.currentTimeMillis() - lValueOf.longValue() < TimeUnit.MILLISECONDS.convert(j, timeUnit);
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public SafeMobileSettings getStoredSettings() {
        MobileSettings mobileSettings = null;
        String string = this.sharedPreferences.getString(MOBILE_SETTINGS_KEY, null);
        if (string != null) {
            String str = LOG_TAG;
            Logger.d(str, "Found stored settings", new Object[0]);
            try {
                MobileSettings mobileSettings2 = (MobileSettings) this.gson.fromJson(string, MobileSettings.class);
                try {
                    Logger.d(str, "Successfully deserialised settings from storage", new Object[0]);
                    mobileSettings = mobileSettings2;
                } catch (JsonSyntaxException unused) {
                    mobileSettings = mobileSettings2;
                    Logger.e(LOG_TAG, "Error deserialising MobileSettings from storage, going to remove stored settings", new Object[0]);
                    deleteStoredSettings();
                }
            } catch (JsonSyntaxException unused2) {
            }
        } else {
            Logger.w(LOG_TAG, "No stored configuration was found, returning null settings", new Object[0]);
        }
        return new SafeMobileSettings(mobileSettings);
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public void setStoredSettings(SafeMobileSettings safeMobileSettings) {
        if (safeMobileSettings == null) {
            Logger.e(LOG_TAG, "Not going to save null MobileSettings, use deleteStoredSettings()", new Object[0]);
            return;
        }
        this.sharedPreferences.edit().putString(MOBILE_SETTINGS_KEY, this.gson.toJson(safeMobileSettings.getMobileSettings())).putLong(MOBILE_SETTINGS_TIMESTAMP_KEY, System.currentTimeMillis()).apply();
        Logger.d(LOG_TAG, "Successfully saved settings to storage", new Object[0]);
    }

    @Override // com.zendesk.sdk.storage.SdkSettingsStorage
    public void deleteStoredSettings() {
        Logger.d(LOG_TAG, "Deleting stored settings", new Object[0]);
        this.sharedPreferences.edit().clear().apply();
    }
}
