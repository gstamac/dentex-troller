package com.zendesk.sdk.network.impl;

import android.content.Context;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.request.CustomField;
import com.zendesk.sdk.network.SdkOptions;
import com.zendesk.sdk.storage.StorageModule;
import com.zendesk.sdk.util.LibraryModule;
import com.zendesk.sdk.util.ScopeCache;
import com.zendesk.sdk.util.StageDetectionUtil;
import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class ApplicationScope {
    private static final String LOG_TAG = "ApplicationScope";
    private final String appId;
    private final Context applicationContext;
    private final boolean coppaEnabled;
    private final List<CustomField> customFields;
    private final boolean developmentMode;
    private ScopeCache<LibraryModule> libraryModuleCache;
    private final Locale locale;
    private final String oAuthToken;
    private ScopeCache<RestAdapterModule> restAdapterCache;
    private final SdkOptions sdkOptions;
    private ScopeCache<StorageModule> storageModuleCache;
    private final Long ticketFormId;
    private final String url;
    private final String userAgentHeader;

    private ApplicationScope(Builder builder) {
        this.libraryModuleCache = new ScopeCache<>();
        this.restAdapterCache = new ScopeCache<>();
        this.storageModuleCache = new ScopeCache<>();
        this.applicationContext = builder.applicationContext;
        this.url = builder.url;
        this.appId = builder.appId;
        this.oAuthToken = builder.oAuthToken;
        this.locale = builder.locale;
        this.coppaEnabled = builder.coppaEnabled;
        this.developmentMode = builder.developmentMode;
        this.sdkOptions = builder.sdkOptions;
        this.ticketFormId = builder.ticketFormId;
        this.customFields = builder.customFields;
        this.userAgentHeader = builder.userAgentHeader;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    public String getUrl() {
        return this.url;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getOAuthToken() {
        return this.oAuthToken;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public boolean isCoppaEnabled() {
        return this.coppaEnabled;
    }

    public boolean isDevelopmentMode() {
        return this.developmentMode;
    }

    public SdkOptions getSdkOptions() {
        return this.sdkOptions;
    }

    public Long getTicketFormId() {
        return this.ticketFormId;
    }

    public List<CustomField> getCustomFields() {
        return CollectionUtils.copyOf(this.customFields);
    }

    public String getUserAgentHeader() {
        return this.userAgentHeader;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public ScopeCache<LibraryModule> getLibraryModuleCache() {
        return this.libraryModuleCache;
    }

    public ScopeCache<RestAdapterModule> getRestAdapterCache() {
        return this.restAdapterCache;
    }

    public ScopeCache<StorageModule> getStorageModuleCache() {
        return this.storageModuleCache;
    }

    static class Builder {
        private final String appId;
        private final Context applicationContext;
        private boolean coppaEnabled;
        private List<CustomField> customFields;
        private final boolean developmentMode;
        private Locale locale;
        private final String oAuthToken;
        private SdkOptions sdkOptions;
        private Long ticketFormId;
        private final String url;
        private String userAgentHeader;

        Builder(Context context, String str, String str2, String str3) {
            boolean zIsDebug;
            this.applicationContext = context;
            this.url = str;
            this.appId = str2;
            this.oAuthToken = str3;
            try {
                zIsDebug = StageDetectionUtil.isDebug(context);
            } catch (Exception unused) {
                Logger.d(ApplicationScope.LOG_TAG, "Unable to detect stage", new Object[0]);
                zIsDebug = true;
            }
            this.developmentMode = zIsDebug;
            this.locale = Locale.getDefault();
            this.coppaEnabled = false;
            this.sdkOptions = new DefaultSdkOptions();
            this.ticketFormId = null;
            this.customFields = new ArrayList();
            this.userAgentHeader = "";
        }

        Builder(ApplicationScope applicationScope) {
            this.applicationContext = applicationScope.getApplicationContext();
            this.url = applicationScope.getUrl();
            this.appId = applicationScope.getAppId();
            this.oAuthToken = applicationScope.getOAuthToken();
            this.developmentMode = applicationScope.isDevelopmentMode();
            this.locale = applicationScope.getLocale();
            this.coppaEnabled = applicationScope.coppaEnabled;
            this.sdkOptions = applicationScope.getSdkOptions();
            this.ticketFormId = applicationScope.getTicketFormId();
            this.customFields = applicationScope.getCustomFields();
            this.userAgentHeader = applicationScope.getUserAgentHeader();
        }

        Builder ticketFormId(Long l) {
            this.ticketFormId = l;
            return this;
        }

        Builder customFields(List<CustomField> list) {
            this.customFields = CollectionUtils.ensureEmpty(list);
            return this;
        }

        Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        Builder coppa(boolean z) {
            this.coppaEnabled = z;
            return this;
        }

        Builder sdkOptions(SdkOptions sdkOptions) {
            this.sdkOptions = sdkOptions;
            return this;
        }

        Builder userAgentHeader(String str) {
            this.userAgentHeader = str;
            return this;
        }

        ApplicationScope build() {
            return new ApplicationScope(this);
        }
    }
}
