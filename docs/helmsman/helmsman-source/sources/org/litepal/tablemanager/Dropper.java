package org.litepal.tablemanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.litepal.tablemanager.model.TableModel;
import org.litepal.util.BaseUtility;
import org.litepal.util.Const;
import org.litepal.util.LogUtil;

/* JADX INFO: loaded from: classes2.dex */
public class Dropper extends AssociationUpdater {
    private Collection<TableModel> mTableModels;

    @Override // org.litepal.tablemanager.AssociationUpdater, org.litepal.tablemanager.Creator, org.litepal.tablemanager.AssociationCreator, org.litepal.tablemanager.Generator
    protected void createOrUpgradeTable(SQLiteDatabase sQLiteDatabase, boolean z) {
        this.mTableModels = getAllTableModels();
        this.mDb = sQLiteDatabase;
        dropTables();
    }

    private void dropTables() {
        List<String> listFindTablesToDrop = findTablesToDrop();
        dropTables(listFindTablesToDrop, this.mDb);
        clearCopyInTableSchema(listFindTablesToDrop);
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0060 A[DONT_GENERATE, PHI: r1
  0x0060: PHI (r1v3 android.database.Cursor) = (r1v2 android.database.Cursor), (r1v4 android.database.Cursor) binds: [B:16:0x005e, B:10:0x0055] A[DONT_GENERATE, DONT_INLINE]] */
    private List<String> findTablesToDrop() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = null;
        try {
            try {
                cursorQuery = this.mDb.query(Const.TableSchema.TABLE_NAME, null, null, null, null, null, null);
                if (cursorQuery.moveToFirst()) {
                    do {
                        String string = cursorQuery.getString(cursorQuery.getColumnIndexOrThrow("name"));
                        if (shouldDropThisTable(string, cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("type")))) {
                            LogUtil.d(AssociationUpdater.TAG, "need to drop " + string);
                            arrayList.add(string);
                        }
                    } while (cursorQuery.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        } finally {
            if (cursorQuery != null) {
                cursorQuery.close();
            }
        }
    }

    private List<String> pickTableNamesFromTableModels() {
        ArrayList arrayList = new ArrayList();
        Iterator<TableModel> it = this.mTableModels.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getTableName());
        }
        return arrayList;
    }

    private boolean shouldDropThisTable(String str, int i) {
        return !BaseUtility.containsIgnoreCases(pickTableNamesFromTableModels(), str) && i == 0;
    }
}
