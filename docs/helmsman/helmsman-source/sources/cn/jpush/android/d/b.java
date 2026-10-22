package cn.jpush.android.d;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.view.PointerIconCompat;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.local.JPushConstants;
import com.facebook.applinks.AppLinkData;
import com.facebook.share.internal.ShareConstants;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Locale;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static CustomMessage a(Intent intent) {
        CustomMessage customMessage = null;
        try {
            String stringExtra = intent.getStringExtra("data");
            String stringExtra2 = intent.getStringExtra("msgid");
            JSONObject jSONObject = new JSONObject(stringExtra);
            CustomMessage customMessage2 = new CustomMessage();
            try {
                customMessage2.messageId = stringExtra2;
                customMessage2.appId = intent.getStringExtra("appId");
                customMessage2.senderId = intent.getStringExtra("senderId");
                customMessage2.message = jSONObject.optString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, "");
                customMessage2.contentType = jSONObject.optString("content_type", "");
                customMessage2.title = jSONObject.optString("title", "");
                customMessage2.extra = jSONObject.optString(AppLinkData.ARGUMENTS_EXTRAS_KEY, "");
                return customMessage2;
            } catch (Throwable th) {
                th = th;
                customMessage = customMessage2;
                Logger.w("MessageHelper", "processMessage failed:" + th.getMessage());
                return customMessage;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static cn.jpush.android.b.b a(d dVar) {
        try {
            cn.jpush.android.b.b bVarA = cn.jpush.android.b.b.a(new JSONObject(dVar.g).optJSONObject("geofence"));
            if (bVarA == null) {
                return null;
            }
            bVarA.t = dVar;
            return bVarA;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static void a(Context context, int i, long j, long j2) {
        JCoreHelper.sendData(context, JPushConstants.SDK_TYPE, 4, 2, j2, cn.jpush.android.q.b.a(0, (byte) i, j));
    }

    public static void a(Context context, int i, String str, String str2, String str3, long j, byte b) {
        if (!str2.equals(context.getPackageName())) {
            Logger.e("MessageHelper", "msg appId is not mine pkgname,appid:" + str2 + ",pkgname:" + context.getPackageName());
            return;
        }
        Logger.d("MessageHelper", "action:receivedPushMessage msgId = " + j);
        d dVarA = d.a(str, str2, str3, j);
        if (dVarA == null || c.a(context, dVarA.a()) || cn.jpush.android.o.b.c(context, dVarA)) {
            return;
        }
        dVarA.X = b;
        cn.jpush.android.b.b bVarA = a(dVarA);
        if (bVarA == null || TextUtils.isEmpty(bVarA.a)) {
            a(context, dVarA);
        } else {
            cn.jpush.android.b.d.a(context).a(bVarA);
        }
    }

    public static void a(Context context, d dVar) {
        int i;
        Logger.d("MessageHelper", "processBasicEntity type:" + dVar.e);
        if (dVar.i) {
            i = dVar.h == 4 ? 3 : 1;
        } else {
            i = 2;
        }
        dVar.e = i;
        if ((dVar.e & 2) != 0) {
            Logger.d("MessageHelper", "processBasicEntity user-defined message.");
            if (TextUtils.isEmpty(dVar.l) && TextUtils.isEmpty(dVar.o)) {
                Logger.ww("MessageHelper", "no message or extra send to user");
            } else {
                b(context, dVar);
            }
        }
        if ((dVar.e & 1) != 0) {
            Logger.d("MessageHelper", "processBasicEntity notification");
            if (cn.jpush.android.cache.a.d(context)) {
                Logger.i("MessageHelper", "Service is stoped, give up all the message");
                return;
            }
            dVar.a(context);
            if (cn.jpush.android.o.b.c(context)) {
                cn.jpush.android.o.b.a(context, dVar);
            } else {
                cn.jpush.android.helper.c.a(dVar.c, 986, context);
                Logger.ii("MessageHelper", "push is invalidPushTime，Intercept the message");
            }
        }
    }

    public static void a(Context context, cn.jpush.android.q.c cVar) {
        a(context, cVar.a(), cVar.b(), cVar.getRid());
        long jB = cVar.b();
        int iA = cVar.a();
        String strC = cVar.c();
        Logger.d("MessageHelper", "msgType = " + iA + ", msgId = " + jB);
        Logger.v("MessageHelper", "msgContent: \n" + strC);
        Logger.d("MessageHelper", "msgContent size:" + strC.getBytes().length);
        LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(strC));
        try {
            String line = lineNumberReader.readLine();
            String line2 = lineNumberReader.readLine();
            if (line == null) {
                Logger.e("MessageHelper", "NO appId");
                return;
            }
            if (line2 == null) {
                Logger.e("MessageHelper", "NO senderId");
                return;
            }
            int length = line.length() + line2.length() + 2;
            if (strC.length() <= length + 1) {
                Logger.d("MessageHelper", "No msgContent");
                return;
            }
            String strSubstring = strC.substring(length);
            if (!line2.equals(JCoreHelper.getAppKey(context))) {
                Logger.e("MessageHelper", "msg senderId is not mine appkey,senderId:" + line2 + ",appkey:" + JCoreHelper.getAppKey(context));
                return;
            }
            Logger.v("MessageHelper", "Message Fields - appId:" + line + ", senderId:" + line2 + ", msgContent:" + strSubstring);
            if (iA != 0 && iA != 2) {
                if (iA == 20) {
                    cn.jpush.android.r.a.a(context, strSubstring, 0, -1L);
                    return;
                } else if (iA != 100 && iA != 101) {
                    Logger.ww("MessageHelper", "unkown msg type");
                    return;
                }
            }
            a(context, iA, strSubstring, line, line2, jB, (byte) 0);
        } catch (Throwable th) {
            Logger.e("MessageHelper", "Parse msgContent failed", th);
        }
    }

    public static void b(Context context, d dVar) {
        try {
            String messageReceiverClass = JPushConstants.getMessageReceiverClass(context);
            if (TextUtils.isEmpty(messageReceiverClass)) {
                Logger.dd("MessageHelper", "not found user push message,use old action to user");
                Intent intent = new Intent(JPushInterface.ACTION_MESSAGE_RECEIVED);
                intent.putExtra(JPushInterface.EXTRA_APP_KEY, dVar.b);
                intent.putExtra(JPushInterface.EXTRA_MESSAGE, dVar.l);
                intent.putExtra(JPushInterface.EXTRA_CONTENT_TYPE, dVar.m);
                intent.putExtra(JPushInterface.EXTRA_TITLE, dVar.n);
                intent.putExtra(JPushInterface.EXTRA_EXTRA, dVar.o);
                intent.putExtra(JPushInterface.EXTRA_MSG_ID, dVar.c);
                intent.addCategory(dVar.a);
                intent.setPackage(context.getPackageName());
                context.sendBroadcast(intent, String.format(Locale.ENGLISH, "%s.permission.JPUSH_MESSAGE", dVar.a));
                Logger.i("MessageHelper", "Send broadcast to app: " + String.format(Locale.ENGLISH, "%s.permission.JPUSH_MESSAGE", dVar.a));
            } else {
                Intent intent2 = new Intent(context, Class.forName(messageReceiverClass));
                intent2.setAction(cn.jpush.android.t.a.e(context, "custom_msg"));
                intent2.putExtra("data", dVar.g);
                intent2.putExtra("msgid", dVar.c);
                intent2.putExtra("appId", dVar.a);
                intent2.putExtra("senderId", dVar.b);
                if (JPushConstants.getPushMessageReceiver(context) != null) {
                    JPushConstants.getPushMessageReceiver(context).onMessage(context, a(intent2));
                } else {
                    context.sendBroadcast(intent2);
                }
            }
            if (dVar.X != 0) {
                cn.jpush.android.helper.c.a(dVar.c, "", dVar.X, PointerIconCompat.TYPE_ZOOM_IN, context);
            } else {
                cn.jpush.android.helper.c.a(dVar.c, PointerIconCompat.TYPE_ZOOM_IN, context);
            }
        } catch (Throwable th) {
            Logger.e("MessageHelper", "sendBroadcastToApp error:" + th.getMessage());
        }
    }
}
