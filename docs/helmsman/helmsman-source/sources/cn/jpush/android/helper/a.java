package cn.jpush.android.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import com.MAVLink.enums.MAV_CMD;
import com.facebook.share.internal.ShareConstants;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static a a;
    private Handler b;

    /* JADX INFO: renamed from: cn.jpush.android.helper.a$a, reason: collision with other inner class name */
    private class RunnableC0009a implements Runnable {
        private Context b;
        private JPushMessageReceiver c;
        private Intent d;

        public RunnableC0009a(Context context, JPushMessageReceiver jPushMessageReceiver, Intent intent) {
            this.b = context;
            this.c = jPushMessageReceiver;
            this.d = intent;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Intent intent = this.d;
                if (intent == null) {
                    Logger.w("JMessageReceiverHelper", "intent was null");
                    return;
                }
                String action = intent.getAction();
                Logger.d("JMessageReceiverHelper", "do receiver action :" + action);
                if (action == null) {
                    Logger.w("JMessageReceiverHelper", "unsupport action type");
                    return;
                }
                if (action.equals("cn.jpush.android.intent.RECEIVE_MESSAGE")) {
                    int intExtra = this.d.getIntExtra("message_type", -1);
                    JPushMessage jPushMessageA = (1 == intExtra || 2 == intExtra) ? cn.jpush.android.r.c.a().a(this.b, this.d) : null;
                    Logger.d("JMessageReceiverHelper", "messageType:" + intExtra + ",jPushMessage:" + jPushMessageA);
                    if (jPushMessageA == null) {
                        Logger.w("JMessageReceiverHelper", "parse tagalias message failed");
                        return;
                    }
                    if (intExtra == 1) {
                        if (jPushMessageA.isTagCheckOperator()) {
                            this.c.onCheckTagOperatorResult(this.b, jPushMessageA);
                            return;
                        } else {
                            this.c.onTagOperatorResult(this.b, jPushMessageA);
                            return;
                        }
                    }
                    if (intExtra == 2) {
                        this.c.onAliasOperatorResult(this.b, jPushMessageA);
                        return;
                    } else {
                        Logger.w("JMessageReceiverHelper", "unsupport message type");
                        return;
                    }
                }
                if (action.equals("mobile_result")) {
                    this.c.onMobileNumberOperatorResult(this.b, d.a().a(this.b, this.d));
                    return;
                }
                if (action.equals(cn.jpush.android.t.a.e(this.b, "custom_msg"))) {
                    this.c.onMessage(this.b, cn.jpush.android.d.b.a(this.d));
                    return;
                }
                if (action.equals(cn.jpush.android.t.a.e(this.b, "cmd_msg"))) {
                    a.a(this.c, this.b, this.d);
                    return;
                }
                if (action.equals("cn.jpush.android.intent.NOTIFICATION_ARRIVED")) {
                    this.c.onNotifyMessageArrived(this.b, cn.jpush.android.o.b.b(this.b, this.d));
                    return;
                }
                if (action.equals("cn.jpush.android.intent.NOTIFICATION_UN_SHOW")) {
                    try {
                        this.c.onNotifyMessageUnShow(this.b, cn.jpush.android.o.b.b(this.b, this.d));
                        return;
                    } catch (Throwable unused) {
                        return;
                    }
                }
                if (action.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
                    this.c.onNotifyMessageOpened(this.b, cn.jpush.android.o.b.b(this.b, this.d));
                    return;
                }
                if (action.equals("cn.jpush.android.intent.NOTIFICATION_DISMISS")) {
                    this.c.onNotifyMessageDismiss(this.b, cn.jpush.android.o.b.b(this.b, this.d));
                    return;
                }
                if (JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION_PROXY.equals(this.d.getAction())) {
                    Intent intent2 = new Intent(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION);
                    if (this.d.getExtras() != null) {
                        intent2.putExtras(this.d.getExtras());
                    }
                    this.c.onMultiActionClicked(this.b, intent2);
                    return;
                }
                if ("on_noti_settings_check".equals(action)) {
                    this.c.onNotificationSettingsCheck(this.b, this.d.getBooleanExtra("isOn", false), this.d.getIntExtra(ShareConstants.FEED_SOURCE_PARAM, 0));
                } else {
                    Logger.w("JMessageReceiverHelper", "unsupport action type");
                }
            } catch (Throwable th) {
                Logger.ww("JMessageReceiverHelper", "MessageReceiver run failed:" + th.getMessage());
            }
        }
    }

    private a() {
        try {
            HandlerThread handlerThread = new HandlerThread("MessageReceiver");
            handlerThread.start();
            this.b = new Handler(handlerThread.getLooper());
        } catch (Throwable th) {
            Logger.w("JMessageReceiverHelper", "create handler failed,error:" + th);
            this.b = new Handler();
        }
    }

    public static a a() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a();
                }
            }
        }
        return a;
    }

    public static void a(Context context, Intent intent) {
        try {
            Logger.i("JMessageReceiverHelper", "Click notification action with extra: " + intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA));
            Intent intent2 = new Intent(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION);
            intent2.putExtras(intent.getExtras());
            intent2.addCategory(context.getPackageName());
            intent2.setPackage(context.getPackageName());
            context.sendBroadcast(intent2, context.getPackageName() + JPushInterface.PUSH_MESSAGE_PERMISSION_POSTFIX);
        } catch (Throwable th) {
            Logger.ww("JMessageReceiverHelper", "Click notification sendBroadcast :" + th.getMessage());
        }
    }

    public static void a(Context context, CustomMessage customMessage) {
        if (context == null || customMessage == null) {
            Logger.dd("JMessageReceiverHelper", "Send custom message to app failed, param is invalid!");
            return;
        }
        Logger.dd("JMessageReceiverHelper", "not found user push message,use old action to user");
        Intent intent = new Intent(JPushInterface.ACTION_MESSAGE_RECEIVED);
        intent.putExtra(JPushInterface.EXTRA_APP_KEY, customMessage.senderId);
        intent.putExtra(JPushInterface.EXTRA_MESSAGE, customMessage.message);
        intent.putExtra(JPushInterface.EXTRA_CONTENT_TYPE, customMessage.contentType);
        intent.putExtra(JPushInterface.EXTRA_TITLE, customMessage.title);
        intent.putExtra(JPushInterface.EXTRA_EXTRA, customMessage.extra);
        intent.putExtra(JPushInterface.EXTRA_MSG_ID, customMessage.messageId);
        intent.addCategory(customMessage.appId);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent, String.format(Locale.ENGLISH, "%s.permission.JPUSH_MESSAGE", customMessage.appId));
        Logger.i("JMessageReceiverHelper", "Send broadcast to app: " + String.format(Locale.ENGLISH, "%s.permission.JPUSH_MESSAGE", customMessage.appId));
    }

    /* JADX WARN: Code duplicated, block: B:41:0x00db A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:44:0x00fb A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:47:0x010a A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:50:0x0119 A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:53:0x0128 A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:56:0x0133 A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:72:0x017a A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:74:0x0188 A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:77:0x0196 A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    /* JADX WARN: Code duplicated, block: B:79:0x019e A[Catch: all -> 0x01d8, TryCatch #0 {all -> 0x01d8, blocks: (B:17:0x0049, B:19:0x0088, B:20:0x008f, B:22:0x0097, B:23:0x009e, B:25:0x00a3, B:27:0x00ab, B:28:0x00af, B:39:0x00d7, B:41:0x00db, B:42:0x00f3, B:44:0x00fb, B:45:0x0102, B:47:0x010a, B:48:0x0111, B:50:0x0119, B:51:0x0120, B:53:0x0128, B:54:0x012f, B:56:0x0133, B:58:0x0137, B:60:0x013c, B:62:0x0140, B:64:0x0148, B:65:0x0157, B:67:0x015b, B:69:0x0163, B:70:0x0174, B:72:0x017a, B:74:0x0188, B:75:0x018c, B:77:0x0196, B:78:0x019a, B:79:0x019e, B:80:0x01a9, B:81:0x01b2, B:29:0x00b3, B:31:0x00b8, B:33:0x00c0, B:34:0x00c5, B:36:0x00ca, B:38:0x00d2), top: B:89:0x0049 }] */
    public static void a(Context context, NotificationMessage notificationMessage, String str) {
        String str2;
        String str3;
        StringBuilder sb;
        String strC;
        Iterator<String> it;
        String next;
        StringBuilder sbAppend;
        List<String> listB;
        if (context == null || notificationMessage == null || TextUtils.isEmpty(str)) {
            Logger.dd("JMessageReceiverHelper", "Send broad cast to app failed,the params are incorrect!");
            return;
        }
        Intent intent = new Intent(str);
        if (str.equals(JPushInterface.ACTION_NOTIFICATION_OPENED) && ((listB = cn.jpush.android.t.a.b(context, intent, notificationMessage.appId + JPushInterface.PUSH_MESSAGE_PERMISSION_POSTFIX)) == null || listB.isEmpty())) {
            Logger.dd("JMessageReceiverHelper", "not found receiver by action:cn.jpush.android.intent.NOTIFICATION_OPENED,will start main activity");
            cn.jpush.android.t.a.e(context);
            return;
        }
        try {
            Logger.dd("JMessageReceiverHelper", "Send push received broadcast to developer defined receiver");
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_MSG_ID, notificationMessage.msgId);
            bundle.putInt(JPushInterface.EXTRA_NOTIFICATION_ID, notificationMessage.notificationId);
            bundle.putString(JPushInterface.EXTRA_ALERT, notificationMessage.notificationContent);
            bundle.putString(JPushInterface.EXTRA_ALERT_TYPE, notificationMessage.notificationAlertType + "");
            if (!TextUtils.isEmpty(notificationMessage.notificationTitle)) {
                bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, notificationMessage.notificationTitle);
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationExtras)) {
                bundle.putString(JPushInterface.EXTRA_EXTRA, notificationMessage.notificationExtras);
            }
            if (notificationMessage.notificationStyle == 1 && !TextUtils.isEmpty(notificationMessage.notificationBigText)) {
                str2 = JPushInterface.EXTRA_BIG_TEXT;
                str3 = notificationMessage.notificationBigText;
            } else {
                if (notificationMessage.notificationStyle != 2 || TextUtils.isEmpty(notificationMessage.notificationInbox)) {
                    if (notificationMessage.notificationStyle == 3 && !TextUtils.isEmpty(notificationMessage.notificationBigPicPath)) {
                        str2 = JPushInterface.EXTRA_BIG_PIC_PATH;
                        str3 = notificationMessage.notificationBigPicPath;
                    }
                    if (notificationMessage.notificationPriority != 0) {
                        bundle.putString(JPushInterface.EXTRA_NOTI_PRIORITY, notificationMessage.notificationPriority + "");
                    }
                    if (!TextUtils.isEmpty(notificationMessage.notificationCategory)) {
                        bundle.putString(JPushInterface.EXTRA_NOTI_CATEGORY, notificationMessage.notificationCategory);
                    }
                    if (!TextUtils.isEmpty(notificationMessage.notificationSmallIcon)) {
                        bundle.putString(JPushInterface.EXTRA_NOTIFICATION_SMALL_ICON, notificationMessage.notificationSmallIcon);
                    }
                    if (!TextUtils.isEmpty(notificationMessage.notificationLargeIcon)) {
                        bundle.putString(JPushInterface.EXTRA_NOTIFICATION_LARGET_ICON, notificationMessage.notificationLargeIcon);
                    }
                    if (!TextUtils.isEmpty(notificationMessage.developerArg0)) {
                        intent.putExtra(JPushInterface.EXTRA_NOTIFICATION_DEVELOPER_ARG0, notificationMessage.developerArg0);
                    }
                    if (notificationMessage.isRichPush) {
                        if (notificationMessage.richType != 0 && notificationMessage.richType != 4 && notificationMessage._webPagePath != null && notificationMessage._webPagePath.startsWith("file://")) {
                            notificationMessage._webPagePath = notificationMessage._webPagePath.replaceFirst("file://", "");
                            intent.putExtra(JPushInterface.EXTRA_RICHPUSH_HTML_PATH, notificationMessage._webPagePath);
                        }
                        if (notificationMessage.showResourceList != null && notificationMessage.showResourceList.size() > 0) {
                            sb = new StringBuilder();
                            strC = cn.jpush.android.t.c.c(context, notificationMessage.msgId);
                            it = notificationMessage.showResourceList.iterator();
                            while (it.hasNext()) {
                                next = it.next();
                                if (next.startsWith("http://")) {
                                    next = cn.jpush.android.t.c.b(next);
                                }
                                if (TextUtils.isEmpty(sb.toString())) {
                                    sbAppend = sb.append(strC);
                                } else {
                                    sbAppend = sb.append(",").append(strC);
                                }
                                sbAppend.append(next);
                            }
                            intent.putExtra(JPushInterface.EXTRA_RICHPUSH_HTML_RES, sb.toString());
                        }
                    }
                    intent.putExtras(bundle);
                    intent.addCategory(notificationMessage.appId);
                    intent.setPackage(context.getPackageName());
                    context.sendBroadcast(intent, notificationMessage.appId + JPushInterface.PUSH_MESSAGE_PERMISSION_POSTFIX);
                }
                str2 = JPushInterface.EXTRA_INBOX;
                str3 = notificationMessage.notificationInbox;
            }
            bundle.putString(str2, str3);
            if (notificationMessage.notificationPriority != 0) {
                bundle.putString(JPushInterface.EXTRA_NOTI_PRIORITY, notificationMessage.notificationPriority + "");
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationCategory)) {
                bundle.putString(JPushInterface.EXTRA_NOTI_CATEGORY, notificationMessage.notificationCategory);
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationSmallIcon)) {
                bundle.putString(JPushInterface.EXTRA_NOTIFICATION_SMALL_ICON, notificationMessage.notificationSmallIcon);
            }
            if (!TextUtils.isEmpty(notificationMessage.notificationLargeIcon)) {
                bundle.putString(JPushInterface.EXTRA_NOTIFICATION_LARGET_ICON, notificationMessage.notificationLargeIcon);
            }
            if (!TextUtils.isEmpty(notificationMessage.developerArg0)) {
                intent.putExtra(JPushInterface.EXTRA_NOTIFICATION_DEVELOPER_ARG0, notificationMessage.developerArg0);
            }
            if (notificationMessage.isRichPush) {
                if (notificationMessage.richType != 0) {
                    notificationMessage._webPagePath = notificationMessage._webPagePath.replaceFirst("file://", "");
                    intent.putExtra(JPushInterface.EXTRA_RICHPUSH_HTML_PATH, notificationMessage._webPagePath);
                }
                if (notificationMessage.showResourceList != null) {
                    sb = new StringBuilder();
                    strC = cn.jpush.android.t.c.c(context, notificationMessage.msgId);
                    it = notificationMessage.showResourceList.iterator();
                    while (it.hasNext()) {
                        next = it.next();
                        if (next.startsWith("http://")) {
                            next = cn.jpush.android.t.c.b(next);
                        }
                        if (TextUtils.isEmpty(sb.toString())) {
                            sbAppend = sb.append(strC);
                        } else {
                            sbAppend = sb.append(",").append(strC);
                        }
                        sbAppend.append(next);
                    }
                    intent.putExtra(JPushInterface.EXTRA_RICHPUSH_HTML_RES, sb.toString());
                }
            }
            intent.putExtras(bundle);
            intent.addCategory(notificationMessage.appId);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent, notificationMessage.appId + JPushInterface.PUSH_MESSAGE_PERMISSION_POSTFIX);
        } catch (Throwable th) {
            Logger.ww("JMessageReceiverHelper", "sendNotificationReceivedBroadcast error:" + th.getMessage());
            cn.jpush.android.t.a.a(context, intent, notificationMessage.appId + JPushInterface.PUSH_MESSAGE_PERMISSION_POSTFIX);
        }
    }

    public static void a(JPushMessageReceiver jPushMessageReceiver, Context context, Intent intent) {
        CmdMessage cmdMessage;
        try {
            int intExtra = intent.getIntExtra(JThirdPlatFormInterface.KEY_CODE, -2);
            int intExtra2 = intent.getIntExtra("cmd", -2);
            String stringExtra = intent.getStringExtra(ShareConstants.WEB_DIALOG_PARAM_MESSAGE);
            Logger.d("JMessageReceiverHelper", "[callCmdMessage] code:" + intExtra + ",cmd:" + intExtra2 + ",message:" + stringExtra);
            Bundle extras = intent.getExtras();
            if (intExtra2 == -1) {
                jPushMessageReceiver.onConnected(context, false);
                return;
            }
            if (intExtra2 == 0) {
                if (intExtra == 0) {
                    jPushMessageReceiver.onRegister(context, stringExtra);
                    return;
                }
                cmdMessage = new CmdMessage(intExtra2, intExtra, stringExtra, extras);
            } else {
                if (intExtra2 == 1) {
                    jPushMessageReceiver.onConnected(context, true);
                    return;
                }
                if (intExtra2 == 2001) {
                    return;
                }
                if (intExtra2 != 10000) {
                    switch (intExtra2) {
                        default:
                            switch (intExtra2) {
                                case 100001:
                                case 100002:
                                case 100003:
                                case 100004:
                                case 100005:
                                    break;
                                default:
                                    Logger.dd("JMessageReceiverHelper", "unkown cmd:" + intExtra2);
                                    break;
                            }
                        case MAV_CMD.MAV_CMD_DO_TRIGGER_CONTROL /* 2003 */:
                        case 2004:
                        case 2005:
                        case 2006:
                        case 2007:
                            cmdMessage = new CmdMessage(intExtra2, intExtra, stringExtra, extras);
                            break;
                    }
                }
                cmdMessage = new CmdMessage(intExtra2, intExtra, stringExtra, extras);
            }
            jPushMessageReceiver.onCommandResult(context, cmdMessage);
        } catch (Throwable th) {
            Logger.e("JMessageReceiverHelper", "callCmdMessage failed:" + th.getMessage());
        }
    }

    public void a(Context context, JPushMessageReceiver jPushMessageReceiver, Intent intent) {
        this.b.post(new RunnableC0009a(context, jPushMessageReceiver, intent));
    }
}
