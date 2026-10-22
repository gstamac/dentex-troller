package cn.jiguang.ad;

import android.util.Log;
import cn.jiguang.internal.JConstants;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static void a(String str, String str2, boolean z, int i, String str3) {
        String str4;
        try {
            if (JConstants.DEBUG_MODE || i >= 5) {
                if (JConstants.INTERNAL_USE || !z || i >= 6) {
                    if (i >= 2) {
                        if (i == 2) {
                            Log.v("JIGUANG-" + str, "[" + str2 + "] " + str3);
                            str4 = "TRACE";
                        } else if (i == 3) {
                            Log.d("JIGUANG-" + str, "[" + str2 + "] " + str3);
                            str4 = "DEBUG";
                        } else if (i == 4) {
                            Log.i("JIGUANG-" + str, "[" + str2 + "] " + str3);
                            str4 = "INFO";
                        } else {
                            if (i != 5) {
                                if (i != 6) {
                                    return;
                                }
                                Log.e("JIGUANG-" + str, "[" + str2 + "] " + str3);
                                b.a("ERROR", str2, str3, null);
                                return;
                            }
                            Log.w("JIGUANG-" + str, "[" + str2 + "] " + str3);
                            str4 = "WARN";
                        }
                        b.a(str4, str2, str3, null);
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }
}
