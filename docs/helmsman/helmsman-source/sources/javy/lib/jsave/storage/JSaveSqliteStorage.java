package javy.lib.jsave.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import javy.lib.jsave.JSave;
import javy.lib.jsave.bean.JSaveDefaultBean;
import javy.lib.jsave.bean.JSaveLitePalBean;

/* JADX INFO: loaded from: classes2.dex */
public class JSaveSqliteStorage extends SQLiteOpenHelper implements JSaveStorage<JSaveDefaultBean> {
    private static final String dbName = "jsave.db";
    private static final int version = 2;
    private final String CREATE_TABLE_SQL;
    private SQLiteDatabase db;
    private final String tableName;

    public JSaveSqliteStorage() {
        super(JSave.mContext, dbName, (SQLiteDatabase.CursorFactory) null, 2);
        this.tableName = "jsKeyValue";
        this.CREATE_TABLE_SQL = "create table jsKeyValue (strKey TEXT UNIQUE,strValue TEXT,strType TEXT,datas_path TEXT);";
    }

    public void convertToLitePal() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        this.db = writableDatabase;
        Cursor cursorQuery = writableDatabase.query("jsKeyValue", null, null, null, null, null, null);
        while (cursorQuery.moveToNext()) {
            JSaveLitePalBean jSaveLitePalBean = new JSaveLitePalBean();
            String string = cursorQuery.getString(cursorQuery.getColumnIndex("strValue"));
            String string2 = cursorQuery.getString(cursorQuery.getColumnIndex("strType"));
            String string3 = cursorQuery.getString(cursorQuery.getColumnIndex("strKey"));
            jSaveLitePalBean.setStrKey(string3);
            jSaveLitePalBean.setStrType(string2);
            jSaveLitePalBean.setStrValue(string);
            jSaveLitePalBean.save();
            delete(string3);
        }
        cursorQuery.close();
        SQLiteDatabase sQLiteDatabase = this.db;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public boolean save(JSaveDefaultBean jSaveDefaultBean) {
        synchronized (this) {
            String strKey = jSaveDefaultBean.getStrKey();
            if (read(strKey) != null && !delete(strKey)) {
                Log.w("jsave faile", "cover faile");
                return false;
            }
            if (this.db == null) {
                this.db = getWritableDatabase();
            }
            long jInsert = this.db.insert("jsKeyValue", null, jSaveDefaultBean.toContentValue());
            SQLiteDatabase sQLiteDatabase = this.db;
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
                this.db = null;
                Log.i("jsave", "save:db close");
            }
            if (jInsert > 0) {
                return true;
            }
            Log.w("jsave faile", "save faile");
            return false;
        }
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public JSaveDefaultBean read(String str) {
        JSaveDefaultBean jSaveDefaultBean;
        synchronized (this) {
            if (this.db == null) {
                this.db = getWritableDatabase();
            }
            Cursor cursorQuery = this.db.query("jsKeyValue", null, "strKey=?", new String[]{str}, null, null, null);
            jSaveDefaultBean = null;
            while (cursorQuery.moveToNext()) {
                jSaveDefaultBean = new JSaveDefaultBean();
                String string = cursorQuery.getString(cursorQuery.getColumnIndex("strValue"));
                String string2 = cursorQuery.getString(cursorQuery.getColumnIndex("strType"));
                jSaveDefaultBean.setStrKey(str);
                jSaveDefaultBean.setStrType(string2);
                jSaveDefaultBean.setStrValue(string);
                cursorQuery.close();
            }
            SQLiteDatabase sQLiteDatabase = this.db;
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
                this.db = null;
                Log.i("jsave", "read:db close");
            }
        }
        return jSaveDefaultBean;
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public int deleteAll() {
        SQLiteDatabase sQLiteDatabase = this.db;
        if (sQLiteDatabase == null || !sQLiteDatabase.isOpen()) {
            this.db = getWritableDatabase();
        }
        int iDelete = this.db.delete("jsKeyValue", null, null);
        SQLiteDatabase sQLiteDatabase2 = this.db;
        if (sQLiteDatabase2 != null) {
            sQLiteDatabase2.close();
            this.db = null;
            Log.i("jsave", "deleteAll:db close");
        }
        return iDelete;
    }

    @Override // javy.lib.jsave.storage.JSaveStorage
    public boolean delete(String str) {
        synchronized (this) {
            if (this.db == null) {
                this.db = getWritableDatabase();
            }
            int iDelete = this.db.delete("jsKeyValue", "strKey=?", new String[]{str});
            SQLiteDatabase sQLiteDatabase = this.db;
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
                this.db = null;
                Log.i("jsave", "delete:db close");
            }
            if (iDelete > 0) {
                return true;
            }
            Log.w("jsave faile", "delete key=" + str);
            return false;
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table jsKeyValue (strKey TEXT UNIQUE,strValue TEXT,strType TEXT,datas_path TEXT);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i != 1) {
            return;
        }
        sQLiteDatabase.execSQL("alter table jsKeyValue add datas blob;");
    }
}
