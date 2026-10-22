package com.amap.api.mapcore.util;

import android.content.Context;
import com.qiniu.android.common.Constants;
import com.qiniu.android.http.Client;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: compiled from: BasicLBSRestHandler.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class eq<T, V> extends ep<T, V> {
    @Override // com.amap.api.mapcore.util.ep, com.amap.api.mapcore.util.hc
    public Map<String, String> b() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ep
    protected V e() {
        return null;
    }

    protected abstract String f();

    public eq(Context context, T t) {
        super(context, t);
    }

    @Override // com.amap.api.mapcore.util.hc
    public byte[] g() {
        try {
            return f().getBytes(Constants.UTF_8);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.ep, com.amap.api.mapcore.util.hc
    public Map<String, String> a() {
        HashMap map = new HashMap();
        map.put(Client.ContentTypeHeader, " application/json");
        map.put("Accept-Encoding", "gzip");
        map.put("User-Agent", "AMAP SDK Android Trace 5.4.0");
        map.put("X-INFO", ez.b(this.d));
        map.put("platinfo", String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s", "5.4.0", "trace"));
        map.put("logversion", "2.1");
        return map;
    }

    @Override // com.amap.api.mapcore.util.hc
    public String c() {
        String str = "key=" + ew.f(this.d);
        String strA = ez.a();
        return "http://restapi.amap.com/v3/grasproad?" + str + ("&ts=" + strA) + ("&scode=" + ez.a(this.d, strA, str));
    }
}
