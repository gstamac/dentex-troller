package com.zendesk.logger;

import com.zendesk.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
class LoggerHelper {
    private static final String DEFAULT_LOG_TAG = "Zendesk";
    private static final int MAXIMUM_ANDROID_LOG_TAG_LENGTH = 23;

    static char getLevelFromPriority(int i) {
        if (i == 2) {
            return 'V';
        }
        if (i == 3) {
            return 'D';
        }
        if (i == 5) {
            return 'W';
        }
        if (i != 6) {
            return i != 7 ? 'I' : 'A';
        }
        return 'E';
    }

    private LoggerHelper() {
    }

    static List<String> splitLogMessage(String str, int i) {
        int iMin;
        ArrayList arrayList = new ArrayList();
        if (i < 1) {
            if (!StringUtils.hasLength(str)) {
                arrayList.add("");
                return arrayList;
            }
            arrayList.add(str);
            return arrayList;
        }
        if (!StringUtils.hasLength(str)) {
            arrayList.add("");
            return arrayList;
        }
        if (str.length() < i) {
            arrayList.add(str);
            return arrayList;
        }
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            int iIndexOf = str.indexOf(StringUtils.LINE_SEPARATOR, i2);
            if (iIndexOf == -1) {
                iIndexOf = length;
            }
            while (true) {
                iMin = Math.min(iIndexOf, i2 + i);
                arrayList.add(str.substring(i2, iMin));
                if (iMin >= iIndexOf) {
                    break;
                }
                i2 = iMin;
            }
            i2 = iMin + 1;
        }
        return arrayList;
    }

    static String getAndroidTag(String str) {
        if (StringUtils.isEmpty(str)) {
            return DEFAULT_LOG_TAG;
        }
        return str.length() > 23 ? str.substring(0, 23) : str;
    }
}
