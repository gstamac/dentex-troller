package im.helmsman.helmsmanandroid.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes2.dex */
public class WifiConnectionUtil {
    private static final int WIFI_CONNECT_TIMEOUT = 20;
    private Condition mCondition;
    private Context mContext;
    private Lock mLock;
    private WifiConnectListener mWifiConnectListener;
    private WiFiConncetReceiver mWifiConnectReceiver;
    private WifiManager mWifiManager;
    private boolean mIsConnnected = false;
    private int mNetworkID = -1;
    private String ssid = "";
    private int connectTime = 3;

    public enum SecurityMode {
        OPEN,
        WEP,
        WPA,
        WPA2
    }

    public interface WifiConnectListener {
        void OnWifiConnectCompleted(boolean z);
    }

    public WifiConnectionUtil(Context context, WifiManager wifiManager, WifiConnectListener wifiConnectListener) {
        this.mContext = context;
        ReentrantLock reentrantLock = new ReentrantLock();
        this.mLock = reentrantLock;
        this.mCondition = reentrantLock.newCondition();
        this.mWifiConnectReceiver = new WiFiConncetReceiver();
        this.mWifiConnectListener = wifiConnectListener;
        this.mWifiManager = wifiManager;
    }

    public void connect(final String str, final String str2, final SecurityMode securityMode) {
        String str3 = "\"" + str + "\"";
        this.ssid = str3;
        if (str3.equals(this.mWifiManager.getConnectionInfo().getSSID())) {
            this.mWifiConnectListener.OnWifiConnectCompleted(true);
        } else {
            this.connectTime = 3;
            new Thread(new Runnable() { // from class: im.helmsman.helmsmanandroid.utils.WifiConnectionUtil.1
                @Override // java.lang.Runnable
                public void run() {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.wifi.STATE_CHANGE");
                    WifiConnectionUtil.this.mContext.registerReceiver(WifiConnectionUtil.this.mWifiConnectReceiver, intentFilter);
                    if (!WifiConnectionUtil.this.onConnect(str, str2, securityMode)) {
                        WifiConnectionUtil.this.mWifiConnectListener.OnWifiConnectCompleted(false);
                    } else {
                        WifiConnectionUtil.this.mWifiConnectListener.OnWifiConnectCompleted(true);
                    }
                    WifiConnectionUtil.this.mContext.unregisterReceiver(WifiConnectionUtil.this.mWifiConnectReceiver);
                }
            }).start();
        }
    }

    protected boolean onConnect(String str, String str2, SecurityMode securityMode) {
        int i = this.connectTime;
        if (i <= 0) {
            return false;
        }
        this.connectTime = i - 1;
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + str + "\"";
        if (str2 != null && !"".equals(str2)) {
            if (securityMode == SecurityMode.WEP) {
                wifiConfiguration.wepKeys[0] = "\"" + str2 + "\"";
                wifiConfiguration.wepTxKeyIndex = 0;
            } else {
                wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
            }
        }
        wifiConfiguration.status = 2;
        this.mNetworkID = this.mWifiManager.addNetwork(wifiConfiguration);
        this.mLock.lock();
        this.mIsConnnected = false;
        if (!this.mWifiManager.enableNetwork(this.mNetworkID, true)) {
            this.mLock.unlock();
            return false;
        }
        try {
            this.mCondition.await(20L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.mLock.unlock();
        for (int i2 = 0; i2 < 90; i2++) {
            NetworkInfo networkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getNetworkInfo(1);
            this.mWifiManager.getConnectionInfo().getSSID();
            networkInfo.getDetailedState().name();
            if (networkInfo.isConnected()) {
                if (wifiConfiguration.SSID.equals(this.mWifiManager.getConnectionInfo().getSSID())) {
                    return true;
                }
                return onConnect(str, "0123456789", SecurityMode.WPA2);
            }
            this.mWifiManager.getConnectionInfo().getSSID();
            try {
                Thread.sleep(350L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    protected class WiFiConncetReceiver extends BroadcastReceiver {
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
        }

        protected WiFiConncetReceiver() {
        }
    }
}
