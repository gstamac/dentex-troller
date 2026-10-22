package cn.jpush.android.j;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;
import cn.jpush.android.i.b;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static String a = "";
    private static String b = "";
    private static final String c = "".toLowerCase();

    public static ComponentInfo a(Context context, String str, Class<?> cls) {
        int i;
        ComponentInfo[] componentInfoArr;
        if (context == null || TextUtils.isEmpty(str)) {
            b.d("AndroidUtils", "Action - hasComponent, invalide param, context:" + context + ",packageName:" + str + ",cls:" + cls);
            return null;
        }
        try {
            if (Service.class.isAssignableFrom(cls)) {
                i = 4;
            } else if (BroadcastReceiver.class.isAssignableFrom(cls)) {
                i = 2;
            } else if (Activity.class.isAssignableFrom(cls)) {
                i = 1;
            } else {
                i = ContentProvider.class.isAssignableFrom(cls) ? 8 : 0;
            }
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, i);
            if (i == 1) {
                componentInfoArr = packageInfo.activities;
            } else if (i == 2) {
                componentInfoArr = packageInfo.receivers;
            } else if (i != 4) {
                componentInfoArr = i != 8 ? null : packageInfo.providers;
            } else {
                componentInfoArr = packageInfo.services;
            }
            if (componentInfoArr == null) {
                return null;
            }
            for (ComponentInfo componentInfo : componentInfoArr) {
                try {
                    if (cls.isAssignableFrom(Class.forName(componentInfo.name))) {
                        return componentInfo;
                    }
                } catch (Throwable th) {
                    b.d("AndroidUtils", "check component e:" + th);
                }
            }
        } catch (Throwable th2) {
            b.e("AndroidUtils", "hasComponent error:" + th2);
        }
        return null;
    }

    public static boolean a(Context context) {
        String str;
        String str2;
        if (context == null) {
            str2 = "context was null";
        } else {
            try {
                str = Build.MANUFACTURER;
                try {
                    b.a("AndroidUtils", "MANUFACTURER:" + str);
                } catch (Throwable th) {
                    th = th;
                    b.e("AndroidUtils", "get MANUFACTURER failed - error:" + th);
                }
            } catch (Throwable th2) {
                th = th2;
                str = null;
            }
            if (!TextUtils.isEmpty(str)) {
                String str3 = c;
                return TextUtils.isEmpty(str3) || TextUtils.equals(str3, str.toLowerCase());
            }
            str2 = "MANUFACTURER was empty";
        }
        b.e("AndroidUtils", str2);
        return false;
    }
}
