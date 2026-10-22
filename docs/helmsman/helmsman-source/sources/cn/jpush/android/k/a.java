package cn.jpush.android.k;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.api.CallBackParams;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.cache.Key;
import cn.jpush.android.cache.Sp;
import cn.jpush.android.data.JPushLocalNotification;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.helper.d;
import cn.jpush.android.local.JPushAction;
import cn.jpush.android.local.JPushConstants;
import cn.jpush.android.local.JPushResponse;
import cn.jpush.android.local.ProxyActivityAction;
import cn.jpush.android.o.e;
import cn.jpush.android.q.c;
import cn.jpush.android.r.b;
import cn.jpush.android.service.JPushMessageReceiver;
import cn.jpush.android.service.SchedulerReceiver;
import cn.jpush.android.ups.UPSPushHelper;
import com.MAVLink.enums.MAV_CMD;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a extends JPushAction {
    private Boolean a;
    private Boolean b;
    private Context c;

    private static JSONObject a(ByteBuffer byteBuffer) {
        try {
            byteBuffer.getLong();
            byte[] bArr = new byte[byteBuffer.getShort()];
            byteBuffer.get(bArr);
            String str = new String(bArr, "UTF-8");
            Logger.d("JPushActionImpl", "parseBundle2Json content: ".concat(str));
            return new JSONObject(str);
        } catch (Exception e) {
            Logger.w("JPushActionImpl", "parseBundle2Json exception:" + e.getMessage());
            return null;
        }
    }

    private static void a(Context context, int i, long j) {
        if (i != 10) {
            switch (i) {
                case 26:
                    d.a().a(context, j, JPushInterface.ErrorCode.TIMEOUT);
                    break;
                case 27:
                    cn.jpush.android.s.a.a().b(context, j);
                    break;
            }
        }
        cn.jpush.android.r.a.a(context, b.a().a(j), JPushInterface.ErrorCode.TIMEOUT, j);
    }

    private static void a(Context context, Bundle bundle) {
        if (bundle == null) {
            Logger.w("JPushActionImpl", "[setMaxNotificationNum] bundle is bull");
            return;
        }
        int i = bundle.getInt("num");
        Logger.dd("JPushActionImpl", "action:setMaxNotificationNum :" + i);
        int iB = e.b();
        Logger.v("JPushActionImpl", "number in queue: " + iB);
        if (i < iB) {
            int i2 = iB - i;
            Logger.v("JPushActionImpl", "decreaseNotification:" + i2);
            cn.jpush.android.o.b.b(context, i2);
        }
        cn.jpush.android.cache.a.c(context, i);
    }

    private static void a(Context context, JPushResponse jPushResponse) {
        Logger.dd("JPushActionImpl", "action:handleMsg:" + jPushResponse);
        int cmd = jPushResponse.getCmd();
        if (cmd == 3) {
            c cVar = new c(jPushResponse);
            if (cn.jpush.android.cache.a.d(context)) {
                Logger.d("JPushActionImpl", "JPush was stoped");
                if (cVar.a() != 20) {
                    return;
                }
            }
            cn.jpush.android.d.b.a(context, cVar);
            return;
        }
        if (cmd != 10) {
            switch (cmd) {
                case 25:
                    JSONObject jSONObjectA = a(jPushResponse.getBody());
                    if (jSONObjectA != null) {
                        switch (jSONObjectA.optInt("cmd")) {
                            case 56:
                                cn.jpush.android.b.d.a(context).a(jSONObjectA);
                                break;
                            case 57:
                                break;
                            case 58:
                            default:
                                Logger.d("JPushActionImpl", "Unknown command for ctrl");
                                break;
                            case 59:
                                a(context, jSONObjectA);
                                break;
                            case 60:
                                cn.jpush.android.o.b.a(context, 2, true);
                                break;
                        }
                    }
                    break;
                case 26:
                    cn.jpush.android.q.a aVar = new cn.jpush.android.q.a(jPushResponse);
                    d.a().a(context, aVar.getRquestId(), aVar.a());
                    break;
                case 27:
                    cn.jpush.android.q.a aVar2 = new cn.jpush.android.q.a(jPushResponse);
                    if (aVar2.a() != 0) {
                        cn.jpush.android.s.a.a().a(context, jPushResponse.getRquestId(), aVar2.a());
                    } else {
                        cn.jpush.android.s.a.a().a(context, jPushResponse.getRquestId());
                    }
                    break;
                case 28:
                case 29:
                    break;
                default:
                    Logger.w("JPushActionImpl", "Unknown command for parsing inbound.");
                    break;
            }
            return;
        }
        cn.jpush.android.r.a.a(context, new cn.jpush.android.q.e(jPushResponse).a(), jPushResponse.getCmd() != 28 ? 2 : 1, jPushResponse.getRquestId());
    }

    private static void a(Context context, JSONObject jSONObject) {
        try {
            String strOptString = jSONObject.getJSONObject("content").optString("ids");
            if (TextUtils.isEmpty(strOptString)) {
                return;
            }
            String[] strArrSplit = strOptString.split(",");
            LinkedList linkedList = new LinkedList();
            for (String str : strArrSplit) {
                if (!TextUtils.isEmpty(str)) {
                    cn.jpush.android.o.b.c(context, cn.jpush.android.o.b.a(str));
                    linkedList.add(str);
                }
            }
            cn.jpush.android.o.b.a(context, linkedList);
        } catch (Throwable th) {
            Logger.w("JPushActionImpl", "dealCancelNotification e:" + th);
        }
    }

    private boolean a(Context context) {
        Boolean bool = this.a;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (context == null) {
            Logger.ww("JPushActionImpl", "context is null");
            return false;
        }
        this.c = context.getApplicationContext();
        Boolean boolValueOf = Boolean.valueOf(cn.jpush.android.t.a.g(context));
        this.a = boolValueOf;
        return boolValueOf.booleanValue();
    }

    private void b(Context context) {
        Logger.d("JPushActionImpl", "serviceInit...");
        try {
            if (this.b != null) {
                return;
            }
            this.b = true;
            int i = 2;
            if (JPushConstants.SUPPORT_DY && JConstants.SDK_VERSION_INT >= 220) {
                JCoreHelper.updateDy(context, 2);
            }
            cn.jpush.android.o.b.a(context, 0, true);
            c(context);
            Logger.d("JPushActionImpl", "google:true");
            int i2 = JConstants.SDK_VERSION_INT >= 220 ? 1 : 0;
            if (cn.jpush.android.l.a.a == JPushConstants.SDK_VERSION_CODE) {
                i = i2;
            }
            Logger.d("JPushActionImpl", "custom:1,dynamic:" + i);
            cn.jpush.android.o.d.a().a(context, null);
            JCoreHelper.reportSdkType(context, "push", 1, i, cn.jpush.android.l.a.a);
            if (JPushConstants.canGetLbsInBackGround(context)) {
                cn.jpush.android.b.d.a(context).a();
            }
        } catch (Throwable unused) {
        }
    }

    private static void b(Context context, Bundle bundle) {
        if (bundle == null) {
            Logger.w("JPushActionImpl", "[setSilenceTime] bundle is bull");
            return;
        }
        String string = bundle.getString("time");
        Logger.dd("JPushActionImpl", "action:setSilenceTime pushTime:" + string);
        cn.jpush.android.cache.a.c(context, string);
    }

    private void c(Context context) {
        try {
            Class.forName("cn.jpush.android.dy.JPushThirdDyManager").getDeclaredMethod("update", Context.class).invoke(null, context);
        } catch (Throwable unused) {
        }
    }

    private static void c(Context context, Bundle bundle) {
        if (bundle == null) {
            Logger.w("JPushActionImpl", "[setPushTime] bundle is bull");
            return;
        }
        cn.jpush.android.cache.a.b(context, true);
        String string = bundle.getString("time");
        if (!Pattern.compile("([0-6]{0,7})_((([0-9]|1[0-9]|2[0-3])\\^([0-9]|1[0-9]|2[0-3]))|(([0-9]|1[0-9]|2[0-3])\\^([0-9]|1[0-9]|2[0-3])-)+(([0-9]|1[0-9]|2[0-3])\\^([0-9]|1[0-9]|2[0-3])))").matcher(string).matches()) {
            Logger.ee("JPushActionImpl", "Invalid time format - " + string);
            return;
        }
        String strG = cn.jpush.android.cache.a.g(context);
        if (string.equals(strG)) {
            Logger.dd("JPushActionImpl", "Already SetPushTime, give up - " + strG);
        } else {
            Logger.dd("JPushActionImpl", "action:setPushTime pushTime:" + string);
            cn.jpush.android.cache.a.b(context, string);
        }
    }

    @Override // cn.jpush.android.local.JPushAction
    public Object beforLogin(Context context, String str, int i, String str2) {
        String str3;
        a(context);
        if (context == null) {
            str3 = "context was null";
        } else {
            if (!TextUtils.isEmpty(str2)) {
                if ("platformtype".equals(str2)) {
                    if (i >= 16) {
                        return Byte.valueOf(cn.jpush.android.s.c.a().e(context));
                    }
                } else if ("platformregid".equals(str2)) {
                    return cn.jpush.android.s.c.a().f(context);
                }
                return null;
            }
            str3 = " filed name was empty";
        }
        Logger.e("JPushActionImpl", str3);
        return null;
    }

    @Override // cn.jpush.android.local.JPushAction
    public ProxyActivityAction getPopWinActivity(Context context) {
        return new cn.jpush.android.ui.b();
    }

    @Override // cn.jpush.android.local.JPushAction
    public ProxyActivityAction getPushActivity(Context context) {
        return new cn.jpush.android.ui.c();
    }

    @Override // cn.jpush.android.local.JPushAction
    public String getSdkVersion(String str) {
        return cn.jpush.android.l.a.b;
    }

    @Override // cn.jpush.android.local.JPushAction
    public Object handleAction(Context context, String str, Object obj) {
        int i;
        Logger.d("JPushActionImpl", "handleAction:" + str);
        try {
            if (!a(context)) {
                return null;
            }
            Context context2 = context == null ? this.c : context;
            if (TextUtils.isEmpty(str)) {
                Logger.w("JPushActionImpl", "action is empty");
                return null;
            }
            Bundle bundle = obj instanceof Bundle ? (Bundle) obj : null;
            if (str.equals("init")) {
                b(context2);
                return null;
            }
            if (str.equals("resume")) {
                if (cn.jpush.android.cache.a.b(context2, 0)) {
                    cn.jpush.android.o.b.b(context2, 2006, 1, "set failed");
                    return null;
                }
                cn.jpush.android.o.b.b(context2, 2006, 0, "set success");
                cn.jpush.android.cache.a.a(context2, 0);
                JCoreManager.onEvent(context2, JPushConstants.SDK_TYPE, 1, null, null, new Object[0]);
                return null;
            }
            if (str.equals("stop")) {
                if (cn.jpush.android.cache.a.b(context2, 0)) {
                    cn.jpush.android.o.b.b(context2, 2007, 1, "set failed");
                    return null;
                }
                Logger.dd("JPushActionImpl", "The service is stopped, it will give up all the actions until you call resumePush method to resume the service");
                cn.jpush.android.o.b.b(context2, 2007, 0, "set success");
                cn.jpush.android.cache.a.a(context2, 1);
                JCoreManager.onEvent(context2, JPushConstants.SDK_TYPE, 0, null, null, new Object[0]);
                return null;
            }
            if (str.equals("clear_all_notify")) {
                cn.jpush.android.o.b.a(context2);
                return null;
            }
            if (str.equals("disable_push")) {
                cn.jpush.android.cache.a.b(context2, false);
                return null;
            }
            if (str.equals("geo_interval")) {
                if (bundle == null) {
                    return null;
                }
                long j = bundle.getLong("interval", -1L);
                if (j == -1) {
                    return null;
                }
                cn.jpush.android.b.d.a(context2).a(j);
                return null;
            }
            if (str.equals("max_num")) {
                a(context2, bundle);
                return null;
            }
            if (str.equals("set_mobile")) {
                d.a().a(context2, bundle);
                return null;
            }
            if (str.equals("pushtime")) {
                c(context2, bundle);
                return null;
            }
            if (str.equals("silenceTime")) {
                b(context2, bundle);
                return null;
            }
            if (str.equals("geo_fence_max_num")) {
                if (bundle == null || (i = bundle.getInt("num", -1)) <= 0) {
                    return null;
                }
                cn.jpush.android.b.d.a(context2).a(i);
                return null;
            }
            if (str.equals("enable_lbs")) {
                if (bundle == null) {
                    return null;
                }
                boolean z = bundle.getBoolean("enable", true);
                cn.jpush.android.cache.a.c(context2, z);
                if (z) {
                    Logger.dd("JPushActionImpl", "lbs is enabled");
                    cn.jpush.android.b.d.a(context2).a();
                    return null;
                }
                Logger.dd("JPushActionImpl", "lbs is disabled,stop geo");
                cn.jpush.android.b.d.a(context2).b();
                return null;
            }
            if (str.equals("third_enable")) {
                if (bundle == null) {
                    return null;
                }
                boolean z2 = bundle.getBoolean("enable", true);
                Logger.dd("JPushActionImpl", !context2.getPackageName().equals(cn.jpush.android.t.a.c(context2)) ? "third enable:" + z2 + ",last :" + cn.jpush.android.l.a.a() : "third enable:" + z2);
                try {
                    JPushConstants.THIRD_ENABLE = z2;
                } catch (Throwable unused) {
                }
                if (z2) {
                    cn.jpush.android.s.c.a().i(context2);
                    return null;
                }
                cn.jpush.android.s.c.a().g(context2);
                return null;
            }
            if (str.equals("delete_geo_fence")) {
                if (bundle == null) {
                    return null;
                }
                String string = bundle.getString("id");
                if (TextUtils.isEmpty(string)) {
                    return null;
                }
                cn.jpush.android.b.d.a(context2).a(string);
                return null;
            }
            if (str.equals("set_badge")) {
                if (bundle == null) {
                    return null;
                }
                int i2 = bundle.getInt("num");
                Sp.set(context2, Key.BadgeCurNum().set(Integer.valueOf(i2)));
                cn.jpush.android.o.b.a(context2, (Notification) null, i2, i2);
                return null;
            }
            if (str.equals("msg")) {
                if (!(obj instanceof JPushResponse)) {
                    return null;
                }
                a(context2, (JPushResponse) obj);
                return null;
            }
            if (str.equals("msg_time_out")) {
                if (bundle == null) {
                    return null;
                }
                a(context2, bundle.getInt("cmd", -1), bundle.getLong("rid", -1L));
                return null;
            }
            if (str.equals("tagalis")) {
                cn.jpush.android.r.a.a(context2, bundle);
                return null;
            }
            if (str.equals("add_local_notify")) {
                if (bundle == null) {
                    return null;
                }
                cn.jpush.android.m.a.a(context2).a(context2, (JPushLocalNotification) bundle.getSerializable("local_notify"));
                return null;
            }
            if (str.equals("rm_local_notify")) {
                if (bundle == null) {
                    return null;
                }
                cn.jpush.android.m.a.a(context2).a(context2, bundle.getLong("local_notify_ID"));
                return null;
            }
            if (str.equals("clear_local_notify")) {
                cn.jpush.android.m.a.a(context2).b(context2);
                return null;
            }
            if (str.equals("show_local_notify")) {
                if (cn.jpush.android.cache.a.d(context2)) {
                    Logger.dd("JPushActionImpl", "push has stoped");
                    return null;
                }
                if (bundle == null) {
                    return null;
                }
                String string2 = bundle.getString("local_notify_msg");
                if (TextUtils.isEmpty(string2)) {
                    Logger.d("JPushActionImpl", "local message is empty");
                    return null;
                }
                cn.jpush.android.d.d dVarA = cn.jpush.android.d.d.a(string2, context2.getPackageName(), JCoreHelper.getAppKey(context2), 0L);
                if (dVarA == null) {
                    return null;
                }
                dVarA.a(context2);
                cn.jpush.android.o.b.a(context2, dVarA);
                return null;
            }
            if (str.equals("set_custom_notify")) {
                if (bundle == null) {
                    return null;
                }
                int i3 = bundle.getInt("buidler_id", -1);
                String string3 = bundle.getString("buidler_string");
                Logger.d("JPushActionImpl", "builderId:" + i3 + ",buildString:" + string3);
                if (i3 < 0 || TextUtils.isEmpty(string3)) {
                    return null;
                }
                cn.jpush.android.cache.a.a(context2, i3 + "", string3);
                return null;
            }
            if ("third_push_upload_regid".equals(str)) {
                if (bundle == null) {
                    return null;
                }
                cn.jpush.android.s.c.a().a(context2, bundle);
                return null;
            }
            if (JThirdPlatFormInterface.ACTION_PLUGIN_PALTFORM_ON_MESSAGING.equals(str)) {
                String string4 = bundle.getString("appId");
                String string5 = bundle.getString("senderId");
                String string6 = bundle.getString("JMessageExtra");
                byte b = bundle.getByte(JThirdPlatFormInterface.KEY_PLATFORM);
                Logger.d("JPushActionImpl", "appId:" + string4 + ",senderId:" + string5 + ",JMessageExtra:" + string6);
                if (TextUtils.isEmpty(string6)) {
                    return null;
                }
                if (TextUtils.isEmpty(string4)) {
                    string4 = context2.getPackageName();
                }
                cn.jpush.android.d.b.a(context2, 2, string6, string4, TextUtils.isEmpty(string5) ? JCoreHelper.getAppKey(context2) : string5, 0L, b);
                return null;
            }
            if ("change_foreground".equals(str)) {
                if (bundle == null) {
                    return null;
                }
                boolean z3 = bundle.getBoolean("foreground");
                if (JPushConstants.isForeGround == -1 && z3) {
                    Logger.d("JPushActionImpl", "first in foreground");
                    cn.jpush.android.b.d.a(context2).a();
                }
                JPushConstants.isForeGround = z3 ? 0 : 1;
                Logger.d("JPushActionImpl", "change foregroud:" + JPushConstants.isForeGround);
                if (!z3) {
                    return null;
                }
                cn.jpush.android.o.b.a(context2, 1, false);
                return null;
            }
            if (str.equals("check_stop")) {
                boolean zD = cn.jpush.android.cache.a.d(context2);
                cn.jpush.android.o.b.b(context2, MAV_CMD.MAV_CMD_DO_TRIGGER_CONTROL, zD ? 1 : 0, zD ? "stopped" : "not stop");
                return null;
            }
            if (str.equals("get_connection")) {
                boolean zIsTcpConnected = JPushConstants.isTcpConnected();
                cn.jpush.android.o.b.b(context2, 2004, !zIsTcpConnected ? 1 : 0, zIsTcpConnected ? "connected" : "not connect");
                return null;
            }
            if (str.equals("get_rid")) {
                cn.jpush.android.o.b.b(context2, 2005, 0, JCoreHelper.getRegistrationID(context2));
                return null;
            }
            if ("jcore_on_event".equals(str)) {
                if (bundle == null) {
                    return null;
                }
                try {
                    int i4 = bundle.getInt("type");
                    int i5 = bundle.getInt(JThirdPlatFormInterface.KEY_CODE);
                    String string7 = bundle.getString("msg");
                    Logger.d("JPushActionImpl", "eventType:" + i4 + ",code:" + i5 + ",msg:" + string7);
                    if (i4 != -1 && i4 != 0 && i4 != 1 && i4 != 2000 && i4 != 2001 && i4 != 2002) {
                        if (i4 != 19) {
                            return null;
                        }
                        cn.jpush.android.m.a.a(context2).d(context2);
                        return null;
                    }
                    if (i4 == 2000 || i4 == 2001 || i4 == 0 || i4 == 2002) {
                        if (i4 == 2000) {
                            Sp.set(context2, Key.UPSRegister().set(false));
                            UPSPushHelper.upsCallBack(context2, 0, "ups.unregister", i5, string7);
                            return null;
                        }
                        Sp.set(context2, Key.UPSRegister().set(true));
                        if ((i5 != 1 && i4 == 2001) || i4 == 2002) {
                            UPSPushHelper.upsCallBack(context2, 0, "ups.register", i5, string7);
                            return null;
                        }
                        if (i4 == 0) {
                            UPSPushHelper.upsCallBack(context2, 0, "ups.register", i5, string7);
                        }
                    }
                    cn.jpush.android.o.b.b(context2, i4, i5, string7);
                    if (i4 == 1) {
                        JPushConstants.setTcpConnected(true);
                        cn.jpush.android.s.c.a().h(context2);
                        return null;
                    }
                    if (i4 != -1) {
                        return null;
                    }
                    JPushConstants.setTcpConnected(false);
                    return null;
                } catch (Throwable th) {
                    Logger.w("JPushActionImpl", "onEvent failed:" + th.getMessage());
                    return null;
                }
            }
            if ("handle_msg".equals(str)) {
                if (!(obj instanceof Intent)) {
                    return null;
                }
                Intent intent = (Intent) obj;
                String action = intent.getAction();
                Logger.dd("JPushActionImpl", "handle third message:" + action);
                if (!JThirdPlatFormInterface.ACTION_PLUGIN_PALTFORM_REFRESSH_REGID.equals(action)) {
                    return null;
                }
                cn.jpush.android.s.c.a().b(context2, intent != null ? intent.getExtras() : new Bundle());
                return null;
            }
            if ("third_init".equals(str)) {
                cn.jpush.android.s.c.a().b(context2);
                return null;
            }
            if ("third_resume".equals(str)) {
                cn.jpush.android.s.c.a().c(context2);
                return null;
            }
            if ("third_stop".equals(str)) {
                cn.jpush.android.s.c.a().d(context2);
                return null;
            }
            if ("third_action".equals(str)) {
                if (bundle == null) {
                    return null;
                }
                cn.jpush.android.s.c.a().a(context2, bundle.getString("third_key_action"), bundle);
                return null;
            }
            if (SchedulerReceiver.DELAY_NOTIFY.equals(str)) {
                cn.jpush.android.o.d.a().a(context2, null);
                return null;
            }
            if ("ups.turnOn".equals(str)) {
                if (bundle != null) {
                    int i6 = bundle.getInt(UPSPushHelper.KEY_RID);
                    if (cn.jpush.android.cache.a.b(context2, 1)) {
                        UPSPushHelper.upsCallBack(context2, i6, "ups.turnOn", 1, JCoreHelper.getRegistrationID(context2));
                        return null;
                    }
                    UPSPushHelper.upsCallBack(context2, i6, "ups.turnOn", 0, JCoreHelper.getRegistrationID(context2));
                }
                cn.jpush.android.cache.a.a(context2, true);
                JCoreManager.onEvent(context2, JPushConstants.SDK_TYPE, 1, null, null, new Object[0]);
                return null;
            }
            if ("ups.unregister".equals(str) || !"ups.turnOff".equals(str)) {
                return null;
            }
            if (bundle != null) {
                int i7 = bundle.getInt(UPSPushHelper.KEY_RID);
                if (cn.jpush.android.cache.a.b(context2, 1)) {
                    UPSPushHelper.upsCallBack(context2, i7, "ups.turnOff", 1, JCoreHelper.getRegistrationID(context2));
                    return null;
                }
                UPSPushHelper.upsCallBack(context2, i7, "ups.turnOff", 0, JCoreHelper.getRegistrationID(context2));
            }
            cn.jpush.android.cache.a.a(context2, false);
            JCoreManager.onEvent(context2, JPushConstants.SDK_TYPE, 0, null, null, new Object[0]);
            return null;
        } catch (Throwable th2) {
            Logger.e("JPushActionImpl", "handleAction failed:" + th2.getMessage());
            return null;
        }
    }

    @Override // cn.jpush.android.local.JPushAction
    public void handleNotificationIntent(Context context, Intent intent) {
        cn.jpush.android.d.d dVarA = cn.jpush.android.o.b.a(context, intent);
        Logger.i("JPushActionImpl", "handleNotificationIntent:" + dVarA);
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()) && dVarA.k != 1) {
            if (dVarA.X == 0) {
                cn.jpush.android.helper.c.a(dVarA.c, 1000, context);
            } else {
                JPushInterface.reportNotificationOpened(context, dVarA.c, dVarA.X);
            }
            int iD = cn.jpush.android.o.b.d(context, dVarA);
            Logger.d("JPushActionImpl", "is deep link:" + iD);
            if (iD == 2) {
                return;
            }
        }
        cn.jpush.android.o.b.a(context, intent.getAction(), dVarA, intent);
    }

    @Override // cn.jpush.android.local.JPushAction
    public void onJPushMessageReceive(Context context, JPushMessageReceiver jPushMessageReceiver, Intent intent) {
        cn.jpush.android.helper.a.a().a(context.getApplicationContext(), jPushMessageReceiver, intent);
    }

    @Override // cn.jpush.android.local.JPushAction
    public void onMessage(Context context, CustomMessage customMessage) {
        cn.jpush.android.helper.a.a(context, customMessage);
    }

    @Override // cn.jpush.android.local.JPushAction
    public void onMultiAction(Context context, Intent intent) {
        cn.jpush.android.helper.a.a(context, intent);
    }

    @Override // cn.jpush.android.local.JPushAction
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        cn.jpush.android.helper.a.a(context, notificationMessage, JPushInterface.ACTION_NOTIFICATION_RECEIVED);
    }

    @Override // cn.jpush.android.local.JPushAction
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        cn.jpush.android.helper.a.a(context, notificationMessage, JPushInterface.ACTION_NOTIFICATION_OPENED);
    }

    @Override // cn.jpush.android.local.JPushAction
    public void onTagAliasResponse(Context context, long j, int i, Intent intent) {
        cn.jpush.android.r.c.a().a(context.getApplicationContext(), j, i, intent);
    }

    @Override // cn.jpush.android.local.JPushAction
    public void setAliasAndTags(Context context, String str, Set<String> set, CallBackParams callBackParams) {
        cn.jpush.android.r.a.a(context, str, set, callBackParams);
    }
}
