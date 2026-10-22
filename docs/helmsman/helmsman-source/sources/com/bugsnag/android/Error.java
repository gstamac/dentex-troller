package com.bugsnag.android;

import android.text.TextUtils;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class Error implements JsonStream.Streamable {
    private static final String PAYLOAD_VERSION = "3";
    private AppData appData;
    private AppState appState;
    private Breadcrumbs breadcrumbs;
    final Configuration config;
    private String context;
    private DeviceData deviceData;
    private DeviceState deviceState;
    private Throwable exception;
    private String groupingHash;
    private User user;
    private Severity severity = Severity.WARNING;
    private MetaData metaData = new MetaData();

    Error(Configuration configuration, Throwable th) {
        this.config = configuration;
        this.exception = th;
    }

    Error(Configuration configuration, String str, String str2, StackTraceElement[] stackTraceElementArr) {
        this.config = configuration;
        this.exception = new BugsnagException(str, str2, stackTraceElementArr);
    }

    @Override // com.bugsnag.android.JsonStream.Streamable
    public void toStream(JsonStream jsonStream) throws IOException {
        MetaData metaDataMerge = MetaData.merge(this.config.getMetaData(), this.metaData);
        jsonStream.beginObject();
        jsonStream.name("payloadVersion").value("3");
        jsonStream.name("context").value(getContext());
        jsonStream.name("severity").value(this.severity);
        jsonStream.name("metaData").value(metaDataMerge);
        if (this.config.getProjectPackages() != null) {
            jsonStream.name("projectPackages").beginArray();
            for (String str : this.config.getProjectPackages()) {
                jsonStream.value(str);
            }
            jsonStream.endArray();
        }
        jsonStream.name("exceptions").value(new Exceptions(this.config, this.exception));
        jsonStream.name("user").value(this.user);
        jsonStream.name("app").value(this.appData);
        jsonStream.name("appState").value(this.appState);
        jsonStream.name("device").value(this.deviceData);
        jsonStream.name("deviceState").value(this.deviceState);
        jsonStream.name("breadcrumbs").value(this.breadcrumbs);
        jsonStream.name("groupingHash").value(this.groupingHash);
        if (this.config.getSendThreads()) {
            jsonStream.name("threads").value(new ThreadState(this.config));
        }
        jsonStream.endObject();
    }

    public void setContext(String str) {
        this.context = str;
    }

    public String getContext() {
        String str = this.context;
        if (str != null && !TextUtils.isEmpty(str)) {
            return this.context;
        }
        if (this.config.getContext() != null) {
            return this.config.getContext();
        }
        if (this.appState != null) {
            return AppState.getActiveScreenClass(this.context);
        }
        return null;
    }

    public void setGroupingHash(String str) {
        this.groupingHash = str;
    }

    public void setSeverity(Severity severity) {
        if (severity != null) {
            this.severity = severity;
        }
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public void setUser(String str, String str2, String str3) {
        this.user = new User(str, str2, str3);
    }

    public User getUser() {
        return this.user;
    }

    public void setUserId(String str) {
        User user = new User(this.user);
        this.user = user;
        user.setId(str);
    }

    public void setUserEmail(String str) {
        User user = new User(this.user);
        this.user = user;
        user.setEmail(str);
    }

    public void setUserName(String str) {
        User user = new User(this.user);
        this.user = user;
        user.setName(str);
    }

    public void addToTab(String str, String str2, Object obj) {
        this.metaData.addToTab(str, str2, obj);
    }

    public void clearTab(String str) {
        this.metaData.clearTab(str);
    }

    public MetaData getMetaData() {
        return this.metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public String getExceptionName() {
        Throwable th = this.exception;
        if (th instanceof BugsnagException) {
            return ((BugsnagException) th).getName();
        }
        return th.getClass().getName();
    }

    public String getExceptionMessage() {
        return this.exception.getLocalizedMessage();
    }

    public Throwable getException() {
        return this.exception;
    }

    void setAppData(AppData appData) {
        this.appData = appData;
    }

    void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }

    void setAppState(AppState appState) {
        this.appState = appState;
    }

    void setDeviceState(DeviceState deviceState) {
        this.deviceState = deviceState;
    }

    void setUser(User user) {
        this.user = user;
    }

    void setBreadcrumbs(Breadcrumbs breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    boolean shouldIgnoreClass() {
        return this.config.shouldIgnoreClass(getExceptionName());
    }
}
