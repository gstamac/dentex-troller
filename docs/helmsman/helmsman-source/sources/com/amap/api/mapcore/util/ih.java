package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.Map;

/* JADX INFO: compiled from: LocationRequest.java */
/* JADX INFO: loaded from: classes.dex */
public final class ih extends gx {
    Map<String, String> c;
    String d;
    byte[] e;
    byte[] i;
    boolean j;
    String k;
    Map<String, String> l;
    boolean m;

    public ih(Context context, ff ffVar) {
        super(context, ffVar);
        this.c = null;
        this.d = "";
        this.e = null;
        this.i = null;
        this.j = false;
        this.k = null;
        this.l = null;
        this.m = false;
    }

    @Override // com.amap.api.mapcore.util.hc
    public final Map<String, String> a() {
        return this.c;
    }

    public final void a(String str) {
        this.k = str;
    }

    public final void a(Map<String, String> map) {
        this.l = map;
    }

    @Override // com.amap.api.mapcore.util.gx, com.amap.api.mapcore.util.hc
    public final Map<String, String> b() {
        return this.l;
    }

    public final void b(String str) {
        this.d = str;
    }

    public final void b(Map<String, String> map) {
        this.c = map;
    }

    public final void b(byte[] bArr) {
        this.e = bArr;
    }

    @Override // com.amap.api.mapcore.util.hc
    public final String c() {
        return this.d;
    }

    public final void d() {
        this.j = true;
    }

    @Override // com.amap.api.mapcore.util.gx
    public final byte[] e() {
        return this.e;
    }

    @Override // com.amap.api.mapcore.util.gx
    public final byte[] f() {
        return this.i;
    }

    @Override // com.amap.api.mapcore.util.gx
    public final boolean i() {
        return this.j;
    }

    @Override // com.amap.api.mapcore.util.gx
    public final String k() {
        return this.k;
    }

    @Override // com.amap.api.mapcore.util.gx
    protected final boolean l() {
        return this.m;
    }

    public final void o() {
        this.m = true;
    }
}
