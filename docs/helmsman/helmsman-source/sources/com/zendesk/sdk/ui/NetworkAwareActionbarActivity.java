package com.zendesk.sdk.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import com.zendesk.sdk.network.NetworkAware;
import com.zendesk.sdk.network.Retryable;
import com.zendesk.sdk.util.NetworkUtils;
import com.zendesk.util.StringUtils;

/* JADX INFO: loaded from: classes2.dex */
public abstract class NetworkAwareActionbarActivity extends AppCompatActivity implements NetworkAware, Retryable {
    private static final String LOG_TAG = "NetworkAwareActionbarActivity";
    private BroadcastReceiver mBroadcastReceiver;
    protected boolean mNetworkAvailable;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private boolean mNetworkPreviouslyUnavailable;
    private View mNoNetworkView;
    private View mRetryView;

    public void onNetworkAvailable() {
        String str = LOG_TAG;
        Logger.d(str, "Network is available.", new Object[0]);
        this.mNetworkAvailable = true;
        if (findNoNetworkView() == null) {
            Logger.w(str, "The no network view is null, nothing to do.", new Object[0]);
            return;
        }
        if (!this.mNetworkPreviouslyUnavailable) {
            Logger.d(str, "Network was not previously unavailable, no need to perform animation", new Object[0]);
            return;
        }
        this.mNetworkPreviouslyUnavailable = false;
        Animation animationLoadAnimation = AnimationUtils.loadAnimation(this, R.anim.info_slide_out);
        animationLoadAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.zendesk.sdk.ui.NetworkAwareActionbarActivity.1
            @Override // com.zendesk.sdk.ui.AnimationListenerAdapter, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                NetworkAwareActionbarActivity.this.findNoNetworkView().setVisibility(8);
            }
        });
        findNoNetworkView().startAnimation(animationLoadAnimation);
    }

    public void onNetworkUnavailable() {
        String str = LOG_TAG;
        Logger.d(str, "Network is unavailable.", new Object[0]);
        this.mNetworkAvailable = false;
        this.mNetworkPreviouslyUnavailable = true;
        if (findRetryView() != null) {
            Logger.d(str, "No network: ensuring that the retry view is gone.", new Object[0]);
            findRetryView().setVisibility(8);
        }
        if (findNoNetworkView() == null) {
            Logger.w(str, "No network: cannot show view because it is missing.", new Object[0]);
            return;
        }
        Animation animationLoadAnimation = AnimationUtils.loadAnimation(this, R.anim.info_slide_in);
        animationLoadAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.zendesk.sdk.ui.NetworkAwareActionbarActivity.2
            @Override // com.zendesk.sdk.ui.AnimationListenerAdapter, android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                NetworkAwareActionbarActivity.this.findNoNetworkView().setVisibility(0);
            }
        });
        findNoNetworkView().startAnimation(animationLoadAnimation);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mNetworkAvailable = NetworkUtils.isConnected(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        registerForNetworkCallbacks();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        unregisterForNetworkCallbacks();
    }

    @Override // com.zendesk.sdk.network.Retryable
    public void onRetryAvailable(String str, View.OnClickListener onClickListener) {
        setRetryViewState(true, str, onClickListener);
    }

    @Override // com.zendesk.sdk.network.Retryable
    public void onRetryUnavailable() {
        setRetryViewState(false, null, null);
    }

    private void registerForNetworkCallbacks() {
        Logger.d(LOG_TAG, "Adding Lollipop network callbacks...", new Object[0]);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        final Handler handler = new Handler(Looper.myLooper());
        this.mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.zendesk.sdk.ui.NetworkAwareActionbarActivity.3
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                handler.post(new Runnable() { // from class: com.zendesk.sdk.ui.NetworkAwareActionbarActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        NetworkAwareActionbarActivity.this.onNetworkAvailable();
                    }
                });
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                handler.post(new Runnable() { // from class: com.zendesk.sdk.ui.NetworkAwareActionbarActivity.3.2
                    @Override // java.lang.Runnable
                    public void run() {
                        NetworkAwareActionbarActivity.this.onNetworkUnavailable();
                    }
                });
            }
        };
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), this.mNetworkCallback);
    }

    private void unregisterForNetworkCallbacks() {
        BroadcastReceiver broadcastReceiver = this.mBroadcastReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if (this.mNetworkCallback != null) {
            ((ConnectivityManager) getSystemService("connectivity")).unregisterNetworkCallback(this.mNetworkCallback);
        }
    }

    private void setRetryViewState(boolean z, String str, View.OnClickListener onClickListener) {
        if (!this.mNetworkAvailable) {
            Logger.e(LOG_TAG, "Network is not available, will not alter retry view state", new Object[0]);
            return;
        }
        if (findRetryView() == null) {
            Logger.e(LOG_TAG, "No retry view was found, will not alter view state", new Object[0]);
            return;
        }
        if (z) {
            View viewFindViewById = findViewById(R.id.retry_view_button);
            if (viewFindViewById != null) {
                viewFindViewById.setOnClickListener(onClickListener);
            }
            TextView textView = (TextView) findViewById(R.id.retry_view_text);
            if (textView != null) {
                textView.setText(str);
            }
            if (StringUtils.isEmpty(str)) {
                Logger.e(LOG_TAG, "Slide in requested but we have no message or action to perform", new Object[0]);
                return;
            }
            Logger.d(LOG_TAG, "Sliding in retry view...", new Object[0]);
            Animation animationLoadAnimation = AnimationUtils.loadAnimation(this, R.anim.info_slide_in);
            animationLoadAnimation.setAnimationListener(new AnimationListenerAdapter() { // from class: com.zendesk.sdk.ui.NetworkAwareActionbarActivity.4
                @Override // com.zendesk.sdk.ui.AnimationListenerAdapter, android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                    NetworkAwareActionbarActivity.this.findRetryView().setVisibility(0);
                }
            });
            findRetryView().startAnimation(animationLoadAnimation);
        }
        if (z || findRetryView().getVisibility() != 0) {
            return;
        }
        Logger.d(LOG_TAG, "Sliding out retry view...", new Object[0]);
        Animation animationLoadAnimation2 = AnimationUtils.loadAnimation(this, R.anim.info_slide_out);
        animationLoadAnimation2.setAnimationListener(new AnimationListenerAdapter() { // from class: com.zendesk.sdk.ui.NetworkAwareActionbarActivity.5
            @Override // com.zendesk.sdk.ui.AnimationListenerAdapter, android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                NetworkAwareActionbarActivity.this.findRetryView().setVisibility(8);
            }
        });
        findRetryView().startAnimation(animationLoadAnimation2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View findRetryView() {
        if (this.mRetryView == null) {
            this.mRetryView = findViewById(R.id.retry_view_container);
        }
        return this.mRetryView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View findNoNetworkView() {
        if (this.mNoNetworkView == null) {
            this.mNoNetworkView = findViewById(R.id.activity_network_no_connectivity);
        }
        return this.mNoNetworkView;
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
                NetworkAwareActionbarActivity.this.onNetworkUnavailable();
            } else {
                NetworkAwareActionbarActivity.this.onNetworkAvailable();
            }
        }
    }
}
