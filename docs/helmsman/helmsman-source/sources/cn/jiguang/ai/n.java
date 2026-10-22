package cn.jiguang.ai;

import android.util.Pair;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
final class n implements Callable<LinkedHashSet<cn.jiguang.ah.h>> {
    private String a;
    private m b;

    n(String str, m mVar) {
        this.a = str;
        this.b = mVar;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ LinkedHashSet<cn.jiguang.ah.h> call() {
        LinkedHashSet<cn.jiguang.ah.h> linkedHashSetA = m.a(this.a);
        if (linkedHashSetA != null && linkedHashSetA.size() > 0) {
            m.a(this.b, this.a, new Pair(linkedHashSetA, Long.valueOf(System.currentTimeMillis())));
        }
        return linkedHashSetA;
    }
}
