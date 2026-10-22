package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.SettingsHelper;
import com.zendesk.service.ZendeskCallback;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskSettingsHelper implements SettingsHelper {
    private BaseProvider baseProvider;

    public ZendeskSettingsHelper(BaseProvider baseProvider) {
        this.baseProvider = baseProvider;
    }

    @Override // com.zendesk.sdk.network.SettingsHelper
    public void loadSetting(ZendeskCallback<SafeMobileSettings> zendeskCallback) {
        this.baseProvider.getSdkSettings(zendeskCallback);
    }
}
