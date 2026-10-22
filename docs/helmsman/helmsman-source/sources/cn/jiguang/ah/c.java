package cn.jiguang.ah;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.core.view.ViewCompat;
import cn.jiguang.api.JCoreManager;
import cn.jiguang.internal.JConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Random;
import kotlin.UByte;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static int a(Context context, cn.jiguang.am.a aVar) {
        String str;
        String str2;
        String str3;
        long jD = cn.jiguang.d.a.d(context);
        String strJ = cn.jiguang.f.i.j(cn.jiguang.d.a.f(context));
        String str4 = strJ == null ? "" : strJ;
        String strA = cn.jiguang.ac.a.a(context);
        cn.jiguang.aj.b.a();
        String strE = cn.jiguang.aj.b.e();
        byte bA = cn.jiguang.aj.b.a(context);
        cn.jiguang.ad.d.e("ConnectingHelper", "Login with - juid:" + jD + ", appKey:" + strA + ", sdkVersion:" + strE + ", pluginPlatformType:" + ((int) bA));
        short sC = cn.jiguang.aj.b.c();
        int iA = cn.jiguang.f.j.a(context);
        String strA2 = cn.jiguang.ar.b.a(context);
        Object objA = cn.jiguang.aj.e.a(context, "get_loc_info", null);
        JSONObject jSONObject = new JSONObject();
        if (objA instanceof Bundle) {
            try {
                Bundle bundle = (Bundle) objA;
                str = str4;
                str2 = strA;
                try {
                    jSONObject.put("lat", bundle.getDouble("lat", 200.0d));
                    jSONObject.put("lng", bundle.getDouble("lot", 200.0d));
                    jSONObject.put("time", bundle.getLong("time"));
                } catch (Throwable unused) {
                }
            } catch (Throwable unused2) {
                str = str4;
                str2 = strA;
            }
        } else {
            str = str4;
            str2 = strA;
        }
        String string = jSONObject.length() > 0 ? jSONObject.toString() : "";
        String strL = JCoreManager.isInternal() ? JConstants.testCountry : null;
        if (TextUtils.isEmpty(strL)) {
            strL = cn.jiguang.f.a.l(context);
        }
        String strB = cn.jiguang.f.i.b(String.format(Locale.ENGLISH, strL, new Object[0]));
        String upperCase = strB == null ? "" : strB.toUpperCase();
        String strC = cn.jiguang.d.a.c(context);
        cn.jiguang.ad.d.b("ConnectingHelper", "login - juid:" + jD + ", flag:" + ((int) sC) + " netType:" + iA + " deviceId:" + strA2 + " locInfo:" + string + " countryCode:" + upperCase + " accountId:" + strC + ",sdkver:" + strE);
        byte[] bArrA = cn.jiguang.al.b.a(b(context), jD, str, str2, strE, sC, bA, iA, strA2, string, upperCase, strC);
        cn.jiguang.ad.d.d("ConnectingHelper", "pluginPlatformType:0b" + Integer.toBinaryString(bA & UByte.MAX_VALUE));
        byte[] bArrA2 = cn.jiguang.al.b.a(context, bArrA);
        if (bArrA2 == null || bArrA2.length <= 0 || aVar.a(bArrA2) != 0) {
            return -1;
        }
        try {
            Pair<cn.jiguang.al.c, ByteBuffer> pairA = cn.jiguang.al.a.a(context, aVar.a(20000).array(), "");
            if (pairA == null || pairA.first == null || pairA.second == null || ((cn.jiguang.al.c) pairA.first).c != 1) {
                str3 = "Login failed - can't parse a Login Response";
                cn.jiguang.ad.d.g("ConnectingHelper", str3);
                return -1;
            }
            cn.jiguang.al.d dVar = new cn.jiguang.al.d((cn.jiguang.al.c) pairA.first, (ByteBuffer) pairA.second);
            cn.jiguang.ad.d.b("ConnectingHelper", dVar.toString());
            int i = dVar.a;
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.f().a(Integer.valueOf(dVar.d))});
            if (i == 0) {
                JConstants.tcpSessionId = dVar.b;
                long j = ((long) dVar.c) * 1000;
                cn.jiguang.af.b.a(context, j);
                cn.jiguang.af.b.b(context, dVar.d);
                cn.jiguang.ad.d.e("ConnectingHelper", "Login succeed - sid:" + JConstants.tcpSessionId + ", serverTime;" + j);
            } else {
                cn.jiguang.ad.d.g("ConnectingHelper", "Login failed with server error - code:" + cn.jiguang.ao.c.a(i));
            }
            return i;
        } catch (cn.jiguang.aj.f e) {
            str3 = "Login failed - recv msg failed wit error:" + e;
        }
    }

    public static void a(Context context) {
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.J().a((String) null), cn.jiguang.g.a.K().a((String) null), cn.jiguang.g.a.d(false).a((String) null), cn.jiguang.g.a.d(true).a((String) null), cn.jiguang.g.a.O().a((String) null), cn.jiguang.g.a.L().a((Boolean) null), cn.jiguang.g.a.M().a((Long) null), cn.jiguang.g.a.b(true).a((String) null), cn.jiguang.g.a.b(false).a((String) null), cn.jiguang.g.a.c(true).a((String) null), cn.jiguang.g.a.c(false).a((String) null)});
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.c().a((Long) null), cn.jiguang.g.a.d().a((String) null), cn.jiguang.g.a.e().a((String) null)});
    }

    private static synchronized byte[] a(String str, int i, byte[] bArr, boolean z, int i2) {
        cn.jiguang.ao.b bVar;
        if (TextUtils.isEmpty(str) || str.length() != 2 || bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("flag or body length error");
        }
        bVar = new cn.jiguang.ao.b(300);
        bVar.b(0);
        bVar.a(str.getBytes());
        bVar.a(i);
        bVar.b(i2);
        bVar.a(bArr);
        bVar.b(bVar.a(), 0);
        bVar.a((int) ((byte) ((z ? 1 : 0) | 16)), 4);
        return bVar.b();
    }

    public static byte[] a(String str, String str2) throws Throwable {
        boolean z;
        byte[] bArrI = cn.jiguang.f.i.i(str2);
        try {
            byte[] bArrA = cn.jiguang.f.k.a(bArrI);
            if (bArrA.length < bArrI.length) {
                bArrI = bArrA;
                z = true;
            } else {
                z = false;
            }
        } catch (IOException unused) {
        }
        int length = bArrI.length;
        int iA = cn.jiguang.ar.d.a();
        String strA = cn.jiguang.ar.d.a(iA);
        return a(str, iA, cn.jiguang.ar.d.a(bArrI, strA, strA.substring(0, 16), true), z, length);
    }

    public static byte[] a(DatagramSocket datagramSocket, DatagramPacket datagramPacket) throws IOException {
        datagramSocket.setSoTimeout(6000);
        datagramSocket.send(datagramPacket);
        DatagramPacket datagramPacket2 = new DatagramPacket(new byte[1024], 1024);
        cn.jiguang.ad.d.c("ConnectingHelper", "udp Receiving...");
        datagramSocket.receive(datagramPacket2);
        int length = datagramPacket2.getLength();
        byte[] bArr = new byte[length];
        System.arraycopy(datagramPacket2.getData(), 0, bArr, 0, length);
        return bArr;
    }

    public static byte[] a(byte[] bArr) throws cn.jiguang.aj.f {
        if (bArr == null || bArr.length == 0) {
            throw new cn.jiguang.aj.f(4, "response is empty!");
        }
        try {
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            byteBufferWrap.getShort();
            byteBufferWrap.getShort();
            int i = byteBufferWrap.getInt();
            int i2 = i >>> 24;
            long j = i & ViewCompat.MEASURED_SIZE_MASK;
            byteBufferWrap.getShort();
            int iRemaining = byteBufferWrap.remaining();
            byte[] bArrA = new byte[iRemaining];
            byteBufferWrap.get(bArrA, 0, iRemaining);
            if (j != 0) {
                String strA = cn.jiguang.ar.d.a(j);
                try {
                    bArrA = cn.jiguang.ar.d.a(bArrA, strA, strA.substring(0, 16), false);
                    if (bArrA == null) {
                        throw new cn.jiguang.aj.f(5, "decrypt response error");
                    }
                } catch (Exception unused) {
                    throw new cn.jiguang.aj.f(5, "decrypt response error");
                }
            }
            if ((i2 & 1) != 1) {
                return bArrA;
            }
            try {
                return cn.jiguang.f.k.b(bArrA);
            } catch (IOException unused2) {
                return bArrA;
            }
        } catch (Throwable th) {
            throw new cn.jiguang.aj.f(4, "parse head error:" + th.getMessage());
        }
    }

    public static int b(Context context, cn.jiguang.am.a aVar) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6 = b(cn.jiguang.ao.d.a(context), " ") + "$$" + b(cn.jiguang.ao.a.a(context).o, " ") + "$$" + context.getPackageName() + "$$" + cn.jiguang.ac.a.a(context);
        String str7 = cn.jiguang.ao.a.a(context).a;
        cn.jiguang.aj.b.a();
        String strD = cn.jiguang.aj.b.d();
        cn.jiguang.ad.d.b("ConnectingHelper", "regVersion:" + strD);
        cn.jiguang.ao.a aVarA = cn.jiguang.ao.a.a(context);
        String str8 = b(aVarA.b, " ") + "$$" + b(aVarA.c, " ") + "$$" + b(aVarA.d, " ") + "$$" + b(aVarA.e, " ") + "$$" + b(cn.jiguang.ac.a.c(context), " ") + "$$" + strD + "$$" + aVarA.g + "$$" + aVarA.h;
        String str9 = cn.jiguang.ao.a.a(context).j;
        if ("unknown".equalsIgnoreCase(str9)) {
            str9 = " ";
        }
        String strA = cn.jiguang.ar.b.a(context);
        int i = cn.jiguang.ar.b.a;
        String str10 = cn.jiguang.ao.a.a(context).p;
        String str11 = cn.jiguang.ao.a.a(context).i;
        String str12 = "";
        String strC = cn.jiguang.d.a.i(context) ? cn.jiguang.f.a.c("") : "";
        Object objA = cn.jiguang.aj.e.a(context, "get_all_ids", null);
        if (objA == null || !(objA instanceof JSONObject)) {
            str = "";
            str2 = str;
            str3 = str2;
            str4 = str3;
            str5 = str4;
        } else {
            cn.jiguang.ad.d.b("ConnectingHelper", "parse ids:" + objA.toString());
            JSONObject jSONObject = (JSONObject) objA;
            String strOptString = jSONObject.optString("udid", "");
            String strOptString2 = jSONObject.optString("oaid", "");
            String strOptString3 = jSONObject.optString("vaid", "");
            String strOptString4 = jSONObject.optString("aaid", "");
            String strOptString5 = jSONObject.optString("gadid", "");
            str2 = strOptString3;
            str5 = strOptString2;
            str3 = strOptString4;
            str12 = strOptString5;
            str = strOptString;
            str4 = "";
        }
        String str13 = i + "$$" + b(strA, " ") + "$$" + b(str10, " ") + "$$" + b(str11, " ") + "$$" + b(strC, " ") + "$$" + b(str9, " ") + "$$" + b(str, " ") + "$$" + b(str5, " ") + "$$" + b(str2, " ") + "$$" + b(str3, " ") + "$$" + b(str12, " ");
        cn.jiguang.aj.b.a();
        long jB = cn.jiguang.aj.b.b();
        String strC2 = cn.jiguang.d.a.c(context);
        cn.jiguang.ad.d.b("ConnectingHelper", "Register with: key:" + str6 + ", apkVersion:" + str7 + ", clientInfo:" + str8 + ", extKey:" + str13 + ",reg business:" + jB + " accountId:" + strC2);
        String str14 = str4;
        byte[] bArrA = cn.jiguang.al.b.a(context, cn.jiguang.al.b.a(b(context), str6, str7, str8, str13, jB, strC2));
        if (bArrA == null) {
            cn.jiguang.ad.d.g("ConnectingHelper", "Register failed - encrytor reg info failed");
            return -1;
        }
        if (aVar.a(bArrA) != 0) {
            cn.jiguang.ad.d.g("ConnectingHelper", "Register failed - send reg info failed");
            return -1;
        }
        try {
            Pair<cn.jiguang.al.c, ByteBuffer> pairA = cn.jiguang.al.a.a(context, aVar.a(20000).array(), str14);
            if (pairA == null || pairA.first == null || pairA.second == null || ((cn.jiguang.al.c) pairA.first).c != 0) {
                cn.jiguang.ad.d.g("ConnectingHelper", "Register failed - can't parse a Register Response");
                return -1;
            }
            cn.jiguang.al.e eVar = new cn.jiguang.al.e((cn.jiguang.al.c) pairA.first, (ByteBuffer) pairA.second);
            cn.jiguang.ad.d.b("ConnectingHelper", "register response:" + eVar);
            int i2 = eVar.a;
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.B().a(Integer.valueOf(i2))});
            if (i2 == 0) {
                long j = eVar.b;
                String str15 = eVar.c;
                String str16 = eVar.d;
                String str17 = eVar.e;
                cn.jiguang.ad.d.e("ConnectingHelper", "Register succeed - juid:" + j + ", registrationId:" + str16 + ", deviceId:" + str17);
                if (cn.jiguang.f.i.a(str16) || 0 == j) {
                    cn.jiguang.ad.d.i("ConnectingHelper", "Unexpected: registrationId/juid should not be empty. ");
                    return -1;
                }
                cn.jiguang.af.b.a(context, str17);
                cn.jiguang.af.b.a(context, j, str15, str16);
            }
            return i2;
        } catch (cn.jiguang.aj.f e) {
            cn.jiguang.ad.d.g("ConnectingHelper", "Register failed - recv msg failed with error:" + e);
            return -1;
        }
    }

    public static synchronized long b(Context context) {
        long j;
        long jLongValue = ((Long) cn.jiguang.g.b.a(context, cn.jiguang.g.a.H())).longValue();
        if (jLongValue == -1) {
            jLongValue = Math.abs(new Random().nextInt(10000));
        }
        j = (jLongValue + (jLongValue % 2 == 0 ? 1L : 2L)) % OkHttpUtils.DEFAULT_MILLISECONDS;
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.H().a(Long.valueOf(j))});
        return j;
    }

    private static String b(String str, String str2) {
        return !cn.jiguang.f.i.a(str) ? str : str2;
    }
}
