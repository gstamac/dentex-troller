package cn.jpush.android.o;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import cn.jpush.android.helper.Logger;
import com.facebook.share.internal.MessengerShareContentUtility;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: cn.jpush.android.o.a$a, reason: collision with other inner class name */
    public static class C0010a {
        public String a;
        public CharSequence b;
        public int c;
        public int d;
        public int e;
        public String f;
        public Uri g;
        public boolean h;

        public C0010a(String str, CharSequence charSequence, int i, int i2, String str2) {
            this.a = str;
            this.b = charSequence;
            this.c = i;
            this.e = i2;
            this.f = str2;
        }
    }

    private static int a(int i) {
        if (i == -2) {
            return 1;
        }
        if (i != -1) {
            return (i == 1 || i == 2) ? 4 : 3;
        }
        return 2;
    }

    private static NotificationChannel a(Context context, String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                NotificationChannel notificationChannel = notificationManager != null ? notificationManager.getNotificationChannel(str) : null;
                if (notificationChannel != null) {
                    Logger.dd("ChannelHelper", "channel has created: " + str);
                    return notificationChannel;
                }
                Logger.dd("ChannelHelper", "not found channed id from user set,will use sdk default channel ID");
            }
        } catch (Throwable th) {
            Logger.e("ChannelHelper", "config channelID error,will use sdk default channel ID:" + th);
        }
        return null;
    }

    private static String a(Context context, int i) {
        String str;
        boolean zContains;
        String str2;
        if (i == -2) {
            str = "MIN";
        } else if (i != -1) {
            str = (i == 1 || i == 2) ? "HIGH" : MessengerShareContentUtility.PREVIEW_DEFAULT;
        } else {
            str = "LOW";
        }
        String string = "";
        try {
            String str3 = "jg_channel_name_p_" + str.toLowerCase();
            int identifier = context.getResources().getIdentifier(str3, TypedValues.Custom.S_STRING, context.getPackageName());
            if (identifier != 0) {
                string = context.getResources().getString(identifier);
                str2 = "found " + string + " from resource by name:" + str3;
            } else {
                str2 = "not found string value from resource by name:" + str3;
            }
            Logger.dd("ChannelHelper", str2);
        } catch (Throwable th) {
            Logger.ww("ChannelHelper", "get resource channel ID failed:" + th.getMessage());
        }
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        try {
            zContains = Locale.getDefault().getLanguage().contains("zh");
        } catch (Throwable th2) {
            Logger.ww("ChannelHelper", "get language failed:" + th2.getMessage());
            zContains = true;
        }
        if (i == -2 || i == -1) {
            return zContains ? "不重要" : "LOW";
        }
        if (i == 1 || i == 2) {
            return zContains ? "重要" : "HIGH";
        }
        return zContains ? "普通" : "NORMAL";
    }

    private static String a(Context context, int i, int i2) {
        int iA = a(i);
        String str = "JPush_" + iA + "_" + i2;
        String strA = a(context, i);
        Logger.d("ChannelHelper", "channelId:" + str + ",channelName:" + strA);
        return a(context, str, strA, iA, i2, null) ? str : "";
    }

    private static String a(C0010a c0010a) {
        String str = "JPush_" + c0010a.d + "_" + c0010a.e;
        return c0010a.g != null ? str + "_" + c0010a.f : str;
    }

    public static void a(Context context) {
        a(context, -1, 0);
        a(context, 0, 7);
    }

    public static void a(Context context, Notification.Builder builder, String str, CharSequence charSequence, int i, int i2, String str2) {
        C0010a c0010a = new C0010a(str, charSequence, i, i2, str2);
        a(context, c0010a);
        b.a(context, builder, c0010a);
    }

    public static void a(Context context, Notification notification, String str, CharSequence charSequence, int i, int i2, String str2) {
        C0010a c0010a = new C0010a(str, charSequence, i, i2, str2);
        a(context, c0010a);
        b.a(notification, c0010a);
    }

    public static void a(Context context, C0010a c0010a) {
        if (b.d(context)) {
            Logger.d("ChannelHelper", "in silence push time,change defaults to 0");
            c0010a.a = "";
            c0010a.e = 0;
            c0010a.f = null;
        } else {
            NotificationChannel notificationChannelA = a(context, c0010a.a);
            if (notificationChannelA != null) {
                c0010a.a = notificationChannelA.getId();
                c0010a.b = notificationChannelA.getName();
                c0010a.d = notificationChannelA.getImportance();
                c0010a.c = b(c0010a.d);
                c0010a.g = notificationChannelA.getSound();
                if (c0010a.g != null) {
                    c0010a.f = c0010a.g.getPath();
                }
                c0010a.h = true;
                return;
            }
        }
        if (c0010a.c == -2 || c0010a.c == -1) {
            c0010a.e = 0;
            c0010a.f = null;
        } else if (c0010a.c >= 0 && c0010a.e == 0 && TextUtils.isEmpty(c0010a.f)) {
            c0010a.c = -1;
        }
        c0010a.e &= 7;
        if (c0010a.c == 2) {
            c0010a.c = 1;
        }
        c0010a.d = a(c0010a.c);
        c0010a.c = b(c0010a.d);
        c0010a.g = b.a(context, c0010a.f);
        if (c0010a.g != null) {
            c0010a.e &= -2;
        } else {
            c0010a.f = null;
        }
        if (TextUtils.isEmpty(c0010a.a)) {
            c0010a.a = a(c0010a);
            NotificationChannel notificationChannelA2 = a(context, c0010a.a);
            if (notificationChannelA2 != null) {
                c0010a.a = notificationChannelA2.getId();
                c0010a.b = notificationChannelA2.getName();
                c0010a.h = true;
                return;
            }
        }
        if (TextUtils.isEmpty(c0010a.b)) {
            c0010a.b = a(context, c0010a.c);
        }
        Logger.d("ChannelHelper", "create channelId:" + c0010a.a + ",channelName:" + ((Object) c0010a.b) + " soundUri:" + c0010a.g);
        c0010a.h = a(context, c0010a.a, c0010a.b, c0010a.d, c0010a.e, c0010a.g);
    }

    private static boolean a(Context context, String str, CharSequence charSequence, int i, int i2, Uri uri) {
        if (context.getApplicationInfo().targetSdkVersion < 26) {
            Logger.d("ChannelHelper", "sdkversion < 26, no need use notificationChannel!");
            return false;
        }
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager == null) {
                Logger.ee("ChannelHelper", "NotificationManager is null!");
                return false;
            }
            Logger.dd("ChannelHelper", "prepare NotificationChannel, channelId=" + str + "");
            try {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(str);
                if (notificationChannel != null) {
                    notificationChannel.setName(charSequence);
                    notificationManager.createNotificationChannel(notificationChannel);
                    return true;
                }
                NotificationChannel notificationChannel2 = new NotificationChannel(str, charSequence, i);
                try {
                    if (Build.VERSION.SDK_INT < 28 || notificationManager.getNotificationChannelGroup("JIGUANG_CHANNEL_GROUP") == null) {
                        notificationManager.createNotificationChannelGroup(new NotificationChannelGroup("JIGUANG_CHANNEL_GROUP", cn.jpush.android.t.a.b(context)));
                    }
                    notificationChannel2.setGroup("JIGUANG_CHANNEL_GROUP");
                } catch (Throwable th) {
                    Logger.ww("ChannelHelper", "config channel group failed:" + th.getMessage());
                }
                try {
                    notificationChannel2.enableLights((i2 & 4) != 0);
                } catch (Throwable th2) {
                    Logger.ww("ChannelHelper", "enableLights fail:" + th2);
                }
                try {
                    notificationChannel2.enableVibration((i2 & 2) != 0);
                } catch (Throwable th3) {
                    Logger.ww("ChannelHelper", "enableVibration fail:" + th3);
                }
                try {
                    if (uri != null) {
                        notificationChannel2.setSound(uri, null);
                    } else {
                        if (!((i2 & 1) != 0)) {
                            notificationChannel2.setSound(null, null);
                        }
                    }
                } catch (Throwable th4) {
                    Logger.ww("ChannelHelper", "setSound fail:" + th4);
                }
                notificationManager.createNotificationChannel(notificationChannel2);
                return true;
            } catch (Throwable th5) {
                Logger.w("ChannelHelper", "check channelID failed:" + th5.getMessage());
            }
            Logger.ee("ChannelHelper", "new NotificationChannel fail:" + th);
            return false;
        } catch (Throwable th6) {
            Logger.ee("ChannelHelper", "new NotificationChannel fail:" + th6);
            return false;
        }
    }

    private static int b(int i) {
        if (i == 1) {
            return -2;
        }
        if (i != 2) {
            return (i == 4 || i == 5) ? 1 : 0;
        }
        return -1;
    }
}
