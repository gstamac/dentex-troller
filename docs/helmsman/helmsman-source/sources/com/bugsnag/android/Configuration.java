package com.bugsnag.android;

import com.facebook.appevents.codeless.internal.Constants;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/* JADX INFO: loaded from: classes.dex */
public class Configuration extends Observable implements Observer {
    static final String DEFAULT_ENDPOINT = "https://notify.bugsnag.com";
    private final String apiKey;
    private String appVersion;
    private String buildUUID;
    private String context;
    private String[] ignoreClasses;
    private MetaData metaData;
    private String[] projectPackages;
    private String releaseStage;
    private String endpoint = DEFAULT_ENDPOINT;
    private String[] filters = {"password"};
    private String[] notifyReleaseStages = null;
    private boolean sendThreads = true;
    private boolean enableExceptionHandler = true;
    private boolean persistUserBetweenSessions = false;
    String defaultExceptionType = Constants.PLATFORM;
    private final Collection<BeforeNotify> beforeNotifyTasks = new LinkedList();

    public Configuration(String str) {
        this.apiKey = str;
        MetaData metaData = new MetaData();
        this.metaData = metaData;
        metaData.addObserver(this);
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String str) {
        this.appVersion = str;
        notifyBugsnagObservers(NotifyType.APP);
    }

    public String getContext() {
        return this.context;
    }

    public void setContext(String str) {
        this.context = str;
        notifyBugsnagObservers(NotifyType.CONTEXT);
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String str) {
        this.endpoint = str;
    }

    public String getBuildUUID() {
        return this.buildUUID;
    }

    public void setBuildUUID(String str) {
        this.buildUUID = str;
        notifyBugsnagObservers(NotifyType.APP);
    }

    public String[] getFilters() {
        return this.filters;
    }

    public void setFilters(String[] strArr) {
        this.filters = strArr;
        this.metaData.setFilters(strArr);
    }

    public String[] getIgnoreClasses() {
        return this.ignoreClasses;
    }

    public void setIgnoreClasses(String[] strArr) {
        this.ignoreClasses = strArr;
    }

    public String[] getNotifyReleaseStages() {
        return this.notifyReleaseStages;
    }

    public void setNotifyReleaseStages(String[] strArr) {
        this.notifyReleaseStages = strArr;
        notifyBugsnagObservers(NotifyType.RELEASE_STAGES);
    }

    public String[] getProjectPackages() {
        return this.projectPackages;
    }

    public void setProjectPackages(String[] strArr) {
        this.projectPackages = strArr;
    }

    public String getReleaseStage() {
        return this.releaseStage;
    }

    public void setReleaseStage(String str) {
        this.releaseStage = str;
        notifyBugsnagObservers(NotifyType.APP);
    }

    public boolean getSendThreads() {
        return this.sendThreads;
    }

    public void setSendThreads(boolean z) {
        this.sendThreads = z;
    }

    public boolean getEnableExceptionHandler() {
        return this.enableExceptionHandler;
    }

    public void setEnableExceptionHandler(boolean z) {
        this.enableExceptionHandler = z;
    }

    protected MetaData getMetaData() {
        return this.metaData;
    }

    protected void setMetaData(MetaData metaData) {
        this.metaData.deleteObserver(this);
        this.metaData = metaData;
        metaData.addObserver(this);
        notifyBugsnagObservers(NotifyType.META);
    }

    protected Collection<BeforeNotify> getBeforeNotifyTasks() {
        return this.beforeNotifyTasks;
    }

    public boolean getPersistUserBetweenSessions() {
        return this.persistUserBetweenSessions;
    }

    public void setPersistUserBetweenSessions(boolean z) {
        this.persistUserBetweenSessions = z;
    }

    protected boolean shouldNotifyForReleaseStage(String str) {
        String[] strArr = this.notifyReleaseStages;
        if (strArr == null) {
            return true;
        }
        return Arrays.asList(strArr).contains(str);
    }

    protected boolean shouldIgnoreClass(String str) {
        String[] strArr = this.ignoreClasses;
        if (strArr == null) {
            return false;
        }
        return Arrays.asList(strArr).contains(str);
    }

    protected void beforeNotify(BeforeNotify beforeNotify) {
        this.beforeNotifyTasks.add(beforeNotify);
    }

    protected boolean inProject(String str) {
        String[] strArr = this.projectPackages;
        if (strArr != null) {
            for (String str2 : strArr) {
                if (str2 != null && str.startsWith(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void notifyBugsnagObservers(NotifyType notifyType) {
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
}
