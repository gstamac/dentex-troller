package cn.jiguang.ao;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import cn.jiguang.f.e;
import cn.jiguang.f.i;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    private static List<String> a;

    static {
        ArrayList arrayList = new ArrayList();
        a = arrayList;
        arrayList.add("358673013795895");
        a.add("004999010640000");
        a.add("00000000000000");
        a.add("000000000000000");
    }

    public static String a(Context context) {
        String str = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.D());
        if (!TextUtils.isEmpty(str)) {
            str = new String(Base64.decode(str, 2));
        }
        if (a(str)) {
            return str;
        }
        String strB = b(context);
        if (!TextUtils.isEmpty(strB)) {
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.D().a(Base64.encodeToString(strB.getBytes(), 2))});
        }
        return strB;
    }

    private static boolean a(String str) {
        if (!i.h(str) || str.length() < 10) {
            return false;
        }
        Iterator<String> it = a.iterator();
        while (it.hasNext()) {
            if (str.startsWith(it.next())) {
                return false;
            }
        }
        return true;
    }

    private static String b(Context context) {
        try {
            String str = a.a(context).p;
            if (a(str)) {
                return str;
            }
            String str2 = a.a(context).i;
            if (a(str2) && !"9774d56d682e549c".equals(str2.toLowerCase(Locale.getDefault()))) {
                return str2;
            }
            String strH = cn.jiguang.d.a.i(context) ? cn.jiguang.f.a.h(context) : "";
            if (!a(strH) && (strH = c(context)) == null) {
                strH = " ";
            }
            return a(strH) ? strH : "";
        } catch (Exception e) {
            cn.jiguang.ad.d.b("UDIDUtils", "", e);
            String strC = c(context);
            return a(strC) ? strC : "";
        }
    }

    private static String c(Context context) {
        cn.jiguang.ad.d.b("UDIDUtils", "Action:getSavedUuid");
        String str = (String) cn.jiguang.g.b.b(context, cn.jiguang.g.a.S());
        if (!i.a(str)) {
            return str;
        }
        if (!cn.jiguang.f.a.a()) {
            return e(context);
        }
        String str2 = (String) cn.jiguang.g.b.a(context, cn.jiguang.g.a.E());
        if (TextUtils.isEmpty(str2)) {
            return (cn.jiguang.f.a.c(context, "android.permission.WRITE_EXTERNAL_STORAGE") && cn.jiguang.f.a.c(context, "android.permission.READ_EXTERNAL_STORAGE")) ? d(context) : e(context);
        }
        return str2;
    }

    private static String d(Context context) {
        String strB = cn.jiguang.f.a.b();
        String str = strB == null ? null : strB + ".push_udid";
        File file = i.a(str) ? null : new File(str);
        String strB2 = e.b(file);
        if (!TextUtils.isEmpty(strB2)) {
            cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.E().a(strB2)});
            cn.jiguang.ad.d.d("UDIDUtils", "Got sdcard file saved udid - " + strB2);
            return strB2;
        }
        String strJ = i.j(UUID.nameUUIDFromBytes(new StringBuilder().append(System.currentTimeMillis()).toString().getBytes()).toString());
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{cn.jiguang.g.a.E().a(strJ)});
        e.a(file, strJ);
        return strJ;
    }

    private static String e(Context context) {
        cn.jiguang.g.a<String> aVarS = cn.jiguang.g.a.S();
        String str = (String) cn.jiguang.g.b.b(context, aVarS);
        if (str != null) {
            return str;
        }
        String string = UUID.randomUUID().toString();
        cn.jiguang.g.b.a(context, (cn.jiguang.g.a<?>[]) new cn.jiguang.g.a[]{aVarS.a(string)});
        return string;
    }
}
