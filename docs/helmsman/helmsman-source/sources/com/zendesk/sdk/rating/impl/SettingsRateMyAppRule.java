package com.zendesk.sdk.rating.impl;

import android.content.Context;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.rating.RateMyAppRule;

/* JADX INFO: loaded from: classes2.dex */
public class SettingsRateMyAppRule implements RateMyAppRule {
    private final Context mContext;

    public SettingsRateMyAppRule(Context context) {
        this.mContext = context != null ? context.getApplicationContext() : null;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppRule
    public boolean permitsShowOfDialog() {
        if (this.mContext == null) {
            return false;
        }
        return ZendeskConfig.INSTANCE.getMobileSettings().isRateMyAppEnabled();
    }
}
