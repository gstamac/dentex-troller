package cn.jiguang.o;

import android.content.Context;
import android.text.TextUtils;
import cn.jiguang.api.JCoreManager;
import java.io.File;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static JSONObject a(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                synchronized (str) {
                    Object objOnEvent = JCoreManager.onEvent(context, "JCOMMON", 42, null, null, str);
                    if (!(objOnEvent instanceof JSONObject)) {
                        return null;
                    }
                    return (JSONObject) objOnEvent;
                }
            } catch (Throwable th) {
                cn.jiguang.w.a.d("JCommonFileHelper", "readJson throwable:" + th.getMessage());
            }
        }
        return null;
    }

    public static boolean a(Context context, String str, String str2) {
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                synchronized (str) {
                    File fileA = cn.jiguang.f.e.a(context, str);
                    if (fileA == null) {
                        return false;
                    }
                    return cn.jiguang.f.e.a(fileA, str2);
                }
            } catch (Throwable th) {
                cn.jiguang.w.a.d("JCommonFileHelper", "writeString throwable:" + th.getMessage());
            }
        }
        return false;
    }

    public static boolean a(Context context, String str, JSONObject jSONObject) {
        boolean zBooleanValue;
        if (context != null && !TextUtils.isEmpty(str) && jSONObject != null) {
            try {
                synchronized (str) {
                    Object objOnEvent = JCoreManager.onEvent(context, "JCOMMON", 41, null, null, str, jSONObject);
                    zBooleanValue = objOnEvent instanceof Boolean ? ((Boolean) objOnEvent).booleanValue() : false;
                }
                return zBooleanValue;
            } catch (Throwable th) {
                cn.jiguang.w.a.d("JCommonFileHelper", "writeJson throwable:" + th.getMessage());
            }
        }
        return false;
    }

    public static String b(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                synchronized (str) {
                    File fileA = cn.jiguang.f.e.a(context, str);
                    if (fileA == null) {
                        return null;
                    }
                    return cn.jiguang.f.e.b(fileA);
                }
            } catch (Throwable th) {
                cn.jiguang.w.a.d("JCommonFileHelper", "readString throwable:" + th.getMessage());
            }
        }
        return null;
    }
}
