package com.qiniu.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* JADX INFO: loaded from: classes2.dex */
public final class AndroidNetwork {
    public static boolean isNetWorkReady() {
        Context contextApplicationContext = ContextGetter.applicationContext();
        if (contextApplicationContext == null) {
            return true;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) contextApplicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception unused) {
            return true;
        }
    }
}
