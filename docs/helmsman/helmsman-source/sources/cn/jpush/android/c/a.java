package cn.jpush.android.c;

import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private String a;
    private int b;
    private Bundle c;

    public a(String str, int i) {
        this.a = str;
        this.b = i;
    }

    public int a() {
        return this.b;
    }

    public void a(Bundle bundle) {
        this.c = bundle;
    }

    public String toString() {
        return "CmdMessage{cmd='" + this.a + "', errorCode=" + this.b + ", extra=" + this.c + '}';
    }
}
