package com.zendesk.sdk.model.settings;

import com.zendesk.sdk.model.access.AuthenticationType;
import com.zendesk.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class SafeMobileSettings {
    private final MobileSettings mobileSettings;

    public SafeMobileSettings(MobileSettings mobileSettings) {
        this.mobileSettings = mobileSettings;
    }

    public MobileSettings getMobileSettings() {
        return this.mobileSettings;
    }

    public boolean isConversationsEnabled() {
        ConversationsSettings conversationsSettings = getConversationsSettings();
        return conversationsSettings != null && conversationsSettings.isEnabled();
    }

    public boolean isAttachmentsEnabled() {
        AttachmentSettings attachmentSettings = getAttachmentSettings();
        return attachmentSettings != null && attachmentSettings.isEnabled();
    }

    public long getMaxAttachmentSize() {
        AttachmentSettings attachmentSettings = getAttachmentSettings();
        if (attachmentSettings != null) {
            return attachmentSettings.getMaxAttachmentSize();
        }
        return 0L;
    }

    public boolean hasHelpCenterSettings() {
        return getHelpCenterSettings() != null;
    }

    public boolean isHelpCenterEnabled() {
        HelpCenterSettings helpCenterSettings = getHelpCenterSettings();
        return helpCenterSettings != null && helpCenterSettings.isEnabled();
    }

    public String getHelpCenterLocale() {
        HelpCenterSettings helpCenterSettings = getHelpCenterSettings();
        return (helpCenterSettings == null || helpCenterSettings.getLocale() == null) ? "" : helpCenterSettings.getLocale();
    }

    public boolean isRateMyAppEnabled() {
        RateMyAppSettings rateMyAppSettings = getRateMyAppSettings();
        return rateMyAppSettings != null && rateMyAppSettings.isEnabled();
    }

    public String getRateMyAppStoreUrl() {
        RateMyAppSettings rateMyAppSettings = getRateMyAppSettings();
        return (rateMyAppSettings == null || rateMyAppSettings.getAndroidStoreUrl() == null) ? "" : rateMyAppSettings.getAndroidStoreUrl();
    }

    public long getRateMyAppDelay() {
        RateMyAppSettings rateMyAppSettings = getRateMyAppSettings();
        if (rateMyAppSettings != null) {
            return rateMyAppSettings.getDelay();
        }
        return 0L;
    }

    public long getRateMyAppDuration() {
        RateMyAppSettings rateMyAppSettings = getRateMyAppSettings();
        if (rateMyAppSettings != null) {
            return rateMyAppSettings.getDuration();
        }
        return 0L;
    }

    public long getRateMyAppVisits() {
        RateMyAppSettings rateMyAppSettings = getRateMyAppSettings();
        if (rateMyAppSettings != null) {
            return rateMyAppSettings.getVisits();
        }
        return 0L;
    }

    public List<String> getRateMyAppTags() {
        RateMyAppSettings rateMyAppSettings = getRateMyAppSettings();
        return (rateMyAppSettings == null || !CollectionUtils.isNotEmpty(rateMyAppSettings.getTags())) ? new ArrayList() : rateMyAppSettings.getTags();
    }

    public AuthenticationType getAuthenticationType() {
        return getAuthenticationTypeSetting();
    }

    private AttachmentSettings getAttachmentSettings() {
        MobileSettings mobileSettings = this.mobileSettings;
        if ((mobileSettings == null || mobileSettings.getAccountSettings() == null || this.mobileSettings.getAccountSettings().getAttachmentSettings() == null) ? false : true) {
            return this.mobileSettings.getAccountSettings().getAttachmentSettings();
        }
        return null;
    }

    private ConversationsSettings getConversationsSettings() {
        MobileSettings mobileSettings = this.mobileSettings;
        if ((mobileSettings == null || mobileSettings.getSdkSettings() == null || this.mobileSettings.getSdkSettings().getConversationsSettings() == null) ? false : true) {
            return this.mobileSettings.getSdkSettings().getConversationsSettings();
        }
        return null;
    }

    public HelpCenterSettings getHelpCenterSettings() {
        MobileSettings mobileSettings = this.mobileSettings;
        if ((mobileSettings == null || mobileSettings.getSdkSettings() == null || this.mobileSettings.getSdkSettings().getHelpCenterSettings() == null) ? false : true) {
            return this.mobileSettings.getSdkSettings().getHelpCenterSettings();
        }
        return null;
    }

    public List<String> getContactZendeskTags() {
        MobileSettings mobileSettings = this.mobileSettings;
        return mobileSettings != null && mobileSettings.getSdkSettings() != null && this.mobileSettings.getSdkSettings().getContactUsSettings() != null && CollectionUtils.isNotEmpty(this.mobileSettings.getSdkSettings().getContactUsSettings().getTags()) ? this.mobileSettings.getSdkSettings().getContactUsSettings().getTags() : new ArrayList();
    }

    private RateMyAppSettings getRateMyAppSettings() {
        MobileSettings mobileSettings = this.mobileSettings;
        if ((mobileSettings == null || mobileSettings.getSdkSettings() == null || this.mobileSettings.getSdkSettings().getRateMyAppSettings() == null) ? false : true) {
            return this.mobileSettings.getSdkSettings().getRateMyAppSettings();
        }
        return null;
    }

    private AuthenticationType getAuthenticationTypeSetting() {
        MobileSettings mobileSettings = this.mobileSettings;
        if ((mobileSettings == null || mobileSettings.getSdkSettings() == null || this.mobileSettings.getSdkSettings().getAuthentication() == null) ? false : true) {
            return this.mobileSettings.getSdkSettings().getAuthentication();
        }
        return null;
    }

    public boolean isTicketFormSupportAvailable() {
        MobileSettings mobileSettings = this.mobileSettings;
        return (mobileSettings != null && mobileSettings.getSdkSettings() != null && this.mobileSettings.getSdkSettings().getTicketFormSettings() != null) && this.mobileSettings.getSdkSettings().getTicketFormSettings().isAvailable();
    }
}
