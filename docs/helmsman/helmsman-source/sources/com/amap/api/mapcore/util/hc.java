package com.amap.api.mapcore.util;

import android.text.TextUtils;
import java.net.Proxy;
import java.util.Map;

/* JADX INFO: compiled from: Request.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class hc {
    int f = 20000;
    int g = 20000;
    Proxy h = null;

    public abstract Map<String, String> a();

    public abstract Map<String, String> b();

    public abstract String c();

    public byte[] g() {
        return null;
    }

    String m() {
        byte[] bArrG = g();
        if (bArrG == null || bArrG.length == 0) {
            return c();
        }
        Map<String, String> mapB = b();
        if (mapB == null) {
            return c();
        }
        String strA = gz.a(mapB);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(c()).append("?").append(strA);
        return stringBuffer.toString();
    }

    byte[] n() {
        byte[] bArrG = g();
        if (bArrG != null && bArrG.length != 0) {
            return bArrG;
        }
        String strA = gz.a(b());
        return !TextUtils.isEmpty(strA) ? fg.a(strA) : bArrG;
    }

    public final void a(int i) {
        this.f = i;
    }

    public final void b(int i) {
        this.g = i;
    }

    public final void a(Proxy proxy) {
        this.h = proxy;
    }
}
