package cn.jiguang.f;

import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.service.PushReceiver;
import com.facebook.internal.AnalyticsEvents;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.security.auth.x500.X500Principal;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static String a = null;
    private static String b = "";
    private static int c;

    public static ComponentInfo a(Context context, String str, Class<?> cls) {
        int i;
        ComponentInfo[] componentInfoArr;
        if (context == null || TextUtils.isEmpty(str) || cls == null) {
            cn.jiguang.ad.d.f("AndroidUtil", "Action - hasComponent, invalide param, context:" + context + ",packageName:" + str + ",cls:" + cls);
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
                } catch (Throwable unused) {
                }
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("AndroidUtil", "hasComponent error:" + th);
        }
        return null;
    }

    public static ProviderInfo a(Context context, String str, String str2) {
        if (context != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 8);
                if (packageInfo.providers != null && packageInfo.providers.length != 0) {
                    for (ProviderInfo providerInfo : packageInfo.providers) {
                        if (str2.equals(providerInfo.authority)) {
                            return providerInfo;
                        }
                    }
                }
            } catch (Throwable unused) {
            }
        }
        return null;
    }

    public static String a(Context context) {
        if (!TextUtils.isEmpty(a)) {
            return a;
        }
        try {
            Context appContext = JConstants.getAppContext(context);
            ActivityManager activityManager = appContext != null ? (ActivityManager) appContext.getSystemService("activity") : null;
            if (activityManager != null) {
                int iMyPid = Process.myPid();
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                    if (runningAppProcessInfo.pid == iMyPid) {
                        a = runningAppProcessInfo.processName;
                        break;
                    }
                }
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("AndroidUtil", "#unexcepted - getCurProcessName failed:" + th.getMessage());
        }
        return a;
    }

    public static String a(Context context, String str) {
        try {
            return context.getPackageManager().getServiceInfo(new ComponentName(context.getPackageName(), str), 128).processName;
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String a(String str) {
        if (i.h(null)) {
            return null;
        }
        return str;
    }

    public static List<String> a(Context context, Intent intent, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            List<ResolveInfo> listQueryIntentServices = context.getPackageManager().queryIntentServices(intent, 0);
            PackageManager packageManager = context.getPackageManager();
            for (ResolveInfo resolveInfo : listQueryIntentServices) {
                if (resolveInfo.serviceInfo != null) {
                    String str2 = resolveInfo.serviceInfo.name;
                    if (!TextUtils.isEmpty(str2)) {
                        if (TextUtils.isEmpty(str) || packageManager.checkPermission(str, resolveInfo.activityInfo.packageName) == 0) {
                            arrayList.add(str2);
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
        return arrayList;
    }

    public static List<String> a(Context context, List<String> list) {
        if (list.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (!c(context, str)) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public static void a(Context context, Intent intent) {
        try {
            context.sendBroadcast(intent);
        } catch (Throwable unused) {
            try {
                List<String> listB = b(context, intent, (String) null);
                if (listB.isEmpty()) {
                    cn.jiguang.ad.d.g("AndroidUtil", "sendBroadcast failed again: receiver not found, action:" + intent.getAction());
                    return;
                }
                for (String str : listB) {
                    try {
                        Intent intent2 = (Intent) intent.clone();
                        intent2.setComponent(new ComponentName(context.getPackageName(), str));
                        context.sendBroadcast(intent2);
                    } catch (Exception e) {
                        cn.jiguang.ad.d.g("AndroidUtil", "sendBroadcast failed again:" + e.getMessage() + ", action:" + intent.getAction());
                    }
                }
            } catch (Throwable th) {
                cn.jiguang.ad.d.g("AndroidUtil", "tryAgainSendBrocast failed:" + th.getMessage());
            }
        }
    }

    public static void a(Context context, String str, int i) {
        int i2;
        if (!m(context)) {
            cn.jiguang.ad.d.b("AndroidUtil", "not debuggable");
            return;
        }
        if (!a(context, (Class<?>) PushReceiver.class)) {
            new Handler(Looper.getMainLooper()).post(new b(context, str));
            return;
        }
        cn.jiguang.ad.d.b("AndroidUtil", "action:showPermanentNotification");
        Intent intent = new Intent(context, (Class<?>) PushReceiver.class);
        intent.setAction("noti_open_proxy");
        intent.addCategory(context.getPackageName());
        intent.putExtra("debug_notification", true);
        intent.putExtra("toastText", str);
        intent.putExtra("type", -1);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 134217728);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        try {
            i2 = context.getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(), 0).icon;
        } catch (Throwable th) {
            cn.jiguang.ad.d.c("AndroidUtil", "failed to get application info and icon.", th);
            i2 = R.drawable.ic_menu_share;
        }
        Notification.Builder when = new Notification.Builder(context.getApplicationContext()).setContentTitle("Jiguang提示：包名和AppKey不匹配").setContentText("请到 Portal 上获取您的包名和AppKey并更新AndroidManifest相应字段").setContentIntent(broadcast).setSmallIcon(i2).setTicker(str).setWhen(System.currentTimeMillis());
        if (notificationManager.getNotificationChannel("JPush_Notification") == null) {
            notificationManager.createNotificationChannel(new NotificationChannel("JPush_Notification", "JPush_Notification", 3));
        }
        when.setChannelId("JPush_Notification");
        Notification notification = when.getNotification();
        notification.flags = 34;
        c = 10000;
        notificationManager.notify(10000, notification);
    }

    public static boolean a() {
        boolean zEquals;
        try {
            zEquals = Environment.getExternalStorageState().equals("mounted");
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("AndroidUtil", "isSdcardExist exception: " + th);
            zEquals = false;
        }
        if (!zEquals) {
            cn.jiguang.ad.d.b("AndroidUtil", "SDCard is not mounted");
        }
        return zEquals;
    }

    public static boolean a(Context context, Class<?> cls) {
        boolean z = false;
        try {
            boolean z2 = !context.getPackageManager().queryBroadcastReceivers(new Intent(context, cls), 0).isEmpty();
            if (z2) {
                return z2;
            }
            try {
                if (a(context, context.getPackageName(), cls) != null) {
                    z = true;
                }
            } catch (Throwable unused) {
                z = z2;
            }
            return z;
        } catch (Throwable unused2) {
        }
    }

    public static ProviderInfo b(Context context, String str, Class<? extends ContentProvider> cls) {
        try {
            return context.getPackageManager().getProviderInfo(new ComponentName(str, cls.getName()), 65536);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static String b() {
        String path;
        try {
            path = Environment.getExternalStorageDirectory().getPath();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            path = null;
        } catch (Exception unused) {
            path = null;
        }
        return !TextUtils.isEmpty(path) ? path + "/data/" : path;
    }

    public static String b(Context context) {
        if (!TextUtils.isEmpty(b)) {
            return b;
        }
        String packageName = context.getPackageName();
        b = packageName;
        return packageName;
    }

    public static String b(Context context, String str) {
        try {
            return context.getPackageManager().getReceiverInfo(new ComponentName(context.getPackageName(), str), 128).processName;
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String b(Context context, String str, String str2) {
        try {
            return (String) h.a(context.getClassLoader().loadClass("android.os.SystemProperties"), "get", new Object[]{str, str2}, new Class[]{String.class, String.class});
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception unused) {
            return "";
        }
    }

    public static String b(String str) {
        return str;
    }

    private static List<String> b(Context context, Intent intent, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            List<ResolveInfo> listQueryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
            PackageManager packageManager = context.getPackageManager();
            for (ResolveInfo resolveInfo : listQueryBroadcastReceivers) {
                if (resolveInfo.activityInfo != null) {
                    String str2 = resolveInfo.activityInfo.name;
                    if (!TextUtils.isEmpty(str2)) {
                        if (TextUtils.isEmpty(null) || packageManager.checkPermission(null, resolveInfo.activityInfo.packageName) == 0) {
                            arrayList.add(str2);
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
        return arrayList;
    }

    public static int c(Context context) {
        String str;
        if (context == null) {
            return -1;
        }
        Intent intentRegisterReceiver = null;
        try {
            intentRegisterReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"), context.getPackageName() + cn.jiguang.a.a.a, null);
        } catch (SecurityException unused) {
            str = "getChargedStatus SecurityException";
            cn.jiguang.ad.d.f("AndroidUtil", str);
        } catch (Exception e) {
            str = "getChargedStatus unkown exception:" + e.getMessage();
            cn.jiguang.ad.d.f("AndroidUtil", str);
        }
        if (intentRegisterReceiver == null) {
            return -1;
        }
        int intExtra = intentRegisterReceiver.getIntExtra("status", -1);
        if (intExtra == 2 || intExtra == 5) {
            return intentRegisterReceiver.getIntExtra("plugged", -1);
        }
        return -1;
    }

    public static String c() {
        return "";
    }

    public static String c(String str) {
        return str;
    }

    public static boolean c(Context context, String str) {
        String strPermissionToOp;
        boolean z = false;
        try {
            if (context.getApplicationInfo().targetSdkVersion < 23 ? !((strPermissionToOp = AppOpsManager.permissionToOp(str)) != null && ((AppOpsManager) context.getSystemService("appops")).noteProxyOpNoThrow(strPermissionToOp, context.getPackageName()) != 0) : context.checkSelfPermission(str) == 0) {
                z = true;
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("AndroidUtil", "checkPermission error:" + th.getMessage());
        }
        return z;
    }

    public static String d() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress() && (inetAddressNextElement instanceof Inet4Address)) {
                        return inetAddressNextElement.getHostAddress();
                    }
                }
            }
            return "";
        } catch (Exception e) {
            cn.jiguang.ad.d.f("AndroidUtil", "getPhoneIp:");
            e.printStackTrace();
            return "";
        }
    }

    public static boolean d(Context context) {
        NetworkInfo activeNetworkInfo;
        try {
            return c(context, "android.permission.ACCESS_NETWORK_STATE") && (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) != null && activeNetworkInfo.isConnected();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return false;
    }

    public static boolean d(Context context, String str) {
        if (i.a(str)) {
            return false;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 0) != null;
        } catch (Throwable unused) {
        }
    }

    public static boolean e(Context context) {
        String str = context.getApplicationInfo().sourceDir;
        if (i.a(str)) {
            cn.jiguang.ad.d.h("AndroidUtil", "Unexpected: cannot get pk installed path");
            return false;
        }
        cn.jiguang.ad.d.b("AndroidUtil", "Current pk installed path: " + str);
        if (str.startsWith("/system/app/")) {
            return true;
        }
        if (str.startsWith("/data/app/")) {
            return false;
        }
        cn.jiguang.ad.d.d("AndroidUtil", "NOTE: the pk does not installed in system/data. ");
        return false;
    }

    public static boolean e(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("empty params");
        }
        try {
            context.getPackageManager().getPermissionInfo(str, 128);
            return true;
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("AndroidUtil", "hasPermissionDefined error:" + th.getMessage());
            return false;
        }
    }

    public static String f(Context context) {
        DisplayMetrics displayMetrics;
        if (context == null || context.getResources() == null || (displayMetrics = context.getResources().getDisplayMetrics()) == null) {
            return "0*0";
        }
        return displayMetrics.widthPixels + "*" + displayMetrics.heightPixels;
    }

    public static void g(Context context) {
        try {
            if (c != 0) {
                ((NotificationManager) context.getSystemService("notification")).cancel(c);
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("AndroidUtil", "[canclePermanentNotification] failed:" + th.getMessage());
        }
    }

    public static String h(Context context) {
        return null;
    }

    public static boolean i(Context context) {
        String strB = b(context, "ro.product.brand", "");
        cn.jiguang.ad.d.b("AndroidUtil", "brand = " + strB);
        String strB2 = b(context, "ro.miui.ui.version.name", "");
        if (TextUtils.isEmpty(strB) || !"Xiaomi".equals(strB) || TextUtils.isEmpty(strB2)) {
            return true;
        }
        String strB3 = b(context, "ro.build.version.incremental", "");
        if (TextUtils.isEmpty(strB3) || !strB3.startsWith("V7.1")) {
            return true;
        }
        cn.jiguang.ad.d.f("AndroidUtil", "7.1 will not get wifi list");
        return false;
    }

    public static String j(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
            }
            String typeName = activeNetworkInfo.getTypeName();
            String subtypeName = activeNetworkInfo.getSubtypeName();
            if (typeName == null) {
                return AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
            }
            return !i.a(subtypeName) ? typeName + "," + subtypeName : typeName;
        } catch (Exception e) {
            e.printStackTrace();
            return AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        }
    }

    public static String k(Context context) {
        String bssid;
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (wifiManager == null || !c(context, "android.permission.ACCESS_WIFI_STATE") || (bssid = wifiManager.getConnectionInfo().getBSSID()) == null) {
                return "";
            }
            try {
                if (bssid.startsWith("02:00:00:")) {
                    return "";
                }
            } catch (Throwable unused) {
            }
            return bssid;
        } catch (Throwable unused2) {
            return "";
        }
    }

    public static String l(Context context) {
        Object objInvoke;
        String str = null;
        int iIntValue = -1;
        if (context != null) {
            try {
                Object systemService = context.getApplicationContext().getSystemService("country_detector");
                if (systemService != null) {
                    Method declaredMethod = systemService.getClass().getDeclaredMethod("detectCountry", new Class[0]);
                    if (declaredMethod != null && (objInvoke = declaredMethod.invoke(systemService, new Object[0])) != null) {
                        String str2 = (String) objInvoke.getClass().getDeclaredMethod("getCountryIso", new Class[0]).invoke(objInvoke, new Object[0]);
                        try {
                            iIntValue = ((Integer) objInvoke.getClass().getDeclaredMethod("getSource", new Class[0]).invoke(objInvoke, new Object[0])).intValue();
                            str = str2;
                        } catch (Throwable th) {
                            th = th;
                            str = str2;
                            cn.jiguang.ad.d.h("AndroidUtil", "getCountryCode failed, error :" + th);
                        }
                    }
                } else {
                    cn.jiguang.ad.d.b("AndroidUtil", "country_detector is null");
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        cn.jiguang.ad.d.b("AndroidUtil", "get CountCode = " + str + " source = " + iIntValue);
        return (iIntValue == 0 || iIntValue == 1) ? str : "";
    }

    private static boolean m(Context context) {
        String name;
        boolean z = false;
        try {
            boolean z2 = (context.getApplicationInfo().flags & 2) != 0;
            cn.jiguang.ad.d.b("AndroidUtil", "isDebug:" + z2);
            if (JConstants.INTERNAL_USE && JConstants.DEBUG_MODE) {
                return z2;
            }
            X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
            String[] strArr = {"CN=Android Debug", "O=Android", "C=US"};
            Signature[] signatureArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures;
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            boolean zEquals = false;
            for (Signature signature : signatureArr) {
                try {
                    X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(signature.toByteArray()));
                    zEquals = x509Certificate.getSubjectX500Principal().equals(x500Principal);
                    cn.jiguang.ad.d.b("AndroidUtil", "debuggable :" + zEquals);
                    if (zEquals) {
                        break;
                    }
                    try {
                        name = x509Certificate.getSubjectX500Principal().getName();
                    } catch (Exception unused) {
                        name = null;
                    }
                    cn.jiguang.ad.d.b("AndroidUtil", "certName:" + name);
                    if (name != null && name.contains(strArr[0]) && name.contains(strArr[1]) && name.contains(strArr[2])) {
                        return true;
                    }
                } catch (Throwable unused2) {
                    z = zEquals;
                }
            }
            return zEquals;
        } catch (Throwable unused3) {
        }
        return z;
    }
}
