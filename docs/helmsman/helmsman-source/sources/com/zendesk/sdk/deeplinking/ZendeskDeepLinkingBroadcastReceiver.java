package com.zendesk.sdk.deeplinking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.deeplinking.targets.DeepLinkType;
import com.zendesk.sdk.deeplinking.targets.DeepLinkingTarget;
import com.zendesk.sdk.deeplinking.targets.DeepLinkingTargetArticle;
import com.zendesk.sdk.deeplinking.targets.DeepLinkingTargetRequest;

/* JADX INFO: loaded from: classes2.dex */
public class ZendeskDeepLinkingBroadcastReceiver extends BroadcastReceiver {
    private static String LOG_TAG = "ZendeskDeepLinkingBroadcastReceiver";

    /* JADX INFO: renamed from: com.zendesk.sdk.deeplinking.ZendeskDeepLinkingBroadcastReceiver$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$zendesk$sdk$deeplinking$targets$DeepLinkType;

        static {
            int[] iArr = new int[DeepLinkType.values().length];
            $SwitchMap$com$zendesk$sdk$deeplinking$targets$DeepLinkType = iArr;
            try {
                iArr[DeepLinkType.Request.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$deeplinking$targets$DeepLinkType[DeepLinkType.Article.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$zendesk$sdk$deeplinking$targets$DeepLinkType[DeepLinkType.Unknown.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int i = AnonymousClass1.$SwitchMap$com$zendesk$sdk$deeplinking$targets$DeepLinkType[DeepLinkingTarget.getDeepLinkType(intent).ordinal()];
        if (i == 1) {
            new DeepLinkingTargetRequest().execute(context, intent);
        } else if (i == 2) {
            new DeepLinkingTargetArticle().execute(context, intent);
        } else {
            if (i != 3) {
                return;
            }
            Logger.e(LOG_TAG, "Unknown intent", new Object[0]);
        }
    }
}
