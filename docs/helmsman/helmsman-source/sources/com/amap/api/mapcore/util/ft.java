package com.amap.api.mapcore.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: DBOperation.java */
/* JADX INFO: loaded from: classes.dex */
public class ft {
    private static Map<Class<? extends fs>, fs> d = new HashMap();
    private fy a;
    private SQLiteDatabase b;
    private fs c;

    private boolean a(Annotation annotation) {
        return annotation != null;
    }

    public static synchronized fs a(Class<? extends fs> cls) throws IllegalAccessException, InstantiationException {
        if (d.get(cls) == null) {
            d.put(cls, cls.newInstance());
        }
        return d.get(cls);
    }

    public ft(Context context, fs fsVar) {
        try {
            this.a = new fy(context.getApplicationContext(), fsVar.b(), null, fsVar.c(), fsVar);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.c = fsVar;
    }

    public static String a(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : map.keySet()) {
            if (z) {
                sb.append(str).append(" = '").append(map.get(str)).append("'");
                z = false;
            } else {
                sb.append(" and ").append(str).append(" = '").append(map.get(str)).append("'");
            }
        }
        return sb.toString();
    }

    public <T> void a(String str, Class<T> cls) {
        synchronized (this.c) {
            String strA = a(b(cls));
            if (TextUtils.isEmpty(strA)) {
                return;
            }
            SQLiteDatabase sQLiteDatabaseB = b(false);
            this.b = sQLiteDatabaseB;
            if (sQLiteDatabaseB == null) {
                return;
            }
            try {
                sQLiteDatabaseB.delete(strA, str, null);
                SQLiteDatabase sQLiteDatabase = this.b;
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                    this.b = null;
                }
            } catch (Throwable th) {
                try {
                    fk.a(th, "DataBase", "deleteData");
                    SQLiteDatabase sQLiteDatabase2 = this.b;
                    if (sQLiteDatabase2 != null) {
                        sQLiteDatabase2.close();
                    }
                } catch (Throwable th2) {
                    SQLiteDatabase sQLiteDatabase3 = this.b;
                    if (sQLiteDatabase3 != null) {
                        sQLiteDatabase3.close();
                        this.b = null;
                    }
                    throw th2;
                }
            }
        }
    }

    public <T> void a(String str, Object obj, boolean z) {
        synchronized (this.c) {
            if (obj == null) {
                return;
            }
            fu fuVarB = b(obj.getClass());
            String strA = a(fuVarB);
            if (TextUtils.isEmpty(strA)) {
                return;
            }
            ContentValues contentValuesA = a(obj, fuVarB);
            if (contentValuesA == null) {
                return;
            }
            SQLiteDatabase sQLiteDatabaseB = b(z);
            this.b = sQLiteDatabaseB;
            if (sQLiteDatabaseB == null) {
                return;
            }
            try {
                sQLiteDatabaseB.update(strA, contentValuesA, str, null);
                SQLiteDatabase sQLiteDatabase = this.b;
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                    this.b = null;
                }
            } catch (Throwable th) {
                try {
                    if (!z) {
                        fk.a(th, "DataBase", "updateData");
                    } else {
                        th.printStackTrace();
                    }
                    SQLiteDatabase sQLiteDatabase2 = this.b;
                    if (sQLiteDatabase2 != null) {
                        sQLiteDatabase2.close();
                    }
                } catch (Throwable th2) {
                    SQLiteDatabase sQLiteDatabase3 = this.b;
                    if (sQLiteDatabase3 != null) {
                        sQLiteDatabase3.close();
                        this.b = null;
                    }
                    throw th2;
                }
            }
        }
    }

    public <T> void a(String str, Object obj) {
        a(str, obj, false);
    }

    public void a(Object obj, String str) {
        synchronized (this.c) {
            List listB = b(str, obj.getClass());
            if (listB == null || listB.size() == 0) {
                a(obj);
            } else {
                a(str, obj);
            }
        }
    }

    public <T> void a(T t) {
        a((Object) t, false);
    }

    public <T> void a(T t, boolean z) {
        synchronized (this.c) {
            SQLiteDatabase sQLiteDatabaseB = b(z);
            this.b = sQLiteDatabaseB;
            if (sQLiteDatabaseB == null) {
                return;
            }
            try {
                a(sQLiteDatabaseB, t);
                SQLiteDatabase sQLiteDatabase = this.b;
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.close();
                    this.b = null;
                }
            } catch (Throwable th) {
                try {
                    fk.a(th, "DataBase", "insertData");
                    SQLiteDatabase sQLiteDatabase2 = this.b;
                    if (sQLiteDatabase2 != null) {
                        sQLiteDatabase2.close();
                    }
                } catch (Throwable th2) {
                    SQLiteDatabase sQLiteDatabase3 = this.b;
                    if (sQLiteDatabase3 != null) {
                        sQLiteDatabase3.close();
                        this.b = null;
                    }
                    throw th2;
                }
            }
        }
    }

    private <T> void a(SQLiteDatabase sQLiteDatabase, T t) {
        ContentValues contentValuesA;
        fu fuVarB = b(t.getClass());
        String strA = a(fuVarB);
        if (TextUtils.isEmpty(strA) || t == null || sQLiteDatabase == null || (contentValuesA = a(t, fuVarB)) == null) {
            return;
        }
        sQLiteDatabase.insert(strA, null, contentValuesA);
    }

    public <T> void a(List<T> list) {
        String str;
        String str2;
        synchronized (this.c) {
            if (list != null) {
                if (list.size() != 0) {
                    SQLiteDatabase sQLiteDatabaseB = b(false);
                    this.b = sQLiteDatabaseB;
                    if (sQLiteDatabaseB == null) {
                        return;
                    }
                    try {
                        sQLiteDatabaseB.beginTransaction();
                        Iterator<T> it = list.iterator();
                        while (it.hasNext()) {
                            a(this.b, it.next());
                        }
                        this.b.setTransactionSuccessful();
                        try {
                            if (this.b.inTransaction()) {
                                this.b.endTransaction();
                            }
                        } catch (Throwable th) {
                            fk.a(th, "DataBase", "insertListData");
                        }
                        try {
                            this.b.close();
                            this.b = null;
                        } catch (Throwable th2) {
                            th = th2;
                            str = "DataBase";
                            str2 = "insertListData";
                            fk.a(th, str, str2);
                        }
                    } catch (Throwable th3) {
                        try {
                            fk.a(th3, "DataBase", "insertListData");
                            try {
                                if (this.b.inTransaction()) {
                                    this.b.endTransaction();
                                }
                            } catch (Throwable th4) {
                                fk.a(th4, "DataBase", "insertListData");
                            }
                            try {
                                this.b.close();
                                this.b = null;
                            } catch (Throwable th5) {
                                th = th5;
                                str = "DataBase";
                                str2 = "insertListData";
                                fk.a(th, str, str2);
                            }
                        } catch (Throwable th6) {
                            try {
                                if (this.b.inTransaction()) {
                                    this.b.endTransaction();
                                    try {
                                        this.b.close();
                                        this.b = null;
                                        throw th6;
                                    } catch (Throwable th7) {
                                        fk.a(th7, "DataBase", "insertListData");
                                        throw th6;
                                    }
                                }
                                this.b.close();
                                this.b = null;
                                throw th6;
                            } catch (Throwable th8) {
                                fk.a(th8, "DataBase", "insertListData");
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0052 A[Catch: all -> 0x0058, TRY_LEAVE, TryCatch #7 {all -> 0x0058, blocks: (B:23:0x004e, B:25:0x0052), top: B:106:0x004e, outer: #2 }] */
    /* JADX WARN: Code duplicated, block: B:67:0x00bb A[Catch: all -> 0x00c1, TRY_LEAVE, TryCatch #0 {all -> 0x00c1, blocks: (B:65:0x00b7, B:67:0x00bb), top: B:93:0x00b7, outer: #2 }] */
    /* JADX WARN: Code duplicated, block: B:81:0x00e0 A[Catch: all -> 0x00e6, TRY_LEAVE, TryCatch #4 {all -> 0x00e6, blocks: (B:79:0x00dc, B:81:0x00e0), top: B:100:0x00dc }] */
    /* JADX WARN: Code duplicated, block: B:95:0x00ce A[EXC_TOP_SPLITTER, SYNTHETIC] */
    public <T> List<T> a(String str, Class<T> cls, boolean z) {
        Cursor cursorQuery;
        SQLiteDatabase sQLiteDatabase;
        String str2;
        String str3;
        SQLiteDatabase sQLiteDatabase2;
        SQLiteDatabase sQLiteDatabase3;
        synchronized (this.c) {
            ArrayList arrayList = new ArrayList();
            fu fuVarB = b(cls);
            String strA = a(fuVarB);
            if (this.b == null) {
                this.b = a(z);
            }
            if (this.b == null || TextUtils.isEmpty(strA) || str == null) {
                return arrayList;
            }
            try {
                cursorQuery = this.b.query(strA, null, str, null, null, null, null);
                try {
                    if (cursorQuery != null) {
                        while (cursorQuery.moveToNext()) {
                            arrayList.add(a(cursorQuery, cls, fuVarB));
                        }
                        if (cursorQuery != null) {
                            try {
                                cursorQuery.close();
                            } catch (Throwable th) {
                                if (!z) {
                                    fk.a(th, "DataBase", "searchListData");
                                }
                            }
                        }
                        try {
                            SQLiteDatabase sQLiteDatabase4 = this.b;
                            if (sQLiteDatabase4 != null) {
                                sQLiteDatabase4.close();
                                this.b = null;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (!z) {
                                str2 = "DataBase";
                                str3 = "searchListData";
                                fk.a(th, str2, str3);
                            }
                        }
                        return arrayList;
                    }
                    this.b.close();
                    this.b = null;
                    if (cursorQuery == null) {
                        sQLiteDatabase3 = this.b;
                        if (sQLiteDatabase3 != null) {
                            sQLiteDatabase3.close();
                            this.b = null;
                        }
                        return arrayList;
                    }
                    try {
                        cursorQuery.close();
                    } catch (Throwable th3) {
                        if (!z) {
                            fk.a(th3, "DataBase", "searchListData");
                        }
                    }
                    try {
                        sQLiteDatabase3 = this.b;
                        if (sQLiteDatabase3 != null) {
                            sQLiteDatabase3.close();
                            this.b = null;
                        }
                    } catch (Throwable th4) {
                        if (!z) {
                            fk.a(th4, "DataBase", "searchListData");
                        }
                    }
                    return arrayList;
                } catch (Throwable th5) {
                    th = th5;
                    if (z) {
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        sQLiteDatabase2 = this.b;
                        if (sQLiteDatabase2 != null) {
                            sQLiteDatabase2.close();
                            this.b = null;
                        }
                    } else {
                        try {
                            fk.a(th, "DataBase", "searchListData");
                            if (cursorQuery != null) {
                                try {
                                    cursorQuery.close();
                                } catch (Throwable th6) {
                                    if (!z) {
                                        fk.a(th6, "DataBase", "searchListData");
                                    }
                                }
                            }
                            try {
                                sQLiteDatabase2 = this.b;
                                if (sQLiteDatabase2 != null) {
                                    sQLiteDatabase2.close();
                                    this.b = null;
                                }
                            } catch (Throwable th7) {
                                th = th7;
                                if (!z) {
                                    str2 = "DataBase";
                                    str3 = "searchListData";
                                    fk.a(th, str2, str3);
                                }
                            }
                        } catch (Throwable th8) {
                            if (cursorQuery == null) {
                                sQLiteDatabase = this.b;
                                if (sQLiteDatabase != null) {
                                    sQLiteDatabase.close();
                                    this.b = null;
                                }
                                throw th8;
                            }
                            try {
                                cursorQuery.close();
                            } catch (Throwable th9) {
                                if (!z) {
                                    fk.a(th9, "DataBase", "searchListData");
                                }
                            }
                            try {
                                sQLiteDatabase = this.b;
                                if (sQLiteDatabase != null) {
                                    sQLiteDatabase.close();
                                    this.b = null;
                                }
                            } catch (Throwable th10) {
                                if (!z) {
                                    fk.a(th10, "DataBase", "searchListData");
                                }
                            }
                            throw th8;
                        }
                    }
                }
            } catch (Throwable th11) {
                th = th11;
                cursorQuery = null;
            }
            throw th;
        }
    }

    public <T> List<T> b(String str, Class<T> cls) {
        return a(str, (Class) cls, false);
    }

    private <T> T a(Cursor cursor, Class<T> cls, fu fuVar) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        Field[] fieldArrA = a((Class<?>) cls, fuVar.b());
        Constructor<T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
        declaredConstructor.setAccessible(true);
        T tNewInstance = declaredConstructor.newInstance(new Object[0]);
        for (Field field : fieldArrA) {
            field.setAccessible(true);
            Annotation annotation = field.getAnnotation(fv.class);
            if (annotation != null) {
                fv fvVar = (fv) annotation;
                int iB = fvVar.b();
                int columnIndex = cursor.getColumnIndex(fvVar.a());
                switch (iB) {
                    case 1:
                        field.set(tNewInstance, Short.valueOf(cursor.getShort(columnIndex)));
                        break;
                    case 2:
                        field.set(tNewInstance, Integer.valueOf(cursor.getInt(columnIndex)));
                        break;
                    case 3:
                        field.set(tNewInstance, Float.valueOf(cursor.getFloat(columnIndex)));
                        break;
                    case 4:
                        field.set(tNewInstance, Double.valueOf(cursor.getDouble(columnIndex)));
                        break;
                    case 5:
                        field.set(tNewInstance, Long.valueOf(cursor.getLong(columnIndex)));
                        break;
                    case 6:
                        field.set(tNewInstance, cursor.getString(columnIndex));
                        break;
                    case 7:
                        field.set(tNewInstance, cursor.getBlob(columnIndex));
                        break;
                }
            }
        }
        return tNewInstance;
    }

    private void a(Object obj, Field field, ContentValues contentValues) {
        Annotation annotation = field.getAnnotation(fv.class);
        if (annotation == null) {
            return;
        }
        fv fvVar = (fv) annotation;
        try {
            switch (fvVar.b()) {
                case 1:
                    contentValues.put(fvVar.a(), Short.valueOf(field.getShort(obj)));
                    break;
                case 2:
                    contentValues.put(fvVar.a(), Integer.valueOf(field.getInt(obj)));
                    break;
                case 3:
                    contentValues.put(fvVar.a(), Float.valueOf(field.getFloat(obj)));
                    break;
                case 4:
                    contentValues.put(fvVar.a(), Double.valueOf(field.getDouble(obj)));
                    break;
                case 5:
                    contentValues.put(fvVar.a(), Long.valueOf(field.getLong(obj)));
                    break;
                case 6:
                    contentValues.put(fvVar.a(), (String) field.get(obj));
                    break;
                case 7:
                    contentValues.put(fvVar.a(), (byte[]) field.get(obj));
                    break;
                default:
                    return;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private ContentValues a(Object obj, fu fuVar) {
        ContentValues contentValues = new ContentValues();
        for (Field field : a(obj.getClass(), fuVar.b())) {
            field.setAccessible(true);
            a(obj, field, contentValues);
        }
        return contentValues;
    }

    private Field[] a(Class<?> cls, boolean z) {
        if (cls == null) {
            return null;
        }
        if (z) {
            return cls.getSuperclass().getDeclaredFields();
        }
        return cls.getDeclaredFields();
    }

    private SQLiteDatabase a(boolean z) {
        try {
            if (this.b == null) {
                this.b = this.a.getReadableDatabase();
            }
        } catch (Throwable th) {
            if (!z) {
                fk.a(th, "DBOperation", "getReadAbleDataBase");
            } else {
                th.printStackTrace();
            }
        }
        return this.b;
    }

    private SQLiteDatabase b(boolean z) {
        try {
            SQLiteDatabase sQLiteDatabase = this.b;
            if (sQLiteDatabase == null || sQLiteDatabase.isReadOnly()) {
                SQLiteDatabase sQLiteDatabase2 = this.b;
                if (sQLiteDatabase2 != null) {
                    sQLiteDatabase2.close();
                }
                this.b = this.a.getWritableDatabase();
            }
        } catch (Throwable th) {
            fk.a(th, "DBOperation", "getWriteDatabase");
        }
        return this.b;
    }

    private <T> String a(fu fuVar) {
        if (fuVar == null) {
            return null;
        }
        return fuVar.a();
    }

    private <T> fu b(Class<T> cls) {
        Annotation annotation = cls.getAnnotation(fu.class);
        if (a(annotation)) {
            return (fu) annotation;
        }
        return null;
    }
}
