package com.zendesk.sdk.network;

/* JADX INFO: loaded from: classes2.dex */
public interface ZendeskTracker {
    void helpCenterArticleViewed();

    void helpCenterLoaded();

    void helpCenterSearched(String str);

    void rateMyAppFeedbackSent();

    void rateMyAppRated();

    void requestCreated();

    void requestUpdated();
}
