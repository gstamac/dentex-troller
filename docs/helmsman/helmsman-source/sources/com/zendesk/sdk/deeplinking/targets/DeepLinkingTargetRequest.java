package com.zendesk.sdk.deeplinking.targets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;
import com.zendesk.sdk.deeplinking.ZendeskDeepLinking;
import com.zendesk.sdk.requests.RequestActivity;
import com.zendesk.sdk.requests.ViewRequestActivity;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
public class DeepLinkingTargetRequest extends DeepLinkingTarget<RequestConfiguration> {
    private static String EXTRA_REQUEST_ID = "extra_request_id";
    private static String EXTRA_REQUEST_SUBJECT = "extra_request_subject";

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.zendesk.sdk.deeplinking.targets.DeepLinkingTarget
    public void openTargetActivity(Context context, RequestConfiguration requestConfiguration) {
        if (ZendeskDeepLinking.INSTANCE.refreshComments(requestConfiguration.getRequestId())) {
            return;
        }
        Intent intent = new Intent(context, (Class<?>) ViewRequestActivity.class);
        intent.putExtra("requestId", requestConfiguration.getRequestId());
        if (StringUtils.hasLength(requestConfiguration.getSubject())) {
            intent.putExtra("subject", requestConfiguration.getSubject());
        }
        TaskStackBuilder taskStackBuilder = getTaskStackBuilder(context, requestConfiguration.getBackStackActivities());
        taskStackBuilder.addNextIntentWithParentStack(new Intent(context, (Class<?>) RequestActivity.class));
        taskStackBuilder.addNextIntentWithParentStack(intent);
        taskStackBuilder.startActivities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.zendesk.sdk.deeplinking.targets.DeepLinkingTarget
    public RequestConfiguration extractConfiguration(Bundle bundle, TargetConfiguration targetConfiguration) {
        if (bundle.containsKey(EXTRA_REQUEST_ID)) {
            return new RequestConfiguration(bundle.getString(EXTRA_REQUEST_ID), bundle.getString(EXTRA_REQUEST_SUBJECT), targetConfiguration.getBackStackActivities(), targetConfiguration.getFallbackActivity());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.zendesk.sdk.deeplinking.targets.DeepLinkingTarget
    public Bundle getExtra(RequestConfiguration requestConfiguration) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_REQUEST_ID, requestConfiguration.getRequestId());
        if (StringUtils.hasLength(requestConfiguration.getSubject())) {
            bundle.putString(EXTRA_REQUEST_SUBJECT, requestConfiguration.getSubject());
        }
        return bundle;
    }
}
