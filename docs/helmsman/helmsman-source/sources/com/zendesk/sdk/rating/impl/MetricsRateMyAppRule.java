package com.zendesk.sdk.rating.impl;

import android.content.Context;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.rating.RateMyAppRule;
import com.zendesk.sdk.storage.RateMyAppStorage;

/* JADX INFO: loaded from: classes2.dex */
public class MetricsRateMyAppRule implements RateMyAppRule {
    private static final String LOG_TAG = "MetricsRateMyAppRule";
    private final Context mContext;

    public MetricsRateMyAppRule(Context context) {
        this.mContext = context != null ? context.getApplicationContext() : null;
    }

    @Override // com.zendesk.sdk.rating.RateMyAppRule
    public boolean permitsShowOfDialog() {
        if (this.mContext == null) {
            return false;
        }
        RateMyAppStorage rateMyAppStorage = new RateMyAppStorage(this.mContext);
        boolean zIsNumberOfLaunchesMet = rateMyAppStorage.isNumberOfLaunchesMet();
        boolean zIsLaunchTimeMet = rateMyAppStorage.isLaunchTimeMet();
        boolean zIsRatedForCurrentVersion = rateMyAppStorage.isRatedForCurrentVersion();
        boolean zIsDontShowAgain = rateMyAppStorage.isDontShowAgain();
        Logger.d(LOG_TAG, "Number of launches met: " + zIsNumberOfLaunchesMet, new Object[0]);
        Logger.d(LOG_TAG, "Launch time met: " + zIsLaunchTimeMet, new Object[0]);
        Logger.d(LOG_TAG, "Rated for current version: " + zIsRatedForCurrentVersion, new Object[0]);
        Logger.d(LOG_TAG, "Don't show again: " + zIsDontShowAgain, new Object[0]);
        return zIsNumberOfLaunchesMet && zIsLaunchTimeMet && !zIsRatedForCurrentVersion && !zIsDontShowAgain;
    }
}
