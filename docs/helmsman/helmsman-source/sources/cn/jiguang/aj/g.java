package cn.jiguang.aj;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.service.AlarmReceiver;

/* JADX INFO: loaded from: classes.dex */
public final class g {
    private static volatile g a;
    private static final Object b = new Object();
    private Context c;
    private cn.jiguang.an.a d = new h(this);
    private long e;

    private g() {
    }

    public static g a() {
        if (a == null) {
            synchronized (b) {
                if (a == null) {
                    a = new g();
                }
            }
        }
        return a;
    }

    private void a(Context context) {
        this.c = context;
        cn.jiguang.an.b.a().a(8000, JConstants.DEFAULT_HEARTBEAT_INTERVAL * 1000, this.d);
    }

    private void b(Context context) {
        this.e = SystemClock.elapsedRealtime();
        if (!((Boolean) cn.jiguang.g.b.a(context, cn.jiguang.g.a.A())).booleanValue()) {
            a.a(context);
            return;
        }
        try {
            ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(PendingIntent.getBroadcast(context, 0, new Intent(context, (Class<?>) AlarmReceiver.class), 0));
        } catch (Throwable unused) {
            cn.jiguang.ad.d.f("AlarmHelper", "Cancel heartbeat alarm failed.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Context context) {
        cn.jiguang.ad.d.b("PeriodWorker", "periodTask...");
        b(context);
        cn.jiguang.a.a.a(context, false, 0L);
        b.a().a(context, 19, 0, "periodTask");
        e.a(context, "periodtask", null);
    }

    public final void a(Context context, boolean z) {
        cn.jiguang.ad.d.d("PeriodWorker", "PeriodWorker resume");
        if (this.e > 0 && SystemClock.elapsedRealtime() > this.e + ((long) ((JConstants.DEFAULT_HEARTBEAT_INTERVAL + 5) * 1000))) {
            cn.jiguang.ad.d.d("PeriodWorker", "schedule time is expired, execute now");
            a(context);
            c(context);
        } else if (!z) {
            cn.jiguang.ad.d.b("PeriodWorker", "need not change period task");
        } else {
            a(context);
            b(context);
        }
    }
}
