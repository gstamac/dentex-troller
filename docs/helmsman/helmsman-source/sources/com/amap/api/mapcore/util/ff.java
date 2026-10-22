package com.amap.api.mapcore.util;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: SDKInfo.java */
/* JADX INFO: loaded from: classes.dex */
@fu(a = "a")
public class ff {

    @fv(a = "a1", b = 6)
    private String a;

    @fv(a = "a2", b = 6)
    private String b;

    @fv(a = "a6", b = 2)
    private int c;

    @fv(a = "a3", b = 6)
    private String d;

    @fv(a = "a4", b = 6)
    private String e;

    @fv(a = "a5", b = 6)
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String[] l;

    public static String h() {
        return "a6=1";
    }

    private ff() {
        this.c = 1;
        this.l = null;
    }

    private ff(a aVar) {
        this.c = 1;
        this.l = null;
        this.g = aVar.a;
        this.h = aVar.b;
        this.j = aVar.c;
        this.i = aVar.d;
        this.c = aVar.e ? 1 : 0;
        this.k = aVar.f;
        this.l = aVar.g;
        this.b = fg.b(this.h);
        this.a = fg.b(this.j);
        this.d = fg.b(this.i);
        this.e = fg.b(a(this.l));
        this.f = fg.b(this.k);
    }

    /* JADX INFO: compiled from: SDKInfo.java */
    public static class a {
        private String a;
        private String b;
        private String c;
        private String d;
        private boolean e = true;
        private String f = "standard";
        private String[] g = null;

        public a(String str, String str2, String str3) {
            this.a = str2;
            this.b = str2;
            this.d = str3;
            this.c = str;
        }

        public a a(String[] strArr) {
            this.g = (String[]) strArr.clone();
            return this;
        }

        public a a(String str) {
            this.b = str;
            return this;
        }

        public ff a() throws ev {
            if (this.g == null) {
                throw new ev("sdk packages is null");
            }
            return new ff(this);
        }
    }

    public void a(boolean z) {
        this.c = z ? 1 : 0;
    }

    public String a() {
        if (TextUtils.isEmpty(this.j) && !TextUtils.isEmpty(this.a)) {
            this.j = fg.c(this.a);
        }
        return this.j;
    }

    public String b() {
        return this.g;
    }

    public String c() {
        if (TextUtils.isEmpty(this.h) && !TextUtils.isEmpty(this.b)) {
            this.h = fg.c(this.b);
        }
        return this.h;
    }

    public String d() {
        if (TextUtils.isEmpty(this.i) && !TextUtils.isEmpty(this.d)) {
            this.i = fg.c(this.d);
        }
        return this.i;
    }

    public String e() {
        if (TextUtils.isEmpty(this.k) && !TextUtils.isEmpty(this.f)) {
            this.k = fg.c(this.f);
        }
        if (TextUtils.isEmpty(this.k)) {
            this.k = "standard";
        }
        return this.k;
    }

    public boolean f() {
        return this.c == 1;
    }

    public String[] g() {
        String[] strArr = this.l;
        if ((strArr == null || strArr.length == 0) && !TextUtils.isEmpty(this.e)) {
            this.l = b(fg.c(this.e));
        }
        return (String[]) this.l.clone();
    }

    private String[] b(String str) {
        try {
            return str.split(";");
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private String a(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                sb.append(str).append(";");
            }
            return sb.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static String a(String str) {
        HashMap map = new HashMap();
        map.put("a1", fg.b(str));
        return ft.a((Map<String, String>) map);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return getClass() == obj.getClass() && hashCode() == ((ff) obj).hashCode();
    }

    public int hashCode() {
        fj fjVar = new fj();
        fjVar.a(this.j).a(this.g).a(this.h).a((Object[]) this.l);
        return fjVar.a();
    }
}
