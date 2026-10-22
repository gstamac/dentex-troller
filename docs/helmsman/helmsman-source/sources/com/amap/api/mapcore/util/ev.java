package com.amap.api.mapcore.util;

import com.amap.api.maps.AMapException;
import com.facebook.internal.NativeProtocol;

/* JADX INFO: compiled from: AMapCoreException.java */
/* JADX INFO: loaded from: classes.dex */
public class ev extends Exception {
    private String a;
    private String b;
    private String c;
    private String d;
    private int e;

    public ev(String str) {
        super(str);
        this.b = "";
        this.c = "1900";
        this.d = NativeProtocol.ERROR_UNKNOWN_ERROR;
        this.e = -1;
        this.a = str;
        a(str);
    }

    public ev(String str, String str2) {
        this(str);
        this.b = str2;
    }

    public String a() {
        return this.a;
    }

    public String b() {
        return this.c;
    }

    public String c() {
        return this.d;
    }

    public String d() {
        return this.b;
    }

    public int e() {
        return this.e;
    }

    private void a(String str) {
        if (AMapException.ERROR_IO.equals(str)) {
            this.e = 21;
            this.c = "1902";
            this.d = "IOException";
            return;
        }
        if (AMapException.ERROR_SOCKET.equals(str)) {
            this.e = 22;
            return;
        }
        if (AMapException.ERROR_SOCKE_TIME_OUT.equals(str)) {
            this.e = 23;
            this.c = "1802";
            this.d = "SocketTimeoutException";
            return;
        }
        if (AMapException.ERROR_INVALID_PARAMETER.equals(str)) {
            this.e = 24;
            this.c = "1901";
            this.d = "IllegalArgumentException";
            return;
        }
        if (AMapException.ERROR_NULL_PARAMETER.equals(str)) {
            this.e = 25;
            this.c = "1903";
            this.d = "NullPointException";
            return;
        }
        if (AMapException.ERROR_URL.equals(str)) {
            this.e = 26;
            this.c = "1803";
            this.d = "MalformedURLException";
            return;
        }
        if (AMapException.ERROR_UNKNOW_HOST.equals(str)) {
            this.e = 27;
            this.c = "1804";
            this.d = "UnknownHostException";
            return;
        }
        if (AMapException.ERROR_UNKNOW_SERVICE.equals(str)) {
            this.e = 28;
            this.c = "1805";
            this.d = "CannotConnectToHostException";
            return;
        }
        if (AMapException.ERROR_PROTOCOL.equals(str)) {
            this.e = 29;
            this.c = "1801";
            this.d = "ProtocolException";
            return;
        }
        if (AMapException.ERROR_CONNECTION.equals(str)) {
            this.e = 30;
            this.c = "1806";
            this.d = "ConnectionException";
            return;
        }
        if (AMapException.ERROR_UNKNOWN.equals(str)) {
            this.e = 31;
            return;
        }
        if (AMapException.ERROR_FAILURE_AUTH.equals(str)) {
            this.e = 32;
            return;
        }
        if ("requeust is null".equals(str)) {
            this.e = 1;
            return;
        }
        if ("request url is empty".equals(str)) {
            this.e = 2;
            return;
        }
        if ("response is null".equals(str)) {
            this.e = 3;
            return;
        }
        if ("thread pool has exception".equals(str)) {
            this.e = 4;
            return;
        }
        if ("sdk name is invalid".equals(str)) {
            this.e = 5;
            return;
        }
        if ("sdk info is null".equals(str)) {
            this.e = 6;
            return;
        }
        if ("sdk packages is null".equals(str)) {
            this.e = 7;
            return;
        }
        if ("线程池为空".equals(str)) {
            this.e = 8;
        } else if ("获取对象错误".equals(str)) {
            this.e = 101;
        } else {
            this.e = -1;
        }
    }
}
