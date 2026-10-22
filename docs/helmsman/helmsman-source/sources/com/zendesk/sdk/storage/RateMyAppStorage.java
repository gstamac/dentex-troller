package com.zendesk.sdk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.model.AppVersion;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.impl.ZendeskConfig;

/* JADX INFO: loaded from: classes2.dex */
public class RateMyAppStorage implements SdkStorage.UserStorage {
    private static final String FEEDBACK_CONTENT = "zd_content";
    private static final String LOG_TAG = "RateMyAppStorage";
    private static final String PREFERENCES_FILE_NAME = "zd_rma";
    private static final String RMA_PREFERENCES_DONT_SHOW_AGAIN_FOR_VERSION = "rma_dont_show_again_for_version";
    private static final String RMA_PREFERENCES_NUMBER_OF_LAUNCHES = "rma_number_of_launches";
    private static final String RMA_PREFERENCES_RATED_FOR_VERSION = "rma_rated_for_version";
    private static final String RMA_PREFERENCES_SAVED_LAUNCH_TIME = "rma_saved_launch_time";
    private AppVersion mAppVersion;
    private SafeMobileSettings mMobileSettings;
    private SharedPreferences mSharedPreferences;

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public String getCacheKey() {
        return PREFERENCES_FILE_NAME;
    }

    public RateMyAppStorage(Context context) {
        if (context == null) {
            Logger.e(LOG_TAG, "Context is null, storage will not work", new Object[0]);
            return;
        }
        this.mSharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        this.mAppVersion = new AppVersion(context);
        this.mMobileSettings = ZendeskConfig.INSTANCE.getMobileSettings();
        ZendeskConfig.INSTANCE.storage().sdkStorage().registerUserStorage(this);
    }

    public int getNumberOfLaunches() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            return 0;
        }
        return sharedPreferences.getInt(RMA_PREFERENCES_NUMBER_OF_LAUNCHES, 0);
    }

    public long getCanShowAfterTime() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            Logger.e(LOG_TAG, "getCanShowAfterTime() Storage is null, returning current time", new Object[0]);
            return System.currentTimeMillis();
        }
        long jCurrentTimeMillis = sharedPreferences.getLong(RMA_PREFERENCES_SAVED_LAUNCH_TIME, 0L);
        if (jCurrentTimeMillis == 0) {
            jCurrentTimeMillis = System.currentTimeMillis();
            this.mSharedPreferences.edit().putLong(RMA_PREFERENCES_SAVED_LAUNCH_TIME, jCurrentTimeMillis).apply();
        }
        return jCurrentTimeMillis + this.mMobileSettings.getRateMyAppDuration();
    }

    public void incrementNumberOfLaunches() {
        if (this.mSharedPreferences == null) {
            Logger.e(LOG_TAG, "incrementNumberOfLaunches() Storage is null, returning", new Object[0]);
            return;
        }
        int numberOfLaunches = getNumberOfLaunches() + 1;
        Logger.d(LOG_TAG, "Incrementing number of RMA launches", new Object[0]);
        this.mSharedPreferences.edit().putInt(RMA_PREFERENCES_NUMBER_OF_LAUNCHES, numberOfLaunches).apply();
    }

    public boolean isNumberOfLaunchesMet() {
        if (!this.mMobileSettings.hasHelpCenterSettings()) {
            Logger.d(LOG_TAG, "Settings are null", new Object[0]);
            return false;
        }
        int numberOfLaunches = getNumberOfLaunches();
        Logger.d(LOG_TAG, "Current launches is " + numberOfLaunches + " and settings requires >= " + this.mMobileSettings.getRateMyAppVisits(), new Object[0]);
        return ((long) numberOfLaunches) >= this.mMobileSettings.getRateMyAppVisits();
    }

    public boolean isLaunchTimeMet() {
        if (this.mSharedPreferences == null) {
            Logger.e(LOG_TAG, "isLaunchTimeMet() Storage is null, returning false", new Object[0]);
            return false;
        }
        long canShowAfterTime = getCanShowAfterTime();
        long jCurrentTimeMillis = System.currentTimeMillis();
        Logger.d(LOG_TAG, "Current time is " + jCurrentTimeMillis + " and settings requires >= " + canShowAfterTime, new Object[0]);
        return jCurrentTimeMillis >= canShowAfterTime;
    }

    public boolean isRatedForCurrentVersion() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(RMA_PREFERENCES_RATED_FOR_VERSION, -1) != -1;
        }
        Logger.e(LOG_TAG, "isRatedForCurrentVersion() Storage is null, returning true", new Object[0]);
        return true;
    }

    public void setRatedForCurrentVersion() {
        if (this.mSharedPreferences == null) {
            Logger.e(LOG_TAG, "setRatedForCurrentVersion() Storage is null, returning", new Object[0]);
            return;
        }
        int appVersionCode = this.mAppVersion.getAppVersionCode();
        Logger.d(LOG_TAG, "Setting rated for version code %s", Integer.valueOf(appVersionCode));
        this.mSharedPreferences.edit().putInt(RMA_PREFERENCES_RATED_FOR_VERSION, appVersionCode).apply();
    }

    public boolean isDontShowAgain() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(RMA_PREFERENCES_DONT_SHOW_AGAIN_FOR_VERSION, -1) != -1;
        }
        Logger.e(LOG_TAG, "isDontShowAgain() Storage is null, returning true", new Object[0]);
        return true;
    }

    public void setDontShowAgain() {
        if (this.mSharedPreferences == null) {
            Logger.e(LOG_TAG, "setDontShowSagain() Storage is null, returning", new Object[0]);
            return;
        }
        int appVersionCode = this.mAppVersion.getAppVersionCode();
        Logger.d(LOG_TAG, "Setting don't show again for version code %s", Integer.valueOf(appVersionCode));
        this.mSharedPreferences.edit().putInt(RMA_PREFERENCES_DONT_SHOW_AGAIN_FOR_VERSION, appVersionCode).apply();
    }

    public String getSavedFeedback() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(FEEDBACK_CONTENT, null);
    }

    public void setSavedFeedback(String str) {
        if (this.mSharedPreferences == null) {
            Logger.e(LOG_TAG, "setSavedFeedback() Storage is null, returning", new Object[0]);
        } else {
            Logger.d(LOG_TAG, "Saving feedback to storage", new Object[0]);
            this.mSharedPreferences.edit().putString(FEEDBACK_CONTENT, str).apply();
        }
    }

    public void reset() {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences == null) {
            Logger.e(LOG_TAG, "reset() Storage is null, returning", new Object[0]);
        } else {
            sharedPreferences.edit().clear().apply();
        }
    }

    @Override // com.zendesk.sdk.storage.SdkStorage.UserStorage
    public void clearUserData() {
        if (this.mSharedPreferences == null) {
            Logger.e(LOG_TAG, "deleteSavedFeedback() Storage is null, returning", new Object[0]);
        } else {
            Logger.d(LOG_TAG, "Deleting saved feedback", new Object[0]);
            this.mSharedPreferences.edit().remove(FEEDBACK_CONTENT).apply();
        }
    }
}
