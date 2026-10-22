package com.bugsnag.android;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import io.reactivex.annotations.SchedulerSupport;
import java.io.IOException;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
class DeviceState implements JsonStream.Streamable {
    private final Float batteryLevel;
    private final Boolean charging;
    private final String locationStatus;
    private final String networkAccess;
    private final String orientation;
    private final Long freeMemory = getFreeMemory();
    private final Long freeDisk = getFreeDisk();
    private final String time = getTime();

    DeviceState(Context context) {
        this.orientation = getOrientation(context);
        this.batteryLevel = getBatteryLevel(context);
        this.charging = isCharging(context);
        this.locationStatus = getLocationStatus(context);
        this.networkAccess = getNetworkAccess(context);
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginObject();
        jsonStream.name("freeMemory").value(this.freeMemory);
        jsonStream.name("orientation").value(this.orientation);
        jsonStream.name("batteryLevel").value(this.batteryLevel);
        jsonStream.name("freeDisk").value(this.freeDisk);
        jsonStream.name("charging").value(this.charging);
        jsonStream.name("locationStatus").value(this.locationStatus);
        jsonStream.name("networkAccess").value(this.networkAccess);
        jsonStream.name("time").value(this.time);
        jsonStream.endObject();
    }

    private static Long getFreeMemory() {
        if (Runtime.getRuntime().maxMemory() != Long.MAX_VALUE) {
            return Long.valueOf((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory());
        }
        return Long.valueOf(Runtime.getRuntime().freeMemory());
    }

    private static String getOrientation(Context context) {
        int i = context.getResources().getConfiguration().orientation;
        if (i == 1) {
            return "portrait";
        }
        if (i != 2) {
            return null;
        }
        return "landscape";
    }

    private static Float getBatteryLevel(Context context) {
        try {
            Intent intentRegisterReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            return Float.valueOf(intentRegisterReceiver.getIntExtra("level", -1) / intentRegisterReceiver.getIntExtra("scale", -1));
        } catch (Exception unused) {
            Logger.warn("Could not get batteryLevel");
            return null;
        }
    }

    private static Long getFreeDisk() {
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
            StatFs statFs2 = new StatFs(Environment.getDataDirectory().getPath());
            return Long.valueOf(Math.min(((long) statFs2.getBlockSize()) * ((long) statFs2.getBlockCount()), blockSize));
        } catch (Exception unused) {
            Logger.warn("Could not get freeDisk");
            return null;
        }
    }

    private static Boolean isCharging(Context context) {
        try {
            int intExtra = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("status", -1);
            return Boolean.valueOf(intExtra == 2 || intExtra == 5);
        } catch (Exception unused) {
            Logger.warn("Could not get charging status");
            return null;
        }
    }

    private static String getLocationStatus(Context context) {
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed");
            return (string == null || string.length() <= 0) ? "disallowed" : "allowed";
        } catch (Exception unused) {
            Logger.warn("Could not get locationStatus");
            return null;
        }
    }

    private static String getNetworkAccess(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
                return SchedulerSupport.NONE;
            }
            if (activeNetworkInfo.getType() == 1) {
                return "wifi";
            }
            return activeNetworkInfo.getType() == 9 ? "ethernet" : "cellular";
        } catch (Exception unused) {
            Logger.warn("Could not get network access information, we recommend granting the 'android.permission.ACCESS_NETWORK_STATE' permission");
            return null;
        }
    }

    private String getTime() {
        return DateUtils.toISO8601(new Date());
    }
}
