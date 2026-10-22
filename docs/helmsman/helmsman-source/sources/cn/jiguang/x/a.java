package cn.jiguang.x;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import cn.jiguang.l.e;
import cn.jiguang.l.h;
import java.lang.reflect.InvocationTargetException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static void a(Context context, int i) {
        boolean z;
        boolean z2;
        String strC = e.c(context);
        new a();
        boolean zA = a(context);
        boolean z3 = true;
        if (!TextUtils.isEmpty(strC)) {
            if (TextUtils.equals("ON", strC)) {
                z2 = true;
                z = false;
            } else if (TextUtils.equals("OFF", strC)) {
                z = false;
                z2 = false;
            } else {
                z = true;
                z2 = false;
            }
            if (z) {
                cn.jiguang.w.a.a("JNotificationState", "notification state do not changed");
                z3 = z;
            } else if (z2 == zA) {
                z3 = false;
            }
        }
        cn.jiguang.w.a.a("JNotificationState", "lastCacheNotificationState:" + strC + ",currentNotificationSate:" + zA + ",isNeedReport:" + z3 + ",triggerScene:" + i);
        if (!z3) {
            cn.jiguang.w.a.a("JNotificationState", "do not need report notification state");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("notification_state", zA);
            jSONObject.put("device_id", cn.jiguang.d.a.e(context));
            jSONObject.put("trigger_scene", i);
            cn.jiguang.d.a.a(context, jSONObject, "android_notification_state");
            h.a(context, (Object) jSONObject);
            e.o(context, zA ? "ON" : "OFF");
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JNotificationState", "report notification state failed, error:" + th.getMessage());
        }
    }

    private static boolean a(Context context) {
        try {
            return ((NotificationManager) context.getSystemService("notification")).areNotificationsEnabled();
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JNotificationState", "invoke areNotificationsEnabled method failed, error:" + th.getMessage());
            return true;
        }
    }

    private static boolean b(Context context) {
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            String packageName = context.getApplicationContext().getPackageName();
            int i = applicationInfo.uid;
            Class<?> cls = Class.forName(AppOpsManager.class.getName());
            return ((Integer) cls.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, Integer.valueOf(((Integer) cls.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(i), packageName)).intValue() == 0;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | RuntimeException | InvocationTargetException unused) {
            return true;
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JNotificationState", "getNotificationStateCommon failed, other error:" + th.getMessage());
            return true;
        }
    }
}
