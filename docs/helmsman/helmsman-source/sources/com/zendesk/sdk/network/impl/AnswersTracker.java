package com.zendesk.sdk.network.impl;

import com.crashlytics.android.answers.shim.AnswersOptionalLogger;
import com.crashlytics.android.answers.shim.KitEvent;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.network.ZendeskTracker;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
class AnswersTracker implements ZendeskTracker {
    private static final String LOG_TAG = "AnswersTracker";

    AnswersTracker() {
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void helpCenterLoaded() {
        Logger.d(LOG_TAG, "helpCenterLoaded", new Object[0]);
        AnswersOptionalLogger.get().logKitEvent(new KitEvent("help-center-fetched"));
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void helpCenterSearched(String str) {
        Logger.d(LOG_TAG, "helpCenterSearched", new Object[0]);
        KitEvent kitEvent = new KitEvent("help-center-search");
        if (StringUtils.isEmpty(str)) {
            str = "";
        }
        kitEvent.putAttribute("search-term", str);
        AnswersOptionalLogger.get().logKitEvent(kitEvent);
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void helpCenterArticleViewed() {
        Logger.d(LOG_TAG, "helpCenterArticleViewed", new Object[0]);
        AnswersOptionalLogger.get().logKitEvent(new KitEvent("help-center-article-viewed"));
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void requestCreated() {
        Logger.d(LOG_TAG, "requestCreated", new Object[0]);
        AnswersOptionalLogger.get().logKitEvent(new KitEvent("request-created"));
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void requestUpdated() {
        Logger.d(LOG_TAG, "requestUpdated", new Object[0]);
        AnswersOptionalLogger.get().logKitEvent(new KitEvent("request-updated"));
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void rateMyAppRated() {
        Logger.d(LOG_TAG, "rateMyAppRated", new Object[0]);
        AnswersOptionalLogger.get().logKitEvent(new KitEvent("rma-rate-app"));
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void rateMyAppFeedbackSent() {
        Logger.d(LOG_TAG, "rateMyAppFeedbackSent", new Object[0]);
        AnswersOptionalLogger.get().logKitEvent(new KitEvent("rma-feedback-sent"));
    }
}
