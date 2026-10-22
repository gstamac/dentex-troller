package cn.jpush.android.helper;

/* JADX INFO: loaded from: classes.dex */
public class g {
    private static long a = 1;

    public static long a() {
        long j = a + 1;
        a = j;
        if (j >= 2147483647L) {
            a = 1L;
        }
        return a;
    }
}
