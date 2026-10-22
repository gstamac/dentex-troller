package com.zendesk.sdk.deeplinking.targets;

import android.content.Intent;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class RequestConfiguration extends TargetConfiguration {
    private final String mRequestId;
    private final String mSubject;

    @Override // com.zendesk.sdk.deeplinking.targets.TargetConfiguration
    public /* bridge */ /* synthetic */ ArrayList getBackStackActivities() {
        return super.getBackStackActivities();
    }

    @Override // com.zendesk.sdk.deeplinking.targets.TargetConfiguration
    public /* bridge */ /* synthetic */ DeepLinkType getDeepLinkType() {
        return super.getDeepLinkType();
    }

    @Override // com.zendesk.sdk.deeplinking.targets.TargetConfiguration
    public /* bridge */ /* synthetic */ Intent getFallbackActivity() {
        return super.getFallbackActivity();
    }

    public RequestConfiguration(String str, String str2, ArrayList<Intent> arrayList, Intent intent) {
        super(DeepLinkType.Request, arrayList, intent);
        this.mRequestId = str;
        this.mSubject = str2;
    }

    public String getRequestId() {
        return this.mRequestId;
    }

    public String getSubject() {
        return this.mSubject;
    }
}
