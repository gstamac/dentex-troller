package com.amap.api.mapcore.util;

import com.qiniu.android.http.Client;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: LogUpdateRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class fm extends hc {
    private byte[] a;
    private String b;

    @Override // com.amap.api.mapcore.util.hc
    public Map<String, String> b() {
        return null;
    }

    public fm(byte[] bArr) {
        this.b = "1";
        this.a = (byte[]) bArr.clone();
    }

    public fm(byte[] bArr, String str) {
        this.b = "1";
        this.a = (byte[]) bArr.clone();
        this.b = str;
    }

    private String d() {
        byte[] bArrA = fg.a(fi.a);
        byte[] bArr = new byte[bArrA.length + 50];
        System.arraycopy(this.a, 0, bArr, 0, 50);
        System.arraycopy(bArrA, 0, bArr, 50, bArrA.length);
        return fc.a(bArr);
    }

    @Override // com.amap.api.mapcore.util.hc
    public Map<String, String> a() {
        HashMap map = new HashMap();
        map.put(Client.ContentTypeHeader, "application/zip");
        map.put("Content-Length", String.valueOf(this.a.length));
        return map;
    }

    @Override // com.amap.api.mapcore.util.hc
    public String c() {
        return String.format(fi.b, "1", this.b, "1", "open", d());
    }

    @Override // com.amap.api.mapcore.util.hc
    public byte[] g() {
        return this.a;
    }
}
