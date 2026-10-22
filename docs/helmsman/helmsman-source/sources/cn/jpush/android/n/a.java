package cn.jpush.android.n;

import cn.jiguang.internal.JConstants;
import cn.jpush.android.helper.Logger;
import cn.jpush.android.t.f;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/* JADX INFO: loaded from: classes.dex */
public class a {
    /* JADX WARN: Code duplicated, block: B:51:0x00fa  */
    /* JADX WARN: Code duplicated, block: B:68:0x00b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:84:0x00f9 A[SYNTHETIC] */
    public static String a(String str, int i, long j) {
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        String str2;
        Exception e;
        AssertionError e2;
        Logger.d("HttpManager", "action:httpSimpleGet - " + str);
        if (i < 1 || i > 10) {
            i = 1;
        }
        if (j < 200 || j > JConstants.MIN) {
            j = 2000;
        }
        int i2 = 0;
        int responseCode = -1;
        String str3 = null;
        while (true) {
            try {
                httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                try {
                    httpURLConnection.setRequestProperty("Connection", "Close");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    responseCode = httpURLConnection.getResponseCode();
                    Logger.v("HttpManager", "statusCode:" + responseCode);
                    try {
                        try {
                            if (responseCode == 200) {
                                inputStream = httpURLConnection.getInputStream();
                                try {
                                    str2 = new String(a(inputStream), "UTF-8");
                                    if (httpURLConnection != null) {
                                        try {
                                            httpURLConnection.disconnect();
                                        } catch (Throwable unused) {
                                        }
                                    }
                                    try {
                                        f.a((Closeable) inputStream);
                                        return str2;
                                    } catch (AssertionError e3) {
                                        e2 = e3;
                                        Logger.e("HttpManager", "Catch AssertionError to avoid http close crash - " + e2.toString());
                                        str3 = str2;
                                        if (i2 >= i) {
                                            return str3;
                                        }
                                        i2++;
                                        try {
                                            Thread.sleep(j);
                                        } catch (InterruptedException unused2) {
                                        }
                                    } catch (Exception e4) {
                                        e = e4;
                                        Logger.d("HttpManager", "http client execute error:" + e.getMessage());
                                        str3 = str2;
                                        if (i2 >= i) {
                                            return str3;
                                        }
                                        i2++;
                                        Thread.sleep(j);
                                    }
                                } catch (Throwable unused3) {
                                    Logger.d("HttpManager", "httpSimpleGet failed,retriedCount:" + i2 + ",statusCode:" + responseCode);
                                    if (httpURLConnection != null) {
                                        try {
                                            httpURLConnection.disconnect();
                                        } catch (Throwable unused4) {
                                        }
                                    }
                                    f.a((Closeable) inputStream);
                                    if (i2 >= i) {
                                        return str3;
                                    }
                                    i2++;
                                    Thread.sleep(j);
                                }
                            } else {
                                if (httpURLConnection != null) {
                                    try {
                                        httpURLConnection.disconnect();
                                    } catch (Throwable unused5) {
                                    }
                                }
                                f.a((Closeable) null);
                                if (i2 >= i) {
                                    return str3;
                                }
                                i2++;
                                Thread.sleep(j);
                            }
                            f.a((Closeable) inputStream);
                        } catch (AssertionError e5) {
                            str2 = str3;
                            e2 = e5;
                            Logger.e("HttpManager", "Catch AssertionError to avoid http close crash - " + e2.toString());
                            str3 = str2;
                        } catch (Exception e6) {
                            str2 = str3;
                            e = e6;
                            Logger.d("HttpManager", "http client execute error:" + e.getMessage());
                            str3 = str2;
                        }
                        Logger.d("HttpManager", "httpSimpleGet failed,retriedCount:" + i2 + ",statusCode:" + responseCode);
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        if (i2 >= i) {
                            return str3;
                        }
                        i2++;
                        Thread.sleep(j);
                    } catch (Throwable th) {
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Throwable unused6) {
                            }
                        }
                        f.a((Closeable) inputStream);
                        throw th;
                    }
                } catch (Throwable unused7) {
                    inputStream = null;
                }
            } catch (Throwable unused8) {
                httpURLConnection = null;
                inputStream = null;
            }
        }
    }

    public static byte[] a(InputStream inputStream) {
        return f.b(inputStream);
    }

    /* JADX WARN: Code duplicated, block: B:22:0x007e A[Catch: all -> 0x0081, TRY_LEAVE, TryCatch #0 {all -> 0x0081, blocks: (B:20:0x0079, B:22:0x007e), top: B:78:0x0079 }] */
    /* JADX WARN: Code duplicated, block: B:30:0x0091 A[Catch: all -> 0x0094, TRY_LEAVE, TryCatch #1 {all -> 0x0094, blocks: (B:28:0x008c, B:30:0x0091), top: B:80:0x008c }] */
    /* JADX WARN: Code duplicated, block: B:37:0x00a3 A[Catch: all -> 0x00a6, TRY_LEAVE, TryCatch #4 {all -> 0x00a6, blocks: (B:35:0x009e, B:37:0x00a3), top: B:85:0x009e }] */
    /* JADX WARN: Code duplicated, block: B:63:0x0112 A[Catch: all -> 0x011a, TRY_ENTER, TryCatch #8 {all -> 0x011a, blocks: (B:52:0x00eb, B:54:0x00f0, B:63:0x0112, B:65:0x0117), top: B:92:0x001f }] */
    /* JADX WARN: Code duplicated, block: B:65:0x0117 A[Catch: all -> 0x011a, TRY_LEAVE, TryCatch #8 {all -> 0x011a, blocks: (B:52:0x00eb, B:54:0x00f0, B:63:0x0112, B:65:0x0117), top: B:92:0x001f }] */
    /* JADX WARN: Code duplicated, block: B:71:0x0123 A[Catch: all -> 0x0126, TRY_LEAVE, TryCatch #5 {all -> 0x0126, blocks: (B:69:0x011e, B:71:0x0123), top: B:87:0x011e }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v1, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r1v15 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    private static byte[] a(String str) {
        InputStream inputStream;
        byte[] bArrA;
        String str2;
        ?? r1 = "httpGetIconBytes - Other wrong response status - ";
        Logger.d("HttpManager", "action:httpGetIconBytes-" + str);
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream2 = null;
        try {
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str).openConnection();
                try {
                    httpURLConnection2.setRequestProperty("Accept-Encoding", "identity");
                    httpURLConnection2.addRequestProperty("Connection", "Close");
                    httpURLConnection2.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
                    int responseCode = httpURLConnection2.getResponseCode();
                    Logger.v("HttpManager", "httpGetIconBytes statusCode:" + responseCode);
                    if (responseCode == 200) {
                        int contentLength = httpURLConnection2.getContentLength();
                        inputStream = httpURLConnection2.getInputStream();
                        if (inputStream != null) {
                            try {
                                bArrA = a(inputStream);
                            } catch (Throwable th) {
                                th = th;
                                bArrA = null;
                                httpURLConnection = httpURLConnection2;
                                r1 = bArrA;
                                try {
                                    Logger.w("HttpManager", "httpGetIconBytes failed:" + th.getMessage());
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                    return r1;
                                } catch (Throwable th2) {
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                            if (httpURLConnection != null) {
                                                httpURLConnection.disconnect();
                                            }
                                        } catch (Throwable unused) {
                                            throw th2;
                                        }
                                    } else if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                    throw th2;
                                }
                            }
                        } else {
                            bArrA = null;
                        }
                        try {
                            if (bArrA == null || contentLength == 0) {
                                Logger.d("HttpManager", "httpGetIconBytes - Unexpected: downloaded bytes content length is 0");
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                        if (httpURLConnection2 != null) {
                                            httpURLConnection2.disconnect();
                                        }
                                    } catch (Throwable unused2) {
                                    }
                                } else if (httpURLConnection2 != null) {
                                    httpURLConnection2.disconnect();
                                }
                                return null;
                            }
                            if (contentLength > 30720) {
                                Logger.d("HttpManager", "httpGetIconBytes - Unexpected:icon  size too big,max size is30720");
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                        if (httpURLConnection2 != null) {
                                            httpURLConnection2.disconnect();
                                        }
                                    } catch (Throwable unused3) {
                                    }
                                } else if (httpURLConnection2 != null) {
                                    httpURLConnection2.disconnect();
                                }
                                return null;
                            }
                            if (bArrA.length < contentLength) {
                                Logger.d("HttpManager", "httpGetIconBytes - Download bytes failed. Got bytes len < header content length.");
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                        if (httpURLConnection2 != null) {
                                            httpURLConnection2.disconnect();
                                        }
                                    } catch (Throwable unused4) {
                                    }
                                } else if (httpURLConnection2 != null) {
                                    httpURLConnection2.disconnect();
                                }
                                return null;
                            }
                            inputStream2 = inputStream;
                        } catch (Throwable th3) {
                            th = th3;
                            httpURLConnection = httpURLConnection2;
                            r1 = bArrA;
                            Logger.w("HttpManager", "httpGetIconBytes failed:" + th.getMessage());
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                        }
                    } else {
                        if (400 == responseCode) {
                            str2 = "httpGetIconBytes - server response failure - " + str;
                        } else {
                            str2 = 404 == responseCode ? "httpGetIconBytes - Request path does not exist: 404 - " + str : "httpGetIconBytes - Other wrong response status - " + responseCode + ", url:" + str;
                        }
                        Logger.d("HttpManager", str2);
                        r1 = 0;
                    }
                    if (inputStream2 != null) {
                        r1 = bArrA;
                        inputStream2.close();
                    }
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                } catch (Throwable th4) {
                    th = th4;
                    inputStream = null;
                    bArrA = null;
                }
            } catch (Throwable th5) {
                th = th5;
                inputStream = null;
                r1 = 0;
            }
        } catch (Throwable unused5) {
        }
        return r1;
    }

    public static byte[] a(String str, int i) {
        byte[] bArrA = null;
        for (int i2 = 0; i2 < i; i2++) {
            bArrA = a(str);
            if (bArrA != null) {
                break;
            }
        }
        return bArrA;
    }

    public static byte[] a(String str, int i, long j, int i2) throws Throwable {
        byte[] bArrB = null;
        for (int i3 = 0; i3 < i2; i3++) {
            bArrB = b(str, i, j);
            if (bArrB != null) {
                break;
            }
        }
        return bArrB;
    }

    /* JADX WARN: Code duplicated, block: B:116:0x013b A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:42:0x00ba A[PHI: r3 r4 r5 r8
  0x00ba: PHI (r3v6 int) = (r3v3 int), (r3v2 int) binds: [B:41:0x00b8, B:28:0x0089] A[DONT_GENERATE, DONT_INLINE]
  0x00ba: PHI (r4v5 int) = (r4v2 int), (r4v12 int) binds: [B:41:0x00b8, B:28:0x0089] A[DONT_GENERATE, DONT_INLINE]
  0x00ba: PHI (r5v7 java.io.InputStream) = (r5v3 java.io.InputStream), (r5v1 java.io.InputStream) binds: [B:41:0x00b8, B:28:0x0089] A[DONT_GENERATE, DONT_INLINE]
  0x00ba: PHI (r8v2 java.net.HttpURLConnection) = (r8v1 java.net.HttpURLConnection), (r8v6 java.net.HttpURLConnection) binds: [B:41:0x00b8, B:28:0x0089] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:79:0x013c  */
    public static byte[] b(String str, int i, long j) throws Throwable {
        HttpURLConnection httpURLConnection;
        Exception e;
        byte[] bArrA;
        StringBuilder sb;
        if (i < 1 || i > 10) {
            i = 1;
        }
        if (j < 200 || j > JConstants.MIN) {
            j = 2000;
        }
        Logger.d("HttpManager", "action:httpGet - " + str);
        int i2 = 0;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection2 = null;
        int responseCode = -1;
        int contentLength = 0;
        while (true) {
            try {
                try {
                    httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    try {
                        try {
                            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                            httpURLConnection.addRequestProperty("Connection", "Close");
                            responseCode = httpURLConnection.getResponseCode();
                            Logger.v("HttpManager", "statusCode:" + responseCode);
                            if (responseCode == 200) {
                                contentLength = httpURLConnection.getContentLength();
                                inputStream = httpURLConnection.getInputStream();
                                if (inputStream == null) {
                                    bArrA = null;
                                    break;
                                }
                                bArrA = a(inputStream);
                                break;
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException unused) {
                                }
                            }
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            httpURLConnection2 = httpURLConnection;
                            if (i2 >= i) {
                                return null;
                            }
                            i2++;
                            try {
                                Thread.sleep(((long) i2) * j);
                            } catch (InterruptedException unused2) {
                            }
                        } catch (Throwable th) {
                            th = th;
                            httpURLConnection2 = httpURLConnection;
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException unused3) {
                                }
                            }
                            if (httpURLConnection2 == null) {
                                throw th;
                            }
                            httpURLConnection2.disconnect();
                            throw th;
                        }
                    } catch (SSLHandshakeException unused4) {
                        httpURLConnection2 = httpURLConnection;
                        Logger.dd("HttpManager", "Catch SSLHandshakeException, http client execute error!");
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException unused5) {
                            }
                        }
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        bArrA = null;
                    } catch (SSLPeerUnverifiedException unused6) {
                        httpURLConnection2 = httpURLConnection;
                        Logger.ee("HttpManager", "Catch SSLPeerUnverifiedException, http client execute error!");
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException unused7) {
                            }
                        }
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        if (i2 >= i) {
                            return null;
                        }
                        i2++;
                        Thread.sleep(((long) i2) * j);
                    } catch (Exception e2) {
                        e = e2;
                        Logger.d("HttpManager", "http client execute error:" + e.getMessage());
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException unused8) {
                            }
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (SSLHandshakeException unused9) {
            } catch (SSLPeerUnverifiedException unused10) {
            } catch (Exception e3) {
                httpURLConnection = httpURLConnection2;
                e = e3;
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException unused11) {
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        if (200 == responseCode) {
            try {
                if (contentLength == 0) {
                    Logger.d("HttpManager", "Unexpected: downloaded bytes content length is 0");
                    return null;
                }
                if (bArrA.length >= contentLength) {
                    return bArrA;
                }
                Logger.d("HttpManager", "Download bytes failed. Got bytes len < header content length.");
                return null;
            } catch (Exception e4) {
                sb = new StringBuilder("parse response error:");
                str = e4.getMessage();
            }
        } else if (400 == responseCode) {
            sb = new StringBuilder("server response failure - ");
        } else {
            sb = 404 == responseCode ? new StringBuilder("Request path does not exist: 404 - ") : new StringBuilder("Other wrong response status - ").append(responseCode).append(", url:");
        }
        Logger.d("HttpManager", sb.append(str).toString());
        return null;
    }
}
