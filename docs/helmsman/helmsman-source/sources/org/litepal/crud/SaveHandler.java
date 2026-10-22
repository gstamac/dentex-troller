package org.litepal.crud;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.annotation.Encrypt;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.exceptions.DataSupportException;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* JADX INFO: loaded from: classes2.dex */
class SaveHandler extends DataHandler {
    private boolean ignoreAssociations = false;
    private ContentValues values = new ContentValues();

    private boolean shouldGiveModelIdValue(String str, Class<?> cls, long j) {
        return (str == null || cls == null || j <= 0) ? false : true;
    }

    SaveHandler(SQLiteDatabase sQLiteDatabase) {
        this.mDatabase = sQLiteDatabase;
    }

    void onSave(DataSupport dataSupport) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        String className = dataSupport.getClassName();
        List<Field> supportedFields = getSupportedFields(className);
        List<Field> supportedGenericFields = getSupportedGenericFields(className);
        Collection<AssociationsInfo> associationInfo = getAssociationInfo(className);
        if (!dataSupport.isSaved()) {
            if (!this.ignoreAssociations) {
                analyzeAssociatedModels(dataSupport, associationInfo);
            }
            doSaveAction(dataSupport, supportedFields, supportedGenericFields);
            if (this.ignoreAssociations) {
                return;
            }
            analyzeAssociatedModels(dataSupport, associationInfo);
            return;
        }
        if (!this.ignoreAssociations) {
            analyzeAssociatedModels(dataSupport, associationInfo);
        }
        doUpdateAction(dataSupport, supportedFields, supportedGenericFields);
    }

    void onSaveFast(DataSupport dataSupport) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.ignoreAssociations = true;
        onSave(dataSupport);
    }

    <T extends DataSupport> void onSaveAll(Collection<T> collection) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (collection == null || collection.size() <= 0) {
            return;
        }
        DataSupport[] dataSupportArr = (DataSupport[]) collection.toArray(new DataSupport[0]);
        String className = dataSupportArr[0].getClassName();
        List<Field> supportedFields = getSupportedFields(className);
        List<Field> supportedGenericFields = getSupportedGenericFields(className);
        Collection<AssociationsInfo> associationInfo = getAssociationInfo(className);
        for (DataSupport dataSupport : dataSupportArr) {
            if (!dataSupport.isSaved()) {
                analyzeAssociatedModels(dataSupport, associationInfo);
                doSaveAction(dataSupport, supportedFields, supportedGenericFields);
                analyzeAssociatedModels(dataSupport, associationInfo);
            } else {
                analyzeAssociatedModels(dataSupport, associationInfo);
                doUpdateAction(dataSupport, supportedFields, supportedGenericFields);
            }
            dataSupport.clearAssociatedData();
        }
    }

    private void doSaveAction(DataSupport dataSupport, List<Field> list, List<Field> list2) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.values.clear();
        beforeSave(dataSupport, list, this.values);
        afterSave(dataSupport, list, list2, saving(dataSupport, this.values));
    }

    private void beforeSave(DataSupport dataSupport, List<Field> list, ContentValues contentValues) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        putFieldsValue(dataSupport, list, contentValues);
        if (this.ignoreAssociations) {
            return;
        }
        putForeignKeyValue(contentValues, dataSupport);
    }

    private long saving(DataSupport dataSupport, ContentValues contentValues) {
        if (contentValues.size() == 0) {
            contentValues.putNull("id");
        }
        return this.mDatabase.insert(dataSupport.getTableName(), null, contentValues);
    }

    private void afterSave(DataSupport dataSupport, List<Field> list, List<Field> list2, long j) throws IllegalAccessException, InvocationTargetException {
        throwIfSaveFailed(j);
        assignIdValue(dataSupport, getIdField(list), j);
        updateGenericTables(dataSupport, list2, j);
        if (this.ignoreAssociations) {
            return;
        }
        updateAssociatedTableWithFK(dataSupport);
        insertIntermediateJoinTableValue(dataSupport, false);
    }

    private void doUpdateAction(DataSupport dataSupport, List<Field> list, List<Field> list2) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.values.clear();
        beforeUpdate(dataSupport, list, this.values);
        updating(dataSupport, this.values);
        afterUpdate(dataSupport, list2);
    }

    private void beforeUpdate(DataSupport dataSupport, List<Field> list, ContentValues contentValues) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        putFieldsValue(dataSupport, list, contentValues);
        if (this.ignoreAssociations) {
            return;
        }
        putForeignKeyValue(contentValues, dataSupport);
        Iterator<String> it = dataSupport.getListToClearSelfFK().iterator();
        while (it.hasNext()) {
            contentValues.putNull(it.next());
        }
    }

    private void updating(DataSupport dataSupport, ContentValues contentValues) {
        this.mDatabase.update(dataSupport.getTableName(), contentValues, "id = ?", new String[]{String.valueOf(dataSupport.getBaseObjId())});
    }

    private void afterUpdate(DataSupport dataSupport, List<Field> list) throws IllegalAccessException, InvocationTargetException {
        updateGenericTables(dataSupport, list, dataSupport.getBaseObjId());
        if (this.ignoreAssociations) {
            return;
        }
        updateAssociatedTableWithFK(dataSupport);
        insertIntermediateJoinTableValue(dataSupport, true);
        clearFKValueInAssociatedTable(dataSupport);
    }

    private Field getIdField(List<Field> list) {
        for (Field field : list) {
            if (isIdColumn(field.getName())) {
                return field;
            }
        }
        return null;
    }

    private void throwIfSaveFailed(long j) {
        if (j == -1) {
            throw new DataSupportException(DataSupportException.SAVE_FAILED);
        }
    }

    private void assignIdValue(DataSupport dataSupport, Field field, long j) {
        try {
            giveBaseObjIdValue(dataSupport, j);
            if (field != null) {
                giveModelIdValue(dataSupport, field.getName(), field.getType(), j);
            }
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage(), e);
        }
    }

    private void giveModelIdValue(DataSupport dataSupport, String str, Class<?> cls, long j) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Object objValueOf;
        if (shouldGiveModelIdValue(str, cls, j)) {
            if (cls == Integer.TYPE || cls == Integer.class) {
                objValueOf = Integer.valueOf((int) j);
            } else if (cls == Long.TYPE || cls == Long.class) {
                objValueOf = Long.valueOf(j);
            } else {
                throw new DataSupportException(DataSupportException.ID_TYPE_INVALID_EXCEPTION);
            }
            DynamicExecutor.setField(dataSupport, str, objValueOf, dataSupport.getClass());
        }
    }

    private void putForeignKeyValue(ContentValues contentValues, DataSupport dataSupport) {
        Map<String, Long> associatedModelsMapWithoutFK = dataSupport.getAssociatedModelsMapWithoutFK();
        for (String str : associatedModelsMapWithoutFK.keySet()) {
            contentValues.put(getForeignKeyColumnName(str), associatedModelsMapWithoutFK.get(str));
        }
    }

    private void updateAssociatedTableWithFK(DataSupport dataSupport) {
        Map<String, Set<Long>> associatedModelsMapWithFK = dataSupport.getAssociatedModelsMapWithFK();
        ContentValues contentValues = new ContentValues();
        for (String str : associatedModelsMapWithFK.keySet()) {
            contentValues.clear();
            contentValues.put(getForeignKeyColumnName(dataSupport.getTableName()), Long.valueOf(dataSupport.getBaseObjId()));
            Set<Long> set = associatedModelsMapWithFK.get(str);
            if (set != null && !set.isEmpty()) {
                this.mDatabase.update(str, contentValues, getWhereOfIdsWithOr(set), null);
            }
        }
    }

    private void clearFKValueInAssociatedTable(DataSupport dataSupport) {
        for (String str : dataSupport.getListToClearAssociatedFK()) {
            String foreignKeyColumnName = getForeignKeyColumnName(dataSupport.getTableName());
            ContentValues contentValues = new ContentValues();
            contentValues.putNull(foreignKeyColumnName);
            this.mDatabase.update(str, contentValues, foreignKeyColumnName + " = " + dataSupport.getBaseObjId(), null);
        }
    }

    private void insertIntermediateJoinTableValue(DataSupport dataSupport, boolean z) {
        Map<String, Set<Long>> associatedModelsMapForJoinTable = dataSupport.getAssociatedModelsMapForJoinTable();
        ContentValues contentValues = new ContentValues();
        for (String str : associatedModelsMapForJoinTable.keySet()) {
            String intermediateTableName = getIntermediateTableName(dataSupport, str);
            if (z) {
                this.mDatabase.delete(intermediateTableName, getWhereForJoinTableToDelete(dataSupport), new String[]{String.valueOf(dataSupport.getBaseObjId())});
            }
            Iterator<Long> it = associatedModelsMapForJoinTable.get(str).iterator();
            while (it.hasNext()) {
                long jLongValue = it.next().longValue();
                contentValues.clear();
                contentValues.put(getForeignKeyColumnName(dataSupport.getTableName()), Long.valueOf(dataSupport.getBaseObjId()));
                contentValues.put(getForeignKeyColumnName(str), Long.valueOf(jLongValue));
                this.mDatabase.insert(intermediateTableName, null, contentValues);
            }
        }
    }

    private String getWhereForJoinTableToDelete(DataSupport dataSupport) {
        return getForeignKeyColumnName(dataSupport.getTableName()) + " = ?";
    }

    private void updateGenericTables(DataSupport dataSupport, List<Field> list, long j) throws IllegalAccessException, InvocationTargetException {
        for (Field field : list) {
            Encrypt encrypt = (Encrypt) field.getAnnotation(Encrypt.class);
            String strAlgorithm = (encrypt == null || !"java.lang.String".equals(getGenericTypeName(field))) ? null : encrypt.algorithm();
            field.setAccessible(true);
            Collection collection = (Collection) field.get(dataSupport);
            if (collection != null) {
                String genericTableName = DBUtility.getGenericTableName(dataSupport.getClassName(), field.getName());
                String genericValueIdColumnName = DBUtility.getGenericValueIdColumnName(dataSupport.getClassName());
                this.mDatabase.delete(genericTableName, genericValueIdColumnName + " = ?", new String[]{String.valueOf(j)});
                for (Object obj : collection) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(genericValueIdColumnName, Long.valueOf(j));
                    DynamicExecutor.send(contentValues, "put", new Object[]{BaseUtility.changeCase(DBUtility.convertToValidColumnName(field.getName())), encryptValue(strAlgorithm, obj)}, contentValues.getClass(), new Class[]{String.class, getGenericTypeClass(field)});
                    this.mDatabase.insert(genericTableName, null, contentValues);
                }
            }
        }
    }
}
