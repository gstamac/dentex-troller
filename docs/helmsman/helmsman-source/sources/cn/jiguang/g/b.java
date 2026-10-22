package cn.jiguang.g;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import cn.jiguang.ar.d;
import cn.jiguang.internal.JConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    private static final Map<String, SharedPreferences> a = new ConcurrentHashMap();

    public static <T> T a(Context context, a<T> aVar) {
        T t = (T) b(context, aVar);
        return t != null ? t : aVar.e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> T a(SharedPreferences sharedPreferences, String str, T t) {
        if (sharedPreferences != null && sharedPreferences.contains(str)) {
            try {
                if (t instanceof Boolean) {
                    return (T) Boolean.valueOf(sharedPreferences.getBoolean(str, ((Boolean) t).booleanValue()));
                }
                if (t instanceof String) {
                    return (T) sharedPreferences.getString(str, (String) t);
                }
                if (t instanceof Integer) {
                    return (T) Integer.valueOf(sharedPreferences.getInt(str, ((Integer) t).intValue()));
                }
                if (t instanceof Long) {
                    return (T) Long.valueOf(sharedPreferences.getLong(str, ((Long) t).longValue()));
                }
                if (t instanceof Float) {
                    return (T) Float.valueOf(sharedPreferences.getFloat(str, ((Float) t).floatValue()));
                }
            } catch (Throwable unused) {
            }
        }
        return null;
    }

    private static <T> void a(Context context, a<T> aVar, a<T> aVar2) {
        T t;
        if (b(context, aVar) != null || (t = (T) b(context, aVar2)) == null) {
            return;
        }
        aVar.e = t;
        a(context, (a<?>[]) new a[]{aVar});
        aVar2.e = null;
        a(context, (a<?>[]) new a[]{aVar2});
    }

    public static void a(Context context, String str) {
        SharedPreferences sharedPreferencesB = b(context, str);
        if (sharedPreferencesB != null) {
            sharedPreferencesB.edit().clear().apply();
        }
    }

    public static void a(Context context, a<?>... aVarArr) {
        SharedPreferences sharedPreferencesB;
        if (aVarArr.length > 0) {
            HashMap map = new HashMap();
            for (a<?> aVar : aVarArr) {
                SharedPreferences.Editor editorEdit = (SharedPreferences.Editor) map.get(aVar.c);
                if (editorEdit == null && (sharedPreferencesB = b(context, aVar.c)) != null) {
                    editorEdit = sharedPreferencesB.edit();
                    map.put(aVar.c, editorEdit);
                }
                String str = aVar.d;
                Object objA = aVar.e;
                boolean z = aVar.g;
                if (editorEdit != null) {
                    if (objA == null) {
                        editorEdit.remove(str);
                    } else if (objA instanceof Boolean) {
                        editorEdit.putBoolean(str, ((Boolean) objA).booleanValue());
                    } else if (objA instanceof String) {
                        if (z) {
                            String str2 = (String) objA;
                            if (str2.length() > 0) {
                                objA = d.a(str2);
                            }
                        }
                        editorEdit.putString(str, (String) objA);
                    } else if (objA instanceof Integer) {
                        editorEdit.putInt(str, ((Integer) objA).intValue());
                    } else if (objA instanceof Long) {
                        editorEdit.putLong(str, ((Long) objA).longValue());
                    } else if (objA instanceof Float) {
                        editorEdit.putFloat(str, ((Float) objA).floatValue());
                    }
                }
            }
            Iterator it = map.values().iterator();
            while (it.hasNext()) {
                ((SharedPreferences.Editor) it.next()).commit();
            }
        }
    }

    private static SharedPreferences b(Context context, String str) {
        Context appContext;
        a aVarD;
        a aVarD2;
        String str2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Map<String, SharedPreferences> map = a;
        SharedPreferences sharedPreferences = map.get(str);
        if (sharedPreferences == null && (appContext = JConstants.getAppContext(context)) != null) {
            sharedPreferences = appContext.getSharedPreferences(str, 0);
            map.put(str, sharedPreferences);
            String str3 = (String) a(appContext, a.w());
            if (TextUtils.isEmpty(str3) || str3.startsWith("1.")) {
                if (str.equals(a.a)) {
                    a<String> aVarI = a.i();
                    a<String> aVarI2 = a.i();
                    aVarI2.c = "cn.jpush.android.user.profile";
                    a(appContext, aVarI, aVarI2);
                    a<Long> aVarJ = a.j();
                    a<Long> aVarJ2 = a.j();
                    aVarJ2.c = "cn.jpush.android.user.profile";
                    a(appContext, aVarJ, aVarJ2);
                    aVarD = a.k();
                    aVarD2 = a.k();
                    aVarD2.c = "cn.jpush.android.user.profile";
                } else if (str.equals("cn.jiguang.sdk.user.set.profile")) {
                    a<String> aVarL = a.l();
                    a<String> aVarL2 = a.l();
                    aVarL2.c = "cn.jpush.preferences.v2";
                    a(appContext, aVarL, aVarL2);
                    aVarD = a.m();
                    aVarD2 = a.m();
                    aVarD2.e = "cn.jpush.android.user.profile";
                } else {
                    if (str.equals("cn.jiguang.sdk.user.profile")) {
                        a<Long> aVarC = a.c();
                        a<Long> aVarC2 = a.c();
                        aVarC2.c = "cn.jpush.android.user.profile";
                        aVarC2.d = "device_uid";
                        a(appContext, aVarC, aVarC2);
                        a<String> aVarD3 = a.d();
                        a<String> aVarD4 = a.d();
                        aVarD4.c = "cn.jpush.android.user.profile";
                        aVarD4.d = "device_registration_id";
                        a(appContext, aVarD3, aVarD4);
                        aVarD = a.e();
                        aVarD2 = a.e();
                        aVarD2.c = "cn.jpush.android.user.profile";
                        str2 = "device_password";
                    } else if (str.equals("cn.jiguang.sdk.address")) {
                        a<String> aVarP = a.P();
                        a<String> aVarP2 = a.P();
                        aVarP2.c = "cn.jpush.android.user.profile";
                        aVarP2.d = "conn";
                        a(appContext, aVarP, aVarP2);
                        aVarD = a.Q();
                        aVarD2 = a.Q();
                        aVarD2.c = "cn.jpush.android.user.profile";
                        str2 = "srv";
                    } else if (str.equals(a.b)) {
                        a<String> aVarC3 = a.C();
                        a<String> aVarC4 = a.C();
                        aVarC4.d = "device_registered_appkey";
                        a(appContext, aVarC3, aVarC4);
                        aVarD = a.D();
                        aVarD2 = a.D();
                        str2 = "imei";
                    }
                    aVarD2.d = str2;
                }
                a(appContext, aVarD, aVarD2);
            }
        }
        return sharedPreferences;
    }

    public static <T> T b(Context context, a<T> aVar) {
        SharedPreferences sharedPreferences;
        Object objA = a(b(context, aVar.c), aVar.d, aVar.e);
        if (objA == null && aVar.f) {
            String str = aVar.c;
            Context appContext = JConstants.getAppContext(context);
            if (appContext != null) {
                appContext.getSharedPreferences(str, 4);
                sharedPreferences = appContext.getSharedPreferences(str, 0);
            } else {
                sharedPreferences = null;
            }
            objA = (T) a(sharedPreferences, aVar.d, aVar.e);
        }
        if (objA == null) {
            return null;
        }
        if (aVar.g && (objA instanceof String)) {
            String str2 = objA;
            if (str2.length() > 0) {
                objA = (T) d.b(str2);
            }
        }
        aVar.e = (T) objA;
        return (T) objA;
    }
}
