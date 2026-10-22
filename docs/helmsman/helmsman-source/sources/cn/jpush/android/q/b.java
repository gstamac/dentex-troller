package cn.jpush.android.q;

import android.text.TextUtils;
import cn.jpush.android.helper.Logger;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static byte[] a(int i, byte b, long j) {
        d dVar = new d(128);
        dVar.b(i);
        dVar.a((int) b);
        dVar.a(j);
        return dVar.a();
    }

    public static byte[] a(String str) {
        d dVar = new d(128);
        dVar.a(7);
        dVar.a(1);
        dVar.a(str != null ? b(str) : new byte[0]);
        return dVar.a();
    }

    public static byte[] a(String str, byte b) {
        d dVar = new d(8192);
        dVar.a(TextUtils.isEmpty(str) ? new byte[0] : b(str));
        dVar.a((int) b);
        return dVar.a();
    }

    public static byte[] a(String str, int i, String str2) {
        d dVar = new d(8192);
        if (i == 10) {
            dVar.a(b(str));
        }
        dVar.a(b(str2));
        return dVar.a();
    }

    public static byte[] b(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Throwable th) {
            Logger.ee("PushPackage", "stringToUtf8Bytes error:" + th.getMessage());
            return str.getBytes();
        }
    }
}
