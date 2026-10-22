package cn.jpush.android.local;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.service.JCommonService;
import cn.jpush.android.service.JPushMessageReceiver;
import cn.jpush.android.t.a;

/* JADX INFO: loaded from: classes.dex */
public class JPushConstants {
    public static int BUILD_ID = 41;
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final int FORE_GROUND = 0;
    public static final int FORE_GROUND_NO = 1;
    public static final int FORE_GROUND_UNKNOWN = -1;
    public static final String HTTPS_PRE = "https://";
    public static final String HTTP_PRE = "http://";
    public static final boolean IG = true;
    public static String SDK_NAME = "";
    public static final String SDK_TYPE = "JPUSH";
    public static int SDK_VERSION_CODE = 358;
    public static String SDK_VERSION_NAME = "3.5.8";
    public static boolean SUPPORT_DY = true;
    private static final String TAG = "JPushConstants";
    public static boolean THIRD_ENABLE = true;
    private static final String USER_MESSAGE_RECEIVER_ACTION = "cn.jpush.android.intent.RECEIVE_MESSAGE";
    public static int isForeGround = -1;
    private static boolean isInitLife = false;
    private static Boolean isSameProcessWithJCommonService = null;
    private static boolean isTestAndroidQ = false;
    private static JPushMessageReceiver jPushMessageReceiver;
    public static Context mApplicationContext;
    private static String messageReceiverClass;
    private static String messageReceiverProcess;
    private static boolean tcpConnected;

    public static boolean canGetLbsInBackGround(Context context) {
        String str;
        if (isAndroidQ(context, true, "canGetLbsInBackGround")) {
            if (isForeGround != 1 || a.a(context, "android.permission.ACCESS_BACKGROUND_LOCATION")) {
                str = (isForeGround == -1 && !a.a(context, "android.permission.ACCESS_BACKGROUND_LOCATION")) ? "app is unkonwn foreground stat and no android.permission.ACCESS_BACKGROUND_LOCATION" : "app is not in foreground and no android.permission.ACCESS_BACKGROUND_LOCATION";
            }
            Logger.ww(TAG, str);
            return false;
        }
        return true;
    }

    public static void changeForegroudStat(Context context, boolean z) {
        Logger.d(TAG, "changeForegroudStat:" + z);
        isForeGround = !z ? 1 : 0;
        Bundle bundle = new Bundle();
        bundle.putBoolean("foreground", z);
        JCoreHelper.runActionWithService(context, SDK_TYPE, "change_foreground", bundle);
    }

    public static Context getAppContext(Context context) {
        if (mApplicationContext == null && context != null) {
            mApplicationContext = context.getApplicationContext();
        }
        return mApplicationContext;
    }

    public static String getMessageReceiverClass(Context context) {
        ComponentInfo componentInfoA;
        try {
            String str = messageReceiverClass;
            if (str != null) {
                return str;
            }
            Intent intent = new Intent();
            intent.setAction(USER_MESSAGE_RECEIVER_ACTION);
            intent.setPackage(context.getPackageName());
            for (ResolveInfo resolveInfo : context.getPackageManager().queryBroadcastReceivers(intent, 0)) {
                if (resolveInfo.activityInfo != null) {
                    String str2 = resolveInfo.activityInfo.name;
                    if (!TextUtils.isEmpty(str2)) {
                        Class<?> cls = Class.forName(str2);
                        if (JPushMessageReceiver.class.isAssignableFrom(cls)) {
                            messageReceiverClass = str2;
                            messageReceiverProcess = resolveInfo.activityInfo.processName;
                            Logger.ii(TAG, "found messageReceiverClass :" + messageReceiverClass + "at " + messageReceiverProcess + " by getCommonReceiverNames");
                            try {
                                String str3 = messageReceiverProcess;
                                if (str3 == null || !str3.equals(a.c(context))) {
                                    break;
                                    break;
                                }
                                jPushMessageReceiver = (JPushMessageReceiver) cls.newInstance();
                                break;
                            } catch (Throwable th) {
                                th.printStackTrace();
                                break;
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
        } catch (Throwable th2) {
            Logger.d(TAG, "getUserServiceClass failed:" + th2.getMessage());
        }
        if (TextUtils.isEmpty(messageReceiverClass) && (componentInfoA = a.a(context, context.getPackageName(), (Class<?>) JPushMessageReceiver.class)) != null) {
            messageReceiverClass = componentInfoA.name;
            messageReceiverProcess = componentInfoA.processName;
            Logger.ii(TAG, "found messageReceiverClass :" + messageReceiverClass + "at " + messageReceiverProcess + " by getComponentInfo");
            try {
                String str4 = messageReceiverProcess;
                if (str4 != null && str4.equals(a.c(context))) {
                    jPushMessageReceiver = (JPushMessageReceiver) Class.forName(messageReceiverClass).newInstance();
                }
            } catch (Throwable th3) {
                th3.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(messageReceiverClass)) {
            messageReceiverClass = "";
        }
        if (TextUtils.isEmpty(messageReceiverProcess)) {
            messageReceiverProcess = "";
        }
        return messageReceiverClass;
    }

    public static JPushMessageReceiver getPushMessageReceiver(Context context) {
        getMessageReceiverClass(getAppContext(context));
        return jPushMessageReceiver;
    }

    public static void init(Context context) {
        initPageLifecycle(context);
    }

    private static void initPageLifecycle(Context context) {
        String str;
        Logger.d(TAG, "initPageLifecycle init");
        if (isInitLife) {
            return;
        }
        try {
            isInitLife = true;
            if (context instanceof Application) {
                String strC = a.c(context);
                String packageName = context.getPackageName();
                if (strC == null || packageName == null || !context.getPackageName().equals(strC)) {
                    str = "need not initPageLifecycle in other process :" + strC;
                } else {
                    ((Application) context).registerActivityLifecycleCallbacks(new ActivityLifeCallBack());
                    str = "initPageLifecycle in main process,packageName:" + packageName + ",currentProcessName:" + strC;
                }
                Logger.d(TAG, str);
            }
        } catch (Throwable th) {
            Logger.ww(TAG, "registerActivityLifecycleCallbacks failed:" + th.getMessage());
        }
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
            boolean r3 = cn.jpush.android.local.JPushConstants.isTestAndroidQ
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
            boolean r4 = cn.jpush.android.local.JPushConstants.isTestAndroidQ
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
            java.lang.String r4 = "JPushConstants"
            cn.jpush.android.helper.Logger.d(r4, r3)
        L41:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.jpush.android.local.JPushConstants.isAndroidQ(android.content.Context, boolean, java.lang.String):boolean");
    }

    public static boolean isSameProcessWithJCommonService(Context context) {
        boolean z;
        try {
            Boolean bool = isSameProcessWithJCommonService;
            if (bool != null) {
                return bool.booleanValue();
            }
            if (messageReceiverProcess == null) {
                getMessageReceiverClass(context);
            }
            ComponentInfo componentInfoA = a.a(context, context.getPackageName(), (Class<?>) JCommonService.class);
            if (componentInfoA != null) {
                if (componentInfoA.processName.equals(messageReceiverProcess)) {
                    Logger.dd(TAG, "same process with JCommonService");
                    z = true;
                } else {
                    Logger.dd(TAG, "is not same process with JCommonService");
                    z = false;
                }
                isSameProcessWithJCommonService = z;
                return isSameProcessWithJCommonService.booleanValue();
            }
            return false;
        } catch (Throwable th) {
            Logger.w(TAG, "call isSameProcessWithJCommonService failed:" + th.getMessage());
        }
    }

    public static boolean isTcpConnected() {
        return tcpConnected;
    }

    public static void setTcpConnected(boolean z) {
        tcpConnected = z;
    }

    public static void testAndroidQ() {
    }
}
