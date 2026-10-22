package com.bugsnag.android;

import android.os.Build;
import io.reactivex.annotations.SchedulerSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

/* JADX INFO: loaded from: classes.dex */
public class NativeInterface {
    private static Client client;

    private static Client getClient() {
        Client client2 = client;
        return client2 != null ? client2 : Bugsnag.getClient();
    }

    public static void setClient(Client client2) {
        client = client2;
        configureClientObservers(client2);
    }

    public static void configureClientObservers(Client client2) {
        try {
            client2.addObserver((Observer) Class.forName("com.bugsnag.android.ndk.BugsnagObserver").newInstance());
        } catch (ClassNotFoundException unused) {
            Logger.info("Bugsnag NDK integration not available");
        } catch (IllegalAccessException e) {
            Logger.warn("Could not access NDK observer", e);
        } catch (InstantiationException e2) {
            Logger.warn("Failed to instantiate NDK observer", e2);
        }
        client2.notifyBugsnagObservers(NotifyType.ALL);
    }

    public static String getContext() {
        return getClient().getContext();
    }

    public static String getErrorStorePath() {
        return getClient().errorStore.path;
    }

    public static String getUserId() {
        return getClient().user.getId();
    }

    public static String getUserEmail() {
        return getClient().user.getEmail();
    }

    public static String getUserName() {
        return getClient().user.getName();
    }

    public static String getPackageName() {
        return getClient().appData.packageName;
    }

    public static String getAppName() {
        return getClient().appData.appName;
    }

    public static String getVersionName() {
        return getClient().appData.versionName;
    }

    public static int getVersionCode() {
        return getClient().appData.versionCode.intValue();
    }

    public static String getBuildUUID() {
        return getClient().config.getBuildUUID();
    }

    public static String getAppVersion() {
        return getClient().appData.getAppVersion();
    }

    public static String getReleaseStage() {
        return getClient().appData.getReleaseStage();
    }

    public static String getDeviceId() {
        return getClient().deviceData.id;
    }

    public static String getDeviceLocale() {
        return getClient().deviceData.locale;
    }

    public static double getDeviceTotalMemory() {
        return getClient().deviceData.totalMemory.longValue();
    }

    public static Boolean getDeviceRooted() {
        return getClient().deviceData.rooted;
    }

    public static float getDeviceScreenDensity() {
        return getClient().deviceData.screenDensity.floatValue();
    }

    public static int getDeviceDpi() {
        return getClient().deviceData.dpi.intValue();
    }

    public static String getDeviceScreenResolution() {
        return getClient().deviceData.screenResolution;
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceOsBuild() {
        return Build.DISPLAY;
    }

    public static int getDeviceApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static String[] getDeviceCpuAbi() {
        return getClient().deviceData.cpuAbi;
    }

    public static Map<String, Object> getMetaData() {
        return getClient().getMetaData().store;
    }

    public static Object[] getBreadcrumbs() {
        return getClient().breadcrumbs.store.toArray();
    }

    public static String[] getFilters() {
        return getClient().config.getFilters();
    }

    public static String[] getReleaseStages() {
        return getClient().config.getNotifyReleaseStages();
    }

    public static void setUser(String str, String str2, String str3) {
        getClient().setUserId(str, false);
        getClient().setUserEmail(str2, false);
        getClient().setUserName(str3, false);
    }

    public static void leaveBreadcrumb(String str, BreadcrumbType breadcrumbType) {
        getClient().leaveBreadcrumb(str, breadcrumbType, new HashMap(), false);
    }

    public static void addToTab(String str, String str2, Object obj) {
        getClient().config.getMetaData().addToTab(str, str2, obj, false);
    }

    public static void notify(String str, String str2, final Severity severity, StackTraceElement[] stackTraceElementArr, final Map<String, Object> map) {
        getClient().notify(str, str2, stackTraceElementArr, new Callback() { // from class: com.bugsnag.android.NativeInterface.1
            @Override // com.bugsnag.android.Callback
            public void beforeNotify(Report report) {
                report.getError().setSeverity(severity);
                report.getError().config.defaultExceptionType = "c";
                for (String str3 : map.keySet()) {
                    Object obj = map.get(str3);
                    if (obj instanceof Map) {
                        Map map2 = (Map) obj;
                        for (Object obj2 : map2.keySet()) {
                            report.getError().getMetaData().addToTab(str3, obj2.toString(), map2.get(obj2));
                        }
                    } else {
                        report.getError().getMetaData().addToTab(SchedulerSupport.CUSTOM, str3, obj);
                    }
                }
            }
        });
    }
}
