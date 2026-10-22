package com.zendesk.logger;

import android.util.Log;
import com.zendesk.service.ErrorResponse;
import com.zendesk.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes2.dex */
public class Logger {
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");
    private static boolean sLoggable;
    private static Platform sPlatform;

    interface Platform {
        boolean appendUtcInLogs(String str);

        void log(Priority priority, String str, String str2, Throwable th);
    }

    static {
        try {
            Class.forName("android.os.Build");
            sPlatform = new Android();
        } catch (ClassNotFoundException unused) {
            if (sPlatform == null) {
                sPlatform = new Java();
            }
        } catch (Throwable th) {
            if (sPlatform == null) {
                sPlatform = new Java();
            }
            throw th;
        }
        sLoggable = false;
    }

    enum Priority {
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6);

        private final int priority;

        Priority(int i) {
            this.priority = i;
        }
    }

    private Logger() {
    }

    public static boolean isLoggable() {
        return sLoggable;
    }

    public static void setLoggable(boolean z) {
        sLoggable = z;
    }

    public static void w(String str, String str2, Object... objArr) {
        logInternal(Priority.WARN, str, str2, null, objArr);
    }

    public static void w(String str, String str2, Throwable th, Object... objArr) {
        logInternal(Priority.WARN, str, str2, th, objArr);
    }

    public static void e(String str, String str2, Object... objArr) {
        logInternal(Priority.ERROR, str, str2, null, objArr);
    }

    public static void e(String str, String str2, Throwable th, Object... objArr) {
        logInternal(Priority.ERROR, str, str2, th, objArr);
    }

    public static void v(String str, String str2, Object... objArr) {
        logInternal(Priority.VERBOSE, str, str2, null, objArr);
    }

    public static void v(String str, String str2, Throwable th, Object... objArr) {
        logInternal(Priority.VERBOSE, str, str2, th, objArr);
    }

    public static void i(String str, String str2, Object... objArr) {
        logInternal(Priority.INFO, str, str2, null, objArr);
    }

    public static void i(String str, String str2, Throwable th, Object... objArr) {
        logInternal(Priority.INFO, str, str2, th, objArr);
    }

    public static void d(String str, String str2, Object... objArr) {
        logInternal(Priority.DEBUG, str, str2, null, objArr);
    }

    public static void d(String str, String str2, Throwable th, Object... objArr) {
        logInternal(Priority.DEBUG, str, str2, th, objArr);
    }

    public static void e(String str, ErrorResponse errorResponse) {
        StringBuilder sb = new StringBuilder();
        if (errorResponse != null) {
            sb.append("Network Error: ").append(errorResponse.isNetworkError());
            sb.append(", Status Code: ").append(errorResponse.getStatus());
            if (StringUtils.hasLength(errorResponse.getReason())) {
                sb.append(", Reason: ").append(errorResponse.getReason());
            }
        }
        String string = sb.toString();
        Priority priority = Priority.ERROR;
        if (!StringUtils.hasLength(string)) {
            string = "Unknown error";
        }
        logInternal(priority, str, string, null, new Object[0]);
    }

    private static void logInternal(Priority priority, String str, String str2, Throwable th, Object... objArr) {
        if (objArr != null && objArr.length > 0) {
            str2 = String.format(Locale.US, str2, objArr);
        }
        sPlatform.log(priority, str, str2, th);
    }

    static class Java implements Platform {
        @Override // com.zendesk.logger.Logger.Platform
        public boolean appendUtcInLogs(String str) {
            return false;
        }

        Java() {
        }

        @Override // com.zendesk.logger.Logger.Platform
        public void log(Priority priority, String str, String str2, Throwable th) {
            if (Logger.sLoggable) {
                StringBuilder sb = new StringBuilder(100);
                StringBuilder sbAppend = sb.append("[").append(new SimpleDateFormat(Logger.ISO_8601_FORMAT, Locale.US).format(new Date())).append("] ");
                if (priority == null) {
                    priority = Priority.INFO;
                }
                StringBuilder sbAppend2 = sbAppend.append(LoggerHelper.getLevelFromPriority(priority.priority)).append("/");
                if (!StringUtils.hasLength(str)) {
                    str = "UNKNOWN";
                }
                sbAppend2.append(str).append(": ").append(str2);
                System.out.println(sb.toString());
                if (th != null) {
                    th.printStackTrace(System.out);
                }
            }
        }
    }

    static class Android implements Platform {
        private static final int MAX_LINE_LENGTH = 4000;

        Android() {
        }

        @Override // com.zendesk.logger.Logger.Platform
        public boolean appendUtcInLogs(String str) {
            return StringUtils.hasLength(str) && (str.endsWith("Provider") || str.endsWith("Service"));
        }

        @Override // com.zendesk.logger.Logger.Platform
        public void log(Priority priority, String str, String str2, Throwable th) {
            if (Logger.sLoggable) {
                String androidTag = LoggerHelper.getAndroidTag(str);
                if (appendUtcInLogs(str) && Priority.ERROR == priority) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Logger.ISO_8601_FORMAT, Locale.US);
                    simpleDateFormat.setTimeZone(Logger.UTC_TIMEZONE);
                    Log.println(Priority.ERROR.priority, androidTag, "Time in UTC: " + simpleDateFormat.format(new Date()));
                }
                if (th != null) {
                    str2 = str2 + StringUtils.LINE_SEPARATOR + Log.getStackTraceString(th);
                }
                Iterator<String> it = LoggerHelper.splitLogMessage(str2, 4000).iterator();
                while (it.hasNext()) {
                    Log.println(priority == null ? Priority.INFO.priority : priority.priority, androidTag, it.next());
                }
            }
        }
    }
}
