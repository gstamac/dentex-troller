package com.qiniu.android.utils;

import com.qiniu.android.common.Constants;
import java.io.UnsupportedEncodingException;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes2.dex */
public final class StringUtils {
    public static String join(String[] strArr, String str) {
        int length;
        if (strArr == null) {
            return null;
        }
        int length2 = strArr.length;
        int length3 = (str == null || str.equals("")) ? 0 : str.length();
        if (length2 == 0) {
            length = 0;
        } else {
            String str2 = strArr[0];
            length = ((str2 == null ? 16 : str2.length()) + length3) * length2;
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length2; i++) {
            if (i > 0) {
                sb.append(str);
            }
            String str3 = strArr[i];
            if (str3 != null) {
                sb.append(str3);
            }
        }
        return sb.toString();
    }

    public static String jsonJoin(String[] strArr) {
        int length = strArr.length;
        StringBuilder sb = new StringBuilder((strArr[0].length() + 3) * length);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(Typography.quote);
            sb.append(strArr[i]);
            sb.append(Typography.quote);
        }
        return sb.toString();
    }

    public static byte[] utf8Bytes(String str) {
        try {
            return str.getBytes(Constants.UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("");
    }

    public static String strip(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt > 31 && cCharAt < 127) {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }
}
