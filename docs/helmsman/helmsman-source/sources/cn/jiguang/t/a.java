package cn.jiguang.t;

import android.content.Context;
import android.os.Build;
import java.lang.reflect.Method;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    private static Class<?> a(Context context, String str) throws ClassNotFoundException {
        if (str.trim().length() == 0) {
            throw new ClassNotFoundException("class is empty");
        }
        boolean z = context != null;
        if (z && Build.VERSION.SDK_INT >= 29) {
            try {
                return context.getClassLoader().loadClass(str);
            } catch (ClassNotFoundException unused) {
            }
        }
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            cn.jiguang.w.a.d("IdProvider", String.format("loadClass fail hasContext= %s, errMsg = %s", Boolean.valueOf(z), e.getLocalizedMessage()));
            throw new ClassNotFoundException("loadClass fail ", e);
        }
    }

    public static String a(Context context) {
        try {
            Class<?> clsA = a(context, "com.android.id.impl.IdProviderImpl");
            Object objNewInstance = clsA.newInstance();
            clsA.getMethod("getUDID", Context.class);
            Method method = clsA.getMethod("getOAID", Context.class);
            Method method2 = clsA.getMethod("getVAID", Context.class);
            Object objInvoke = clsA.getMethod("getAAID", Context.class).invoke(objNewInstance, context);
            Object objInvoke2 = method2.invoke(objNewInstance, context);
            Object objInvoke3 = method.invoke(objNewInstance, context);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("oaid", objInvoke3);
            jSONObject.put("vaid", objInvoke2);
            jSONObject.put("aaid", objInvoke);
            return jSONObject.toString();
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdProvider", "getIds failed:" + th.getMessage());
            return "";
        }
    }
}
