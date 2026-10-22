package com.autonavi.ae.search.model;

/* JADX INFO: loaded from: classes.dex */
public class GADAREAINFO {
    private int bHasData;
    private String pzName;
    private String pzPinyin;
    private GADMINCODE stAdCode;

    public GADAREAINFO(int i, String str, String str2, GADMINCODE gadmincode) {
        this.bHasData = i;
        this.stAdCode = gadmincode;
        this.pzName = str;
        this.pzPinyin = str2;
    }
}
