package com.bugsnag.android;

import android.content.Context;
import android.content.pm.PackageManager;
import com.facebook.internal.ServerProtocol;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
class AppData implements JsonStream.Streamable {
    protected final String appName;
    private final Configuration config;
    protected final String guessedReleaseStage;
    protected final String packageName;
    protected final Integer versionCode;
    protected final String versionName;

    AppData(Context context, Configuration configuration) {
        this.config = configuration;
        this.packageName = getPackageName(context);
        this.appName = getAppName(context);
        this.versionCode = getVersionCode(context);
        this.versionName = getVersionName(context);
        this.guessedReleaseStage = guessReleaseStage(context);
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        jsonStream.beginObject();
        jsonStream.name("id").value(this.packageName);
        jsonStream.name("name").value(this.appName);
        jsonStream.name("packageName").value(this.packageName);
        jsonStream.name("versionName").value(this.versionName);
        jsonStream.name("versionCode").value(this.versionCode);
        jsonStream.name("buildUUID").value(this.config.getBuildUUID());
        jsonStream.name(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION).value(getAppVersion());
        jsonStream.name("releaseStage").value(getReleaseStage());
        jsonStream.endObject();
    }

    public String getReleaseStage() {
        if (this.config.getReleaseStage() != null) {
            return this.config.getReleaseStage();
        }
        return this.guessedReleaseStage;
    }

    public String getAppVersion() {
        if (this.config.getAppVersion() != null) {
            return this.config.getAppVersion();
        }
        return this.versionName;
    }

    private static String getPackageName(Context context) {
        return context.getPackageName();
    }

    private static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
        } catch (PackageManager.NameNotFoundException unused) {
            Logger.warn("Could not get app name");
            return null;
        }
    }

    private static Integer getVersionCode(Context context) {
        try {
            return Integer.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException unused) {
            Logger.warn("Could not get versionCode");
            return null;
        }
    }

    private static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            Logger.warn("Could not get versionName");
            return null;
        }
    }

    private static String guessReleaseStage(Context context) {
        try {
            return (context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags & 2) != 0 ? "development" : "production";
        } catch (PackageManager.NameNotFoundException unused) {
            Logger.warn("Could not get releaseStage");
            return "production";
        }
    }
}
