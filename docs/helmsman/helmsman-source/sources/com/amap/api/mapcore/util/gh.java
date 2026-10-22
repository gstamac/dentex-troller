package com.amap.api.mapcore.util;

/* JADX INFO: compiled from: DexDownloadItem.java */
/* JADX INFO: loaded from: classes.dex */
public class gh {
    String a;
    String b;
    String c;
    String d;
    String e;
    int f;
    int g;
    private String h;
    private String i;

    public gh(String str, String str2, String str3) {
        this.h = str;
        this.i = str2;
        try {
            String[] strArrSplit = str.split("/");
            int length = strArrSplit.length;
            if (length <= 1) {
                return;
            }
            String str4 = strArrSplit[length - 1];
            this.a = str4;
            String[] strArrSplit2 = str4.split("_");
            this.b = strArrSplit2[0];
            this.c = strArrSplit2[2];
            this.d = strArrSplit2[1];
            this.f = Integer.parseInt(strArrSplit2[3]);
            this.g = Integer.parseInt(strArrSplit2[4].split("\\.")[0]);
        } catch (Throwable th) {
            gr.a(th, "DexDownloadItem", "DexDownloadItem");
        }
    }

    String a() {
        return this.h;
    }

    String b() {
        return this.i;
    }

    String c() {
        return this.c;
    }
}
