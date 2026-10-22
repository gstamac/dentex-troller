package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.maps.AMapException;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* JADX INFO: compiled from: StatisticsEntity.java */
/* JADX INFO: loaded from: classes.dex */
public class hi {
    private Context a;
    private String b;
    private String c;
    private String d;
    private String e;

    public byte[] a(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public hi(Context context, String str, String str2, String str3) throws ev {
        if (TextUtils.isEmpty(str3) || str3.length() > 256) {
            throw new ev(AMapException.ERROR_INVALID_PARAMETER);
        }
        this.a = context.getApplicationContext();
        this.c = str;
        this.d = str2;
        this.b = str3;
    }

    public void a(String str) throws ev {
        if (TextUtils.isEmpty(str) || str.length() > 65536) {
            throw new ev(AMapException.ERROR_INVALID_PARAMETER);
        }
        this.e = str;
    }

    public byte[] b(String str) {
        if (TextUtils.isEmpty(str)) {
            return new byte[]{0, 0};
        }
        int length = str.length();
        return new byte[]{(byte) (length / 256), (byte) (length % 256)};
    }

    public byte[] a() {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        byte[] byteArray = new byte[0];
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    fg.a(byteArrayOutputStream, this.c);
                    fg.a(byteArrayOutputStream, this.d);
                    fg.a(byteArrayOutputStream, this.b);
                    fg.a(byteArrayOutputStream, String.valueOf(fa.m(this.a)));
                    new SimpleDateFormat("SSS").format(new Date());
                    byteArrayOutputStream.write(a(Calendar.getInstance().get(14)));
                    byteArrayOutputStream.write(b(this.e));
                    byteArrayOutputStream.write(fg.a(this.e));
                    byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        fk.a(th, "StatisticsEntity", "toDatas");
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        return byteArray;
                    } catch (Throwable th3) {
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (Throwable th4) {
                                th4.printStackTrace();
                            }
                        }
                        throw th3;
                    }
                }
            } catch (Throwable th5) {
                byteArrayOutputStream = null;
                th = th5;
            }
        } catch (Throwable th6) {
            th6.printStackTrace();
        }
        return byteArray;
    }
}
