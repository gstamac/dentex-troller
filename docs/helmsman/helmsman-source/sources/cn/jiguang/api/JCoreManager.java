package cn.jiguang.api;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.d.a;
import cn.jiguang.g.b;
import cn.jiguang.internal.ActionManager;
import cn.jiguang.internal.JConstants;
import cn.jiguang.internal.JCoreInternalHelper;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class JCoreManager {
    private static final String TAG = "JCoreManager";
    private static final AtomicBoolean isInited = new AtomicBoolean();

    public static void addDispatchAction(String str, String str2) {
        ActionManager.getInstance().addAction(str, str2);
    }

    public static void changeLiveStatus(boolean z) {
    }

    public static Context getAppContext(Context context) {
        return JConstants.getAppContext(context);
    }

    @Deprecated
    public static boolean getConnectionState(Context context) {
        Object objOnEvent = JCoreInternalHelper.getInstance().onEvent(context, JConstants.SDK_TYPE, 67, false, null, null, new Object[0]);
        if (objOnEvent instanceof Boolean) {
            return ((Boolean) objOnEvent).booleanValue();
        }
        return false;
    }

    public static boolean getDebugMode() {
        return JConstants.DEBUG_MODE;
    }

    public static void init(Context context) {
        Context appContext = JConstants.getAppContext(context);
        if (appContext == null) {
            return;
        }
        JConstants.isCallInit.set(true);
        AtomicBoolean atomicBoolean = isInited;
        if (atomicBoolean.get()) {
            return;
        }
        atomicBoolean.set(true);
        a.a(appContext);
        JCoreInternalHelper.getInstance().onEvent(appContext, JConstants.SDK_TYPE, 10, true, "tcp_a1", null, new Object[0]);
    }

    public static void initCrashHandler(Context context) {
        onEvent(context, JConstants.SDK_TYPE, 70, true, null, null, new Object[0]);
    }

    public static boolean isInternal() {
        return JConstants.INTERNAL_USE;
    }

    public static boolean isTestEnv() {
        return JConstants.isTestEnv();
    }

    /* JADX WARN: Code duplicated, block: B:31:0x003e A[FALL_THROUGH] */
    public static Object onEvent(Context context, String str, int i, String str2, Bundle bundle, Object... objArr) {
        boolean z;
        if (i != 13 && i != 49 && i != 53 && i != 58 && i != 60 && i != 25 && i != 26 && i != 42 && i != 43 && i != 63 && i != 64) {
            switch (i) {
                default:
                    switch (i) {
                        default:
                            switch (i) {
                                default:
                                    switch (i) {
                                        default:
                                            switch (i) {
                                                case 67:
                                                case 68:
                                                case 69:
                                                    break;
                                                default:
                                                    z = true;
                                                    break;
                                            }
                                        case 45:
                                        case 46:
                                        case 47:
                                            z = false;
                                            break;
                                    }
                                case 32:
                                case 33:
                                case 34:
                                    z = false;
                                    break;
                            }
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                            z = false;
                            break;
                    }
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    z = false;
                    break;
            }
        } else {
            z = false;
        }
        return onEvent(context, str, i, z, str2, bundle, objArr);
    }

    public static Object onEvent(Context context, String str, int i, boolean z, String str2, Bundle bundle, Object... objArr) {
        if (i != 18 && i != 13) {
            init(context);
        }
        return JCoreInternalHelper.getInstance().onEvent(context, str, i, z, str2, bundle, objArr);
    }

    public static void register(Context context, String str) {
        try {
            JConstants.isCallInit.set(true);
            a.a(context);
            String strB = cn.jiguang.ac.a.b(context);
            if (TextUtils.isEmpty(strB) || strB.equals(str)) {
                JConstants.APP_KEY = str;
            } else {
                d.g(TAG, "[register] not same appkey with manifest,please check it");
            }
            Bundle bundle = new Bundle();
            bundle.putString("appkey", str);
            JCoreInternalHelper.getInstance().onEvent(context, JConstants.SDK_TYPE, 10, true, "a5", bundle, new Object[0]);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void requestPermission(Context context) {
        String str;
        if (context == null) {
            str = "[requestPermission] context was null";
        } else if (!(context instanceof Activity)) {
            str = "[requestPermission] context must instanceof Activity";
        } else {
            if (context.getApplicationInfo().targetSdkVersion >= 23) {
                if (context.getApplicationInfo().targetSdkVersion >= 23) {
                    try {
                        LinkedList linkedList = new LinkedList();
                        linkedList.add("android.permission.WRITE_EXTERNAL_STORAGE");
                        linkedList.add("android.permission.READ_EXTERNAL_STORAGE");
                        linkedList.add("android.permission.ACCESS_FINE_LOCATION");
                        linkedList.add("android.permission.READ_PHONE_STATE");
                        if (JConstants.isAndroidQ(context, false, "will request background location permission")) {
                            linkedList.add("android.permission.ACCESS_BACKGROUND_LOCATION");
                        }
                        List<String> listA = cn.jiguang.f.a.a(context, linkedList);
                        if (listA != null && !listA.isEmpty()) {
                            d.a(TAG, "lackPermissions:" + listA);
                            Class.forName("android.app.Activity").getDeclaredMethod("requestPermissions", String[].class, Integer.TYPE).invoke(context, listA.toArray(new String[listA.size()]), 1);
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        d.g(TAG, "#unexcepted - requestPermission e:" + e);
                        return;
                    }
                }
                return;
            }
            str = "[requestPermission] app targetSdkVersion<23";
        }
        d.g(TAG, str);
    }

    public static void setAnalysisAction(JAnalyticsAction jAnalyticsAction) {
        JConstants.jAnalyticsAction = jAnalyticsAction;
    }

    public static void setDebugMode(boolean z) {
        JConstants.DEBUG_MODE = z;
    }

    public static void setLBSEnable(Context context, boolean z) {
        d.b(TAG, "action - setLBSEnable-control:" + z);
        if (!z) {
            try {
                b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.z().a(false)});
            } catch (Throwable unused) {
            }
        }
        onEvent(context, null, 27, null, null, Boolean.valueOf(z));
    }

    public static void setSDKConfigs(Context context, Bundle bundle) {
        JCoreInternalHelper.getInstance().onEvent(context, JConstants.SDK_TYPE, 55, true, null, bundle, new Object[0]);
    }

    public static void stopCrashHandler(Context context) {
        onEvent(context, JConstants.SDK_TYPE, 71, true, null, null, new Object[0]);
    }

    public static void unRegister(Context context) {
        JConstants.isCallInit.set(true);
        a.a(context);
        JCoreInternalHelper.getInstance().onEvent(context, JConstants.SDK_TYPE, 10, true, "tcp_a23", null, new Object[0]);
    }
}
