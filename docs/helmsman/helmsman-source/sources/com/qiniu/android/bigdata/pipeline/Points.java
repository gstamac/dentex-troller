package com.qiniu.android.bigdata.pipeline;

import com.qiniu.android.utils.FastDatePrinter;
import com.qiniu.android.utils.Json;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public final class Points {
    private static String buildString(Object obj) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof Boolean)) {
            return obj.toString();
        }
        if (obj instanceof String) {
            return ((String) obj).replace("\n", "\\n").replace("\t", "\\t");
        }
        if (obj instanceof Collection) {
            return Json.encodeList((Collection) obj);
        }
        if (obj instanceof Map) {
            return Json.encodeMap((Map) obj);
        }
        if (!(obj instanceof Date)) {
            return obj.toString();
        }
        return LazyHolder.INSTANCE.format((Date) obj);
    }

    public static <V> StringBuilder formatPoint(Map<String, V> map, StringBuilder sb) {
        for (Map.Entry<String, V> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(buildString(entry.getValue())).append("\t");
        }
        sb.replace(sb.length() - 1, sb.length(), "\n");
        return sb;
    }

    public static StringBuilder formatPoint(Object obj, StringBuilder sb) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        new Points();
        HashMap map = new HashMap();
        for (Field field : declaredFields) {
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException unused) {
            }
        }
        return formatPoint((Map) map, sb);
    }

    public static <V> StringBuilder formatPoints(Map<String, V>[] mapArr) {
        StringBuilder sb = new StringBuilder();
        for (Map<String, V> map : mapArr) {
            formatPoint((Map) map, sb);
        }
        return sb;
    }

    public static StringBuilder formatPoints(Object[] objArr) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objArr) {
            formatPoint(obj, sb);
        }
        return sb;
    }

    public static <V> StringBuilder formatPoints(List<Map<String, V>> list) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map<String, V>> it = list.iterator();
        while (it.hasNext()) {
            formatPoint((Map) it.next(), sb);
        }
        return sb;
    }

    public static <V> StringBuilder formatPointsObjects(List<V> list) {
        StringBuilder sb = new StringBuilder();
        Iterator<V> it = list.iterator();
        while (it.hasNext()) {
            formatPoint(it.next(), sb);
        }
        return sb;
    }

    private static class LazyHolder {
        private static final FastDatePrinter INSTANCE = new FastDatePrinter("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Calendar.getInstance().getTimeZone(), Locale.getDefault());

        private LazyHolder() {
        }
    }
}
