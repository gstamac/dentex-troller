package com.tencent.wxop.stat;

/* JADX INFO: loaded from: classes2.dex */
class az implements Runnable {
    final /* synthetic */ f a;
    final /* synthetic */ au b;

    az(au auVar, f fVar) {
        this.b = auVar;
        this.a = fVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.b.b(this.a);
    }
}
