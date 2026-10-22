package cn.jpush.android.helper;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.api.JCoreManager;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.local.JPushConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class c {
    public static void a(Context context, JSONObject jSONObject) {
        if (context == null) {
            throw new IllegalArgumentException("NULL context");
        }
        if (jSONObject == null || jSONObject.length() <= 0) {
            return;
        }
        Logger.d("JPushReportHelper", "action:reportOperation - content:" + jSONObject.toString());
        JCoreManager.onEvent(context, JPushConstants.SDK_TYPE, 14, null, null, jSONObject);
    }

    public static void a(String str, int i, Context context) {
        a(str, i, null, context);
    }

    public static void a(String str, int i, String str2, Context context) {
        if (context == null) {
            Logger.d("JPushReportHelper", "context did not init, return");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("action:reportActionResult - messageId: " + str + ", code: " + i + "-" + f.a(i));
        if (!TextUtils.isEmpty(str2)) {
            stringBuffer.append(" report content: " + str2);
        }
        Logger.d("JPushReportHelper", stringBuffer.toString());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(JThirdPlatFormInterface.KEY_MSG_ID, str);
            jSONObject.put("result", i);
            if (!TextUtils.isEmpty(str2)) {
                jSONObject.put("data", str2);
            }
            JSONObject jSONObjectFillBase = JCoreHelper.fillBase(context, jSONObject, "msg_status");
            jSONObjectFillBase.put("type", "msg_status");
            a(context, jSONObjectFillBase);
        } catch (JSONException unused) {
        }
    }

    public static void a(String str, String str2, byte b, int i, Context context) {
        if (context == null) {
            Logger.d("JPushReportHelper", "context did not init, return");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("action:reportThirdSDKMsgActionResult - messageId: " + str + ", code: " + i);
        Logger.d("JPushReportHelper", stringBuffer.toString());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(JThirdPlatFormInterface.KEY_MSG_ID, str);
            jSONObject.put("tmsg_id", str2);
            jSONObject.put("result", i);
            jSONObject.put("sdk_type", (int) b);
            JSONObject jSONObjectFillBase = JCoreHelper.fillBase(context, jSONObject, "third_msg_status");
            jSONObjectFillBase.put("type", "third_msg_status");
            a(context, jSONObjectFillBase);
        } catch (JSONException unused) {
        }
    }
}
