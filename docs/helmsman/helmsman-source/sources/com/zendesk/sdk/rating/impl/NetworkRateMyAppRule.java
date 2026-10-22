package com.zendesk.sdk.rating.impl;

import android.content.Context;
import com.zendesk.sdk.rating.RateMyAppRule;
import com.zendesk.sdk.util.NetworkUtils;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkRateMyAppRule implements RateMyAppRule {
    private final Context mContext;

    public NetworkRateMyAppRule(Context context) {
        this.mContext = context != null ? context.getApplicationContext() : null;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppRule
    public boolean permitsShowOfDialog() {
        return NetworkUtils.isConnected(this.mContext);
    }
}
