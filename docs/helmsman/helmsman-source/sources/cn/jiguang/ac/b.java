package cn.jiguang.ac;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    private static volatile b i;
    private static final Object j = new Object();
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;
    private boolean e;
    private boolean f;
    private boolean g;
    private boolean h;

    private b() {
        this.a = false;
        this.b = false;
        this.c = false;
        this.d = false;
        this.e = false;
        this.f = false;
        this.g = false;
        this.h = false;
        this.a = e();
        this.b = f();
        this.c = g();
        this.d = h();
        this.e = i();
        this.f = k();
        this.g = j();
        this.h = l();
    }

    public static b a() {
        if (i == null) {
            synchronized (j) {
                if (i == null) {
                    i = new b();
                }
            }
        }
        return i;
    }

    private static boolean e() {
        boolean z;
        try {
            Class.forName("cn.jpush.android.api.JPushInterface");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJpushSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJpushSDK:" + z);
        return z;
    }

    private static boolean f() {
        boolean z;
        try {
            Class.forName("cn.jpush.im.android.api.JMessageClient");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJMessageSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJMessageSDK:" + z);
        return z;
    }

    private static boolean g() {
        boolean z;
        try {
            Class.forName("cn.jiguang.analytics.android.api.JAnalyticsInterface");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJanalyticsSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJanalyticsSDK:" + z);
        return z;
    }

    private static boolean h() {
        boolean z;
        try {
            Class.forName("cn.jiguang.share.android.api.JShareInterface");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJshareSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJshareSDK:" + z);
        return z;
    }

    private static boolean i() {
        boolean z;
        try {
            Class.forName("cn.jiguang.adsdk.api.JSSPInterface");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJSspSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJSspSDK:" + z);
        return z;
    }

    private static boolean j() {
        boolean z;
        try {
            Class.forName("cn.jiguang.h.a");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJCommonSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJCommonSDK:" + z);
        return z;
    }

    private static boolean k() {
        boolean z;
        try {
            Class.forName("cn.jiguang.verifysdk.api.JVerificationInterface");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJVerificationSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJVerificationSDK:" + z);
        return z;
    }

    private static boolean l() {
        boolean z;
        try {
            Class.forName("cn.jiguang.jmlinksdk.core.JMlinkInterfaceImpl");
            z = true;
        } catch (ClassNotFoundException e) {
            cn.jiguang.ad.d.a("JClientsHelper", "isPluginJMLinkSDK:" + e.getMessage());
            z = false;
        }
        cn.jiguang.ad.d.a("JClientsHelper", "isPluginJMLinkSDK:" + z);
        return z;
    }

    public final boolean b() {
        return this.b;
    }

    public final boolean c() {
        return this.a;
    }

    public final boolean d() {
        return this.b || this.a;
    }
}
