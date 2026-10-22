package cn.jiguang.al;

import android.content.Context;
import cn.jiguang.internal.JConstants;
import com.qiniu.android.common.Constants;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import kotlin.jvm.internal.ByteCompanionObject;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class b {
    private static Integer a;

    private static int a() {
        Integer num = a;
        if (num != null) {
            return num.intValue();
        }
        Integer numValueOf = Integer.valueOf(Math.abs(new SecureRandom().nextInt()));
        a = numValueOf;
        return numValueOf.intValue();
    }

    public static String a(Context context) {
        long jD = cn.jiguang.d.a.d(context);
        return jD > 0 ? cn.jiguang.ar.d.a(jD) : cn.jiguang.ar.d.a(a());
    }

    public static String a(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[byteBuffer.getShort()];
        byteBuffer.get(bArr);
        try {
            return new String(bArr, "UTF-8");
        } catch (Throwable unused) {
            return null;
        }
    }

    public static byte[] a(int i, byte b, long j, String str) {
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        bVar.b(0);
        bVar.a(0);
        bVar.b(j);
        bVar.a(str);
        return bVar.b();
    }

    public static byte[] a(long j, int i, long j2, short s) {
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        bVar.b(0);
        bVar.a(4);
        bVar.a(2);
        bVar.b(j);
        bVar.a(i);
        bVar.b(j2);
        bVar.a(1);
        bVar.b(bVar.a(), 0);
        return bVar.b();
    }

    public static byte[] a(long j, long j2, String str, String str2, String str3, long j3, byte b, int i, String str4, String str5, String str6, String str7) {
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        bVar.b(0);
        bVar.a(23);
        bVar.a(1);
        bVar.b(j);
        bVar.a(0L);
        bVar.b(j2);
        bVar.a(97);
        bVar.a(0);
        bVar.b(0);
        bVar.a(str);
        bVar.a(str3);
        bVar.a(str2);
        bVar.a(0);
        bVar.a(j3);
        bVar.a((int) b);
        bVar.a(i);
        bVar.a(str4);
        bVar.a(str5);
        bVar.a(str6);
        bVar.a(str7);
        bVar.b(bVar.a(), 0);
        return bVar.b();
    }

    public static byte[] a(long j, String str, String str2, String str3, String str4, long j2, String str5) {
        int iA = a();
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        bVar.b(0);
        bVar.a(24);
        bVar.a(0);
        bVar.b(j);
        bVar.a(iA);
        bVar.b(0L);
        bVar.a(str);
        bVar.a(str2);
        bVar.a(str3);
        bVar.a(0);
        bVar.a(str4);
        bVar.a(j2);
        bVar.a(str5);
        bVar.b(bVar.a(), 0);
        return bVar.b();
    }

    public static byte[] a(long j, String str, long[] jArr) {
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("device_id", str);
            JSONArray jSONArray = new JSONArray();
            if (jArr != null) {
                jSONArray.put(j);
                for (long j2 : jArr) {
                    jSONArray.put(j2);
                }
            }
            jSONObject.put("uids", jSONArray);
            cn.jiguang.ad.d.b("CorePackage", "attach uids:" + jSONArray.toString());
            bVar.a(jSONObject.toString());
            return bVar.b();
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("CorePackage", "packageAttachInfo:" + th);
            return null;
        }
    }

    public static byte[] a(Context context, int i, int i2, long j, byte[] bArr, long j2) {
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        bVar.b(0);
        bVar.a(i2);
        bVar.a(i);
        bVar.b(j);
        bVar.a(JConstants.tcpSessionId);
        cn.jiguang.ad.d.b("CorePackage", "packageSendData uid:" + j2);
        if (j2 == 0) {
            j2 = cn.jiguang.d.a.d(context);
            cn.jiguang.ad.d.b("CorePackage", "use mine uid:" + j2);
        }
        bVar.b(j2);
        bVar.a(bArr);
        bVar.b(bVar.a(), 0);
        return a(context, bVar.b());
    }

    public static byte[] a(Context context, byte[] bArr) {
        byte[] bArrA;
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        int length = bArr.length - 24;
        byte[] bArr2 = new byte[24];
        byte[] bArr3 = new byte[length];
        System.arraycopy(bArr, 0, bArr2, 0, 24);
        System.arraycopy(bArr, 24, bArr3, 0, length);
        String strA = a(context);
        try {
            byte b = JConstants.tcpAlgorithm;
            cn.jiguang.ad.d.b("CorePackage", "encryptBuf algorithm=" + ((int) b) + ", key=" + strA);
            if (b == 2) {
                cn.jiguang.ar.c cVar = new cn.jiguang.ar.c();
                byte[] bArrA2 = cn.jiguang.ar.c.a(strA);
                String strSubstring = strA.substring(0, 16);
                cn.jiguang.ad.d.b("SM4", "seed=" + strA);
                bArrA = cVar.a(bArr3, bArrA2, strSubstring.getBytes(Constants.UTF_8));
            } else {
                bArrA = cn.jiguang.ar.d.a(bArr3, strA, strA.substring(0, 16), true);
            }
            int length2 = bArrA.length + 24;
            byte[] bArr4 = new byte[length2];
            System.arraycopy(bArr2, 0, bArr4, 0, 24);
            System.arraycopy(bArrA, 0, bArr4, 24, bArrA.length);
            byte b2 = (byte) ((length2 >>> 8) & 255);
            bArr4[0] = b2;
            bArr4[1] = (byte) (length2 & 255);
            bArr4[0] = (byte) (b2 | ByteCompanionObject.MIN_VALUE);
            bArr4[4] = b;
            return bArr4;
        } catch (Exception e) {
            cn.jiguang.ad.d.f("CorePackage", "e:" + e);
            cn.jiguang.ad.d.g("CorePackage", "encrpt data failed");
            return null;
        }
    }

    public static byte[] a(String str, long[] jArr) {
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("device_id", str);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i <= 0; i++) {
                jSONArray.put(jArr[0]);
            }
            jSONObject.put("uids", jSONArray);
            bVar.a(jSONObject.toString());
            return bVar.b();
        } catch (Throwable th) {
            cn.jiguang.ad.d.f("CorePackage", "packageDetachInfo:" + th);
            return null;
        }
    }

    public static byte[] a(short s, short s2, String str) {
        cn.jiguang.ao.b bVar = new cn.jiguang.ao.b(20480);
        bVar.a((int) s);
        bVar.a(1);
        bVar.a(str);
        return bVar.b();
    }
}
