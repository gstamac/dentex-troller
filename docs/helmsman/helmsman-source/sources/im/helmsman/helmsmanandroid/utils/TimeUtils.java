package im.helmsman.helmsmanandroid.utils;

import android.content.Context;
import im.helmsman.helmsmanandroid.MyApplication;
import im.helmsman.helmsmanandroid.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes2.dex */
public class TimeUtils {
    private static final int seconds_of_1day = 86400;
    private static final int seconds_of_1hour = 3600;
    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_1year = 31104000;
    private static final int seconds_of_30days = 2592000;

    public static String formatNowTime(String str) {
        return formatTimeToString(str, System.currentTimeMillis());
    }

    public static String formatTimeToString(String str, long j) {
        return new SimpleDateFormat(str).format(new Date(j));
    }

    public static Date timeZoneToLocal(String str) {
        return new Date(parserGMT(str).getTime() + ((long) TimeZone.getDefault().getRawOffset()));
    }

    public static String format(long j) {
        return new SimpleDateFormat("MM/dd/yy,HH:mm").format(new Date(j));
    }

    public static Date parserGMT(String str) {
        try {
            return new SimpleDateFormat("EEE, d MMM y HH:mm:ss 'GMT'", Locale.US).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTimeElapse(long j) {
        Context context = MyApplication.getContext();
        long jCurrentTimeMillis = (System.currentTimeMillis() - j) / 1000;
        if (jCurrentTimeMillis < 60) {
            return context.getString(R.string.timeutils_rightnow);
        }
        if (jCurrentTimeMillis < 120) {
            return "1 " + context.getString(R.string.timeutils_minuteago);
        }
        if (jCurrentTimeMillis < 3600) {
            return (jCurrentTimeMillis / 60) + " " + context.getString(R.string.timeutils_minutesago);
        }
        if (jCurrentTimeMillis < 7200) {
            return "1 " + context.getString(R.string.timeutils_hourago);
        }
        if (jCurrentTimeMillis < 86400) {
            return (jCurrentTimeMillis / 3600) + " " + context.getString(R.string.timeutils_hoursago);
        }
        if (jCurrentTimeMillis < 172800) {
            return "1 " + context.getString(R.string.timeutils_dayago);
        }
        if (jCurrentTimeMillis < 2592000) {
            return (jCurrentTimeMillis / 86400) + " " + context.getString(R.string.timeutils_daysago);
        }
        if (jCurrentTimeMillis < 5184000) {
            return "1 " + context.getString(R.string.timeutils_monthago);
        }
        if (jCurrentTimeMillis < 31104000) {
            return (jCurrentTimeMillis / 2592000) + " " + context.getString(R.string.timeutils_monthsago);
        }
        if (jCurrentTimeMillis < 62208000) {
            return (jCurrentTimeMillis / 31104000) + " " + context.getString(R.string.timeutils_yearago);
        }
        return jCurrentTimeMillis >= 31104000 ? (jCurrentTimeMillis / 31104000) + " " + context.getString(R.string.timeutils_yearsago) : "";
    }

    public static String getTimeElapse(Date date) {
        return getTimeElapse(date.getTime());
    }
}
