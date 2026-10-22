package com.zendesk.sdk.model.access;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.network.impl.ZendeskConfig;

/* JADX INFO: loaded from: classes2.dex */
public class AnonymousIdentity implements Identity {
    private static final String LOG_TAG = "AnonymousIdentity";
    private String email;
    private String externalId;
    private String name;
    private String sdkGuid;

    private AnonymousIdentity() {
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AnonymousIdentity anonymousIdentity = (AnonymousIdentity) obj;
        String str = this.email;
        if (str == null ? anonymousIdentity.email != null : !str.equals(anonymousIdentity.email)) {
            return false;
        }
        String str2 = this.externalId;
        if (str2 == null ? anonymousIdentity.externalId != null : !str2.equals(anonymousIdentity.externalId)) {
            return false;
        }
        String str3 = this.name;
        if (str3 == null ? anonymousIdentity.name != null : !str3.equals(anonymousIdentity.name)) {
            return false;
        }
        String str4 = this.sdkGuid;
        String str5 = anonymousIdentity.sdkGuid;
        return str4 == null ? str5 == null : str4.equals(str5);
    }

    public int hashCode() {
        String str = this.sdkGuid;
        int iHashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.externalId;
        int iHashCode2 = (iHashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.email;
        int iHashCode3 = (iHashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.name;
        return iHashCode3 + (str4 != null ? str4.hashCode() : 0);
    }

    public String getEmail() {
        if (ZendeskConfig.INSTANCE.isCoppaEnabled()) {
            return null;
        }
        return this.email;
    }

    public String getName() {
        if (ZendeskConfig.INSTANCE.isCoppaEnabled()) {
            return null;
        }
        return this.name;
    }

    @Deprecated
    public String getExternalId() {
        Logger.w(LOG_TAG, "withExternalIdentifier is Deprecated in 1.8.0.1. This will be removed in the next release. You will need to use JWT authentication if you want to use external IDs", new Object[0]);
        return this.externalId;
    }

    public String getSdkGuid() {
        return this.sdkGuid;
    }

    public void reloadGuid() {
        this.sdkGuid = ZendeskConfig.INSTANCE.storage().identityStorage().getUUID();
    }

    public static class Builder {
        private static final String LOG_TAG = "AnonymousIdentityBuilder";
        private String email;
        private String externalId;
        private String name;
        private String sdkGuid = ZendeskConfig.INSTANCE.storage().identityStorage().getUUID();

        public Builder withNameIdentifier(String str) {
            this.name = str;
            return this;
        }

        public Builder withEmailIdentifier(String str) {
            this.email = str;
            return this;
        }

        @Deprecated
        public Builder withExternalIdentifier(String str) {
            Logger.w(LOG_TAG, "withExternalIdentifier is Deprecated in 1.8.0.1. This will be removed in the next release. You will need to use JWT authentication if you want to use external IDs", new Object[0]);
            this.externalId = str;
            return this;
        }

        public Identity build() {
            AnonymousIdentity anonymousIdentity = new AnonymousIdentity();
            anonymousIdentity.sdkGuid = this.sdkGuid;
            anonymousIdentity.externalId = this.externalId;
            if (!ZendeskConfig.INSTANCE.isCoppaEnabled()) {
                anonymousIdentity.email = this.email;
                anonymousIdentity.name = this.name;
            } else {
                Logger.w(LOG_TAG, "Ignoring name and / or email because we are in COPPA mode", new Object[0]);
            }
            return anonymousIdentity;
        }
    }
}
