package cn.jpush.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class WakedResultReceiver extends BroadcastReceiver {
    public static final String CONTEXT_KEY = "1";
    public static final String WAKE_TYPE_KEY = "2";

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
    }

    public void onWake(int i) {
    }

    public void onWake(Context context, int i) {
    }

    public final void onWakeMap(Map map) {
        try {
            Object obj = map.get("2");
            int iIntValue = obj != null ? ((Integer) obj).intValue() : -1;
            onWake(iIntValue);
            Object obj2 = map.get("1");
            onWake(obj2 != null ? (Context) obj2 : null, iIntValue);
        } catch (Throwable unused) {
        }
    }
}
