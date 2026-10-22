package com.zendesk.sdk.power;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.zendesk.logger.Logger;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class PowerConfig {
    public static final float FIFTEEN_PERCENT_BATTERY_CHARGE = 0.15f;
    private static final String LOG_TAG = "PowerConfig";
    public static final float THIRTY_PERCENT_BATTERY_CHARGE = 0.3f;
    private static PowerConfig sInstance;
    private boolean mBatteryOk;
    private boolean mHasReadSyncSettingsPermission;

    private PowerConfig(Context context) {
        String str = LOG_TAG;
        Logger.d(str, "PowerConfig is initialising...", new Object[0]);
        Intent intentRegisterReceiver = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (intentRegisterReceiver == null) {
            Logger.w(str, "Battery status is unknown, assuming it is ok...", new Object[0]);
            this.mBatteryOk = true;
            return;
        }
        float intExtra = intentRegisterReceiver.getIntExtra("level", -1) / intentRegisterReceiver.getIntExtra("scale", -1);
        boolean z = intentRegisterReceiver.getIntExtra("plugged", -1) == 1;
        Logger.d(str, String.format(Locale.US, "Ac charging is %s and battery capacity is %s", Boolean.valueOf(z), Float.valueOf(intExtra)), new Object[0]);
        this.mBatteryOk = (z && intExtra > 0.15f) || intExtra > 0.3f;
        Logger.d(str, String.format(Locale.US, "Initial battery state is OK?: %s", Boolean.valueOf(this.mBatteryOk)), new Object[0]);
        this.mHasReadSyncSettingsPermission = context.checkCallingOrSelfPermission("android.permission.READ_SYNC_SETTINGS") == 0;
        Logger.d(str, String.format(Locale.US, "Has permission to read sync settings: %s", Boolean.valueOf(this.mHasReadSyncSettingsPermission)), new Object[0]);
    }

    public static synchronized PowerConfig getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PowerConfig(context);
        }
        return sInstance;
    }

    public boolean isBatteryOk() {
        return this.mBatteryOk;
    }

    public boolean isBatteryLow() {
        return !this.mBatteryOk;
    }

    void setBatteryOk(boolean z) {
        Logger.d(LOG_TAG, "Setting batteryOk to: " + z, new Object[0]);
        this.mBatteryOk = z;
    }

    public boolean isGlobalSyncEnabled() {
        if (this.mHasReadSyncSettingsPermission) {
            return ContentResolver.getMasterSyncAutomatically();
        }
        return true;
    }

    public boolean isGlobalSyncDisabled() {
        return !isGlobalSyncEnabled();
    }
}
