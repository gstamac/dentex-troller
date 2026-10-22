package org.litepal.tablemanager;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.litepal.parser.LitePalAttr;
import org.litepal.tablemanager.model.AssociationsModel;
import org.litepal.tablemanager.model.ColumnModel;
import org.litepal.tablemanager.model.GenericModel;
import org.litepal.tablemanager.model.TableModel;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;
import org.litepal.util.LogUtil;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AssociationUpdater extends Creator {
    public static final String TAG = "AssociationUpdater";
    private Collection<AssociationsModel> mAssociationModels;
    protected SQLiteDatabase mDb;

    @Override // org.litepal.tablemanager.Creator, org.litepal.tablemanager.AssociationCreator, org.litepal.tablemanager.Generator
    protected abstract void createOrUpgradeTable(SQLiteDatabase sQLiteDatabase, boolean z);

    @Override // org.litepal.tablemanager.AssociationCreator, org.litepal.tablemanager.Generator
    protected void addOrUpdateAssociation(SQLiteDatabase sQLiteDatabase, boolean z) {
        this.mAssociationModels = getAllAssociations();
        this.mDb = sQLiteDatabase;
        removeAssociations();
    }

    protected List<String> getForeignKeyColumns(TableModel tableModel) {
        ArrayList arrayList = new ArrayList();
        for (ColumnModel columnModel : getTableModelFromDB(tableModel.getTableName()).getColumnModels()) {
            String columnName = columnModel.getColumnName();
            if (isForeignKeyColumnFormat(columnModel.getColumnName()) && !tableModel.containsColumn(columnName)) {
                LogUtil.d(TAG, "getForeignKeyColumnNames >> foreign key column is " + columnName);
                arrayList.add(columnName);
            }
        }
        return arrayList;
    }

    protected boolean isForeignKeyColumn(TableModel tableModel, String str) {
        return BaseUtility.containsIgnoreCases(getForeignKeyColumns(tableModel), str);
    }

    protected TableModel getTableModelFromDB(String str) {
        return DBUtility.findPragmaTableInfo(str, this.mDb);
    }

    protected void dropTables(List<String> list, SQLiteDatabase sQLiteDatabase) {
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(generateDropTableSQL(list.get(i)));
        }
        execute(arrayList, sQLiteDatabase);
    }

    protected void removeColumns(Collection<String> collection, String str) {
        if (collection == null || collection.isEmpty()) {
            return;
        }
        execute(getRemoveColumnSQLs(collection, str), this.mDb);
    }

    protected void clearCopyInTableSchema(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder("delete from table_schema where");
        boolean z = false;
        for (String str : list) {
            if (z) {
                sb.append(" or ");
            }
            sb.append(" lower(name) = lower('");
            sb.append(str).append("')");
            z = true;
        }
        LogUtil.d(TAG, "clear table schema value sql is " + ((Object) sb));
        ArrayList arrayList = new ArrayList();
        arrayList.add(sb.toString());
        execute(arrayList, this.mDb);
    }

    private void removeAssociations() {
        removeForeignKeyColumns();
        removeIntermediateTables();
        removeGenericTables();
    }

    private void removeForeignKeyColumns() {
        Iterator<String> it = LitePalAttr.getInstance().getClassNames().iterator();
        while (it.hasNext()) {
            TableModel tableModel = getTableModel(it.next());
            removeColumns(findForeignKeyToRemove(tableModel), tableModel.getTableName());
        }
    }

    private void removeIntermediateTables() {
        List<String> listFindIntermediateTablesToDrop = findIntermediateTablesToDrop();
        dropTables(listFindIntermediateTablesToDrop, this.mDb);
        clearCopyInTableSchema(listFindIntermediateTablesToDrop);
    }

    private void removeGenericTables() {
        List<String> listFindGenericTablesToDrop = findGenericTablesToDrop();
        dropTables(listFindGenericTablesToDrop, this.mDb);
        clearCopyInTableSchema(listFindGenericTablesToDrop);
    }

    private List<String> findForeignKeyToRemove(TableModel tableModel) {
        ArrayList arrayList = new ArrayList();
        List<String> foreignKeyColumns = getForeignKeyColumns(tableModel);
        String tableName = tableModel.getTableName();
        for (String str : foreignKeyColumns) {
            if (shouldDropForeignKey(tableName, DBUtility.getTableNameByForeignColumn(str))) {
                arrayList.add(str);
            }
        }
        LogUtil.d(TAG, "findForeignKeyToRemove >> " + tableModel.getTableName() + " " + arrayList);
        return arrayList;
    }

    private List<String> findIntermediateTablesToDrop() {
        ArrayList arrayList = new ArrayList();
        for (String str : DBUtility.findAllTableNames(this.mDb)) {
            if (DBUtility.isIntermediateTable(str, this.mDb)) {
                boolean z = true;
                for (AssociationsModel associationsModel : this.mAssociationModels) {
                    if (associationsModel.getAssociationType() == 3 && str.equalsIgnoreCase(DBUtility.getIntermediateTableName(associationsModel.getTableName(), associationsModel.getAssociatedTableName()))) {
                        z = false;
                    }
                }
                if (z) {
                    arrayList.add(str);
                }
            }
        }
        LogUtil.d(TAG, "findIntermediateTablesToDrop >> " + arrayList);
        return arrayList;
    }

    private List<String> findGenericTablesToDrop() {
        ArrayList arrayList = new ArrayList();
        for (String str : DBUtility.findAllTableNames(this.mDb)) {
            if (DBUtility.isGenericTable(str, this.mDb)) {
                Iterator<GenericModel> it = getGenericModels().iterator();
                boolean z = true;
                while (it.hasNext()) {
                    if (str.equalsIgnoreCase(it.next().getTableName())) {
                        z = false;
                    }
                }
                if (z) {
                    arrayList.add(str);
                }
            }
        }
        return arrayList;
    }

    protected String generateAlterToTempTableSQL(String str) {
        StringBuilder sb = new StringBuilder("alter table ");
        sb.append(str).append(" rename to ").append(getTempTableName(str));
        return sb.toString();
    }

    private String generateCreateNewTableSQL(Collection<String> collection, TableModel tableModel) {
        Iterator<String> it = collection.iterator();
        while (it.hasNext()) {
            tableModel.removeColumnModelByName(it.next());
        }
        return generateCreateTableSQL(tableModel);
    }

    protected String generateDataMigrationSQL(TableModel tableModel) {
        String tableName = tableModel.getTableName();
        List<ColumnModel> columnModels = tableModel.getColumnModels();
        if (columnModels.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(tableName).append("(");
        boolean z = false;
        boolean z2 = false;
        for (ColumnModel columnModel : columnModels) {
            if (z2) {
                sb.append(", ");
            }
            sb.append(columnModel.getColumnName());
            z2 = true;
        }
        sb.append(") select ");
        for (ColumnModel columnModel2 : columnModels) {
            if (z) {
                sb.append(", ");
            }
            sb.append(columnModel2.getColumnName());
            z = true;
        }
        sb.append(" from ").append(getTempTableName(tableName));
        return sb.toString();
    }

    protected String generateDropTempTableSQL(String str) {
        return generateDropTableSQL(getTempTableName(str));
    }

    protected String getTempTableName(String str) {
        return str + "_temp";
    }

    private List<String> getRemoveColumnSQLs(Collection<String> collection, String str) {
        TableModel tableModelFromDB = getTableModelFromDB(str);
        String strGenerateAlterToTempTableSQL = generateAlterToTempTableSQL(str);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + strGenerateAlterToTempTableSQL);
        String strGenerateCreateNewTableSQL = generateCreateNewTableSQL(collection, tableModelFromDB);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + strGenerateCreateNewTableSQL);
        String strGenerateDataMigrationSQL = generateDataMigrationSQL(tableModelFromDB);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + strGenerateDataMigrationSQL);
        String strGenerateDropTempTableSQL = generateDropTempTableSQL(str);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + strGenerateDropTempTableSQL);
        ArrayList arrayList = new ArrayList();
        arrayList.add(strGenerateAlterToTempTableSQL);
        arrayList.add(strGenerateCreateNewTableSQL);
        arrayList.add(strGenerateDataMigrationSQL);
        arrayList.add(strGenerateDropTempTableSQL);
        return arrayList;
    }

    private boolean shouldDropForeignKey(String str, String str2) {
        for (AssociationsModel associationsModel : this.mAssociationModels) {
            if (associationsModel.getAssociationType() == 1) {
                if (!str.equalsIgnoreCase(associationsModel.getTableHoldsForeignKey())) {
                    continue;
                } else if (associationsModel.getTableName().equalsIgnoreCase(str)) {
                    if (isRelationCorrect(associationsModel, str, str2)) {
                        return false;
                    }
                } else if (associationsModel.getAssociatedTableName().equalsIgnoreCase(str) && isRelationCorrect(associationsModel, str2, str)) {
                    return false;
                }
            } else if (associationsModel.getAssociationType() == 2 && isRelationCorrect(associationsModel, str2, str)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRelationCorrect(AssociationsModel associationsModel, String str, String str2) {
        return associationsModel.getTableName().equalsIgnoreCase(str) && associationsModel.getAssociatedTableName().equalsIgnoreCase(str2);
    }
}
