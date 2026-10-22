package com.amap.api.mapcore.util;

import android.database.sqlite.SQLiteDatabase;

/* JADX INFO: compiled from: DynamicFileDBCreator.java */
/* JADX INFO: loaded from: classes.dex */
public class gn implements fs {
    private static gn a;

    @Override // com.amap.api.mapcore.util.fs
    public void a(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    @Override // com.amap.api.mapcore.util.fs
    public String b() {
        return "dafile.db";
    }

    @Override // com.amap.api.mapcore.util.fs
    public int c() {
        return 1;
    }

    public static synchronized gn a() {
        if (a == null) {
            a = new gn();
        }
        return a;
    }

    private gn() {
    }

    @Override // com.amap.api.mapcore.util.fs
    public void a(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS file (_id integer primary key autoincrement, sname  varchar(20), fname varchar(100),md varchar(20),version varchar(20),dversion varchar(20),status varchar(20),reservedfield varchar(20));");
        } catch (Throwable th) {
            gr.a(th, "DynamicFileDBCreator", "onCreate");
        }
    }
}
