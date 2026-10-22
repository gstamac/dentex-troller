package cn.jiguang.dy;

import android.content.Context;
import cn.jiguang.ad.d;

/* JADX INFO: loaded from: classes.dex */
public class Protocol {
    private static final String TAG = "Protocol";
    public static Context mContext = null;
    private static Protocol p = new Protocol();
    public static final String soName = "jcore230";

    static {
        try {
            System.loadLibrary("jcore230");
        } catch (Throwable th) {
            d.h("PushProtocol", "System.loadLibrary::jcore230" + th);
        }
    }

    public static Protocol bf() {
        return p;
    }

    public static Object d(int i, int i2, Object obj) {
        return null;
    }

    public static boolean e(int i) {
        return bf().c(i);
    }

    public native boolean c(int i);
}
