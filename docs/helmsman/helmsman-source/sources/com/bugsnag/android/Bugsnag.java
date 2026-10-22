package com.bugsnag.android;

import android.content.Context;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class Bugsnag {
    static Client client;

    private Bugsnag() {
    }

    public static Client init(Context context) {
        Client client2 = new Client(context);
        client = client2;
        NativeInterface.configureClientObservers(client2);
        return client;
    }

    public static Client init(Context context, String str) {
        Client client2 = new Client(context, str);
        client = client2;
        NativeInterface.configureClientObservers(client2);
        return client;
    }

    public static Client init(Context context, String str, boolean z) {
        Client client2 = new Client(context, str, z);
        client = client2;
        NativeInterface.configureClientObservers(client2);
        return client;
    }

    public static Client init(Context context, Configuration configuration) {
        Client client2 = new Client(context, configuration);
        client = client2;
        NativeInterface.configureClientObservers(client2);
        return client;
    }

    public static void setAppVersion(String str) {
        getClient().setAppVersion(str);
    }

    public static String getContext() {
        return getClient().getContext();
    }

    public static void setContext(String str) {
        getClient().setContext(str);
    }

    @Deprecated
    public static void setEndpoint(String str) {
        getClient().setEndpoint(str);
    }

    public static void setBuildUUID(String str) {
        getClient().setBuildUUID(str);
    }

    public static void setFilters(String... strArr) {
        getClient().setFilters(strArr);
    }

    public static void setIgnoreClasses(String... strArr) {
        getClient().setIgnoreClasses(strArr);
    }

    public static void setNotifyReleaseStages(String... strArr) {
        getClient().setNotifyReleaseStages(strArr);
    }

    public static void setProjectPackages(String... strArr) {
        getClient().setProjectPackages(strArr);
    }

    public static void setReleaseStage(String str) {
        getClient().setReleaseStage(str);
    }

    public static void setSendThreads(boolean z) {
        getClient().setSendThreads(z);
    }

    public static void setUser(String str, String str2, String str3) {
        getClient().setUser(str, str2, str3);
    }

    public static void clearUser() {
        getClient().clearUser();
    }

    public static void setUserId(String str) {
        getClient().setUserId(str);
    }

    public static void setUserEmail(String str) {
        getClient().setUserEmail(str);
    }

    public static void setUserName(String str) {
        getClient().setUserName(str);
    }

    public static void beforeNotify(BeforeNotify beforeNotify) {
        getClient().beforeNotify(beforeNotify);
    }

    public static void notify(Throwable th) {
        getClient().notify(th);
    }

    public static void notify(Throwable th, Callback callback) {
        getClient().notify(th, callback);
    }

    public static void notify(String str, String str2, StackTraceElement[] stackTraceElementArr, Callback callback) {
        getClient().notify(str, str2, stackTraceElementArr, callback);
    }

    public static void notify(Throwable th, Severity severity) {
        getClient().notify(th, severity);
    }

    public static void notify(Throwable th, final MetaData metaData) {
        getClient().notify(th, new Callback() { // from class: com.bugsnag.android.Bugsnag.1
            @Override // com.bugsnag.android.Callback
            public void beforeNotify(Report report) {
                report.getError().setMetaData(metaData);
            }
        });
    }

    @Deprecated
    public static void notify(Throwable th, final Severity severity, final MetaData metaData) {
        getClient().notify(th, new Callback() { // from class: com.bugsnag.android.Bugsnag.2
            @Override // com.bugsnag.android.Callback
            public void beforeNotify(Report report) {
                report.getError().setSeverity(severity);
                report.getError().setMetaData(metaData);
            }
        });
    }

    @Deprecated
    public static void notify(String str, String str2, StackTraceElement[] stackTraceElementArr, final Severity severity, final MetaData metaData) {
        getClient().notify(str, str2, stackTraceElementArr, new Callback() { // from class: com.bugsnag.android.Bugsnag.3
            @Override // com.bugsnag.android.Callback
            public void beforeNotify(Report report) {
                report.getError().setSeverity(severity);
                report.getError().setMetaData(metaData);
            }
        });
    }

    @Deprecated
    public static void notify(String str, String str2, final String str3, StackTraceElement[] stackTraceElementArr, final Severity severity, final MetaData metaData) {
        getClient().notify(str, str2, stackTraceElementArr, new Callback() { // from class: com.bugsnag.android.Bugsnag.4
            @Override // com.bugsnag.android.Callback
            public void beforeNotify(Report report) {
                report.getError().setSeverity(severity);
                report.getError().setMetaData(metaData);
                report.getError().setContext(str3);
            }
        });
    }

    public static void addToTab(String str, String str2, Object obj) {
        getClient().addToTab(str, str2, obj);
    }

    public static void clearTab(String str) {
        getClient().clearTab(str);
    }

    public static MetaData getMetaData() {
        return getClient().getMetaData();
    }

    public static void setMetaData(MetaData metaData) {
        getClient().setMetaData(metaData);
    }

    public static void leaveBreadcrumb(String str) {
        getClient().leaveBreadcrumb(str);
    }

    public static void leaveBreadcrumb(String str, BreadcrumbType breadcrumbType, Map<String, String> map) {
        getClient().leaveBreadcrumb(str, breadcrumbType, map);
    }

    public static void setMaxBreadcrumbs(int i) {
        getClient().setMaxBreadcrumbs(i);
    }

    public static void clearBreadcrumbs() {
        getClient().clearBreadcrumbs();
    }

    public static void enableExceptionHandler() {
        getClient().enableExceptionHandler();
    }

    public static void disableExceptionHandler() {
        getClient().disableExceptionHandler();
    }

    public static Client getClient() {
        Client client2 = client;
        if (client2 != null) {
            return client2;
        }
        throw new IllegalStateException("You must call Bugsnag.init before any other Bugsnag methods");
    }
}
