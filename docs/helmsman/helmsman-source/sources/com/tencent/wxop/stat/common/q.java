package com.tencent.wxop.stat.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.facebook.places.model.PlaceFields;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class q {
    private static String a = "";

    public static String a(Context context) {
        try {
            if (a(context, "android.permission.READ_PHONE_STATE")) {
                String deviceId = ((TelephonyManager) context.getSystemService(PlaceFields.PHONE)).getDeviceId();
                if (deviceId != null) {
                    return deviceId;
                }
            } else {
                Log.e(StatConstants.LOG_TAG, "Could not get permission of android.permission.READ_PHONE_STATE");
            }
            return null;
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "get device id error", th);
            return null;
        }
    }

    public static String a(String str) {
        if (str == null) {
            return null;
        }
        try {
            return new String(f.b(g.a(str.getBytes("UTF-8"), 0)), "UTF-8");
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "decode error", th);
            return str;
        }
    }

    public static JSONArray a(Context context, int i) {
        List<ScanResult> scanResults;
        try {
            if (!a(context, "android.permission.INTERNET") || !a(context, "android.permission.ACCESS_NETWORK_STATE")) {
                Log.e(StatConstants.LOG_TAG, "can not get the permisson of android.permission.INTERNET");
                return null;
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager == null || (scanResults = wifiManager.getScanResults()) == null || scanResults.size() <= 0) {
                return null;
            }
            Collections.sort(scanResults, new r());
            JSONArray jSONArray = new JSONArray();
            for (int i2 = 0; i2 < scanResults.size() && i2 < i; i2++) {
                ScanResult scanResult = scanResults.get(i2);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("bs", scanResult.BSSID);
                jSONObject.put("ss", scanResult.SSID);
                jSONArray.put(jSONObject);
            }
            return jSONArray;
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "isWifiNet error", th);
            return null;
        }
    }

    public static void a(JSONObject jSONObject, String str, String str2) {
        if (str2 != null) {
            try {
                if (str2.length() > 0) {
                    jSONObject.put(str, str2);
                }
            } catch (Throwable th) {
                Log.e(StatConstants.LOG_TAG, "jsonPut error", th);
            }
        }
    }

    public static boolean a(Context context, String str) {
        try {
            return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "checkPermission error", th);
            return false;
        }
    }

    public static String b(Context context) {
        if (!a(context, "android.permission.ACCESS_WIFI_STATE")) {
            Log.e(StatConstants.LOG_TAG, "Could not get permission of android.permission.ACCESS_WIFI_STATE");
            return "";
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            return wifiManager == null ? "" : wifiManager.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            Log.e(StatConstants.LOG_TAG, "get wifi address error", e);
            return "";
        }
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        try {
            return new String(g.b(f.a(str.getBytes("UTF-8")), 0), "UTF-8");
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "encode error", th);
            return str;
        }
    }

    public static WifiInfo c(Context context) {
        WifiManager wifiManager;
        if (!a(context, "android.permission.ACCESS_WIFI_STATE") || (wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi")) == null) {
            return null;
        }
        return wifiManager.getConnectionInfo();
    }

    public static String d(Context context) {
        try {
            WifiInfo wifiInfoC = c(context);
            if (wifiInfoC != null) {
                return wifiInfoC.getBSSID();
            }
            return null;
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "encode error", th);
            return null;
        }
    }

    public static String e(Context context) {
        try {
            WifiInfo wifiInfoC = c(context);
            if (wifiInfoC != null) {
                return wifiInfoC.getSSID();
            }
            return null;
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "encode error", th);
            return null;
        }
    }

    public static boolean f(Context context) {
        try {
            if (a(context, "android.permission.INTERNET") && a(context, "android.permission.ACCESS_NETWORK_STATE")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                if (connectivityManager != null) {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                        return true;
                    }
                    Log.w(StatConstants.LOG_TAG, "Network error");
                    return false;
                }
            } else {
                Log.e(StatConstants.LOG_TAG, "can not get the permisson of android.permission.INTERNET");
            }
        } catch (Throwable th) {
            Log.e(StatConstants.LOG_TAG, "isNetworkAvailable error", th);
        }
        return false;
    }
}
