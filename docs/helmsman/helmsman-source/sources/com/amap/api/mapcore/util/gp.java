package com.amap.api.mapcore.util;

import com.facebook.internal.ServerProtocol;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: DynamicPlugin.java */
/* JADX INFO: loaded from: classes.dex */
@fu(a = "file")
public class gp {

    @fv(a = "fname", b = 6)
    private String a;

    @fv(a = "md", b = 6)
    private String b;

    @fv(a = "sname", b = 6)
    private String c;

    @fv(a = ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, b = 6)
    private String d;

    @fv(a = "dversion", b = 6)
    private String e;

    @fv(a = "status", b = 6)
    private String f;

    public gp(a aVar) {
        this.a = aVar.a;
        this.b = aVar.b;
        this.c = aVar.c;
        this.d = aVar.d;
        this.e = aVar.e;
        this.f = aVar.f;
    }

    private gp() {
    }

    public static String a(String str, String str2) {
        HashMap map = new HashMap();
        map.put("sname", str);
        map.put("dversion", str2);
        return ft.a((Map<String, String>) map);
    }

    public static String b(String str, String str2) {
        HashMap map = new HashMap();
        map.put("sname", str);
        map.put("status", str2);
        return ft.a((Map<String, String>) map);
    }

    public static String a(String str) {
        HashMap map = new HashMap();
        map.put("sname", str);
        return ft.a((Map<String, String>) map);
    }

    public static String b(String str) {
        HashMap map = new HashMap();
        map.put("fname", str);
        return ft.a((Map<String, String>) map);
    }

    public static String a(String str, String str2, String str3, String str4) {
        HashMap map = new HashMap();
        map.put("fname", str);
        map.put("sname", str2);
        map.put("dversion", str4);
        map.put(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, str3);
        return ft.a((Map<String, String>) map);
    }

    public String a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }

    public String d() {
        return this.d;
    }

    public String e() {
        return this.e;
    }

    public String f() {
        return this.f;
    }

    public void c(String str) {
        this.f = str;
    }

    /* JADX INFO: compiled from: DynamicPlugin.java */
    public static class a {
        private String a;
        private String b;
        private String c;
        private String d;
        private String e;
        private String f = "copy";

        public a(String str, String str2, String str3, String str4, String str5) {
            this.a = str;
            this.b = str2;
            this.c = str3;
            this.d = str4;
            this.e = str5;
        }

        public a a(String str) {
            this.f = str;
            return this;
        }

        public gp a() {
            return new gp(this);
        }
    }
}
