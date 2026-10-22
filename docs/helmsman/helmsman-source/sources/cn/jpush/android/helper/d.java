package cn.jpush.android.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.JThirdPlatFormInterface;
import cn.jpush.android.local.JPushConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/* JADX INFO: loaded from: classes.dex */
public class d {
    private static ConcurrentLinkedQueue<Long> a = new ConcurrentLinkedQueue<>();
    private static final Object c = new Object();
    private static d d;
    private ConcurrentHashMap<Long, a> b = new ConcurrentHashMap<>();

    private class a {
        public int a;
        public String b;

        public a(int i, String str) {
            this.a = i;
            this.b = str;
        }

        public String toString() {
            return "MobileBean{sequence=" + this.a + ", mobileNumber='" + this.b + "'}";
        }
    }

    private static int a(long j) {
        if (a.size() >= 3) {
            long jLongValue = j - a.element().longValue();
            if (jLongValue < 0) {
                a.clear();
                return 2;
            }
            if (jLongValue <= OkHttpUtils.DEFAULT_MILLISECONDS) {
                return 1;
            }
            while (a.size() >= 3) {
                a.poll();
            }
        }
        a.offer(Long.valueOf(j));
        return 0;
    }

    public static synchronized d a() {
        if (d == null) {
            synchronized (c) {
                if (d == null) {
                    d = new d();
                }
            }
        }
        return d;
    }

    public JPushMessage a(Context context, Intent intent) {
        JPushMessage jPushMessage;
        Object th;
        if (intent == null) {
            return null;
        }
        try {
            int intExtra = intent.getIntExtra("sequence", -1);
            int intExtra2 = intent.getIntExtra(JThirdPlatFormInterface.KEY_CODE, -1);
            String stringExtra = intent.getStringExtra("mobile");
            jPushMessage = new JPushMessage();
            try {
                jPushMessage.setSequence(intExtra);
                jPushMessage.setErrorCode(intExtra2);
                jPushMessage.setMobileNumber(stringExtra);
            } catch (Throwable th2) {
                th = th2;
                Logger.w("MobileNumberHelper", "parese mobile number response to JPushMessage failed, error:" + th);
            }
        } catch (Throwable th3) {
            jPushMessage = null;
            th = th3;
        }
        return jPushMessage;
    }

    public void a(Context context, int i, int i2, String str) {
        try {
            Logger.d("MobileNumberHelper", "Action - onResult,sequence:" + i + ",code:" + i2 + ",mobileNumber:" + str);
            String messageReceiverClass = JPushConstants.getMessageReceiverClass(context);
            Intent intent = new Intent();
            if (TextUtils.isEmpty(messageReceiverClass)) {
                Logger.d("MobileNumberHelper", "not found messageReceiver");
            } else {
                intent = new Intent(context, Class.forName(messageReceiverClass));
                intent.setAction("mobile_result");
                intent.putExtra("sequence", i);
                intent.putExtra(JThirdPlatFormInterface.KEY_CODE, i2);
                intent.putExtra("mobile", str);
            }
            context.sendBroadcast(intent);
        } catch (Throwable th) {
            Logger.ww("MobileNumberHelper", "onResult error:" + th);
        }
    }

    public void a(Context context, int i, String str) {
        Logger.dd("MobileNumberHelper", "action - setMobileNubmer, sequence:" + i + ",mobileNumber:" + str);
        Bundle bundle = new Bundle();
        bundle.putInt("sequence", i);
        bundle.putString("mobile", str);
        JCoreHelper.runActionWithService(context, JPushConstants.SDK_TYPE, "set_mobile", bundle);
    }

    public void a(Context context, long j, int i) {
        if (this.b.size() != 0) {
            a aVarRemove = this.b.remove(Long.valueOf(j));
            Logger.d("MobileNumberHelper", "mobileBean:" + aVarRemove);
            if (aVarRemove == null) {
                Logger.d("MobileNumberHelper", "#unexcepted, do not find mobile number request cache");
                return;
            }
            if (i == 0) {
                cn.jpush.android.cache.a.a(context, aVarRemove.b);
            } else if (i == 11) {
                i = JPushInterface.ErrorCode.ERROR_CODE_INTERNEL_SERVER_ERROR;
            } else if (i == 10) {
                i = JPushInterface.ErrorCode.ERROR_CODE_INVALID_MOBILENUMBER;
            }
            a(context, aVarRemove.a, i, aVarRemove.b);
        }
    }

    public void a(Context context, Bundle bundle) {
        if (bundle == null) {
            Logger.ww("MobileNumberHelper", "[setMobileNum] bundle is bull");
            return;
        }
        int i = bundle.getInt("sequence", 0);
        String string = bundle.getString("mobile");
        String strE = cn.jpush.android.cache.a.e(context);
        Logger.dd("MobileNumberHelper", "action:setMobileNum sequence:" + i + ",mobileNumber:" + string + ",lastMobileNumber:" + strE);
        if (strE != null && TextUtils.equals(string, strE)) {
            Logger.dd("MobileNumberHelper", "already set this mobile number");
            a(context, i, JPushInterface.ErrorCode.SUCCESS, string);
            return;
        }
        if (cn.jpush.android.cache.a.b(context) == 1) {
            a(context, i, JPushInterface.ErrorCode.PUSH_STOPED, string);
            return;
        }
        if (strE != null) {
            cn.jpush.android.cache.a.a(context, (String) null);
        }
        int iA = a(System.currentTimeMillis());
        if (iA != 0) {
            Logger.w("MobileNumberHelper", iA == 1 ? "set mobile number too soon,over 3 times in 10s" : "set mobile number failed,time shaft error，please try again");
            a(context, i, iA == 1 ? JPushInterface.ErrorCode.INVOKE_TOO_SOON : JPushInterface.ErrorCode.INCORRECT_TIME, string);
            return;
        }
        int iC = cn.jpush.android.t.d.c(string);
        if (iC != 0) {
            Logger.dd("MobileNumberHelper", "Invalid mobile number: " + string + ", will not set mobile number this time.");
            a(context, i, iC, string);
        } else {
            byte[] bArrA = cn.jpush.android.q.b.a(string);
            long jA = g.a();
            this.b.put(Long.valueOf(jA), new a(i, string));
            JCoreHelper.sendRequest(context, JPushConstants.SDK_TYPE, 26, 1, jA, 0L, bArrA);
        }
    }
}
