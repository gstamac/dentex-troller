package com.zendesk.sdk.rating.impl;

import android.content.Context;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.access.JwtIdentity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.rating.RateMyAppRule;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
public class IdentityRateMyAppRule implements RateMyAppRule {
    private static final String LOG_TAG = "IdentityRateMyAppRule";
    private final Context mContext;

    public IdentityRateMyAppRule(Context context) {
        this.mContext = context != null ? context.getApplicationContext() : null;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppRule
    public boolean permitsShowOfDialog() {
        if (this.mContext == null) {
            return false;
        }
        Identity identity = ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity();
        boolean zIsConversationsEnabled = ZendeskConfig.INSTANCE.getMobileSettings().isConversationsEnabled();
        boolean z = identity instanceof JwtIdentity;
        boolean z2 = identity instanceof AnonymousIdentity;
        boolean z3 = z2 && StringUtils.hasLength(((AnonymousIdentity) identity).getEmail());
        boolean z4 = z2 && zIsConversationsEnabled;
        boolean z5 = z || z4 || z3;
        Logger.d(LOG_TAG, "Is Jwt: %s, is anonymous with conversations enabled: %s, is anonymous with email: %s", Boolean.valueOf(z), Boolean.valueOf(z4), Boolean.valueOf(z3));
        return z5;
    }
}
