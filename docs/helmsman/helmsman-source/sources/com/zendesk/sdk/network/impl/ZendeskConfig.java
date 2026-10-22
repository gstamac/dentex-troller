package com.zendesk.sdk.network.impl;

import android.content.Context;
import android.os.Build;
import android.util.Pair;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.push.PushRegistrationResponse;
import com.zendesk.sdk.model.request.CustomField;
import com.zendesk.sdk.model.settings.MobileSettings;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.Constants;
import com.zendesk.sdk.network.SdkOptions;
import com.zendesk.sdk.network.ZendeskTracker;
import com.zendesk.sdk.storage.IdentityStorage;
import com.zendesk.sdk.storage.SdkStorage;
import com.zendesk.sdk.storage.StorageStore;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.CollectionUtils;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public enum ZendeskConfig {
    INSTANCE;

    public static final int HEADER_SUFFIX_MAX_LENGTH = 1024;
    private static final String LOG_TAG = "ZendeskConfiguration";
    private static final String SLASH = "/";
    private ApplicationScope applicationScope;
    private boolean initialised;
    private ZendeskTracker tracker;
    private List<Pair<String, String>> uaHeaderSuffixes;
    private ZendeskConfigHelper zendeskConfigHelper;

    ZendeskConfig() {
        initWithStubs();
    }

    public void init(Context context, String str, String str2, String str3) {
        this.uaHeaderSuffixes = null;
        if (this.initialised) {
            Logger.i(LOG_TAG, "ZendeskConfig is already initialised, skipping reinitialisation.", new Object[0]);
            initApplicationScope(this.applicationScope.newBuilder().userAgentHeader(buildUserAgentHeader()).build());
            return;
        }
        if (context != null && StringUtils.hasLength(str) && StringUtils.hasLength(str2) && StringUtils.hasLength(str3)) {
            installTracker(new AnswersTracker());
            initApplicationScope(new ApplicationScope.Builder(context.getApplicationContext(), str, str2, str3).userAgentHeader(buildUserAgentHeader()).build());
            this.initialised = true;
            return;
        }
        ConfigurationRuntimeException configurationRuntimeException = new ConfigurationRuntimeException("Could not validate minimal configuration");
        Object[] objArr = new Object[4];
        objArr[0] = Boolean.valueOf(context != null);
        objArr[1] = Boolean.valueOf(StringUtils.hasLength(str));
        objArr[2] = Boolean.valueOf(StringUtils.hasLength(str2));
        objArr[3] = Boolean.valueOf(StringUtils.hasLength(str3));
        Logger.e(LOG_TAG, "Invalid configuration provided | Context: %b | Url: %b | Application Id: %b | OauthClientId: %b", configurationRuntimeException, objArr);
    }

    public boolean isInitialized() {
        return this.initialised;
    }

    public String getZendeskUrl() {
        return this.applicationScope.getUrl();
    }

    public String getOauthClientId() {
        return this.applicationScope.getOAuthToken();
    }

    public SafeMobileSettings getMobileSettings() {
        SafeMobileSettings storedSettings = this.zendeskConfigHelper.getStorageStore().sdkSettingsStorage().getStoredSettings();
        return storedSettings == null ? new SafeMobileSettings(new MobileSettings()) : storedSettings;
    }

    public boolean isAuthenticationAvailable() {
        return this.zendeskConfigHelper.getStorageStore().identityStorage().getIdentity() != null;
    }

    public String getApplicationId() {
        return this.applicationScope.getAppId();
    }

    public void setIdentity(Identity identity) {
        IdentityStorage identityStorage = this.zendeskConfigHelper.getStorageStore().identityStorage();
        Identity identity2 = this.zendeskConfigHelper.getStorageStore().identityStorage().getIdentity();
        if (identity2 == null) {
            Logger.d(LOG_TAG, "No previous identity set, storing identity", new Object[0]);
            identityStorage.storeIdentity(identity);
            initApplicationScope(this.applicationScope.newBuilder().build());
            return;
        }
        boolean z = identity == null;
        boolean z2 = !identity2.equals(identity);
        if (z || z2) {
            Logger.d(LOG_TAG, String.format(Locale.US, "Clearing user data. End user cleared: %s, end user changed: %s", Boolean.valueOf(z), Boolean.valueOf(z2)), new Object[0]);
            this.zendeskConfigHelper.getStorageStore().sdkStorage().clearUserData();
            if (identity != null) {
                Logger.d(LOG_TAG, "Identity has changed, storing new identity", new Object[0]);
                if (identity instanceof AnonymousIdentity) {
                    ((AnonymousIdentity) identity).reloadGuid();
                }
                identityStorage.storeIdentity(identity);
                initApplicationScope(this.applicationScope.newBuilder().build());
            }
        }
    }

    public void setCustomFields(List<CustomField> list) {
        initApplicationScope(this.applicationScope.newBuilder().customFields(list).build());
    }

    public List<CustomField> getCustomFields() {
        return this.applicationScope.getCustomFields();
    }

    public void setTicketFormId(Long l) {
        initApplicationScope(this.applicationScope.newBuilder().ticketFormId(l).build());
    }

    public Long getTicketFormId() {
        return this.applicationScope.getTicketFormId();
    }

    public void setDeviceLocale(Locale locale) {
        if (this.initialised) {
            initApplicationScope(this.applicationScope.newBuilder().locale(locale).build());
            Logger.d(LOG_TAG, "Locale has been overridden, requesting new settings.", new Object[0]);
            this.zendeskConfigHelper.getProviderStore().sdkSettingsProvider().getSettings(null);
            return;
        }
        Logger.w(LOG_TAG, "Locale cannot be set before SDK has been initialised. init() must be called before setDeviceLocale().", new Object[0]);
    }

    Locale getDeviceLocale() {
        return this.applicationScope.getLocale();
    }

    public void setCoppaEnabled(boolean z) {
        if (this.initialised && z) {
            this.zendeskConfigHelper.getStorageStore().identityStorage().anonymiseIdentity();
        }
        initApplicationScope(this.applicationScope.newBuilder().coppa(z).build());
    }

    public boolean isCoppaEnabled() {
        return this.applicationScope.isCoppaEnabled();
    }

    public void enablePushWithIdentifier(String str, ZendeskCallback<PushRegistrationResponse> zendeskCallback) {
        if (this.initialised) {
            this.zendeskConfigHelper.getProviderStore().pushRegistrationProvider().registerDeviceWithIdentifier(str, getDeviceLocale(), zendeskCallback);
        } else if (zendeskCallback != null) {
            zendeskCallback.onError(new ErrorResponseAdapter("Zendesk not initialized, skipping enablePush()"));
        }
    }

    public void enablePushWithUAChannelId(String str, ZendeskCallback<PushRegistrationResponse> zendeskCallback) {
        if (this.initialised) {
            this.zendeskConfigHelper.getProviderStore().pushRegistrationProvider().registerDeviceWithUAChannelId(str, getDeviceLocale(), zendeskCallback);
        } else if (zendeskCallback != null) {
            zendeskCallback.onError(new ErrorResponseAdapter("Zendesk not initialized, skipping enablePush()"));
        }
    }

    public void disablePush(String str, ZendeskCallback<Void> zendeskCallback) {
        if (this.initialised) {
            this.zendeskConfigHelper.getProviderStore().pushRegistrationProvider().unregisterDevice(str, zendeskCallback);
        } else if (zendeskCallback != null) {
            zendeskCallback.onError(new ErrorResponseAdapter("Zendesk not initialized, skipping disablePush()"));
        }
    }

    public SdkOptions getSdkOptions() {
        return this.applicationScope.getSdkOptions();
    }

    public void setSdkOptions(SdkOptions sdkOptions) {
        initApplicationScope(this.applicationScope.newBuilder().sdkOptions(sdkOptions).build());
    }

    boolean addUserAgentHeaderSuffix(Pair<String, String> pair) {
        if (pair == null || pair.first == null || pair.second == null) {
            Logger.e(LOG_TAG, "User agent header suffix must be a valid non-null name-value pair. Suffix not added.", new Object[0]);
            return false;
        }
        if (suffixContainsInvalidCharacter(pair)) {
            Logger.e(LOG_TAG, "Neither element of the suffix Pair can contain a slash or a new line character. Suffix not added.", new Object[0]);
            return false;
        }
        if (suffixWouldMakeHeaderTooLong(pair)) {
            Logger.e(LOG_TAG, "Header must be less than 1024 characters. Suffix not added.", new Object[0]);
            return false;
        }
        if (this.uaHeaderSuffixes == null) {
            this.uaHeaderSuffixes = new ArrayList(1);
        }
        this.uaHeaderSuffixes.add(pair);
        initApplicationScope(this.applicationScope.newBuilder().userAgentHeader(buildUserAgentHeader()).build());
        return true;
    }

    private boolean suffixContainsInvalidCharacter(Pair<String, String> pair) {
        return ((String) pair.first).contains(SLASH) || ((String) pair.second).contains(SLASH) || ((String) pair.first).contains(StringUtils.LINE_SEPARATOR) || ((String) pair.second).contains(StringUtils.LINE_SEPARATOR);
    }

    private boolean suffixWouldMakeHeaderTooLong(Pair<String, String> pair) {
        String str = String.format(Constants.HEADER_SUFFIX_FORMAT, pair.first, pair.second);
        return str.length() >= 1024 || str.length() + (getUserAgentHeader() == null ? 0 : getUserAgentHeader().length()) >= 1024;
    }

    void clearUserAgentHeaderSuffixes() {
        List<Pair<String, String>> list = this.uaHeaderSuffixes;
        if (list != null) {
            list.clear();
        }
        initApplicationScope(this.applicationScope.newBuilder().userAgentHeader(buildUserAgentHeader()).build());
    }

    String getUserAgentHeader() {
        return this.applicationScope.getUserAgentHeader();
    }

    private String buildUserAgentHeader() {
        StringBuilder sb = new StringBuilder(String.format(Locale.US, Constants.ZENDESK_SDK_FOR_ANDROID, "1.8.0.1", Integer.valueOf(Build.VERSION.SDK_INT), INSTANCE.isDevelopment() ? Constants.ENVIRONMENT_DEBUG : Constants.ENVIRONMENT_PRODUCTION));
        if (CollectionUtils.isNotEmpty(this.uaHeaderSuffixes)) {
            for (Pair<String, String> pair : this.uaHeaderSuffixes) {
                sb.append(String.format(Locale.US, Constants.HEADER_SUFFIX_FORMAT, pair.first, pair.second));
            }
        }
        return sb.toString();
    }

    boolean isDevelopment() {
        return this.applicationScope.isDevelopmentMode();
    }

    public ProviderStore provider() {
        return this.zendeskConfigHelper.getProviderStore();
    }

    public StorageStore storage() {
        return this.zendeskConfigHelper.getStorageStore();
    }

    private void initApplicationScope(ApplicationScope applicationScope) {
        this.applicationScope = applicationScope;
        ZendeskConfigHelper zendeskConfigHelperInjectZendeskConfigHelper = ZendeskConfigInjector.injectZendeskConfigHelper(applicationScope);
        this.zendeskConfigHelper = zendeskConfigHelperInjectZendeskConfigHelper;
        SdkStorage sdkStorage = zendeskConfigHelperInjectZendeskConfigHelper.getStorageStore().sdkStorage();
        sdkStorage.registerUserStorage(this.zendeskConfigHelper.getStorageStore().identityStorage());
        sdkStorage.registerUserStorage(this.zendeskConfigHelper.getStorageStore().requestStorage());
    }

    private void initWithStubs() {
        ApplicationScope applicationScopeBuild = new ApplicationScope.Builder(null, "", "", "").build();
        this.applicationScope = applicationScopeBuild;
        this.zendeskConfigHelper = ZendeskConfigInjector.injectStubZendeskConfigHelper(applicationScopeBuild);
        this.initialised = false;
    }

    void reset(Context context) {
        Logger.d(LOG_TAG, "Tearing down ZendeskConfig", new Object[0]);
        this.uaHeaderSuffixes = null;
        storage().sdkStorage().clearUserData();
        storage().sdkSettingsStorage().deleteStoredSettings();
        initWithStubs();
    }

    void installTracker(ZendeskTracker zendeskTracker) {
        this.tracker = zendeskTracker;
    }

    public ZendeskTracker getTracker() {
        ZendeskTracker zendeskTracker = this.tracker;
        return zendeskTracker == null ? new NoOpTracker() : zendeskTracker;
    }
}
