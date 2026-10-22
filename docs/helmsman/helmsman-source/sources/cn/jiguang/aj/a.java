package cn.jiguang.aj;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.service.AlarmReceiver;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static void a(Context context) {
        long j = JConstants.DEFAULT_ALARM_DELAY * 1000;
        long jCurrentTimeMillis = System.currentTimeMillis() + j;
        cn.jiguang.ad.d.d("AlarmHelper", "Reset heartbeat alarm, wait " + j + "ms.");
        try {
            ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setWindow(0, jCurrentTimeMillis, 0L, PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) AlarmReceiver.class), 0));
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("AlarmHelper", "can't trigger alarm cause by exception:" + th);
        }
    }
}
