package com.zendesk.sdk.network;

import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
public interface SettingsHelper {
    void loadSetting(ZendeskCallback<SafeMobileSettings> zendeskCallback);
}
