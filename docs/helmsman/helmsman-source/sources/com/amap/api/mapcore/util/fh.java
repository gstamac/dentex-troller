package com.amap.api.mapcore.util;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: AuthRequest.java */
/* JADX INFO: loaded from: classes.dex */
@Deprecated
class fh extends hc {
    private String b;
    private Map<String, String> a = new HashMap();
    private Map<String, String> c = new HashMap();

    fh() {
    }

    void a(Map<String, String> map) {
        this.a.clear();
        this.a.putAll(map);
    }

    void a(String str) {
        this.b = str;
    }

    void b(Map<String, String> map) {
        this.c.clear();
        this.c.putAll(map);
    }

    @Override // com.amap.api.mapcore.util.hc
    public String c() {
        return this.b;
    }

    @Override // com.amap.api.mapcore.util.hc
    public Map<String, String> a() {
        return this.a;
    }

    @Override // com.amap.api.mapcore.util.hc
    public Map<String, String> b() {
        return this.c;
    }
}
