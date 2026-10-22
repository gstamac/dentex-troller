package cn.jiguang.internal;

import android.content.Context;
import android.os.Bundle;
import cn.jiguang.ad.d;

/* JADX INFO: loaded from: classes.dex */
final class a implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ int c;
    final /* synthetic */ String d;
    final /* synthetic */ Bundle e;
    final /* synthetic */ Object[] f;
    final /* synthetic */ JCoreInternalHelper g;

    a(JCoreInternalHelper jCoreInternalHelper, Context context, String str, int i, String str2, Bundle bundle, Object[] objArr) {
        this.g = jCoreInternalHelper;
        this.a = context;
        this.b = str;
        this.c = i;
        this.d = str2;
        this.e = bundle;
        this.f = objArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            this.g.initLoad(this.a);
            JCoreInternalHelper.jCoreHelperAction.onEvent(this.a, this.b, this.c, this.d, this.e, this.f);
        } catch (Throwable th) {
            d.f("JCoreInternalHelper", "onEvent e:" + th);
        }
    }
}
