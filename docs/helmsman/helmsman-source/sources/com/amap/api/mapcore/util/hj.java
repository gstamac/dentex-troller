package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import cn.jiguang.internal.JConstants;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/* JADX INFO: compiled from: StatisticsManager.java */
/* JADX INFO: loaded from: classes.dex */
public class hj {
    private static boolean a = true;

    public static synchronized void a(final hi hiVar, final Context context) {
        fn.c().submit(new Runnable() { // from class: com.amap.api.mapcore.util.hj.1
            @Override // java.lang.Runnable
            public void run() {
                hk.a(context, fl.g, 307200, hiVar.a());
            }
        });
    }

    private static byte[] b(Context context) {
        byte[] bArrC = c(context);
        byte[] bArrD = d(context);
        byte[] bArr = new byte[bArrC.length + bArrD.length];
        System.arraycopy(bArrC, 0, bArr, 0, bArrC.length);
        System.arraycopy(bArrD, 0, bArr, bArrC.length, bArrD.length);
        return a(context, bArr);
    }

    public static void a(Context context) {
        try {
            if (e(context)) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));
                stringBuffer.append(" ");
                stringBuffer.append(UUID.randomUUID().toString());
                stringBuffer.append(" ");
                if (stringBuffer.length() != 53) {
                    return;
                }
                byte[] bArrA = fg.a(stringBuffer.toString());
                byte[] bArrB = b(context);
                byte[] bArr = new byte[bArrA.length + bArrB.length];
                System.arraycopy(bArrA, 0, bArr, 0, bArrA.length);
                System.arraycopy(bArrB, 0, bArr, bArrA.length, bArrB.length);
                gw.a().b(new fm(fg.c(bArr), "2"));
            }
        } catch (Throwable th) {
            fk.a(th, "StatisticsManager", "updateStaticsData");
        }
    }

    private static byte[] a(Context context, byte[] bArr) {
        try {
            return ez.a(context, bArr);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private static byte[] c(Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byteArray = new byte[0];
        try {
            try {
                fg.a(byteArrayOutputStream, "1.2.13.6");
                fg.a(byteArrayOutputStream, "Android");
                fg.a(byteArrayOutputStream, fa.q(context));
                fg.a(byteArrayOutputStream, fa.i(context));
                fg.a(byteArrayOutputStream, fa.f(context));
                fg.a(byteArrayOutputStream, Build.MANUFACTURER);
                fg.a(byteArrayOutputStream, Build.MODEL);
                fg.a(byteArrayOutputStream, Build.DEVICE);
                fg.a(byteArrayOutputStream, fa.r(context));
                fg.a(byteArrayOutputStream, ew.c(context));
                fg.a(byteArrayOutputStream, ew.d(context));
                fg.a(byteArrayOutputStream, ew.f(context));
                byteArrayOutputStream.write(new byte[]{0});
                byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        } catch (Throwable th2) {
            try {
                fk.a(th2, "StatisticsManager", "getHeader");
                byteArrayOutputStream.close();
            } catch (Throwable th3) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th4) {
                    th4.printStackTrace();
                }
                throw th3;
            }
        }
        return byteArray;
    }

    private static byte[] d(Context context) {
        return hk.a(context, fl.g, 307200);
    }

    private static boolean e(Context context) {
        try {
            if (fa.m(context) != 1 || !a || hk.a(context, fl.g) < 30) {
                return false;
            }
            long jB = hk.b(context, "c.log");
            long time = new Date().getTime();
            if (time - jB < JConstants.HOUR) {
                return false;
            }
            hk.a(context, time, "c.log");
            a = false;
            return true;
        } catch (Throwable th) {
            fk.a(th, "StatisticsManager", "isUpdate");
        }
        return false;
    }
}
