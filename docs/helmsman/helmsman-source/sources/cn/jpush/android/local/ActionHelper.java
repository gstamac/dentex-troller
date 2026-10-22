package cn.jpush.android.local;

import android.content.Context;
import android.content.Intent;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import cn.jpush.android.api.CallBackParams;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.helper.JCoreHelper;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.service.JPushMessageReceiver;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class ActionHelper {
    private static final String TAG = "ActionHelper";
    private static volatile ActionHelper actionHelper;
    private static final Object locker = new Object();
    private JPushAction jPushAction;
    private cn.jpush.android.p.a loadedResource;

    class a implements Runnable {
        private Context b;
        private String c;
        private Object d;

        public a(Context context, String str, Object obj) {
            this.b = context;
            this.c = str;
            this.d = obj;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ActionHelper.this.initLoad(this.b);
                ActionHelper.this.jPushAction.handleAction(this.b, this.c, this.d);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    class b implements Runnable {
        private Context b;
        private String c;
        private int d;
        private Set<String> e;
        private CallBackParams f;

        public b(Context context, String str, Set<String> set, int i, CallBackParams callBackParams) {
            this.f = callBackParams;
            this.c = str;
            this.d = i;
            this.b = context;
            this.e = set;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ActionHelper.this.initLoad(this.b);
                ActionHelper.this.jPushAction.setAliasAndTags(this.b, this.c, this.e, this.f);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private ActionHelper() {
        JCoreManager.onEvent(null, JPushConstants.SDK_TYPE, 13, TAG, null, new Object[0]);
    }

    public static ActionHelper getInstance() {
        if (actionHelper == null) {
            synchronized (locker) {
                if (actionHelper == null) {
                    actionHelper = new ActionHelper();
                }
            }
        }
        return actionHelper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:20:0x0077 A[Catch: all -> 0x0080, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0005, B:18:0x0073, B:20:0x0077, B:17:0x005d, B:8:0x000b, B:10:0x000f, B:12:0x0015, B:14:0x0046), top: B:26:0x0005, inners: #1 }] */
    public synchronized void initLoad(Context context) {
        if (this.jPushAction != null) {
            return;
        }
        try {
            if (!JPushConstants.SUPPORT_DY || JConstants.SDK_VERSION_INT < 220) {
                if (this.jPushAction == null) {
                    this.jPushAction = new cn.jpush.android.k.a();
                }
                return;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.loadedResource = cn.jpush.android.p.a.a(context);
            Logger.d(TAG, "load use time:" + (System.currentTimeMillis() - jCurrentTimeMillis) + ",lr:" + this.loadedResource);
            cn.jpush.android.p.a aVar = this.loadedResource;
            if (aVar != null) {
                Class clsB = aVar.b("cn.p.jpush.JPushActionImpl");
                Logger.d(TAG, "load from cloud");
                this.jPushAction = (JPushAction) clsB.newInstance();
            }
            if (this.jPushAction == null) {
                this.jPushAction = new cn.jpush.android.k.a();
            }
            return;
        } catch (Throwable th) {
            Logger.ww(TAG, "try l p failed:" + th.getMessage());
        }
        throw th;
    }

    public Object beforLogin(Context context, String str, int i, String str2) {
        initLoad(context);
        return this.jPushAction.beforLogin(context, str, i, str2);
    }

    public void doAction(Context context, String str, Object obj) {
        Logger.dd(TAG, "doAction:" + str);
        JCoreHelper.execue(context, TAG, new a(context, str, obj));
    }

    public ProxyActivityAction getPopWinActivity(Context context) {
        initLoad(context);
        return this.jPushAction.getPopWinActivity(context);
    }

    public ProxyActivityAction getPushActivity(Context context) {
        initLoad(context);
        return this.jPushAction.getPushActivity(context);
    }

    public int getResourceID(Context context, String str, String str2, String str3) {
        int identifier;
        StringBuilder sb;
        cn.jpush.android.p.a aVar = this.loadedResource;
        if (aVar != null) {
            identifier = aVar.a(str2, str);
            sb = new StringBuilder("[getResourceID] try load:");
        } else {
            identifier = context.getResources().getIdentifier(str, str2, str3);
            sb = new StringBuilder("[getResourceID]:");
        }
        Logger.d(TAG, sb.append(identifier).toString());
        return identifier;
    }

    public String getSdkVersion(String str) {
        JPushAction jPushAction = this.jPushAction;
        return jPushAction != null ? jPushAction.getSdkVersion(str) : JPushConstants.SDK_VERSION_NAME;
    }

    public void handleNotificationIntent(Context context, Intent intent) {
        initLoad(context);
        this.jPushAction.handleNotificationIntent(context, intent);
    }

    public boolean isSupportedCMD(String str, int i) {
        JPushAction jPushAction = this.jPushAction;
        if (jPushAction != null) {
            return jPushAction.isSupportedCMD(str, i);
        }
        return i == 3 || i == 29 || i == 28 || i == 27 || i == 10 || i == 26 || i == 25;
    }

    public void onJPushMessageReceive(Context context, JPushMessageReceiver jPushMessageReceiver, Intent intent) {
        initLoad(context);
        this.jPushAction.onJPushMessageReceive(context, jPushMessageReceiver, intent);
    }

    public void onMessage(Context context, CustomMessage customMessage) {
        initLoad(context);
        this.jPushAction.onMessage(context, customMessage);
    }

    public void onMultiAction(Context context, Intent intent) {
        initLoad(context);
        this.jPushAction.onMultiAction(context, intent);
    }

    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        initLoad(context);
        this.jPushAction.onNotifyMessageArrived(context, notificationMessage);
    }

    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        initLoad(context);
        this.jPushAction.onNotifyMessageOpened(context, notificationMessage);
    }

    public void onTagAliasResponse(Context context, long j, int i, Intent intent) {
        initLoad(context);
        this.jPushAction.onTagAliasResponse(context, j, i, intent);
    }

    public void setAliasAndTagsWithCallBack(Context context, String str, Set<String> set, TagAliasCallback tagAliasCallback, int i, int i2) {
        JCoreHelper.execue(context, TAG, new b(context, str, set, 0, new CallBackParams(str, set, tagAliasCallback, System.currentTimeMillis(), i, i2)));
    }

    public void setAliasWithCallBack(Context context, int i, String str, int i2, int i3) {
        JCoreHelper.execue(context, TAG, new b(context, str, null, i, new CallBackParams(i, str, System.currentTimeMillis(), i2, i3)));
    }

    public void setTagsWithCallBack(Context context, int i, Set<String> set, int i2, int i3) {
        JCoreHelper.execue(context, TAG, new b(context, null, set, i, new CallBackParams(i, set, System.currentTimeMillis(), i2, i3)));
    }
}
