package cn.jiguang.ao;

import android.content.Context;
import android.os.Build;
import cn.jiguang.aj.e;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static volatile a r;
    private static final Object s = new Object();
    private static String t;
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public int g;
    public String h;
    public String i;
    public String j;
    public String k;
    public String l;
    public String m;
    public String n;
    public String o;
    public String p;
    private transient AtomicBoolean q;

    private a(Context context) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        this.q = atomicBoolean;
        if (atomicBoolean.get() || context == null) {
            return;
        }
        this.b = a(Build.VERSION.RELEASE) + "," + Build.VERSION.SDK_INT;
        this.c = a(Build.MODEL);
        this.d = cn.jiguang.f.a.b(context, "gsm.version.baseband", "baseband");
        this.e = a(Build.DEVICE);
        this.k = a(Build.PRODUCT);
        this.l = a(Build.MANUFACTURER);
        this.m = a(Build.FINGERPRINT);
        this.n = a(Build.BRAND);
        this.a = b(context);
        this.f = cn.jiguang.ac.a.c(context);
        this.g = cn.jiguang.f.a.e(context) ? 1 : 0;
        this.h = cn.jiguang.f.a.f(context);
        this.i = cn.jiguang.f.a.c();
        this.j = " ";
        this.o = cn.jiguang.f.a.b("");
        Object objA = e.a(context, "get_imei", null);
        if (objA instanceof String) {
            this.p = (String) objA;
        }
        this.q.set(true);
    }

    public static a a(Context context) {
        if (r == null) {
            synchronized (s) {
                if (r == null) {
                    r = new a(context);
                }
            }
        }
        return r;
    }

    private static String a(String str) {
        if (str != null) {
            return str.trim();
        }
        return null;
    }

    private static String b(Context context) {
        if (t == null) {
            try {
                String strSubstring = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                if (strSubstring.length() > 30) {
                    strSubstring = strSubstring.substring(0, 30);
                }
                t = strSubstring;
            } catch (Throwable unused) {
                cn.jiguang.ad.d.c("DeviceInfo", "NO versionName defined in manifest.");
            }
        }
        String str = t;
        return str == null ? "" : str;
    }
}
