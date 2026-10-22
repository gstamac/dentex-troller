package cn.jiguang.internal;

import android.content.Context;
import android.os.Bundle;
import cn.jiguang.ad.d;

/* JADX INFO: loaded from: classes.dex */
final class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ Bundle d;
    final /* synthetic */ JCoreInternalHelper e;

    b(JCoreInternalHelper jCoreInternalHelper, Context context, String str, String str2, Bundle bundle) {
        this.e = jCoreInternalHelper;
        this.a = context;
        this.b = str;
        this.c = str2;
        this.d = bundle;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.e.initLoad(this.a);
            JCoreInternalHelper.jCoreHelperAction.directHandle(this.a, this.b, this.c, this.d);
        } catch (Throwable th) {
            d.f("JCoreInternalHelper", "directHandle e:" + th);
        }
    }
}
