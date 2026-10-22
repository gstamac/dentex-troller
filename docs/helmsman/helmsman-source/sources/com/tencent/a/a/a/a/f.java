package com.tencent.a.a.a.a;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
public abstract class f {
    protected Context a;

    protected f(Context context) {
        this.a = context;
    }

    public final void a(c cVar) {
        if (cVar == null) {
            return;
        }
        String string = cVar.toString();
        if (a()) {
            a(h.g(string));
        }
    }

    protected abstract void a(String str);

    protected abstract boolean a();

    protected abstract String b();

    public final c o() {
        String strF = a() ? h.f(b()) : null;
        if (strF != null) {
            return c.e(strF);
        }
        return null;
    }
}
