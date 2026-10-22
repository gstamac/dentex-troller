package com.zendesk.util;

import androidx.exifinterface.media.ExifInterface;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes2.dex */
public class NumberFormatUtil {
    private static long MILLION_PRECISION = 1000000;
    private static final NavigableMap<Long, NumberSuffix> SUFFIXES;

    public interface SuffixFormatDelegate {
        String getSuffix(NumberSuffix numberSuffix);
    }

    public enum NumberSuffix {
        NONE(""),
        KILO("k"),
        MEGA("M"),
        GIGA("G"),
        TERA(ExifInterface.GPS_DIRECTION_TRUE),
        PETA("P"),
        EXA(ExifInterface.LONGITUDE_EAST);

        private String suffix;

        NumberSuffix(String str) {
            this.suffix = str;
        }

        public String getSuffix() {
            return this.suffix;
        }
    }

    private NumberFormatUtil() {
    }

    static {
        TreeMap treeMap = new TreeMap();
        SUFFIXES = treeMap;
        treeMap.put(1000L, NumberSuffix.KILO);
        treeMap.put(1000000L, NumberSuffix.MEGA);
        treeMap.put(1000000000L, NumberSuffix.GIGA);
        treeMap.put(1000000000000L, NumberSuffix.TERA);
        treeMap.put(1000000000000000L, NumberSuffix.PETA);
        treeMap.put(1000000000000000000L, NumberSuffix.EXA);
    }

    public static String format(long j) {
        return processValue(j, null);
    }

    public static String format(long j, SuffixFormatDelegate suffixFormatDelegate) {
        return processValue(j, suffixFormatDelegate);
    }

    private static String processValue(long j, SuffixFormatDelegate suffixFormatDelegate) {
        long jLongValue;
        if (j == Long.MIN_VALUE) {
            return processValue(-9223372036854775807L, suffixFormatDelegate);
        }
        boolean z = j < 0;
        if (z) {
            j = -j;
        }
        if (j < 1000) {
            return formatValue(stringValue(j), NumberSuffix.NONE, suffixFormatDelegate);
        }
        Map.Entry<Long, NumberSuffix> entryFloorEntry = SUFFIXES.floorEntry(Long.valueOf(j));
        Long key = entryFloorEntry.getKey();
        NumberSuffix value = entryFloorEntry.getValue();
        if (key.longValue() <= MILLION_PRECISION) {
            jLongValue = (long) Math.ceil(j / (key.longValue() / 10.0d));
        } else {
            jLongValue = j / (key.longValue() / 10);
        }
        double d = jLongValue < 100 && ((double) jLongValue) / 10.0d != ((double) (jLongValue / 10)) ? jLongValue / 10.0d : jLongValue / 10;
        if (z) {
            d = -d;
        }
        return formatValue(stringValue(d), value, suffixFormatDelegate);
    }

    private static String formatValue(String str, NumberSuffix numberSuffix, SuffixFormatDelegate suffixFormatDelegate) {
        String suffix = numberSuffix.getSuffix();
        if (suffixFormatDelegate != null) {
            suffix = suffixFormatDelegate.getSuffix(numberSuffix);
        }
        return String.format(Locale.US, "%1$s%2$s", str, suffix);
    }

    private static String stringValue(double d) {
        if (d % 1.0d == 0.0d) {
            return String.format(Locale.US, "%1.0f", Double.valueOf(d));
        }
        return String.format(Locale.US, "%.1f", Double.valueOf(d));
    }
}
