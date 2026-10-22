package cn.jpush.android.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.Logger;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static SharedPreferences a;

    public static void a(Context context) {
        a = context.getSharedPreferences("cn.jpush.config", 0);
    }

    public static void a(Context context, int i) {
        m(context).edit().putInt("service_stoped", i).apply();
    }

    public static void a(Context context, long j) {
        if (j < 0) {
            Logger.w("PushConfig", "freeze end time was incorrect!");
            return;
        }
        if (j > 1800) {
            Logger.i("PushConfig", "freeze end time was greate than half an hour");
            j = 1800;
        }
        m(context).edit().putLong("TAFreezeSetTime", System.currentTimeMillis()).putLong("TAFreezeEndTime", j * 1000).apply();
    }

    public static void a(Context context, String str) {
        m(context).edit().putString("mobile_number", str).apply();
    }

    public static void a(Context context, String str, String str2) {
        m(context).edit().putString("jpush_save_custom_builder" + str, str2).apply();
    }

    public static void a(Context context, boolean z) {
        m(context).edit().putBoolean("ups_state", z).apply();
    }

    public static int b(Context context) {
        SharedPreferences sharedPreferencesReload = Sp.reload(context, "cn.jpush.config");
        if (sharedPreferencesReload == null) {
            sharedPreferencesReload = m(context);
        }
        int i = sharedPreferencesReload.getInt("service_stoped", -1);
        if (i >= 0) {
            return i;
        }
        int i2 = context.getSharedPreferences("cn.jpush.android.user.profile", 0).getInt("service_stoped", 0);
        a(context, i2);
        return i2;
    }

    public static void b(Context context, long j) {
        m(context).edit().putLong("geofence_interval", j).apply();
    }

    public static void b(Context context, String str) {
        m(context).edit().putString("setting_push_time", str).apply();
    }

    public static void b(Context context, boolean z) {
        m(context).edit().putBoolean("notification_enabled", z).apply();
    }

    public static boolean b(Context context, int i) {
        String str;
        if (i != 0 && b(context) >= 1) {
            str = "The service is stopped, it will give up all the actions until you call resumePush method to resume the service.";
        } else if (i != 1 && !c(context)) {
            str = "The service is stopped, it will give up all the actions until you call JPushUPSManager.trunOn method to resume the service.";
        } else {
            if (i == 2 || ((Boolean) Sp.get(context, Key.UPSRegister())).booleanValue()) {
                return false;
            }
            str = "The service is stopped, it will give up all the actions until you call JPushUPSManager.registerToken method to resume the service.";
        }
        Logger.ww("PushConfig", str);
        return true;
    }

    public static long c(Context context, long j) {
        long j2 = m(context).getLong("geofence_interval", -1L);
        if (j2 == -1) {
            j2 = context.getSharedPreferences("cn.jpush.android.user.profile", 0).getLong("geofence_interval", -1L);
            if (j2 != -1) {
                b(context, j2);
            }
        }
        return j2 == -1 ? j : j2;
    }

    public static void c(Context context, int i) {
        m(context).edit().putInt("notification_num", i).apply();
    }

    public static void c(Context context, String str) {
        m(context).edit().putString("setting_silence_push_time", str).apply();
    }

    public static void c(Context context, boolean z) {
        m(context).edit().putBoolean("push_lbs_enable", z).apply();
    }

    public static boolean c(Context context) {
        return m(context).getBoolean("ups_state", true);
    }

    public static String d(Context context, String str) {
        String string = m(context).getString("jpush_save_custom_builder" + str, "");
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        String string2 = context.getSharedPreferences("cn.jpush.android.user.profile", 0).getString("jpush_save_custom_builder" + str, "");
        a(context, str, string2);
        return string2;
    }

    public static void d(Context context, int i) {
        m(context).edit().putInt("geofence_max_num", i).apply();
    }

    public static boolean d(Context context) {
        String str;
        if (b(context) >= 1) {
            str = "The service is stopped, it will give up all the actions until you call resumePush method to resume the service.";
        } else if (!c(context)) {
            str = "The service is stopped, it will give up all the actions until you call JPushUPSManager.trunOn method to resume the service.";
        } else {
            if (((Boolean) Sp.get(context, Key.UPSRegister())).booleanValue()) {
                return false;
            }
            str = "The service is stopped, it will give up all the actions until you call JPushUPSManager.registerToken method to resume the service.";
        }
        Logger.dd("PushConfig", str);
        return true;
    }

    public static int e(Context context, int i) {
        int i2 = m(context).getInt("geofence_max_num", -1);
        if (i2 == -1 && (i2 = context.getSharedPreferences("cn.jpush.android.user.profile", 0).getInt("geofence_max_num", -1)) != -1) {
            d(context, i2);
        }
        return i2 == -1 ? i : i2;
    }

    public static String e(Context context) {
        return m(context).getString("mobile_number", null);
    }

    public static void e(Context context, String str) {
        m(context).edit().putString("push_ssp_last_state", str).apply();
    }

    public static boolean f(Context context) {
        return m(context).getBoolean("notification_enabled", true);
    }

    public static String g(Context context) {
        return m(context).getString("setting_push_time", "");
    }

    public static String h(Context context) {
        return m(context).getString("setting_silence_push_time", "");
    }

    public static int i(Context context) {
        return m(context).getInt("notification_num", JPushInterface.DEFAULT_NOTIFICATION_NUM);
    }

    public static boolean j(Context context) {
        long j = m(context).getLong("TAFreezeEndTime", -1L);
        if (j > 1800) {
            j = 0;
        }
        long j2 = m(context).getLong("TAFreezeSetTime", -1L);
        if (j == -1 || j2 == -1) {
            return false;
        }
        if (System.currentTimeMillis() - j2 >= 0 && System.currentTimeMillis() - j2 <= j) {
            return false;
        }
        m(context).edit().putLong("TAFreezeSetTime", -1L).putLong("TAFreezeEndTime", -1L).apply();
        Logger.w("PushConfig", "incorrect timestamp");
        return true;
    }

    public static boolean k(Context context) {
        return m(context).getBoolean("push_lbs_enable", true);
    }

    public static String l(Context context) {
        return m(context).getString("push_ssp_last_state", "0,0");
    }

    private static SharedPreferences m(Context context) {
        if (a == null) {
            a(context);
        }
        return a;
    }
}
