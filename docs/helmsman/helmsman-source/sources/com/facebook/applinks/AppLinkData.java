package com.facebook.applinks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AppLinkData {
    private static final String APPLINK_BRIDGE_ARGS_KEY = "bridge_args";
    private static final String APPLINK_METHOD_ARGS_KEY = "method_args";
    private static final String APPLINK_VERSION_KEY = "version";
    public static final String ARGUMENTS_EXTRAS_KEY = "extras";
    public static final String ARGUMENTS_NATIVE_CLASS_KEY = "com.facebook.platform.APPLINK_NATIVE_CLASS";
    public static final String ARGUMENTS_NATIVE_URL = "com.facebook.platform.APPLINK_NATIVE_URL";
    public static final String ARGUMENTS_REFERER_DATA_KEY = "referer_data";
    public static final String ARGUMENTS_TAPTIME_KEY = "com.facebook.platform.APPLINK_TAP_TIME_UTC";
    private static final String BRIDGE_ARGS_METHOD_KEY = "method";
    private static final String BUNDLE_AL_APPLINK_DATA_KEY = "al_applink_data";
    static final String BUNDLE_APPLINK_ARGS_KEY = "com.facebook.platform.APPLINK_ARGS";
    private static final String DEFERRED_APP_LINK_ARGS_FIELD = "applink_args";
    private static final String DEFERRED_APP_LINK_CLASS_FIELD = "applink_class";
    private static final String DEFERRED_APP_LINK_CLICK_TIME_FIELD = "click_time";
    private static final String DEFERRED_APP_LINK_EVENT = "DEFERRED_APP_LINK";
    private static final String DEFERRED_APP_LINK_PATH = "%s/activities";
    private static final String DEFERRED_APP_LINK_URL_FIELD = "applink_url";
    private static final String EXTRAS_DEEPLINK_CONTEXT_KEY = "deeplink_context";
    private static final String METHOD_ARGS_REF_KEY = "ref";
    private static final String METHOD_ARGS_TARGET_URL_KEY = "target_url";
    private static final String PROMOTION_CODE_KEY = "promo_code";
    private static final String REFERER_DATA_REF_KEY = "fb_ref";
    private static final String TAG = "com.facebook.applinks.AppLinkData";
    private Bundle argumentBundle;
    private JSONObject arguments;
    private String promotionCode;
    private String ref;
    private Uri targetUri;

    public interface CompletionHandler {
        void onDeferredAppLinkDataFetched(AppLinkData appLinkData);
    }

    public static void fetchDeferredAppLinkData(Context context, CompletionHandler completionHandler) {
        fetchDeferredAppLinkData(context, null, completionHandler);
    }

    public static void fetchDeferredAppLinkData(Context context, final String str, final CompletionHandler completionHandler) {
        Validate.notNull(context, "context");
        Validate.notNull(completionHandler, "completionHandler");
        if (str == null) {
            str = Utility.getMetadataApplicationId(context);
        }
        Validate.notNull(str, "applicationId");
        final Context applicationContext = context.getApplicationContext();
        FacebookSdk.getExecutor().execute(new Runnable() { // from class: com.facebook.applinks.AppLinkData.1
            @Override // java.lang.Runnable
            public void run() {
                AppLinkData.fetchDeferredAppLinkFromServer(applicationContext, str, completionHandler);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:21:0x0088 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:25:0x0090 A[Catch: JSONException -> 0x009b, Exception -> 0x00b9, TRY_ENTER, TryCatch #4 {JSONException -> 0x009b, blocks: (B:22:0x008a, B:25:0x0090, B:26:0x0093, B:28:0x0097), top: B:56:0x008a, outer: #3 }] */
    /* JADX WARN: Code duplicated, block: B:28:0x0097 A[Catch: JSONException -> 0x009b, Exception -> 0x00b9, TRY_LEAVE, TryCatch #4 {JSONException -> 0x009b, blocks: (B:22:0x008a, B:25:0x0090, B:26:0x0093, B:28:0x0097), top: B:56:0x008a, outer: #3 }] */
    /* JADX WARN: Code duplicated, block: B:31:0x00a0 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:35:0x00a8 A[Catch: JSONException -> 0x00b3, Exception -> 0x00b9, TRY_ENTER, TryCatch #0 {JSONException -> 0x00b3, blocks: (B:32:0x00a2, B:35:0x00a8, B:36:0x00ab, B:38:0x00af), top: B:48:0x00a2, outer: #3 }] */
    /* JADX WARN: Code duplicated, block: B:38:0x00af A[Catch: JSONException -> 0x00b3, Exception -> 0x00b9, TRY_LEAVE, TryCatch #0 {JSONException -> 0x00b3, blocks: (B:32:0x00a2, B:35:0x00a8, B:36:0x00ab, B:38:0x00af), top: B:48:0x00a2, outer: #3 }] */
    /* JADX WARN: Code duplicated, block: B:48:0x00a2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:56:0x008a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public static void fetchDeferredAppLinkFromServer(Context context, String str, CompletionHandler completionHandler) {
        JSONObject jSONObject;
        Bundle bundle;
        JSONObject jSONObject2;
        Bundle bundle2;
        JSONObject jSONObject3 = new JSONObject();
        try {
            jSONObject3.put(NotificationCompat.CATEGORY_EVENT, DEFERRED_APP_LINK_EVENT);
            Utility.setAppEventAttributionParameters(jSONObject3, AttributionIdentifiers.getAttributionIdentifiers(context), AppEventsLogger.getAnonymousAppDeviceGUID(context), FacebookSdk.getLimitEventAndDataUsage(context));
            Utility.setAppEventExtendedDeviceInfoParameters(jSONObject3, FacebookSdk.getApplicationContext());
            jSONObject3.put("application_package_name", context.getPackageName());
            String str2 = String.format(DEFERRED_APP_LINK_PATH, str);
            AppLinkData appLinkDataCreateFromJson = null;
            try {
                JSONObject jSONObject4 = GraphRequest.newPostRequest(null, str2, jSONObject3, null).executeAndWait().getJSONObject();
                if (jSONObject4 != null) {
                    String strOptString = jSONObject4.optString(DEFERRED_APP_LINK_ARGS_FIELD);
                    long jOptLong = jSONObject4.optLong(DEFERRED_APP_LINK_CLICK_TIME_FIELD, -1L);
                    String strOptString2 = jSONObject4.optString(DEFERRED_APP_LINK_CLASS_FIELD);
                    String strOptString3 = jSONObject4.optString(DEFERRED_APP_LINK_URL_FIELD);
                    if (!TextUtils.isEmpty(strOptString)) {
                        appLinkDataCreateFromJson = createFromJson(strOptString);
                        if (jOptLong != -1) {
                            try {
                                JSONObject jSONObject5 = appLinkDataCreateFromJson.arguments;
                                if (jSONObject5 != null) {
                                    jSONObject5.put(ARGUMENTS_TAPTIME_KEY, jOptLong);
                                }
                                Bundle bundle3 = appLinkDataCreateFromJson.argumentBundle;
                                if (bundle3 != null) {
                                    bundle3.putString(ARGUMENTS_TAPTIME_KEY, Long.toString(jOptLong));
                                    if (strOptString2 != null) {
                                        try {
                                            jSONObject = appLinkDataCreateFromJson.arguments;
                                            if (jSONObject != null) {
                                                jSONObject.put(ARGUMENTS_NATIVE_CLASS_KEY, strOptString2);
                                            }
                                            bundle = appLinkDataCreateFromJson.argumentBundle;
                                            if (bundle != null) {
                                                bundle.putString(ARGUMENTS_NATIVE_CLASS_KEY, strOptString2);
                                                if (strOptString3 != null) {
                                                    try {
                                                        jSONObject2 = appLinkDataCreateFromJson.arguments;
                                                        if (jSONObject2 != null) {
                                                            jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                                        }
                                                        bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                                        if (bundle2 != null) {
                                                            bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                                        }
                                                    } catch (JSONException unused) {
                                                        Utility.logd(TAG, "Unable to put tap time in AppLinkData.arguments");
                                                    }
                                                }
                                            } else if (strOptString3 != null) {
                                                jSONObject2 = appLinkDataCreateFromJson.arguments;
                                                if (jSONObject2 != null) {
                                                    jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                                }
                                                bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                                if (bundle2 != null) {
                                                    bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                                }
                                            }
                                        } catch (JSONException unused2) {
                                            Utility.logd(TAG, "Unable to put tap time in AppLinkData.arguments");
                                        }
                                    } else if (strOptString3 != null) {
                                        jSONObject2 = appLinkDataCreateFromJson.arguments;
                                        if (jSONObject2 != null) {
                                            jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                        }
                                        bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                        if (bundle2 != null) {
                                            bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                        }
                                    }
                                } else if (strOptString2 != null) {
                                    jSONObject = appLinkDataCreateFromJson.arguments;
                                    if (jSONObject != null) {
                                        jSONObject.put(ARGUMENTS_NATIVE_CLASS_KEY, strOptString2);
                                    }
                                    bundle = appLinkDataCreateFromJson.argumentBundle;
                                    if (bundle != null) {
                                        bundle.putString(ARGUMENTS_NATIVE_CLASS_KEY, strOptString2);
                                        if (strOptString3 != null) {
                                            jSONObject2 = appLinkDataCreateFromJson.arguments;
                                            if (jSONObject2 != null) {
                                                jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                            }
                                            bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                            if (bundle2 != null) {
                                                bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                            }
                                        }
                                    } else if (strOptString3 != null) {
                                        jSONObject2 = appLinkDataCreateFromJson.arguments;
                                        if (jSONObject2 != null) {
                                            jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                        }
                                        bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                        if (bundle2 != null) {
                                            bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                        }
                                    }
                                } else if (strOptString3 != null) {
                                    jSONObject2 = appLinkDataCreateFromJson.arguments;
                                    if (jSONObject2 != null) {
                                        jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                    }
                                    bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                    if (bundle2 != null) {
                                        bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                    }
                                }
                            } catch (JSONException unused3) {
                                Utility.logd(TAG, "Unable to put tap time in AppLinkData.arguments");
                            }
                        } else if (strOptString2 != null) {
                            jSONObject = appLinkDataCreateFromJson.arguments;
                            if (jSONObject != null) {
                                jSONObject.put(ARGUMENTS_NATIVE_CLASS_KEY, strOptString2);
                            }
                            bundle = appLinkDataCreateFromJson.argumentBundle;
                            if (bundle != null) {
                                bundle.putString(ARGUMENTS_NATIVE_CLASS_KEY, strOptString2);
                                if (strOptString3 != null) {
                                    jSONObject2 = appLinkDataCreateFromJson.arguments;
                                    if (jSONObject2 != null) {
                                        jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                    }
                                    bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                    if (bundle2 != null) {
                                        bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                    }
                                }
                            } else if (strOptString3 != null) {
                                jSONObject2 = appLinkDataCreateFromJson.arguments;
                                if (jSONObject2 != null) {
                                    jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                                }
                                bundle2 = appLinkDataCreateFromJson.argumentBundle;
                                if (bundle2 != null) {
                                    bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                                }
                            }
                        } else if (strOptString3 != null) {
                            jSONObject2 = appLinkDataCreateFromJson.arguments;
                            if (jSONObject2 != null) {
                                jSONObject2.put(ARGUMENTS_NATIVE_URL, strOptString3);
                            }
                            bundle2 = appLinkDataCreateFromJson.argumentBundle;
                            if (bundle2 != null) {
                                bundle2.putString(ARGUMENTS_NATIVE_URL, strOptString3);
                            }
                        }
                    }
                }
            } catch (Exception unused4) {
                Utility.logd(TAG, "Unable to fetch deferred applink from server");
            }
            completionHandler.onDeferredAppLinkDataFetched(appLinkDataCreateFromJson);
        } catch (JSONException e) {
            throw new FacebookException("An error occurred while preparing deferred app link", e);
        }
    }

    public static AppLinkData createFromActivity(Activity activity) {
        Validate.notNull(activity, "activity");
        Intent intent = activity.getIntent();
        if (intent == null) {
            return null;
        }
        AppLinkData appLinkDataCreateFromAlApplinkData = createFromAlApplinkData(intent);
        if (appLinkDataCreateFromAlApplinkData == null) {
            appLinkDataCreateFromAlApplinkData = createFromJson(intent.getStringExtra(BUNDLE_APPLINK_ARGS_KEY));
        }
        return appLinkDataCreateFromAlApplinkData == null ? createFromUri(intent.getData()) : appLinkDataCreateFromAlApplinkData;
    }

    public static AppLinkData createFromAlApplinkData(Intent intent) {
        Bundle bundleExtra;
        String string;
        String string2;
        if (intent == null || (bundleExtra = intent.getBundleExtra(BUNDLE_AL_APPLINK_DATA_KEY)) == null) {
            return null;
        }
        AppLinkData appLinkData = new AppLinkData();
        Uri data = intent.getData();
        appLinkData.targetUri = data;
        if (data == null && (string2 = bundleExtra.getString(METHOD_ARGS_TARGET_URL_KEY)) != null) {
            appLinkData.targetUri = Uri.parse(string2);
        }
        appLinkData.argumentBundle = bundleExtra;
        appLinkData.arguments = null;
        Bundle bundle = bundleExtra.getBundle(ARGUMENTS_REFERER_DATA_KEY);
        if (bundle != null) {
            appLinkData.ref = bundle.getString(REFERER_DATA_REF_KEY);
        }
        Bundle bundle2 = bundleExtra.getBundle(ARGUMENTS_EXTRAS_KEY);
        if (bundle2 != null && (string = bundle2.getString("deeplink_context")) != null) {
            try {
                JSONObject jSONObject = new JSONObject(string);
                if (jSONObject.has("promo_code")) {
                    appLinkData.promotionCode = jSONObject.getString("promo_code");
                }
            } catch (JSONException e) {
                Utility.logd(TAG, "Unable to parse deeplink_context JSON", e);
            }
        }
        return appLinkData;
    }

    private static AppLinkData createFromJson(String str) {
        if (str == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("version");
            if (jSONObject.getJSONObject("bridge_args").getString(BRIDGE_ARGS_METHOD_KEY).equals("applink") && string.equals("2")) {
                AppLinkData appLinkData = new AppLinkData();
                JSONObject jSONObject2 = jSONObject.getJSONObject("method_args");
                appLinkData.arguments = jSONObject2;
                if (jSONObject2.has(METHOD_ARGS_REF_KEY)) {
                    appLinkData.ref = appLinkData.arguments.getString(METHOD_ARGS_REF_KEY);
                } else if (appLinkData.arguments.has(ARGUMENTS_REFERER_DATA_KEY)) {
                    JSONObject jSONObject3 = appLinkData.arguments.getJSONObject(ARGUMENTS_REFERER_DATA_KEY);
                    if (jSONObject3.has(REFERER_DATA_REF_KEY)) {
                        appLinkData.ref = jSONObject3.getString(REFERER_DATA_REF_KEY);
                    }
                }
                if (appLinkData.arguments.has(METHOD_ARGS_TARGET_URL_KEY)) {
                    appLinkData.targetUri = Uri.parse(appLinkData.arguments.getString(METHOD_ARGS_TARGET_URL_KEY));
                }
                if (appLinkData.arguments.has(ARGUMENTS_EXTRAS_KEY)) {
                    JSONObject jSONObject4 = appLinkData.arguments.getJSONObject(ARGUMENTS_EXTRAS_KEY);
                    if (jSONObject4.has("deeplink_context")) {
                        JSONObject jSONObject5 = jSONObject4.getJSONObject("deeplink_context");
                        if (jSONObject5.has("promo_code")) {
                            appLinkData.promotionCode = jSONObject5.getString("promo_code");
                        }
                    }
                }
                appLinkData.argumentBundle = toBundle(appLinkData.arguments);
                return appLinkData;
            }
        } catch (FacebookException e) {
            Utility.logd(TAG, "Unable to parse AppLink JSON", e);
        } catch (JSONException e2) {
            Utility.logd(TAG, "Unable to parse AppLink JSON", e2);
        }
        return null;
    }

    private static AppLinkData createFromUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        AppLinkData appLinkData = new AppLinkData();
        appLinkData.targetUri = uri;
        return appLinkData;
    }

    private static Bundle toBundle(JSONObject jSONObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            Object obj = jSONObject.get(next);
            if (obj instanceof JSONObject) {
                bundle.putBundle(next, toBundle((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                JSONArray jSONArray = (JSONArray) obj;
                int i = 0;
                if (jSONArray.length() == 0) {
                    bundle.putStringArray(next, new String[0]);
                } else {
                    Object obj2 = jSONArray.get(0);
                    if (obj2 instanceof JSONObject) {
                        Bundle[] bundleArr = new Bundle[jSONArray.length()];
                        while (i < jSONArray.length()) {
                            bundleArr[i] = toBundle(jSONArray.getJSONObject(i));
                            i++;
                        }
                        bundle.putParcelableArray(next, bundleArr);
                    } else {
                        if (obj2 instanceof JSONArray) {
                            throw new FacebookException("Nested arrays are not supported.");
                        }
                        String[] strArr = new String[jSONArray.length()];
                        while (i < jSONArray.length()) {
                            strArr[i] = jSONArray.get(i).toString();
                            i++;
                        }
                        bundle.putStringArray(next, strArr);
                    }
                }
            } else {
                bundle.putString(next, obj.toString());
            }
        }
        return bundle;
    }

    private AppLinkData() {
    }

    public Uri getTargetUri() {
        return this.targetUri;
    }

    public String getRef() {
        return this.ref;
    }

    public String getPromotionCode() {
        return this.promotionCode;
    }

    public Bundle getArgumentBundle() {
        return this.argumentBundle;
    }

    public Bundle getRefererData() {
        Bundle bundle = this.argumentBundle;
        if (bundle != null) {
            return bundle.getBundle(ARGUMENTS_REFERER_DATA_KEY);
        }
        return null;
    }
}
