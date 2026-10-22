package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.facebook.appevents.AppEventsConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

/* JADX INFO: compiled from: ClientInfo.java */
/* JADX INFO: loaded from: classes.dex */
public class ez {
    public static String a(Context context, String str, String str2) {
        try {
            return fc.b(ew.e(context) + ":" + str.substring(0, str.length() - 3) + ":" + str2);
        } catch (Throwable th) {
            fk.a(th, "CInfo", "Scode");
            return null;
        }
    }

    public static String a() {
        try {
            String strValueOf = String.valueOf(System.currentTimeMillis());
            String str = ew.a() ? "1" : AppEventsConstants.EVENT_PARAM_VALUE_NO;
            int length = strValueOf.length();
            return strValueOf.substring(0, length - 2) + str + strValueOf.substring(length - 1);
        } catch (Throwable th) {
            fk.a(th, "CInfo", "getTS");
            return null;
        }
    }

    public static String a(Context context) {
        try {
            a aVar = new a();
            aVar.d = ew.c(context);
            aVar.i = ew.d(context);
            return a(context, aVar);
        } catch (Throwable th) {
            fk.a(th, "CInfo", "InitXInfo");
            return null;
        }
    }

    public static byte[] a(Context context, byte[] bArr) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, CertificateException, NullPointerException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        if (keyGenerator == null) {
            return null;
        }
        keyGenerator.init(256);
        byte[] encoded = keyGenerator.generateKey().getEncoded();
        PublicKey publicKeyB = fg.b(context);
        if (publicKeyB == null) {
            return null;
        }
        byte[] bArrA = fb.a(encoded, publicKeyB);
        byte[] bArrA2 = fb.a(encoded, bArr);
        byte[] bArr2 = new byte[bArrA.length + bArrA2.length];
        System.arraycopy(bArrA, 0, bArr2, 0, bArrA.length);
        System.arraycopy(bArrA2, 0, bArr2, bArrA.length, bArrA2.length);
        return bArr2;
    }

    public static byte[] a(Context context, boolean z) {
        try {
            return b(context, b(context, z));
        } catch (Throwable th) {
            fk.a(th, "CInfo", "getGZipXInfo");
            return null;
        }
    }

    public static String b(Context context) {
        try {
            return a(context, b(context, false));
        } catch (Throwable th) {
            fk.a(th, "CInfo", "getClientXInfo");
            return null;
        }
    }

    @Deprecated
    public static String a(Context context, ff ffVar, Map<String, String> map, boolean z) {
        try {
            return a(context, b(context, z));
        } catch (Throwable th) {
            fk.a(th, "CInfo", "rsaLocClineInfo");
            return null;
        }
    }

    public static String b(Context context, byte[] bArr) {
        try {
            return d(context, bArr);
        } catch (Throwable th) {
            fk.a(th, "CInfo", "AESData");
            return "";
        }
    }

    public static byte[] c(Context context, byte[] bArr) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, CertificateException, NullPointerException {
        PublicKey publicKeyB = fg.b(context);
        if (bArr.length > 117) {
            byte[] bArr2 = new byte[117];
            System.arraycopy(bArr, 0, bArr2, 0, 117);
            byte[] bArrA = fb.a(bArr2, publicKeyB);
            byte[] bArr3 = new byte[(bArr.length + 128) - 117];
            System.arraycopy(bArrA, 0, bArr3, 0, 128);
            System.arraycopy(bArr, 117, bArr3, 128, bArr.length - 117);
            return bArr3;
        }
        return fb.a(bArr, publicKeyB);
    }

    private static String a(Context context, a aVar) {
        return fb.a(b(context, aVar));
    }

    private static byte[] b(Context context, a aVar) {
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                a(byteArrayOutputStream, aVar.a);
                a(byteArrayOutputStream, aVar.b);
                a(byteArrayOutputStream, aVar.c);
                a(byteArrayOutputStream, aVar.d);
                a(byteArrayOutputStream, aVar.e);
                a(byteArrayOutputStream, aVar.f);
                a(byteArrayOutputStream, aVar.g);
                a(byteArrayOutputStream, aVar.h);
                a(byteArrayOutputStream, aVar.i);
                a(byteArrayOutputStream, aVar.j);
                a(byteArrayOutputStream, aVar.k);
                a(byteArrayOutputStream, aVar.l);
                a(byteArrayOutputStream, aVar.m);
                a(byteArrayOutputStream, aVar.n);
                a(byteArrayOutputStream, aVar.o);
                a(byteArrayOutputStream, aVar.p);
                a(byteArrayOutputStream, aVar.q);
                a(byteArrayOutputStream, aVar.r);
                a(byteArrayOutputStream, aVar.s);
                a(byteArrayOutputStream, aVar.t);
                a(byteArrayOutputStream, aVar.u);
                a(byteArrayOutputStream, aVar.v);
                a(byteArrayOutputStream, aVar.w);
                a(byteArrayOutputStream, aVar.x);
                a(byteArrayOutputStream, aVar.y);
                byte[] bArrA = a(context, byteArrayOutputStream);
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                return bArrA;
            } catch (Throwable th2) {
                th = th2;
                try {
                    fk.a(th, "CInfo", "InitXInfo");
                    return null;
                } finally {
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Throwable th3) {
                            th3.printStackTrace();
                        }
                    }
                }
            }
        } catch (Throwable th4) {
            th = th4;
            byteArrayOutputStream = null;
        }
    }

    private static byte[] a(Context context, ByteArrayOutputStream byteArrayOutputStream) throws BadPaddingException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, CertificateException {
        return c(context, fg.b(byteArrayOutputStream.toByteArray()));
    }

    static String d(Context context, byte[] bArr) throws BadPaddingException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, IOException, CertificateException {
        byte[] bArrB = fg.b(a(context, bArr));
        return bArrB != null ? fb.a(bArrB) : "";
    }

    public static void a(ByteArrayOutputStream byteArrayOutputStream, String str) {
        if (!TextUtils.isEmpty(str)) {
            fg.a(byteArrayOutputStream, str.getBytes().length > 255 ? (byte) -1 : (byte) str.getBytes().length, fg.a(str));
        } else {
            fg.a(byteArrayOutputStream, (byte) 0, new byte[0]);
        }
    }

    public static String e(Context context, byte[] bArr) {
        try {
            return d(context, bArr);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private static a b(Context context, boolean z) {
        a aVar = new a();
        aVar.a = fa.q(context);
        aVar.b = fa.i(context);
        String strF = fa.f(context);
        if (strF == null) {
            strF = "";
        }
        aVar.c = strF;
        aVar.d = ew.c(context);
        aVar.e = Build.MODEL;
        aVar.f = Build.MANUFACTURER;
        aVar.g = Build.DEVICE;
        aVar.h = ew.b(context);
        aVar.i = ew.d(context);
        aVar.j = String.valueOf(Build.VERSION.SDK_INT);
        aVar.k = fa.r(context);
        aVar.l = fa.p(context);
        aVar.m = fa.m(context) + "";
        aVar.n = fa.l(context) + "";
        aVar.o = fa.s(context);
        aVar.p = fa.k(context);
        if (z) {
            aVar.q = "";
        } else {
            aVar.q = fa.h(context);
        }
        if (z) {
            aVar.r = "";
        } else {
            aVar.r = fa.g(context);
        }
        if (z) {
            aVar.s = "";
            aVar.t = "";
        } else {
            String[] strArrJ = fa.j(context);
            aVar.s = strArrJ[0];
            aVar.t = strArrJ[1];
        }
        aVar.w = fa.a();
        return aVar;
    }

    /* JADX INFO: compiled from: ClientInfo.java */
    private static class a {
        String a;
        String b;
        String c;
        String d;
        String e;
        String f;
        String g;
        String h;
        String i;
        String j;
        String k;
        String l;
        String m;
        String n;
        String o;
        String p;
        String q;
        String r;
        String s;
        String t;
        String u;
        String v;
        String w;
        String x;
        String y;

        private a() {
        }
    }
}
