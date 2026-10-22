package cn.jiguang.q;

import android.content.Context;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
final class b implements Callable<String> {
    private Context a;

    public b(Context context) {
        this.a = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public String call() {
        try {
            return a.g(this.a);
        } catch (Throwable th) {
            cn.jiguang.w.a.e("IdHelper", "[GoogleIdCall] failed:" + th.getMessage());
            return "";
        }
    }
}
