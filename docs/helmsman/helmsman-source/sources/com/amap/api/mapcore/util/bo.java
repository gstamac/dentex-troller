package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: CanDeleteState.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class bo extends bp {
    public bo(int i, al alVar) {
        super(i, alVar);
    }

    @Override // com.amap.api.mapcore.util.bp
    public void a() {
        b(this.b.a);
        this.b.setCompleteCode(0);
        this.b.a(this.b.a);
        this.b.d();
    }
}
