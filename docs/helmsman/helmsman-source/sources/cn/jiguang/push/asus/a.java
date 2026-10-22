package cn.jiguang.push.asus;

import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
final class a implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ Intent b;
    final /* synthetic */ PushMessageReceiver c;

    a(PushMessageReceiver pushMessageReceiver, Context context, Intent intent) {
        this.c = pushMessageReceiver;
        this.a = context;
        this.b = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.c.a(this.a, this.b);
    }
}
