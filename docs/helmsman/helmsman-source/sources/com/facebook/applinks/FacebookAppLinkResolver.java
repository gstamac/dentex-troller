package com.facebook.applinks;

import android.net.Uri;
import android.os.Bundle;
import bolts.AppLink;
import bolts.AppLinkResolver;
import bolts.Continuation;
import bolts.Task;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class FacebookAppLinkResolver implements AppLinkResolver {
    private static final String APP_LINK_ANDROID_TARGET_KEY = "android";
    private static final String APP_LINK_KEY = "app_links";
    private static final String APP_LINK_TARGET_APP_NAME_KEY = "app_name";
    private static final String APP_LINK_TARGET_CLASS_KEY = "class";
    private static final String APP_LINK_TARGET_PACKAGE_KEY = "package";
    private static final String APP_LINK_TARGET_SHOULD_FALLBACK_KEY = "should_fallback";
    private static final String APP_LINK_TARGET_URL_KEY = "url";
    private static final String APP_LINK_WEB_TARGET_KEY = "web";
    private final HashMap<Uri, AppLink> cachedAppLinks = new HashMap<>();

    @Override // bolts.AppLinkResolver
    public Task<AppLink> getAppLinkFromUrlInBackground(final Uri uri) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(uri);
        return getAppLinkFromUrlsInBackground(arrayList).onSuccess(new Continuation<Map<Uri, AppLink>, AppLink>() { // from class: com.facebook.applinks.FacebookAppLinkResolver.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // bolts.Continuation
            public AppLink then(Task<Map<Uri, AppLink>> task) throws Exception {
                return task.getResult().get(uri);
            }
        });
    }

    public Task<Map<Uri, AppLink>> getAppLinkFromUrlsInBackground(List<Uri> list) {
        AppLink appLink;
        final HashMap map = new HashMap();
        final HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder();
        for (Uri uri : list) {
            synchronized (this.cachedAppLinks) {
                appLink = this.cachedAppLinks.get(uri);
            }
            if (appLink != null) {
                map.put(uri, appLink);
            } else {
                if (!hashSet.isEmpty()) {
                    sb.append(',');
                }
                sb.append(uri.toString());
                hashSet.add(uri);
            }
        }
        if (hashSet.isEmpty()) {
            return Task.forResult(map);
        }
        final Task.TaskCompletionSource taskCompletionSourceCreate = Task.create();
        Bundle bundle = new Bundle();
        bundle.putString("ids", sb.toString());
        bundle.putString(GraphRequest.FIELDS_PARAM, String.format("%s.fields(%s,%s)", "app_links", "android", "web"));
        new GraphRequest(AccessToken.getCurrentAccessToken(), "", bundle, null, new GraphRequest.Callback() { // from class: com.facebook.applinks.FacebookAppLinkResolver.2
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse graphResponse) {
                FacebookRequestError error = graphResponse.getError();
                if (error != null) {
                    taskCompletionSourceCreate.setError(error.getException());
                    return;
                }
                JSONObject jSONObject = graphResponse.getJSONObject();
                if (jSONObject == null) {
                    taskCompletionSourceCreate.setResult(map);
                    return;
                }
                for (Uri uri2 : hashSet) {
                    if (jSONObject.has(uri2.toString())) {
                        try {
                            JSONObject jSONObject2 = jSONObject.getJSONObject(uri2.toString()).getJSONObject("app_links");
                            JSONArray jSONArray = jSONObject2.getJSONArray("android");
                            int length = jSONArray.length();
                            ArrayList arrayList = new ArrayList(length);
                            for (int i = 0; i < length; i++) {
                                AppLink.Target androidTargetFromJson = FacebookAppLinkResolver.getAndroidTargetFromJson(jSONArray.getJSONObject(i));
                                if (androidTargetFromJson != null) {
                                    arrayList.add(androidTargetFromJson);
                                }
                            }
                            AppLink appLink2 = new AppLink(uri2, arrayList, FacebookAppLinkResolver.getWebFallbackUriFromJson(uri2, jSONObject2));
                            map.put(uri2, appLink2);
                            synchronized (FacebookAppLinkResolver.this.cachedAppLinks) {
                                try {
                                    FacebookAppLinkResolver.this.cachedAppLinks.put(uri2, appLink2);
                                } catch (Throwable th) {
                                    throw th;
                                }
                            }
                        } catch (JSONException unused) {
                            continue;
                        }
                    }
                }
                taskCompletionSourceCreate.setResult(map);
            }
        }).executeAsync();
        return taskCompletionSourceCreate.getTask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AppLink.Target getAndroidTargetFromJson(JSONObject jSONObject) {
        String strTryGetStringFromJson = tryGetStringFromJson(jSONObject, APP_LINK_TARGET_PACKAGE_KEY, null);
        if (strTryGetStringFromJson == null) {
            return null;
        }
        String strTryGetStringFromJson2 = tryGetStringFromJson(jSONObject, APP_LINK_TARGET_CLASS_KEY, null);
        String strTryGetStringFromJson3 = tryGetStringFromJson(jSONObject, "app_name", null);
        String strTryGetStringFromJson4 = tryGetStringFromJson(jSONObject, "url", null);
        return new AppLink.Target(strTryGetStringFromJson, strTryGetStringFromJson2, strTryGetStringFromJson4 != null ? Uri.parse(strTryGetStringFromJson4) : null, strTryGetStringFromJson3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Uri getWebFallbackUriFromJson(Uri uri, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("web");
            if (!tryGetBooleanFromJson(jSONObject2, APP_LINK_TARGET_SHOULD_FALLBACK_KEY, true)) {
                return null;
            }
            String strTryGetStringFromJson = tryGetStringFromJson(jSONObject2, "url", null);
            Uri uri2 = strTryGetStringFromJson != null ? Uri.parse(strTryGetStringFromJson) : null;
            return uri2 != null ? uri2 : uri;
        } catch (JSONException unused) {
            return uri;
        }
    }

    private static String tryGetStringFromJson(JSONObject jSONObject, String str, String str2) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException unused) {
            return str2;
        }
    }

    private static boolean tryGetBooleanFromJson(JSONObject jSONObject, String str, boolean z) {
        try {
            return jSONObject.getBoolean(str);
        } catch (JSONException unused) {
            return z;
        }
    }
}
