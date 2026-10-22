package cn.jiguang.c;

import android.content.Context;
import android.content.Intent;
import cn.jiguang.ad.d;
import cn.jiguang.internal.ActionManager;

/* JADX INFO: loaded from: classes.dex */
final class c implements Runnable {
    final /* synthetic */ b a;
    private Context b;
    private Intent c;
    private String d;

    public c(b bVar, Context context, Intent intent, String str) {
        this.a = bVar;
        this.b = context;
        this.d = str;
        this.c = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            ActionManager.getInstance().handleMessage(this.b, this.d, this.c);
            b.a(this.a, this.b, this.c);
        } catch (Throwable th) {
            d.h("JCoreHelper", "[PushReceiverAction failed:]" + th.getMessage());
        }
    }
}
