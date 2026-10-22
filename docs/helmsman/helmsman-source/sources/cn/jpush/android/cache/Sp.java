package cn.jpush.android.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import cn.jpush.android.local.JPushConstants;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class Sp {
    private static final Map<String, SharedPreferences> a = new ConcurrentHashMap();

    private static SharedPreferences a(Context context, String str) {
        Context appContext;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Map<String, SharedPreferences> map = a;
        SharedPreferences sharedPreferences = map.get(str);
        if (sharedPreferences != null || (appContext = JPushConstants.getAppContext(context)) == null) {
            return sharedPreferences;
        }
        SharedPreferences sharedPreferences2 = appContext.getSharedPreferences(str, 0);
        map.put(str, sharedPreferences2);
        b(appContext, str);
        return sharedPreferences2;
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

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> void a(SharedPreferences.Editor editor, String str, T t) {
        if (editor != null) {
            if (t == 0) {
                editor.remove(str);
                return;
            }
            if (t instanceof Boolean) {
                editor.putBoolean(str, ((Boolean) t).booleanValue());
                return;
            }
            if (t instanceof String) {
                editor.putString(str, (String) t);
                return;
            }
            if (t instanceof Integer) {
                editor.putInt(str, ((Integer) t).intValue());
            } else if (t instanceof Long) {
                editor.putLong(str, ((Long) t).longValue());
            } else if (t instanceof Float) {
                editor.putFloat(str, ((Float) t).floatValue());
            }
        }
    }

    private static void b(Context context, String str) {
    }

    public static void clear(Context context, String str) {
        SharedPreferences sharedPreferencesA = a(context, str);
        if (sharedPreferencesA != null) {
            sharedPreferencesA.edit().clear().apply();
        }
    }

    public static <T> T get(Context context, Key<T> key) {
        T t = (T) getWithNull(context, key);
        return t != null ? t : key.c;
    }

    public static <T> T getWithNull(Context context, Key<T> key) {
        T t = (T) a(a(context, key.a), key.b, key.c);
        if (t == null && key.d) {
            t = (T) a(reload(context, key.a), key.b, key.c);
        }
        if (t == null) {
            return null;
        }
        key.set(t);
        return t;
    }

    public static void read(Context context, Key<?>... keyArr) {
        if (keyArr == null || keyArr.length <= 0) {
            return;
        }
        for (Key<?> key : keyArr) {
            get(context, key);
        }
    }

    public static SharedPreferences reload(Context context, String str) {
        Context appContext = JPushConstants.getAppContext(context);
        if (appContext == null) {
            return null;
        }
        appContext.getSharedPreferences(str, 4);
        return appContext.getSharedPreferences(str, 0);
    }

    public static void set(Context context, Key<?>... keyArr) {
        if (keyArr == null || keyArr.length <= 0) {
            return;
        }
        SharedPreferences sharedPreferencesA = a(context, keyArr[0].a);
        if (sharedPreferencesA != null) {
            SharedPreferences.Editor editorEdit = sharedPreferencesA.edit();
            for (Key<?> key : keyArr) {
                a(editorEdit, key.b, key.c);
            }
            editorEdit.commit();
        }
    }
}
