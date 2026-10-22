package cn.jiguang.aj;

/* JADX INFO: loaded from: classes.dex */
public final class f extends Exception {
    public final int a;

    public f(int i, String str) {
        super(str);
        this.a = i;
    }

    @Override // java.lang.Throwable
    public final String toString() {
        return "JException(" + this.a + "):" + getLocalizedMessage();
    }
}
