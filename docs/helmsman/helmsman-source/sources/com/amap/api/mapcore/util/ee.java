package com.amap.api.mapcore.util;

import com.amap.api.mapcore.util.ed;

/* JADX INFO: compiled from: Pool.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class ee<T extends ed<?>> {
    protected T a;

    protected boolean a(T t) {
        return true;
    }

    public T b(T t) {
        if (t == null) {
            return null;
        }
        while (t != null) {
            Object obj = t.f;
            a(t);
            t.f = this.a;
            this.a = t;
            t = (T) obj;
        }
        return null;
    }
}
