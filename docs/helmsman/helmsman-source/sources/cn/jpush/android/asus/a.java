package cn.jpush.android.asus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import cn.jpush.android.cache.Key;
import cn.jpush.android.cache.Sp;
import cn.jpush.android.e.c;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static boolean c;
    private static boolean d;
    public static final String a = "asus".toLowerCase();
    public static String b = "";
    private static String e = null;

    private static ActivityInfo a(Context context, String str, Class<? extends BroadcastReceiver> cls) {
        if (context == null || TextUtils.isEmpty(str) || cls == null) {
            Logger.w("AsusPushHelper", "Action - hasComponent, invalid param, context:" + context + ",packageName:" + str + ",cls:" + cls);
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 2);
            if (packageInfo.receivers != null && packageInfo.receivers.length != 0) {
                for (ActivityInfo activityInfo : packageInfo.receivers) {
                    try {
                        if (cls.isAssignableFrom(Class.forName(activityInfo.name)) && activityInfo.enabled) {
                            return activityInfo;
                        }
                    } catch (Throwable unused) {
                    }
                }
                return null;
            }
            return null;
        } catch (Throwable th) {
            Logger.ww("AsusPushHelper", "getReceiver error:" + th);
        }
    }

    public static boolean a(Context context) {
        b(context);
        return c;
    }

    public static synchronized void b(Context context) {
        if (d) {
            return;
        }
        if (context == null) {
            Logger.ww("AsusPushHelper", "context is null");
            return;
        }
        if (i(context) && cn.jpush.android.c.b.a(context)) {
            c = true;
        }
        Logger.d("AsusPushHelper", (c ? "support " : "not support ") + a);
        d = true;
    }

    public static void c(final Context context) {
        try {
            b(context);
            if (c) {
                cn.jpush.android.c.b.a(context, e(context), new c() { // from class: cn.jpush.android.asus.a.1
                    @Override // cn.jpush.android.e.a
                    public void a(cn.jpush.android.e.b bVar) {
                        try {
                            Logger.dd("AsusPushHelper", "tokenResult:" + bVar);
                            cn.jpush.android.s.c.a().a(context, (byte) 6, bVar.a());
                        } catch (Throwable unused) {
                        }
                    }
                });
            }
        } catch (Throwable th) {
            Logger.w("AsusPushHelper", "register error:" + th.getMessage());
        }
    }

    public static byte d(Context context) {
        return (byte) 6;
    }

    public static String e(Context context) {
        return !TextUtils.isEmpty(b) ? b : JCoreHelper.getAppKey(context);
    }

    public static String f(Context context) {
        return "";
    }

    public static String g(Context context) {
        return cn.jpush.android.c.b.d(context);
    }

    public static boolean h(Context context) {
        String str;
        String strE = e(context);
        if (TextUtils.isEmpty(strE)) {
            str = a + " appkey or appid is empty,need not clear plugin rid";
        } else {
            String appKey = JCoreHelper.getAppKey(context);
            if (!TextUtils.isEmpty(appKey)) {
                String str2 = (String) Sp.get(context, Key.ThirdPush_ClearFlag());
                String md5 = b.toMD5(strE + appKey + JCoreHelper.getJCoreSDKVersionInt() + JCoreHelper.getJPushSDKVersionInt());
                if (TextUtils.isEmpty(str2)) {
                    Sp.set(context, Key.ThirdPush_ClearFlag().set(md5));
                    return true;
                }
                if (TextUtils.isEmpty(md5)) {
                    return true;
                }
                if (TextUtils.equals(str2, md5)) {
                    return false;
                }
                Logger.d("AsusPushHelper", "local set changed,re bind token");
                Sp.set(context, Key.ThirdPush_ClearFlag().set(md5));
                return true;
            }
            str = "jpush appkey is empty,need not clear plugin rid";
        }
        Logger.ii("AsusPushHelper", str);
        return false;
    }

    private static boolean i(Context context) {
        ActivityInfo activityInfoA = a(context, context.getPackageName(), AsusPushMessageReceiver.class);
        if (activityInfoA != null) {
            Logger.d("AsusPushHelper", "found receiver:" + activityInfoA.name);
            return true;
        }
        Logger.ww("AsusPushHelper", "AndroidManifest.xml missing receiver extends asus's AsusPushMessageReceiver");
        return false;
    }
}
