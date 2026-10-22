package cn.jpush.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.jiguang.ad.d;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            d.b(TAG, "onReceive:" + intent.getAction());
            if (!JConstants.isCallInit.get()) {
                d.c(TAG, "please call init");
            } else {
                JCoreManager.onEvent(context.getApplicationContext(), intent.getStringExtra("sdktype"), 31, null, null, intent);
            }
        } catch (Throwable unused) {
        }
    }
}
