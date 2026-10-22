package cn.jpush.android.t;

import android.text.TextUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class b {
    private static final Object a = new Object();
    private static ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>> b = new ConcurrentHashMap<>();

    private static class a extends ThreadLocal<SimpleDateFormat> {
        private String a;

        a(String str) {
            this.a = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat(this.a, Locale.ENGLISH);
        }
    }

    public static SimpleDateFormat a(String str) {
        ThreadLocal<SimpleDateFormat> aVar = b.get(str);
        if (aVar == null) {
            synchronized (a) {
                aVar = b.get(str);
                if (aVar == null) {
                    aVar = new a(str);
                    b.put(str, aVar);
                }
            }
        }
        return aVar.get();
    }

    public static boolean a(long j, long j2) {
        try {
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            Long lValueOf = Long.valueOf(j);
            Long lValueOf2 = Long.valueOf(j2);
            String str = simpleDateFormat.format(lValueOf);
            String str2 = simpleDateFormat2.format(lValueOf2);
            Date date = simpleDateFormat.parse(str);
            Date date2 = simpleDateFormat2.parse(str2);
            calendar.setTime(date);
            calendar2.setTime(date2);
            return calendar.get(0) == calendar2.get(0) && calendar.get(1) == calendar2.get(1) && calendar.get(6) == calendar2.get(6);
        } catch (Exception unused) {
            return false;
        }
    }

    public static long b(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1L;
        }
        try {
            return a("yyyy-MM-dd HH:mm:ss").parse(str).getTime();
        } catch (ParseException unused) {
            return -1L;
        }
    }
}
