package cn.jiguang.c;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jiguang.ad.d;
import cn.jiguang.ae.k;
import cn.jiguang.aj.e;
import cn.jiguang.analytics.page.PushSA;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.api.ReportCallBack;
import cn.jiguang.f.i;
import cn.jiguang.internal.ActionManager;
import cn.jiguang.internal.JConstants;
import cn.jiguang.internal.JCoreHelperAction;
import com.facebook.internal.ServerProtocol;
import io.reactivex.annotations.SchedulerSupport;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Set;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class b extends JCoreHelperAction {
    public boolean a = false;

    private static Bundle a(Context context, int i, Bundle bundle) {
        try {
            Bundle bundle2 = new Bundle();
            if (i == 4096) {
                if (bundle != null) {
                    cn.jiguang.ac.a.a(context, cn.jiguang.ac.a.a(bundle, "arg1"));
                }
                return bundle2;
            }
            if (i == 4098) {
                JCoreManager.init(context);
                return bundle2;
            }
            if (i == 36865 && JCoreManager.isInternal()) {
                JConstants.testAndroidQ();
            }
            return null;
        } catch (Throwable th) {
            d.h("JCoreHelper", "si e:" + th);
            return null;
        }
    }

    public static Object a(Context context) {
        return e.a(context, "deviceinfo", null);
    }

    private static void a(Context context, String str, Bundle bundle, String str2) {
        if (bundle != null) {
            bundle.putString("sdk_type", str);
            cn.jiguang.a.a.a(context, str2, bundle);
        }
    }

    public static void a(Context context, String str, String str2, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        d.c("JCoreHelper", "runActionWithService action:" + str2);
        bundle.putString("sdk_type", str);
        bundle.putString("internal_action", str2);
        cn.jiguang.a.a.a(context, "a3", bundle);
    }

    /* JADX WARN: Code duplicated, block: B:76:0x018a  */
    static /* synthetic */ void a(b bVar, Context context, Intent intent) {
        Method declaredMethod;
        Boolean bool;
        boolean zBooleanValue;
        d.b("JCoreHelper", "[handleReceiverIntent]:" + intent.getAction());
        e.a(context, "get_receiver", intent);
        String action = intent.getAction();
        if (action == null) {
            d.f("JCoreHelper", "onReceive empty action");
            return;
        }
        if (action.equals("android.intent.action.USER_PRESENT")) {
            d.d("JCoreHelper", "onReceiveandroid.intent.action.USER_PRESENT");
            cn.jiguang.a.a.a(context, true, 0L);
            return;
        }
        if (action.equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            if (networkInfo == null) {
                d.f("JCoreHelper", "Not found networkInfo");
                return;
            }
            d.b("JCoreHelper", "Connection state changed to - " + networkInfo.toString());
            if (2 == networkInfo.getType() || 3 == networkInfo.getType()) {
                d.b("JCoreHelper", "MMS or SUPL network state change, to do nothing!");
                return;
            }
            boolean booleanExtra = intent.getBooleanExtra("noConnectivity", false);
            Bundle extras = intent.getExtras();
            if (extras == null) {
                extras = new Bundle();
            }
            if (booleanExtra) {
                d.b("JCoreHelper", "No any network is connected");
                extras.putBoolean("connected", false);
            } else {
                try {
                    if (NetworkInfo.State.CONNECTED == networkInfo.getState()) {
                        d.b("JCoreHelper", "Network is connected.");
                        extras.putBoolean("connected", true);
                    } else if (NetworkInfo.State.DISCONNECTED == networkInfo.getState()) {
                        d.b("JCoreHelper", "Network is disconnected.");
                        extras.putBoolean("connected", false);
                    } else {
                        d.b("JCoreHelper", "other network state - " + networkInfo.getState() + ". Do nothing.");
                    }
                } catch (Throwable unused) {
                    extras.putBoolean("connected", cn.jiguang.f.a.d(context));
                }
            }
            cn.jiguang.a.a.a(context, "tcp_a15", extras);
            return;
        }
        if (!action.equals("android.os.action.DEVICE_IDLE_MODE_CHANGED") && !action.equals("android.os.action.POWER_SAVE_MODE_CHANGED")) {
            if (action.equals("noti_open_proxy") && intent.getBooleanExtra("debug_notification", false)) {
                String stringExtra = intent.getStringExtra("toastText");
                if (TextUtils.isEmpty(stringExtra)) {
                    return;
                }
                Toast toastMakeText = Toast.makeText(context, stringExtra, 0);
                try {
                    View view = toastMakeText.getView();
                    if (view instanceof LinearLayout) {
                        View childAt = ((LinearLayout) view).getChildAt(0);
                        if (childAt instanceof TextView) {
                            TextView textView = (TextView) childAt;
                            if (!i.a(stringExtra)) {
                                textView.setText(stringExtra);
                            }
                            textView.setTextSize(13.0f);
                        }
                    }
                } catch (Exception unused2) {
                }
                toastMakeText.show();
                return;
            }
            return;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (powerManager != null) {
            try {
                Class<?> cls = Class.forName("android.os.PowerManager");
                if (cls != null) {
                    if (action.equals("android.os.action.DEVICE_IDLE_MODE_CHANGED")) {
                        Method declaredMethod2 = cls.getDeclaredMethod("isDeviceIdleMode", new Class[0]);
                        if (declaredMethod2 != null) {
                            bool = (Boolean) declaredMethod2.invoke(powerManager, new Object[0]);
                            zBooleanValue = bool.booleanValue();
                        } else {
                            zBooleanValue = true;
                        }
                    } else if (!action.equals("android.os.action.POWER_SAVE_MODE_CHANGED") || (declaredMethod = cls.getDeclaredMethod("isPowerSaveMode", new Class[0])) == null) {
                        zBooleanValue = true;
                    } else {
                        bool = (Boolean) declaredMethod.invoke(powerManager, new Object[0]);
                        zBooleanValue = bool.booleanValue();
                    }
                    if (zBooleanValue) {
                        return;
                    }
                    d.b("JCoreHelper", "doze or powersave mode exit.");
                    cn.jiguang.a.a.a(context, true, 0L);
                }
            } catch (Throwable th) {
                d.h("JCoreHelper", "handle DEVICE_IDLE_MODE_CHANGED or POWER_SAVE_MODE_CHANGED fail:" + th);
            }
        }
    }

    public static Pair<String, Integer> b(Context context) {
        return cn.jiguang.ac.a.d(context);
    }

    private static boolean c(Context context) {
        try {
            Bundle bundleA = cn.jiguang.ac.e.a().a(context, "INTERNAL_API", "isTcpLoggedIn", null, cn.jiguang.ac.c.a(context));
            return (bundleA == null || !bundleA.containsKey(ServerProtocol.DIALOG_PARAM_STATE)) ? cn.jiguang.aj.i.a().d() : bundleA.getBoolean(ServerProtocol.DIALOG_PARAM_STATE);
        } catch (Throwable unused) {
            return false;
        }
    }

    @Override // cn.jiguang.internal.JCoreHelperAction
    public final Bundle directHandle(Context context, String str, String str2, Bundle bundle) {
        cn.jiguang.ac.e.a();
        return cn.jiguang.ac.e.b(context, str, str2, bundle);
    }

    @Override // cn.jiguang.internal.JCoreHelperAction
    public final Object onCommonMethod(Context context, String str, Object obj) {
        return e.a(context, str, obj);
    }

    @Override // cn.jiguang.internal.JCoreHelperAction
    public final Object onEvent(Context context, String str, int i, String str2, Bundle bundle, Object... objArr) {
        Bundle bundle2;
        String str3;
        Object obj;
        String str4;
        Bundle bundle3;
        String str5;
        Object obj2;
        Bundle bundle4;
        Boolean bool;
        try {
            Context appContext = JConstants.getAppContext(context);
            boolean zBooleanValue = false;
            switch (i) {
                case 0:
                    bundle2 = new Bundle();
                    bundle2.putString("sdk_type", str);
                    str3 = "tcp_a8";
                    cn.jiguang.a.a.a(appContext, str3, bundle2);
                    return null;
                case 1:
                    bundle2 = new Bundle();
                    bundle2.putString("sdk_type", str);
                    str3 = "tcp_a9";
                    cn.jiguang.a.a.a(appContext, str3, bundle2);
                    return null;
                case 2:
                    cn.jiguang.a.a.b(appContext, str2, bundle);
                    return null;
                case 3:
                    a(appContext, str, str2, bundle);
                    return null;
                case 4:
                    return cn.jiguang.d.a.b(appContext);
                case 5:
                    return cn.jiguang.d.a.c(appContext);
                case 6:
                    return cn.jiguang.ac.a.c(appContext);
                case 7:
                    return cn.jiguang.ac.a.a(appContext);
                case 8:
                    return cn.jiguang.d.a.e(appContext);
                case 9:
                    cn.jiguang.ac.a.a(appContext, str2);
                    return null;
                case 10:
                    cn.jiguang.a.a.a(appContext, str2, bundle);
                    return null;
                case 11:
                    if (objArr == null || objArr.length <= 0) {
                        return null;
                    }
                    Object obj3 = objArr[0];
                    if (!(obj3 instanceof Runnable)) {
                        return null;
                    }
                    cn.jiguang.aq.a.a(str2, (Runnable) obj3);
                    return null;
                case 12:
                    if (objArr == null || objArr.length <= 0) {
                        return null;
                    }
                    Object obj4 = objArr[0];
                    if (!(obj4 instanceof Runnable)) {
                        return null;
                    }
                    cn.jiguang.aq.a.a("ASYNC", (Runnable) obj4);
                    return null;
                case 13:
                case 32:
                case 33:
                case 52:
                case 64:
                case 65:
                case 69:
                default:
                    return null;
                case 14:
                    obj = (objArr == null || objArr.length <= 0) ? null : objArr[0];
                    k.a(appContext, str, obj);
                    return null;
                case 15:
                    if (objArr == null || objArr.length <= 1) {
                        return null;
                    }
                    Object obj5 = objArr[1];
                    if (!(obj5 instanceof ReportCallBack)) {
                        return null;
                    }
                    k.a(appContext, (JSONObject) objArr[0], (ReportCallBack) obj5);
                    return null;
                case 16:
                    str4 = "tcp_a3";
                    a(appContext, str, bundle, str4);
                    return null;
                case 17:
                    str4 = "tcp_a5";
                    a(appContext, str, bundle, str4);
                    return null;
                case 18:
                    if (objArr == null || objArr.length <= 2) {
                        return null;
                    }
                    if (objArr.length > 3) {
                        Object obj6 = objArr[3];
                        if (obj6 instanceof Throwable) {
                        }
                    }
                    cn.jiguang.ad.a.a(str, str2, ((Boolean) objArr[1]).booleanValue(), ((Integer) objArr[0]).intValue(), (String) objArr[2]);
                    return null;
                case 19:
                    if (objArr != null && objArr.length > 0) {
                        Object obj7 = objArr[0];
                        if (obj7 instanceof Long) {
                            return Long.valueOf(cn.jiguang.d.b.a(appContext, ((Long) obj7).longValue()));
                        }
                    }
                    return Long.valueOf(cn.jiguang.d.b.a(appContext, System.currentTimeMillis()));
                case 20:
                    return Long.valueOf(cn.jiguang.d.a.d(appContext));
                case 21:
                    return Boolean.valueOf(cn.jiguang.d.b.a(appContext));
                case 22:
                    cn.jiguang.g.b.a(appContext, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.m().a(str2)});
                    return null;
                case 23:
                    if (!JConstants.INTERNAL_USE) {
                        return null;
                    }
                    JConstants.testCountry = str2;
                    return null;
                case 24:
                    if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof Integer)) {
                        return null;
                    }
                    new Bundle().putInt("cmd", ((Integer) objArr[0]).intValue());
                    a(appContext, JConstants.SDK_TYPE, "old_cmd", (Bundle) null);
                    return null;
                case 25:
                    return Integer.valueOf(cn.jiguang.a.a.c);
                case 26:
                    if (objArr != null && objArr.length > 1) {
                        Object obj8 = objArr[0];
                        if ((obj8 instanceof JSONObject) && ((obj2 = objArr[1]) == null || (obj2 instanceof String))) {
                            return cn.jiguang.d.a.a(appContext, (JSONObject) obj8, (String) obj2);
                        }
                    }
                case 27:
                    if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof Boolean)) {
                        return null;
                    }
                    bundle3 = new Bundle();
                    bundle3.putBoolean("enable", ((Boolean) objArr[0]).booleanValue());
                    str5 = "lbsenable";
                    a(appContext, JConstants.SDK_TYPE, str5, bundle3);
                    return null;
                case 28:
                    if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof Long)) {
                        return null;
                    }
                    bundle3 = new Bundle();
                    bundle3.putLong("forenry", ((Long) objArr[0]).longValue());
                    str5 = "lbsforenry";
                    a(appContext, JConstants.SDK_TYPE, str5, bundle3);
                    return null;
                case 29:
                    if (objArr == null) {
                        return null;
                    }
                    if (!(objArr.length > 0) || !(objArr[0] instanceof Integer)) {
                        return null;
                    }
                    bundle4 = new Bundle();
                    bundle4.putInt("scence", ((Integer) objArr[0]).intValue());
                    a(appContext, JConstants.SDK_TYPE, "notification_state", bundle4);
                    return null;
                case 30:
                    if (objArr == null || objArr.length <= 0) {
                        return null;
                    }
                    Object obj9 = objArr[0];
                    if (obj9 instanceof Integer) {
                        return a(appContext, ((Integer) obj9).intValue(), bundle);
                    }
                    return null;
                case 31:
                    if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof Intent)) {
                        return null;
                    }
                    cn.jiguang.aq.a.a("PUSHRECEIVER_ACTION", new c(this, appContext, (Intent) objArr[0], str));
                    return null;
                case 34:
                    return cn.jiguang.d.a.f(appContext);
                case 35:
                    if (objArr == null || objArr.length <= 2 || !(objArr[0] instanceof Long) || !(objArr[1] instanceof String) || !(objArr[2] instanceof String)) {
                        return null;
                    }
                    cn.jiguang.g.b.a(appContext, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.c().a(Long.valueOf(((Long) objArr[0]).longValue())), cn.jiguang.g.a.e().a((String) objArr[1]), cn.jiguang.g.a.d().a((String) objArr[2])});
                    return null;
                case 36:
                    if (objArr == null || objArr.length <= 0) {
                        return null;
                    }
                    Object obj10 = objArr[0];
                    if (!(obj10 instanceof String)) {
                        return null;
                    }
                    cn.jiguang.ar.b.a(appContext, (String) obj10);
                    return null;
                case 37:
                    if (objArr == null || objArr.length <= 0) {
                        return null;
                    }
                    Object obj11 = objArr[0];
                    if (!(obj11 instanceof Long)) {
                        return null;
                    }
                    cn.jiguang.d.b.b(appContext, ((Long) obj11).longValue());
                    return null;
                case 38:
                    cn.jiguang.g.b.a(appContext, "cn.jiguang.sdk.user.profile");
                    return null;
                case 39:
                    k.a(appContext, objArr != null ? objArr[0] : null);
                    return null;
                case 40:
                    if (objArr == null || objArr.length <= 1) {
                        return null;
                    }
                    Object obj12 = objArr[0];
                    if (!(obj12 instanceof Boolean) || !(objArr[1] instanceof Long)) {
                        return null;
                    }
                    cn.jiguang.a.a.a(appContext, ((Boolean) obj12).booleanValue(), ((Long) objArr[1]).longValue());
                    return null;
                case 41:
                    if (objArr == null || objArr.length <= 1) {
                        return null;
                    }
                    Object obj13 = objArr[0];
                    if (!(obj13 instanceof String)) {
                        return null;
                    }
                    Object obj14 = objArr[1];
                    if (obj14 instanceof JSONObject) {
                        return Boolean.valueOf(k.a(appContext, (String) obj13, (JSONObject) obj14));
                    }
                    return null;
                case 42:
                    if (objArr == null || objArr.length <= 0) {
                        return null;
                    }
                    Object obj15 = objArr[0];
                    if (obj15 instanceof String) {
                        return cn.jiguang.d.a.a(appContext, (String) obj15);
                    }
                    return null;
                case 43:
                    return ActionManager.getActionMap();
                case 44:
                    if (objArr == null || objArr.length <= 1 || !(objArr[0] instanceof String) || !(objArr[1] instanceof Long)) {
                        return null;
                    }
                    cn.jiguang.g.b.a(appContext, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.i().a((String) objArr[0]), cn.jiguang.g.a.j().a(Long.valueOf(((Long) objArr[1]).longValue()))});
                    return null;
                case 45:
                    return cn.jiguang.d.a.g(appContext);
                case 46:
                    return Integer.valueOf(cn.jiguang.d.a.h(appContext));
                case 47:
                    return Integer.valueOf(cn.jiguang.d.a.j(appContext));
                case 48:
                    cn.jiguang.ae.d.a(appContext);
                    return null;
                case 49:
                    return true;
                case 50:
                    str4 = "tcp_a4";
                    a(appContext, str, bundle, str4);
                    return null;
                case 51:
                    if (objArr == null || objArr.length != 1 || !(objArr[0] instanceof Integer)) {
                        return null;
                    }
                    cn.jiguang.g.b.a(appContext, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.f().a(Integer.valueOf(((Integer) objArr[0]).intValue()))});
                    return null;
                case 53:
                    return Boolean.valueOf(cn.jiguang.ac.b.a().d());
                case 54:
                    if (objArr == null || objArr.length <= 2) {
                        return null;
                    }
                    String str6 = (String) objArr[0];
                    int iIntValue = ((Integer) objArr[1]).intValue();
                    int iIntValue2 = ((Integer) objArr[2]).intValue();
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("name", str6);
                    bundle5.putInt(SchedulerSupport.CUSTOM, iIntValue);
                    bundle5.putInt("dynamic", iIntValue2);
                    e.a(appContext, "set_sdktype_info", bundle5);
                    return null;
                case 55:
                    if (bundle == null || bundle.isEmpty()) {
                        return null;
                    }
                    try {
                        d.b("JCoreHelper", "setSDKConfigs");
                        int i2 = 86400;
                        try {
                            int i3 = bundle.getInt("heartbeat_interval", JConstants.DEFAULT_HEARTBEAT_INTERVAL);
                            if (i3 < 30) {
                                i3 = 30;
                            } else if (i3 > 86400) {
                                i3 = 86400;
                            }
                            JConstants.DEFAULT_HEARTBEAT_INTERVAL = i3;
                            d.b("JCoreHelper", "set heartbeat interval=" + i3);
                            break;
                        } catch (Throwable unused) {
                        }
                        try {
                            int i4 = bundle.getInt("alarm_delay", JConstants.DEFAULT_ALARM_DELAY);
                            if (i4 <= 86400) {
                                i2 = i4;
                            }
                            if (i2 < JConstants.DEFAULT_HEARTBEAT_INTERVAL + 5) {
                                i2 = JConstants.DEFAULT_HEARTBEAT_INTERVAL + 5;
                            }
                            JConstants.DEFAULT_ALARM_DELAY = i2;
                            d.b("JCoreHelper", "set alarm delay=" + i2);
                            break;
                        } catch (Throwable unused2) {
                        }
                        byte b = (byte) bundle.getInt("tcp_algorithm", -1);
                        if (b >= 0) {
                            JConstants.tcpAlgorithm = b;
                            d.b("JCoreHelper", "set tcp algorithm=" + ((int) b));
                        }
                        if (bundle.containsKey("tcp_report")) {
                            k.b = bundle.getBoolean("tcp_report");
                        }
                        int i5 = bundle.getInt("ipv_config", -1);
                        cn.jiguang.ak.c.a(i5);
                        if (i5 != 2 && i5 != 3) {
                            if (i5 != 0 && i5 != 1) {
                                return null;
                            }
                            k.c = true;
                            return null;
                        }
                        k.c = false;
                        return null;
                    } catch (Throwable unused3) {
                        return null;
                    }
                case 56:
                    if ("resume".equals(str2)) {
                        if (!JConstants.isInstrumentationHookFailed) {
                            return null;
                        }
                        PushSA.getInstance().onResume(appContext);
                        return null;
                    }
                    if ("pause".equals(str2)) {
                        if (!JConstants.isInstrumentationHookFailed) {
                            return null;
                        }
                        PushSA.getInstance().onPause(appContext);
                        return null;
                    }
                    if ("kill".equals(str2)) {
                        PushSA.getInstance().onKillProcess(appContext);
                        return null;
                    }
                    if ("enable".equals(str2)) {
                        if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof Boolean)) {
                            return null;
                        }
                        PushSA.getInstance().setStatEnable(((Boolean) objArr[0]).booleanValue());
                        return null;
                    }
                    if ("s_timeout".equals(str2)) {
                        if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof Long)) {
                            return null;
                        }
                        PushSA.getInstance().setInterval(((Long) objArr[0]).longValue());
                        return null;
                    }
                    if ("f_resume".equals(str2)) {
                        if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof String)) {
                            return null;
                        }
                        PushSA.getInstance().onFragmentResume(appContext, (String) objArr[0]);
                        return null;
                    }
                    if (!"f_pause".equals(str2) || objArr == null || objArr.length <= 0 || !(objArr[0] instanceof String)) {
                        return null;
                    }
                    PushSA.getInstance().onFragmentPause(appContext, (String) objArr[0]);
                    return null;
                case 57:
                    if (objArr == null || objArr.length <= 0 || !(objArr[0] instanceof Integer)) {
                        return null;
                    }
                    d.b("JCoreHelper", "SET_SHARE_PROCESS_STATE state:" + objArr[0]);
                    cn.jiguang.g.b.a(appContext, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.k().a(Integer.valueOf(((Integer) objArr[0]).intValue()))});
                    return null;
                case 58:
                    return Boolean.valueOf(cn.jiguang.d.a.i(appContext));
                case 59:
                    str4 = "tcp_a20";
                    a(appContext, str, bundle, str4);
                    return null;
                case 60:
                    String str7 = "unknown msg";
                    if (objArr != null && objArr.length > 0) {
                        Object obj16 = objArr[0];
                        if (obj16 instanceof Boolean) {
                            zBooleanValue = ((Boolean) obj16).booleanValue();
                        }
                    }
                    if (objArr != null && objArr.length > 1) {
                        Object obj17 = objArr[1];
                        if (obj17 instanceof String) {
                            str7 = (String) obj17;
                        }
                    }
                    return Boolean.valueOf(JConstants.isAndroidQ(appContext, zBooleanValue, str7));
                case 61:
                    if (objArr == null || objArr.length <= 6) {
                        return null;
                    }
                    k.a(appContext, ((Integer) objArr[0]).intValue(), (JSONObject) objArr[1], (byte[]) objArr[2], ((Integer) objArr[3]).intValue(), (File) objArr[4], (Set) objArr[5], (ReportCallBack) objArr[6]);
                    return null;
                case 62:
                    if (objArr == null || objArr.length <= 0) {
                        return null;
                    }
                    cn.jiguang.ac.e.a().a(appContext, (String) objArr[0], str2, bundle);
                    return null;
                case 63:
                    if (objArr == null || objArr.length <= 1) {
                        return null;
                    }
                    return cn.jiguang.ac.e.a().a(appContext, (String) objArr[0], str2, bundle, (String) objArr[1]);
                case 66:
                    if (objArr == null || objArr.length <= 0 || (bool = (Boolean) objArr[0]) == null) {
                        return null;
                    }
                    boolean zBooleanValue2 = bool.booleanValue();
                    d.b("JCoreHelper", "changeForegroudStat:" + zBooleanValue2);
                    this.a = zBooleanValue2;
                    Bundle bundle6 = new Bundle();
                    bundle6.putBoolean("foreground", this.a);
                    cn.jiguang.a.a.a(appContext, "a4", bundle6);
                    if (!bool.booleanValue()) {
                        obj = null;
                        k.a(appContext, (Object) null);
                        k.a(appContext, str, obj);
                        return null;
                    }
                    cn.jiguang.a.a.a(appContext, false, 0L);
                    bundle4 = new Bundle();
                    bundle4.putInt("scence", 1);
                    a(appContext, JConstants.SDK_TYPE, "notification_state", bundle4);
                    return null;
                case 67:
                    return Boolean.valueOf(c(appContext));
                case 68:
                    return e.a(appContext, "deviceinfo", null);
                case 70:
                    cn.jiguang.b.a aVarA = cn.jiguang.b.a.a();
                    if (aVarA.a) {
                        return null;
                    }
                    aVarA.a = true;
                    d.b("JPushCrashHandler", "init caughtException");
                    cn.jiguang.g.b.a(JConstants.getAppContext(appContext), (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.b().a(true)});
                    return null;
                case 71:
                    cn.jiguang.b.a aVarA2 = cn.jiguang.b.a.a();
                    if (!aVarA2.a) {
                        return null;
                    }
                    d.b("JPushCrashHandler", "stop caughtException");
                    aVarA2.a = false;
                    cn.jiguang.g.b.a(JConstants.getAppContext(appContext), (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.b().a(false)});
                    return null;
                case 72:
                    if (objArr == null || objArr.length != 4) {
                        return null;
                    }
                    Object obj18 = objArr[0];
                    if (!(obj18 instanceof String)) {
                        return null;
                    }
                    Object obj19 = objArr[1];
                    if (!(obj19 instanceof Integer) || !(objArr[2] instanceof Integer)) {
                        return null;
                    }
                    int iIntValue3 = ((Integer) obj19).intValue();
                    int iIntValue4 = ((Integer) objArr[2]).intValue();
                    int iIntValue5 = ((Integer) objArr[3]).intValue();
                    Bundle bundle7 = new Bundle();
                    bundle7.putString("name", (String) obj18);
                    bundle7.putInt(SchedulerSupport.CUSTOM, iIntValue3);
                    bundle7.putInt("dynamic", iIntValue4);
                    bundle7.putInt("sdk_v", iIntValue5);
                    e.a(appContext, "set_sdktype_info", bundle7);
                    return null;
            }
        } catch (Throwable th) {
            d.f("JCoreHelper", "onEvent:" + th);
            return null;
        }
    }
}
