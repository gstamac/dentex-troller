package cn.jpush.android.support;

import android.content.Context;
import android.text.TextUtils;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.MultiActionsNotificationBuilder;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.api.PushNotificationBuilder;
import cn.jpush.android.cache.a;
import cn.jpush.android.helper.Logger;
import io.reactivex.annotations.SchedulerSupport;

/* JADX INFO: loaded from: classes.dex */
public class JPushSupport {
    private static final Integer a = 0;

    public static PushNotificationBuilder getNotificationBuilder(Context context, NotificationMessage notificationMessage) {
        try {
            int iIntValue = notificationMessage.notificationBuilderId;
            if (iIntValue < 1) {
                iIntValue = a.intValue();
            }
            String strD = a.d(context, iIntValue + "");
            if (TextUtils.isEmpty(strD)) {
                Logger.d("JPushSupport", "not found custom notification");
                return null;
            }
            Logger.ii("JPushSupport", "get customBuilder:" + strD);
            if (!strD.startsWith("basic") && !strD.startsWith(SchedulerSupport.CUSTOM)) {
                return MultiActionsNotificationBuilder.parseFromPreference(context, strD);
            }
            return BasicPushNotificationBuilder.parseFromPreference(context, strD);
        } catch (Throwable th) {
            Logger.ww("JPushSupport", "getNotification failed:" + th.getMessage());
            return null;
        }
    }
}
