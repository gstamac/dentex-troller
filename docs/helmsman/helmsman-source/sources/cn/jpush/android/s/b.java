package cn.jpush.android.s;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.view.PointerIconCompat;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.d.d;
import cn.jpush.android.helper.Logger;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static d a(Context context, String str, String str2) {
        return d.a(context, str, str2);
    }

    private static void a(Context context, d dVar) {
        Intent intentB = 2 == dVar.S ? cn.jpush.android.o.b.b(context, JPushInterface.ACTION_NOTIFICATION_OPENED, dVar) : cn.jpush.android.o.b.a(context, JPushInterface.ACTION_NOTIFICATION_OPENED, dVar);
        if (intentB != null) {
            intentB.addFlags(268435456);
            context.getApplicationContext().startActivity(intentB);
        }
    }

    private static void a(Context context, d dVar, String str, int i) {
        Logger.v("PluginPlatformsNotificationHelper", "Action - onNotificationMessageArrived");
        dVar.d = i;
        cn.jpush.android.o.b.a(context, "cn.jpush.android.intent.NOTIFICATION_ARRIVED", dVar, (Intent) null);
        cn.jpush.android.helper.c.a(dVar.c, str, dVar.X, PointerIconCompat.TYPE_ZOOM_IN, context);
    }

    public static void a(Context context, String str, String str2, int i, byte b, boolean z) {
        String str3;
        if (context == null) {
            str3 = "context was null";
        } else if (TextUtils.isEmpty(str)) {
            str3 = "content was null";
        } else {
            Logger.v("PluginPlatformsNotificationHelper", "message content:" + str);
            d dVarA = a(context, str, str2);
            Logger.v("PluginPlatformsNotificationHelper", "entity:" + dVarA);
            if (dVarA == null) {
                str3 = "entity was null";
            } else {
                if (!TextUtils.isEmpty(dVarA.c)) {
                    dVarA.X = b;
                    if (z) {
                        b(context, dVarA, str2, i);
                        return;
                    } else {
                        a(context, dVarA, str2, i);
                        return;
                    }
                }
                str3 = "message id was empty";
            }
        }
        Logger.ww("PluginPlatformsNotificationHelper", str3);
    }

    private static void b(Context context, d dVar, String str, int i) {
        Logger.v("PluginPlatformsNotificationHelper", "Action - onNotificationMessageClick");
        dVar.d = i;
        if (dVar.N) {
            a(context, dVar);
        } else {
            cn.jpush.android.o.b.a(context, JPushInterface.ACTION_NOTIFICATION_OPENED, dVar, (Intent) null);
            cn.jpush.android.helper.c.a(dVar.c, str, dVar.X, 1000, context);
        }
    }
}
