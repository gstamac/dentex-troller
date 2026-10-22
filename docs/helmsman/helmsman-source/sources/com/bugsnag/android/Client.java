package com.bugsnag.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.facebook.share.internal.ShareConstants;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/* JADX INFO: loaded from: classes.dex */
public class Client extends Observable implements Observer {
    private static final boolean BLOCKING = true;
    private static final String SHARED_PREF_KEY = "com.bugsnag.android";
    private static final String USER_EMAIL_KEY = "user.email";
    private static final String USER_ID_KEY = "user.id";
    private static final String USER_NAME_KEY = "user.name";
    private final Context appContext;
    protected final AppData appData;
    final Breadcrumbs breadcrumbs;
    protected final Configuration config;
    protected final DeviceData deviceData;
    protected final ErrorStore errorStore;
    protected final User user;

    public Client(Context context) {
        this(context, null, true);
    }

    public Client(Context context, String str) {
        this(context, str, true);
    }

    public Client(Context context, String str, boolean z) {
        this(context, createNewConfiguration(context, str, z));
    }

    public Client(Context context, Configuration configuration) {
        String string;
        this.user = new User();
        Context applicationContext = context.getApplicationContext();
        this.appContext = applicationContext;
        this.config = configuration;
        try {
            string = applicationContext.getPackageManager().getApplicationInfo(applicationContext.getPackageName(), 128).metaData.getString("com.bugsnag.android.BUILD_UUID");
        } catch (Exception unused) {
            string = null;
        }
        if (string != null) {
            this.config.setBuildUUID(string);
        }
        this.appData = new AppData(this.appContext, this.config);
        DeviceData deviceData = new DeviceData(this.appContext);
        this.deviceData = deviceData;
        AppState.init();
        this.breadcrumbs = new Breadcrumbs();
        setProjectPackages(this.appContext.getPackageName());
        if (this.config.getPersistUserBetweenSessions()) {
            SharedPreferences sharedPreferences = this.appContext.getSharedPreferences("com.bugsnag.android", 0);
            this.user.setId(sharedPreferences.getString(USER_ID_KEY, deviceData.getUserId()));
            this.user.setName(sharedPreferences.getString(USER_NAME_KEY, null));
            this.user.setEmail(sharedPreferences.getString(USER_EMAIL_KEY, null));
        } else {
            this.user.setId(deviceData.getUserId());
        }
        ErrorStore errorStore = new ErrorStore(this.config, this.appContext);
        this.errorStore = errorStore;
        if (this.config.getEnableExceptionHandler()) {
            enableExceptionHandler();
        }
        this.config.addObserver(this);
        errorStore.flush();
    }

    public void notifyBugsnagObservers(NotifyType notifyType) {
        setChanged();
        super.notifyObservers(notifyType.getValue());
    }

    @Override // java.util.Observer
    public void update(Observable observable, Object obj) {
        NotifyType notifyTypeFromInt;
        if (!(obj instanceof Integer) || (notifyTypeFromInt = NotifyType.fromInt((Integer) obj)) == null) {
            return;
        }
        notifyBugsnagObservers(notifyTypeFromInt);
    }

    private static Configuration createNewConfiguration(Context context, String str, boolean z) {
        Context applicationContext = context.getApplicationContext();
        if (TextUtils.isEmpty(str)) {
            try {
                str = applicationContext.getPackageManager().getApplicationInfo(applicationContext.getPackageName(), 128).metaData.getString("com.bugsnag.android.API_KEY");
            } catch (Exception unused) {
            }
        }
        if (str == null) {
            throw new NullPointerException("You must provide a Bugsnag API key");
        }
        Configuration configuration = new Configuration(str);
        configuration.setEnableExceptionHandler(z);
        return configuration;
    }

    public void setAppVersion(String str) {
        this.config.setAppVersion(str);
    }

    public String getContext() {
        return this.config.getContext();
    }

    public void setContext(String str) {
        this.config.setContext(str);
    }

    public void setEndpoint(String str) {
        this.config.setEndpoint(str);
    }

    public void setBuildUUID(String str) {
        this.config.setBuildUUID(str);
    }

    public void setFilters(String... strArr) {
        this.config.setFilters(strArr);
    }

    public void setIgnoreClasses(String... strArr) {
        this.config.setIgnoreClasses(strArr);
    }

    public void setNotifyReleaseStages(String... strArr) {
        this.config.setNotifyReleaseStages(strArr);
    }

    public void setProjectPackages(String... strArr) {
        this.config.setProjectPackages(strArr);
    }

    public void setReleaseStage(String str) {
        this.config.setReleaseStage(str);
    }

    public void setSendThreads(boolean z) {
        this.config.setSendThreads(z);
    }

    public void setUser(String str, String str2, String str3) {
        setUserId(str);
        setUserEmail(str2);
        setUserName(str3);
    }

    public void clearUser() {
        this.user.setId(this.deviceData.getUserId());
        this.user.setEmail(null);
        this.user.setName(null);
        this.appContext.getSharedPreferences("com.bugsnag.android", 0).edit().remove(USER_ID_KEY).remove(USER_EMAIL_KEY).remove(USER_NAME_KEY).commit();
        notifyBugsnagObservers(NotifyType.USER);
    }

    public void setUserId(String str) {
        setUserId(str, true);
    }

    void setUserId(String str, boolean z) {
        this.user.setId(str);
        if (this.config.getPersistUserBetweenSessions()) {
            storeInSharedPrefs(USER_ID_KEY, str);
        }
        if (z) {
            notifyBugsnagObservers(NotifyType.USER);
        }
    }

    public void setUserEmail(String str) {
        setUserEmail(str, true);
    }

    void setUserEmail(String str, boolean z) {
        this.user.setEmail(str);
        if (this.config.getPersistUserBetweenSessions()) {
            storeInSharedPrefs(USER_EMAIL_KEY, str);
        }
        if (z) {
            notifyBugsnagObservers(NotifyType.USER);
        }
    }

    public void setUserName(String str) {
        setUserName(str, true);
    }

    void setUserName(String str, boolean z) {
        this.user.setName(str);
        if (this.config.getPersistUserBetweenSessions()) {
            storeInSharedPrefs(USER_NAME_KEY, str);
        }
        if (z) {
            notifyBugsnagObservers(NotifyType.USER);
        }
    }

    public void beforeNotify(BeforeNotify beforeNotify) {
        this.config.beforeNotify(beforeNotify);
    }

    public void notify(Throwable th) {
        notify(new Error(this.config, th), false);
    }

    public void notifyBlocking(Throwable th) throws Throwable {
        notify(new Error(this.config, th), true);
    }

    public void notify(Throwable th, Callback callback) {
        notify(new Error(this.config, th), DeliveryStyle.ASYNC, callback);
    }

    public void notifyBlocking(Throwable th, Callback callback) throws Throwable {
        notify(new Error(this.config, th), DeliveryStyle.SAME_THREAD, callback);
    }

    public void notify(String str, String str2, StackTraceElement[] stackTraceElementArr, Callback callback) {
        notify(new Error(this.config, str, str2, stackTraceElementArr), DeliveryStyle.ASYNC, callback);
    }

    public void notifyBlocking(String str, String str2, StackTraceElement[] stackTraceElementArr, Callback callback) throws Throwable {
        notify(new Error(this.config, str, str2, stackTraceElementArr), DeliveryStyle.SAME_THREAD, callback);
    }

    public void notify(Throwable th, Severity severity) {
        Error error = new Error(this.config, th);
        error.setSeverity(severity);
        notify(error, false);
    }

    public void notifyBlocking(Throwable th, Severity severity) throws Throwable {
        Error error = new Error(this.config, th);
        error.setSeverity(severity);
        notify(error, true);
    }

    public void addToTab(String str, String str2, Object obj) {
        this.config.getMetaData().addToTab(str, str2, obj);
    }

    public void clearTab(String str) {
        this.config.getMetaData().clearTab(str);
    }

    public MetaData getMetaData() {
        return this.config.getMetaData();
    }

    public void setMetaData(MetaData metaData) {
        this.config.setMetaData(metaData);
    }

    public void leaveBreadcrumb(String str) {
        this.breadcrumbs.add(str);
        notifyBugsnagObservers(NotifyType.BREADCRUMB);
    }

    public void leaveBreadcrumb(String str, BreadcrumbType breadcrumbType, Map<String, String> map) {
        leaveBreadcrumb(str, breadcrumbType, map, true);
    }

    void leaveBreadcrumb(String str, BreadcrumbType breadcrumbType, Map<String, String> map, boolean z) {
        this.breadcrumbs.add(str, breadcrumbType, map);
        if (z) {
            notifyBugsnagObservers(NotifyType.BREADCRUMB);
        }
    }

    public void setMaxBreadcrumbs(int i) {
        this.breadcrumbs.setSize(i);
    }

    public void clearBreadcrumbs() {
        this.breadcrumbs.clear();
        notifyBugsnagObservers(NotifyType.BREADCRUMB);
    }

    public void enableExceptionHandler() {
        ExceptionHandler.enable(this);
    }

    public void disableExceptionHandler() {
        ExceptionHandler.disable(this);
    }

    private void notify(Error error, boolean z) throws Throwable {
        notify(error, z ? DeliveryStyle.SAME_THREAD : DeliveryStyle.ASYNC, (Callback) null);
    }

    private void notify(final Error error, DeliveryStyle deliveryStyle, Callback callback) throws Throwable {
        if (!error.shouldIgnoreClass() && this.config.shouldNotifyForReleaseStage(this.appData.getReleaseStage())) {
            error.setAppData(this.appData);
            error.setDeviceData(this.deviceData);
            error.setAppState(new AppState(this.appContext));
            error.setDeviceState(new DeviceState(this.appContext));
            error.setBreadcrumbs(this.breadcrumbs);
            error.setUser(this.user);
            if (!runBeforeNotifyTasks(error)) {
                Logger.info("Skipping notification - beforeNotify task returned false");
                return;
            }
            final Report report = new Report(this.config.getApiKey(), error);
            if (callback != null) {
                callback.beforeNotify(report);
            }
            int i = AnonymousClass2.$SwitchMap$com$bugsnag$android$DeliveryStyle[deliveryStyle.ordinal()];
            if (i == 1) {
                deliver(report, error);
            } else if (i == 2) {
                Async.run(new Runnable() { // from class: com.bugsnag.android.Client.1
                    @Override // java.lang.Runnable
                    public void run() throws Throwable {
                        Client.this.deliver(report, error);
                    }
                });
            } else if (i == 3) {
                this.errorStore.write(error);
                this.errorStore.flush();
            }
            this.breadcrumbs.add(error.getExceptionName(), BreadcrumbType.ERROR, Collections.singletonMap(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, error.getExceptionMessage()));
        }
    }

    /* JADX INFO: renamed from: com.bugsnag.android.Client$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$bugsnag$android$DeliveryStyle;

        static {
            int[] iArr = new int[DeliveryStyle.values().length];
            $SwitchMap$com$bugsnag$android$DeliveryStyle = iArr;
            try {
                iArr[DeliveryStyle.SAME_THREAD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bugsnag$android$DeliveryStyle[DeliveryStyle.ASYNC.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$bugsnag$android$DeliveryStyle[DeliveryStyle.ASYNC_WITH_CACHE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    void deliver(Report report, Error error) throws Throwable {
        try {
            HttpClient.post(this.config.getEndpoint(), report);
            Logger.info(String.format(Locale.US, "Sent 1 new error to Bugsnag", new Object[0]));
        } catch (HttpClient.BadResponseException unused) {
            Logger.info("Bad response when sending data to Bugsnag");
        } catch (HttpClient.NetworkException unused2) {
            Logger.info("Could not send error(s) to Bugsnag, saving to disk to send later");
            this.errorStore.write(error);
        } catch (Exception e) {
            Logger.warn("Problem sending error to Bugsnag", e);
        }
    }

    void cacheAndNotify(Throwable th, Severity severity) throws Throwable {
        Error error = new Error(this.config, th);
        error.setSeverity(severity);
        notify(error, DeliveryStyle.ASYNC_WITH_CACHE, (Callback) null);
    }

    private boolean runBeforeNotifyTasks(Error error) {
        Iterator<BeforeNotify> it = this.config.getBeforeNotifyTasks().iterator();
        while (it.hasNext()) {
            try {
                if (!it.next().run(error)) {
                    return false;
                }
            } catch (Throwable th) {
                Logger.warn("BeforeNotify threw an Exception", th);
            }
        }
        return true;
    }

    private boolean storeInSharedPrefs(String str, String str2) {
        return this.appContext.getSharedPreferences("com.bugsnag.android", 0).edit().putString(str, str2).commit();
    }

    public void notify(Throwable th, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, th);
        error.setMetaData(metaData);
        notify(error, false);
    }

    public void notifyBlocking(Throwable th, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, th);
        error.setMetaData(metaData);
        notify(error, true);
    }

    @Deprecated
    public void notify(Throwable th, Severity severity, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, th);
        error.setSeverity(severity);
        error.setMetaData(metaData);
        notify(error, false);
    }

    @Deprecated
    public void notifyBlocking(Throwable th, Severity severity, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, th);
        error.setSeverity(severity);
        error.setMetaData(metaData);
        notify(error, true);
    }

    @Deprecated
    public void notify(String str, String str2, StackTraceElement[] stackTraceElementArr, Severity severity, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, str, str2, stackTraceElementArr);
        error.setSeverity(severity);
        error.setMetaData(metaData);
        notify(error, false);
    }

    @Deprecated
    public void notifyBlocking(String str, String str2, StackTraceElement[] stackTraceElementArr, Severity severity, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, str, str2, stackTraceElementArr);
        error.setSeverity(severity);
        error.setMetaData(metaData);
        notify(error, true);
    }

    @Deprecated
    public void notify(String str, String str2, String str3, StackTraceElement[] stackTraceElementArr, Severity severity, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, str, str2, stackTraceElementArr);
        error.setSeverity(severity);
        error.setMetaData(metaData);
        error.setContext(str3);
        notify(error, false);
    }

    @Deprecated
    public void notifyBlocking(String str, String str2, String str3, StackTraceElement[] stackTraceElementArr, Severity severity, MetaData metaData) throws Throwable {
        Error error = new Error(this.config, str, str2, stackTraceElementArr);
        error.setSeverity(severity);
        error.setMetaData(metaData);
        error.setContext(str3);
        notify(error, true);
    }
}
