package cn.jiguang.u;

import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
final class c implements Callable<cn.jiguang.v.b> {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public cn.jiguang.v.b call() {
        try {
            f fVarA = f.a(this.a.a);
            fVarA.c();
            for (int i = 0; i < 30; i++) {
                if (fVarA.b()) {
                    return fVarA.a();
                }
                Thread.sleep(1000L);
            }
            return null;
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JLocation", "JLocationGpsInfo call failed:" + th.getMessage());
            return null;
        }
    }
}
