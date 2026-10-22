package com.zendesk.sdk.network.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.network.NetworkAware;
import com.zendesk.sdk.network.NetworkInfoProvider;
import com.zendesk.sdk.network.RetryAction;
import com.zendesk.sdk.util.NetworkUtils;
import com.zendesk.util.CollectionUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
class ZendeskNetworkInfoProvider implements NetworkInfoProvider, NetworkAware {
    private static final String LOG_TAG = "ZendeskNetworkInfoProvider";
    private BroadcastReceiver broadcastReceiver;
    private Context context;
    private boolean networkAvailable;
    private ConnectivityManager.NetworkCallback networkCallback;
    private Map<Integer, WeakReference<NetworkAware>> listeners = new HashMap();
    private Map<Integer, WeakReference<RetryAction>> retryActions = new HashMap();

    ZendeskNetworkInfoProvider(Context context) {
        this.context = context;
        this.networkAvailable = NetworkUtils.isConnected(context);
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void register() {
        if (this.context == null) {
            Logger.w(LOG_TAG, "Cannot register NetworkInformer, supplied Context is null.", new Object[0]);
        } else {
            registerForNetworkCallbacks();
        }
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void unregister() {
        if (this.context == null) {
            Logger.w(LOG_TAG, "Cannot unregister NetworkInformer, supplied Context is null.", new Object[0]);
        } else {
            unregisterForNetworkCallbacks();
        }
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public boolean isNetworkAvailable() {
        return this.networkAvailable;
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void addNetworkAwareListener(Integer num, NetworkAware networkAware) {
        if (num == null || networkAware == null) {
            return;
        }
        this.listeners.put(num, new WeakReference<>(networkAware));
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void removeNetworkAwareListener(Integer num) {
        this.listeners.remove(num);
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void clearNetworkAwareListeners() {
        this.listeners.clear();
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void addRetryAction(Integer num, RetryAction retryAction) {
        if (num == null || retryAction == null) {
            return;
        }
        this.retryActions.put(num, new WeakReference<>(retryAction));
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void removeRetryAction(Integer num) {
        this.retryActions.remove(num);
    }

    @Override // com.zendesk.sdk.network.NetworkInfoProvider
    public void clearRetryActions() {
        this.retryActions.clear();
    }

    @Override // com.zendesk.sdk.network.NetworkAware
    public void onNetworkAvailable() {
        Map mapCopyOf = CollectionUtils.copyOf(this.listeners);
        Map mapCopyOf2 = CollectionUtils.copyOf(this.retryActions);
        this.networkAvailable = true;
        for (WeakReference weakReference : mapCopyOf.values()) {
            if (weakReference.get() != null) {
                ((NetworkAware) weakReference.get()).onNetworkAvailable();
            }
        }
        for (WeakReference weakReference2 : mapCopyOf2.values()) {
            if (weakReference2.get() != null) {
                ((RetryAction) weakReference2.get()).onRetry();
            }
        }
        this.retryActions.clear();
    }

    @Override // com.zendesk.sdk.network.NetworkAware
    public void onNetworkUnavailable() {
        Map mapCopyOf = CollectionUtils.copyOf(this.listeners);
        this.networkAvailable = false;
        for (WeakReference weakReference : mapCopyOf.values()) {
            if (weakReference.get() != null) {
                ((NetworkAware) weakReference.get()).onNetworkUnavailable();
            }
        }
    }

    private void registerForNetworkCallbacks() {
        Logger.d(LOG_TAG, "Adding Lollipop network callbacks...", new Object[0]);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
        final Handler handler = new Handler(Looper.getMainLooper());
        this.networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.zendesk.sdk.network.impl.ZendeskNetworkInfoProvider.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                handler.post(new Runnable() { // from class: com.zendesk.sdk.network.impl.ZendeskNetworkInfoProvider.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ZendeskNetworkInfoProvider.this.onNetworkAvailable();
                    }
                });
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                handler.post(new Runnable() { // from class: com.zendesk.sdk.network.impl.ZendeskNetworkInfoProvider.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ZendeskNetworkInfoProvider.this.onNetworkUnavailable();
                    }
                });
            }
        };
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), this.networkCallback);
    }

    private void unregisterForNetworkCallbacks() {
        BroadcastReceiver broadcastReceiver = this.broadcastReceiver;
        if (broadcastReceiver != null) {
            this.context.unregisterReceiver(broadcastReceiver);
        }
        if (this.networkCallback != null) {
            ((ConnectivityManager) this.context.getSystemService("connectivity")).unregisterNetworkCallback(this.networkCallback);
        }
    }

    class NetworkAvailabilityBroadcastReceiver extends BroadcastReceiver {
        private final String LOG_TAG = NetworkAvailabilityBroadcastReceiver.class.getSimpleName();

        NetworkAvailabilityBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || !"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                Logger.w(this.LOG_TAG, "onReceive: intent was null or getAction() was mismatched", new Object[0]);
            } else if (intent.getBooleanExtra("noConnectivity", false)) {
                ZendeskNetworkInfoProvider.this.onNetworkUnavailable();
            } else {
                ZendeskNetworkInfoProvider.this.onNetworkAvailable();
            }
        }
    }
}
