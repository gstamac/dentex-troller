package com.tencent.wxop.stat.common;

import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
class o {
    private static int a = -1;

    public static boolean a() {
        int i = a;
        if (i == 1) {
            return true;
        }
        if (i == 0) {
            return false;
        }
        String[] strArr = {"/bin", "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        for (int i2 = 0; i2 < 6; i2++) {
            try {
                if (new File(strArr[i2] + "su").exists()) {
                    a = 1;
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        a = 0;
        return false;
    }
}
