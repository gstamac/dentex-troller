package com.bugsnag.android;

import android.app.ActivityManager;
import android.content.Context;
import android.os.SystemClock;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class AppState implements JsonStream.Streamable {
    private static final Long startTime = Long.valueOf(SystemClock.elapsedRealtime());
    private final String activeScreen;
    private final Boolean inForeground;
    private final Boolean lowMemory;
    private final Long duration = getDuration();
    private final Long memoryUsage = getMemoryUsage();

    static void init() {
    }

    AppState(Context context) {
        this.inForeground = isInForeground(context);
        this.activeScreen = getActiveScreen(context);
        this.lowMemory = isLowMemory(context);
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginObject();
        jsonStream.name(TypedValues.TransitionType.S_DURATION).value(this.duration);
        jsonStream.name("inForeground").value(this.inForeground);
        jsonStream.name("activeScreen").value(this.activeScreen);
        jsonStream.name("memoryUsage").value(this.memoryUsage);
        jsonStream.name("lowMemory").value(this.lowMemory);
        jsonStream.endObject();
    }

    public static String getActiveScreenClass(String str) {
        if (str != null) {
            return str.substring(str.lastIndexOf(46) + 1);
        }
        return null;
    }

    private static Long getMemoryUsage() {
        return Long.valueOf(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    private static Boolean isLowMemory(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            return Boolean.valueOf(memoryInfo.lowMemory);
        } catch (Exception unused) {
            Logger.warn("Could not check lowMemory status");
            return null;
        }
    }

    private static String getActiveScreen(Context context) {
        try {
            return ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0).topActivity.getClassName();
        } catch (Exception unused) {
            Logger.warn("Could not get active screen information, we recommend granting the 'android.permission.GET_TASKS' permission");
            return null;
        }
    }

    private static Boolean isInForeground(Context context) {
        try {
            List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
            if (runningTasks.isEmpty()) {
                return false;
            }
            return Boolean.valueOf(runningTasks.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName()));
        } catch (Exception unused) {
            Logger.warn("Could not check if app is in the foreground, we recommend granting the 'android.permission.GET_TASKS' permission");
            return null;
        }
    }

    private static Long getDuration() {
        return Long.valueOf(SystemClock.elapsedRealtime() - startTime.longValue());
    }
}
