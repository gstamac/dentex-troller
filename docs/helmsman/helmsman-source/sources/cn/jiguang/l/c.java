package cn.jiguang.l;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Runnable {
    final /* synthetic */ a a;
    private Context b;
    private String c;

    c(a aVar, Context context, String str) {
        this.a = aVar;
        this.b = context;
        this.c = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            a.a(this.a, this.b, this.c);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
