package cn.jiguang.net;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class HttpResponse {
    private String a;
    private String b;
    private Map<String, Object> c;
    private int d;
    private long e;
    private boolean f;
    private boolean g;
    private int h;

    public HttpResponse() {
        this.h = -1;
        this.c = new HashMap();
    }

    public HttpResponse(String str) {
        this.h = -1;
        this.a = str;
        this.d = 0;
        this.f = false;
        this.g = false;
        this.c = new HashMap();
    }

    private int a() {
        int iIndexOf;
        try {
            String str = (String) this.c.get("cache-control");
            if (TextUtils.isEmpty(str) || (iIndexOf = str.indexOf("max-age=")) == -1) {
                return -1;
            }
            int iIndexOf2 = str.indexOf(",", iIndexOf);
            int i = iIndexOf + 8;
            return Integer.parseInt(iIndexOf2 != -1 ? str.substring(i, iIndexOf2) : str.substring(i));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getExpiredTime() {
        long gmtTime;
        if (this.g) {
            return this.e;
        }
        this.g = true;
        int iA = a();
        if (iA != -1) {
            gmtTime = System.currentTimeMillis() + ((long) (iA * 1000));
        } else {
            gmtTime = !TextUtils.isEmpty(getExpiresHeader()) ? HttpUtils.parseGmtTime(getExpiresHeader()) : -1L;
        }
        this.e = gmtTime;
        return gmtTime;
    }

    public String getExpiresHeader() {
        try {
            Map<String, Object> map = this.c;
            if (map == null) {
                return null;
            }
            return (String) map.get("expires");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getResponseBody() {
        return this.b;
    }

    public int getResponseCode() {
        return this.h;
    }

    public int getType() {
        return this.d;
    }

    public String getUrl() {
        return this.a;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.e;
    }

    public boolean isInCache() {
        return this.f;
    }

    public void setExpiredTime(long j) {
        this.g = true;
        this.e = j;
    }

    public HttpResponse setInCache(boolean z) {
        this.f = z;
        return this;
    }

    public void setResponseBody(String str) {
        this.b = str;
    }

    public void setResponseCode(int i) {
        this.h = i;
    }

    public void setResponseHeader(String str, String str2) {
        Map<String, Object> map = this.c;
        if (map != null) {
            map.put(str, str2);
        }
    }

    public void setResponseHeaders(Map<String, Object> map) {
        this.c = map;
    }

    public void setType(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("The type of HttpResponse cannot be smaller than 0.");
        }
        this.d = i;
    }

    public void setUrl(String str) {
        this.a = str;
    }

    public String toString() {
        return "HttpResponse{responseBody='" + this.b + "', responseCode=" + this.h + '}';
    }
}
