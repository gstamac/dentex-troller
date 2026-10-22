package cn.jiguang.ac;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.aj.i;
import cn.jiguang.android.IDataShare;
import cn.jiguang.internal.ActionManager;
import cn.jiguang.internal.JConstants;
import cn.jiguang.internal.JCoreInternalHelper;
import cn.jpush.android.service.DataShare;
import cn.jpush.android.service.JCommonService;
import cn.jpush.android.service.PushReceiver;
import com.facebook.internal.ServerProtocol;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class e {
    private static e a;
    private static Map<String, String> b = new HashMap();

    private e() {
    }

    private static Bundle a(String str, String str2, Bundle bundle, String str3) {
        String str4 = str + "." + str2;
        try {
            IDataShare dataShare = DataShare.getInstance(str3);
            if (dataShare == null) {
                cn.jiguang.ad.d.b("JMessenger", str3 + "'s aidl not found, " + str4);
                return null;
            }
            cn.jiguang.ad.d.b("JMessenger", "sendByAidl, " + str4);
            if ("INTERNAL_API".equals(str)) {
                return dataShare.execute(str, str2, bundle);
            }
            dataShare.onAction(str, str2, bundle);
            return new Bundle();
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("JMessenger", "sendByAidl, " + str4 + ", e=" + th);
            return null;
        }
    }

    public static e a() {
        if (a == null) {
            synchronized (e.class) {
                if (a == null) {
                    a = new e();
                }
            }
        }
        return a;
    }

    private static boolean a(Context context, Class cls, String str, Bundle bundle) {
        if (cls == null) {
            return false;
        }
        String str2 = cls.getName() + "." + str;
        try {
            Intent intent = new Intent();
            intent.setClass(context, cls);
            intent.setAction(str);
            intent.setPackage(context.getPackageName());
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (BroadcastReceiver.class.isAssignableFrom(cls)) {
                context.sendBroadcast(intent);
                cn.jiguang.ad.d.b("JMessenger", "sendByReceiver, " + str2);
                return true;
            }
            if (Service.class.isAssignableFrom(cls)) {
                if (context.startService(intent) != null) {
                    cn.jiguang.ad.d.b("JMessenger", "sendByService, " + str2);
                    return true;
                }
                cn.jiguang.ad.d.f("JMessenger", "startService, " + str2 + ", can't find component");
            }
            return false;
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("JMessenger", "sendByComponent, " + str2 + " e=" + th);
        }
    }

    public static Bundle b(Context context, String str, String str2, Bundle bundle) {
        try {
            cn.jiguang.ad.d.b("JMessenger", "directHandle, " + str + "." + str2);
            if ("INTERNAL_API".equals(str)) {
                if (!str2.equals("isTcpLoggedIn")) {
                    cn.jiguang.ad.d.h("JMessenger", "directHandle, not support " + str + "." + str2);
                    return null;
                }
                boolean zD = i.a().d();
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean(ServerProtocol.DIALOG_PARAM_STATE, zD);
                return bundle2;
            }
            Class<?> cls = Class.forName(str);
            if (!BroadcastReceiver.class.isAssignableFrom(cls) && !Service.class.isAssignableFrom(cls)) {
                cn.jiguang.ad.d.h("JMessenger", "directHandle, not support " + str + "." + str2);
                return null;
            }
            if (JCommonService.class.isAssignableFrom(cls)) {
                cn.jiguang.ad.d.b("JMessenger", "JCommonService handle succeed," + str + "." + str2);
                JCoreInternalHelper.getInstance().onEvent(context, JConstants.SDK_TYPE, 2, true, str2, bundle, new Object[0]);
                return new Bundle();
            }
            Intent intent = new Intent(str2);
            intent.setClass(context, cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.setPackage(context.getPackageName());
            intent.addCategory(context.getPackageName());
            if (PushReceiver.class.isAssignableFrom(cls)) {
                cn.jiguang.ad.d.b("JMessenger", "PushReceiver handle succeed," + str + "." + str2);
                ActionManager.getInstance().handleMessage(context, bundle != null ? bundle.getString("sdktype") : null, intent);
            } else if (BroadcastReceiver.class.isAssignableFrom(cls)) {
                cn.jiguang.ad.d.b("JMessenger", "Receiver onReceive," + str + "." + str2);
                ((BroadcastReceiver) cls.newInstance()).onReceive(context, intent);
            } else {
                cn.jiguang.ad.d.b("JMessenger", "Service onStartCommand," + str + "." + str2);
                ((Service) cls.newInstance()).onStartCommand(intent, 0, 0);
            }
            return new Bundle();
        } catch (Throwable th) {
            cn.jiguang.ad.d.h("JMessenger", "directHandle e:" + th);
            return null;
        }
    }

    private static Bundle b(Context context, String str, String str2, Bundle bundle, String str3) {
        Map<String, String> map;
        String str4 = str + "." + str2;
        try {
            String str5 = b.get(str3);
            if (TextUtils.isEmpty(str5)) {
                String str6 = context.getPackageName() + JConstants.DATA_PROVIDER_SUFFIX;
                ProviderInfo providerInfoA = cn.jiguang.f.a.a(context, context.getPackageName(), str6);
                if (str3.equals(providerInfoA != null ? providerInfoA.processName : null)) {
                    str5 = "content://" + str6 + "/";
                    map = b;
                } else {
                    String str7 = context.getPackageName() + ".DownloadProvider";
                    ProviderInfo providerInfoA2 = cn.jiguang.f.a.a(context, context.getPackageName(), str7);
                    if (str3.equals(providerInfoA2 != null ? providerInfoA2.processName : null)) {
                        str5 = "content://" + str7 + "/";
                        map = b;
                    } else {
                        str5 = "";
                    }
                }
                map.put(str3, str5);
            }
            if (TextUtils.isEmpty(str5)) {
                cn.jiguang.ad.d.b("JMessenger", "uri is null, check provider config");
                return null;
            }
            Uri uri = Uri.parse(str5);
            cn.jiguang.ad.d.b("JMessenger", "sendByProvider, uri=" + str5 + ", " + str4);
            return context.getContentResolver().call(uri, str, str2, bundle);
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("JMessenger", "provider call:" + th);
        }
    }

    public final Bundle a(Context context, String str, String str2, Bundle bundle, String str3) {
        if (TextUtils.isEmpty(str3)) {
            return null;
        }
        String strA = cn.jiguang.f.a.a(context);
        if (TextUtils.isEmpty(strA)) {
            return null;
        }
        if (strA.equals(str3)) {
            return b(context, str, str2, bundle);
        }
        Bundle bundleA = a(str, str2, bundle, str3);
        if (bundleA != null) {
            return bundleA;
        }
        Bundle bundleB = b(context, str, str2, bundle, str3);
        if (bundleB != null) {
            return bundleB;
        }
        return null;
    }

    public final boolean a(Context context, String str, String str2, Bundle bundle) {
        Class<?> cls;
        String strA;
        String str3 = str + "." + str2;
        String strB = null;
        try {
            cls = Class.forName(str);
            try {
                if (Service.class.isAssignableFrom(cls)) {
                    strA = cn.jiguang.f.a.a(context, str);
                } else {
                    if (BroadcastReceiver.class.isAssignableFrom(cls)) {
                        strB = cn.jiguang.f.a.b(context, str);
                    }
                    strA = strB;
                }
            } catch (Throwable th) {
                th = th;
                cn.jiguang.ad.d.b("JMessenger", str3 + " meet e:" + th);
            }
        } catch (Throwable th2) {
            th = th2;
            cls = null;
        }
        Class<?> cls2 = cls;
        try {
            if (a(context, str, str2, bundle, strA) != null || a(context, cls2, str2, bundle)) {
                return true;
            }
        } catch (Throwable th3) {
            cn.jiguang.ad.d.f("JMessenger", "send, " + str3 + " meet e:" + th3);
        }
        cn.jiguang.ad.d.h("JMessenger", "send all failed, " + str3);
        return false;
    }
}
