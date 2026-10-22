package com.tencent.wxop.stat.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes2.dex */
public class e {
    ExecutorService a;

    public e() {
        this.a = null;
        this.a = Executors.newSingleThreadExecutor();
    }

    public void a(Runnable runnable) {
        this.a.execute(runnable);
    }
}
