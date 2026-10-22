package cn.jpush.android.d;

import android.content.Context;
import android.text.TextUtils;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.t.f;
import com.facebook.applinks.AppLinkData;
import com.facebook.share.internal.ShareConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class d implements Serializable {
    private static final long serialVersionUID = 8407025843324043625L;
    public String A;
    public String B;
    public String E;
    public String F;
    public String G;
    public String H;
    public String I;
    public int J;
    public String K;
    public String L;
    public int M;
    public String O;
    public String P;
    public String Q;
    public int R;
    public int T;
    public Integer Y;
    public String Z;
    public String a;
    public long aa;
    public String ab;
    public boolean ac;
    public boolean ad;
    public long ae;
    public String ah;
    public String ai;
    public String aj;
    public String ak;
    public String al;
    public String b;
    public String c;
    public int d;
    public int e;
    public String f;
    public String g;
    public int h;
    public boolean i;
    public int j;
    public String l;
    public String m;
    public String n;
    public String o;
    public int p;
    public boolean q;
    public int r;
    public String s;
    public String t;
    public String u;
    public int v;
    public String w;
    public String x;
    public String y;
    public int z;
    public int k = 0;
    public int C = -1;
    public String D = "";
    public boolean N = false;
    public int S = 0;
    public ArrayList<String> U = new ArrayList<>();
    public String V = "";
    public String W = "";
    public byte X = 0;
    public double af = 200.0d;
    public double ag = 200.0d;

    public static d a(Context context, String str, String str2) {
        String str3;
        d dVar = new d();
        byte b = 0;
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("_jmsgid_");
            dVar.c = strOptString;
            if (strOptString.isEmpty()) {
                dVar.c = jSONObject.optString(JThirdPlatFormInterface.KEY_MSG_ID);
            }
            dVar.X = (byte) jSONObject.optInt("rom_type");
            int iOptInt = jSONObject.optInt("show_type", -1);
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("m_content");
            if (jSONObjectOptJSONObject != null) {
                dVar.u = jSONObjectOptJSONObject.optString("n_content");
                dVar.s = jSONObjectOptJSONObject.optString("n_title");
                dVar.B = jSONObjectOptJSONObject.optString("n_extras");
                dVar.D = jSONObjectOptJSONObject.optString("n_channel_id");
                dVar.E = jSONObjectOptJSONObject.optString("n_display_foreground");
                dVar.C = jSONObjectOptJSONObject.optInt("n_alert_type");
                dVar.z = jSONObjectOptJSONObject.optInt("n_priority");
                dVar.E = jSONObjectOptJSONObject.optString("n_display_foreground");
                dVar.aj = jSONObjectOptJSONObject.optString("n_sound");
                dVar.r = jSONObjectOptJSONObject.optInt("n_flag", 1);
                JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject("rich_content");
                if (jSONObjectOptJSONObject2 != null) {
                    dVar.N = true;
                    dVar.a(jSONObjectOptJSONObject2);
                    dVar.h = 3;
                } else {
                    dVar.h = 4;
                    dVar.S = -1;
                }
            } else {
                dVar.u = jSONObject.optString("n_content");
                dVar.s = jSONObject.optString("n_title");
                dVar.B = jSONObject.optString("n_extras");
                dVar.D = jSONObject.optString("n_channel_id");
                dVar.E = jSONObject.optString("n_display_foreground");
                dVar.C = jSONObject.optInt("n_alert_type");
                dVar.z = jSONObject.optInt("n_priority");
                dVar.E = jSONObject.optString("n_display_foreground");
                dVar.X = (byte) jSONObject.optInt("rom_type");
                dVar.aj = jSONObject.optString("n_sound");
            }
            if (iOptInt != -1) {
                dVar.h = iOptInt;
            }
            dVar.p = 0;
            dVar.q = true;
            dVar.a = context.getPackageName();
            return dVar;
        } catch (Throwable th) {
            Logger.e("PushEntity", "parseContent error:" + th);
            if (TextUtils.isEmpty(dVar.c)) {
                str3 = "NO MSGID";
            } else {
                str3 = dVar.c;
                b = dVar.X;
            }
            cn.jpush.android.helper.c.a(str3, str2, b, 996, context);
            return null;
        }
    }

    public static d a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            d dVar = new d();
            dVar.a = jSONObject.optString("appId");
            dVar.b = jSONObject.optString("senderId");
            dVar.c = jSONObject.optString("messageId");
            dVar.d = jSONObject.optInt("notificationId");
            dVar.e = jSONObject.optInt("messageType");
            dVar.f = jSONObject.optString("overrideMessageId");
            dVar.h = jSONObject.optInt("messageVersion");
            dVar.i = jSONObject.optBoolean("notificationOnly");
            dVar.j = jSONObject.optInt("notificationBuilderId");
            dVar.k = jSONObject.optInt("notificationType");
            dVar.l = jSONObject.optString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE);
            dVar.m = jSONObject.optString("contentType");
            dVar.n = jSONObject.optString("title");
            dVar.o = jSONObject.optString(AppLinkData.ARGUMENTS_EXTRAS_KEY);
            dVar.p = jSONObject.optInt("type");
            dVar.q = jSONObject.optBoolean("isFullScreen");
            dVar.r = jSONObject.optInt("notificationRemoveMode");
            dVar.s = jSONObject.optString("notificationTitle");
            dVar.t = jSONObject.optString("notificationAppName");
            dVar.u = jSONObject.optString("notificationContent");
            dVar.v = jSONObject.optInt("notificationStyle");
            dVar.w = jSONObject.optString("notificationBigText");
            dVar.x = jSONObject.optString("notificationBigPicPath");
            dVar.y = jSONObject.optString("notificationInbox");
            dVar.z = jSONObject.optInt("notificationPriority");
            dVar.A = jSONObject.optString("notificationCategory");
            dVar.B = jSONObject.optString("notificationExtra");
            dVar.C = jSONObject.optInt("notificationAlertType");
            dVar.D = jSONObject.optString("notificationChannelId");
            dVar.E = jSONObject.optString("displayForeground");
            dVar.F = jSONObject.optString(ShareConstants.FEED_SOURCE_PARAM);
            dVar.G = jSONObject.optString("smallIcon");
            dVar.H = jSONObject.optString("largeIcon");
            dVar.I = jSONObject.optString("deeplink");
            dVar.J = jSONObject.optInt("failedAction");
            dVar.K = jSONObject.optString("failedLink");
            dVar.L = jSONObject.optString("targetPkgName");
            dVar.M = jSONObject.optInt("deeplinkBuilderId");
            dVar.N = jSONObject.optBoolean("isRichPush");
            dVar.O = jSONObject.optString("showUrl");
            dVar.P = jSONObject.optString("showTitle");
            dVar.Q = jSONObject.optString("_webPagePath");
            dVar.R = jSONObject.optInt("jumpMode");
            dVar.S = jSONObject.optInt("richType");
            dVar.T = jSONObject.optInt("showMode");
            try {
                String strOptString = jSONObject.optString("showResourceList");
                if (!TextUtils.isEmpty(strOptString)) {
                    dVar.U = new ArrayList<>(Arrays.asList(strOptString.split(",")));
                }
            } catch (Throwable th) {
                Logger.e("PushEntity", "showResourceList e:" + th);
            }
            dVar.V = jSONObject.optString("fromNum");
            dVar.W = jSONObject.optString("toNum");
            dVar.X = (byte) jSONObject.optInt(JThirdPlatFormInterface.KEY_PLATFORM);
            if (jSONObject.has("badgeAddNum")) {
                dVar.Y = Integer.valueOf(jSONObject.optInt("badgeAddNum"));
            }
            dVar.Z = jSONObject.optString("geofenceid");
            dVar.aa = jSONObject.optLong("radius");
            dVar.ab = jSONObject.optString("status");
            dVar.ac = jSONObject.optBoolean("repeat");
            dVar.ad = jSONObject.optBoolean("hasShow");
            dVar.ae = jSONObject.optLong("expiration");
            dVar.af = jSONObject.optDouble("longitude");
            dVar.ag = jSONObject.optDouble("latitude");
            dVar.ah = jSONObject.optString("lastGeoStatus");
            dVar.ai = jSONObject.optString("developerArg0");
            dVar.aj = jSONObject.optString("sound");
            dVar.ak = jSONObject.optString("showBeginTime");
            dVar.al = jSONObject.optString("showEndTime");
            return dVar;
        } catch (Throwable th2) {
            Logger.w("PushEntity", "parseJSONString e:" + th2);
            th2.printStackTrace();
            return null;
        }
    }

    public static d a(String str, String str2, String str3, long j) {
        d dVar = null;
        try {
            JSONObject jSONObject = new JSONObject(str);
            d dVar2 = new d();
            try {
                String strOptString = jSONObject.optString(JThirdPlatFormInterface.KEY_MSG_ID, "");
                if (TextUtils.isEmpty(strOptString)) {
                    strOptString = jSONObject.optString("ad_id", "");
                }
                if (TextUtils.isEmpty(strOptString)) {
                    strOptString = jSONObject.optString("_jmsgid_", "");
                }
                if (TextUtils.isEmpty(strOptString)) {
                    strOptString = j + "";
                }
                Logger.d("PushEntity", "preParseOriginalMsgMessage msgId = " + strOptString);
                dVar2.c = strOptString;
                dVar2.a = str2;
                dVar2.b = str3;
                boolean z = true;
                if (jSONObject.optInt("n_only", 0) != 1) {
                    z = false;
                }
                int iOptInt = z ? jSONObject.optInt("n_builder_id", 0) : 0;
                dVar2.i = z;
                dVar2.j = iOptInt;
                dVar2.g = str;
                dVar2.h = jSONObject.optInt("show_type", 3);
                dVar2.k = jSONObject.optInt("notificaion_type", 0);
                dVar2.f = jSONObject.optString("override_msg_id", "");
                dVar2.l = jSONObject.optString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, "");
                dVar2.m = jSONObject.optString("content_type", "");
                dVar2.n = jSONObject.optString("title", "");
                dVar2.o = jSONObject.optString(AppLinkData.ARGUMENTS_EXTRAS_KEY, "");
                return dVar2;
            } catch (Throwable th) {
                th = th;
                dVar = dVar2;
                Logger.w("PushEntity", "processMessage failed:" + th.getMessage());
                return dVar;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void a(JSONObject jSONObject) {
        this.N = true;
        this.O = jSONObject.optString("e_url", "").trim();
        this.P = jSONObject.optString("e_title", "").trim();
        if (!TextUtils.isEmpty(this.O) && !cn.jpush.android.t.a.a(this.O)) {
            this.O = "http://" + this.O;
            Logger.i("PushEntity", "Add http to non-prefix url: " + this.O);
        }
        this.S = jSONObject.optInt("e_rich_type", 0);
        this.R = jSONObject.optInt("e_jump_mode", 0);
        this.T = jSONObject.optInt("e_show", 0);
        int i = this.S;
        if (3 == i || 2 == i || 1 == i) {
            this.U = f.a(jSONObject.optJSONArray("e_eres"));
        }
        this.V = jSONObject.optString("from_num", "");
        this.W = jSONObject.optString("to_num", "");
    }

    public a a() {
        return new a(this);
    }

    public void a(Context context) {
        try {
            JSONObject jSONObjectOptJSONObject = new JSONObject(this.g).optJSONObject("m_content");
            int iOptInt = jSONObjectOptJSONObject.optInt("ad_t", 0);
            this.p = iOptInt;
            if (iOptInt != 0) {
                return;
            }
            this.q = jSONObjectOptJSONObject.optInt("full_screen", 0) >= 1;
            this.r = jSONObjectOptJSONObject.optInt("n_flag", 1);
            this.s = jSONObjectOptJSONObject.optString("n_title", "");
            this.t = jSONObjectOptJSONObject.optString("n_app_name", "");
            this.u = jSONObjectOptJSONObject.optString("n_content", "");
            this.v = jSONObjectOptJSONObject.optInt("n_style", 0);
            this.w = jSONObjectOptJSONObject.optString("n_big_text", "");
            this.x = jSONObjectOptJSONObject.optString("n_big_pic_path", "");
            this.y = jSONObjectOptJSONObject.optString("n_inbox", "");
            this.B = jSONObjectOptJSONObject.optString("n_extras", "");
            this.z = jSONObjectOptJSONObject.optInt("n_priority", 0);
            this.A = jSONObjectOptJSONObject.optString("n_category", "");
            this.C = jSONObjectOptJSONObject.optInt("n_alert_type", -1);
            this.D = jSONObjectOptJSONObject.optString("n_channel_id");
            this.E = jSONObjectOptJSONObject.optString("n_display_foreground");
            this.G = jSONObjectOptJSONObject.optString("n_small_icon", "");
            this.H = jSONObjectOptJSONObject.optString("n_large_icon", "");
            this.F = jSONObjectOptJSONObject.optString("n_source", "");
            JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject("n_intent");
            if (jSONObjectOptJSONObject2 != null) {
                this.I = jSONObjectOptJSONObject2.optString("n_url", "");
                this.J = jSONObjectOptJSONObject2.optInt("n_fail_handle_type", 0);
                this.K = jSONObjectOptJSONObject2.optString("n_fail_handle_url", "");
                this.L = jSONObjectOptJSONObject2.optString("n_package_name", "");
                this.M = jSONObjectOptJSONObject2.optInt("n_builder_id", 0);
            }
            if (TextUtils.isEmpty(this.s)) {
                Logger.dd("PushEntity", "Not found notificaiton title for developer mode. Use the application name.");
                this.s = cn.jpush.android.t.a.b(context);
            }
            JSONObject jSONObjectOptJSONObject3 = jSONObjectOptJSONObject.optJSONObject("ad_content");
            if (!jSONObjectOptJSONObject.isNull("ad_content") && jSONObjectOptJSONObject3 != null) {
                a(jSONObjectOptJSONObject3);
            }
            if (jSONObjectOptJSONObject.has("n_badge_add_num")) {
                this.Y = Integer.valueOf(jSONObjectOptJSONObject.optInt("n_badge_add_num"));
            } else {
                this.Y = null;
            }
            this.aj = jSONObjectOptJSONObject.optString("n_sound");
            this.ak = jSONObjectOptJSONObject.optString("n_show_begin_time");
            this.al = jSONObjectOptJSONObject.optString("n_show_end_time");
        } catch (Throwable th) {
            Logger.ww("PushEntity", "parse notification failed:" + th.getMessage());
        }
    }

    public NotificationMessage b() {
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.appkey = this.b;
        notificationMessage.msgId = this.c;
        notificationMessage.notificationAlertType = this.C;
        notificationMessage.notificationBigPicPath = this.x;
        notificationMessage.notificationBigText = this.w;
        notificationMessage.notificationBuilderId = this.j;
        notificationMessage.notificationCategory = this.A;
        notificationMessage.notificationContent = this.u;
        notificationMessage.notificationExtras = this.B;
        notificationMessage.notificationId = this.d;
        notificationMessage.notificationInbox = this.y;
        notificationMessage.notificationLargeIcon = this.H;
        notificationMessage.notificationPriority = this.z;
        notificationMessage.notificationSmallIcon = this.G;
        notificationMessage.notificationStyle = this.v;
        notificationMessage.notificationTitle = this.s;
        notificationMessage.appId = this.a;
        notificationMessage.notificationType = this.k;
        notificationMessage.developerArg0 = this.ai;
        notificationMessage.notificationChannelId = this.D;
        notificationMessage.isRichPush = this.N;
        notificationMessage.richType = this.S;
        notificationMessage._webPagePath = this.Q;
        notificationMessage.showResourceList = this.U;
        notificationMessage.platform = this.X;
        try {
            notificationMessage.displayForeground = this.E;
        } catch (Throwable unused) {
        }
        return notificationMessage;
    }

    public String c() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("appId", this.a);
            jSONObject.put("senderId", this.b);
            jSONObject.put("messageId", this.c);
            jSONObject.put("notificationId", this.d);
            jSONObject.put("messageType", this.e);
            jSONObject.put("overrideMessageId", this.f);
            jSONObject.put("messageVersion", this.h);
            jSONObject.put("notificationOnly", this.i);
            jSONObject.put("notificationBuilderId", this.j);
            jSONObject.put("notificationType", this.k);
            jSONObject.put(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, this.l);
            jSONObject.put("contentType", this.m);
            jSONObject.put("title", this.n);
            jSONObject.put(AppLinkData.ARGUMENTS_EXTRAS_KEY, this.o);
            jSONObject.put("type", this.p);
            jSONObject.put("isFullScreen", this.q);
            jSONObject.put("notificationRemoveMode", this.r);
            jSONObject.put("notificationTitle", this.s);
            jSONObject.put("notificationAppName", this.t);
            jSONObject.put("notificationContent", this.u);
            jSONObject.put("notificationStyle", this.v);
            jSONObject.put("notificationBigText", this.w);
            jSONObject.put("notificationBigPicPath", this.x);
            jSONObject.put("notificationInbox", this.y);
            jSONObject.put("notificationPriority", this.z);
            jSONObject.put("notificationCategory", this.A);
            jSONObject.put("notificationExtra", this.B);
            jSONObject.put("notificationAlertType", this.C);
            jSONObject.put("notificationChannelId", this.D);
            jSONObject.put("displayForeground", this.E);
            jSONObject.put(ShareConstants.FEED_SOURCE_PARAM, this.F);
            jSONObject.put("smallIcon", this.G);
            jSONObject.put("largeIcon", this.H);
            jSONObject.put("deeplink", this.I);
            jSONObject.put("failedAction", this.J);
            jSONObject.put("failedLink", this.K);
            jSONObject.put("targetPkgName", this.L);
            jSONObject.put("deeplinkBuilderId", this.M);
            jSONObject.put("isRichPush", this.N);
            jSONObject.put("showUrl", this.O);
            jSONObject.put("showTitle", this.P);
            jSONObject.put("_webPagePath", this.Q);
            jSONObject.put("jumpMode", this.R);
            jSONObject.put("richType", this.S);
            jSONObject.put("showMode", this.T);
            jSONObject.put("showResourceList", f.a(this.U, ","));
            jSONObject.put("fromNum", this.V);
            jSONObject.put("toNum", this.W);
            jSONObject.put(JThirdPlatFormInterface.KEY_PLATFORM, (int) this.X);
            jSONObject.put("badgeAddNum", this.Y);
            jSONObject.put("geofenceid", this.Z);
            jSONObject.put("radius", this.aa);
            jSONObject.put("status", this.ab);
            jSONObject.put("repeat", this.ac);
            jSONObject.put("hasShow", this.ad);
            jSONObject.put("expiration", this.ae);
            jSONObject.put("longitude", this.af);
            jSONObject.put("latitude", this.ag);
            jSONObject.put("lastGeoStatus", this.ah);
            jSONObject.put("developerArg0", this.ai);
            jSONObject.put("sound", this.aj);
            jSONObject.put("showBeginTime", this.ak);
            jSONObject.put("showEndTime", this.al);
            return jSONObject.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public String toString() {
        return "PushEntity{appId='" + this.a + "', senderId='" + this.b + "', messageId='" + this.c + "', notificationId=" + this.d + ", messageType=" + this.e + ", overrideMessageId='" + this.f + "', originalMessage='" + this.g + "', messageVersion=" + this.h + ", notificationOnly=" + this.i + ", notificationBuilderId=" + this.j + ", notificationType=" + this.k + ", message='" + this.l + "', contentType='" + this.m + "', title='" + this.n + "', extras='" + this.o + "', type=" + this.p + ", isFullScreen=" + this.q + ", notificationRemoveMode=" + this.r + ", notificationTitle='" + this.s + "', notificationAppName='" + this.t + "', notificationContent='" + this.u + "', notificationStyle=" + this.v + ", notificationBigText='" + this.w + "', notificationBigPicPath='" + this.x + "', notificationInbox='" + this.y + "', notificationPriority=" + this.z + ", notificationCategory='" + this.A + "', notificationExtra='" + this.B + "', notificationAlertType=" + this.C + ", notificationChannelId='" + this.D + "', displayForeground='" + this.E + "', source='" + this.F + "', smallIcon='" + this.G + "', largeIcon='" + this.H + "', deeplink='" + this.I + "', failedAction=" + this.J + ", failedLink='" + this.K + "', targetPkgName='" + this.L + "', deeplinkBuilderId=" + this.M + ", isRichPush=" + this.N + ", showUrl='" + this.O + "', showTitle='" + this.P + "', _webPagePath='" + this.Q + "', jumpMode=" + this.R + ", richType=" + this.S + ", showMode=" + this.T + ", showResourceList=" + this.U + ", fromNum='" + this.V + "', toNum='" + this.W + "', platform=" + ((int) this.X) + ", badgeAddNum=" + this.Y + ", geofenceid='" + this.Z + "', radius=" + this.aa + ", status='" + this.ab + "', repeat=" + this.ac + ", hasShow=" + this.ad + ", expiration=" + this.ae + ", longitude=" + this.af + ", latitude=" + this.ag + ", lastGeoStatus='" + this.ah + "', developerArg0='" + this.ai + "', sound='" + this.aj + "', showBeginTime='" + this.ak + "', showEndTime='" + this.al + "'}";
    }
}
