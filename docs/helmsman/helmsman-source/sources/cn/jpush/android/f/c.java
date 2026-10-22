package cn.jpush.android.f;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.text.TextUtils;
import cn.jiguang.push.asus.PushMessageReceiver;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static Context a;
    public static String b;
    private static cn.jpush.android.g.a g;
    private static String h;
    private static Boolean i;
    private static AtomicLong f = new AtomicLong(0);
    public static String c = null;
    public static boolean d = false;
    public static String e = "com.asus.as";
    private static ExecutorService j = Executors.newSingleThreadExecutor();

    public static long a() {
        long jIncrementAndGet = f.incrementAndGet();
        if (jIncrementAndGet == Long.MAX_VALUE) {
            f.set(0L);
        }
        return jIncrementAndGet;
    }

    public static void a(Context context, String str, long j2, JSONObject jSONObject) {
        if (c(context)) {
            j.execute(new d(str, j2, jSONObject));
        }
    }

    static /* synthetic */ void a(String str, long j2, JSONObject jSONObject) {
        cn.jpush.android.g.a aVar = g;
        if (!(aVar != null ? aVar.b() : false)) {
            cn.jpush.android.i.b.a("Gobal", "connect has disconnect, will reconnect.");
            cn.jpush.android.g.a aVar2 = g;
            if (aVar2 != null) {
                aVar2.a();
            }
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        try {
            jSONObject.put("JPUSH_PACKAGE", c);
            if (j2 <= 0) {
                j2 = a();
            }
            jSONObject.put("JPUSH_RID", j2);
            String string = jSONObject.toString();
            cn.jpush.android.i.b.c("Gobal", "cmd:" + str + ",data:" + string);
            cn.jpush.android.g.a aVar3 = g;
            if (aVar3 != null) {
                aVar3.a(str, string);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static boolean a(Context context) {
        return cn.jpush.android.g.c.a(context, e);
    }

    public static String b(Context context) {
        if (!TextUtils.isEmpty(h)) {
            return h;
        }
        try {
            Intent intent = new Intent();
            intent.setAction("com.ups.push.PUSH_RESPONSE");
            intent.setPackage(context.getPackageName());
            ComponentInfo componentInfoA = cn.jpush.android.j.a.a(context, context.getPackageName(), PushMessageReceiver.class);
            if (componentInfoA != null) {
                h = componentInfoA.name;
                cn.jpush.android.i.a.a("ASUS_PUSH", "Gobal", 4, "found userServiceClass :" + h + " by getComponentInfo");
            }
        } catch (Throwable th) {
            cn.jpush.android.i.b.e("Gobal", "not found PushMessageReceiver :" + th.getMessage());
        }
        return h;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0039  */
    /* JADX WARN: Code duplicated, block: B:23:0x0049  */
    /* JADX WARN: Code duplicated, block: B:26:0x0066 A[Catch: all -> 0x0077, TRY_LEAVE, TryCatch #0 {all -> 0x0077, blocks: (B:24:0x0058, B:26:0x0066), top: B:31:0x0058 }] */
    private static boolean c(Context context) {
        String str;
        Boolean bool;
        NotificationManager notificationManager;
        Boolean bool2 = i;
        if (bool2 != null) {
            return bool2.booleanValue();
        }
        if (context == null) {
            cn.jpush.android.i.b.g("Gobal", "context is null");
            return false;
        }
        cn.jpush.android.g.d dVar = null;
        if (cn.jpush.android.j.a.a(context)) {
            if (cn.jpush.android.g.c.a(context, e)) {
                dVar = new cn.jpush.android.g.d();
            } else {
                str = "cn.jpush.android.service.PushManagerService not exist in system, give up create connection.";
            }
            g = dVar;
            if (dVar == null) {
                cn.jpush.android.i.b.g("Gobal", "unsupport this device.");
                bool = false;
            } else {
                Context applicationContext = context.getApplicationContext();
                a = applicationContext;
                c = applicationContext.getPackageName();
                cn.jpush.android.a.b.a(context);
                try {
                    notificationManager = (NotificationManager) context.getSystemService("notification");
                    if (notificationManager.getNotificationChannel("asus_default_channel_id") == null) {
                        cn.jpush.android.i.b.b("Gobal", "create default channel:默认");
                        notificationManager.createNotificationChannel(new NotificationChannel("asus_default_channel_id", "默认", 3));
                    }
                } catch (Throwable th) {
                    cn.jpush.android.i.b.e("Gobal", "[initNotificationChannel] failed:" + th.getMessage());
                }
                bool = true;
            }
            i = bool;
            return bool.booleanValue();
        }
        str = "Manufacturer not match, give up create connection.";
        cn.jpush.android.i.b.f("ConnectionFactory", str);
        g = dVar;
        if (dVar == null) {
            cn.jpush.android.i.b.g("Gobal", "unsupport this device.");
            bool = false;
        } else {
            Context applicationContext2 = context.getApplicationContext();
            a = applicationContext2;
            c = applicationContext2.getPackageName();
            cn.jpush.android.a.b.a(context);
            notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager.getNotificationChannel("asus_default_channel_id") == null) {
                cn.jpush.android.i.b.b("Gobal", "create default channel:默认");
                notificationManager.createNotificationChannel(new NotificationChannel("asus_default_channel_id", "默认", 3));
            }
            bool = true;
        }
        i = bool;
        return bool.booleanValue();
    }
}
