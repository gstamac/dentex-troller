package com.zendesk.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes2.dex */
public class StringUtils {
    public static final String EMPTY_STRING = "";
    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static Map<Character, String> JS_ESCAPE_LOOKUP_MAP;
    public static final String LINE_SEPARATOR;

    static {
        HashMap map = new HashMap();
        JS_ESCAPE_LOOKUP_MAP = map;
        map.put('\'', "\\'");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf(Typography.quote), "\\\"");
        JS_ESCAPE_LOOKUP_MAP.put('\\', "\\\\");
        JS_ESCAPE_LOOKUP_MAP.put('/', "\\/");
        JS_ESCAPE_LOOKUP_MAP.put('\b', "\\b");
        JS_ESCAPE_LOOKUP_MAP.put('\n', "\\n");
        JS_ESCAPE_LOOKUP_MAP.put('\t', "\\t");
        JS_ESCAPE_LOOKUP_MAP.put('\f', "\\f");
        JS_ESCAPE_LOOKUP_MAP.put('\r', "\\r");
        LINE_SEPARATOR = System.getProperty("line.separator");
    }

    private StringUtils() {
    }

    public static boolean hasLength(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static boolean hasLengthMany(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return false;
        }
        for (String str : strArr) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        return !hasLength(str);
    }

    public static String toCsvString(String... strArr) {
        return toCsvString((List<String>) (strArr == null ? null : Arrays.asList(strArr)));
    }

    public static String toCsvString(List<String> list) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (hasLength(list.get(i))) {
                sb.append(list.get(i));
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    public static String toCsvStringNumber(Number... numberArr) {
        return toCsvStringNumber((List<? extends Number>) (numberArr == null ? null : Arrays.asList(numberArr)));
    }

    public static String toCsvStringNumber(List<? extends Number> list) {
        ArrayList arrayList;
        if (list != null) {
            arrayList = new ArrayList();
            for (Number number : list) {
                if (number != null) {
                    arrayList.add(number.toString());
                }
            }
        } else {
            arrayList = null;
        }
        return toCsvString(arrayList);
    }

    public static List<String> fromCsv(String str) {
        if (hasLength(str)) {
            String[] strArrSplit = str.split(",");
            ArrayList arrayList = new ArrayList();
            for (String str2 : strArrSplit) {
                if (hasLength(str2)) {
                    arrayList.add(str2);
                }
            }
            return CollectionUtils.unmodifiableList(arrayList);
        }
        return CollectionUtils.unmodifiableList(new ArrayList(0));
    }

    public static String toCsvString(long... jArr) {
        if (jArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (long j : jArr) {
            arrayList.add(Long.valueOf(j));
        }
        return toCsvStringNumber(arrayList);
    }

    public static String toDateInIsoFormat(Date date) {
        return date != null ? new SimpleDateFormat(ISO_8601_DATE_FORMAT).format(date) : "";
    }

    public static String escapeEcmaScript(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length() * 2);
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (JS_ESCAPE_LOOKUP_MAP.containsKey(Character.valueOf(cCharAt))) {
                sb.append(JS_ESCAPE_LOOKUP_MAP.get(Character.valueOf(cCharAt)));
            } else {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }

    public static String capitalize(String str) {
        if (!hasLength(str)) {
            if (str != null) {
                return str;
            }
            return null;
        }
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        sb.append(Character.toTitleCase(str.charAt(0)));
        sb.append(str.substring(1));
        return sb.toString();
    }

    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean startsWithIdeographic(String str) {
        if (hasLength(str)) {
            return Character.isIdeographic(str.codePointAt(0));
        }
        return false;
    }
}
