package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.network.HelpCenterProvider;
import com.zendesk.sdk.network.NetworkInfoProvider;
import com.zendesk.sdk.network.PushRegistrationProvider;
import com.zendesk.sdk.network.RequestProvider;
import com.zendesk.sdk.network.SdkSettingsProvider;
import com.zendesk.sdk.network.SettingsHelper;
import com.zendesk.sdk.network.UploadProvider;
import com.zendesk.sdk.network.UserProvider;

/* JADX INFO: loaded from: classes2.dex */
public interface ProviderStore {
    HelpCenterProvider helpCenterProvider();

    NetworkInfoProvider networkInfoProvider();

    PushRegistrationProvider pushRegistrationProvider();

    RequestProvider requestProvider();

    SdkSettingsProvider sdkSettingsProvider();

    SettingsHelper uiSettingsHelper();

    UploadProvider uploadProvider();

    UserProvider userProvider();
}
