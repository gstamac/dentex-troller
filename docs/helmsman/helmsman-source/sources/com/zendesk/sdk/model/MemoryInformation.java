package com.zendesk.sdk.model;

import android.app.ActivityManager;
import android.content.Context;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.R;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: classes2.dex */
public class MemoryInformation {
    public static final double BYTES_MULTIPLIER = 1024.0d;
    public static final int EXPECTED_TOKEN_COUNT = 3;
    private static final String LOG_TAG = "MemoryInformation";
    private final ActivityManager mActivityManager;
    private final Context mContext;
    private int mTotalMemory = getTotalMemory();

    public MemoryInformation(Context context) {
        this.mActivityManager = (ActivityManager) context.getSystemService("activity");
        this.mContext = context;
    }

    public String formatMemoryUsage() {
        return String.format(Locale.US, this.mContext.getString(R.string.rate_my_app_dialog_feedback_device_memory), String.valueOf(getUsedMemory()), String.valueOf(this.mTotalMemory));
    }

    public int getUsedMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        this.mActivityManager.getMemoryInfo(memoryInfo);
        return this.mTotalMemory - bytesToMegabytes(memoryInfo.availMem);
    }

    public int getTotalMemory() {
        Logger.d(LOG_TAG, "Using getTotalMemoryApi() to determine memory", new Object[0]);
        return getTotalMemoryApi();
    }

    /* JADX WARN: Code duplicated, block: B:40:0x009f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public int getTotalMemoryCompat() throws Throwable {
        BufferedReader bufferedReader;
        IOException e;
        String line;
        long jLongValue;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"));
            try {
                try {
                    line = bufferedReader.readLine();
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        Logger.w(LOG_TAG, "Failed to close /proc/meminfo file stream: " + e2.getMessage(), e2, new Object[0]);
                    }
                } catch (IOException e3) {
                    e = e3;
                    Logger.e(LOG_TAG, "Failed to determine total memory from /proc/meminfo: " + e.getMessage(), e, new Object[0]);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e4) {
                            Logger.w(LOG_TAG, "Failed to close /proc/meminfo file stream: " + e4.getMessage(), e4, new Object[0]);
                        }
                    }
                    line = "";
                }
            } catch (Throwable th) {
                th = th;
                bufferedReader2 = bufferedReader;
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e5) {
                        Logger.w(LOG_TAG, "Failed to close /proc/meminfo file stream: " + e5.getMessage(), e5, new Object[0]);
                    }
                }
                throw th;
            }
        } catch (IOException e6) {
            bufferedReader = null;
            e = e6;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(line);
        if (stringTokenizer.countTokens() == 3) {
            stringTokenizer.nextToken();
            jLongValue = Long.valueOf(stringTokenizer.nextToken()).longValue() * 1024;
        } else {
            jLongValue = 0;
        }
        return bytesToMegabytes(jLongValue);
    }

    public int getTotalMemoryApi() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        this.mActivityManager.getMemoryInfo(memoryInfo);
        return bytesToMegabytes(memoryInfo.totalMem);
    }

    public boolean isLowMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        this.mActivityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }

    private int bytesToMegabytes(long j) {
        return (int) Math.round((j / 1024.0d) / 1024.0d);
    }
}
