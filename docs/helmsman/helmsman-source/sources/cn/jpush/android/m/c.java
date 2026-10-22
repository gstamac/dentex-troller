package cn.jpush.android.m;

/* JADX INFO: loaded from: classes.dex */
public class c {
    private long a = 0;
    private int b = 0;
    private int c = 0;
    private int d = 0;
    private String e = "";
    private long f = 0;
    private long g = 0;

    public long a() {
        return this.a;
    }

    public void a(int i) {
        this.b = i;
    }

    public void a(long j) {
        this.a = j;
    }

    public void a(String str) {
        this.e = str;
    }

    public int b() {
        return this.b;
    }

    public void b(int i) {
        this.c = i;
    }

    public void b(long j) {
        this.g = j;
    }

    public int c() {
        return this.c;
    }

    public void c(int i) {
        this.d = i;
    }

    public void c(long j) {
        this.f = j;
    }

    public String d() {
        return this.e;
    }

    public long e() {
        return this.g;
    }

    public long f() {
        return this.f;
    }

    public String toString() {
        return "LocalNotificationDBData [ln_id=" + this.a + ", ln_count=" + this.b + ", ln_remove=" + this.c + ", ln_type=" + this.d + ", ln_extra=" + this.e + ", ln_trigger_time=" + this.f + ", ln_add_time=" + this.g + "]";
    }
}
