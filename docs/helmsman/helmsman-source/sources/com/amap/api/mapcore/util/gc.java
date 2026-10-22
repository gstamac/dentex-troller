package com.amap.api.mapcore.util;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: LogInfo.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class gc {

    @fv(a = "b1", b = 6)
    protected String b;

    @fv(a = "a1", b = 6)
    private String d;

    @fv(a = "b2", b = 2)
    protected int a = -1;

    @fv(a = "b3", b = 2)
    protected int c = 1;

    public int a() {
        return this.a;
    }

    public void a(int i) {
        this.a = i;
    }

    public String b() {
        return this.b;
    }

    public void a(String str) {
        this.b = str;
    }

    public void b(String str) {
        this.d = fg.b(str);
    }

    public int c() {
        return this.c;
    }

    public void b(int i) {
        this.c = i;
    }

    public static String c(String str) {
        HashMap map = new HashMap();
        map.put("b1", str);
        return ft.a((Map<String, String>) map);
    }

    public static String c(int i) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("b2").append("=").append(i);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return sb.toString();
    }
}
