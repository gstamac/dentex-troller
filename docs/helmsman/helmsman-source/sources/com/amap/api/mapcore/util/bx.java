package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: WaitingState.java */
/* JADX INFO: loaded from: classes.dex */
public class bx extends bo {
    public bx(int i, al alVar) {
        super(i, alVar);
    }

    @Override // com.amap.api.mapcore.util.bp
    public void c() {
        this.b.l();
        this.b.d();
    }

    @Override // com.amap.api.mapcore.util.bp
    public void d() {
        b(this.b.c);
        this.b.a(this.b.c);
        this.b.c().c();
    }

    @Override // com.amap.api.mapcore.util.bp
    public void f() {
        b(this.b.d);
        this.b.a(this.b.d);
        this.b.c().c();
    }

    @Override // com.amap.api.mapcore.util.bp
    public void a(int i) {
        b(this.b.b(i));
        this.b.a(this.b.b(i));
        this.b.c().c();
    }
}
