package com.tencent.wxop.stat.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import cn.jiguang.internal.JConstants;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.UserDataStore;
import com.facebook.appevents.codeless.internal.Constants;
import com.facebook.places.model.PlaceFields;
import com.tencent.wxop.stat.StatConfig;
import com.tencent.wxop.stat.StatSpecifyReportedInfo;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import kotlin.UByte;
import kotlin.time.DurationKt;
import org.apache.http.HttpHost;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class k {
    private static String a = null;
    private static String b = null;
    private static String c = null;
    private static String d = null;
    private static Random e = null;
    private static DisplayMetrics f = null;
    private static String g = null;
    private static String h = "";
    private static String i = "";
    private static int j = -1;
    private static StatLogger k = null;
    private static String l = null;
    private static String m = null;
    private static volatile int n = -1;
    private static String o = null;
    private static String p = null;
    private static long q = -1;
    private static String r = "";
    private static n s = null;
    private static String t = "__MTA_FIRST_ACTIVATE__";
    private static int u = -1;
    private static long v = -1;
    private static int w = 0;
    private static String x = "";

    public static int A(Context context) {
        return p.a(context, "mta.qq.com.difftime", 0);
    }

    public static boolean B(Context context) {
        ActivityManager activityManager;
        if (context == null || (activityManager = (ActivityManager) context.getSystemService("activity")) == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
            if (runningAppProcessInfo.processName.startsWith(packageName)) {
                if (runningAppProcessInfo.importance == 400) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String C(Context context) {
        if (context == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveInfoResolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveInfoResolveActivity.activityInfo == null || resolveInfoResolveActivity.activityInfo.packageName.equals(Constants.PLATFORM)) {
            return null;
        }
        return resolveInfoResolveActivity.activityInfo.packageName;
    }

    private static long D(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static int a() {
        return g().nextInt(Integer.MAX_VALUE);
    }

    public static int a(Context context, boolean z) {
        if (z) {
            w = A(context);
        }
        return w;
    }

    public static Long a(String str, String str2, int i2, int i3, Long l2) {
        if (str != null && str2 != null) {
            if (str2.equalsIgnoreCase(".") || str2.equalsIgnoreCase("|")) {
                str2 = "\\" + str2;
            }
            String[] strArrSplit = str.split(str2);
            if (strArrSplit.length == i3) {
                try {
                    Long lValueOf = 0L;
                    for (String str3 : strArrSplit) {
                        lValueOf = Long.valueOf(((long) i2) * (lValueOf.longValue() + Long.valueOf(str3).longValue()));
                    }
                    return lValueOf;
                } catch (NumberFormatException unused) {
                }
            }
        }
        return l2;
    }

    public static String a(int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.roll(6, i2);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }

    public static String a(long j2) {
        return new SimpleDateFormat("yyyyMMdd").format(new Date(j2));
    }

    public static String a(Context context, String str) {
        if (StatConfig.isEnableConcurrentProcess()) {
            if (m == null) {
                m = q(context);
            }
            if (m != null) {
                return str + "_" + m;
            }
        }
        return str;
    }

    public static String a(String str) {
        if (str == null) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] bArrDigest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : bArrDigest) {
                int i2 = b2 & UByte.MAX_VALUE;
                if (i2 < 16) {
                    stringBuffer.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                }
                stringBuffer.append(Integer.toHexString(i2));
            }
            return stringBuffer.toString();
        } catch (Throwable unused) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
    }

    public static HttpHost a(Context context) {
        NetworkInfo activeNetworkInfo;
        String extraInfo;
        if (context == null) {
            return null;
        }
        try {
            if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) != 0 || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
                return null;
            }
            if ((activeNetworkInfo.getTypeName() != null && activeNetworkInfo.getTypeName().equalsIgnoreCase("WIFI")) || (extraInfo = activeNetworkInfo.getExtraInfo()) == null) {
                return null;
            }
            if (!extraInfo.equals("cmwap") && !extraInfo.equals("3gwap") && !extraInfo.equals("uniwap")) {
                if (extraInfo.equals("ctwap")) {
                    return new HttpHost("10.0.0.200", 80);
                }
                String defaultHost = Proxy.getDefaultHost();
                if (defaultHost != null && defaultHost.trim().length() > 0) {
                    return new HttpHost(defaultHost, Proxy.getDefaultPort());
                }
                return null;
            }
            return new HttpHost("10.0.0.172", 80);
        } catch (Throwable th) {
            k.e(th);
        }
    }

    public static void a(Context context, int i2) {
        w = i2;
        p.b(context, "mta.qq.com.difftime", i2);
    }

    public static boolean a(StatSpecifyReportedInfo statSpecifyReportedInfo) {
        if (statSpecifyReportedInfo == null) {
            return false;
        }
        return c(statSpecifyReportedInfo.getAppKey());
    }

    public static byte[] a(byte[] bArr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
        byte[] bArr2 = new byte[4096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length * 2);
        while (true) {
            int i2 = gZIPInputStream.read(bArr2);
            if (i2 == -1) {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayInputStream.close();
                gZIPInputStream.close();
                byteArrayOutputStream.close();
                return byteArray;
            }
            byteArrayOutputStream.write(bArr2, 0, i2);
        }
    }

    public static long b(String str) {
        return a(str, ".", 100, 3, 0L).longValue();
    }

    public static synchronized StatLogger b() {
        if (k == null) {
            StatLogger statLogger = new StatLogger(StatConstants.LOG_TAG);
            k = statLogger;
            statLogger.setDebugEnable(false);
        }
        return k;
    }

    public static synchronized String b(Context context) {
        String str = a;
        if (str != null && str.trim().length() != 0) {
            return a;
        }
        String strA = q.a(context);
        a = strA;
        if (strA == null || strA.trim().length() == 0) {
            a = Integer.toString(g().nextInt(Integer.MAX_VALUE));
        }
        return a;
    }

    public static long c() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            return calendar.getTimeInMillis() + JConstants.DAY;
        } catch (Throwable th) {
            k.e(th);
            return System.currentTimeMillis() + JConstants.DAY;
        }
    }

    public static synchronized String c(Context context) {
        String str = c;
        if (str == null || str.trim().length() == 0) {
            c = q.b(context);
        }
        return c;
    }

    public static boolean c(String str) {
        return (str == null || str.trim().length() == 0) ? false : true;
    }

    public static DisplayMetrics d(Context context) {
        if (f == null) {
            f = new DisplayMetrics();
            ((WindowManager) context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(f);
        }
        return f;
    }

    public static String d() {
        if (c(p)) {
            return p;
        }
        long jE = e() / 1000000;
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        String str = String.valueOf((((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks())) / 1000000) + "/" + String.valueOf(jE);
        p = str;
        return str;
    }

    public static long e() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static boolean e(Context context) {
        NetworkInfo[] allNetworkInfo;
        try {
            if (!q.a(context, "android.permission.ACCESS_WIFI_STATE")) {
                k.warn("can not get the permission of android.permission.ACCESS_WIFI_STATE");
                return false;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
            if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null) {
                for (int i2 = 0; i2 < allNetworkInfo.length; i2++) {
                    if (allNetworkInfo[i2].getTypeName().equalsIgnoreCase("WIFI") && allNetworkInfo[i2].isConnected()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Throwable th) {
            k.e(th);
        }
    }

    public static String f(Context context) {
        String str = b;
        if (str != null) {
            return str;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null) {
                return null;
            }
            String string = applicationInfo.metaData.getString("TA_APPKEY");
            if (string != null) {
                b = string;
                return string;
            }
            k.i("Could not read APPKEY meta-data from AndroidManifest.xml");
            return null;
        } catch (Throwable unused) {
            k.i("Could not read APPKEY meta-data from AndroidManifest.xml");
            return null;
        }
    }

    public static String g(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null) {
                return null;
            }
            Object obj = applicationInfo.metaData.get("InstallChannel");
            if (obj != null) {
                return obj.toString();
            }
            k.w("Could not read InstallChannel meta-data from AndroidManifest.xml");
            return null;
        } catch (Throwable unused) {
            k.e("Could not read InstallChannel meta-data from AndroidManifest.xml");
            return null;
        }
    }

    private static synchronized Random g() {
        if (e == null) {
            e = new Random();
        }
        return e;
    }

    private static long h() {
        long jIntValue;
        long j2 = q;
        if (j2 > 0) {
            return j2;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            jIntValue = Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).intValue() * 1024;
            try {
                bufferedReader.close();
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            jIntValue = 1;
        }
        q = jIntValue;
        return jIntValue;
    }

    public static String h(Context context) {
        if (context == null) {
            return null;
        }
        return context.getClass().getName();
    }

    public static String i(Context context) {
        TelephonyManager telephonyManager;
        String str = g;
        if (str != null) {
            return str;
        }
        try {
            if (!q.a(context, "android.permission.READ_PHONE_STATE")) {
                k.e("Could not get permission of android.permission.READ_PHONE_STATE");
            } else if (k(context) && (telephonyManager = (TelephonyManager) context.getSystemService(PlaceFields.PHONE)) != null) {
                g = telephonyManager.getSimOperator();
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return g;
    }

    public static String j(Context context) {
        if (c(h)) {
            return h;
        }
        try {
            String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            h = str;
            if (str == null) {
                return "";
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return h;
    }

    public static boolean k(Context context) {
        return context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()) == 0;
    }

    public static String l(Context context) {
        try {
            if (q.a(context, "android.permission.INTERNET") && q.a(context, "android.permission.ACCESS_NETWORK_STATE")) {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    String typeName = activeNetworkInfo.getTypeName();
                    String extraInfo = activeNetworkInfo.getExtraInfo();
                    if (typeName != null) {
                        if (typeName.equalsIgnoreCase("WIFI")) {
                            return "WIFI";
                        }
                        if (typeName.equalsIgnoreCase("MOBILE")) {
                            if (extraInfo == null) {
                                return "MOBILE";
                            }
                        } else if (extraInfo == null) {
                            return typeName;
                        }
                        return extraInfo;
                    }
                }
            } else {
                k.e("can not get the permission of android.permission.ACCESS_WIFI_STATE");
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return "";
    }

    public static Integer m(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(PlaceFields.PHONE);
            if (telephonyManager != null) {
                return Integer.valueOf(telephonyManager.getNetworkType());
            }
            return null;
        } catch (Throwable unused) {
            return null;
        }
    }

    public static String n(Context context) {
        if (c(i)) {
            return i;
        }
        try {
            String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            i = str;
            if (str == null || str.length() == 0) {
                return "unknown";
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return i;
    }

    public static int o(Context context) {
        int i2 = j;
        if (i2 != -1) {
            return i2;
        }
        try {
            if (o.a()) {
                j = 1;
            }
        } catch (Throwable th) {
            k.e(th);
        }
        j = 0;
        return 0;
    }

    public static String p(Context context) {
        String path;
        if (c(l)) {
            return l;
        }
        try {
            if (!q.a(context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                k.warn("can not get the permission of android.permission.WRITE_EXTERNAL_STORAGE");
                return null;
            }
            String externalStorageState = Environment.getExternalStorageState();
            if (externalStorageState != null && externalStorageState.equals("mounted") && (path = Environment.getExternalStorageDirectory().getPath()) != null) {
                StatFs statFs = new StatFs(path);
                String str = String.valueOf((((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) / 1000000) + "/" + String.valueOf((((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / 1000000);
                l = str;
                return str;
            }
            return null;
        } catch (Throwable th) {
            k.e(th);
        }
    }

    static String q(Context context) {
        try {
            String str = m;
            if (str != null) {
                return str;
            }
            int iMyPid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (runningAppProcessInfo.pid == iMyPid) {
                    m = runningAppProcessInfo.processName;
                    break;
                }
            }
        } catch (Throwable unused) {
        }
        return m;
    }

    public static String r(Context context) {
        return a(context, StatConstants.DATABASE_NAME);
    }

    public static synchronized Integer s(Context context) {
        int i2;
        int i3 = 0;
        if (n <= 0) {
            n = p.a(context, "MTA_EVENT_INDEX", 0);
            p.b(context, "MTA_EVENT_INDEX", n + 1000);
        } else if (n % 1000 == 0) {
            try {
                int i4 = n + 1000;
                if (n < 2147383647) {
                    i3 = i4;
                }
                p.b(context, "MTA_EVENT_INDEX", i3);
            } catch (Throwable th) {
                k.w(th);
            }
        }
        i2 = n + 1;
        n = i2;
        return Integer.valueOf(i2);
    }

    public static String t(Context context) {
        try {
            return String.valueOf(D(context) / 1000000) + "/" + String.valueOf(h() / 1000000);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static JSONObject u(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("n", l.a());
            String strD = l.d();
            if (strD != null && strD.length() > 0) {
                jSONObject.put("na", strD);
            }
            int iB = l.b();
            if (iB > 0) {
                jSONObject.put("fx", iB / DurationKt.NANOS_IN_MILLIS);
            }
            int iC = l.c();
            if (iC > 0) {
                jSONObject.put(UserDataStore.FIRST_NAME, iC / DurationKt.NANOS_IN_MILLIS);
            }
        } catch (Throwable th) {
            Log.w(StatConstants.LOG_TAG, "get cpu error", th);
        }
        return jSONObject;
    }

    public static String v(Context context) {
        List<Sensor> sensorList;
        if (c(r)) {
            return r;
        }
        try {
            SensorManager sensorManager = (SensorManager) context.getSystemService("sensor");
            if (sensorManager != null && (sensorList = sensorManager.getSensorList(-1)) != null) {
                StringBuilder sb = new StringBuilder(sensorList.size() * 10);
                for (int i2 = 0; i2 < sensorList.size(); i2++) {
                    sb.append(sensorList.get(i2).getType());
                    if (i2 != sensorList.size() - 1) {
                        sb.append(",");
                    }
                }
                r = sb.toString();
            }
        } catch (Throwable th) {
            k.e(th);
        }
        return r;
    }

    public static synchronized int w(Context context) {
        int i2 = u;
        if (i2 != -1) {
            return i2;
        }
        x(context);
        return u;
    }

    public static void x(Context context) {
        int iA = p.a(context, t, 1);
        u = iA;
        if (iA == 1) {
            p.b(context, t, 0);
        }
    }

    public static boolean y(Context context) {
        if (v < 0) {
            v = p.a(context, "mta.qq.com.checktime", 0L);
        }
        return Math.abs(System.currentTimeMillis() - v) > JConstants.DAY;
    }

    public static void z(Context context) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        v = jCurrentTimeMillis;
        p.b(context, "mta.qq.com.checktime", jCurrentTimeMillis);
    }
}
