package cn.jiguang.f;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    private static final Object b = new Object();
    private static ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>> c = new ConcurrentHashMap<>();
    public static String a = "yyyyMMdd_HHmm";

    public static String a() {
        return a("yyyy-MM-dd_HH:mm:ss").format(new Date());
    }

    public static SimpleDateFormat a(String str) {
        ThreadLocal<SimpleDateFormat> dVar = c.get(str);
        if (dVar == null) {
            synchronized (b) {
                dVar = c.get(str);
                if (dVar == null) {
                    dVar = new d(str);
                    c.put(str, dVar);
                }
            }
        }
        return dVar.get();
    }

    public static boolean a(Date date, int i) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date.getTime());
        calendar.roll(6, -2);
        return calendar.after(calendar2);
    }

    public static String b() {
        return a(a).format(new Date());
    }

    public static Date b(String str) {
        try {
            return a(a).parse(str);
        } catch (ParseException e) {
            cn.jiguang.ad.d.b("DateUtil", "parse filename datetime error - " + str, e);
            return null;
        }
    }
}
