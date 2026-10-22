package com.zendesk.sdk.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.zendesk.logger.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class AppVersion {
    private static final String LOG_TAG = "AppVersion";
    private int mAppVersionCode;
    private String mAppVersionName;

    public AppVersion(Context context) {
        this.mAppVersionName = "";
        this.mAppVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.mAppVersionName = packageInfo.versionName;
            this.mAppVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(LOG_TAG, "Unable to find the package name", e, new Object[0]);
        }
    }

    public String getAppVersionName() {
        return this.mAppVersionName;
    }

    public int getAppVersionCode() {
        return this.mAppVersionCode;
    }
}
