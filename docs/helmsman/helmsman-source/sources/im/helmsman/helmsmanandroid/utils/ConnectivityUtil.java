package im.helmsman.helmsmanandroid.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import im.helmsman.helmsmanandroid.MyApplication;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectivityUtil {
    public static boolean isConnect() {
        NetworkInfo[] allNetworkInfo = ((ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService("connectivity")).getAllNetworkInfo();
        if (allNetworkInfo != null) {
            for (int i = 0; i < 2; i++) {
                System.out.println(i + "===状态===" + allNetworkInfo[i].getState());
                System.out.println(i + "===类型===" + allNetworkInfo[i].getTypeName());
                if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
