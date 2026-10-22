package cn.jiguang.u;

import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
final class b implements Callable<cn.jiguang.v.a> {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public cn.jiguang.v.a call() {
        try {
            d dVar = new d(this.a.a);
            dVar.b();
            for (int i = 0; i < 20; i++) {
                cn.jiguang.v.a aVarA = dVar.a();
                if (aVarA != null) {
                    return aVarA;
                }
                Thread.sleep(2000L);
            }
            return null;
        } catch (Throwable th) {
            cn.jiguang.w.a.d("JLocation", "JLocationCellInfo call failed:" + th.getMessage());
            return null;
        }
    }
}
