package com.zendesk.sdk.network.impl;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.SdkConfiguration;
import com.zendesk.sdk.model.access.AccessToken;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.AuthenticationType;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.access.JwtIdentity;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.AccessProvider;
import com.zendesk.sdk.network.BaseProvider;
import com.zendesk.sdk.network.SdkSettingsProvider;
import com.zendesk.sdk.storage.IdentityStorage;
import com.zendesk.sdk.storage.SdkSettingsStorage;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskBaseProvider implements BaseProvider {
    private static final String LOG_TAG = "BaseProvider";
    private static final int SDK_SETTINGS_MAX_AGE_HOURS = 1;
    private final AccessProvider accessProvider;
    private final IdentityStorage identityStorage;
    private final SdkSettingsStorage sdkSettingsStorage;
    private SdkSettingsProvider settingsProvider;

    public ZendeskBaseProvider(AccessProvider accessProvider, SdkSettingsStorage sdkSettingsStorage, IdentityStorage identityStorage, SdkSettingsProvider sdkSettingsProvider) {
        this.accessProvider = accessProvider;
        this.identityStorage = identityStorage;
        this.sdkSettingsStorage = sdkSettingsStorage;
        this.settingsProvider = sdkSettingsProvider;
    }

    @Override // com.zendesk.sdk.network.BaseProvider
    public void configureSdk(final ZendeskCallback<SdkConfiguration> zendeskCallback) {
        getSdkSettings(new ZendeskCallback<SafeMobileSettings>() { // from class: com.zendesk.sdk.network.impl.ZendeskBaseProvider.1
            @Override // com.zendesk.service.ZendeskCallback
            public void onSuccess(final SafeMobileSettings safeMobileSettings) {
                ZendeskBaseProvider.this.getAccessToken(safeMobileSettings, new ZendeskCallback<AccessToken>() { // from class: com.zendesk.sdk.network.impl.ZendeskBaseProvider.1.1
                    @Override // com.zendesk.service.ZendeskCallback
                    public void onSuccess(AccessToken accessToken) {
                        if (zendeskCallback != null) {
                            ZendeskCallback zendeskCallback2 = zendeskCallback;
                            SafeMobileSettings safeMobileSettings2 = safeMobileSettings;
                            if (safeMobileSettings2 == null) {
                                safeMobileSettings2 = new SafeMobileSettings(null);
                            }
                            zendeskCallback2.onSuccess(new SdkConfiguration(accessToken, safeMobileSettings2));
                        }
                    }

                    @Override // com.zendesk.service.ZendeskCallback
                    public void onError(ErrorResponse errorResponse) {
                        if (zendeskCallback != null) {
                            zendeskCallback.onError(errorResponse);
                        }
                    }
                });
            }

            @Override // com.zendesk.service.ZendeskCallback
            public void onError(ErrorResponse errorResponse) {
                ZendeskCallback zendeskCallback2 = zendeskCallback;
                if (zendeskCallback2 != null) {
                    zendeskCallback2.onError(errorResponse);
                }
            }
        });
    }

    @Override // com.zendesk.sdk.network.BaseProvider
    public void getSdkSettings(ZendeskCallback<SafeMobileSettings> zendeskCallback) {
        boolean zHasStoredSdkSettings = this.sdkSettingsStorage.hasStoredSdkSettings();
        boolean zAreSettingsUpToDate = this.sdkSettingsStorage.areSettingsUpToDate(1L, TimeUnit.HOURS);
        if (zHasStoredSdkSettings && zAreSettingsUpToDate) {
            Logger.d(LOG_TAG, "Settings available - skipping download", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onSuccess(this.sdkSettingsStorage.getStoredSettings());
                return;
            }
            return;
        }
        Logger.d(LOG_TAG, "Downloading settings: 'hasStoredSetting': %b | 'areSettingsUpToDate:' %b", Boolean.valueOf(zHasStoredSdkSettings), Boolean.valueOf(zAreSettingsUpToDate));
        this.settingsProvider.getSettings(zendeskCallback);
    }

    @Override // com.zendesk.sdk.network.BaseProvider
    public void getAccessToken(SafeMobileSettings safeMobileSettings, final ZendeskCallback<AccessToken> zendeskCallback) {
        AccessToken storedAccessToken = this.identityStorage.getStoredAccessToken();
        if (storedAccessToken != null) {
            Logger.d(LOG_TAG, "We have a stored access token so we will use that.", new Object[0]);
            if (zendeskCallback != null) {
                zendeskCallback.onSuccess(storedAccessToken);
                return;
            }
        }
        Logger.d(LOG_TAG, "We do not have a stored access token.", new Object[0]);
        Identity identity = this.identityStorage.getIdentity();
        AuthenticationType authenticationType = safeMobileSettings.getAuthenticationType();
        if (AuthenticationType.ANONYMOUS == authenticationType && (identity instanceof AnonymousIdentity)) {
            this.accessProvider.getAndStoreAuthTokenViaAnonymous((AnonymousIdentity) identity, new PassThroughErrorZendeskCallback<AccessToken>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskBaseProvider.2
                @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                public void onSuccess(AccessToken accessToken) {
                    ZendeskCallback zendeskCallback2 = zendeskCallback;
                    if (zendeskCallback2 != null) {
                        zendeskCallback2.onSuccess(accessToken);
                    }
                }
            });
            return;
        }
        if (AuthenticationType.JWT == authenticationType && (identity instanceof JwtIdentity)) {
            this.accessProvider.getAndStoreAuthTokenViaJwt((JwtIdentity) identity, new PassThroughErrorZendeskCallback<AccessToken>(zendeskCallback) { // from class: com.zendesk.sdk.network.impl.ZendeskBaseProvider.3
                @Override // com.zendesk.sdk.network.impl.PassThroughErrorZendeskCallback, com.zendesk.service.ZendeskCallback
                public void onSuccess(AccessToken accessToken) {
                    ZendeskCallback zendeskCallback2 = zendeskCallback;
                    if (zendeskCallback2 != null) {
                        zendeskCallback2.onSuccess(accessToken);
                    }
                }
            });
            return;
        }
        this.sdkSettingsStorage.deleteStoredSettings();
        String logMessage = new AuthenticationLoggerHelper(authenticationType, identity).getLogMessage();
        Logger.e(LOG_TAG, logMessage, new Object[0]);
        if (zendeskCallback != null) {
            zendeskCallback.onError(new ErrorResponseAdapter(logMessage));
        }
    }
}
