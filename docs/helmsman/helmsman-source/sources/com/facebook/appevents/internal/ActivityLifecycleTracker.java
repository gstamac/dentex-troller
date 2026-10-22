package com.facebook.appevents.internal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.appevents.codeless.CodelessMatcher;
import com.facebook.appevents.codeless.ViewIndexer;
import com.facebook.appevents.codeless.ViewIndexingTrigger;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.FetchedAppSettings;
import com.facebook.internal.FetchedAppSettingsManager;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ActivityLifecycleTracker {
    private static final String INCORRECT_IMPL_WARNING = "Unexpected activity pause without a matching activity resume. Logging data may be incorrect. Make sure you call activateApp from your Application's onCreate method";
    private static final long INTERRUPTION_THRESHOLD_MILLISECONDS = 1000;
    private static final String TAG = "com.facebook.appevents.internal.ActivityLifecycleTracker";
    private static String appId;
    private static long currentActivityAppearTime;
    private static volatile ScheduledFuture currentFuture;
    private static volatile SessionInfo currentSession;
    private static SensorManager sensorManager;
    private static ViewIndexer viewIndexer;
    private static final ScheduledExecutorService singleThreadExecutor = Executors.newSingleThreadScheduledExecutor();
    private static final Object currentFutureLock = new Object();
    private static AtomicInteger foregroundActivityCount = new AtomicInteger(0);
    private static AtomicBoolean tracking = new AtomicBoolean(false);
    private static final CodelessMatcher codelessMatcher = new CodelessMatcher();
    private static final ViewIndexingTrigger viewIndexingTrigger = new ViewIndexingTrigger();
    private static String deviceSessionID = null;
    private static Boolean isAppIndexingEnabled = false;
    private static volatile Boolean isCheckingSession = false;
    private static int activityReferences = 0;

    static /* synthetic */ int access$108() {
        int i = activityReferences;
        activityReferences = i + 1;
        return i;
    }

    static /* synthetic */ int access$110() {
        int i = activityReferences;
        activityReferences = i - 1;
        return i;
    }

    public static void startTracking(Application application, String str) {
        if (tracking.compareAndSet(false, true)) {
            appId = str;
            application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.1
                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    Logger.log(LoggingBehavior.APP_EVENTS, ActivityLifecycleTracker.TAG, "onActivityCreated");
                    AppEventUtility.assertIsMainThread();
                    ActivityLifecycleTracker.onActivityCreated(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStarted(Activity activity) {
                    ActivityLifecycleTracker.access$108();
                    Logger.log(LoggingBehavior.APP_EVENTS, ActivityLifecycleTracker.TAG, "onActivityStarted");
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityResumed(Activity activity) {
                    Logger.log(LoggingBehavior.APP_EVENTS, ActivityLifecycleTracker.TAG, "onActivityResumed");
                    AppEventUtility.assertIsMainThread();
                    ActivityLifecycleTracker.onActivityResumed(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityPaused(Activity activity) {
                    Logger.log(LoggingBehavior.APP_EVENTS, ActivityLifecycleTracker.TAG, "onActivityPaused");
                    AppEventUtility.assertIsMainThread();
                    ActivityLifecycleTracker.onActivityPaused(activity);
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityStopped(Activity activity) {
                    Logger.log(LoggingBehavior.APP_EVENTS, ActivityLifecycleTracker.TAG, "onActivityStopped");
                    AppEventsLogger.onContextStop();
                    ActivityLifecycleTracker.access$110();
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                    Logger.log(LoggingBehavior.APP_EVENTS, ActivityLifecycleTracker.TAG, "onActivitySaveInstanceState");
                }

                @Override // android.app.Application.ActivityLifecycleCallbacks
                public void onActivityDestroyed(Activity activity) {
                    Logger.log(LoggingBehavior.APP_EVENTS, ActivityLifecycleTracker.TAG, "onActivityDestroyed");
                }
            });
        }
    }

    public static boolean isInBackground() {
        return activityReferences == 0;
    }

    public static boolean isTracking() {
        return tracking.get();
    }

    public static UUID getCurrentSessionGuid() {
        if (currentSession != null) {
            return currentSession.getSessionId();
        }
        return null;
    }

    public static void onActivityCreated(Activity activity) {
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.2
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityLifecycleTracker.currentSession == null) {
                    SessionInfo unused = ActivityLifecycleTracker.currentSession = SessionInfo.getStoredSessionInfo();
                }
            }
        });
    }

    public static void onActivityResumed(Activity activity) {
        foregroundActivityCount.incrementAndGet();
        cancelCurrentTask();
        final long jCurrentTimeMillis = System.currentTimeMillis();
        currentActivityAppearTime = jCurrentTimeMillis;
        final String activityName = Utility.getActivityName(activity);
        codelessMatcher.add(activity);
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.3
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityLifecycleTracker.currentSession == null) {
                    SessionInfo unused = ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(jCurrentTimeMillis), null);
                    SessionLogger.logActivateApp(activityName, null, ActivityLifecycleTracker.appId);
                } else if (ActivityLifecycleTracker.currentSession.getSessionLastEventTime() != null) {
                    long jLongValue = jCurrentTimeMillis - ActivityLifecycleTracker.currentSession.getSessionLastEventTime().longValue();
                    if (jLongValue > ActivityLifecycleTracker.getSessionTimeoutInSeconds() * 1000) {
                        SessionLogger.logDeactivateApp(activityName, ActivityLifecycleTracker.currentSession, ActivityLifecycleTracker.appId);
                        SessionLogger.logActivateApp(activityName, null, ActivityLifecycleTracker.appId);
                        SessionInfo unused2 = ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(jCurrentTimeMillis), null);
                    } else if (jLongValue > 1000) {
                        ActivityLifecycleTracker.currentSession.incrementInterruptionCount();
                    }
                }
                ActivityLifecycleTracker.currentSession.setSessionLastEventTime(Long.valueOf(jCurrentTimeMillis));
                ActivityLifecycleTracker.currentSession.writeSessionToDisk();
            }
        });
        Context applicationContext = activity.getApplicationContext();
        final String applicationId = FacebookSdk.getApplicationId();
        final FetchedAppSettings appSettingsWithoutQuery = FetchedAppSettingsManager.getAppSettingsWithoutQuery(applicationId);
        if (appSettingsWithoutQuery == null || !appSettingsWithoutQuery.getCodelessEventsEnabled()) {
            return;
        }
        SensorManager sensorManager2 = (SensorManager) applicationContext.getSystemService("sensor");
        sensorManager = sensorManager2;
        if (sensorManager2 == null) {
            return;
        }
        Sensor defaultSensor = sensorManager2.getDefaultSensor(1);
        viewIndexer = new ViewIndexer(activity);
        ViewIndexingTrigger viewIndexingTrigger2 = viewIndexingTrigger;
        viewIndexingTrigger2.setOnShakeListener(new ViewIndexingTrigger.OnShakeListener() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.4
            @Override // com.facebook.appevents.codeless.ViewIndexingTrigger.OnShakeListener
            public void onShake() {
                FetchedAppSettings fetchedAppSettings = appSettingsWithoutQuery;
                boolean z = fetchedAppSettings != null && fetchedAppSettings.getCodelessEventsEnabled();
                boolean z2 = FacebookSdk.getCodelessSetupEnabled();
                if (z && z2) {
                    ActivityLifecycleTracker.checkCodelessSession(applicationId);
                }
            }
        });
        sensorManager.registerListener(viewIndexingTrigger2, defaultSensor, 2);
        if (appSettingsWithoutQuery == null || !appSettingsWithoutQuery.getCodelessEventsEnabled()) {
            return;
        }
        viewIndexer.schedule();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onActivityPaused(Activity activity) {
        if (foregroundActivityCount.decrementAndGet() < 0) {
            foregroundActivityCount.set(0);
            Log.w(TAG, INCORRECT_IMPL_WARNING);
        }
        cancelCurrentTask();
        final long jCurrentTimeMillis = System.currentTimeMillis();
        final String activityName = Utility.getActivityName(activity);
        codelessMatcher.remove(activity);
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.5
            @Override // java.lang.Runnable
            public void run() {
                if (ActivityLifecycleTracker.currentSession == null) {
                    SessionInfo unused = ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(jCurrentTimeMillis), null);
                }
                ActivityLifecycleTracker.currentSession.setSessionLastEventTime(Long.valueOf(jCurrentTimeMillis));
                if (ActivityLifecycleTracker.foregroundActivityCount.get() <= 0) {
                    Runnable runnable = new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ActivityLifecycleTracker.foregroundActivityCount.get() <= 0) {
                                SessionLogger.logDeactivateApp(activityName, ActivityLifecycleTracker.currentSession, ActivityLifecycleTracker.appId);
                                SessionInfo.clearSavedSessionFromDisk();
                                SessionInfo unused2 = ActivityLifecycleTracker.currentSession = null;
                            }
                            synchronized (ActivityLifecycleTracker.currentFutureLock) {
                                ScheduledFuture unused3 = ActivityLifecycleTracker.currentFuture = null;
                            }
                        }
                    };
                    synchronized (ActivityLifecycleTracker.currentFutureLock) {
                        ScheduledFuture unused2 = ActivityLifecycleTracker.currentFuture = ActivityLifecycleTracker.singleThreadExecutor.schedule(runnable, ActivityLifecycleTracker.getSessionTimeoutInSeconds(), TimeUnit.SECONDS);
                    }
                }
                long j = ActivityLifecycleTracker.currentActivityAppearTime;
                AutomaticAnalyticsLogger.logActivityTimeSpentEvent(activityName, j > 0 ? (jCurrentTimeMillis - j) / 1000 : 0L);
                ActivityLifecycleTracker.currentSession.writeSessionToDisk();
            }
        });
        ViewIndexer viewIndexer2 = viewIndexer;
        if (viewIndexer2 != null) {
            viewIndexer2.unschedule();
        }
        SensorManager sensorManager2 = sensorManager;
        if (sensorManager2 != null) {
            sensorManager2.unregisterListener(viewIndexingTrigger);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getSessionTimeoutInSeconds() {
        FetchedAppSettings appSettingsWithoutQuery = FetchedAppSettingsManager.getAppSettingsWithoutQuery(FacebookSdk.getApplicationId());
        if (appSettingsWithoutQuery == null) {
            return Constants.getDefaultAppEventsSessionTimeoutInSeconds();
        }
        return appSettingsWithoutQuery.getSessionTimeoutInSeconds();
    }

    private static void cancelCurrentTask() {
        synchronized (currentFutureLock) {
            if (currentFuture != null) {
                currentFuture.cancel(false);
            }
            currentFuture = null;
        }
    }

    public static void checkCodelessSession(final String str) {
        if (isCheckingSession.booleanValue()) {
            return;
        }
        isCheckingSession = true;
        FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.appevents.internal.ActivityLifecycleTracker.6
            @Override // java.lang.Runnable
            public void run() {
                GraphRequest graphRequestNewPostRequest = GraphRequest.newPostRequest(null, String.format(Locale.US, "%s/app_indexing_session", str), null, null);
                Bundle parameters = graphRequestNewPostRequest.getParameters();
                if (parameters == null) {
                    parameters = new Bundle();
                }
                AttributionIdentifiers attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(FacebookSdk.getApplicationContext());
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(Build.MODEL != null ? Build.MODEL : "");
                if (attributionIdentifiers != null && attributionIdentifiers.getAndroidAdvertiserId() != null) {
                    jSONArray.put(attributionIdentifiers.getAndroidAdvertiserId());
                } else {
                    jSONArray.put("");
                }
                String str2 = AppEventsConstants.EVENT_PARAM_VALUE_NO;
                jSONArray.put(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                if (AppEventUtility.isEmulator()) {
                    str2 = "1";
                }
                jSONArray.put(str2);
                Locale currentLocale = Utility.getCurrentLocale();
                jSONArray.put(currentLocale.getLanguage() + "_" + currentLocale.getCountry());
                String string = jSONArray.toString();
                parameters.putString(com.facebook.appevents.codeless.internal.Constants.DEVICE_SESSION_ID, ActivityLifecycleTracker.getCurrentDeviceSessionID());
                parameters.putString(com.facebook.appevents.codeless.internal.Constants.EXTINFO, string);
                graphRequestNewPostRequest.setParameters(parameters);
                if (graphRequestNewPostRequest != null) {
                    JSONObject jSONObject = graphRequestNewPostRequest.executeAndWait().getJSONObject();
                    Boolean unused = ActivityLifecycleTracker.isAppIndexingEnabled = Boolean.valueOf(jSONObject != null && jSONObject.optBoolean(com.facebook.appevents.codeless.internal.Constants.APP_INDEXING_ENABLED, false));
                    if (!ActivityLifecycleTracker.isAppIndexingEnabled.booleanValue()) {
                        String unused2 = ActivityLifecycleTracker.deviceSessionID = null;
                    } else {
                        ActivityLifecycleTracker.viewIndexer.schedule();
                    }
                }
                Boolean unused3 = ActivityLifecycleTracker.isCheckingSession = false;
            }
        });
    }

    public static String getCurrentDeviceSessionID() {
        if (deviceSessionID == null) {
            deviceSessionID = UUID.randomUUID().toString();
        }
        return deviceSessionID;
    }

    public static boolean getIsAppIndexingEnabled() {
        return isAppIndexingEnabled.booleanValue();
    }

    public static void updateAppIndexing(Boolean bool) {
        isAppIndexingEnabled = bool;
    }
}
