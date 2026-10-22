package com.facebook.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class FetchedAppGateKeepersManager {
    private static final String APPLICATION_FIELDS = "fields";
    private static final long APPLICATION_GATEKEEPER_CACHE_TIMEOUT = 3600000;
    private static final String APPLICATION_GATEKEEPER_EDGE = "mobile_sdk_gk";
    private static final String APPLICATION_GATEKEEPER_FIELD = "gatekeepers";
    private static final String APPLICATION_GRAPH_DATA = "data";
    private static final String APPLICATION_PLATFORM = "platform";
    private static final String APPLICATION_SDK_VERSION = "sdk_version";
    private static final String APP_GATEKEEPERS_PREFS_KEY_FORMAT = "com.facebook.internal.APP_GATEKEEPERS.%s";
    private static final String APP_GATEKEEPERS_PREFS_STORE = "com.facebook.internal.preferences.APP_GATEKEEPERS";
    private static final String APP_PLATFORM = "android";
    private static final String TAG = "com.facebook.internal.FetchedAppGateKeepersManager";
    private static final Map<String, JSONObject> fetchedAppGateKeepers = new ConcurrentHashMap();
    private static Long timestamp = null;

    public static synchronized void loadAppGateKeepersAsync() {
        if (isTimestampValid(timestamp)) {
            return;
        }
        final Context applicationContext = FacebookSdk.getApplicationContext();
        final String applicationId = FacebookSdk.getApplicationId();
        final String str = String.format(APP_GATEKEEPERS_PREFS_KEY_FORMAT, applicationId);
        FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.internal.FetchedAppGateKeepersManager.1
            @Override // java.lang.Runnable
            public void run() {
                SharedPreferences sharedPreferences = applicationContext.getSharedPreferences(FetchedAppGateKeepersManager.APP_GATEKEEPERS_PREFS_STORE, 0);
                JSONObject jSONObject = null;
                String string = sharedPreferences.getString(str, null);
                if (!Utility.isNullOrEmpty(string)) {
                    try {
                        jSONObject = new JSONObject(string);
                    } catch (JSONException e) {
                        Utility.logd("FacebookSDK", e);
                    }
                    if (jSONObject != null) {
                        FetchedAppGateKeepersManager.parseAppGateKeepersFromJSON(applicationId, jSONObject);
                    }
                }
                JSONObject appGateKeepersQueryResponse = FetchedAppGateKeepersManager.getAppGateKeepersQueryResponse(applicationId);
                if (appGateKeepersQueryResponse != null) {
                    Long unused = FetchedAppGateKeepersManager.timestamp = Long.valueOf(System.currentTimeMillis());
                    FetchedAppGateKeepersManager.parseAppGateKeepersFromJSON(applicationId, appGateKeepersQueryResponse);
                    sharedPreferences.edit().putString(str, appGateKeepersQueryResponse.toString()).apply();
                }
            }
        });
    }

    public static JSONObject queryAppGateKeepers(String str, boolean z) {
        if (!z) {
            Map<String, JSONObject> map = fetchedAppGateKeepers;
            if (map.containsKey(str)) {
                return map.get(str);
            }
        }
        JSONObject appGateKeepersQueryResponse = getAppGateKeepersQueryResponse(str);
        if (appGateKeepersQueryResponse == null) {
            return null;
        }
        Context applicationContext = FacebookSdk.getApplicationContext();
        applicationContext.getSharedPreferences(APP_GATEKEEPERS_PREFS_STORE, 0).edit().putString(String.format(APP_GATEKEEPERS_PREFS_KEY_FORMAT, str), appGateKeepersQueryResponse.toString()).apply();
        return parseAppGateKeepersFromJSON(str, appGateKeepersQueryResponse);
    }

    public static boolean getGateKeeperForKey(String str, String str2, boolean z) {
        loadAppGateKeepersAsync();
        if (str2 != null) {
            Map<String, JSONObject> map = fetchedAppGateKeepers;
            if (map.containsKey(str2)) {
                return map.get(str2).optBoolean(str, z);
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JSONObject getAppGateKeepersQueryResponse(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("platform", "android");
        bundle.putString(APPLICATION_SDK_VERSION, FacebookSdk.getSdkVersion());
        bundle.putString("fields", APPLICATION_GATEKEEPER_FIELD);
        GraphRequest graphRequestNewGraphPathRequest = GraphRequest.newGraphPathRequest(null, String.format("%s/%s", str, APPLICATION_GATEKEEPER_EDGE), null);
        graphRequestNewGraphPathRequest.setSkipClientToken(true);
        graphRequestNewGraphPathRequest.setParameters(bundle);
        return graphRequestNewGraphPathRequest.executeAndWait().getJSONObject();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized JSONObject parseAppGateKeepersFromJSON(String str, JSONObject jSONObject) {
        JSONObject jSONObject2;
        Map<String, JSONObject> map = fetchedAppGateKeepers;
        if (map.containsKey(str)) {
            jSONObject2 = map.get(str);
        } else {
            jSONObject2 = new JSONObject();
        }
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("data");
        JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray != null ? jSONArrayOptJSONArray.optJSONObject(0) : null;
        if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.optJSONArray(APPLICATION_GATEKEEPER_FIELD) != null) {
            JSONArray jSONArrayOptJSONArray2 = jSONObjectOptJSONObject.optJSONArray(APPLICATION_GATEKEEPER_FIELD);
            for (int i = 0; i < jSONArrayOptJSONArray2.length(); i++) {
                try {
                    JSONObject jSONObject3 = jSONArrayOptJSONArray2.getJSONObject(i);
                    jSONObject2.put(jSONObject3.getString("key"), jSONObject3.getBoolean("value"));
                } catch (JSONException e) {
                    Utility.logd("FacebookSDK", e);
                }
            }
        }
        fetchedAppGateKeepers.put(str, jSONObject2);
        return jSONObject2;
    }

    private static boolean isTimestampValid(Long l) {
        return l != null && System.currentTimeMillis() - l.longValue() < 3600000;
    }
}
