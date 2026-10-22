package com.zendesk.sdk.network.impl;

import com.zendesk.sdk.network.ZendeskTracker;

/* JADX INFO: loaded from: classes2.dex */
class NoOpTracker implements ZendeskTracker {
    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void helpCenterArticleViewed() {
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void helpCenterLoaded() {
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void helpCenterSearched(String str) {
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void rateMyAppFeedbackSent() {
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void rateMyAppRated() {
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void requestCreated() {
    }

    @Override // com.zendesk.sdk.network.ZendeskTracker
    public void requestUpdated() {
    }

    NoOpTracker() {
    }
}
