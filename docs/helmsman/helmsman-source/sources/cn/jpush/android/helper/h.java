package cn.jpush.android.helper;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class h {
    private static List<String> a;
    private static List<String> b;
    private static Map<String, Integer> c;
    private static Map<String, Integer> d;
    private static List<String> e;
    private static String f;
    private static String g;
    private static String h;

    static {
        HashMap map = new HashMap();
        c = map;
        map.put("xiaomi", 1);
        c.put("meizu", 1);
        c.put("vivo", 1);
        c.put("lge", 1);
        ArrayList arrayList = new ArrayList();
        a = arrayList;
        arrayList.add("Smartisan");
        a.add("Lenovo");
        ArrayList arrayList2 = new ArrayList();
        b = arrayList2;
        arrayList2.add("MI 5C");
        ArrayList arrayList3 = new ArrayList();
        e = arrayList3;
        arrayList3.add("nubia");
        HashMap map2 = new HashMap();
        d = map2;
        map2.put("galaxy nexus", 1);
        f = "";
        g = "";
        h = "";
    }

    public static int a(Context context, int i, long j) {
        try {
            String[] strArrSplit = cn.jpush.android.cache.a.l(context).split(",");
            int iIntValue = Integer.valueOf(strArrSplit[0]).intValue();
            long jLongValue = Long.valueOf(strArrSplit[1]).longValue();
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (cn.jpush.android.t.b.a(jCurrentTimeMillis, jLongValue)) {
                if (i == 0) {
                    Logger.d("SSPHelper", "ssp count is 0，not limit count");
                } else if (iIntValue >= i) {
                    Logger.d("SSPHelper", "ssp message count limit,sspLastCount:" + iIntValue + ",sspLimitCount:" + i);
                    return -1;
                }
                if (j == 0) {
                    Logger.d("SSPHelper", "ssp interval is 0，not limit time");
                } else if (jCurrentTimeMillis - jLongValue <= j) {
                    Logger.d("SSPHelper", "ssp message time limit,sspLastTime:" + jLongValue + ",currentTime:" + jCurrentTimeMillis + ",sspLimitInterval:" + j);
                    return -2;
                }
            } else {
                Logger.d("SSPHelper", "is a new day,reset sspState");
                cn.jpush.android.cache.a.e(context, "0,0");
            }
        } catch (Throwable th) {
            Logger.w("SSPHelper", "getSSPState error:" + th.getMessage());
        }
        return 0;
    }

    private static String a(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str2 = (String) cls.getDeclaredMethod("get", String.class).invoke(cls, str);
            Logger.i("SSPHelper", "get " + str + " version is:" + str2);
            return str2;
        } catch (Throwable th) {
            Logger.e("SSPHelper", " get " + str + "wrong error:" + th.getMessage());
            return "";
        }
    }

    public static void a(Context context) {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            String[] strArrSplit = cn.jpush.android.cache.a.l(context).split(",");
            int iIntValue = cn.jpush.android.t.b.a(jCurrentTimeMillis, Long.valueOf(strArrSplit[1]).longValue()) ? 1 + Integer.valueOf(strArrSplit[0]).intValue() : 1;
            Logger.d("SSPHelper", "setSSPState sspCount:" + iIntValue + ",sspTime:" + jCurrentTimeMillis);
            cn.jpush.android.cache.a.e(context, iIntValue + "," + jCurrentTimeMillis);
        } catch (Throwable th) {
            Logger.w("SSPHelper", "setSSPState error:" + th.getMessage());
        }
    }

    public static boolean a() {
        List<String> list;
        List<String> list2;
        try {
            String str = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(str) && (list2 = a) != null && !list2.isEmpty()) {
                Iterator<String> it = a.iterator();
                while (it.hasNext()) {
                    if (it.next().toLowerCase().equals(str.toLowerCase())) {
                        Logger.d("SSPHelper", "black manufacturer:" + str);
                        return true;
                    }
                }
            }
            str.toLowerCase().equals("nubia");
        } catch (Throwable th) {
            Logger.d("SSPHelper", "manufacturer fiflter failed:" + th.getMessage());
        }
        try {
            String str2 = Build.MODEL;
            if (!TextUtils.isEmpty(str2) && (list = b) != null && !list.isEmpty()) {
                Iterator<String> it2 = b.iterator();
                while (it2.hasNext()) {
                    if (it2.next().toLowerCase().equals(str2.toLowerCase())) {
                        Logger.d("SSPHelper", "black model:" + str2);
                        return true;
                    }
                }
            }
        } catch (Throwable th2) {
            Logger.d("SSPHelper", "model fiflter failed:" + th2.getMessage());
        }
        String strD = d();
        if (!TextUtils.isEmpty(strD) && strD.startsWith("EmotionUI_4.0")) {
            Logger.d("SSPHelper", "emuiVersion is in black:" + strD);
            return true;
        }
        try {
            String strE = e();
            if (TextUtils.isEmpty(strE)) {
                return false;
            }
            if (!strE.startsWith("Funtouch OS_4.0") && !strE.startsWith("Funtouch OS_3.1") && !strE.startsWith("Funtouch OS_9")) {
                return false;
            }
            Logger.d("SSPHelper", "vivoVersion is in black:" + strE);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static int b() {
        Map<String, Integer> map;
        Map<String, Integer> map2;
        try {
            String str = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(str) && (map2 = c) != null && !map2.isEmpty() && c.containsKey(str.toLowerCase())) {
                Logger.d("SSPHelper", "found notify style by manufacturer:" + str);
                return c.get(str.toLowerCase()).intValue();
            }
        } catch (Throwable th) {
            Logger.d("SSPHelper", "manufacturer fiflter failed:" + th.getMessage());
        }
        try {
            String str2 = Build.MODEL;
            if (!TextUtils.isEmpty(str2) && (map = d) != null && !map.isEmpty() && d.containsKey(str2.toLowerCase())) {
                Logger.d("SSPHelper", "found notify style by model:" + str2);
                return d.get(str2.toLowerCase()).intValue();
            }
        } catch (Throwable th2) {
            Logger.d("SSPHelper", "model fiflter failed:" + th2.getMessage());
        }
        String strF = f();
        if (TextUtils.isEmpty(strF)) {
            return 0;
        }
        if (!strF.startsWith("V3.0") && !strF.startsWith("V2.")) {
            return 0;
        }
        Logger.d("SSPHelper", "oppo V2.X/V3.0 version use left-right notify style :" + strF);
        return -1;
    }

    public static boolean c() {
        List<String> list;
        try {
            String str = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(str) && (list = e) != null && !list.isEmpty()) {
                Iterator<String> it = e.iterator();
                while (it.hasNext()) {
                    if (it.next().toLowerCase().equals(str.toLowerCase())) {
                        Logger.d("SSPHelper", "one line manufacturer:" + str);
                        return true;
                    }
                }
            }
        } catch (Throwable th) {
            Logger.d("SSPHelper", "manufacturer fiflter failed:" + th.getMessage());
        }
        String strF = f();
        if (TextUtils.isEmpty(strF)) {
            return false;
        }
        if (!strF.startsWith("V3.2") && !strF.startsWith("V3.1")) {
            return false;
        }
        Logger.d("SSPHelper", "oppo V3.1/V3.2 version use one line  qnotify style :" + strF);
        return true;
    }

    private static String d() {
        if (!TextUtils.isEmpty(f)) {
            return f;
        }
        String strA = a("ro.build.version.emui");
        f = strA;
        return strA;
    }

    private static String e() {
        if (!TextUtils.isEmpty(h)) {
            return h;
        }
        String strA = a("ro.vivo.os.build.display.id");
        h = strA;
        return strA;
    }

    private static String f() {
        if (!TextUtils.isEmpty(g)) {
            return g;
        }
        String strA = a("ro.build.version.opporom");
        g = strA;
        return strA;
    }
}
