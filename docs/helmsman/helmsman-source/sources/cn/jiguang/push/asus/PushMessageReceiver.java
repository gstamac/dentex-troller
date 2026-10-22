package cn.jiguang.push.asus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.f.c;
import cn.jpush.android.f.e;
import cn.jpush.android.i.b;
import com.facebook.GraphResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class PushMessageReceiver extends BroadcastReceiver {
    private static ExecutorService a;

    /* JADX WARN: Code duplicated, block: B:23:0x005d  */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private void a(Context context, String str, long j, JSONObject jSONObject) {
        int iOptInt;
        cn.jpush.android.c.a aVar;
        JSONObject jSONObject2;
        byte b = 5;
        if (jSONObject.has("cmd_msg_result")) {
            iOptInt = jSONObject.optInt("cmd_msg_result");
            if (iOptInt != 0) {
                b.c("PushMessageReceiver", "errorCode:" + iOptInt);
                if (iOptInt == 2) {
                    iOptInt = e.d;
                } else if (iOptInt == 5) {
                    iOptInt = e.c;
                } else if (iOptInt == 1) {
                    iOptInt = e.g;
                }
            }
            aVar = new cn.jpush.android.c.a(str, iOptInt);
        } else {
            iOptInt = 3;
            aVar = null;
        }
        if (jSONObject.has("cmd_msg_result_content")) {
            try {
                jSONObject2 = new JSONObject(jSONObject.getString("cmd_msg_result_content"));
            } catch (Throwable unused) {
                jSONObject2 = null;
            }
            if (jSONObject2 == null) {
                jSONObject2 = jSONObject;
            }
        } else {
            jSONObject2 = jSONObject;
        }
        if (TextUtils.isEmpty(str)) {
            a(context, aVar);
            return;
        }
        str.hashCode();
        switch (str.hashCode()) {
            case -530269543:
                b = !str.equals("ups.register") ? (byte) -1 : (byte) 0;
                break;
            case -129063684:
                b = !str.equals("ups.turnOff") ? (byte) -1 : (byte) 1;
                break;
            case -76940622:
                b = !str.equals("ups.unregister") ? (byte) -1 : (byte) 2;
                break;
            case 685741602:
                b = !str.equals("ups.pushStatus") ? (byte) -1 : (byte) 3;
                break;
            case 1306253107:
                b = !str.equals("ups.login") ? (byte) -1 : (byte) 4;
                break;
            case 2074046642:
                if (!str.equals("ups.turnOn")) {
                    b = -1;
                }
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                String strOptString = jSONObject2.optString("registrationId", "");
                cn.jpush.android.f.b bVar = new cn.jpush.android.f.b(str, j);
                cn.jpush.android.e.a aVarA = cn.jpush.android.f.a.a(bVar);
                if (aVarA != null) {
                    cn.jpush.android.f.a.b(bVar);
                }
                if (!TextUtils.isEmpty(strOptString)) {
                    b.b("PushMessageReceiver", "register succ.");
                    if (aVarA != null) {
                        aVarA.a(new cn.jpush.android.e.b(0, strOptString, "successs"));
                    }
                    a(context, strOptString);
                    cn.jpush.android.a.b.a(strOptString);
                    return;
                }
                if (iOptInt != 1) {
                    a(context, aVar);
                    if (aVarA != null) {
                        aVarA.a(new cn.jpush.android.e.b(aVar.a(), "", "failed"));
                        return;
                    }
                    return;
                }
                return;
            case 1:
            case 5:
                break;
            case 2:
                if (iOptInt == 0) {
                    cn.jpush.android.a.b.a("");
                }
                cn.jpush.android.f.b bVar2 = new cn.jpush.android.f.b(str, j);
                cn.jpush.android.e.a aVarA2 = cn.jpush.android.f.a.a(bVar2);
                if (aVarA2 != null) {
                    cn.jpush.android.f.a.b(bVar2);
                    aVarA2.a(new cn.jpush.android.e.b(iOptInt, "", iOptInt == 0 ? GraphResponse.SUCCESS_KEY : "failed"));
                    return;
                }
                return;
            case 3:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPushStopped", jSONObject2.optBoolean("isPushStopped", false));
                if (aVar != null) {
                    aVar.a(bundle);
                }
                break;
            case 4:
                b.b("PushMessageReceiver", "logined");
                if (TextUtils.isEmpty(c.b) || !TextUtils.isEmpty(cn.jpush.android.a.b.b(context))) {
                    return;
                }
                cn.jpush.android.h.a.a(context, c.b, null);
                return;
            default:
                b.d("PushMessageReceiver", "unkown cmd:" + str);
                return;
        }
        a(context, aVar);
    }

    public void a(Context context, long j, String str, String str2) {
        JSONObject jSONObject;
        b.a("PushMessageReceiver", "receive cmd:" + str + ",rid:" + j + ",content:" + str2);
        if (TextUtils.isEmpty(str2)) {
            jSONObject = null;
        } else {
            try {
                jSONObject = new JSONObject(str2);
            } catch (JSONException e) {
                e.printStackTrace();
                jSONObject = null;
            }
        }
        JSONObject jSONObject2 = jSONObject;
        if (jSONObject2 == null) {
            b.g("PushMessageReceiver", "handle json null");
            return;
        }
        try {
            a(context, str, j, jSONObject2);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    protected final void a(Context context, Intent intent) {
        if ("com.ups.push.PUSH_RESPONSE".equals(intent.getAction())) {
            a(context, intent.getLongExtra("message_rid", -1L), intent.getStringExtra("message_cmd"), intent.getStringExtra("message_content"));
        }
    }

    public abstract void a(Context context, cn.jpush.android.c.a aVar);

    public abstract void a(Context context, String str);

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (a == null) {
            a = Executors.newSingleThreadExecutor();
        }
        a.execute(new a(this, context, intent));
    }
}
