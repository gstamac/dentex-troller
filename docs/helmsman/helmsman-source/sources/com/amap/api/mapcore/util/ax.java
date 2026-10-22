package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: DTDownloadInfo.java */
/* JADX INFO: loaded from: classes.dex */
@fu(a = "update_item_download_info")
class ax {

    @fv(a = "mAdcode", b = 6)
    private String a;

    @fv(a = "fileLength", b = 5)
    private long b;

    @fv(a = "splitter", b = 2)
    private int c;

    @fv(a = "startPos", b = 5)
    private long d;

    @fv(a = "endPos", b = 5)
    private long e;

    public ax() {
        this.a = "";
        this.b = 0L;
        this.c = 0;
        this.d = 0L;
        this.e = 0L;
    }

    public ax(String str, long j, int i, long j2, long j3) {
        this.a = str;
        this.b = j;
        this.c = i;
        this.d = j2;
        this.e = j3;
    }

    public static String a(String str) {
        return "mAdcode='" + str + "'";
    }
}
