package com.zendesk.util;

import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class FileUtils {
    private static final String BINARY_PREFIXES = "KMGTPE";
    private static final int BINARY_UNIT = 1024;
    private static final String SI_PREFIXES = "kMGTPE";
    private static final int SI_UNIT = 1000;

    private FileUtils() {
    }

    public static String humanReadableFileSize(Long l) {
        return humanReadableFileSize(l, true);
    }

    public static String humanReadableFileSize(Long l, boolean z) {
        if (l == null || l.longValue() < 0) {
            return "";
        }
        int i = z ? 1000 : 1024;
        if (l.longValue() < i) {
            return l + " B";
        }
        double d = i;
        int iLog = (int) (Math.log(l.longValue()) / Math.log(d));
        return String.format(Locale.US, "%.1f %sB", Double.valueOf(l.longValue() / Math.pow(d, iLog)), (z ? SI_PREFIXES : BINARY_PREFIXES).charAt(iLog - 1) + (z ? "" : "i"));
    }

    public static String getFileExtension(String str) {
        int iLastIndexOf;
        return (!StringUtils.hasLength(str) || (iLastIndexOf = str.lastIndexOf(".")) == -1) ? "" : str.substring(iLastIndexOf + 1).toLowerCase(Locale.US).trim();
    }
}
