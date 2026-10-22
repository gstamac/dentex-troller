package cn.jpush.android.e;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public int a;
    public String b;
    public String c;

    public b(int i, String str, String str2) {
        this.a = i;
        this.b = str;
        this.c = str2;
    }

    public String a() {
        return this.b;
    }

    public String toString() {
        return "TokenResult{code=" + this.a + ", token='" + this.b + "', msg='" + this.c + "'}";
    }
}
