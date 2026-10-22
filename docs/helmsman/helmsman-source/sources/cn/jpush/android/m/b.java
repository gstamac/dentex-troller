package cn.jpush.android.m;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.jpush.android.helper.Logger;

/* JADX INFO: loaded from: classes.dex */
public class b extends e {
    private static volatile b b;
    private static final String[] a = {"_id", "ln_id", "ln_count", "ln_remove", "ln_type", "ln_extra", "ln_trigger_time", "ln_add_time"};
    private static final Object c = new Object();

    private b(Context context) {
        super(context, "jpush_local_notification.db", null, 1);
    }

    public static b a(Context context) {
        if (b == null) {
            synchronized (c) {
                if (b == null) {
                    b = new b(context.getApplicationContext());
                }
            }
        }
        return b;
    }

    public static c a(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            Logger.d("LocalNotificationDb", "cursor is null");
            return null;
        }
        try {
            c cVar = new c();
            cVar.a(cursor.getLong(1));
            cVar.a(cursor.getInt(2));
            cVar.b(cursor.getInt(3));
            cVar.c(cursor.getInt(4));
            cVar.a(cursor.getString(5));
            cVar.c(cursor.getLong(6));
            cVar.b(cursor.getLong(7));
            Logger.i("LocalNotificationDb", cVar.toString());
            return cVar;
        } catch (Exception e) {
            e.getStackTrace();
            Logger.w("LocalNotificationDb", "initLocalNotificationDBData exception:" + e.getMessage());
            return null;
        }
    }

    public int a(long j) {
        try {
            if (!a(true)) {
                return 0;
            }
            return getWritableDatabase().delete("t_localnotification", "ln_id=" + j, null);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            b(true);
        }
    }

    public long a(long j, int i, int i2, int i3, String str, long j2, long j3) {
        try {
            if (a(true)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ln_id", Long.valueOf(j));
                contentValues.put("ln_count", Integer.valueOf(i));
                contentValues.put("ln_remove", Integer.valueOf(i2));
                contentValues.put("ln_type", Integer.valueOf(i3));
                contentValues.put("ln_extra", str);
                contentValues.put("ln_trigger_time", Long.valueOf(j2));
                contentValues.put("ln_add_time", Long.valueOf(j3));
                return getWritableDatabase().insert("t_localnotification", null, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            b(true);
        }
        return 0L;
    }

    public Cursor a(int i, long j) {
        try {
            return getReadableDatabase().query(true, "t_localnotification", a, "ln_count=" + i + " and ln_trigger_time<" + j, null, null, null, null, null);
        } catch (Exception e) {
            Logger.w("LocalNotificationDb", "getOutDatas exception:" + e.getMessage());
            return null;
        }
    }

    public Cursor a(long j, long j2) {
        try {
            return getReadableDatabase().query(true, "t_localnotification", a, "ln_count>0 and ln_trigger_time<" + (j2 + j) + " and ln_trigger_time>" + j, null, null, null, null, null);
        } catch (Exception e) {
            Logger.w("LocalNotificationDb", "getRtcDatas exception:" + e.getMessage());
            return null;
        }
    }

    public c a(long j, int i) throws Exception {
        if (!a(false)) {
            throw new Exception("open database failed");
        }
        Cursor cursorQuery = null;
        try {
            cursorQuery = getReadableDatabase().query(true, "t_localnotification", a, "ln_id=" + j + " and ln_type=" + i, null, null, null, null, null);
            if (cursorQuery != null) {
                cursorQuery.moveToFirst();
            }
            return a(cursorQuery);
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            b(false);
        }
    }

    /* JADX WARN: Code duplicated, block: B:41:0x0081  */
    /* JADX WARN: Code duplicated, block: B:44:0x0089  */
    /* JADX WARN: Code duplicated, block: B:48:0x0091  */
    /* JADX WARN: Code duplicated, block: B:51:0x0099  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v19 */
    public int[] a() throws Throwable {
        ?? r0;
        int[] iArr;
        ?? r1;
        Cursor cursor = null;
        iArr = null;
        iArr = null;
        int[] iArr2 = null;
        cursor = null;
        if (!a(false)) {
            return null;
        }
        try {
            Cursor cursorQuery = getReadableDatabase().query(true, "t_localnotification", new String[]{"ln_id"}, "1", null, null, null, null, null);
            if (cursorQuery != null) {
                try {
                    try {
                        if (cursorQuery.getCount() > 0) {
                            iArr = new int[cursorQuery.getCount()];
                            try {
                                cursorQuery.moveToFirst();
                                int i = 0;
                                do {
                                    iArr[i] = cursorQuery.getInt(0);
                                    i++;
                                } while (cursorQuery.moveToNext());
                                iArr2 = iArr;
                            } catch (Exception e) {
                                e = e;
                                cursor = cursorQuery;
                                r0 = false;
                                try {
                                    e.printStackTrace();
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    b(false);
                                    if (r0 != false) {
                                        b(true);
                                    }
                                    return iArr;
                                } catch (Throwable th) {
                                    th = th;
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    b(false);
                                    if (r0 != false) {
                                        b(true);
                                    }
                                    throw th;
                                }
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = cursorQuery;
                        r0 = false;
                        if (cursor != null) {
                            cursor.close();
                        }
                        b(false);
                        if (r0 != false) {
                            b(true);
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    iArr = iArr2;
                }
            }
            if (a(true)) {
                try {
                    getWritableDatabase().execSQL("delete from t_localnotification");
                    r1 = true;
                } catch (Exception e3) {
                    e = e3;
                    iArr = iArr2;
                    cursor = cursorQuery;
                    r0 = true;
                    e.printStackTrace();
                    if (cursor != null) {
                        cursor.close();
                    }
                    b(false);
                    if (r0 != false) {
                        b(true);
                    }
                    return iArr;
                } catch (Throwable th3) {
                    th = th3;
                    cursor = cursorQuery;
                    r0 = true;
                    if (cursor != null) {
                        cursor.close();
                    }
                    b(false);
                    if (r0 != false) {
                        b(true);
                    }
                    throw th;
                }
            } else {
                r1 = false;
            }
            if (cursorQuery != null) {
                cursorQuery.close();
            }
            b(false);
            if (r1 != true) {
                return iArr2;
            }
            b(true);
            return iArr2;
        } catch (Exception e4) {
            e = e4;
            r0 = false;
            iArr = null;
        } catch (Throwable th4) {
            th = th4;
        }
    }

    public long b(long j, int i, int i2, int i3, String str, long j2, long j3) {
        try {
            if (a(true)) {
                String str2 = "ln_id=" + j;
                ContentValues contentValues = new ContentValues();
                contentValues.put("ln_id", Long.valueOf(j));
                contentValues.put("ln_count", Integer.valueOf(i));
                contentValues.put("ln_remove", Integer.valueOf(i2));
                contentValues.put("ln_type", Integer.valueOf(i3));
                contentValues.put("ln_extra", str);
                contentValues.put("ln_trigger_time", Long.valueOf(j2));
                contentValues.put("ln_add_time", Long.valueOf(j3));
                return getWritableDatabase().update("t_localnotification", contentValues, str2, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            b(true);
        }
        return 0L;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE t_localnotification (_id INTEGER PRIMARY KEY AUTOINCREMENT ,ln_id long not null,ln_count integer not null,ln_remove integer not null,ln_type integer not null,ln_extra text ,ln_trigger_time long ,ln_add_time long );");
        } catch (Exception unused) {
            Logger.v("LocalNotificationDb", "表已经存在");
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS t_localnotification");
        onCreate(sQLiteDatabase);
    }
}
