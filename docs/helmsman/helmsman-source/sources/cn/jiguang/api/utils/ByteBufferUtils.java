package cn.jiguang.api.utils;

import cn.jiguang.ap.d;
import cn.jiguang.api.JResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/* JADX INFO: loaded from: classes.dex */
public class ByteBufferUtils {
    public static final int ERROR_CODE = 10000;
    private static final String TAG = "ByteBufferUtils";

    private static String generalExtraStr(Throwable th, JResponse jResponse, ByteBuffer byteBuffer) {
        StringBuilder sb = new StringBuilder();
        if (jResponse != null) {
            sb.append(jResponse.toString());
            sb.append("|bytebuffer:" + (byteBuffer == null ? "byteBuffer is null" : byteBuffer.toString()));
        }
        d.f(TAG, "byteBuffer info:" + sb.toString());
        try {
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            d.f(TAG, "parse data error stackTrace:" + stringWriter.toString());
        } catch (Exception unused) {
        }
        return sb.toString();
    }

    /* JADX WARN: Code duplicated, block: B:12:0x001f  */
    /* JADX WARN: Code duplicated, block: B:17:? A[RETURN, SYNTHETIC] */
    public static Byte get(ByteBuffer byteBuffer, JResponse jResponse) {
        Throwable thFillInStackTrace;
        try {
            return Byte.valueOf(byteBuffer.get());
        } catch (BufferOverflowException e) {
            thFillInStackTrace = e.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return null;
            }
            jResponse.code = 10000;
            return null;
        } catch (BufferUnderflowException e2) {
            thFillInStackTrace = e2.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return null;
            }
            jResponse.code = 10000;
            return null;
        } catch (Exception e3) {
            thFillInStackTrace = e3.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return null;
            }
            jResponse.code = 10000;
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x001b  */
    /* JADX WARN: Code duplicated, block: B:17:? A[RETURN, SYNTHETIC] */
    public static ByteBuffer get(ByteBuffer byteBuffer, byte[] bArr, JResponse jResponse) {
        Throwable thFillInStackTrace;
        try {
            return byteBuffer.get(bArr);
        } catch (BufferOverflowException e) {
            thFillInStackTrace = e.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return null;
            }
            jResponse.code = 10000;
            return null;
        } catch (BufferUnderflowException e2) {
            thFillInStackTrace = e2.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return null;
            }
            jResponse.code = 10000;
            return null;
        } catch (Exception e3) {
            thFillInStackTrace = e3.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return null;
            }
            jResponse.code = 10000;
            return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x001b  */
    /* JADX WARN: Code duplicated, block: B:17:? A[RETURN, SYNTHETIC] */
    public static int getInt(ByteBuffer byteBuffer, JResponse jResponse) {
        Throwable thFillInStackTrace;
        try {
            return byteBuffer.getInt();
        } catch (BufferOverflowException e) {
            thFillInStackTrace = e.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return -1;
            }
            jResponse.code = 10000;
            return -1;
        } catch (BufferUnderflowException e2) {
            thFillInStackTrace = e2.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return -1;
            }
            jResponse.code = 10000;
            return -1;
        } catch (Exception e3) {
            thFillInStackTrace = e3.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return -1;
            }
            jResponse.code = 10000;
            return -1;
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x001b  */
    /* JADX WARN: Code duplicated, block: B:17:? A[RETURN, SYNTHETIC] */
    public static long getLong(ByteBuffer byteBuffer, JResponse jResponse) {
        Throwable thFillInStackTrace;
        try {
            return byteBuffer.getLong();
        } catch (BufferOverflowException e) {
            thFillInStackTrace = e.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return 0L;
            }
            jResponse.code = 10000;
            return 0L;
        } catch (BufferUnderflowException e2) {
            thFillInStackTrace = e2.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return 0L;
            }
            jResponse.code = 10000;
            return 0L;
        } catch (Exception e3) {
            thFillInStackTrace = e3.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return 0L;
            }
            jResponse.code = 10000;
            return 0L;
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x001b  */
    /* JADX WARN: Code duplicated, block: B:17:? A[RETURN, SYNTHETIC] */
    public static short getShort(ByteBuffer byteBuffer, JResponse jResponse) {
        Throwable thFillInStackTrace;
        try {
            return byteBuffer.getShort();
        } catch (BufferOverflowException e) {
            thFillInStackTrace = e.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return (short) -1;
            }
            jResponse.code = 10000;
            return (short) -1;
        } catch (BufferUnderflowException e2) {
            thFillInStackTrace = e2.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return (short) -1;
            }
            jResponse.code = 10000;
            return (short) -1;
        } catch (Exception e3) {
            thFillInStackTrace = e3.fillInStackTrace();
            onException(thFillInStackTrace, jResponse, byteBuffer);
            if (jResponse == null) {
                return (short) -1;
            }
            jResponse.code = 10000;
            return (short) -1;
        }
    }

    private static void onException(Throwable th, JResponse jResponse, ByteBuffer byteBuffer) {
        generalExtraStr(th, jResponse, byteBuffer);
    }
}
