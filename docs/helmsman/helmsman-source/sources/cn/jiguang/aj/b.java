package cn.jiguang.aj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.api.JDispatchAction;
import cn.jiguang.internal.ActionManager;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.api.JPushInterface;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    private static volatile b b;
    private static final Object c = new Object();
    public static HashMap<String, JDispatchAction> a = new HashMap<>();

    private b() {
        HashMap<String, String> actionMap = ActionManager.getActionMap();
        if (actionMap instanceof HashMap) {
            cn.jiguang.ad.d.b("DispatchActionManager", "actiom map size:" + actionMap.size());
            a(actionMap);
            b(JConstants.SDK_TYPE, cn.jiguang.c.a.class.getCanonicalName());
        }
    }

    public static byte a(Context context) {
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null) {
                Object objBeforLogin = value.beforLogin(context, entry.getKey(), 23, "platformtype");
                if (objBeforLogin instanceof Byte) {
                    return ((Byte) objBeforLogin).byteValue();
                }
            }
        }
        return (byte) 0;
    }

    private static int a(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        String[] strArrSplit = str.split("\\.");
        return (Integer.parseInt(strArrSplit[0]) << 16) + (Integer.parseInt(strArrSplit[1]) << 8) + Integer.parseInt(strArrSplit[2]);
    }

    public static b a() {
        if (b == null) {
            synchronized (c) {
                if (b == null) {
                    b = new b();
                }
            }
        }
        return b;
    }

    public static String a(String str, String str2) {
        StringBuilder sbAppend;
        String sdkVersion;
        JDispatchAction jDispatchAction = a.get(str);
        if (jDispatchAction != null) {
            sdkVersion = jDispatchAction.getSdkVersion(str);
            if (!TextUtils.isEmpty(sdkVersion)) {
                return sdkVersion;
            }
            sbAppend = new StringBuilder().append(str).append(" sdk action sdkversion:");
        } else {
            sbAppend = new StringBuilder().append(str);
            sdkVersion = " sdk action is null";
        }
        cn.jiguang.ad.d.a("DispatchActionManager", sbAppend.append(sdkVersion).toString());
        return str2;
    }

    public static void a(Context context, cn.jiguang.al.c cVar, ByteBuffer byteBuffer) {
        if (cVar == null) {
            cn.jiguang.ad.d.f("DispatchActionManager", "Action - dispatchMessage unexcepted - head was null");
            return;
        }
        d dVarB = o.a().b(cVar.e);
        if (dVarB != null) {
            cn.jiguang.ad.d.b("DispatchActionManager", "dispacth msg with reuqest :" + dVarB);
            JDispatchAction jDispatchAction = a.get(dVarB.c);
            if (jDispatchAction != null) {
                jDispatchAction.dispatchMessage(context, dVarB.c, cVar.c, cVar.b, cVar.e, dVarB.b, byteBuffer);
            }
            Bundle bundle = new Bundle();
            bundle.putLong("rid", cVar.e);
            i.a().a(context, "tcp_a7", bundle);
            return;
        }
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null && value.isSupportedCMD(entry.getKey(), cVar.c)) {
                value.dispatchMessage(context, entry.getKey(), cVar.c, cVar.b, cVar.e, -1L, byteBuffer);
            }
        }
    }

    public static void a(Context context, String str, long j, int i) {
        if (TextUtils.isEmpty(str) || !str.equals(JConstants.SDK_TYPE)) {
            JDispatchAction jDispatchAction = a.get(str);
            if (jDispatchAction != null) {
                jDispatchAction.dispatchTimeOutMessage(context, str, j, i);
                return;
            } else {
                cn.jiguang.ad.d.f("DispatchActionManager", "not found dispatch action by sdktype:" + str);
                return;
            }
        }
        if (i == 26) {
            q.a().a(j);
        } else if (i == 30 || i == 32) {
            cn.jiguang.e.a.a().a(context, i);
        }
    }

    public static void a(Context context, String str, Bundle bundle) {
        if (bundle == null) {
            cn.jiguang.ad.d.g("DispatchActionManager", "run action bundle is null");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            cn.jiguang.ad.d.g("DispatchActionManager", "run action sdktype is empty");
            return;
        }
        if (JConstants.SDK_TYPE.contains(str)) {
            str = JConstants.SDK_TYPE;
        }
        JDispatchAction jDispatchAction = a.get(str);
        if (jDispatchAction == null) {
            cn.jiguang.ad.d.g("DispatchActionManager", "dispacth action is null by sdktype:" + str);
        } else {
            jDispatchAction.onActionRun(context, str, bundle.getString("internal_action"), bundle);
        }
    }

    private void a(HashMap<String, String> map) {
        if (map == null || map.isEmpty()) {
            cn.jiguang.ad.d.f("DispatchActionManager", "init map is empty");
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            b(entry.getKey(), entry.getValue());
        }
    }

    public static boolean a(int i) {
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null) {
                try {
                    cn.jiguang.ad.d.c("DispatchActionManager", "isAllowAction actionType:0,sdktype:" + entry.getKey() + ",action:" + value.checkAction(entry.getKey(), 0));
                    if (!value.checkAction(entry.getKey(), 0)) {
                        return false;
                    }
                } catch (Throwable th) {
                    cn.jiguang.ad.d.f("DispatchActionManager", "isAllowAction error:" + th.getMessage());
                }
            }
        }
        return true;
    }

    public static String b(int i) {
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null && value.getRegPriority(entry.getKey()) == i) {
                return value.getSdkVersion(entry.getKey());
            }
        }
        return "";
    }

    public static String b(Context context) {
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null) {
                Object objBeforLogin = value.beforLogin(context, entry.getKey(), 23, "platformregid");
                if (objBeforLogin instanceof String) {
                    return (String) objBeforLogin;
                }
            }
        }
        return "";
    }

    public static short b() {
        short regFlag;
        short s = 0;
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null && (regFlag = value.getRegFlag(entry.getKey())) != 0) {
                s = (short) (s | regFlag);
            }
        }
        return s;
    }

    private static void b(String str, String str2) {
        cn.jiguang.ad.d.b("DispatchActionManager", "addAction type:" + str + ",action:" + str2);
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        if (a.containsKey(str)) {
            cn.jiguang.ad.d.b("DispatchActionManager", "has same type action");
            return;
        }
        try {
            Object objNewInstance = Class.forName(str2).newInstance();
            if (!(objNewInstance instanceof JDispatchAction)) {
                cn.jiguang.ad.d.f("DispatchActionManager", "this action is not a JDispatchAction,please check and extends JDispatchAction");
            } else {
                a.put(str, (JDispatchAction) objNewInstance);
                cn.jiguang.ad.d.b("DispatchActionManager", "action init:" + objNewInstance.getClass().getName());
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.g("DispatchActionManager", "#unexcepted - instance " + str2 + " class failed:" + th);
        }
    }

    public static short c() {
        short loginFlag;
        short s = 0;
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null && (loginFlag = value.getLoginFlag(entry.getKey())) != 0) {
                s = (short) (s | loginFlag);
            }
        }
        return s;
    }

    public static String d() {
        StringBuilder sbAppend;
        short s = 3;
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            short regPriority = entry.getValue().getRegPriority(entry.getKey());
            if (s < regPriority) {
                s = regPriority;
            }
        }
        cn.jiguang.ad.d.b("DispatchActionManager", "max reg priority:" + ((int) s));
        String string = "";
        for (int i = 0; i <= s; i++) {
            if (i == 3) {
                sbAppend = new StringBuilder().append(string).append(cn.jiguang.a.a.b);
            } else {
                for (Map.Entry<String, JDispatchAction> entry2 : a.entrySet()) {
                    JDispatchAction value = entry2.getValue();
                    if (value.getRegPriority(entry2.getKey()) == i) {
                        string = string + value.getSdkVersion(entry2.getKey());
                        break;
                    }
                }
                sbAppend = new StringBuilder().append(string);
            }
            string = sbAppend.append("|").toString();
        }
        return string.substring(0, string.length() - 1);
    }

    public static String e() {
        String str = a(cn.jiguang.a.a.b) + "|";
        short s = 0;
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            short logPriority = entry.getValue().getLogPriority(entry.getKey());
            if (s < logPriority) {
                s = logPriority;
            }
        }
        cn.jiguang.ad.d.b("DispatchActionManager", "max login priority:" + ((int) s));
        for (int i = 1; i <= s; i++) {
            for (Map.Entry<String, JDispatchAction> entry2 : a.entrySet()) {
                JDispatchAction value = entry2.getValue();
                if (value.getLogPriority(entry2.getKey()) == i) {
                    str = str + a(value.getSdkVersion(entry2.getKey()));
                    break;
                }
            }
            str = str + "|";
        }
        return str.substring(0, str.length() - 1);
    }

    public final void a(Context context, int i, int i2, String str) {
        Intent intent;
        for (Map.Entry<String, JDispatchAction> entry : a.entrySet()) {
            JDispatchAction value = entry.getValue();
            if (value != null) {
                value.onEvent(context, entry.getKey(), i, i2, str);
            }
        }
        try {
            if (i == 0 && i2 == 0) {
                intent = new Intent(JPushInterface.ACTION_REGISTRATION_ID);
                intent.putExtra(JPushInterface.EXTRA_REGISTRATION_ID, str);
            } else if (i == -1 || i == 1) {
                Intent intent2 = new Intent(JPushInterface.ACTION_CONNECTION_CHANGE);
                if (i == -1) {
                    intent2.putExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                } else {
                    intent2.putExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, true);
                }
                intent = intent2;
            } else {
                intent = null;
            }
            if (intent != null) {
                String packageName = context.getPackageName();
                intent.addCategory(packageName);
                intent.setPackage(packageName);
                cn.jiguang.f.a.a(context, intent);
            }
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("DispatchActionManager", "sendToOldPushUser failed:" + th.getMessage());
        }
    }
}
