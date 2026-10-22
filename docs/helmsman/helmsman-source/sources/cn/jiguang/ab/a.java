package cn.jiguang.ab;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.DaemonService;
import cn.jpush.android.service.DownloadProvider;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static Boolean a;
    private static Boolean b;
    private static final String c = "Xiaomi".toLowerCase();

    private static Intent a(PackageManager packageManager, String str) {
        try {
            Intent intent = new Intent();
            intent.setAction("cn.jpush.android.intent.DActivity");
            intent.addCategory(str);
            intent.setPackage(str);
            ResolveInfo resolveInfoResolveActivity = packageManager.resolveActivity(intent, 0);
            if (resolveInfoResolveActivity == null) {
                cn.jiguang.w.a.d("JWakeComponentHelper", "getDActivity resolveInfo was null from:" + str);
                return null;
            }
            cn.jiguang.w.a.a("JWakeComponentHelper", "target actvity name:" + resolveInfoResolveActivity.activityInfo.name + ", theme:" + resolveInfoResolveActivity.activityInfo.theme + ",exported:" + resolveInfoResolveActivity.activityInfo.exported);
            if (resolveInfoResolveActivity.activityInfo.exported && resolveInfoResolveActivity.activityInfo.enabled && "jpush.custom".equals(resolveInfoResolveActivity.activityInfo.taskAffinity)) {
                if (resolveInfoResolveActivity.activityInfo.theme != 16973840) {
                    cn.jiguang.w.a.d("JWakeComponentHelper", resolveInfoResolveActivity.activityInfo.name + "activity theme must config as @android:style/Theme.Translucent.NoTitleBar");
                    return null;
                }
                intent.setComponent(new ComponentName(str, resolveInfoResolveActivity.activityInfo.name));
                cn.jiguang.w.a.a("JWakeComponentHelper", "dIntent:" + intent);
                return intent;
            }
            cn.jiguang.w.a.d("JWakeComponentHelper", "activity muse be exported and enabled, and taskAffinity must be jpush.custom");
            return null;
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWakeComponentHelper", "get deeplink activity error#" + th);
            return null;
        }
    }

    static cn.jiguang.aa.b a(Context context, PackageManager packageManager, String str, String str2) {
        StringBuilder sb;
        String message;
        if (packageManager != null && !TextUtils.isEmpty(str)) {
            try {
                int iCheckPermission = packageManager.checkPermission(str + JPushInterface.PUSH_MESSAGE_PERMISSION_POSTFIX, str);
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 128);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    String strA = cn.jiguang.ac.a.a(context);
                    Intent intent = new Intent();
                    intent.setClassName(str, "cn.jpush.android.service.PushService");
                    boolean z = false;
                    List<ResolveInfo> listQueryIntentServices = packageManager.queryIntentServices(intent, 0);
                    if (listQueryIntentServices != null && listQueryIntentServices.size() != 0) {
                        z = true;
                    }
                    if (iCheckPermission == 0 && z && !TextUtils.isEmpty(strA) && strA.length() == 24) {
                        cn.jiguang.aa.b bVar = new cn.jiguang.aa.b(str, str2, applicationInfo.targetSdkVersion);
                        ComponentInfo componentInfoA = cn.jiguang.f.a.a(context, str, (Class<?>) DownloadProvider.class);
                        if (componentInfoA instanceof ProviderInfo) {
                            ProviderInfo providerInfo = (ProviderInfo) componentInfoA;
                            if (providerInfo.exported && providerInfo.enabled && providerInfo.authority != null && TextUtils.equals(str + ".DownloadProvider", providerInfo.authority)) {
                                bVar.d = providerInfo.authority;
                            }
                        }
                        if (b(context) && !a(context)) {
                            bVar.e = a(packageManager, str);
                        }
                        return bVar;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                sb = new StringBuilder("checkWhetherToStart exception:");
                message = e.toString();
                cn.jiguang.w.a.d("JWakeComponentHelper", sb.append(message).toString());
            } catch (Throwable th) {
                sb = new StringBuilder("checkWhetherToStart throwable:");
                message = th.getMessage();
                cn.jiguang.w.a.d("JWakeComponentHelper", sb.append(message).toString());
            }
        }
        return null;
    }

    public static void a(Context context, boolean z) {
        ServiceInfo serviceInfo;
        try {
            if (context == null) {
                cn.jiguang.w.a.e("JWakeComponentHelper", "context is null, give up setComponentEnabled");
            } else {
                PackageManager packageManager = context.getApplicationContext().getPackageManager();
                if (packageManager == null) {
                    cn.jiguang.w.a.e("JWakeComponentHelper", "PackageManager is null, give up setComponentEnabled");
                } else {
                    String packageName = context.getPackageName();
                    int i = z ? 1 : 2;
                    Intent intent = new Intent();
                    intent.setPackage(packageName);
                    intent.setAction("cn.jpush.android.intent.DaemonService");
                    List<ResolveInfo> listQueryIntentServices = packageManager.queryIntentServices(intent, 512);
                    if (listQueryIntentServices == null || listQueryIntentServices.isEmpty()) {
                        cn.jiguang.w.a.d("JWakeComponentHelper", "cant't find DaemonService");
                    } else {
                        for (ResolveInfo resolveInfo : listQueryIntentServices) {
                            if (resolveInfo != null && (serviceInfo = resolveInfo.serviceInfo) != null) {
                                String str = serviceInfo.name;
                                if (TextUtils.isEmpty(str)) {
                                    continue;
                                } else {
                                    try {
                                        Class<?> cls = Class.forName(str);
                                        if (cls != null) {
                                            if (DaemonService.class.isAssignableFrom(cls)) {
                                                ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                                                cn.jiguang.w.a.a("JWakeComponentHelper", componentName + " setComponentEnabledSetting newState: " + i);
                                                if (packageManager.getComponentEnabledSetting(componentName) == i) {
                                                    cn.jiguang.w.a.a("JWakeComponentHelper", "DaemonService  enabled is :" + z + ", no need repeat set.");
                                                    break;
                                                }
                                                packageManager.setComponentEnabledSetting(componentName, i, 1);
                                            } else {
                                                cn.jiguang.w.a.e("JWakeComponentHelper", "give up setting, as " + str + " is not extend from: " + DaemonService.class.getName());
                                            }
                                        }
                                    } catch (ClassNotFoundException unused) {
                                        cn.jiguang.w.a.d("JWakeComponentHelper", "cant't find service class:" + str);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JWakeComponentHelper", "setServiceEnabled throwable:" + th.getMessage());
        }
        try {
            if (context == null) {
                cn.jiguang.w.a.e("JWakeComponentHelper", "context is null, give up setComponentEnabled");
                return;
            }
            PackageManager packageManager2 = context.getApplicationContext().getPackageManager();
            if (packageManager2 == null) {
                cn.jiguang.w.a.e("JWakeComponentHelper", "PackageManager is null, give up setComponentEnabled");
                return;
            }
            int i2 = z ? 1 : 2;
            ComponentName componentName2 = new ComponentName(context, (Class<?>) DownloadProvider.class);
            if (packageManager2.getComponentEnabledSetting(componentName2) == i2) {
                cn.jiguang.w.a.a("JWakeComponentHelper", componentName2.getClassName() + " enabled is :" + z + ", no need repeat set.");
            } else {
                cn.jiguang.w.a.a("JWakeComponentHelper", componentName2 + " setDownloadProviderEnabledSetting newState: " + i2);
                packageManager2.setComponentEnabledSetting(componentName2, i2, 1);
            }
        } catch (Throwable th2) {
            cn.jiguang.w.a.d("JWakeComponentHelper", "setContentProviderEnabled throwable:" + th2.getMessage());
        }
    }

    private static boolean a() {
        try {
            String str = Build.MANUFACTURER;
            return !TextUtils.isEmpty(str) && TextUtils.equals(c, str.toLowerCase());
        } catch (Throwable th) {
            cn.jiguang.w.a.e("JWakeComponentHelper", "get MANUFACTURER failed - error:" + th);
            return false;
        }
    }

    public static boolean a(Context context) {
        Boolean bool = b;
        if (bool != null) {
            return bool.booleanValue();
        }
        b = Boolean.valueOf(a());
        return b.booleanValue();
    }

    private static boolean a(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(str);
            intent.addCategory(context.getPackageName());
            return !packageManager.queryIntentActivities(intent, 0).isEmpty();
        } catch (Throwable th) {
            cn.jiguang.w.a.e("JWakeComponentHelper", "hasActivityIntentFilter error:" + th.getMessage());
            return false;
        }
    }

    public static boolean b(Context context) {
        Boolean bool = a;
        if (bool != null) {
            return bool.booleanValue();
        }
        a = Boolean.valueOf(a(context, "cn.jpush.android.intent.DActivity"));
        return a.booleanValue();
    }
}
