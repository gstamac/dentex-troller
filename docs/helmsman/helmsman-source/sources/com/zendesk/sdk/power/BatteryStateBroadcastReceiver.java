package com.zendesk.sdk.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zendesk.logger.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class BatteryStateBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "BatteryStateBroadcastReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String str = LOG_TAG;
        Logger.d(str, "onReceive entry()", new Object[0]);
        if (intent == null) {
            Logger.w(str, "Cannot determine battery state", new Object[0]);
        } else {
            Logger.d(str, "Received a battery status update with action: " + intent.getAction(), new Object[0]);
            if ("android.intent.action.BATTERY_OKAY".equals(intent.getAction())) {
                Logger.i(str, "Battery state is OK.", new Object[0]);
                PowerConfig.getInstance(context).setBatteryOk(true);
            } else if ("android.intent.action.BATTERY_LOW".equals(intent.getAction())) {
                Logger.i(str, "Battery is low.", new Object[0]);
                PowerConfig.getInstance(context).setBatteryOk(false);
            }
        }
        Logger.d(str, "onReceive exit()", new Object[0]);
    }
}
