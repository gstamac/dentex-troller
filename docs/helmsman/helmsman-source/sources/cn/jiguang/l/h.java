package cn.jiguang.l;

import android.content.Context;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import com.facebook.internal.ServerProtocol;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class h {
    public static String a(int i) {
        cn.jiguang.aj.b.a();
        return cn.jiguang.aj.b.b(i);
    }

    public static void a(Context context, Object obj) {
        JCoreManager.onEvent(context, "JCOMMON", 39, null, null, obj);
    }

    public static void a(Context context, Object obj, Object obj2) {
        JCoreManager.onEvent(context, "JCOMMON", 15, null, null, obj, obj2);
    }

    public static void a(Context context, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("content");
            cn.jiguang.w.a.a("JCommonPresenter", "jsonContent:" + jSONObject2);
            int iOptInt = jSONObject2.optInt(ServerProtocol.DIALOG_PARAM_STATE, -1);
            if (iOptInt == -1) {
                cn.jiguang.w.a.d("JCommonPresenter", "unknow state");
                return;
            }
            if (iOptInt == 0) {
                cn.jiguang.w.a.a("JCommonPresenter", "turn on share process");
                cn.jiguang.af.b.a(context, iOptInt);
            } else {
                if (iOptInt != 1) {
                    cn.jiguang.w.a.d("JCommonPresenter", "#exception - unsupport state:" + iOptInt);
                    return;
                }
                cn.jiguang.w.a.a("JCommonPresenter", "turn off share process");
                cn.jiguang.af.b.a(context, iOptInt);
                cn.jiguang.e.a.a().a(context, cn.jiguang.d.a.d(context));
                cn.jiguang.e.a.a(context, context.getPackageName());
            }
        } catch (Exception e) {
            cn.jiguang.w.a.d("JCommonPresenter", "configReportRunningApp exception:" + e.getMessage());
        }
    }

    public static boolean a(Context context) {
        if (!JConstants.isAndroidQ(context, true, "canGetLbsInBackGround") || cn.jiguang.af.b.a || cn.jiguang.f.a.c(context, "android.permission.ACCESS_BACKGROUND_LOCATION")) {
            return true;
        }
        cn.jiguang.w.a.e("JCommonPresenter", "app is not in foreground and no android.permission.ACCESS_BACKGROUND_LOCATION");
        return false;
    }
}
