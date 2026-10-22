package cn.jiguang.ac;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import cn.jiguang.f.i;
import cn.jiguang.internal.JConstants;
import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static String a = "";
    private static String b;
    private static Boolean c;
    private static Pair<String, Integer> d;
    private static final ArrayList<String> e;
    private static final ArrayList<String> f;
    private static final ArrayList<String> g;

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        e = arrayList;
        ArrayList<String> arrayList2 = new ArrayList<>();
        f = arrayList2;
        arrayList.add("android.permission.INTERNET");
        arrayList.add("android.permission.ACCESS_NETWORK_STATE");
        arrayList2.add("android.permission.WAKE_LOCK");
        arrayList2.add("android.permission.VIBRATE");
        arrayList2.add("android.permission.CHANGE_WIFI_STATE");
        arrayList2.add("android.permission.WRITE_EXTERNAL_STORAGE");
        ArrayList<String> arrayList3 = new ArrayList<>();
        g = arrayList3;
        arrayList3.add("android.permission.ACCESS_FINE_LOCATION");
        arrayList3.add("android.permission.ACCESS_COARSE_LOCATION");
        arrayList3.add("android.permission.ACCESS_LOCATION_EXTRA_COMMANDS");
        arrayList3.add("android.permission.ACCESS_WIFI_STATE");
    }

    public static String a(Context context) {
        try {
            if (c == null && !TextUtils.isEmpty(JConstants.APP_KEY)) {
                return JConstants.APP_KEY;
            }
        } catch (Throwable unused) {
            c = false;
        }
        b(context);
        return a;
    }

    public static String a(Bundle bundle, String str) {
        Object obj;
        if (bundle == null || (obj = bundle.get(str)) == null) {
            return null;
        }
        return obj.toString();
    }

    public static void a(Context context, String str) {
        b = str;
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.l().a(str)});
        c(context);
    }

    public static String b(Context context) {
        if (!TextUtils.isEmpty(a)) {
            return a;
        }
        if (TextUtils.isEmpty(a)) {
            try {
                if (context != null) {
                    ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                    if (applicationInfo != null && applicationInfo.metaData != null) {
                        String strA = a(applicationInfo.metaData, "JPUSH_APPKEY");
                        a = strA;
                        if (!TextUtils.isEmpty(strA)) {
                            a = a.toLowerCase(Locale.getDefault());
                        }
                    }
                } else {
                    cn.jiguang.ad.d.b("CheckManifestHelper", "[getAppKey] context is null");
                }
            } catch (Throwable unused) {
            }
        }
        return a;
    }

    public static String c(Context context) {
        StringBuilder sbAppend;
        if (b == null && context != null) {
            try {
                String str = (String) cn.jiguang.g.b.b(context, cn.jiguang.g.a.l());
                b = str;
                if (str != null) {
                    sbAppend = new StringBuilder("get option channel - ").append(b);
                } else {
                    ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                    if (applicationInfo != null && applicationInfo.metaData != null) {
                        String strA = a(applicationInfo.metaData, "JPUSH_CHANNEL");
                        b = strA;
                        if (!TextUtils.isEmpty(strA)) {
                            b = i.b(b);
                        }
                    }
                    sbAppend = new StringBuilder("manifest:channel - ").append(b);
                }
                cn.jiguang.ad.d.c("CheckManifestHelper", sbAppend.toString());
            } catch (Throwable unused) {
            }
        }
        return b;
    }

    public static Pair<String, Integer> d(Context context) {
        if (d == null) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                String strSubstring = packageInfo.versionName;
                if (strSubstring != null && strSubstring.length() > 30) {
                    strSubstring = strSubstring.substring(0, 30);
                }
                d = new Pair<>(strSubstring, Integer.valueOf(packageInfo.versionCode));
            } catch (Throwable unused) {
                cn.jiguang.ad.d.c("CheckManifestHelper", "NO versionCode or versionName defined in manifest.");
            }
        }
        return d;
    }

    public static boolean e(Context context) {
        String str;
        String strA = a(context);
        if (TextUtils.isEmpty(strA)) {
            cn.jiguang.ad.d.i("CheckManifestHelper", "errorcode:10001,metadata: JCore appKey - not defined in manifest");
            str = " 未在manifest中配置AppKey";
        } else {
            if (strA.length() == 24) {
                return true;
            }
            cn.jiguang.ad.d.i("CheckManifestHelper", "errorcode:1008,Invalid appKey : " + strA + ", Please get your Appkey from JIGUANG web console!");
            str = " AppKey:" + strA + " 是无效的AppKey,请确认与JIGUANG web端的AppKey一致";
        }
        cn.jiguang.f.a.a(context, str, -1);
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:15:0x005b  */
    /* JADX WARN: Code duplicated, block: B:21:0x0081  */
    /* JADX WARN: Code duplicated, block: B:27:0x00aa  */
    /* JADX WARN: Code duplicated, block: B:32:0x0067 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:34:? A[LOOP:0: B:13:0x0055->B:34:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:36:0x008d A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:38:0x0079 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:41:0x00b6 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:43:0x00a4 A[SYNTHETIC] */
    public static boolean f(Context context) {
        StringBuilder sbAppend;
        c(context);
        if (!b.a().c() && !b.a().b()) {
            for (String str : e) {
                if (!cn.jiguang.f.a.c(context, str)) {
                    sbAppend = new StringBuilder("The permissoin is required - ").append(str);
                }
            }
            for (String str2 : f) {
                if (!cn.jiguang.f.a.e(context, str2)) {
                    cn.jiguang.ad.d.f("CheckManifestHelper", "We recommend you add the permission - " + str2);
                }
            }
            for (String str3 : g) {
                if (!cn.jiguang.f.a.e(context, str3)) {
                    cn.jiguang.ad.d.f("CheckManifestHelper", "We recommend you add the permission - " + str3 + ", otherwise you can not locate the devices.");
                }
            }
            return true;
        }
        String str4 = context.getPackageName() + cn.jiguang.a.a.a;
        if (cn.jiguang.f.a.e(context, str4)) {
            e.add(str4);
            while (r0.hasNext()) {
                if (!cn.jiguang.f.a.c(context, str)) {
                    sbAppend = new StringBuilder("The permissoin is required - ").append(str);
                }
            }
            while (r0.hasNext()) {
                if (!cn.jiguang.f.a.e(context, str2)) {
                    cn.jiguang.ad.d.f("CheckManifestHelper", "We recommend you add the permission - " + str2);
                }
            }
            while (r0.hasNext()) {
                if (!cn.jiguang.f.a.e(context, str3)) {
                    cn.jiguang.ad.d.f("CheckManifestHelper", "We recommend you add the permission - " + str3 + ", otherwise you can not locate the devices.");
                }
            }
            return true;
        }
        sbAppend = new StringBuilder("The permission should be defined - ").append(str4);
        cn.jiguang.ad.d.i("CheckManifestHelper", sbAppend.toString());
        return false;
    }
}
