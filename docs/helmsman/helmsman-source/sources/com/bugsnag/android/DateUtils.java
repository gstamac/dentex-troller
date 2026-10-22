package com.bugsnag.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
class DateUtils {
    private static final ThreadLocal<DateFormat> iso8601Holder = new ThreadLocal<DateFormat>() { // from class: com.bugsnag.android.DateUtils.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public DateFormat initialValue() {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            simpleDateFormat.setTimeZone(timeZone);
            return simpleDateFormat;
        }
    };

    DateUtils() {
    }

    static String toISO8601(Date date) {
        return iso8601Holder.get().format(date);
    }
}
