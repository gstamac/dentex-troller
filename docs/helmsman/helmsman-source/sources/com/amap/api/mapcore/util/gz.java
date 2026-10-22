package com.amap.api.mapcore.util;

import android.os.Build;
import com.amap.api.maps.AMapException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.PushbackInputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

/* JADX INFO: compiled from: HttpUrlUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class gz {
    private static ha a;
    private int b;
    private int c;
    private boolean d;
    private SSLContext e;
    private Proxy f;
    private volatile boolean g;
    private long h;
    private long i;
    private String j;
    private gw.a k;
    private HostnameVerifier l;

    private void b() {
        try {
            this.j = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        } catch (Throwable th) {
            fk.a(th, "HttpUrlUtil", "initCSID");
        }
    }

    public static void a(ha haVar) {
        a = haVar;
    }

    gz(int i, int i2, Proxy proxy, boolean z) {
        this(i, i2, proxy, z, null);
    }

    gz(int i, int i2, Proxy proxy, boolean z, gw.a aVar) {
        this.g = false;
        this.h = -1L;
        this.i = 0L;
        this.l = new HostnameVerifier() { // from class: com.amap.api.mapcore.util.gz.1
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str, SSLSession sSLSession) {
                HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
                return defaultHostnameVerifier.verify("*.amap.com", sSLSession) || defaultHostnameVerifier.verify("*.apilocate.amap.com", sSLSession);
            }
        };
        this.b = i;
        this.c = i2;
        this.f = proxy;
        this.d = z;
        this.k = aVar;
        b();
        if (z) {
            try {
                SSLContext sSLContext = SSLContext.getInstance("TLS");
                sSLContext.init(null, null, null);
                this.e = sSLContext;
            } catch (Throwable th) {
                fk.a(th, "HttpUtil", "HttpUtil");
            }
        }
    }

    gz(int i, int i2, Proxy proxy) {
        this(i, i2, proxy, false);
    }

    void a() {
        this.g = true;
    }

    void a(long j) {
        this.i = j;
    }

    void b(long j) {
        this.h = j;
    }

    void a(String str, Map<String, String> map, Map<String, String> map2, gy.a aVar) {
        HttpURLConnection httpURLConnection;
        int i;
        if (aVar == null) {
            return;
        }
        InputStream inputStream = null;
        try {
            try {
                String strA = a(map2);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                if (strA != null) {
                    stringBuffer.append("?").append(strA);
                }
                HttpURLConnection httpURLConnectionA = a(stringBuffer.toString(), map, false);
                try {
                    httpURLConnectionA.setRequestProperty("RANGE", "bytes=" + this.i + "-");
                    httpURLConnectionA.connect();
                    int responseCode = httpURLConnectionA.getResponseCode();
                    boolean z = true;
                    boolean z2 = responseCode != 200;
                    if (responseCode == 206) {
                        z = false;
                    }
                    if (z2 & z) {
                        aVar.a(new ev("网络异常原因：" + httpURLConnectionA.getResponseMessage() + " 网络异常状态码：" + responseCode));
                    }
                    InputStream inputStream2 = httpURLConnectionA.getInputStream();
                    byte[] bArr = new byte[1024];
                    while (!Thread.interrupted() && !this.g && (i = inputStream2.read(bArr, 0, 1024)) > 0) {
                        long j = this.h;
                        if (j != -1 && this.i >= j) {
                            break;
                        }
                        if (i == 1024) {
                            aVar.a(bArr, this.i);
                        } else {
                            byte[] bArr2 = new byte[i];
                            System.arraycopy(bArr, 0, bArr2, 0, i);
                            aVar.a(bArr2, this.i);
                        }
                        this.i += (long) i;
                    }
                    if (this.g) {
                        aVar.d();
                    } else {
                        aVar.e();
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (IOException e) {
                            fk.a(e, "HttpUrlUtil", "makeDownloadGetRequest");
                        } catch (Throwable th) {
                            fk.a(th, "HttpUrlUtil", "makeDownloadGetRequest");
                        }
                    }
                    if (httpURLConnectionA != null) {
                        httpURLConnectionA.disconnect();
                    }
                } catch (Throwable th2) {
                    httpURLConnection = httpURLConnectionA;
                    th = th2;
                    try {
                        aVar.a(th);
                        if (0 != 0) {
                            try {
                                inputStream.close();
                            } catch (IOException e2) {
                                fk.a(e2, "HttpUrlUtil", "makeDownloadGetRequest");
                            } catch (Throwable th3) {
                                fk.a(th3, "HttpUrlUtil", "makeDownloadGetRequest");
                            }
                        }
                        if (httpURLConnection == null) {
                        }
                    } finally {
                        if (0 != 0) {
                            try {
                                inputStream.close();
                            } catch (IOException e3) {
                                fk.a(e3, "HttpUrlUtil", "makeDownloadGetRequest");
                            } catch (Throwable th4) {
                                fk.a(th4, "HttpUrlUtil", "makeDownloadGetRequest");
                            }
                        }
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Throwable th5) {
                                fk.a(th5, "HttpUrlUtil", "makeDownloadGetRequest");
                            }
                        }
                    }
                }
            } catch (Throwable th6) {
                th = th6;
                httpURLConnection = null;
            }
        } catch (Throwable th7) {
        }
    }

    he a(String str, Map<String, String> map, Map<String, String> map2) throws ev {
        HttpURLConnection httpURLConnectionA = null;
        try {
            try {
                try {
                    try {
                        try {
                            try {
                                try {
                                    try {
                                        String strA = a(map2);
                                        StringBuffer stringBuffer = new StringBuffer();
                                        stringBuffer.append(str);
                                        if (strA != null) {
                                            stringBuffer.append("?").append(strA);
                                        }
                                        httpURLConnectionA = a(stringBuffer.toString(), map, false);
                                        he heVarA = a(httpURLConnectionA);
                                        if (httpURLConnectionA != null) {
                                            try {
                                                httpURLConnectionA.disconnect();
                                            } catch (Throwable th) {
                                                fk.a(th, "HttpUrlUtil", "makeGetRequest");
                                            }
                                        }
                                        return heVarA;
                                    } catch (IOException unused) {
                                        throw new ev(AMapException.ERROR_IO);
                                    }
                                } catch (UnknownHostException unused2) {
                                    throw new ev(AMapException.ERROR_UNKNOW_HOST);
                                }
                            } catch (ev e) {
                                throw e;
                            }
                        } catch (ConnectException unused3) {
                            throw new ev(AMapException.ERROR_CONNECTION);
                        }
                    } catch (InterruptedIOException unused4) {
                        throw new ev(AMapException.ERROR_UNKNOWN);
                    }
                } catch (MalformedURLException unused5) {
                    throw new ev(AMapException.ERROR_URL);
                } catch (SocketException unused6) {
                    throw new ev(AMapException.ERROR_SOCKET);
                }
            } catch (SocketTimeoutException unused7) {
                throw new ev(AMapException.ERROR_SOCKE_TIME_OUT);
            } catch (Throwable th2) {
                th2.printStackTrace();
                throw new ev(AMapException.ERROR_UNKNOWN);
            }
        } catch (Throwable th3) {
            if (httpURLConnectionA != null) {
                try {
                    httpURLConnectionA.disconnect();
                } catch (Throwable th4) {
                    fk.a(th4, "HttpUrlUtil", "makeGetRequest");
                }
            }
            throw th3;
        }
    }

    he a(String str, Map<String, String> map, byte[] bArr) throws ev {
        HttpURLConnection httpURLConnectionA = null;
        try {
            try {
                try {
                    try {
                        try {
                            try {
                                try {
                                    httpURLConnectionA = a(str, map, true);
                                    if (bArr != null && bArr.length > 0) {
                                        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnectionA.getOutputStream());
                                        dataOutputStream.write(bArr);
                                        dataOutputStream.close();
                                    }
                                    he heVarA = a(httpURLConnectionA);
                                    if (httpURLConnectionA != null) {
                                        try {
                                            httpURLConnectionA.disconnect();
                                        } catch (Throwable th) {
                                            fk.a(th, "HttpUrlUtil", "makePostReqeust");
                                        }
                                    }
                                    return heVarA;
                                } catch (InterruptedIOException unused) {
                                    throw new ev(AMapException.ERROR_UNKNOWN);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new ev(AMapException.ERROR_IO);
                            }
                        } catch (Throwable th2) {
                            fk.a(th2, "HttpUrlUtil", "makePostReqeust");
                            throw new ev(AMapException.ERROR_UNKNOWN);
                        }
                    } catch (ev e2) {
                        fk.a(e2, "HttpUrlUtil", "makePostReqeust");
                        throw e2;
                    } catch (UnknownHostException e3) {
                        e3.printStackTrace();
                        throw new ev(AMapException.ERROR_UNKNOW_HOST);
                    }
                } catch (MalformedURLException e4) {
                    e4.printStackTrace();
                    throw new ev(AMapException.ERROR_URL);
                } catch (SocketTimeoutException e5) {
                    e5.printStackTrace();
                    throw new ev(AMapException.ERROR_SOCKE_TIME_OUT);
                }
            } catch (ConnectException e6) {
                e6.printStackTrace();
                throw new ev(AMapException.ERROR_CONNECTION);
            } catch (SocketException e7) {
                e7.printStackTrace();
                throw new ev(AMapException.ERROR_SOCKET);
            }
        } catch (Throwable th3) {
            if (httpURLConnectionA != null) {
                try {
                    httpURLConnectionA.disconnect();
                } catch (Throwable th4) {
                    fk.a(th4, "HttpUrlUtil", "makePostReqeust");
                }
            }
            throw th3;
        }
    }

    HttpURLConnection a(String str, Map<String, String> map, boolean z) throws IOException {
        HttpURLConnection httpURLConnection;
        fa.b();
        URL url = new URL(str);
        gw.a aVar = this.k;
        URLConnection uRLConnectionA = aVar != null ? aVar.a(this.f, url) : null;
        if (uRLConnectionA == null) {
            Proxy proxy = this.f;
            if (proxy != null) {
                uRLConnectionA = url.openConnection(proxy);
            } else {
                uRLConnectionA = url.openConnection();
            }
        }
        if (this.d) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) uRLConnectionA;
            httpsURLConnection.setSSLSocketFactory(this.e.getSocketFactory());
            httpsURLConnection.setHostnameVerifier(this.l);
            httpURLConnection = httpsURLConnection;
        } else {
            httpURLConnection = (HttpURLConnection) uRLConnectionA;
        }
        if (Build.VERSION.SDK != null) {
            httpURLConnection.setRequestProperty("Connection", "close");
        }
        a(map, httpURLConnection);
        if (z) {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
        } else {
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
        }
        return httpURLConnection;
    }

    /* JADX WARN: Code duplicated, block: B:103:0x011a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:113:? A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:89:0x0124 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:93:0x012e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:99:0x0138 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private he a(HttpURLConnection httpURLConnection) throws Throwable {
        InputStream inputStream;
        InputStream inputStream2;
        PushbackInputStream pushbackInputStream;
        InputStream inputStream3;
        InputStream inputStream4;
        List<String> list;
        String str = "";
        ByteArrayOutputStream byteArrayOutputStream = null;
        gZIPInputStream = null;
        InputStream gZIPInputStream = null;
        byteArrayOutputStream = null;
        try {
            httpURLConnection.connect();
            Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
            int responseCode = httpURLConnection.getResponseCode();
            if (headerFields != null && (list = headerFields.get("gsid")) != null && list.size() > 0) {
                str = list.get(0);
            }
            if (responseCode != 200) {
                throw new ev("网络异常原因：" + httpURLConnection.getResponseMessage() + " 网络异常状态码：" + responseCode + "  " + str + " " + this.j, str);
            }
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            try {
                inputStream3 = httpURLConnection.getInputStream();
                try {
                    pushbackInputStream = new PushbackInputStream(inputStream3, 2);
                    try {
                        byte[] bArr = new byte[2];
                        pushbackInputStream.read(bArr);
                        pushbackInputStream.unread(bArr);
                        gZIPInputStream = (bArr[0] == 31 && bArr[1] == -117) ? new GZIPInputStream(pushbackInputStream) : pushbackInputStream;
                        byte[] bArr2 = new byte[1024];
                        while (true) {
                            int i = gZIPInputStream.read(bArr2);
                            if (i == -1) {
                                break;
                            }
                            byteArrayOutputStream2.write(bArr2, 0, i);
                        }
                        ha haVar = a;
                        if (haVar != null) {
                            haVar.a();
                        }
                        he heVar = new he();
                        heVar.a = byteArrayOutputStream2.toByteArray();
                        heVar.b = headerFields;
                        heVar.c = this.j;
                        heVar.d = str;
                        try {
                            byteArrayOutputStream2.close();
                        } catch (Throwable th) {
                            fk.a(th, "HttpUrlUtil", "parseResult");
                        }
                        if (inputStream3 != null) {
                            try {
                                inputStream3.close();
                            } catch (Throwable th2) {
                                fk.a(th2, "HttpUrlUtil", "parseResult");
                            }
                        }
                        try {
                            pushbackInputStream.close();
                        } catch (Throwable th3) {
                            fk.a(th3, "HttpUrlUtil", "parseResult");
                        }
                        try {
                            gZIPInputStream.close();
                        } catch (Throwable th4) {
                            fk.a(th4, "HttpUrlUtil", "parseResult");
                        }
                        return heVar;
                    } catch (IOException unused) {
                        InputStream inputStream5 = gZIPInputStream;
                        byteArrayOutputStream = byteArrayOutputStream2;
                        inputStream4 = inputStream5;
                        try {
                            throw new ev(AMapException.ERROR_IO, str);
                        } catch (Throwable th5) {
                            InputStream inputStream6 = inputStream4;
                            inputStream = inputStream3;
                            th = th5;
                            inputStream2 = inputStream6;
                            if (byteArrayOutputStream != null) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Throwable th6) {
                                    fk.a(th6, "HttpUrlUtil", "parseResult");
                                }
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th7) {
                                    fk.a(th7, "HttpUrlUtil", "parseResult");
                                }
                            }
                            if (pushbackInputStream != null) {
                                try {
                                    pushbackInputStream.close();
                                } catch (Throwable th8) {
                                    fk.a(th8, "HttpUrlUtil", "parseResult");
                                }
                            }
                            if (inputStream2 != null) {
                                throw th;
                            }
                            try {
                                inputStream2.close();
                                throw th;
                            } catch (Throwable th9) {
                                fk.a(th9, "HttpUrlUtil", "parseResult");
                                throw th;
                            }
                        }
                    } catch (Throwable th10) {
                        inputStream = inputStream3;
                        th = th10;
                        inputStream2 = gZIPInputStream;
                        byteArrayOutputStream = byteArrayOutputStream2;
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (pushbackInputStream != null) {
                            pushbackInputStream.close();
                        }
                        if (inputStream2 != null) {
                            throw th;
                        }
                        inputStream2.close();
                        throw th;
                    }
                } catch (IOException unused2) {
                    pushbackInputStream = null;
                    byteArrayOutputStream = byteArrayOutputStream2;
                    inputStream4 = pushbackInputStream;
                    throw new ev(AMapException.ERROR_IO, str);
                } catch (Throwable th11) {
                    pushbackInputStream = null;
                    byteArrayOutputStream = byteArrayOutputStream2;
                    inputStream = inputStream3;
                    th = th11;
                    inputStream2 = null;
                }
            } catch (IOException unused3) {
                inputStream3 = null;
                pushbackInputStream = null;
            } catch (Throwable th12) {
                th = th12;
                inputStream2 = null;
                pushbackInputStream = null;
                byteArrayOutputStream = byteArrayOutputStream2;
                inputStream = null;
            }
        } catch (IOException unused4) {
            inputStream3 = null;
            inputStream4 = null;
            pushbackInputStream = null;
        } catch (Throwable th13) {
            th = th13;
            inputStream = null;
            inputStream2 = null;
            pushbackInputStream = null;
        }
    }

    private void a(Map<String, String> map, HttpURLConnection httpURLConnection) {
        if (map != null) {
            for (String str : map.keySet()) {
                httpURLConnection.addRequestProperty(str, map.get(str));
            }
        }
        try {
            httpURLConnection.addRequestProperty("csid", this.j);
        } catch (Throwable th) {
            fk.a(th, "HttpUrlUtil", "addHeaders");
        }
        httpURLConnection.setConnectTimeout(this.b);
        httpURLConnection.setReadTimeout(this.c);
    }

    static String a(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(key));
            sb.append("=");
            sb.append(URLEncoder.encode(value));
        }
        return sb.toString();
    }
}
