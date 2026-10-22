package cn.jiguang.aa;

import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    public String a;
    public String b;
    public int c;
    public String d;
    public Intent e;
    public int f;

    public b() {
    }

    public b(String str, String str2, int i) {
        this.a = str;
        this.b = str2;
        this.c = i;
    }

    public final String toString() {
        return "JWakeTargetInfo{packageName='" + this.a + "', serviceName='" + this.b + "', targetVersion=" + this.c + ", providerAuthority='" + this.d + "', dActivityIntent=" + this.e + ", cmd=" + this.f + '}';
    }
}
