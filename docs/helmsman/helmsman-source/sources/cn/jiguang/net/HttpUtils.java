package cn.jiguang.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import cn.jiguang.ad.d;
import cn.jiguang.f.c;
import cn.jiguang.f.k;
import com.autonavi.ae.gmap.utils.GLMapStaticValue;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;

/* JADX INFO: loaded from: classes.dex */
public class HttpUtils {

    public abstract class HttpListener {
    }

    /* JADX WARN: Code duplicated, block: B:100:0x01fe A[Catch: all -> 0x023b, TryCatch #16 {all -> 0x023b, blocks: (B:96:0x01e7, B:98:0x01f3, B:99:0x01fa, B:106:0x0216, B:100:0x01fe, B:102:0x0202, B:103:0x020a, B:105:0x020e), top: B:137:0x01e7 }] */
    /* JADX WARN: Code duplicated, block: B:102:0x0202 A[Catch: all -> 0x023b, TryCatch #16 {all -> 0x023b, blocks: (B:96:0x01e7, B:98:0x01f3, B:99:0x01fa, B:106:0x0216, B:100:0x01fe, B:102:0x0202, B:103:0x020a, B:105:0x020e), top: B:137:0x01e7 }] */
    /* JADX WARN: Code duplicated, block: B:103:0x020a A[Catch: all -> 0x023b, TryCatch #16 {all -> 0x023b, blocks: (B:96:0x01e7, B:98:0x01f3, B:99:0x01fa, B:106:0x0216, B:100:0x01fe, B:102:0x0202, B:103:0x020a, B:105:0x020e), top: B:137:0x01e7 }] */
    /* JADX WARN: Code duplicated, block: B:105:0x020e A[Catch: all -> 0x023b, TryCatch #16 {all -> 0x023b, blocks: (B:96:0x01e7, B:98:0x01f3, B:99:0x01fa, B:106:0x0216, B:100:0x01fe, B:102:0x0202, B:103:0x020a, B:105:0x020e), top: B:137:0x01e7 }] */
    /* JADX WARN: Code duplicated, block: B:106:0x0216 A[Catch: all -> 0x023b, TRY_LEAVE, TryCatch #16 {all -> 0x023b, blocks: (B:96:0x01e7, B:98:0x01f3, B:99:0x01fa, B:106:0x0216, B:100:0x01fe, B:102:0x0202, B:103:0x020a, B:105:0x020e), top: B:137:0x01e7 }] */
    /* JADX WARN: Code duplicated, block: B:109:0x0237  */
    /* JADX WARN: Code duplicated, block: B:117:0x026e A[PHI: r1 r3 r9 r11
  0x026e: PHI (r1v7 java.io.InputStream) = (r1v2 java.io.InputStream), (r1v8 java.io.InputStream) binds: [B:91:0x01dd, B:116:0x026c] A[DONT_GENERATE, DONT_INLINE]
  0x026e: PHI (r3v9 java.io.OutputStream) = (r3v51 java.io.OutputStream), (r3v52 java.io.OutputStream) binds: [B:91:0x01dd, B:116:0x026c] A[DONT_GENERATE, DONT_INLINE]
  0x026e: PHI (r9v9 java.io.InputStream) = (r9v20 java.io.InputStream), (r9v21 java.io.InputStream) binds: [B:91:0x01dd, B:116:0x026c] A[DONT_GENERATE, DONT_INLINE]
  0x026e: PHI (r11v21 java.net.HttpURLConnection) = (r11v37 java.net.HttpURLConnection), (r11v38 java.net.HttpURLConnection) binds: [B:91:0x01dd, B:116:0x026c] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:122:0x027e  */
    /* JADX WARN: Code duplicated, block: B:34:0x00ce A[Catch: all -> 0x0192, IOException -> 0x0197, MalformedURLException -> 0x019d, TryCatch #14 {MalformedURLException -> 0x019d, IOException -> 0x0197, all -> 0x0192, blocks: (B:7:0x0017, B:11:0x0044, B:13:0x0051, B:14:0x0058, B:16:0x005e, B:17:0x0065, B:34:0x00ce, B:36:0x00df, B:32:0x00b6, B:20:0x006a, B:22:0x0070, B:24:0x0089, B:25:0x0093, B:27:0x0099, B:28:0x00a0, B:29:0x00a4), top: B:139:0x0017, inners: #14 }] */
    /* JADX WARN: Code duplicated, block: B:36:0x00df A[Catch: all -> 0x0192, IOException -> 0x0197, MalformedURLException -> 0x019d, TRY_LEAVE, TryCatch #14 {MalformedURLException -> 0x019d, IOException -> 0x0197, all -> 0x0192, blocks: (B:7:0x0017, B:11:0x0044, B:13:0x0051, B:14:0x0058, B:16:0x005e, B:17:0x0065, B:34:0x00ce, B:36:0x00df, B:32:0x00b6, B:20:0x006a, B:22:0x0070, B:24:0x0089, B:25:0x0093, B:27:0x0099, B:28:0x00a0, B:29:0x00a4), top: B:139:0x0017, inners: #14 }] */
    /* JADX WARN: Code duplicated, block: B:38:0x00e7  */
    /* JADX WARN: Code duplicated, block: B:47:0x0112 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:54:0x0125  */
    /* JADX WARN: Code duplicated, block: B:56:0x0128  */
    /* JADX WARN: Code duplicated, block: B:62:0x013c A[Catch: all -> 0x0175, IOException -> 0x017a, MalformedURLException -> 0x0180, TryCatch #13 {MalformedURLException -> 0x0180, IOException -> 0x017a, all -> 0x0175, blocks: (B:45:0x010c, B:58:0x012c, B:60:0x0132, B:62:0x013c, B:64:0x0148), top: B:141:0x010c }] */
    /* JADX WARN: Code duplicated, block: B:64:0x0148 A[Catch: all -> 0x0175, IOException -> 0x017a, MalformedURLException -> 0x0180, TRY_LEAVE, TryCatch #13 {MalformedURLException -> 0x0180, IOException -> 0x017a, all -> 0x0175, blocks: (B:45:0x010c, B:58:0x012c, B:60:0x0132, B:62:0x013c, B:64:0x0148), top: B:141:0x010c }] */
    /* JADX WARN: Code duplicated, block: B:67:0x0170  */
    /* JADX WARN: Code duplicated, block: B:98:0x01f3 A[Catch: all -> 0x023b, TryCatch #16 {all -> 0x023b, blocks: (B:96:0x01e7, B:98:0x01f3, B:99:0x01fa, B:106:0x0216, B:100:0x01fe, B:102:0x0202, B:103:0x020a, B:105:0x020e), top: B:137:0x01e7 }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r11v20 */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r11v39 */
    /* JADX WARN: Type inference failed for: r11v40 */
    /* JADX WARN: Type inference failed for: r11v41 */
    /* JADX WARN: Type inference failed for: r11v5, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r11v7 */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v53 */
    /* JADX WARN: Type inference failed for: r3v54 */
    /* JADX WARN: Type inference failed for: r3v55 */
    /* JADX WARN: Type inference failed for: r3v56 */
    /* JADX WARN: Type inference failed for: r3v57 */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r9v0, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r9v14 */
    /* JADX WARN: Type inference failed for: r9v18 */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v22 */
    /* JADX WARN: Type inference failed for: r9v23 */
    /* JADX WARN: Type inference failed for: r9v24 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v6, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r9v7, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r9v8 */
    private static HttpResponse a(Context context, HttpRequest httpRequest, boolean z) throws Throwable {
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        OutputStream outputStream;
        InputStream inputStream2;
        InputStream inputStream3;
        HttpURLConnection httpURLConnection2;
        OutputStream outputStream2;
        String str;
        HttpURLConnection httpURLConnection3;
        InputStream inputStream4;
        OutputStream outputStream3;
        OutputStream outputStream4;
        HttpsURLConnection httpsURLConnection;
        HostnameVerifier defaultHostVerifier;
        OutputStream outputStream5;
        byte[] inputStream5;
        byte[] paras;
        InputStream inputStream6 = null;
        errorStream = null;
        errorStream = null;
        errorStream = null;
        errorStream = null;
        errorStream = null;
        InputStream errorStream = null;
        inputStream6 = null;
        inputStream6 = null;
        inputStream6 = null;
        inputStream6 = null;
        inputStream6 = null;
        HttpURLConnection httpURLConnection4 = null;
        inputStream6 = null;
        if (httpRequest == null) {
            return null;
        }
        ?? url = httpRequest.getUrl();
        HttpResponse httpResponse = new HttpResponse(url);
        try {
            try {
                HttpURLConnection httpURLConnectionWithProxy = getHttpURLConnectionWithProxy(context, httpRequest.getUrl());
                try {
                    URL url2 = httpURLConnectionWithProxy.getURL();
                    d.a("HttpUtils", "host:" + url2.getHost() + ",port:" + url2.getPort());
                    if (httpRequest != null && httpURLConnectionWithProxy != null) {
                        setURLConnection(httpRequest.getRequestProperties(), httpURLConnectionWithProxy);
                        if (httpRequest.getConnectTimeout() >= 0) {
                            httpURLConnectionWithProxy.setConnectTimeout(httpRequest.getConnectTimeout());
                        }
                        if (httpRequest.getReadTimeout() >= 0) {
                            httpURLConnectionWithProxy.setReadTimeout(httpRequest.getReadTimeout());
                        }
                    }
                    if (httpURLConnectionWithProxy instanceof HttpsURLConnection) {
                        try {
                            if (httpRequest.getSslTrustManager() != null) {
                                SSLContext sSLContext = SSLContext.getInstance("TLS");
                                sSLContext.init(null, new TrustManager[]{httpRequest.getSslTrustManager()}, new SecureRandom());
                                if (sSLContext != null) {
                                    ((HttpsURLConnection) httpURLConnectionWithProxy).setSSLSocketFactory(sSLContext.getSocketFactory());
                                }
                            }
                            if (httpRequest.getHostnameVerifier() != null) {
                                httpsURLConnection = (HttpsURLConnection) httpURLConnectionWithProxy;
                                defaultHostVerifier = httpRequest.getHostnameVerifier();
                            } else {
                                httpsURLConnection = (HttpsURLConnection) httpURLConnectionWithProxy;
                                defaultHostVerifier = new DefaultHostVerifier(httpURLConnectionWithProxy.getURL().getHost());
                            }
                            httpsURLConnection.setHostnameVerifier(defaultHostVerifier);
                        } catch (Throwable th) {
                            d.g("HttpUtils", "set ssl config error:" + th.getMessage());
                        }
                        if (z != 0) {
                            httpURLConnectionWithProxy.setRequestMethod("POST");
                            httpURLConnectionWithProxy.setDoOutput(true);
                            httpURLConnectionWithProxy.setDoInput(true);
                            paras = httpRequest.getParas();
                            if (paras != null) {
                                outputStream5 = httpURLConnectionWithProxy.getOutputStream();
                                try {
                                    outputStream5.write(paras);
                                    outputStream5 = outputStream5;
                                } catch (MalformedURLException e) {
                                    e = e;
                                    httpURLConnection2 = httpURLConnectionWithProxy;
                                    inputStream3 = null;
                                    outputStream2 = outputStream5;
                                    d.f("HttpUtils", "http post  error:" + e.getMessage());
                                    httpResponse.setResponseCode(3004);
                                    httpResponse.setResponseBody("MalformedURLException");
                                    k.a((Closeable) inputStream6);
                                    k.a((Closeable) inputStream3);
                                    k.a(outputStream2);
                                    outputStream3 = outputStream2;
                                    inputStream4 = inputStream3;
                                    httpURLConnection3 = httpURLConnection2;
                                    url = outputStream2;
                                    context = inputStream3;
                                    z = httpURLConnection2;
                                    if (httpURLConnection2 != null) {
                                        httpURLConnection3.disconnect();
                                        url = outputStream3;
                                        context = inputStream4;
                                        z = httpURLConnection3;
                                    }
                                    return httpResponse;
                                } catch (IOException e2) {
                                    e = e2;
                                    inputStream2 = null;
                                    outputStream4 = outputStream5;
                                    httpURLConnection4 = httpURLConnectionWithProxy;
                                    context = inputStream2;
                                    url = outputStream4;
                                    try {
                                        httpResponse.setResponseCode(2998);
                                        httpResponse.setResponseBody("网络错误");
                                        if (e instanceof SocketTimeoutException) {
                                            httpResponse.setResponseCode(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_REALCITY);
                                            str = "请求超时";
                                        } else {
                                            if (e instanceof UnknownHostException) {
                                                if (e instanceof SSLHandshakeException) {
                                                    httpResponse.setResponseCode(3005);
                                                    str = "SSL失败";
                                                } else {
                                                    d.f("HttpUtils", "http post IOException error:" + e.getMessage());
                                                    k.a((Closeable) inputStream2);
                                                    k.a((Closeable) context);
                                                    k.a((Closeable) url);
                                                    if (httpURLConnection4 != null) {
                                                        httpURLConnection4.disconnect();
                                                    }
                                                }
                                                return httpResponse;
                                            }
                                            httpResponse.setResponseCode(3003);
                                            str = "域名无效";
                                        }
                                        httpResponse.setResponseBody(str);
                                        d.f("HttpUtils", "http post IOException error:" + e.getMessage());
                                        k.a((Closeable) inputStream2);
                                        k.a((Closeable) context);
                                        k.a((Closeable) url);
                                        if (httpURLConnection4 != null) {
                                            httpURLConnection4.disconnect();
                                        }
                                        return httpResponse;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        z = httpURLConnection4;
                                        inputStream6 = inputStream2;
                                        k.a((Closeable) inputStream6);
                                        k.a((Closeable) context);
                                        k.a((Closeable) url);
                                        if (z != 0) {
                                            z.disconnect();
                                        }
                                        throw th;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    httpURLConnection = httpURLConnectionWithProxy;
                                    inputStream = null;
                                    outputStream = outputStream5;
                                    d.f("HttpUtils", "http post Exception error:" + th);
                                    httpResponse.setResponseCode(3006);
                                    httpResponse.setResponseBody("UNKnow execption" + th.getMessage());
                                    k.a((Closeable) inputStream6);
                                    k.a((Closeable) inputStream);
                                    k.a(outputStream);
                                    outputStream3 = outputStream;
                                    inputStream4 = inputStream;
                                    httpURLConnection3 = httpURLConnection;
                                    url = outputStream;
                                    context = inputStream;
                                    z = httpURLConnection;
                                    if (httpURLConnection != null) {
                                        httpURLConnection3.disconnect();
                                        url = outputStream3;
                                        context = inputStream4;
                                        z = httpURLConnection3;
                                    }
                                    return httpResponse;
                                }
                            } else {
                                outputStream5 = null;
                            }
                            int responseCode = httpURLConnectionWithProxy.getResponseCode();
                            httpResponse.setResponseCode(responseCode);
                            try {
                                inputStream2 = httpURLConnectionWithProxy.getInputStream();
                            } catch (Throwable th4) {
                                d.b("HttpUtils", "get input stream error:" + th4.getMessage());
                                inputStream2 = null;
                            }
                            try {
                                if (httpRequest.isHaveRspData() || inputStream2 == null) {
                                    inputStream5 = null;
                                } else {
                                    try {
                                        inputStream5 = readInputStream(inputStream2);
                                        if (inputStream5 != null) {
                                            try {
                                                if (httpRequest.isRspDatazip()) {
                                                    inputStream5 = k.b(inputStream5);
                                                }
                                            } catch (Throwable unused) {
                                            }
                                        }
                                    } catch (Throwable unused2) {
                                        inputStream5 = null;
                                    }
                                }
                                if (inputStream5 == null && responseCode != 200 && httpRequest.isNeedErrorInput()) {
                                    errorStream = httpURLConnectionWithProxy.getErrorStream();
                                    inputStream5 = readInputStream(errorStream);
                                }
                                if (inputStream5 != null) {
                                    httpResponse.setResponseBody(new String(inputStream5, "UTF-8"));
                                }
                                if (httpURLConnectionWithProxy != null) {
                                    httpResponse.setResponseCode(httpURLConnectionWithProxy.getResponseCode());
                                    httpResponse.setResponseHeader("expires", httpURLConnectionWithProxy.getHeaderField("Expires"));
                                    httpResponse.setResponseHeader("cache-control", httpURLConnectionWithProxy.getHeaderField("Cache-Control"));
                                }
                                k.a((Closeable) inputStream2);
                                k.a((Closeable) errorStream);
                                k.a(outputStream5);
                                if (httpURLConnectionWithProxy != null) {
                                    httpURLConnectionWithProxy.disconnect();
                                }
                            } catch (MalformedURLException e3) {
                                e = e3;
                                httpURLConnection2 = httpURLConnectionWithProxy;
                                inputStream3 = errorStream;
                                inputStream6 = inputStream2;
                                outputStream2 = outputStream5;
                                d.f("HttpUtils", "http post  error:" + e.getMessage());
                                httpResponse.setResponseCode(3004);
                                httpResponse.setResponseBody("MalformedURLException");
                                k.a((Closeable) inputStream6);
                                k.a((Closeable) inputStream3);
                                k.a(outputStream2);
                                outputStream3 = outputStream2;
                                inputStream4 = inputStream3;
                                httpURLConnection3 = httpURLConnection2;
                                url = outputStream2;
                                context = inputStream3;
                                z = httpURLConnection2;
                                if (httpURLConnection2 != null) {
                                    httpURLConnection3.disconnect();
                                    url = outputStream3;
                                    context = inputStream4;
                                    z = httpURLConnection3;
                                }
                            } catch (IOException e4) {
                                e = e4;
                                InputStream inputStream7 = errorStream;
                                httpURLConnection4 = httpURLConnectionWithProxy;
                                context = inputStream7;
                                url = outputStream5;
                                httpResponse.setResponseCode(2998);
                                httpResponse.setResponseBody("网络错误");
                                if (e instanceof SocketTimeoutException) {
                                    httpResponse.setResponseCode(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_REALCITY);
                                    str = "请求超时";
                                } else if (e instanceof UnknownHostException) {
                                    httpResponse.setResponseCode(3003);
                                    str = "域名无效";
                                } else if (e instanceof SSLHandshakeException) {
                                    httpResponse.setResponseCode(3005);
                                    str = "SSL失败";
                                } else {
                                    d.f("HttpUtils", "http post IOException error:" + e.getMessage());
                                    k.a((Closeable) inputStream2);
                                    k.a((Closeable) context);
                                    k.a((Closeable) url);
                                    if (httpURLConnection4 != null) {
                                        httpURLConnection4.disconnect();
                                    }
                                }
                                httpResponse.setResponseBody(str);
                                d.f("HttpUtils", "http post IOException error:" + e.getMessage());
                                k.a((Closeable) inputStream2);
                                k.a((Closeable) context);
                                k.a((Closeable) url);
                                if (httpURLConnection4 != null) {
                                    httpURLConnection4.disconnect();
                                }
                            } catch (Throwable th5) {
                                th = th5;
                                httpURLConnection = httpURLConnectionWithProxy;
                                inputStream = errorStream;
                                inputStream6 = inputStream2;
                                outputStream = outputStream5;
                                d.f("HttpUtils", "http post Exception error:" + th);
                                httpResponse.setResponseCode(3006);
                                httpResponse.setResponseBody("UNKnow execption" + th.getMessage());
                                k.a((Closeable) inputStream6);
                                k.a((Closeable) inputStream);
                                k.a(outputStream);
                                outputStream3 = outputStream;
                                inputStream4 = inputStream;
                                httpURLConnection3 = httpURLConnection;
                                url = outputStream;
                                context = inputStream;
                                z = httpURLConnection;
                                if (httpURLConnection != null) {
                                    httpURLConnection3.disconnect();
                                    url = outputStream3;
                                    context = inputStream4;
                                    z = httpURLConnection3;
                                }
                            }
                        } else {
                            outputStream5 = null;
                            int responseCode2 = httpURLConnectionWithProxy.getResponseCode();
                            httpResponse.setResponseCode(responseCode2);
                            inputStream2 = httpURLConnectionWithProxy.getInputStream();
                            if (httpRequest.isHaveRspData()) {
                                inputStream5 = null;
                            } else {
                                inputStream5 = null;
                            }
                            if (inputStream5 == null) {
                                errorStream = httpURLConnectionWithProxy.getErrorStream();
                                inputStream5 = readInputStream(errorStream);
                            }
                            if (inputStream5 != null) {
                                httpResponse.setResponseBody(new String(inputStream5, "UTF-8"));
                            }
                            if (httpURLConnectionWithProxy != null) {
                                httpResponse.setResponseCode(httpURLConnectionWithProxy.getResponseCode());
                                httpResponse.setResponseHeader("expires", httpURLConnectionWithProxy.getHeaderField("Expires"));
                                httpResponse.setResponseHeader("cache-control", httpURLConnectionWithProxy.getHeaderField("Cache-Control"));
                            }
                            k.a((Closeable) inputStream2);
                            k.a((Closeable) errorStream);
                            k.a(outputStream5);
                            if (httpURLConnectionWithProxy != null) {
                                httpURLConnectionWithProxy.disconnect();
                            }
                        }
                    } else if (z != 0) {
                        httpURLConnectionWithProxy.setRequestMethod("POST");
                        httpURLConnectionWithProxy.setDoOutput(true);
                        httpURLConnectionWithProxy.setDoInput(true);
                        paras = httpRequest.getParas();
                        if (paras != null) {
                            outputStream5 = httpURLConnectionWithProxy.getOutputStream();
                            outputStream5.write(paras);
                            outputStream5 = outputStream5;
                        } else {
                            outputStream5 = null;
                        }
                        int responseCode3 = httpURLConnectionWithProxy.getResponseCode();
                        httpResponse.setResponseCode(responseCode3);
                        inputStream2 = httpURLConnectionWithProxy.getInputStream();
                        if (httpRequest.isHaveRspData()) {
                            inputStream5 = null;
                        } else {
                            inputStream5 = null;
                        }
                        if (inputStream5 == null) {
                            errorStream = httpURLConnectionWithProxy.getErrorStream();
                            inputStream5 = readInputStream(errorStream);
                        }
                        if (inputStream5 != null) {
                            httpResponse.setResponseBody(new String(inputStream5, "UTF-8"));
                        }
                        if (httpURLConnectionWithProxy != null) {
                            httpResponse.setResponseCode(httpURLConnectionWithProxy.getResponseCode());
                            httpResponse.setResponseHeader("expires", httpURLConnectionWithProxy.getHeaderField("Expires"));
                            httpResponse.setResponseHeader("cache-control", httpURLConnectionWithProxy.getHeaderField("Cache-Control"));
                        }
                        k.a((Closeable) inputStream2);
                        k.a((Closeable) errorStream);
                        k.a(outputStream5);
                        if (httpURLConnectionWithProxy != null) {
                            httpURLConnectionWithProxy.disconnect();
                        }
                    } else {
                        outputStream5 = null;
                        int responseCode4 = httpURLConnectionWithProxy.getResponseCode();
                        httpResponse.setResponseCode(responseCode4);
                        inputStream2 = httpURLConnectionWithProxy.getInputStream();
                        if (httpRequest.isHaveRspData()) {
                            inputStream5 = null;
                        } else {
                            inputStream5 = null;
                        }
                        if (inputStream5 == null) {
                            errorStream = httpURLConnectionWithProxy.getErrorStream();
                            inputStream5 = readInputStream(errorStream);
                        }
                        if (inputStream5 != null) {
                            httpResponse.setResponseBody(new String(inputStream5, "UTF-8"));
                        }
                        if (httpURLConnectionWithProxy != null) {
                            httpResponse.setResponseCode(httpURLConnectionWithProxy.getResponseCode());
                            httpResponse.setResponseHeader("expires", httpURLConnectionWithProxy.getHeaderField("Expires"));
                            httpResponse.setResponseHeader("cache-control", httpURLConnectionWithProxy.getHeaderField("Cache-Control"));
                        }
                        k.a((Closeable) inputStream2);
                        k.a((Closeable) errorStream);
                        k.a(outputStream5);
                        if (httpURLConnectionWithProxy != null) {
                            httpURLConnectionWithProxy.disconnect();
                        }
                    }
                } catch (MalformedURLException e5) {
                    e = e5;
                    httpURLConnection2 = httpURLConnectionWithProxy;
                    inputStream3 = null;
                    outputStream2 = null;
                    d.f("HttpUtils", "http post  error:" + e.getMessage());
                    httpResponse.setResponseCode(3004);
                    httpResponse.setResponseBody("MalformedURLException");
                    k.a((Closeable) inputStream6);
                    k.a((Closeable) inputStream3);
                    k.a(outputStream2);
                    outputStream3 = outputStream2;
                    inputStream4 = inputStream3;
                    httpURLConnection3 = httpURLConnection2;
                    url = outputStream2;
                    context = inputStream3;
                    z = httpURLConnection2;
                    if (httpURLConnection2 != null) {
                        httpURLConnection3.disconnect();
                        url = outputStream3;
                        context = inputStream4;
                        z = httpURLConnection3;
                    }
                    return httpResponse;
                } catch (IOException e6) {
                    e = e6;
                    outputStream4 = null;
                    inputStream2 = null;
                    httpURLConnection4 = httpURLConnectionWithProxy;
                    context = inputStream2;
                    url = outputStream4;
                    httpResponse.setResponseCode(2998);
                    httpResponse.setResponseBody("网络错误");
                    if (e instanceof SocketTimeoutException) {
                        httpResponse.setResponseCode(GLMapStaticValue.AM_PARAMETERNAME_PROCESS_REALCITY);
                        str = "请求超时";
                    } else {
                        if (e instanceof UnknownHostException) {
                            if (e instanceof SSLHandshakeException) {
                                httpResponse.setResponseCode(3005);
                                str = "SSL失败";
                            } else {
                                d.f("HttpUtils", "http post IOException error:" + e.getMessage());
                                k.a((Closeable) inputStream2);
                                k.a((Closeable) context);
                                k.a((Closeable) url);
                                if (httpURLConnection4 != null) {
                                    httpURLConnection4.disconnect();
                                }
                            }
                            return httpResponse;
                        }
                        httpResponse.setResponseCode(3003);
                        str = "域名无效";
                    }
                    httpResponse.setResponseBody(str);
                    d.f("HttpUtils", "http post IOException error:" + e.getMessage());
                    k.a((Closeable) inputStream2);
                    k.a((Closeable) context);
                    k.a((Closeable) url);
                    if (httpURLConnection4 != null) {
                        httpURLConnection4.disconnect();
                    }
                    return httpResponse;
                } catch (Throwable th6) {
                    th = th6;
                    httpURLConnection = httpURLConnectionWithProxy;
                    inputStream = null;
                    outputStream = null;
                    d.f("HttpUtils", "http post Exception error:" + th);
                    httpResponse.setResponseCode(3006);
                    httpResponse.setResponseBody("UNKnow execption" + th.getMessage());
                    k.a((Closeable) inputStream6);
                    k.a((Closeable) inputStream);
                    k.a(outputStream);
                    outputStream3 = outputStream;
                    inputStream4 = inputStream;
                    httpURLConnection3 = httpURLConnection;
                    url = outputStream;
                    context = inputStream;
                    z = httpURLConnection;
                    if (httpURLConnection != null) {
                        httpURLConnection3.disconnect();
                        url = outputStream3;
                        context = inputStream4;
                        z = httpURLConnection3;
                    }
                    return httpResponse;
                }
            } catch (Throwable th7) {
                th = th7;
                k.a((Closeable) inputStream6);
                k.a((Closeable) context);
                k.a((Closeable) url);
                if (z != 0) {
                    z.disconnect();
                }
                throw th;
            }
        } catch (MalformedURLException e7) {
            e = e7;
            inputStream3 = null;
            httpURLConnection2 = null;
            outputStream2 = null;
        } catch (IOException e8) {
            e = e8;
            context = 0;
            url = 0;
            inputStream2 = null;
        } catch (Throwable th8) {
            th = th8;
            inputStream = null;
            httpURLConnection = null;
            outputStream = null;
        }
        return httpResponse;
    }

    public static String appendParaToUrl(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        if (str.contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        return sb.append(str2).append("=").append(str3).toString();
    }

    public static HttpURLConnection getHttpURLConnectionWithProxy(Context context, String str) {
        NetworkInfo activeNetworkInfo;
        String extraInfo;
        URL url = new URL(str);
        if (context != null) {
            try {
                if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0 && (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) != null && activeNetworkInfo.getType() != 1 && (extraInfo = activeNetworkInfo.getExtraInfo()) != null && (extraInfo.equals("cmwap") || extraInfo.equals("3gwap") || extraInfo.equals("uniwap"))) {
                    return (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80)));
                }
            } catch (Throwable unused) {
            }
        }
        return (HttpURLConnection) url.openConnection();
    }

    public static String getUrlWithParas(String str, Map<String, String> map) {
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(str);
        String strJoinParas = joinParas(map);
        if (!TextUtils.isEmpty(strJoinParas)) {
            sb.append("?").append(strJoinParas);
        }
        return sb.toString();
    }

    public static String getUrlWithValueEncodeParas(String str, Map<String, String> map) {
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(str);
        String strJoinParasWithEncodedValue = joinParasWithEncodedValue(map);
        if (!TextUtils.isEmpty(strJoinParasWithEncodedValue)) {
            sb.append("?").append(strJoinParasWithEncodedValue);
        }
        return sb.toString();
    }

    public static HttpResponse httpGet(Context context, HttpRequest httpRequest) {
        return a(context, httpRequest, false);
    }

    public static HttpResponse httpGet(Context context, String str) {
        return httpGet(context, new HttpRequest(str));
    }

    public static void httpGet(Context context, HttpRequest httpRequest, HttpListener httpListener) {
        new a(context, httpListener).execute(httpRequest);
    }

    public static void httpGet(Context context, String str, HttpListener httpListener) {
        new b(httpListener, context).execute(str);
    }

    public static String httpGetString(Context context, HttpRequest httpRequest) {
        HttpResponse httpResponseHttpGet = httpGet(context, httpRequest);
        if (httpResponseHttpGet == null) {
            return null;
        }
        return httpResponseHttpGet.getResponseBody();
    }

    public static String httpGetString(Context context, String str) {
        HttpResponse httpResponseHttpGet = httpGet(context, new HttpRequest(str));
        if (httpResponseHttpGet == null) {
            return null;
        }
        return httpResponseHttpGet.getResponseBody();
    }

    public static HttpResponse httpPost(Context context, HttpRequest httpRequest) {
        return a(context, httpRequest, true);
    }

    public static HttpResponse httpPost(Context context, String str) {
        return httpPost(context, new HttpRequest(str));
    }

    public static String httpPostString(Context context, String str) {
        HttpResponse httpResponseHttpPost = httpPost(context, new HttpRequest(str));
        if (httpResponseHttpPost == null) {
            return null;
        }
        return httpResponseHttpPost.getResponseBody();
    }

    public static String httpPostString(Context context, String str, Map<String, String> map) {
        HttpResponse httpResponseHttpPost = httpPost(context, new HttpRequest(str, map));
        if (httpResponseHttpPost == null) {
            return null;
        }
        return httpResponseHttpPost.getResponseBody();
    }

    public static String joinParas(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> next = it.next();
            sb.append(next.getKey()).append("=").append(next.getValue());
            if (it.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    public static String joinParasWithEncodedValue(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("");
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                try {
                    Map.Entry<String, String> next = it.next();
                    sb.append(next.getKey()).append("=").append(URLEncoder.encode(next.getValue(), "UTF-8"));
                    if (it.hasNext()) {
                        sb.append("&");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static long parseGmtTime(String str) {
        try {
            return c.a("EEE, d MMM yyyy HH:mm:ss z").parse(str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public static byte[] readInputStream(InputStream inputStream) {
        return k.a(inputStream);
    }

    public static void setURLConnection(Map<String, String> map, HttpURLConnection httpURLConnection) {
        if (map == null || map.size() == 0 || httpURLConnection == null) {
            return;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!TextUtils.isEmpty(entry.getKey())) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
