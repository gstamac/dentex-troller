package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: CityStateImp.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class bp {
    protected int a;
    protected al b;

    public abstract void c();

    public bp(int i, al alVar) {
        this.a = i;
        this.b = alVar;
    }

    public int b() {
        return this.a;
    }

    public boolean a(bp bpVar) {
        return bpVar.b() == b();
    }

    public void b(bp bpVar) {
        bj.a(b() + " ==> " + bpVar.b() + "   " + getClass() + "==>" + bpVar.getClass());
    }

    public void d() {
        bj.a("Wrong call start()  State: " + b() + "  " + getClass());
    }

    public void e() {
        bj.a("Wrong call continueDownload()  State: " + b() + "  " + getClass());
    }

    public void f() {
        bj.a("Wrong call pause()  State: " + b() + "  " + getClass());
    }

    public void a() {
        bj.a("Wrong call delete()  State: " + b() + "  " + getClass());
    }

    public void a(int i) {
        bj.a("Wrong call fail()  State: " + b() + "  " + getClass());
    }

    public void g() {
        bj.a("Wrong call hasNew()  State: " + b() + "  " + getClass());
    }

    public void h() {
        bj.a("Wrong call complete()  State: " + b() + "  " + getClass());
    }
}
