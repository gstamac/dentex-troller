package cn.jpush.android.api;

import android.R;
import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import android.widget.RemoteViews;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.o.a;
import cn.jpush.android.o.b;
import cn.jpush.android.o.f;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DefaultPushNotificationBuilder implements PushNotificationBuilder {
    public static final int NOTI_STYLE_BIG_PICTURE = 3;
    public static final int NOTI_STYLE_BIG_TEXT = 1;
    public static final int NOTI_STYLE_INBOX = 2;
    private static final String TAG = "DefaultPushNotificationBuilder";
    protected Context context;
    private NotificationMessage notificationMessage;

    RemoteViews buildContentView(String str, String str2) {
        return null;
    }

    /* JADX WARN: Code duplicated, block: B:28:0x0128  */
    /* JADX WARN: Code duplicated, block: B:31:0x0148  */
    /* JADX WARN: Code duplicated, block: B:34:0x0157  */
    /* JADX WARN: Code duplicated, block: B:37:0x0166  */
    @Override // cn.jpush.android.api.PushNotificationBuilder
    public Notification buildNotification(Context context, NotificationMessage notificationMessage) {
        String str;
        String str2;
        this.context = context;
        this.notificationMessage = notificationMessage;
        HashMap map = new HashMap();
        if (notificationMessage != null) {
            Logger.d(TAG, "notificationContent:" + notificationMessage.notificationContent + "\nnotificationTitle" + notificationMessage.notificationTitle + "\nnotificationStyle:" + notificationMessage.notificationStyle + "\nnotificationPriority:" + notificationMessage.notificationPriority + "\nnotificationBigText:" + notificationMessage.notificationBigText + "\nnotificationBigPicPath:" + notificationMessage.notificationBigPicPath + "\nnotificationInbox:" + notificationMessage.notificationInbox + "\nnotificationCategory:" + notificationMessage.notificationCategory + "\nnotificationAlertType:" + notificationMessage.notificationAlertType + "\nnotificationSmallIcon:" + notificationMessage.notificationSmallIcon + "\nnotificationLargeIcon:" + notificationMessage.notificationLargeIcon + "\nextraJson:" + notificationMessage.notificationExtras);
            map.put(JPushInterface.EXTRA_MSG_ID, notificationMessage.msgId);
            map.put(JPushInterface.EXTRA_ALERT, notificationMessage.notificationContent);
            map.put(JPushInterface.EXTRA_ALERT_TYPE, notificationMessage.notificationAlertType + "");
            if (!TextUtils.isEmpty(notificationMessage.notificationTitle)) {
                map.put(JPushInterface.EXTRA_NOTIFICATION_TITLE, notificationMessage.notificationTitle);
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationExtras)) {
                map.put(JPushInterface.EXTRA_EXTRA, notificationMessage.notificationExtras);
            }
            if (notificationMessage.notificationStyle == 1 && !TextUtils.isEmpty(notificationMessage.notificationBigText)) {
                str = JPushInterface.EXTRA_BIG_TEXT;
                str2 = notificationMessage.notificationBigText;
            } else if (notificationMessage.notificationStyle == 2 && !TextUtils.isEmpty(notificationMessage.notificationInbox)) {
                str = JPushInterface.EXTRA_INBOX;
                str2 = notificationMessage.notificationInbox;
            } else if (notificationMessage.notificationStyle != 3 || TextUtils.isEmpty(notificationMessage.notificationBigPicPath)) {
                if (notificationMessage.notificationPriority != 0) {
                    map.put(JPushInterface.EXTRA_NOTI_PRIORITY, notificationMessage.notificationPriority + "");
                }
                if (!TextUtils.isEmpty(notificationMessage.notificationCategory)) {
                    map.put(JPushInterface.EXTRA_NOTI_CATEGORY, notificationMessage.notificationCategory);
                }
                if (!TextUtils.isEmpty(notificationMessage.notificationSmallIcon)) {
                    map.put(JPushInterface.EXTRA_NOTIFICATION_SMALL_ICON, notificationMessage.notificationSmallIcon);
                }
                if (!TextUtils.isEmpty(notificationMessage.notificationLargeIcon)) {
                    map.put(JPushInterface.EXTRA_NOTIFICATION_LARGET_ICON, notificationMessage.notificationSmallIcon);
                }
            } else {
                str = JPushInterface.EXTRA_BIG_PIC_PATH;
                str2 = notificationMessage.notificationBigPicPath;
            }
            map.put(str, str2);
            if (notificationMessage.notificationPriority != 0) {
                map.put(JPushInterface.EXTRA_NOTI_PRIORITY, notificationMessage.notificationPriority + "");
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationCategory)) {
                map.put(JPushInterface.EXTRA_NOTI_CATEGORY, notificationMessage.notificationCategory);
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationSmallIcon)) {
                map.put(JPushInterface.EXTRA_NOTIFICATION_SMALL_ICON, notificationMessage.notificationSmallIcon);
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationLargeIcon)) {
                map.put(JPushInterface.EXTRA_NOTIFICATION_LARGET_ICON, notificationMessage.notificationSmallIcon);
            }
        }
        return buildNotification(map);
    }

    @Override // cn.jpush.android.api.PushNotificationBuilder
    public Notification buildNotification(Map<String, String> map) {
        String string;
        Context context = this.context;
        if (context == null) {
            Logger.w(TAG, "Can't find valid context when build notification.");
            return null;
        }
        try {
            string = context.getApplicationInfo().loadLabel(this.context.getPackageManager()).toString();
        } catch (Throwable unused) {
            string = null;
        }
        if (map.containsKey(JPushInterface.EXTRA_MSG_ID)) {
            map.get(JPushInterface.EXTRA_MSG_ID);
        }
        String str = map.containsKey(JPushInterface.EXTRA_ALERT) ? map.get(JPushInterface.EXTRA_ALERT) : "";
        if (TextUtils.isEmpty(str)) {
            Logger.ww(TAG, "No notification content to show. Give up.");
            return null;
        }
        if (map.containsKey(JPushInterface.EXTRA_NOTIFICATION_SMALL_ICON)) {
            map.get(JPushInterface.EXTRA_NOTIFICATION_SMALL_ICON);
        }
        String str2 = map.containsKey(JPushInterface.EXTRA_NOTIFICATION_LARGET_ICON) ? map.get(JPushInterface.EXTRA_NOTIFICATION_LARGET_ICON) : "";
        if (map.containsKey(JPushInterface.EXTRA_NOTIFICATION_TITLE)) {
            string = map.get(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        }
        String str3 = map.containsKey(JPushInterface.EXTRA_BIG_TEXT) ? map.get(JPushInterface.EXTRA_BIG_TEXT) : "";
        String str4 = map.containsKey(JPushInterface.EXTRA_INBOX) ? map.get(JPushInterface.EXTRA_INBOX) : "";
        int i = map.containsKey(JPushInterface.EXTRA_NOTI_PRIORITY) ? Integer.parseInt(map.get(JPushInterface.EXTRA_NOTI_PRIORITY)) : 0;
        String str5 = map.containsKey(JPushInterface.EXTRA_NOTI_CATEGORY) ? map.get(JPushInterface.EXTRA_NOTI_CATEGORY) : "";
        String str6 = map.containsKey(JPushInterface.EXTRA_BIG_PIC_PATH) ? map.get(JPushInterface.EXTRA_BIG_PIC_PATH) : "";
        int i2 = map.containsKey(JPushInterface.EXTRA_ALERT_TYPE) ? Integer.parseInt(map.get(JPushInterface.EXTRA_ALERT_TYPE)) : -1;
        int i3 = (i2 < -1 || i2 > 7) ? -1 : i2;
        RemoteViews remoteViewsBuildContentView = buildContentView(str, string);
        Notification.Builder builder = new Notification.Builder(this.context);
        builder.setContentTitle(string).setContentText(str).setTicker(str);
        int iA = f.a(this.context);
        if (iA != 0) {
            Logger.dd(TAG, "setSmallIcon with resId:" + iA);
        } else {
            Logger.ww(TAG, "not found small icon in your app,will setSmallIcon with android.R.color.transparent ");
            iA = R.color.transparent;
        }
        builder.setSmallIcon(iA);
        Object objC = b.c(this.context, str2);
        if (objC instanceof Icon) {
            builder.setLargeIcon((Icon) objC);
        }
        int i4 = 1;
        builder.setShowWhen(true);
        Context context2 = this.context;
        NotificationMessage notificationMessage = this.notificationMessage;
        int i5 = i3;
        String str7 = str6;
        a.a(context2, builder, notificationMessage != null ? notificationMessage.notificationChannelId : "", "", i, i5, (String) null);
        if (remoteViewsBuildContentView == null) {
            if (!TextUtils.isEmpty(str3)) {
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
                bigTextStyle.bigText(str3);
                builder.setStyle(bigTextStyle);
            }
            if (!TextUtils.isEmpty(str4)) {
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
                try {
                    TreeMap treeMap = new TreeMap();
                    JSONObject jSONObject = new JSONObject(str4);
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
                    Logger.ee(TAG, "Set inbox style error: " + th.getMessage());
                }
                builder.setStyle(inboxStyle);
            }
            if (!TextUtils.isEmpty(str7)) {
                Logger.i(TAG, "Set notification BPS with picture path:" + str7);
                try {
                    Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
                    bigPictureStyle.bigPicture(BitmapFactory.decodeFile(str7));
                    builder.setStyle(bigPictureStyle);
                } catch (Throwable unused2) {
                    Logger.ww(TAG, "Create big picture style failed.");
                }
            }
        }
        if (i != 0) {
            if (i == 1) {
                builder.setPriority(i4);
            } else {
                int i6 = 2;
                if (i != 2) {
                    i4 = -1;
                    if (i != -1) {
                        i6 = -2;
                        if (i != -2) {
                            i4 = 0;
                        }
                    }
                    builder.setPriority(i4);
                }
                builder.setPriority(i6);
            }
        }
        if (!TextUtils.isEmpty(str5)) {
            builder.setCategory(str5);
        }
        if (remoteViewsBuildContentView != null) {
            builder.setContent(remoteViewsBuildContentView);
        } else {
            Logger.i(TAG, " Use default notification view! ");
        }
        builder.setDefaults(i5);
        return getNotification(builder);
    }

    @Override // cn.jpush.android.api.PushNotificationBuilder
    public String getDeveloperArg0() {
        return null;
    }

    Notification getNotification(Notification.Builder builder) {
        try {
            return builder.build();
        } catch (Throwable th) {
            Logger.ww(TAG, "Build notification error:", th);
            return null;
        }
    }

    void resetNotificationParams(Notification notification) {
    }

    public String toString() {
        return "";
    }
}
