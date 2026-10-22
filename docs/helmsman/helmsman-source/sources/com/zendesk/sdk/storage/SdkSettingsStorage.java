package com.zendesk.sdk.storage;

import com.zendesk.sdk.model.settings.SafeMobileSettings;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public interface SdkSettingsStorage {
    boolean areSettingsUpToDate(long j, TimeUnit timeUnit);

    void deleteStoredSettings();

    SafeMobileSettings getStoredSettings();

    boolean hasStoredSdkSettings();

    void setStoredSettings(SafeMobileSettings safeMobileSettings);
}
