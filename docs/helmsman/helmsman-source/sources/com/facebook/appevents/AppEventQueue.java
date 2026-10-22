package com.facebook.appevents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.internal.FetchedAppSettings;
import com.facebook.internal.FetchedAppSettingsManager;
import com.facebook.internal.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;

/* JADX INFO: loaded from: classes.dex */
class AppEventQueue {
    private static final int FLUSH_PERIOD_IN_SECONDS = 15;
    private static final int NUM_LOG_EVENTS_TO_TRY_TO_FLUSH_AFTER = 100;
    private static final String TAG = "com.facebook.appevents.AppEventQueue";
    private static ScheduledFuture scheduledFuture;
    private static volatile AppEventCollection appEventCollection = new AppEventCollection();
    private static final ScheduledExecutorService singleThreadExecutor = Executors.newSingleThreadScheduledExecutor();
    private static final Runnable flushRunnable = new Runnable() { // from class: com.facebook.appevents.AppEventQueue.1
        @Override // java.lang.Runnable
        public void run() {
            ScheduledFuture unused = AppEventQueue.scheduledFuture = null;
            if (AppEventsLogger.getFlushBehavior() != AppEventsLogger.FlushBehavior.EXPLICIT_ONLY) {
                AppEventQueue.flushAndWait(FlushReason.TIMER);
            }
        }
    };

    AppEventQueue() {
    }

    public static void persistToDisk() {
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.2
            @Override // java.lang.Runnable
            public void run() {
                AppEventStore.persistEvents(AppEventQueue.appEventCollection);
                AppEventCollection unused = AppEventQueue.appEventCollection = new AppEventCollection();
            }
        });
    }

    public static void flush(final FlushReason flushReason) {
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.3
            @Override // java.lang.Runnable
            public void run() {
                AppEventQueue.flushAndWait(flushReason);
            }
        });
    }

    public static void add(final AccessTokenAppIdPair accessTokenAppIdPair, final AppEvent appEvent) {
        singleThreadExecutor.execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.4
            @Override // java.lang.Runnable
            public void run() {
                AppEventQueue.appEventCollection.addEvent(accessTokenAppIdPair, appEvent);
                if (AppEventsLogger.getFlushBehavior() == AppEventsLogger.FlushBehavior.EXPLICIT_ONLY || AppEventQueue.appEventCollection.getEventCount() <= 100) {
                    if (AppEventQueue.scheduledFuture == null) {
                        ScheduledFuture unused = AppEventQueue.scheduledFuture = AppEventQueue.singleThreadExecutor.schedule(AppEventQueue.flushRunnable, 15L, TimeUnit.SECONDS);
                        return;
                    }
                    return;
                }
                AppEventQueue.flushAndWait(FlushReason.EVENT_THRESHOLD);
            }
        });
    }

    public static Set<AccessTokenAppIdPair> getKeySet() {
        return appEventCollection.keySet();
    }

    static void flushAndWait(FlushReason flushReason) {
        appEventCollection.addPersistedEvents(AppEventStore.readAndClearStore());
        try {
            FlushStatistics flushStatisticsSendEventsToServer = sendEventsToServer(flushReason, appEventCollection);
            if (flushStatisticsSendEventsToServer != null) {
                Intent intent = new Intent(AppEventsLogger.ACTION_APP_EVENTS_FLUSHED);
                intent.putExtra(AppEventsLogger.APP_EVENTS_EXTRA_NUM_EVENTS_FLUSHED, flushStatisticsSendEventsToServer.numEvents);
                intent.putExtra(AppEventsLogger.APP_EVENTS_EXTRA_FLUSH_RESULT, flushStatisticsSendEventsToServer.result);
                LocalBroadcastManager.getInstance(FacebookSdk.getApplicationContext()).sendBroadcast(intent);
            }
        } catch (Exception e) {
            Log.w(TAG, "Caught unexpected exception while flushing app events: ", e);
        }
    }

    private static FlushStatistics sendEventsToServer(FlushReason flushReason, AppEventCollection appEventCollection2) {
        FlushStatistics flushStatistics = new FlushStatistics();
        boolean limitEventAndDataUsage = FacebookSdk.getLimitEventAndDataUsage(FacebookSdk.getApplicationContext());
        ArrayList arrayList = new ArrayList();
        for (AccessTokenAppIdPair accessTokenAppIdPair : appEventCollection2.keySet()) {
            GraphRequest graphRequestBuildRequestForSession = buildRequestForSession(accessTokenAppIdPair, appEventCollection2.get(accessTokenAppIdPair), limitEventAndDataUsage, flushStatistics);
            if (graphRequestBuildRequestForSession != null) {
                arrayList.add(graphRequestBuildRequestForSession);
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flushing %d events due to %s.", Integer.valueOf(flushStatistics.numEvents), flushReason.toString());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((GraphRequest) it.next()).executeAndWait();
        }
        return flushStatistics;
    }

    private static GraphRequest buildRequestForSession(final AccessTokenAppIdPair accessTokenAppIdPair, final SessionEventsState sessionEventsState, boolean z, final FlushStatistics flushStatistics) {
        String applicationId = accessTokenAppIdPair.getApplicationId();
        FetchedAppSettings fetchedAppSettingsQueryAppSettings = FetchedAppSettingsManager.queryAppSettings(applicationId, false);
        final GraphRequest graphRequestNewPostRequest = GraphRequest.newPostRequest(null, String.format("%s/activities", applicationId), null, null);
        Bundle parameters = graphRequestNewPostRequest.getParameters();
        if (parameters == null) {
            parameters = new Bundle();
        }
        parameters.putString("access_token", accessTokenAppIdPair.getAccessTokenString());
        String pushNotificationsRegistrationId = AppEventsLogger.getPushNotificationsRegistrationId();
        if (pushNotificationsRegistrationId != null) {
            parameters.putString("device_token", pushNotificationsRegistrationId);
        }
        graphRequestNewPostRequest.setParameters(parameters);
        int iPopulateRequest = sessionEventsState.populateRequest(graphRequestNewPostRequest, FacebookSdk.getApplicationContext(), fetchedAppSettingsQueryAppSettings != null ? fetchedAppSettingsQueryAppSettings.supportsImplicitLogging() : false, z);
        if (iPopulateRequest == 0) {
            return null;
        }
        flushStatistics.numEvents += iPopulateRequest;
        graphRequestNewPostRequest.setCallback(new GraphRequest.Callback() { // from class: com.facebook.appevents.AppEventQueue.5
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse graphResponse) {
                AppEventQueue.handleResponse(accessTokenAppIdPair, graphRequestNewPostRequest, graphResponse, sessionEventsState, flushStatistics);
            }
        });
        return graphRequestNewPostRequest;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleResponse(final AccessTokenAppIdPair accessTokenAppIdPair, GraphRequest graphRequest, GraphResponse graphResponse, final SessionEventsState sessionEventsState, FlushStatistics flushStatistics) {
        String str;
        String string;
        FacebookRequestError error = graphResponse.getError();
        FlushResult flushResult = FlushResult.SUCCESS;
        if (error == null) {
            str = "Success";
        } else if (error.getErrorCode() == -1) {
            flushResult = FlushResult.NO_CONNECTIVITY;
            str = "Failed: No Connectivity";
        } else {
            str = String.format("Failed:\n  Response: %s\n  Error %s", graphResponse.toString(), error.toString());
            flushResult = FlushResult.SERVER_ERROR;
        }
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.APP_EVENTS)) {
            try {
                string = new JSONArray((String) graphRequest.getTag()).toString(2);
            } catch (JSONException unused) {
                string = "<Can't encode events for debug logging>";
            }
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flush completed\nParams: %s\n  Result: %s\n  Events JSON: %s", graphRequest.getGraphObject().toString(), str, string);
        }
        sessionEventsState.clearInFlightAndStats(error != null);
        if (flushResult == FlushResult.NO_CONNECTIVITY) {
            FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.appevents.AppEventQueue.6
                @Override // java.lang.Runnable
                public void run() {
                    AppEventStore.persistEvents(accessTokenAppIdPair, sessionEventsState);
                }
            });
        }
        if (flushResult == FlushResult.SUCCESS || flushStatistics.result == FlushResult.NO_CONNECTIVITY) {
            return;
        }
        flushStatistics.result = flushResult;
    }
}
