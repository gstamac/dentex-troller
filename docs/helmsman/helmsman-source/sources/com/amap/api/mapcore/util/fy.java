package com.amap.api.mapcore.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* JADX INFO: compiled from: DB.java */
/* JADX INFO: loaded from: classes.dex */
public class fy extends SQLiteOpenHelper {
    private static boolean b = true;
    private static boolean c = false;
    private fs a;

    public fy(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i, fs fsVar) {
        super(context, str, cursorFactory, i);
        this.a = fsVar;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        this.a.a(sQLiteDatabase);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        this.a.a(sQLiteDatabase, i, i2);
    }
}
