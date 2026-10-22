package cn.jiguang.k;

import android.text.TextUtils;
import cn.jiguang.o.e;
import java.util.ArrayList;
import java.util.List;
import kotlin.UByte;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static String a(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(i & 255);
        stringBuffer.append('.');
        stringBuffer.append((i >> 8) & 255);
        stringBuffer.append('.');
        stringBuffer.append((i >> 16) & 255);
        stringBuffer.append('.');
        stringBuffer.append((i >> 24) & 255);
        return stringBuffer.toString();
    }

    static /* synthetic */ String a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(bArr[0] & UByte.MAX_VALUE);
        stringBuffer.append('.');
        stringBuffer.append(bArr[1] & UByte.MAX_VALUE);
        stringBuffer.append('.');
        stringBuffer.append(bArr[2] & UByte.MAX_VALUE);
        stringBuffer.append('.');
        stringBuffer.append(bArr[3] & UByte.MAX_VALUE);
        return stringBuffer.toString();
    }

    public static List<cn.jiguang.j.a> a(String str) {
        cn.jiguang.j.a aVarB;
        List<String> listA = e.a(new String[]{"cat /proc/net/arp"}, 1);
        if (listA == null || listA.isEmpty()) {
            cn.jiguang.w.a.d("JArpHelper", "execute command failed");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : listA) {
            if (!TextUtils.isEmpty(str2) && (aVarB = b(str2)) != null && aVarB.c.equals("0x2") && !str.equals(aVarB.a) && !aVarB.d.equals("00:00:00:00:00:00")) {
                arrayList.add(aVarB);
            }
        }
        return arrayList;
    }

    public static void a(String str, byte[] bArr) {
        new b(str, 300).a(bArr, 0, 255);
    }

    private static cn.jiguang.j.a b(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] bytes = str.getBytes();
        cn.jiguang.j.a aVar = new cn.jiguang.j.a();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < bytes.length - 1) {
            i++;
            if (bytes[i] == 32) {
                int i4 = i - i2;
                if (i4 > 1) {
                    String str2 = new String(bytes, i2, i4);
                    if (i3 != 0) {
                        if (i3 != 1) {
                            if (i3 != 2) {
                                if (i3 == 3) {
                                    aVar.d = str2;
                                    break;
                                }
                            } else {
                                aVar.c = str2;
                            }
                        } else {
                            aVar.b = str2;
                        }
                    } else {
                        aVar.a = str2;
                    }
                    i3++;
                }
                i2 = i + 1;
            }
        }
        return aVar;
    }
}
