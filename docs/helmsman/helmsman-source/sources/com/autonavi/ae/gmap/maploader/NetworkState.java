package com.autonavi.ae.gmap.maploader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.autonavi.ae.gmap.callback.GLMapCoreCallback;

/* JADX INFO: loaded from: classes.dex */
public class NetworkState {
    private static volatile NetworkState mNetworkState;
    private volatile boolean isConnected;
    private boolean mNetworkInitFlag;
    private Object mNetworkInitLock = new Object();
    private GLMapCoreCallback mNetWorkChanggeListener = null;

    public static NetworkState getInstance() {
        if (mNetworkState == null) {
            synchronized (NetworkState.class) {
                if (mNetworkState == null) {
                    mNetworkState = new NetworkState();
                }
            }
        }
        return mNetworkState;
    }

    public boolean isInternetConnected(Context context) {
        initNetworkReceiver(context);
        return this.isConnected;
    }

    private void initNetworkReceiver(Context context) {
        if (this.mNetworkInitFlag) {
            return;
        }
        synchronized (this.mNetworkInitLock) {
            if (!this.mNetworkInitFlag) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                context.registerReceiver(new BroadcastReceiver() { // from class: com.autonavi.ae.gmap.maploader.NetworkState.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context2, Intent intent) {
                        NetworkState.this.resetNetwork(context2);
                    }
                }, intentFilter);
                resetNetwork(context);
                this.mNetworkInitFlag = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetNetwork(Context context) {
        NetworkInfo activeNetworkInfo;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Throwable unused) {
            activeNetworkInfo = null;
        }
        if (activeNetworkInfo != null) {
            boolean z = NetworkInfo.State.CONNECTED == activeNetworkInfo.getState();
            if (this.mNetWorkChanggeListener != null && z && !this.isConnected) {
                this.mNetWorkChanggeListener.resetRenderTimeLong();
            }
            if (z != this.isConnected) {
                this.isConnected = z;
                return;
            }
            return;
        }
        if (this.isConnected) {
            this.isConnected = false;
        }
    }

    public void setNetWorkChangeListener(GLMapCoreCallback gLMapCoreCallback) {
        this.mNetWorkChanggeListener = gLMapCoreCallback;
    }
}
