package cn.jpush.android.l;

import cn.jpush.android.local.JPushConstants;

/* JADX INFO: loaded from: classes.dex */
public class a {
    public static int a = 358;
    public static String b = "3.5.8";

    public static boolean a() {
        try {
            return JPushConstants.THIRD_ENABLE;
        } catch (Throwable unused) {
            return true;
        }
    }
}
