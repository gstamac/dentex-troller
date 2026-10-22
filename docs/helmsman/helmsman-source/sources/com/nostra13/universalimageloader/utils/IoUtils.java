package com.nostra13.universalimageloader.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes2.dex */
public final class IoUtils {
    public static final int CONTINUE_LOADING_PERCENTAGE = 75;
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    public static final int DEFAULT_IMAGE_TOTAL_SIZE = 512000;

    public interface CopyListener {
        boolean onBytesCopied(int i, int i2);
    }

    private IoUtils() {
    }

    public static boolean copyStream(InputStream inputStream, OutputStream outputStream, CopyListener copyListener) throws IOException {
        return copyStream(inputStream, outputStream, copyListener, 32768);
    }

    public static boolean copyStream(InputStream inputStream, OutputStream outputStream, CopyListener copyListener, int i) throws IOException {
        int iAvailable = inputStream.available();
        if (iAvailable <= 0) {
            iAvailable = DEFAULT_IMAGE_TOTAL_SIZE;
        }
        byte[] bArr = new byte[i];
        if (shouldStopLoading(copyListener, 0, iAvailable)) {
            return false;
        }
        int i2 = 0;
        do {
            int i3 = inputStream.read(bArr, 0, i);
            if (i3 != -1) {
                outputStream.write(bArr, 0, i3);
                i2 += i3;
            } else {
                outputStream.flush();
                return true;
            }
        } while (!shouldStopLoading(copyListener, i2, iAvailable));
        return false;
    }

    private static boolean shouldStopLoading(CopyListener copyListener, int i, int i2) {
        return (copyListener == null || copyListener.onBytesCopied(i, i2) || (i * 100) / i2 >= 75) ? false : true;
    }

    public static void readAndCloseStream(InputStream inputStream) {
        do {
            try {
            } catch (IOException unused) {
            } finally {
                closeSilently(inputStream);
            }
        } while (inputStream.read(new byte[32768], 0, 32768) != -1);
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }
}
