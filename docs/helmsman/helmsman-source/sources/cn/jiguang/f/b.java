package cn.jiguang.f;

import android.content.Context;
import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
final class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;

    b(Context context, String str) {
        this.a = context;
        this.b = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            Toast.makeText(this.a, this.b, 0).show();
        } catch (Throwable unused) {
        }
    }
}
