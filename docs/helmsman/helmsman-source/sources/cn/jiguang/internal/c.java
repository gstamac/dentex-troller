package cn.jiguang.internal;

import android.content.Context;
import cn.jiguang.ad.d;

/* JADX INFO: loaded from: classes.dex */
final class c implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ Object c;
    final /* synthetic */ JCoreInternalHelper d;

    c(JCoreInternalHelper jCoreInternalHelper, Context context, String str, Object obj) {
        this.d = jCoreInternalHelper;
        this.a = context;
        this.b = str;
        this.c = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.d.initLoad(this.a);
            JCoreInternalHelper.jCoreHelperAction.onCommonMethod(this.a, this.b, this.c);
        } catch (Throwable th) {
            d.f("JCoreInternalHelper", "commonMethod e:" + th);
        }
    }
}
