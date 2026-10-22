package cn.jiguang.analytics.page;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLifecycle";
    private static int activityTaskCount;

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
        cn.jiguang.ad.d.b(TAG, "onActivityCreated:" + activity.getClass().getCanonicalName());
        try {
            if (JConstants.jAnalyticsAction != null) {
                JConstants.jAnalyticsAction.dispatchStatus(activity, "onCreate");
            }
        } catch (Throwable unused) {
            cn.jiguang.ad.d.b(TAG, "onActivityCreated failed");
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        try {
            cn.jiguang.ad.d.d(TAG, "onActivityPaused:" + activity.getClass().getCanonicalName());
            if (JConstants.jAnalyticsAction != null) {
                JConstants.jAnalyticsAction.dispatchPause(activity);
            }
            if (JConstants.isInstrumentationHookFailed) {
                return;
            }
            PushSA.getInstance().onPause(activity);
        } catch (Throwable unused) {
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        try {
            cn.jiguang.ad.d.d(TAG, "onActivityResumed:" + activity.getClass().getCanonicalName());
            if (JConstants.jAnalyticsAction != null) {
                JConstants.jAnalyticsAction.dispatchResume(activity);
            }
            if (JConstants.isInstrumentationHookFailed) {
                return;
            }
            PushSA.getInstance().onResume(activity);
        } catch (Throwable unused) {
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        if (activity != null) {
            cn.jiguang.ad.d.b(TAG, "onActivityStarted:" + activity.getClass().getCanonicalName());
        }
        try {
            if (activityTaskCount == 0) {
                cn.jiguang.ad.d.b(TAG, "isForeground");
                if (activity != null) {
                    JCoreManager.onEvent(activity.getApplicationContext(), JConstants.SDK_TYPE, 66, null, null, true);
                }
                if (JConstants.jAnalyticsAction != null) {
                    JConstants.jAnalyticsAction.dispatchStatus(activity, "onStart");
                }
            }
            activityTaskCount++;
        } catch (Throwable unused) {
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        try {
            cn.jiguang.ad.d.b(TAG, "onActivityStopped:" + activity.getClass().getCanonicalName());
            int i = activityTaskCount;
            if (i > 0) {
                activityTaskCount = i - 1;
            }
            if (activityTaskCount == 0) {
                cn.jiguang.ad.d.b(TAG, "is not Foreground");
                JCoreManager.onEvent(JConstants.getAppContext(activity), JConstants.SDK_TYPE, 66, null, null, false);
            }
        } catch (Throwable unused) {
        }
    }
}
