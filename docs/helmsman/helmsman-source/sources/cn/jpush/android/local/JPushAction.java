package cn.jpush.android.local;

import android.content.Context;
import android.content.Intent;
import cn.jpush.android.api.CallBackParams;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.l.a;
import cn.jpush.android.service.JPushMessageReceiver;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class JPushAction {
    public abstract Object beforLogin(Context context, String str, int i, String str2);

    public abstract ProxyActivityAction getPopWinActivity(Context context);

    public abstract ProxyActivityAction getPushActivity(Context context);

    public String getSdkVersion(String str) {
        return a.b;
    }

    public abstract Object handleAction(Context context, String str, Object obj);

    public abstract void handleNotificationIntent(Context context, Intent intent);

    public boolean isSupportedCMD(String str, int i) {
        return i == 3 || i == 29 || i == 28 || i == 27 || i == 10 || i == 26 || i == 25;
    }

    public abstract void onJPushMessageReceive(Context context, JPushMessageReceiver jPushMessageReceiver, Intent intent);

    public abstract void onMessage(Context context, CustomMessage customMessage);

    public abstract void onMultiAction(Context context, Intent intent);

    public abstract void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage);

    public abstract void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage);

    public abstract void onTagAliasResponse(Context context, long j, int i, Intent intent);

    public abstract void setAliasAndTags(Context context, String str, Set<String> set, CallBackParams callBackParams);
}
