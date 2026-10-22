package cn.jpush.android.r;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import cn.jpush.android.api.CallBackParams;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.service.TagAliasReceiver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private static volatile c a;
    private static final Object d = new Object();
    private TagAliasReceiver b;
    private ConcurrentHashMap<Long, CallBackParams> c = new ConcurrentHashMap<>();
    private AtomicBoolean e = new AtomicBoolean(false);

    private c() {
    }

    public static c a() {
        if (a == null) {
            synchronized (d) {
                if (a == null) {
                    a = new c();
                }
            }
        }
        return a;
    }

    private String a(CallBackParams callBackParams) {
        if (callBackParams == null) {
            Logger.w("TagAliasOperator", "callBack was null");
            return null;
        }
        try {
            if (callBackParams.tags != null && callBackParams.tags.size() > 0) {
                return (String) callBackParams.tags.toArray()[0];
            }
        } catch (Throwable th) {
            Logger.d("TagAliasOperator", "get origin check tag failed, error:" + th);
        }
        return null;
    }

    private void a(Context context, int i, long j) {
        Logger.v("TagAliasOperator", i == JPushInterface.ErrorCode.TIMEOUT ? "action - onTimeout rid:" + j : "action - on send data over limit");
        CallBackParams callBackParamsA = a(j);
        if (callBackParamsA == null) {
            Logger.w("TagAliasOperator", "tagalias callback is null; rid=" + j);
        } else {
            b(j);
            a(context, callBackParamsA, i, false);
        }
    }

    private void a(Context context, int i, long j, Intent intent) {
        String str;
        Logger.v("TagAliasOperator", "SetAliasAndTags finish : errorCode:" + i + " rid:" + j);
        CallBackParams callBackParamsA = a(j);
        if (callBackParamsA == null) {
            Logger.w("TagAliasOperator", "tagalias callback is null; rid=" + j);
            return;
        }
        a().b(j);
        boolean booleanExtra = false;
        if (intent != null) {
            try {
                if (callBackParamsA.action == 5) {
                    if (callBackParamsA.protoType == 1) {
                        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("tags");
                        if (stringArrayListExtra != null) {
                            callBackParamsA.tags = new HashSet(stringArrayListExtra);
                            str = "all tags was loaded, value:" + callBackParamsA.tags;
                            Logger.d("TagAliasOperator", str);
                        }
                    } else if (callBackParamsA.protoType == 2) {
                        callBackParamsA.alias = intent.getStringExtra("alias");
                        str = "alias was loaded, value:" + callBackParamsA.tags;
                        Logger.d("TagAliasOperator", str);
                    }
                } else if (callBackParamsA.action == 6) {
                    booleanExtra = intent.getBooleanExtra("validated", false);
                }
            } catch (Throwable th) {
                Logger.d("TagAliasOperator", "get tag or alias failed - error:" + th);
            }
        }
        a(context, callBackParamsA, i, booleanExtra);
    }

    private void a(Context context, CallBackParams callBackParams, int i, boolean z) {
        Logger.d("TagAliasOperator", "action - invokeUserCallback, errorCode:" + i + ",callBack:" + callBackParams);
        if (callBackParams.protoType != 0) {
            Logger.w("TagAliasOperator", "new proto type do not call user callback");
        } else if (callBackParams.tagAliasCallBack != null) {
            callBackParams.tagAliasCallBack.gotResult(i, callBackParams.alias, callBackParams.tags);
        }
    }

    private void c(Context context) {
        ConcurrentHashMap<Long, CallBackParams> concurrentHashMap = this.c;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        ArrayList<Long> arrayList = new ArrayList();
        for (Map.Entry<Long, CallBackParams> entry : this.c.entrySet()) {
            if (entry.getValue().isTimeOut(20000L)) {
                arrayList.add(entry.getKey());
            }
        }
        for (Long l : arrayList) {
            Logger.w("TagAliasOperator", "cleanTimeOutCallback timeout rid:" + l);
            a(context, JPushInterface.ErrorCode.TIMEOUT, l.longValue());
        }
    }

    public CallBackParams a(long j) {
        return this.c.get(Long.valueOf(j));
    }

    public JPushMessage a(Context context, Intent intent) {
        String str;
        long longExtra = intent.getLongExtra(TagAliasReceiver.KEY_TAGALIASOPERATOR_SEQID, -1L);
        boolean booleanExtra = false;
        int intExtra = intent.getIntExtra(TagAliasReceiver.KEY_TAGALIASOPERATOR_CALLBACKCODE, 0);
        Logger.v("TagAliasOperator", "parseTagAliasResponse2JPushMessage, errorCode:" + intExtra + " rid:" + longExtra);
        CallBackParams callBackParamsA = a(longExtra);
        if (callBackParamsA == null) {
            Logger.w("TagAliasOperator", "tagalias callback is null; rid=" + longExtra);
            return null;
        }
        a().b(longExtra);
        if (intExtra == 0) {
            try {
                if (callBackParamsA.action == 5) {
                    if (callBackParamsA.protoType == 1) {
                        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("tags");
                        if (stringArrayListExtra != null) {
                            callBackParamsA.tags = new HashSet(stringArrayListExtra);
                            str = "all tags was loaded, value:" + callBackParamsA.tags;
                            Logger.d("TagAliasOperator", str);
                        }
                    } else if (callBackParamsA.protoType == 2) {
                        callBackParamsA.alias = intent.getStringExtra("alias");
                        str = "alias was loaded, value:" + callBackParamsA.tags;
                        Logger.d("TagAliasOperator", str);
                    }
                } else if (callBackParamsA.action == 6) {
                    booleanExtra = intent.getBooleanExtra("validated", false);
                }
            } catch (Throwable th) {
                Logger.d("TagAliasOperator", "get tag or alias failed - error:" + th);
            }
        }
        JPushMessage jPushMessage = new JPushMessage();
        jPushMessage.setErrorCode(intExtra);
        jPushMessage.setSequence(callBackParamsA.sequence);
        if (callBackParamsA.protoType != 1) {
            jPushMessage.setAlias(callBackParamsA.alias);
        } else if (callBackParamsA.action == 6) {
            jPushMessage.setCheckTag(a(callBackParamsA));
            jPushMessage.setTagCheckStateResult(booleanExtra);
            jPushMessage.setTagCheckOperator(true);
        } else {
            jPushMessage.setTags(callBackParamsA.tags);
        }
        return jPushMessage;
    }

    public synchronized void a(Context context) {
        try {
            if (this.e.get()) {
                Logger.d("TagAliasOperator", "tag alias callback register is called");
            } else {
                try {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addCategory(context.getPackageName());
                    intentFilter.addAction(TagAliasReceiver.ACTION_TAG_ALIAS_TIMEOUT);
                    intentFilter.addAction(TagAliasReceiver.ACTION_TAG_ALIAS_CALLBACK);
                    if (this.b == null) {
                        this.b = new TagAliasReceiver();
                    }
                    context.registerReceiver(this.b, intentFilter);
                    this.e.set(true);
                } catch (Exception e) {
                    Logger.e("TagAliasOperator", "setTagAndAlias e:" + e.getMessage());
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public void a(Context context, long j, int i, Intent intent) {
        Logger.v("TagAliasOperator", "action - onTagAliasResponse rid:" + j + " tagAliasCallbacks :" + a().b());
        if (TagAliasReceiver.ACTION_TAG_ALIAS_TIMEOUT.equals(intent.getAction())) {
            a(context, i, j);
        } else {
            a(context, i, j, intent);
        }
        b(context);
    }

    public void a(Context context, Long l, CallBackParams callBackParams) {
        c(context);
        this.c.put(l, callBackParams);
    }

    public ConcurrentHashMap<Long, CallBackParams> b() {
        return this.c;
    }

    public void b(long j) {
        this.c.remove(Long.valueOf(j));
    }

    public synchronized void b(Context context) {
        String str;
        String str2;
        ConcurrentHashMap<Long, CallBackParams> concurrentHashMap;
        String str3;
        String str4;
        c(context);
        if (this.e.get() && (concurrentHashMap = this.c) != null && concurrentHashMap.isEmpty()) {
            try {
                TagAliasReceiver tagAliasReceiver = this.b;
                if (tagAliasReceiver != null) {
                    context.unregisterReceiver(tagAliasReceiver);
                    this.b = null;
                }
            } catch (IllegalArgumentException e) {
                e = e;
                str3 = "TagAliasOperator";
                str4 = "Receiver not registered, cannot call unregisterReceiver";
                Logger.ww(str3, str4, e);
            } catch (Exception e2) {
                e = e2;
                str3 = "TagAliasOperator";
                str4 = "other exception";
                Logger.ww(str3, str4, e);
            }
            this.e.set(false);
            str = "TagAliasOperator";
            str2 = "unRegister tag alias callback";
        } else {
            str = "TagAliasOperator";
            str2 = "tagAliasCallbacks is not empty";
        }
        Logger.v(str, str2);
    }
}
