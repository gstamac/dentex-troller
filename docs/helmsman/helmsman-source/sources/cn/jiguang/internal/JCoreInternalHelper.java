package cn.jiguang.internal;

import android.content.Context;
import android.os.Bundle;
import cn.jiguang.ad.d;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class JCoreInternalHelper {
    private static final String TAG = "JCoreInternalHelper";
    private static JCoreHelperAction jCoreHelperAction;
    private static final Object locker = new Object();
    private static volatile JCoreInternalHelper mJCoreBridge;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private JCoreInternalHelper() {
    }

    public static JCoreInternalHelper getInstance() {
        if (mJCoreBridge == null) {
            synchronized (locker) {
                if (mJCoreBridge == null) {
                    mJCoreBridge = new JCoreInternalHelper();
                }
            }
        }
        return mJCoreBridge;
    }

    public Object commonMethod(Context context, String str, Object obj) {
        JCoreHelperAction jCoreHelperAction2 = jCoreHelperAction;
        if (jCoreHelperAction2 != null) {
            return jCoreHelperAction2.onCommonMethod(context, str, obj);
        }
        this.executor.execute(new c(this, context, str, obj));
        return null;
    }

    public Bundle directHandle(Context context, String str, String str2, Bundle bundle) {
        JCoreHelperAction jCoreHelperAction2 = jCoreHelperAction;
        if (jCoreHelperAction2 != null) {
            return jCoreHelperAction2.directHandle(context, str, str2, bundle);
        }
        this.executor.execute(new b(this, context, str, str2, bundle));
        return new Bundle();
    }

    public synchronized void initLoad(Context context) {
        JCoreHelperAction jCoreHelperAction2 = jCoreHelperAction;
        if (jCoreHelperAction2 != null) {
            return;
        }
        if (context == null) {
            return;
        }
        if (jCoreHelperAction2 == null) {
            d.b(TAG, "load action by local");
            jCoreHelperAction = new cn.jiguang.c.b();
        }
    }

    public Object onEvent(Context context, String str, int i, boolean z, String str2, Bundle bundle, Object... objArr) {
        JCoreHelperAction jCoreHelperAction2 = jCoreHelperAction;
        if (jCoreHelperAction2 != null) {
            return jCoreHelperAction2.onEvent(context, str, i, str2, bundle, objArr);
        }
        if (!z) {
            return new cn.jiguang.c.b().onEvent(context, str, i, str2, bundle, objArr);
        }
        this.executor.execute(new a(this, context, str, i, str2, bundle, objArr));
        return null;
    }
}
