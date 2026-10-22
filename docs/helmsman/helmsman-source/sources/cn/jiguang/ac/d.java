package cn.jiguang.ac;

import android.content.Context;
import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
final class d implements Runnable {
    final /* synthetic */ c a;
    private String b;
    private Bundle c;
    private Context d;

    public d(c cVar, Context context, String str, Bundle bundle) {
        this.a = cVar;
        this.b = str;
        this.c = bundle;
        this.d = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            cn.jiguang.af.a.a(this.d, this.b, this.c);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
