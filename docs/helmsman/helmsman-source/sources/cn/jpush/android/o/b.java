package cn.jpush.android.o;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.RemoteViews;
import androidx.core.view.PointerIconCompat;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.api.PushNotificationBuilder;
import cn.jpush.android.cache.Key;
import cn.jpush.android.cache.Sp;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.helper.h;
import cn.jpush.android.local.JPushConstants;
import cn.jpush.android.service.JNotifyActivity;
import cn.jpush.android.service.JPushMessageReceiver;
import cn.jpush.android.ui.PopWinActivity;
import cn.jpush.android.ui.PushActivity;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.internal.ShareConstants;
import im.helmsman.helmsmanandroid.factory.OkHttpFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.UUID;
import java.util.zip.Adler32;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class b {
    private static Boolean a;

    static class a implements Runnable {
        private Context a;
        private cn.jpush.android.d.d b;

        public a(Context context, cn.jpush.android.d.d dVar) {
            this.a = context;
            this.b = dVar;
        }

        public static void a(Context context, cn.jpush.android.d.d dVar) throws Throwable {
            b.j(context, dVar);
            if (dVar.S != 0 && dVar.S != 4) {
                b.i(context, dVar);
            } else {
                cn.jpush.android.helper.c.a(dVar.c, 995, context);
                b.h(context, dVar);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                long jB = cn.jpush.android.t.b.b(this.b.ak);
                long jB2 = cn.jpush.android.t.b.b(this.b.al);
                if (jB > 0 && jB == jB2) {
                    Logger.ww("NotificationHelper", "the beginTime == endTime,not deal this notification");
                    return;
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (jB2 > 0) {
                    if (jB2 < jB) {
                        Logger.e("NotificationHelper", "illegal argument, endTime earlier than beginTime");
                        return;
                    } else if (jB2 < jCurrentTimeMillis) {
                        Logger.w("NotificationHelper", this.b.c + " already end");
                        cn.jpush.android.helper.c.a(this.b.c, 1034, this.a);
                        return;
                    }
                }
                if (jB >= jCurrentTimeMillis) {
                    d.a().a(this.a, new d.a(jB, this.b));
                    cn.jpush.android.helper.c.a(this.b.c, 1035, this.a);
                } else {
                    a(this.a, this.b);
                    if (jB2 > 0) {
                        d.a().a(this.a, new d.a(jB2, b.a(this.b)));
                    }
                }
            } catch (Throwable th) {
                Logger.w("NotificationHelper", "run NotifyAction failed:" + th.getMessage());
            }
        }
    }

    private static int a(int i) {
        if (i == 0) {
            return 1;
        }
        if (i != 1) {
            return i != 2 ? 1 : 32;
        }
        return 16;
    }

    public static int a(cn.jpush.android.d.d dVar) {
        return a(TextUtils.isEmpty(dVar.f) ? dVar.c : dVar.f);
    }

    public static int a(String str) {
        if (TextUtils.isEmpty(str)) {
            Logger.d("NotificationHelper", "action:getNotificationID - empty messageId");
            return 0;
        }
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception unused) {
            Logger.w("NotificationHelper", "Ths msgId is not a integer");
            Adler32 adler32 = new Adler32();
            adler32.update(str.getBytes());
            int value = (int) adler32.getValue();
            return value < 0 ? Math.abs(value) : value;
        }
    }

    static Notification a(Notification.Builder builder) {
        try {
            return builder.build();
        } catch (Throwable th) {
            Logger.ww("NotificationHelper", "Build notification error:" + th.getMessage());
            return null;
        }
    }

    private static Notification a(Context context, cn.jpush.android.d.d dVar, RemoteViews remoteViews, boolean z, c cVar, c cVar2) {
        StringBuilder sb;
        cn.jpush.android.o.a.C0010a c0010a = new cn.jpush.android.o.a.C0010a(dVar.D, "", dVar.z, dVar.C, dVar.aj);
        cn.jpush.android.o.a.a(context, c0010a);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(dVar.s).setContentText(dVar.u).setTicker(dVar.u);
        if (z) {
            builder.setGroupSummary(false);
            builder.setGroup("group");
        }
        if (z) {
            builder.setSmallIcon(R.color.transparent);
            if (h.b() == -1) {
                int iA = cVar2.a();
                if (iA == 102) {
                    builder.setLargeIcon((Icon) cVar2.b());
                } else if (iA == 100) {
                    builder.setLargeIcon(cVar2.c());
                } else {
                    Logger.d("NotificationHelper", "not set large icon");
                }
            }
        } else {
            a(JPushConstants.mApplicationContext, builder, cVar, cVar2);
        }
        builder.setShowWhen(true);
        a(context, builder, c0010a);
        if (remoteViews == null) {
            if (dVar.v == 1 && !TextUtils.isEmpty(dVar.w)) {
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
                bigTextStyle.bigText(dVar.w);
                builder.setStyle(bigTextStyle);
            }
            if (dVar.v == 2 && !TextUtils.isEmpty(dVar.y)) {
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
                try {
                    TreeMap treeMap = new TreeMap();
                    JSONObject jSONObject = new JSONObject(dVar.y);
                    Iterator<String> itKeys = jSONObject.keys();
                    while (itKeys.hasNext()) {
                        String next = itKeys.next();
                        treeMap.put(next, jSONObject.optString(next));
                    }
                    Iterator it = treeMap.values().iterator();
                    while (it.hasNext()) {
                        inboxStyle.addLine((String) it.next());
                    }
                    inboxStyle.setSummaryText(" + " + jSONObject.length() + " new messages");
                } catch (Throwable th) {
                    Logger.ee("NotificationHelper", "Set inbox style error: " + th.getMessage());
                }
                builder.setStyle(inboxStyle);
            }
            if (dVar.v == 3 && !TextUtils.isEmpty(dVar.x)) {
                Logger.i("NotificationHelper", "Set notification BPS with picture path:" + dVar.x);
                try {
                    Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
                    bigPictureStyle.bigPicture(BitmapFactory.decodeFile(dVar.x));
                    builder.setStyle(bigPictureStyle);
                } catch (OutOfMemoryError e) {
                    e = e;
                    sb = new StringBuilder("Create bitmap failed caused by OutOfMemoryError.error:");
                    Logger.ww("NotificationHelper", sb.append(e).toString());
                } catch (Throwable th2) {
                    e = th2;
                    sb = new StringBuilder("Create big picture style failed. error:");
                    Logger.ww("NotificationHelper", sb.append(e).toString());
                }
            }
        }
        if (!TextUtils.isEmpty(dVar.A)) {
            builder.setCategory(dVar.A);
        }
        if (remoteViews != null) {
            builder.setContent(remoteViews);
        }
        return a(builder);
    }

    public static Intent a(Context context, String str, cn.jpush.android.d.d dVar) {
        Intent intent = new Intent(str);
        intent.setClass(context, PushActivity.class);
        if (dVar != null) {
            intent.putExtra("msg_data", dVar.c());
        }
        if (!cn.jpush.android.t.a.h(context)) {
            intent.addFlags(32768);
        }
        return intent;
    }

    public static Uri a(Context context, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (context.getResources().getIdentifier(str, "raw", context.getPackageName()) == 0) {
                Logger.d("NotificationHelper", "not found sound=" + str);
                return null;
            }
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + str);
            Logger.d("NotificationHelper", "found sound uri=" + uri);
            return uri;
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "find sound e:" + th);
            return null;
        }
    }

    private static PushNotificationBuilder a(Context context, NotificationMessage notificationMessage) {
        try {
            Class<?> cls = Class.forName("cn.jpush.android.support.JPushSupport");
            return (PushNotificationBuilder) cls.getDeclaredMethod("getNotificationBuilder", Context.class, NotificationMessage.class).invoke(cls, context, notificationMessage);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static cn.jpush.android.d.d a(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("msg_data");
        if (TextUtils.isEmpty(stringExtra)) {
            return null;
        }
        return cn.jpush.android.d.d.a(stringExtra);
    }

    public static c a(Context context, String str, String str2, String str3, String str4) {
        c cVar = new c();
        String strG = "";
        int identifier = 0;
        try {
            if (TextUtils.isEmpty(str)) {
                Logger.d("NotificationHelper", "source is empty,need not diy small icon");
            } else if (!TextUtils.isEmpty(str3)) {
                if (cn.jpush.android.t.a.a(str3)) {
                    strG = g(JPushConstants.mApplicationContext, str3);
                } else {
                    identifier = JPushConstants.mApplicationContext.getResources().getIdentifier(str3, "drawable", JPushConstants.mApplicationContext.getPackageName());
                }
            }
        } catch (Throwable th) {
            Logger.d("NotificationHelper", "load small icon failed:" + th.getMessage());
        }
        if (!TextUtils.isEmpty(strG)) {
            try {
                Bitmap bitmapDecodeFile = cn.jpush.android.t.c.a(strG) ? BitmapFactory.decodeFile(strG) : null;
                if (bitmapDecodeFile != null) {
                    Logger.d("NotificationHelper", "set small icon with path bitmap:" + bitmapDecodeFile);
                    cVar.a(100);
                    cVar.a(bitmapDecodeFile);
                    return cVar;
                }
                Logger.d("NotificationHelper", "not set small icon by bitmap");
                Icon iconB = b(strG);
                if (iconB != null) {
                    Logger.d("NotificationHelper", "set small icon success by path:" + strG);
                    cVar.a(102);
                    cVar.a(iconB);
                    return cVar;
                }
            } catch (Throwable unused) {
                Logger.w("NotificationHelper", "resource not found with sdcard path");
            }
        }
        if (identifier != 0) {
            cVar.a(101);
            cVar.b(identifier);
            Logger.d("NotificationHelper", "set small icon success by resId:" + identifier + ",resName:" + str3);
            return cVar;
        }
        if (TextUtils.isEmpty(str2) || str2.equals(context.getPackageName())) {
            int iA = f.a(context);
            if (iA == 0) {
                cVar.c(972);
                return cVar;
            }
            cVar.a(101);
            cVar.b(iA);
            Logger.d("NotificationHelper", "set small icon success by mine icon resId:" + iA);
            return cVar;
        }
        try {
            if (!cn.jpush.android.t.a.c(context, str2)) {
                Logger.w("NotificationHelper", "targetApp ：" + str2 + " is not install,will give up notify the msg");
                cVar.c(993);
                return cVar;
            }
            Bitmap bitmapE = e(context, str2);
            if (bitmapE == null) {
                Logger.w("NotificationHelper", "not found targetApp small icon bitmap,will give up notify the msg");
                cVar.c(972);
                return cVar;
            }
            cVar.a(100);
            cVar.a(bitmapE);
            Logger.d("NotificationHelper", "set small icon success by targetAppBitmap");
            return cVar;
        } catch (Throwable th2) {
            Logger.w("NotificationHelper", "set small icon by targetApp icon bitmap failed:" + th2.getMessage());
            cVar.c(972);
            return cVar;
        }
    }

    /* JADX WARN: Code duplicated, block: B:135:0x0473  */
    private static Object a(Context context, cn.jpush.android.d.d dVar, c cVar, c cVar2) {
        int identifier;
        String str;
        int i;
        String strOptString;
        int iOptInt;
        String str2;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        String str3;
        cn.jpush.android.helper.e.a(JPushConstants.mApplicationContext);
        Context appContext = JPushConstants.getAppContext(context);
        int identifier2 = appContext.getResources().getIdentifier("push_notification", "layout", appContext.getPackageName());
        Logger.d("NotificationHelper", "layoutId:" + identifier2);
        if (identifier2 <= 0) {
            return null;
        }
        int identifier3 = appContext.getResources().getIdentifier("v", "id", appContext.getPackageName());
        int identifier4 = appContext.getResources().getIdentifier("v21", "id", appContext.getPackageName());
        Logger.d("NotificationHelper", "targetVersion:" + Build.VERSION.SDK_INT + ",vId:" + identifier3 + ",v21Id:" + identifier4);
        if (Build.VERSION.SDK_INT >= 29 && identifier4 <= 0) {
            return null;
        }
        boolean z6 = identifier4 <= 0;
        int identifier5 = appContext.getResources().getIdentifier("push_root_view", "id", appContext.getPackageName());
        int iB = h.b();
        int identifier6 = appContext.getResources().getIdentifier("push_notification_style_default", "id", appContext.getPackageName());
        if (iB != 0) {
            identifier = appContext.getResources().getIdentifier("push_notification_style_" + iB, "id", appContext.getPackageName());
            if (identifier <= 0) {
                Logger.ww("NotificationHelper", "not found push_notification_style_" + iB + " in layout");
                return null;
            }
        } else {
            identifier = 0;
        }
        String str4 = iB == 0 ? "" : "style_" + iB + "_";
        int identifier7 = appContext.getResources().getIdentifier("push_notification_" + str4 + "title", "id", appContext.getPackageName());
        String str5 = "";
        int identifier8 = appContext.getResources().getIdentifier("push_notification_" + str4 + "content", "id", appContext.getPackageName());
        int identifier9 = appContext.getResources().getIdentifier("push_notification_" + str4 + "big_icon", "id", appContext.getPackageName());
        int identifier10 = appContext.getResources().getIdentifier("push_notification_" + str4 + "date", "id", appContext.getPackageName());
        int identifier11 = appContext.getResources().getIdentifier("push_notification_content_one_line", "id", appContext.getPackageName());
        boolean z7 = z6;
        int identifier12 = appContext.getResources().getIdentifier("push_notification_sub_title", "id", appContext.getPackageName());
        int i2 = identifier;
        int identifier13 = appContext.getResources().getIdentifier("push_notification_dot", "id", appContext.getPackageName());
        int identifier14 = appContext.getResources().getIdentifier("push_notification_small_icon", "id", appContext.getPackageName());
        if (iB == 0) {
            str = "NotificationHelper";
            Logger.d(str, "buildDiyLayout layoutId:" + identifier2 + ",defaultStyleId:" + identifier6 + ",largeId:" + identifier9 + ",appNameId:" + identifier7 + ",contentId:" + identifier8 + ",smallId:" + identifier14 + ",dateId:" + identifier10 + ",dotId:" + identifier13 + ",titleId:" + identifier12 + ",oneLinecontentId:" + identifier11);
            if (identifier2 <= 0 || identifier5 <= 0 || identifier6 <= 0 || identifier7 <= 0 || identifier8 <= 0 || identifier9 <= 0 || identifier14 <= 0 || identifier10 <= 0 || identifier13 <= 0 || identifier12 <= 0 || identifier11 <= 0) {
                Logger.ww(str, "not found valid push_notification in layout");
                return null;
            }
            i = i2;
        } else {
            str = "NotificationHelper";
            i = i2;
            Logger.d(str, "buildDiyLayout layoutId:" + identifier2 + ",useStyleId:" + i + ",appNameId:" + identifier7 + ",contentId:" + identifier8 + ",largeId:" + identifier9 + ",dateId:" + identifier10);
            if (identifier2 <= 0 || identifier5 <= 0 || i <= 0 || identifier7 <= 0 || identifier8 <= 0 || identifier9 <= 0 || identifier10 <= 0) {
                Logger.ww(str, "not found valid push_notification in layout");
                return null;
            }
        }
        RemoteViews remoteViews = new RemoteViews(appContext.getPackageName(), identifier2);
        if (z7) {
            try {
                int iA = cn.jpush.android.helper.e.a();
                int iB2 = cn.jpush.android.helper.e.b();
                remoteViews.setTextColor(identifier7, iA);
                if (iB == 0) {
                    remoteViews.setTextColor(identifier10, iB2);
                    remoteViews.setTextColor(identifier13, iA);
                    remoteViews.setTextColor(identifier12, iA);
                } else {
                    remoteViews.setTextColor(identifier10, iB2);
                }
                remoteViews.setTextColor(identifier11, iB2);
                remoteViews.setTextColor(identifier8, iB2);
            } catch (Throwable th) {
                Logger.d(str, "set diy color error:" + th.getMessage());
            }
        }
        float fD = cn.jpush.android.helper.e.d();
        if (fD != -1.0f) {
            remoteViews.setTextViewTextSize(identifier8, 0, fD);
            remoteViews.setTextViewTextSize(identifier11, 0, fD);
        }
        String str6 = dVar.t;
        try {
            JSONObject jSONObject = new JSONObject(dVar.B);
            strOptString = jSONObject.optString("ssp_appname");
            try {
                iOptInt = jSONObject.optInt("ssp_use_title");
            } catch (Throwable unused) {
                str5 = strOptString;
                strOptString = str5;
                iOptInt = 0;
            }
        } catch (Throwable unused2) {
        }
        if (TextUtils.isEmpty(str6) && !TextUtils.isEmpty(strOptString)) {
            str6 = strOptString;
        }
        if (iB == 0) {
            remoteViews.setViewVisibility(identifier6, 0);
            if (TextUtils.isEmpty(str6)) {
                remoteViews.setTextViewText(identifier7, dVar.s);
                remoteViews.setViewVisibility(identifier12, 8);
                z3 = false;
            } else if (appContext.getApplicationInfo().targetSdkVersion >= 24) {
                remoteViews.setTextViewText(identifier7, str6);
                remoteViews.setTextViewText(identifier12, dVar.s);
                z3 = false;
                remoteViews.setViewVisibility(identifier12, 0);
                if (h.c()) {
                    remoteViews.setViewVisibility(identifier8, 8);
                    remoteViews.setTextViewText(identifier11, dVar.u);
                    remoteViews.setViewVisibility(identifier11, 0);
                }
                float fC = cn.jpush.android.helper.e.c();
                if (fC != -1.0f) {
                    remoteViews.setTextViewTextSize(identifier12, 0, fC);
                }
            } else {
                z3 = false;
                remoteViews.setTextViewText(identifier7, dVar.s);
                remoteViews.setViewVisibility(identifier12, 8);
            }
            z2 = z3;
        } else {
            remoteViews.setViewVisibility(identifier6, 8);
            remoteViews.setViewVisibility(i, 0);
            if (TextUtils.isEmpty(str6) || iOptInt == 1) {
                str2 = dVar.s;
                z = false;
            } else {
                str2 = str6;
                z = true;
            }
            remoteViews.setTextViewText(identifier7, str2);
            float fC2 = cn.jpush.android.helper.e.c();
            if (fC2 != -1.0f) {
                z2 = false;
                remoteViews.setTextViewTextSize(identifier7, 0, fC2);
            } else {
                z2 = false;
            }
            z3 = z;
        }
        remoteViews.setTextViewText(identifier8, dVar.u);
        remoteViews.setTextViewText(identifier10, new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis())));
        Logger.d(str, "notificationStyle:" + iB + ",noUseLargeIcon:" + z3);
        if (iB == 0 || z3) {
            int iA2 = cVar.a();
            if (iA2 == 102) {
                if (iB == 0) {
                    remoteViews.setImageViewIcon(identifier14, (Icon) cVar.b());
                }
                if (z3) {
                    remoteViews.setImageViewIcon(identifier9, (Icon) cVar.b());
                }
                str3 = "diy set small icon by icon obj";
            } else {
                if (iA2 == 100) {
                    if (iB == 0) {
                        remoteViews.setImageViewBitmap(identifier14, cVar.c());
                    }
                    if (z3) {
                        remoteViews.setImageViewBitmap(identifier9, cVar.c());
                    }
                    str3 = "diy set small icon by bitmap";
                } else {
                    if (iA2 == 101) {
                        try {
                            appContext.getResources().getDrawable(cVar.d());
                            z4 = true;
                        } catch (Throwable unused3) {
                            Logger.w(str, "diy resource not found with local app");
                            z4 = z2;
                        }
                        if (z4) {
                            Logger.d(str, "diy set small icon by local resId:" + cVar.d());
                            if (iB == 0) {
                                remoteViews.setImageViewResource(identifier14, cVar.d());
                            }
                            if (z3) {
                                remoteViews.setImageViewResource(identifier9, cVar.d());
                            }
                            z5 = true;
                        }
                    }
                    z5 = z2;
                }
                if (!z5) {
                    cn.jpush.android.helper.c.a(dVar.c, 972, appContext);
                    return 1;
                }
            }
            Logger.d(str, str3);
            z5 = true;
            if (!z5) {
                cn.jpush.android.helper.c.a(dVar.c, 972, appContext);
                return 1;
            }
        }
        if (z3) {
            return remoteViews;
        }
        int iA3 = cVar2.a();
        if (iA3 == 102) {
            remoteViews.setImageViewIcon(identifier9, (Icon) cVar2.b());
        } else if (iA3 == 100) {
            remoteViews.setImageViewBitmap(identifier9, cVar2.c());
        } else {
            Logger.d(str, "diy not set large icon");
            if (iB == 1) {
                cn.jpush.android.helper.c.a(dVar.c, 973, appContext);
                return 2;
            }
        }
        return remoteViews;
    }

    private static String a(Context context, String str, String str2) throws Throwable {
        if (!str.endsWith(".jpg") && !str.endsWith(".png")) {
            Logger.i("NotificationHelper", "The url is not a picture resources.");
            return "";
        }
        Logger.i("NotificationHelper", "The url is a picture resources.");
        String str3 = cn.jpush.android.t.c.c(context, str2) + (str2 + str.substring(str.lastIndexOf(".")));
        Logger.i("NotificationHelper", "Big picture notification resource path: " + str3);
        byte[] bArrA = cn.jpush.android.n.a.a(str, 5, 5000L, 4);
        return (bArrA == null || !cn.jpush.android.t.c.a(str3, bArrA)) ? "" : str3;
    }

    public static void a(Notification notification, Context context, String str, String str2, PendingIntent pendingIntent) {
        try {
            Class.forName("android.app.Notification").getDeclaredMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class).invoke(notification, context, str, str2, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(Notification notification, cn.jpush.android.o.a.C0010a c0010a) {
    }

    public static void a(Context context) {
        while (true) {
            Integer numValueOf = Integer.valueOf(e.a());
            if (numValueOf.intValue() == 0) {
                return;
            } else {
                c(context, numValueOf.intValue());
            }
        }
    }

    public static void a(Context context, int i) {
        int iA;
        if (!e.b(i)) {
            e.a(i);
        }
        Logger.d("NotificationHelper", "queueSize:" + e.b() + ",max num:" + cn.jpush.android.cache.a.i(context));
        if (e.b() <= cn.jpush.android.cache.a.i(context) || (iA = e.a()) == 0) {
            return;
        }
        c(context, iA);
    }

    public static void a(Context context, int i, int i2, String str) {
    }

    public static void a(Context context, int i, int i2, String str, Bundle bundle) {
        try {
            String messageReceiverClass = JPushConstants.getMessageReceiverClass(context);
            if (TextUtils.isEmpty(messageReceiverClass)) {
                return;
            }
            Intent intent = new Intent();
            intent.setClassName(context, messageReceiverClass);
            intent.setAction(cn.jpush.android.t.a.e(context, "cmd_msg"));
            intent.putExtra("cmd", i);
            intent.putExtra(JThirdPlatFormInterface.KEY_CODE, i2);
            intent.putExtra(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, str);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (JPushConstants.getPushMessageReceiver(context) != null) {
                cn.jpush.android.helper.a.a(JPushConstants.getPushMessageReceiver(context), context, intent);
            } else {
                context.sendBroadcast(intent);
            }
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "sendCmdMsgToUser failed:" + th.getMessage());
        }
    }

    private static void a(Context context, int i, cn.jpush.android.d.d dVar) {
        try {
            StatusBarNotification[] activeNotifications = ((NotificationManager) context.getSystemService("notification")).getActiveNotifications();
            Logger.d("NotificationHelper", "active size:" + activeNotifications.length);
            boolean z = false;
            if (activeNotifications != null) {
                for (StatusBarNotification statusBarNotification : activeNotifications) {
                    if (statusBarNotification.getId() == i) {
                        z = true;
                        break;
                    }
                }
            }
            Logger.dd("NotificationHelper", z ? "show  notification success:" + i : "show  notification maybe failed:" + i);
        } catch (Throwable th) {
            Logger.ww("NotificationHelper", "[checkNotficationShow] failed:" + th.getMessage());
        }
    }

    public static void a(Context context, int i, boolean z) {
        try {
            boolean z2 = true;
            if (e(context) != 1) {
                z2 = false;
            }
            Logger.d("NotificationHelper", "[checkNotificationSettings] new state:" + z2 + ",last state:" + a + ",soucre:" + i + ",isFocre:" + z);
            Boolean bool = a;
            if (bool != null && z2 == bool.booleanValue() && !z) {
                Logger.d("NotificationHelper", "[checkNotificationSettings] no change");
                return;
            }
            a = Boolean.valueOf(z2);
            JPushMessageReceiver pushMessageReceiver = JPushConstants.getPushMessageReceiver(context);
            if (pushMessageReceiver != null) {
                pushMessageReceiver.onNotificationSettingsCheck(context, a.booleanValue(), i);
                return;
            }
            try {
                Intent intent = new Intent(context, Class.forName(JPushConstants.getMessageReceiverClass(context)));
                intent.setAction("on_noti_settings_check");
                intent.putExtra("isOn", a);
                intent.putExtra(ShareConstants.FEED_SOURCE_PARAM, i);
                context.sendBroadcast(intent);
            } catch (Throwable th) {
                Logger.w("NotificationHelper", "checkNotificationSettings send to user e:" + th);
            }
        } catch (Throwable th2) {
            Logger.w("NotificationHelper", "checkNotificationSettings e:" + th2);
        }
    }

    public static void a(Context context, Notification.Builder builder, cn.jpush.android.o.a.C0010a c0010a) {
        if (c0010a != null) {
            try {
                if (c0010a.h) {
                    Logger.d("NotificationHelper", "setChannelId =" + c0010a.a);
                    f(context, c0010a.a);
                    builder.setChannelId(c0010a.a);
                }
                builder.setPriority(c0010a.c);
                builder.setDefaults(c0010a.e);
                builder.setSound(c0010a.g);
            } catch (Throwable unused) {
            }
        }
    }

    public static void a(Context context, cn.jpush.android.d.d dVar) {
        Logger.d("NotificationHelper", "[processMessage] pushEntity:" + dVar);
        if (dVar.T != 0) {
            Logger.d("NotificationHelper", "Unexpected: unknown show  mode - " + dVar.T);
        } else {
            JCoreManager.onEvent(context, JPushConstants.SDK_TYPE, 12, null, null, new a(context, dVar));
        }
    }

    public static void a(Context context, Integer num, Notification notification) {
        if (num == null) {
            return;
        }
        Key<Integer> keyBadgeCurNum = Key.BadgeCurNum();
        int iIntValue = ((Integer) Sp.get(context, keyBadgeCurNum)).intValue();
        if (iIntValue < 0) {
            iIntValue = 0;
        }
        if (num.intValue() != 0) {
            if (num.intValue() + iIntValue < 0) {
                num = Integer.valueOf(-iIntValue);
            }
            iIntValue += num.intValue();
            Sp.set(context, keyBadgeCurNum.set(Integer.valueOf(iIntValue)));
        }
        Logger.d("NotificationHelper", "curNum：" + iIntValue + ",addNum:" + num);
        a(context, notification, num.intValue(), iIntValue);
    }

    public static void a(Context context, String str, cn.jpush.android.d.d dVar, Intent intent) {
        try {
            Intent intent2 = new Intent(context, Class.forName(JPushConstants.getMessageReceiverClass(context)));
            if (intent != null) {
                String string = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);
                if (!TextUtils.isEmpty(string)) {
                    intent2.putExtra(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA, string);
                }
            }
            intent2.setAction(str);
            if (dVar != null) {
                intent2.putExtra("msg_data", dVar.c());
            }
            if (JPushConstants.getPushMessageReceiver(context) != null) {
                if (str.equals(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION_PROXY)) {
                    intent2.setAction(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION);
                    JPushConstants.getPushMessageReceiver(context).onMultiActionClicked(context, intent2);
                    return;
                }
                NotificationMessage notificationMessageB = dVar.b();
                if (TextUtils.isEmpty(notificationMessageB.appkey)) {
                    notificationMessageB.appkey = JCoreHelper.getAppKey(context);
                }
                if (str.equals("cn.jpush.android.intent.NOTIFICATION_ARRIVED")) {
                    JPushConstants.getPushMessageReceiver(context).onNotifyMessageArrived(context, notificationMessageB);
                    return;
                }
                if (str.equals("cn.jpush.android.intent.NOTIFICATION_DISMISS")) {
                    JPushConstants.getPushMessageReceiver(context).onNotifyMessageDismiss(context, notificationMessageB);
                    return;
                } else if (str.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
                    JPushConstants.getPushMessageReceiver(context).onNotifyMessageOpened(context, notificationMessageB);
                    return;
                } else if (str.equals("cn.jpush.android.intent.NOTIFICATION_UN_SHOW")) {
                    JPushConstants.getPushMessageReceiver(context).onNotifyMessageUnShow(context, notificationMessageB);
                    return;
                }
            }
            context.sendBroadcast(intent2);
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "sendToUserReceiver failed:" + th.getMessage());
        }
    }

    public static void a(Context context, List<String> list) {
        try {
            JSONArray jSONArrayB = b(context);
            JSONArray jSONArray = new JSONArray();
            long jCurrentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < jSONArrayB.length(); i++) {
                JSONObject jSONObject = jSONArrayB.getJSONObject(i);
                String string = jSONObject.getString("id");
                if (list != null && list.remove(string)) {
                    jSONObject.put("time", jCurrentTimeMillis);
                }
                if (jCurrentTimeMillis < jSONObject.getLong("time") + JConstants.DAY) {
                    jSONArray.put(jSONObject);
                }
            }
            if (list != null) {
                for (String str : list) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("time", jCurrentTimeMillis);
                    jSONObject2.put("id", str);
                    jSONArray.put(jSONObject2);
                }
            }
            String string2 = jSONArray.length() > 0 ? jSONArray.toString() : null;
            Logger.d("NotificationHelper", "saveString=" + string2);
            Sp.set(context, Key.NotiCancel().set(string2));
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "save NotiCancel e:" + th);
        }
    }

    private static boolean a() {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str = (String) cls.getDeclaredMethod("get", String.class).invoke(cls, "ro.build.version.emui");
            if (!TextUtils.isEmpty(str)) {
                Logger.i("NotificationHelper", "get EMUI version is:" + str);
                return cn.jpush.android.t.a.a(str.toLowerCase().replace("emotionui_", ""), "8.0") >= 0;
            }
        } catch (Throwable th) {
            Logger.e("NotificationHelper", " getEmuiVersion wrong error:" + th);
        }
        return false;
    }

    private static boolean a(Notification notification, int i) {
        if (notification != null) {
            try {
                Object obj = notification.getClass().getDeclaredField("extraNotification").get(notification);
                obj.getClass().getDeclaredMethod("setMessageCount", Integer.TYPE).invoke(obj, Integer.valueOf(i));
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private static boolean a(Context context, Notification.Builder builder, c cVar, c cVar2) {
        boolean z;
        boolean z2;
        int iA = cVar.a();
        if (iA == 102) {
            builder.setSmallIcon((Icon) cVar.b());
            z = true;
        } else {
            z = false;
        }
        if (!z && iA == 100) {
            builder.setSmallIcon(Icon.createWithBitmap(cVar.c()));
            z = true;
        }
        if (!z && iA == 101) {
            try {
                context.getResources().getDrawable(cVar.d());
                z2 = true;
            } catch (Throwable unused) {
                Logger.w("NotificationHelper", "resource not found with local app");
                z2 = false;
            }
            if (z2) {
                Logger.d("NotificationHelper", "set small icon by local resId:" + cVar.d());
                builder.setSmallIcon(Integer.valueOf(cVar.d()).intValue());
                z = true;
            }
        }
        if (!z) {
            return false;
        }
        int iA2 = cVar2.a();
        if (iA2 == 102) {
            builder.setLargeIcon((Icon) cVar2.b());
        } else if (iA2 == 100) {
            builder.setLargeIcon(cVar2.c());
        } else {
            Logger.d("NotificationHelper", "not set large icon");
        }
        return true;
    }

    public static boolean a(Context context, Notification notification, int i, int i2) {
        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            return a(notification, i);
        }
        if (!a()) {
            Logger.d("NotificationHelper", "not support");
            return false;
        }
        if (i2 == 0 || i != 0) {
            return d(context, i2);
        }
        return false;
    }

    public static boolean a(Context context, String str, cn.jpush.android.d.d dVar, NotificationMessage notificationMessage) {
        try {
            if (Class.forName(str).getMethod("onNotifyMessageUnShow", Context.class, NotificationMessage.class) == null) {
                Logger.dd("NotificationHelper", "not found onNotifyMessageUnShow method");
                return false;
            }
            if (!TextUtils.isEmpty(dVar.E)) {
                if (dVar.E.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    if (cn.jpush.android.t.a.h(context)) {
                        Logger.dd("NotificationHelper", "displayForeground...");
                        a(context, "cn.jpush.android.intent.NOTIFICATION_UN_SHOW", dVar, (Intent) null);
                        cn.jpush.android.helper.c.a(dVar.c, 1060, context);
                        return true;
                    }
                } else if (!((JPushMessageReceiver) Class.forName(str).newInstance()).isNeedShowNotification(context, notificationMessage, cn.jpush.android.t.a.c(context))) {
                    Logger.dd("NotificationHelper", "need not show notication by user");
                    a(context, "cn.jpush.android.intent.NOTIFICATION_UN_SHOW", dVar, (Intent) null);
                    cn.jpush.android.helper.c.a(dVar.c, 1061, context);
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            Logger.ww("NotificationHelper", "check display foreground failed:" + th.getMessage());
        }
    }

    public static Intent b(Context context, String str, cn.jpush.android.d.d dVar) {
        Intent intent = new Intent(str);
        intent.setClass(context, PopWinActivity.class);
        if (dVar != null) {
            intent.putExtra("msg_data", dVar.c());
        }
        return intent;
    }

    private static Icon b(String str) {
        try {
            if (new File(str).exists()) {
                return Icon.createWithFilePath(str);
            }
            Logger.w("NotificationHelper", "icon file doesn't exist, path=" + str);
            return null;
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "createIcon err:" + th);
            return null;
        }
    }

    public static NotificationMessage b(Context context, Intent intent) {
        NotificationMessage notificationMessageB = a(context, intent).b();
        if (TextUtils.isEmpty(notificationMessageB.appkey)) {
            notificationMessageB.appkey = JCoreHelper.getAppKey(context);
        }
        return notificationMessageB;
    }

    public static c b(Context context, String str) {
        c cVar = new c();
        Object objC = c(context, str);
        if (objC == null) {
            cVar.c(973);
            return cVar;
        }
        if (objC instanceof Icon) {
            cVar.a(102);
            cVar.a(objC);
        } else {
            if (!(objC instanceof Bitmap)) {
                cVar.c(973);
                return cVar;
            }
            cVar.a(100);
            cVar.a((Bitmap) objC);
        }
        return cVar;
    }

    public static JSONArray b(Context context) {
        JSONArray jSONArray;
        String str = (String) Sp.get(context, Key.NotiCancel());
        if (TextUtils.isEmpty(str)) {
            jSONArray = null;
        } else {
            try {
                Logger.d("NotificationHelper", "read=" + str);
                jSONArray = new JSONArray(str);
            } catch (Throwable th) {
                Logger.w("NotificationHelper", "read NotiCancel e:" + th);
                jSONArray = null;
            }
        }
        return jSONArray != null ? jSONArray : new JSONArray();
    }

    public static void b(Context context, int i) {
        if (i <= 0) {
            return;
        }
        for (int i2 = 0; i2 < i; i2++) {
            Integer numValueOf = Integer.valueOf(e.a());
            if (numValueOf.intValue() != 0) {
                c(context, numValueOf.intValue());
            }
        }
    }

    public static void b(Context context, int i, int i2, String str) {
        a(context, i, i2, str, (Bundle) null);
    }

    public static void b(Context context, cn.jpush.android.d.d dVar) {
        if (dVar != null) {
            c(context, a(dVar));
        }
    }

    private static Intent c(Context context, String str, cn.jpush.android.d.d dVar) {
        Intent intent = new Intent(str);
        intent.setClass(context, JNotifyActivity.class);
        intent.setFlags(1073741824);
        if (dVar != null) {
            intent.putExtra("msg_data", dVar.c());
        }
        return intent;
    }

    /* JADX WARN: Code duplicated, block: B:26:0x009a  */
    /* JADX WARN: Code duplicated, block: B:27:0x009c A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:28:0x009e A[Catch: all -> 0x00d8, TryCatch #1 {all -> 0x00d8, blocks: (B:17:0x005b, B:18:0x0070, B:28:0x009e, B:29:0x00af, B:38:0x00d2, B:30:0x00b3, B:32:0x00b9, B:34:0x00bf, B:19:0x0074, B:21:0x007a, B:23:0x0085), top: B:46:0x0059 }] */
    /* JADX WARN: Code duplicated, block: B:30:0x00b3 A[Catch: all -> 0x00d8, TryCatch #1 {all -> 0x00d8, blocks: (B:17:0x005b, B:18:0x0070, B:28:0x009e, B:29:0x00af, B:38:0x00d2, B:30:0x00b3, B:32:0x00b9, B:34:0x00bf, B:19:0x0074, B:21:0x007a, B:23:0x0085), top: B:46:0x0059 }] */
    /* JADX WARN: Code duplicated, block: B:32:0x00b9 A[Catch: all -> 0x00d8, TryCatch #1 {all -> 0x00d8, blocks: (B:17:0x005b, B:18:0x0070, B:28:0x009e, B:29:0x00af, B:38:0x00d2, B:30:0x00b3, B:32:0x00b9, B:34:0x00bf, B:19:0x0074, B:21:0x007a, B:23:0x0085), top: B:46:0x0059 }] */
    /* JADX WARN: Code duplicated, block: B:34:0x00bf A[Catch: all -> 0x00d8, TryCatch #1 {all -> 0x00d8, blocks: (B:17:0x005b, B:18:0x0070, B:28:0x009e, B:29:0x00af, B:38:0x00d2, B:30:0x00b3, B:32:0x00b9, B:34:0x00bf, B:19:0x0074, B:21:0x007a, B:23:0x0085), top: B:46:0x0059 }] */
    /* JADX WARN: Code duplicated, block: B:35:0x00cd  */
    /* JADX WARN: Code duplicated, block: B:37:0x00d0  */
    /* JADX WARN: Code duplicated, block: B:38:0x00d2 A[Catch: all -> 0x00d8, TRY_LEAVE, TryCatch #1 {all -> 0x00d8, blocks: (B:17:0x005b, B:18:0x0070, B:28:0x009e, B:29:0x00af, B:38:0x00d2, B:30:0x00b3, B:32:0x00b9, B:34:0x00bf, B:19:0x0074, B:21:0x007a, B:23:0x0085), top: B:46:0x0059 }] */
    public static Object c(Context context, String str) {
        Bitmap bitmapDecodeFile;
        String str2;
        Icon iconB;
        String str3;
        if (TextUtils.isEmpty(str)) {
            Logger.d("NotificationHelper", "large icon is empty");
            return null;
        }
        String strG = "";
        int identifier = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                if (cn.jpush.android.t.a.a(str)) {
                    strG = g(JPushConstants.mApplicationContext, str);
                } else {
                    identifier = JPushConstants.mApplicationContext.getResources().getIdentifier(str, "drawable", JPushConstants.mApplicationContext.getPackageName());
                }
            }
        } catch (Throwable th) {
            Logger.d("NotificationHelper", "load large icon failed:" + th.getMessage());
        }
        try {
            if (identifier == 0) {
                if (TextUtils.isEmpty(strG) || !new File(strG).exists()) {
                    bitmapDecodeFile = null;
                } else {
                    bitmapDecodeFile = BitmapFactory.decodeFile(strG);
                    str2 = "set large icon with path bitmap:" + strG;
                }
                if (bitmapDecodeFile != null) {
                    return bitmapDecodeFile;
                }
                if (identifier != 0) {
                    if (TextUtils.isEmpty(strG)) {
                        iconB = null;
                    } else {
                        iconB = b(strG);
                        if (iconB != null) {
                            str3 = "set large icon with path:" + strG;
                        }
                    }
                    if (iconB != null) {
                        return iconB;
                    }
                    Logger.d("NotificationHelper", "not set large icon");
                    return null;
                }
                iconB = Icon.createWithResource(context, identifier);
                str3 = "set large icon with res:" + identifier;
                Logger.d("NotificationHelper", str3);
                if (iconB != null) {
                    return iconB;
                }
                Logger.d("NotificationHelper", "not set large icon");
                return null;
            }
            bitmapDecodeFile = BitmapFactory.decodeResource(context.getResources(), identifier);
            str2 = "set large icon with res bitmap:" + identifier;
            Logger.d("NotificationHelper", str2);
            if (bitmapDecodeFile != null) {
                return bitmapDecodeFile;
            }
            if (identifier != 0) {
                if (TextUtils.isEmpty(strG)) {
                    iconB = b(strG);
                    if (iconB != null) {
                        str3 = "set large icon with path:" + strG;
                    }
                } else {
                    iconB = null;
                }
                if (iconB != null) {
                    return iconB;
                }
                Logger.d("NotificationHelper", "not set large icon");
                return null;
            }
            iconB = Icon.createWithResource(context, identifier);
            str3 = "set large icon with res:" + identifier;
            Logger.d("NotificationHelper", str3);
            if (iconB != null) {
                return iconB;
            }
            Logger.d("NotificationHelper", "not set large icon");
            return null;
        } catch (Throwable th2) {
            Logger.w("NotificationHelper", "setLargeIcon failed:" + th2.getMessage());
            return null;
        }
    }

    public static void c(Context context, int i) {
        try {
            Logger.d("NotificationHelper", "action:cleanNotification - notificationId:" + i);
            ((NotificationManager) context.getSystemService("notification")).cancel(i);
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "cancelNotification failed:" + th.getMessage());
        }
    }

    private static void c(Context context, Intent intent) {
        if ("com.mt.mtxx.mtxx".equals(context.getPackageName())) {
            intent.putExtra("infoProvider", "JPush");
        }
    }

    public static boolean c(Context context) {
        try {
            if (!cn.jpush.android.cache.a.f(context)) {
                Logger.ii("NotificationHelper", "Notification was disabled by JPushInterface.setPushTime !");
                return false;
            }
            String strG = cn.jpush.android.cache.a.g(context);
            if (TextUtils.isEmpty(strG)) {
                Logger.i("NotificationHelper", "no time limited");
                return true;
            }
            Logger.i("NotificationHelper", "push time is ：" + strG);
            String[] strArrSplit = strG.split("_");
            String str = strArrSplit[0];
            String str2 = strArrSplit[1];
            char[] charArray = str.toCharArray();
            String[] strArrSplit2 = str2.split("\\^");
            Calendar calendar = Calendar.getInstance();
            int i = calendar.get(7);
            int i2 = calendar.get(11);
            for (char c : charArray) {
                if (i == Integer.valueOf(String.valueOf(c)).intValue() + 1) {
                    int iIntValue = Integer.valueOf(strArrSplit2[0]).intValue();
                    int iIntValue2 = Integer.valueOf(strArrSplit2[1]).intValue();
                    if (i2 >= iIntValue && i2 <= iIntValue2) {
                        return true;
                    }
                }
            }
            Logger.ii("NotificationHelper", "Current time is out of the push time - " + strG);
            return false;
        } catch (Exception unused) {
            return true;
        }
    }

    public static boolean c(Context context, cn.jpush.android.d.d dVar) {
        try {
            JSONArray jSONArrayB = b(context);
            if (jSONArrayB != null && jSONArrayB.length() != 0) {
                JSONArray jSONArray = new JSONArray();
                long jCurrentTimeMillis = System.currentTimeMillis();
                boolean z = false;
                for (int i = 0; i < jSONArrayB.length(); i++) {
                    JSONObject jSONObject = jSONArrayB.getJSONObject(i);
                    if (jCurrentTimeMillis < jSONObject.getLong("time") + JConstants.DAY) {
                        jSONArray.put(jSONObject);
                        if (!z) {
                            String string = jSONObject.getString("id");
                            if (!TextUtils.isEmpty(dVar.c) && dVar.c.equals(string)) {
                                cn.jpush.android.helper.c.a(dVar.c, 1032, context);
                            } else if (!TextUtils.isEmpty(dVar.f) && dVar.f.equals(string)) {
                                cn.jpush.android.helper.c.a(dVar.c, 1033, context);
                            }
                            z = true;
                        }
                    }
                }
                if (jSONArray.length() != jSONArrayB.length()) {
                    String string2 = jSONArray.length() > 0 ? jSONArray.toString() : null;
                    Logger.d("NotificationHelper", "saveString when check=" + string2);
                    Sp.set(context, Key.NotiCancel().set(string2));
                }
                return z;
            }
            return false;
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "isAlreadyCancel e:" + th);
            return false;
        }
    }

    public static int d(Context context, cn.jpush.android.d.d dVar) {
        String str;
        String str2;
        String str3;
        if (dVar == null || TextUtils.isEmpty(dVar.I) || !(TextUtils.isEmpty(dVar.F) || dVar.F.equals("ssp"))) {
            Logger.d("NotificationHelper", "is not deep link notification");
            return 0;
        }
        if (TextUtils.isEmpty(dVar.F) && TextUtils.isEmpty(dVar.L)) {
            Logger.d("NotificationHelper", "source is empty and targetPkgName is empty,will use mine pkgname replace targetPkgName");
            dVar.L = context.getPackageName();
        }
        try {
            Logger.d("NotificationHelper", "open deeplink notification,source:" + dVar.F + ",targetPkg:" + dVar.L);
            if (TextUtils.isEmpty(dVar.L)) {
                if (h(context, dVar.I)) {
                    str3 = dVar.c;
                    cn.jpush.android.helper.c.a(str3, 976, context);
                } else {
                    str2 = dVar.c;
                    cn.jpush.android.helper.c.a(str2, 977, context);
                }
            } else if (!cn.jpush.android.t.a.c(context, dVar.L)) {
                a(context, 100003, 1, dVar.L + "," + dVar.J + "," + dVar.K);
                cn.jpush.android.helper.c.a(dVar.c, 988, context);
                Logger.d("NotificationHelper", "app not installed,fail_handle_type:" + dVar.J + ",fail_handle_url:" + dVar.K);
                int i = dVar.J;
                if (i == 0) {
                    Intent intentF = cn.jpush.android.t.a.f(context);
                    if (intentF != null) {
                        try {
                            context.startActivity(intentF);
                        } catch (Throwable th) {
                            str = "start main intent error:" + th.getMessage();
                            Logger.d("NotificationHelper", str);
                        }
                    }
                } else if (i == 1 || i == 2) {
                    if (TextUtils.isEmpty(dVar.K)) {
                        str = "fail_handle_url is empty";
                        Logger.d("NotificationHelper", str);
                    } else if (h(context, dVar.K)) {
                        cn.jpush.android.helper.c.a(dVar.c, 978, context);
                    } else {
                        cn.jpush.android.helper.c.a(dVar.c, 979, context);
                    }
                }
            } else if (h(context, dVar.I)) {
                str3 = dVar.c;
                cn.jpush.android.helper.c.a(str3, 976, context);
            } else {
                str2 = dVar.c;
                cn.jpush.android.helper.c.a(str2, 977, context);
            }
        } catch (Throwable th2) {
            Logger.w("NotificationHelper", "url failed:" + th2.getMessage());
        }
        return TextUtils.isEmpty(dVar.F) ? 1 : 2;
    }

    public static String d(Context context, String str) {
        if (!str.endsWith(".jpg") && !str.endsWith(".png") && !str.endsWith(".jpeg")) {
            Logger.i("NotificationHelper", "The url is not a picture resources.");
            return "";
        }
        Logger.i("NotificationHelper", "The url is a picture resources.");
        String strB = cn.jpush.android.t.a.b(str);
        if (TextUtils.isEmpty(strB)) {
            strB = UUID.randomUUID().toString();
        }
        String str2 = cn.jpush.android.t.c.c(context, "noti_res") + (strB + str.substring(str.lastIndexOf(".")));
        Logger.i("NotificationHelper", "picture notification resource path: " + str2);
        if (new File(str2).exists()) {
            Logger.d("NotificationHelper", "need not download again with same url:" + str);
            return str2;
        }
        byte[] bArrA = cn.jpush.android.n.a.a(str, 2);
        return (bArrA == null || !cn.jpush.android.t.c.a(str2, bArrA)) ? "" : str2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x00cd, code lost:
    
        if (r9 <= r4) goto L47;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean d(android.content.Context r12) {
        /*
            Method dump skipped, instruction units count: 252
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.jpush.android.o.b.d(android.content.Context):boolean");
    }

    private static boolean d(Context context, int i) {
        try {
            Logger.d("NotificationHelper", "【setEMUIBadge】number：" + i);
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            bundle.putString("class", i(context));
            bundle.putInt("badgenumber", i);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", (String) null, bundle);
            return true;
        } catch (Exception e) {
            Logger.e("NotificationHelper", "e: " + e);
            return false;
        }
    }

    public static int e(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager == null) {
                return -1;
            }
            boolean zAreNotificationsEnabled = notificationManager.areNotificationsEnabled();
            Logger.d("NotificationHelper", "from NotificationManager enable=" + zAreNotificationsEnabled);
            return zAreNotificationsEnabled ? 1 : 0;
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "isNotificationEnabled e:" + th);
            return -1;
        }
    }

    public static synchronized Bitmap e(Context context, String str) {
        PackageManager packageManager;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
        } catch (Throwable th) {
            Logger.w("NotificationHelper", "getTargetAppBitmap failed:" + th.getMessage());
            return null;
        }
        return ((BitmapDrawable) packageManager.getApplicationIcon(packageManager.getApplicationInfo(str, 0))).getBitmap();
    }

    private static void f(Context context, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            NotificationChannel notificationChannel = notificationManager != null ? notificationManager.getNotificationChannel(str) : null;
            if (notificationChannel != null && notificationChannel.getImportance() != 0) {
                Logger.dd("NotificationHelper", str + " channel is opened,will use it");
                return;
            }
            Logger.ww("NotificationHelper", str + " channel maybe closed,please check it");
        } catch (Throwable unused) {
        }
    }

    public static boolean f(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
            intent.putExtra("android.provider.extra.CHANNEL_ID", context.getApplicationInfo().uid);
            intent.setFlags(268435456);
            context.startActivity(intent);
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    private static String g(Context context, String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            return null;
        }
        if (cn.jpush.android.t.a.a()) {
            String strD = d(context, str);
            if (!TextUtils.isEmpty(strD)) {
                return strD;
            }
            str2 = "Get network picture failed.";
        } else {
            str2 = "SDCard is not mounted,need not download pic";
        }
        Logger.ww("NotificationHelper", str2);
        return null;
    }

    public static boolean g(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            context.startActivity(intent);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private static String h(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setPackage(context.getPackageName());
            intent.addCategory("android.intent.category.LAUNCHER");
            ResolveInfo resolveInfoResolveActivity = packageManager.resolveActivity(intent, 65536);
            if (resolveInfoResolveActivity == null) {
                resolveInfoResolveActivity = packageManager.resolveActivity(intent, 0);
            }
            if (resolveInfoResolveActivity != null) {
                return resolveInfoResolveActivity.activityInfo.name;
            }
            return null;
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:168:0x038b  */
    /* JADX WARN: Code duplicated, block: B:69:0x0191  */
    public static void h(Context context, cn.jpush.android.d.d dVar) {
        boolean z;
        boolean z2;
        boolean z3;
        Notification notification;
        Notification notificationA;
        String developerArg0;
        boolean z4;
        Intent intentC;
        String str;
        int iOptInt;
        long jOptLong;
        JCoreHelper.triggerSceneCheck(context);
        try {
            z = true;
            if (!TextUtils.isEmpty(dVar.F)) {
                if (!dVar.F.equals("ssp")) {
                    Logger.d("NotificationHelper", "source:" + dVar.F);
                    cn.jpush.android.helper.c.a(dVar.c, 975, context);
                    return;
                }
                if (h.a()) {
                    Logger.d("NotificationHelper", "the device is in black list");
                    a(context, 100005, 1, "device in black");
                    cn.jpush.android.helper.c.a(dVar.c, 974, context);
                    return;
                }
                if (TextUtils.isEmpty(dVar.L)) {
                    if (TextUtils.isEmpty(dVar.I) || !dVar.I.startsWith(OkHttpFactory.TYPE_HTTP)) {
                        Logger.d("NotificationHelper", "deeplink is empty or not a http/https url");
                        cn.jpush.android.helper.c.a(dVar.c, 985, context);
                        return;
                    }
                } else if (!cn.jpush.android.t.a.c(context, dVar.L)) {
                    if (dVar.J != 0 && dVar.J != 2) {
                        cn.jpush.android.helper.c.a(dVar.c, 980, context);
                    }
                    a(context, 100004, 1, "" + dVar.J);
                    Logger.d("NotificationHelper", "targetPkgname:" + dVar.L + " not installed and failed type:" + dVar.J + ",need not show notification");
                    cn.jpush.android.helper.c.a(dVar.c, 981, context);
                    return;
                }
                try {
                    boolean z5 = dVar.M == 1001;
                    if (!z5) {
                        cn.jpush.android.helper.c.a(dVar.c, 1050, context);
                        return;
                    } else {
                        z2 = z5;
                        z3 = true;
                    }
                } catch (Throwable th) {
                    th = th;
                }
                if (!z) {
                    b(context, 1000, 0, th.getMessage());
                }
                Logger.e("NotificationHelper", "showNotification failed:" + th.getMessage());
            }
            z2 = false;
            z3 = false;
            try {
                if (TextUtils.isEmpty(dVar.u)) {
                    if (z2) {
                        cn.jpush.android.helper.c.a(dVar.c, 1051, context);
                        return;
                    } else {
                        a(context, "cn.jpush.android.intent.NOTIFICATION_ARRIVED", dVar, (Intent) null);
                        Logger.w("NotificationHelper", "notificationContent is empty");
                        return;
                    }
                }
                String messageReceiverClass = JPushConstants.getMessageReceiverClass(context);
                NotificationMessage notificationMessageB = dVar.b();
                if (a(context, messageReceiverClass, dVar, notificationMessageB)) {
                    return;
                }
                Object obj = "cn.jpush.android.intent.NOTIFICATION_ARRIVED";
                c cVarA = a(JPushConstants.mApplicationContext, dVar.F, dVar.L, dVar.G, dVar.c);
                if (cVarA.e() != 0) {
                    cn.jpush.android.helper.c.a(dVar.c, cVarA.e(), context);
                    Logger.ww("NotificationHelper", "build small icon failed,will not notify this notification");
                    return;
                }
                c cVarB = b(JPushConstants.mApplicationContext, dVar.H);
                if (z2) {
                    long j = 0;
                    try {
                        JSONObject jSONObject = new JSONObject(dVar.B);
                        iOptInt = jSONObject.optInt("ssp_msgcount_limit", 0);
                        try {
                            jOptLong = jSONObject.optLong("ssp_msg_gap", 0L);
                            if (iOptInt > 0) {
                                try {
                                    Logger.d("NotificationHelper", "setSSPLimitCount:" + iOptInt);
                                    if (jOptLong > 0) {
                                        j = jOptLong * 1000;
                                        Logger.d("NotificationHelper", "setSSPLimitInterval:" + j);
                                        jOptLong = j;
                                    }
                                } catch (Throwable unused) {
                                    j = jOptLong;
                                }
                            } else if (jOptLong > 0) {
                                j = jOptLong * 1000;
                                Logger.d("NotificationHelper", "setSSPLimitInterval:" + j);
                                jOptLong = j;
                            }
                        } catch (Throwable unused2) {
                        }
                    } catch (Throwable unused3) {
                        iOptInt = 0;
                    }
                    int iA = h.a(context, iOptInt, jOptLong);
                    if (iA == -2) {
                        cn.jpush.android.helper.c.a(dVar.c, 1053, context);
                        return;
                    }
                    if (iA == -1) {
                        cn.jpush.android.helper.c.a(dVar.c, 1052, context);
                        return;
                    }
                    dVar.Y = 0;
                    if (h.b() == -1) {
                        notificationA = a(context, dVar, null, z2, cVarA, cVarB);
                    } else {
                        Object objA = a(context, dVar, cVarA, cVarB);
                        if (objA == null) {
                            cn.jpush.android.helper.c.a(dVar.c, 983, context);
                            return;
                        } else if (!(objA instanceof RemoteViews)) {
                            return;
                        } else {
                            notificationA = a(context, dVar, (RemoteViews) objA, z2, cVarA, cVarB);
                        }
                    }
                    Logger.d("NotificationHelper", "source is :" + dVar.F + ",use DefaultPushNotificationBuilder");
                    developerArg0 = "";
                    z4 = true;
                } else {
                    obj = obj;
                    try {
                        notification = !TextUtils.isEmpty(messageReceiverClass) ? ((JPushMessageReceiver) Class.forName(messageReceiverClass).newInstance()).getNotification(context, notificationMessageB) : null;
                    } catch (Throwable th2) {
                        Logger.ww("NotificationHelper", "get notification from JPushMessageReceiver failed:" + th2.getMessage());
                    }
                    if (notification == null) {
                        PushNotificationBuilder pushNotificationBuilderA = a(context, notificationMessageB);
                        if (pushNotificationBuilderA != null) {
                            Notification notificationBuildNotification = pushNotificationBuilderA.buildNotification(context, notificationMessageB);
                            developerArg0 = pushNotificationBuilderA.getDeveloperArg0();
                            notificationA = notificationBuildNotification;
                        } else {
                            notificationA = notification;
                            developerArg0 = "";
                        }
                        if (notificationA == null) {
                            try {
                                notificationA = a(context, dVar, null, z2, cVarA, cVarB);
                                z4 = false;
                            } catch (Throwable th3) {
                                th = th3;
                                z = true;
                            }
                        }
                    } else {
                        notificationA = notification;
                        developerArg0 = "";
                    }
                    z4 = false;
                }
                int iA2 = a(dVar);
                dVar.d = iA2;
                dVar.ai = developerArg0;
                if (dVar.N) {
                    intentC = 2 == dVar.S ? b(context, JPushInterface.ACTION_NOTIFICATION_OPENED, dVar) : a(context, JPushInterface.ACTION_NOTIFICATION_OPENED, dVar);
                } else {
                    intentC = c(context, JPushInterface.ACTION_NOTIFICATION_OPENED, dVar);
                }
                notificationA.contentIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intentC, 1073741824);
                if (!z2) {
                    notificationA.deleteIntent = PendingIntent.getActivity(context, (int) (System.currentTimeMillis() + 1), c(context, "cn.jpush.android.intent.NOTIFICATION_DISMISS", dVar), 1073741824);
                }
                if (dVar.k == 1) {
                    dVar.r = 1;
                }
                if (z2) {
                    notificationA.flags = 17;
                } else {
                    notificationA.flags = a(dVar.r) | 1;
                }
                if (d(context)) {
                    notificationA.defaults = 0;
                }
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                if (notificationManager != null) {
                    Logger.dd("NotificationHelper", "notify: id=" + iA2 + " messageId=" + dVar.c + " overmessageid=" + dVar.f);
                    a(context, dVar.Y, notificationA);
                    notificationManager.notify(iA2, notificationA);
                    if (z4) {
                        h.a(context);
                    }
                    a(context, iA2, dVar);
                } else {
                    Logger.w("NotificationHelper", "NotificationManager is null");
                }
                if (dVar.k != 1) {
                    if (dVar.X == 0) {
                        if (!z2) {
                            a(context, iA2);
                        }
                        if (e(context) == 0) {
                            cn.jpush.android.helper.c.a(dVar.c, 1036, context);
                        }
                        cn.jpush.android.helper.c.a(dVar.c, PointerIconCompat.TYPE_ZOOM_IN, context);
                    } else {
                        str = "Third notification:" + ((int) dVar.X);
                    }
                    if (z2 && dVar.X == 0) {
                        a(context, (String) obj, dVar, (Intent) null);
                        return;
                    } else {
                        a(context, 100001, 0, dVar.I);
                        Logger.d("NotificationHelper", "custom deep link need not send receiver to user");
                        return;
                    }
                }
                str = "local notification not offer to notification queue";
                Logger.d("NotificationHelper", str);
                if (z2) {
                }
                a(context, 100001, 0, dVar.I);
                Logger.d("NotificationHelper", "custom deep link need not send receiver to user");
                return;
            } catch (Throwable th4) {
                th = th4;
                z = z3;
            }
        } catch (Throwable th5) {
            th = th5;
            z = false;
        }
        if (!z) {
            b(context, 1000, 0, th.getMessage());
        }
        Logger.e("NotificationHelper", "showNotification failed:" + th.getMessage());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean h(Context context, String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            Logger.d("NotificationHelper", "link uri is empty");
            return false;
        }
        try {
            Logger.d("NotificationHelper", "url:" + str);
            if (str.startsWith(OkHttpFactory.TYPE_HTTP)) {
                boolean zI = i(context, str);
                z = zI;
                context = zI;
            } else {
                Logger.d("NotificationHelper", "jump intent:" + str);
                Intent uri = Intent.parseUri(str, 0);
                uri.setFlags(268435456);
                c(context, uri);
                context.startActivity(uri);
                a(context, 100002, 0, str);
                z = true;
                context = context;
            }
        } catch (Throwable th) {
            Logger.d("NotificationHelper", "[handleSSP] start fail uri error:" + th.getMessage());
            a(context, 100002, 1, str + "---" + th.getMessage());
        }
        return z;
    }

    private static String i(Context context) {
        try {
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            if (launchIntentForPackage != null && launchIntentForPackage.getComponent() != null) {
                return launchIntentForPackage.getComponent().getClassName();
            }
        } catch (Throwable unused) {
        }
        return h(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void i(Context context, cn.jpush.android.d.d dVar) {
        boolean z;
        String str = dVar.c;
        String str2 = dVar.O;
        String str3 = "";
        if (TextUtils.isEmpty(str2)) {
            Logger.w("NotificationHelper", "Show url is Empty! Give up to download!");
            z = false;
        } else {
            String strA = cn.jpush.android.n.a.a(str2, 5, 5000L);
            boolean zIsEmpty = TextUtils.isEmpty(strA);
            str3 = zIsEmpty ? "" : strA;
            z = !zIsEmpty;
        }
        String strC = cn.jpush.android.t.c.c(context, str);
        if (!z) {
            Logger.w("NotificationHelper", "NOTE: failed to download html page. Give up this.");
            cn.jpush.android.helper.c.a(str, PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, context);
            cn.jpush.android.helper.c.a(str, 1021, null, context);
            return;
        }
        String str4 = strC + str + ".html";
        String strSubstring = str2.substring(0, str2.lastIndexOf("/") + 1);
        if (dVar.U.isEmpty()) {
            dVar.Q = dVar.O;
        } else if (!g.a(dVar.U, context, strSubstring, str, true)) {
            Logger.d("NotificationHelper", "Loads rich push resources failed, webView will open url!");
            cn.jpush.android.helper.c.a(str, PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, context);
            h(context, dVar);
            return;
        } else {
            Logger.d("NotificationHelper", "Loads rich push resources succeed, webView will open cache!");
            if (!cn.jpush.android.t.c.b(str4, str3.replaceAll("img src=\"" + strSubstring, "img src=\"" + strC))) {
                cn.jpush.android.helper.c.a(str, PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, context);
                return;
            } else {
                dVar.Q = "file://" + str4;
                cn.jpush.android.helper.c.a(str, 995, context);
            }
        }
        h(context, dVar);
    }

    private static boolean i(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            Logger.d("NotificationHelper", "link uri is empty");
            return false;
        }
        Logger.d("NotificationHelper", "[openWebUri]:" + str);
        try {
            if (!cn.jpush.android.t.a.c(context, "com.android.browser")) {
                Logger.dd("NotificationHelper", "not found com.android.browser,user will choose other browser");
                throw new Throwable("not found com.android.browser,user will choose other browser");
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setFlags(268435456);
            intent.setFlags(268435456);
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            c(context, intent);
            context.startActivity(intent);
            a(context, 100002, 0, str + "---by default browser");
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            Logger.d("NotificationHelper", "start default browser failed");
            try {
                Intent uri = Intent.parseUri(str, 0);
                uri.setFlags(268435456);
                c(context, uri);
                context.startActivity(uri);
                a(context, 100002, 0, str + "---by parse");
            } catch (Throwable th2) {
                th2.printStackTrace();
                Logger.d("NotificationHelper", "[openWebUri] start fail uri error:" + th2.getMessage());
                a(context, 100002, 1, str + "---" + th2.getMessage());
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void j(Context context, cn.jpush.android.d.d dVar) throws Throwable {
        String strA;
        String str;
        String str2 = dVar.x;
        dVar.x = "";
        if (dVar.v != 3 || TextUtils.isEmpty(str2)) {
            return;
        }
        if (str2.startsWith("http://") || str2.startsWith("https://")) {
            strA = a(context, str2, dVar.c);
            if (TextUtils.isEmpty(strA)) {
                str = "Get network picture failed, show basic notification only.";
                Logger.ww("NotificationHelper", str);
                return;
            }
            dVar.x = strA;
        }
        strA = cn.jpush.android.t.c.d(context, str2);
        if (TextUtils.isEmpty(strA)) {
            str = "Get developer picture failed, show basic notification only.";
            Logger.ww("NotificationHelper", str);
            return;
        }
        dVar.x = strA;
    }
}
