package cn.jiguang.ar;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.f.e;
import cn.jiguang.f.i;
import cn.jiguang.internal.JConstants;
import java.io.File;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    public static int a = 1;

    public static String a(Context context) {
        String strTrim;
        String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.a());
        if (i.h(str)) {
            a = 3;
            return str;
        }
        if (i.h(str)) {
            a = 1;
            b(context, str);
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.a().a(str)});
            return str;
        }
        if (JConstants.isAndroidQ(context, true, "do not get deviceId from SD") || !cn.jiguang.f.a.c(context, "android.permission.READ_EXTERNAL_STORAGE")) {
            strTrim = null;
        } else {
            String strB = cn.jiguang.f.a.b();
            if (TextUtils.isEmpty(strB)) {
                cn.jiguang.ad.d.h("DeviceIdUtils", "can't get sdcard data path");
            } else {
                String strB2 = e.b(new File(strB + ".push_deviceid"));
                if (strB2 != null) {
                    int iIndexOf = strB2.indexOf("\n");
                    if (iIndexOf >= 0) {
                        strB2 = strB2.substring(0, iIndexOf);
                    }
                    strTrim = strB2.trim();
                }
            }
            strTrim = null;
        }
        if (i.h(strTrim)) {
            a = 2;
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.a().a(strTrim)});
            return strTrim;
        }
        String strC = cn.jiguang.f.a.c();
        String strC2 = cn.jiguang.f.a.c("");
        String string = UUID.randomUUID().toString();
        String strD = i.d("" + strC + strC2 + string);
        if (!TextUtils.isEmpty(strD)) {
            string = strD;
        }
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.a().a(string)});
        a = 0;
        b(context, string);
        return string;
    }

    public static void a(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        b(context, str);
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.a().a(str)});
    }

    private static String b(Context context, String str) {
        if (JConstants.isAndroidQ(context, true, "not write deviceId to SD")) {
            return str;
        }
        if (!cn.jiguang.f.a.c(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            return null;
        }
        try {
            String strB = cn.jiguang.f.a.b();
            if (TextUtils.isEmpty(strB)) {
                cn.jiguang.ad.d.h("DeviceIdUtils", "can't get sdcard data path");
                return null;
            }
            e.a(new File(strB + ".push_deviceid"), str);
            return str;
        } catch (Throwable unused) {
            return null;
        }
    }
}
