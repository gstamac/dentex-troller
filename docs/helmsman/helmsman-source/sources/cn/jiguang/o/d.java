package cn.jiguang.o;

import android.text.TextUtils;
import cn.jiguang.f.i;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    private static long a = 1;

    public static long a() {
        long j = a + 1;
        a = j;
        if (j >= 2147483647L) {
            a = 1L;
        }
        return a;
    }

    public static byte[] a(long j, String str) {
        cn.jiguang.p.a aVar = new cn.jiguang.p.a(20480);
        aVar.a(0);
        aVar.a(j);
        if (str != null && !TextUtils.isEmpty(str)) {
            byte[] bArrI = i.i(str);
            aVar.a(bArrI.length);
            aVar.a(bArrI, 0, bArrI.length);
        }
        return aVar.a();
    }
}
