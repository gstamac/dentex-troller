package com.bugsnag.android;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import com.facebook.appevents.codeless.internal.Constants;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
class DeviceData implements JsonStream.Streamable {
    private static final String[] ROOT_INDICATORS = {"/system/xbin/su", "/system/bin/su", "/system/app/Superuser.apk", "/system/app/SuperSU.apk", "/system/app/Superuser", "/system/app/SuperSU", "/system/xbin/daemonsu", "/su/bin"};
    protected final Integer dpi;
    protected final String id;
    protected final Float screenDensity;
    protected final String screenResolution;
    protected final Long totalMemory = getTotalMemory();
    protected final Boolean rooted = isRooted();
    protected final String locale = getLocale();
    protected final String[] cpuAbi = getCpuAbi();

    DeviceData(Context context) {
        this.screenDensity = getScreenDensity(context);
        this.dpi = getScreenDensityDpi(context);
        this.screenResolution = getScreenResolution(context);
        this.id = getAndroidId(context);
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginObject();
        jsonStream.name("osName").value(Constants.PLATFORM);
        jsonStream.name("manufacturer").value(Build.MANUFACTURER);
        jsonStream.name("brand").value(Build.BRAND);
        jsonStream.name("model").value(Build.MODEL);
        jsonStream.name("id").value(this.id);
        jsonStream.name("apiLevel").value(Build.VERSION.SDK_INT);
        jsonStream.name("osVersion").value(Build.VERSION.RELEASE);
        jsonStream.name("osBuild").value(Build.DISPLAY);
        jsonStream.name("locale").value(this.locale);
        jsonStream.name("totalMemory").value(this.totalMemory);
        jsonStream.name("jailbroken").value(this.rooted);
        jsonStream.name("screenDensity").value(this.screenDensity);
        jsonStream.name("dpi").value(this.dpi);
        jsonStream.name("screenResolution").value(this.screenResolution);
        jsonStream.name("cpuAbi").beginArray();
        for (String str : this.cpuAbi) {
            jsonStream.value(str);
        }
        jsonStream.endArray();
        jsonStream.endObject();
    }

    public String getUserId() {
        return this.id;
    }

    private static Float getScreenDensity(Context context) {
        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }
        return Float.valueOf(resources.getDisplayMetrics().density);
    }

    private static Integer getScreenDensityDpi(Context context) {
        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }
        return Integer.valueOf(resources.getDisplayMetrics().densityDpi);
    }

    private static String getScreenResolution(Context context) {
        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return String.format(Locale.US, "%dx%d", Integer.valueOf(Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels)), Integer.valueOf(Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels)));
    }

    private static Long getTotalMemory() {
        if (Runtime.getRuntime().maxMemory() != Long.MAX_VALUE) {
            return Long.valueOf(Runtime.getRuntime().maxMemory());
        }
        return Long.valueOf(Runtime.getRuntime().totalMemory());
    }

    private static Boolean isRooted() {
        if (Build.TAGS != null && Build.TAGS.contains("test-keys")) {
            return true;
        }
        try {
            for (String str : ROOT_INDICATORS) {
                if (new File(str).exists()) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return null;
        }
    }

    private static String getLocale() {
        return Locale.getDefault().toString();
    }

    private static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    private static String[] getCpuAbi() {
        return SupportedAbiWrapper.getSupportedAbis();
    }

    private static class SupportedAbiWrapper {
        private SupportedAbiWrapper() {
        }

        public static String[] getSupportedAbis() {
            return Build.SUPPORTED_ABIS;
        }
    }

    private static class Abi2Wrapper {
        private Abi2Wrapper() {
        }

        public static String[] getAbi1andAbi2() {
            return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
    }
}
