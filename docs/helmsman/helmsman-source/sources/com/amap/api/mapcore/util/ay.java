package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: DTFileInfo.java */
/* JADX INFO: loaded from: classes.dex */
@fu(a = "update_item_file")
class ay {

    @fv(a = "mAdcode", b = 6)
    private String a;

    @fv(a = "file", b = 6)
    private String b;

    public ay() {
        this.a = "";
        this.b = "";
    }

    public ay(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public String a() {
        return this.b;
    }

    public static String a(String str) {
        return "mAdcode='" + str + "'";
    }
}
