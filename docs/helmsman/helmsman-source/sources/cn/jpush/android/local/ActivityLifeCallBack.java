package cn.jpush.android.local;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import cn.jpush.android.helper.Logger;

/* JADX INFO: loaded from: classes.dex */
public class ActivityLifeCallBack implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLifeCallBack";
    private static int activityTaskCount;

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        if (activityTaskCount == 0) {
            Logger.d(TAG, "is Foreground");
            if (activity != null) {
                JPushConstants.changeForegroudStat(activity.getApplicationContext(), true);
            }
        }
        activityTaskCount++;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        int i = activityTaskCount;
        if (i > 0) {
            activityTaskCount = i - 1;
        }
        if (activityTaskCount == 0) {
            Logger.d(TAG, "is not Foreground");
            if (activity != null) {
                JPushConstants.changeForegroudStat(activity.getApplicationContext(), false);
            }
        }
    }
}
