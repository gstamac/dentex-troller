package com.zendesk.sdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.zendesk.logger.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkUtils {
    private static final String LOG_TAG = "NetworkUtils";

    private NetworkUtils() {
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static boolean isMobile(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 0;
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        if (context == null) {
            Logger.w(LOG_TAG, "Context is null. Cannot get ConnectivityManager", new Object[0]);
            return null;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            Logger.w(LOG_TAG, "Connectivity manager is null", new Object[0]);
        }
        return connectivityManager;
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if (connectivityManager != null && context != null) {
            if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
                Logger.i(LOG_TAG, "Getting active network information", new Object[0]);
                return connectivityManager.getActiveNetworkInfo();
            }
            Logger.w(LOG_TAG, "Will not return if network is available because we do not have the permission to do so: ACCESS_NETWORK_STATE", new Object[0]);
        }
        return null;
    }
}
