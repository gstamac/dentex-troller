package com.tencent.wxop.stat;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
final class o implements Runnable {
    final /* synthetic */ Context a;

    o(Context context) {
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        StatServiceImpl.flushDataToDB(this.a);
    }
}
