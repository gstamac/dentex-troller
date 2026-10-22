package com.zendesk.sdk.deeplinking.targets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.deeplinking.ZendeskDeepLinkingBroadcastReceiver;
import com.zendesk.sdk.deeplinking.targets.TargetConfiguration;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.access.JwtIdentity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DeepLinkingTarget<E extends TargetConfiguration> {
    private static final String EXTRA_APP_ID = "extra_appid";
    private static final String EXTRA_AUTH_BUNDLE = "extra_auth_bundle";
    private static final String EXTRA_BACK_STACK_ACTIVITIES = "extra_follow_up_activities";
    private static final String EXTRA_CLIENT_ID = "extra_client_id";
    private static final String EXTRA_DEEP_LINK_TYPE = "extra_deeplink";
    private static final String EXTRA_EXTRA_BUNDLE = "extra_extra_bundle";
    private static final String EXTRA_FALLBACK_ACTIVITY = "extra_fallback_activity";
    private static final String EXTRA_LOGGABLE = "extra_loggable";
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_USER_TOKEN = "extra_user_token";
    private static final String LOG_TAG = "DeepLinkingTarget";

    abstract E extractConfiguration(Bundle bundle, TargetConfiguration targetConfiguration);

    abstract Bundle getExtra(E e);

    abstract void openTargetActivity(Context context, E e);

    public Intent getIntent(Context context, E e) {
        if (ZendeskConfig.INSTANCE.isInitialized()) {
            return getIntent(context, e, ZendeskConfig.INSTANCE.getZendeskUrl(), ZendeskConfig.INSTANCE.getApplicationId(), ZendeskConfig.INSTANCE.getOauthClientId());
        }
        return null;
    }

    public Intent getIntent(Context context, E e, String str, String str2, String str3) {
        Intent intent = new Intent(context, (Class<?>) ZendeskDeepLinkingBroadcastReceiver.class);
        String userToken = getUserToken();
        if (!StringUtils.hasLength(userToken)) {
            Logger.e(LOG_TAG, "No user configuration found", new Object[0]);
            return null;
        }
        intent.putExtra(EXTRA_LOGGABLE, Logger.isLoggable());
        intent.putExtra(EXTRA_AUTH_BUNDLE, getAuthBundle(str, str2, str3, userToken));
        intent.putExtra(EXTRA_DEEP_LINK_TYPE, e.getDeepLinkType());
        if (e.getFallbackActivity() != null) {
            checkIntent(e.getFallbackActivity());
            intent.putExtra(EXTRA_FALLBACK_ACTIVITY, e.getFallbackActivity());
        }
        if (e.getBackStackActivities() != null) {
            Iterator<Intent> it = e.getBackStackActivities().iterator();
            while (it.hasNext()) {
                checkIntent(it.next());
            }
            intent.putParcelableArrayListExtra(EXTRA_BACK_STACK_ACTIVITIES, e.getBackStackActivities());
        }
        intent.putExtra(EXTRA_EXTRA_BUNDLE, getExtra(e));
        return intent;
    }

    public boolean execute(Context context, Intent intent) {
        Logger.setLoggable(intent.getBooleanExtra(EXTRA_LOGGABLE, false));
        Intent intent2 = (intent.hasExtra(EXTRA_FALLBACK_ACTIVITY) && (intent.getParcelableExtra(EXTRA_FALLBACK_ACTIVITY) instanceof Intent)) ? (Intent) intent.getParcelableExtra(EXTRA_FALLBACK_ACTIVITY) : null;
        if (!applyAuthBundle(context, intent.getBundleExtra(EXTRA_AUTH_BUNDLE))) {
            Logger.e(LOG_TAG, "No valid auth bundle found", new Object[0]);
            fireFallBackActivity(context, intent2);
            return false;
        }
        if (!intent.hasExtra(EXTRA_DEEP_LINK_TYPE) || !(intent.getSerializableExtra(EXTRA_DEEP_LINK_TYPE) instanceof DeepLinkType)) {
            Logger.e(LOG_TAG, "No valid deep link type found", new Object[0]);
            fireFallBackActivity(context, intent2);
            return false;
        }
        DeepLinkType deepLinkType = (DeepLinkType) intent.getSerializableExtra(EXTRA_DEEP_LINK_TYPE);
        ArrayList arrayList = new ArrayList();
        if (intent.hasExtra(EXTRA_BACK_STACK_ACTIVITIES)) {
            arrayList = intent.getParcelableArrayListExtra(EXTRA_BACK_STACK_ACTIVITIES);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                checkIntent((Intent) it.next());
            }
        }
        if (!intent.hasExtra(EXTRA_EXTRA_BUNDLE) || intent.getBundleExtra(EXTRA_EXTRA_BUNDLE) == null) {
            Logger.e(LOG_TAG, "No activity configuration found", new Object[0]);
            fireFallBackActivity(context, intent2);
            return false;
        }
        TargetConfiguration targetConfigurationExtractConfiguration = extractConfiguration(intent.getBundleExtra(EXTRA_EXTRA_BUNDLE), new TargetConfiguration(deepLinkType, arrayList, intent2));
        if (targetConfigurationExtractConfiguration == null) {
            fireFallBackActivity(context, intent2);
            return false;
        }
        openTargetActivity(context, targetConfigurationExtractConfiguration);
        return true;
    }

    protected static TaskStackBuilder getTaskStackBuilder(Context context, List<Intent> list) {
        TaskStackBuilder taskStackBuilderCreate = TaskStackBuilder.create(context);
        Iterator<Intent> it = list.iterator();
        while (it.hasNext()) {
            taskStackBuilderCreate.addNextIntentWithParentStack(it.next());
        }
        return taskStackBuilderCreate;
    }

    private Bundle getAuthBundle(String str, String str2, String str3, String str4) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, str);
        bundle.putString(EXTRA_APP_ID, str2);
        bundle.putString(EXTRA_CLIENT_ID, str3);
        bundle.putString(EXTRA_USER_TOKEN, str4);
        return bundle;
    }

    private boolean applyAuthBundle(Context context, Bundle bundle) {
        if (bundle == null || !hasBundleValidStrings(bundle, EXTRA_CLIENT_ID, EXTRA_APP_ID, EXTRA_URL, EXTRA_USER_TOKEN)) {
            Logger.e(LOG_TAG, "Auth bundle content is invalid", new Object[0]);
            return false;
        }
        ZendeskConfig.INSTANCE.init(context, bundle.getString(EXTRA_URL), bundle.getString(EXTRA_APP_ID), bundle.getString(EXTRA_CLIENT_ID));
        String string = bundle.getString(EXTRA_USER_TOKEN);
        if (string == null || string.equals(getUserToken())) {
            return true;
        }
        Logger.e(LOG_TAG, "User configuration has changed", new Object[0]);
        return false;
    }

    private boolean hasBundleValidStrings(Bundle bundle, String... strArr) {
        boolean z = true;
        for (String str : strArr) {
            if (!bundle.containsKey(str) || !StringUtils.hasLength(bundle.getString(str))) {
                z = false;
            }
        }
        return z;
    }

    private String getUserToken() {
        Identity identity = ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity();
        if (identity != null && (identity instanceof AnonymousIdentity)) {
            return ((AnonymousIdentity) identity).getSdkGuid();
        }
        if (identity == null || !(identity instanceof JwtIdentity)) {
            return null;
        }
        return ((JwtIdentity) identity).getJwtUserIdentifier();
    }

    private void fireFallBackActivity(Context context, Intent intent) {
        if (intent != null) {
            checkIntent(intent);
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }

    private void checkIntent(Intent intent) {
        if (intent.getComponent() == null) {
            throw new IllegalArgumentException("Illegal Intent provided. Make sure that the provided Intent has a ComponentName. To achieve this you should for example this constructor: ``Intent(Context packageContext, Class<?> cls)``");
        }
    }

    public static DeepLinkType getDeepLinkType(Intent intent) {
        if (!intent.hasExtra(EXTRA_DEEP_LINK_TYPE) || !(intent.getSerializableExtra(EXTRA_DEEP_LINK_TYPE) instanceof DeepLinkType)) {
            return DeepLinkType.Unknown;
        }
        return (DeepLinkType) intent.getSerializableExtra(EXTRA_DEEP_LINK_TYPE);
    }
}
