package cn.jpush.android.t;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushConstants;
import cn.jpush.android.service.JNotifyActivity;
import cn.jpush.android.ui.PopWinActivity;
import cn.jpush.android.ui.PushActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static String a = "";
    private static String b;

    public static int a(String str, String str2) {
        if (str.equals(str2)) {
            return 0;
        }
        try {
            String[] strArrSplit = str.split("[._]");
            String[] strArrSplit2 = str2.split("[._]");
            if (strArrSplit.length > strArrSplit2.length) {
                strArrSplit2 = (String[]) Arrays.copyOf(strArrSplit2, strArrSplit.length);
            } else if (strArrSplit.length < strArrSplit2.length) {
                strArrSplit = (String[]) Arrays.copyOf(strArrSplit, strArrSplit2.length);
            }
            for (int i = 0; i < strArrSplit.length; i++) {
                long j = (TextUtils.isEmpty(strArrSplit[i]) ? 0L : Long.parseLong(strArrSplit[i])) - (TextUtils.isEmpty(strArrSplit2[i]) ? 0L : Long.parseLong(strArrSplit2[i]));
                if (j > 0) {
                    return 1;
                }
                if (j < 0) {
                    return -1;
                }
            }
            return 0;
        } catch (Throwable unused) {
            return str.compareTo(str2);
        }
    }

    public static ComponentInfo a(Context context, String str, Class<?> cls) {
        int i;
        ComponentInfo[] componentInfoArr;
        if (context == null || TextUtils.isEmpty(str) || cls == null) {
            Logger.w("AndroidUtil", "Action - hasComponent, invalide param, context:" + context + ",packageName:" + str + ",cls:" + cls);
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
            Logger.ww("AndroidUtil", "hasComponent error:" + th.getMessage());
        }
        return null;
    }

    public static String a(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (byte b2 : bArr) {
            a(stringBuffer, b2);
        }
        return stringBuffer.toString();
    }

    public static void a(Context context, Intent intent, String str) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action) || JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            List<String> listB = b(context, intent, str);
            if (listB == null || listB.isEmpty()) {
                Logger.ww("AndroidUtil", "sendBroadcast failed again: receiver not found, action:" + intent.getAction());
                return;
            }
            for (String str2 : listB) {
                try {
                    Intent intent2 = (Intent) intent.clone();
                    intent2.setComponent(new ComponentName(context.getPackageName(), str2));
                    if (TextUtils.isEmpty(str)) {
                        context.sendBroadcast(intent2);
                    } else {
                        context.sendBroadcast(intent2, str);
                    }
                } catch (Exception e) {
                    Logger.ww("AndroidUtil", "sendBroadcast failed again:" + e.getMessage() + ", action:" + intent.getAction());
                }
            }
        }
    }

    public static void a(WebSettings webSettings) {
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(2);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
    }

    public static void a(WebView webView) {
        try {
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
            webView.getSettings().setMixedContentMode(0);
        } catch (Throwable th) {
            Logger.e("AndroidUtil", "fixSecure failed, error:" + th);
        }
    }

    private static void a(StringBuffer stringBuffer, byte b2) {
        stringBuffer.append("0123456789ABCDEF".charAt((b2 >> 4) & 15)).append("0123456789ABCDEF".charAt(b2 & 15));
    }

    public static boolean a() {
        boolean zEquals;
        try {
            zEquals = Environment.getExternalStorageState().equals("mounted");
        } catch (Throwable th) {
            Logger.w("AndroidUtil", "isSdcardExist exception: " + th);
            zEquals = false;
        }
        if (!zEquals) {
            Logger.d("AndroidUtil", "SDCard is not mounted");
        }
        return zEquals;
    }

    public static boolean a(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean a(Context context, Class<?> cls) {
        try {
            return !context.getPackageManager().queryIntentActivities(new Intent(context, cls), 0).isEmpty();
        } catch (Throwable th) {
            Logger.dd("AndroidUtil", "hasActivityResolves error:" + th.getMessage());
            return false;
        }
    }

    public static boolean a(Context context, String str) {
        try {
            boolean z = true;
            if (context.getApplicationInfo().targetSdkVersion < 23) {
                String strPermissionToOp = AppOpsManager.permissionToOp(str);
                if (strPermissionToOp != null) {
                    z = ((AppOpsManager) context.getSystemService("appops")).noteProxyOpNoThrow(strPermissionToOp, context.getPackageName()) == 0;
                }
            } else if (context.checkSelfPermission(str) != 0) {
                return false;
            }
            return z;
        } catch (Throwable th) {
            Logger.w("AndroidUtil", "checkPermission error:" + th.getMessage());
            return false;
        }
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String strTrim = str.trim();
        boolean zMatches = strTrim.matches("^[http|https]+://.*");
        if (!zMatches) {
            Logger.w("AndroidUtil", "Invalid url - " + strTrim);
        }
        return zMatches;
    }

    public static String b(Context context) {
        if (!TextUtils.isEmpty(a)) {
            return a;
        }
        try {
            a = context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0)).toString();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return a;
    }

    public static String b(String str) {
        if (str != null && !"".equals(str)) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes());
                return a(messageDigest.digest());
            } catch (NoSuchAlgorithmException unused) {
            }
        }
        return null;
    }

    public static List<String> b(Context context, Intent intent, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            List<ResolveInfo> listQueryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
            PackageManager packageManager = context.getPackageManager();
            for (ResolveInfo resolveInfo : listQueryBroadcastReceivers) {
                if (resolveInfo.activityInfo != null) {
                    String str2 = resolveInfo.activityInfo.name;
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

    public static void b(Context context, String str) {
        try {
            Intent intentF = f(context);
            if (intentF != null) {
                if (!TextUtils.isEmpty(str)) {
                    intentF.putExtra("extra", str);
                }
                context.startActivity(intentF);
            }
        } catch (Throwable th) {
            Logger.dd("AndroidUtil", "startMainActivity error:" + th.getMessage());
        }
    }

    public static String c(Context context) {
        if (!TextUtils.isEmpty(b)) {
            return b;
        }
        try {
            Context appContext = JPushConstants.getAppContext(context);
            ActivityManager activityManager = appContext != null ? (ActivityManager) appContext.getSystemService("activity") : null;
            if (activityManager != null) {
                int iMyPid = Process.myPid();
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                    if (runningAppProcessInfo.pid == iMyPid) {
                        b = runningAppProcessInfo.processName;
                        break;
                    }
                }
            }
        } catch (Throwable th) {
            Logger.ww("AndroidUtil", "#unexcepted - getCurProcessName failed:" + th.getMessage());
        }
        return b;
    }

    public static boolean c(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 0) != null;
        } catch (Throwable unused) {
        }
    }

    public static void d(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                String strC = c(context);
                String packageName = context.getPackageName();
                if (strC == null || packageName == null || packageName.equals(strC)) {
                    return;
                }
                WebView.setDataDirectorySuffix(strC);
                Logger.d("AndroidUtil", "setDataDirectorySuffix =" + strC);
            }
        } catch (Throwable th) {
            Logger.e("AndroidUtil", "fixWebViewProcess e:" + th);
        }
    }

    public static boolean d(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(str);
            intent.addCategory(context.getPackageName());
            return !packageManager.queryIntentActivities(intent, 0).isEmpty();
        } catch (Throwable th) {
            Logger.dd("AndroidUtil", "hasActivityIntentFilter error:" + th.getMessage());
            return true;
        }
    }

    public static String e(Context context, String str) {
        return context.getPackageName() + "." + str;
    }

    public static void e(Context context) {
        b(context, null);
    }

    public static Intent f(Context context) {
        Throwable th;
        Intent launchIntentForPackage;
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getApplicationContext().getPackageName();
            if (TextUtils.isEmpty(packageName)) {
                Logger.ww("AndroidUtil", "The package with the given name cannot be found!");
                return null;
            }
            launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName);
            try {
                if (launchIntentForPackage == null) {
                    Logger.ww("AndroidUtil", "Can't get launch intent for this package!");
                    return null;
                }
                launchIntentForPackage.addFlags(268468224);
                return launchIntentForPackage;
            } catch (Throwable th2) {
                th = th2;
                Logger.w("AndroidUtil", "getLaunchIntent error:" + th.getMessage());
            }
        } catch (Throwable th3) {
            th = th3;
            launchIntentForPackage = null;
        }
    }

    public static boolean g(Context context) {
        String string;
        StringBuilder sb;
        Class cls;
        Logger.dd("AndroidUtil", "action:checkValidManifest");
        if (a(context, (Class<?>) PushActivity.class)) {
            if (!a(context, (Class<?>) PopWinActivity.class)) {
                Logger.ww("AndroidUtil", "AndroidManifest.xml missing activity: " + PopWinActivity.class.getCanonicalName());
                Logger.ww("AndroidUtil", "You will unable to use pop-window rich push type.");
            }
            if (!d(context, "cn.jpush.android.ui.PushActivity")) {
                string = "AndroidManifest.xml missing required intent filter for PushActivity: cn.jpush.android.ui.PushActivity";
            } else if (!a(context, (Class<?>) JNotifyActivity.class)) {
                sb = new StringBuilder("AndroidManifest.xml missing required activity: ");
                cls = JNotifyActivity.class;
            } else {
                if (!TextUtils.isEmpty(JPushConstants.getMessageReceiverClass(context))) {
                    return true;
                }
                string = "AndroidManifest.xml missing required receiver: please custom receiver extends JPushMessageReceiver";
            }
            Logger.ee("AndroidUtil", string);
            return false;
        }
        sb = new StringBuilder("AndroidManifest.xml missing required activity: ");
        cls = PushActivity.class;
        string = sb.append(cls.getCanonicalName()).toString();
        Logger.ee("AndroidUtil", string);
        return false;
    }

    public static boolean h(Context context) {
        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return false;
            }
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.processName.equals(context.getPackageName()) && runningAppProcessInfo.importance == 100) {
                    return true;
                }
            }
        } catch (Throwable th) {
            Logger.ww("AndroidUtil", "[isAppOnForeground] error:" + th.getMessage());
        }
        return false;
    }
}
