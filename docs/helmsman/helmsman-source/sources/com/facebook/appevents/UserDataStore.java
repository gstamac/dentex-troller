package com.facebook.appevents;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import com.facebook.FacebookSdk;
import com.facebook.appevents.internal.AppEventUtility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class UserDataStore {
    public static final String CITY = "ct";
    public static final String COUNTRY = "country";
    public static final String DATE_OF_BIRTH = "db";
    public static final String EMAIL = "em";
    public static final String FIRST_NAME = "fn";
    public static final String GENDER = "ge";
    public static final String LAST_NAME = "ln";
    public static final String PHONE = "ph";
    public static final String STATE = "st";
    private static final String TAG = "UserDataStore";
    private static final String USER_DATA_KEY = "com.facebook.appevents.UserDataStore.userData";
    public static final String ZIP = "zp";
    private static ConcurrentHashMap<String, String> hashedUserData;
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static SharedPreferences sharedPreferences;

    static void initStore() {
        if (initialized.get()) {
            return;
        }
        initAndWait();
    }

    static void setUserDataAndHash(final Bundle bundle) {
        AppEventsLogger.getAnalyticsExecutor().execute(new Runnable() { // from class: com.facebook.appevents.UserDataStore.1
            @Override // java.lang.Runnable
            public void run() {
                if (!UserDataStore.initialized.get()) {
                    Log.w(UserDataStore.TAG, "initStore should have been called before calling setUserData");
                    UserDataStore.initAndWait();
                }
                UserDataStore.updateHashUserData(bundle);
                UserDataStore.sharedPreferences.edit().putString(UserDataStore.USER_DATA_KEY, UserDataStore.mapToJsonStr(UserDataStore.hashedUserData)).apply();
            }
        });
    }

    static void setUserDataAndHash(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        Bundle bundle = new Bundle();
        if (str != null) {
            bundle.putString(EMAIL, str);
        }
        if (str2 != null) {
            bundle.putString(FIRST_NAME, str2);
        }
        if (str3 != null) {
            bundle.putString(LAST_NAME, str3);
        }
        if (str4 != null) {
            bundle.putString(PHONE, str4);
        }
        if (str5 != null) {
            bundle.putString(DATE_OF_BIRTH, str5);
        }
        if (str6 != null) {
            bundle.putString(GENDER, str6);
        }
        if (str7 != null) {
            bundle.putString(CITY, str7);
        }
        if (str8 != null) {
            bundle.putString(STATE, str8);
        }
        if (str9 != null) {
            bundle.putString(ZIP, str9);
        }
        if (str10 != null) {
            bundle.putString(COUNTRY, str10);
        }
        setUserDataAndHash(bundle);
    }

    static void clear() {
        AppEventsLogger.getAnalyticsExecutor().execute(new Runnable() { // from class: com.facebook.appevents.UserDataStore.2
            @Override // java.lang.Runnable
            public void run() {
                if (!UserDataStore.initialized.get()) {
                    Log.w(UserDataStore.TAG, "initStore should have been called before calling setUserData");
                    UserDataStore.initAndWait();
                }
                UserDataStore.hashedUserData.clear();
                UserDataStore.sharedPreferences.edit().putString(UserDataStore.USER_DATA_KEY, null).apply();
            }
        });
    }

    static String getHashedUserData() {
        if (!initialized.get()) {
            Log.w(TAG, "initStore should have been called before calling setUserID");
            initAndWait();
        }
        return mapToJsonStr(hashedUserData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void initAndWait() {
        if (initialized.get()) {
            return;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(FacebookSdk.getApplicationContext());
        sharedPreferences = defaultSharedPreferences;
        hashedUserData = new ConcurrentHashMap<>(JsonStrToMap(defaultSharedPreferences.getString(USER_DATA_KEY, "")));
        initialized.set(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateHashUserData(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            if (obj != null) {
                String string = obj.toString();
                if (maybeSHA256Hashed(string)) {
                    hashedUserData.put(str, string.toLowerCase());
                } else {
                    String strEncryptData = encryptData(normalizeData(str, string));
                    if (strEncryptData != null) {
                        hashedUserData.put(str, strEncryptData);
                    }
                }
            }
        }
    }

    private static String encryptData(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(str.getBytes());
                return AppEventUtility.bytesToHex(messageDigest.digest());
            } catch (NoSuchAlgorithmException unused) {
            }
        }
        return null;
    }

    private static String normalizeData(String str, String str2) {
        String lowerCase = str2.trim().toLowerCase();
        if (EMAIL.equals(str)) {
            if (Patterns.EMAIL_ADDRESS.matcher(lowerCase).matches()) {
                return lowerCase;
            }
            Log.e(TAG, "Setting email failure: this is not a valid email address");
            return "";
        }
        if (PHONE.equals(str)) {
            return lowerCase.replaceAll("[^0-9]", "");
        }
        if (!GENDER.equals(str)) {
            return lowerCase;
        }
        String strSubstring = lowerCase.length() > 0 ? lowerCase.substring(0, 1) : "";
        if ("f".equals(strSubstring) || "m".equals(strSubstring)) {
            return strSubstring;
        }
        Log.e(TAG, "Setting gender failure: the supported value for gender is f or m");
        return "";
    }

    private static boolean maybeSHA256Hashed(String str) {
        return str.matches("[A-Fa-f0-9]{64}");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String mapToJsonStr(Map<String, String> map) {
        if (map.isEmpty()) {
            return "";
        }
        try {
            JSONObject jSONObject = new JSONObject();
            for (String str : map.keySet()) {
                jSONObject.put(str, map.get(str));
            }
            return jSONObject.toString();
        } catch (JSONException unused) {
            return "";
        }
    }

    private static Map<String, String> JsonStrToMap(String str) {
        if (str.isEmpty()) {
            return new HashMap();
        }
        try {
            HashMap map = new HashMap();
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                map.put(next, jSONObject.getString(next));
            }
            return map;
        } catch (JSONException unused) {
            return new HashMap();
        }
    }
}
