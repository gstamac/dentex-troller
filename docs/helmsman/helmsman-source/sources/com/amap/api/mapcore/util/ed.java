package com.amap.api.mapcore.util;

import com.amap.api.mapcore.util.ed;

/* JADX INFO: compiled from: Inlist.java */
/* JADX INFO: loaded from: classes.dex */
public class ed<T extends ed<T>> {
    public T f;

    public static <T extends ed<?>> T a(T t, T t2) {
        if (t2.f != null) {
            throw new IllegalArgumentException("'item' is a list");
        }
        t2.f = t;
        return t2;
    }
}
