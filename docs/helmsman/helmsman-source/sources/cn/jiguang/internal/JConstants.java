package cn.jiguang.internal;

import android.content.Context;
import cn.jiguang.ad.d;
import cn.jiguang.api.JAnalyticsAction;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class JConstants {
    public static final byte AES_CBC = 1;
    public static String APP_KEY = "";
    public static boolean AT_I = false;
    public static final String DATA_PROVIDER_SUFFIX = ".DataProvider";
    public static final long DAY = 86400000;
    public static boolean DEBUG_MODE = false;
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final long HOUR = 3600000;
    public static final String HTTPS_PRE = "https://";
    public static final String HTTP_PRE = "http://";
    public static boolean INTERNAL_USE = false;
    public static final boolean IS_FOR_GOOGLE_PLAY_USE = true;
    public static final boolean LOG_ENABLE = true;
    public static final int LOG_ENABLED_LEVEL = 2;
    public static final boolean LOG_FILE_ENABLE = true;
    public static final long MIN = 60000;
    public static final boolean REPORT_SUPPORT_HTTP = false;
    public static final boolean REPORT_USE_HTTPS = true;
    public static String SDK_NAME = "";
    public static final String SDK_TYPE = "JCore";
    public static int SDK_VERSION_INT = 230;
    public static final long SECOND = 1000;
    public static final byte SM4_CBC = 2;
    public static final boolean SUPPORT_DY = false;
    private static final String TAG = "JConstants";
    public static String TUU = "";
    public static final String USER_SERVICE_ACTION = "cn.jiguang.user.service.action";
    public static boolean isInstrumentationHookFailed = true;
    public static JAnalyticsAction jAnalyticsAction;
    public static int tcpSessionId;
    public static AtomicBoolean isCallInit = new AtomicBoolean();
    public static int DEFAULT_HEARTBEAT_INTERVAL = 285;
    public static int DEFAULT_ALARM_DELAY = 290;
    public static byte tcpAlgorithm = 1;
    private static boolean testEnv = false;
    public static String testCountry = "";
    private static boolean isTestAndroidQ = false;
    public static Context mApplicationContext = null;

    public static Context getAppContext(Context context) {
        if (mApplicationContext == null && context != null) {
            mApplicationContext = context.getApplicationContext();
        }
        return mApplicationContext;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0029, code lost:
    
        if (r3.getApplicationInfo().targetSdkVersion <= 28) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isAndroidQ(android.content.Context r3, boolean r4, java.lang.String r5) {
        /*
            r0 = 1
            r1 = 0
            r2 = 28
            if (r4 != 0) goto L16
            boolean r3 = cn.jiguang.internal.JConstants.isTestAndroidQ
            if (r3 == 0) goto Lf
            int r3 = android.os.Build.VERSION.SDK_INT
            if (r3 < r2) goto L14
            goto L2b
        Lf:
            int r3 = android.os.Build.VERSION.SDK_INT
            if (r3 <= r2) goto L14
            goto L2b
        L14:
            r0 = r1
            goto L2b
        L16:
            boolean r4 = cn.jiguang.internal.JConstants.isTestAndroidQ
            if (r4 == 0) goto L1f
            int r4 = android.os.Build.VERSION.SDK_INT
            if (r4 < r2) goto L14
            goto L23
        L1f:
            int r4 = android.os.Build.VERSION.SDK_INT
            if (r4 <= r2) goto L14
        L23:
            android.content.pm.ApplicationInfo r3 = r3.getApplicationInfo()
            int r3 = r3.targetSdkVersion
            if (r3 <= r2) goto L14
        L2b:
            if (r0 == 0) goto L41
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "is Android Q, msg: "
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "JConstants"
            cn.jiguang.ad.d.b(r4, r3)
        L41:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.jiguang.internal.JConstants.isAndroidQ(android.content.Context, boolean, java.lang.String):boolean");
    }

    public static boolean isTestEnv() {
        return false;
    }

    public static void setTestEnv(boolean z) {
    }

    public static void testAndroidQ() {
        d.b(TAG, "call testAndroidQ");
        isTestAndroidQ = true;
    }
}
