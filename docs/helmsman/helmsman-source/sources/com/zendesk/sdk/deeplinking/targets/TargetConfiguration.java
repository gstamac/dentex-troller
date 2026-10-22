package com.zendesk.sdk.deeplinking.targets;

import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes2.dex */
public class TargetConfiguration {
    private final List<Intent> mBackStackActivities;
    private final DeepLinkType mDeepLinkType;
    private final Intent mFallbackActivity;

    TargetConfiguration(DeepLinkType deepLinkType, List<Intent> list, Intent intent) {
        this.mDeepLinkType = deepLinkType;
        this.mBackStackActivities = list;
        this.mFallbackActivity = intent;
    }

    public DeepLinkType getDeepLinkType() {
        return this.mDeepLinkType;
    }

    public ArrayList<Intent> getBackStackActivities() {
        if (this.mBackStackActivities != null) {
            return new ArrayList<>(this.mBackStackActivities);
        }
        return new ArrayList<>();
    }

    public Intent getFallbackActivity() {
        return this.mFallbackActivity;
    }
}
