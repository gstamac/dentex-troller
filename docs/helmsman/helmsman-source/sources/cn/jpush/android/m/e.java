package cn.jpush.android.m;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/* JADX INFO: loaded from: classes.dex */
public abstract class e extends SQLiteOpenHelper {
    private volatile int a;
    private volatile int b;
    private volatile SQLiteDatabase c;
    private volatile SQLiteDatabase d;
    private final Object e;
    private final Object f;
    private final Context g;
    private final String h;
    private final int i;
    private final SQLiteDatabase.CursorFactory j;

    public e(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
        this.a = 0;
        this.b = 0;
        this.e = new Object();
        this.f = new Object();
        this.g = context;
        this.h = str;
        this.i = i;
        this.j = cursorFactory;
    }

    public boolean a(boolean z) {
        try {
            if (z) {
                synchronized (this.e) {
                    getWritableDatabase();
                    this.b++;
                }
                return true;
            }
            synchronized (this.f) {
                getReadableDatabase();
                this.a++;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
        return false;
    }

    public void b(boolean z) {
        boolean z2 = true;
        if (z) {
            synchronized (this.e) {
                if (this.d != null && this.d.isOpen()) {
                    int i = this.b - 1;
                    this.b = i;
                    if (i > 0) {
                        z2 = false;
                    }
                }
                if (z2) {
                    this.b = 0;
                    if (this.d != null) {
                        this.d.close();
                    }
                    this.d = null;
                }
            }
            return;
        }
        synchronized (this.f) {
            if (this.c != null && this.c.isOpen()) {
                int i2 = this.a - 1;
                this.a = i2;
                if (i2 > 0) {
                    z2 = false;
                }
            }
            if (z2) {
                this.a = 0;
                if (this.c != null) {
                    this.c.close();
                }
                this.c = null;
            }
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper, java.lang.AutoCloseable
    @Deprecated
    public void close() {
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public SQLiteDatabase getReadableDatabase() {
        if (this.c == null || !this.c.isOpen()) {
            synchronized (this.f) {
                if (this.c == null || !this.c.isOpen()) {
                    try {
                        getWritableDatabase();
                    } catch (SQLiteException unused) {
                    }
                    String path = this.g.getDatabasePath(this.h).getPath();
                    this.c = SQLiteDatabase.openDatabase(path, this.j, 1);
                    if (this.c.getVersion() != this.i) {
                        throw new SQLiteException("Can't upgrade read-only database from version " + this.c.getVersion() + " to " + this.i + ": " + path);
                    }
                    this.a = 0;
                    onOpen(this.c);
                }
            }
        }
        return this.c;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public SQLiteDatabase getWritableDatabase() {
        if (this.d == null || !this.d.isOpen()) {
            synchronized (this.e) {
                if (this.d == null || !this.d.isOpen()) {
                    this.b = 0;
                    this.d = super.getWritableDatabase();
                    this.d.enableWriteAheadLogging();
                }
            }
        }
        return this.d;
    }
}
