package cn.jiguang.a;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.aj.e;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.service.DataShare;
import cn.jpush.android.service.JCommonService;
import cn.jpush.android.service.PushReceiver;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static String a = ".permission.JPUSH_MESSAGE";
    public static String b = "2.3.0";
    public static int c = 230;
    public static String d = "";
    private static Boolean e;
    private static Boolean f;
    private static ServiceConnection g = new b();

    public static void a(Context context, String str, Bundle bundle) {
        cn.jiguang.aq.a.a("SDK_INIT", new c(context, false, str, bundle));
    }

    public static void a(Context context, boolean z, long j) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("force", z);
            bundle.putLong("delay_time", j);
            a(context, "tcp_a2", bundle);
        } catch (Throwable th) {
            d.g("JCoreGobal", "sendHeartBeat error:" + th);
        }
    }

    public static synchronized boolean a(Context context) {
        Boolean bool = e;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (context == null) {
            d.i("JCoreGobal", "init failed,context is null");
            return false;
        }
        d.e("JCoreGobal", "action:init jcore,version:" + b + ",build id:72,l:" + JConstants.SDK_VERSION_INT);
        d.b("JCoreGobal", "build type:google_play");
        JConstants.mApplicationContext = context.getApplicationContext();
        Context applicationContext = context.getApplicationContext();
        cn.jiguang.ac.b.a();
        String strB = cn.jiguang.ac.c.b(applicationContext);
        if ((cn.jiguang.ac.b.a().c() || cn.jiguang.ac.b.a().b()) && TextUtils.isEmpty(strB)) {
            e = false;
            d.i("JCoreGobal", "AndroidManifest.xml missing required service:" + JCommonService.class.getCanonicalName() + ",please custom one service and extends JCommonService");
            return false;
        }
        cn.jiguang.b.a.a().b();
        String strB2 = cn.jiguang.ac.c.b(applicationContext);
        if (TextUtils.isEmpty(strB2)) {
            d.d("JCoreGobal", "not found commonServiceClass（JCommonService）");
        } else if (DataShare.isBinding()) {
            d.b("JCoreGobal", "is binding service");
        } else {
            try {
                Intent intent = new Intent();
                intent.setClass(applicationContext, Class.forName(strB2));
                if (applicationContext.bindService(intent, g, 1)) {
                    d.a("JCoreGobal", "Remote Service on binding...");
                    DataShare.setBinding();
                } else {
                    d.a("JCoreGobal", "Remote Service bind failed");
                }
            } catch (SecurityException unused) {
                d.g("JCoreGobal", "Remote Service bind failed caused by SecurityException!");
            } catch (Throwable th) {
                d.g("JCoreGobal", "Remote Service bind failed :" + th);
            }
        }
        Boolean bool2 = true;
        e = bool2;
        return bool2.booleanValue();
    }

    public static void b(Context context, String str, Bundle bundle) {
        cn.jiguang.aq.a.a("SDK_SERVICE_INIT", new c(context, true, str, bundle));
    }

    public static synchronized boolean b(Context context) {
        Boolean bool = f;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (context == null) {
            d.i("JCoreGobal", "init failed,context is null");
            return false;
        }
        d.b("JCoreGobal", "serviceInit...");
        JConstants.mApplicationContext = context.getApplicationContext();
        Context applicationContext = context.getApplicationContext();
        if (!cn.jiguang.ac.a.e(applicationContext)) {
            return false;
        }
        if (!cn.jiguang.ac.a.f(applicationContext)) {
            f = false;
            return false;
        }
        cn.jiguang.ac.b.a();
        try {
            JConstants.isCallInit.set(true);
        } catch (Throwable unused) {
        }
        try {
            IntentFilter intentFilter = new IntentFilter();
            PushReceiver pushReceiver = new PushReceiver();
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            applicationContext.registerReceiver(pushReceiver, intentFilter, applicationContext.getPackageName() + a, null);
            cn.jiguang.f.a.a(applicationContext, (Class<?>) PushReceiver.class);
        } catch (Throwable th) {
            d.h("JCoreGobal", "registerPushReceiver fail:" + th);
        }
        f = true;
        cn.jiguang.b.a.a();
        cn.jiguang.b.a.c(applicationContext);
        e.a(applicationContext, "service_create", null);
        return f.booleanValue();
    }
}
