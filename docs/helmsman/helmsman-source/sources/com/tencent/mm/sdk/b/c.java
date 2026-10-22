package com.tencent.mm.sdk.b;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/* JADX INFO: loaded from: classes2.dex */
final class c implements b.a {
    private Handler handler = new Handler(Looper.getMainLooper());

    c() {
    }

    @Override // com.tencent.mm.sdk.b.b.a
    public final void f(String str, String str2) {
        if (b.level <= 2) {
            Log.i(str, str2);
        }
    }

    @Override // com.tencent.mm.sdk.b.b.a
    public final void g(String str, String str2) {
        if (b.level <= 1) {
            Log.d(str, str2);
        }
    }

    @Override // com.tencent.mm.sdk.b.b.a
    public final int getLogLevel() {
        return b.level;
    }

    @Override // com.tencent.mm.sdk.b.b.a
    public final void h(String str, String str2) {
        if (b.level <= 3) {
            Log.w(str, str2);
        }
    }

    @Override // com.tencent.mm.sdk.b.b.a
    public final void i(String str, String str2) {
        if (b.level <= 4) {
            Log.e(str, str2);
        }
    }
}
