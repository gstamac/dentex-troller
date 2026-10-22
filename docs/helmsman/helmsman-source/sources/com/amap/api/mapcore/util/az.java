package com.amap.api.mapcore.util;

import com.facebook.internal.ServerProtocol;

/* JADX INFO: compiled from: DTInfo.java */
/* JADX INFO: loaded from: classes.dex */
@fu(a = "update_item")
public class az {

    @fv(a = "localPath", b = 6)
    protected String h;

    @fv(a = "mCompleteCode", b = 2)
    protected int j;

    @fv(a = "mState", b = 2)
    public int l;

    @fv(a = "title", b = 6)
    protected String a = null;

    @fv(a = "url", b = 6)
    protected String b = null;

    @fv(a = "mAdcode", b = 6)
    protected String c = null;

    @fv(a = "fileName", b = 6)
    protected String d = null;

    @fv(a = ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION, b = 6)
    protected String e = "";

    @fv(a = "lLocalLength", b = 5)
    protected long f = 0;

    @fv(a = "lRemoteLength", b = 5)
    protected long g = 0;

    @fv(a = "isProvince", b = 2)
    protected int i = 0;

    @fv(a = "mCityCode", b = 6)
    protected String k = "";

    @fv(a = "mPinyin", b = 6)
    public String m = "";

    public String d() {
        return this.a;
    }

    public String e() {
        return this.e;
    }

    public String f() {
        return this.c;
    }

    public void c(String str) {
        this.c = str;
    }

    public int g() {
        return this.j;
    }

    public void d(String str) {
        this.k = str;
    }

    public String h() {
        return this.m;
    }

    public static String e(String str) {
        return "mAdcode='" + str + "'";
    }

    public static String f(String str) {
        return "mPinyin='" + str + "'";
    }
}
